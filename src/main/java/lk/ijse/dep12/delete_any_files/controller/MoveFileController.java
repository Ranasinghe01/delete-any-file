package lk.ijse.dep12.delete_any_files.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.*;

public class MoveFileController {

    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnBrowse1;

    @FXML
    private Button btnMove;

    @FXML
    private Button btnExit;

    @FXML
    private TextField txtCustomText;

    @FXML
    private TextField txtDestination;

    @FXML
    private TextField txtExtension;

    @FXML
    private TextField txtSource;


    public void initialize() {
        btnMove.setDisable(true);
    }

    @FXML
    void btnBrowseSourceOnAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File sourcePath = directoryChooser.showDialog(btnBrowse.getScene().getWindow());

        if (sourcePath == null) {
            txtSource.setText("No folder selected");
        } else {
            txtSource.setText(sourcePath.getAbsolutePath());
        }
    }

    @FXML
    void btnBrowseDestinationOnAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File destinationPath = directoryChooser.showDialog(btnBrowse1.getScene().getWindow());

        if (destinationPath == null) {
            txtDestination.setText("No folder selected");
            btnMove.setDisable(true);

        } else {
            txtDestination.setText(destinationPath.getAbsolutePath());
            btnMove.setDisable(false);
        }
    }

    @FXML
    void btnMoveOnAction(ActionEvent event) {
        File folder = new File(txtSource.getText());
        fileWalker(folder);
        new Alert(Alert.AlertType.INFORMATION, "Moved File Successfully");
    }

    private void fileWalker(File folder) {
        File[] folderContent = folder.listFiles();

        for (File file : folderContent) {
            if (file.isDirectory()) {
                fileWalker(file);

            } else {
                moveFile(file);
            }
        }
    }

    private void moveFile(File sourceFile) {

        String extension = txtExtension.getText();
        String customText = txtCustomText.getText();

        if (sourceFile.getName().endsWith(customText + "." + extension)) {

            String destinationPath = txtDestination.getText();

            try {
                File movedFile = new File(destinationPath, sourceFile.getName());
                if (!movedFile.exists()) movedFile.createNewFile();

                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
                     BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(movedFile))) {

                    byte[] byteBuffer = new byte[4096];
                    int read;
                    while ((read = bis.read(byteBuffer)) != -1) {
                        bos.write(byteBuffer, 0, read);
                    }
                }

                //check if the destination file is created successfully
                if (movedFile.exists() && sourceFile.length() == movedFile.length()) {
                    sourceFile.delete(); // delete the source file after moving

                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to move file: " + sourceFile.getName()).show();
                }

            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Exception: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnExitOnAction(ActionEvent event) {
        System.exit(0);
    }
}

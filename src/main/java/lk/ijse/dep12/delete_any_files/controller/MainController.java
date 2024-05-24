package lk.ijse.dep12.delete_any_files.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.Arrays;

public class MainController {

    public TextField txtCustomText;
    @FXML
    private Button btnBrowse;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnExit;

    @FXML
    private TextField txtExtension;

    @FXML
    private TextField txtPath;


    public void initialize() {
        btnDelete.setDisable(true);
    }

    @FXML
    void btnBrowseOnAction(ActionEvent event) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File path = directoryChooser.showDialog(btnBrowse.getScene().getWindow());

        if (path == null) {
            txtPath.setText("No Folder Selected");

        } else {
            txtPath.setText(path.getAbsolutePath());
            btnDelete.setDisable(false);
        }
    }

    private void fileWalker(File path) {

        try {
            File[] folderContent = path.listFiles();

            for (File file : folderContent) {
                if (file.isDirectory()) {
                    fileWalker(file);

                } else {
                    deleteFile(file);
                }
            }

        }catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Exception: " + e.getMessage()).show();
        }
    }

    private void deleteFile(File file) {
        String extension = txtExtension.getText();
        String custom_text = txtCustomText.getText();

        if (file.getName().endsWith(custom_text + "." + extension)) {
            file.delete();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        File path = new File(txtPath.getText());
        fileWalker(path);
        new Alert(Alert.AlertType.INFORMATION, "Successfully Deleted").show();
    }

    @FXML
    void btnExitOnAction(ActionEvent event) {
        System.exit(0);
    }
}
package com.qverkk.controller;

import com.qverkk.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Qverkk
 * @project DocumentConverter
 * @date 8/7/2019
 **/

public class MainController implements Initializable {

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private TextField pathField;

    @FXML
    private Button selectDocumentButton;

    @FXML
    private Button startButton;

    @FXML
    private TextArea resultArea;

    @FXML
    private Button saveButton;

    @FXML
    private Label resultFilePath;

    private String result = "";

    public void initialize(URL location, ResourceBundle resources) {
        pathField.setDisable(true);
        resultArea.setEditable(false);
        File folder = new File(System.getProperty("user.home") + File.separator + "tessdata");
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName().replace(".traineddata", "");
                languageComboBox.getItems().add(fileName);
            }
        }
        languageComboBox.getSelectionModel().selectFirst();

        selectDocumentButton.setOnAction(this::selectFileAction);
        startButton.setOnAction(a -> {
            execute();
        });
    }

    private String getJarPath() throws URISyntaxException {
        return new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();
    }

    private void selectFileAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("All files", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return;
        }
        pathField.setText(file.getPath());
    }

    private void execute() {
        resultArea.setText("Loading...");
        File imageFile = new File(pathField.getText());
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping

        instance.setDatapath(System.getProperty("user.home") + File.separator + "tessdata");
        instance.setLanguage(languageComboBox.getSelectionModel().getSelectedItem());

        try {
            result = instance.doOCR(imageFile);
            resultArea.setText(result);
            File resultFile = new File(imageFile.getPath() + "-translated.txt");
            if (!resultFile.exists()) {
                resultFile.createNewFile();
                FileUtils.writeStringToFile(resultFile, result, true);
            }
            resultFilePath.setText(resultFile.getPath());
        } catch (TesseractException e) {
            resultArea.setText("Error...");
            System.err.println(e.getMessage());
        } catch (IOException e) {
            resultArea.setText("Error...");
            e.printStackTrace();
        }
    }
}

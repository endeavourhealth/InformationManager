package org.endeavourhealth.informationmanager.converter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorController {
    @FXML
    private TextArea stackTrace;

    @FXML
    private Button closeButton;

    public static void ShowError(Stage parentStage, Exception error) {
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(ErrorController.class.getClassLoader().getResource("error.fxml"));
            Parent root = loader.load();

            ErrorController controller = loader.getController();
            controller.setText(error);

            Scene scene = new Scene(root);
            dialog.setTitle("Error");
            dialog.setScene(scene);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    public void setText(Exception error) {
        stackTrace.clear();
        stackTrace.appendText(error.getMessage() + "\n\n");
        List<String> stack = Arrays.stream(error.getStackTrace())
            .map(s -> s.toString())
            .collect(Collectors.toList());
        stackTrace.appendText(String.join("\n", stack));
    }

    @FXML
    private void closeButtonClick(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

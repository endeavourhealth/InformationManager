package org.endeavourhealth.informationmanager.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.endeavourhealth.informationmanager.common.transform.DOWLManager;

import java.io.IOException;

public class ECLController {
    @FXML
    TextArea eclTextField;
    @FXML
    TextArea owlTextField;

    private Stage _stage;

    public void setStage(Stage stage) {

        this._stage = stage;

    }

    public void eclConvertToDString(ActionEvent actionEvent) throws JsonProcessingException {

        String discoveryExpression= new DOWLManager()
                .convertEclToDiscoveryString(eclTextField.getText());

        owlTextField.setText(discoveryExpression);


    }


    public void reloadMain(ActionEvent actionEvent) throws IOException {
        _stage.close();
        Stage newStage = new Stage();
        newStage.setTitle("Discovery Converter");
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.setStage(newStage);
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.show();
    }

    public void eclConvertToOString(ActionEvent actionEvent) {


        String owlFS= new DOWLManager()
                .convertEclToOWLString(eclTextField.getText());

        owlTextField.setText(makePretty(owlFS,130));
    }
    private String makePretty(String outString,Integer width) {
        String result = "";
        char[] original = outString.toCharArray();
        boolean isIri = false;
        boolean isString = false;
        boolean isEsc = false;
        boolean isLocal = false;
        Integer tabs=0;
        String indent="";
        Integer pos = 0;
        Integer col=0;
        while (pos < original.length) {
            char c = original[pos];
            if (isString) {
                if (isEsc) {
                    result = result + c;
                    isEsc = false;
                } else if (c == '\\')
                    isEsc = true;
                else {
                    result = result + c;
                    if (c=='"')
                        isString=false;
                }
            }
            else {
                if (c == '"') {
                    result = result + c;
                    isString = true;
                }
                else {
                    if (isIri) {
                        if (isLocal) {
                            if (c != '>') {
                                result = result + c;
                            } else {
                                isLocal = false;
                                isIri = false;
                            }
                        } else {
                            if (c == '#') {
                                result = result + ":";
                                isLocal = true;
                            }
                        }
                    }
                    else
                    if (c == '<') {
                        isIri = true;
                    }
                    else {
                        result = result + c;
                        if (c == '(') {
                            tabs++;
                            indent = indent + "  ";
                            result = result + "\n" + indent;
                            col = indent.length();
                        } else if (c == ')') {
                            tabs = tabs - 1;
                            if (tabs == 0) {
                                indent = "";
                                col = 1;
                            } else {
                                indent = indent.substring(0, tabs * 2);
                                result = result + "\n" + indent;
                                col = indent.length();
                            }
                        } else {
                            if (c == ' ') {
                                if (col > width) {
                                    result = result + "\n" + indent;
                                    col = 1;
                                }
                            }
                        }
                    }

                }
            }
            pos++;
            col++;
        }
        return result;
    }


}

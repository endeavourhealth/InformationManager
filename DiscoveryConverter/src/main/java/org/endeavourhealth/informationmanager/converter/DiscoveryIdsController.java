package org.endeavourhealth.informationmanager.converter;

import com.google.common.base.Strings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.endeavourhealth.informationmanager.common.transform.OntologyIri;
import org.endeavourhealth.informationmanager.common.transform.OntologyModuleIri;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class DiscoveryIdsController {
    @FXML private ComboBox<String> cmbOntologyIri;
    @FXML private TextField txtDocumentId;
    private DiscoveryIdsResult result = null;

    public static DiscoveryIdsResult PromptDocDetails(Stage parentStage) {
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(DiscoveryIdsController.class.getClassLoader().getResource("discovery-ids.fxml"));
            Parent root = loader.load();

            DiscoveryIdsController controller = loader.getController();
            controller.initialize();

            Scene scene = new Scene(root);
            dialog.setTitle("Output document details");
            dialog.setScene(scene);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();
            return controller.result;
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

    private DiscoveryIdsResult getDiscoveryIds() {

        if (Strings.isNullOrEmpty(txtDocumentId.getText()))
            return null;

        return new DiscoveryIdsResult()
            .setOntologyIri(cmbOntologyIri.getValue())
            .setDocumentId(UUID.fromString(txtDocumentId.getText()));
    }

    void initialize() {
        cmbOntologyIri.getItems().addAll(Arrays.stream(OntologyIri.values()).map(OntologyIri::getValue).collect(Collectors.toSet()));
        cmbOntologyIri.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String t, boolean bln) {
                        super.updateItem(t, bln);
                        OntologyIri item = OntologyIri.byValue(t);
                        if(item != null){
                            setText(item.getName() + " : " + item.getValue());
                        }else{
                            setText(null);
                        }
                    }
                };
            }
        });

    }

    @FXML
    private void btnNewDocumentIdClick(ActionEvent event) {
        txtDocumentId.setText(UUID.randomUUID().toString());
    }

    @FXML
    private void btnOkClick(ActionEvent event) {
        result = getDiscoveryIds();
        if (result != null) {
            close();
        }
    }

    @FXML
    private void btnCancelClick(ActionEvent event) {
        result = null;
        close();
    }

    private void close() {
        Stage stage = (Stage) cmbOntologyIri.getScene().getWindow();
        stage.close();
    }
}

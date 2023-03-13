package com.example.projetplanning;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class DateEventController {
    //déclaration des champs FXML

    @FXML
    private Button annulerDateEvent;

    @FXML
    private Button validerDateEvent;


    @FXML
    private DatePicker DatePickerDateEventId;

    //Pour faire communiquer cette page avec la page : EventController.java
    private EventController eventController;

    public void setEventController(EventController e) {

        this.eventController = e;
    }


    /**
     * Vérifie si le datePicker est vide (DatePickerDateEventId) sinon ferme la page
     *
     * @param actionEvent
     */
    @FXML
    public void ValiderDateEvent(ActionEvent actionEvent) {
    //Si une date n'est pas choisit par le datePicker -> message d'erreur
        if (DatePickerDateEventId.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Erreur système");
            alert.setContentText("Vous n'avez pas de date !");
            Stage stage = new Stage();
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/Image/erreur.png"));

            alert.showAndWait();
            return;

        } else {
            //sinon, la date est bien prise en compte puis ferme la page
            eventController.setDateEventId(DatePickerDateEventId.getValue().toString());
            Stage stage = (Stage) validerDateEvent.getScene().getWindow();
            stage.close();
        }
    }


    /**
     * Permet de femer la page (DateEvent.fxml)
     *
     * @param actionEvent
     */
    @FXML
    public void AnnulerDateEvent(ActionEvent actionEvent) {

        Stage stage = (Stage) annulerDateEvent.getScene().getWindow();
        stage.close();

    }
}

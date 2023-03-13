package com.example.projetplanning;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DateController {
    //déclaration des champs FXML

    @FXML
    private Label mylabel;

    @FXML
    private DatePicker myDatePicker;

    @FXML
    private javafx.scene.control.Button validerDate;


    /**
     * Permet de récuperer la valeur du datePicker sous le format dans la méthode
      * @param actionEvent
     */
    public void getDate(ActionEvent actionEvent) {
        //recupération de la date du datePicker
        LocalDate myDate = myDatePicker.getValue();
        String myFormattedDate = myDate.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy"));
        //le label prend la valeur récup
        mylabel.setText(myFormattedDate);
    }


    /**
     * Permet de fermer la page (Date.fxml)
     * @param actionEvent
     */
    public void changeDate(ActionEvent actionEvent) {
        Stage stage = (Stage) validerDate.getScene().getWindow();
        stage.close();

    }
}
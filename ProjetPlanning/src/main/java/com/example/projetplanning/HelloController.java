package com.example.projetplanning;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    private DatePicker myDatePicker;
    @FXML
    private Label myLabel;

    public void getDate(ActionEvent actionEvent) {
        LocalDate myDate = myDatePicker.getValue();
        String myFormattedDate = myDate.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy"));
        myLabel.setText(myFormattedDate);
    }
}

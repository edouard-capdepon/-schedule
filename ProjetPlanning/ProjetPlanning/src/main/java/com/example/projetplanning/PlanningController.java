package com.example.projetplanning;

import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PlanningController {
    //déclaration des champs FXML

    @FXML
    private Label day1;

    @FXML
    private Label day2;

    @FXML
    private Label day3;

    @FXML
    private Label day4;

    @FXML
    private Label day5;

    @FXML
    private Label day6;

    @FXML
    private Label day7;


    @FXML
    private Label myDateJour;

    @FXML
    public DatePicker myDatePicker;

    @FXML
    private Label mylabel;



    @FXML
    //@Deprecated veut dire que je suis "conscient" que ca peut faire n'importe quoi mais j'accepte les risques
    @Deprecated
    void initialize() {
        //le label recup la date du jour avec la méthode de la page HelloPlanning
        myDateJour.setText(HelloPlanning.getDateDuJour());

        int month = Calendar.getInstance().getTime().getMonth();
        int year = Calendar.getInstance().getTime().getYear();

        Date date1 = new Date();
        date1.setYear(year);
        date1.setMonth(month);
        date1.setDate(1);

        ArrayList<String> list = getOrderOfWeekDay(date1);

        setNameOfFirstDayOfMonth(list);

    }

    @Deprecated
    private ArrayList<String> getOrderOfWeekDay(Date firstDay) {
        ArrayList<String> list = new ArrayList<String>();

        //Cas jour 1

        list.add(getDayOfWeek(firstDay));

        //Cas general


        for (int i = 2; i < 8; i++) {
            Date date = firstDay;
            date.setMonth(firstDay.getMonth());
            date.setYear(firstDay.getYear());
            date.setDate(i);
            list.add(getDayOfWeek(date));

        }

        return list;

    }

    /**
     * Permet d'afficher les jours correspondant au début du mois sélectionné
     * @param listDays
     */
    private void setNameOfFirstDayOfMonth(ArrayList<String> listDays) {
        //permet d'associer les jours correspondant aux case du planning
        String a, b, c, d, e, f, g;
        a = " 01";
        b = " 02";
        c = " 03";
        d = " 04";
        e = " 05";
        f = " 06";
        g = " 07";
        day1.setText(" " + listDays.get(6) + a);
        day2.setText(" " + listDays.get(0) + b);
        day3.setText(" " + listDays.get(1) + c);
        day4.setText(" " + listDays.get(2) + d);
        day5.setText(" " + listDays.get(3) + e);
        day6.setText(" " + listDays.get(4) + f);
        day7.setText(" " + listDays.get(5) + g);

    }

    /**
     *On trouve le jour de date1
      * @param date1
     * @return les jours dans l'ordre
     */
    private String getDayOfWeek(Date date1) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.FRENCH);
        Date myDate = date1;
        sdf.applyPattern("EEEEEEEEEEEEE");
        String sMyDate = sdf.format(myDate);
        return sMyDate;

    }

    /**
     * Si le datePicker est vide --> erreur , sinon on recupere la valeur du datePicker qu'on ajoute dans le ArrayList
     * @param actionEvent
     */
    @Deprecated
    public void getDate(ActionEvent actionEvent) {
        if (myDatePicker.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Erreur système");
            alert.setContentText("Vous n'avez pas selectionné de date !");
            Stage stage = new Stage();
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/Image/erreur.png"));
            alert.showAndWait();
            return;

        } else {
            LocalDate myDate = myDatePicker.getValue();
            String myFormattedDate = myDate.format(DateTimeFormatter.ofPattern("MMMM-yyyy"));
            mylabel.setText(myFormattedDate);
        }

        int month = myDatePicker.getValue().getMonth().getValue();
        int year = myDatePicker.getValue().getYear();
        Date date1 = new Date();

        date1.setYear(year);
        date1.setMonth(month - 1);
        date1.setDate(1);


        ArrayList<String> list = getOrderOfWeekDay(date1);
        setNameOfFirstDayOfMonth(list);

    }

    /**
     * Permet d'aller sur la page de creation d'event(Event.fxml)
     *
     * @param actionEvent
     */
    public void geteveent(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Event.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Création de tâches");
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            Image image = new Image("file:src/Image/ico-planning.png");
            stage.getIcons().add(image);
            root1.getStylesheets().add(getClass().getResource("styleEvent.css").toExternalForm());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Si il n'y a pas de date choisi --> erreur, sinon on ouvre la page avec les tâches de la journée choisi( PlanningOfDay.fxml)
     *
     * @param actionEvent
     */
    public void getPlanningJour(ActionEvent actionEvent) {
        if (myDatePicker.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Erreur système");
            alert.setContentText("Selectionnez un mois pour le planning!");
            Stage stage = new Stage();
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/Image/erreur.png"));

            alert.showAndWait();
            return;

        } else
        //if pas de tache pour ma date : exception
        {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PlanningOfDay.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                PlanningOfDayController planningOfDayController = fxmlLoader.getController();
                planningOfDayController.planningController = this;
                Stage stage = new Stage();
                Image image = new Image("file:src/Image/ico-planning.png");
                stage.getIcons().add(image);
                Object o = actionEvent.getSource();
                if (o instanceof Button) {

                    Button button = (Button) o;
                    planningOfDayController.clickedButton = button.getId();
                    planningOfDayController.refresh();
                }

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.DECORATED);
                stage.setTitle("Planning de la journée");
                stage.setScene(new Scene(root1));
                stage.setResizable(false);
                root1.getStylesheets().add(getClass().getResource("stylePlanningOfDay.css").toExternalForm());
                stage.show();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

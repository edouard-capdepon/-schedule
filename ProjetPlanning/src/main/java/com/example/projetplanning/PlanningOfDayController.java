package com.example.projetplanning;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

//Importation de toute la classe HelloPlanning avec les variables de connection
import static com.example.projetplanning.HelloPlanning.*;

public class PlanningOfDayController {

    //déclaration des champs FXML

    @FXML
    private Button annulerPlanningOfDayId;

    @FXML
    private VBox VBoxId;

    public PlanningController planningController;

    public String clickedButton;

    /**
     * Permet de fermer la page(PlanningOfDay.fxml)
     * @param actionEvent
     */
    @FXML
    public void AnnulerPlanningOfDay(ActionEvent actionEvent) {

        Stage stage = (Stage) annulerPlanningOfDayId.getScene().getWindow();
        stage.close();

    }


    /**
     * Permet de récuperer dans un String la date du boutton cliqué
      * @throws SQLException
     */
    public void refresh() throws SQLException {
        //récup dans un String la date du boutton cliqué
        String dateOfDay = planningController.myDatePicker.getValue().toString();
        String DD = String.valueOf(clickedButton.charAt(1)) + String.valueOf(clickedButton.charAt(2));
        String s = dateOfDay.substring(0, 8);
        String clickedDay = s + DD;
        initializelabel(clickedDay);
    }


    /**
     * Permet de récuperer de la BDD le nom de tâche, ses horaires et sa description puis de les afficher
     * @param date
     * @throws SQLException
     */
    private void initializelabel(String date) throws SQLException {

        // requete pour recuperer les infos des taches que je veux afficher et les tries dans l'ordre de priorité
        String s = "select tache.nomTache, tache.heureDebut, tache.heureFin, tache.description , alerte.priorite from tache, alerte where alerte.idtache = tache.idtache   AND tache.idutilisateur = ? and tache.date = ? ORDER BY ordrePriorite ";

        PreparedStatement ps = co.prepareStatement(s);
        ps.setInt(1, userid);
        ps.setString(2, date);
        ResultSet rs = ps.executeQuery();

        co.commit();

        ArrayList<String> nomsTaches = new ArrayList<>();
        ArrayList<String> heuresDebut = new ArrayList<>();
        ArrayList<String> heuresFin = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();
        ArrayList<String> priorite = new ArrayList<>();

        while (rs.next()) {

            nomsTaches.add(rs.getString(1));
            heuresDebut.add(rs.getString(2));
            heuresFin.add(rs.getString(3));
            descriptions.add(rs.getString(4));
            priorite.add(rs.getString(5));

        }
        // si il n'y a pas de tache à afficher je lui dit et je rajoute une image
        if(nomsTaches.size() == 0){
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.BASELINE_CENTER);
            Image image = new Image("File:src/Image/chat.gif");
            ImageView imageView = new ImageView(image);
            Label vide = new Label("Aucune tache à la date :  " + date);
            vBox.getChildren().addAll(new Label(), new Label(),new Label(),new Label(),new Label(),new Label(),vide,new Label(), imageView);
            VBoxId.getChildren().add(vBox);

        }else

        for (int i = 0; i < nomsTaches.size(); i++) {

            //affichage des infos de la BDD dans le Vbox
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.BASELINE_CENTER);
            VBox.setVgrow(vbox, Priority.ALWAYS);
            vbox.setId("tachesId");
            HBox hbox = new HBox(new Label("de " + heuresDebut.get(i) + " h à "),            new Label(heuresFin.get(i) + " h \n"));
            TextArea textArea = new TextArea("Description :  " + descriptions.get(i));
            textArea.setWrapText(true);
            textArea.setEditable(false);
            textArea.setMinWidth(100);
            ScrollPane scrollPane = new ScrollPane(textArea);
            scrollPane.setMinViewportHeight(150);
            hbox.setAlignment(Pos.BASELINE_CENTER);
            Separator separator1 = new Separator();
            Label label1 = new Label(priorite.get(i));
            label1.setId("coucou");

            vbox.getChildren().addAll(new Label(nomsTaches.get(i) + " (tâche " + label1.getText() + ")" ), hbox, new Label(), scrollPane, new Label(), new Label(), new Label(), separator1);
            VBoxId.getChildren().add(vbox);


        }


    }


}

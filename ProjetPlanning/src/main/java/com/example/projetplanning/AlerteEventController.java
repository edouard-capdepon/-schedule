package com.example.projetplanning;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//Importation de toute la classe HelloPlanning avec les variables de connection
import static com.example.projetplanning.HelloPlanning.*;

public class AlerteEventController {
    //déclaration des champs FXML

    @FXML
    private javafx.scene.control.Button annulerAlerteid;

    @FXML
    private ChoiceBox alertChoiceId;

    @FXML
    private ChoiceBox priorityId;

    @FXML
    private Button validerAlertId;

    /**
     * Création de 2 ArrayList
     */
    private int eventid = 0;
    private static ArrayList<Integer> ids = new ArrayList<Integer>();
    private static ArrayList<String> names = new ArrayList<String>();

    /**
     * Initialisation avec les méthodes : initializePriotityChoiceBox et initializeAlertChoiceBox
     */
    @FXML
    void initialize() {

        ids = new ArrayList<Integer>();
        names = new ArrayList<String>();
        initializePriotityChoiceBox();
        try {
            initializeAlertChoiceBox();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Permet de fermer la page (AlerteEvent.fxml)
     *
     * @param actionEvent
     */
    public void annulerlAlerte(ActionEvent actionEvent) {
        Stage stage = (Stage) annulerAlerteid.getScene().getWindow();
        stage.close();

    }

    /**
     * Donne les valeurs de base pour la choiceBox (priorityId)
     */
    private void initializePriotityChoiceBox() {
    //Valeurs pour mon ChoiceBox
        priorityId.getItems().addAll("très importante", "importante", "normale", "à faire", "ne pas oublier");
        priorityId.setValue(priorityId.getItems().get(2));
    }


    /**
     * La choiceBox (alertChoiceId) récupère les valeurs des noms de tache de la BDD
     *
     * @throws SQLException
     */
    private void initializeAlertChoiceBox() throws SQLException {

        //affichage des taches qui n'ont pas d'alerte associées dans un second ChoiceBox
        String s = "select idtache, nomTache from tache t1 where idutilisateur = ? and not exists (select idtache from alerte where t1.idtache = alerte.idtache);";
        PreparedStatement ps = co.prepareStatement(s);

        ps.setInt(1, userid);
        ResultSet rs = ps.executeQuery();
        co.commit();

        while (rs.next()) {
            ids.add(rs.getInt(1));
            names.add(rs.getString(2));
        }

        for (String name : names
        ) {

            alertChoiceId.getItems().add(name);

        }


    }

    /**
     * me permet de récupérer l'index de la priorité pour ensuite trier mes taches par rapport à leur alerte
     * @param priorites je récupère le String du premier ChoiceBox (initializePriotityChoiceBox())
     * @return
     */
    public int IntPriorite(String priorites) {

        switch (priorites) {

            case "très importante":
                return 0;

            case "importante":
                return 1;

            case "normale":
                return 2;

            case "à faire":
                return 3;

            case "ne pas oublier":
                return 4;

        }
        return 5    ;
    }

    /**
     * Vérification que la choiceBox (alertChoiceId) a bien une valeur, puis ajoute dans la BDD une alerte relié à sa tache, puis ferme la page avec un message de confiramtion
     *
     * @param event
     */
    @FXML
    void validerAlerteEvent(ActionEvent event) {

        //verification que le champs n'est pas null, sinon exception
        if (alertChoiceId.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Erreur système");
            alert.setContentText("Vous n'avez pas selectionné de tache !");
            Stage stage = new Stage();
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/Image/erreur.png"));

            alert.showAndWait();
            return;

            //Si il n'est pas null on crée la requete et on l'execute
        } else {
            int i = alertChoiceId.getSelectionModel().getSelectedIndex();
            int id = ids.get(i);


            String r1 = "select idalerte from alerte where idtache = ?";
            try {

                PreparedStatement ps = co.prepareStatement(r1);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                co.commit();
                if (rs.next() == false) {

                    //requete pour ajouter dans la BDD
                    String r = "insert into alerte ( idtache, priorite, ordrePriorite, idutilisateur) values (?,?,?,?)";

                    PreparedStatement ps1 = null;

                    ps1 = co.prepareStatement(r);
                    ps1.setInt(1, id);
                    ps1.setString(2, priorityId.getValue().toString());
                    ps1.setInt(3, IntPriorite(priorityId.getValue().toString()));
                    ps1.setInt(4, userid);
                    ps1.execute();
                    co.commit();

                    //Le message de confiramtion de l'ajout dans la base de donnée
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Ajout d'une alerte réussi ");
                    alert.setHeaderText("Results:");
                    alert.setContentText("La tache \"" + alertChoiceId.getValue().toString() + "\"  a bien une nouvelle alerte associé \n"
                            + "L'alerte a une priorité de type : \"" + priorityId.getValue().toString() + "\" .");
                    Stage stage1 = new Stage();
                    stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage1.getIcons().add(new Image("file:src/Image/valider.png"));

                    alert.showAndWait();
                    Stage stage = (Stage) validerAlertId.getScene().getWindow();
                    stage.close();

                    //Sinon affichage d'une erreur
                } else {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Erreur système");
                    alert.setContentText("La tache selectionnée a déjà une alerte !");
                    Stage stage = new Stage();
                    stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("file:src/Image/erreur.png"));

                    alert.showAndWait();
                    return;

                }

            } catch (SQLException e) {


                e.printStackTrace();
                return;
            }
            return;
        }
    }
}
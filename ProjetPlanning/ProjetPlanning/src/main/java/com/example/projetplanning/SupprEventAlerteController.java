package com.example.projetplanning;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//je récupere les valeure de connection
import static com.example.projetplanning.HelloPlanning.co;
import static com.example.projetplanning.HelloPlanning.userid;

public class SupprEventAlerteController {

    @FXML
    private Button ValiderSupprEventAlerteId;

    @FXML
    private Button AnnulerSupprEventAlerteId;

    @FXML
    private ChoiceBox ChoiceBoxSupprEventId;

    /**
     * Création de 2 ArrayList
     */
    private int eventid = 0;
    private static ArrayList<Integer> ids = new ArrayList<Integer>();
    private static ArrayList<String> names = new ArrayList<String>();

    /**
     * Initialisation avec les méthodes : initializeAlertChoiceBox
     */
    @FXML
    void initialize() {

        ids = new ArrayList<Integer>();
        names = new ArrayList<String>();
        try {
            initializeAlertChoiceBox();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * La choiceBox (ChoiceBoxSupprEventId) récupère les valeurs des noms de tache de la BDD
     *
     * @throws SQLException
     */
    private void initializeAlertChoiceBox() throws SQLException {

        String s = "select idtache, nomTache from tache where idutilisateur = ?";

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

            ChoiceBoxSupprEventId.getItems().add(name);

        }
    }


    /**
     * Vérification que le choix n'est pas vide, puis fait une requete pour supprimer la tache et son alerte si elle en a une, affiche ensuite une confiration et ferme la page
     *
     * @param actionEvent
     */
    public void ValiderSupprEventAlerte(ActionEvent actionEvent) {


        if (ChoiceBoxSupprEventId.getValue() == null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Erreur système");
            alert.setContentText("Vous n'avez pas selectionné de tache !");
            Stage stage = new Stage();
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/Image/erreur.png"));

            alert.showAndWait();
            return;

        } else {

            int i = ChoiceBoxSupprEventId.getSelectionModel().getSelectedIndex();
            int id = ids.get(i);



            //Création des requetes sql pour supprimer la tache et son alerte
            String r1 = "delete from alerte where idtache = ?  ";
            String r2 = "delete from tache where idtache = ?";
            try {

                PreparedStatement ps = co.prepareStatement(r1);
                PreparedStatement ps1 = co.prepareStatement(r2);
                ps.setInt(1, id);
                ps.executeUpdate();
                ps1.setInt(1, id);
                ps1.executeUpdate();

                co.commit();

                //Affichage de la confirmation de supression

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Suppression reussi ");
                alert.setHeaderText("Results:");
                alert.setContentText("La tache \"" + ChoiceBoxSupprEventId.getValue().toString() + "\"  a bien été supprimée \n" +
                        "ainsi que son alerte si elle en avait une associée");
                Stage stage1 = new Stage();
                stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
                stage1.getIcons().add(new Image("file:src/Image/valider.png"));

                alert.showAndWait();
                Stage stage = (Stage) ValiderSupprEventAlerteId.getScene().getWindow();
                stage.close();


            } catch (SQLException e) {


                e.printStackTrace();
                return;
            }
            return;
        }
    }

    /**
     * Permet de fermer la page (SupprEventAlerte.fxml)
     *
     * @param actionEvent
     */
    public void AnnulerSupprEventAlerte(ActionEvent actionEvent) {

        Stage stage = (Stage) AnnulerSupprEventAlerteId.getScene().getWindow();
        stage.close();

    }
}

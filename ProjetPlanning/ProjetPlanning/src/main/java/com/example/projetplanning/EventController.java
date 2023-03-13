package com.example.projetplanning;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

//importation de toute la classe HelloPlanning
import static com.example.projetplanning.HelloPlanning.*;


public class EventController {


    @FXML
    private TextField dateEventid;

    @FXML
    private TextArea descriptionEventid;

    @FXML
    private TextField heureDebutEvent;

    @FXML
    private TextField heureFinEvent;

    @FXML
    private TextField TitreEventId;

    @FXML
    private Label caracteresRastantId;


    @FXML
    private javafx.scene.control.Button annulerEventid;

    private DateEventController dateEventController;



//initialize pour le compteur de caractères de la description
    @FXML
    public void initialize() {
        caracteresRastantId.setText("1000");

        final int MAX_CHARS = 1000;

        descriptionEventid.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() - 1 < MAX_CHARS ? change : null));

        descriptionEventid.setWrapText(true);

    }


    @FXML

    void onKeyPressed(KeyEvent event) {
        caracteresRastantId.setText(String.valueOf(1000 - descriptionEventid.getLength() - 1));
    }

    /**
     * Permet de remplir le TextField avec la valeur du date picker (DateEventController.java)
     *
     * @param s un string qui contient la date récuperer dans le DateEventController.java
     */
    public void setDateEventId(String s) {
        //Recup la valeur du date picker selectionné dans le DateEventController.java
        this.dateEventid.setText(s);

    }

    /**
     * Vérification de tout les champs, si un champ obligatoire est vide ou mal rempli -> erreur, puis ajoute dans la BDD
     *
     * @param event Valider pour ajouter dans la BDD
     */
    @FXML
    //méthode pour mettre un event dans la BDD(tache)
    void AddInBDD(ActionEvent event) {

        //Vérifie que le nom de tache n'est pas vide
        if (TitreEventId.getText().equals("")) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Erreur système");
            alert.setContentText("Vous n'avez pas selectionné de nom de tache !");
            Stage stage = new Stage();
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/Image/erreur.png"));
            alert.showAndWait();
            return;

            //Vérifie que la date de la tache n'est pas vide
        } else if (dateEventid.getText().equals("")) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Erreur système");
            alert.setContentText("Vous n'avez pas selectionné de date pour la tache !");
            Stage stage = new Stage();
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/Image/erreur.png"));
            alert.showAndWait();
            return;

            //Vérifie que l'heure de début respecte le bon format
        } else if (isTimeValid(heureDebutEvent.getText()) != true) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Erreur système");
            alert.setContentText("Le format de l'horaire n'est pas réspecter, \n" + " Le format à suivre est le suivant : \"HH:MM:SS\"");
            Stage stage = new Stage();
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/Image/erreur.png"));
            alert.showAndWait();
            return;

            //Vérifie que l'heure de fin respecte le bon format
        }else if (isTimeValid(heureFinEvent.getText()) != true) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Erreur système");
            alert.setContentText("Le format de l'horaire n'est pas réspecter, \n" + " Le format à suivre est le suivant : \"HH:MM:SS\"");
            Stage stage = new Stage();
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/Image/erreur.png"));
            alert.showAndWait();
            return;

            //Vérifie que l'heure de début est bien rempli
        } else if (heureDebutEvent.getText().equals("")) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Erreur système");
            alert.setContentText("Vous n'avez pas selectionné d'heure de début !");
            Stage stage = new Stage();
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/Image/erreur.png"));
            alert.showAndWait();
            return;

            //Vérifie que l'heure de fin de la tache est bien rempli
        } else if (heureFinEvent.getText().equals("")) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Erreur système");
            alert.setContentText("Vous n'avez pas selectionné d'heure de fin pour la tache !");
            Stage stage = new Stage();
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/Image/erreur.png"));
            alert.showAndWait();
            return;

            //appel la méthode pour vérifier que l'heure de début est plus petite que l'heure de fin
        } else if (!checkIfFirstIsGreater(heureDebutEvent.getText(), heureFinEvent.getText())) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Erreur système");
            alert.setContentText("L'horaire entrée n'est pas correct !");
            Stage stage = new Stage();
            stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("file:src/Image/erreur.png"));
            alert.showAndWait();
            return;

        }else
        {
            try {
                //récupère les infos entrée pour les envoyer dans la base de données, puis ferme la fenetre
                String query = " insert into tache (nomTache, heureDebut, heureFin,date, description, idutilisateur) values (?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStmt = co.prepareStatement(query);
                preparedStmt.setString(1, TitreEventId.getText());
                preparedStmt.setTime(2, Time.valueOf(heureDebutEvent.getText()));
                preparedStmt.setTime(3, Time.valueOf(heureFinEvent.getText()));
                preparedStmt.setDate(4, Date.valueOf(dateEventid.getText()));
                preparedStmt.setString(5, descriptionEventid.getText());
                preparedStmt.setInt(6, HelloPlanning.userid);

                // execute le preparedstatement
                preparedStmt.executeUpdate();
                co.commit();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Création reussi ");
                alert.setHeaderText("Results : ");
                alert.setContentText("La tache \"" + TitreEventId.getText() + "\"  a bien été ajouté \n" +
                        "AJOUTEZ UNE ALERTE POUR QUE LA TÂCHE SOIT DANS LE PLANNING");
                Stage stage1 = new Stage();
                stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
                stage1.getIcons().add(new Image("file:src/Image/valider.png"));

                alert.showAndWait();
                Stage stage = (Stage) dateEventid.getScene().getWindow();
                stage.close();


            } catch (SQLException e) {
                System.out.println("Got an exception!");
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Permet d'aller sur la page de creation d'alerte(AlerteEvent.fxml)
     *
     * @param event
     */
    @FXML
    //ouvre la page alerte quand on clique sur le bouton
    void getAlerte(ActionEvent event) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AlerteEvent.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Alerte pour tâches");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            Image image = new Image("file:src/Image/ico-planning.png");
            stage.getIcons().add(image);
            root1.getStylesheets().add(getClass().getResource("styleAlerteEvent.css").toExternalForm());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Permet de fermer la page (Event.fxml)
     *
     * @param actionEvent
     */
    //ferme la page event
    public void annulerEvent(ActionEvent actionEvent) {

        Stage stage = (Stage) annulerEventid.getScene().getWindow();
        stage.close();

    }

    /**
     * Permet d'aller sur la page de choix de la date(DateEvent.fxml)
     *
     * @param actionEvent
     */
    public void GoDateEvent(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DateEvent.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            this.dateEventController = fxmlLoader.getController();
            dateEventController.setEventController(this);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Alerte pour les tâches");

            stage.setScene(new Scene(root1));
            Image image = new Image("file:src/Image/ico-planning.png");
            stage.getIcons().add(image);
            root1.getStylesheets().add(getClass().getResource("styleDateEvent.css").toExternalForm());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour vérifier si l'heure de début est plus petite que l'heure de fin
     *
     * @param debut qui correspond à l'heure de début entrée
     * @param fin   qui correspond à l'heure de fin entrée
     * @return
     */
    public static boolean checkIfFirstIsGreater(String debut, String fin) {

        //On compare d'abord les heures
        String heured = String.valueOf(debut.charAt(0) + "" + debut.charAt(1));
        String heuref = String.valueOf(fin.charAt(0) + "" + fin.charAt(1));
        int heuredebut = Integer.parseInt(heured);
        int heurefin = Integer.parseInt(heuref);

        //Puis les minutes
        String mind = String.valueOf(debut.charAt(3) + "" + debut.charAt(4));
        String minf = String.valueOf(fin.charAt(3) + "" + fin.charAt(4));
        int minutedebut = Integer.parseInt(mind);
        int minutefin = Integer.parseInt(minf);

        //et les secondes à la fin
        String secd = String.valueOf(debut.charAt(6) + "" + debut.charAt(7));
        String secf = String.valueOf(fin.charAt(6) + "" + fin.charAt(7));
        int secondedebut = Integer.parseInt(secd);
        int secondefin = Integer.parseInt(secf);

        if (heuredebut <= heurefin) {
            if (heuredebut < heurefin) {
                return true;
            }
            if (minutedebut <= minutefin) {
                if (minutedebut < minutefin) {
                    return true;
                }
                if (secondedebut <= secondefin) {
                    if (secondedebut < secondefin) {
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Permet d'aller sur la page pour supprimer une tache et son alerte associée(SupprEventAlerte.fxml)
     *
     * @param actionEvent
     */
    public void GoSupprEventAlerte(ActionEvent actionEvent) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SupprEventAlerte.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Supprimer une alerte ou une tâche");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            Image image = new Image("file:src/Image/ico-planning.png");
            stage.getIcons().add(image);
            root1.getStylesheets().add(getClass().getResource("styleSupprEventAlerte.css").toExternalForm());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Verifie que le format de l'horaire est valide
     * @param value
     * @return
     */
    public static boolean isTimeValid(String value) {
        try {
            String[] time = value.split(":");
            return  Integer.parseInt(time[0]) < 24 && Integer.parseInt(time[1]) < 60 && Integer.parseInt(time[2]) < 60;
        } catch (Exception e) {
            return false;
        }
    }


}



package com.example.projetplanning;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;

public class HelloPlanning extends Application {

    /**
     * Affiche la page appelé
     *
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        //permet d'ouvir la page Planning
        FXMLLoader fxmlLoader = new FXMLLoader(HelloPlanning.class.getResource("Planning.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("stylePlanning.css").toExternalForm());
        Image image = new Image("file:src/Image/ico-planning.png");
        stage.getIcons().add(image);
        stage.setTitle("Planning");
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }

    //Connection que l'on conserve
    /**
     * declaration pour la connection
     */
    //declaration de ma connexion que je conserve sur mes autres page
    static String uri = "jdbc:mysql://localhost:3306/projet";
    static Connection co;
    static int userid = 1;

    /**
     * Méthode pour la connection à la BDD
     */
    public static void connect() {
        //connexion à la base de donnée
        try {
            Connection con = DriverManager.getConnection(uri, "root", "root");

            if (con != null) {

                System.out.println("Connected !");
                con.setAutoCommit(false);
                co = con;


            } else {
                System.out.println("Connection Failed !");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    /**
     * Se connecte d'abord à la BDD, puis lance la page
     *
     * @param args
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException {
        connect();
        launch();
    }

    /**
     * Méthode pour recupérer la date de l'ordinateur actuel
     *
     * @return l'année, le mois et le jour de l'ordinateur
     */
    public static String getDateDuJour() {
        //récup la date du jour
        String mois[] = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet",
                "Aout", "Septembre", "Octobre", "Novembre", "Décembre"};
        String jour[] = {"Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};

        int jour1;
        int mois1;
        int annee;

        Calendar cal = Calendar.getInstance();

        jour1 = cal.get(Calendar.DAY_OF_MONTH);
        int jourW = cal.get(Calendar.DAY_OF_WEEK);
        mois1 = cal.get(Calendar.MONTH);
        annee = cal.get(Calendar.YEAR);

        String d = jour[jourW - 1] + " " + jour1 + " " + mois[mois1] + " " + annee;
        return d;
    }
}
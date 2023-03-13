package com.example.projetplanning;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class HelloApplication extends Application {
    //ouvrir la page fxml appelé
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Planning.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Date");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws ClassNotFoundException {




        String uri = "jdbc:mysql://localhost:3306/Projet";

        try (Connection co = DriverManager.getConnection(uri, "root", "root")) {


            if (co != null) {

                System.out.println("connecté !");
                Statement stmt = co.createStatement();
//lancer la page si on est bien connecté a la BDD
                launch();

            }




        } catch (SQLException e) {
            e.printStackTrace();
        }


         }
//recuperer la date et l'heure du jour

         public static String getDate(){
             LocalDateTime myDateObj = LocalDateTime.now();
             System.out.println("Before formatting: " + myDateObj);
             DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

             String d = myDateObj.format(myFormatObj);
             d = myDateObj.format(myFormatObj);
             return d;

         }


}
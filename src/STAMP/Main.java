package STAMP;


import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Button btnShowDialog = new Button("Show Popup");
        // Set the action to call the showPopup() method when clicked
        btnShowDialog.setOnAction(e -> showPopup());

        pane.getChildren().add(btnShowDialog);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void showPopup() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact?");
        alert.setHeaderText("Are you sure you want to delete this contact?");

        // Set the available buttons for the alert
        ButtonType btnYes = new ButtonType("Yes");
        ButtonType btnNo = new ButtonType("No");

        alert.getButtonTypes().setAll(btnYes, btnNo);

        // This allows you to get the response back from the user
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == btnYes) {
                System.out.println("User clicked Yes!");
            } else if (result.get() == btnNo) {
                System.out.println("User clicked No!");
            }
        }

    }
}
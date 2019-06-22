package STAMP;


//Java Program to create a text input dialog 
//and add a label to display the text entered 
import javafx.application.Application; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.scene.layout.*; 
import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 
import javafx.scene.control.*; 
import javafx.stage.Stage; 
import javafx.scene.control.Alert.AlertType; 
import java.time.LocalDate; 
public class TextInputDialog_2 extends Application { 

 // launch the application 
 public void start(Stage s) 
 { 
     // set title for the stage 
     s.setTitle("creating textInput dialog"); 

     // create a tile pane 
     TilePane r = new TilePane(); 

     // create a label to show the input in text dialog 
     Label l = new Label("no text input"); 

     // create a text input dialog 
     TextInputDialog td = new TextInputDialog(); 

     // create a button 
     Button d = new Button("click"); 

     // create a event handler 
     EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
         public void handle(ActionEvent e) 
         { 
             // show the text input dialog 
             td.showAndWait(); 

             // set the text of the label 
             l.setText(td.getEditor().getText()); 
         } 
     }; 

     // set on action of event 
     d.setOnAction(event); 

     // add button and label 
     r.getChildren().add(d); 
     r.getChildren().add(l); 

     // create a scene 
     Scene sc = new Scene(r, 500, 300); 

     // set the scene 
     s.setScene(sc); 

     s.show(); 
 } 

 public static void main(String args[]) 
 { 
     // launch the application 
     launch(args); 
 } 
} 

import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//Define the player's choices as a type
enum playerChoice{
    Rock,
    Paper,
    Scissors,
    Spock,
    Lizard;
}


public class gameClient extends Application {
    int port;
    String IPAddress;
    Game currGame;

    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage) throws Exception{
        Stage myStage = primaryStage;
        Scene scene;
        HashMap<String, Scene> sceneMap = new HashMap<String, Scene>();
        primaryStage.setTitle("Rock, Paper, Scissors, Spock, and Lizard - Client GUI");
        BorderPane pane = new BorderPane(); //Welcome Pane

        //Server connection button
        Button connectionButton = new Button("Connect to server");

        //Text field to get port number
        TextField portNumberInput = new TextField();
        Text warningText = new Text("Please Enter Port number that the game server is on ");
        TextField IPAddressInput = new TextField();
        Text warningText2 = new Text("Please Enter your IP Address");

        VBox welcomeBox = new VBox(warningText, portNumberInput,warningText2, IPAddressInput);
        connectionButton.setAlignment(Pos.BOTTOM_CENTER);
        welcomeBox.setAlignment(Pos.CENTER_LEFT);

        pane.setCenter(connectionButton);
        pane.setTop(welcomeBox);

        //Event for server
        EventHandler<ActionEvent> connection = new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                Button tempButton = ((Button)event.getSource());
                if(tempButton == connectionButton){
                    //Get Port Number input
                    port = Integer.parseInt(portNumberInput.getText()); //Turn String to Int
                    IPAddress = IPAddressInput.getText();

                    currGame = new Game(port, IPAddress);
                    sceneMap.put("gamePlayScreen", currGame.gameScreen);
                    myStage.setScene(currGame.gameScreen); //Set Scene
                    currGame.driverFunction(); //Call driver
                    javafx.event.EventHandler<ActionEvent> returnButton = new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            myStage.setScene(sceneMap.get("welcome"));
                            try {
                                currGame.stop();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    Button homeButton  = currGame.homeButton;
                    homeButton.setOnAction(returnButton);

                }
            }
        };

        connectionButton.setOnAction(connection);

        Button exitButton = new Button("Exit Game");
        //Create Scene
        scene = new Scene(pane, 1200, 650);
        //Map Welcome Scene
        sceneMap.put("welcome", scene);
        //Lambda expression for exit button
        exitButton.addEventHandler(ActionEvent.ACTION, event1 -> System.exit(0));
        pane.setBottom(exitButton);
        primaryStage.setScene(sceneMap.get("welcome"));
        primaryStage.show();
    }

}

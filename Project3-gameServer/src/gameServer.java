
import java.util.HashMap;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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


public class gameServer extends Application{
    int port; //Holds port number for server
    Game currGame; //Stores current game being played

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception{
        Stage myStage = primaryStage;
        Scene scene;
        HashMap<String, Scene> sceneMap = new HashMap<String, Scene>();
        primaryStage.setTitle("Rock, Paper, Scissors, Spock, and Lizard - Server GUI");
        BorderPane pane = new BorderPane(); //Welcome Pane


        //Server power button
        Button serverPowerButton = new Button("Turn On Server");

        //Text field to get port number
        TextField portNumberInput = new TextField();
        Text warningText = new Text("Please Enter Port number to play on - before you press Turn On Server");

        VBox welcomeBox = new VBox(warningText, portNumberInput);
        serverPowerButton.setAlignment(Pos.BOTTOM_CENTER);
        welcomeBox.setAlignment(Pos.CENTER_LEFT);

        pane.setCenter(serverPowerButton);
        pane.setTop(welcomeBox);

        //Event for server
        EventHandler<ActionEvent> powerServerButton = new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                Button tempButton = ((Button)event.getSource());
                if(tempButton == serverPowerButton){
                    //Get Port Number input
                    port = Integer.parseInt(portNumberInput.getText()); //Turn String to Int
                    currGame = new Game(port);
                    sceneMap.put("gamePlayScreen", currGame.gameScreen);
                    myStage.setScene(currGame.gameScreen); //Set Scene
                    currGame.driverFunction(); //Call driver
                    javafx.event.EventHandler<ActionEvent> returnButton = new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event) {
                            myStage.setScene(sceneMap.get("welcome"));
                        }
                    };
                    Button homeButton  = currGame.homeButton;
                    homeButton.setOnAction(returnButton);
                }
            }
        };
        serverPowerButton.setOnAction(powerServerButton);

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

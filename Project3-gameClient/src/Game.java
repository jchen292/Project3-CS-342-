import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;

//Class that represents the game being played
public class Game {
    //Hold both player's scores
    int player1Score, player2Score;
    Scene gameScreen;
    BorderPane pane2;
    Button rockButton, paperButton, scissorsButton, spockButton, lizardButton, homeButton; //Buttons for GUI
    int gamePortNum; //store the port number of the gmae being played
    NetworkConnection conn;
    String IPAddress; //Holds IP Address
    String playerWinner; //Holds the winning string message
    TextArea messages = new TextArea(); //Message Box for game
    Game(int portNum, String IP) {
        pane2 = new BorderPane();
        gameScreen = new Scene(pane2, 1200, 550);
        homeButton = new Button("Quit To Home");
        //Create Buttons for game
        rockButton = new Button();
        paperButton = new Button();
        scissorsButton = new Button();
        spockButton = new Button();
        lizardButton = new Button();

        //Map Images to buttons
        HashMap<String, Image> ImageMap = new HashMap<>();
        Image rockPic = new Image("project3pictures/Rock.jpg");
        ImageMap.put("Rock", rockPic);
        ImageView rockView = new ImageView(rockPic);
        rockView.setFitHeight(200);
        rockView.setFitWidth(100);
        rockButton.setMinSize(120,120);
        rockView.setPreserveRatio(true);
        rockButton.setGraphic(rockView);

        Image paperPic = new Image("project3pictures/Paper.jpg");
        ImageMap.put("Paper", paperPic);
        ImageView paperView = new ImageView(paperPic);
        paperView.setFitHeight(200);
        paperView.setFitWidth(100);
        paperButton.setMinSize(120,120);
        paperView.setPreserveRatio(true);
        paperButton.setGraphic(paperView);

        Image scissorsPic = new Image("project3pictures/Scissors.jpg");
        ImageMap.put("Scissors", scissorsPic);
        ImageView scissorsView = new ImageView(scissorsPic);
        scissorsView.setFitHeight(200);
        scissorsView.setFitWidth(100);
        scissorsButton.setMinSize(120,120);
        scissorsView.setPreserveRatio(true);
        scissorsButton.setGraphic(scissorsView);

        Image lizardPic = new Image("project3pictures/Lizard.jpg");
        ImageMap.put("Lizard", lizardPic);
        ImageView lizardView = new ImageView(lizardPic);
        lizardView.setFitHeight(200);
        lizardView.setFitWidth(100);
        lizardButton.setMinSize(120,120);
        lizardView.setPreserveRatio(true);
        lizardButton.setGraphic(lizardView);

        Image spockPic = new Image("project3pictures/Spock.jpg");
        ImageMap.put("Spock", spockPic);
        ImageView spockView = new ImageView(spockPic);
        spockView.setFitHeight(200);
        spockView.setFitWidth(100);
        spockButton.setMinSize(120,120);
        spockView.setPreserveRatio(true);
        spockButton.setGraphic(spockView);

        //Game buttons
        HBox gameButtons = new HBox(rockButton,paperButton,scissorsButton,spockButton,lizardButton);
        gameButtons.setAlignment(Pos.CENTER);
        pane2.setCenter(gameButtons);
        pane2.setBottom(homeButton);
        homeButton.setAlignment(Pos.BOTTOM_RIGHT);
        this.gamePortNum = portNum;
        this.IPAddress = IP;

        conn = createClient();
        try {
            this.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        messages.setPrefHeight(300);

    } //End Constructor for Game
    // Test IP: 127.0.0.1
    private threadClient createClient() {
        return new threadClient(this.IPAddress, this.gamePortNum, data -> {
            Platform.runLater(()->{
                messages.appendText(data.toString() + "\n");
                this.playerWinner = data.toString(); //Get data from server on who won game
                receiveRoundWinner();
            });
        });
    }
    public void init() throws Exception{
        conn.startConn();
    }
    public void stop() throws Exception{
        conn.closeConn();
    }

    Parent createScoreBoard(){
        Text portNumText = new Text("You are playing on port: " + (this.gamePortNum));
        Text IPAddressText = new Text("Your IPAddress: " + this.IPAddress);
        Text player0Score = new Text("Player 1 Score: " + player1Score);
        Text player1Score = new Text("Player 2 Score: " + player2Score);
        VBox playerScores = new VBox();
        playerScores.getChildren().addAll(portNumText,IPAddressText,player0Score,player1Score,messages);
        playerScores.setAlignment(Pos.TOP_CENTER);
        return playerScores;
    }

    //Main driver function for the game
    void driverFunction(){
        //display score
        pane2.setTop(createScoreBoard());
         //Call Player Function
        playerMove();
    }

    //Function for player's move
    void playerMove(){
        javafx.event.EventHandler<ActionEvent> playerInput = new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                Button b = (Button)event.getSource();
                //Detect which button was pressed
                if(b == paperButton){
                    String data = "Paper";
                    //Send data to server
                    try {
                        conn.send(data);
                    } catch (Exception e) {
                        messages.appendText("Connection Closed for Server");
                    }
                    messages.appendText("You sent Paper"  + "\n");
                }
                else if(b == rockButton){
                    String data = "Rock";
                    //Send data to server
                    try {
                        conn.send(data);
                    } catch (Exception e) {
                        messages.appendText("Connection Closed for Server");
                    }
                    messages.appendText("You sent Rock"  + "\n");
                }
                else if(b == scissorsButton){
                    String data = "Scissors";
                    //Send data to server
                    try {
                        conn.send(data);
                    } catch (Exception e) {
                        messages.appendText("Connection Closed for Server");
                    }
                    messages.appendText("You sent Scissors"  + "\n");
                }
                else if(b == lizardButton){
                    String data = "Lizard";
                    //Send data to server
                    try {
                        conn.send(data);
                    } catch (Exception e) {
                        messages.appendText("Connection Closed for Server");
                    }
                    messages.appendText("You sent Lizard"  + "\n");
                }
                else if(b == spockButton){
                    String data = "Spock";
                    //Send data to server
                    try {
                        conn.send(data);
                    } catch (Exception e) {
                        messages.appendText("Connection Closed for Server");
                    }
                    messages.appendText("You sent Spock"  + "\n");
                }
            }
        };

        rockButton.setOnAction(playerInput);
        paperButton.setOnAction(playerInput);
        scissorsButton.setOnAction(playerInput);
        lizardButton.setOnAction(playerInput);
        spockButton.setOnAction(playerInput);
    }



    //Receive data on who won from server
    void receiveRoundWinner(){
        //Find out who won the round
        if(playerWinner.equals("Winner of the Round: Player 1")){
            player1Score++;
        }
        else if(playerWinner.equals("Winner of the Round: Player 2")){
            player2Score++;
        }
        //Recall the driver function to move on to next round
        playerWinner = "";
        driverFunction();

    } //End of function receiveRoundWinner

} //End of Class

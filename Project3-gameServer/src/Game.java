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

import java.net.Socket;
import java.util.ArrayList;

//Class that represents the game being played
public class Game {
    //Hold both player's scores
    int player1Score, player2Score;
    Player player1,player2;
    Scene gameScreen;
    BorderPane pane2;
    Button homeButton; //Buttons for GUI
    int gamePortNum; //store the port number of the gmae being played
    NetworkConnection GameServer; //Stores game server
    ArrayList<Socket> clients = new ArrayList<>(2); //Stores cilents in the server (Index 0 is player1, 1 is player2)
    int threadCounter, driverCallCounter; //Counters
    String player1Data,player2Data;
    //Array to hold the gameplay strings
    String gameplayStrings[] =  {           //Index Number
            "Scissors cuts paper",          // 0
            "paper covers rock",            // 1
            "rock crushes lizard",          // 2
            "lizard poisons Spock",         // 3
            "Spock smashes scissors",       // 4
            "scissors decapitates lizard",  // 5
            "lizard eats paper",            // 6
            "paper disproves Spock",        // 7
            "Spock vaporizes rock",         // 8
            "rock crushes scissors" };      // 9
    Game(int portNum) {
        player1 = new Player();
        player2 = new Player();
        this.gamePortNum = portNum;
        pane2 = new BorderPane();
        gameScreen = new Scene(pane2, 1200, 550);
        homeButton = new Button("Quit To Home and Close Server");
        homeButton.setAlignment(Pos.BASELINE_CENTER);
        homeButton.setTranslateX(500);
        pane2.setBottom(homeButton);

        GameServer = createServer();
        try {
            this.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } //End Constructor for Game

    //Function to create game server
    private threadServer createServer() {
        return new threadServer(this.gamePortNum, data-> {
            Platform.runLater(()->{
                this.clients = GameServer.clients;
                Text messageBox = new Text("Clients Connected: " + clients.size());
                pane2.setRight(messageBox);
                if(clients.size() == 2){
                    if(threadCounter % 2 == 0){
                        player1Data = data.toString();
                    }
                    else if(threadCounter % 2 == 1){
                        player2Data = data.toString();
                        driverCallCounter++;
                    }
                    if(threadCounter % 2 == 1){
                        driverFunction();
                    }
                    threadCounter++;
                }
            });
        });
    } //End createServer()

    public void init() throws Exception{
        GameServer.startConn();
    }


    Parent createScoreBoard(){
        Text portNumText = new Text("Port Number game is being played on: " + this.gamePortNum);
        Text player0Score = new Text("Player 1 Score: " + player1Score);
        Text player1Score = new Text("Player 2 Score: " + player2Score);
        VBox playerScores = new VBox();
        playerScores.getChildren().addAll(portNumText,player0Score,player1Score);
        playerScores.setAlignment(Pos.TOP_CENTER);
        return playerScores;
    }

    //Main driver function for the game
    void driverFunction(){
        //display score
        pane2.setTop(createScoreBoard());
        if(driverCallCounter % 2 == 1){
            getplayer1Move();//Call Player 1 Function
        }
    }

    //Function for first player's move(Get the move from the client)
    void getplayer1Move() {
        System.out.println("Player1 chosen: " + player1Data);

        if(player1Data.equals("Rock")){
            player1.setChosenMove(playerChoice.Rock);
        }
        else if(player1Data.equals("Paper")){
            player1.setChosenMove(playerChoice.Paper);
        }
        else if(player1Data.equals("Scissors")){
            player1.setChosenMove(playerChoice.Scissors);
        }
        else if(player1Data.equals("Lizard")){
            player1.setChosenMove(playerChoice.Lizard);
        }
        else if(player1Data.equals("Spock")){
            player1.setChosenMove(playerChoice.Spock);
        }
        //Call the 2nd player function
        getplayer2Move();
    } //End of func
    //Function for second player's move(Get the move from the client)
    void getplayer2Move(){
        System.out.println("Player2 chosen: " + player2Data);

        //Set player move according to data
        if(player2Data.equals("Rock")){
            player2.setChosenMove(playerChoice.Rock);
        }
        else if(player2Data.equals("Paper")){
            player2.setChosenMove(playerChoice.Paper);
        }
        else if(player2Data.equals("Scissors")){
            player2.setChosenMove(playerChoice.Scissors);
        }
        else if(player2Data.equals("Lizard")){
            player2.setChosenMove(playerChoice.Lizard);
        }
        else if(player2Data.equals("Spock")){
            player2.setChosenMove(playerChoice.Spock);
        }
        //determine who won the round
        determineRoundWinner();
    } //End of func


    //Run the choices of the players here to determine the winner of the round, I will use player1 as the main comparison
    void determineRoundWinner(){
        //Checks for the win message
        boolean player1Check = false;
        boolean player2Check = false;
        //Checks for the gameplay strings
        boolean string0 = false;
        boolean string1 = false;
        boolean string2 = false;
        boolean string3 = false;
        boolean string4 = false;
        boolean string5 = false;
        boolean string6 = false;
        boolean string7 = false;
        boolean string8 = false;
        boolean string9 = false;

        try {
            GameServer.send1("Opponent selected: " + player2Data);
            GameServer.send2("Opponent selected: " + player1Data);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Check for cases of rock
        if(player1.getChosenMove() == playerChoice.Rock){
                if(player2.getChosenMove() == playerChoice.Paper){
                    player2Score++; //Paper beats Rock, player 2 wins
                    try {
                        GameServer.send1(gameplayStrings[1]);
                        GameServer.send2(gameplayStrings[1]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    string1 = true;
                    player2Check = true;
                }
                else if(player2.getChosenMove() == playerChoice.Scissors){
                    player1Score++; //Rock Beats Scissors, player 1 wins
                    try {
                        GameServer.send1(gameplayStrings[9]);
                        GameServer.send2(gameplayStrings[9]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    string9 = true;
                    player1Check = true;
                }
                else if(player2.getChosenMove() == playerChoice.Lizard){
                    player1Score++; //Rock Beats Lizard, player 1 wins
                    try {
                        GameServer.send1(gameplayStrings[2]);
                        GameServer.send2(gameplayStrings[2]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    string2 = true;
                    player1Check = true;
                }
                else if(player2.getChosenMove() == playerChoice.Spock){
                    player2Score++; //Spock beats rock, player 2 wins
                    try {
                        GameServer.send1(gameplayStrings[8]);
                        GameServer.send2(gameplayStrings[8]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    string8 = true;
                    player2Check = true;
                }
                //Check for tied scenario
                else if(player2.getChosenMove() == playerChoice.Rock){
                    player1Check = true;
                    player2Check = true;
                }
        } //End if statements for rock

        //Check for cases with paper
        if(player1.getChosenMove() == playerChoice.Paper){
            if(player2.getChosenMove() == playerChoice.Rock){
                player1Score++; //Paper beats Rock, player 1 wins
                try {
                    GameServer.send1(gameplayStrings[1]);
                    GameServer.send2(gameplayStrings[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string1 = true;
                player1Check = true;
            }
            else if(player2.getChosenMove() == playerChoice.Scissors){
                player2Score++; //Scissors Beats Paper, player 2 wins
                try {
                    GameServer.send1(gameplayStrings[0]);
                    GameServer.send2(gameplayStrings[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string0 = true;
                player2Check = true;
            }
            else if(player2.getChosenMove() == playerChoice.Lizard){
                player2Score++; //Lizard Beats Paper, player 2 wins
                try {
                    GameServer.send1(gameplayStrings[6]);
                    GameServer.send2(gameplayStrings[6]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string6 = true;
                player2Check = true;
            }
            else if(player2.getChosenMove() == playerChoice.Spock){
                player1Score++; //paper beats Spock, player 1 wins
                try {
                    GameServer.send1(gameplayStrings[7]);
                    GameServer.send2(gameplayStrings[7]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string7 = true;
                player1Check = true;
            }
            //Check for tied scenario
            else if(player2.getChosenMove() == playerChoice.Paper){
                player1Check = true;
                player2Check = true;
            }
        } //End if statements for paper

        //Check for cases with Scissors
        if(player1.getChosenMove() == playerChoice.Scissors){
            if(player2.getChosenMove() == playerChoice.Rock){
                player2Score++; //Rock beats Scissors, player 2 wins
                try {
                    GameServer.send1(gameplayStrings[9]);
                    GameServer.send2(gameplayStrings[9]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string9 = true;
                player2Check = true;
            }
            else if(player2.getChosenMove() == playerChoice.Paper){
                player1Score++; //Scissors Beats Paper, player 1 wins
                try {
                    GameServer.send1(gameplayStrings[0]);
                    GameServer.send2(gameplayStrings[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string0 = true;
                player1Check = true;
            }
            else if(player2.getChosenMove() == playerChoice.Lizard){
                player1Score++; //Scissors Beats Lizard, player 1 wins
                try {
                    GameServer.send1(gameplayStrings[5]);
                    GameServer.send2(gameplayStrings[5]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string5 = true;
                player1Check = true;
            }
            else if(player2.getChosenMove() == playerChoice.Spock){
                player2Score++; //Spock beats Scissors, player 2 wins
                try {
                    GameServer.send1(gameplayStrings[4]);
                    GameServer.send2(gameplayStrings[4]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string4 = true;
                player2Check = true;
            }
            //Check for tied scenario
            else if(player2.getChosenMove() == playerChoice.Scissors){
                player1Check = true;
                player2Check = true;
            }
        } //End if statements for Scissors

        //Check for cases with lizard
        if(player1.getChosenMove() == playerChoice.Lizard){
            if(player2.getChosenMove() == playerChoice.Rock){
                player2Score++; //Rock beats lizard, player 2 wins
                try {
                    GameServer.send1(gameplayStrings[2]);
                    GameServer.send2(gameplayStrings[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string2 = true;
                player2Check = true;
            }
            else if(player2.getChosenMove() == playerChoice.Paper){
                player1Score++; //Lizard Beats Paper, player 1 wins
                try {
                    GameServer.send1(gameplayStrings[6]);
                    GameServer.send2(gameplayStrings[6]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string6 = true;
                player1Check = true;
            }
            else if(player2.getChosenMove() == playerChoice.Scissors){
                player2Score++; //Scissors beats lizard, player 2 wins
                try {
                    GameServer.send1(gameplayStrings[5]);
                    GameServer.send2(gameplayStrings[5]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string5 = true;
                player2Check = true;
            }
            else if(player2.getChosenMove() == playerChoice.Spock){
                player1Score++; //lizard beats Spock, player 1 wins
                try {
                    GameServer.send1(gameplayStrings[3]);
                    GameServer.send2(gameplayStrings[3]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string3 = true;
                player1Check = true;
            }
            //Check for tied scenario
            else if(player2.getChosenMove() == playerChoice.Lizard){
                player1Check = true;
                player2Check = true;
            }
        } //End if statements for lizard

        //Check for cases with Spock
        if(player1.getChosenMove() == playerChoice.Spock) {
            if (player2.getChosenMove() == playerChoice.Rock) {
                player1Score++; //Spock beats Rock, player 1 wins
                try {
                    GameServer.send1(gameplayStrings[8]);
                    GameServer.send2(gameplayStrings[8]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string8 = true;
                player1Check = true;
            }
            else if(player2.getChosenMove() == playerChoice.Paper){
                player2Score++; //Paper Beats Spock, player 2 wins
                try {
                    GameServer.send1(gameplayStrings[7]);
                    GameServer.send2(gameplayStrings[7]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string7 = true;
                player2Check = true;
            }
            else if(player2.getChosenMove() == playerChoice.Scissors){
                player1Score++; //Spock beats Scissors, player 1 wins
                try {
                    GameServer.send1(gameplayStrings[4]);
                    GameServer.send2(gameplayStrings[4]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string4 = true;
                player1Check = true;
            }
            else if(player2.getChosenMove() == playerChoice.Lizard){
                player2Score++; //lizard beats Spock, player 2 wins
                try {
                    GameServer.send1(gameplayStrings[3]);
                    GameServer.send2(gameplayStrings[3]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                string3 = true;
                player2Check = true;
            }
            //Check for tied scenario
            else if(player2.getChosenMove() == playerChoice.Spock){
                player1Check = true;
                player2Check = true;
            }
        } //End if statements for Spock

        //Create action message on the server screen
        Text actionText = null;
        if(string0){
            actionText = new Text(gameplayStrings[0]);
        }
        if(string1){
            actionText = new Text(gameplayStrings[1]);
        }
        if(string2){
            actionText = new Text(gameplayStrings[2]);
        }
        if(string3){
            actionText = new Text(gameplayStrings[3]);
        }
        if(string4){
            actionText = new Text(gameplayStrings[4]);
        }
        if(string5){
            actionText = new Text(gameplayStrings[5]);
        }
        if(string6){
            actionText = new Text(gameplayStrings[6]);
        }
        if(string7){
            actionText = new Text(gameplayStrings[7]);
        }
        if(string8){
            actionText = new Text(gameplayStrings[8]);
        }
        if(string9){
            actionText = new Text(gameplayStrings[9]);
        }
        pane2.setCenter(actionText);


        //Find out who won the round
        if((player1Check == true) && (player2Check == true)){  //Check for tie first
            Text winnerText = new Text("Both players Tied this round");
            System.out.println("Both players Tied this round");
            try {
                GameServer.send1("Both players Tied this round");
                GameServer.send2("Both players Tied this round");
            } catch (Exception e) {
                e.printStackTrace();
            }

            pane2.setLeft(winnerText);
        }
        else if(player1Check){
            Text winnerText = new Text("Winner of the Round: Player 1");
            System.out.println("Winner of the Round: Player 1");
            try {
                GameServer.send1("Winner of the Round: Player 1");
                GameServer.send2("Winner of the Round: Player 1");
            } catch (Exception e) {
                e.printStackTrace();
            }
            pane2.setLeft(winnerText);
        }
        else if(player2Check){
            Text winnerText = new Text("Winner of the Round: Player 2");
            System.out.println("Winner of the Round: Player 2");
            try {
                GameServer.send1("Winner of the Round: Player 2");
                GameServer.send2("Winner of the Round: Player 2");
            } catch (Exception e) {
                e.printStackTrace();
            }
            pane2.setLeft(winnerText);
        }

        //Recall the driver function to move on to next round
        driverCallCounter++;
        driverFunction();

    } //End of function determineRoundWinner

} //End of Class

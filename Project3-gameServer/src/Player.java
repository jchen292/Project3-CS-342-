
//Represents the two players in the game
public class Player {
    private playerChoice chosenMove; //Holds the move of the player during the round

    void setChosenMove(playerChoice C){
        this.chosenMove = C;
    }

    playerChoice getChosenMove(){
        return this.chosenMove;
    }

}

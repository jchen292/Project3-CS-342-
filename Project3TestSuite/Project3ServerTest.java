import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.*;

// Testing class that holds all the test cases for the whole project (6 in Total
class Project3ServerTest {

    @Test
    void gameConstructorTest(){
        Game tester = new Game(12);
        assertEquals(12,tester.gamePortNum);
    }

    @Test
    void determineRoundWinnerTest() {
        Game testGame = new Game(12);
        testGame.player1.setChosenMove(playerChoice.Paper);
        testGame.player2.setChosenMove(playerChoice.Rock);
        testGame.determineRoundWinner();
        assertEquals(2,testGame.driverCallCounter);
    }

    @Test
    void playerConstructorTest(){
        Player playerTest = new Player();
        playerTest.setChosenMove(playerChoice.Lizard);
        assertEquals(playerChoice.Lizard,playerTest.getChosenMove());
    }

    @Test
    void threadServerTest(){
        Consumer<Serializable> dummyObject = null;
        threadServer test = new threadServer(12, dummyObject);
        assertEquals(12,test.getPort());
    }

    @Test
    void networkConnectionTest(){
        Consumer<Serializable> dummy = null;
        NetworkConnection conn = new NetworkConnection(dummy) {
            @Override
            protected int getPort() {
                return 0;
            }
        };
        assertEquals(0,conn.getPort());
    }

    @Test
    void gameServerTest(){
        gameServer test = new gameServer();
        assertNull(test.currGame);
    }




} //End Testing Class
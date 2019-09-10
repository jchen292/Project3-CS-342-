import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

//Holds the Test cases for the project (4 in total)
class Project3ClientTest {
    @Test
    void clientTest(){
        Consumer<Serializable> dummy = null;
        threadClient test = new threadClient("127.0.0.1",12, dummy);
        assertEquals("127.0.0.1",test.getIP());
        assertEquals(12,test.getPort());
    }
    @Test
    void gameTest(){
        gameClient GUITest = new gameClient();
        GUITest.currGame  = new Game(12,"127.0.0.1");
        assertEquals(12,GUITest.currGame.gamePortNum);
        assertEquals("127.0.0.1",GUITest.currGame.IPAddress);
    }

    @Test
    void gameTest2(){
        Game test = new Game(12,"127.0.0.1");
        assertEquals(12,test.gamePortNum);
        assertEquals("127.0.0.1",test.IPAddress);
    }
    @Test
    void gameClientTest(){
        gameClient test = new gameClient();
        test.port = 12;
        test.IPAddress = "127.0.0.1";
        assertEquals(12,test.port);
        assertEquals("127.0.0.1",test.IPAddress);
        assertNull(test.currGame);
    }


} //End Test Cases
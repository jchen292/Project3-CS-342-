import java.io.Serializable;
import java.util.function.Consumer;

public class threadServer extends NetworkConnection{
    private int portNum; //Stores port number for server

    public threadServer(int port, Consumer<Serializable> callback) {
        super(callback);
        this.portNum = port;
    }

    @Override
    protected int getPort() {
        return portNum;
    }

}

import java.io.PrintWriter;
import java.util.HashMap;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class NetworkConnection {

    private ConnThread connthread = new ConnThread();
    private Consumer<Serializable> callback;
    ArrayList<Socket> clients = new ArrayList<>(); //Stores clients in the server (Index 0 is player1, 1 is player2)

    public NetworkConnection(Consumer<Serializable> callback) {
        this.callback = callback;
        connthread.setDaemon(true);
    }

    public void startConn() throws Exception{
        connthread.start();
    }

    public void send1(Serializable data) throws Exception{
        connthread.c1.out.writeObject(data);
    }
    public void send2(Serializable data) throws Exception{
        connthread.c2.out.writeObject(data);
    }


    abstract protected int getPort();

    class ConnThread extends Thread{
        clientThread c1,c2;

        //One of my bugs: couldn't distinguish players and clients(first run of client program is player1, next is player2)
        public void run() {
            try (
                ServerSocket server = new ServerSocket(getPort());
            ) {
                while(true) {
                    c1 = new clientThread(server.accept());
                    c2 = new clientThread(server.accept());
                    clients.add(c1.socket);
                    clients.add(c2.socket);

                    c1.start();
                    c2.start();
                }
            }
            catch(Exception e) {
                callback.accept("Connection Closed for server");
            }
        } //End run()
    } //end class

    class clientThread extends Thread{
        Socket socket;
        ObjectOutputStream out;
        clientThread(Socket s){
            this.socket = s;
        }
        @Override
        public void run() {
            try(ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
                this.out = out;
                while(true){
                    Serializable data = (Serializable) in.readObject();
                    callback.accept(data);
                } //End while loop
            }
            catch(Exception e) {
                callback.accept("connection Closed");
            }
        } //End run()

    } //End class clientThread

} // End Network connection class



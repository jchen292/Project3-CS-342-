import java.io.Serializable;
import java.util.function.Consumer;

public class threadClient extends NetworkConnection {

	private String ip;
	private int port; 
	
	public threadClient(String ip, int port, Consumer<Serializable> callback) {
		super(callback);
		this.ip = ip;
		this.port = port;
	}

	@Override
	protected String getIP() {
		return this.ip;
	}

	@Override
	protected int getPort() {
		return this.port;
	}

}

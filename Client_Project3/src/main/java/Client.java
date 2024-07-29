import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {
	private Socket socketClient;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Consumer<Game> onResponse;
	int port;
	String host;
	public Client(Consumer<Game> onResponse) {
		this.onResponse = onResponse;
	}

	public void startConnection(String host, int port) {
		this.port = port;
		this.host = host;
	}

	@Override
	public void run() {
		try {
			System.out.println(port);
			System.out.println(host);
			socketClient = new Socket(this.host, this.port);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);

			while (true) {
				try {
					Game data = (Game) in.readObject();
					onResponse.accept(data);
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// todo: use a callback to the gui if the user put the wrong port number
			// Handle connection error or stream initialization error
		}
	}

	public void send(Game data) {
		try {
			out.writeObject(data);				// giving an error
			out.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnectionEstablished() {
		return socketClient != null && out != null && in != null;
	}

}

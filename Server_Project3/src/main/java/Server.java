import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Server{

	int portNumber;
	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	Game gameObject = new Game();

	Server(Consumer<Serializable> call, int port){
		this.portNumber = port;
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{

		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(portNumber);){
				System.out.println("Server started");
				while(true) {
					Socket socket = mysocket.accept();
					ClientThread c = new ClientThread(socket, count);

					gameObject.flag = "Client #" + count + " has connected to server!";
					callback.accept(gameObject); // send data to Platform.runLater
					clients.add(c);
					c.start();
					count++;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				gameObject.flag = "Server socket did not launch";
				callback.accept(gameObject);
			}
			finally {
				for (ClientThread client : clients) {
					client.interrupt(); // Interrupt each client thread
				}
				try {
					server.interrupt(); // Interrupt the server thread
					server.join(); // Wait for the server thread to terminate
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} //end of while
	}
	

		class ClientThread extends Thread{
			// each client has it's on thread to revise and send the data
		
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			GameLogic gameLogic = new GameLogic(); // Instantiate GameLogic
			String wordToGuess1;
//			Game data = new Game(); // Instantiate GameLogic
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}

//			private void updateGameData(Game newData) {
//				data.category = newData.category;
//				data.wordToGuess = newData.wordToGuess;
//				data.numberOfLetter = newData.numberOfLetter;
//				data.guessedLetters = newData.guessedLetters;
//				data.remainingGuesses = newData.remainingGuesses;
//				data.guess = newData.guess;
//				data.isValidGuess = newData.isValidGuess;
//				data.isGameWon = newData.isGameWon;
//				data.isGameLoss = newData.isGameLoss;
//				data.isGameOver = newData.isGameOver;
//				data.isReplay = newData.isReplay;
//				data.position = newData.position;
//				data.animaAttempt = newData.animaAttempt;
//				data.fruitsAttempt = newData.fruitsAttempt;
//				data.countryAttempt = newData.countryAttempt;
//				data.flag = newData.flag; 			// todo: check with TA
//			}

			public void run(){
				// receive the game object and send the obj
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				while(true) {
					try {
						Game newData =(Game) in.readObject(); 				// take object
//						updateGameData(newData);
						System.out.println("flag " + newData.flag);
						System.out.println("Guess: #" + newData.guess);
						if (newData.flag.equals("Select request category")) {
							this.wordToGuess1 = new WordGenerator().getRandomWord(newData.category).toUpperCase();
							System.out.println("word original: " + wordToGuess1);
							newData.numberOfLetter = gameLogic.wordLength(wordToGuess1, newData);
							newData.tryAgainGame();
							newData.flag = "Updated request category";
						}
						else if (newData.flag.equals("Select request user guess")) {
							System.out.println("guess letter: " + newData.guess);
							newData.isValidGuess = gameLogic.validateUserGuess(wordToGuess1, newData);
							newData.guessedLetters.add(newData.guess);
							if (newData.isValidGuess) {
								newData.position = gameLogic.position(wordToGuess1, newData);
								if (gameLogic.isWordGuessed(newData, wordToGuess1)) {
									newData.isGameWon = true;
								}
							}
							else {
								System.out.println("reaming before: " + newData.remainingGuesses);
								newData.remainingGuesses = gameLogic.validateGuess(wordToGuess1, newData);
								System.out.println("reaming after: " + newData.remainingGuesses);
								if (newData.remainingGuesses <= 0) {
									newData.isGameLoss = true;
								}
							}
							newData.flag = "Updated request user guess";
						}
						else if (newData.flag.equals("Select request replay")) {
							System.out.println("in server: replay");
							if (newData.isReplay) {
								gameLogic.restartGame(newData);
							}
							newData.flag = "Updated request replay";
						}
						else if (newData.flag.equals("Select request game over")) {
							if (newData.animaAttempt == 0 && newData.category.getCategoryName().equals("ANIMALS")) {
								newData.isGameOver = true;
							}
							if (newData.fruitsAttempt == 0 && newData.category.getCategoryName().equals("FRUITS")) {
								newData.isGameOver = true;
							}
							if (newData.countryAttempt == 0 && newData.category.getCategoryName().equals("COUNTRIES")) {
								newData.isGameOver = true;
							}
							newData.flag = "Updated game over";
						}
						else if (newData.flag.equals("Select request attempt left")) {
							gameLogic.attemptLeft(newData, wordToGuess1);
							newData.flag = "Updated attempt left";
						}
						else if (newData.flag.equals("Select request exit")) {
							// set on the exit
							gameObject.flag = "Client # " + count + " disconnected! Closing down";
							callback.accept(gameObject);
							clients.remove(this);
							newData.flag = "Updated exit";
							try {
								connection.close();
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							break;
						}
						System.out.println("after: " + newData.remainingGuesses);
						System.out.println("Updated: " + newData.flag);
//						updateGameData(newData);
//						out.writeObject(data);
						out.writeObject(newData);
						out.reset();
					}
					catch(Exception e) {
						gameObject.flag = "Client # " + count + " disconnected! Closing down";
						callback.accept(gameObject);
						clients.remove(this);
						try {
							connection.close();
						} catch (IOException ex) {
							throw new RuntimeException(ex);
						}
						e.printStackTrace();

						break;
					}
				}

			}

		} //end of client thread
}


	
	

	

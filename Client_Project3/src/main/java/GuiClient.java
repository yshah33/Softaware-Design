import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.shape.Line;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class GuiClient extends Application {

	private ListView<String> listItems2; // Make this a field so it can be accessed in the update method

	private List<Label> wordDisplayLabels = new ArrayList<>();    // Hold each label for each letter in the guessing Word
	private static GuiClient instance;
	HBox wordDisplayBox = new HBox(10); // Adjust spacing to match your design
	public Label totalAttemptsLabel;


	public GuiClient() {
		instance = this;
	}
	private Button tryAgain, newCategory, exit;
	Label wordToGuess;
	private HashMap<String, Scene> sceneMap;
	private VBox clientBox;
	private Client clientConnection;
	private BorderPane welcomePane, categoryPane, guessWordPane, rulePane, displayWinPane, exitPane;
	Game game = new Game();
	ArrayList<Label> wordText;
	private Scene previousScene;	//to keep track when click "Go Back" button from Rules scene


	public static void main(String[] args) {
		launch(args);
	}

	public static void launchGui(String[] args) {
		Thread guiThread = new Thread(() -> launch(args));
		guiThread.setDaemon(true);
		guiThread.start();
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Hangman Client GUI");

		exitPane = new BorderPane();
		sceneMap = new HashMap<>();

		sceneMap.put("client", firstScene(primaryStage));
		sceneMap.put("rules", createRulesScene(primaryStage));

		primaryStage.setScene(sceneMap.get("client"));

		initializeClientConnection(primaryStage);
		primaryStage.setOnCloseRequest(this::handleCloseRequest);
		primaryStage.show();
	}

	private MenuBar menuBar(Stage primaryStage) {
		MenuBar menuBar = new MenuBar();
		Menu menuOption = new Menu("Menu");
		MenuItem gameRule = new MenuItem("Rules");
		MenuItem exitMenu = new MenuItem("Exit");

		menuOption.getItems().addAll(gameRule, exitMenu);
		menuBar.getMenus().add(menuOption);

		primaryStage.setAlwaysOnTop(true);

		exitMenu.setOnAction(e -> {
			if (clientConnection != null && clientConnection.isConnectionEstablished()) {
				game.flag = "Select request exit";
				clientConnection.send(game);
			}
//			game.flag = "Select request exit";
//			clientConnection.send(game);

			Platform.exit();
		});

		gameRule.setOnAction(e -> {
			previousScene = primaryStage.getScene(); // Store the current scene
			primaryStage.setScene(sceneMap.get("rules"));
		});

		return menuBar;
	}

	private void initializeClientConnection(Stage primaryStage) {
		primaryStage.setScene(sceneMap.get("client"));
		primaryStage.setTitle("This is a client");


		clientConnection = new Client(data -> {
			Platform.runLater(() -> {
				if (data instanceof Game) {
					game = (Game) data;
					if (game.flag.equals("Updated request category")) {
						System.out.println("in the category");
						primaryStage.setScene(thirdScene(primaryStage));
						System.out.println("called scene three");

						wordDisplayBox.getChildren().clear();
						wordText = new ArrayList<>();
						for (int i = 0; i < game.numberOfLetter; i++) {
							Label letterLabel = new Label("_");
							wordText.add(letterLabel);
							letterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));
							letterLabel.setTextFill(Color.BLACK);
							wordDisplayBox.getChildren().add(letterLabel);
						}
						game.flag = "Select request category";
					}
					else if (game.flag.equals("Updated request user guess")) {
						System.out.println("in the user guess");
						if (game.isValidGuess) {
							System.out.println("in the guess -> valid guess");
							// Substitutes _ for letter
							for (int eachPos : game.position) {
								if (eachPos <= wordText.size()) {
									wordText.get(eachPos).setText(String.valueOf(game.guessedLetters.get(game.guessedLetters.size() -1)));
								}
							}
							if(game.isGameWon){
								primaryStage.setScene(fourthScene(primaryStage));

							}
						}
						else {	// if it is not a valid guess
							System.out.println("in the guess -> not valid guess");
							if(game.remainingGuesses > 0) {
								totalAttemptsLabel.setText("Total Attempts: #" + game.remainingGuesses);
							}
							if (game.isGameLoss) {
								primaryStage.setScene(fourthScene(primaryStage));
							}
						}
						game.flag = "Select request user guess";
					}
					else if (game.flag.equals("Updated request replay")) {
						System.out.println("in the replay");
						System.out.println("in the replay remain" + game.remainingGuesses);
						System.out.println("in the replay game win" + game.isGameWon);
						System.out.println("in the replay game loss" + game.isGameLoss);
						System.out.println("in the replay game cat" + game.category);
						System.out.println("in the replay game over" + game.isGameOver);
						game.flag = "Select request replay";
					}
					else if (game.flag.equals("Updated game over")) {
						game.flag = "Select request game over";
					}
					else if (game.flag.equals("Updated attempt left")) {
						System.out.println("in the attempts left");
						System.out.println("animal: " + game.animaAttempt);
						if (game.category.getCategoryName().equals("ANIMALS") && game.animaAttempt <= 0 && !game.isGameWon) {
							tryAgain.setDisable(true);
							newCategory.setDisable(true);
						}
						else if (game.category.getCategoryName().equals("FRUITS") && game.fruitsAttempt <= 0 && !game.isGameWon) {
							tryAgain.setDisable(true);
							newCategory.setDisable(true);
						}
						else if (game.category.getCategoryName().equals("COUNTRIES") && game.countryAttempt <= 0 && !game.isGameWon) {
							tryAgain.setDisable(true);
							newCategory.setDisable(true);
						}
						game.flag = "Select request attempt left";
					}
					else if (game.flag.equals("Updated exit")) {
						game.flag = "Select request exit";
						Platform.exit();
						System.exit(0);
					}
				}
			});
		});
	}

	public Scene firstScene(Stage primaryStage) {
		welcomePane = new BorderPane();
		Label welcomeText = new Label("WELCOME TO");
		welcomeText.setStyle("-fx-font-family: " +
				"'Times New Roman'; " +
				"-fx-font-size: 50; " +
				"-fx-text-fill: #DAA520; " +
				"-fx-font-weight: bold");

		Label TextWelcome = new Label("HANGMAN");
		TextWelcome.setStyle("-fx-font-family: " +
				"'Times New Roman'; " +
				"-fx-font-size: 50; " +
				"-fx-text-fill: #DAA520; " +
				"-fx-font-weight: bold");

		TextField portTextField = new TextField();
		portTextField.setPromptText("Enter port Number");
		portTextField.setMaxWidth(80);

		Button playGame = new Button("PLAY");
		playGame.setPrefSize(100, 50);
		playGame.setDisable(true);
		buttonStyle(playGame);

		portTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			playGame.setDisable(!newValue.matches("\\d{5}")); // Enable b1 only for 5 digits
		});

//		playGame.setOnAction(e -> {
//			int port = Integer.parseInt(portTextField.getText().trim()); // Get the port number from the TextField
//			try {
//				String portString = portTextField.getText().trim();
//				if (portString.isEmpty()) {
//					throw new NumberFormatException("Port field is empty");
//				}
//					//todo: EDGE CASES FOR PORT NUMBER
//				int portValue = Integer.parseInt(portString);
//				if (portValue < 1024 || portValue > 65535) {
//					throw new IllegalArgumentException("Port number must be between 1024 and 65535");
//				}
//				primaryStage.setScene(secondScene(primaryStage)); // Switch to the next scene
//
//			} catch (NumberFormatException x) {
//				System.out.println("Invalid port number");
//			}
//
//			clientConnection.startConnection("127.0.0.1", port); // Use thee port number
//			clientConnection.start();
//		});

		playGame.setOnAction(e -> {
			try {
				String portString = portTextField.getText().trim();
				if (portString.isEmpty()) {
					throw new IllegalArgumentException("Port field is empty");
				}
				int portValue = Integer.parseInt(portString);
				if (portValue < 1024 || portValue > 65535) {
					throw new IllegalArgumentException("Port number must be between 1024 and 65535");
				}
				primaryStage.setScene(secondScene(primaryStage)); // Switch to the next scene
				clientConnection.startConnection("127.0.0.1", portValue); // Start connection
				clientConnection.start();
			} catch (IllegalArgumentException ex) {
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				errorAlert.initOwner(primaryStage);
				errorAlert.setHeaderText("Invalid Input");
				errorAlert.setContentText(ex.getMessage());
				errorAlert.showAndWait();
			}
		});


		this.welcomePane.setTop(menuBar(primaryStage));
		VBox vbox = new VBox(15, welcomeText, TextWelcome, portTextField, playGame);
		VBox.setMargin(portTextField, new Insets(20, 0, 0, 0));
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));
		this.welcomePane.setCenter(vbox);

		return new Scene(this.welcomePane, 700, 700);
	}

	public Scene secondScene(Stage primaryStage) {
		categoryPane = new BorderPane();

		VBox topVBox = new VBox(menuBar(primaryStage)); // VBox for the menuBar
		topVBox.setAlignment(Pos.CENTER);

		Label categoryLabel = new Label("Pick a Category");
		categoryLabel.setStyle("-fx-font-family: 'Times New Roman'; " +
				"-fx-font-size: 50; " +
				"-fx-font-weight: bold;");
		categoryLabel.setAlignment(Pos.CENTER);

		VBox categoryLabelVBox = new VBox(categoryLabel);
		categoryLabelVBox.setAlignment(Pos.CENTER);
		categoryLabel.setTranslateY(60);
		this.categoryPane.setTop(new VBox(topVBox, categoryLabelVBox));


		Line line = new Line(100, 170, 600, 170);
		BorderPane.setAlignment(line, Pos.CENTER);
		line.setStroke(Color.BLACK);
		line.setStrokeWidth(4);
		BorderPane.setMargin(line, new Insets(100, 0, 0, 0));
		this.categoryPane.getChildren().add(line);

		Button Animal = new Button("ANIMALS");
		Button Fruit = new Button("FRUITS");
		Button Country = new Button("COUNTRIES");
		buttonStyle(Animal);
		buttonStyle(Fruit);
		buttonStyle(Country);

		if (game.animaAttempt == 0) {
			Animal.setDisable(true);
		}
		if (game.fruitsAttempt == 0) {
			Fruit.setDisable(true);
		}
		if (game.countryAttempt == 0) {
			Country.setDisable(true);
		}
		HBox categoryName = new HBox(50, Animal, Fruit, Country);
		Animal.setPrefSize(160, 70);
		Fruit.setPrefSize(160, 70);
		Country.setPrefSize(160, 70);

		Text animalLeft = new Text("Attempts: " + game.animaAttempt);
		Text fruitLeft = new Text("Attempts: " + game.fruitsAttempt);
		Text countryLeft = new Text("Attempts: " + game.countryAttempt);

		animalLeft.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		fruitLeft.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		countryLeft.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		HBox attemptLeft = new HBox(105, animalLeft, fruitLeft, countryLeft);

		if (game.animaAttempt == 0 && game.fruitsAttempt == 0 && game.countryAttempt == 0) {
			Label winGame = new Label("CONGRATULATIONS, YOU WON!!");
			winGame.setStyle("-fx-font-family: 'Times New Roman'; " +
					"-fx-font-size: 30; " +
					"-fx-font-weight: bold;");
			winGame.setAlignment(Pos.CENTER);
			this.categoryPane.setBottom(winGame);
		}

		VBox centerVbox = new VBox(10, categoryName, attemptLeft);
		attemptLeft.setTranslateX(10);
		BorderPane.setMargin(centerVbox, new Insets(200, 0, 130, 70));
		this.categoryPane.setCenter(centerVbox);

		this.game.category = new Category();
		Animal.setOnAction(e -> {
			game.category.setCategoryName("ANIMALS");
			game.flag = "Select request category";
			clientConnection.send(game);
		});

		Fruit.setOnAction(e -> {
			game.category.setCategoryName("FRUITS");
			game.flag = "Select request category";
			clientConnection.send(game);
		});

		Country.setOnAction(e -> {
			game.category.setCategoryName("COUNTRIES");
			game.flag = "Select request category";
			clientConnection.send(game);
		});

		return new Scene(this.categoryPane, 700, 700);
	}


	public Scene thirdScene(Stage primaryStage) {
		guessWordPane = new BorderPane();
		this.guessWordPane.setTop(menuBar(primaryStage));

		Label categoryLabel = new Label(game.category.getCategoryName());
		categoryLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		categoryLabel.setAlignment(Pos.CENTER);

		// Reset the word display labels for a new game
		wordDisplayLabels.clear();

		wordDisplayBox.setAlignment(Pos.CENTER);

		// Total attempts label
		totalAttemptsLabel = new Label("Total Attempts: #" + game.remainingGuesses);
		totalAttemptsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		totalAttemptsLabel.setAlignment(Pos.CENTER);

		// Alphabet buttons
		FlowPane alphabetButtonsPane = new FlowPane(5, 5); // FlowPane for alphabet buttons
		alphabetButtonsPane.setAlignment(Pos.CENTER);
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		for (int i = 0; i < alphabet.length(); i++) {
			final char letter = alphabet.charAt(i);
			Button letterButton = new Button(String.valueOf(letter));
			buttonStyle(letterButton);

			letterButton.setOnAction(e -> {
				game.flag = "Select request user guess";
				game.guess = letter;
				clientConnection.send(game);
				letterButton.setDisable(true);
			});
			alphabetButtonsPane.getChildren().add(letterButton);
		}

		VBox gameBox = new VBox(30, categoryLabel, wordDisplayBox, alphabetButtonsPane, totalAttemptsLabel);
		gameBox.setAlignment(Pos.CENTER);
		gameBox.setPadding(new Insets(20));

		this.guessWordPane.setCenter(gameBox);
		return new Scene(this.guessWordPane, 700, 700);
	}


	public Scene fourthScene(Stage primaryStage) {
		displayWinPane = new BorderPane();

		Label gameLose = new Label("YOU LOST");
		gameLose.setStyle("-fx-font-family: 'Times New Roman'; " +
				"-fx-font-size: 70; " +
				"-fx-font-weight: bold;");

		Label gameWin = new Label("YOU WIN!");
		gameWin.setStyle("-fx-font-family: 'Times New Roman'; " +
				"-fx-font-size: 70; " +
				"-fx-font-weight: bold;");

		game.flag = "Select request word";
		clientConnection.send(game);

		tryAgain = new Button("Try Again");
		newCategory = new Button("New Category");
		exit = new Button("Exit");

		tryAgain.setPrefSize(180, 70);
		newCategory.setPrefSize(180, 70);
		exit.setPrefSize(180, 70);
		buttonStyle(tryAgain);
		buttonStyle(newCategory);
		buttonStyle(exit);

		game.flag = "Select request game over";
		clientConnection.send(game);

		game.flag = "Select request attempt left";
		clientConnection.send(game);

		System.out.println("animal: " + game.animaAttempt);
		if (game.isGameWon) {
			HBox winnerHbox = new HBox(50, newCategory, exit);
			winnerHbox.setAlignment(Pos.CENTER);
			winnerHbox.setPadding(new Insets(20));

			VBox winnerScene = new VBox(20, gameWin, winnerHbox);
			winnerScene.setAlignment(Pos.CENTER);
			this.displayWinPane.setCenter(winnerScene);
		}
		else if (game.isGameLoss) {
			//todo the attempt is not going back to 6

			HBox loserHbox = new HBox(30, tryAgain, newCategory, exit);
			loserHbox.setAlignment(Pos.CENTER);
			loserHbox.setPadding(new Insets(20));


			VBox loserScene = new VBox(20, gameLose, loserHbox);
			loserScene.setAlignment(Pos.CENTER);
			this.displayWinPane.setCenter(loserScene);
		}

		tryAgain.setOnAction(e -> {
			String cat = game.category.getCategoryName();
			game.isReplay = true;
			game.flag = "Select request replay";
			clientConnection.send(game);
			game.category.setCategoryName(cat);
			game.flag = "Select request category";
			clientConnection.send(game);
		});

		newCategory.setOnAction(e -> {
			game.isReplay = true;
			game.flag = "Select request replay";
			clientConnection.send(game);
			primaryStage.setScene(secondScene(primaryStage));
		});

		exit.setOnAction(e -> {
			game.flag = "Select request exit";
			clientConnection.send(game);
			Platform.exit();
		});

		return new Scene(this.displayWinPane, 700, 700);
	}

	public Scene fifthScene(Stage primaryStage) {
		exitPane = new BorderPane();

		Label gameLose = new Label("YOU LOSE");
		gameLose.setStyle("-fx-font-family: 'Times New Roman'; " +
				"-fx-font-size: 70; " +
				"-fx-font-weight: bold;");

		Button exitGame = new Button("Exit");
		exitGame.setPrefSize(180, 100);
		buttonStyle(exitGame);

		VBox GameOver = new VBox(40, gameLose, exitGame);
		GameOver.setAlignment(Pos.CENTER);
		this.exitPane.setCenter(GameOver);

		exitGame.setOnAction(e -> {
			game.flag = "Select request exit";
			clientConnection.send(game);
			Platform.exit();
		});

		return new Scene(this.exitPane, 700, 700);
	}

	public Scene createRulesScene(Stage primaryStage) {
		rulePane = new BorderPane();
		BorderPane rulesPane = new BorderPane();
		rulesPane.setTop(menuBar(primaryStage)); // Keep the menu bar

		// Title for the rules
		Label title = new Label("Game Rules");
		title.setStyle("-fx-font-family: " +
				"'Times New Roman'; " +
				"-fx-font-size: 50; " +
				"-fx-text-fill: #DAA520; " +
				"-fx-font-weight: bold");

		//title.setFont(new Font("Arial", 24));
		title.setPadding(new Insets(10, 0, 20, 0));
		title.setAlignment(Pos.CENTER);


		// Rules
		String rulesText = "Welcome to Hangman!\n\n" +
				"Objective: Guess a word in each of the three categories to win.\n\n" +
				"1. Start with a Category: Choose one of three categories. A word from that category will be chosen for you to guess.\n\n" +
				"2. Guess the Letters: You get six chances to guess the letters in the word. Correct guesses don’t count against your total.\n\n" +
				"3. Feedback on Your Guess: The server will inform you if your guess is correct or not, and how many guesses you have left.\n\n" +
				"4. Guessing the Word: Successfully guess the word to proceed to the next category. You can’t guess another word in the same category.\n\n" +
				"5. Missed Guesses: If you don’t guess the word, you can try again in any category. Three attempts per category are allowed.\n\n" +
				"6. End of the Game: The game ends when you guess a word in each category or exhaust your three attempts in any category. You can then play again or exit.";

		TextArea rulesArea = new TextArea(rulesText);
		rulesArea.setEditable(false); // Make the text area non-editable
		rulesArea.setWrapText(true); // Enable text wrapping
		rulesArea.setFont(new Font("Times New Roman", 16));
		rulesArea.setPadding(new Insets(10));

		// Go Back Button
		Button goBackButton = new Button("Go Back");
		buttonStyle(goBackButton); // Apply the existing button style
		//goBackButton.setOnAction(e -> primaryStage.setScene(sceneMap.get("client"))); // Set the scene to the first scene

		goBackButton.setOnAction(e -> {
			if (previousScene != null) {
				primaryStage.setScene(previousScene); // Go back to the previous scene
			}
		});


		// Layout for rules and button
		VBox vbox = new VBox(10); // Spacing of 10 between elements
		vbox.getChildren().addAll(title, rulesArea, goBackButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(10));
		rulesPane.setCenter(vbox);

		return new Scene(rulesPane, 700, 700);
	}

	public static void buttonStyle(Button buttonStyle) {
		buttonStyle.setStyle(
				"-fx-font-family: 'Arial'; " +
						"-fx-font-size: 20; " +
						"-fx-text-fill: #FFFFFF; " +
						"-fx-font-weight: bold; " +
						"-fx-background-color: #B8860B; " +
						"-fx-border-radius: 10; " +
						"-fx-background-radius: 10;"
		);
		buttonStyle.setOnMouseEntered(e -> buttonStyle.setStyle(
				"-fx-font-family: 'Arial'; " +
						"-fx-background-color: #DAA520; " +
						"-fx-font-size: 20; " +
						"-fx-text-fill: #FFFFFF; " +
						"-fx-font-weight: bold; " +
						"-fx-border-radius: 10; " +
						"-fx-background-radius: 10;"
		));

		buttonStyle.setOnMouseExited(e -> buttonStyle.setStyle(
				"-fx-font-family: 'Arial'; " +
						"-fx-background-color: #B8860B; " +
						"-fx-font-size: 20; " +
						"-fx-text-fill: #FFFFFF; " +
						"-fx-font-weight: bold; " +
						"-fx-border-radius: 10; " +
						"-fx-background-radius: 10;"
		));
		buttonStyle.setOnMousePressed(e -> buttonStyle.setStyle(
				"-fx-font-family: 'Arial'; " +
						"-fx-background-color: #8B6914; " +
						"-fx-font-size: 20; " +
						"-fx-text-fill: #FFFFFF; " +
						"-fx-font-weight: bold; " +
						"-fx-border-radius: 10; " +
						"-fx-background-radius: 10;"
		));
		buttonStyle.setOnMouseReleased(e -> buttonStyle.setStyle(
				"-fx-font-family: 'Arial'; " +
						"-fx-background-color: #B8860B; " +
						"-fx-font-size: 20; " +
						"-fx-text-fill: #FFFFFF; " +
						"-fx-font-weight: bold; " +
						"-fx-border-radius: 10; " +
						"-fx-background-radius: 10;"
		));
	}

	private void handleCloseRequest(WindowEvent event) {
		// Ensure to close any network connections or other resources here
		Platform.exit();
		System.exit(0);
	}

	// Add this method to update the GUI
	public void update(String message) {
		Platform.runLater(() -> {
			listItems2.getItems().add(message);
		});
	}


}

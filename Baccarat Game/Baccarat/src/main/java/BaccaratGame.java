/**********************************************************
 * Program 2: Baccarat

 Course: CS 342, Fall 2023.
 Author: Yashwi S & Maria B

 Project Description:
  This program simulates the popular casino game Baccarat. In this one-player version,
  the user places a bet on either The Banker, The Player, or a Draw. Two cards are dealt
  to both The Banker and The Player. The objective is to have a hand total closest to 9.

  Card values:
  - 10’s and face cards: 0 points
  - Aces: 1 point
  - Other cards: Face value

  If the total exceeds 9, the first digit is dropped. For instance, a 9 and 6 (totaling 15)
  would count as 5.

  Gameplay summary:
  1. The user places a bet.
  2. Two cards are dealt to both The Player and The Banker.
  3. If either hand totals 8 or 9, it's a "natural" win.
  4. If not, The Player may draw a third card under certain conditions.
  5. The Banker can also draw a third card based on specific rules.
  6. The hand closest to 9 wins.

 **********************************************************/


import javafx.application.Application;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class BaccaratGame extends Application {
	// Declared public and private variables
	public ArrayList<Card> playerHand;
	public ArrayList<Card> bankerHand;
	public BaccaratDealer theDealer = new BaccaratDealer();
	public BaccaratGameLogic gameLogic = new BaccaratGameLogic();
	public String userBet;
	public double currentBet;
	public double totalWinnings;
	private boolean dealPressed = false;
	private boolean freshStartPressed = false;

	// set up getters and setters for private variables
	public String getUserBet() { return userBet; }

	public double getCurrentBet() {
		return currentBet;
	}

	public double getTotalWinnings() {
		return totalWinnings;
	}

	public void setUserBet(String userBet) {
		this.userBet = userBet;
	}

	public void setCurrentBet(double currentBet) {
		this.currentBet = currentBet;
	}

	public void setTotalWinnings(double totalWinnings) {
		this.totalWinnings = totalWinnings;
	}

	// Evaluate who won and calculate the totalwinning
	public double evaluateWinnings() {
		String gameResult = gameLogic.whoWon(playerHand, bankerHand);		// Determine the game result

		// If the game result favors the "Player" add to totalwinning if won
		// otherwise subtract their bet amount
		if (gameResult.equals("Player")) {
			if (getUserBet().equals("Player")) {
				setTotalWinnings(getTotalWinnings() + getCurrentBet());
				return getCurrentBet();
			}
			else {
				setTotalWinnings(getTotalWinnings() - getCurrentBet());
				return -getCurrentBet();
			}
		}
		// If the game result favors the "banker", calculate the wining amount to totalwinning if won
		// otherwise subtract their bet amount
		else if (gameResult.equals("Banker")) {
			if (getUserBet().equals("Banker")) {
				double winnings = getCurrentBet() - (getCurrentBet() * 0.05); // Deducting 5% commission
				setTotalWinnings(getTotalWinnings() + winnings);
				return getCurrentBet() * 0.95;
			}
			else {
				setTotalWinnings(getTotalWinnings() - getCurrentBet());
				return -getCurrentBet();
			}
		}
		// If the game result favors the "draw", calculate the wining amount to totalwinning if won
		// otherwise subtract their bet amount
		else if (gameResult.equals("Draw")) {
			if (getUserBet().equals("Draw")) {
				setTotalWinnings(getTotalWinnings() + (getCurrentBet() * 8)); // 8:1 payout
				return getCurrentBet()*8;
			}
			else {
				setTotalWinnings(getTotalWinnings() - getCurrentBet());
				return -getCurrentBet();
			}
		}
		return -currentBet;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		primaryStage.setTitle("Welcome to Baccarat");
		BorderPane pane = new BorderPane();
		BorderPane pane1 = new BorderPane();
		BorderPane pane2 = new BorderPane();

		Scene scene1 = new Scene(pane, 700,700);
		Scene scene2 = new Scene(pane1, 800,700);
		Scene scene3 = new Scene(pane2, 800,700);

		//FIRST SCREEN - WELCOME PAGE
		Label welcomeText = new Label("WELCOME TO");
		Label baccaratTextWelcome = new Label("BACCARAT");
		Button playButton = new Button("PLAY");

		//WINNING LABEL
		Label totalWinningsLabel = new Label("Total Winnings");
		totalWinningsLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 10; -fx-text-fill: #FFFFFF; -fx-font-weight: bold;");
		TextField winningsRectangle = new TextField("");
		winningsRectangle.setMaxWidth(80);
		winningsRectangle.setEditable(false);
		Pane winningsPane = new Pane();
		VBox verticalWinning = new VBox(totalWinningsLabel, winningsRectangle);
		winningsPane.getChildren().addAll(verticalWinning);
		winningsPane.setLayoutX(200);
		winningsPane.setLayoutY(620);

		// Add this after initializing your pane
		Image backgroundImage = new Image("greenBackground.png");
		BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		pane.setBackground(new Background(background));

		VBox text1 = new VBox(welcomeText, baccaratTextWelcome, playButton);
		text1.setAlignment(Pos.CENTER);

		// Styling the welcomeText
		welcomeText.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 35; -fx-text-fill: #DAA520; -fx-font-weight: bold");

		// Styling the baccaratTextWelcome
		baccaratTextWelcome.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 35; -fx-text-fill: #DAA520; -fx-font-weight: bold");

		//Image backgroundImage = new Image("greenBackground.png"); // Replace with the correct path

		// Create a BackgroundImage object
		BackgroundImage backgroundPane1 = new BackgroundImage(
				backgroundImage,
				BackgroundRepeat.REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT
		);

		// Setting the background of the pane1 to the green background image
		pane1.setBackground(new Background(backgroundPane1));
		ImagePattern imagePattern = new ImagePattern(backgroundImage);

		// MENU BAR (FRESH START, EXIT)
		MenuBar menu = new MenuBar();
		Menu menuOption = new Menu("Options");
		MenuItem freshStart_menu = new MenuItem("Fresh Start");
		MenuItem exitMenu = new MenuItem ("Exit");
		menuOption.getItems().addAll(freshStart_menu, exitMenu);
		menu.getMenus().add(menuOption);

		playButtonsFun(playButton);

		playButton.setPrefSize(150, 50);
		text1.setSpacing(10);
		pane.setTop(menu);
		pane.setCenter(text1);

		primaryStage.setScene(scene1);
		primaryStage.show();

		// EVALUATE THE WIN
		/////////////////////////////////////////////////////////////////////////////////

		Rectangle displayWin = new Rectangle(550.0f, 260.0f, imagePattern);
		displayWin.setX(130.0f);
		displayWin.setY(280.0f);
		displayWin.setArcWidth(20.0);
		displayWin.setArcHeight(20.0);

		Button continueGameButton = new Button("CONTINUE");
		playButtonsFun(continueGameButton);

		Button exitButton = new Button("EXIT");
		playButtonsFun(exitButton);

		HBox gameEndOptions = new HBox(40, continueGameButton, exitButton);
		continueGameButton.setMinSize(200, 50);
		exitButton.setMinSize(200, 50);
		gameEndOptions.setLayoutX(200);
		gameEndOptions.setLayoutY(440);

		// CREATE TEXT TO DISPLAY GAME ENDED MESSAGE
		Text gameEndMessage = new Text("GAME ENDED");
		gameEndMessage.setStyle("-fx-font-family: Times New Roman; -fx-font-size: 30; -fx-font-weight: bold;");
		gameEndMessage.setX(320.0f);
		gameEndMessage.setY(410.0f);
		gameEndMessage.setFill(Color.WHITESMOKE);

		// CREATE TEXT FOR USER WIN, DISPLAYS WHEN USER WON
		Text gameWin = new Text("YOU WIN!");
		gameWin.setStyle("-fx-font-family: Times New Roman; -fx-font-size: 50; -fx-font-weight: bold;");
		gameWin.setX(300.0f);
		gameWin.setY(360.0f);
		gameWin.setFill(Color.WHITESMOKE);

		// CREATE TEXT FOR USER LOSE, DISPLAYS WHEN USER LOSE
		Text gameLose = new Text("YOU LOSE");
		gameLose.setStyle("-fx-font-family: Times New Roman; -fx-font-size: 50; -fx-font-weight: bold;");
		gameLose.setX(290.0f);
		gameLose.setY(360.0f);
		gameLose.setFill(Color.WHITESMOKE);

		// CREATE TEXT FOR USER NATURAL WIN, DISPLAYS WHEN GAME DRAW
		Text gameTie = new Text("NATURAL WIN!");
		gameTie.setStyle("-fx-font-family: Times New Roman; -fx-font-size: 50; -fx-font-weight: bold;");
		gameTie.setX(220.0f);
		gameTie.setY(360.0f);
		gameTie.setFill(Color.WHITESMOKE);

		// EXIT THE GAME WHEN USER SELECT EXIT
		exitButton.setOnAction(e-> {
			Platform.exit();
		});


		/////////////////////////////////////////////////////////////////////////////////


		 // scene two
		 Rectangle middleSquare = new Rectangle(40, 60, 730, 550);

		 // Setting the fill of the middleSquare to the created ImagePattern
		 middleSquare.setFill(imagePattern);

		 middleSquare.setStroke(Color.BLACK);
		 middleSquare.setStrokeWidth(2);
		 pane1.setTop(menu);
		 pane1.setCenter(middleSquare);

		 Rectangle rectangleOfActions = outterRec();
		 pane1.setTop(menu);
		 pane1.getChildren().add(rectangleOfActions);
		 pane1.getChildren().add(winningsPane);		//to make sure it shows on top of the red rectangle (outterRec)

		 // CREATE SMALL RECTANGLE ON THE TOP FOR GAME TITLE
		 Rectangle baccaratTopNameGame = new Rectangle();
		 baccaratTopNameGame.setX(345.0f);
		 baccaratTopNameGame.setY(45.0f);
		 baccaratTopNameGame.setWidth(105.0f);
		 baccaratTopNameGame.setHeight(30.0f);
		 baccaratTopNameGame.setFill(Color.DARKOLIVEGREEN);
		 baccaratTopNameGame.setStroke(Color.BLACK);
		 baccaratTopNameGame.setArcWidth(20.0);
		 baccaratTopNameGame.setArcHeight(20.0);
		 baccaratTopNameGame.setStrokeWidth(2);
		 pane1.setTop(menu);
		 pane1.getChildren().add(baccaratTopNameGame);

		// CREATE SMALL TEXT OF GAME TITLE TO DISPLAY ON THE RECTANGLE
		 Text baccaratTopText = new Text("BACCARAT");
		 baccaratTopText.setStyle("-fx-font-family: Times New Roman; -fx-font-size: 16; -fx-font-weight: bold;");
		 baccaratTopText.setX(356.0f);
		 baccaratTopText.setY(64.0f);
		 baccaratTopText.setFill(Color.WHITESMOKE);
		 pane1.getChildren().add(baccaratTopText);

		 Line lineBetweenCards = new Line(400, 100, 400, 370);
		 lineBetweenCards.setStroke(Color.BLACK);
		 lineBetweenCards.setStrokeWidth(2);
		 pane1.getChildren().add(lineBetweenCards);

		 // CREATE BUTTONS FOR USER OPTION FOR PLAYER, BANKER, AND DRAW
		 Button playerBet = new Button("PLAYER");
		 Button bankerBet = new Button("BANKER");
		 Button DrawTieBet = new Button("DRAW");

		 //When one button is selected, it turns green and the rest turns white.
		 // disable the other two option
		 playerBet.setOnAction(e-> {
		 	 playerBet.setStyle("-fx-background-color: GREEN;");
		 	 bankerBet.setStyle("-fx-background-color: WHITE;");
		 	 DrawTieBet.setStyle("-fx-background-color: WHITE;");
			 bankerBet.setDisable(true);
			 DrawTieBet.setDisable(true);
		 	 setUserBet("Player");

		 });
		 bankerBet.setOnAction(e-> {
		 	 playerBet.setStyle("-fx-background-color: WHITE;");
		 	 bankerBet.setStyle("-fx-background-color: GREEN;");
		 	 DrawTieBet.setStyle("-fx-background-color: WHITE;");
			 playerBet.setDisable(true);
			 DrawTieBet.setDisable(true);
			 setUserBet("Banker");
		 });
		 DrawTieBet.setOnAction(e-> {
		 	 playerBet.setStyle("-fx-background-color: WHITE;");
		 	 bankerBet.setStyle("-fx-background-color: WHITE;");
		 	 DrawTieBet.setStyle("-fx-background-color: GREEN;");
			 playerBet.setDisable(true);
			 bankerBet.setDisable(true);
			 setUserBet("Draw");
		 });

		 // SET THE USER OPTION INTO A HBOX
		 HBox text2 = new HBox(20, playerBet, bankerBet, DrawTieBet);
		 playerBet.setMinSize(90, 70);
		 bankerBet.setMinSize(90, 70);
		 DrawTieBet.setMinSize(90, 70);
		 text2.setLayoutX(110);
		 text2.setLayoutY(420);
		 pane1.getChildren().add(text2);

		// CREATE BUTTONS FOR DEAL AND CLEAR, AND ADD THEM TO HBOX
		 Button selectDeal = new Button("DEAL");
		 Button selectClear = new Button("CLEAR");
		 HBox dealClear = new HBox(15, selectDeal, selectClear);
		 selectDeal.setMinSize(90, 70);
		 selectClear.setMinSize(90, 70);

		 dealClear.setLayoutX(500);
		 dealClear.setLayoutY(420);
		 pane1.getChildren().add(dealClear);

		 // CREATE BUTTONS FOR CHIPS AND ADD THEM TO HBOX
		 Button chip1 = new Button("$1");
		 Button chip5= new Button("$5");
		 Button chip25 = new Button("$25");
		 Button chip100 = new Button("$100");
		 Button chip500= new Button("$500");
		 HBox money = new HBox(30, chip1, chip5, chip25, chip100, chip500);

		//creating the total of bet
		TextField totalBet = new TextField();
		totalBet.setPrefWidth(110.0f);
		totalBet.setPrefHeight(40.0f);

		Pane stack = new Pane();
		stack.getChildren().addAll(totalBet);
		stack.setLayoutX(600);
		stack.setLayoutY(550);
		stack.setMinWidth(100);
		stack.setMinHeight(50);
		pane1.getChildren().add(stack);


		// Define a common style for the chips
		String normalStyle = "-fx-background-radius: 5em; " +
				"-fx-min-width: 50px; " +
				"-fx-min-height: 50px; " +
				"-fx-max-width: 50px; " +
				"-fx-max-height: 50px; " +
				"-fx-background-color: WHITE;" +
				"-fx-background-insets: 0px; " +
				"-fx-padding: 0px;";

		String pressedStyle = "-fx-background-radius: 5em; " +
				"-fx-min-width: 50px; " +
				"-fx-min-height: 50px; " +
				"-fx-max-width: 50px; " +
				"-fx-max-height: 50px; " +
				"-fx-background-color: DARKGREY;" +
				"-fx-background-insets: 0px; " +
				"-fx-padding: 0px;";

		// Apply styles and set events
		for (Node chip : Arrays.asList(chip1, chip5, chip25, chip100, chip500)) {
			chip.setStyle(normalStyle);

			chip.setOnMousePressed(e -> chip.setStyle(pressedStyle));
			chip.setOnMouseReleased(e -> chip.setStyle(normalStyle));
		}

		// Actions for each chips and add that amount to the totalBet
		chip1.setOnAction(e -> {
			setCurrentBet(getCurrentBet() + 1);
			totalBet.setText(String.valueOf(getCurrentBet()));
		});

		chip5.setOnAction(e -> {
			setCurrentBet(getCurrentBet() + 5);
			totalBet.setText(String.valueOf(getCurrentBet()));
		});

		chip25.setOnAction(e -> {
			setCurrentBet(getCurrentBet() + 25);
			totalBet.setText(String.valueOf(getCurrentBet()));
		});

		chip100.setOnAction(e -> {
			setCurrentBet(getCurrentBet() + 100);
			totalBet.setText(String.valueOf(getCurrentBet()));
		});

		chip500.setOnAction(e -> {
			setCurrentBet(getCurrentBet() + 500);
			totalBet.setText(String.valueOf(getCurrentBet()));
		});

		 money.setLayoutX(170);
		 money.setLayoutY(540);
		 pane1.getChildren().add(money);

		 TextField display = new TextField("MONEY");
		 display.setLayoutY(540);
		 display.setLayoutX(500);
		 //pane1.getChildren().add(display);


		//PLACEHOLDER RECTANGLE FOR THE PLAYER CARDS
		Rectangle playerCard1 = createCardPlaceholder();
		Rectangle playerCard2 = createCardPlaceholder();
		Rectangle playerCard3 = createCardPlaceholder();

		//PLACEHOLDER RECTANGLE FOR THE BANKER CARDS
		Rectangle bankerCard1 = createCardPlaceholder();
		Rectangle bankerCard2 = createCardPlaceholder();
		Rectangle bankerCard3 = createCardPlaceholder();

		//RECTANGLE TO DISPLAY THE TOTAL FOR THE CARD ON THE TOP
		Rectangle bankerTotal = displayCardTotal();
		Rectangle playerTotal = displayCardTotal();

		// DISPLAY THE TEXT(TOTAL) ON THE ABOVE RECTANGLE
		Text bankerText = new Text("0");
		bankerText.setFill(Color.WHITE);
		bankerText.setLayoutX(575);
		bankerText.setLayoutY(135);
		bankerText.setFont(Font.font(30));

		Text playerText = new Text("0");
		playerText.setFill(Color.WHITE);
		playerText.setLayoutX(223);
		playerText.setLayoutY(135);
		playerText.setFont(Font.font(30));

		//ORGANIZING THE CARDS TOTALDISPLAY IN A HBOX
		HBox displayTotal = new HBox(10, bankerTotal, playerTotal);
		displayTotal.setLayoutX(180);
		displayTotal.setLayoutY(100);
		displayTotal.setSpacing(250);
		pane1.getChildren().add(displayTotal);
		pane1.getChildren().add(bankerText);
		pane1.getChildren().add(playerText);

		//ORGANIZING THE CARDS PLACEHOLDER IN A HBOX
		HBox playerCardsHorizontal = new HBox(10, playerCard1, playerCard2, playerCard3);
		playerCardsHorizontal.setLayoutX(60);
		playerCardsHorizontal.setLayoutY(180);
		pane1.getChildren().add(playerCardsHorizontal);

		HBox bankerCardsHorizontal = new HBox(10, bankerCard1, bankerCard2, bankerCard3);
		bankerCardsHorizontal.setLayoutX(420);
		bankerCardsHorizontal.setLayoutY(180);
		pane1.getChildren().add(bankerCardsHorizontal);

		//Action for the Clear button -> it will only work if Deal button wasn't previously pressed.
		selectClear.setOnAction(e-> {
			if (!dealPressed || freshStartPressed) {
				playerBet.setStyle("-fx-background-color: WHITE;");
				bankerBet.setStyle("-fx-background-color: WHITE;");
				DrawTieBet.setStyle("-fx-background-color: WHITE;");
				bankerBet.setDisable(false);
				playerBet.setDisable(false);
				DrawTieBet.setDisable(false);
				setCurrentBet(0);
				totalBet.setText(String.valueOf(getCurrentBet()));
			}
		});

		selectDeal.setOnAction(e-> {
			playerHand = theDealer.dealHand();
			bankerHand = theDealer.dealHand();

			dealPressed = true;
			if(currentBet != 0 && userBet != null) {
				// SET UP THE CARD IMAGES FOR PLAYER
				Image playerImage1 = new Image(playerHand.get(0).getSuite() + playerHand.get(0).getValue() + ".png");
				Image playerImage2 = new Image(playerHand.get(1).getSuite() + playerHand.get(1).getValue() + ".png");

				Card playerCardX = null;
				Image playerImage3 = null;

				//if there is a need for a third card, display 3rd card for player
				if (gameLogic.evaluatePlayerDraw(playerHand)) {
					playerCardX = theDealer.drawOne();
					playerHand.add(playerCardX);
					playerImage3 = new Image(playerHand.get(2).getSuite() + playerHand.get(2).getValue() + ".png");
					playerCard3.setFill(new ImagePattern(playerImage3));

				}

				// SET UP THE CARD IMAGES FOR BANKER
				Image bankerImage1 = new Image(bankerHand.get(0).getSuite() + bankerHand.get(0).getValue() + ".png");
				Image bankerImage2 = new Image(bankerHand.get(1).getSuite() + bankerHand.get(1).getValue() + ".png");

				Image bankerImage3 = null;

				//if there is a need for a third card, display 3rd card for banker
				if(gameLogic.evaluateBankerDraw(bankerHand,playerCardX)){
					bankerHand.add(theDealer.drawOne());
					bankerImage3 = new Image(bankerHand.get(2).getSuite() + bankerHand.get(2).getValue() + ".png");
					bankerCard3.setFill(new ImagePattern(bankerImage3));
				}

				// SET UP THE CARDS FOR PLAYER AND BANKER
				playerCard1.setFill(new ImagePattern(playerImage1));
				playerCard2.setFill(new ImagePattern(playerImage2));

				bankerCard1.setFill(new ImagePattern(bankerImage1));
				bankerCard2.setFill(new ImagePattern(bankerImage2));

				// disable the chips to avoid add money after cards turn over
				chip1.setDisable(true);
				chip5.setDisable(true);
				chip25.setDisable(true);
				chip100.setDisable(true);
				chip500.setDisable(true);

				// DISPLAY THE SCORES
				bankerText.setText("" + gameLogic.handTotal(bankerHand));
				playerText.setText("" + gameLogic.handTotal(playerHand));

				// ADD LOGIC TO DISPLAY SCENE FOR WIN
				pane2.getChildren().clear();
				pane2.getChildren().add(pane1);
				pane2.getChildren().add(displayWin);
				pane2.getChildren().add(gameEndOptions);
				pane2.getChildren().add(gameEndMessage);
				menuOption.setDisable(true);

				// EVALUATE THE WIN AND DISPLAY THE MESSAGE ACCORDINGLY
				if (gameLogic.whoWon(playerHand, bankerHand).equals(userBet) && !userBet.equals("Draw")) {
					pane2.getChildren().add(gameWin);
				}
				else if (!gameLogic.whoWon(playerHand, bankerHand).equals(userBet)) {
					pane2.getChildren().add(gameLose);
				}
				else if (gameLogic.whoWon(playerHand, bankerHand).equals(userBet)) {
					pane2.getChildren().add(gameTie);
				}

				// SET PRIMARY STATE TO SCENE3 AND DISPLAY SCENE3
				primaryStage.setScene(scene3);
				primaryStage.show();

				evaluateWinnings();
				winningsRectangle.setText(String.valueOf(totalWinnings));
			}
		});

		// DISPLAY SCENE TWO WHEN USE CLICK ON PLAY
		playButton.setOnAction(e-> {
			primaryStage.setScene(scene2);
			primaryStage.show();
		});

		exitMenu.setOnAction(e-> {
			Platform.exit();
		});

		// SET EVERYTHING TO THEIR DEFAULT
		continueGameButton.setOnAction(e -> {
			setUserBet(null);

			// CLEAR THE PANE
			pane2.getChildren().clear();
			pane2.getChildren().add(pane1);
			dealPressed = false;

			// SET TO DEFAULT VALUE
			totalBet.setText("");
			setUserBet("");
			bankerText.setText("0");
			playerText.setText("0");

			// CLEAR THE CARDS
			playerCard1.setFill(Color.BLACK);
			playerCard2.setFill(Color.BLACK);
			playerCard3.setFill(Color.BLACK);

			bankerCard1.setFill(Color.BLACK);
			bankerCard2.setFill(Color.BLACK);
			bankerCard3.setFill(Color.BLACK);

			// REVERT EH BACKGROUND TO DEFAULT
			playerBet.setStyle("-fx-background-color: WHITE;");
			bankerBet.setStyle("-fx-background-color: WHITE;");
			DrawTieBet.setStyle("-fx-background-color: WHITE;");

			// ENABLE THE BUTTON FOR USER TO SELECT THE CHOICE
			playerBet.setDisable(false);
			bankerBet.setDisable(false);
			DrawTieBet.setDisable(false);

			menuOption.setDisable(false);
			chip1.setDisable(false);
			chip5.setDisable(false);
			chip25.setDisable(false);
			chip100.setDisable(false);
			chip500.setDisable(false);

			setCurrentBet(0);
			totalBet.setText(String.valueOf(getCurrentBet()));
		});

		// SET EVERYTHING TO THEIR DEFAULT
		freshStart_menu.setOnAction(e-> {
			freshStartPressed = true;
			totalWinnings = 0;

			// SET TO DEFAULT VALUE
			winningsRectangle.setText("0.0");
			theDealer = new BaccaratDealer();	//re-setting the Dealer
			setTotalWinnings(0);
			setUserBet("");

			bankerText.setText("0");
			playerText.setText("0");

			// CLEAR THE CARDS
			playerCard1.setFill(null);
			playerCard2.setFill(null);
			playerCard3.setFill(null);

			bankerCard1.setFill(null);
			bankerCard2.setFill(null);
			bankerCard3.setFill(null);

			// REVERT EH BACKGROUND TO DEFAULT
			playerBet.setStyle("-fx-background-color: WHITE;");
			bankerBet.setStyle("-fx-background-color: WHITE;");
			DrawTieBet.setStyle("-fx-background-color: WHITE;");

			// ENABLE THE BUTTON FOR USER TO SELECT THE CHOICE
			playerBet.setDisable(false);
			bankerBet.setDisable(false);
			DrawTieBet.setDisable(false);

			chip1.setDisable(false);
			chip5.setDisable(false);
			chip25.setDisable(false);
			chip100.setDisable(false);
			chip500.setDisable(false);

			setCurrentBet(0);
			totalBet.setText(String.valueOf(getCurrentBet()));

			primaryStage.show();

		});

	}

	// set up style for the buttons
	public static void playButtonsFun(Button playButton) {
		playButton.setStyle(
				"-fx-font-family: 'Arial'; " +
						"-fx-font-size: 20; " +
						"-fx-text-fill: #FFFFFF; " +
						"-fx-font-weight: bold; " +
						"-fx-background-color: #B8860B; " +
						"-fx-border-radius: 10; " +
						"-fx-background-radius: 10;"
		);
		playButton.setOnMouseEntered(e -> playButton.setStyle(
				"-fx-font-family: 'Arial'; " +
						"-fx-background-color: #DAA520; " +
						"-fx-font-size: 20; " +
						"-fx-text-fill: #FFFFFF; " +
						"-fx-font-weight: bold; " +
						"-fx-border-radius: 10; " +
						"-fx-background-radius: 10;"
		));

		playButton.setOnMouseExited(e -> playButton.setStyle(
				"-fx-font-family: 'Arial'; " +
						"-fx-background-color: #B8860B; " +
						"-fx-font-size: 20; " +
						"-fx-text-fill: #FFFFFF; " +
						"-fx-font-weight: bold; " +
						"-fx-border-radius: 10; " +
						"-fx-background-radius: 10;"
		));
		playButton.setOnMousePressed(e -> playButton.setStyle(
				"-fx-font-family: 'Arial'; " +
						"-fx-background-color: #8B6914; " +
						"-fx-font-size: 20; " +
						"-fx-text-fill: #FFFFFF; " +
						"-fx-font-weight: bold; " +
						"-fx-border-radius: 10; " +
						"-fx-background-radius: 10;"
		));
		playButton.setOnMouseReleased(e -> playButton.setStyle(
				"-fx-font-family: 'Arial'; " +
						"-fx-background-color: #B8860B; " +
						"-fx-font-size: 20; " +
						"-fx-text-fill: #FFFFFF; " +
						"-fx-font-weight: bold; " +
						"-fx-border-radius: 10; " +
						"-fx-background-radius: 10;"
		));
	}


	// Create a Rectangle object representing the placeholder for a card
	public Rectangle createCardPlaceholder() {
		Rectangle card = new Rectangle(100.0f, 170.0f, Color.BLACK);
		card.setStroke(Color.BLACK);		// Set the stroke color of the rectangle to black
		card.setStrokeWidth(2);
		card.setArcWidth(20.0);
		card.setArcHeight(20.0);
		return card;
	}

	// Create a Rectangle object representing the scores for banker and player
	public Rectangle displayCardTotal () {
		Rectangle card = new Rectangle(100.0f, 50.0f, Color.DARKGREEN);
		card.setStroke(Color.BLACK);
		card.setStrokeWidth(2);
		card.setArcWidth(5.0);
		card.setArcHeight(5.0);
		return card;
	}

	// Create a Rectangle object representing the bottom rectangle that contains user options
	public Rectangle outterRec () {
		Rectangle rectangleOfActions = new Rectangle(650.0f, 260.0f, Color.DARKRED);
		rectangleOfActions.setX(80.0f);
		rectangleOfActions.setY(400.0f);
		rectangleOfActions.setFill(Color.DARKRED);
		rectangleOfActions.setArcWidth(20.0);
		rectangleOfActions.setArcHeight(20.0);
		return rectangleOfActions;
	}


}

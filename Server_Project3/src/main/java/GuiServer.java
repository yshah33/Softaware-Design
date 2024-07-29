
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application {

	TextField portTextField;
	Button b1;
	HashMap<String, Scene> sceneMap;
	Server serverConnection;
	ListView<String> listItems;


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Hangman Server GUI");

		Scene mainScene = createMainGui(primaryStage);
		primaryStage.setScene(mainScene);

		primaryStage.setOnCloseRequest(event -> {
			Platform.exit();
			System.exit(0);
		});
		primaryStage.show();
	}

	private Scene createMainGui(Stage primaryStage) {

		portTextField = new TextField();
		portTextField.setPromptText("Enter port Number");
		portTextField.setMaxWidth(80);

		b1 = new Button("Go to Server Screen");
		b1.setDisable(true);
		buttonStyle(b1);

		Label welcomeText = new Label("WELCOME TO");
		welcomeText.setStyle("-fx-font-family: " +
				"'Times New Roman'; " +
				"-fx-font-size: 40; " +
				"-fx-text-fill: #DAA520; " +
				"-fx-font-weight: bold");

		Label TextWelcome = new Label("SERVER SIDE");
		TextWelcome.setStyle("-fx-font-family: " +
				"'Times New Roman'; " +
				"-fx-font-size: 40; " +
				"-fx-text-fill: #DAA520; " +
				"-fx-font-weight: bold");


		portTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			b1.setDisable(!newValue.matches("\\d{5}")); // Enable b1 only for 5 digits
		});

		b1.setOnAction(e -> {
//			try {
//				int port = Integer.parseInt(portTextField.getText().trim()); // Get port number
//				primaryStage.setScene(createServerGui(port)); // Switch to server GUI with port
//			} catch (NumberFormatException ex) {
//				portTextField.setText("Invalid port!"); // Handle invalid port number
//			}

			try {
				String portString = portTextField.getText().trim();
				if (portString.isEmpty()) {
					throw new IllegalArgumentException("Port field is empty");
				}
				int portValue = Integer.parseInt(portString);
				if (portValue < 1024 || portValue > 65535) {
					throw new IllegalArgumentException("Port number must be between 1024 and 65535");
				}

				serverConnection = new Server(data -> {
					Platform.runLater(()->{
						Game game = (Game) data;
						listItems.getItems().add(game.flag);
					});
				}, portValue); // Initialize server connection with the valid port number
				primaryStage.setScene(createServerGui(portValue)); // Switch to server GUI with port

			} catch (IllegalArgumentException ex) {
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				errorAlert.initOwner(primaryStage);
				errorAlert.setHeaderText("Invalid Input");
				errorAlert.setContentText(ex.getMessage());
				errorAlert.showAndWait();
			}



//			serverConnection = new Server(data -> {
//					Platform.runLater(()->{
//						Game game = (Game) data;
//						listItems.getItems().add(game.flag);
//					});
//
//				}, Integer.parseInt(portTextField.getText())); // get the port from the text filed Integer.parseInt(s1.getText())

		});

		VBox vbox = new VBox(15, welcomeText, TextWelcome, portTextField, b1);
		VBox.setMargin(portTextField, new Insets(20, 0, 0, 0));
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20));

		return new Scene(vbox, 800, 800);
	}


	private Scene createServerGui(int port) {
		listItems = new ListView<>();


		Label logsLabel = new Label("LOG HISTORY: ");
		logsLabel.setStyle("-fx-font-family: " +
				"'Times New Roman'; " +
				"-fx-font-size: 35; " +
				"-fx-text-fill: #000000; " +
				"-fx-font-weight: bold");

		VBox vbox = new VBox(10, logsLabel, listItems);
		BorderPane pane = new BorderPane();
		vbox.setPadding(new Insets(20));
		pane.setStyle("-fx-background-color: #DAA520");
		pane.setCenter(vbox);

		return new Scene(pane, 800, 800);
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


}

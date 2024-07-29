# Hangman Game | Java, JavaFX GUI

## Overview

This Hangman Game is a full-stack word-guessing game developed using Java and JavaFX for the graphical user interface (GUI). The game supports multiple users with server-client communication via sockets, advanced game logic, and showcases precise object-oriented programming principles. The server maintains logs of user activities and facilitates real-time interaction between players.

## Features

- **JavaFX GUI**: An intuitive and user-friendly graphical interface for the game.
- **Multi-User Support**: Multiple users can connect to the server and play the game simultaneously.
- **Server-Client Architecture**: Implements communication between the client and server using sockets.
- **Advanced Game Logic**: Includes robust and efficient game logic for a seamless gaming experience.
- **Server Logs**: Maintains logs of user activities for monitoring and debugging purposes.
- **Object-Oriented Design**: Follows best practices in object-oriented programming to ensure maintainable and scalable code.

## Game Rules
- Connect to the server to start the game.
- Guess letters to reveal the hidden word.
- Each incorrect guess adds a part to the hangman.
- The game ends when the word is fully revealed or the hangman is completed.
- Use the GUI to interact with the game.

## Run:

1. Starting the Server: Run the server with a specified port number
```
mvn clean compile exec:java
```

2. For each user, run the client and enter the same port number used for the server
```
mvn clean compile exec:java
```

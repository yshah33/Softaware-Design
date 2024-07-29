# Baccarat Game | Java, JavaFX GUI

## Overview

This project is a networked version of the popular casino game Baccarat, implemented in Java with a graphical user interface (GUI) using JavaFX. The game supports multiple players, each playing a separate game with the server. The server handles multiple clients, each on a separate thread, and maintains game state and player activities. 

## Game Rules

1. Each player bids on either The Banker, The Player, or a Draw.
2. The dealer deals two cards each to The Banker and The Player.
3. Cards are valued as follows: 10’s and face cards are worth zero points, Aces are worth one point, and all other cards are worth their face value.
4. If either hand totals 8 or 9 points, it's a "natural" win. Otherwise, additional cards may be drawn based on the rules.
5. The hand closest to 9 points wins. See the included PDF for betting payouts.

## Features

- **JavaFX GUI**: Intuitive and user-friendly interface for both server and client.
- **Multi-User Support**: Server can handle multiple clients, each playing separate games.
- **Server-Client Architecture**: Communication via Java Sockets.
- **Game State Management**: Server maintains game state and logs player activities.
- **Object-Oriented Design**: Implements best practices for maintainable and scalable code.

## run

To run the Baccarat game locally, ensure you have Java and Maven installed. Then, follow these steps:

1. Execute the command on the terminal
```
mvn clean compile exec:java
```



# Tic Tac Toe Game using JavaFX - Client/Server

## Overview


This repository contains the implementation of a Tic Tac Toe game developed as a Java project during the Android Mobile Applications Development track at the Intensive Code Camp program held in ITI - Smart Village.

The Tic Tac Toe game features a client/server architecture built using JavaFX. It offers users the opportunity to play against a computer opponent with two distinct difficulty levels or engage in online multiplayer matches with other players on the same network.

The client application, constructed using JavaFX, provides an intuitive and visually appealing user interface for an immersive gaming experience. On the other hand, the server application utilizes sockets and JSON for efficient network communication, facilitating seamless interaction between players during multiplayer sessions.

To ensure effective data management, the application leverages Apache Derby as a reliable database management system.

This project serves as a demonstration of skills acquired in Java programming, network communication, user interface development, and database management. It offers an entertaining and interactive game while showcasing the practical implementation of fundamental concepts and technologies in the domain of Java application development.


## Features
### Client Side Features

- User Authentication: The application features a login and signup system to authenticate users.
- Single Player Mode: Users can play against a computer with two difficulty levels
- Multiplayer Mode: Users can play against other players on the same network.
- User Avatars: Users can set an avatar and keep track of their score level,
- Online/Offline Status: Users can see who is online or offline.
- Game Recording: Users can record theirgame and view it later. 
- Remember Me Option: Users can select a 'remember me' option to save their login information.
### Server Side Features
- User Management: The server application maintains a list of all registered users.

- Player Status: The server application can track the status of each player, whether they are online, offline, or currently in a game.

- Server Management: The server application can be closed and reopened as needed.

- Player Status Pie Chart: The server application features a pie chart that shows the current status of all players, including those who are online, offline, or in a game.This provides an easy-to-read visualization of the current state of the system.



## Screenshots
![1](https://github.com/Amon3m/TictacToe-Server/assets/112562093/bfb0ed95-94ce-46d4-8fe3-935b2f5e4cf8)
![2](https://github.com/Amon3m/TictacToe-Server/assets/112562093/99aa7b2b-c0ba-4295-8894-262696a45d4c)
![3](https://github.com/Amon3m/TictacToe-Server/assets/112562093/159d1346-367a-4863-bd70-5c4ac84ace1f)
![4](https://github.com/Amon3m/TictacToe-Server/assets/112562093/9bb79ed4-9185-474e-9c91-7986a82a0392)
![5](https://github.com/Amon3m/TictacToe-Server/assets/112562093/8e87cdc3-5598-4fd9-87d2-8c0012323983)
![6](https://github.com/Amon3m/TictacToe-Server/assets/112562093/06ec1f3c-3d4b-4670-904e-ce0c351b5c79)
![7](https://github.com/Amon3m/TictacToe-Server/assets/112562093/70a525a6-34da-4496-85dc-fd18d485305c)
![8](https://github.com/Amon3m/TictacToe-Server/assets/112562093/c52bba65-d718-4fe4-bd3c-9ca17522608e)
![9](https://github.com/Amon3m/TictacToe-Server/assets/112562093/3bfc33a7-4e35-462e-aa16-0e51b9653623)
![10](https://github.com/Amon3m/TictacToe-Server/assets/112562093/940da1eb-a406-4bb7-9bb9-3ca02b2cc80c)
![11](https://github.com/Amon3m/TictacToe-Server/assets/112562093/b2706341-7d16-4954-8374-f60d579ad88f)
![12](https://github.com/Amon3m/TictacToe-Server/assets/112562093/e0561981-c537-4aeb-a91d-eaa621f5296a)


## Architecture
### Database Management

The application uses Apache Derby to manage the database. The database is generated automatically by the application.

### Observable Pattern

The application uses the Observable pattern to make custom views (listviews/piecharts) observe changes about players' states from the server and re-render themselves in real-time.

### Networking with Sockets

The application uses sockets to establish a connection between the server and client apps. Client/Server Model The application uses a client/server model, where one app acts as a server that can handle multiple clients at the same time.

### JSON

The application uses JSON to save recorded game and remember me user options and to send and receive messages using streams between the server and client.

## Installation
1- Download and install Java Development Kit (JDK) 8 or higher. 

2- Download and install Apache Derby from https://db.apache.org/derby/derby_downloads.html.

3- Download and extract the project files. 

4- Open the project in an IDE such as Eclipse or IntellJ

5- Maybe it need to Download some jason libaray files.

6- Run the server application first and then run the client application on multiple devices to play the game.
    
## 🔗 Check Client Repository 

[Client repository ](https://github.com/basseemos1212/TictacToe)
## Conclusion
This application is Tic Tac Toe game that offers both single and multiplayer modes. The client/servert architecture provides a seamless experience,and the use of sockets and JSON make communication between the client and server efficient and secure. The application is built using modern software development best practices, including the Observable pattern and animation effects. The use of Apache Derby as a database management system adds further robustness to the application. The player status pie chart in the server application provides an easy-to-read visualization of the current state of all players. Overall, this application is a example of a implemented client/server game using JavaFX.

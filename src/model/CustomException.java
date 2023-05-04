/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author ahmed
 */
public class CustomException {
    public static class PlayerNotFoundException extends Exception {
        public PlayerNotFoundException() {
            super("Player not found!");
        }
    }

    public static class IncorrectPasswordException extends Exception {
        public IncorrectPasswordException() {
            super("Incorrect password!");
        }
    }

    public static class DatabaseException extends Exception {
        public DatabaseException() {
            super("Error accessing the database. Please try again later.");
        }
    }
}

package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {

    Scanner scanner = new Scanner(System.in);
    public PreparedStatement stmt;
    public Connection connection;

    public static void main(String[] args) {

        Main main = new Main();

        main.jdbcConnection();

        main.createTableIfNotExist();

        main.bookstoreUserInterface();

    }

    public void jdbcConnection() {
        String url = "jdbc:mysql://localhost:3306/lab0db";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, user, password);


        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
        } catch (SQLException e) {
            System.out.println("Connection failed.");
        }
    }

    void createTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255)," +
                "author VARCHAR(255)," +
                "isbn INT," +
                "pages INT)";

        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error creating table.");
        }
    }

    void bookstoreUserInterface() {

        System.out.println("Bookstore Application\n" +
                "---------------------\n" +
                "1. Add Book\n" +
                "2. Edit Book\n" +
                "3. Delete Book\n" +
                "4. List Books\n" +
                "99. Exit\n" +
                "---------------------\n" +
                "Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                addBook();
                String cont = askAgain();
                if (cont.equalsIgnoreCase("yes")) {
                    bookstoreUserInterface();
                } else if (cont.equalsIgnoreCase("no")) {
                    System.out.println("Goodbye.");
                }
                break;
            case 2:
                editBook();
                String cont2 = askAgain();
                if (cont2.equalsIgnoreCase("yes")) {
                    bookstoreUserInterface();
                } else if (cont2.equalsIgnoreCase("no")) {
                    System.out.println("Goodbye.");
                }
                break;
            case 3:
                deleteBook();
                String cont3 = askAgain();
                if (cont3.equalsIgnoreCase("yes")) {
                    bookstoreUserInterface();
                } else if (cont3.equalsIgnoreCase("no")) {
                    System.out.println("Goodbye.");
                }
                break;
            case 4:
                showBooks();
                String cont4 = askAgain();
                if (cont4.equalsIgnoreCase("yes")) {
                    bookstoreUserInterface();
                } else if (cont4.equalsIgnoreCase("no")) {
                    System.out.println("Goodbye.");
                }
                break;
            case 99:
                System.out.println("Goodbye.");
                break;
        }
    }

    public void addBook() {

        String sql = "INSERT INTO books (title, author, isbn, pages) VALUES (?, ?, ?, ?)";

        try {
            stmt = connection.prepareStatement(sql);

            System.out.println("Enter book title: ");
            String title = scanner.nextLine();

            System.out.println("Enter book author: ");
            String author = scanner.nextLine();

            System.out.println("Enter book ISBN: ");
            String isbn = scanner.nextLine();

            System.out.println("Enter book pages: ");
            int pages = scanner.nextInt();
            scanner.nextLine();

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, isbn);
            stmt.setInt(4, pages);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Book added successfully.");
            }

            stmt.close();

        } catch (SQLException e) {
            System.out.println("Error adding book.");
        }
    }

    void editBook() {

        String sqlTitle = "UPDATE books SET title = ? WHERE id = ?";
        String sqlAuthor = "UPDATE books SET author = ? WHERE id = ?";
        String sqlIsbn = "UPDATE books SET isbn = ? WHERE id = ?";
        String sqlPages = "UPDATE books SET pages = ? WHERE id = ?";

        System.out.println("What field would you like to edit: title, author, isbn, pages? ");
        String choice = scanner.nextLine().toLowerCase();

        System.out.println("Enter book ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case ("title"):
                try {
                    stmt = connection.prepareStatement(sqlTitle);
                    System.out.println("Enter book title: ");
                    String title = scanner.nextLine();

                    stmt.setString(1, title);
                    stmt.setInt(2, id);

                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Title updated successfully.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error editing title.");
                }
                break;
            case ("author"):
                try {
                    stmt = connection.prepareStatement(sqlAuthor);
                    System.out.println("Enter book author: ");
                    String author = scanner.nextLine();

                    stmt.setString(1, author);
                    stmt.setInt(2, id);

                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Author updated successfully.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error editing author.");
                }
                break;
            case ("isbn"):
                try {
                    stmt = connection.prepareStatement(sqlIsbn);
                    System.out.println("Enter book isbn: ");
                    int isbn = scanner.nextInt();
                    scanner.nextLine();

                    stmt.setInt(1, isbn);
                    stmt.setInt(2, id);

                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("ISBN updated successfully.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error editing isbn.");
                }
                break;
            case ("pages"):
                try {
                    stmt = connection.prepareStatement(sqlPages);
                    System.out.println("Enter book pages: ");
                    int pages = scanner.nextInt();
                    scanner.nextLine();

                    stmt.setInt(1, pages);
                    stmt.setInt(2, id);

                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Pages updated successfully.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error editing pages.");
                }
                break;
        }

    }

    void deleteBook() {

        String sql = "DELETE FROM books WHERE id = ?";

        try {
            stmt = connection.prepareStatement(sql);

            System.out.println("Enter book ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Book deleted successfully.");
            }

            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error deleting book.");
        }
    }

    void showBooks() {

        String sql = "SELECT * FROM books";

        try {
            stmt = connection.prepareStatement(sql);
            ResultSet resultset = stmt.executeQuery();

            System.out.println("---------------------------------------------------------------------------------------------------------------");
            while (resultset.next()) {
                int id = resultset.getInt("id");
                String title = resultset.getString("title");
                String author = resultset.getString("author");
                String isbn = resultset.getString("isbn");
                int pages = resultset.getInt("pages");

                System.out.printf("%d. Title: %-20s Author: %-20s ISBN: %-10s Pages: %d\n",
                        id, title, author, isbn, pages);
                System.out.println("---------------------------------------------------------------------------------------------------------------");


            }
            resultset.close();

        } catch (SQLException e) {
            System.out.println("Error getting books.");
        }

    }

    String askAgain() {
        System.out.println("Would you like to do anything else?");
        String cont = scanner.nextLine();
        if (cont.equalsIgnoreCase("yes")) {
            return "yes";
        } else if (cont.equalsIgnoreCase("no")) {
            return "no";
        } else {
            System.out.println("Only enter yes or no.");
            return askAgain();
        }
    }

}
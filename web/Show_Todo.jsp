
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.math.BigInteger"%>
<%@page import="java.security.SecureRandom"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%! 
    // Simple Monoalphabetic Cipher
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String ENCRYPTION_KEY = "zyxwvutsrqponmlkjihgfedcba";

    private String decrypt(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                int index = ENCRYPTION_KEY.indexOf(c);
                result.append(index != -1 ? ALPHABET.charAt(index) : c);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
%>

<%
    // Initialize the HttpSession
    session = request.getSession(); 
    String loggedInUsername = (String) session.getAttribute("loggedInUsername");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Todo List</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f0f0f0;
                margin: 0;
                padding: 0;
            }
            .container {
                max-width: 800px;
                margin: 0 auto;
                padding: 20px;
                background-color: #ffffff;
                border: 1px solid #ccc;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                position: relative;
            }
            header {
                background-color: #3498db;
                color: #ffffff;
                padding: 25px;
                border-radius: 10px 10px 0 0;
                text-align: center;
            }
            header h1 {
                font-size: 36px;
                margin: 0;
            }
            header p {
                font-size: 18px;
                margin: 10px 0;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            th, td {
                text-align: left;
                padding: 12px 16px;
            }
            th {
                background-color: #3498db;
                color: #ffffff;
            }
            tr:nth-child(even) {
                background-color: #f2f2f2;
            }
            .add-button {
                position: absolute;
                top: 20px;
                right: 20px;
                padding: 12px 24px;
                background-color: #4CAF50;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                transition: transform 0.2s, box-shadow 0.2s;
            }
            .add-button:hover {
                background-color: #45a049;
            }
            .add-button:active {
                background-color: #3e8448;
                transform: translateY(2px);
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            }


            .action-links a {
                text-decoration: none;
                padding: 4px 8px;
                margin: 0 4px;
                border-radius: 5px;
                font-weight: bold;
                transition: background-color 0.2s;
            }

            .action-links a.delete {
                background-color: #e74c3c; /* Red for Delete */
                color: white;
            }

            .action-links a.edit {
                background-color: #3498db; /* Blue for Edit */
                color: white;
            }

            .action-links a:hover {
                opacity: 0.8;
            }
        </style>

    </head>
    <body>
        <div class="container">
            <header>
                <h1>Todo List</h1>
                 <h4>Hello , <%= loggedInUsername %></h4>
                <p>Welcome to your task management</p>
            </header>
            <a href="LogoutServlet" class="add-button" >Logout</a>
            <a href="Addtodo.html" class="add-button" style="top: 70px">Add Task</a>


            <table>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Target Date</th>
                        <th>Description</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        Connection connection = null;
                        PreparedStatement preparedStatement = null;
                        ResultSet resultSet = null;

                        try {
                            // Load the Derby JDBC driver
                            Class.forName("org.apache.derby.jdbc.ClientDriver");

                            // Establish a database connection
                            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");

                            // Create a SQL query to retrieve todos for the logged-in user
                            String query = "SELECT id,title, target_date, description FROM todo_list WHERE username = ?";
                            preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setString(1, decrypt(loggedInUsername));

                            resultSet = preparedStatement.executeQuery();

                            // Iterate through the result set and display todo data
                            while (resultSet.next()) {
                    %>
                    <tr>
                        <td><b><%= decrypt(resultSet.getString("title"))%></b></td>
                        <td><%= resultSet.getDate("target_date")%></td>
                        <td><%= decrypt(resultSet.getString("description"))%></td>
                        <td class="action-links">
                           
                            <a href="DeleteTodoServlet?id=<%= resultSet.getInt("id")%>">Delete</a>
                            <a href="UpdatTodo.jsp?id=<%= resultSet.getInt("id")%>">Edit</a>
                        </td>

                        
                    </tr>
                    <%
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            // Close resources (preparedStatement, resultSet, and connection) here
                            try {
                                if (resultSet != null) {
                                    resultSet.close();
                                }
                                if (preparedStatement != null) {
                                    preparedStatement.close();
                                }
                                if (connection != null) {
                                    connection.close();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
    </body>
</html>

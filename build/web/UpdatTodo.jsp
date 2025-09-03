<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Edit Todo Item</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f0f0f0;
                margin: 0;
                padding: 0;
            }

            h1 {
                text-align: center;
                margin-top: 20px;
                color: #3498db;
            }

            form {
                max-width: 400px;
                margin: 0 auto;
                padding: 40px;
                background-color: #ffffff;
                border: 1px solid #ccc;
                border-radius: 10px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }

            label {
                font-weight: bold;
            }

            input[type="text"],
            input[type="date"],
            textarea {
                width: 100%;
                padding: 8px;
                margin-bottom: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }

            textarea {
                resize: vertical;
            }

            input[type="submit"] {
                background-color: #3498db;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }

            input[type="submit"]:hover {
                background-color: #2980b9;
            }
        </style>
    </head>
    <body>
  
        <h1>Edit Todo Item</h1>
        <form action="UpdateServlet" method="post">
            <input type="hidden" name="id" value="<%= request.getParameter("id")%>">
            <label for="title">Title:</label>
            <input type="text" id="title" name="title" required><br><br>

            <label for="target_date">Target Date:</label>
            <input type="date" id="target_date" name="target_date" required><br><br>

            <label for="description">Description:</label><br>
            <textarea id="description" name="description" rows="4" cols="50" required></textarea><br><br>

            <input type="submit" value="Update">
        </form>
    </body>
</html>


<%@ page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="ISO-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Change Password</title>
    <style>
        body, html {
            margin: 0;
            padding: 0;
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
        }

        .changepassword-container {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .changepassword-form {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 400px;
        }

        .changepassword-form h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #333;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            font-size: 14px;
            color: #555;
        }

        .form-group input {
            width: 100%;
            padding: 10px;
            font-size: 16px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }

        .form-group input:focus {
            border-color: #007bff;
        }

        .changepassword-btn {
            width: 100%;
            padding: 12px;
            font-size: 16px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .changepassword-btn:hover {
            background-color: #0056b3;
        }

        .error-message {
            color: red;
            text-align: center;
        }

        .success-message {
            color: green;
            text-align: center;
        }

    </style>
</head>
<body>

    <div class="changepassword-container">
        <form class="changepassword-form" action="<%= request.getContextPath() %>/changePassword" method="POST">
            <h2>Change Password</h2>

            <% 
                // Fetch the current username (assuming the user is logged in, using session)
                String username = (String) session.getAttribute("username");
                String currentPassword = (String) session.getAttribute("password"); // For demo purposes, we keep the password in session (in real life, don't do this!)
                
                // Get form values
                String currentPwd = request.getParameter("currentPassword");
                String newPwd = request.getParameter("newPassword");
                String confirmPwd = request.getParameter("confirmPassword");

                String errorMessage = null;
                String successMessage = null;

                if ("POST".equalsIgnoreCase(request.getMethod())) {
                    // Simple validation and password change logic (for demonstration)
                    if (currentPwd != null && newPwd != null && confirmPwd != null) {
                        if (!currentPwd.equals(currentPassword)) {
                            errorMessage = "Current password is incorrect.";
                        } else if (!newPwd.equals(confirmPwd)) {
                            errorMessage = "New password and confirm password do not match.";
                        } else {
                            successMessage = "Password changed successfully.";
                        }
                    } else {
                        errorMessage = "All fields are required.";
                    }
                }
            %>

            <% if (errorMessage != null) { %>
                <p class="error-message"><%= errorMessage %></p>
            <% } %>
            
            <% if (successMessage != null) { %>
                <p class="success-message"><%= successMessage %></p>
            <% } %>

            <div class="form-group">
                <label for="currentPassword">Current Password</label>
                <input type="password" id="currPassword" name="currPasswd" required placeholder="Enter current password">
            </div>

            <div class="form-group">
                <label for="newPassword">New Password</label>
                <input type="password" id="newPassword" name="newPasswd" required placeholder="Enter new password">
            </div>

            <div class="form-group">
                <label for="confirmPassword">Confirm New Password</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required placeholder="Confirm new password">
            </div>

            <button type="submit" class="changepassword-btn">Change Password</button>
        </form>
    </div>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cpt.model.AttendedDrive, java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Selected Drives - Placement Tracking System</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: Arial, sans-serif; }
        body { background: #f4f4f4; }
        /* Header */
        .header {
            background: #2c3e50; color: white; padding: 15px 20px;
            display: flex; justify-content: space-between; align-items: center;
            position: fixed; top: 0; width: 100%; z-index: 1000;
        }
        .header .logo { font-size: 24px; font-weight: bold; }
        .header .profile { position: relative; cursor: pointer; }
        .header .profile span { font-size: 18px; }
        .header .dropdown {
            display: none; position: absolute; right: 0; top: 40px;
            background: white; border: 1px solid #ccc; box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        }
        .header .dropdown a {
            display: block; padding: 10px 20px; color: #333; text-decoration: none;
        }
        .header .dropdown a:hover { background: #f4f4f4; }
        /* Content */
        .content {
            margin-top: 80px; padding: 20px; background: white; margin: 80px 20px 20px;
            border-radius: 5px;
        }
        h2 { color: #333; margin-bottom: 20px; }
        .alert {
            padding: 15px; margin-bottom: 20px; border-radius: 5px;
        }
        .alert.error { background: #ffe6e6; color: #d32f2f; }
        .table {
            width: 100%; border-collapse: collapse; margin-bottom: 20px;
        }
        .table th, .table td {
            border: 1px solid #ddd; padding: 12px; text-align: left;
        }
        .table th { background: #3498db; color: white; }
        .table tr:nth-child(even) { background: #f9f9f9; }
        .table tr:hover { background: #f1f1f1; }
        /* Responsive */
        @media (max-width: 768px) {
            .content { margin: 80px 10px 10px; }
        }
    </style>
</head>
<body>
    <!-- Header -->
    <div class="header">
        <div class="logo">Placement Tracking System</div>
        <div class="profile" onclick="toggleDropdown()">
            <span>Profile ▼</span>
            <div class="dropdown" id="profileDropdown">
                <a href="/student/viewProfile">View Profile</a>
                <a href="/student/changePassword">Change Password</a>
                <a href="/logout">Logout</a>
            </div>
        </div>
    </div>

    <!-- Content -->
    <div class="content">
        <h2>Selected Drives</h2>
        <%-- Error Message --%>
        <% String error = request.getParameter("error"); %>
        <% if (error != null) { %>
            <div class="alert error">
                <% if ("already_applied".equals(error)) { %>
                    You have already applied to this drive.
                <% } else { %>
                    Unknown error: <%= error %>
                <% } %>
            </div>
        <% } %>

        <table class="table">
            <tr>
                <th>Drive Name</th>
                <th>Company</th>
                <th>Status</th>
                <th>Fizzled Round</th>
            </tr>
            <% List<AttendedDrive> drives = (List<AttendedDrive>)request.getAttribute("attendedDrives"); %>
            <% if (drives != null) { %>
                <% for (AttendedDrive drive : drives) { %>
                    <tr>
                        <td><%= drive.getPldName() %></td>
                        <td><%= drive.getCmpName() %></td>
                        <td><%= "SEL".equals(drive.getStatus()) ? "Selected" : "Not Selected" %></td>
                        <td><%= drive.getFizzledRound() != null ? drive.getFizzledRound() : "N/A" %></td>
                    </tr>
                <% } %>
            <% } else { %>
                <tr><td colspan="4">No attended drives.</td></tr>
            <% } %>
        </table>
    </div>

    <script>
        function toggleDropdown() {
            var dropdown = document.getElementById("profileDropdown");
            dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
        }
        document.addEventListener("click", function(event) {
            var profile = document.querySelector(".profile");
            if (!profile.contains(event.target)) {
                document.getElementById("profileDropdown").style.display = "none";
            }
        });
    </script>
</body>
</html>
```
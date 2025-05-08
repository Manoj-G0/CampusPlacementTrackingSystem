<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cpt.model.*, java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Placement Tracking System</title>
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
        /* Sidebar */
        .sidebar {
            width: 250px; background: #ecf0f1; position: fixed; top: 70px; bottom: 0;
            padding: 20px; overflow-y: auto;
        }
        .sidebar a {
            display: block; padding: 10px; color: #333; text-decoration: none; margin-bottom: 10px;
            border-radius: 5px;
        }
        .sidebar a:hover { background: #3498db; color: white; }
        /* Content */
        .content {
            margin-left: 270px; margin-top: 80px; padding: 20px; background: white;
            min-height: calc(100vh - 80px); border-radius: 5px;
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
        .btn {
            padding: 8px 15px; background: #3498db; color: white; border: none;
            border-radius: 5px; text-decoration: none; display: inline-block;
        }
        .btn:hover { background: #2980b9; }
        .btn.delete { background: #d32f2f; }
        .btn.delete:hover { background: #b71c1c; }
        /* Responsive */
        @media (max-width: 768px) {
            .sidebar { width: 100%; position: static; }
            .content { margin-left: 0; }
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

    <!-- Sidebar -->
    <div class="sidebar">
        <a href="/student/dashboard">Dashboard</a>
        <a href="/student/updateProfile">Update Profile</a>
        <a href="/student/upcomingDrives">Upcoming Drives</a>
        <a href="/student/eligibleDrives">Eligible Drives</a>
        <a href="/student/selectedDrives">Selected Drives</a>
        <a href="/student/apply/0">Apply to Drive</a>
        <a href="/student/notifications">Notifications</a>
    </div>

    <!-- Content -->
    <div class="content">
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

        <%-- Student Info --%>
        <h2>Welcome, <%= ((Student)request.getAttribute("student")).getFullName() %></h2>
        <div>
            <p><strong>Roll Number:</strong> <%= ((Student)request.getAttribute("student")).getRollNo() %></p>
            <p><strong>Email:</strong> <%= ((Student)request.getAttribute("student")).getCollegeEmail() %></p>
            <p><strong>CGPA:</strong> <%= ((Student)request.getAttribute("student")).getCgpa() %></p>
            <p><strong>Backlogs:</strong> <%= ((Student)request.getAttribute("student")).getBacklogs() %></p>
            <p><strong>Status:</strong> <%= ((Student)request.getAttribute("student")).getStatus() %></p>
        </div>

        <%-- Resumes --%>
        <h2>Resumes</h2>
        <form action="/student/addResume" method="post">
            <input type="text" name="resumeFile" placeholder="Enter resume file path" required style="padding: 8px; width: 300px;">
            <button type="submit" class="btn">Add Resume</button>
        </form>
        <table class="table">
            <tr>
                <th>File</th>
                <th>Upload Date</th>
                <th>Action</th>
            </tr>
            <% List<Resume> resumes = (List<Resume>)request.getAttribute("resumes"); %>
            <% if (resumes != null) { %>
                <% for (Resume resume : resumes) { %>
                    <tr>
                        <td><%= resume.getResFile() %></td>
                        <td><%= resume.getResUploadDate() != null ? resume.getResUploadDate() : "N/A" %></td>
                        <td><a href="/student/deleteResume/<%= resume.getResId() %>" class="btn delete">Delete</a></td>
                    </tr>
                <% } %>
            <% } %>
        </table>

        <%-- Notifications --%>
        <h2>Notifications</h2>
        <table class="table">
            <tr>
                <th>Message</th>
                <th>Date</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
            <% List<Notification> notifications = (List<Notification>)request.getAttribute("notifications"); %>
            <% if (notifications != null) { %>
                <% for (Notification notification : notifications) { %>
                    <tr>
                        <td><%= notification.getNtfMessage() %></td>
                        <td><%= notification.getNtfDate() != null ? notification.getNtfDate() : "N/A" %></td>
                        <td><%= notification.getNtfRead() ? "Read" : "Unread" %></td>
                        <td>
                            <% if (!notification.getNtfRead()) { %>
                                <a href="/student/markNotificationRead/<%= notification.getNtfId() %>" class="btn">Mark as Read</a>
                            <% } %>
                        </td>
                    </tr>
                <% } %>
            <% } %>
        </table>

        <%-- Applications --%>
        <h2>Applications</h2>
        <table class="table">
            <tr>
                <th>Drive Name</th>
                <th>Company</th>
                <th>Date</th>
                <th>Status</th>
            </tr>
            <% List<Application> applications = (List<Application>)request.getAttribute("applications"); %>
            <% if (applications != null) { %>
                <% for (Application app : applications) { %>
                    <tr>
                        <td>
                            <% List<PlacementDrive> drives = (List<PlacementDrive>)request.getAttribute("upcomingDrives"); %>
                            <% for (PlacementDrive drive : drives) { %>
                                <% if (drive.getPldId().equals(app.getAppPldId())) { %>
                                    <%= drive.getPldName() %>
                                <% } %>
                            <% } %>
                        </td>
                        <td><%= app.getAppCmpId() %></td>
                        <td><%= app.getAppDate() %></td>
                        <td><%= app.getAppStatus() %></td>
                    </tr>
                <% } %>
            <% } %>
        </table>
    </div>

    <script>
        function toggleDropdown() {
            var dropdown = document.getElementById("profileDropdown");
            dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
        }
        // Close dropdown when clicking outside
        document.addEventListener("click", function(event) {
            var profile = document.querySelector(".profile");
            if (!profile.contains(event.target)) {
                document.getElementById("profileDropdown").style.display = "none";
            }
        });
    </script>
</body>
</html>
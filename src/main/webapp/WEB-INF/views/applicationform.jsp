```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cpt.model.Student, com.cpt.model.Resume, java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apply to Drive - Placement Tracking System</title>
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
            border-radius: 5px; max-width: 600px; margin-left: auto; margin-right: auto;
        }
        h2 { color: #333; margin-bottom: 20px; }
        .alert {
            padding: 15px; margin-bottom: 20px; border-radius: 5px;
        }
        .alert.error { background: #ffe6e6; color: #d32f2f; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; color: #333; }
        .form-group input, .form-group select {
            width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 5px;
        }
        .btn {
            padding: 8px 15px; background: #3498db; color: white; border: none;
            border-radius: 5px; text-decoration: none; display: inline-block;
        }
        .btn:hover { background: #2980b9; }
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
        <h2>Apply to Placement Drive</h2>
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

        <% Student student = (Student)request.getAttribute("student"); %>
        <% Integer pldId = (Integer)request.getAttribute("pldId"); %>
        <% Integer cmpId = (Integer)request.getAttribute("cmpId"); %>
        <form action="/student/submitApplication" method="post">
            <div class="form-group">
                <label for="usrId">User ID</label>
                <input type="text" id="usrId" name="usrId" value="<%= student.getRollNo() %>" readonly>
            </div>
            <div class="form-group">
                <label for="pldId">Drive ID</label>
                <input type="text" id="pldId" name="pldId" value="<%= pldId %>" readonly>
            </div>
            <div class="form-group">
                <label for="cmpId">Company ID</label>
                <input type="text" id="cmpId" name="cmpId" value="<%= cmpId %>" readonly>
            </div>
            <div class="form-group">
                <label for="resumeFile">Select Resume</label>
                <select id="resumeFile" name="resumeFile" required>
                    <option value="">-- Select Resume --</option>
                    <% List<Resume> resumes = (List<Resume>)request.getAttribute("resumes"); %>
                    <% if (resumes != null) { %>
                        <% for (Resume resume : resumes) { %>
                            <option value="<%= resume.getResFile() %>"><%= resume.getResFile() %></option>
                        <% } %>
                    <% } %>
                </select>
            </div>
            <input type="hidden" name="_csrf" value="dummy-csrf-token">
            <button type="submit" class="btn">Submit Application</button>
        </form>
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
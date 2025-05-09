<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="/CPT/resources/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        body { background-color: #ffffff; }
    </style>
</head>
<body>
    <div class="header <%= session.getAttribute("sidebarClosed") != null && (boolean) session.getAttribute("sidebarClosed") ? "sidebar-closed" : "" %>">
        <div class="search-bar">
            <i class="fas fa-search"></i>
            <input type="text" placeholder="Search <%= request.getAttribute("userRole") %> Dashboard...">
        </div>
        <div class="profile-section">
            <div class="notification-icon">
                <i class="fas fa-bell"></i>
                <span class="badge">0</span>
            </div>
            <div class="profile-avatar"><%= request.getAttribute("userInitials") %></div>
            <span><%= request.getAttribute("userName") %></span>
            <div class="dropdown">
                <a href="/CPT/logout">Logout</a>
            </div>
        </div>
    </div>
    <script>
        document.querySelector('.profile-section').addEventListener('click', () => {
            document.querySelector('.dropdown').style.display = 
                document.querySelector('.dropdown').style.display === 'block' ? 'none' : 'block';
        });
    </script>
</body>
</html>
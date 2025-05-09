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
    <div class="sidebar <%= session.getAttribute("sidebarClosed") != null && (boolean) session.getAttribute("sidebarClosed") ? "closed" : "" %>">
        <div class="logo-container">
            <div class="logo">
                <i class="fas fa-graduation-cap"></i>
                <span>CPT</span>
            </div>
            <div class="toggle-btn" onclick="toggleSidebar()">
                <i class="fas fa-chevron-left"></i>
            </div>
        </div>
        <div class="menu-container">
            <div class="menu-title">HR MENU</div>
            <a href="/CPT/hr/dashboard" class="menu-item <%= "dashboard".equals(request.getAttribute("activePage")) ? "active" : "" %>">
                <i class="fas fa-tachometer-alt"></i>
                <span>Dashboard</span>
            </a>
            <a href="/CPT/hr/addJobDescription" class="menu-item <%= "addJobDescription".equals(request.getAttribute("activePage")) ? "active" : "" %>">
                <i class="fas fa-plus-circle"></i>
                <span>Add Job Description</span>
            </a>
            <a href="/CPT/hr/roundWiseStudents" class="menu-item <%= "roundWiseStudents".equals(request.getAttribute("activePage")) ? "active" : "" %>">
                <i class="fas fa-users"></i>
                <span>Round-Wise Students</span>
            </a>
            <a href="/CPT/hr/finalCandidates" class="menu-item <%= "finalCandidates".equals(request.getAttribute("activePage")) ? "active" : "" %>">
                <i class="fas fa-check-circle"></i>
                <span>Final Candidates</span>
            </a>
        </div>
    </div>
    <script>
        function toggleSidebar() {
            fetch('/CPT/toggleSidebar', { method: 'POST' })
                .then(() => {
                    document.querySelector('.sidebar').classList.toggle('closed');
                    document.querySelector('.main-content').classList.toggle('sidebar-closed');
                    document.querySelector('.header').classList.toggle('sidebar-closed');
                });
        }
    </script>
</body>
</html>
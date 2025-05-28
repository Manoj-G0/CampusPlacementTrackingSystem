<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cpt.model.*, java.util.*" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Applications - Placement Tracking System</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <script type="text/javascript" src="${jsUrl}"></script>
    <style>
        .table-container {
            background: #fff;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            animation: fadeIn 0.6s ease;
        }
        .data-table th, .data-table td {
            padding: 15px;
        }
        .data-table tr {
            transition: background 0.3s ease, transform 0.2s;
        }
        .data-table tr:hover {
            background: #f0f7ff;
            transform: scale(1.01);
        }
        .data-table th {
            background: linear-gradient(180deg, #f1f5f9 0%, #e2e8f0 100%);
        }
        .data-table td {
            color: #1e2a3c;
        }
    </style>
</head>
<body>
<jsp:include page="./shared/sidebar_student_dashboard.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Applications</div>
    <div class="container">
        <div class="table-container">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Company</th>
                        <th>Date</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<Application> applications = (List<Application>) request.getAttribute("applications");
                        List<PlacementDrive> drives = (List<PlacementDrive>) request.getAttribute("upcomingDrives");
                        
                    %>
                    <% if (applications != null && !applications.isEmpty()) { %>
                        <% for (Application app : applications) { %>
                            
                                <tr>
                                    <td><%= app.getAppCmpId() %></td>
                                    <td><%= app.getAppDate() != null ? app.getAppDate() : "N/A" %></td>
                                    <td><%= app.getAppStatus() != null ? app.getAppStatus() : "N/A" %></td>
                                </tr>
                            <% } %>
                    <% } else { %>
                        <tr>
                            <td colspan="4">No applications available.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
<!-- Notifications Popup -->
    <div class="popup-overlay" id="notificationsPopup">
        <div class="popup-content">
            <div class="popup-header">
                <div class="popup-title">Notifications</div>
                <button class="close-popup" onclick="closeNotifications()">×</button>
            </div>
            <div class="popup-body">
                <div class="notifications-list" id="notificationsList"></div>
            </div>
        </div>
    </div>
<script>
    function toggleSidebar() {
        document.querySelector('.sidebar').classList.toggle('collapsed');
        document.querySelector('.main-content').classList.toggle('sidebar-closed');
        document.querySelector('.header').classList.toggle('sidebar-closed');
    }
    function toggleDropdown() {
        var dropdown = document.getElementById("profileDropdown");
        dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
    }
</script>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>
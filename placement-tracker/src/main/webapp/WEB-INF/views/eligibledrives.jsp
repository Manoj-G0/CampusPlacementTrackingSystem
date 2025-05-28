<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cpt.model.EligibleDrive, java.util.*" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<% String error = request.getParameter("error"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Eligible Drives - Placement Tracking System</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${cssUrl}" />
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
        .alert.error {
            background: #fee2e2;
            color: #dc2626;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            animation: slideIn 0.5s ease;
        }
        .btn.apply-btn {
            background: linear-gradient(45deg, #22c55e, #16a34a);
            border: none;
            padding: 8px 16px;
            border-radius: 25px;
            color: #fff;
            font-weight: 600;
            transition: transform 0.3s ease, box-shadow 0.3s;
        }
        .btn.apply-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(34, 197, 94, 0.4);
        }
        .btn.apply-btn:disabled {
            background: #d1d5db;
            cursor: not-allowed;
            transform: none;
            box-shadow: none;
        }
        .company-icon {
            margin-right: 10px;
            color: #3b82f6;
        }
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        @keyframes slideIn {
            from { opacity: 0; transform: translateX(10px); }
            to { opacity: 1; transform: translateX(0); }
        }
    </style>
</head>
<body>
<jsp:include page="./shared/sidebar_student_dashboard.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Eligible Drives</div>
    <div class="container">
        <% if (error != null) { %>
            <div class="alert error">
                <% if ("already_applied".equals(error)) { %>
                    You have already applied to this drive.
                <% } else { %>
                    Unknown error: <%= error %>
                <% } %>
            </div>
        <% } %>
        <div class="table-container">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Drive Name</th>
                        <th>Company</th>
                        <th>Start Date</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% List<EligibleDrive> drives = (List<EligibleDrive>)request.getAttribute("eligibleDrives"); %>
                    <% if (drives != null && !drives.isEmpty()) { %>
                        <% for (EligibleDrive drive : drives) { %>
                            <tr>
                                <td><%= drive.getDriveName() %></td>
                                <td><i class="fas fa-building company-icon"></i><%= drive.getCompanyName() %></td>
                                <td><%= drive.getStartDate() %></td>
                                <td>
                                    <a href="<%= request.getContextPath()%>/student/apply/<%= drive.getPldId() %>" 
                                       class="btn apply-btn" 
                                       id="apply-<%= drive.getPldId() %>"
                                       >Apply</a>
                                </td>
                            </tr>
                        <% } %>
                    <% } else { %>
                        <tr>
                            <td colspan="4">No eligible drives available.</td>
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
    document.addEventListener("click", function(event) {
        var profile = document.querySelector(".profile-section");
        if (!profile.contains(event.target)) {
            document.getElementById("profileDropdown").style.display = "none";
        }
    });
</script>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>
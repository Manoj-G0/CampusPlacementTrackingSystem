<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.cpt.model.*, java.util.*" %>

<%@ page import="com.cpt.model.*, java.util.*, java.text.SimpleDateFormat" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Edit Placement Drive</title>
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
	<div class="management-card">
        <div class="card-header">
            <h3>Edit Placement Drive</h3>
        </div>
        <div class="card-body">
            <table class="data-table">
                <thead>
                    <tr>
                    	<th>College</th>
                        <th>Drive Name</th>
                        <th>Company</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<PlacementDrive> placementDrives = (List<PlacementDrive>) request.getAttribute("placementDrives");
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                        for (PlacementDrive placementDrive : placementDrives) {
                    %>
                    <tr>
                    	<td><% out.print(placementDrive.getCollege().getClgName()); %></td>
                        <td><% out.print(placementDrive.getName()); %></td>
                        <td><% out.print(placementDrive.getCompany().getCmpName()); %></td>
                        <td><% out.print(sdf.format(placementDrive.getStartDate())); %></td>
                        <td><% out.print(sdf.format(placementDrive.getEndDate())); %></td>
                        <td>
                            <a href="edit-placement-drives/<% out.print(placementDrive.getPldId()); %>" class="action-btn btn-edit"><i class="fas fa-edit"></i></a>
                            <a href="delete-placement-drives/<% out.print(placementDrive.getPldId()); %>" class="action-btn btn-delete" onclick="return confirm('Are you sure?')">
                                <i class="fas fa-trash"></i>
                            </a>
                        </td>
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
    <script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>
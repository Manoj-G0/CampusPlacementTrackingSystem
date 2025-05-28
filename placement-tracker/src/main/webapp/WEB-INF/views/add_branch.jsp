<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cpt.model.*, java.util.*" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Branch - Placement Tracker</title>
	<link rel="stylesheet" type="text/css" href="${cssUrl}" />
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Add Branch</div>
    <form action="add-branch" method="post">
        <div class="form-group">
            <label for="brnClgId">College</label>
            <select id="brnClgId" name="brnClgId" required>
                <% 
                    List<College> colleges = (List<College>) request.getAttribute("colleges");
                    for (College college : colleges) {
                %>
                <option value="<% out.print(college.getClgId()); %>"><% out.print(college.getClgName()); %></option>
                <% } %>
            </select>
        </div>
        <div class="form-group">
            <label for="brnName">Branch Name</label>
            <input type="text" id="brnName" name="brnName" required>
        </div>
        <div class="form-group">
            <label for="brnDesc">Description</label>
            <textarea id="brnDesc" name="brnDesc"></textarea>
        </div>
        <button type="submit" class="popup-btn btn-primary">Add Branch</button>
    </form>
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
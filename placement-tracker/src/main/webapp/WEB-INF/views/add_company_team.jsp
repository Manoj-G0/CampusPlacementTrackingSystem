<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Company Team - Placement Tracker</title>
    <link rel="stylesheet" type="text/css" href="${cssUrl}" />
</head>
<body>
<jsp:include page="./shared/hr_sidebar.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Add Company Team</div>
    <form action="add-company-team" method="post">
        <div class="form-group">
            <label for="ctmName">Team Name</label>
            <input type="text" id="ctmName" name="ctmName" required>
        </div>
        <div class="form-group">
            <label for="ctmRole">Contact</label>
            <input type="text" id="ctmRole" name="ctmRole" required>
        </div>
        <button type="submit" class="popup-btn btn-primary">Add Team</button>
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
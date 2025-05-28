<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add College - Placement Tracker</title>
    <link rel="stylesheet" type="text/css" href="${cssUrl}" />
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Add College</div>
    <form action="add-college" method="post">
        <div class="form-group">
            <label for="clgName">College Name</label>
            <input type="text" id="clgName" name="clgName" required>
        </div>
        <div class="form-group">
            <label for="clgAddress">Address</label>
            <textarea id="clgAddress" name="clgAddress"></textarea>
        </div>
        <div class="form-group">
            <label for="clgContact">Contact Number</label>
            <input type="text" id="clgContact" name="clgContact">
        </div>
        <button type="submit" class="popup-btn btn-primary">Add College</button>
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
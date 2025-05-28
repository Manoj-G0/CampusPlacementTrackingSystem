<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cpt.model.College" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>College Form</title>
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />
<%
	College college = (College) request.getAttribute("college");
	%>
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Edit College</div>
    <form action="../updatecollege" method="post">
    	<input type="hidden" name="clgId" id="clgId" value="<%=college.getClgId()%>"/>
        <div class="form-group">
            <label for="clgName">College Name</label>
            <input type="text" id="clgName" name="clgName" value="<%=college.getClgName() %>" required>
        </div>
        <div class="form-group">
            <label for="clgAddress">Address</label>
            <textarea id="clgAddress" name="clgAddress"><%=college.getClgAddress() %></textarea>
        </div>
        <div class="form-group">
            <label for="clgContact">Contact Number</label>
            <input type="text" id="clgContact" name="clgContact" value="<%=college.getClgContact() %>">
        </div>
        <button type="submit" class="popup-btn btn-primary">Edit College</button>
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
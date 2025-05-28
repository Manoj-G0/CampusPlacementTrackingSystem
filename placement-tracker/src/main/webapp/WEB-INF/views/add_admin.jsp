<%@ page import="com.cpt.model.College"%>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<html>
<head>
<title>Add Admin</title>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f4f7f9;
	margin: 0;
	padding: 0;
}

.form-container {
	background-color: #ffffff;
	padding: 30px;
	max-width: 600px;
	margin: auto;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h2 {
	color: #001f4d; /* Navy blue */
	margin-bottom: 25px;
	text-align: center;
}

.form-group {
	margin-bottom: 20px;
}

.form-group label, .form-group input, .form-group select {
	display: block;
	width: 100%;
}

.form-group input, .form-group select {
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	font-size: 15px;
	margin-top: 5px;
}

.form-group input:focus, .form-group select:focus {
	outline: none;
	border-color: #001f4d;
	box-shadow: 0 0 5px rgba(0, 31, 77, 0.3);
}

.submit-btn {
	background-color: #001f4d; /* Navy blue */
	color: white;
	padding: 12px 20px;
	border: none;
	font-size: 16px;
	border-radius: 4px;
	cursor: pointer;
	width: 100%;
}

.submit-btn:hover {
	background-color: #003366;
}
</style>
<script type="text/javascript" src="${jsUrl}"></script>

</head>

<body>
	<jsp:include page="./shared/sidebar_admin.jsp" />
	<div class="main-content">
		<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
		<div class="form-container">
			<h2>Add Admin</h2>
			<% String error = (String) request.getAttribute("error"); %>
			<% String message = (String) request.getAttribute("message"); %>
			<% if (error != null) { %>
			<p class="error"><%= error %></p>
			<% } %>
			<% if (message != null) { %>
			<p class="success"><%= message %></p>
			<% } %>
			<form action="addAdmin" method="post">
				<div class="form-group">
					<label for="adminId">Admin ID:</label> <input type="text"
						id="adminId" name="adminId" required>
				</div>
				<div class="form-group">
					<label for="adminName">Admin Name:</label> <input type="text"
						id="adminName" name="adminName" required>
				</div>
				<div class="form-group">
					<label for="designation">Designation:</label> <input type="text"
						id="designation" name="designation" required>
				</div>
				<div class="form-group">
					<label for="email">Email:</label> <input type="email"
						id="email" name="email" required>
				</div>
				<div class="form-group">
					<label for="collegeId">College:</label> <select id="collegeId"
						name="collegeId" required>
						<option value="">Select a college</option>
						<% 
                    List<College> colleges = (List<College>) request.getAttribute("colleges");
                    if (colleges != null) {
                        for (College college : colleges) {
                %>
						<option value="<%= college.getClgId() %>"><%= college.getClgName() %></option>
						<% 
                        }
                    }
                %>
					</select>
				</div>
				<button type="submit" class="submit-btn">Add Admin</button>
			</form>
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
	
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>    
<%@ page import="com.cpt.model.*"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
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

.form-group label,
.form-group input,
.form-group select {
    display: block;
    width: 100%;
}

.form-group input,
.form-group select {
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 15px;
    margin-top: 5px;
}

.form-group input:focus,
.form-group select:focus {
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

</head>
<body>
<% List<Company> companies = (List<Company>)request.getAttribute("companies") ;
	List<College> colleges = (List<College>)request.getAttribute("colleges");
%>

<jsp:include page="./shared/sidebar_admin.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
<div class="form-container">
		<h2>Add HR</h2>
		<form action="save-hr-details" method="POST" >
			<div class="form-group">
					HR ID:<input type="text" name="hrId">
			</div>
						<div class="form-group">
					HR Name:<input type="text" name="hrName">
			</div>
			<div class="form-group">
				College:
				<select
					id="college" name="clgId" required>
					<option value="" disabled selected>Select a College</option>
					<%for(College c:colleges){ %>
					<option value="<%=c.getClgId()%>"><%=c.getClgName()%></option>
					<%} %>
				</select>
			</div>
			<div class="form-group">
				Company:
				<select
					id="company" name="cmpId" required>
					<option value="" disabled selected>Select a Company</option>
					<%for(Company c:companies){ %>
					<option value="<%=c.getCmpId()%>"><%=c.getCmpName()%></option>
					<%} %>
				</select>
			</div>
			<div class="form-group">
					HR Contact:<input type="number" name="hr_phone">
			</div>
			<div class="form-group">
					HR Email:<input type="email" name="hrEmail">
			</div>


			<div class="form-group">
				<button type="submit" class="submit-btn">Submit</button>
			</div>
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
    <script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>
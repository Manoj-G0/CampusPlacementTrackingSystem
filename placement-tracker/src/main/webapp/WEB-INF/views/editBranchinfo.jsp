<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cpt.model.*" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html>
<head>

<meta charset="ISO-8859-1">
<title>Branch Edit Form</title>
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />


	<%
	Branch branch = (Branch) request.getAttribute("branch");
	%>
	
	<div class="main-content">
		<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Edit Branch</div>
    <form action="../update-branch" method="post">
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
        <input type="hidden" name="brnId" id="brnId" value="<% out.print(branch.getBrnId()); %>"/>
        <div class="form-group">
            <label for="brnName">Branch Name</label>
            <input type="text" id="brnName" name="brnName" value="<% out.print(branch.getBrnName()); %>" required>
        </div>
        <div class="form-group">
            <label for="brnDesc">Description</label>
            <textarea id="brnDesc" name="brnDesc"><% out.print(branch.getBrnDesc()); %></textarea>
        </div>
        <button type="submit" class="popup-btn btn-primary">Edit Branch</button>
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
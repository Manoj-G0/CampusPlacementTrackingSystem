<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.cpt.model.*, java.util.*" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Placement Drive - Placement Tracker</title>
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />
<% PlacementDrive placement = (PlacementDrive) request.getAttribute("placementDrive");%>
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Edit Placement Drive</div>
	
    <form action="../update-placement-drive" method="post">
    	<input type="hidden" id="pldId" name="pldId" value="<% out.print(placement.getPldId()); %>">
        <div class="form-group">
            <label for="collegeId">College</label>
            <select id="collegeId" name="collegeId" required>
                <% 
                
                    List<College> colleges = (List<College>) request.getAttribute("colleges");
                    for (College college : colleges) {
                %>
                <option value="<% out.print(college.getClgId()); %>"><% out.print(college.getClgName()); %></option>
                <% } %>
            </select>
        </div>
        <div class="form-group">
            <label for="companyId">Company</label>
            <select id="companyId" name="companyId" required>
                <% 
                    List<Company> companies = (List<Company>) request.getAttribute("companies");
                    for (Company company : companies) {
                %>
                <option value="<% out.print(company.getCmpId()); %>"><% out.print(company.getCmpName()); %></option>
                <% } %>
            </select>
        </div>
        <div class="form-group">
        
            <label for="name">Drive Name</label>
            <input type="text" id="name" name="name" value="<% out.print(placement.getName()); %>" required>
        </div>
        <div class="form-group">
            <label for="startDate">Start Date</label>
            <input type="date" id="startDate" name="startDate" value="<% out.print(placement.getStartDate()); %>" required>
        </div>
        <div class="form-group">
            <label for="endDate">End Date</label>
            <input type="date" id="endDate" name="endDate" value="<% out.print(placement.getEndDate()); %>">
            <small id="dateError" style="color:red;"></small>
        </div>
        <div class="form-group">
            <label for="pldRole">Role</label>
            <input type="text" id="pldRole" name="pldRole"  value="<% out.print(placement.getPldRole()); %>" required>
        </div>
        <div class="form-group">
            <label for="pldSalary">Salary</label>
            <input type="text" id="pldSalary" name="pldSalary"  value="<% out.print(placement.getPldSalary()); %>" required>
        </div>
        <button type="submit" class="popup-btn btn-primary">Update Drive</button>
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
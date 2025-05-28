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
    <title>Add Placement Drive - Placement Tracker</title>
    <link rel="stylesheet" type="text/css" href="${cssUrl}" />
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Add Placement Drive</div>
    <form action="add-placement-drive" method="post" id ="myform">
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
            <input type="text" id="name" name="name" required>
        </div>
        <div class="form-group">
		  <label for="startDate">Start Date</label>
		  <input type="date" id="startDate" name="startDate" required>
		</div>
		
		<div class="form-group">
		  <label for="endDate">End Date</label>
		  <input type="date" id="endDate" name="endDate" required>
		  <small id="dateError" style="color:red;"></small>
		</div>
		<div class="form-group">
            <label for="pldRole">Role</label>
            <input type="text" id="pldRole" name="pldRole" required>
        </div>
        <div class="form-group">
            <label for="pldSalary">Salary</label>
            <input type="text" id="pldSalary" name="pldSalary" required>
        </div>

        <button type="submit" class="popup-btn btn-primary">Add Drive</button>
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
    <script>
    window.onload = function () {
      console.log(" window.onload called");
      const startDate = document.getElementById("startDate");
      const endDate = document.getElementById("endDate");
      const errorMessage = document.getElementById("dateError");

      function validateDate() {
        const start = new Date(startDate.value);
        const end = new Date(endDate.value);
        if (start && end && end < start) {
          errorMessage.textContent = "End Date must be after Start Date.";
        } else {
          errorMessage.textContent = "";
        }
      }

      startDate.addEventListener("input", validateDate);
      endDate.addEventListener("input", validateDate);
    };

	    	
    </script>
    <script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>
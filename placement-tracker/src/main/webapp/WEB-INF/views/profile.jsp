
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cpt.model.Profile" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<% 
    Profile pr = (Profile) request.getAttribute("profile");
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Profile - Placement Tracking System</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
  	<style>
  		.profile-image{
			width: 100px; 
		    height: 100px; 
		    border-radius: 50%; 
		    object-fit: cover;
		}
  	</style>
    
</head>
<body>
<jsp:include page="./shared/sidebar_student_dashboard.jsp" />
        <!-- Main Content -->
        <div class="main-content">
        	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
            <div class="section-title">Update Profile</div>
            <div class="container">
                <% if (error != null) { %>
                    <div class="alert error"><%= error %></div>
                <% } %>
                <% if (success != null) { %>
                    <div class="alert success"><%= success %></div>
                <% } %>
                <% if (pr == null) { %>
                    <div class="alert error">Profile not found.</div>
                <% } else { %>
                    <form id="profileForm" action="<%= request.getContextPath() %>/student/updateProfile" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="prfUsrId" value="<%= pr.getPrfUsrId() %>">
                        <div class="form-group">
                            <label for="prfImage">Profile Image</label>
                            <% if (pr.getPrfImage() != null && pr.getPrfImage().length > 0) { %>
                                <img class="profile-image" src="data:image/jpeg;base64,<%= java.util.Base64.getEncoder().encodeToString(pr.getPrfImage()) %>" alt="Profile Image">
                            <% } else { %>
                                <p>No profile image available</p>
                            <% } %>
                            <input type="file" id="prfImage" name="prfImage" accept="image/*" disabled>
                        </div>
                        <div class="form-group">
                            <label for="rollNo">Roll Number</label>
                            <input type="text" id="rollNo" value="<%= pr.getPrfUsrId() %>" disabled>
                        </div>
                        <div class="form-group">
                            <label for="fullName">Full Name</label>
                            <input type="text" id="fullName" value="<%= pr.getName() %>" disabled>
                        </div>
                        <div class="form-group">
                            <label for="collegeName">College Name</label>
                            <input type="text" id="collegeName" value="<%= pr.getCollegeName() %>" disabled>
                        </div>
                        <div class="form-group">
                            <label for="branchName">Branch</label>
                            <input type="text" id="branchName" value="<%= pr.getBranchName() %>" disabled>
                        </div>
                        <div class="form-group editable">
                            <label for="collegeEmail">College Email</label>
                            <input type="email" id="collegeEmail" name="collegeEmail" value="<%= pr.getEmail() %>" disabled required>
                        </div>
                        <div class="form-group">
                            <label for="cgpa">CGPA</label>
                            <input type="number" step="0.01" min="0" max="10" id="cgpa" value="<%= pr.getPrfGpa() %>" disabled>
                        </div>
                        <div class="form-group">
                            <label for="backlogs">Backlogs</label>
                            <input type="number" min="0" id="backlogs" value="<%= pr.getBacklogs() %>" disabled>
                        </div>
                        <div class="form-group">
                            <label for="gender">Gender</label>
                            <input type="text" id="gender" value="<%= pr.getGender() != null ? pr.getGender() : "" %>" disabled>
                        </div>
                        <div class="form-group editable">
                            <label for="contact">Contact</label>
                            <input type="text" id="contact" name="contact" value="<%= pr.getPrfContact() != null ? pr.getPrfContact() : "" %>" disabled>
                        </div>
                        <div class="form-group editable">
                            <label for="skills">Skills</label>
                            <input type="text" id="skills" name="skills" value="<%= pr.getSkills() != null ? pr.getSkills() : "" %>" disabled>
                        </div>
                        <div class="form-group editable">
                            <label for="github">GitHub URL</label>
                            <input type="text" id="github" name="githubUrl" value="<%= pr.getGithubUrl() != null ? pr.getGithubUrl() : "" %>" disabled>
                        </div>
                        <button type="button" class="popup-btn btn-primary update-btn" onclick="enableEdit()">Update Profile</button>
                        <button type="submit" class="popup-btn btn-info" style="display: none;" id="saveBtn">Save Changes</button>
                    </form>
                <% } %>
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

    <script>
        function enableEdit() {
            const editableFields = document.querySelectorAll('.form-group.editable input');
            editableFields.forEach(field => field.disabled = false);
            document.getElementById('prfImage').disabled = false;
            document.querySelector('.update-btn').style.display = 'none';
            document.getElementById('saveBtn').style.display = 'inline-block';
        }

        function toggleSidebar() {
            document.querySelector('.sidebar').classList.toggle('closed');
            document.querySelector('.main-content').classList.toggle('sidebar-closed');
            document.querySelector('.header').classList.toggle('sidebar-closed');
        }

        function toggleDropdown() {
            var dropdown = document.getElementById("profileDropdown");
            dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
        }

        document.addEventListener("click", function(event) {
            var profile = document.querySelector(".profile-section");
            if (!profile.contains(event.target)) {
                document.getElementById("profileDropdown").style.display = "none";
            }
        });
    </script>
    <script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>
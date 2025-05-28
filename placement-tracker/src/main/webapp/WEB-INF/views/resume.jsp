<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cpt.model.*, com.cpt.util.MessagesDTO" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<%
    String message = (String)request.getAttribute("message");
    Resume resume = (Resume) request.getAttribute("resume");
    String error = request.getParameter("error");
    System.out.println(request.getAttribute("usr_id"));
    
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Resume - Placement Tracking System</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <style>
    .container {
        background: #fff;
        padding: 25px 30px;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
        max-width: 900px;
        margin: auto;
    }

    h2 {
        color: #333;
        margin-bottom: 20px;
    }

    label {
        font-weight: 600;
    }

    input, select {
        width: 100%;
        padding: 10px;
        margin: 8px 0 16px;
        border: 1px solid #ccc;
        border-radius: 6px;
        font-size: 15px;
    }

    button, input[type="submit"] {
    background-color: #3b5998; /* Matches blue sidebar */
    color: #fff;
    border: none;
    padding: 10px;
    font-size: 16px;
    cursor: pointer;
    border-radius: 5px;
    transition: background-color 0.3s ease;
	}
	
	button:hover, input[type="submit"]:hover {
	    background-color: #2e4676; /* Slightly darker on hover */
	}
	
	#phaseButtons button {
	    background-color: #f0f0f0;
	    color: #3b5998;
	    border: 1px solid #3b5998;
	    margin: 5px;
	}
	
	
	#phaseButtons button:hover {
	    background-color: #3b5998;
	    color: #fff;
	}

	#sendShortlistedBtn, #tempShortlisted, #evaluate {
	    background-color: #3b5998;
	    color: #fff;
	    border: none;
	    padding: 10px 20px;
	    font-size: 15px;
	    cursor: pointer;
	    border-radius: 5px;
	    transition: background-color 0.3s ease;
	}
	
	#sendShortlistedBtn:hover, #tempShortlisted:hover, #evaluate:hover {
	    background-color: #2e4676;
	}

    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }

    th, td {
        padding: 10px;
        border: 1px solid #ddd;
        text-align: left;
    }

    .scroll-container {
        max-height: 200px;
        overflow-y: auto;
        border: 1px solid #ccc;
        padding: 10px;
        margin-top: 20px;
        background: #fafafa;
    }

    .hidden {
        display: none;
    }

    

    #downloadBtn {
        margin-top: 20px;
        background-color: #27ae60;
    }

    #downloadBtn:hover {
        background-color: #1e8449;
    }

    .upload {
        margin-top: 30px;
    }

    #shortlistedContainer table {
        background: #fff;
        border-radius: 8px;
    }

    #shortlistedContainer th {
        background-color: #f0f0f0;
    }

    .button-row {
        display: flex;
    }
    .button-row a{
    	text-decoration:none;
    	padding:10px;
    }
    
</style>
</head>
<body>
<jsp:include page="./shared/sidebar_student_dashboard.jsp" />">
        <!-- Main Content -->
        <div class="main-content">
        	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    		</button>
            <h3 class="section-title">Manage Resume</h3>
            <div class="container">
                <% if (message != null) { %>
                    <div class="alert success">
                        <%= message %>
                    </div>
                <% } %>
				<% if (error != null) { %>
                    <div class="alert error">
                        <%= error %>
                    </div>
                <% } %>
                <div class="form-group">
                    <h3>Upload Resume</h3>
                    <form action="addresume" method="post" enctype="multipart/form-data">
                        <input type="file" name="resume" accept=".pdf,.doc,.docx" required>
                        <button type="submit" class="popup-btn btn-primary">Upload</button>
                    </form>
                </div>

                <div class="form-group">
                    <h3>Current Resume</h3>
                    <% 
                    	String usrId = String.valueOf(session.getAttribute("userId"));
                    if (resume != null && resume.getResumeData() != null) { %>
                        <p>File: <%= resume.getResFileName() %></p><br><br>
                        <div class="button-row">
                        	<a href="view/<%= usrId %>" target="_blank" class="popup-btn btn-primary">View Resume</a>
	                        <form action="deleteresume/<%= usrId %>" method="post" style="margin-top: 10px;">
	                            <button type="submit" class="popup-btn btn-danger" style="background-color: #e74c3c;">Delete Resume</button>
	                        </form>
                        </div>
                    <% } else { %>
                        <p>No resume uploaded.</p>
                    <% } %>
                </div>
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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cpt.model.Student, com.cpt.model.Resume, java.util.*" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<% 
    String error = request.getParameter("error");
	String message = request.getParameter("message"); 
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apply to Drive - Placement Tracking System</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <script type="text/javascript" src="${jsUrl}"></script>
    <style>
        /* Internal CSS for Apply to Drive JSP */
        .main-content {
            background: linear-gradient(135deg, #f5f7fb 0%, #eef1f8 100%);
            animation: fadeIn 0.5s ease-in-out;
        }
        
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }
        
        .section-title {
            position: relative;
            padding-bottom: 10px;
            margin-bottom: 30px;
            color: #1f2a44;
            font-weight: 700;
            letter-spacing: 0.5px;
        }
        
        .section-title::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            width: 50px;
            height: 3px;
            background: linear-gradient(90deg, #4361ee, #3b82f6);
            border-radius: 3px;
            animation: expandWidth 1s ease-in-out forwards;
        }
        
        @keyframes expandWidth {
            from { width: 0; }
            to { width: 50px; }
        }
        
        .container {
            background-color: white;
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
            padding: 30px;
            transition: all 0.3s ease;
        }
        
        .container:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 30px rgba(0, 0, 0, 0.12);
        }
        
        .alert {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            position: relative;
            animation: slideIn 0.5s ease-in-out;
        }
        
        @keyframes slideIn {
            from { opacity: 0; transform: translateX(-20px); }
            to { opacity: 1; transform: translateX(0); }
        }
        
        .alert.error {
            background-color: #fee2e2;
            border-left: 4px solid #ef4444;
            color: #b91c1c;
        }
        
        .alert.success {
            background-color: #d1fae5;
            border-left: 4px solid #10b981;
            color: #065f46;
        }
        
        .form-group {
            margin-bottom: 25px;
            position: relative;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #4b5563;
            transition: all 0.3s ease;
            font-size: 0.9rem;
        }
        
        .form-group input {
            width: 100%;
            padding: 12px 15px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 1rem;
            transition: all 0.3s ease;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        }
        
        .form-group input:focus {
            border-color: #4361ee;
            box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.15);
            outline: none;
        }
        
        .form-group input:hover {
            border-color: #9ca3af;
        }
        
        .btn {
            background: linear-gradient(135deg, #4361ee 0%, #3b82f6 100%);
            color: white;
            border: none;
            padding: 12px 25px;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            display: inline-block;
            text-align: center;
            letter-spacing: 0.5px;
            box-shadow: 0 4px 6px rgba(67, 97, 238, 0.25);
        }
        
        .btn:hover {
            background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
            transform: translateY(-3px);
            box-shadow: 0 6px 10px rgba(67, 97, 238, 0.3);
        }
        
        .btn:active {
            transform: translateY(-1px);
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
            <div class="section-title">Apply to Placement Drive</div>
            <div class="container">
                <% if (error != null) { %>
                    <div class="alert error">
                        <% if ("already_applied".equals(error)) { %>
                            You have already applied to this drive.
                        <% } else { %>
                            <%= error %>
                        <% } %>
                    </div>
                <% } %>
                
                                <% if (message != null) { %>
                    <div class="alert success">
                            <%= message %>
                    </div>
                <% } %>

                <% Student student = (Student)request.getAttribute("student"); 
                	System.out.println(student);
                %>
                <% Integer pldId = (Integer)request.getAttribute("pldId"); %>
                <% Integer cmpId = (Integer)request.getAttribute("cmpId"); %>
                <form action="submitApplication" method="post">
                    <div class="form-group">
                        <label for="usrId">User ID</label>
                        <input type="text" id="usrId" name="usrId" value="<%= student != null ? student.getRollNo() : "" %>" >
                    </div>
                    <div class="form-group">
                        <label for="name">Name</label>
                        <input type="text" id="name" name="name" value="<%= student.getFullName() != null ? student.getFullName() : "" %>">
                    </div>
                    <div class="form-group">
                        <label for="cgpa">CGPA</label>
                        <input type="text" id="cgpa" name="cgpa" value="<%= student.getCgpa() %>">
                    </div>
                    <div class="form-group">
                        <label for="backlogs">Backlogs</label>
                        <input type="number" id="backlogs" name="backlogs" value="<%= student.getBacklogs()%>">
                    </div>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="text" id="email" name="email" value="<%= student.getCollegeEmail() != null ? student.getCollegeEmail() : "" %>">
                    </div>
                    
                    <input type="hidden" name="_csrf" value="dummy-csrf-token">
                    <button type="submit" class="btn">Submit Application</button>
                </form>
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
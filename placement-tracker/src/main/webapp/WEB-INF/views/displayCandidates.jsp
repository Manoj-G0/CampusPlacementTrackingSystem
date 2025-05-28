<%@ page import="java.util.List" %>
<%@ page import="com.cpt.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<html>
<head>
    <title>Applications</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
        }



        h1 {
            text-align: center;
            color: #333;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: white;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #007bff;
            color: white;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        td {
            color: #333;
        }
    </style>
</head>
<body>
<jsp:include page="./shared/hr_sidebar.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <h1>Applications for Placement Drive</h1>

    <%
        List<ApplicationDTO> applications = (List<ApplicationDTO>) request.getAttribute("applications");
        Integer pldId = (Integer) request.getAttribute("pldId");
    %>

    <table>
        <thead>
            <tr>
                <th>Roll No</th>
                <th>Full Name</th>
                <th>Branch</th>
                <th>CGPA</th>
                <th>Status</th>
                <th>College Email</th>
            </tr>
        </thead>
        <tbody>
        <%
            if (applications != null) {
                for (ApplicationDTO app : applications) {
        %>
            <tr>
                <td><%= app.getAppUsrId() %></td>
                <td><%= app.getFullName() %></td>
                <td><%= app.getBranchId() %></td>
                <td><%= app.getCgpa() %></td>
                <td><%= app.getStatus() %></td>
                <td><%= app.getCollegeEmail() %></td>
            </tr>
        <%
                }
            } else {
        %>
            <tr><td colspan="6">No applications found.</td></tr>
        <%
            }
        %>
        </tbody>
    </table>
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

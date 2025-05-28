<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.cpt.model.*" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Round Wise Shortlisted Candidates - Placement Tracker</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${cssUrl}" />
    <style>
        .main-content {
            padding: 40px;
        }
        h1, h2 {
            color: #1e2a3c;
            text-align: center;
            margin-bottom: 20px;
        }
        .dropdown-conts {
            margin-bottom: 30px;
            display: flex;
            justify-content: center;
        }
        select {
            padding: 12px;
            font-size: 16px;
            border: 1px solid #e5e7eb;
            border-radius: 8px;
            width: 300px;
            background: #fff;
            color: #1e2a3c;
            transition: border-color 0.3s ease, box-shadow 0.3s;
            animation: slideIn 0.3s ease;
        }
        select:focus {
            border-color: #3b82f6;
            box-shadow: 0 0 8px rgba(59, 130, 246, 0.2);
            outline: none;
        }
        .data-table {
            animation: fadeIn 0.6s ease;
        }
        .data-table th {
            background: linear-gradient(180deg, #3b82f6 0%, #60a5fa 100%);
            color: #fff;
        }
        .data-table tr:hover {
            background: #f0f7ff;
            transform: scale(1.01);
        }
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        @keyframes slideIn {
            from { opacity: 0; transform: translateX(10px); }
            to { opacity: 1; transform: translateX(0); }
        }
    </style>
</head>
<body>
<jsp:include page="./shared/hr_sidebar.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <h1>Round Wise Shortlisted Candidates for <%= request.getAttribute("pdName") %></h1>
    <%
        int pldId = (int) request.getAttribute("pldId");
        List<HiringPhase> phases = (List<HiringPhase>) request.getAttribute("phases");
        List<RoundWiseShortlisted> roundwiseList = (List<RoundWiseShortlisted>) request.getAttribute("roundwiseList");
    %>
    <h2>Hiring Phases</h2>
    <table class="data-table">
        <thead>
            <tr>
                <th>Phase Name</th>
                <th>Sequence</th>
                <th>Score</th>
                <th>Threshold</th>
            </tr>
        </thead>
        <tbody>
            <% for (HiringPhase phase : phases) { %>
            <tr>
                <td><%= phase.getHphName() %></td>
                <td><%= phase.getHphSequence() %></td>
                <td><%= phase.getTotalScore() %></td>
                <td><%= phase.getThresholdScore() %></td>
            </tr>
            <% } %>
        </tbody>
    </table>
    <h2>Round-wise Shortlisted Candidates</h2>
    <div class="dropdown-conts">
        <select id="roundFilter" name="round">
            <option value="">-- All Rounds --</option>
            <% for (HiringPhase phase : phases) { %>
            <option value="<%= phase.getHphName() %>"><%= phase.getHphName() %></option>
            <% } %>
        </select>
    </div>
    <table class="data-table">
        <thead>
            <tr>
                <th>Phase Name</th>
                <th>Student ID</th>
                <th>Student Name</th>
                <th>Score</th>
            </tr>
        </thead>
        <tbody id="roundwiseTableBody">
            <% if (roundwiseList != null && !roundwiseList.isEmpty()) { %>
                <% for (RoundWiseShortlisted student : roundwiseList) { %>
                <tr>
                    <td><%= student.getPhase_name() %></td>
                    <td><%= student.getStudentId() %></td>
                    <td><%= student.getStudent_name() %></td>
                    <td><%= student.getScore() %></td>
                </tr>
                <% } %>
            <% } else { %>
                <tr>
                    <td colspan="4">No students found.</td>
                </tr>
            <% } %>
        </tbody>
    </table>
    <a href="javascript:history.back()">Go Back</a>
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
document.getElementById("roundFilter").addEventListener("change", function () {
    const round = this.value;
    const pldId = '<%= request.getAttribute("pldId") %>';
    const xhr = new XMLHttpRequest();
    xhr.open("GET", "../getShortlistedAjax?pld_id=" + pldId + "&round=" + encodeURIComponent(round), true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("roundwiseTableBody").innerHTML = xhr.responseText;
        }
    };
    xhr.send();
});
</script>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>
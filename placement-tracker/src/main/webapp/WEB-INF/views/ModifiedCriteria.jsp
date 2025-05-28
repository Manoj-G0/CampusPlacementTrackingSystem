<%@ page import="java.util.*, com.cpt.model.Phase, com.cpt.model.PhaseData" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<%
List<Phase> PhaseList = (List<Phase>) request.getAttribute("PhaseList");
Integer selectedHphId = (Integer) request.getAttribute("selectedHphid");
Integer selectedPldId = (Integer) request.getAttribute("selectedPldid");
Double threshold = (Double) request.getAttribute("threshold");
List<PhaseData> studentDetails = (List<PhaseData>) request.getAttribute("studentDetails");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Phase Selection</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f7f9fc;
            margin: auto 0;
            padding: 0;
        }

       
        .section-title {
            font-size: 24px;
            margin-bottom: 20px;
            font-weight: bold;
            color: #333;
        }

        form {
            margin-bottom: 25px;
        }

        label {
            font-weight: bold;
            display: block;
            margin-bottom: 8px;
        }

        select, input[type="number"] {
            width: 100%;
            max-width: 400px;
            padding: 10px;
            font-size: 16px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 6px;
            background-color: #f9f9f9;
        }

        input[type="submit"] {
            background-color:  #4361ee;
            color: white;
            border: none;
            padding: 12px 20px;
            font-size: 16px;
            border-radius: 6px;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #4361ee;
        }

        .info-box {
            background-color: #eef5ff;
            border-left: 5px solid #3f87f5;
            padding: 15px 20px;
            margin: 20px 0;
            border-radius: 6px;
        }

        .info-box p {
            margin: 5px 0;
        }

        .no-students-msg {
            background-color: #ffe6e6;
            border-left: 5px solid #f44336;
            padding: 15px 20px;
            margin-top: 20px;
            border-radius: 6px;
            color: #b30000;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: #fff;
            border-radius: 6px;
            overflow: hidden;
        }

        th {
            background-color: #4361ee;
            color: white;
            padding: 12px;
            text-align: left;
        }

        td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        @media (max-width: 768px) {
            .main-content {
                margin-left: 0;
                padding: 20px;
            }

            table, th, td {
                font-size: 14px;
            }
        }
    </style>
</head>
<body>
<jsp:include page="./shared/hr_sidebar.jsp" />

<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Modify Threshold</div>

    <form action="modify-threshold" method="get">
        <label for="hph_id">Phase:</label>
        <select name="hph_id" id="hph_id" onchange="this.form.submit()">
            <option value="">--Select--</option>
            <%
            if (PhaseList != null) {
                for (Phase phase : PhaseList) {
            %>
                <option value="<%= phase.getHphId() %>"
                    <%= (selectedHphId != null && phase.getHphId() == selectedHphId) ? "selected" : "" %>>
                    <%= phase.getPhaseName() %>
                </option>
            <%
                }
            }
            %>
        </select>
    </form>

    <%
    if (selectedHphId != null) {
    %>
    <form action="modify-threshold" method="post">
        <label for="modifiedThreshold">Modify Threshold:</label>
        <input type="number" step="0.01" id="modifiedThreshold" name="modifiedThreshold" value="<%= threshold %>" required>
        <input type="hidden" name="hph_id" value="<%= selectedHphId %>">
        <input type="submit" value="Update Threshold">
    </form>
    <%
    }

    if (studentDetails != null && !studentDetails.isEmpty()) {
    %>
    <div class="info-box">
        <p><strong>Threshold:</strong> <%= threshold %></p>
        <p><strong>Total Students in Phase:</strong> <%= studentDetails.size() %></p>
    </div>

    <table>
        <thead>
            <tr>
                <th>Student ID</th>
                <th>Name</th>
                <th>Score</th>
            </tr>
        </thead>
        <tbody>
            <%
            for (PhaseData student : studentDetails) {
            %>
            <tr>
                <td><%= student.getStudentId() %></td>
                <td><%= student.getStudentName() %></td>
                <td><%= student.getScore() %></td>
            </tr>
            <%
            }
            %>
        </tbody>
    </table>
    <%
    } else if (selectedHphId != null) {
    %>
    <div class="no-students-msg">
        No students are available for this phase. So change the Threshold.
    </div>
    <%
    }
    %>
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

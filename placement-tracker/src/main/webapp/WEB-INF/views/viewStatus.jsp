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
    <title>Shortlisted Candidates - Placement Tracker</title>
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
        .resume-btn, .action-btn {
            padding: 8px 16px;
            border: none;
            border-radius: 25px;
            color: #fff;
            font-size: 14px;
            cursor: pointer;
            transition: transform 0.3s ease, box-shadow 0.3s;
            animation: slideIn 0.3s ease;
        }
        .resume-btn {
            background: linear-gradient(45deg, #3b82f6, #60a5fa);
        }
        .action-btn.offer {
            background: linear-gradient(45deg, #22c55e, #16a34a);
        }
        .action-btn.reject {
            background: linear-gradient(45deg, #dc2626, #b91c1c);
        }
        .resume-btn:hover, .action-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
        }
        textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #e5e7eb;
            border-radius: 8px;
            font-size: 14px;
            transition: border-color 0.3s ease;
        }
        textarea:focus {
            border-color: #3b82f6;
            outline: none;
        }
        strong {
            color: #1e2a3c;
            font-weight: 600;
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
    <h1>Shortlisted Candidates for <%= request.getAttribute("pdName") %></h1>
    <%
        int pldId = (int) request.getAttribute("pldId");
        List<Shortlisted> finalList = (List<Shortlisted>) request.getAttribute("finalShortlisted");
    %>
    <h2>Final Shortlisted Candidates</h2>
    <table class="data-table">
        <thead>
            <tr>
                <th>Student ID</th>
                <th>Student Name</th>
                <th>Total Score</th>
                <th>Resume</th>
                <th>Remarks</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <% for (Shortlisted student : finalList) { %>
            <tr>
                <td><%= student.getStudentId() %></td>
                <td><%= student.getStudentName() %></td>
                <td><%= student.getScore() %></td>
                <td>
                    <a href="view/<%= student.getStudentId() %>" target="_blank" class="resume-btn">View Resume</a>
                </td>
                <td>
                    <% String currentStatus = student.getStatus(); %>
                    <% if (currentStatus == null || currentStatus.equalsIgnoreCase("PENDING")) { %>
                    <textarea name="remarks_<%= student.getStudentId() %>"
                              id="remarks_<%= student.getStudentId() %>"
                              rows="2"
                              placeholder="Enter remarks here..."></textarea>
                    <% } else { %>
                        <%= student.getRemarks() != null ? student.getRemarks() : "N/A" %>
                    <% } %>
                </td>
                <td>
                    <% if (currentStatus == null || currentStatus.equalsIgnoreCase("PENDING")) { %>
                    <form action="../updateApplicationStatus" method="post"
                          style="display: inline;"
                          onsubmit="return attachRemarks(this, '<%= student.getStudentId() %>')">
                        <input type="hidden" name="pld_id" value="<%= pldId %>" />
                        <input type="hidden" name="student_id" value="<%= student.getStudentId() %>" />
                        <input type="hidden" name="status" value="OFFR" />
                        <input type="hidden" name="remarks" />
                        <button type="submit" class="action-btn offer">Offer</button>
                    </form>
                    <form action="../updateApplicationStatus" method="post"
                          style="display: inline;"
                          onsubmit="return attachRemarks(this, '<%= student.getStudentId() %>')">
                        <input type="hidden" name="pld_id" value="<%= pldId %>" />
                        <input type="hidden" name="student_id" value="<%= student.getStudentId() %>" />
                        <input type="hidden" name="status" value="REJD" />
                        <input type="hidden" name="remarks" />
                        <button type="submit" class="action-btn reject">Reject</button>
                    </form>
                    <% } else { %>
                        <% if (currentStatus.equalsIgnoreCase("OFFR")) { %>
                            <strong>Offered</strong>
                        <% } else { %>
                            <strong>Rejected</strong>
                        <% } %>
                    <% } %>
                </td>
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
    function attachRemarks(form, studentId) {
        const textarea = document.getElementById("remarks_" + studentId);
        if (textarea) {
            form.querySelector("input[name='remarks']").value = textarea.value;
        }
        return true;
    }
</script>
 <script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>
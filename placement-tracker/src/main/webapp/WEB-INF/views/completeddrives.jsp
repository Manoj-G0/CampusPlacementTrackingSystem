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
    <title>Completed Drives - Placement Tracker</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${cssUrl}" />
    <style>
        .drives-section {
            margin-bottom: 40px;
            animation: fadeIn 0.8s ease;
        }
        .drives-list {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
        }
        .drive-card {
            background: #fff;
            border-radius: 12px;
            padding: 20px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
            transition: transform 0.3s ease, box-shadow 0.3s;
            cursor: pointer;
        }
        .drive-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
        }
        .drive-details {
            display: flex;
            align-items: center;
        }
        .company-logo {
            font-size: 24px;
            margin-right: 15px;
            color: #3b82f6;
        }
        .drive-info h4 {
            font-size: 18px;
            color: #1e2a3c;
            margin: 0 0 10px;
        }
        .drive-meta span {
            font-size: 14px;
            color: #64748b;
            margin-right: 15px;
        }
        .drive-status {
            font-size: 14px;
            padding: 6px 12px;
            border-radius: 25px;
            font-weight: 600;
            animation: slideIn 0.3s ease;
            background: #d1fae5;
            color: #065f46;
        }
        .btn {
            background: linear-gradient(45deg, #3b82f6, #60a5fa);
            border: none;
            padding: 8px 16px;
            border-radius: 25px;
            color: #fff;
            font-weight: 600;
            transition: transform 0.3s ease, box-shadow 0.3s;
            margin: 5px;
        }
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
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
    <div class="section-title">Completed Drives</div>
    <div class="drives-section">
        <div class="drives-list">
            <%
                List<PlacementDrive> drives = (List<PlacementDrive>) request.getAttribute("drives");
                if (drives != null && !drives.isEmpty()) {
                    for (PlacementDrive d : drives) {
            %>
            <div class="drive-card">
                <div class="drive-details">
                    <div class="company-logo">
                        <i class="fas fa-building"></i>
                    </div>
                    <div class="drive-info">
                        <h4><%= d.getName() %></h4>
                        <div class="drive-meta">
                            <span><i class="fas fa-calendar-alt"></i> <%= d.getEndDate() %></span>
                        </div>
                    </div>
                </div>
                <div class="drive-status">Completed</div>
            </div>
            <div class="action-buttons">
                <form action="getCandidates/<%= d.getPldId() %>" method="get" style="display: inline;">
                    <button type="submit" class="btn">View Candidates</button>
                </form>
                <form action="getRoundWiseShortListedCandidates/<%= d.getPldId() %>" method="get" style="display: inline;">
                    <button type="submit" class="btn">Round Wise Status</button>
                </form>
                <form action="getShortListedCandidates/<%= d.getPldId() %>" method="get" style="display: inline;">
                    <button type="submit" class="btn">View Status</button>
                </form>
            </div>
            <%
                    }
                } else {
            %>
            <p>No completed drives available.</p>
            <%
                }
            %>
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
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>
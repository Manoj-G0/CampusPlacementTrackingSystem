<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cpt.model.*, java.util.*, java.text.SimpleDateFormat" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HR Dashboard - Placement Tracker</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${cssUrl}" />
    <style>
        .dashboard-stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }
       /*  .stat-card {
            background: #fff;
            border-radius: 12px;
            padding: 20px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            text-align: center;
            transition: transform 0.3s ease, box-shadow 0.3s;
            cursor: pointer;
            animation: fadeIn 0.6s ease;
            position: relative;
            overflow: hidden;
        }
        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
        }
        .stat-card .icon {
            font-size: 24px;
            margin-bottom: 10px;
        }
        .stat-card .icon.blue { color: #3b82f6; }
        .stat-card .icon.green { color: #22c55e; }
        .stat-card .icon.purple { color: #9333ea; }
        .stat-card h3 {
            font-size: 18px;
            color: #1e2a3c;
            margin: 10px 0;
        } */
        .stat-card .number {
            font-size: 28px;
            font-weight: 700;
            color: #3b82f6;
        }
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
        }
        .status-ongoing {
            background: #fef3c7;
            color: #d97706;
        }
        .status-completed {
            background: #d1fae5;
            color: #065f46;
        }
        .action-buttons {
            margin-top: 20px;
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }
        .btn {
            background: linear-gradient(45deg, #3b82f6, #60a5fa);
            border: none;
            padding: 10px 20px;
            border-radius: 25px;
            color: #fff;
            font-weight: 600;
            transition: transform 0.3s ease, box-shadow 0.3s;
            cursor: pointer;
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
        .stat-card .fas{
        	display:flex;
        	justify-content:center;
        	align-items:center;
        }
    </style>
</head>
<body>
<jsp:include page="./shared/hr_sidebar.jsp" />
<div class="main-content">
    <div class="section-title">HR Dashboard</div>
    <div class="dashboard-stats">
        <div class="stat-card">
            <div class="icon blue"><i class="fas fa-file-alt"></i></div>
            <h3>Total Applications</h3>
            <div class="number"><% out.print((Long) request.getAttribute("applicationCount")); %></div>
        </div>
        <div class="stat-card" onclick="window.location.href='../hr/ongoing-drives'">
            <div class="icon green"><i class="fas fa-briefcase"></i></div>
            <h3>Ongoing Drives</h3>
            <div class="number"><% out.print(((List<?>) request.getAttribute("ongoingDrives")).size()); %></div>
        </div>
        <div class="stat-card" onclick="window.location.href='../hr/completed-drives'">
            <div class="icon purple"><i class="fas fa-check-circle"></i></div>
            <h3>Completed Drives</h3>
            <div class="number"><% out.print(((List<?>) request.getAttribute("completedDrives")).size()); %></div>
        </div>
        <div class="stat-card" onclick="window.location.href='../hr/upcoming-drives'">
            <div class="icon purple"><i class="fas fa-bullseye"></i></div>
            <h3>Upcoming Drives</h3>
            <div class="number"><% out.print(((List<?>) request.getAttribute("upcomingDrives")).size()); %></div>
        </div>
    </div>

    <div class="drives-section">
        <div class="section-title">Ongoing Drives</div>
        <div class="drives-list">
            <% 
                List<PlacementDrive> ongoingDrives = (List<PlacementDrive>) request.getAttribute("ongoingDrives");
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                for (PlacementDrive drive : ongoingDrives) {
            %>
            <div class="drive-card" onclick="window.location.href='../hr/getCandidates/<% out.print(drive.getPldId()); %>'">
                <div class="drive-details">
                    <div class="company-logo">
                        <i class="fas fa-building"></i>
                    </div>
                    <div class="drive-info">
                        <h4><% out.print(drive.getName()); %></h4>
                        <div class="drive-meta">
                            <span><i class="fas fa-calendar"></i> <% out.print(sdf.format(drive.getStartDate())); %></span>
                            <span><i class="fas fa-map-marker-alt"></i> Main Campus</span>
                        </div>
                    </div>
                </div>
                <div class="drive-status status-ongoing">Ongoing</div>
            </div>
            <% } %>
        </div>
    </div>
    <div class="drives-section">
        <div class="section-title">Completed Drives</div>
        <div class="drives-list">
            <% 
                List<PlacementDrive> completedDrives = (List<PlacementDrive>) request.getAttribute("completedDrives");
                for (PlacementDrive drive : completedDrives) {
            %>
            <div class="drive-card" onclick="window.location.href='../hr/getCandidates/<% out.print(drive.getPldId()); %>'">
                <div class="drive-details">
                    <div class="company-logo">
                        <i class="fas fa-building"></i>
                    </div>
                    <div class="drive-info">
                        <h4><% out.print(drive.getName()); %></h4>
                        <div class="drive-meta">
                            <span><i class="fas fa-calendar"></i> <% out.print(sdf.format(drive.getStartDate())); %></span>
                            <span><i class="fas fa-map-marker-alt"></i> Main Campus</span>
                        </div>
                    </div>
                </div>
                <div class="drive-status status-completed">Completed</div>
            </div>
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
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>
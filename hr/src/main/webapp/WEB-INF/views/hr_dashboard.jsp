<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cpt.model.HrPlacementDrive,com.cpt.model.HrHrDetails,com.cpt.model.HrRecruitmentStat,java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>HR Dashboard</title>
    <link rel="stylesheet" href="/CPT/resources/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
    <style>
        body { background-color: #ffffff; }
    </style>
</head>
<body>
    <%@ include file="hr_sidebar.jsp" %>
    <%@ include file="header.jsp" %>
    <div class="main-content <%= session.getAttribute("sidebarClosed") != null && (boolean) session.getAttribute("sidebarClosed") ? "sidebar-closed" : "" %>">
        <h2 class="section-title"><%= ((Map<String, String>) request.getAttribute("messages")).get("ongoing.drives.title") %></h2>
        <% HrHrDetails hrDetails = (HrHrDetails) request.getAttribute("hrDetails"); %>
        <div class="container">
            <p>Welcome, <%= hrDetails.getHrName() %> (<%= hrDetails.getDesignation() %>)</p>
        </div>
        <div class="dashboard-stats">
            <% List<HrPlacementDrive> ongoingDrives = (List<HrPlacementDrive>) request.getAttribute("ongoingDrives"); %>
            <% for (HrPlacementDrive drive : ongoingDrives) { %>
                <div class="stat-card">
                    <div class="icon blue"><i class="fas fa-briefcase"></i></div>
                    <h3><%= drive.getPldName() %></h3>
                    <p class="number"><%= drive.getApplicationCount() %> Applications</p>
                    <p>Role: <%= drive.getPldRole() %></p>
                    <p>Package: <%= drive.getPldPackage() %> LPA</p>
                    <a href="/CPT/hr/downloadApplications?pldId=<%= drive.getPldId() %>" class="btn">
                        <%= ((Map<String, String>) request.getAttribute("messages")).get("download.applications") %>
                    </a>
                </div>
            <% } %>
        </div>
        <h2 class="section-title"><%= ((Map<String, String>) request.getAttribute("messages")).get("completed.drives.title") %></h2>
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Role</th>
                        <th>Package</th>
                        <th>End Date</th>
                        <th>Description</th>
                    </tr>
                </thead>
                <tbody>
                    <% List<HrPlacementDrive> completedDrives = (List<HrPlacementDrive>) request.getAttribute("completedDrives"); %>
                    <% for (HrPlacementDrive drive : completedDrives) { %>
                        <tr>
                            <td><%= drive.getPldName() %></td>
                            <td><%= drive.getPldRole() %></td>
                            <td><%= drive.getPldPackage() %> LPA</td>
                            <td><%= drive.getPldEndDate() %></td>
                            <td><%= drive.getCmpDesc() %></td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
        <h2 class="section-title"><%= ((Map<String, String>) request.getAttribute("messages")).get("recruitment.chart.title") %></h2>
        <div class="charts-section">
            <div class="chart-card">
                <div class="chart-header">
                    <h3 class="chart-title">Recruitment Trends</h3>
                </div>
                <div class="chart-container">
                    <canvas id="recruitmentChart"></canvas>
                </div>
            </div>
        </div>
    </div>
    <script>
        const ctx = document.getElementById('recruitmentChart').getContext('2d');
        const stats = [
            <% List<HrRecruitmentStat> stats = (List<HrRecruitmentStat>) request.getAttribute("recruitmentStats"); %>
            <% for (HrRecruitmentStat stat : stats) { %>
                { year: <%= stat.getYear() %>, count: <%= stat.getSelectedCount() %> },
            <% } %>
        ];
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: stats.map(s => s.year),
                datasets: [{
                    label: 'Students Recruited',
                    data: stats.map(s => s.count),
                    backgroundColor: '#4361ee',
                    borderColor: '#3f37c9',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: { beginAtZero: true }
                }
            }
        });
    </script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.cpt.model.*" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Companies - Placement Tracking System</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background: #f3f4f6;
            margin: 0;
            padding: 0;
            display: flex;
            min-height: 100vh;
        }
        .sidebar {
            width: 250px;
            background: #1e2a3c;
            color: #fff;
            transition: width 0.3s;
        }
        .sidebar.collapsed {
            width: 60px;
        }
        .main-content {
            flex-grow: 1;
            padding: 20px;
            transition: margin-left 0.3s;
        }
        .main-content.sidebar-closed {
            margin-left: 60px;
        }
        .section-title {
            font-size: 1.8rem;
            color: #1e2a3c;
            margin-bottom: 20px;
        }
        .container {
            max-width: 100%;
            margin: 0 auto;
        }
        .management-card {
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            overflow: hidden;
        }
        .card-header {
            background: #f0f7ff;
            padding: 15px;
            border-bottom: 1px solid #e5e7eb;
        }
        .card-header h3 {
            margin: 0;
            font-size: 1.5rem;
            color: #1e2a3c;
        }
        .card-body {
            padding: 15px;
            overflow-x: auto;
        }
        .data-table {
            width: 100%;
            border-collapse: collapse;
        }
        .data-table th, .data-table td {
            border: 1px solid #e5e7eb;
            padding: 12px;
            text-align: left;
            font-size: 0.9rem;
        }
        .data-table th {
            background: #f0f7ff;
            color: #1e2a3c;
        }
        .data-table tr {
            transition: background 0.3s, transform 0.2s;
            cursor: pointer;
        }
        .data-table tr:hover {
            background: #f0f7ff;
            transform: scale(1.01);
        }
        .stats-dropdown {
            display: none;
            background: #f8fafc;
            padding: 10px;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            margin: 10px 0;
        }
        .stats-dropdown.active {
            display: block;
            animation: slideIn 0.3s ease;
        }
        .stats-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }
        .stats-table th, .stats-table td {
            border: 1px solid #e5e7eb;
            padding: 10px;
            text-align: left;
            font-size: 0.85rem;
        }
        .stats-table th {
            background: #e0f2fe;
            color: #1e2a3c;
        }
        .stats-table tr:hover {
            background: #e0f2fe;
            cursor: pointer;
        }
        .nested-row {
            display: none;
        }
        .nested-row.active {
            display: table-row;
            animation: slideIn 0.3s ease;
        }
        .nested-table {
            width: 100%;
            border-collapse: collapse;
            background: #f1f5f9;
            margin: 10px 0;
        }
        .nested-table th, .nested-table td {
            border: 1px solid #d1d5db;
            padding: 8px;
            font-size: 0.8rem;
        }
        .nested-table th {
            background: #dbeafe;
        }
        .nested-section {
            margin-bottom: 15px;
        }
        .nested-section h4 {
            margin: 0 0 10px;
            font-size: 1rem;
            color: #1e2a3c;
        }
        @keyframes slideIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
        /* Responsive Design */
        @media (max-width: 768px) {
            .sidebar {
                width: 60px;
            }
            .main-content {
                margin-left: 60px;
            }
            .section-title {
                font-size: 1.5rem;
            }
            .data-table th, .data-table td {
                padding: 8px;
                font-size: 0.8rem;
            }
            .stats-table th, .stats-table td {
                padding: 6px;
                font-size: 0.75rem;
            }
            .nested-table th, .nested-table td {
                padding: 6px;
                font-size: 0.7rem;
            }
        }
        @media (max-width: 480px) {
            .data-table th, .data-table td {
                padding: 6px;
                font-size: 0.7rem;
            }
            .stats-table th, .stats-table td {
                padding: 5px;
                font-size: 0.7rem;
            }
            .nested-table th, .nested-table td {
                padding: 5px;
                font-size: 0.65rem;
            }
        }
    </style>
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />
<div class="main-content">
    <div class="section-title">View Companies</div>
    <div class="container">
        <div class="management-card">
            <div class="card-header">
                <h3>Company Information</h3>
            </div>
            <div class="card-body">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Company ID</th>
                            <th>Company Name</th>
                            <th>Category</th>
                            <th>Description</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            List<CompanyStatsDTO> companyStatsList = (List<CompanyStatsDTO>) request.getAttribute("companyStatsList");
                            if (companyStatsList != null && !companyStatsList.isEmpty()) {
                                for (CompanyStatsDTO stats : companyStatsList) {
                                    Company company = stats.getCompany();
                        %>
                            <tr onclick="toggleStats('stats-<%= company.getCmpId() %>')">
                                <td><%= company.getCmpId() %></td>
                                <td><%= company.getCmpName() %></td>
                                <td><%= company.getCategoryName() %></td>
                                <td><%= company.getCmpDesc() %></td>
                            </tr>
                            <tr>
                                <td colspan="4">
                                    <div class="stats-dropdown" id="stats-<%= company.getCmpId() %>">
                                        <table class="stats-table">
                                            <thead>
                                                <tr>
                                                    <th>Drive Name</th>
                                                    <th>College</th>
                                                    <th>Start Date</th>
                                                    <th>Status</th>
                                                    <th>Package (LPA)</th>
                                                    <th>Role</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <% 
                                                    List<DriveStatsDTO> drives = stats.getDrives();
                                                    if (drives != null && !drives.isEmpty()) {
                                                        for (DriveStatsDTO driveStats : drives) {
                                                            DriveInfo drive = driveStats.getDrive();
                                                %>
                                                    <tr onclick="toggleNestedTable('nested-drive-<%= drive.getPldId() %>')">
                                                        <td><%= drive.getPldName() %></td>
                                                        <td><%= drive.getCollegeName() %></td>
                                                        <td><%= drive.getStartDate() %></td>
                                                        <td><%= drive.getStatus() %></td>
                                                        <td><%= drive.getPackageAmount() / 100000 %></td>
                                                        <td><%= drive.getRoleName() %></td>
                                                    </tr>
                                                    <tr class="nested-row" id="nested-drive-<%= drive.getPldId() %>">
                                                        <td colspan="6">
                                                            <div class="nested-section">
                                                                <h4>Drive Statistics</h4>
                                                                <!-- Rounds Conducted -->
                                                                <div class="nested-section">
                                                                    <h4>Number of Rounds: <%= driveStats.getRoundCount() %></h4>
                                                                </div>
                                                                <!-- Parameters -->
                                                                <div class="nested-section">
                                                                    <h4>Parameters Given</h4>
                                                                    <table class="nested-table">
                                                                        <thead>
                                                                            <tr>
                                                                                <th>Parameter Name</th>
                                                                                <th>Threshold Score</th>
                                                                                <th>Total Score</th>
                                                                            </tr>
                                                                        </thead>
                                                                        <tbody>
                                                                            <% 
                                                                                List<Parameter> parameters = driveStats.getParameters();
                                                                                if (parameters != null && !parameters.isEmpty()) {
                                                                                    for (Parameter param : parameters) {
                                                                            %>
                                                                                <tr>
                                                                                    <td><%= param.getParameterName() %></td>
                                                                                    <td><%= param.getThreshold() %></td>
                                                                                    <td><%= param.getTotalScore() %></td>
                                                                                </tr>
                                                                            <% 
                                                                                    }
                                                                                } else {
                                                                            %>
                                                                                <tr>
                                                                                    <td colspan="3">No parameters defined.</td>
                                                                                </tr>
                                                                            <% 
                                                                                }
                                                                            %>
                                                                        </tbody>
                                                                    </table>
                                                                </div>
                                                                <!-- Criteria -->
                                                                <div class="nested-section">
                                                                    <h4>Screening Criteria</h4>
                                                                    <table class="nested-table">
                                                                        <thead>
                                                                            <tr>
                                                                                <th>Min GPA</th>
                                                                                <th>Max Backlogs</th>
                                                                                <th>Branch</th>
                                                                            </tr>
                                                                        </thead>
                                                                        <tbody>
                                                                            <% 
                                                                                List<Criterion> criteria = driveStats.getCriteria();
                                                                                if (criteria != null && !criteria.isEmpty()) {
                                                                                    for (Criterion criterion : criteria) {
                                                                            %>
                                                                                <tr>
                                                                                    <td><%= criterion.getMinGpa() %></td>
                                                                                    <td><%= criterion.getMinBacklogs() %></td>
                                                                                    <td><%= criterion.getBranchName() %></td>
                                                                                </tr>
                                                                            <% 
                                                                                    }
                                                                                } else {
                                                                            %>
                                                                                <tr>
                                                                                    <td colspan="3">No criteria defined.</td>
                                                                                </tr>
                                                                            <% 
                                                                                }
                                                                            %>
                                                                        </tbody>
                                                                    </table>
                                                                </div>
                                                                <!-- Shortlisted per Round -->
                                                                <div class="nested-section">
                                                                    <h4>Students Shortlisted per Round</h4>
                                                                    <% 
                                                                        List<RoundShortlist> shortlisted = driveStats.getShortlistedPerRound();
                                                                        if (shortlisted != null && !shortlisted.isEmpty()) {
                                                                            for (RoundShortlist round : shortlisted) {
                                                                    %>
                                                                        <div class="nested-section">
                                                                            <h4>Round: <%= round.getHphName() %> (Sequence: <%= round.getSequence() %>)</h4>
                                                                            <table class="nested-table">
                                                                                <thead>
                                                                                    <tr>
                                                                                        <th>Name</th>
                                                                                        <th>Roll No</th>
                                                                                        <th>Email</th>
                                                                                    </tr>
                                                                                </thead>
                                                                                <tbody>
                                                                                    <% 
                                                                                        List<StudentShortlist> students = round.getStudents();
                                                                                        if (students != null && !students.isEmpty()) {
                                                                                            for (StudentShortlist student : students) {
                                                                                    %>
                                                                                        <tr>
                                                                                            <td><%= student.getFullName() %></td>
                                                                                            <td><%= student.getRollNo() %></td>
                                                                                            <td><%= student.getEmail() %></td>
                                                                                        </tr>
                                                                                    <% 
                                                                                            }
                                                                                        } else {
                                                                                    %>
                                                                                        <tr>
                                                                                            <td colspan="3">No shortlisted students.</td>
                                                                                        </tr>
                                                                                    <% 
                                                                                        }
                                                                                    %>
                                                                                </tbody>
                                                                            </table>
                                                                        </div>
                                                                    <% 
                                                                            }
                                                                        } else {
                                                                    %>
                                                                        <p>No rounds defined.</p>
                                                                    <% 
                                                                        }
                                                                    %>
                                                                </div>
                                                                <!-- Finalized Students -->
                                                                <div class="nested-section">
                                                                    <h4>Students Finalized: <%= driveStats.getFinalizedCount() %></h4>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                <% 
                                                        }
                                                    } else {
                                                %>
                                                    <tr>
                                                        <td colspan="6">No placement drives conducted.</td>
                                                    </tr>
                                                <% 
                                                    }
                                                %>
                                            </tbody>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        <% 
                                }
                            } else {
                        %>
                            <tr>
                                <td colspan="4">No companies available.</td>
                            </tr>
                        <% 
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script>
    function toggleStats(id) {
        var stats = document.getElementById(id);
        stats.classList.toggle('active');
    }

    function toggleNestedTable(id) {
        var nestedRows = document.querySelectorAll('#stats-' + id.split('-')[2] + ' .nested-row');
        nestedRows.forEach(function(row) {
            if (row.id !== id) {
                row.classList.remove('active');
            }
        });
        var nestedRow = document.getElementById(id);
        nestedRow.classList.toggle('active');
    }

    function toggleSidebar() {
        document.querySelector('.sidebar').classList.toggle('collapsed');
        document.querySelector('.main-content').classList.toggle('sidebar-closed');
    }
</script>
</body>
</html>
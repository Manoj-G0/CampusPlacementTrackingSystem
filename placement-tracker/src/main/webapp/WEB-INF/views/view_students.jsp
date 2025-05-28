<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.cpt.model.StudentStatsDTO, com.cpt.model.Student, com.cpt.model.DriveInfo" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Students - Placement Tracking System</title>
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
        .action-btn {
            display: inline-flex;
            align-items: center;
            padding: 6px 12px;
            margin: 2px;
            border-radius: 6px;
            text-decoration: none;
            font-size: 0.85rem;
        }
        .btn-edit {
            background: #3b82f6;
            color: #fff;
        }
        .btn-edit:hover {
            background: #2563eb;
        }
        .btn-delete {
            background: #dc2626;
            color: #fff;
        }
        .btn-delete:hover {
            background: #b91c1c;
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
            .action-btn {
                padding: 5px 10px;
                font-size: 0.8rem;
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
            .action-btn {
                padding: 4px 8px;
                font-size: 0.75rem;
            }
        }
    </style>
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />
<div class="main-content">
    <div class="section-title">View Students</div>
    <div class="container">
        <div class="management-card">
            <div class="card-header">
                <h3>Student Information</h3>
            </div>
            <div class="card-body">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Roll No</th>
                            <th>Full Name</th>
                            <th>College Name</th>
                            <th>Branch Name</th>
                            <th>Gender</th>
                            <th>CGPA</th>
                            <th>Backlogs</th>
                            <th>Email</th>
                            <th>GitHub URL</th>
                            <th>Skills</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% 
                            List<StudentStatsDTO> studentStatsList = (List<StudentStatsDTO>) request.getAttribute("studentStatsList");
                            if (studentStatsList != null && !studentStatsList.isEmpty()) {
                                for (StudentStatsDTO stats : studentStatsList) {
                                    Student student = stats.getStudent();
                        %>
                            <tr onclick="toggleStats('stats-<%= student.getRollNo() %>')">
                                <td><%= student.getRollNo() %></td>
                                <td><%= student.getFullName() %></td>
                                <td><%= student.getCollegeName() %></td>
                                <td><%= student.getBranchName() %></td>
                                <td><%= student.getGender() %></td>
                                <td><%= student.getCgpa() %></td>
                                <td><%= student.getBacklogs() %></td>
                                <td><%= student.getCollegeEmail() %></td>
                                <td><a href="<%= student.getGithubUrl() %>" target="_blank"><%= student.getGithubUrl() %></a></td>
                                <td><%= student.getSkills() %></td>
                                <td>
                                    <a href="editStudent/<%= student.getRollNo() %>" class="action-btn btn-edit">
                                        <i class="fas fa-edit"></i> Edit
                                    </a>
                                    <a href="deleteStudent/<%= student.getRollNo() %>" class="action-btn btn-delete">
                                        <i class="fas fa-trash"></i> Delete
                                    </a>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="11">
                                    <div class="stats-dropdown" id="stats-<%= student.getRollNo() %>">
                                        <table class="stats-table">
                                            <thead>
                                                <tr>
                                                    <th>Statistic Type</th>
                                                    <th>Count</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr onclick="toggleNestedTable('nested-attended-<%= student.getRollNo() %>')">
                                                    <td>Attended Drives</td>
                                                    <td><%= stats.getAttendedDrivesCount() %></td>
                                                </tr>
                                                <tr class="nested-row" id="nested-attended-<%= student.getRollNo() %>">
                                                    <td colspan="2">
                                                        <table class="nested-table">
                                                            <thead>
                                                                <tr>
                                                                    <th>Drive Name</th>
                                                                    <th>Company</th>
                                                                    <th>Status</th>
                                                                    <th>Date</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <% 
                                                                    List<DriveInfo> attendedDrives = stats.getAttendedDrives();
                                                                    if (attendedDrives != null && !attendedDrives.isEmpty()) {
                                                                        for (DriveInfo drive : attendedDrives) {
                                                                %>
                                                                    <tr>
                                                                        <td><%= drive.getPldName() %></td>
                                                                        <td><%= drive.getCmpName() %></td>
                                                                        <td><%= drive.getStatus() %></td>
                                                                        <td><%= drive.getPldDate() %></td>
                                                                    </tr>
                                                                <% 
                                                                        }
                                                                    } else {
                                                                %>
                                                                    <tr>
                                                                        <td colspan="4">No attended drives.</td>
                                                                    </tr>
                                                                <% 
                                                                    }
                                                                %>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr onclick="toggleNestedTable('nested-selected-<%= student.getRollNo() %>')">
                                                    <td>Selected Drives</td>
                                                    <td><%= stats.getSelectedCount() %></td>
                                                </tr>
                                                <tr class="nested-row" id="nested-selected-<%= student.getRollNo() %>">
                                                    <td colspan="2">
                                                        <table class="nested-table">
                                                            <thead>
                                                                <tr>
                                                                    <th>Drive Name</th>
                                                                    <th>Company</th>
                                                                    <th>Status</th>
                                                                    <th>Date</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <% 
                                                                    List<DriveInfo> selectedDrives = stats.getSelectedDrives();
                                                                    if (selectedDrives != null && !selectedDrives.isEmpty()) {
                                                                        for (DriveInfo drive : selectedDrives) {
                                                                %>
                                                                    <tr>
                                                                        <td><%= drive.getPldName() %></td>
                                                                        <td><%= drive.getCmpName() %></td>
                                                                        <td><%= drive.getStatus() %></td>
                                                                        <td><%= drive.getPldDate() %></td>
                                                                    </tr>
                                                                <% 
                                                                        }
                                                                    } else {
                                                                %>
                                                                    <tr>
                                                                        <td colspan="4">No selected drives.</td>
                                                                    </tr>
                                                                <% 
                                                                    }
                                                                %>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr onclick="toggleNestedTable('nested-eligible-<%= student.getRollNo() %>')">
                                                    <td>Eligible Drives</td>
                                                    <td><%= stats.getEligibleDrivesCount() %></td>
                                                </tr>
                                                <tr class="nested-row" id="nested-eligible-<%= student.getRollNo() %>">
                                                    <td colspan="2">
                                                        <table class="nested-table">
                                                            <thead>
                                                                <tr>
                                                                    <th>Drive Name</th>
                                                                    <th>Company</th>
                                                                    <th>Date</th>
                                                                    <th>Min CGPA</th>
                                                                    <th>Max Backlogs</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <% 
                                                                    List<DriveInfo> eligibleDrives = stats.getEligibleDrives();
                                                                    if (eligibleDrives != null && !eligibleDrives.isEmpty()) {
                                                                        for (DriveInfo drive : eligibleDrives) {
                                                                %>
                                                                    <tr>
                                                                        <td><%= drive.getPldName() %></td>
                                                                        <td><%= drive.getCmpName() %></td>
                                                                        <td><%= drive.getPldDate() %></td>
                                                                        <td><%= drive.getMinCgpa() %></td>
                                                                        <td><%= drive.getMaxBacklogs() %></td>
                                                                    </tr>
                                                                <% 
                                                                        }
                                                                    } else {
                                                                %>
                                                                    <tr>
                                                                        <td colspan="5">No eligible drives.</td>
                                                                    </tr>
                                                                <% 
                                                                    }
                                                                %>
                                                            </tbody>
                                                        </table>
                                                    </td>
                                                </tr>
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
                                <td colspan="11">No students available.</td>
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
        // Hide all nested tables for this student
        var nestedRows = document.querySelectorAll('#stats-' + id.split('-')[2] + ' .nested-row');
        nestedRows.forEach(function(row) {
            if (row.id !== id) {
                row.classList.remove('active');
            }
        });
        // Toggle the selected nested table
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
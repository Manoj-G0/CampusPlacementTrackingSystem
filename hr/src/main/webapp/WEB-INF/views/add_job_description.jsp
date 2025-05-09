<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Job Description</title>
    <link rel="stylesheet" href="/CPT/resources/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        body { background-color: #ffffff; }
    </style>
</head>
<body>
    <%@ include file="hr_sidebar.jsp" %>
    <%@ include file="header.jsp" %>
    <div class="main-content <%= session.getAttribute("sidebarClosed") != null && (boolean) session.getAttribute("sidebarClosed") ? "sidebar-closed" : "" %>">
        <h2 class="section-title"><%= ((Map<String, String>) request.getAttribute("messages")).get("add.job.description.title") %></h2>
        <% String successMessage = (String) request.getAttribute("successMessage"); %>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (successMessage != null) { %>
            <div class="alert success"><%= successMessage %></div>
        <% } %>
        <% if (errorMessage != null) { %>
            <div class="alert error"><%= errorMessage %></div>
        <% } %>
        <div class="container">
            <form action="/CPT/hr/addJobDescription" method="post">
                <div class="form-group">
                    <label>Drive Name</label>
                    <input type="text" name="pldName" required>
                </div>
                <div class="form-group">
                    <label>Role Offered</label>
                    <input type="text" name="pldRole" required>
                </div>
                <div class="form-group">
                    <label>Package (LPA)</label>
                    <input type="number" step="0.01" name="pldPackage" required>
                </div>
                <div class="form-group">
                    <label>Start Date</label>
                    <input type="date" name="pldStartDate" required>
                </div>
                <div class="form-group">
                    <label>End Date</label>
                    <input type="date" name="pldEndDate" required>
                </div>
                <div class="form-group">
                    <label>Minimum CGPA</label>
                    <input type="number" step="0.01" name="scrMinGpa" required>
                </div>
                <div class="form-group">
                    <label>Maximum Backlogs</label>
                    <input type="number" name="scrMinBacklogs" required>
                </div>
                <div class="form-group">
                    <label>Eligible Branches</label>
                    <select name="scrBrnId" multiple required>
                        <% List<Map<String, Object>> branches = (List<Map<String, Object>>) request.getAttribute("branches"); %>
                        <% for (Map<String, Object> branch : branches) { %>
                            <option value="<%= branch.get("brn_id") %>"><%= branch.get("brn_name") %></option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group">
                    <label>Gender</label>
                    <select name="scrGender">
                        <option value="">All</option>
                        <option value="M">Male</option>
                        <option value="F">Female</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Number of Phases</label>
                    <input type="number" name="phaseCount" id="phaseCount" min="1" required onchange="generatePhaseFields()">
                </div>
                <div id="phaseFields"></div>
                <button type="submit" class="btn"><%= ((Map<String, String>) request.getAttribute("messages")).get("form.submit") %></button>
                <a href="/CPT/hr/dashboard" class="btn"><%= ((Map<String, String>) request.getAttribute("messages")).get("form.cancel") %></a>
            </form>
        </div>
    </div>
    <script>
        function generatePhaseFields() {
            const phaseCount = parseInt(document.getElementById('phaseCount').value) || 0;
            const phaseFields = document.getElementById('phaseFields');
            phaseFields.innerHTML = '';
            for (let i = 1; i <= phaseCount; i++) {
                phaseFields.innerHTML += `
                    <div class="form-group">
                        <label>Phase ${i} Name</label>
                        <input type="text" name="hphName" required>
                    </div>
                    <div class="form-group">
                        <label>Phase ${i} Cutoff Score</label>
                        <input type="number" step="0.01" name="cutoffScore" required>
                    </div>
                `;
            }
        }
    </script>
</body>
</html>
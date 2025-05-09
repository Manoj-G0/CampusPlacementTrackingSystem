<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cpt.model.HrHiringPhase,com.cpt.model.HrStudent,java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Round-Wise Selected Students</title>
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
        <h2 class="section-title"><%= ((Map<String, String>) request.getAttribute("messages")).get("round.wise.students.title") %></h2>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (errorMessage != null) { %>
            <div class="alert error"><%= errorMessage %></div>
        <% } %>
        <% List<HrHiringPhase> phases = (List<HrHiringPhase>) request.getAttribute("phases"); %>
        <% for (HrHiringPhase phase : phases) { %>
            <div class="container">
                <h3><%= phase.getHphName() %> (Phase <%= phase.getHphSequence() %>)</h3>
                <% List<HrStudent> students = phase.getStudents(); %>
                <% if (students.isEmpty()) { %>
                    <p class="note"><%= ((Map<String, String>) request.getAttribute("messages")).get("no.data.round") %></p>
                <% } else { %>
                    <div class="table-container">
                        <table>
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Roll Number</th>
                                    <th>Email</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (HrStudent student : students) { %>
                                    <tr>
                                        <td><%= student.getFullName() %></td>
                                        <td><%= student.getRolNo() %></td>
                                        <td><%= student.getCollegeEmail() %></td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                <% } %>
            </div>
        <% } %>
    </div>
</body>
</html>
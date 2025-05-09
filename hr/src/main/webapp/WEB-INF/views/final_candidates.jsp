<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cpt.model.HrStudent,java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Final Selected Candidates</title>
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
        <h2 class="section-title"><%= ((Map<String, String>) request.getAttribute("messages")).get("final.candidates.title") %></h2>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (errorMessage != null) { %>
            <div class="alert error"><%= errorMessage %></div>
        <% } %>
        <div class="container">
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Contact Number</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% List<HrStudent> candidates = (List<HrStudent>) request.getAttribute("candidates"); %>
                        <% for (HrStudent candidate : candidates) { %>
                            <tr>
                                <td><%= candidate.getFullName() %></td>
                                <td><%= candidate.getCollegeEmail() %></td>
                                <td><%= candidate.getContactNumber() %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
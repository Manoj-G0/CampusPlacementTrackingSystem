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
    <title>Update Company Details - Placement Tracker</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${cssUrl}" />
    <style>
        .form-container {
            background: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            max-width: 600px;
            margin: 20px auto;
            animation: fadeIn 0.6s ease;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            font-weight: 600;
            color: #1e2a3c;
            margin-bottom: 8px;
        }
        .form-group input, .form-group select, .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #e5e7eb;
            border-radius: 8px;
            font-size: 14px;
            transition: border-color 0.3s ease, box-shadow 0.3s;
        }
        .form-group input:focus, .form-group select:focus, .form-group textarea:focus {
            border-color: #3b82f6;
            box-shadow: 0 0 8px rgba(59, 130, 246, 0.2);
            outline: none;
        }
        .submit-btn {
            background: linear-gradient(45deg, #3b82f6, #60a5fa);
            border: none;
            padding: 12px 24px;
            border-radius: 25px;
            color: #fff;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.3s ease, box-shadow 0.3s;
            width: 100%;
        }
        .submit-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
        }
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
    </style>
</head>
<body>
<jsp:include page="./shared/hr_sidebar.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <%
        String hrname = (String) request.getAttribute("hrname");
        List<CompanyCategories> categories = (List<CompanyCategories>) request.getAttribute("companyCategories");
        Company company = (Company) request.getAttribute("company");
    %>
    <div class="form-container">
        <h2>Company Information</h2>
        <form action="updateCompany" method="POST">
            <div class="form-group">
                <label for="companyName">Company Name</label>
                <input type="text" id="companyName" name="cmpName" value="<%= company.getCmpName() %>" required>
            </div>
            <div class="form-group">
                <label for="companyCategory">Company Category</label>
                <select id="companyCategory" name="cmpCctId" required>
                    <option value="" disabled>Select a Category</option>
                    <% for (CompanyCategories c : categories) { %>
                    <option value="<%= c.getId() %>" ><%= c.getName() %></option>
                    <% } %>
                </select>
            </div>
            <div class="form-group">
                <label for="companyDescription">Company Description</label>
                <textarea id="companyDescription" name="cmpDesc" rows="4" required><%= company.getCmpDesc() %></textarea>
            </div>
            <div class="form-group">
                <button type="submit" class="submit-btn">Submit</button>
            </div>
        </form>
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
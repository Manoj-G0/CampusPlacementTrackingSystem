<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cpt.model.*" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Company Edit Form</title>
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />


	<%
		//String hrname = (String) request.getAttribute("hrname");
		List<CompanyCategories>	categories = (List<CompanyCategories>)request.getAttribute("categories");
		Company company = (Company)request.getAttribute("company");
	%>
	
	<div class="main-content">
		<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Edit Company Details</div>
    <form action="../update-company" method="post">
        	<div class="form-group">
				<label for="companyName">Company Name</label> <input type="text"
					id="companyName" name="cmpName" value="<%=company.getCmpName()%>" required>
			</div>
 			<input type="hidden" name="cmpId" id="cmpId" value="<%=company.getCmpId() %>"/>
			<div class="form-group">
				<label for="companyCategory">Company Category</label> <select
					id="companyCategory" name="cmpCctId" required>
					<option value="" disabled selected>Select a Category</option>
					<%for(CompanyCategories c:categories){ %>
					<option value="<%=c.getId()%>"><%=c.getName()%></option>
					<%} %>
				</select>
			</div>

			<div class="form-group">
				<label for="companyDescription">Company Description</label>
				<textarea id="companyDescription" name="cmpDesc" rows="4"
					required><%=company.getCmpDesc()%></textarea>
			</div>
        <button type="submit" class="popup-btn btn-primary">Edit Company</button>
    </form>
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
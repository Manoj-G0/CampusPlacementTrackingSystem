<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cpt.model.*, java.util.*" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Company - Placement Tracker</title>
   <link rel="stylesheet" type="text/css" href="${cssUrl}" />
</head>
<body>
<%
		
		List<CompanyCategories>	categories = (List<CompanyCategories>)request.getAttribute("categories");

	%>
<jsp:include page="./shared/sidebar_admin.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Add Company</div>
    <form action="add-company" method="post">
        <div class="form-group">
            <label for="cmpName">Company Name</label>
            <input type="text" id="cmpName" name="cmpName" required>
        </div>
        <div class="form-group">
            <label for="cmpDesc">Description</label>
            <textarea id="cmpDesc" name="cmpDesc"></textarea>
        </div>
        <div class="form-group">
				<label for="companyCategory">Company Category</label> <select
					id="companyCategory" name="cmpCctId" required>
					<option value="" disabled selected>Select a Category</option>
					<%for(CompanyCategories c:categories){ %>
					<option value="<%=c.getId()%>"><%=c.getName()%></option>
					<%} %>
				</select>
			</div>
        <button type="submit" class="popup-btn btn-primary">Add Company</button>
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
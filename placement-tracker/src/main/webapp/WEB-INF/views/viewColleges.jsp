<%@ page import="java.util.*" %>
<%@ page import="com.cpt.model.College" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html>
<head>
    <title>View College</title>
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />
<div class="main-content" >
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
<div class="management-card">
        <div class="card-header">
            <h3>Edit College Info</h3>
        </div>
 <div class="card-body">
            <table class="data-table">
                <thead>
                    <tr>
			            <th>College Name</th>
			            <th>Address</th>
			            <th>Contact Details</th>
			            <th> Action </th>
                    </tr>
                </thead>
                <tbody>
        <% 
            ArrayList<College> colleges = (ArrayList<College>) request.getAttribute("colleges");
            if (colleges != null && !colleges.isEmpty()) {
                for (College college : colleges) {
        %>
            <tr>
                <td><%= college.getClgName() %></td>
                <td><%= college.getClgAddress() %></td>
                <td><%= college.getClgContact() %></td>
                <td>
                	 <a href="editcollege/<%=college.getClgId()%>" class="action-btn btn-edit"> <i class="fas fa-edit"></i></a>
                     <a href="deletecollege/<%=college.getClgId()%>" class="action-btn btn-delete">
                         <i class="fas fa-trash"></i>
                     </a>
                </td>
            </tr>
        <% 
                }
            } else {
        %>
            <tr>
                <td colspan="4">No Colleges available.</td>
            </tr>
        <% 
            }
        %>
	        </tbody>
	    </table>
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

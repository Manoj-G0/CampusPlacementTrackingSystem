<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cpt.model.*"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manage Company Teams</title>
<style>
body {
    font-family: Arial, sans-serif;
    background-color: #f4f7f9;
    margin: 0;
    padding: 0;
}



h2, h3 {
    color: #002147; /* Dark navy */
    margin-bottom: 20px;
}

table {
    border-collapse: collapse;
    width: 100%;
    background-color: #fff;
    margin-bottom: 30px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.05);
}

th {
    background-color: #001f4d; /* Navy blue */
    color: #ffffff;
    padding: 12px;
    font-weight: bold;
    text-transform: uppercase;
}

td {
    border: 1px solid #ddd;
    padding: 10px;
    color: #333;
}

tr:nth-child(even) {
    background-color: #f1f1f1;
}

tr:hover {
    background-color: #e0e7ff;
}

form {
    background-color: #fefefe;
    padding: 20px;
    border: 1px solid #ddd;
    max-width: 500px;
    box-shadow: 0 0 5px rgba(0, 0, 0, 0.05);
    border-radius: 5px;
}

form label {
    display: inline-block;
    width: 100px;
    margin-bottom: 10px;
    font-weight: bold;
    color: #002147;
}

form input[type="text"] {
    width: calc(100% - 110px);
    padding: 8px;
    border: 1px solid #ccc;
    margin-bottom: 15px;
    border-radius: 4px;
    font-size: 14px;
}

form input[type="submit"] {
    background-color: #001f4d; /* Navy blue */
    color: white;
    padding: 10px 20px;
    border: none;
    cursor: pointer;
    border-radius: 4px;
    font-size: 16px;
}

form input[type="submit"]:hover {
    background-color: #003366;
}
</style>

</head>
<body>
<jsp:include page="./shared/hr_sidebar.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
	<h2>Company Teams Management</h2>

	<!-- Display existing teams -->
	<table>
		<thead>
			<tr>
				<th>Company Name</th>
				<th>Team ID</th>
				<th>Team Name</th>
				<th>Contact</th>
			</tr>
		</thead>
		<tbody>
			<%
			Company company = (Company) session.getAttribute("company");
			List<CompanyTeam> companyTeams = (List<CompanyTeam>) request.getAttribute("companyTeams");
			if (companyTeams != null && !companyTeams.isEmpty()) {
				for (CompanyTeam team : companyTeams) {
			%>
			<tr>
				<td><%=company.getCmpName()%></td>
				<td><%=team.getCtmId()%></td>
				<td><%=team.getCtmName()%></td>
				<td><%=team.getCtmContact()%></td>
				
			</tr>
			<%
			}
			} else {
			%>
			<tr>
				<td colspan="5">No teams found.</td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>

	<h3>Add New Company Team</h3>
	<form action="addCompanyTeam" method="post">
		<input type="hidden" name="ctmCmpId" value="<%=company.getCmpId() %>">
		<br> <label>Team Name:</label> <input type="text"
			name="ctmName" required><br>
		<br> <label>Contact:</label> <input type="text"
			name="ctmContact" required><br>
		<br> <input type="submit" value="Add Team">
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
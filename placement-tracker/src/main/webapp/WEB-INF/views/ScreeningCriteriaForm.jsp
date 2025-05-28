<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List,com.cpt.model.Branch,com.cpt.model.PlacementDrive"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Screening Criteria Form</title>
    <style>
.section-title {
    font-size: 1.5rem;
    color: #0059b3;
    margin-bottom: 30px;
    margin-left:380px;
}

.cont{
width:100%;
margin-left:50px;
align-items:center;
justify-content:center;
}

.form-group {
margin-left:250px;
    margin-bottom: 20px;
    display: flex;
    flex-direction: column; /* Stack the label and input vertically */
}

label {
    display: block;
    font-weight: bold;
    margin-bottom: 8px;
}

input[type="number"],
select {
    width: 100%;
    padding: 10px;
    margin-top: 5px;
    border-radius: 4px;
    border: 1px solid #ddd;
    font-size: 1rem;
    box-sizing: border-box;
}

input[type="number"]:focus,
select:focus,
button:focus {
    outline: none;
    border-color: #0059b3;
}



button:hover {
    background-color: #3548b6;
}

button:disabled {
    background-color: #ccc;
    cursor: not-allowed;
}

/* Flexbox for round fields */
.round-fields {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    gap: 10px;
    margin-bottom: 15px;
}

.round-fields input {
    width: calc(33.33% - 10px); /* Each input takes up one-third of the container */
}

/* For responsive design */
@media (max-width: 600px) {
    .main-content {
        padding: 15px;
        width: 90%;
    }

    .section-title {
        font-size: 1.2rem;
    }

    label {
        font-size: 0.9rem;
    }

    input[type="number"],
    select,
    button {
        font-size: 0.9rem;
        padding: 8px;
    }

    .round-fields {
        flex-direction: column; /* Stack round fields vertically on smaller screens */
    }

    .round-fields input {
        width: 100%; /* Make input fields take up full width on mobile */
    }
}

    </style>
</head>
<body>
<jsp:include page="./shared/hr_sidebar.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Screening Criteria</div>
    <form action="submitCriteriaForm" method="post">
    <div class="cont">
        <div class="form-group">
            <label for="branch">Select Branch:</label>
            <select name="branch" id="branch" required>
                <option value="">Select Branch</option>
                <%
                    List<Branch> branches = (List<Branch>) request.getAttribute("branches");
                    if (branches != null) {
                        for (Branch b : branches) {
                %>
                <option value="<%=b.getBrnId()%>"><%=b.getBrnName()%></option>
                <%
                        }
                    } 
                %>
            </select>
        </div>
         <div class="form-group">
        <label for="branch">Select placement drive:</label>
        <select name="pld_id" id="pld_id" required>
                <option value="">Select placement drive </option>
        <%
    List<PlacementDrive> drives = (List<PlacementDrive>) request.getAttribute("drives");

    if (drives != null) {
        for (PlacementDrive drive : drives) {
%>
	
    <option value="<%= drive.getPldId() %>"><%= drive.getName() %></option>
<%
        }
    }
%>
</select>
</div>
        <div class="form-group">
            <label for="mincgpa">Minimum CGPA:</label>
            <input type="number" step="0.01" id="mincgpa" name="minCgpa" required>
        </div>

        <div class="form-group">
            <label for="backlogs">Backlogs:</label>
            <input type="number" id="backlogs" name="backlogs" required>
        </div>

        <div class="form-group">
            <label for="gender">Gender:</label>
            <select id="gender" name="gender" required>
                <option value="O">Select Gender</option>
                <option value="M">Male</option>
                <option value="F">Female</option>
            </select>
        </div>
        <%
		        String errorMessage = (String) request.getAttribute("errorMessage");
		        if (errorMessage != null) {
		    %>
		    <div>
		        <p style="color: red; font-weight: bold; margin-left:80px;"><%= errorMessage %></p>
		    </div>
		    <% } %>
		    <br>
        <div class="form-group">
            <button type="submit" class="popup-btn btn-primary">Submit</button>
             
        </div>
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

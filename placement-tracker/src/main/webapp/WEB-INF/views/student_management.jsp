<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.cpt.model.Student, com.cpt.util.MessagesDTO" %>
<% MessagesDTO mg = (MessagesDTO) request.getAttribute("messageDTO"); %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<html>
<head>
    <title>Student Management</title>
    <style>
        :root {
            --primary-color: #003366;
            --secondary-color: #ffffff;
            --accent-color: #0059b3;
            --error-color: #f8d7da;
            --success-color: #d4edda;
            --text-dark: #333333;
        }

        body {
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--secondary-color);
            color: var(--text-dark);
        }

        .main-content {
            padding: 20px;
            margin-left: 250px;
        }

        h2 {
            color: var(--primary-color);
            text-align: center;
        }

        .btn {
            padding: 10px 20px;
            background-color: var(--accent-color);
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .btn:hover {
            background-color: #004080;
        }

        .btn-danger {
            background-color: #dc3545;
        }

        .btn-danger:hover {
            background-color: #c82333;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .file-input {
            display: block;
            margin: 10px 0;
        }

        .message {
            padding: 10px;
            background-color: var(--success-color);
            color: #155724;
            border-left: 4px solid #28a745;
            margin: 10px 0;
        }

        .error {
            padding: 10px;
            background-color: var(--error-color);
            color: #721c24;
            border-left: 4px solid #dc3545;
            margin: 10px 0;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: var(--primary-color);
            color: white;
        }

        input, select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
            background-color: #fff;
        }

        input:focus, select:focus {
            border-color: var(--accent-color);
            outline: none;
        }

        .non-editable {
            background-color: #f4f4f4;
            pointer-events: none;
        }

        .editable {
            background-color: #fff;
            pointer-events: auto;
        }

        .no-data {
            padding: 15px;
            text-align: center;
            background-color: #ffeeba;
            color: #856404;
            border-radius: 5px;
            margin-top: 20px;
        }

        @media (max-width: 768px) {
            body {
                margin: 0;
            }

            .main-content {
                margin: 0;
                padding: 15px;
            }

            table, thead, tbody, th, td, tr {
                display: block;
            }

            tr {
                margin-bottom: 15px;
            }

            td {
                padding-left: 50%;
                position: relative;
            }

            td::before {
                content: attr(data-label);
                position: absolute;
                left: 10px;
                top: 10px;
                font-weight: bold;
            }

            th {
                display: none;
            }

            .btn {
                width: 100%;
                margin-top: 10px;
            }
        }
    </style>
    <script>
    
        function editRow(index) {
            var row = document.getElementById("row_" + index);
            var inputs = row.getElementsByTagName("input");
            var selects = row.getElementsByTagName("select");
            for (var i = 0; i < inputs.length; i++) {
                inputs[i].classList.remove("non-editable");
                inputs[i].classList.add("editable");
            }
            for (var i = 0; i < selects.length; i++) {
                selects[i].classList.remove("non-editable");
                selects[i].classList.add("editable");
            }
            var cells = row.getElementsByTagName("td");
            cells[cells.length - 1].innerHTML = '<button type="button" class="btn" onclick="saveRow(' + index + ')">Save</button>';
        }

        function saveRow(index) {
            var row = document.getElementById("row_" + index);
            var inputs = row.getElementsByTagName("input");
            var selects = row.getElementsByTagName("select");
            for (var i = 0; i < inputs.length; i++) {
                inputs[i].classList.remove("editable");
                inputs[i].classList.add("non-editable");
            }
            for (var i = 0; i < selects.length; i++) {
                selects[i].classList.remove("editable");
                selects[i].classList.add("non-editable");
            }
            var cells = row.getElementsByTagName("td");
            cells[cells.length - 1].innerHTML = '<button type="button" class="btn" onclick="editRow(' + index + ')">Edit</button> ' +
                                               '<button type="button" class="btn btn-danger" onclick="deleteRow(' + index + ')">Delete</button>';
        }

        function deleteRow(index) {
            var row = document.getElementById("row_" + index);
            row.parentNode.removeChild(row);
        }

        async function validateAndSave() {
            try {
                var saveButton = document.getElementById("saveButton");
                var loadingIndicator = document.getElementById("loadingIndicator");
                var messageDiv = document.getElementById("messageDiv");

                // Check if saveButton exists
                if (!saveButton) {
                    messageDiv.innerHTML = '<div class="error">Error: Save button not found. Please ensure students are loaded.</div>';
                    return;
                }

                saveButton.disabled = true;
                loadingIndicator.style.display = "inline";
                messageDiv.innerHTML = "";

                var rows = document.getElementsByTagName("tr");
                var students = [];

                // Start from 1 to skip header row
                for (var i = 1; i < rows.length; i++) {
                    var index = i - 1;
                    var rollNo = document.getElementById("rollNo_" + index).value.trim();
                    var fullName = document.getElementById("fullName_" + index).value.trim();
                    var branchId = document.getElementById("branchId_" + index).value.trim();
                    var collegeId = document.getElementById("collegeId_" + index).value.trim();
                    var gender = document.getElementById("gender_" + index).value.trim();
                    var status = document.getElementById("status_" + index).value.trim();
                    var cgpa = document.getElementById("cgpa_" + index).value.trim();
                    var backlogs = document.getElementById("backlogs_" + index).value.trim();
                    var collegeEmail = document.getElementById("collegeEmail_" + index).value.trim();

                    // Client-side validation
                    if (!rollNo || !fullName || !branchId || !collegeId || !gender || !status || !cgpa || !backlogs || !collegeEmail) {
                        messageDiv.innerHTML = '<div class="error">All fields are required for student ' + (index + 1) + '.</div>';
                        saveButton.disabled = false;
                        loadingIndicator.style.display = "none";
                        return;
                    }
                    if (isNaN(parseInt(branchId)) || parseInt(branchId) < 1) {
                        messageDiv.innerHTML = '<div class="error">Branch ID must be a positive integer for student ' + (index + 1) + '.</div>';
                        saveButton.disabled = false;
                        loadingIndicator.style.display = "none";
                        return;
                    }
                    if (isNaN(parseInt(collegeId)) || parseInt(collegeId) < 1) {
                        messageDiv.innerHTML = '<div class="error">College ID must be a positive integer for student ' + (index + 1) + '.</div>';
                        saveButton.disabled = false;
                        loadingIndicator.style.display = "none";
                        return;
                    }
                    if (isNaN(parseFloat(cgpa)) || parseFloat(cgpa) < 0 || parseFloat(cgpa) > 10) {
                        messageDiv.innerHTML = '<div class="error">CGPA must be a number between 0 and 10 for student ' + (index + 1) + '.</div>';
                        saveButton.disabled = false;
                        loadingIndicator.style.display = "none";
                        return;
                    }
                    if (isNaN(parseInt(backlogs)) || parseInt(backlogs) < 0) {
                        messageDiv.innerHTML = '<div class="error">Backlogs must be a non-negative integer for student ' + (index + 1) + '.</div>';
                        saveButton.disabled = false;
                        loadingIndicator.style.display = "none";
                        return;
                    }
                    if (!collegeEmail.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) {
                        messageDiv.innerHTML = '<div class="error">Invalid email format for student ' + (index + 1) + '.</div>';
                        saveButton.disabled = false;
                        loadingIndicator.style.display = "none";
                        return;
                    }

                    var student = {
                        rollNo: rollNo,
                        fullName: fullName,
                        branchId: parseInt(branchId),
                        collegeId: parseInt(collegeId),
                        gender: gender,
                        status: status,
                        cgpa: parseFloat(cgpa),
                        backlogs: parseInt(backlogs),
                        collegeEmail: collegeEmail
                    };
                    students.push(student);
                }

                if (students.length === 0) {
                    messageDiv.innerHTML = '<div class="error">No students to save.</div>';
                    saveButton.disabled = false;
                    loadingIndicator.style.display = "none";
                    return;
                }

                // Fetch call to save students
                const response = await fetch('saveStudents', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json;charset=UTF-8'
                    },
                    body: JSON.stringify(students)
                });

                saveButton.disabled = false;
                loadingIndicator.style.display = "none";

                if (response.ok) {
                    const result = await response.json();
                    if (result.status === "success") {
                        messageDiv.innerHTML = '<div class="message">' + result.message + '</div>';
                        // Clear table
                        var table = document.getElementById("studentTable");
                        while (table.rows.length > 1) {
                            table.deleteRow(1);
                        }
                    } else {
                        messageDiv.innerHTML = '<div class="error">' + result.message + '</div>';
                    }
                } else {
                    messageDiv.innerHTML = '<div class="error">Server error: ' + response.status + ' - ' + response.statusText + '</div>';
                }
            } catch (error) {
                var messageDiv = document.getElementById("messageDiv");
                messageDiv.innerHTML = '<div class="error">Client error: ' + error.message + '</div>';
                var saveButton = document.getElementById("saveButton");
                if (saveButton) saveButton.disabled = false;
                var loadingIndicator = document.getElementById("loadingIndicator");
                if (loadingIndicator) loadingIndicator.style.display = "none";
            }
        }
    </script>
</head>
<body>

<jsp:include page="./shared/sidebar_admin.jsp" />
	<div class="main-content">
		<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <h2>Student Management</h2>

    <div id="messageDiv">
        <% if (request.getAttribute("message") != null) { %>
            <div class="message"><%= request.getAttribute("message") %></div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>
    </div>

    <div class="form-group">
        <a href="downloadTemplate" class="btn">Download CSV Template</a>
    </div>

    <form action="uploadStudents" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="file">Upload Student Data (CSV, TXT, XLS):</label>
            <input type="file" id="file" name="file" accept=".csv,.txt,.xls" class="file-input" required>
            <button type="submit" class="btn">Upload</button>
        </div>
    </form>

    <% List<Student> students = (List<Student>) request.getAttribute("students"); %>
    <% if (students != null && !students.isEmpty()) { %>
        <table id="studentTable">
            <tr>
                <th>Roll No</th>
                <th>Full Name</th>
                <th>Branch ID</th>
                <th>College ID</th>
                <th>Gender</th>
                <th>Status</th>
                <th>CGPA</th>
                <th>Backlogs</th>
                <th>College Email</th>
                <th>Actions</th>
            </tr>
            <% for (int i = 0; i < students.size(); i++) { %>
                <% Student student = students.get(i); %>
                <tr id="row_<%= i %>">
                    <td>
                        <input type="text" id="rollNo_<%= i %>" name="students[<%= i %>].rollNo" value="<%= student.getRollNo() %>" class="non-editable">
                    </td>
                    <td>
                        <input type="text" id="fullName_<%= i %>" name="students[<%= i %>].fullName" value="<%= student.getFullName() %>" class="non-editable">
                    </td>
                    <td>
                        <input type="number" id="branchId_<%= i %>" name="students[<%= i %>].branchId" value="<%= student.getBranchId() %>" class="non-editable">
                    </td>
                    <td>
                        <input type="number" id="collegeId_<%= i %>" name="students[<%= i %>].collegeId" value="<%= student.getClgId() %>" class="non-editable">
                    </td>
                    <td>
                        <select id="gender_<%= i %>" name="students[<%= i %>].gender" class="non-editable">
                            <option value="Male" <%= student.getGender().equals("Male") ? "selected" : "" %>>Male</option>
                            <option value="Female" <%= student.getGender().equals("Female") ? "selected" : "" %>>Female</option>
                            <option value="Other" <%= student.getGender().equals("Other") ? "selected" : "" %>>Other</option>
                        </select>
                    </td>
                    <td>
                        <select id="status_<%= i %>" name="students[<%= i %>].status" class="non-editable">
                            <option value="Active" <%= student.getStatus().equals("Active") ? "selected" : "" %>>Active</option>
                            <option value="Inactive" <%= student.getStatus().equals("Inactive") ? "selected" : "" %>>Inactive</option>
                        </select>
                    </td>
                    <td>
                        <input type="number" step="0.01" id="cgpa_<%= i %>" name="students[<%= i %>].cgpa" value="<%= student.getCgpa() %>" class="non-editable">
                    </td>
                    <td>
                        <input type="number" id="backlogs_<%= i %>" name="students[<%= i %>].backlogs" value="<%= student.getBacklogs() %>" class="non-editable">
                    </td>
                    <td>
                        <input type="email" id="collegeEmail_<%= i %>" name="students[<%= i %>].collegeEmail" value="<%= student.getCollegeEmail() %>" class="non-editable">
                    </td>
                    <td>
                        <button type="button" class="btn" onclick="editRow(<%= i %>)">Edit</button>
                        <button type="button" class="btn btn-danger" onclick="deleteRow(<%= i %>)">Delete</button>
                    </td>
                </tr>
            <% } %>
        </table>
        <div style="text-align: center; margin-top: 20px;">
            <button type="button" class="btn" id="saveButton" onclick="validateAndSave()">Add Students</button>
            <span id="loadingIndicator" style="display: none;">Loading...</span>
        </div>
    <% } else { %>
        <div class="no-data"><%= mg.getStudentFail() %></div>
    <% } %>
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


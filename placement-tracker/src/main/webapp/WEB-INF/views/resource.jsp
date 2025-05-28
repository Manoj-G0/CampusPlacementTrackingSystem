<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<html>
<head>
    <title>Resource Management</title>
    <style>
        .main-content {
            padding: 40px;
        }
        h1, h2 {
            color: #1e2a3c;
            text-align: center;
            margin-bottom: 20px;
        }
        .dropdown-conts {
            margin-bottom: 30px;
            display: flex;
            justify-content: center;
        }
        select {
            padding: 12px;
            font-size: 16px;
            border: 1px solid #e5e7eb;
            border-radius: 8px;
            width: 300px;
            background: #fff;
            color: #1e2a3c;
            transition: border-color 0.3s ease, box-shadow 0.3s;
            animation: slideIn 0.3s ease;
        }
        select:focus {
            border-color: #3b82f6;
            box-shadow: 0 0 8px rgba(59, 130, 246, 0.2);
            outline: none;
        }
        .data-table {
            animation: fadeIn 0.6s ease;
        }
        .data-table th {
            background: linear-gradient(180deg, #3b82f6 0%, #60a5fa 100%);
            color: #fff;
        }
        .data-table tr:hover {
            background: #f0f7ff;
            transform: scale(1.01);
        }
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        @keyframes slideIn {
            from { opacity: 0; transform: translateX(10px); }
            to { opacity: 1; transform: translateX(0); }
        }
    </style>
    </style>
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
<h3 class="section-title">Resources</h3>

<button onclick="location.href='rescrud'" class="popup-btn btn-primary">Create Resource</button>
<br><br><br>
<table class="data-table" id="resourceTable">
    <thead>
    <tr>
        <th>Resource ID</th>
        <th>College Name</th>
        <th>Branch Name</th>
        <th>Capacity</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody></tbody>
</table>
</div>

<script>
    function fetchResources() {
        const xhr = new XMLHttpRequest();
        xhr.open("GET", "getAllResources", true);
        xhr.onload = function () {
            if (xhr.status === 200) {
                const data = JSON.parse(xhr.responseText);
                console.log(data);
                const tbody = document.getElementById("resourceTable").getElementsByTagName("tbody")[0];
                tbody.innerHTML = "";
                data.forEach(r => {
                    const row = tbody.insertRow();
                    row.innerHTML =
                        "<td>" + r.resourceId + "</td>" +
                        "<td contenteditable='true'>" + r.clgName + "</td>" +
                        "<td contenteditable='true'>" + r.brnName + "</td>" +
                        "<td contenteditable='true'>" + r.resourceCapacity + "</td>" +
                        "<td>" +
                        "<button onclick='editResource(this)' class='action-btn btn-edit'><i class='fas fa-edit'></i></button>" +
                        "<button onclick='deleteResource(" + r.resourceId + ")' class='action-btn btn-delete'><i class='fas fa-trash'></i></button>" +
                        "</td>";
                });
            }
        };
        xhr.send();
    }

    function editResource(btn) {
        const row = btn.parentNode.parentNode;
        const resource = {
            resourceId: parseInt(row.cells[0].textContent),
            clgName: row.cells[1].textContent,
            brnName: row.cells[2].textContent,
            resourceCapacity: parseInt(row.cells[3].textContent)
        };

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "edit", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onload = () => {
            if (xhr.status === 200) {
                alert("Updated successfully!");
                fetchResources();
            }
        };
        xhr.send(JSON.stringify(resource));
    }

    function deleteResource(resourceId) {
        if (!confirm("Are you sure you want to delete?")) return;
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "deleteRes", true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onload = () => {
            if (xhr.status === 200) {
                alert("Deleted successfully!");
                fetchResources();
            }
        };
        xhr.send(JSON.stringify({ resourceId }));
    }

    window.onload = fetchResources;
</script>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>

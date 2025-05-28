<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />

<html lang="en">
<head>
<meta charset="UTF-8">
<title>Round Wise Student Selection Status</title>
<style>
    body {
        font-family: 'Arial', sans-serif;
        background-color: #f4f7fa;
        color: #333;
        margin: 0;
        padding: 0;
    }

    .main-content-roundData {
        margin: 0 auto; /* assumes sidebar width is ~250px */
        padding: 40px 20px;
        display: flex;
        flex-direction: column;
        align-items: center;
    }

    h2, h3 {
        color: #2c3e50;
        text-align: center;
        margin-bottom: 20px;
    }

    .dropdown-conts {
        margin-bottom: 30px;
        width: 150%;
        display: flex;
        justify-content: center;
    }

    select {
        padding: 10px 14px;
        font-size: 16px;
        border: 1px solid #ccc;
        border-radius: 6px;
        width: 300px;
        appearance: none;
        background-color: white;
        color: #333;
        background-repeat: no-repeat;
        background-position: right 10px center;
        background-size: 10px 6px;
        transition: border-color 0.3s ease;
        box-shadow: 0 2px 6px rgba(0,0,0,0.05);
    }

    select:focus {
        outline: none;
        border-color: #3498db;
        box-shadow: 0 0 4px rgba(52, 152, 219, 0.5);
    }

    table {
        width: 100%;
        max-width: 1000px;
        border-collapse: collapse;
        background-color: #fff;
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        margin-top: 20px;
    }

    th, td {
        padding: 12px;
        border: 1px solid #ddd;
        text-align: center;
        font-size: 14px;
        line-height: 1.6;
    }

    th {
        background-color: #3498db;
        color: white;
        font-weight: bold;
        text-transform: uppercase;
    }

    tr:nth-child(even) {
        background-color: #f9f9f9;
    }

    tr:hover {
        background-color: #f1f1f1;
        cursor: pointer;
    }

    .status-pass {
        color: green;
        font-weight: bold;
        text-transform: uppercase;
    }

    .status-fail {
        color: red;
        font-weight: bold;
        text-transform: uppercase;
    }

    button {
        padding: 8px 12px;
        background-color: #3498db;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        transition: all 0.3s ease;
        font-size: 14px;
    }

    button:hover {
        background-color: #2980b9;
        transform: scale(1.05);
    }

    button:active {
        transform: scale(0.95);
    }

    @media (max-width: 768px) {
        .main-content-roundData {
            margin-left: 0;
            padding: 20px;
        }

        select {
            width: 100%;
        }

        table {
            font-size: 12px;
            width: 100%;
        }

        th, td {
            padding: 8px;
        }

        button {
            font-size: 12px;
            padding: 6px 10px;
        }
    }
</style>

<script>
        window.onload = function () {
            fetch("driveslist")
                .then(res => res.json())
                .then(data => {
                    const select = document.getElementById("driveSelect");
                    data.forEach(function (d) {
                        const opt = document.createElement("option");
                        opt.value = d.pldId;
                        opt.innerText = d.name;
                        select.appendChild(opt);
                    });
                });
        };

        function loadRoundData() {
            const pldId = document.getElementById("driveSelect").value;
            if (!pldId) return;

            fetch("roundData/byDrive?pldId=" + pldId)
                .then(function (res) {
                    return res.json();
                })
                .then(function (data) {
                    const tableBody = document.querySelector("tbody");
                    const theadRow = document.querySelector("thead tr");
                    const tfootRow = document.querySelector("tfoot tr");

                    const roundHeaders = data.roundNames || [];
                    theadRow.innerHTML = "<th>Student ID</th>" +
                        "<th>Student Name</th>" +
                        roundHeaders.map(function (r) {
                            return "<th>" + (r || "-") + "</th>";
                        }).join("") +
                        "<th>Actions</th>";

                    tableBody.innerHTML = "";

                    const students = data.studentRoundStatus || {};

                    Object.entries(students).forEach(function ([studentId, roundData]) {
                        if (!studentId || !roundData) return;

                        const studentName = roundData.name || "N/A";

                        const statusCells = roundHeaders.map(function (round) {
                            if (!round) return "<td>-</td>"; // Handle null or undefined round
                            const status = roundData[round];
                            if (status == null || status === "null") {
                                return "<td>-</td>";
                            } else if (status === "Selected") {
                                return "<td class='status-pass'>✔</td>";
                            } else if (status === "Rejected") {
                                return "<td class='status-fail'>❌</td>";
                            } else {
                                return "<td>-</td>";
                            }
                        }).join("");

                        const rowHtml = "<tr>" +
                            "<td>" + studentId + "</td>" +
                            "<td>" + studentName + "</td>" +
                            statusCells +
                            "<td><button onclick='toggleDrilldown(\"" + studentId + "\", " + pldId + ")'>View Details</button></td>" +
                            "</tr>" +
                            "<tr id='details-" + studentId + "' style='display: none;'>" +
                            "<td colspan='" + (roundHeaders.length + 3) + "' id='details-cell-" + studentId + "'></td>" +
                            "</tr>";

                        tableBody.insertAdjacentHTML("beforeend", rowHtml);
                    });

                    // Add the phase counts row using the precomputed counts from roundStats.phaseDetails
                    const phaseDetails = data.roundStats.phaseDetails || [];
                    const phaseCountsRow = "<tr class='count-row'>" +
                        "<td colspan='2'>Round-wise Student Count</td>" +
                        roundHeaders.map(function (round) {
                            if (!round) return "<td>-</td>"; // Handle null or undefined round
                            const phase = phaseDetails.find(p => p.phase_name === round);
                            const count = phase ? phase.student_count : 0;
                            return "<td>" + round + ": " + count + " students</td>";
                        }).join("") +
                        "<td></td>" +
                        "</tr>";

                    tableBody.insertAdjacentHTML("beforeend", phaseCountsRow);

                    // Update the footer with the total number of students from roundStats
                    const totalStudents = data.roundStats.totalStudents || 0;
                    tfootRow.innerHTML = "<tr class='total-students'>" +
                        "<td>Total Students: " + totalStudents + "</td>" +
                        "<td colspan='" + (roundHeaders.length + 2) + "'></td>" +
                        "</tr>";
                })
                .catch(function (error) {
                    console.error("Error loading round data:", error);
                   
                });
        }

        function toggleDrilldown(studentId, pldId) {
            const row = document.getElementById('details-' + studentId);
            const cell = document.getElementById('details-cell-' + studentId);

            if (!row || !cell) return;

            if (row.style.display === 'none') {
                fetch('student/drilldown?studentId=' + studentId + '&pldId=' + pldId)
                    .then(function (res) {
                        return res.json();
                    })
                    .then(function (data) {
                        if (!data || data.length === 0) {
                            cell.innerHTML = "<p>No details available for this student.</p>";
                            row.style.display = '';
                            return;
                        }

                        let html = '';
                        let currentRound = '';

                        data.forEach(function (p) {
                            if (!p || !p.phaseName || !p.parameter) return;

                            if (p.phaseName !== currentRound) {
                                if (currentRound !== '') html += '</table><br>';
                                currentRound = p.phaseName;
                                html += "<h4>" + currentRound + "</h4>";
                                html += '<table border="1" width="100%"><tr><th>Parameter</th><th>Score</th><th>Threshold</th><th>Status</th></tr>';
                            }

                            const score = parseFloat(p.score);
                            const threshold = parseFloat(p.paramThreshold);
                            const passed = !isNaN(score) && !isNaN(threshold) && score >= threshold;
                            const icon = passed ? '&#10004;' : '&#10060;';
                            const color = passed ? 'green' : 'red';

                            html += "<tr>" +
                                "<td>" + (p.parameter || '-') + "</td>" +
                                "<td>" + (p.score != null ? p.score : '-') + "</td>" +
                                "<td>" + (p.paramThreshold != null ? p.paramThreshold : '-') + "</td>" +
                                "<td style='color:" + color + "; font-weight:bold'>" + icon + "</td>" +
                                "</tr>";
                        });

                        html += '</table>';
                        cell.innerHTML = html;
                        row.style.display = '';
                    });
            } else {
                row.style.display = 'none';
                cell.innerHTML = '';
            }
        }
    </script>
</head>

<body>
	<jsp:include page="./shared/hr_sidebar.jsp" />
	<div class="main-content">
		<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
		<div class="main-content-roundData">
	<h2>Select Placement Drive</h2>
	<div class="dropdown-conts">
		<select id="driveSelect" onchange="loadRoundData()">
			<option value="">-- Select Drive --</option>
		</select>
	</div>
	<h2>Round Wise Student Selection Status</h2>

	<table>
		<thead>
			<tr>
				<th>Student ID</th>
				<th>Student Name</th>
				<!-- Round names will be injected here -->
				<th>Actions</th>
			</tr>
		</thead>
		<tbody id="studentsTableBody">
			<!-- Populated via JS -->
		</tbody>
	
	</table>
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
	</div>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cpt.model.*, java.util.*, java.text.SimpleDateFormat" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard_student.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard - Placement Tracker</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${cssUrl}" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.7.1/chart.min.js"></script>
    <script type="text/javascript" src="${jsUrl}"></script>
    <style>
    	.scores-table {
		    width: 100%;
		    border-collapse: collapse;
		    margin-top: 10px;
		}
		.scores-table th, .scores-table td {
		    padding: 8px;
		    text-align: left;
		    border-bottom: 1px solid #eee;
		}
		.scores-table th {
		    background-color: #f4f4f4;
		    font-weight: bold;
		}
		.scores-table td {
		    font-size: 14px;
		}

	.chart-container {
		    height: 200px;
		}
	
	 .data-table {
	    width: 100%;
	    border-collapse: collapse;
	    background-color: #fff;
	    border-radius: 10px;
	    overflow: hidden;
	    box-shadow: 0 2px 4px rgba(0,0,0,0.05);
	    margin-top: 15px;
	}
	
	.data-table th, .data-table td {
	    padding: 10px;
	    border-bottom: 1px solid #e5e7eb;
	    text-align: center;
	    font-size: 14px;
	}
	
	.data-table th {
	    background-color: #f5f7fb;
	    font-weight: 600;
	} 
	
</style>
    
</head>
<body>
<jsp:include page="./shared/sidebar_student_dashboard.jsp" />
    <!-- Main Content -->
    <div class="main-content">
        <div class="section-title">Student Dashboard</div>

        <!-- Dashboard Stats -->
        <div class="dashboard-stats">
            <div class="stat-card">
                <div class="icon blue"><i class="fas fa-briefcase"></i></div>
                <h3>Upcoming Drives</h3>
                <div class="number"><% out.print(((List<?>) request.getAttribute("upcomingDrives")).size()); %></div>
                <div class="trend up"><i class="fas fa-arrow-up"></i> +<% out.print(((List<?>) request.getAttribute("upcomingDrives")).size()); %> this month</div>
                <div class="ripple"></div>
            </div>
            <div class="stat-card">
                <div class="icon purple"><i class="fas fa-file-alt"></i></div>
                <h3>Applications</h3>
                <div class="number"><% out.print(((List<?>) request.getAttribute("applications")).size()); %></div>
                <div class="trend up"><i class="fas fa-arrow-up"></i> +<% out.print(((List<?>) request.getAttribute("applications")).size()); %> applied</div>
                <div class="ripple"></div>
            </div>
            <div class="stat-card">
                <div class="icon green"><i class="fas fa-bell"></i></div>
                <h3>Notifications</h3>
                <div class="number"><% out.print((Long) request.getAttribute("notificationCount")); %></div>
                <div class="trend"><i class="fas fa-arrow-up"></i> New alerts</div>
                <div class="ripple"></div>
            </div>
        </div>

        <!-- Charts Section -->
        <div class="charts-section">
            <div class="chart-card">
                <div class="chart-header">
                    <div class="chart-title">Application Status</div>
                    <div class="chart-options">
                        <select>
                            <option>This Month</option>
                            <option>Last Month</option>
                        </select>
                    </div>
                </div>
                <div class="chart-container">
                    <canvas id="applicationChart"></canvas>
                </div>
            </div>
           <!-- Replace Drive Participation section -->
			<div class="chart-card">
			    <h3>Drive Scores</h3>
			    <div class="filter">
			        <select id="driveSelect" onchange="selectDrive(this.value)">
			            <option value="">Select a Drive</option>
			        </select>
			    </div>
			    <div id="driveScores"></div>
			</div>
        </div>

        <!-- Drives Section -->
        <div class="drives-section">
            <div class="section-title">Placement Drives</div>
            <div class="tabs">
                <div class="tab active" onclick="showDrives('upcoming')">Upcoming Drives</div>
                <div class="tab" onclick="showDrives('attended')">Attended Drives</div>
            </div>
            <div class="drives-list" id="drives-list">
            </div>
            <div class="view-more">
                <button class="view-more-btn">View All Drives</button>
            </div>
        </div>
    </div>

    <!-- Drive Details Popup -->
    <div class="popup-overlay" id="driveDetailsPopup">
        <div class="popup-content">
            <div class="popup-header">
                <div class="popup-title">Drive Details</div>
                <button class="close-popup" onclick="closeDriveDetails()">×</button>
            </div>
            <div class="popup-body" id="driveDetailsContent"></div>
            <div class="popup-footer">
                <button class="popup-btn btn-outline" onclick="closeDriveDetails()">Close</button>
                <button class="popup-btn btn-primary" onclick="applyForDrive()">Apply Now</button>
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
<script>
    // Initialize Charts
    new Chart(document.getElementById('applicationChart'), {
        type: 'bar',
        data: {
            labels: ['Applied', 'Shortlisted', 'Selected'],
            datasets: [{
                label: 'Applications',
                data: [<% out.print(((List<?>) request.getAttribute("applications")).size()); %>, 0, 0],
                backgroundColor: ['#4361ee', '#10b981', '#f59e0b']
            }]
        }
    });
</script>
<script>



document.addEventListener("DOMContentLoaded", function () {
    fetch("attendeddrives")
        .then(response => response.json())
        .then(data => {
            const driveSelect = document.getElementById("driveSelect");

            data.forEach(drive => {
                const option = document.createElement("option");
                option.value = drive.pld_id;
                option.textContent = drive.pld_name;
                driveSelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error("Error fetching drives:", error);
        });
});

function selectDrive(pldId) {
    const container = document.getElementById("driveScores");
    container.innerHTML = "";
    console.log("###",pldId);
    if (!pldId) return;
	
    fetch("drive-scores?pldId="+pldId)
        .then(response => response.json())
        .then(data => {
            if (!data.length) {
                container.innerHTML = "<p>No scores available for this drive.</p>";
                return;
            }

            const table = document.createElement("table");
            table.classList.add("data-table");

            const header = table.insertRow();
            ["Round No", "Phase Name", "Score"].forEach(title => {
                const th = document.createElement("th");
                th.textContent = title;
                header.appendChild(th);
            });

            data.forEach(row => {
                const tr = table.insertRow();
                tr.insertCell().textContent = row.hph_sequence ?? "-";
                tr.insertCell().textContent = row.hph_name ?? "-";
                tr.insertCell().textContent = row.score ?? "-";
            });

            container.appendChild(table);
        })
        .catch(error => {
            console.error("Error fetching scores:", error);
            container.innerHTML = "<p>Error loading scores.</p>";
        });
}
</script>


<script type="text/javascript" src="${jsUrl}"></script>
<script>
window.userId = '<%= session.getAttribute("userId") %>';

</script>
</body>
</html>
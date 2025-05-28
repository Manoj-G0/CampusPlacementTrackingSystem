<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<html>
<head>
    <style>
    body container{
        font-family: 'Segoe UI', Roboto, sans-serif;
        background-color: #f4f7fc;
        margin: 0 auto;
        padding: 40px;
        display: flex;

    }
    .container {
    	margin: 0 auto;
        background: #ffffff;
        padding: 100px 40px;
        border-radius: 12px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        max-height:800px;
        max-width: 600px;
        width: 100%;
    }

    h2, h3 {
        color: #2c3e50;
        text-align: center;
        margin-bottom: 20px;
    }

    select {
        width: 100%;
        padding: 12px;
        font-size: 16px;
        border: 1px solid #ccc;
        border-radius: 6px;
        margin-bottom: 20px;
        transition: border-color 0.3s ease;
    }

    select:focus {
        border-color: #3498db;
        outline: none;
    }

    .report-btn {
        display: inline-block;
        width: 100%;
        background-color: #3498db;
        color: white;
        text-decoration: none;
        padding: 12px;
        margin: 10px 0;
        font-weight: 600;
        text-align: center;
        border-radius: 6px;
        transition: background 0.3s, transform 0.2s;
        box-shadow: 0 4px 12px rgba(52, 152, 219, 0.2);
    }

    .report-btn:hover {
        background-color: #2d80b7;
        transform: translateY(-1px);
        box-shadow: 0 6px 16px rgba(52, 152, 219, 0.3);
    }
    </style>
    <title>Round-wise Shortlisted Report</title>
    <script>
        window.onload = function () {
            fetch("driveslist")
                .then(res => res.json())
                .then(data => {
                    const select = document.getElementById("driveSelect");
                    data.forEach(d => {
                        const opt = document.createElement("option");
                        opt.value = d.pldId;
                        opt.innerText = d.name;
                        select.appendChild(opt);
                    });
                })
                .catch(error => {
                    console.error("Error fetching drives:", error);
                });
            
       	   const roundButtonsDiv = document.getElementById("roundButtons");
           roundButtonsDiv.style.display="block";
           roundButtonsDiv.innerHTML = "<h3>Download Placement Reports</h3>";

           // Add Company-wise Report button
           const companyBtn = document.createElement("a");
           companyBtn.className = "report-btn";
		   companyBtn.target = "_blank";
           companyBtn.href = "companypdf";
           companyBtn.innerText = "Company-wise Report";
           roundButtonsDiv.appendChild(companyBtn);
           roundButtonsDiv.appendChild(document.createElement("br"));
           roundButtonsDiv.appendChild(document.createElement("br"));

           // Add Branch-wise Report button
           const branchBtn = document.createElement("a");
           branchBtn.className = "report-btn";
		   branchBtn.target = "_blank";
           branchBtn.href = "branchpdf";
           branchBtn.innerText = "Branch-wise Report";
           roundButtonsDiv.appendChild(branchBtn);
           roundButtonsDiv.appendChild(document.createElement("br"));
           roundButtonsDiv.appendChild(document.createElement("br"));

           // Add Drive-wise Report button (assumed as college-wise)
           const driveBtn = document.createElement("a");
           driveBtn.className = "report-btn";
		   driveBtn.target = "_blank";
           driveBtn.href = "drivepdf";
           driveBtn.innerText = "Drive-wise Report";
           roundButtonsDiv.appendChild(driveBtn);
           roundButtonsDiv.appendChild(document.createElement("br"));
           roundButtonsDiv.appendChild(document.createElement("br"));
        };

        function showRoundButtons() {
            const driveId = document.getElementById("driveSelect").value;
            const roundButtonsDiv = document.getElementById("roundButtons");
            
            if (!driveId) {
                roundButtonsDiv.style.display = "none";
                return;
            }

            
            fetch("phases?pld_id=" + driveId)
                .then(res => res.json())
                .then(phases => {
                    roundButtonsDiv.style.display = "block";
                    roundButtonsDiv.innerHTML = "<h3>Download Round-wise Shortlisted PDFs</h3>";

                    if (phases.length === 0) {
                        roundButtonsDiv.innerHTML += "<p>No rounds available for this drive.</p>";
                        return;
                    }

                    // Dynamically generate buttons for each phase
                    phases.forEach(phase => {
                    	console.log(phase);
                        const button = document.createElement("a");
                        button.id = "round" + phase.phase_id;
                        button.className = "report-btn";
                        button.target = "_blank";
                        button.href = "phasewisepdf?pld_id=" + driveId + "&phase_id=" + phase.phase_id+"&phase_sequence="+ phase.phase_sequence;
                        button.innerText = "Shortlisted ("+ phase.phase_name+")";
                        roundButtonsDiv.appendChild(button);

                        // Add line breaks and spacing
                        roundButtonsDiv.appendChild(document.createElement("br"));
                        roundButtonsDiv.appendChild(document.createElement("br"));
                    });
                })
                .catch(error => {
                    console.error("Error fetching phases:", error);
                    roundButtonsDiv.style.display = "block";
                    roundButtonsDiv.innerHTML = "<h3>Download Round-wise Shortlisted PDFs</h3><p>Error loading rounds. Please try again.</p>";
                });
        }
    </script>
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
<div class="container">
	
    <h2>Select Placement Drive</h2>
    <select id="driveSelect" onchange="showRoundButtons()">
        <option value="">-- Select Drive --</option>
    </select>

    <div id="roundButtons" style="display:none; margin-top: 20px;">
        <h3>Download Round-wise Shortlisted PDFs</h3>
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
</div>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>

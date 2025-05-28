<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.cpt.model.HiringPhase" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<!DOCTYPE html>
<html>
<head>  
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Phase Wise Evaluation</title>
    <link rel="stylesheet" type="text/css" href="${cssUrl}" />
   <style>
    .container {
        background: #fff;
        padding: 25px 30px;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
        max-width: 900px;
        margin: auto;
    }

    h2 {
        color: #333;
        margin-bottom: 20px;
    }

    label {
        font-weight: 600;
    }

    input, select {
        width: 100%;
        padding: 10px;
        margin: 8px 0 16px;
        border: 1px solid #ccc;
        border-radius: 6px;
        font-size: 15px;
    }

    button, input[type="submit"] {
    background-color: #3b5998; /* Matches blue sidebar */
    color: #fff;
    border: none;
    padding: 10px;
    font-size: 16px;
    cursor: pointer;
    border-radius: 5px;
    transition: background-color 0.3s ease;
	}
	
	button:hover, input[type="submit"]:hover {
	    background-color: #2e4676; /* Slightly darker on hover */
	}
	
	#phaseButtons button {
	    background-color: #f0f0f0;
	    color: #3b5998;
	    border: 1px solid #3b5998;
	    margin: 5px;
	}
	
	
	#phaseButtons button:hover {
	    background-color: #3b5998;
	    color: #fff;
	}

	#sendShortlistedBtn, #tempShortlisted, #evaluate {
	    background-color: #3b5998;
	    color: #fff;
	    border: none;
	    padding: 10px 20px;
	    font-size: 15px;
	    cursor: pointer;
	    border-radius: 5px;
	    transition: background-color 0.3s ease;
	}
	
	#sendShortlistedBtn:hover, #tempShortlisted:hover, #evaluate:hover {
	    background-color: #2e4676;
	}

    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }

    th, td {
        padding: 10px;
        border: 1px solid #ddd;
        text-align: left;
    }

    .scroll-container {
        max-height: 200px;
        overflow-y: auto;
        border: 1px solid #ccc;
        padding: 10px;
        margin-top: 20px;
        background: #fafafa;
    }

    .hidden {
        display: none;
    }

    

    #downloadBtn {
        margin-top: 20px;
        background-color: #27ae60;
    }

    #downloadBtn:hover {
        background-color: #1e8449;
    }

    .upload {
        margin-top: 30px;
    }

    #shortlistedContainer table {
        background: #fff;
        border-radius: 8px;
    }

    #shortlistedContainer th {
        background-color: #f0f0f0;
    }

    .button-row {
        display: flex;
        justify-content: center;
        gap: 20px;
        margin-top: 20px;
    }
</style>

</head>
<body>
<jsp:include page="shared/sidebar_admin.jsp" />
<%
    List<String> companies = (List<String>) request.getAttribute("companies");
    String selectedCompany = (String) request.getAttribute("selectedCompany");
%>
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
	<div class="container">
	    <form id="companyForm" action="filter" method="POST" style="display: flex; gap: 10px; align-items: center; justify-content: center; margin-bottom: 15px;">
	        <!-- <label for="companySelect"><strong>Select Company:</strong></label> -->
	        <select name="selectedCompany" id="companySelect" required>
	            <option value="" disabled <%= (selectedCompany == null) ? "selected" : "" %>>Select a company</option>
	            <%
	                if (companies != null) {
	                    for (String company : companies) {
	            %>
	                <option value="<%= company %>" <%= company.equals(selectedCompany) ? "selected" : "" %>><%= company %></option>
	            <%
	                    }
	                }
	            %>
	        </select>
	        <button type="submit">Filter</button>
	    </form>
        


        
	
	    <div id="evaluationSection" style="<%= (selectedCompany == null) ? "display:none;" : "display:block;" %>">
	        <h2>Phase Wise Evaluation</h2>
	
	        <%
	            List<HiringPhase> screeningRounds = (List<HiringPhase>) request.getAttribute("screeningRounds");
	            Map<String, Map<String, Integer>> phaseCriteriaMap = new LinkedHashMap<>();
	
	            if (screeningRounds != null) {
	                for (HiringPhase round : screeningRounds) {
	                    String phaseName = round.getHphName();
	                    if (!phaseCriteriaMap.containsKey(phaseName)) {
	                        phaseCriteriaMap.put(phaseName, new LinkedHashMap<>());
	                    }
	                    phaseCriteriaMap.get(phaseName).putAll(round.getHm());
	                }
	            }
	        %>
	
	        <div id="phaseButtons">
	            <%
	                for (String phaseName : phaseCriteriaMap.keySet()) {
	            %>
	                <button type="button" onclick="getCriteria('<%= phaseName %>')"><%= phaseName %></button>
	            <%
	                }
	            %>
	        </div>
	
	        <br>
	        <h3 id="selectedPhaseName" style="text-align: center; margin-top: 20px; color: #3a3f51;font-weight: 600;font-size: 1.5rem;letter-spacing: 0.03em;"></h3>
	        <table id="table" class="phase-table"></table>
	
	        <br>
	
	        <button onclick="downloadTemplate()" id="downloadBtn">Download CSV Template</button><br><br>
	
	        <div class="upload" id="uploadSection" style="display:none;">
	            <h2>Upload Test Results</h2>
	            <form id="up" enctype="multipart/form-data">
	                <input type="file" name="file" required>
	                <input type="submit" value="Upload">
	            </form>
	        </div>
	
	        <div id="uploadedData" style="display:none;" class="scroll-container">
	            <table id="studentTable">
	                <thead>
	                    <tr></tr>
	                </thead>
	                <tbody></tbody>
	            </table>
	            <br>
	        </div>
	
	        <!-- Evaluate form -->
	<form id="evaluateForm" style="margin-top:20px;">
	    <input type="hidden" id="selectedPhase" name="phase" />
	    <button id ="evaluate" style ="display:none;">Evaluate</button>
	</form>
	
	  <!-- Container for shortlisted students table -->
	  <div id="shortlistedContainer" class="hidden" style="margin-top: 20px; margin-top:20px">
	    <table id="shortlistedTable" border="1" cellpadding="5" cellspacing="0">
	        <thead>
	            <tr>
	                <th>ID</th>
	                <th>Name</th>
	                <th>Score</th>
	            </tr>
	        </thead>
	        <tbody>
	            <!-- Rows added dynamically here -->
	        </tbody>
	    </table>
	
	    <!-- Send shortlisted button, hidden initially -->
	    <div style="display:flex; justify-content:center; gap:20px; margin-top:20px;">
	       <form action="send" method="POST">
	      		<button id="sendShortlistedBtn" style="display:none;">Send Shortlisted</button>
	      		
		    </form><br>
		    
		     <form action = "saveTemp" method="POST">
		       <button id ="tempShortlisted" style ="display:none;">Save Temporarily</button>
		    </form>
	    </div>
	     
	    
	</div>
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
    let criteria = [];
    let phaseName = "";

    function getCriteria(phaseId) {
        phaseName = phaseId;
        const xhr = new XMLHttpRequest();
        xhr.open("GET", "getCriteria?phaseName=" + encodeURIComponent(phaseId), true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                const data = JSON.parse(xhr.responseText);
                criteria = Object.keys(data);

                const table = document.getElementById("table");
                table.innerHTML = "";
                
                const phaseNameElement = document.getElementById('selectedPhaseName');
                phaseNameElement.textContent = phaseName;

                const headerRow = document.createElement("tr");
                const paramHeader = document.createElement("th");
                paramHeader.textContent = "Parameter";
                const scoreHeader = document.createElement("th");
                scoreHeader.textContent = "Score";
                headerRow.appendChild(paramHeader);
                headerRow.appendChild(scoreHeader);
                table.appendChild(headerRow);

                for (const key in data) {
                    const row = document.createElement("tr");
                    const paramCell = document.createElement("td");
                    paramCell.textContent = key;
                    const scoreCell = document.createElement("td");
                    scoreCell.textContent = data[key];
                    row.appendChild(paramCell);
                    row.appendChild(scoreCell);
                    table.appendChild(row);
                }

                table.style.display = "table";

                document.getElementById("selectedPhase").value = phaseId;
                document.getElementById("uploadSection").style.display = "block";
                document.getElementById("evaluateForm").style.display = "block";
                document.getElementById("shortlistedContainer").classList.add("hidden");
                document.getElementById("uploadedData").style.display = "none";
            }
        };
        xhr.send();
    }

    function downloadTemplate() {
        if (criteria.length === 0) {
            alert("Please select a phase to generate the correct template");
            return;
        }
        let headers = ["Roll Number", "Name", ...criteria];
        const csvContent = headers.join(",") + "\n";
        const blob = new Blob([csvContent], { type: 'text/csv' });
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = "template_" + phaseName + ".csv";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }

    document.getElementById("up").addEventListener("submit", function (event) {
        event.preventDefault();
        const form = event.target;
        const formData = new FormData(form);

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "upload", true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                if (response && response.length > 0) {
                    document.getElementById("uploadedData").style.display = "block";
                    displayStudentData(response);
                } else {
                    alert("No data received from upload.");
                }
            }
            else if(xhr.readyState == 4){
                alert("Error uploading file.");
            }
        };
        xhr.send(formData);
    });

    function displayStudentData(studentList) {
        const table = document.getElementById("studentTable");
        const thead = table.querySelector("thead tr");
        const tbody = table.querySelector("tbody");
        const evaluateBtn = document.getElementById("evaluate")
        thead.innerHTML = "";
        tbody.innerHTML = "";

        if (!studentList || studentList.length === 0) {
            alert("No student data available");
            return;
        }

        thead.appendChild(document.createElement("th")).textContent = "Roll Number";
        thead.appendChild(document.createElement("th")).textContent = "Name";
        criteria.forEach(criterion => {
            thead.appendChild(document.createElement("th")).textContent = criterion;
        });

        studentList.forEach((studentRow) => {
            const row = document.createElement("tr");
            row.appendChild(document.createElement("td")).textContent = studentRow[0];
            row.appendChild(document.createElement("td")).textContent = studentRow[1];

            criteria.forEach((criterion, index) => {
                row.appendChild(document.createElement("td")).textContent = studentRow[index + 2];
            });

            tbody.appendChild(row);
        });
        evaluateBtn.style.display="inline-block";
    }

    
    document.getElementById("evaluateForm").addEventListener("submit", function(e) {
        e.preventDefault();
        const phase = document.getElementById("selectedPhase").value;
        if (!phase) {
            alert("Please select a phase");
            return;
        }

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "evaluate", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    // Parse shortlisted students JSON
                    const shortlisted = JSON.parse(xhr.responseText);
                    displayShortlisted(shortlisted);
                } else {
                    alert("Error during evaluation");
                }
            }
        };

        xhr.send("phase=" + encodeURIComponent(phase));
    });

    function displayShortlisted(shortlisted) {
        const container = document.getElementById("shortlistedContainer");
        const tbody = document.querySelector("#shortlistedTable tbody");
        const sendBtn = document.getElementById("sendShortlistedBtn");
       const tempBtn = document.getElementById("tempShortlisted");

        tbody.innerHTML = "";

        if (!shortlisted || shortlisted.length === 0) {
            tbody.innerHTML = "<tr><td colspan='3'>No shortlisted students found.</td></tr>";
            container.classList.remove("hidden");
            sendBtn.style.display = "none";  // Hide send button if no data
            return;
        }
        var i=0;
        shortlisted.forEach(function(student) {
        	var score = [];
        	 score = <%= (List<Integer>) session.getAttribute("scoresList") %>;
            const row = document.createElement("tr");

            const idCell = document.createElement("td");
            idCell.textContent = student[0];
            row.appendChild(idCell);

            const nameCell = document.createElement("td");
            nameCell.textContent = student[1];
            row.appendChild(nameCell);

            const scoreCell = document.createElement("td");
            scoreCell.textContent = score[i++];
            console.log(score[i++]);
            row.appendChild(scoreCell);

            tbody.appendChild(row);
        });

        container.classList.remove("hidden");
        sendBtn.style.display = "inline-block";
       tempBtn.style.display="inline-block";
        
        // Show send button after displaying shortlisted
    }

    
    document.getElementById("sendShortlistedBtn").addEventListener("click", function() {
        window.location.href = "send";
    });
     document.getElementById("tempShortlisted").addEventListener("click", function() {
        window.location.href = "saveTemp";
    }); 

     

    </script>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>

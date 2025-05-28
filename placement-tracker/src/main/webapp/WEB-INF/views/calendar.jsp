<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="com.cpt.model.DriveDates" %>
<%@ page import ="org.json.*" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Calendar with Drives</title>
<link rel="stylesheet" type="text/css" href="${cssUrl}" />	
<style>
   	body {
        font-family: 'Inter', sans-serif;
        background-color: #f5f7fb;
        color: #1f2a44;
        margin: 0;
        padding: 0;
    }

    h1, h2 {
        text-align: center;
        color: #1f2a44;
        margin: 10px 0;
    }

    .month-header {
        font-size: 22px;
        font-weight: 700;
        color: #4361ee;
        margin-top: 5px;
        text-align: center;
    }

    .calendar {
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        gap: 8px;
        max-width: 95vw;
        margin: 20px auto;
        padding: 15px;
        background-color: #fff;
        border-radius: 12px;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.06);
    }

    .date-header {
        font-weight: 600;
        text-align: center;
        color: #6b7280;
        font-size: 14px;
    }

    .calendar-day {
        background-color: #fff;
        border: 1px solid #e5e7eb;
        border-radius: 8px;
        padding: 10px 6px;
        text-align: center;
        min-height: 70px;
        font-size: 14px;
        font-weight: 500;
        transition: background 0.2s;
        box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
    }

    .calendar-day:hover {
        background-color: #f0f4ff;
        cursor: pointer;
    }

    .calendar-day.active {
        background-color: #e6f0ff;
        border-color: #4361ee;
    }

    .drive, .no-drive {
        font-size: 11px;
        margin-top: 5px;
        display: block;
    }

    .drive {
        color: #4361ee;
    }

    .no-drive {
        color: #9ca3af;
    }

    .button-container {
        display: flex;
        justify-content: center;
        flex-wrap: wrap;
        gap: 10px;
        margin: 15px auto 20px;
        max-width: 95vw;
    }

    .navigation-button {
        background-color: #4361ee;
        color: white;
        padding: 8px 16px;
        border: none;
        border-radius: 8px;
        font-size: 13px;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    .navigation-button:hover {
        background-color: #3b82f6;
    }

    .form-container {
        max-width: 800px;
        margin: 25px auto;
        padding: 20px;
        background-color: #fff;
        border-radius: 10px;
        box-shadow: 0 3px 6px rgba(0, 0, 0, 0.06);
        border: 1px solid #e5e7eb;
    }

    form label {
        display: block;
        margin-top: 12px;
        font-weight: 600;
        color: #1f2a44;
    }

    form input[type="text"],
    form input[type="number"],
    form select {
        width: 100%;
        padding: 9px;
        margin-top: 4px;
        border: 1px solid #d1d5db;
        border-radius: 6px;
        font-size: 14px;
    }

    form input[type="submit"] {
        background-color: #4361ee;
        color: white;
        padding: 12px;
        margin-top: 18px;
        border: none;
        border-radius: 6px;
        font-size: 15px;
        cursor: pointer;
        width: 100%;
        transition: background-color 0.3s ease;
    }

    form input[type="submit"]:hover {
        background-color: #3b82f6;
    }

    table {
        width: 100%;
        margin-top: 20px;
        border-collapse: collapse;
        background-color: #ffffff;
        border-radius: 10px;
        overflow: hidden;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
    }

    table th,
    table td {
        padding: 10px;
        text-align: center;
        border-bottom: 1px solid #e5e7eb;
        font-size: 13px;
    }

    table th {
        background-color: #f5f7fb;
        font-weight: 600;
    }

    .resource-table {
        margin-top: 20px;
        max-width: 95vw;
        margin-left: auto;
        margin-right: auto;
    }

    @media (max-width: 768px) {
        .calendar {
            gap: 6px;
            padding: 10px;
        }

        .calendar-day {
            min-height: 60px;
            font-size: 13px;
        }

        .month-header {
            font-size: 18px;
        }

        .navigation-button {
            padding: 6px 12px;
            font-size: 12px;
        }
    }
</style>
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />
<div class = "main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
	<h1>Drive Calendar</h1>
<h1 id="monthHeader">Month Year</h1>

<div class="calendar" id="calendar"></div>

<div class="button-container">
    <button class="navigation-button" id="previousYearBtn">Previous Year</button>
    <button class="navigation-button" id="previousMonthBtn">Previous Month</button>
    <button class="navigation-button" id="nextMonthBtn">Next Month</button>
    <button class="navigation-button" id="nextYearBtn">Next Year</button>
</div>

<div id="resourceTableContainer" class="resource-table"></div> <!-- This is where the resource table will be appended -->
<div class="form-container">
    <h2>Drive Resource Allocation</h2>
    <form id = "ResAlloc" action="ResAlloc" method="post">
        <label for="driveNameDropdown">Drive Name:</label>
        <select id="driveNameDropdown" name="driveName" required>
            <option value="">Loading drives...</option>
        </select>
		
		<input type = "hidden" id = "pld_id" name = "pld_id">
		<input type = "hidden" id = "clg_id" name = "clg_id">
		
        <label for="rounds">Rounds:</label>
        <select id="rounds" name="rounds" required>
             <option value="">Select Round...</option>
        </select>

        <label for="numStudents">Number of Students:</label>
        <input type="number" id="numStudents" name="numStudents" min="1" required>

        <input type="submit" value="Allocate Resources">
    </form>
   <table id="resource" style="display:none;" border="1">
    <thead id="tableHead">
        <!-- Table headers will be dynamically inserted here -->
    </thead>
    <tbody>
        <!-- Table rows will be dynamically inserted here -->
    </tbody>
</table>

<div id="responseMsg"></div>

    
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
// Store drive names for each day (initially empty)
let dayDrives = {};
let currentYear = new Date().getFullYear();
let currentMonth = new Date().getMonth();

// List of month names for easy lookup
const monthNames = [
    'January', 'February', 'March', 'April', 'May', 'June', 
    'July', 'August', 'September', 'October', 'November', 'December'
];

// Generate calendar for a given year and month
function generateCalendar(year, month) {
    const calendarContainer = document.getElementById('calendar');
    const monthHeader = document.getElementById('monthHeader');
    const currentDate = new Date(year, month);
    const firstDay = new Date(year, month, 1).getDay();
    const lastDate = new Date(year, month + 1, 0).getDate();

    // Set the month and year in the header
    monthHeader.innerText = monthNames[month] + " " + year;

    // Clear the previous calendar
    calendarContainer.innerHTML = '';

    // Create the header for the days of the week
    const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    daysOfWeek.forEach(day => {
        const headerDiv = document.createElement('div');
        headerDiv.classList.add('date-header');
        headerDiv.innerText = day;
        calendarContainer.appendChild(headerDiv);
    });

    // Add empty cells for the days before the 1st day of the month
    for (let i = 0; i < firstDay; i++) {
        const emptyDiv = document.createElement('div');
        calendarContainer.appendChild(emptyDiv);
    }

    // Create the calendar days for the current month
    const dayElements = [];
    for (let day = 1; day <= lastDate; day++) {
        const dayDiv = document.createElement('div');
        dayDiv.classList.add('calendar-day');
        dayDiv.setAttribute('data-date', day);
        dayDiv.innerText = day;
        calendarContainer.appendChild(dayDiv);
        dayElements.push(dayDiv); // Store the element for later use
    }

    // Fetch drives for the current month
    fetchDrives(year, month, dayElements);
}

// Fetch drives based on the year and month
function fetchDrives(year, month, dayElements) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', 'getDrives', true); // Replace with your actual controller URL
    xhr.onload = function() {
        if (xhr.status === 200) {
            const drives = JSON.parse(xhr.responseText);
            const daysWithDrives = new Set(); // Set to track days with drives

            // Reset the drive names
            dayDrives = {}; // Clear any previous data

            // Loop through the drives and highlight the relevant dates
            drives.forEach(function(drive) {
                const startDate = new Date(drive.pldStartDate[0], drive.pldStartDate[1] - 1, drive.pldStartDate[2]);
                const endDate = new Date(drive.pldEndDate[0], drive.pldEndDate[1] - 1, drive.pldEndDate[2]);

                if (startDate.getFullYear() === year && startDate.getMonth() === month) {
                    for (let i = startDate.getDate(); i <= endDate.getDate(); i++) {
                        const dayElement = dayElements.find(el => el.getAttribute('data-date') == i); // Find the correct day element
                        if (dayElement) {
                            dayElement.classList.add('active');
                            const driveElement = document.createElement('div');
                            driveElement.classList.add('drive');
                            driveElement.innerText = drive.pldName; // Append the drive name
                            dayElement.appendChild(driveElement); // Append to the day cell
                            daysWithDrives.add(i); // Mark this day as having a drive

                            // Store the drive names for the day in the dayDrives object
                            if (!dayDrives[i]) {
                                dayDrives[i] = [];
                            }
                            dayDrives[i].push(drive.pldName); // Add the drive name to this day's list
                        }
                    }
                }
            });

            // For days with no drives, display "No drive"
            dayElements.forEach(dayElement => {
                const day = dayElement.getAttribute('data-date');
                if (!daysWithDrives.has(parseInt(day))) {
                    const noDriveElement = document.createElement('div');
                    noDriveElement.classList.add('no-drive');
                    noDriveElement.innerText = 'No drive';
                    dayElement.appendChild(noDriveElement); // Append "No drive" message
                }
            });
        }
    };
    xhr.send();
}

// Handle date click and fetch resources
function handleDateClick(event) {
    const selectedDate = event.target.getAttribute('data-date');
    const year = new Date().getFullYear(); // current year
    const month = new Date().getMonth(); // current month

    if (selectedDate) {
        // Get the drive names for the clicked date from the dayDrives object
        const driveNames = dayDrives[selectedDate] || [];
        const token = '<%= session.getAttribute("token") %>'
        console.log("TOKEN  ",token);
        if (driveNames.length > 0) {
            console.log('Drive names for selected date:', driveNames);
            //document.getElementById("nodrive").innerText = "";
             document.getElementById('resourceTableContainer').innerHTML = "";

            // You can send the drive names to your backend here instead of the date
            const xhr = new XMLHttpRequest();
            xhr.open('GET', 'getResources?drives=' + encodeURIComponent(driveNames.join(',')), true);
            xhr.setRequestHeader("Authorization","Bearer " + token);
            xhr.onload = function() {
                if (xhr.status === 200) {
                    const resources = JSON.parse(xhr.responseText);
                    console.log('Resources for selected date:', resources);

                    // Create and populate the table with the resources data
                    createResourceTable(resources);
                }
            };
            xhr.send();
        } else {
            console.log('No drives for selected date');
            tableContainer = document.getElementById('resourceTableContainer')
            tableContainer.innerHTML = "<h1>No Drive</h1>";
            setTimeout( () =>{
            	tableContainer.innerHTML = "";
            	console.log("function executed");
            },10000);
        }
    }
}

// Function to create and populate the resource table
function createResourceTable(resources) {
    const tableContainer = document.getElementById('resourceTableContainer');
    tableContainer.innerHTML = ''; // Clear previous table if any

    if (resources.length === 0) {
        tableContainer.innerHTML = '<h1>No resources allocated yet for this drive.</h1>';
        setTimeout( () =>{
        	tableContainer.innerHTML = "";
        	console.log("function executed");
        },10000);
        return;
    }

    const table = document.createElement('table');

    // Create table header
    const headerRow = document.createElement('tr');
    const th = document.createElement('th');
    th.innerText = "RoomNo";
    
    const th1 = document.createElement('th');
    th1.innerText = "Drive Name";
    
    const th2 = document.createElement('th');
    th2.innerText = "Phase No";
    
    const th3 = document.createElement('th');
    th3.innerText = "Total Capacity";
    
    const th4 = document.createElement('th');
    th4.innerText = "Occupied";
    
    const th5 = document.createElement('th');
    th5.innerText = "Faculty Name";
    
    headerRow.appendChild(th);
    headerRow.appendChild(th1);
    headerRow.appendChild(th2);
    headerRow.appendChild(th3);
    headerRow.appendChild(th4);
    headerRow.appendChild(th5);
    
    
   /*  Object.keys(resources[0]).forEach(key => {
        const th = document.createElement('th');
        th.innerText = key.charAt(0).toUpperCase() + key.slice(1); // Capitalize the first letter
        headerRow.appendChild(th);
    }); */
    table.appendChild(headerRow);

    // Create table rows
    resources.forEach(resource => {
        const row = document.createElement('tr');
        
        const td = document.createElement('td');
        td.innerText = resource.ralResourceId;
        row.appendChild(td);
        
        const td1 = document.createElement('td');
        td1.innerText = resource.ralPldName;
        row.appendChild(td);
        
        const td2 = document.createElement('td');
        td2.innerText = resource.ralPhaseId;
        row.appendChild(td);
        
        const td3 = document.createElement('td');
        td3.innerText = resource.ralCapacity;
        row.appendChild(td);
        
        const td4 = document.createElement('td');
        td4.innerText = resource.ralOccupied;
        row.appendChild(td);
        
        const td5 = document.createElement('td');
        td5.innerText = resource.ralFacultyName;
        console.log(resource.ralFacultyName);
        row.appendChild(td);
        row.appendChild(td1);
        row.appendChild(td2);
        row.appendChild(td3);
        row.appendChild(td4);
        row.appendChild(td5);
        
        /* Object.values(resource).forEach(value => {
            const td = document.createElement('td');
            td.innerText = value;
            row.appendChild(td);
        });
 */        table.appendChild(row);
    });

    // Append the table to the container
    tableContainer.appendChild(table);
    setTimeout( () =>{
    	tableContainer.innerHTML = "";
    	console.log("function executed");
    },10000);
}

// Event listeners for navigation buttons
document.getElementById('previousYearBtn').addEventListener('click', function() {
    currentYear--;
    generateCalendar(currentYear, currentMonth);
});

document.getElementById('previousMonthBtn').addEventListener('click', function() {
    if (currentMonth === 0) {
        currentMonth = 11;
        currentYear--;
    } else {
        currentMonth--;
    }
    generateCalendar(currentYear, currentMonth);
});

document.getElementById('nextMonthBtn').addEventListener('click', function() {
    if (currentMonth === 11) {
        currentMonth = 0;
        currentYear++;
    } else {
        currentMonth++;
    }
    generateCalendar(currentYear, currentMonth);
});

document.getElementById('nextYearBtn').addEventListener('click', function() {
    currentYear++;
    generateCalendar(currentYear, currentMonth);
});

// Initialize the calendar for the current month
document.addEventListener('DOMContentLoaded', function() {
    generateCalendar(currentYear, currentMonth);

    // Add event listener for date clicks
    document.getElementById('calendar').addEventListener('click', function(event) {
        if (event.target.classList.contains('calendar-day')) {
            handleDateClick(event);
        }
    });
});
var resources = {};






var pid = 0;

document.getElementById("driveNameDropdown").addEventListener('change',function () {
	const selectedVal = this.value;
	console.log(selectedVal);
	if(selectedVal === "") return;
	
	const xhr = new XMLHttpRequest();
	xhr.open("Get","getDriveDet?dname="+encodeURIComponent(selectedVal),true);
	
	xhr.onreadystatechange = function(){
		if(xhr.status == 200 && xhr.readyState == 4)
		{
			let data = JSON.parse(xhr.responseText);
			console.log(data);
			document.getElementById("pld_id").innerText = data.pld_id;
			document.getElementById("pld_id").value = data.pld_id;
			console.log(data.pld_id);
			pid = data.pld_id;
			document.getElementById("clg_id").innerText = data.pld_clg_id;
			document.getElementById("pld_id").value = data.pld_id;
			document.getElementById("clg_id").value = data.pld_clg_id;
		}
	}
	xhr.send();
});

window.onload = function () {
    // Load Drive Names
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "driveNames", true);
    xhr.setRequestHeader("Accept", "application/json");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var drives = JSON.parse(xhr.responseText);
            var driveSel = document.getElementById("driveNameDropdown");
            driveSel.innerHTML = "<option value=''>Select Drive</option>";
            drives.forEach(function (drive) {
                var opt = document.createElement("option");
                opt.text = drive;
                opt.value = drive;
                driveSel.appendChild(opt);
            });
        }
    };
    xhr.send();

    // Load number of students on rounds change
    document.getElementById("rounds").addEventListener("change", function () {
        var selectedRounds = this.value;
        if (!selectedRounds) return;
		let pid = document.getElementById("pld_id").value;
        var xhrRound = new XMLHttpRequest();
        xhrRound.open("GET", "roundStu?rounds=" + selectedRounds+"&pid="+pid, true);
        xhrRound.setRequestHeader("Accept", "application/json");

        xhrRound.onreadystatechange = function () {
            if (xhrRound.readyState === 4 && xhrRound.status === 200) {
                var numStudents = JSON.parse(xhrRound.responseText);
                document.getElementById("numStudents").value = numStudents;
            }
        };
        xhrRound.send();
    });
};
var t = 0;
		
const dynamicInput = document.getElementById("pld_id");
const observer = new MutationObserver(function(mutationsList) {
    for (const mutation of mutationsList) {
        if (mutation.type === 'attributes' && mutation.attributeName === 'value') {
            // When the input value is populated, trigger the next AJAX call
            const populatedValue = dynamicInput.value;
            console.log('Input populated with value:', populatedValue);
            pid = populatedValue;
            // Trigger the second AJAX call
            t = 1;
            makeNextAjaxCall(populatedValue);
        }
    }
});
		
		
		
		
		
		
		const drivedropdowm = document.getElementById("driveNameDropdown");

        // Get the second dropdown
        const rounddp = document.getElementById('rounds');

        // Add event listener to the first dropdown
        drivedropdowm.addEventListener('change', function() {
            const selectedValue = this.value;
            //let pid = document.getElementById("pld_id").value;
            
            // If a value is selected, make an AJAX request
            setTimeout(() => ajaxcall(selectedValue),600);
            if (pid === 2) {
               ajaxcall(selectedValue);
            } else {
                // If no selection, clear the second dropdown
                rounddp.innerHTML = '<option value="">Select an option</option>';
            }
        });
		
        
        function ajaxcall(sel)
        {
        	console.log("ajax call has been made");
        	 fetch("getRounds?pid="+pid)
             .then(response => response.json()) // Parse the JSON response
             .then(data => {
                 // Clear the second dropdown
                 console.log(data);
                 rounddp.innerHTML = '<option value="">Select an option</option>';

                 // Populate the second dropdown with the received data
                 /* data.forEach(option => {
                     const optionElement = document.createElement('option');
                     optionElement.value = option.id;
                     optionElement.textContent = option.name;
                     rounddp.appendChild(optionElement);
                 });  */
                 
                 for(let i = 0;i < data.length;i++)
                 {
                	 const optionElement = document.createElement('option');
                     optionElement.value = data[i];
                     console.log(data[i]);
                     optionElement.innerText = data[i];
                     rounddp.appendChild(optionElement);
                 }
                 
             })
             .catch(error => {
                 console.error('Error fetching data:', error);
             });
        }
        
        
        const form = document.getElementById('ResAlloc');

        form.addEventListener('submit', function (e) {
            e.preventDefault(); // Prevent full page reload

            const formData = new FormData(form);
            const jsonData = {};

            formData.forEach((value, key) => {
                jsonData[key] = value;
            });
			console.log(formData, " 32148230157" , jsonData);
            fetch('ResAlloc', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(jsonData)
            })
            .then(response => response.json())
            .then(data => {
               // document.getElementById('responseMsg').innerText = 'Submitted successfully!';
                console.log("1212468219",data);
                t = data;
                populateTable(data);
            })
            .catch(error => {
                //document.getElementById('responseMsg').innerText = 'Submission failed!';
                console.error('Error:', error);
            });
        });
        
        function populateTable(response) {
            const table = document.getElementById('resource');
            const tableHead = document.getElementById('tableHead');
            const tableBody = table.getElementsByTagName('tbody')[0];

            // Clear previous content
            tableHead.innerHTML = '';
            tableBody.innerHTML = '';

            // Add header row
            const headerRow = document.createElement('tr');
            ['Lab No', 'No Of Students', 'Faculty'].forEach(text => {
                const th = document.createElement('th');
                th.textContent = text;
                headerRow.appendChild(th);
            });
            tableHead.appendChild(headerRow);
            console.log("response",response);
            const parsed = typeof response === "string" ? JSON.parse(response):response;
          
            const resources = parsed.resource;
            const faculty = parsed.faculty;
			
            console.log(resources,"123244324",faculty);
            
            const labNos = Object.keys(resources);
            for (let i = 0; i < labNos.length; i++) {
                const labNo = labNos[i];
                const numStudents = resources[labNo];
                const facultyName = faculty[i] || 'N/A';

                const row = document.createElement('tr');

                [labNo, numStudents, facultyName].forEach(value => {
                    const td = document.createElement('td');
                    td.textContent = value;
                    row.appendChild(td);
                });

                tableBody.appendChild(row);
            }

            // Show the table
            table.style.display = 'table';
        }

</script>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>

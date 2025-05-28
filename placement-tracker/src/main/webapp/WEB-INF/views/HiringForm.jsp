<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List,com.cpt.model.Branch,com.cpt.model.PlacementDrive"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<html>
<head>
    <title>Round-wise Parameter Scores</title>
    <style>
    
    
/* Make label for "Select placement drive" bold and black */
.placement-id-container label {
    font-weight: bold;
    color: black;
    margin-right: 10px; /* Space between the label and the dropdown */
}

/* Wrapper to align dropdown and button side by side */
.placement-id-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

/* Make dropdown and button have the same height and appear side by side */
.placement-id-container select {
    width: 48%; /* Adjust to fit alongside the button */
    padding: 10px;
    font-size: 1rem;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
    font-weight: normal; /* Ensures the dropdown text is not bold */
}

/* Button styling */
.placement-id-container .generate-btn {
    width: 48%; /* Adjust to fit alongside the dropdown */
    padding: 10px 14px;
    background-color: #4361ee;
    color: white;
    border: none;
    cursor: pointer;
    font-size: 1rem;
    border-radius: 4px;
    box-sizing: border-box;
}

.placement-id-container .generate-btn:hover {
    background-color: #3548b6;
}

/* Responsive design for smaller screens */
@media (max-width: 600px) {
    .placement-id-container {
        flex-direction: row;
        align-items: stretch;
    }
    .placement-id-container select,
    .placement-id-container .generate-btn {
        width: 100%;
        margin-bottom: 10px; /* Equal gap between elements */
    }
}
  
    
    
body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
    background-color: #f4f7fc;
}

.section-title {
    font-size: 1.5rem;
    color: #0059b3;
    text-align: center;
    margin-bottom: 30px;
}

.form-group {
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.form-group label {
    flex: 1;
    font-weight: bold;
}

.form-group input {
    width: 180px; /* Same width for all fields */
    padding: 10px;
    font-size: 1rem; /* Consistent font size */
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box; /* Ensures padding does not affect width */
}

.form-group .generate-btn {
    padding: 10px 14px;
    background-color: #4361ee;
    color: white;
    border: none;
    cursor: pointer;
    font-size: 1rem;
    border-radius: 4px;
}

.form-group .generate-btn:hover {
    background-color: #3548b6;
}

.round {
    margin-bottom: 25px;
    border: 1px solid #ccc;
    padding: 15px;
}

.round-fields {
margin-top: 20px;
    display: flex;
    flex-wrap: wrap;
    gap: 20px; /* Equal gap between fields */
    margin-bottom: 10px;
    justify-content: space-between;
}

.round-fields input,
.round-fields button {
    width: 180px; /* Same width for fields and buttons */
    padding: 10px;
    font-size: 1rem;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box; /* Ensure padding doesn't affect width */
}

.parameter-block {
    margin-top: 30px;
    display: flex;             /* Enables flex layout */
    flex-wrap: wrap;           /* Wraps fields if not enough space */
    gap: 150px;                 /* Equal spacing between inputs */
    justify-content: flex-start; /* Align items to start */
}

.parameter-block input {
    width: 250px;
    padding: 10px;
    font-size: 1rem;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
}


.generate-btn, .submit-btn, .add-round-btn, .delete-round-btn {
    padding: 10px 14px;
    background-color: #4361ee;
    color: white;
    border: none;
    cursor: pointer;
    font-size: 1rem;
    border-radius: 4px;
}

.generate-btn:hover, .submit-btn:hover, .add-round-btn:hover, .delete-round-btn:hover {
    background-color: #3548b6;
}

.button-group {
    display: flex;
    gap: 10px;
    justify-content: center;
}

.button-group button {
    padding: 10px 14px;
}

h4 {
    color: #0059b3;
}

.form-group input[type="number"] {
    width: 180px;
    padding: 10px;
}

/* Responsive design for mobile screens */
@media (max-width: 600px) {
    .form-group {
        flex-direction: column;
    }

    .form-group label, .form-group input, .form-group .generate-btn {
        width: 100%;
        margin-bottom: 10px;
    }

    .round-fields {
        flex-direction: column;
        width: 100%;
    }

    .round-fields input,
    .round-fields button {
        width: 100%;
        margin-bottom: 10px; /* Ensure gap is equal between fields */
    }
    
    /* Ensure parameter fields also resize correctly */
    .parameter-block input {
        width: 100%;
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
    <div class="section-title">Hiring Phases</div>

    <h2 style="color: #0059b3; text-align: center; margin-bottom: 30px;">
        Enter Round-wise Parameter Scores
    </h2>
    <div class="placement-id-container">
         <div class="placement_id">
        <label for="pld_id">Select placement drive:</label>
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
        <label for="numberOfRounds">Rounds:</label>
        <input type="number" id="numberOfRounds" name="numberOfRounds" min="1" required />
        <button type="button" class="generate-btn" onclick="generateFormFields()">Generate Rounds</button>
    </div>

</div>

    <div class="button-group">
        <button type="button" class="add-round-btn" onclick="addRound()" style="display:none;">Add Another Round</button>
        <button type="button" class="delete-round-btn" onclick="deleteLastRound()" style="display:none;">Delete Last Round</button>
    </div>

    <div id="roundContainer"></div>

    <div style="text-align: center;">
        <button type="button" id="submitBtn" class="submit-btn" style="display:none;" onclick="submitData()">Submit</button>
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
    let sequence = 1; // Initial sequence value
    let roundCount = 0; // Keeps track of the number of generated rounds

    // Generates the initial number of rounds based on input
    function generateFormFields() {
        roundCount = parseInt(document.getElementById("numberOfRounds").value, 10);
        const container = document.getElementById("roundContainer");
        container.innerHTML = "";

        if (isNaN(roundCount) || roundCount <= 0) {
            alert("Please enter a valid number of rounds.");
            return;
        }

        for (let i = 0; i < roundCount; i++) {
            createRound(i);
        }

        // Show the "Add Round" and "Delete Last Round" buttons
        document.querySelector(".add-round-btn").style.display = "inline-block";
        document.querySelector(".delete-round-btn").style.display = "inline-block";
        document.getElementById("submitBtn").style.display = "inline-block";

        // Update the number of rounds input field
        document.getElementById("numberOfRounds").value = roundCount;
    }

    // Function to create a round dynamically
    function createRound(i) {
        const roundDiv = document.createElement("div");
        roundDiv.className = "round";
        roundDiv.id = 'round_' + i;

        const roundHeader = document.createElement("h4");
        roundHeader.textContent = 'Round ' + (i + 1);
        roundDiv.appendChild(roundHeader);

        const roundFields = document.createElement("div");
        roundFields.className = "round-fields";

        const inputRoundName = document.createElement("input");
        inputRoundName.type = "text";
        inputRoundName.name = `roundWrapper.rounds[${i}].roundName`;
        inputRoundName.placeholder = "Round Name";
        inputRoundName.required = true;

        const inputSequence = document.createElement("input");
        inputSequence.type = "number";
        inputSequence.name = `roundWrapper.rounds[${i}].sequence`;
        inputSequence.placeholder = "Sequence";
        inputSequence.value = sequence++;
        inputSequence.required = true;

        const paramCount = document.createElement("input");
        paramCount.type = "number";
        paramCount.min = "0";
        paramCount.placeholder = "No. of Parameters";
        paramCount.setAttribute("data-round-index", i);

        const generateBtn = document.createElement("button");
        generateBtn.type = "button";
        generateBtn.textContent = "Generate Parameters";
        generateBtn.className = "generate-btn";
        generateBtn.onclick = function () {
            generateParameters(i, paramCount.value, roundDiv);
        };

        roundFields.appendChild(inputRoundName);
        roundFields.appendChild(inputSequence);
        roundFields.appendChild(paramCount);
        roundFields.appendChild(generateBtn);

        roundDiv.appendChild(roundFields);
        document.getElementById("roundContainer").appendChild(roundDiv);
    }

    // Function to add another round dynamically
    function addRound() {
        const container = document.getElementById("roundContainer");
        const newIndex = roundCount;
        roundCount++;

        createRound(newIndex);

        // Update the number of rounds input field
        document.getElementById("numberOfRounds").value = roundCount;
    }

    // Function to delete the last round
    function deleteLastRound() {
        if (roundCount <= 0) {
            alert("No rounds to delete.");
            return;
        }

        roundCount--;
        const roundDiv = document.getElementById('round_' + roundCount);
        roundDiv.remove();

        // Hide the delete button if no rounds remain
        if (roundCount === 0) {
            document.querySelector(".delete-round-btn").style.display = "none";
        }

        // Update the number of rounds input field
        document.getElementById("numberOfRounds").value = roundCount;
    }

    // Generates parameters for each round based on the parameter count
    function generateParameters(roundIndex, count, roundDiv) {
        const oldParams = roundDiv.querySelectorAll(".parameter-block");
        oldParams.forEach(p => p.remove());

        if (isNaN(count) || count <= 0) {
            alert("Please enter a valid number of parameters.");
            return;
        }

        for (let j = 0; j < count; j++) {
            const paramDiv = document.createElement("div");
            paramDiv.className = "parameter-block";

            const inputParamName = document.createElement("input");
            inputParamName.type = "text";
            inputParamName.name = `roundWrapper.rounds[${roundIndex}].parameters[${j}].parameterName`;
            inputParamName.placeholder = "Parameter Name";
            inputParamName.required = true;

            const inputTotalScore = document.createElement("input");
            inputTotalScore.type = "number";
            inputTotalScore.name = `roundWrapper.rounds[${roundIndex}].parameters[${j}].totalScore`;
            inputTotalScore.placeholder = "Total Score";
            inputTotalScore.required = true;

            const inputThreshold = document.createElement("input");
            inputThreshold.type = "number";
            inputThreshold.name = `roundWrapper.rounds[${roundIndex}].parameters[${j}].threshold`;
            inputThreshold.placeholder = "Threshold";
            inputThreshold.required = true;

            paramDiv.appendChild(inputParamName);
            paramDiv.appendChild(inputTotalScore);
            paramDiv.appendChild(inputThreshold);
            roundDiv.appendChild(paramDiv);
        }
    }

    // Function to submit the data after validation
    function submitData() {
        const data = { rounds: [] };
		const pld_id = document.getElementById("pld_id").value;
        for (let i = 0; i < roundCount; i++) {
            const roundDiv = document.getElementById('round_' + i);
            if (!roundDiv) continue;

            const roundNameInput = roundDiv.querySelector(`input[name="roundWrapper.rounds[${i}].roundName"]`);
            const sequenceInput = roundDiv.querySelector(`input[name="roundWrapper.rounds[${i}].sequence"]`);

            if (!roundNameInput.value.trim()) {
                alert(`Round ${i + 1}: Round name is required.`);
                return;
            }

            const sequence = parseInt(sequenceInput.value.trim());
            if (!sequenceInput.value.trim() || isNaN(sequence) || sequence <= 0) {
                alert(`Round ${i + 1}: Sequence must be a positive number.`);
                return;
            }

            const parameterBlocks = roundDiv.querySelectorAll(".parameter-block");
            const parameters = [];

            for (let j = 0; j < parameterBlocks.length; j++) {
                const paramDiv = parameterBlocks[j];
                const paramNameInput = paramDiv.querySelector(`input[name="roundWrapper.rounds[${i}].parameters[${j}].parameterName"]`);
                const totalScoreInput = paramDiv.querySelector(`input[name="roundWrapper.rounds[${i}].parameters[${j}].totalScore"]`);
                const thresholdInput = paramDiv.querySelector(`input[name="roundWrapper.rounds[${i}].parameters[${j}].threshold"]`);

                const paramName = paramNameInput.value.trim();
                const totalScore = parseFloat(totalScoreInput.value.trim());
                const threshold = parseFloat(thresholdInput.value.trim());

                if (!paramName) {
                    alert(`Round ${i + 1}, Parameter ${j + 1}: Parameter name is required.`);
                    return;
                }

                if (isNaN(totalScore) || totalScore < 0) {
                    alert(`Round ${i + 1}, Parameter ${j + 1}: Total score must be a non-negative number.`);
                    return;
                }

                if (isNaN(threshold) || threshold < 0) {
                    alert(`Round ${i + 1}, Parameter ${j + 1}: Threshold must be a non-negative number.`);
                    return;
                }

                if (totalScore <= threshold) {
                    alert(`Round ${i + 1}, Parameter ${j + 1}: Total score must be greater than threshold.`);
                    return;
                }

                parameters.push({ parameterName: paramName, totalScore, threshold });
            }

            data.rounds.push({
                roundName: roundNameInput.value.trim(),
                sequence: sequence,
                parameters: parameters
            });
        }

        fetch("submitRounds?pld_id="+pld_id, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        })
        .then(response => response.text())
        .then(result => {
            alert( result);
            document.getElementById("numberOfRounds").value = "";

            // Clear the round container
            document.getElementById("roundContainer").innerHTML = "";
            
            document.getElementById("submitBtn").style.display = "none";
            document.querySelector(".add-round-btn").style.display = "none";
            document.querySelector(".delete-round-btn").style.display = "none";
        })
        .catch(error => {
            console.error("Error:", error);
        });
    }
</script>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>

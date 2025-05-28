<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html>
<head>
    <title>Resource Form</title>
    <script>
        // Load colleges on page load
        var clgid = 0;
        window.onload = function () {
            var xhr = new XMLHttpRequest();
            xhr.open("GET", "getcolleges", true); // Backend controller endpoint
            xhr.setRequestHeader("Accept", "application/json");

            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    var colleges = JSON.parse(xhr.responseText);
                    console.log(colleges);
                    var collegeSelect = document.getElementById("college");
                    collegeSelect.innerHTML = "<option value=''>Select College</option>";
                    for(let i = 0;i < colleges.length;i++)
                    {
                        var opt = document.createElement("option");
                        opt.value = colleges[i];
                        opt.text = colleges[i];
                        collegeSelect.add(opt);
                        console.log(document.getElementById("college"));
                    };
                }
            };
            xhr.send();
            
            document.getElementById("college").addEventListener('change',function () {
            	console.log("hii");
            	 console.log(document.getElementById("college"));
        		const selectedVal = this.value;
        		console.log(selectedVal);
        		if(selectedVal === "") return;
        		
        		const xhr = new XMLHttpRequest();
        		xhr.open("Get","getClgId?cname="+encodeURIComponent(selectedVal),true);
        		
        		xhr.onreadystatechange = function(){
        			if(xhr.status == 200 && xhr.readyState == 4)
        			{
        				let data = JSON.parse(xhr.responseText);
        				console.log(data);
        				document.getElementById("clg_id").value = data;
        				//document.getElementById("college").value = data;
        				console.log("coe;gwgw",data);
        				clgid = data;
        			}
        		}
        		xhr.send();
        	});
        };
        
        function fetchBranches(clgId) {
            const branchSelect = document.getElementById("branch");
            branchSelect.innerHTML = '<option value="">Loading branches...</option>';
			console.log("the clg id ",clgId);
            if (!clgId) {
                branchSelect.innerHTML = '<option value="">Select Branch</option>';
                return;
            }

            fetch("branch?clgId="+clgId)
                .then(response => response.json())
                .then(data => {
                    branchSelect.innerHTML = '<option value="">Select Branch</option>';
                    data.forEach(branch => {
                        const option = document.createElement("option");
                        option.value = branch.brn_name;
                        option.textContent = branch.branch_name;
                        option.innerText = branch.brn_name;
                        branchSelect.appendChild(option);
                    });
                })
                .catch(err => {
                    console.error("Error fetching branches:", err);
                    branchSelect.innerHTML = '<option value="">Failed to load branches</option>';
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
<div class="section-title">Resource Creation</div>
<form action="resAdd" method="post">
	 <div class="form-group">
    <label for="resourceId">Room Number</label>
    <input type="text" id="resourceId" name="resourceId" required>
	</div>
	 <div class="form-group">
    <label for="college">College:</label>
    <select id="college" name="college"  onchange = "fetchBranches(this.value)" required>
        <option value="">Loading colleges...</option>
    </select>
    </div>
	<input type = "hidden" id = "clg_id" name = "clg_id">
	 <div class="form-group">
    <label for="branch">Branch:</label>
    <select id="branch" name="branch" required>
        <option value="">Select Branch</option>
        
    </select>
    </div>
	 <div class="form-group">
    <label for="capacity">Capacity:</label>
    <input type="number" id="capacity" name="capacity" required>
	</div>
    <button type="submit" class="popup-btn btn-primary">Submit</button>
</form>
</div>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>

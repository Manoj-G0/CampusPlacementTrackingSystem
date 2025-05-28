<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html>
<head>
    <title>FAQs</title>
    <style>
        .faq-container {
            max-width: 800px;
            margin: 0 auto;
            padding-top:30px;
            padding: 30px;
            font-family: Arial, sans-serif;
        }

        .faq-item {
            border: 1px solid #ccc;
            border-radius: 5px;
            padding: 15px;
            margin-top: 20px;
            margin-bottom: 10px;
            background-color: #f9f9f9;
        }

        .faq-question {
            font-weight: bold;
            cursor: pointer;
            display: flex;
            justify-content: space-between;
        }

        .faq-answer {
            display: none;
            margin-top: 15px;
            font-family:sans-serif;
            color: #333;
        }

        .toggle-icon {
            font-size: 20px;
            color: #3498db;
        }
    </style>
    <script>
        function toggleFAQ(id) {
            var answerDiv = document.getElementById("faq-answer-" + id);
            if (answerDiv.style.display === 'block') {
                answerDiv.style.display = 'none';
                return;
            }

            if (answerDiv.innerHTML.trim() !== '') {
                answerDiv.style.display = 'block';
                return;
            }

            fetch("faqs/" + id)
                .then(function(res) {
                    return res.text();
                })
                .then(function(answer) {
                    answerDiv.innerHTML = answer;
                    answerDiv.style.display = 'block';
                });
        }

        window.onload = function () {
            fetch("faqs")
                .then(function(res) {
                    return res.json();
                })
                .then(function(data) {
                    var container = document.getElementById("faqList");
                    data.forEach(function(faq) {
                        var div = document.createElement("div");
                        div.className = "faq-item";

                        var questionDiv = document.createElement("div");
                        questionDiv.className = "faq-question";
                        questionDiv.setAttribute("onclick", "toggleFAQ(" + faq.faq_id + ")");
                        
                        var spanQuestion = document.createElement("span");
                        spanQuestion.textContent = faq.faq_question;

                        var spanIcon = document.createElement("span");
                        spanIcon.className = "toggle-icon";
                        spanIcon.textContent = "+";

                        questionDiv.appendChild(spanQuestion);
                        questionDiv.appendChild(spanIcon);

                        var answerDiv = document.createElement("div");
                        answerDiv.className = "faq-answer";
                        answerDiv.id = "faq-answer-" + faq.faq_id;

                        div.appendChild(questionDiv);
                        div.appendChild(answerDiv);
                        container.appendChild(div);
                    });
                });
        };
    </script>
</head>
<body>
	<jsp:include page="./shared/sidebar_student_dashboard.jsp" />
	    <div class="main-content">
	    	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
		    </button>
	    	<div class="faq-container">
	        <h2>Frequently Asked Questions</h2>
	        <div id="faqList"></div>
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
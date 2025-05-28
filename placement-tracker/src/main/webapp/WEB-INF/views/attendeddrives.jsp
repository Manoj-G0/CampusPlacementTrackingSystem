<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cpt.model.AttendedDrive, com.cpt.model.HiringPhase, java.util.List, org.apache.commons.lang3.StringEscapeUtils" %>
<% String error = request.getParameter("error"); %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attended Drives - Placement Tracking System</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <style>
    
        body {
            font-family: 'Arial', sans-serif;
            background: #f3f4f6;
            margin: 0;
            padding: 0;
            display: flex;
            min-height: 100vh;
        }
        .sidebar {
            width: 250px;
            background: #1e2a3c;
            color: #fff;
            transition: width 0.3s;
        }
        .sidebar.collapsed {
            width: 60px;
        }
        .main-content {
            flex-grow: 1;
            padding: 20px;
            transition: margin-left 0.3s;
        }
        .main-content.sidebar-closed {
            margin-left: 60px;
        }
        .section-title {
            font-size: 1.8rem;
            color: #1e2a3c;
            margin-bottom: 20px;
        }
        .container {
            max-width: 100%;
            margin: 0 auto;
        }
        .table-container {
            background: #fff;
            padding: 15px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            overflow-x: auto; /* Scrollable table on mobile */
        }
        .data-table {
            width: 100%;
            border-collapse: collapse;
        }
        .data-table th, .data-table td {
            border: 1px solid #e5e7eb;
            padding: 12px;
            text-align: left;
            font-size: 0.9rem;
        }
        .data-table th {
            background: #f0f7ff;
            color: #1e2a3c;
        }
        .data-table tr {
            transition: background 0.3s, transform 0.2s;
        }
        .data-table tr:hover {
            background: #f0f7ff;
            transform: scale(1.01);
        }
        .alert.error {
            background: #fee2e2;
            color: #dc2626;
            padding: 12px;
            border-radius: 8px;
            margin-bottom: 15px;
        }
        .timeline-container {
            margin-top: 10px;
            padding: 10px;
            background: #f8fafc;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .timeline-svg {
            width: 100%;
            height: 120px;
            overflow: visible;
            display: block;
        }
        .timeline-circle {
            stroke: #fff;
            stroke-width: 2px;
            transition: transform 0.3s ease, filter 0.3s ease;
            cursor: pointer;
        }
        .timeline-circle.cleared {
            animation: pulseGlow 2s infinite;
        }
        .timeline-circle:hover {
            transform: scale(1.2);
            filter: drop-shadow(0 0 8px rgba(59, 130, 246, 0.5));
        }
        .timeline-line {
            stroke: #64748b;
            stroke-width: 3px;
            stroke-dasharray: 100;
            stroke-dashoffset: 100;
            animation: drawLine 1.5s ease forwards;
        }
        .timeline-text {
            font-size: 11px;
            text-anchor: middle;
            fill: #1e2a3c;
            font-weight: 500;
            white-space: nowrap;
        }
        .timeline-icon {
            font-size: 14px;
            fill: #fff;
        }
        .tooltip {
            position: absolute;
            background: #1e2a3c;
            color: #fff;
            padding: 6px 10px;
            border-radius: 6px;
            font-size: 12px;
            z-index: 10;
            opacity: 0;
            transition: opacity 0.3s;
            pointer-events: none;
        }
        .timeline-circle:hover + .tooltip {
            opacity: 1;
        }
        .phase-details {
            display: none;
            margin-top: 8px;
            padding: 8px;
            background: #fff;
            border-radius: 6px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            font-size: 12px;
            color: #1e2a3c;
            width: 100%;
        }
        .phase-details.active {
            display: block;
            animation: slideIn 0.3s ease;
        }
        @keyframes pulseGlow {
            0% { filter: drop-shadow(0 0 5px rgba(34, 197, 94, 0.5)); }
            50% { filter: drop-shadow(0 0 10px rgba(34, 197, 94, 0.8)); }
            100% { filter: drop-shadow(0 0 5px rgba(34, 197, 94, 0.5)); }
        }
        @keyframes drawLine {
            to { stroke-dashoffset: 0; }
        }
        @keyframes slideIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
        /* Responsive Design */
        @media (max-width: 768px) {
            .sidebar {
                width: 60px;
            }
            .main-content {
                margin-left: 60px;
            }
            .section-title {
                font-size: 1.5rem;
            }
            .data-table th, .data-table td {
                padding: 8px;
                font-size: 0.8rem;
            }
            .timeline-svg {
                height: 100px;
            }
            .timeline-circle {
                r: 15px; /* Smaller nodes */
            }
            .timeline-text {
                font-size: 10px;
            }
            .timeline-icon {
                font-size: 12px;
            }
            .timeline-container {
                padding: 8px;
            }
        }
        @media (max-width: 480px) {
            .data-table th, .data-table td {
                padding: 6px;
                font-size: 0.7rem;
            }
            .timeline-svg {
                height: 80px;
            }
            .timeline-circle {
                r: 12px;
            }
            .timeline-text {
                font-size: 9px;
                transform: rotate(-60); /* Steeper angle for tight space */
            }
            .timeline-icon {
                font-size: 10px;
            }
            .phase-details {
                font-size: 11px;
            }
        }
    </style>
    <script type="text/javascript" src="${jsUrl}"></script>
</head>
<body>
<jsp:include page="./shared/sidebar_student_dashboard.jsp" />
<div class="main-content">
	<button class="btn-back" onclick="history.back()">
            <i class="fas fa-arrow-left"></i>
    </button>
    <div class="section-title">Attended Drives</div>
    <div class="container">
        <% if (error != null) { %>
            <div class="alert error">
                <% switch (error) {
                    case "already_applied": %>
                        You have already applied to this drive.
                        <% break;
                    case "db_error": %>
                        Database connection failed. Please try again later.
                        <% break;
                    default: %>
                        Unknown error: <%= error %>
                <% } %>
            </div>
        <% } %>
        <div class="table-container">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Drive Name</th>
                        <th>Company</th>
                        <th>Status</th>
                        <th>Total Rounds</th>
                        <th>Rounds Cleared</th>
                    </tr>
                </thead>
                <tbody>
                    <% List<AttendedDrive> drives = (List<AttendedDrive>) request.getAttribute("attendedDrives"); %>
                    <% if (drives != null && !drives.isEmpty()) { %>
                        <% for (AttendedDrive drive : drives) { %>
                            <tr>
                                <td><%= drive.getPldName() %></td>
                                <td><%= drive.getCmpName() %></td>
                                <td><%= "SELECTED".equals(drive.getStatus()) ? "Selected" : "Not Selected" %></td>
                                <td><%= drive.getPhases().size() %></td>
                                <td><%= "SELECTED".equals(drive.getStatus()) ? drive.getPhases().size() : (drive.getFizzledRound() > 0 ? drive.getFizzledRound() - 1 : 0) %></td>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <div class="timeline-container">
                                        <% if (!drive.getPhases().isEmpty()) { %>
                                            <svg class="timeline-svg" id="svg-<%= drive.getPldId() %>"></svg>
                                            <div id="details-<%= drive.getPldId() %>" class="phase-details">
                                                <% for (HiringPhase phase : drive.getPhases()) { %>
                                                    <p><strong>Phase <%= phase.getHphSequence() %>:</strong> <%= phase.getHphName() %></p>
                                                <% } %>
                                            </div>
                                            <script>
                                                (function() {
                                                    try {
                                                        var svg = document.getElementById('svg-<%= drive.getPldId() %>');
                                                        var phases = [
                                                            <% for (HiringPhase phase : drive.getPhases()) { %>
                                                                {
                                                                    name: '<%= StringEscapeUtils.escapeEcmaScript(phase.getHphName()) %>',
                                                                    sequence: <%= phase.getHphSequence() %>,
                                                                    icon: '<%= phase.getHphName().toLowerCase().contains("test") ? "fa-clipboard-check" : phase.getHphName().toLowerCase().contains("interview") ? "fa-user-tie" : "fa-tasks" %>'
                                                                },
                                                            <% } %>
                                                        ];
                                                        console.log('Drive <%= drive.getPldId() %> phases:', phases);
                                                        if (phases.length === 0) {
                                                            svg.outerHTML = '<p>No phases to display</p>';
                                                            return;
                                                        }

                                                        var status = '<%= drive.getStatus() %>';
                                                        var fizzledRound = <%= drive.getFizzledRound() %>;
                                                        var nodeRadius = window.innerWidth <= 480 ? 12 : window.innerWidth <= 768 ? 15 : 20;
                                                        var minNodeSpacing = window.innerWidth <= 480 ? 60 : window.innerWidth <= 768 ? 80 : 100;
                                                        var width = Math.max(600, phases.length * minNodeSpacing);
                                                        var height = window.innerWidth <= 480 ? 80 : window.innerWidth <= 768 ? 100 : 120;
                                                        var nodeSpacing = Math.max(minNodeSpacing, width / (phases.length + 1));
                                                        var clearedRounds = status === 'SELECTED' ? phases.length : (fizzledRound > 0 ? fizzledRound - 1 : 0);
                                                        console.log('Drive <%= drive.getPldId() %> clearedRounds:', clearedRounds, 'width:', width);

                                                        svg.setAttribute('width', width);
                                                        svg.setAttribute('height', height);

                                                        var nodes = phases.map(function(phase, i) {
                                                            return {
                                                                x: (i + 1) * nodeSpacing,
                                                                y: height / 2,
                                                                name: phase.name,
                                                                icon: phase.icon,
                                                                sequence: phase.sequence,
                                                                color: i < clearedRounds ? 'url(#greenGradient)' : 'url(#redGradient)',
                                                                class: i < clearedRounds ? 'cleared' : ''
                                                            };
                                                        });

                                                        var links = [];
                                                        for (var i = 0; i < nodes.length - 1; i++) {
                                                            links.push({ source: nodes[i], target: nodes[i + 1] });
                                                        }

                                                        var defs = document.createElementNS('http://www.w3.org/2000/svg', 'defs');
                                                        var greenGradient = document.createElementNS('http://www.w3.org/2000/svg', 'linearGradient');
                                                        greenGradient.setAttribute('id', 'greenGradient');
                                                        greenGradient.setAttribute('x1', '0%');
                                                        greenGradient.setAttribute('y1', '0%');
                                                        greenGradient.setAttribute('x2', '100%');
                                                        greenGradient.setAttribute('y2', '100%');
                                                        var stop1 = document.createElementNS('http://www.w3.org/2000/svg', 'stop');
                                                        stop1.setAttribute('offset', '0%');
                                                        stop1.setAttribute('stop-color', '#22c55e');
                                                        var stop2 = document.createElementNS('http://www.w3.org/2000/svg', 'stop');
                                                        stop2.setAttribute('offset', '100%');
                                                        stop2.setAttribute('stop-color', '#16a34a');
                                                        greenGradient.appendChild(stop1);
                                                        greenGradient.appendChild(stop2);
                                                        defs.appendChild(greenGradient);

                                                        var redGradient = document.createElementNS('http://www.w3.org/2000/svg', 'linearGradient');
                                                        redGradient.setAttribute('id', 'redGradient');
                                                        redGradient.setAttribute('x1', '0%');
                                                        redGradient.setAttribute('y1', '0%');
                                                        redGradient.setAttribute('x2', '100%');
                                                        redGradient.setAttribute('y2', '100%');
                                                        var stop3 = document.createElementNS('http://www.w3.org/2000/svg', 'stop');
                                                        stop3.setAttribute('offset', '0%');
                                                        stop3.setAttribute('stop-color', '#dc2626');
                                                        var stop4 = document.createElementNS('http://www.w3.org/2000/svg', 'stop');
                                                        stop4.setAttribute('offset', '100%');
                                                        stop4.setAttribute('stop-color', '#b91c1c');
                                                        redGradient.appendChild(stop3);
                                                        redGradient.appendChild(stop4);
                                                        defs.appendChild(redGradient);
                                                        svg.appendChild(defs);

                                                        links.forEach(function(link) {
                                                            var line = document.createElementNS('http://www.w3.org/2000/svg', 'line');
                                                            line.setAttribute('x1', link.source.x);
                                                            line.setAttribute('y1', link.source.y);
                                                            line.setAttribute('x2', link.target.x);
                                                            line.setAttribute('y2', link.target.y);
                                                            line.setAttribute('class', 'timeline-line');
                                                            svg.appendChild(line);
                                                        });

                                                        nodes.forEach(function(node, i) {
                                                            var circle = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
                                                            circle.setAttribute('cx', node.x);
                                                            circle.setAttribute('cy', node.y);
                                                            circle.setAttribute('r', nodeRadius);
                                                            circle.setAttribute('fill', node.color);
                                                            circle.setAttribute('class', 'timeline-circle ' + node.class);
                                                            circle.setAttribute('onclick', "toggleDetails('details-<%= drive.getPldId() %>')");
                                                            circle.setAttribute('aria-label', 'Phase ' + node.sequence + ': ' + node.name);
                                                            svg.appendChild(circle);

                                                            var foreignObject = document.createElementNS('http://www.w3.org/2000/svg', 'foreignObject');
                                                            foreignObject.setAttribute('x', node.x - nodeRadius * 0.6);
                                                            foreignObject.setAttribute('y', node.y - nodeRadius * 0.6);
                                                            foreignObject.setAttribute('width', nodeRadius * 1.2);
                                                            foreignObject.setAttribute('height', nodeRadius * 1.2);
                                                            var iconDiv = document.createElement('div');
                                                            iconDiv.setAttribute('class', 'fas ' + node.icon + ' timeline-icon');
                                                            iconDiv.style.textAlign = 'center';
                                                            iconDiv.style.lineHeight = (nodeRadius * 1.2) + 'px';
                                                            foreignObject.appendChild(iconDiv);
                                                            svg.appendChild(foreignObject);

                                                            var text = document.createElementNS('http://www.w3.org/2000/svg', 'text');
                                                            text.setAttribute('x', node.x);
                                                            text.setAttribute('y', node.y + nodeRadius + 25);
                                                            text.setAttribute('class', 'timeline-text');
                                                            text.setAttribute('transform', 'rotate(-45 ' + node.x + ',' + (node.y + nodeRadius + 25) + ')');
                                                            text.textContent = node.name.length > 15 ? node.name.substring(0, 12) + '...' : node.name;
                                                            svg.appendChild(text);

                                                            var tooltip = document.createElement('div');
                                                            tooltip.setAttribute('class', 'tooltip');
                                                            var svgRect = svg.getBoundingClientRect();
                                                            tooltip.style.left = (node.x + svgRect.left - 50) + 'px';
                                                            tooltip.style.top = (node.y + svgRect.top - 50) + 'px';
                                                            tooltip.textContent = 'Phase ' + node.sequence + ': ' + node.name;
                                                            svg.parentNode.appendChild(tooltip);
                                                        });
                                                    } catch (e) {
                                                        console.error('Error rendering timeline for drive <%= drive.getPldId() %>:', e);
                                                        svg.outerHTML = '<p>Error rendering timeline</p>';
                                                    }
                                                })();
                                            </script>
                                        <% } else { %>
                                            <p>No phases available for this drive.</p>
                                        <% } %>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                    <% } else { %>
                        <tr>
                            <td colspan="5">No attended drives.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
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
    function toggleSidebar() {
        document.querySelector('.sidebar').classList.toggle('collapsed');
        document.querySelector('.main-content').classList.toggle('sidebar-closed');
    }
    function toggleDetails(id) {
        var details = document.getElementById(id);
        if (details.classList.contains('active')) {
            details.classList.remove('active');
        } else {
            var allDetails = document.querySelectorAll('.phase-details');
            allDetails.forEach(function(detail) {
                detail.classList.remove('active');
            });
            details.classList.add('active');
        }
    }
</script>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>


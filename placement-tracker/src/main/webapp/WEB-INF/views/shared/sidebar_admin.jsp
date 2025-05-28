<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.cpt.model.*, java.util.*, java.text.SimpleDateFormat" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html>
<head>
 	<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Placement Tracker</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${cssUrl}" />
   
    <style>
        /* Internal CSS enhancements */
        .admin-dashboard {
            background: linear-gradient(120deg, #f5f7fb 0%, #eef1f8 100%);
        }
        
        .sidebar {
            box-shadow: 3px 0px 15px rgba(0, 0, 0, 0.1);
            background: linear-gradient(180deg, #1f2a44 0%, #2d3a57 100%);
        }
        
        .logo-container {
            transition: all 0.3s ease;
            overflow: hidden;
        }
        
        .logo i {
            animation: pulse 2s infinite;
        }
        
        @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.1); }
            100% { transform: scale(1); }
        }
        
        .menu-item {
            transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
            border-left: 3px solid transparent;
        }
        
        .menu-item:hover, .menu-item.active {
            background: rgba(67, 97, 238, 0.2);
            border-left: 3px solid #4361ee;
            transform: translateX(5px);
        }
        
        .menu-item i {
            transition: all 0.3s ease;
        }
        
        .menu-item:hover i, .menu-item.active i {
            transform: scale(1.2);
        }
        
        .header {
            backdrop-filter: blur(5px);
            background-color: rgba(255, 255, 255, 0.9);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
            border-bottom: 1px solid rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
        }
        
        .search-bar {
            transition: all 0.3s ease;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
        }
        
        .search-bar:focus-within {
            box-shadow: 0 3px 10px rgba(67, 97, 238, 0.15);
            transform: translateY(-2px);
        }
        
        .profile-avatar {
            box-shadow: 0 3px 8px rgba(0, 0, 0, 0.2);
            transition: all 0.3s ease;
            background: linear-gradient(135deg, #4361ee 0%, #3b82f6 100%);
        }
        
        .profile-avatar:hover {
            transform: rotate(10deg);
        }
        
        .notification-icon {
            transition: all 0.3s ease;
        }
        
        .notification-icon:hover {
            transform: translateY(-3px);
        }
        
        .badge {
            transform-origin: center;
            animation: bounce 2s infinite;
        }
        
        @keyframes bounce {
            0%, 100% { transform: translateY(0); }
            50% { transform: translateY(-3px); }
        }
        
        .toggle-btn i {
            transition: all 0.5s ease;
        }
        
        .toggle-btn:hover i {
            transform: rotate(180deg);
            color: #4361ee;
        }
    	/* Profile Dropdown */
		.profile-dropdown {
		    position: absolute;
		    top: 100%;
		    right: 0;
		    background: #ffffff;
		    border-radius: 8px;
		    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
		    min-width: 160px;
		    overflow: hidden;
		    opacity: 0;
		    visibility: hidden;
		    transform: translateY(10px);
		    transition: all 0.3s ease;
		    z-index: 100;
		}
		
		.profile-dropdown.show {
		    opacity: 1;
		    visibility: visible;
		    transform: translateY(0);
		}
		
		.dropdown-item {
		    display: block;
		    padding: 12px 16px;
		    color: #1e293b;
		    font-size: 14px;
		    text-decoration: none;
		    transition: background 0.3s ease, color 0.3s ease;
		}
		
		.dropdown-item:hover {
		    background: #f1f5f9;
		    color: #3b82f6;
		}
		
		.dropdown-item i {
		    margin-right: 8px;
		}
    </style>
</head>
<body>
<div class="admin-dashboard">
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="logo-container">
            <div class="logo">
                <i class="fas fa-graduation-cap"></i>
                <span>Placement Tracker</span>
            </div>
            <div class="toggle-btn">
                <i class="fas fa-chevron-left"></i>
            </div>
        </div>
        <div class="menu-container">
            <div class="menu-title">Admin Menu</div>
            <div class="menu-item active" data-section="dashboard" onclick="loadSection('dashboard')">
                <i class="fas fa-th-large"></i>
                <span>Dashboard</span>
            </div>
            <div class="menu-item" data-section="add-placement-drive" onclick="loadSection('add-placement-drive')">
                <i class="fas fa-briefcase"></i>
                <span>Add Placement Drive</span>
            </div>
            <div class="menu-item" data-section="add-company" onclick="loadSection('add-company')">
                <i class="fas fa-building"></i>
                <span>Add Company</span>
            </div>
            <div class="menu-item" data-section="add-college" onclick="loadSection('add-college')">
                <i class="fas fa-university"></i>
                <span>Add College</span>
            </div>
            <div class="menu-item" data-section="add-branch" onclick="loadSection('add-branch')">
                <i class="fas fa-code-branch"></i>
                <span>Add Branch</span>
            </div>
            <div class="menu-item" data-section="resource-allocations" onclick="loadSection('resource-allocations')">
                <i class="fas fa-cogs"></i>
                <span>Resource Allocation</span>
            </div>
            <div class="menu-item" data-section="report-generation" onclick="loadSection('report-generation')">
                <i class="fas fa-chart-bar"></i>
                <span>Reports & Analytics</span>
            </div>
            <div class="menu-item" data-section="phase-evaluation" onclick="loadSection('phase-evaluation')">
                <i class="fas fa-chart-bar"></i>
                <span>Phase Evaluation</span>
            </div>
            <div class="menu-item" data-section="round-wise" onclick="loadSection('round-wise')">
                <i class="fas fa-chart-bar"></i>
                <span>Round wise</span>
            </div>
             <div class="menu-item" data-section="add-hr-details" onclick="loadSection('add-hr-details')">
                <i class="fas fa-chart-bar"></i>
                <span>Add Hr</span>
            </div>
             <div class="menu-item" data-section="addAdmin" onclick="loadSection('addAdmin')">
                <i class="fas fa-chart-bar"></i>
                <span>Add TPO</span>
            </div>
            <div class="menu-item" onclick="window.location.href='../logout'">
                <i class="fas fa-sign-out-alt"></i>
                <span>Logout</span>
            </div>
        </div>
    </div>

    <!-- Header -->
    <div class="header">
        <div class="search-bar">
            <!-- <i class="fas fa-search"></i>
            <input type="text" placeholder="Search..."> -->
        </div>
        <div class="profile-section">
            <% String userId  = (String) session.getAttribute("userId"); %>
            <div class="notification-icon" onclick="showNotifications('<%= userId %>')">
                <i class="fas fa-bell"></i>
                <span class="badge"><% out.print(session.getAttribute("notificationCount") != null ? session.getAttribute("notificationCount") : 0); %></span>
            </div>
            <div class="profile-avatar">A</div>
            <div class="profile-info">
                <span class="profile-name"><%= session.getAttribute("userId") != null ? session.getAttribute("userId") : "Admin User" %></span>
                <i class="fas fa-chevron-down"></i>
            </div>
            <div class="profile-dropdown">
            	<a href="../changePassword" class="dropdown-item">Change Password</a>
                <a href="../admin/profile" class="dropdown-item">View Profile</a>
                <a href="../logout" class="dropdown-item">Logout</a>
            </div>
           </div>
    </div>

<script type="text/javascript">
document.addEventListener('DOMContentLoaded', () => {
    // Sidebar Toggle
    const sidebar = document.querySelector('.sidebar');
    const toggleBtn = document.querySelector('.toggle-btn');
    const header = document.querySelector('.header');
    const mainContent = document.querySelector('.main-content');

    toggleBtn.addEventListener('click', () => {
    	sidebar.classList.toggle('collapsed');
        if (sidebar.classList.contains('collapsed')) {
            header.style.left = '80px';
            mainContent.style.marginLeft = '80px';
        } else {
            header.style.left = '260px';
            mainContent.style.marginLeft = '260px';
        }
    });
	//Profile dropdown toggle
    const profileSection = document.querySelector('.profile-section');
    const profileDropdown = document.querySelector('.profile-dropdown');

    if (!profileSection || !profileDropdown) {
        console.error('Profile section or dropdown not found');
        return;
    }

    // Toggle dropdown on click
    profileSection.addEventListener('click', (e) => {
        e.stopPropagation(); // Prevent closing immediately
        profileDropdown.classList.toggle('show');
    });

    // Close dropdown when clicking outside
    document.addEventListener('click', (e) => {
        if (!profileSection.contains(e.target)) {
            profileDropdown.classList.remove('show');
        }
    });
});
</script>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>
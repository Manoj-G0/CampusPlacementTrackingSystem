<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="com.cpt.model.*, java.util.*, java.text.SimpleDateFormat" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard_student.js" var="jsUrl" />
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Student Dashboard - Placement Tracker</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<link rel="stylesheet" type="text/css" href="${cssUrl}" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.7.1/chart.min.js"></script>
    <style>
        /* Internal CSS enhancements */
        .student-dashboard {
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
<div class="student-dashboard">
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="logo-container">
            <div class="logo">
                <i class="fas fa-graduation-cap"></i>
                <span>Placement Tracker</span>
            </div>
            <div class="toggle-btn" onclick="toggleSidebar()">
                <i class="fas fa-chevron-left"></i>
            </div>
        </div>
        <div class="menu-container">
            <div class="menu-title">Main Menu</div>
            <div class="menu-item active" data-section="dashboard" onclick="loadSection('dashboard')">
                <i class="fas fa-th-large"></i>
                <span>Dashboard</span>
            </div>
            <div class="menu-item" data-section="eligible-drives" onclick="loadSection('eligible-drives')">
                <i class="fas fa-briefcase"></i>
                <span>Eligible Drives</span>
            </div>
            <div class="menu-item" data-section="attended-drives" onclick="loadSection('attended-drives')">
                <i class="fas fa-file-alt"></i>
                <span>Attended Drives</span>
            </div>
            <div class="menu-item" data-section="selected-drives" onclick="loadSection('selected-drives')">
                <i class="fas fa-file-alt"></i>
                <span>Selected Drives</span>
            </div>
            <div class="menu-item" data-section="my-applications" onclick="loadSection('my-applications')">
                <i class="fas fa-file-alt"></i>
                <span>My Applications</span>
            </div>
            <div class="menu-item" data-section="notifications" onclick="loadSection('notifications')">
                <i class="fas fa-bell"></i>
                <span>Notifications</span>
            </div>
            <div class="menu-title">Profile</div>
            <div class="menu-item" data-section="resume" onclick="loadSection('resume')">
                <i class="fas fa-file-alt"></i>
                <span>Resume</span>
            </div>
            <div class="menu-item" data-section="profile" onclick="loadSection('profile')">
                <i class="fas fa-user"></i>
                <span>My Profile</span>
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
            <div class="profile-avatar">S</div>
            <div class="profile-info">
                <span class="profile-name"><%= session.getAttribute("userId") != null ? session.getAttribute("userId") : "Student User" %></span>
                <i class="fas fa-chevron-down"></i>
            </div>
            <div class="profile-dropdown">
            	<a href="../changePassword" class="dropdown-item">Change Password</a>
                <a href="../student/profile" class="dropdown-item">View Profile</a>
                <a href="../logout" class="dropdown-item">Logout</a>
            </div>
           </div>
    </div>


<script>
    function toggleSidebar() {
        document.querySelector('.sidebar').classList.toggle('collapsed');
        document.querySelector('.main-content').classList.toggle('sidebar-closed');
        document.querySelector('.header').classList.toggle('sidebar-closed');
    }
    
    document.addEventListener('DOMContentLoaded', () => {
        // Sidebar Toggle
        const sidebar = document.querySelector('.sidebar');
        const toggleBtn = document.querySelector('.toggle-btn');
        const header = document.querySelector('.header');
        const mainContent = document.querySelector('.main-content');

        toggleBtn.addEventListener('click', () => {
            console.log("Helo");
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
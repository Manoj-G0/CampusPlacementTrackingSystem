/* src/main/webapp/resources/js/dashboard.js */
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

    // Ripple Effect for Stat Cards
    document.querySelectorAll('.stat-card').forEach(card => {
        card.addEventListener('click', (e) => {
            const ripple = document.createElement('span');
            ripple.classList.add('ripple');
            card.appendChild(ripple);
            const rect = card.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            ripple.style.width = ripple.style.height = `${size}px`;
            ripple.style.left = `${e.clientX - rect.left - size / 2}px`;
            ripple.style.top = `${e.clientY - rect.top - size / 2}px`;
            ripple.classList.add('show');
            setTimeout(() => ripple.remove(), 500);
        });
    });
    
    	document.addEventListener('DOMContentLoaded', () => {
		const userId = window.userId || 'S001';
	    const driveSelect = document.getElementById('driveSelect');
	    console.log(userId);
	    fetch(`../student/attendeddrives?usr_id=${userId}`)
	        .then(response => {
	            if (!response.ok) {
	                throw new Error(`HTTP error! Status: ${response.status}`);
	            }
	            return response.json();
	        })
	        .then(drives => {
	            driveSelect.innerHTML = '<option value="">Select a Drive</option>';
	            drives.forEach(drive => {
	                const option = document.createElement('option');
	                option.value = drive.pldId;
	                option.textContent = drive.name || 'Unnamed Drive';
	                driveSelect.appendChild(option);
	            });
	            if (drives.length > 0) {
	                driveSelect.value = drives[0].pldId;
	                fetchDriveScores(drives[0].pldId);
	            } else {
	                document.getElementById('driveScores').innerHTML = '<p>No attended drives available.</p>';
	            }
	        })
	        .catch(error => {
	            console.error('Error fetching attended drives:', error);
	            document.getElementById('driveScores').innerHTML = '<p>Error loading drives. Please try again.</p>';
	        });
	});

    // Load Section (Mocked Navigation)
    //window.loadSection = function(section) {
        //document.querySelectorAll('.menu-item').forEach(item => {
           // item.classList.remove('active');
           // if (item.dataset.section === section) {
                //item.classList.add('active');
            //}
        //});
        
		// Load Section and Navigate
		window.loadSection = function(section) {
		    const path = window.location.pathname;
		    let role = '';
		
		    if (path.includes('/student/')) {
		        role = 'student';
		    } else if (path.includes('/admin/')) {
		        role = 'admin';
		    } else if (path.includes('/hr/')) {
		        role = 'hr';
		    }
		
		    // Redirect to the correct section URL based on role
		    window.location.href = `/placement-tracker/${role}/${section}`;
		};
		
		// Highlight active menu item
		window.addEventListener('DOMContentLoaded', () => {
		    const path = window.location.pathname; // /placement-tracker/student/attended-drives
		    const parts = path.split('/');
		    const currentSection = parts[parts.length - 1]; // attended-drives
		
		    document.querySelectorAll('.menu-item').forEach(item => {
		        const section = item.getAttribute('data-section');
		        if (section === currentSection) {
		            item.classList.add('active');
		        } else {
		            item.classList.remove('active');
		        }
		    });
		});
		
		
		
        //console.log(`Loading section: ${section}`);
        /*switch (section) {
            case 'dashboard':
                window.location.href = 'dashboard';
                break;
            case 'add-placement-drive':
                window.location.href = 'add-placement-drive';
                break;
            case 'profile':
                window.location.href = 'profile';
                break;
            case 'add-company':
                window.location.href = 'add-company';
                break;
            case 'add-college':
                window.location.href = 'add-college';
                break;
            case 'add-company-team':
                window.location.href = 'add-company-team';
                break;
            case 'eligible-drives':
                window.location.href = 'eligible-drives';
                break;
            case 'attended-drives':
                window.location.href = 'attended-drives';
                break;
            case 'report-generation':
	            window.location.href = 'report-generation';
	            break;
            case 'notifications':
                window.location.href = 'notifications';
                break;
            case 'add-branch':
                window.location.href = 'add-branch';
                break;
            case 'resource-allocations':
                window.location.href = 'resource-allocations';
                break;
            case 'assign-faculty':
                window.location.href = 'assign-faculty';
                break;
        }*/

    // Show Drives (Filter Drives)
    /*window.showDrives = function(status) {
        document.querySelectorAll('.tab').forEach(tab => {
            tab.classList.remove('active');
            if (tab.textContent.toLowerCase().includes(status === 'all' ? 'all' : status)) {
                tab.classList.add('active');
            }
        });
        const drivesList = document.getElementById('drives-list');
        const driveCards = drivesList.querySelectorAll('.drive-card');
        driveCards.forEach(card => {
            const driveStatus = card.querySelector('.drive-status').textContent.toLowerCase();
            if (status === 'all' || driveStatus === status) {
                card.style.display = 'flex';
            } else {
                card.style.display = 'none';
            }
        });
    };*/
    
	window.addEventListener('DOMContentLoaded', async () => {
	    console.log('Loading drives dynamically');
	    const drivesList = document.getElementById('drives-list');
	    const userId = window.userId || 'S001'; // Fallback to S001 if not set
	
	    try {
	        const response = await fetch(`../student/drives?userId=${userId}`, {
	            method: 'GET',
	            headers: {
	                'Accept': 'application/json'
	            }
	        });
	
	        if (!response.ok) {
	            throw new Error(`HTTP error! Status: ${response.status}`);
	        }
	
	        const drives = await response.json();
	        console.log('Drives fetched:', drives);
	
	        drivesList.innerHTML = '';
	        drives.forEach(drive => {
	            const card = document.createElement('div');
	            card.className = 'drive-card';
	            card.dataset.type = drive.type; // upcoming or attended
	            card.onclick = () => showDriveDetails(drive.pldId);
	            card.innerHTML = `
	                <div class="drive-details">
	                    <div class="company-logo">
	                        <i class="fas fa-briefcase"></i>
	                    </div>
	                    <div class="drive-info">
	                        <h4>${drive.name || 'Unnamed Drive'}</h4>
	                        <div class="drive-meta">
	                            <span><i class="fas fa-calendar"></i> ${drive.startDate}</span>
	                            <span><i class="fas fa-map-marker-alt"></i> ${drive.location}</span>
	                            <span><i class="fas fa-users"></i> ${drive.applicants} Applicants</span>
                        	</div>
	                    </div>
	                </div>
	                <span class="drive-status">${drive.status}</span>
	            `;
	            drivesList.appendChild(card);
	        });
        
    } catch (error) {
        console.error('Error fetching drives:', error);
        drivesList.innerHTML = '<div>Error loading drives. Please try again.</div>';
    }
});


	// Load Section and Navigate
		window.applyForDrive = function() {
		    const path = window.location.pathname;
		    let role = '';
		
		    if (path.includes('/student/')) {
		        role = 'student';
		    } else if (path.includes('/admin/')) {
		        role = 'admin';
		    } else if (path.includes('/hr/')) {
		        role = 'hr';
		    }
		
		    // Redirect to the correct section URL based on role
		    window.location.href = `/placement-tracker/${role}/eligible-drives`;
		};

	

	// Show Drives (Filter Drives)
	window.showDrives = function(filter) {
	    console.log('Filtering drives by:', filter);
	    document.querySelectorAll('.tab').forEach(tab => {
	        tab.classList.remove('active');
	        if (tab.textContent.toLowerCase().includes(filter === 'all' ? 'all' : filter)) {
	            tab.classList.add('active');
	        }
	    });
	    const drivesList = document.getElementById('drives-list');
	    const driveCards = drivesList.querySelectorAll('.drive-card');
	    driveCards.forEach(card => {
	        const driveType = card.dataset.type.toLowerCase();
	        if (filter === 'all' || driveType === filter) {
	            card.style.display = 'flex';
	        } else {
	            card.style.display = 'none';
	        }
	    });
	};
    // Show Drive Details (Mocked)
    /*window.showDriveDetails = function(driveId) {
        const popup = document.getElementById('driveDetailsPopup');
        const content = document.getElementById('driveDetailsContent');
        const mockDetails = {
            1: { name: 'Google Hiring 2025', company: 'Google', startDate: 'Jan 15, 2025', location: 'Main Campus', applicants: 50, status: 'Upcoming' },
            2: { name: 'Amazon SDE Drive', company: 'Amazon', startDate: 'Feb 10, 2025', location: 'Tech Park', applicants: 30, status: 'Ongoing' }
        };
        const drive = mockDetails[driveId] || { name: 'Unknown', company: 'N/A', startDate: 'N/A', location: 'N/A', applicants: 0, status: 'N/A' };
        content.innerHTML = `
            <p><strong>Name:</strong> ${drive.name}</p>
            <p><strong>Company:</strong> ${drive.company}</p>
            <p><strong>Start Date:</strong> ${drive.startDate}</p>
            <p><strong>Location:</strong> ${drive.location}</p>
            <p><strong>Applicants:</strong> ${drive.applicants}</p>
            <p><strong>Status:</strong> ${drive.status}</p>
        `;
        popup.classList.add('active');
    };*/
    window.showDriveDetails = async function(driveId) {
        console.log('Fetching details for driveId:', driveId);
        const popup = document.getElementById('driveDetailsPopup');
        const content = document.getElementById('driveDetailsContent');

        if (!popup || !content) {
            console.error('Popup or content element not found:', { popup: !!popup, content: !!content });
            return;
        }
        try {
            const response = await fetch("../student/drive-details/"+driveId, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const drive = await response.json();
            console.log('Drive details fetched:', drive);

            // Validate response
            if (!drive || !drive.name) {
                throw new Error('Invalid drive data received');
            }

            content.innerHTML = `
                <p><strong>Name:</strong> ${drive.name}</p>
                <p><strong>Company:</strong> ${drive.company}</p>
                <p><strong>Start Date:</strong> ${drive.startDate}</p>
                <p><strong>Location:</strong> ${drive.location}</p>
                <p><strong>Applicants:</strong> ${drive.applicants}</p>
            `;

            // Store driveId for editDrive
            popup.dataset.driveId = driveId;

            popup.classList.add('active');
            console.log('Drive details popup opened');
        } catch (error) {
            console.error('Error fetching drive details:', error);
            content.innerHTML = '<p>Error loading drive details. Please try again.</p>';
            popup.classList.add('active');
        }
    };

    // Edit Drive (Mocked)
    window.editDrive = function() {
         const path = window.location.pathname;
		    let role = '';
		
		    if (path.includes('/student/')) {
		        role = 'student';
		    } else if (path.includes('/admin/')) {
		        role = 'admin';
		    } else if (path.includes('/hr/')) {
		        role = 'hr';
		    }
		
		    // Redirect to the correct section URL based on role
		    window.location.href = `/placement-tracker/${role}/placement-drives`;
		};

    // Show Notifications (Mocked)
   /* window.showNotifications = async function(userId) {
        const popup = document.getElementById('notificationsPopup');
        const list = document.getElementById('notificationsList');
        const response = await fetch("../getNotifications?userId="+userId, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            const notifications = await response.json();
            console.log('Notifications Fetched:', notifications);

            // Validate response
            if (!notifications) {
                throw new Error('Invalid notifications data received');
            }
        
        list.innerHTML = notifications.map(notif => `
            <div class="notification-item">
                <div class="notification-message">${notif.message}</div>
            </div>
        `).join('');
        popup.classList.add('active');
    };*/
    window.showNotifications = async function(userId) {
    // Validate userId
    if (!userId || typeof userId !== 'string') {
        console.error('Invalid userId provided');
        alert('Error: Invalid user ID');
        return;
    }

    const popup = document.getElementById('notificationsPopup');
    const list = document.getElementById('notificationsList');

    // Check if DOM elements exist
    if (!popup || !list) {
        console.error('Required DOM elements not found');
        alert('Error: Unable to display notifications');
        return;
    }

    try {
        const response = await fetch(`../getNotifications?userId=${encodeURIComponent(userId)}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const notifications = await response.json();
        console.log('Notifications Fetched:', notifications);

        // Validate response
        if (!Array.isArray(notifications)) {
            throw new Error('Invalid notifications data received');
        }
        
         list.innerHTML = notifications.length > 0 ? notifications.map(notif => `
            <div class="notification-item">
                <div class="notification-message">${notif.ntf_message}</div>
            </div>
        `).join('') : '<div class="notification-item">No notifications available</div>';
        popup.classList.add('active');
    } catch (error) {
        console.error('Error fetching notifications:', error);
        list.innerHTML = '<div class="notification-item">Failed to load notifications</div>';
        popup.classList.add('active'); // Still show popup with error message
    }
};
    // Close Notifications
    window.closeNotifications = function() {
        document.getElementById('notificationsPopup').classList.remove('active');
    };

    // Close Drive Details
    window.closeDriveDetails = function() {
        document.getElementById('driveDetailsPopup').classList.remove('active');
    };
});
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.cpt.model.PlacementDrive, java.util.List, java.text.SimpleDateFormat, java.util.Map, com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<spring:url value="/resources/css/styles.css" var="cssUrl" />
<spring:url value="/resources/js/dashboard.js" var="jsUrl" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Placement Tracker</title>
    <link rel="stylesheet" href="${cssUrl}" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.7.1/chart.min.js"></script>
</head>
<body>
<jsp:include page="./shared/sidebar_admin.jsp" />

<%
    Long companyCount = (Long) request.getAttribute("companyCount");
    Long studentCount = (Long) request.getAttribute("studentCount");
    Long activeDriveCount = (Long) request.getAttribute("activeDriveCount");
    Long placementCount = (Long) request.getAttribute("placementCount");
    Double placementConversionRate = (Double) request.getAttribute("placementConversionRate");
    List<Map<String, Object>> companyWisePlacements = (List<Map<String, Object>>) request.getAttribute("companyWisePlacements");
    List<Map<String, Object>> branchGenderWisePlacements = (List<Map<String, Object>>) request.getAttribute("branchGenderWisePlacements");
    List<Map<String, Object>> roundWiseSuccessRate = (List<Map<String, Object>>) request.getAttribute("roundWiseSuccessRate");
    Map<String, Long> driveStatusCounts = (Map<String, Long>) request.getAttribute("driveStatusCounts");

    // Validate and set drive status counts with fallbacks
    Long upcoming = driveStatusCounts != null && driveStatusCounts.get("upcoming") != null ? driveStatusCounts.get("upcoming") : 0L;
    Long ongoing = driveStatusCounts != null && driveStatusCounts.get("ongoing") != null ? driveStatusCounts.get("ongoing") : 0L;
    Long completed = driveStatusCounts != null && driveStatusCounts.get("completed") != null ? driveStatusCounts.get("completed") : 0L;

    // Check for null attributes and display error if critical data is missing
    if (companyCount == null || studentCount == null || activeDriveCount == null || placementCount == null) {
        out.println("<p>Error: Missing required dashboard data. Please check server logs.</p>");
        return;
    }

    // Initialize Jackson ObjectMapper for JSON serialization
    ObjectMapper objectMapper = new ObjectMapper();
    String companyWisePlacementsJson;
    String branchGenderWisePlacementsJson;
    String roundWiseSuccessRateJson;
    try {
        companyWisePlacementsJson = objectMapper.writeValueAsString(companyWisePlacements != null ? companyWisePlacements : new java.util.ArrayList<>());
        branchGenderWisePlacementsJson = objectMapper.writeValueAsString(branchGenderWisePlacements != null ? branchGenderWisePlacements : new java.util.ArrayList<>());
        roundWiseSuccessRateJson = objectMapper.writeValueAsString(roundWiseSuccessRate != null ? roundWiseSuccessRate : new java.util.ArrayList<>());
    } catch (Exception e) {
        out.println("<p>Error: Failed to serialize JSON data. Check server logs for details.</p>");
        e.printStackTrace();
        return;
    }
%>

<!-- Main Content -->
<div class="main-content">
    <div class="section-title">Admin Dashboard</div>

    <!-- Dashboard Stats -->
    <div class="dashboard-stats">
        <div class="stat-card">
            <div class="icon blue"><i class="fas fa-building"></i></div>
            <h3>Total Companies</h3>
            <div class="number"><% out.print(companyCount); %></div>
            <div class="ripple"></div>
        </div>
        <div class="stat-card">
            <div class="icon purple"><i class="fas fa-users"></i></div>
            <h3>Total Students</h3>
            <div class="number"><% out.print(studentCount); %></div>
            <div class="ripple"></div>
        </div>
        <div class="stat-card">
            <div class="icon green"><i class="fas fa-briefcase"></i></div>
            <h3>Active Drives</h3>
            <div class="number"><% out.print(activeDriveCount); %></div>
            <div class="ripple"></div>
        </div>
        <div class="stat-card">
            <div class="icon orange"><i class="fas fa-check-circle"></i></div>
            <h3>Placements</h3>
            <div class="number"><% out.print(placementCount); %></div>
            <div class="ripple"></div>
        </div>
        <div class="stat-card">
            <div class="icon blue"><i class="fas fa-percentage"></i></div>
            <h3>Placement Conversion</h3>
            <div class="number"><% out.print(String.format("%.2f%%", placementConversionRate != null ? placementConversionRate : 0.0)); %></div>
            <div class="ripple"></div>
        </div>
    </div>

    <!-- Charts Section -->
    <div class="charts-section">
        <div class="chart-card">
            <div class="chart-header">
                <div class="chart-title">Placement Metrics</div>
                <div class="chart-options">
                    <select onchange="updateCharts(this.value)">
                        <option value="all">All Time</option>
                        <option value="year">This Year</option>
                        <option value="month">This Month</option>
                    </select>
                </div>
            </div>
            <div class="chart-container">
                <canvas id="placementMetricsChart"></canvas>
            </div>
        </div>
        <div class="chart-card">
            <div class="chart-header">
                <div class="chart-title">Drive Status Distribution</div>
                <div class="chart-options">
                    <select onchange="updateCharts(this.value)">
                        <option value="all">All Drives</option>
                        <option value="active">Active Drives</option>
                        <option value="completed">Completed Drives</option>
                    </select>
                </div>
            </div>
            <div class="chart-container">
                <% if (upcoming == 0 && ongoing == 0 && completed == 0) { %>
                    <p>No drive status data available.</p>
                <% } else { %>
                    <canvas id="driveStatusChart"></canvas>
                <% } %>
            </div>
        </div>
        <div class="chart-card">
            <div class="chart-header">
                <div class="chart-title">Company-Wise Placements by Branch</div>
                <div class="chart-options">
                    <select onchange="updateCompanyWiseChart(this.value)">
                        <option value="all">All Companies</option>
                        <option value="top5">Top 5 Companies</option>
                    </select>
                </div>
            </div>
            <div class="chart-container">
                <canvas id="companyWisePlacementsChart"></canvas>
            </div>
        </div>
        <div class="chart-card">
            <div class="chart-header">
                <div class="chart-title">Branch & Gender-Wise Placements</div>
                <div class="chart-options">
                    <select onchange="updateBranchGenderChart(this.value)">
                        <option value="all">All Branches</option>
                        <option value="gender">By Gender</option>
                    </select>
                </div>
            </div>
            <div class="chart-container">
                <canvas id="branchGenderPlacementsChart"></canvas>
            </div>
        </div>
        <div class="chart-card">
            <div class="chart-header">
                <div class="chart-title">Rounds-Wise Success Rate</div>
                <div class="chart-options">
                    <select onchange="updateRoundWiseChart(this.value)">
                        <option value="all">All Drives</option>
                        <option value="recent">Recent Drives</option>
                    </select>
                </div>
            </div>
            <div class="chart-container">
                <canvas id="roundWiseSuccessRateChart"></canvas>
            </div>
        </div>
    </div>

    <!-- Drives Section -->
    <div class="drives-section">
        <div class="section-title">Placement Drives</div>
        <div class="tabs">
            <div class="tab active" onclick="showDrives('all')">All Drives</div>
            <div class="tab" onclick="showDrives('upcoming')">Upcoming Drives</div>
            <div class="tab" onclick="showDrives('ongoing')">Ongoing Drives</div>
            <div class="tab" onclick="showDrives('completed')">Completed Drives</div>
        </div>
        <div class="drives-list" id="drives-list">
        </div>
    </div>

    <!-- Management Cards -->
    <div class="management-cards">
        <div class="management-card">
            <div class="card-header">
                <h3>Placement Drives</h3>
                <p>Manage placement drives</p>
            </div>
            <div class="card-body">
                <div class="action-buttons">
                    <a href="add-placement-drive" class="action-btn btn-add"><i class="fas fa-plus"></i> Add Drive</a>
                    <a href="placement-drives" class="action-btn btn-manage"><i class="fas fa-list"></i> Manage</a>
                </div>
            </div>
        </div>
        <div class="management-card">
            <div class="card-header">
                <h3>Companies</h3>
                <p>Add or manage companies</p>
            </div>
            <div class="card-body">
                <div class="action-buttons">
                    <a href="add-company" class="action-btn btn-add"><i class="fas fa-plus"></i> Add Company</a>
                    <a href="companies" class="action-btn btn-manage"><i class="fas fa-list"></i> Manage</a>
                </div>
            </div>
        </div>
        <div class="management-card">
            <div class="card-header">
                <h3>Colleges</h3>
                <p>Add or manage colleges</p>
            </div>
            <div class="card-body">
                <div class="action-buttons">
                    <a href="add-college" class="action-btn btn-add"><i class="fas fa-plus"></i> Add College</a>
                    <a href="colleges" class="action-btn btn-manage"><i class="fas fa-list"></i> Manage</a>
                </div>
            </div>
        </div>
        <div class="management-card">
            <div class="card-header">
                <h3>Branches</h3>
                <p>Add or manage branches</p>
            </div>
            <div class="card-body">
                <div class="action-buttons">
                    <a href="add-branch" class="action-btn btn-add"><i class="fas fa-plus"></i> Add Branch</a>
                    <a href="branches" class="action-btn btn-manage"><i class="fas fa-list"></i> Manage</a>
                </div>
            </div>
        </div>
        <div class="management-card">
            <div class="card-header">
                <h3>Resources</h3>
                <p>Allocate resources for drives</p>
            </div>
            <div class="card-body">
                <div class="action-buttons">
                    <a href="resource-allocations" class="action-btn btn-add"><i class="fas fa-cogs"></i> Allocate</a>
                </div>
            </div>
        </div>
         <div class="management-card">
            <div class="card-header">
                <h3>Students</h3>
                <p>Add or manage Students</p>
            </div>
            <div class="card-body">
                <div class="action-buttons">
                    <a href="addstudents" class="action-btn btn-add"><i class="fas fa-plus"></i> Add Students</a>
                    <a href="#" class="action-btn btn-manage"><i class="fas fa-list"></i> Manage</a>
                </div>
            </div>
        </div>
        <div class="management-card">
            <div class="card-header">
                <h3>Resources</h3>
                <p>Add or manage Resources</p>
            </div>
            <div class="card-body">
                <div class="action-buttons">
                    <a href="rescrud" class="action-btn btn-add"><i class="fas fa-plus"></i> Add Resources</a>
                    <a href="resources" class="action-btn btn-manage"><i class="fas fa-list"></i> Manage</a>
                </div>
            </div>
        </div>
    </div>



    <!-- Drive Details Popup -->
    <div class="popup-overlay" id="driveDetailsPopup">
        <div class="popup-content">
            <div class="popup-header">
                <div class="popup-title">Drive Details</div>
                <button class="close-popup" onclick="closeDriveDetails()">×</button>
            </div>
            <div class="popup-body" id="driveDetailsContent"></div>
            <div class="popup-footer">
                <button class="popup-btn btn-outline" onclick="closeDriveDetails()">Close</button>
                <button class="popup-btn btn-primary" onclick="editDrive()">Edit Drive</button>
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
    // Log page load
    console.log('Admin Dashboard JSP loaded at:', new Date().toISOString());

    // Define chart data with fallbacks
    const placementData = {
        companyCount: <%= companyCount != null ? companyCount : 0 %>,
        studentCount: <%= studentCount != null ? studentCount : 0 %>,
        activeDriveCount: <%= activeDriveCount != null ? activeDriveCount : 0 %>,
        placementCount: <%= placementCount != null ? placementCount : 0 %>
    };
    const driveStatusData = {
        upcoming: <%= upcoming != null ? upcoming : 0 %>,
        ongoing: <%= ongoing != null ? ongoing : 0 %>,
        completed: <%= completed != null ? completed : 0 %>
    };
    const placementConversionRate = <%= placementConversionRate != null ? placementConversionRate : 0 %>;
    const companyWisePlacements = <%= companyWisePlacementsJson %>;
    const branchGenderWisePlacements = <%= branchGenderWisePlacementsJson %>;
    const roundWiseSuccessRate = <%= roundWiseSuccessRateJson %>;

    // Debug data
    console.log('Placement Data:', placementData);
    console.log('Drive Status Data:', driveStatusData);
    console.log('Placement Conversion Rate:', placementConversionRate);
    console.log('Company Wise Placements:', companyWisePlacements);
    console.log('Branch Gender Wise Placements:', branchGenderWisePlacements);
    console.log('Round Wise Success Rate:', roundWiseSuccessRate);

    document.addEventListener('DOMContentLoaded', () => {
        console.log('DOMContentLoaded fired');

        if (typeof Chart === 'undefined') {
            console.error('Chart.js is not loaded. Verify that chart.min.js is in resources/js/ and loaded via ${jsUrl}/chart.min.js');
            return;
        }
        console.log('Chart.js is loaded:', Chart.version);

        // Placement Metrics Bar Chart
        const placementMetricsChart = document.getElementById('placementMetricsChart');
        let placementMetricsChartInstance = null;
        if (placementMetricsChart) {
            try {
                placementMetricsChartInstance = new Chart(placementMetricsChart, {
                    type: 'bar',
                    data: {
                        labels: ['Companies', 'Students', 'Active Drives', 'Placements'],
                        datasets: [{
                            label: 'Placement Metrics',
                            data: [
                                placementData.companyCount,
                                placementData.studentCount,
                                placementData.activeDriveCount,
                                placementData.placementCount
                            ],
                            backgroundColor: ['#4361ee', '#7c3aed', '#10b981', '#f59e0b'],
                            borderColor: ['#4361ee', '#7c3aed', '#10b981', '#f59e0b'],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            y: { beginAtZero: true, title: { display: true, text: 'Count' } },
                            x: { title: { display: true, text: 'Metrics' } }
                        },
                        plugins: { legend: { display: false } }
                    }
                });
                console.log('Placement Metrics Chart initialized');
            } catch (error) {
                console.error('Error initializing Placement Metrics Chart:', error);
            }
        } else {
            console.error('Placement Metrics Chart canvas not found');
        }

        // Drive Status Pie Chart
        const driveStatusChart = document.getElementById('driveStatusChart');
        let driveStatusChartInstance = null;
        if (!driveStatusChart) {
            console.error('Drive Status Chart canvas element not found. Check if <canvas id="driveStatusChart"> exists in the DOM.');
        } else if (!driveStatusData || 
                   typeof driveStatusData.upcoming !== 'number' || 
                   typeof driveStatusData.ongoing !== 'number' || 
                   typeof driveStatusData.completed !== 'number') {
            console.error('Invalid driveStatusData. Expected {upcoming: number, ongoing: number, completed: number}, got:', driveStatusData);
        } else if (driveStatusData.upcoming === 0 && driveStatusData.ongoing === 0 && driveStatusData.completed === 0) {
            console.warn('No drive status data to display (all counts are zero)');
        } else {
            console.log('Initializing Drive Status Pie Chart with data:', driveStatusData);
            try {
                driveStatusChartInstance = new Chart(driveStatusChart, {
                    type: 'pie',
                    data: {
                        labels: ['Upcoming', 'Ongoing', 'Completed'],
                        datasets: [{
                            data: [
                                driveStatusData.upcoming,
                                driveStatusData.ongoing,
                                driveStatusData.completed
                            ],
                            backgroundColor: ['#4361ee', '#10b981', '#7c3aed'],
                            borderColor: ['#4361ee', '#10b981', '#7c3aed'],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: { position: 'bottom' },
                            tooltip: {
                                callbacks: {
                                    label: function(context) {
                                        let label = context.label || '';
                                        let value = context.parsed || 0;
                                        return `${label}: ${value} drives`;
                                    }
                                }
                            }
                        }
                    }
                });
                console.log('Drive Status Pie Chart initialized successfully');
            } catch (error) {
                console.error('Error initializing Drive Status Pie Chart:', error);
            }
        }

        // Company-Wise Placements Stacked Bar Chart
        const companyWisePlacementsChart = document.getElementById('companyWisePlacementsChart');
        let companyWisePlacementsChartInstance = null;
        if (companyWisePlacementsChart && companyWisePlacements && Array.isArray(companyWisePlacements)) {
            try {
                const companies = [...new Set(companyWisePlacements.map(item => item.cmp_name))];
                const branches = [...new Set(companyWisePlacements.map(item => item.brn_name))];
                const datasets = branches.map(branch => ({
                    label: branch,
                    data: companies.map(company => {
                        const record = companyWisePlacements.find(r => r.cmp_name === company && r.brn_name === branch);
                        return record ? record.placement_count : 0;
                    }),
                    backgroundColor: '#' + Math.floor(Math.random() * 16777215).toString(16)
                }));

                companyWisePlacementsChartInstance = new Chart(companyWisePlacementsChart, {
                    type: 'bar',
                    data: { labels: companies, datasets: datasets },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            x: { stacked: true, title: { display: true, text: 'Companies' } },
                            y: { stacked: true, beginAtZero: true, title: { display: true, text: 'Placement Count' } }
                        },
                        plugins: { legend: { position: 'top' } }
                    }
                });
                console.log('Company-Wise Placements Chart initialized');
            } catch (error) {
                console.error('Error initializing Company-Wise Placements Chart:', error);
            }
        } else {
            console.error('Company-Wise Placements Chart failed: canvas missing or invalid data', { canvas: !!companyWisePlacementsChart, data: companyWisePlacements });
        }

        // Branch & Gender-Wise Placements Grouped Bar Chart
        const branchGenderPlacementsChart = document.getElementById('branchGenderPlacementsChart');
        let branchGenderPlacementsChartInstance = null;
        if (branchGenderPlacementsChart && branchGenderWisePlacements && Array.isArray(branchGenderWisePlacements)) {
            try {
                const branches = [...new Set(branchGenderWisePlacements.map(item => item.brn_name))];
                const genders = [...new Set(branchGenderWisePlacements.map(item => item.gender))];
                const datasets = genders.map(gender => ({
                    label: gender,
                    data: branches.map(branch => {
                        const record = branchGenderWisePlacements.find(r => r.brn_name === branch && r.gender === gender);
                        return record ? record.placement_count : 0;
                    }),
                    backgroundColor: gender === 'M' ? '#4361ee' : '#f59e0b'
                }));

                branchGenderPlacementsChartInstance = new Chart(branchGenderPlacementsChart, {
                    type: 'bar',
                    data: { labels: branches, datasets: datasets },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            y: { beginAtZero: true, title: { display: true, text: 'Placement Count' } },
                            x: { title: { display: true, text: 'Branches' } }
                        },
                        plugins: { legend: { position: 'top' } }
                    }
                });
                console.log('Branch & Gender-Wise Placements Chart initialized');
            } catch (error) {
                console.error('Error initializing Branch & Gender-Wise Placements Chart:', error);
            }
        } else {
            console.error('Branch & Gender-Wise Placements Chart failed: canvas missing or invalid data', { canvas: !!branchGenderPlacementsChart, data: branchGenderWisePlacements });
        }

        // Rounds-Wise Success Rate Line Chart
        const roundWiseSuccessRateChart = document.getElementById('roundWiseSuccessRateChart');
        let roundWiseSuccessRateChartInstance = null;
        if (roundWiseSuccessRateChart && roundWiseSuccessRate && Array.isArray(roundWiseSuccessRate)) {
            try {
                const drives = [...new Set(roundWiseSuccessRate.map(item => item.pld_name))];
                const phases = [...new Set(roundWiseSuccessRate.map(item => item.hph_name))];
                const datasets = drives.map(drive => ({
                    label: drive,
                    data: phases.map(phase => {
                        const record = roundWiseSuccessRate.find(r => r.pld_name === drive && r.hph_name === phase);
                        return record ? record.success_rate : 0;
                    }),
                    fill: false,
                    borderColor: '#' + Math.floor(Math.random() * 16777215).toString(16),
                    tension: 0.1
                }));

                roundWiseSuccessRateChartInstance = new Chart(roundWiseSuccessRateChart, {
                    type: 'line',
                    data: { labels: phases, datasets: datasets },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                            y: { beginAtZero: true, max: 100, title: { display: true, text: 'Success Rate (%)' } },
                            x: { title: { display: true, text: 'Hiring Phases' } }
                        },
                        plugins: { legend: { position: 'top' } }
                    }
                });
                console.log('Rounds-Wise Success Rate Chart initialized');
            } catch (error) {
                console.error('Error initializing Rounds-Wise Success Rate Chart:', error);
            }
        } else {
            console.error('Rounds-Wise Success Rate Chart failed: canvas missing or invalid data', { canvas: !!roundWiseSuccessRateChart, data: roundWiseSuccessRate });
        }

        // Chart Update Functions
        window.updateCharts = async function(period) {
            console.log('Updating charts for period:', period);
            const mockData = {
                all: {
                    companyCount: placementData.companyCount,
                    studentCount: placementData.studentCount,
                    activeDriveCount: placementData.activeDriveCount,
                    placementCount: placementData.placementCount,
                    driveStatusCounts: {
                        upcoming: driveStatusData.upcoming,
                        ongoing: driveStatusData.ongoing,
                        completed: driveStatusData.completed
                    }
                },
                year: {
                    companyCount: Math.round(placementData.companyCount * 0.8),
                    studentCount: Math.round(placementData.studentCount * 0.9),
                    activeDriveCount: Math.round(placementData.activeDriveCount * 0.7),
                    placementCount: Math.round(placementData.placementCount * 0.85),
                    driveStatusCounts: {
                        upcoming: Math.round(driveStatusData.upcoming * 0.6),
                        ongoing: Math.round(driveStatusData.ongoing * 0.8),
                        completed: Math.round(driveStatusData.completed * 0.9)
                    }
                },
                month: {
                    companyCount: Math.round(placementData.companyCount * 0.3),
                    studentCount: Math.round(placementData.studentCount * 0.4),
                    activeDriveCount: Math.round(placementData.activeDriveCount * 0.5),
                    placementCount: Math.round(placementData.placementCount * 0.35),
                    driveStatusCounts: {
                        upcoming: Math.round(driveStatusData.upcoming * 0.4),
                        ongoing: Math.round(driveStatusData.ongoing * 0.5),
                        completed: Math.round(driveStatusData.completed * 0.3)
                    }
                },
                active: {
                    driveStatusCounts: {
                        upcoming: driveStatusData.upcoming,
                        ongoing: driveStatusData.ongoing,
                        completed: 0
                    }
                },
                completed: {
                    driveStatusCounts: {
                        upcoming: 0,
                        ongoing: 0,
                        completed: driveStatusData.completed
                    }
                }
            };
            const data = mockData[period] || mockData.all;
            if (placementMetricsChartInstance && period !== 'active' && period !== 'completed') {
                placementMetricsChartInstance.data.datasets[0].data = [
                    data.companyCount,
                    data.studentCount,
                    data.activeDriveCount,
                    data.placementCount
                ];
                placementMetricsChartInstance.update();
                console.log('Placement Metrics Chart updated');
            }
            if (driveStatusChartInstance) {
                driveStatusChartInstance.data.datasets[0].data = [
                    data.driveStatusCounts.upcoming,
                    data.driveStatusCounts.ongoing,
                    data.driveStatusCounts.completed
                ];
                driveStatusChartInstance.update();
                console.log('Drive Status Pie Chart updated');
            }
        };

        // Update Company-Wise Chart
        window.updateCompanyWiseChart = function(filter) {
            if (!companyWisePlacementsChartInstance) return;
            let filteredData = companyWisePlacements;
            if (filter === 'top5') {
                const companyTotals = companyWisePlacements.reduce((acc, item) => {
                    acc[item.cmp_name] = (acc[item.cmp_name] || 0) + parseInt(item.placement_count);
                    return acc;
                }, {});
                const topCompanies = Object.keys(companyTotals)
                    .sort((a, b) => companyTotals[b] - companyTotals[a])
                    .slice(0, 5);
                filteredData = companyWisePlacements.filter(item => topCompanies.includes(item.cmp_name));
            }
            const companies = [...new Set(filteredData.map(item => item.cmp_name))];
            const branches = [...new Set(filteredData.map(item => item.brn_name))];
            const datasets = branches.map(branch => ({
                label: branch,
                data: companies.map(company => {
                    const record = filteredData.find(r => r.cmp_name === company && r.brn_name === branch);
                    return record ? record.placement_count : 0;
                }),
                backgroundColor: '#' + Math.floor(Math.random() * 16777215).toString(16)
            }));
            companyWisePlacementsChartInstance.data.labels = companies;
            companyWisePlacementsChartInstance.data.datasets = datasets;
            companyWisePlacementsChartInstance.update();
            console.log('Company-Wise Placements Chart updated');
        };

        // Update Branch & Gender-Wise Chart
        window.updateBranchGenderChart = function(filter) {
            if (!branchGenderPlacementsChartInstance) return;
            const branches = [...new Set(branchGenderWisePlacements.map(item => item.brn_name))];
            let datasets = [];
            if (filter === 'gender') {
                const genders = [...new Set(branchGenderWisePlacements.map(item => item.gender))];
                datasets = genders.map(gender => ({
                    label: gender,
                    data: branches.map(branch => {
                        const record = branchGenderWisePlacements.find(r => r.brn_name === branch && r.gender === gender);
                        return record ? record.placement_count : 0;
                    }),
                    backgroundColor: gender === 'Male' ? '#4361ee' : '#f59e0b'
                }));
            } else {
                datasets = [{
                    label: 'Placements',
                    data: branches.map(branch => {
                        const total = branchGenderWisePlacements
                            .filter(r => r.brn_name === branch)
                            .reduce((sum, r) => sum + parseInt(r.placement_count), 0);
                        return total;
                    }),
                    backgroundColor: '#4361ee'
                }];
            }
            branchGenderPlacementsChartInstance.data.labels = branches;
            branchGenderPlacementsChartInstance.data.datasets = datasets;
            branchGenderPlacementsChartInstance.update();
            console.log('Branch & Gender-Wise Placements Chart updated');
        };

        // Update Rounds-Wise Success Rate Chart
        window.updateRoundWiseChart = function(filter) {
            if (!roundWiseSuccessRateChartInstance) return;
            let filteredData = roundWiseSuccessRate;
            if (filter === 'recent') {
                const drives = [...new Set(roundWiseSuccessRate.map(item => item.pld_name))].slice(-5);
                filteredData = roundWiseSuccessRate.filter(item => drives.includes(item.pld_name));
            }
            const drives = [...new Set(filteredData.map(item => item.pld_name))];
            const phases = [...new Set(filteredData.map(item => item.hph_name))];
            const datasets = drives.map(drive => ({
                label: drive,
                data: phases.map(phase => {
                    const record = filteredData.find(r => r.pld_name === drive && r.hph_name === phase);
                    return record ? record.success_rate : 0;
                }),
                fill: false,
                borderColor: '#' + Math.floor(Math.random() * 16777215).toString(16),
                tension: 0.1
            }));
            roundWiseSuccessRateChartInstance.data.labels = phases;
            roundWiseSuccessRateChartInstance.data.datasets = datasets;
            roundWiseSuccessRateChartInstance.update();
            console.log('Rounds-Wise Success Rate Chart updated');
        };
    });



    function showDrives(filter) {
        document.querySelectorAll('.tab').forEach(tab => tab.classList.remove('active'));
        event.target.classList.add('active');
        // Implement drive filtering
    }
</script>
<script type="text/javascript" src="${jsUrl}"></script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="bg-white p-6 rounded shadow">
    <h2 class="text-xl font-bold mb-4">Student Dashboard</h2>
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
            <h3 class="text-lg mb-2">Future Drives</h3>
            <table class="w-full border">
                <thead>
                    <tr class="bg-gray-200">
                        <th class="p-2">Name</th>
                        <th class="p-2">Start Date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="drive" items="${drives}">
                        <tr>
                            <td class="p-2">${drive.name}</td>
                            <td class="p-2">${drive.startDate}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div>
            <h3 class="text-lg mb-2">Eligible Drives</h3>
            <table class="w-full border">
                <thead>
                    <tr class="bg-gray-200">
                        <th class="p-2">Name</th>
                        <th class="p-2">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="drive" items="${eligibleDrives}">
                        <tr>
                            <td class="p-2">${drive.name}</td>
                            <td class="p-2">
                                <a href="/student/application/manage" class="text-blue-600 underline">Apply</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div>
            <h3 class="text-lg mb-2">Attended Drives</h3>
            <table class="w-full border">
                <thead>
                    <tr class="bg-gray-200">
                        <th class="p-2">Drive ID</th>
                        <th class="p-2">Company ID</th>
                        <th class="p-2">Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="app" items="${applications}">
                        <tr>
                            <td class="p-2">${app.placementDriveId}</td>
                            <td class="p-2">${app.companyId}</td>
                            <td class="p-2">${app.status}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <div>
            <h3 class="text-lg mb-2">Notifications</h3>
            <ul>
                <c:forEach var="notification" items="${notifications}">
                    <li class="${notification.read ? '' : 'text-red-500 font-bold'}">
                        ${notification.message} (${notification.date})
                        <c:if test="${!notification.read}">
                            <a href="/notification/read/${notification.id}" class="text-blue-600 underline">Mark as Read</a>
                        </c:if>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</div>
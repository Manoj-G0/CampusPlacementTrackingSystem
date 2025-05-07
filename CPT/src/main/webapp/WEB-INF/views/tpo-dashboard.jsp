<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="bg-white p-6 rounded shadow">
    <h2 class="text-xl font-bold mb-4">TPO Dashboard</h2>
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
            <h3 class="text-lg mb-2">Placement Drives</h3>
            <a href="/tpo/drive/manage" class="text-blue-600 underline">Manage Drives</a>
            <table class="w-full border mt-2">
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
            <h3 class="text-lg mb-2">Management Actions</h3>
            <ul>
                <li><a href="/tpo/college/manage" class="text-blue-600 underline">College Management</a></li>
                <li><a href="/tpo/company/manage" class="text-blue-600 underline">Company Management</a></li>
                <li><a href="/tpo/resource/manage" class="text-blue-600 underline">Resource Allocation</a></li>
            </ul>
        </div>
    </div>
</div>
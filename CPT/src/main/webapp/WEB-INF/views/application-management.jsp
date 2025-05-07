<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="bg-white p-6 rounded shadow">
    <h2 class="text-xl font-bold mb-4">Application Management</h2>
    <form action="/student/application/save" method="post" class="mb-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
                <label class="block text-gray-700">Placement Drive</label>
                <select name="placementDriveId" class="w-full p-2 border rounded" required>
                    <c:forEach var="drive" items="${eligibleDrives}">
                        <option value="${drive.id}">${drive.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div>
                <label class="block text-gray-700">Company ID</label>
                <input type="number" name="companyId" class="w-full p-2 border rounded" required/>
            </div>
        </div>
        <button type="submit" class="mt-4 bg-blue-600 text-white p-2 rounded">Apply</button>
    </form>
    <h3 class="text-lg mb-2">Your Applications</h3>
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
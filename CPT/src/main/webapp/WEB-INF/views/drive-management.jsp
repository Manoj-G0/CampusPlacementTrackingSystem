<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div class="bg-white p-6 rounded shadow">
    <h2 class="text-xl font-bold mb-4">Placement Drive Management</h2>
    <form action="/tpo/drive/save" method="post" class="mb-4">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
                <label class="block text-gray-700">Name</label>
                <input type="text" name="name" class="w-full p-2 border rounded" required/>
            </div>
            <div>
                <label class="block text-gray-700">College ID</label>
                <input type="number" name="collegeId" class="w-full p-2 border rounded" required/>
            </div>
            <div>
                <label class="block text-gray-700">Start Date</label>
                <input type="date" name="startDate" class="w-full p-2 border rounded" required/>
            </div>
            <div>
                <label class="block text-gray-700">End Date</label>
                <input type="date" name="endDate" class="w-full p-2 border rounded" required/>
            </div>
            <div>
                <label class="block text-gray-700">Minimum GPA</label>
                <input type="number" step="0.01" name="minGpa" class="w-full p-2 border rounded" required/>
            </div>
            <div>
                <label class="block text-gray-700">Max Backlogs</label>
                <input type="number" name="maxBacklogs" class="w-full p-2 border rounded" required/>
            </div>
            <div>
                <label class="block text-gray-700">Allowed Branches (comma-separated IDs)</label>
                <input type="text" name="allowedBranches" class="w-full p-2 border rounded" required/>
            </div>
            <div>
                <label class="block text-gray-700">Package (in lakhs)</label>
                <input type="number" step="0.01" name="packageAmount" class="w-full p-2 border rounded" required/>
            </div>
        </div>
        <button type="submit" class="mt-4 bg-blue-600 text-white p-2 rounded">Save Drive</button>
    </form>
    <h3 class="text-lg mb-2">Placement Drives</h3>
    <table class="w-full border">
        <thead>
            <tr class="bg-gray-200">
                <th class="p-2">Name</th>
                <th class="p-2">Start Date</th>
                <th class="p-2">End Date</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="drive" items="${drives}">
                <tr>
                    <td class="p-2">${drive.name}</td>
                    <td class="p-2">${drive.startDate}</td>
                    <td class="p-2">${drive.endDate}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
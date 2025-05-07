<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Campus Placement Tracker</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="flex flex-col min-h-screen">
    <!-- Fixed Header -->
    <header class="bg-blue-600 text-white p-4 flex justify-between items-center fixed w-full top-0 z-10">
        <h1 class="text-xl font-bold">Campus Placement Tracker</h1>
        <div>
            <c:if test="${pageContext.request.userPrincipal != null}">
                <span>Welcome, ${pageContext.request.userPrincipal.name}</span>
                <a href="/logout" class="ml-4 underline">Logout</a>
            </c:if>
        </div>
    </header>

    <!-- Main Content with Sidebar -->
    <div class="flex flex-1 mt-16">
        <!-- Fixed Sidebar -->
        <nav class="bg-gray-800 text-white w-64 p-4 fixed h-full hidden md:block">
            <ul>
                <li><a href="/dashboard" class="block py-2 hover:bg-gray-700">Dashboard</a></li>
                <c:if test="${user.role == 'STUDENT'}">
                    <li><a href="/student/application/manage" class="block py-2 hover:bg-gray-700">Applications</a></li>
                </c:if>
                <c:if test="${user.role == 'TPO'}">
                    <li><a href="/tpo/drive/manage" class="block py-2 hover:bg-gray-700">Placement Drives</a></li>
                </c:if>
                <c:if test="${user.role == 'HR'}">
                    <li><a href="/hr/job/manage" class="block py-2 hover:bg-gray-700">Job Descriptions</a></li>
                </c:if>
            </ul>
        </nav>

        <!-- Main Content Area -->
        <main class="md:ml-64 p-4 flex-1 overflow-auto">
            <jsp:include page="${contentPage}"/>
        </main>
    </div>
</body>
</html>
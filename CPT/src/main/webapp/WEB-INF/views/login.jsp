<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Campus Placement Tracker</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="flex items-center justify-center min-h-screen bg-gray-100">
    <div class="bg-white p-8 rounded shadow-md w-full max-w-md">
        <h2 class="text-2xl font-bold mb-4 text-center">Login</h2>
        <c:if test="${param.error != null}">
            <p class="text-red-500 mb-4">Invalid credentials</p>
        </c:if>
        <c:if test="${param.logout != null}">
            <p class="text-green-500 mb-4">You have been logged out</p>
        </c:if>
        <form action="/login" method="post">
            <div class="mb-4">
                <label class="block text-gray-700">Email</label>
                <input type="text" name="username" class="w-full p-2 border rounded"/>
            </div>
            <div class="mb-4">
                <label class="block text-gray-700">Password</label>
                <input type="password" name="password" class="w-full p-2 border rounded"/>
            </div>
            <button type="submit" class="w-full bg-blue-600 text-white p-2 rounded">Login</button>
        </form>
    </div>
</body>
</html>
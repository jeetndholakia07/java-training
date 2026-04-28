<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Edit Student</title>
  <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
  <script>
    function cancelForm() {
      window.location.href = '/students';
    }
  </script>
</head>
<body class="bg-gray-50">
  <jsp:include page="header.jsp"/>
  <div class="max-w-3xl mx-auto mt-12 bg-white p-8 rounded-lg shadow-lg">
  <form action="students" method="post" class="space-y-6">
    <h2 class="text-2xl font-semibold mb-4">Edit Student</h2>
    <input type="hidden" name="action" value="editStudent"/>
    <input type="hidden" name="studentId" value="${student.id}"/>

    <div class="grid grid-cols-1 sm:grid-cols-2 gap-6 mb-6">
      <div>
        <label for="firstName" class="block text-sm font-medium text-gray-700">First Name</label>
        <input type="text" id="firstName" name="firstName" class="mt-2 p-3 w-full border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500" value="${student.firstName}" required>
      </div>
      <div>
        <label for="middleName" class="block text-sm font-medium text-gray-700">Middle Name</label>
        <input type="text" id="middleName" name="middleName" class="mt-2 p-3 w-full border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500" value="${student.middleName}" required>
      </div>
      <div>
        <label for="lastName" class="block text-sm font-medium text-gray-700">Last Name</label>
        <input type="text" id="lastName" name="lastName" class="mt-2 p-3 w-full border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500" value="${student.lastName}" required>
      </div>
    </div>

    <div class="flex items-center justify-end gap-4">
      <button type="submit" class="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 focus:ring-4 focus:ring-blue-300">Save</button>
      <button onclick="cancelForm()" type="button" class="w-full border border-gray-300 px-4 py-2 bg-white text-gray-600 rounded-lg hover:bg-gray-50 focus:ring-4 focus:ring-gray-100">Cancel</button>
    </div>
  </form>
</div>
</body>
</html>
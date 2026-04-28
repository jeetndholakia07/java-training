<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Course Form</title>
  <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
  <script>
    function cancelForm() {
      window.location.href = '/students';
    }
  </script>
</head>
<body class="bg-gray-50">
<div class="container mx-auto px-4 py-6">
  <jsp:include page="header.jsp"/>

  <div class="max-w-3xl mx-auto mt-12 bg-white p-8 rounded-lg shadow-lg">
    <h2 class="text-2xl font-semibold mb-4">Course Form</h2>
    <form action="course" method="post" class="space-y-6">
    <input type="hidden" name="action" value="addCourse"/>
    <div class="mb-6">
      <label for="courseName" class="block text-sm font-medium text-gray-700">Course Name</label>
      <input type="text" id="courseName" name="courseName" class="mt-2 p-3 w-full border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Enter Course Name" required>
    </div>
      <div class="flex items-center justify-end gap-4">
        <button type="submit" class="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 focus:ring-4 focus:ring-blue-300">Save</button>
        <button onclick="cancelForm()" type="button" class="w-full border border-gray-300 px-4 py-2 bg-white text-gray-600 rounded-lg hover:bg-gray-50 focus:ring-4 focus:ring-gray-100">Cancel</button>
      </div>
  </form>
</div>
</body>
</html>
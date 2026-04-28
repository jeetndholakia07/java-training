<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
  <title>Students List</title>
  <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
</head>
<body class="bg-gray-100">

<jsp:include page="header.jsp"/>

<div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-12">
  <div class="flex justify-between mb-6">
    <h2 class="text-2xl font-semibold text-gray-800">Student List</h2>
    <a href="/" class="inline-block bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">Add Student</a>
  </div>

  <table class="min-w-full bg-white border border-gray-200 rounded-lg shadow-lg">
    <thead class="bg-gray-100">
    <tr>
      <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Full Name</th>
      <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Courses</th>
      <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Status</th>
      <th class="px-6 py-3 text-left text-sm font-semibold text-gray-700">Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${not empty studentList}">
      <c:forEach var="s" items="${studentList}">
        <tr class="border-t border-gray-200">
          <td class="px-6 py-4 text-sm text-gray-800">${s.fullName}</td>
          <td class="px-6 py-4 text-sm text-gray-800">
            <c:forEach var="c" items="${s.courses}">
              <span class="inline-block bg-blue-600 text-white text-xs font-semibold py-1 px-2 rounded-full mr-2">${c.courseName}</span>
            </c:forEach>
          </td>
          <td class="px-6 py-4 text-sm text-gray-800">
            <span class="inline-block ${s.status == "A" ? "bg-green-600" : "bg-red-600"} text-white text-xs font-semibold py-1 px-2 rounded-full">${s.status}</span>
          </td>
          <td class="px-6 py-4">
            <a href="/students?action=edit&id=${s.id}" style="display:inline;">
              <input type="hidden" name="studentId" value="${s.id}"/>
              <button class="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700" name="action" value="editStudent">Edit</button>
            </a>
            <form action="students" method="post" style="display:inline;">
              <input type="hidden" name="studentId" value="${s.id}"/>
              <button class="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700" name="action" value="deleteStudent">Delete</button>
            </form>
          </td>
        </tr>
      </c:forEach>
    </c:if>
    <c:if test="${empty studentList}">
      <tr>
        <td colspan="4" class="px-6 py-4 text-center text-gray-500">No students found.</td>
      </tr>
    </c:if>
    </tbody>
  </table>
</div>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>Student Form</title>
    <script>
        function validateForm(){
            const nameRegex = /^[A-Za-z]+([ '-][A-Za-z]+)*$/
            const selectedCourses = document.querySelectorAll('input[name="courses"]:checked');
            const firstName = document.getElementById("firstName").value.trim();
            const middleName = document.getElementById("middleName").value.trim();
            const lastName = document.getElementById("lastName").value.trim();
            const errorBox = document.getElementById("errors");
            errorBox.textContent = "";
            if(!nameRegex.test(firstName)){
                errorBox.textContent = "Invalid first name.";
                return false;
            }

            if(!nameRegex.test(middleName)){
                errorBox.textContent = "Invalid middle name.";
                return false;
            }

            if(!nameRegex.test(lastName)){
                errorBox.textContent = "Invalid last name.";
                return false;
            }
            if(selectedCourses.length === 0){
                errorBox.textContent = "Please select atleast any one course.";
                return false;
            }
            return true;
        }
        function cancelForm() {
            window.location.href = '/students';
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
</head>
<body class="bg-gray-100">

<jsp:include page="header.jsp"/>

<div class="max-w-3xl mx-auto mt-12 bg-white p-8 rounded-lg shadow-lg">
    <h2 class="text-2xl font-semibold mb-4">Add Student</h2>

    <form action="students" method="post" class="space-y-6" onsubmit="return validateForm()">
        <input type="hidden" name="action" value="addStudent"/>

        <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
            <div>
                <label for="firstName" class="block text-gray-700">First Name</label>
                <input type="text" name="firstName" id="firstName" class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
            </div>
            <div>
                <label for="middleName" class="block text-gray-700">Middle Name</label>
                <input type="text" name="middleName" id="middleName" class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
            </div>
            <div>
                <label for="lastName" class="block text-gray-700">Last Name</label>
                <input type="text" name="lastName" id="lastName" class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
            </div>
            <div>
                <label for="birthDate" class="block text-gray-700">Birth Date</label>
                <input type="date" name="birthDate" id="birthDate" class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
            </div>
        </div>

        <div>
            <label class="block text-gray-700">Courses</label>
            <div class="space-y-2">
                <c:if test="${not empty courseList}">
                    <c:forEach var="course" items="${courseList}">
                        <div class="flex items-center">
                            <input type="checkbox" name="courses" value="${course.id}" class="h-5 w-5 text-blue-600 border-gray-300 rounded focus:ring-blue-500">
                            <label class="ml-2 text-gray-700">${course.courseName}</label>
                        </div>
                    </c:forEach>
                </c:if>
                <c:if test="${empty courseList}">
                    <p class="text-gray-500">No courses available.</p>
                </c:if>
            </div>
        </div>
        <div id="errors" class="text-red-500">
        </div>
        <div class="flex items-center justify-end gap-4">
            <button type="submit" class="w-full px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 focus:ring-4 focus:ring-blue-300">Save</button>
            <button onclick="cancelForm()" type="button" class="w-full border border-gray-300 px-4 py-2 bg-white text-gray-600 rounded-lg hover:bg-gray-50 focus:ring-4 focus:ring-gray-100">Cancel</button>
        </div>
    </form>
</div>

</body>
</html>
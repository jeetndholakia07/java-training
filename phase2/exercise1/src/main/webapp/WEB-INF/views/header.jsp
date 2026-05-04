<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
document.addEventListener("DOMContentLoaded", function () {
setTimeout(() => {
const toast = document.getElementById("toast");
if (toast) toast.remove();
}, 3000);
});
</script>
<script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
</script>
<nav class="bg-gray-800 shadow-md">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <div class="flex items-center justify-between">
            <div class="text-white text-xl font-semibold">
                Student Management
            </div>
            <div class="hidden md:flex space-x-4">
                <a href="/students" class="text-white hover:text-gray-400">Home</a>
                <a href="/course" class="text-white hover:text-gray-400">Add Course</a>
            </div>
        </div>
    </div>
</nav>
<c:if test="${not empty flash}">
    <div id="toast"
         class="fixed top-10 right-5 text-white p-4 z-50 rounded shadow-lg
       ${flash.type eq 'success' ? 'bg-green-600' : 'bg-red-600'}">
            ${flash.message}
    </div>
</c:if>
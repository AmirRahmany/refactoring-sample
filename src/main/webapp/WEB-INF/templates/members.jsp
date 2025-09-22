<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html dir="rtl" lang="fa">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>لیست اعضا</title>
    <style>
        :root {
            --primary-color: #2e5cb8;
            --secondary-color: #4CAF50;
            --light-gray: #f5f5f5;
            --border-color: #ddd;
            --text-color: #333;
        }

        body {
            font-family: Tahoma, Arial, sans-serif;
            line-height: 1.6;
            color: var(--text-color);
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
        }

        #page {
            width: 100%;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        #content {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin-bottom: 30px;
        }

        .post {
            margin-bottom: 20px;
        }

        .title {
            font-size: 24px;
            color: var(--primary-color);
            text-align: center;
            margin-bottom: 25px;
            padding-bottom: 10px;
            border-bottom: 2px solid var(--primary-color);
        }

        .entry {
            padding: 10px;
        }

        /* Table Styles */
        .memberTable {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            font-size: 16px;
            text-align: right;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
        }

        .memberTable th {
            background-color: var(--primary-color);
            color: white;
            padding: 12px 15px;
            font-weight: bold;
        }

        .memberTable td {
            padding: 12px 15px;
            border-bottom: 1px solid var(--border-color);
        }

        .memberTable tr:nth-of-type(even) {
            background-color: var(--light-gray);
        }

        .memberTable tr:last-of-type {
            border-bottom: 2px solid var(--primary-color);
        }

        .memberTable tr:hover {
            background-color: #e9f0ff;
        }

        /* Pagination Styles */
        .pagination-container {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 30px;
            flex-wrap: wrap;
            gap: 15px;
        }

        .pagination-info {
            font-size: 16px;
            color: #666;
        }

        .page-selector {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .page-dropdown {
            padding: 8px 12px;
            border: 1px solid var(--border-color);
            border-radius: 4px;
            font-size: 16px;
        }

        .GreenButton {
            background-color: var(--secondary-color);
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
        }

        .GreenButton:hover {
            background-color: #3d8b40;
        }

        .profile-link {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: bold;
            transition: color 0.3s;
        }

        .profile-link:hover {
            color: #1e4080;
            text-decoration: underline;
        }

        .member-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            object-fit: cover;
            margin-left: 10px;
        }

        .member-info {
            display: flex;
            align-items: center;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .memberTable {
                font-size: 14px;
            }

            .memberTable th,
            .memberTable td {
                padding: 8px 10px;
            }

            .pagination-container {
                flex-direction: column;
            }

            .member-avatar {
                width: 30px;
                height: 30px;
            }
        }

        @media (max-width: 480px) {
            .memberTable {
                display: block;
                overflow-x: auto;
            }

            #content {
                padding: 15px;
            }

            .title {
                font-size: 20px;
            }
        }

        .empty-message {
            text-align: center;
            padding: 40px;
            color: #666;
            font-style: italic;
            font-size: 18px;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/templates/fragments/header.jsp" />
<div id="page">
    <div id="content">
        <div class="post">
            <h3 class="title">لیست اعضا</h3>
            <div class="entry">
                <c:if test="${not empty members}">
                    <table class="memberTable">
                        <thead>
                        <tr>
                            <th>عکس پروفایل</th>
                            <th>نام کاربری</th>
                            <th>نام</th>
                            <th>نام خانوادگی</th>
                            <th>تاریخ عضویت</th>
                            <th>عملیات</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="member" items="${members}">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty member.profilePicture}">
                                            <img src="${member.profilePicture}" class="member-avatar" alt="عکس پروفایل" />
                                        </c:when>
                                        <c:otherwise>
                                            <img src="/images/default-avatar.png" class="member-avatar" alt="عکس پیش‌فرض" />
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${member.username}</td>
                                <td>${member.firstname}</td>
                                <td>${member.lastname}</td>
                                <td><fmt:formatDate value="${member.registerDate}" pattern="yyyy/MM/dd" /></td>
                                <td>
                                    <a href="<c:url value='/profile?ID=${member.id}' />" class="profile-link">مشاهده پروفایل</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <c:if test="${empty members}">
                    <div class="empty-message">
                        هیچ عضوی یافت نشد.
                    </div>
                </c:if>

                <c:if test="${totalPages > 1}">
                    <div class="pagination-container">
                        <div class="pagination-info">
                            صفحه <span>${currentPage}</span> از <span>${totalPages}</span>
                        </div>

                        <div class="page-selector">
                            <label for="pageSelect">برو به صفحه:</label>
                            <select id="pageSelect" class="page-dropdown" onchange="goToPage(this.value)">
                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <option value="${i}" ${i == currentPage ? 'selected' : ''}>${i}</option>
                                </c:forEach>
                            </select>
                            <button class="GreenButton" onclick="goToSelectedPage()">برو</button>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<script>
    function goToPage(pageNumber) {
        window.location.href = '/members?page=' + pageNumber;
    }

    function goToSelectedPage() {
        const selectElement = document.getElementById('pageSelect');
        const pageNumber = selectElement.value;
        goToPage(pageNumber);
    }

    // Keyboard navigation for pagination
    document.addEventListener('keydown', function(event) {
        const currentPage = ${currentPage};
        const totalPages = ${totalPages};

        if (event.key === 'ArrowRight' && currentPage < totalPages) {
            goToPage(currentPage + 1);
        } else if (event.key === 'ArrowLeft' && currentPage > 1) {
            goToPage(currentPage - 1);
        }
    });
</script>
</body>
</html>
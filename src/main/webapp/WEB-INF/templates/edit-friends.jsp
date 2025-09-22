<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>مدیریت دوستان</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <style>
        .error-big {
            color: #d9534f;
            font-weight: bold;
            display: block;
            padding: 10px;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 4px;
            margin: 10px 0;
        }

        .title {
            color: #333;
            border-bottom: 2px solid #007acc;
            padding-bottom: 5px;
            margin-top: 30px;
            font-weight: bold;
        }

        .member-table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            border: 1px solid #dee2e6;
        }

        .member-table th,
        .member-table td {
            padding: 12px;
            text-align: center;
            border: 1px solid #dee2e6;
            vertical-align: middle;
        }

        .member-table th {
            background-color: #f8f9fa;
            font-weight: bold;
            color: #495057;
        }

        .member-table img {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            object-fit: cover;
        }

        .btn-action {
            padding: 5px 10px;
            font-size: 0.9em;
            margin: 2px;
        }

        /* RTL specific styles */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        .text-center {
            text-align: center;
        }
    </style>
</head>
<body>
<!-- Header inclusion -->
<jsp:include page="/WEB-INF/templates/fragments/header.jsp" />

<div class="container">
    <div id="page">
        <div id="content">
            <div class="post">
                <h3 class="title" id="headFriends">لیست دوستان</h3>

                <div class="entry">
                    <!-- Error message display -->
                    <c:if test="${not empty errorMessage}">
                        <div class="error-big"><c:out value="${errorMessage}" /></div>
                    </c:if>

                    <!-- Friends List Table -->
                    <c:if test="${not empty friends}">
                        <table class="member-table">
                            <thead>
                            <tr>
                                <th>تصویر پروفایل</th>
                                <th>نام کاربری</th>
                                <th>نام و نام خانوادگی</th>
                                <th>مشاهده پروفایل</th>
                                <th>حذف دوستی</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="friend" items="${friends}">
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty friend.profilePicture}">
                                                <img src="/images/profiles/${friend.profilePicture}" alt="پروفایل" />
                                            </c:when>
                                            <c:otherwise>
                                                <img src="/images/default-profile.png" alt="پروفایل" />
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${friend.username}" /></td>
                                    <td><c:out value="${friend.firstname} ${friend.lastname}" /></td>
                                    <td>
                                        <a href="/profile/${friend.username}"
                                           class="btn btn-primary btn-action">مشاهده</a>
                                    </td>
                                    <td>
                                        <form action="/friends/remove/${friend.id}" method="post">
                                            <button type="submit" class="btn btn-danger btn-action"
                                                    onclick="return confirm('آیا از حذف این دوست اطمینان دارید؟')">
                                                حذف
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>

                    <c:if test="${empty friends}">
                        <div class="alert alert-info text-center">
                            هیچ دوستی ندارید.
                        </div>
                    </c:if>

                    <br /><br /><br />

                    <!-- Pending Friend Requests -->
                    <h3 class="title" id="headWaiting">درخواست های دوستی تایید نشده</h3>

                    <c:if test="${not empty pendingRequests}">
                        <table class="member-table">
                            <thead>
                            <tr>
                                <th>تصویر پروفایل</th>
                                <th>نام کاربری</th>
                                <th>نام و نام خانوادگی</th>
                                <th>مشاهده پروفایل</th>
                                <th>تایید دوستی</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="request" items="${pendingRequests}">
                                <tr>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty request.sender.profilePicture}">
                                                <img src="/images/profiles/${request.sender.profilePicture}" alt="پروفایل" />
                                            </c:when>
                                            <c:otherwise>
                                                <img src="/images/default-profile.png" alt="پروفایل" />
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${request.sender.username}" /></td>
                                    <td><c:out value="${request.sender.firstname} ${request.sender.lastname}" /></td>
                                    <td>
                                        <a href="/profile/${request.sender.username}"
                                           class="btn btn-primary btn-action">مشاهده</a>
                                    </td>
                                    <td>
                                        <form action="/friends/accept/${request.id}" method="post" style="display: inline;">
                                            <button type="submit" class="btn btn-success btn-action">تایید</button>
                                        </form>
                                        <form action="/friends/reject/${request.id}" method="post" style="display: inline;">
                                            <button type="submit" class="btn btn-danger btn-action">رد</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>

                    <c:if test="${empty pendingRequests}">
                        <div class="alert alert-info text-center">
                            هیچ درخواست دوستی pending ندارید.
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer inclusion -->
<jsp:include page="/WEB-INF/templates/fragments/footer.jsp" />

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
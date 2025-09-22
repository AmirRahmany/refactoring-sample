<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - ${username}</title>

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
            margin: 15px 0;
        }

        .member-table td {
            text-align: center;
            padding: 20px;
            border: 1px solid #ddd;
            vertical-align: top;
        }

        .member-table img {
            margin-bottom: 10px;
            max-width: 64px;
            height: auto;
        }

        .member-table a {
            text-decoration: none;
            color: #007acc;
            font-weight: 500;
        }

        .member-table a:hover {
            color: #0056b3;
            text-decoration: underline;
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
    </style>
</head>
<body>
<!-- Header inclusion -->
<jsp:include page="/WEB-INF/templates/fragments/header.jsp" />

<div class="container">
    <div id="page">
        <div id="content">
            <div class="post">
                <div class="entry">

                    <!-- Error message display -->
                    <c:if test="${not empty errorMessage}">
                        <div class="error-big"><c:out value="${errorMessage}" /></div>
                    </c:if>

                    <!-- Profile Panel -->
                    <div id="panelProfile">
                        <h3 class="title">ویرایش</h3>
                        <table class="member-table">
                            <tr>
                                <td>
                                    <img src="/images/edit-user.png" alt="ویرایش پروفایل کاربری" />
                                    <br/>
                                    <a href="/edit-profile">ویرایش مشخصات کاربری</a>
                                </td>

                                <td>
                                    <img src="/images/edit-friends.png" alt="مدیریت دوستان" />
                                    <br/>
                                    <a href="/edit-friends">ویرایش لیست دوستان</a>
                                </td>
                            </tr>
                        </table>

                        <h3 class="title">پیام خصوصی</h3>
                        <table class="member-table">
                            <tr>
                                <td>
                                    <img src="/images/pm-inbox.png" alt="صندوق ورودی" />
                                    <br/>
                                    <a href="/inbox">پیام های دریافتی</a>
                                </td>

                                <td>
                                    <img src="/images/pm-outbox.png" alt="صندوق خروجی" />
                                    <br/>
                                    <a href="/outbox">پیام های ارسالی</a>
                                </td>

                                <td>
                                    <img src="/images/pm-compose.png" alt="ارسال پیام جدید" />
                                    <br/>
                                    <a href="/compose">ارسال پیام جدید</a>
                                </td>
                            </tr>
                        </table>
                    </div>
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
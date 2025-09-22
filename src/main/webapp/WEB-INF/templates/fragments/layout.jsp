<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle}" default="پنل کاربری" /></title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <style>
        :root {
            --primary-color: #2e5cb8;
            --secondary-color: #4CAF50;
            --accent-color: #ff9800;
            --light-gray: #f5f5f5;
            --border-color: #ddd;
            --text-color: #333;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            color: var(--text-color);
            line-height: 1.6;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        .navbar {
            background-color: var(--primary-color);
            padding: 15px 0;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .navbar-brand, .nav-link {
            color: white !important;
            font-weight: 500;
        }

        .nav-link:hover {
            color: var(--accent-color) !important;
        }

        .footer {
            background-color: var(--primary-color);
            color: white;
            text-align: center;
            padding: 20px 0;
            margin-top: 30px;
            position: relative;
            bottom: 0;
            width: 100%;
        }

        .footer a {
            color: var(--accent-color);
            text-decoration: none;
        }

        .footer a:hover {
            text-decoration: underline;
        }

        .content-wrapper {
            min-height: calc(100vh - 200px);
        }

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
            font-size: 24px;
            color: var(--primary-color);
            text-align: center;
            margin-bottom: 25px;
            padding-bottom: 10px;
            border-bottom: 2px solid var(--primary-color);
        }
    </style>
</head>
<body>
    <!-- Header -->
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">پنل کاربری</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/schools">لیست مدارس</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/sharing">اشتراک‌گذاری</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/inbox">پیام‌ها</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin">پنل مدیریت</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/sendInvite">ارسال دعوتنامه</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">خروج</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Content Placeholder -->
    <div class="content-wrapper">
        <div class="container">
            <jsp:include page="${contentPage}" />
        </div>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="container">
            <p>&copy; 1404 پنل کاربری. تمامی حقوق محفوظ است.</p>
            <p><a href="${pageContext.request.contextPath}/support">پشتیبانی</a> | <a href="${pageContext.request.contextPath}/about">درباره ما</a></p>
        </div>
    </footer>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
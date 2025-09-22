<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>صندوق پیشنهادات</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Vazir Font -->
    <link href="https://cdn.jsdelivr.net/npm/vazir-font@32.102.0/dist/font-face.css" rel="stylesheet">
    <!-- Custom CSS -->
    <style>
        :root {
            --primary-color: #2e5cb8;
            --secondary-color: #4CAF50;
            --text-color: #333;
            --gradient-start: #2e5cb8;
            --gradient-end: #1e3c72;
        }

        body {
            font-family: 'Vazir', Tahoma, Arial, sans-serif;
            background-color: #f8f9fa;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        #page {
            background-color: #ffffff;
            border: 1px solid #dee2e6;
            border-radius: 4px;
            padding: 20px;
            margin-bottom: 20px;
        }

        .error-big {
            color: #d9534f;
            font-size: 1.2em;
            display: block;
            margin-bottom: 20px;
            text-align: right;
        }

        .form-table {
            width: 100%;
            margin-bottom: 20px;
        }

        .form-table td {
            padding: 15px;
            vertical-align: middle;
        }

        .form-table label {
            color: #495057;
            font-weight: bold;
        }

        .form-table .form-control {
            width: 100%;
        }

        .form-table .error {
            color: #d9534f;
            font-size: 0.9em;
            margin-right: 5px;
        }

        .green-button {
            background-color: var(--secondary-color);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        .green-button:hover {
            background-color: #3d8b40;
        }

        .text-right {
            text-align: right;
        }

        @media (max-width: 768px) {
            .form-table td {
                display: block;
                width: 100%;
                padding: 10px;
            }
            .form-table .green-button,
            .form-table .form-control {
                width: 100%;
                margin-bottom: 5px;
            }
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/templates/fragments/header.jsp" />
<div class="container">
    <div id="page">
        <div id="content">
            <div class="post">
                <h3 class="title">صندوق پیشنهادات</h3>
                <div class="entry">
                    <c:if test="${not empty labelError}">
                        <span class="error-big">${labelError}</span>
                    </c:if>
                    <c:if test="${not empty successMessage}">
                        <span class="text-success" style="font-size: 1.2em; display: block; margin-bottom: 20px; text-align: right;">
                            ${successMessage}
                        </span>
                    </c:if>

                    <c:if test="${isAuthenticated}">
                        <form action="${pageContext.request.contextPath}/contactUs" method="post" id="contactForm">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <table class="form-table">
                                <tr>
                                    <td><label for="textSubject">موضوع :</label></td>
                                    <td>
                                        <input type="text" name="textSubject" id="textSubject" class="form-control" value="${textSubject}" required />
                                        <c:if test="${not empty subjectError}">
                                            <span class="error">${subjectError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td><label for="textMessage">متن :</label></td>
                                    <td>
                                        <textarea name="textMessage" id="textMessage" class="form-control" rows="5" required>${textMessage}</textarea>
                                        <c:if test="${not empty messageError}">
                                            <span class="error">${messageError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="text-right">
                                        <button type="submit" name="buttonSend" class="green-button">ارسال</button>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/templates/fragments/footer.jsp" />
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    (function () {
        'use strict';
        const form = document.getElementById('contactForm');
        if (form) {
            form.addEventListener('submit', function (event) {
                const subject = document.getElementById('textSubject').value;
                const message = document.getElementById('textMessage').value;
                if (!subject.trim()) {
                    event.preventDefault();
                    alert('لطفاً موضوع را وارد کنید');
                }
                if (!message.trim()) {
                    event.preventDefault();
                    alert('لطفاً متن پیام را وارد کنید');
                }
                form.classList.add('was-validated');
            }, false);
        }
    })();
</script>
</body>
</html>
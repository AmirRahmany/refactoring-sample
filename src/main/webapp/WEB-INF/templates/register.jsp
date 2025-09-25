<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle}</title>

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

        .error-small {
            color: #d9534f;
            font-size: 0.9em;
            margin-top: 5px;
            display: block;
        }

        .title {
            color: #333;
            border-bottom: 2px solid #007acc;
            padding-bottom: 5px;
            margin-top: 30px;
            font-weight: bold;
        }

        .register-table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }

        .register-table td {
            padding: 12px;
            vertical-align: top;
        }

        .register-label {
            font-weight: bold;
            color: #495057;
            text-align: left;
            width: 150px;
            padding-right: 20px;
        }

        .form-control {
            width: 250px;
        }

        .english-input {
            direction: ltr;
            text-align: left;
        }

        .green-button {
            background-color: #28a745;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        .green-button:hover {
            background-color: #218838;
        }

        /* RTL specific styles */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }

        .form-check {
            text-align: right;
            margin: 15px 0;
        }

        .text-center {
            text-align: center;
        }
    </style>
</head>
<body>
<!-- Header inclusion -->
<jsp:include page="fragments/header.jsp" />

<div class="container">
    <div id="page">
        <div id="content">
            <div class="post">
                <h3 class="title">ثبت نام</h3>

                <div class="entry">
                    <!-- Success message -->
                    <c:if test="${param.action == 'successfull'}">
                        <div class="alert alert-success">${successMessage}</div>
                    </c:if>

                    <!-- Error message -->
                    <c:if test="${not empty labelError}">
                        <div class="error-big">${labelError}</div>
                    </c:if>

                    <form action="/register" method="post" enctype="multipart/form-data">
                        <table class="register-table">
                            <tr>
                                <td class="register-label">نام کاربری :</td>
                                <td>
                                    <input type="text" name="textUsername" class="form-control" maxlength="50" value="${form.textUsername}"/>
                                    <c:if test="${not empty errors.username}">
                                        <span class="error-small">${errors.username}</span>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td class="register-label">رمز عبور :</td>
                                <td>
                                    <input type="password" name="textPassword" class="form-control" maxlength="30" />
                                    <c:if test="${not empty errors.password}">
                                        <span class="error-small">${errors.password}</span>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td class="register-label">نام :</td>
                                <td>
                                    <input type="text" name="textFirstname" class="form-control" maxlength="25" value="${form.textFirstname}"/>
                                    <c:if test="${not empty errors.firstname}">
                                        <span class="error-small">${errors.firstname}</span>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td class="register-label">نام خانوادگی :</td>
                                <td>
                                    <input type="text" name="textLastname" class="form-control" maxlength="25" value="${form.textLastname}"/>
                                    <c:if test="${not empty errors.lastname}">
                                        <span class="error-small">${errors.lastname}</span>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td class="register-label">ایمیل :</td>
                                <td>
                                    <input type="email" name="textEmail" class="form-control english-input" maxlength="50" value="${form.textEmail}"/>
                                    <c:if test="${not empty errors.email}">
                                        <span class="error-small">${errors.email}</span>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td class="register-label">وب سایت :</td>
                                <td>
                                    <input type="url" name="textWebsite" class="form-control english-input" maxlength="50" value="${form.textWebsite}"/>
                                    <c:if test="${not empty errors.website}">
                                        <span class="error-small">${errors.website}</span>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td class="register-label">عکس پروفایل :</td>
                                <td>
                                    <input type="file" name="profilePicture" class="form-control" accept="image/*" />
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <div class="form-check">
                                        <input type="checkbox" name="receivePrivateMessages" class="form-check-input" id="pmCheckbox" <c:if test="${form.receivePrivateMessages}">checked</c:if> />
                                        <label class="form-check-label" for="pmCheckbox">
                                            مایل به دریافت پیام خصوصی از بقیه اعضای سایت هستم
                                        </label>
                                    </div>
                                </td>
                            </tr>
                        </table>

                        <br />
                        <div class="text-center">
                            <button type="submit" class="green-button">ثبت نام</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer inclusion -->

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
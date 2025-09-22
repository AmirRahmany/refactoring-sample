<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
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

        #header {
            background: linear-gradient(90deg, var(--gradient-start), var(--gradient-end));
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
            padding: 20px 0;
            margin-bottom: 20px;
            display: flex;
            direction:ltr;

            justify-content: flex-end;
        }

        #logo {
            display: flex;
            align-items: center;
        }

        #logo h1 {
            color: white;
            font-weight: bold;
            margin: 0;
            padding: 10px 20px;
            font-size: 1.5rem;
        }

        #logo img {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            object-fit: cover;
            margin-left: 10px;
        }

        #menu {
            background-color: rgba(255, 255, 255, 0.1);
            border-radius: 4px;
            padding: 10px;
        }

        #menu ul {
            list-style: none;
            padding: 0;
            margin: 0;
            display: flex;
            justify-content: end;
            gap: 15px;
        }

        #menu ul li a {
            color: white;
            text-decoration: none;
            font-size: 1.1rem;
            padding: 8px 12px;
            border-radius: 5px;
            transition: background-color 0.3s ease, color 0.3s ease;
        }

        #menu ul li a:hover {
            background-color: var(--secondary-color);
            color: #e9f0ff;
        }

        #sidebar {
            margin-bottom: 20px;
        }

        #sidebar .sideBox {
            background-color: #ffffff;
            border: 1px solid #dee2e6;
            border-radius: 4px;
            padding: 15px;
            margin-bottom: 20px;
        }

        #sidebar h3 {
            color: #333;
            font-weight: bold;
            margin-bottom: 15px;
            border-bottom: 2px solid var(--primary-color);
            padding-bottom: 5px;
        }

        #sidebar label {
            color: #495057;
            font-weight: bold;
        }

        #sidebar .form-control {
            width: 100%;
            margin-bottom: 10px;
        }

        #sidebar .error {
            color: #d9534f;
            font-size: 0.9em;
            display: block;
            margin-bottom: 10px;
        }

        #sidebar .green-button {
            background-color: var(--secondary-color);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        #sidebar .green-button:hover {
            background-color: #3d8b40;
        }

        #sidebar table {
            width: 100%;
        }

        #sidebar table td {
            padding: 8px;
        }

        #sidebar table a {
            color: var(--secondary-color);
            text-decoration: none;
        }

        #sidebar table a:hover {
            text-decoration: underline;
        }

        #sidebar .row {
            margin-left: 0;
            margin-right: 0;
        }

        #sidebar .col-md-6 {
            padding: 0 10px;
        }

        .text-right {
            text-align: right;
        }

        .required::after {
            content: "*";
            color: red;
            margin-right: 5px;
        }

        @media (max-width: 768px) {
            #menu ul {
                flex-direction: column;
                text-align: right;
                padding: 10px;
                gap: 10px;
            }
            #logo h1 {
                font-size: 1.2rem;
            }
            #logo img {
                width: 35px;
                height: 35px;
            }
            #sidebar .col-md-6 {
                margin-bottom: 15px;
            }
        }

        @media (max-width: 480px) {
            #logo h1 {
                font-size: 1.1rem;
            }
            #logo img {
                width: 30px;
                height: 30px;
            }
            #menu ul li a {
                font-size: 0.9rem;
            }
        }
    </style>
</head>
<body>
<div id="wrapper">
    <div id="wrapper2">
      <jsp:include page="/WEB-INF/templates/fragments/header.jsp" />
        <!-- end #header -->
        <div class="container">
        <div id="sidebar">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="sideBox">
                                <h3>آمار سایت</h3>
                                <label>تعداد اعضا : ${memberCount}</label><br />
                                <label>تعداد استان ها : ${provinceCount}</label><br />
                                <label>تعداد شهر ها : ${cityCount}</label><br />
                                <label>تعداد مدارس : ${schoolCount}</label>
                            </div>
                            <div class="sideBox">
                                <h3>امکانات متفرقه</h3>
                                <table dir="rtl">
                                    <tr>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/send-invite">ارسال دعوتنامه</a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/contactUs">صندوق پیشنهادات</a>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="sideBox">
                                <c:choose>
                                    <c:when test="${not isAuthenticated}">
                                        <h3>ورود</h3>
                                        <form action="${pageContext.request.contextPath}/login" method="post" id="loginForm">
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                            <label class="required">نام کاربری :</label><br />
                                            <input type="text" name="TextUsername" id="TextUsername" class="form-control" required />
                                            <c:if test="${not empty usernameError}">
                                                <span class="error">${usernameError}</span>
                                            </c:if>
                                            <label class="required">رمز عبور :</label><br />
                                            <input type="password" name="TextPassword" id="TextPassword" class="form-control" required />
                                            <c:if test="${not empty passwordError}">
                                                <span class="error">${passwordError}</span>
                                            </c:if>
                                            <input type="checkbox" name="CheckRemember" id="CheckRemember" />
                                            <label for="CheckRemember">مرا به خاطر بسپار</label><br />
                                            <c:if test="${not empty loginError}">
                                                <span class="error">${loginError}</span>
                                            </c:if>
                                            <button type="submit" class="green-button" name="buttonLogin">ورود</button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <h3>کاربر</h3>
                                        <p class="text-right">خوش آمدید، ${userName}</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>

        </div>
          <!-- end #sidebar -->

        <div style="clear: both;">&nbsp;</div>
        <div id="widebar">
            <div id="colA"></div>
            <div id="colB"></div>
            <div id="colC"></div>
            <div style="clear: both;">&nbsp;</div>
        </div>
        <!-- end #widebar -->
    </div>
    <!-- end #wrapper2 -->
</div>
<!-- end #wrapper -->
<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    (function () {
        'use strict';
        const form = document.getElementById('loginForm');
        if (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        }
    })();
</script>
</body>
</html>
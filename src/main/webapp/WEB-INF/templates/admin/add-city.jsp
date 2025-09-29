<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>اضافه کردن شهر</title>

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

        .form-table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }

        .form-table td {
            padding: 12px;
            vertical-align: top;
        }

        .form-label {
            font-weight: bold;
            color: #495057;
            text-align: right;
            width: 120px;
            padding-left: 20px;
        }

        .form-control {
            width: 250px;
        }

        .green-button {
            background-color: #28a745;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            margin-top: 15px;
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
                <div class="entry">

                    <!-- Error message display -->
                    <c:if test="${not empty errorMessage}">
                        <div class="error-big">${errorMessage}</div>
                    </c:if>

                    <!-- Success message -->
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success">${successMessage}</div>
                    </c:if>

                    <!-- Add City Form -->
                    <c:if test="${panelVisible}">
                    <div id="panelAddCity">
                        <form action="${pageContext.request.contextPath}/admin/cities/add" method="post">
                            <table class="form-table">
                                <tr>
                                    <td class="form-label">نام شهر :</td>
                                    <td>
                                        <input type="text" name="cityName" value="${cityForm.cityName}" class="form-control"
                                               required maxlength="100" />
                                        <c:if test="${not empty cityNameError}">
                                            <span class="error-small">${cityNameError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="form-label">نام استان :</td>
                                    <td>
                                        <select name="provinceId" class="form-control" required>
                                            <option value="">-- انتخاب استان --</option>
                                            <c:forEach var="province" items="${provinces}">
                                                <option value="${province.id}" ${province.id == cityForm.provinceId ? 'selected' : ''}>${province.name}</option>
                                            </c:forEach>
                                        </select>
                                        <c:if test="${not empty provinceIdError}">
                                            <span class="error-small">${provinceIdError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="text-center">
                                        <button type="submit" class="green-button">ثبت شهر</button>
                                    </td>
                                </tr>
                            </table>
                        </form>
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
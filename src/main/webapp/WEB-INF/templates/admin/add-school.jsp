<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>اضافه کردن مدرسه</title>

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
            width: 150px;
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
        }

        .green-button:hover {
            background-color: #218838;
        }

        .title {
            color: #333;
            border-bottom: 2px solid #007acc;
            padding-bottom: 5px;
            margin-top: 30px;
            font-weight: bold;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
        }

        .container {
            max-width: 900px;
            margin: 0 auto;
            padding: 20px;
        }

        .text-center {
            text-align: center;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/templates/fragments/header.jsp" />

<div class="container">
    <div id="page">
        <div id="content">
            <div class="post">
                <h3 class="title">اضافه کردن مدرسه</h3>

                <div class="entry">
                    <c:if test="${not empty errorMessage}">
                        <div class="error-big">${errorMessage}</div>
                    </c:if>

                    <c:choose>
                        <c:when test="${panelVisible}">
                            <form action="${pageContext.request.contextPath}/admin/AddSchool" method="post">
                                <table class="form-table">
                                    <tr>
                                        <td class="form-label">نام آموزشگاه :</td>
                                        <td>
                                            <input type="text" name="textSchoolName" id="textSchoolName"
                                                   class="form-control" required maxlength="100" value="${formData.textSchoolName}" />
                                            <span style="color: red">*</span>
                                            <c:if test="${not empty textSchoolNameError}">
                                                <span class="error-small">${textSchoolNameError}</span>
                                            </c:if>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="form-label">نوع آموزشگاه :</td>
                                        <td>
                                            <select name="listSchoolType" id="listSchoolType" class="form-control" required>
                                                <option value="">-- انتخاب نوع --</option>
                                                <c:forEach var="type" items="${schoolTypes}">
                                                    <option value="${type.id}" ${type.id == formData.listSchoolType ? 'selected' : ''}>${type.typeName}</option>
                                                </c:forEach>
                                            </select>
                                            <c:if test="${not empty listSchoolTypeError}">
                                                <span class="error-small">${listSchoolTypeError}</span>
                                            </c:if>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="form-label">نام استان :</td>
                                        <td>
                                            <select name="listProvince" id="listProvince" class="form-control" required
                                                    onchange="updateCities()">
                                                <option value="">-- انتخاب استان --</option>
                                                <c:forEach var="province" items="${provinces}">
                                                    <option value="${province.id}" ${province.id == formData.listProvince ? 'selected' : ''}>${province.name}</option>
                                                </c:forEach>
                                            </select>
                                            <c:if test="${not empty listProvinceError}">
                                                <span class="error-small">${listProvinceError}</span>
                                            </c:if>
                                        </td>
                                        <td>
                                            <button type="button" class="green-button" onclick="updateCities()">
                                                نمایش شهرها
                                            </button>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="form-label">نام شهر :</td>
                                        <td>
                                            <select name="listCity" id="listCity" class="form-control" required>
                                                <option value="">-- انتخاب شهر --</option>
                                                <c:forEach var="city" items="${cities}">
                                                    <option value="${city.id}" ${city.id == formData.listCity ? 'selected' : ''}>${city.name}</option>
                                                </c:forEach>
                                            </select>
                                            <c:if test="${not empty listCityError}">
                                                <span class="error-small">${listCityError}</span>
                                            </c:if>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td colspan="2" class="text-center">
                                            <button type="submit" class="green-button">ثبت آموزشگاه</button>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-danger">
                                شما مجوز دسترسی به این صفحه را ندارید
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function updateCities() {
        var provinceSelect = document.getElementById('listProvince');
        var provinceId = provinceSelect.value;

        if (provinceId) {
            var form = document.createElement('form');
            form.method = 'post';
            form.action = '${pageContext.request.contextPath}/admin/AddSchool';

            var input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'action';
            input.value = 'showCities';
            form.appendChild(input);

            var provinceInput = document.createElement('input');
            provinceInput.type = 'hidden';
            provinceInput.name = 'provinceId';
            provinceInput.value = provinceId;
            form.appendChild(provinceInput);

            document.body.appendChild(form);
            form.submit();
        }
    }
</script>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
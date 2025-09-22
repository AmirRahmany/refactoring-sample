<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>گزارش تعداد پست‌های آموزشگاه</title>
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

        .form-table .date-input {
            width: 60px;
            display: inline-block;
            margin-left: 5px;
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

        .table-result {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .table-result th, .table-result td {
            border: 1px solid #dee2e6;
            padding: 10px;
            text-align: right;
        }

        .table-result .header-row {
            background-color: var(--primary-color);
            color: white;
        }

        .table-result .odd-row {
            background-color: #f8f9fa;
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
            }
            .form-table .date-input {
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
                <div class="entry">
                    <c:if test="${not empty labelError}">
                        <span class="error-big">${labelError}</span>
                    </c:if>
                    
                    <c:if test="${isAuthenticated}">
                        <form action="${pageContext.request.contextPath}/reportSchoolPostCount" method="post" id="reportForm">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <table class="form-table">
                                <tr>
                                    <td>استان :</td>
                                    <td>
                                        <select name="listProvinces" id="listProvinces" class="form-control">
                                            <c:forEach var="province" items="${provinces}">
                                                <option value="${province.id}" ${province.id == selectedProvinceId ? 'selected' : ''}>${province.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <button type="submit" name="buttonFillCities" class="green-button">نمایش شهرها</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>شهر :</td>
                                    <td>
                                        <select name="listCities" id="listCities" class="form-control">
                                            <c:forEach var="city" items="${cities}">
                                                <option value="${city.id}" ${city.id == selectedCityId ? 'selected' : ''}>${city.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <button type="submit" name="buttonFillSchools" class="green-button">نمایش مدارس</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td>آموزشگاه :</td>
                                    <td colspan="2">
                                        <select name="listSchools" id="listSchools" class="form-control">
                                            <c:forEach var="school" items="${schools}">
                                                <option value="${school.id}" ${school.id == selectedSchoolId ? 'selected' : ''}>${school.name}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="checkStartDate" id="checkStartDate" ${checkStartDate ? 'checked' : ''} />
                                        <label for="checkStartDate">تاریخ شروع :</label>
                                    </td>
                                    <td>
                                        <input type="text" name="textStartDay" id="textStartDay" class="form-control date-input" maxlength="2" value="${textStartDay}" placeholder="روز" />
                                        <span>/</span>
                                        <input type="text" name="textStartMonth" id="textStartMonth" class="form-control date-input" maxlength="2" value="${textStartMonth}" placeholder="ماه" />
                                        <span>/</span>
                                        <input type="text" name="textStartYear" id="textStartYear" class="form-control date-input" maxlength="4" value="${textStartYear}" placeholder="سال" />
                                        <c:if test="${not empty startDateError}">
                                            <span class="error">${startDateError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" name="checkEndDate" id="checkEndDate" ${checkEndDate ? 'checked' : ''} />
                                        <label for="checkEndDate">تاریخ پایان :</label>
                                    </td>
                                    <td>
                                        <input type="text" name="textEndDay" id="textEndDay" class="form-control date-input" maxlength="2" value="${textEndDay}" placeholder="روز" />
                                        <span>/</span>
                                        <input type="text" name="textEndMonth" id="textEndMonth" class="form-control date-input" maxlength="2" value="${textEndMonth}" placeholder="ماه" />
                                        <span>/</span>
                                        <input type="text" name="textEndYear" id="textEndYear" class="form-control date-input" maxlength="4" value="${textEndYear}" placeholder="سال" />
                                        <c:if test="${not empty endDateError}">
                                            <span class="error">${endDateError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="text-right">
                                        <button type="submit" name="buttonShow" class="green-button">نمایش</button>
                                    </td>
                                </tr>
                            </table>
                        </form>

                        <c:if test="${showResultTable}">
                            <table class="table-result">
                                <tr class="header-row">
                                    <th>نام شهر</th>
                                    <th>نوع آموزشگاه</th>
                                    <th>نام آموزشگاه</th>
                                    <th>تعداد پست‌ها</th>
                                </tr>
                                <tr class="odd-row">
                                    <td>${cityName}</td>
                                    <td>${schoolType}</td>
                                    <td>${schoolName}</td>
                                    <td>${postCount}</td>
                                </tr>
                            </table>
                        </c:if>
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
        const form = document.getElementById('reportForm');
        if (form) {
            form.addEventListener('submit', function (event) {
                const startDateChecked = document.getElementById('checkStartDate').checked;
                const endDateChecked = document.getElementById('checkEndDate').checked;
                const startDay = document.getElementById('textStartDay').value;
                const startMonth = document.getElementById('textStartMonth').value;
                const startYear = document.getElementById('textStartYear').value;
                const endDay = document.getElementById('textEndDay').value;
                const endMonth = document.getElementById('textEndMonth').value;
                const endYear = document.getElementById('textEndYear').value;

                if (startDateChecked && (!startDay || !startMonth || !startYear)) {
                    event.preventDefault();
                    event.stopPropagation();
                    alert('لطفاً تمام فیلدهای تاریخ شروع را پر کنید');
                }
                if (endDateChecked && (!endDay || !endMonth || !endYear)) {
                    event.preventDefault();
                    event.stopPropagation();
                    alert('لطفاً تمام فیلدهای تاریخ پایان را پر کنید');
                }
                form.classList.add('was-validated');
            }, false);
        }
    })();
</script>
</body>
</html>
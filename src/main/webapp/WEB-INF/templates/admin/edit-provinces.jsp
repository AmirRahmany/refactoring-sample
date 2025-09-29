<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ویرایش استان‌ها</title>

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

        .table-result {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }

        .table-result th, .table-result td {
            padding: 12px;
            border: 1px solid #dee2e6;
            text-align: center;
            vertical-align: middle;
        }

        .header-row {
            background-color: #f8f9fa;
            font-weight: bold;
            color: #495057;
        }

        .odd-row {
            background-color: #f8f9fa;
        }

        .even-row {
            background-color: #ffffff;
        }

        .green-button {
            background-color: #28a745;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            margin: 5px;
        }

        .green-button:hover {
            background-color: #218838;
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

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
        }

        .container {
            max-width: 1000px;
            margin: 0 auto;
            padding: 20px;
        }

        .text-center {
            text-align: center;
        }

        .mb-3 {
            margin-bottom: 1rem;
        }

        .required::after {
            content: "*";
            color: red;
            margin-right: 5px;
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
                    <c:if test="${not empty errorMessage}">
                        <div class="error-big">${errorMessage}</div>
                    </c:if>

                    <c:if test="${showTable != false}">
                        <table class="table-result">
                            <tr class="header-row">
                                <th>کد</th>
                                <th>نام استان</th>
                                <th>ویرایش</th>
                                <th>حذف</th>
                            </tr>
                            <c:forEach var="province" items="${provinceList}" varStatus="status">
                                <tr class="${status.count % 2 == 1 ? 'odd-row' : 'even-row'}">
                                    <td>${province.id}</td>
                                    <td>${province.name}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/edit-provinces?ID=${province.id}" class="green-button">ویرایش</a>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/DeleteProvinces" method="post" style="display: inline;">
                                            <input type="hidden" name="ID" value="${province.id}" />
                                            <button type="submit" class="green-button" style="background-color: #dc3545;" onclick="return confirmDelete()">
                                                حذف
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>

                    <c:if test="${showEditPanel != false}">
                        <h4>ویرایش استان</h4>
                        <form action="${pageContext.request.contextPath}/admin/EditProvinces" method="post" id="editForm">
                            <input type="hidden" name="provinceId" value="${provinceId}" />
                            <table class="form-table">
                                <tr>
                                    <td class="form-label">کد استان:</td>
                                    <td>${provinceId}</td>
                                </tr>
                                <tr>
                                    <td class="form-label required">نام استان:</td>
                                    <td>
                                        <input type="text" name="provinceName" value="${provinceName}" class="form-control" required />
                                        <c:if test="${not empty provinceNameError}">
                                            <span style="color: red" class="invalid-feedback">${provinceNameError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="text-center">
                                        <button type="submit" class="green-button">ذخیره تغییرات</button>
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
    function confirmDelete() {
        return confirm('آیا از حذف این استان اطمینان دارید؟');
    }

    (function () {
        'use strict';
        const form = document.getElementById('editForm');
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    })();
</script>
</body>
</html>
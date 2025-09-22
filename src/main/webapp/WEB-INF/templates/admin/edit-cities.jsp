<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ویرایش شهرها</title>

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

        .dropdown-container {
            margin: 15px 0;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 5px;
            border: 1px solid #dee2e6;
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

                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success">${successMessage}</div>
                    </c:if>

                    <c:if test="${panelChooseProvinceVisible}">
                        <div class="dropdown-container">
                            <h4>انتخاب استان</h4>
                            <form action="${pageContext.request.contextPath}/admin/edit-city" method="post">
                                <input type="hidden" name="action" value="showCities" />
                                <div class="mb-3">
                                    <label class="form-label">انتخاب استان :</label>
                                    <select name="provinceId" class="form-control" style="width: 200px; display: inline-block;">
                                        <option value="">-- انتخاب استان --</option>
                                        <c:forEach var="province" items="${provinces}">
                                            <option value="${province.id}" ${selectedProvinceId == province.id ? 'selected' : ''}>${province.name}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="green-button">نمایش شهرها</button>
                                </div>
                            </form>
                        </div>
                    </c:if>

                    <c:if test="${tableCitiesVisible}">
                        <table class="table-result">
                            <tr class="header-row">
                                <th>کد</th>
                                <th>نام شهر</th>
                                <th>ویرایش</th>
                                <th>حذف</th>
                            </tr>
                            <c:forEach var="city" items="${cities}" varStatus="status">
                                <tr class="${status.count % 2 == 1 ? 'odd-row' : 'even-row'}">
                                    <td>${city.id}</td>
                                    <td>${city.name}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/edit-city?action=edit&id=${city.id}" class="green-button">ویرایش</a>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/edit-city" method="post" style="display: inline;">
                                            <input type="hidden" name="action" value="delete" />
                                            <input type="hidden" name="cityId" value="${city.id}" />
                                            <button type="submit" class="green-button" style="background-color: #dc3545;" onclick="return confirmDelete()">
                                                حذف
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>

                    <c:if test="${panelEditCityVisible}">
                        <h4>ویرایش شهر</h4>
                        <form action="${pageContext.request.contextPath}/admin/edit-city" method="post">
                            <input type="hidden" name="action" value="saveChanges" />
                            <input type="hidden" name="cityId" value="${cityId}" />

                            <table class="form-table">
                                <tr>
                                    <td class="form-label">کد شهر :</td>
                                    <td>${cityId}</td>
                                </tr>
                                <tr>
                                    <td class="form-label">نام شهر :</td>
                                    <td>
                                        <input type="text" name="cityName" value="${cityName}" class="form-control" required />
                                        <span style="color: red">*</span>
                                        <c:if test="${not empty cityNameError}">
                                            <span class="error-small">${cityNameError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="form-label">نام استان :</td>
                                    <td>
                                        <select name="provinceId" class="form-control">
                                            <option value="">-- انتخاب استان --</option>
                                            <c:forEach var="province" items="${provinces}">
                                                <option value="${province.id}" ${selectedProvinceId == province.id ? 'selected' : ''}>${province.name}</option>
                                            </c:forEach>
                                        </select>
                                        <c:if test="${not empty provinceIdError}">
                                            <span class="error-small">${provinceIdError}</span>
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
        return confirm('آیا از حذف این شهر اطمینان دارید؟');
    }
</script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ویرایش مدارس</title>

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
            width: 300px;
        }

        .dropdown-container {
            margin: 15px 0;
            padding: 10px;
            background-color: #f8f9fa;
            border-radius: 5px;
            border: 1px solid #dee2e6;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
        }

        .container {
            max-width: 1200px;
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
                            <h4>انتخاب استان و شهر</h4>
                            <form action="${pageContext.request.contextPath}/admin/edit-school" method="post">
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

                            <c:if test="${cities != null}">
                                <form action="${pageContext.request.contextPath}/admin/edit-school" method="post">
                                    <input type="hidden" name="action" value="showSchools" />
                                    <input type="hidden" name="provinceId" value="${selectedProvinceId}" />
                                    <div class="mb-3">
                                        <label class="form-label">انتخاب شهر :</label>
                                        <select name="cityId" class="form-control" style="width: 200px; display: inline-block;">
                                            <option value="">-- انتخاب شهر --</option>
                                            <c:forEach var="city" items="${cities}">
                                                <option value="${city.id}" ${selectedCityId == city.id ? 'selected' : ''}>${city.name}</option>
                                            </c:forEach>
                                        </select>
                                        <button type="submit" class="green-button">نمایش مدارس</button>
                                    </div>
                                </form>
                            </c:if>
                        </div>
                    </c:if>

                    <c:if test="${tableSchoolsVisible}">
                        <table class="table-result">
                            <tr class="header-row">
                                <th>نوع آموزشگاه</th>
                                <th>نام آموزشگاه</th>
                                <th>ویرایش</th>
                                <th>حذف</th>
                            </tr>
                            <c:forEach var="school" items="${schools}" varStatus="status">
                                <tr class="${status.count % 2 == 1 ? 'odd-row' : 'even-row'}">
                                    <td>${school.typeName}</td>
                                    <td>${school.name}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/edit-school?action=edit&id=${school.id}" class="green-button">ویرایش</a>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/edit-school" method="post" style="display: inline;">
                                            <input type="hidden" name="action" value="delete" />
                                            <input type="hidden" name="schoolId" value="${school.id}" />
                                            <button type="submit" class="green-button" style="background-color: #dc3545;" onclick="return confirmDelete()">
                                                حذف
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>

                    <c:if test="${panelEditSchoolVisible}">
                        <h4>ویرایش مدرسه</h4>
                        <form action="${pageContext.request.contextPath}/admin/edit-school" method="post">
                            <input type="hidden" name="action" value="saveChanges" />
                            <input type="hidden" name="schoolId" value="${schoolId}" />

                            <table class="form-table">
                                <tr>
                                    <td class="form-label">کد آموزشگاه :</td>
                                    <td>${schoolId}</td>
                                </tr>
                                <tr>
                                    <td class="form-label">نام آموزشگاه :</td>
                                    <td colspan="2">
                                        <input type="text" name="schoolName" value="${schoolName}" class="form-control" required style="width: 300px;" />
                                        <span style="color: red">*</span>
                                        <c:if test="${not empty schoolNameError}">
                                            <span class="error-small">${schoolNameError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="form-label">نوع آموزشگاه :</td>
                                    <td>
                                        <select name="schoolType" class="form-control">
                                            <c:forEach var="type" items="${schoolTypes}">
                                                <option value="${type.id}" ${selectedSchoolTypeId == type.id ? 'selected' : ''}>${type.typeName}</option>
                                            </c:forEach>
                                        </select>
                                        <c:if test="${not empty schoolTypeError}">
                                            <span class="error-small">${schoolTypeError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="form-label">نام استان :</td>
                                    <td>
                                        <select name="editProvince" class="form-control" onchange="this.form.submit()">
                                            <option value="">-- انتخاب استان --</option>
                                            <c:forEach var="province" items="${provinces}">
                                                <option value="${province.id}" ${editProvinceId == province.id ? 'selected' : ''}>${province.name}</option>
                                            </c:forEach>
                                        </select>
                                        <c:if test="${not empty editProvinceError}">
                                            <span class="error-small">${editProvinceError}</span>
                                        </c:if>
                                    </td>
                                    <td>
                                        <button type="submit" name="action" value="fillCity" class="green-button">نمایش شهر ها</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="form-label">نام شهر :</td>
                                    <td>
                                        <select name="editCity" class="form-control">
                                            <option value="">-- انتخاب شهر --</option>
                                            <c:forEach var="city" items="${editCities}">
                                                <option value="${city.id}" ${editCityId == city.id ? 'selected' : ''}>${city.name}</option>
                                            </c:forEach>
                                        </select>
                                        <c:if test="${not empty editCityError}">
                                            <span class="error-small">${editCityError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="3" class="text-center">
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
        return confirm('آیا از حذف این مدرسه اطمینان دارید؟');
    }
</script>
</body>
</html>
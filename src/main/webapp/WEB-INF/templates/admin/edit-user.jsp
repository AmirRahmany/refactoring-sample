<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ویرایش کاربران</title>

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
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
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

                    <c:if test="${tableUsersVisible}">
                        <table class="table-result">
                            <tr class="header-row">
                                <th>کد</th>
                                <th>نام کاربری</th>
                                <th>نام و نام خانوادگی</th>
                                <th>ویرایش</th>
                            </tr>
                            <c:forEach var="user" items="${userList}" varStatus="status">
                                <tr class="${status.count % 2 == 1 ? 'odd-row' : 'even-row'}">
                                    <td>${user.id}</td>
                                    <td>${user.username}</td>
                                    <td>${user.firstname} ${user.lastname}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/edit-users?ID=${user.id}">
                                            <img src="${pageContext.request.contextPath}/images/edit-icon.png" alt="ویرایش" width="20" height="20" />
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>

                    <c:if test="${panelEditUserVisible}">
                        <form action="${pageContext.request.contextPath}/admin/edit-users" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="userId" value="${userId}" />

                            <table class="form-table">
                                <tr>
                                    <td class="form-label">کد کاربری :</td>
                                    <td>${userId}</td>
                                </tr>
                                <tr>
                                    <td class="form-label">نام کاربری :</td>
                                    <td>
                                        <input type="text" name="textUsername" value="${userUsername}" class="form-control" required />
                                        <c:if test="${not empty textUsernameError}">
                                            <span class="error-small">${textUsernameError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="form-label">رمز عبور :</td>
                                    <td>
                                        <input type="password" name="textPassword" value="${userPassword}" class="form-control" />
                                        <c:if test="${not empty textPasswordError}">
                                            <span class="error-small">${textPasswordError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="form-label">نام :</td>
                                    <td>
                                        <input type="text" name="textFirstname" value="${userFirstname}" class="form-control" />
                                        <c:if test="${not empty textFirstnameError}">
                                            <span class="error-small">${textFirstnameError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="form-label">نام خانوادگی :</td>
                                    <td>
                                        <input type="text" name="textLastname" value="${userLastname}" class="form-control" />
                                        <c:if test="${not empty textLastnameError}">
                                            <span class="error-small">${textLastnameError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="form-label">ایمیل :</td>
                                    <td>
                                        <input type="email" name="textEmail" value="${userEmail}" class="form-control" />
                                        <c:if test="${not empty textEmailError}">
                                            <span class="error-small">${textEmailError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="form-label">وب سایت :</td>
                                    <td>
                                        <input type="url" name="textWebsite" value="${userWebsite}" class="form-control" />
                                        <c:if test="${not empty textWebsiteError}">
                                            <span class="error-small">${textWebsiteError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="form-label">عکس پروفایل :</td>
                                    <td>
                                        <input type="file" name="fileUpload" class="form-control" />
                                    </td>
                                    <td>
                                        <c:if test="${not empty userProfilePicture}">
                                            <img src="${pageContext.request.contextPath}/user-images/${userProfilePicture}" width="50" height="50" alt="پروفایل" />
                                        </c:if>
                                    </td>
                                </tr>
                                <c:if test="${editPermissionVisible}">
                                    <tr>
                                        <td class="form-label">سطح دسترسی :</td>
                                        <td>
                                            <select name="listPermission" class="form-control">
                                                <c:forEach var="permission" items="${permissionList}">
                                                    <option value="${permission.id}" ${userPermission == permission.id ? 'selected' : ''}>${permission.permissionName}</option>
                                                </c:forEach>
                                            </select>
                                            <c:if test="${not empty listPermissionError}">
                                                <span class="error-small">${listPermissionError}</span>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td colspan="2">
                                        <input type="checkbox" name="checkPmActive" id="checkPmActive" ${userPmActive ? 'checked' : ''} />
                                        <label for="checkPmActive">دریافت پیام خصوصی از اعضای سایت</label>
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

                    <c:if test="${not panelEditUserVisible and not tableUsersVisible}">
                        <div class="alert alert-danger">
                            <c:choose>
                                <c:when test="${not empty errorMessage}">${errorMessage}</c:when>
                                <c:otherwise>شما مجوز دسترسی به این صفحه را ندارید</c:otherwise>
                            </c:choose>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/templates/fragments/footer.jsp" />

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
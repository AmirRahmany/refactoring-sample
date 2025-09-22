<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <style>
        .errorBig {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/templates/fragments/layout.jsp" />

<div id="page">
    <div id="content">
        <div class="post">
            <h3 class="title">خروج</h3>
            <div class="entry">
                <c:if test="${not empty errorMessage}">
                    <p class="errorBig">${errorMessage}</p>
                </c:if>
                <p>شما با موفقیت از حساب کاربری خارج شدید.</p>
                <a href="${pageContext.request.contextPath}/login">ورود مجدد</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
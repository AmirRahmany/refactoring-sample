<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

        .title {
            color: #333;
            border-bottom: 2px solid #007acc;
            padding-bottom: 5px;
            margin-top: 30px;
            font-weight: bold;
        }

        .message-table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }

        .message-table td {
            padding: 12px;
            vertical-align: top;
        }

        .message-table tr:nth-child(even) {
            background-color: #f8f9fa;
        }

        .message-label {
            font-weight: bold;
            color: #495057;
            text-align: left;
            width: 120px;
        }

        .message-content {
            background-color: white;
            border: 1px solid #dee2e6;
            border-radius: 4px;
            padding: 15px;
            min-height: 250px;
            white-space: pre-wrap;
            word-wrap: break-word;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }

        .action-buttons {
            margin-top: 20px;
            text-align: left;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/templates/fragments/header.jsp" />

<div class="container">
    <div id="page">
        <div id="content">
            <div class="post">
                <h3 class="title">پیام خصوصی</h3>

                <div class="entry">
                    <c:if test="${not empty errorMessage}">
                        <div class="error-big">${errorMessage}</div>
                    </c:if>

                    <c:choose>
                        <c:when test="${not empty message}">
                            <div id="panelShowMessage">
                                <table class="message-table">
                                    <tr>
                                        <td class="message-label">
                                            <c:choose>
                                                <c:when test="${isInbox}">فرستنده :</c:when>
                                                <c:otherwise>گیرنده :</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${isInbox}">${message.sender}</c:when>
                                                <c:otherwise>${message.recipient}</c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="message-label">موضوع :</td>
                                        <td>${message.subject}</td>
                                    </tr>
                                    <tr>
                                        <td class="message-label">تاریخ ارسال :</td>
                                        <td><fmt:formatDate value="${message.sentDate}" pattern="yyyy/MM/dd HH:mm"/></td>
                                    </tr>
                                    <tr>
                                        <td class="message-label">متن :</td>
                                        <td>
                                            <div class="message-content">${message.content}</div>
                                        </td>
                                    </tr>
                                </table>

                                <div class="action-buttons">
                                    <a href="${pageContext.request.contextPath}/${isInbox ? 'inbox' : 'outbox'}" class="btn btn-secondary">بازگشت</a>
                                    <c:if test="${isInbox}">
                                        <a href="${pageContext.request.contextPath}/messages/reply/${message.id}" class="btn btn-primary me-2">پاسخ</a>
                                    </c:if>
                                    <a href="${pageContext.request.contextPath}/messages/delete/${message.id}" class="btn btn-danger me-2" onclick="return confirm('آیا از حذف این پیام اطمینان دارید؟')">حذف</a>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-warning">پیامی یافت نشد.</div>
                        </c:otherwise>
                    </c:choose>
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
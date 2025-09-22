<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <style>
        .TableResult {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            font-family: Tahoma, sans-serif;
        }
        .TableResult th, .TableResult td {
            border: 1px solid #ddd;
            padding: 12px 15px;
            text-align: right;
        }
        .headerRow {
            background-color: #2e5cb8;
            color: white;
            font-weight: bold;
        }
        .TableResult tr {
            border-bottom: 1px solid #ddd;
        }
        .TableResult tr:nth-of-type(even) {
            background-color: #f3f3f3;
        }
        .TableResult tr:hover {
            background-color: #e9f0ff;
        }
        .errorBig {
            color: #d9534f;
            font-weight: bold;
            display: block;
            padding: 10px;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 4px;
            margin: 10px 0;
            text-align: center;
        }
        .new-message {
            color: #d9534f;
            font-weight: bold;
        }
        .view-link {
            color: #2e5cb8;
            text-decoration: none;
            font-weight: 500;
        }
        .view-link:hover {
            color: #1e4080;
            text-decoration: underline;
        }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
        }
        .title {
            font-size: 24px;
            color: #2e5cb8;
            text-align: center;
            margin-bottom: 25px;
            padding-bottom: 10px;
            border-bottom: 2px solid #2e5cb8;
        }
        .empty-message {
            text-align: center;
            padding: 40px;
            color: #666;
            font-style: italic;
            font-size: 18px;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/templates/fragments/header.jsp" />

<div class="container">
    <div id="page">
        <div id="content">
            <div class="post">
                <h3 class="title">صندوق دریافت</h3>
                <div class="entry">
                    <c:if test="${not empty errorMessage}">
                        <p class="errorBig"><c:out value="${errorMessage}" /></p>
                    </c:if>

                    <c:if test="${not empty inboxMessages}">
                        <table class="TableResult">
                            <thead>
                            <tr class="headerRow">
                                <th>وضعیت</th>
                                <th>فرستنده</th>
                                <th>موضوع</th>
                                <th>تاریخ دریافت</th>
                                <th>عملیات</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="message" items="${inboxMessages}">
                                <tr>
                                    <td>
                                        <c:if test="${message.status == 0}">
                                            <span class="new-message">جدید</span>
                                        </c:if>
                                    </td>
                                    <td><c:out value="${message.senderUsername}" /></td>
                                    <td><c:out value="${message.subject}" /></td>
                                    <td><c:out value="${message.sendDate}" /></td>
                                    <td>
                                        <a href="/inbox/view?id=${message.id}" class="view-link">مشاهده</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>

                    <c:if test="${empty inboxMessages}">
                        <div class="empty-message">
                            هیچ پیامی در صندوق دریافت وجود ندارد.
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/templates/fragments/footer.jsp" />
</body>
</html>
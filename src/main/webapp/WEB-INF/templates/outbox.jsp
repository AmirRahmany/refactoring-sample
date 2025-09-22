<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>صندوق ارسال</title>
    <style>
        #page { width: 100%; max-width: 960px; margin: 0 auto; }
        #content { float: left; width: 70%; }
        .post { margin-bottom: 20px; }
        .title { font-size: 24px; text-align: right; }
        .entry { padding: 10px; }
        .errorBig { color: red; font-size: 16px; text-align: center; display: block; margin: 10px 0; }

        /* Table Styles */
        .TableResult {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            font-family: Tahoma, sans-serif;
            text-align: right;
            direction: rtl;
        }

        .TableResult .headerRow {
            background-color: #2e5cb8;
            color: white;
            font-weight: bold;
        }

        .TableResult th, .TableResult td {
            padding: 12px 15px;
            border: 1px solid #ddd;
        }

        .TableResult tr {
            border-bottom: 1px solid #ddd;
        }

        .TableResult tr:nth-of-type(even) {
            background-color: #f3f3f3;
        }

        .TableResult tr:last-of-type {
            border-bottom: 2px solid #2e5cb8;
        }

        .TableResult tr:hover {
            background-color: #e9f0ff;
        }

        .action-button {
            background-color: #2e5cb8;
            color: white;
            padding: 6px 12px;
            text-decoration: none;
            border-radius: 4px;
            display: inline-block;
        }

        .action-button:hover {
            background-color: #1e4080;
        }

        .empty-message {
            text-align: center;
            padding: 20px;
            color: #666;
            font-style: italic;
        }

        .pagination {
            text-align: center;
            margin: 20px 0;
        }

        .pagination a {
            color: #2e5cb8;
            padding: 8px 16px;
            text-decoration: none;
            border: 1px solid #ddd;
            margin: 0 4px;
            border-radius: 4px;
        }

        .pagination a.active {
            background-color: #2e5cb8;
            color: white;
            border: 1px solid #2e5cb8;
        }

        .pagination a:hover:not(.active) {
            background-color: #ddd;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/templates/fragments/header.jsp" />

<div id="page">
    <div id="content">
        <div class="post">
            <h3 class="title">صندوق ارسال</h3>
            <div class="entry">
                <c:if test="${not empty labelError}">
                    <div class="errorBig"><c:out value="${labelError}" /></div>
                </c:if>

                <c:if test="${not empty messages}">
                    <table class="TableResult">
                        <tr class="headerRow">
                            <th>گیرنده</th>
                            <th>موضوع</th>
                            <th>تاریخ ارسال</th>
                            <th>مشاهده</th>
                        </tr>
                        <c:forEach var="message" items="${messages}">
                            <tr>
                                <td><c:out value="${message.recipientName}" /></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${message.read}">
                                            <c:out value="${message.subject}" />
                                        </c:when>
                                        <c:otherwise>
                                            <strong><c:out value="${message.subject}" /></strong>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <fmt:formatDate value="${message.sentDate}" pattern="yyyy/MM/dd HH:mm" />
                                </td>
                                <td>
                                    <a href="/message/view/${message.id}" class="action-button">مشاهده</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:if>

                <c:if test="${empty messages}">
                    <div class="empty-message">
                        هیچ پیامی در صندوق ارسال وجود ندارد.
                    </div>
                </c:if>

                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <c:if test="${currentPage > 1}">
                            <a href="/outbox?page=${currentPage - 1}">قبلی</a>
                        </c:if>

                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <a href="/outbox?page=${i}"
                               <c:if test="${i == currentPage}">class="active"</c:if>>
                                ${i}
                            </a>
                        </c:forEach>

                        <c:if test="${currentPage < totalPages}">
                            <a href="/outbox?page=${currentPage + 1}">بعدی</a>
                        </c:if>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>
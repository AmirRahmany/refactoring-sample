<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <title>ارسال پیام جدید</title>
    <style>
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

        .title {
            color: #333;
            border-bottom: 2px solid #007acc;
            padding-bottom: 5px;
            margin-top: 30px;
            font-weight: bold;
            font-size: 24px;
            text-align: center;
        }

        .form-container {
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 25px;
            margin: 20px 0;
        }

        .form-table {
            width: 100%;
            border-collapse: collapse;
        }

        .form-table tr {
            margin-bottom: 15px;
        }

        .form-table td {
            padding: 12px;
            vertical-align: top;
        }

        .form-table td:first-child {
            text-align: left;
            font-weight: bold;
            color: #495057;
            width: 100px;
        }

        .form-input {
            width: 100%;
            padding: 10px 15px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            font-size: 16px;
            box-sizing: border-box;
        }

        .form-input:focus {
            border-color: #007acc;
            outline: none;
            box-shadow: 0 0 0 3px rgba(0, 122, 204, 0.1);
        }

        .form-textarea {
            width: 100%;
            height: 250px;
            padding: 15px;
            border: 1px solid #ced4da;
            border-radius: 4px;
            font-size: 16px;
            font-family: inherit;
            resize: vertical;
            box-sizing: border-box;
        }

        .form-textarea:focus {
            border-color: #007acc;
            outline: none;
            box-shadow: 0 0 0 3px rgba(0, 122, 204, 0.1);
        }

        .GreenButton {
            background-color: #28a745;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
            transition: background-color 0.3s;
        }

        .GreenButton:hover {
            background-color: #218838;
        }

        .error-field {
            color: #d9534f;
            font-size: 14px;
            margin-top: 5px;
            display: block;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 20px;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
        }

        .text-center {
            text-align: center;
        }

        @media (max-width: 768px) {
            .form-table {
                display: block;
            }

            .form-table tr {
                display: block;
                margin-bottom: 20px;
            }

            .form-table td {
                display: block;
                width: 100% !important;
                text-align: right !important;
                padding: 8px 0;
            }

            .form-table td:first-child {
                padding-bottom: 5px;
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
                <h3 class="title">ارسال پیام جدید</h3>

                <div class="entry">
                    <c:if test="${not empty errorMessage}">
                        <div class="errorBig"><c:out value="${errorMessage}" /></div>
                    </c:if>

                    <div class="form-container">
                        <form action="/compose" method="post">
                            <table class="form-table">
                                <tr>
                                    <td>گیرنده :</td>
                                    <td>
                                        <select name="recipient" class="form-input" required>
                                            <option value="">-- انتخاب کاربر --</option>
                                            <c:forEach var="user" items="${users}">
                                                <option value="${user.id}"
                                                    <c:if test="${user.id == param.recipient}">selected</c:if>>
                                                    ${user.username} - ${user.firstName} ${user.lastName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <c:if test="${not empty recipientError}">
                                            <span class="error-field">${recipientError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td>موضوع :</td>
                                    <td>
                                        <input type="text" name="subject" class="form-input"
                                               value="${param.subject}" placeholder="موضوع پیام" required />
                                        <c:if test="${not empty subjectError}">
                                            <span class="error-field">${subjectError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">متن :</td>
                                    <td>
                                        <textarea name="message" class="form-textarea"
                                                  placeholder="متن پیام خود را وارد کنید..." required>${param.message}</textarea>
                                        <c:if test="${not empty messageError}">
                                            <span class="error-field">${messageError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" class="text-center">
                                        <button type="submit" class="GreenButton">ارسال پیام</button>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/templates/fragments/footer.jsp" />
</body>
</html>
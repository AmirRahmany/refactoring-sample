<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ارسال دعوتنامه</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
        }

        #page {
            width: 100%;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        #content {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin-bottom: 30px;
        }

        .post {
            margin-bottom: 20px;
        }

        .title {
            font-size: 24px;
            color: #2e5cb8;
            text-align: center;
            margin-bottom: 25px;
            padding-bottom: 10px;
            border-bottom: 2px solid #2e5cb8;
        }

        .entry {
            padding: 10px;
        }

        .errorBig {
            color: #d9534f;
            font-size: 16px;
            text-align: center;
            display: block;
            margin: 15px 0;
            padding: 10px;
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 4px;
        }

        .englishInput {
            width: 200px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 16px;
            direction: ltr;
        }

        .GreenButton {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
            margin: 10px 0;
        }

        .GreenButton:hover {
            background-color: #3d8b40;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/templates/fragments/header.jsp" />

<div id="page">
    <div id="content">
        <div class="post">
            <h3 class="title">ارسال دعوتنامه</h3>
            <div class="entry">
                <c:if test="${not empty labelError}">
                    <div class="errorBig">${labelError}</div>
                </c:if>

                <div id="panelsend">
                    برای ارسال دعوتنامه، ایمیل شخص مورد نظر را در کادر زیر نوشته و بر روی ارسال کلیک کنید :
                    <br /><br />
                    ایمیل : <input type="text" id="textemail" name="email" class="englishInput" style="width: 200px;" />
                    <br /><br />
                    <form action="${pageContext.request.contextPath}/sendInvite" method="post">
                        <input type="hidden" name="email" id="hiddenEmail" />
                        <button type="submit" class="GreenButton" onclick="document.getElementById('hiddenEmail').value = document.getElementById('textemail').value;">ارسال</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/templates/fragments/footer.jsp" />
</body>
</html>
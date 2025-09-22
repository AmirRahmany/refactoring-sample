<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fa" dir="rtl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>اشتراک‌گذاری</title>
    <style>
        :root {
            --primary-color: #2e5cb8;
            --secondary-color: #4CAF50;
            --accent-color: #ff9800;
            --warning-color: #f44336;
            --light-gray: #f5f5f5;
            --border-color: #ddd;
            --text-color: #333;
        }

        body {
            font-family: Tahoma, Arial, sans-serif;
            line-height: 1.6;
            color: var(--text-color);
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
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
            color: var(--primary-color);
            text-align: center;
            margin-bottom: 25px;
            padding-bottom: 10px;
            border-bottom: 2px solid var(--primary-color);
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

        .error {
            color: #d9534f;
            font-size: 14px;
        }

        .GreenButton {
            background-color: var(--secondary-color);
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

        .info-message {
            text-align: center;
            padding: 20px;
            color: #666;
            font-size: 18px;
            background-color: var(--light-gray);
            border-radius: 8px;
            margin: 20px 0;
        }

        .Posts {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }

        .post-item {
            background: white;
            border: 1px solid var(--border-color);
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .post-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid var(--border-color);
        }

        .post-author {
            font-weight: bold;
            color: var(--primary-color);
        }

        .post-date {
            color: #666;
            font-size: 14px;
        }

        .post-content {
            margin-bottom: 15px;
            line-height: 1.8;
        }

        .post-attachment {
            margin-top: 15px;
        }

        .post-attachment a {
            color: var(--primary-color);
            text-decoration: none;
        }

        .post-attachment a:hover {
            text-decoration: underline;
        }

        .new-post-form {
            background: var(--light-gray);
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
        }

        .form-table {
            width: 100%;
            border-collapse: collapse;
        }

        .form-table td {
            padding: 10px;
            vertical-align: top;
        }

        .form-table textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid var(--border-color);
            border-radius: 4px;
            font-family: Tahoma, Arial, sans-serif;
            font-size: 16px;
            resize: vertical;
        }

        .form-table input[type="text"] {
            width: 100%;
            padding: 10px;
            border: 1px solid var(--border-color);
            border-radius: 4px;
            font-size: 16px;
        }

        .media-gallery {
            margin: 30px 0;
        }

        .gallery-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }

        .gallery-item {
            background: white;
            border: 1px solid var(--border-color);
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s;
        }

        .gallery-item:hover {
            transform: translateY(-5px);
        }

        .gallery-item img {
            width: 100%;
            height: 180px;
            object-fit: cover;
        }

        .gallery-item-content {
            padding: 15px;
        }

        .gallery-item-title {
            font-weight: bold;
            margin-bottom: 5px;
        }

        .gallery-item-desc {
            color: #666;
            font-size: 14px;
        }

        .gallery-link {
            display: block;
            text-align: center;
            margin-top: 15px;
            color: var(--primary-color);
            text-decoration: none;
            font-weight: bold;
        }

        .gallery-link:hover {
            text-decoration: underline;
        }

        .memberTable {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }

        .memberTable th {
            background-color: var(--primary-color);
            color: white;
            padding: 12px 15px;
            font-weight: bold;
        }

        .memberTable td {
            padding: 12px 15px;
            border-bottom: 1px solid var(--border-color);
        }

        .memberTable tr:nth-of-type(even) {
            background-color: var(--light-gray);
        }

        .memberTable tr:hover {
            background-color: #e9f0ff;
        }

        .pagination-container {
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 20px 0;
            gap: 15px;
        }

        .page-selector {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .page-dropdown {
            padding: 8px 12px;
            border: 1px solid var(--border-color);
            border-radius: 4px;
            font-size: 16px;
        }

        @media (max-width: 768px) {
            .gallery-grid {
                grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            }

            .form-table {
                display: block;
            }

            .form-table tr {
                display: block;
                margin-bottom: 15px;
            }

            .form-table td {
                display: block;
                padding: 5px 0;
            }
        }

        @media (max-width: 480px) {
            #content {
                padding: 15px;
            }

            .title {
                font-size: 20px;
            }

            .gallery-grid {
                grid-template-columns: 1fr;
            }

            .pagination-container {
                flex-direction: column;
            }
        }

        .panel_Bottom_Padding {
            padding-bottom: 30px;
        }

        .media-type-title {
            text-align: center;
            color: var(--primary-color);
            margin-bottom: 15px;
        }

        .media-type-section {
            margin-bottom: 30px;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/fragments/header.jsp" />

<div id="page">
    <div id="content">
        <div class="post">
            <h3 class="title" id="pagetitle">اشتراک‌گذاری</h3>

            <div class="entry">
                <c:if test="${not empty labelError}">
                    <div class="errorBig">${labelError}</div>
                </c:if>

                <!-- First Member Panel -->
                <c:if test="${panelFirstMemberVisible}">
                    <div class="info-message">
                        <p>در سال تحصیلی مورد نظر شما در این مدرسه یا دانشگاه هیچ کاربری عضو نمی باشد.</p>
                        <p>اولین عضو این قسمت باشید:</p>
                        <button class="GreenButton" onclick="registerAsFirstMember()">عضویت</button>
                    </div>
                </c:if>

                <!-- Not Member Panel -->
                <c:if test="${panelNotMemberVisible}">
                    <div class="info-message">
                        <p>در سال تحصیلی مورد نظر شما در این مدرسه یا دانشگاه هیچ کاربری عضو نمی باشد.</p>
                    </div>
                </c:if>

                <!-- Register Button -->
                <c:if test="${buttonRegisterVisible}">
                    <button class="GreenButton" onclick="registerInThisSection()">عضویت در این بخش</button>
                </c:if>

                <!-- Posts Panel -->
                <c:if test="${panelPostsVisible}">
                    <div class="panel_Bottom_Padding">
                        <c:forEach var="post" items="${posts}">
                            <div class="post-item">
                                <div class="post-header">
                                    <span class="post-author">${post.authorName}</span>
                                    <span class="post-date"><fmt:formatDate value="${post.postDate}" pattern="yyyy/MM/dd HH:mm"/></span>
                                </div>
                                <div class="post-content">${post.content}</div>
                                <c:if test="${not empty post.attachmentLink}">
                                    <div class="post-attachment">
                                        <a href="${post.attachmentLink}" target="_blank">لینک ضمیمه</a>
                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>

                        <!-- Pagination -->
                        <c:if test="${totalPages > 1}">
                            <div class="pagination-container">
                                <div class="page-selector">
                                    <label for="pageSelect">صفحه:</label>
                                    <select id="pageSelect" class="page-dropdown" onchange="goToPage(this.value)">
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <option value="${i}" ${i == currentPage ? 'selected' : ''}>${i}</option>
                                        </c:forEach>
                                    </select>
                                    <button class="GreenButton" onclick="goToSelectedPage()">برو</button>
                                </div>
                                <div class="pagination-info">
                                    صفحه ${currentPage} از ${totalPages}
                                </div>
                            </div>
                        </c:if>
                    </div>
                </c:if>

                <!-- New Post Panel -->
                <c:if test="${panelNewPostVisible}">
                    <div class="panel_Bottom_Padding new-post-form">
                        <h3 class="title">پست جدید</h3>
                        <form action="${pageContext.request.contextPath}/sharing/new-post" method="post">
                            <table class="form-table">
                                <tr>
                                    <td valign="top">متن پست:</td>
                                    <td>
                                        <textarea name="postContent" rows="5" required placeholder="متن پست خود را وارد کنید..."></textarea>
                                        <c:if test="${not empty postTextError}">
                                            <span class="error">${postTextError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td>لینک ضمیمه:</td>
                                    <td dir="ltr">
                                        <input type="url" name="postLink" placeholder="https://example.com" value="${postLinkValue}"/>
                                        <br />
                                        <c:if test="${not empty postLinkError}">
                                            <span class="error">${postLinkError}</span>
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" align="center">
                                        <button type="submit" class="GreenButton">ارسال</button>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                </c:if>

                <!-- Media Gallery Panel -->
                <c:if test="${panelMediaGalleryVisible}">
                    <div class="panel_Bottom_Padding media-gallery">
                        <h3 class="title">گالری چندرسانه ای</h3>

                        <div class="gallery-grid">
                            <!-- Pictures Section -->
                            <div class="media-type-section">
                                <h4 class="media-type-title">تصویر</h4>
                                <c:choose>
                                    <c:when test="${not empty pictures}">
                                        <div class="gallery-grid">
                                            <c:forEach var="picture" items="${pictures}">
                                                <div class="gallery-item">
                                                    <img src="${picture.thumbnailUrl}" alt="تصویر" />
                                                    <div class="gallery-item-content">
                                                        <div class="gallery-item-title">${picture.title}</div>
                                                        <div class="gallery-item-desc">${picture.description}</div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="info-message">هیچ تصویری وجود ندارد.</div>
                                    </c:otherwise>
                                </c:choose>
                                <a href="${pageContext.request.contextPath}/gallery/pictures" class="gallery-link">مشاهده گالری</a>
                            </div>

                            <!-- Videos Section -->
                            <div class="media-type-section">
                                <h4 class="media-type-title">ویدئو</h4>
                                <c:choose>
                                    <c:when test="${not empty videos}">
                                        <div class="gallery-grid">
                                            <c:forEach var="video" items="${videos}">
                                                <div class="gallery-item">
                                                    <img src="${video.thumbnailUrl}" alt="ویدئو" />
                                                    <div class="gallery-item-content">
                                                        <div class="gallery-item-title">${video.title}</div>
                                                        <div class="gallery-item-desc">${video.description}</div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="info-message">هیچ ویدئویی وجود ندارد.</div>
                                    </c:otherwise>
                                </c:choose>
                                <a href="${pageContext.request.contextPath}/gallery/videos" class="gallery-link">مشاهده گالری</a>
                            </div>

                            <!-- Sounds Section -->
                            <div class="media-type-section">
                                <h4 class="media-type-title">صوت</h4>
                                <c:choose>
                                    <c:when test="${not empty sounds}">
                                        <div class="gallery-grid">
                                            <c:forEach var="sound" items="${sounds}">
                                                <div class="gallery-item">
                                                    <div style="background-color: #f0f0f0; height: 180px; display: flex; align-items: center; justify-content: center;">
                                                        <span style="font-size: 48px;">🔊</span>
                                                    </div>
                                                    <div class="gallery-item-content">
                                                        <div class="gallery-item-title">${sound.title}</div>
                                                        <div class="gallery-item-desc">${sound.description}</div>
                                                    </div>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="info-message">هیچ فایل صوتی وجود ندارد。</div>
                                    </c:otherwise>
                                </c:choose>
                                <a href="${pageContext.request.contextPath}/gallery/sounds" class="gallery-link">مشاهده گالری</a>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Registered Users Panel -->
                <c:if test="${panelregisterUsersVisible}">
                    <div class="panel_Bottom_Padding">
                        <h3 class="title">لیست اعضای این بخش</h3>
                        <c:choose>
                            <c:when test="${not empty registeredUsers}">
                                <table class="memberTable">
                                    <thead>
                                        <tr>
                                            <th>نام کاربری</th>
                                            <th>نام</th>
                                            <th>نام خانوادگی</th>
                                            <th>تاریخ عضویت</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="user" items="${registeredUsers}">
                                            <tr>
                                                <td>${user.username}</td>
                                                <td>${user.firstName}</td>
                                                <td>${user.lastName}</td>
                                                <td><fmt:formatDate value="${user.joinDate}" pattern="yyyy/MM/dd"/></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <div class="info-message">هیچ عضوی ثبت نام نکرده است.</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<script>
    function registerAsFirstMember() {
        window.location.href = '${pageContext.request.contextPath}/sharing/register?firstMember=true';
    }

    function registerInThisSection() {
        window.location.href = '${pageContext.request.contextPath}/sharing/register';
    }

    function goToPage(pageNumber) {
        window.location.href = '${pageContext.request.contextPath}/sharing?page=' + pageNumber;
    }

    function goToSelectedPage() {
        const selectElement = document.getElementById('pageSelect');
        const pageNumber = selectElement.value;
        goToPage(pageNumber);
    }
</script>
</body>
</html>
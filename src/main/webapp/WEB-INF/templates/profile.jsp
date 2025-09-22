<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html dir="rtl" lang="fa">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>پروفایل</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.rtl.min.css" rel="stylesheet"
          integrity="sha384-4m0VOA9ANcu0zTjwj8ThzKrRbPna9n4nKdbEb7WmmV4s5fV6G2P3q3b1A"
          crossorigin="anonymous">
    <style>
        :root {
            --primary-color: #2e5cb8;
            --secondary-color: #4CAF50;
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
            max-width: 960px;
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
            color: red;
            font-size: 16px;
            text-align: center;
            margin-bottom: 15px;
        }

        .GreenButton {
            background-color: var(--secondary-color);
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin: 5px;
            font-size: 16px;
            transition: background-color 0.3s;
        }

        .GreenButton:hover {
            background-color: #3d8b40;
        }

        .GreenButton:disabled {
            background-color: #cccccc;
            cursor: not-allowed;
        }

        .noUnderline {
            text-decoration: none;
            color: var(--primary-color);
        }

        .friend-table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
            font-size: 16px;
            text-align: right;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
        }

        .friend-table th {
            background-color: var(--primary-color);
            color: white;
            padding: 12px 15px;
            font-weight: bold;
        }

        .friend-table td {
            border: 1px solid var(--border-color);
            padding: 12px 15px;
        }

        .friend-table tr:nth-child(even) {
            background-color: var(--light-gray);
        }

        .friend-table tr:hover {
            background-color: #e9f0ff;
        }

        .profile-table {
            width: 100%;
            border: 0;
            margin: 20px 0;
        }

        .profile-table td {
            padding: 15px;
            vertical-align: top;
        }

        .profile-image {
            width: 100px;
            height: 100px;
            border-radius: 10px;
            object-fit: cover;
        }

        @media (max-width: 768px) {
            .friend-table, .profile-table {
                font-size: 14px;
            }

            .friend-table th, .friend-table td, .profile-table td {
                padding: 8px 10px;
            }

            .profile-image {
                width: 80px;
                height: 80px;
            }
        }

        @media (max-width: 480px) {
            .friend-table, .profile-table {
                display: block;
                overflow-x: auto;
            }

            #content {
                padding: 15px;
            }

            .title {
                font-size: 20px;
            }
        }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/templates/fragments/header.jsp" />
<div id="page">
    <div id="content">
        <div class="post">
            <h3 class="title">پروفایل</h3>
            <div class="entry">
                <c:if test="${not empty labelNotification}">
                    <div class="errorBig">${labelNotification}</div>
                </c:if>
                <br />

                <table class="profile-table">
                    <tr>
                        <td>
                            <h3 class="noUnderline">${labelUsername}</h3>
                        </td>
                        <td>
                            <c:if test="${buttonAddFriendVisible}">
                                <button class="GreenButton" onclick="addFriend('${profileUserId}')">
                                    اضافه کردن به لیست دوستان
                                </button>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${buttonConfirmFriendVisible}">
                                <button class="GreenButton" onclick="confirmFriend('${profileUserId}')">
                                    تایید درخواست دوستی
                                </button>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${buttonRejectFriendVisible}">
                                <button class="GreenButton" onclick="rejectFriend('${profileUserId}')">
                                    رد درخواست دوستی
                                </button>
                            </c:if>
                            <c:if test="${buttonWaitingForConfirmVisible}">
                                <button class="GreenButton" disabled>
                                    در انتظار تایید درخواست دوستی
                                </button>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>نام : </label>
                            <span>${labelFirstName}</span>
                        </td>
                        <td></td>
                        <td rowspan="6">
                            <c:choose>
                                <c:when test="${not empty imageProfile}">
                                    <img src="${imageProfile}" class="profile-image" alt="Profile Image" />
                                </c:when>
                                <c:otherwise>
                                    <img src="/images/default-profile.png" class="profile-image" alt="Default Profile" />
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>نام خانوادگی : </label>
                            <span>${labelLastname}</span>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>سطح کاربری : </label>
                            <span>${LabelPermission}</span>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>ایمیل : </label>
                            <span>${labelEmail}</span>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>وب سایت : </label>
                            <c:choose>
                                <c:when test="${not empty labelWebsite}">
                                    <a href="${labelWebsite}" class="noUnderline">${labelWebsite}</a>
                                </c:when>
                                <c:otherwise>--</c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table>

                <br /><br />
                <h3>لیست دوستان</h3>
                <c:choose>
                    <c:when test="${not empty friendsList}">
                        <table class="friend-table">
                            <tr>
                                <th>نام کاربری</th>
                                <th>نام</th>
                                <th>نام خانوادگی</th>
                                <th>عملیات</th>
                            </tr>
                            <c:forEach var="friend" items="${friendsList}">
                                <tr>
                                    <td>${friend.username}</td>
                                    <td>${friend.firstname}</td>
                                    <td>${friend.lastname}</td>
                                    <td>
                                        <button class="GreenButton" onclick="viewProfile('${friend.id}')">
                                            مشاهده پروفایل
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p>هیچ دوستی وجود ندارد</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script>
    function addFriend(userId) {
        fetch('/profile/add-friend/' + userId, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': getCsrfToken()
            }
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('خطا در افزودن دوست');
            }
        })
        .catch(error => console.error('Error:', error));
    }

    function confirmFriend(userId) {
        fetch('/profile/confirm-friend/' + userId, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': getCsrfToken()
            }
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('خطا در تایید دوستی');
            }
        })
        .catch(error => console.error('Error:', error));
    }

    function rejectFriend(userId) {
        fetch('/profile/reject-friend/' + userId, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': getCsrfToken()
            }
        })
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                alert('خطا در رد درخواست دوستی');
            }
        })
        .catch(error => console.error('Error:', error));
    }

    function viewProfile(userId) {
        window.location.href = '/profile/' + userId;
    }

    function getCsrfToken() {
        return document.querySelector('meta[name="_csrf"]').getAttribute('content');
    }
</script>
</body>
</html>
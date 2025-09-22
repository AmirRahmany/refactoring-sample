<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<header>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <nav class="navbar navbar-expand-lg navbar-dark sticky-top">
        <div class="container-fluid">
            <a class="navbar-brand d-flex align-items-center" href="/home">
                <img src="https://bcassetcdn.com/public/blog/wp-content/uploads/2022/05/20174807/colorful-simple-logo-design-by-sizuka-designcrowd.png" alt="Logo"
                class="logo me-2 mx-2" />
                <span class="brand-text">همکلاسی</span>
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto d-flex flex-row align-items-center">
                    <li class="nav-item">
                        <a class="nav-link" href="/home">خانه</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/schools">لیست مدارس</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/members">لیست اعضا</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/register">ثبت نام</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link btn btn-secondary ms-2" href="/login">ورود/خروج</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>

<style>
    :root {
        --primary-color: #2e5cb8;
        --secondary-color: #4CAF50;
        --text-color: #333;
        --gradient-start: #2e5cb8;
        --gradient-end: #1e3c72;
    }

    body {
        font-family: 'Vazir', Tahoma, Arial, sans-serif;
    }

    .navbar {
        background: linear-gradient(90deg, var(--gradient-start), var(--gradient-end));
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
        transition: background 0.3s ease;
    }

    .navbar.sticky-top {
        z-index: 1030;
    }

    .navbar-brand {
        display: flex;
        align-items: center;
        font-weight: 700;
        font-size: 1.5rem;
        color: white !important;
        transition: transform 0.3s ease;
    }

    .navbar-brand:hover {
        transform: scale(1.05);
    }

    .logo {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        object-fit: cover;
    }

    .brand-text {
        font-size: 1.3rem;
    }

    .navbar-nav {
        gap: 15px;
    }

    .nav-item {
        display: inline-flex;
        align-items: center;
    }

    .nav-link {
        color: white !important;
        font-size: 1.1rem;
        padding: 8px 12px;
        position: relative;
        transition: color 0.3s ease, background-color 0.3s ease;
        border-radius: 5px;
    }

    .nav-link:hover {
        color: #e9f0ff !important;
        background-color: rgba(255, 255, 255, 0.1);
    }

    .nav-link::after {
        content: '';
        position: absolute;
        width: 0;
        height: 2px;
        bottom: 0;
        right: 0;
        background-color: var(--secondary-color);
        transition: width 0.3s ease;
    }

    .nav-link:hover::after {
        width: 100%;
        left: 0;
        right: auto;
    }

    .btn-secondary {
        background-color: var(--secondary-color);
        border: none;
        padding: 8px 16px;
        font-size: 1rem;
        border-radius: 5px;
        transition: background-color 0.3s ease, transform 0.2s ease;
    }

    .btn-secondary:hover {
        background-color: #3d8b40;
        transform: translateY(-2px);
    }

    @media (max-width: 768px) {
        .navbar-nav {
            flex-direction: column !important;
            text-align: right;
            padding: 10px;
            gap: 10px;
        }
        .nav-link {
            padding: 10px;
            font-size: 1rem;
        }
        .logo {
            width: 35px;
            height: 35px;
        }
        .brand-text {
            font-size: 1.2rem;
        }
    }

    @media (max-width: 480px) {
        .navbar-brand {
            font-size: 1.2rem;
        }
        .logo {
            width: 30px;
            height: 30px;
        }
        .nav-link {
            font-size: 0.9rem;
        }
    }
</style>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/vazir-font@32.102.0/dist/font-face.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
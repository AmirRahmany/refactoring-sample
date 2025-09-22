<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Footer Fragment (footer.jsp) -->
<footer class="footer mt-5">
    <style>
        .footer {
            background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
            color: white;
            padding: 30px 0 20px;
            margin-top: 50px !important;
            border-top: 4px solid #3498db;
        }

        .footer h5 {
            color: #3498db;
            font-weight: bold;
            margin-bottom: 20px;
            font-size: 1.2rem;
            border-bottom: 2px solid #3498db;
            padding-bottom: 10px;
            display: inline-block;
        }

        .footer-links {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .footer-links li {
            margin-bottom: 12px;
        }

        .footer-links a {
            color: #ecf0f1;
            text-decoration: none;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .footer-links a:hover {
            color: #3498db;
            transform: translateX(-5px);
        }

        .footer-links i {
            font-size: 1.1rem;
        }

        .social-links {
            display: flex;
            gap: 15px;
            margin-top: 20px;
        }

        .social-links a {
            display: flex;
            align-items: center;
            justify-content: center;
            width: 40px;
            height: 40px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 50%;
            color: white;
            transition: all 0.3s ease;
        }

        .social-links a:hover {
            background: #3498db;
            transform: translateY(-3px);
        }

        .contact-info {
            color: #ecf0f1;
            margin-bottom: 15px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .contact-info i {
            color: #3498db;
            font-size: 1.2rem;
        }

        .copyright {
            background: rgba(0, 0, 0, 0.2);
            padding: 15px 0;
            margin-top: 30px;
            text-align: center;
            font-size: 0.9rem;
        }

        .footer-divider {
            border-top: 1px solid rgba(255, 255, 255, 0.1);
            margin: 25px 0;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .footer {
                text-align: center;
            }

            .footer h5 {
                text-align: center;
                display: block;
            }

            .social-links {
                justify-content: center;
            }

            .contact-info {
                justify-content: center;
            }
        }
    </style>

    <div class="container">
        <div class="row">
            <!-- Quick Links -->
            <div class="col-md-4 mb-4">
                <h5>دسترسی سریع</h5>
                <ul class="footer-links">
                    <li>
                        <a href="/">
                            <i class="bi bi-house-door"></i>
                            صفحه اصلی
                        </a>
                    </li>
                    <li>
                        <a href="/profile">
                            <i class="bi bi-person"></i>
                            پروفایل من
                        </a>
                    </li>
                    <li>
                        <a href="/friends">
                            <i class="bi bi-people"></i>
                            دوستان
                        </a>
                    </li>
                    <li>
                        <a href="/messages">
                            <i class="bi bi-chat"></i>
                            پیام‌ها
                        </a>
                    </li>
                </ul>
            </div>

            <!-- Support -->
            <div class="col-md-4 mb-4">
                <h5>پشتیبانی</h5>
                <ul class="footer-links">
                    <li>
                        <a href="/help">
                            <i class="bi bi-question-circle"></i>
                            راهنمایی
                        </a>
                    </li>
                    <li>
                        <a href="/faq">
                            <i class="bi bi-patch-question"></i>
                            سوالات متداول
                        </a>
                    </li>
                    <li>
                        <a href="/contact">
                            <i class="bi bi-envelope"></i>
                            تماس با ما
                        </a>
                    </li>
                    <li>
                        <a href="/report">
                            <i class="bi bi-flag"></i>
                            گزارش مشکل
                        </a>
                    </li>
                </ul>
            </div>

            <!-- Contact Info -->
            <div class="col-md-4 mb-4">
                <h5>ارتباط با ما</h5>
                <div class="contact-info">
                    <i class="bi bi-geo-alt"></i>
                    <span>تهران، خیابان آزادی، دانشگاه شریف</span>
                </div>
                <div class="contact-info">
                    <i class="bi bi-telephone"></i>
                    <span>۰۲۱-۶۶۱۶۶۱۶۶</span>
                </div>
                <div class="contact-info">
                    <i class="bi bi-envelope"></i>
                    <span>support@hamkelasi.com</span>
                </div>

                <div class="footer-divider"></div>

                <div class="social-links">
                    <a href="#" title="فیس‌بوک">
                        <i class="bi bi-facebook"></i>
                    </a>
                    <a href="#" title="توییتر">
                        <i class="bi bi-twitter"></i>
                    </a>
                    <a href="#" title="اینستاگرام">
                        <i class="bi bi-instagram"></i>
                    </a>
                    <a href="#" title="لینکدین">
                        <i class="bi bi-linkedin"></i>
                    </a>
                    <a href="#" title="تلگرام">
                        <i class="bi bi-telegram"></i>
                    </a>
                </div>
            </div>
        </div>

        <div class="footer-divider"></div>

        <!-- Copyright -->
        <div class="copyright">
            <div class="container">
                <p class="mb-0">
                    © ۱۴۰۲ - تمام حقوق مادی و معنوی این سامانه متعلق به
                    <strong>هم‌کلاسی</strong>
                    می‌باشد.
                </p>
                <p class="mb-0 mt-2">
                    <small>نسخه ۲.۱.۰</small>
                </p>
            </div>
        </div>
    </div>

    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</footer>
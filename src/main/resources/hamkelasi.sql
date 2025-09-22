-- Drop tables in reverse order of dependencies to avoid foreign key issues
DROP TABLE IF EXISTS PM;
DROP TABLE IF EXISTS MediaShare;
DROP TABLE IF EXISTS TextShare;
DROP TABLE IF EXISTS SchoolRegistration;
DROP TABLE IF EXISTS Friendship;
DROP TABLE IF EXISTS SchoolYear;
DROP TABLE IF EXISTS School;
DROP TABLE IF EXISTS SchoolType;
DROP TABLE IF EXISTS ShareType;
DROP TABLE IF EXISTS Userlist;
DROP TABLE IF EXISTS Permission;
DROP TABLE IF EXISTS City;
DROP TABLE IF EXISTS Province;
DROP TABLE IF EXISTS AcademicYear;

-- Create tables in order of dependencies with AUTO_INCREMENT
CREATE TABLE AcademicYear (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    AcademicYear VARCHAR(50) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE Province (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ProvinceName VARCHAR(25) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE City (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    CityName VARCHAR(25) NOT NULL,
    ProvinceID INT NOT NULL,
    FOREIGN KEY (ProvinceID) REFERENCES Province(ID) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE Permission (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    UserType VARCHAR(20) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE Userlist (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    Firstname VARCHAR(25) NOT NULL,
    Lastname VARCHAR(25) NOT NULL,
    ProfilePicture VARCHAR(1000) NOT NULL,
    Email VARCHAR(50) NOT NULL UNIQUE,
    Website VARCHAR(50),
    Permission INT NOT NULL,
    RegisterDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    IsPmActivate BOOLEAN NOT NULL,
    FOREIGN KEY (Permission) REFERENCES Permission(ID)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE Friendship (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    FriendID INT NOT NULL,
    FriendshipStatus INT NOT NULL,
    FOREIGN KEY (UserID) REFERENCES Userlist(ID),
    FOREIGN KEY (FriendID) REFERENCES Userlist(ID)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE SchoolType (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    Type VARCHAR(20) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE School (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    SchoolName VARCHAR(100) NOT NULL,
    SchoolType INT NOT NULL,
    CityID INT NOT NULL,
    FOREIGN KEY (CityID) REFERENCES City(ID) ON DELETE CASCADE,
    FOREIGN KEY (SchoolType) REFERENCES SchoolType(ID)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE SchoolYear (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    SchoolID INT NOT NULL,
    YearID INT NOT NULL,
    FOREIGN KEY (SchoolID) REFERENCES School(ID) ON DELETE CASCADE,
    FOREIGN KEY (YearID) REFERENCES AcademicYear(ID) ON DELETE CASCADE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE SchoolRegistration (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    SchoolYearID INT NOT NULL,
    FOREIGN KEY (SchoolYearID) REFERENCES SchoolYear(ID) ON DELETE CASCADE,
    FOREIGN KEY (UserID) REFERENCES Userlist(ID)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE ShareType (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ShareType VARCHAR(50) NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE TextShare (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    SchoolYearID INT NOT NULL,
    PostText TEXT NOT NULL,
    URL TEXT,
    PostDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (SchoolYearID) REFERENCES SchoolYear(ID) ON DELETE CASCADE,
    FOREIGN KEY (UserID) REFERENCES Userlist(ID)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE MediaShare (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    SchoolYearID INT NOT NULL,
    Subject VARCHAR(150) NOT NULL,
    Description TEXT,
    URL TEXT NOT NULL,
    ThumbURL TEXT NOT NULL,
    PostDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ShareType INT NOT NULL,
    FOREIGN KEY (SchoolYearID) REFERENCES SchoolYear(ID) ON DELETE CASCADE,
    FOREIGN KEY (ShareType) REFERENCES ShareType(ID),
    FOREIGN KEY (UserID) REFERENCES Userlist(ID)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE PM (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    FromUser INT NOT NULL,
    ToUser INT NOT NULL,
    Subject VARCHAR(100) NOT NULL,
    PmText TEXT NOT NULL,
    SendDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Status INT NOT NULL,
    FOREIGN KEY (FromUser) REFERENCES Userlist(ID),
    FOREIGN KEY (ToUser) REFERENCES Userlist(ID)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Insert Data
INSERT INTO AcademicYear (AcademicYear) VALUES
('86 - 87'),
('87 - 88'),
('88 - 89'),
('89 - 90');

INSERT INTO Permission (UserType) VALUES
('Administrator'),
('User'),
('Banned');

INSERT INTO Province (ProvinceName) VALUES
('آذربایجان شرقی'),
('آذربایجان غربی'),
('اردبیل'),
('اصفهان'),
('البرز'),
('ایلام'),
('بوشهر'),
('تهران'),
('چهارمحال و بختیاری'),
('خراسان جنوبی'),
('خراسان رضوی'),
('خراسان شمالی'),
('خوزستان'),
('زنجان'),
('سمنان'),
('سیستان و بلوچستان'),
('فارس'),
('قزوین'),
('قم'),
('کردستان'),
('کرمان'),
('کرمانشاه'),
('کهگیلویه و بویراحمد'),
('گلستان'),
('گیلان'),
('لرستان'),
('مازندران'),
('مرکزی'),
('هرمزگان'),
('همدان'),
('یزد');

INSERT INTO City (CityName, ProvinceID) VALUES
('آذرشهر', 1),('اسکو', 1),('اهر', 1),('بستان آباد', 1),('بناب', 1),
('تبریز', 1),('جلفا', 1),('چاراويماق', 1),('سراب', 1),('شبستر', 1),
('عجب شير', 1),('كليبر', 1),('مراغه', 1),('مرند', 1),('ملکان', 1),
('میانه', 1),('ورزقان', 1),('هریس', 1),('هشترود', 1),('ارومیه', 2),
('اشنويه', 2),('بوkan', 2),('پيرانشهر', 2),('تکاب', 2),('چالدران', 2),
('خوی', 2),('چايپاره', 2),('سردشت', 2),('سلماس', 2),('شاهین دژ', 2),
('ماكو', 2),('پلدشت', 2),('شوط', 2),('مهاباد', 2),('مياندوآب', 2),
('نقده', 2),('اردبیل', 3),('بیله سوار', 3),('پارس آباد', 3),('خلخال', 3),
('کوثر', 3),('گرمی', 3),('مشگين شهر', 3),('نمين', 3),('نیر', 3),
('شاهين شهر و ميمه', 4),('برخوار', 4),('تيران و كرون', 4),('دهاقان', 4),('فريدن', 4),
('فریدونشهر', 4),('فلاورجان', 4),('کاشان', 4),('گلپايگان', 4),('لنجان', 4),
('مباركه', 4),('نائین', 4),('نجف آباد', 4),('اردستان', 4),('آران و بيدگل', 4),
('اصفهان', 4),('چادگان', 4),('خمینی شهر', 4),('خوانسار', 4),('سمیرم', 4),
('شهرضا', 4),('نطنز', 4),('کرج', 5),('ساوجبلاغ', 5),('نظرآباد', 5),
('طالقان', 5),('اشتهارد', 5),('آبدانان', 6),('ايلام', 6),('ایوان', 6),
('دره شهر', 6),('دهلران', 6),('شيروان وچرداول', 6),('مهران', 6),('ملكشاهي', 6),
('بوشهر', 7),('تنگستان', 7),('دشتی', 7),('دير', 7),('ديلم', 7);


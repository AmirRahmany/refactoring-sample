package com.hamkelasi.ui;

import com.hamkelasi.bll.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/gallery")
@MultipartConfig
public class GalleryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Check for UserID cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("UserID".equals(cookie.getName()) && session.getAttribute("UserID") == null) {
                    session.setAttribute("UserID", cookie.getValue());
                }
            }
        }

        // Initialize variables
        int userID;
        int schoolYearID;
        MediaShare.Types galleryType;
        String pageTitle = "";
        String headerGalleryType = "";
        List<TableRow> tableRows = new ArrayList<>();

        // Validate session and query parameters
        if (session.getAttribute("UserID") == null || request.getParameter("ID") == null || request.getParameter("Type") == null) {
            response.sendRedirect(request.getContextPath() + "/Schools");
            return;
        }

        try {
            userID = Integer.parseInt((String) session.getAttribute("UserID"));
            schoolYearID = Integer.parseInt(request.getParameter("ID"));
            galleryType = MediaShare.Types.values()[Integer.parseInt(request.getParameter("Type"))];
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            response.sendRedirect(request.getContextPath() + "/Schools");
            return;
        }

        // Fetch school year details
        SchoolYear schoolYear = new SchoolYear(schoolYearID);
        School school = new School(schoolYear.getSchoolId());
        Year year = new Year(schoolYear.getYearId());
        pageTitle = school.getName() + "  |  " + " سال تحصیلی " + year.getAcademicYear();

        // Fetch media items
        int itemCount = 0;
        List<MediaShare> items = new ArrayList<>();
        if (galleryType == MediaShare.Types.PICTURE) {
            itemCount = new MediaShare().getPictureCount(schoolYearID);
            if (itemCount > 0) {
                items = new MediaShare().getPictures(schoolYearID);
            }
            headerGalleryType = "گالری تصاویر";
        } else if (galleryType == MediaShare.Types.VIDEO) {
            itemCount = new MediaShare().getVideoCount(schoolYearID);
            if (itemCount > 0) {
                items = new MediaShare().getVideos(schoolYearID);
            }
            headerGalleryType = "گالری ویدئو";
        } else if (galleryType == MediaShare.Types.SOUND) {
            itemCount = new MediaShare().getSoundCount(schoolYearID);
            if (itemCount > 0) {
                items = new MediaShare().getSounds(schoolYearID);
            }
            headerGalleryType = "گالری فایل های صوتی";
        }

        // Build table
        if (itemCount == 0) {
            TableRow row = new TableRow();
            row.getCells().add(new TableCell("در این گالری هیچ فایلی وجود ندارد"));
            tableRows.add(row);
        } else {
            TableRow row = new TableRow();
            int lastCell = 0;
            for (int i = 0; i < itemCount; i++) {
                User user = new User(items.get(i).getUserId());
                StringBuilder cellContent = new StringBuilder();
                cellContent.append(items.get(i).getSubject()).append("<br/>");
                cellContent.append("<img src=\"").append(items.get(i).getThumbnailUrl()).append("\"/><br/>");
                cellContent.append("توضیحات : ").append(items.get(i).getDescription()).append("<br/>");
                cellContent.append("تاریخ ارسال : ").append(items.get(i).getPostDate().toString()).append("<br/>");
                cellContent.append("ارسال شده توسط : <br/>");
                cellContent.append("<a href=\"Profile?ID=").append(items.get(i).getUserId()).append("\">")
                        .append(user.getFirstname()).append(" ").append(user.getLastname()).append("</a><br/>");
                String downloadText = galleryType == MediaShare.Types.PICTURE ? "مشاهده سایز اصلی عکس" :
                        galleryType == MediaShare.Types.VIDEO ? "دانلود ویدئو" : "دانلود فایل";
                cellContent.append("<a href=\"").append(items.get(i).getUrl()).append("\">").append(downloadText).append("</a>");

                row.getCells().add(new TableCell(cellContent.toString()));

                if (lastCell == 1) {
                    tableRows.add(row);
                    row = new TableRow();
                    lastCell = 0;
                } else {
                    lastCell++;
                }
            }
            if (!row.getCells().isEmpty()) {
                tableRows.add(row);
            }
        }

        // Set attributes for JSP
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("headerGalleryType", headerGalleryType);
        request.setAttribute("tableRows", tableRows);
        request.setAttribute("schoolYearID", schoolYearID);
        request.setAttribute("galleryType", galleryType);

        request.getRequestDispatcher("/gallery.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Validate session and query parameters
        if (session.getAttribute("UserID") == null || request.getParameter("ID") == null || request.getParameter("Type") == null) {
            response.sendRedirect(request.getContextPath() + "/Schools");
            return;
        }

        int userID = Integer.parseInt((String) session.getAttribute("UserID"));
        int schoolYearID = Integer.parseInt(request.getParameter("ID"));
        MediaShare.Types galleryType = MediaShare.Types.values()[Integer.parseInt(request.getParameter("Type"))];

        // Mimic Page.IsValid
        if (isValidForm(request)) {
            MediaShare newMedia = new MediaShare();
            newMedia.setSubject(request.getParameter("textSubject"));
            newMedia.setDescription(request.getParameter("textDescription"));
            newMedia.setPostDate(LocalDateTime.now());
            newMedia.setSchoolYearId(schoolYearID);
            newMedia.setUserId(userID);
            newMedia.setShareType(galleryType);

            // Handle file upload
            Part filePart = request.getPart("uploadFile");
            if (filePart != null && filePart.getSize() > 0) {
                String filename = filePart.getSubmittedFileName();
                String savePath = getServletContext().getRealPath("/Sharing/") + File.separator + filename;
                File uploadDir = new File(getServletContext().getRealPath("/Sharing/"));
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                filePart.write(savePath);
                newMedia.setFilename(filename);
                newMedia.setServerMap(getServletContext().getRealPath("/Sharing/"));
            }

            newMedia.add();
            response.sendRedirect(request.getRequestURI() + "?ID=" + schoolYearID + "&Type=" + galleryType.ordinal());
        } else {
            // Re-render page if form is invalid
            doGet(request, response);
        }
    }

    private boolean isValidForm(HttpServletRequest request) throws ServletException, IOException {
        // Check required fields
        return request.getParameter("textSubject") != null &&
                request.getParameter("textDescription") != null &&
                !request.getParameter("textSubject").isEmpty() &&
                !request.getParameter("textDescription").isEmpty() &&
                request.getPart("uploadFile") != null;
    }

    // Helper class for table rows
    public static class TableRow {
        private List<TableCell> cells = new ArrayList<>();

        public List<TableCell> getCells() {
            return cells;
        }
    }

    // Helper class for table cells
    public static class TableCell {
        private String content;

        public TableCell(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}

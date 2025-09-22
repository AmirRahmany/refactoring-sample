package com.hamkelasi.ui.admin;

import com.hamkelasi.bll.Permission;
import com.hamkelasi.bll.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/edit-users")
public class EditUsersServlet extends HttpServlet {

    private String oldUsername, oldEmail, oldPicture, oldPermission;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();

        // Check for UserID cookie
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("UserID".equals(cookie.getName()) && session.getAttribute("UserID") == null) {
                    session.setAttribute("UserID", cookie.getValue());
                    break;
                }
            }
        }

        if (session.getAttribute("UserID") != null) {
            try {
                int loggedID = Integer.parseInt(session.getAttribute("UserID").toString());
                User loggedUser = new User(loggedID);

                String idParam = request.getParameter("ID");

                if (idParam != null) {
                    // Edit specific user
                    int id = Integer.parseInt(idParam);

                    if (loggedUser.getId() == id || loggedUser.getPermission() == 1) {
                        User editUser = new User(id);

                        request.setAttribute("tableUsersVisible", false);
                        request.setAttribute("panelEditUserVisible", true);

                        // Set user data for editing
                        request.setAttribute("userId", editUser.getId());
                        request.setAttribute("userEmail", editUser.getEmail());
                        request.setAttribute("userFirstname", editUser.getFirstname());
                        request.setAttribute("userLastname", editUser.getLastname());
                        request.setAttribute("userPassword", editUser.getPassword());
                        request.setAttribute("userUsername", editUser.getUsername());
                        request.setAttribute("userWebsite", editUser.getWebsite());
                        request.setAttribute("userPmActive", editUser.isPmActivate());
                        request.setAttribute("userProfilePicture", editUser.getProfilePicture());

                        oldEmail = editUser.getEmail();
                        oldUsername = editUser.getUsername();
                        oldPicture = editUser.getProfilePicture();

                        if (loggedUser.getPermission() == 1) {
                            request.setAttribute("editPermissionVisible", true);
                            List<Permission> permissionList = Permission.getList();
                            request.setAttribute("permissionList", permissionList);
                            request.setAttribute("userPermission", editUser.getPermission());
                            oldPermission = String.valueOf(editUser.getPermission());
                        } else {
                            request.setAttribute("editPermissionVisible", false);
                            oldPermission = String.valueOf(loggedUser.getPermission());
                        }
                    } else {
                        request.setAttribute("tableUsersVisible", false);
                        request.setAttribute("panelEditUserVisible", false);
                        request.setAttribute("errorMessage", "شما مجوز دسترسی به این صفحه را ندارید");
                    }
                } else {
                    // List all users (admin only)
                    if (loggedUser.getPermission() == 1) {
                        request.setAttribute("panelEditUserVisible", false);
                        request.setAttribute("tableUsersVisible", true);

                        List<User> userList = new User().getList();
                        request.setAttribute("userList", userList);
                    } else {
                        request.setAttribute("tableUsersVisible", false);
                        request.setAttribute("panelEditUserVisible", false);
                        request.setAttribute("errorMessage", "شما مجوز دسترسی به این صفحه را ندارید");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                request.setAttribute("tableUsersVisible", false);
                request.setAttribute("panelEditUserVisible", false);
                request.setAttribute("errorMessage", "خطا در پردازش درخواست");
            }
        } else {
            request.setAttribute("tableUsersVisible", false);
            request.setAttribute("panelEditUserVisible", false);
            request.setAttribute("errorMessage", "شما هنوز وارد سایت نشده اید");
        }

        // Forward to JSP page
        request.getRequestDispatcher("/WEB-INF/templates/admin/edit-user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String errorMessage = null;

        if (session.getAttribute("UserID") != null) {
            try {
                int loggedID = Integer.parseInt(session.getAttribute("UserID").toString());
                User loggedUser = new User(loggedID);

                String idParam = request.getParameter("userId");
                if (idParam != null) {
                    int userId = Integer.parseInt(idParam);

                    if (loggedUser.getId() == userId || loggedUser.getPermission() == 1) {
                        // Process form submission
                        String username = request.getParameter("textUsername");
                        String password = request.getParameter("textPassword");
                        String firstname = request.getParameter("textFirstname");
                        String lastname = request.getParameter("textLastname");
                        String email = request.getParameter("textEmail");
                        String website = request.getParameter("textWebsite");
                        String permissionStr = request.getParameter("listPermission");
                        boolean pmActive = "on".equals(request.getParameter("checkPmActive"));

                        User updatedUser = new User();
                        int isValid = updatedUser.isUpdateValid(oldUsername, username, oldEmail, email, website);

                        if (isValid == 0) {
                            String pictureUrl = oldPicture;

                            // Handle file upload would go here in real implementation
                            /*
                            Part filePart = request.getPart("fileUpload");
                            if (filePart != null && filePart.getSize() > 0) {
                                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                                String savePath = getServletContext().getRealPath("/UserImages/") + fileName;

                                // Delete old picture
                                if (oldPicture != null && !oldPicture.isEmpty()) {
                                    String deleteFile = oldPicture.substring(oldPicture.lastIndexOf("/") + 1);
                                    try {
                                        Files.deleteIfExists(Paths.get(getServletContext().getRealPath("/UserImages/") + deleteFile));
                                    } catch (Exception e) {
                                        // Ignore if file doesn't exist
                                    }
                                }

                                // Save new picture
                                filePart.write(savePath);
                                pictureUrl = "UserImages/" + fileName;
                            }
                            */

                            if (loggedUser.getPermission() == 1 && permissionStr != null) {
                                oldPermission = permissionStr;
                            }

                            int permission = Integer.parseInt(oldPermission);
                            int result = updatedUser.update(userId, username, password, firstname, lastname,
                                    pictureUrl, email, website, permission, pmActive);

                            if (result == 0) {
                                errorMessage = "ثبت داده با موفقیت انجام شد";
                            } else {
                                errorMessage = "اشکال در ثبت داده";
                            }
                        } else {
                            switch (isValid) {
                                case 1:
                                    errorMessage = "ایمیل وارد شده تکراری می باشد";
                                    break;
                                case 2:
                                    errorMessage = "نام کاربری وارد شده تکراری است";
                                    break;
                                case 3:
                                    errorMessage = "فرمت ایمیل وارد شده نادرست می باشد";
                                    break;
                                case 4:
                                    errorMessage = "فرمت وب سایت وارد شده نادرست می باشد";
                                    break;
                            }
                        }
                    } else {
                        errorMessage = "شما مجوز دسترسی به این صفحه را ندارید";
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                errorMessage = "خطا در پردازش درخواست";
            }
        } else {
            errorMessage = "شما هنوز وارد سایت نشده اید";
        }

        request.setAttribute("errorMessage", errorMessage);

        // Redirect back to GET to refresh the page
        response.sendRedirect(request.getContextPath() + "/admin/edit-users" +
                (request.getParameter("ID") != null ? "?ID=" + request.getParameter("ID") : ""));
    }
}
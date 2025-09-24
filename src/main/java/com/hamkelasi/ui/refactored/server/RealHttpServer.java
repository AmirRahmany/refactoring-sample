package com.hamkelasi.ui.refactored.server;

import jakarta.servlet.http.HttpServlet;

public class RealHttpServer implements Server {
    private HttpServlet httpServlet;

    @Override
    public String getRealPath(String path) {
        return httpServlet.getServletContext().getRealPath(path);
    }
}

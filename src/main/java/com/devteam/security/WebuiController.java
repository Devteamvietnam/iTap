package com.devteam.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.devteam.util.io.IOUtil;
import org.springframework.stereotype.Controller;

@Controller
public class WebuiController {

    public void appPage(HttpServletRequest req, HttpServletResponse response) throws Exception {
        process(req, response);
    }

    void process(HttpServletRequest req, HttpServletResponse response) {
        try {
            response.setContentType("text/html");
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            String content = IOUtil.getResourceAsString("public/**", "UTF-8");
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

package com.devteam.digital;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.devteam.digital.util.IOUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WebuiController {
    @GetMapping(value = { "/", "/app", "/app/ws:{id}/**", "/login" })
    public void appPage(HttpServletRequest req, HttpServletResponse response) throws Exception {
        process(req, response);
    }

    void process(HttpServletRequest req, HttpServletResponse response) {
        try {
            response.setContentType("text/html");
            String content = IOUtil.getResourceAsString("public/index.html", "UTF-8");
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

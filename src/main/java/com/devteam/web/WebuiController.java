package com.devteam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.devteam.util.dataformat.DataSerializer;
import com.devteam.util.io.IOUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebuiController {
    static class Config {
        String build = "latest";
    }

    @GetMapping( value= { "app/config.js" } )
    public void appConfig(HttpServletRequest req, HttpServletResponse response) throws Exception {
        response.setContentType("application/javascript");
        String content = "var CONFIG = " + DataSerializer.JSON.toString(new Config());
        response.getWriter().write(content);
        response.getWriter().flush();
    }

    @GetMapping(
            value= {
                    "/", "/app", "/app/ws:{id}/**", "/login/app"
            }
    )

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

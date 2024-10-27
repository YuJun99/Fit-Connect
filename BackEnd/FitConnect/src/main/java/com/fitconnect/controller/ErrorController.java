package com.fitconnect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    // 모든 에러 요청을 index.html로 포워드
    @RequestMapping("/error")
    public String handleError() {
        return "forward:/index.html";
    }
}
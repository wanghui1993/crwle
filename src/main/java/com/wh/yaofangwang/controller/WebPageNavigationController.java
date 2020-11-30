package com.wh.yaofangwang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName WebPageNavigationController
 * @Desc
 * @Author wh
 * @Date 2020/10/15
 */
@Controller
public class WebPageNavigationController {

    @GetMapping("home")
    public String index() {
        return "index";
    }

    @GetMapping("pageTest")
    public String pageTest() {
        return "pageTest";
    }
}

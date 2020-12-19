package com.hikvision.fireprotection.hikalarm.module.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TODO
 *
 * @author wangjinchang5
 * @date 2020/12/19 17:54
 * @since TODO
 */
@Controller
public class HomeController {

    @GetMapping("/home")
    public String goHome() {
        return "static/index.html";
    }
}

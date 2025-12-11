package com.shengxuan.speed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/admin/user.html")
    public String user(){
        return "admin/user";
    }

    @GetMapping("/admin/server.html")
    public String server(){
        return "admin/server";
    }


    @GetMapping("/admin/device_manage.html")
    public String user_role(){
        return "admin/device_manage";
    }

    @GetMapping("/admin/logger.html")
    public String logger(){
        return "admin/logger";
    }

    @GetMapping("/admin/apply.html")
    public String apply(){
        return "admin/apply";
    }

    @GetMapping("/admin/pushAlarm.html")
    public String pushAlarm(){
       return "admin/pushAlarm";
    }

    @GetMapping("/admin/update_user_password.html")
    public String updateUserPassword(){
        return "admin/update_user_password";
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/admin/device.html")
    public ModelAndView device(@RequestParam("sessionId") String sessionId){
        ModelAndView modelAndView = new ModelAndView("admin/device");
        modelAndView.addObject("sessionId",sessionId);
        return modelAndView;
    }

    @GetMapping("/index")
    public String goHome(){
        return "index";
    }

    @GetMapping("/403")
    public String accessDenied(){
        return "403";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "session",required = false) String session, Model model){
        if("expired".equals(session)){
            model.addAttribute("message","登陆超时，请重新登录");
        }
        return "login";
    }

    @GetMapping("/admin/home.html")
    public String getMap(){
        return "admin/home";
    }

}

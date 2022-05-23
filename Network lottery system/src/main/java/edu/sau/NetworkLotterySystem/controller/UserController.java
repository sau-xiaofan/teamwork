package edu.sau.NetworkLotterySystem.controller;

import edu.sau.NetworkLotterySystem.entity.PageData;
import edu.sau.NetworkLotterySystem.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;


@Api(tags = "用户接口")
@Controller
@RequestMapping("user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @ApiOperation(value="添加用户")
    @PostMapping(value = "/saveUser")
    @ResponseBody
    public Object saveUser(@RequestBody PageData pd) throws Exception {
        return userService.saveUser(pd);
    }

    @ApiOperation("获取图灵验证码")
    @GetMapping(value = "/captcha")
    @ResponseBody
    public void getCaptcha(HttpServletResponse response,
                           @RequestParam("captcha_key") String captchaKey) throws IOException, FontFormatException {
            userService.createCaptcha(response, captchaKey);
    }

    @ApiOperation("发送手机验证码")
    @PostMapping(value = "/sendMassage")
    @ResponseBody
    public Object sendMassage(@RequestBody PageData pd) throws Exception {
        return userService.sendMassage(pd);
    }

    @ApiOperation("注册")
    @PostMapping(value = "/register")
    @ResponseBody
    public Object register(@RequestBody PageData pd) throws Exception {
        return userService.register(pd);
    }

    @ApiOperation("登录")
    @PostMapping(value = "/login")
    @ResponseBody
    public Object login(@RequestBody PageData pd) throws Exception {
        return userService.login(pd);
    }

}

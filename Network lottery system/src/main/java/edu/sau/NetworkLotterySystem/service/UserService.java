package edu.sau.NetworkLotterySystem.service;


import edu.sau.NetworkLotterySystem.entity.PageData;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

public interface UserService {
    Object saveUser(PageData pd);

    void createCaptcha(HttpServletResponse response, String captchaKey) throws IOException, FontFormatException;

    Object sendMassage(PageData pd);

    Object register(PageData pd);
}

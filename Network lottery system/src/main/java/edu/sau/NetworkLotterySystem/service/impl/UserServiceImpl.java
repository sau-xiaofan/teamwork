package edu.sau.NetworkLotterySystem.service.impl;

import com.baomidou.mybatisplus.extension.api.R;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import edu.sau.NetworkLotterySystem.config.TxSendSmsUtil;
import edu.sau.NetworkLotterySystem.entity.PageData;
import edu.sau.NetworkLotterySystem.mapper.UserMapper;
import edu.sau.NetworkLotterySystem.service.UserService;
import edu.sau.NetworkLotterySystem.util.MD5;
import edu.sau.NetworkLotterySystem.util.Tools;
import edu.sau.NetworkLotterySystem.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 验证码类型数组
     */
    public static final int[] captcha_font_type = {Captcha.FONT_1
            , Captcha.FONT_2
            , Captcha.FONT_3
            , Captcha.FONT_4
            , Captcha.FONT_5
            , Captcha.FONT_6
            , Captcha.FONT_7
            , Captcha.FONT_8
            , Captcha.FONT_9
            , Captcha.FONT_10};


    public String get32UUID(){
        return UuidUtil.get32UUID();
    }

    @Override
    public Object saveUser(PageData pd) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("result", "success");
        pd.put("user_id", this.get32UUID());
        pd.put("password", MD5.md5(pd.getString("password")));
        if(Tools.isEmpty(pd.getString("username"))) {
            pd.put("username", "");
        }

        return pd.getString("username");
    }

    public int sendSms(String phone) {
        String[] phoneNumbers = {"+86" + phone};
        log.debug(phone);
        //生成随机验证码
        final String code = Integer.toString(Tools.getRandomNum4());
        //加入数组
        log.debug(code);
        String[] templateParams = {code};
        try {
            final String sendSmsResponse = TxSendSmsUtil.txSendSms(phoneNumbers, templateParams);
            log.info("短信发送成功：{}", sendSmsResponse);
            //加入缓存
            redisTemplate.opsForValue().set("key_phone_content_" + phone, code, 5, TimeUnit.MINUTES);
            return 1;
        } catch (Exception e) {
            log.error("发送失败，或者剩余短信数量不足", e);
            return 2;
        }
    }


    @Override
    public void createCaptcha(HttpServletResponse response, String captchaKey) throws IOException, FontFormatException {
        if (Tools.isEmpty(captchaKey) || captchaKey.length() < 13) {
            return;
        }
        long key;
        try {
            key = Long.parseLong(captchaKey);
        } catch (Exception e) {
            return;
        }
        // 可以用了
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 设置图灵验证码的类型（多个类型）
        Random random = new Random();
        int captchaType = random.nextInt(3);
        Captcha targetCaptcha;
        int width = 120;
        int height = 40;

        if (captchaType == 0) {
            // 三个参数分别为宽、高、位数
            targetCaptcha = new SpecCaptcha(width, height, 4);
        } else if (captchaType == 1) {
            // gif 类型
            targetCaptcha = new GifCaptcha(width, height);
        } else {
            // 算术类型,几位数运算，默认是两位
            targetCaptcha = new ArithmeticCaptcha(width, height);
            targetCaptcha.setLen(2);
        }

        // 设置不同的类型图灵验证码
        int index = random.nextInt(captcha_font_type.length);
        // 设置字体，上面已自定义为一个数组
        targetCaptcha.setFont(captcha_font_type[index]);
        // 设置类型，纯数字、纯字母、字母数字混合
        targetCaptcha.setCharType(Captcha.TYPE_DEFAULT);

        // 获取验证码内容
        String content = targetCaptcha.text().toLowerCase();

        // 保存到 redis, 有效时长 10 分钟
        redisTemplate.opsForValue().set("key_captcha_content_" + key, content, 5, TimeUnit.MINUTES);

        // 输出图片流
        targetCaptcha.out(response.getOutputStream());
    }

    @Override
    public Object sendMassage(PageData pd) {
        HashMap<String, Object> map = new HashMap<>();
        String phone = pd.getString("phone").trim();
        String phoneCity = pd.getString("phoneCity").trim();
        int num = userMapper.findNumByPhone(phone);
        if(num == 0) {
            if (!phoneCity.equals("中国")) {
                map.put("result", "error");
                map.put("message", "该地区暂不支持注册账号");
                return map;
            } else {
                if (sendSms(phone) == 1) {
                    map.put("result", "success");
                    return map;
                } else {
                    map.put("result", "error");
                    map.put("message", "验证码未发送成功");
                    return map;
                }
            }
        } else {
            map.put("result", "error");
            map.put("message", "手机号已被注册");
            return map;
        }
    }

    @Override
    public Object register(PageData pd) {
        HashMap<String, Object> map = new HashMap<>();
        String phone = pd.getString("phone").trim();
        String password = pd.getString("password").trim();
        String code = pd.getString("code").trim();
        int num = userMapper.findNumByPhone(phone);
        if(num == 0) {
            pd.put("password", MD5.md5(password));
            String redisVerifyCode = (String) redisTemplate.opsForValue().get("key_phone_content_" + phone);
            if(redisVerifyCode.equals(code)) {
                map.put("result", "success");
                pd.put("userId", this.get32UUID());
                pd.put("status", 0);
                userMapper.insertUserByPhone(pd);
                return map;
            } else {
                map.put("result", "error");
                map.put("message", "验证码错误");
                return map;
            }
        } else {
            map.put("result", "error");
            map.put("message", "该手机号已被注册");
            return map;
        }
    }
}

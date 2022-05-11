import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.yunhua.Application;
import com.yunhua.config.TaskThreadPoolConfig;
import com.yunhua.domain.User;
import com.yunhua.mapper.MenuMapper;
import com.yunhua.mapper.UserMapper;
import com.yunhua.service.UserService;
import com.yunhua.service.impl.UserServiceImpl;
import com.yunhua.sms.service.AliyunSmsSenderService;
import com.yunhua.utils.RedisCache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @version V1.0
 * @program: CHK
 * @description: 测试类
 * @author: Mr.Zhang
 * @create: 2022-05-09 14:35
 **/
@SpringBootTest(classes = Application.class)
public class MapperTest {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AliyunSmsSenderService aliyunSmsSenderServiceImpl;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskThreadPoolConfig taskThreadPoolConfig;

    @Test
    public void test(){
        System.out.println(menuMapper.selectPermsByUserId(1L));
    }

    @Test
    public void sendSms(){

//        Map<String, String> map = new HashMap<>();
//        map.put("code", "123456");
//        SendSmsResponse sendSmsResponse = aliyunSmsSenderServiceImpl.sendSms("15298376155",
//                JSON.toJSONString(map),
//                "SMS_154950909");
//        System.out.println(JSON.toJSONString(sendSmsResponse));

//        redisCache.setCacheObject("SMS:15298376155","123456",60, TimeUnit.SECONDS);

//        System.out.println(userMapper.findUserByMobile("15298376155"));
        for (int i = 0; i<=11;i++) {
            User user = new User();
            user.setNickName("车管家_6155");
            user.setPwd(passwordEncoder.encode("15298376155"));
            user.setMobile("15298376155");
            user.setDelFlag(0);
            userService.insertUser(user);
        }

    }

    @Test
    public void testThreadPool(){
        System.out.println(taskThreadPoolConfig.getMaxPoolSize());
        userService.findUserByMobile("15298376155");
    }
}

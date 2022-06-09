import com.yunhua.UserApplication;
import com.yunhua.config.TaskThreadPoolConfig;
import com.yunhua.dao.CarDao;
import com.yunhua.domain.User;
import com.yunhua.mapper.CarMapper;
import com.yunhua.mapper.MenuMapper;
import com.yunhua.mapper.UserMapper;
import com.yunhua.order.OrderThreadPoolManager;
import com.yunhua.service.UserService;
import com.yunhua.sms.service.AliyunSmsSenderService;
import com.yunhua.utils.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;


/**
 * @version V1.0
 * @program: CHK
 * @description: 测试类
 * @author: Mr.Zhang
 * @create: 2022-05-09 14:35
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class MapperTest {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private AliyunSmsSenderService aliyunSmsSenderServiceImpl;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskThreadPoolConfig taskThreadPoolConfig;

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private CarDao carDao;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private OrderThreadPoolManager orderThreadPoolManager;


    @Test
    public void test(){
        System.out.println(menuMapper.selectPermsByUserId(1L));
    }

    @Test
    public void sendSms(){

        // 获取配置的数据源
        User user = new User();
        user.setNickName("车管家_9999");
        user.setPwd("aaaaa");
        user.setMobile("15298376155");
        user.setDelFlag(0);
        userService.insertUser(user);

    }

    @Test
    public void testThreadPool() throws SQLException, NoSuchFieldException {
        System.out.println(userService.findUserByMobile("15298379999"));
//        List<User> all = userMapper.findAll();
//        System.out.println(carMapper.findAllCarInfoByUserId("11"));
//        System.out.println(carDao.findAllCarInfoByUserId("11"));

//        for(int i = 0 ; i<100; i++) {
//            String orderNo = System.currentTimeMillis() + UUID.randomUUID().toString();
//            orderThreadPoolManager.addOrders(orderNo);
//        }



    }
}

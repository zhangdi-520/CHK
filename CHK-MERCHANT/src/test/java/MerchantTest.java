import com.yunhua.MerchantApplication;
import com.yunhua.domain.User;
import com.yunhua.feign.UserFeignService;
import com.yunhua.service.ChkMerchantEmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @version V1.0
 * @program: CHK
 * @description: 测试类
 * @author: Mr.Zhang
 * @create: 2022-05-09 14:35
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MerchantApplication.class)
public class MerchantTest {

    @Autowired
    private ChkMerchantEmployeeService chkMerchantEmployeeService;


    @Test
    public void sendSms(){

        // 获取配置的数据源
       chkMerchantEmployeeService.deleteMerchantEmployeeByEmployeeId(1l,1l);

    }

}

package cn.ipanel.authorization.demo;

import cn.ipanel.authorization.demo.task.AsyncTask;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhaolei
 * Create: 2018/9/18 09:22
 * Modified By:
 * Description:
 */
@Component
@Order(value = 1)
public class StartUpRunner implements CommandLineRunner {

    @Resource
    private AsyncTask asyncTask;

    @Override
    public void run(String... strings) throws Exception {
        asyncTask.initPcManagerLogin();
    }
}


package cn.ipanel.authorization.demo.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author zhaolei
 * Create: 2018/10/30 17:56
 * Modified By:
 * Description:
 */
@Component
public class QueuesTask {
    private static Logger logger = LoggerFactory.getLogger(QueuesTask.class);

    @Async("myAsync")
    public void createQueue(){

    }
}

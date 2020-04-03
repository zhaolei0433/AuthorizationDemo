package cn.ipanel.authorization.demo.task;

import cn.ipanel.authorization.demo.service.QueueProduceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhaolei
 * Create: 2018/10/30 17:56
 * Modified By:
 * Description:
 */
@Component
public class QueuesTask {
    private static Logger logger = LoggerFactory.getLogger(QueuesTask.class);
    @Resource
    private QueueProduceService queueProduceService;

    @Async("myAsync")
    public void createQueueByManageLogin(String queueName) {
        queueProduceService.declareAndBindingQueue(queueName);
    }
}

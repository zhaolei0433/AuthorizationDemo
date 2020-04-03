package cn.ipanel.authorization.demo.service;

/**
 * @author zhaolei
 * Create: 2018/10/31 09:37
 * Modified By:
 * Description:
 */
public interface QueueProduceService {
    /**
     * 管理员登录之后声明一个独立队列
     *
     * @param queueName 队列名称
     */
    void declareAndBindingQueue(String queueName);

    /**
     * 管理员退出或异常登录，删除当前队列
     *
     * @param queueName 队列名
     */
    void deleteQueue(String queueName);

}

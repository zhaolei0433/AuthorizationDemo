package cn.ipanel.authorization.demo.service.impl;

import cn.ipanel.authorization.demo.global.SystemDefines;
import cn.ipanel.authorization.demo.service.QueueProduceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaolei
 * Create: 2018/10/31 09:37
 * Modified By:
 * Description:
 */
@Service
public class QueueProduceServiceImpl implements QueueProduceService, RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private static Logger logger = LoggerFactory.getLogger(QueueProduceServiceImpl.class);
    private Map<String, Object> arguments = new HashMap<>();
    private RabbitAdmin rabbitAdmin;
    private final DirectExchange alarmCallExchange;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public QueueProduceServiceImpl(RabbitTemplate rabbitTemplate, DirectExchange alarmCallExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnCallback(this);
        this.rabbitTemplate.setMandatory(true);
        this.rabbitAdmin = new RabbitAdmin(rabbitTemplate.getConnectionFactory());
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        this.alarmCallExchange = alarmCallExchange;
    }

    @Override
    public void declareAndBindingQueue(String queueName) {
        // 消息存活时间
        arguments.put("x-message-ttl", SystemDefines.ALARM_CALL_DEFAULT_TTL * 1000);
        // 队列超过多久没访问失效
        arguments.put("x-expires", SystemDefines.ALARM_CALL_QUEUE_DEFAULT_EXPIRES * 1000);
        Queue queue = new Queue(queueName, true, false, false, arguments);
        this.rabbitAdmin.declareQueue(queue);
        this.rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(alarmCallExchange).with(queueName));
    }

    @Override
    public void deleteQueue(String queueName) {

    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }

    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {

    }
}

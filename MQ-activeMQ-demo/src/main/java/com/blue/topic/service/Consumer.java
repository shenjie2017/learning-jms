package com.blue.topic.service;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author shenjie
 * @version v1.0
 * @Description
 * @Date: Create in 15:55 2017/12/28
 * @Modifide By:
 **/

//      ┏┛ ┻━━━━━┛ ┻┓
//      ┃　　　　　　 ┃
//      ┃　　　━　　　┃
//      ┃　┳┛　  ┗┳　┃
//      ┃　　　　　　 ┃
//      ┃　　　┻　　　┃
//      ┃　　　　　　 ┃
//      ┗━┓　　　┏━━━┛
//        ┃　　　┃   神兽保佑
//        ┃　　　┃   代码无BUG！
//        ┃　　　┗━━━━━━━━━┓
//        ┃　　　　　　　    ┣┓
//        ┃　　　　         ┏┛
//        ┗━┓ ┓ ┏━━━┳ ┓ ┏━┛
//          ┃ ┫ ┫   ┃ ┫ ┫
//          ┗━┻━┛   ┗━┻━┛

public class Consumer extends Thread implements MessageListener {

    private ConnectionFactory connectionFactory;
    private Connection connection = null;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;
    private String topic = "topic-01";

    public void init(){
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic(topic);
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
            connection.start();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        init();
    }

    private int i = 0;

    public void onMessage(Message message) {
        try {
            TextMessage msg = (TextMessage) message;
            i++;
            System.out.println(Thread.currentThread().getName() + "   收到消息" + i + ":" + msg.getText());
        }catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

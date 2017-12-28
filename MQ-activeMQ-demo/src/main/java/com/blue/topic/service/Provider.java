package com.blue.topic.service;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Random;

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

public class Provider extends Thread {
    // Provider 的连接
    // ConnectionFactory ：连接工厂，JMS 用它创建连接
    private ConnectionFactory connectionFactory;
    // Connection ：JMS 客户端到JMS
    private Connection connection = null;
    // Session： 一个发送或接收消息的线程
    private Session session;
    // Destination ：消息的目的地;消息发送给谁.
    private Destination destination;
    // MessageProducer：消息发送者
    private MessageProducer producer;
    private String topic = "topic-01";

    public void init(){
        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        /*
         * connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD, "tcp://192.168.0.104:61616");
         */
         connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
        ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
        //打印出用户和密码
        System.out.println("ActiveMQConnection.DEFAULT_USER:" + ActiveMQConnection.DEFAULT_USER + ",ActiveMQConnection.DEFAULT_PASSWORD:" + ActiveMQConnection.DEFAULT_PASSWORD);
        try { // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
//            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic(topic);
            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 构造消息，此处写死，项目就是参数，或者方法获取
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() throws Exception {
        connection.start();

        for (int i = 0; i <= Long.MAX_VALUE; i++) {
            Thread.sleep(new Random().nextInt(10)*200);
            String sendMsg = "ActiveMq 发送的消息" + i;
            TextMessage message = session.createTextMessage(sendMsg);
            // 发送消息到目的地方
            System.out.println(Thread.currentThread().getName() + " 发送消息：" + sendMsg);
            producer.send(message);
            session.commit();
        }

        connection.close();
    }

    @Override
    public void run() {
        init();
        try {
            sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

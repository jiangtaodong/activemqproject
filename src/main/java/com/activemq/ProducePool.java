package com.activemq;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ProducePool {
	
	private String user = ActiveMQConnection.DEFAULT_USER;
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;
//    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private String url = "tcp://192.168.127.3:61616";
    private String subject = "mytopic";
    private Destination destination = null;
    private Connection connection = null;
    private Session session = null;
    private MessageProducer messageProducer = null;
    MessageProducer producer = null;

    /**
     * 初始化
     * @throws JMSException
     */
    public void initialize() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(user, password, url);
        connection = activeMQConnectionFactory.createConnection();
        session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        destination = session.createTopic(subject);
        producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }

    /**
     * 发送消息
     * @param message
     * @throws JMSException
     */
    public void produceMessage(String message) throws JMSException {
        initialize();
        TextMessage textMessage = session.createTextMessage(message);
        connection.start();
        System.out.println("Producer -> Send Message:"+message);
        producer.send(textMessage);
        System.out.println("Producer -> Send Message complete!");
    }

    public void close() throws JMSException {
        System.out.println("Producer -> close connection!");
        if(producer != null){
            producer.close();
        }
        if(session != null){
            session.close();
        }
        if(connection != null){
            connection.close();
        }
    }

}

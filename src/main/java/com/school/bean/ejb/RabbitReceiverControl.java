package com.school.bean.ejb;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Class listen messages from MQ server
 */
@Startup
@Singleton
public class RabbitReceiverControl {

    private final static String QUEUE_NAME = "queue2";
    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Channel channel = null;

    private static final Logger LOG = Logger.getLogger(RabbitReceiverControl.class);

    @EJB
    TariffsInfo tariffsInfo;

    /**
     * Create connection to MQ server and catch new messages
     * @throws IOException
     * @throws TimeoutException
     */
    @PostConstruct
    void init() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        LOG.setLevel(Level.INFO);
        LOG.info(" [*] Waiting for messages.");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                LOG.info(" [x] Received '" + message + "'");
                tariffsInfo.updateInfo();
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }

    @PreDestroy
    private void close() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}


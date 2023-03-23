package co.za.flash.ms.georep.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public boolean sendMessageToRabbit(String payloadJson) {
        boolean isSuccess = true;
        try {
            Message payload = MessageBuilder.withBody(payloadJson.getBytes())
                    .build();
            rabbitTemplate.convertAndSend(exchange, routingkey ,payload);
        } catch (AmqpException amqpException){
            LOGGER.error(amqpException.getLocalizedMessage());
            isSuccess = false;
        }
        return isSuccess;
    }
}

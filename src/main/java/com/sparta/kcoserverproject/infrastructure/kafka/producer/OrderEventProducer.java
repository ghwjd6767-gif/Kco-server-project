package com.sparta.kcoserverproject.infrastructure.kafka.producer;

import com.sparta.kcoserverproject.infrastructure.kafka.event.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private static final String TOPIC = "order.completed";

    private final KafkaTemplate<String, OrderCompletedEvent> kafkaTemplate;

    public void send(OrderCompletedEvent event) {
        kafkaTemplate.send(TOPIC, String.valueOf(event.orderId()),event);
    }
}

package com.sparta.kcoserverproject.infrastructure.kafka.consumer;

import com.sparta.kcoserverproject.external.dataplatform.DataPlatformClient;
import com.sparta.kcoserverproject.external.dataplatform.OrderEventRequest;
import com.sparta.kcoserverproject.infrastructure.kafka.event.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final DataPlatformClient dataPlatformClient;

    @KafkaListener(
            topics = "order.completed",
            groupId = "kco-order-group")
    public void consume(OrderCompletedEvent event) {
        log.info("주문 완료 이벤트 수신: {}", event);

        dataPlatformClient.send(new OrderEventRequest(
                event.userId(),
                event.productId(),
                event.paymentAmount()
        ));
    }
}

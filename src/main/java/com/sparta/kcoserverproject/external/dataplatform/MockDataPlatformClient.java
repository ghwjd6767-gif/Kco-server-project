package com.sparta.kcoserverproject.external.dataplatform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockDataPlatformClient implements DataPlatformClient {

    @Override
    public void send(OrderEventRequest request) {
        log.info("데이터 플랫폼 전송 완료: {}", request);
    }
}

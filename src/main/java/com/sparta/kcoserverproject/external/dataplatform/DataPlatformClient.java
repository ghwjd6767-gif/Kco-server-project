package com.sparta.kcoserverproject.external.dataplatform;

public interface DataPlatformClient {

    void send(OrderEventRequest request);
}

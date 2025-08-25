package com.icar.platform.api.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MercadoPagoNotificationDTO {

    private long id;
    private boolean liveMode;
    private String type;
    private String dateCreated;
    private long userId;
    private String apiVersion;
    private String action;
    private Data data;

    @Getter
    @Setter
    public static class Data {
        private String id;
    }
}
package com.me.injin.firstcomeevent.constant;

import lombok.Getter;

@Getter
public enum EventType {
    POINT_100_COUPON("포인트 쿠폰");

    private String type;

    EventType(String type) {
        this.type = type;
    }
}

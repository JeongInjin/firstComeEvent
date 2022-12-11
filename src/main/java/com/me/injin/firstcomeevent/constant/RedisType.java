package com.me.injin.firstcomeevent.constant;

public enum RedisType {
    POINT_100_COUPON_CONSECUTIVE_DATES("포인트 쿠폰 이벤트 연속날짜 확인용");

    private String type;

    RedisType(String type) {
        this.type = type;
    }
}

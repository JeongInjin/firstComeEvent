package com.me.injin.firstcomeevent.event.domain;

import com.me.injin.firstcomeevent.constant.EventType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Event {

    private static int couponQuantity = 10; // 지급 수량
    private EventType eventType;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public synchronized void couponQuantityMinus() {
        this.couponQuantity--;
    }

    // 지급완료 true
    public boolean isEventOver() {
        return this.couponQuantity <= 0;
    }

    public int getCouponQuantity() {
        return this.couponQuantity;
    }

    public void couponQuantityInit() {
        couponQuantity = 10;
    }
}

package com.me.injin.firstcomeevent.event.domain;

import com.me.injin.firstcomeevent.constant.EventType;
import com.me.injin.firstcomeevent.event.repository.CouponPaymentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Getter
public class Coupon {

    private EventType eventType;
    private int couponPoint;
    private String code;

    private boolean duplicatePayment = false;

    ConcurrentHashMap<String, HashMap<String, Integer>> paymentMap = new ConcurrentHashMap<>();
    private static HashMap<String, Integer> customerMap = new HashMap<>();

//    private boolean todayPaymentComplete = false;

    public Coupon(EventType eventType, String date, String customer) {
        this.eventType = eventType;
        this.couponPoint = 100;
        this.code = UUID.randomUUID().toString(); //쿠폰 발급 고유번호
//        this.todayPaymentComplete = this.isDuplicatePayment(date, customer);
    }

    /**
     * 해당 함수는 중복 지급 체크를 수행합니다.
     * redis sort set 에 add 할때 중복이 들어가지 않음을 확인하였으나(RedisUtilTest 참조)
     * 다른 방법 또한 염두해 두어 한번 더 체크 로직을 수행합니다.
     * 수정: 메모리형식에서, db 형식으로 인하여 주석처리.
     * @return true: 지급한적 있음, false: 첫 지급
     */
//    public boolean isDuplicatePayment(String date, String customer) {
//        boolean result = false;
//        if (paymentMap.containsKey(date)) { // 오늘날짜로 데이터가 있다면
//            HashMap<String, Integer> innerMap = paymentMap.getOrDefault(date, null);
//            if (innerMap == null) { // 오늘날짜로 고객에게 지급한적이 없다면
//                HashMap<String, Integer> todayPaymentMap = paymentMap.get(date);
//                todayPaymentMap.put(customer, 1);
//                paymentMap.put(date, todayPaymentMap);
//                Integer getCustomer = innerMap.getOrDefault(customer, 1);
//            } else {
//                result = true;
//            }
//            return result;
//        }
//        //오늘날짜로 최초 지급
//        paymentMap.put(date, new HashMap<>(new HashMap<>() {{
//            put(customer, 1);
//        }}));
//        return result;
//    }
}

package com.me.injin.firstcomeevent.event.service;

import com.me.injin.firstcomeevent.common.util.DateUtil;
import com.me.injin.firstcomeevent.common.util.RedisUtil;
import com.me.injin.firstcomeevent.constant.EventType;
import com.me.injin.firstcomeevent.event.domain.Coupon;
import com.me.injin.firstcomeevent.event.domain.Event;
import com.me.injin.firstcomeevent.event.domain.Point;
import com.me.injin.firstcomeevent.event.entity.ConsecutiveDates;
import com.me.injin.firstcomeevent.event.entity.CouponPayment;
import com.me.injin.firstcomeevent.event.repository.ConsecutiveDatesRepository;
import com.me.injin.firstcomeevent.event.repository.CouponPaymentRepository;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.me.injin.firstcomeevent.constant.EventType.POINT_100_COUPON;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirstComeService {

    private final RedisUtil redisUtil;
    private final DateUtil dateUtil;

    private final CouponPaymentRepository couponPaymentRepository;

    private final ConsecutiveDatesRepository consecutiveDatesRepository;

    private Event event;

    public void setEvent(EventType eventType) {
        this.event = new Event(eventType);
    }

    public void addCustomerQueue(EventType eventType, String id, String date) {
        if (isEventOver()) {
            log.info("이벤트가 종료되었습니다. 감사합니다.");
            return;
        }
        redisUtil.addCustomerQueue(eventType, id, date);
    }

    public void paymentCoupon(EventType eventType, String date) {
        if(StringUtil.isNullOrEmpty(date)) date = DateUtil.getCurrentYyyyMMdd();
        setEvent(eventType);
        Set<Object> customerQueue = this.getPaymentQueue(eventType, date);
        for (Object customer : customerQueue) {
            payment_point(eventType, date, customer.toString());
        }
        //mq 나 소켓통신으로 고객한테 알림전송 또는 10명정도 이령우는 즉시알림.
        // ex) sendCustomerCouponNoti()
    }

    @Transactional
    void payment_point(EventType eventType, String date, String customer) {
        try {
            if (!this.isEventOver()) {
                Coupon coupon = new Coupon(eventType, date, customer);
                CouponPayment customerCouponPayment = couponPaymentRepository.findByCustomerIdAndPaymentDate(customer, date);
                if (customerCouponPayment == null) {
                    //지급, 포인트 확인
                    int point = getPoint(eventType, customer);
                    printPoint(customer, coupon, point);
                    couponQuantityMinus();
                    log.info("남은 쿠폰 수량: " + this.event.getCouponQuantity());
                    couponPaymentSave(eventType, date, customer, point, coupon.getCode());
                } else {
                    log.info("중복으로 인하여 지급하지 않습니다. - [" + customer + "]");
                }
            } else
                log.info("이벤트 지급이 종료되었습니다.");
        } catch (Exception e) {
            log.error("==========================================================");
            log.error("쿠폰 지급에 실패하였습니다.");
            log.error("==========================================================");
            e.printStackTrace();
        } finally {
            customerRedisRemove(eventType, customer, date);
        }
    }

    private void printPoint(String customer, Coupon coupon, int point) {
        log.info("========================================");
        log.info(coupon.getEventType() + " 쿠폰 지급에 성공하였습니다. ID: [" + customer + "] 총 지급 포인트: [" + point + "] code: [" + coupon.getCode() + "]");
        log.info("========================================");
    }

    private void couponPaymentSave(EventType eventType, String date, String customer, int point, String code) {
        CouponPayment entity = new CouponPayment(eventType, date, customer, point, code);
        couponPaymentRepository.save(entity);
    }

    private void customerRedisRemove(EventType eventType, String customer, String date) {
        redisUtil.removeOne(DateUtil.getEventDateKey(eventType, date), customer);
    }

    private void couponQuantityMinus() {
        this.event.couponQuantityMinus();
    }

    public int getPoint(EventType eventType, String customer) {
        Optional<ConsecutiveDates> consecutiveDates = consecutiveDatesRepository.findByEventTypeAndCustomerId(eventType, customer);
        if (consecutiveDates.isEmpty()) {
            ConsecutiveDates entity = new ConsecutiveDates(eventType, customer, 1);
            consecutiveDatesRepository.save(entity);
            return 100;
        }
        int dates = consecutiveDates.get().getConsecutiveDates() + 1;
        if(dates > 10) dates = 1;
        consecutiveDates.get().setConsecutiveDates(dates);
        consecutiveDatesRepository.save(consecutiveDates.get());
        return Point.getPoint(dates);
    }

    private Set<Object> getPaymentQueue(EventType eventType, String date) {
        return redisUtil.getCustomerQueue(eventType, date);
    }

    public void removeAllRedisData(EventType eventType, String date) {
        redisUtil.removeAllEvent(dateUtil.getEventDateKey(eventType, date));
    }

    public boolean isEventOver() {
        return this.event != null? this.event.isEventOver(): false;
    }

    @SneakyThrows
    public String eventNextDayInit(EventType eventType, String preDate) {
        if(StringUtil.isNullOrEmpty(preDate)) preDate = dateUtil.getCurrentYyyyMMdd();
        setEvent(eventType);
        this.event.couponQuantityInit();
        String currentDate = preDate;
        String addDate = dateUtil.getAddDate(currentDate, 1);
        removeAllRedisData(POINT_100_COUPON, currentDate);
        redisUtil.setDate(addDate);
        log.info(currentDate + " => " + addDate + " 로 데이터 초기화 하였습니다.");
        return addDate;
    }

    public List<CouponPayment> getEventList(String date) {
        validDate(date);
        return couponPaymentRepository.findByPaymentDateOrderByCreatedDateAsc(date);
    }

    private String validDate(String date) {
        if(date.isEmpty() || date == null) throw new IllegalArgumentException("date 값 누락");
        date = date.replaceAll("-", "").replaceAll("_", "");
        if(date.length() != 8) throw new IllegalArgumentException("date 값을 확인바랍니다.");
        return date;
    }

    private void validId(String id) {
        if(id.isEmpty() || id == null) throw new IllegalArgumentException("id 값 누락");
    }

    public CouponPayment getEvenPaymentSelectOne(String date, String id) {
        validDate(date);
        validId(id);
        return couponPaymentRepository.findByPaymentDateAndCustomerId(date, id);
    }
}

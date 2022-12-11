package com.me.injin.firstcomeevent.controller.evnet.service;

import com.me.injin.firstcomeevent.common.util.RedisUtil;
import com.me.injin.firstcomeevent.event.domain.Event;
import com.me.injin.firstcomeevent.event.entity.ConsecutiveDates;
import com.me.injin.firstcomeevent.event.entity.CouponPayment;
import com.me.injin.firstcomeevent.event.repository.ConsecutiveDatesRepository;
import com.me.injin.firstcomeevent.event.repository.CouponPaymentRepository;
import com.me.injin.firstcomeevent.event.service.FirstComeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static com.me.injin.firstcomeevent.constant.EventType.POINT_100_COUPON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Slf4j
@SpringBootTest
class FirstComeServiceTest {
    @Autowired
    FirstComeService firstComeService;

    @Autowired
    ConsecutiveDatesRepository consecutiveDatesRepository;

    @Autowired
    CouponPaymentRepository couponPaymentRepository;

    @Autowired
    RedisUtil redisUtil;

    private static final long  CUSTOMERS_COUNT= 15;

    @AfterEach
    public void AfterEach() {
        System.out.println("redis 데이터를 모두 삭제 합니다.");
        firstComeService.removeAllRedisData(POINT_100_COUPON, "");
        consecutiveDatesRepository.deleteAll();
    }

    @Test
    void 정해진_수량만큼_지급하고_종료한다() {
        //givent
        for (int i = 0; i < 30; i++) {
            redisUtil.addCustomerQueue(POINT_100_COUPON, "정인진_" + i, "");
        }
        Event target = new Event(POINT_100_COUPON);
        firstComeService.setEvent(POINT_100_COUPON);

        //when
        firstComeService.paymentCoupon(POINT_100_COUPON, "");
        firstComeService.paymentCoupon(POINT_100_COUPON, "");
        firstComeService.paymentCoupon(POINT_100_COUPON, "");
        firstComeService.paymentCoupon(POINT_100_COUPON, "");
        firstComeService.paymentCoupon(POINT_100_COUPON, "");

        //then
        assertThat(target.getCouponQuantity()).isEqualTo(0);
        assertThat(target.isEventOver()).isTrue();
    }

    @Test
    void 연속일수에_따라_포인트_지급이_달라진다() {
        //given
        List<ConsecutiveDates> data = Arrays.asList(
            new ConsecutiveDates(POINT_100_COUPON, "test_100", 1),
            new ConsecutiveDates(POINT_100_COUPON, "test_400", 2),
            new ConsecutiveDates(POINT_100_COUPON, "test_600", 4),
            new ConsecutiveDates(POINT_100_COUPON, "test_1100", 9)
        );
        consecutiveDatesRepository.saveAll(data);

        //when
        int test_100 = firstComeService.getPoint(POINT_100_COUPON, "test_100");
        int test_400 = firstComeService.getPoint(POINT_100_COUPON, "test_400");
        int test_600 = firstComeService.getPoint(POINT_100_COUPON, "test_600");
        int test_1100 = firstComeService.getPoint(POINT_100_COUPON, "test_1100");

        //then
        assertThat(test_100).isEqualTo(100);
        assertThat(test_400).isEqualTo(400);
        assertThat(test_600).isEqualTo(600);
        assertThat(test_1100).isEqualTo(1100);
    }

    @Test
    void 연속된_10일_이후에는_다시_1일_포인트_100_지급() {
        //given
        ConsecutiveDates data = new ConsecutiveDates(POINT_100_COUPON, "test_100", 10);
        consecutiveDatesRepository.save(data);

        //when
        int test_100 = firstComeService.getPoint(POINT_100_COUPON, "test_100");

        //then
        assertThat(test_100).isEqualTo(100);
    }

    @Test
    void 날짜로_조회합니다() {
        //given
        List<CouponPayment> data = couponPaymentsInit();
        couponPaymentRepository.saveAll(data);

        //when
        List<CouponPayment> target_20211212 = firstComeService.getEventList("20221212");
        List<CouponPayment> target_20211213 = firstComeService.getEventList("20221213");

        //then
        assertThat(target_20211212.size()).isEqualTo(5);
        assertThat(target_20211213.size()).isEqualTo(2);
    }

    @Test
    void 날짜와_고객으로_조회합니다() {
        //given
        List<CouponPayment> data = couponPaymentsInit();
        couponPaymentRepository.saveAll(data);

        //when
        CouponPayment test_1 = firstComeService.getEvenPaymentSelectOne("20221212", "test_1");
        CouponPayment test_2 = firstComeService.getEvenPaymentSelectOne("20221213", "test_2");

        //then
        assertThat(test_1).isNotNull();
        assertThat(test_1.getPoint()).isEqualTo(400);
        assertThat(test_2).isNotNull();
        assertThat(test_2.getPoint()).isEqualTo(100);
    }

    private List<CouponPayment> couponPaymentsInit() {
        List<CouponPayment> data = Arrays.asList(
                new CouponPayment(POINT_100_COUPON, "20221212", "test_1", 400, "code_test_1"),
                new CouponPayment(POINT_100_COUPON, "20221212", "test_2", 100, "code_test_2"),
                new CouponPayment(POINT_100_COUPON, "20221212", "test_3", 400, "code_test_3"),
                new CouponPayment(POINT_100_COUPON, "20221212", "test_4", 100, "code_test_4"),
                new CouponPayment(POINT_100_COUPON, "20221212", "test_5", 600, "code_test_5"),
                new CouponPayment(POINT_100_COUPON, "20221213", "test_1", 100, "code_test_1"),
                new CouponPayment(POINT_100_COUPON, "20221213", "test_2", 100, "code_test_2")

        );
        return data;
    }
}
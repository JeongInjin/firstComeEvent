package com.me.injin.firstcomeevent.event.entity;

import com.me.injin.firstcomeevent.event.repository.CouponPaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.me.injin.firstcomeevent.constant.EventType.POINT_100_COUPON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CouponPaymentTest {

    @Autowired
    EntityManager em;

    @Autowired
    CouponPaymentRepository couponPaymentRepository;

    @AfterEach
    public void AfterEach() {
        couponPaymentRepository.deleteAll();
    }

    @Test
    @DisplayName("entity 와 h2 의 연결을 확인 목적용 테스트 코드.")
    void jpaInsertTest() {
        //given
        CouponPayment couponPayment = new CouponPayment(POINT_100_COUPON, "20221212", "정인진", 100, "testcode");
        couponPaymentRepository.save(couponPayment);

        //when
        Optional<CouponPayment> target = couponPaymentRepository.findById(couponPayment.getId());

        //then
        assertThat(target.isEmpty()).isFalse();
        assertThat(target.get().getCustomerId()).isEqualTo("정인진");
    }
}
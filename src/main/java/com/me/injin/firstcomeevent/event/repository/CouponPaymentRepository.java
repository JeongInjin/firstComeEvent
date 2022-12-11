package com.me.injin.firstcomeevent.event.repository;

import com.me.injin.firstcomeevent.event.domain.Coupon;
import com.me.injin.firstcomeevent.event.entity.CouponPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponPaymentRepository extends JpaRepository<CouponPayment, Long> {

    CouponPayment findByCustomerIdAndPaymentDate(String customerId, String paymentDate);

    List<CouponPayment> findByPaymentDateOrderByCreatedDateAsc(String date);

    CouponPayment findByPaymentDateAndCustomerId(String date, String id);
}
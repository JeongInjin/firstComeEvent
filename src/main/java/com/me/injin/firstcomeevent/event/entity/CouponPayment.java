package com.me.injin.firstcomeevent.event.entity;

import com.me.injin.firstcomeevent.common.entity.JpaBaseEntity;
import com.me.injin.firstcomeevent.constant.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class CouponPayment extends JpaBaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String paymentDate;

    private String customerId;

    private int point;

    private String code;
    @Builder
    public CouponPayment(EventType eventType, String payment_date, String id, int point, String code) {
        this.eventType = eventType;
        this.paymentDate = payment_date;
        this.customerId = id;
        this.point = point;
        this.code = code;
    }
}

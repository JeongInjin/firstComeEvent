package com.me.injin.firstcomeevent.event.entity;

import com.me.injin.firstcomeevent.common.entity.JpaBaseEntity;
import com.me.injin.firstcomeevent.constant.EventType;
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
public class ConsecutiveDates extends JpaBaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String customerId;

    private int ConsecutiveDates;

    public ConsecutiveDates(EventType eventType, String customerId, int consecutiveDates) {
        this.eventType = eventType;
        this.customerId = customerId;
        ConsecutiveDates = consecutiveDates;
    }
}
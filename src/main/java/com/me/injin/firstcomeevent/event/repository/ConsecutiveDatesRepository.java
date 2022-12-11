package com.me.injin.firstcomeevent.event.repository;

import com.me.injin.firstcomeevent.constant.EventType;
import com.me.injin.firstcomeevent.event.entity.ConsecutiveDates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsecutiveDatesRepository extends JpaRepository<ConsecutiveDates, Long> {

    Optional<ConsecutiveDates> findByEventTypeAndCustomerId(EventType eventType, String customer);
}
package com.me.injin.firstcomeevent.schedule;
import com.me.injin.firstcomeevent.event.controller.FirstComeController;
import com.me.injin.firstcomeevent.event.service.FirstComeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import static com.me.injin.firstcomeevent.constant.EventType.POINT_100_COUPON;

@Slf4j
@RequiredArgsConstructor
@Component
public class Schedule {

    private final FirstComeController firstComeController;
    private final FirstComeService firstComeService;

    private static String date = "";

    @Scheduled(fixedDelay = 1000)
    private void runSchedule() {
//        log.info("Run Schedule: " +  System.currentTimeMillis() / 1000);
        if (firstComeService.isEventOver()) {
            date = firstComeService.eventNextDayInit(POINT_100_COUPON, date);
            firstComeController.setIndex();
            firstComeController.setDate(date);
        }
        firstComeService.paymentCoupon(POINT_100_COUPON, date);
    }
}

package com.me.injin.firstcomeevent.event.controller;

import com.me.injin.firstcomeevent.common.util.RedisUtil;
import com.me.injin.firstcomeevent.event.service.FirstComeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.me.injin.firstcomeevent.constant.EventType.POINT_100_COUPON;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class FirstComeController {
    static int INDEX = 1;
    static String date = "";
    private final FirstComeService firstComeService;

    private final RedisUtil redisUtil;

    @GetMapping("/event/coupon")
    public void firstCome() {
        // 파라미터에 이름을 적어서 받을까도 생각했지만, 실제로는 파라미터보다는, util, jwt 토큰으로 인하여 생성이 나은듯하여,
        // 임시로 이름_index 로 생성하여 호출할때마다 추가하고있음.
        firstComeService.addCustomerQueue(POINT_100_COUPON, "정인진_" + INDEX++, date);
    }

    @GetMapping("/event/init")
    public void initRedisData() {
        firstComeService.removeAllRedisData(POINT_100_COUPON, "");
        log.info("저장된 레디스 데이터를 삭제하였습니다.");
        INDEX = 1;
    }

    @GetMapping("/event/list/{yyyymmdd}")
    public Object eventList(@PathVariable("yyyymmdd") String date) {
        return firstComeService.getEventList(date);
    }

    @GetMapping("/event/one/{yyyymmdd}/{id}")
    public Object eventPaymentSelectOne(@PathVariable("yyyymmdd") String date, @PathVariable("id") String id) {
        return firstComeService.getEvenPaymentSelectOne(date, id);
    }

    @GetMapping("/event/nextday")
    public void eventNextDay() {
        INDEX = 1;
        firstComeService.eventNextDayInit(POINT_100_COUPON, "");
    }

    public void setIndex() {
        INDEX = 1;
    }
    public void setDate(String date) {
        this.date = date;
    }
}

package com.me.injin.firstcomeevent.common.util;

import com.me.injin.firstcomeevent.constant.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
public class DateUtil {

    /**
     * yyyyMMdd 의 날짜를 반환 합니다.
     * @return
     */
    public static String getCurrentYyyyMMdd() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        String strToday = sdf.format(calendar.getTime());
//        log.info("날짜생성 완료: " + strToday);
        return strToday;
    }

    /**
     * 날짜_이벤트타입 의 문자열(key)를 반환한다.
     * @param eventType 이벤트타입
     * @param strToday 날짜
     * @return
     */
    public static String getEventDateKey(EventType eventType, String strToday) {
        if(strToday.isBlank() || strToday == null)
            strToday = getCurrentYyyyMMdd();
        return strToday + "_" + eventType.toString();
    }

    /**
     * 문자열 yyyyMMdd 를 받아 amount 만큼 day 를 더하여 반환합니다.
     * @param currentDate target yyyyMMdd
     * @param amount 더할 일수
     * @return 문자열
     * @throws ParseException
     */
    public String getAddDate(String currentDate, int amount) throws ParseException {
        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
        Date formatDate = dtFormat.parse(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(formatDate);
        cal.add(Calendar.DATE, amount);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String strToday = sdf.format(cal.getTime());
        return strToday;
    }
}

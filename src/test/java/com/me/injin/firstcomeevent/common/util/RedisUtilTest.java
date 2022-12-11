package com.me.injin.firstcomeevent.common.util;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Set;

import static com.me.injin.firstcomeevent.constant.EventType.POINT_100_COUPON;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
class RedisUtilTest {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    DateUtil dateUtil;

    @AfterEach
    public void AfterEach() {
        redisUtil.removeAllEvent(dateUtil.getEventDateKey(POINT_100_COUPON, ""));
    }

    @Test
    @DisplayName("중복으로 데이터(ID)가 들어가지 않는다.")
    void do_not_allow_duplication_id() {
        //given when
        duplicatedNameInit();
        Set<Object> target = redisUtil.getZAll(dateUtil.getEventDateKey(POINT_100_COUPON, ""));

        //then
        assertThat(target.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("날짜별로(yyyyMMdd) 데이터가 저장된다")
    void create_new_date_key() throws ParseException {
        //given when
        String currentDate = dateUtil.getCurrentYyyyMMdd();
        String addDate = dateUtil.getAddDate(currentDate, 1);

        duplicatedNameInit();
        addDateRedisSet(addDate);
        Set<Object> target = redisUtil.getZAll(dateUtil.getEventDateKey(POINT_100_COUPON, currentDate));
        Set<Object> target2 = redisUtil.getZAll(dateUtil.getEventDateKey(POINT_100_COUPON, addDate));

        //then
        assertThat(target).isNotNull();
        assertThat(target2).isNotNull();
        redisUtil.removeAllEvent(dateUtil.getEventDateKey(POINT_100_COUPON, addDate));
    }

    private Boolean addDateRedisSet(String addDate) {
        return redisTemplate.opsForZSet().addIfAbsent(dateUtil.getEventDateKey(POINT_100_COUPON, addDate), "정인진_0", 1234);
    }


    private void duplicatedNameInit() {
        redisUtil.addCustomerQueue(POINT_100_COUPON, "정인진_0", "");
        redisUtil.addCustomerQueue(POINT_100_COUPON, "정인진_1", "");
        redisUtil.addCustomerQueue(POINT_100_COUPON, "정인진_0", "");
        redisUtil.addCustomerQueue(POINT_100_COUPON, "정인진_1", "");
    }

    @Test
    void basicRedisSet() {
        boolean target = redisUtil.redisSet("key입니다", "값입니다.");
        assertThat(target).isTrue();
    }

    @Test
    void 레디스_기능_정상작동_확인() {
        LinkedHashMap<String, String> redisMap = new LinkedHashMap<>();
        redisMap.put("key_한글_123", "value_한글_123");
        Gson gson = new Gson();
        String json = gson.toJson(redisMap);
        boolean target = redisUtil.redisJsonSet("key입니다", json);
        assertThat(target).isTrue();
    }
}
package com.me.injin.firstcomeevent.common.util;

import com.me.injin.firstcomeevent.constant.EventType;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final RedisTemplate<String,Object> redisTemplate;
    private final DateUtil dateUtil;
    private static final long END_ALL = -1;
    private static final long START = 0;
    private final long END = 10;

    private static String date = "";

    public static void setDate(String date) {
        RedisUtil.date = date;
    }

    /**
     * redis 에 적재된 sort set 의 데이터를 모두 불러 옵니다.
     * @param key
     * @return
     */
    public Set<Object> getZAll(String key) {
        Set<Object> queue = redisTemplate.opsForZSet().range(key, START, END_ALL);
        return queue;
    }

    /**
     * eventType 의 고객을 추가 합니다.
     * @param eventType : add target
     * @param id : 고객 ID
     * @param date
     */
    public void addCustomerQueue(EventType eventType, String id, String date) {
        if(RedisUtil.date.isEmpty()) RedisUtil.date = dateUtil.getCurrentYyyyMMdd();
        long now = System.currentTimeMillis();
        log.info(date + " => [" + id + " 고객 생성 및 queue 적재 완료: " + now);
        redisTemplate.opsForZSet().addIfAbsent(dateUtil.getEventDateKey(eventType, RedisUtil.date), id, (int) now);
    }

    /**
     * eventType 의 고객을 추가 합니다.
     * @param eventType : target
     * @param date
     * @return
     */
    public Set<Object> getCustomerQueue(EventType eventType, String date) {
        String key = dateUtil.getEventDateKey(eventType, date);
        Set<Object> queue = redisTemplate.opsForZSet().range(key, START, END);
        return queue;
    }

    /**
     * 넘어온 key 와 id 로 특정 데이터만 삭제합니다.
     * @param key
     */
    public void removeOne(String key, String id) {
        redisTemplate.opsForZSet().remove(key, id);
    }

    /**
     * 넘어온 key 의 모든 데이터를 삭제합니다.
     * @param key
     */
    public void removeAllEvent(String key) {
        Set<Object> queue = redisTemplate.opsForZSet().range(key, START, END_ALL);
        for (Object customer : queue) {
            redisTemplate.opsForZSet().remove(key, customer);
//            log.info("삭제: " + customer);
        }
    }

    /**
     * 저장된 value를 String으로 반환한다.
     * @param k : key
     * @return
     */
    public String redisGet(String k) {
        return String.valueOf(redisTemplate.opsForValue().get(k));
    }

    /**
     * key와 value를 저장한다.
     * @param k : key(String)
     * @param v : json
     * @return
     */
    public boolean redisJsonSet(String k, String v) {
        if(StringUtil.isNullOrEmpty(k) || StringUtil.isNullOrEmpty(v)) return false;
        redisTemplate.opsForValue().set(k, v);
        return !StringUtil.isNullOrEmpty(String.valueOf(redisTemplate.opsForValue().get(k)));
    }

    /**
     * key와 value를 저장한다.
     * @param k : key(String)
     * @param v : Value(String)
     * @return
     */
    public boolean redisSet(String k, String v) {
        if(StringUtil.isNullOrEmpty(k) || StringUtil.isNullOrEmpty(v)) return false;
        redisTemplate.opsForValue().set(k, v);
        return !StringUtil.isNullOrEmpty(String.valueOf(redisTemplate.opsForValue().get(k)));
    }
}

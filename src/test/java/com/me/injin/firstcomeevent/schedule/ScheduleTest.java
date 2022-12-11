package com.me.injin.firstcomeevent.schedule;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Component
@EnableAsync
@Slf4j
class ScheduleTest {

    @SpyBean
    private Schedule myTask;

    @Test
    @DisplayName("스케줄러 작동 확인")
    void fixedDelayTest() {
        await()
                .pollDelay(Duration.ofMillis(1000))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    verify(myTask, times(2));
                });
    }
}
package com.me.injin.firstcomeevent.event.domain;

import java.util.Map;

public class Point {
    public static Map<Integer, Integer> pointMap = Map.of(
            1, 100,
            2, 100,
            3, 400,
            4, 100,
            5, 600,
            6, 100,
            7, 100,
            8, 100,
            9, 100,
            10, 1100
    );

    private static int consecutiveEventsDays = 1;

    public static int getPoint(int dates) {
        return pointMap.get(dates);
    }
}

package com.intelligentsports.tcp.service;

import com.google.gson.Gson;
import com.intelligentsports.tcp.domain.HealthMetrics;
import com.intelligentsports.tcp.mapper.HealthMetricsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class HealthMetricsService {

    @Resource
    private HealthMetricsMapper healthMetricsMapper;

    /**
     * 将传入的JSON字符串转换为HealthMetrics对象数组，并插入到数据库中。
     * 此方法首先使用Gson库将JSON字符串解析为HealthMetrics对象数组，然后为每个对象设置当前时间戳，
     * 最后将这些对象以批量插入的方式存储到数据库中。
     *
     * @param msg 包含HealthMetrics数据的JSON字符串。
     */
    public void insert(String msg) {
        // 使用Gson从JSON字符串解析HealthMetrics对象数组
        Gson gson = new Gson();
        HealthMetrics[] healthMetrics = gson.fromJson(msg, HealthMetrics[].class);
        // 将解析出的HealthMetrics对象数组转换为流，为每个对象设置当前时间戳，并转换回List集合
        List<HealthMetrics> list = Arrays.stream(healthMetrics)
                .peek(e -> {
                            e.setTimestamp(System.currentTimeMillis() / 1000L);
                        }
                ).toList();
        // 调用healthMetricsMapper的insertBatch方法，将处理后的HealthMetrics对象列表批量插入数据库
        healthMetricsMapper.insertBatch(list);
    }
}

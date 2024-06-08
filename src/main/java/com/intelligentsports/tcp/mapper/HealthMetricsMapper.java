package com.intelligentsports.tcp.mapper;


import com.intelligentsports.tcp.core.BaseMapperPlus;
import com.intelligentsports.tcp.domain.HealthMetrics;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HealthMetricsMapper extends BaseMapperPlus<HealthMetrics, HealthMetrics> {


}

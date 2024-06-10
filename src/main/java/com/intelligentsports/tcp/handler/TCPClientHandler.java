package com.intelligentsports.tcp.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.google.gson.Gson;
import com.intelligentsports.tcp.domain.Base;
import com.intelligentsports.tcp.domain.HealthMetrics;
import com.intelligentsports.tcp.service.HealthMetricsService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TCPClientHandler extends SimpleChannelInboundHandler<String> {

    private HealthMetricsService healthMetricsService;
    // 登录请求
    private static final String LOGIN_REQUEST = "{\"type\":\"LOGIN\",\"name\":\"fjqm\",\"password\":\"fjqmProd@2024\"}";
    private final Gson gson = new Gson();

    public TCPClientHandler(HealthMetricsService healthMetricsService) {
        this.healthMetricsService = healthMetricsService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 当通道激活时发送登录请求
        ctx.writeAndFlush(LOGIN_REQUEST);
    }

    /**
     * 当从通道读取到消息时执行此方法。
     * 此方法重写了ChannelInboundHandlerAdapter的channelRead0方法，专门处理读取到的字符串消息。
     *
     * @param ctx 通道上下文，用于通道的管理和操作。
     * @param msg 从通道读取到的字符串消息。
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        // 记录接收到的TCP服务器消息。
        log.info("接收到消息: {}", msg);
        try {
            HealthMetrics[] jsonObject = gson.fromJson(msg, HealthMetrics[].class);
            List<HealthMetrics> jsonObjectList = Arrays.asList(jsonObject);
            if (ObjectUtil.isNotNull(jsonObjectList.get(0).getUuid())) {
                log.info("开始获取实时数据");
                healthMetricsService.insert(msg);
            }
        } catch (Exception e) {
            log.error("解析数据失败", e);
        }

    }

    /**
     * 当发生异常时执行此方法。
     * 此方法重写了ChannelInboundHandlerAdapter的exceptionCaught方法，用于处理通道操作过程中发生的异常。
     *
     * @param ctx   通道上下文，用于通道的管理和操作。
     * @param cause 异常原因，记录并关闭通道。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 记录发生的异常并关闭通道。
        log.error("TCPClient Error", cause);
//        ctx.close();
    }
}

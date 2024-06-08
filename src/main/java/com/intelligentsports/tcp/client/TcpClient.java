package com.intelligentsports.tcp.client;

import com.intelligentsports.tcp.handler.TCPClientHandler;
import com.intelligentsports.tcp.service.HealthMetricsService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

//@Component
@Slf4j
public class TcpClient implements CommandLineRunner {

    private static final String SERVER_ADDRESS = "api.atomconnection.com";
    private static final int SERVER_PORT = 8062;
    private Channel channel;
    private EventLoopGroup group;

    @Resource
    private HealthMetricsService healthMetricsService;


    public void close() {
        if (channel != null) {
            channel.close();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    @Override
    public void run(String... args) {
        group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch){
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new StringDecoder());
                    pipeline.addLast(new TCPClientHandler(healthMetricsService));
                }
            });
            ChannelFuture future = bootstrap.connect(SERVER_ADDRESS, SERVER_PORT).sync();
            channel = future.channel();
            log.info("TCPClient Start, Connect host:" + SERVER_ADDRESS + ":" + SERVER_PORT);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("TCPClient Error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}

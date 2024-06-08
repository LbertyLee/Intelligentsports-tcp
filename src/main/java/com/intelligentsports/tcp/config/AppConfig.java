package com.intelligentsports.tcp.config;


import com.intelligentsports.tcp.client.TcpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public TcpClient tcpClient() {
        return new TcpClient();
    }
}

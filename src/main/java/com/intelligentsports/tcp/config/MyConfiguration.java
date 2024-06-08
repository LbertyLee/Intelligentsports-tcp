package com.intelligentsports.tcp.config;


import com.intelligentsports.tcp.client.TcpClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MyConfiguration {

    @Bean
    public ApplicationRunner myApplicationRunner(TcpClient tcpClient) throws IOException {
        return args -> {
            Thread tcpClientThread = new Thread(tcpClient::run);
            tcpClientThread.start();
        };
    }

}

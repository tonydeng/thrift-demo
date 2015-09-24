package com.github.tonydeng.demo.thrift.ping.server;

import com.github.tonydeng.demo.thrift.ping.api.PingPongService;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServlet;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tonydeng on 15/9/24.
 */
@Configuration
@ComponentScan(basePackages = "com.github.tonydeng.demo.thrift.ping")
public class SpringConfiguration {

    @Bean
    public TServlet thriftServlet(PingPongService.Iface pingPongService){
        TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();

        PingPongService.Processor<PingPongService.Iface> pingPongProcessor = new PingPongService.Processor<>(pingPongService);

        multiplexedProcessor.registerProcessor("PingPongService",pingPongProcessor);
        TBinaryProtocol.Factory protocolFactory =  new TBinaryProtocol.Factory();
        TServlet thriftServlet=new TServlet(multiplexedProcessor,protocolFactory);

        LoggerFactory.getLogger(SpringConfiguration.class).info("Thrift sevlet initialized");

        return thriftServlet;

    }
}

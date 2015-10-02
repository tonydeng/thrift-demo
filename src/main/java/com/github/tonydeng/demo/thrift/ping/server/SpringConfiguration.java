package com.github.tonydeng.demo.thrift.ping.server;

import com.github.tonydeng.demo.thrift.api.AdditionService;
import com.github.tonydeng.demo.thrift.ping.api.PingPongService;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServlet;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tonydeng on 15/9/24.
 */
@Configuration
@ComponentScan(basePackages = "com.github.tonydeng.demo.thrift.ping")
public class SpringConfiguration {
    @Qualifier
    private PingPongService.Iface pingPongService;
    @Qualifier
    private AdditionService.Iface additionService;



    @Bean
    public TServlet thriftServlet(){
        TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();

        PingPongService.Processor<PingPongService.Iface> pingPongProcessor = new PingPongService.Processor<>(pingPongService);
        AdditionService.Processor<AdditionService.Iface> additionProcessor = new AdditionService.Processor<>(additionService);

        multiplexedProcessor.registerProcessor("pingPongService",new PingPongService.Processor<>(pingPongService));
        multiplexedProcessor.registerProcessor("additionService",new AdditionService.Processor<>(additionService));

        TBinaryProtocol.Factory protocolFactory =  new TBinaryProtocol.Factory();
        TServlet thriftServlet=new TServlet(multiplexedProcessor,protocolFactory);

        LoggerFactory.getLogger(SpringConfiguration.class).info("Thrift sevlet initialized");

        return thriftServlet;

    }
}

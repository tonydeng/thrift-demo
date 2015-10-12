package com.github.tonydeng.demo.thrift.ping.client;

import com.github.tonydeng.demo.thrift.api.AdditionService;
import com.github.tonydeng.demo.thrift.ping.api.Ping;
import com.github.tonydeng.demo.thrift.ping.api.PingPongService;
import com.github.tonydeng.demo.thrift.ping.api.Pong;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonydeng on 15/9/24.
 */
public class PingPongClient {
    private static final Logger log =  LoggerFactory.getLogger(PingPongClient.class);
    public static void main(String[] args) throws TException {
        THttpClient transport   = new THttpClient("http://localhost:8080/api");

        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);

        PingPongService.Client client = new PingPongService.Client(new TMultiplexedProtocol(protocol,"pingPongService"));

        Ping ping = new Ping();
        ping.setMessage("Hello PingPong");
        Pong pong = client.knock(ping);
        log.info("Got answer: {}",pong.getAnswer());

//        AdditionService.Client client1 = new AdditionService.Client(new TMultiplexedProtocol(protocol,"additionService"));
//
//        int number = client1.add(100,200);
//
//        log.info("number:'{}'",number);
    }
}

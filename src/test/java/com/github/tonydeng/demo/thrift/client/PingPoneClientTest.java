package com.github.tonydeng.demo.thrift.client;

import com.github.tonydeng.demo.BaseTest;
import com.github.tonydeng.demo.thrift.ping.api.Ping;
import com.github.tonydeng.demo.thrift.ping.api.Pong;
import com.github.tonydeng.demo.thrift.ping.client.PingPongClient;
import org.apache.thrift.TException;
import org.junit.Test;

/**
 * Created by tonydeng on 15/10/12.
 */
public class PingPoneClientTest extends BaseTest {

    @Test
    public void testPing() throws TException {
        PingPongClient client = new PingPongClient("192.168.1.200",7911);

        Ping ping = new Ping("Hello World!");

        Pong pong = client.knock(ping);

        log.info("pong message:'{}'",pong);
    }
}

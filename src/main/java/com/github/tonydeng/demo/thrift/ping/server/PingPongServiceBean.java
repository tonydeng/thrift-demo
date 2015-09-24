package com.github.tonydeng.demo.thrift.ping.server;

import com.github.tonydeng.demo.thrift.ping.api.Ping;
import com.github.tonydeng.demo.thrift.ping.api.PingPongService;
import com.github.tonydeng.demo.thrift.ping.api.Pong;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;
import org.slf4j.LoggerFactory;

/**
 * Created by tonydeng on 15/9/24.
 */
public class PingPongServiceBean implements PingPongService.Iface{
    @Override
    public Pong knock(Ping ping) throws TException {

        String message  = ping.getMessage();
        String answer = StringUtils.reverse(message);

        Pong pong = new Pong();

        pong.setAnswer(answer);

        LoggerFactory.getLogger(PingPongServiceBean.class).info("Got message {} and sent answer {}",message,answer);

        return pong;
    }
}

package com.github.tonydeng.demo.thrift.client;

import com.github.tonydeng.demo.BaseTest;
import com.github.tonydeng.demo.thrift.api.LoginService;
import com.github.tonydeng.demo.thrift.api.RegisterService;
import com.github.tonydeng.demo.thrift.api.User;
import com.github.tonydeng.demo.thrift.ping.api.PingPongService;
import org.apache.thrift.TException;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.Test;

/**
 * Created by tonydeng on 15/9/25.
 */
public class UserClientTest extends BaseTest {
//    @Test
    public void testLogin() throws TException {

        TTransport transport = new TSocket("localhost",9090);

        TProtocol protocol = new TBinaryProtocol(transport);
        transport.open();
        LoginService.Client loginClient = new LoginService.Client(new TMultiplexedProtocol(protocol,"loginService"));

        User user = loginClient.login("tonydeng","123");
        transport.close();
        log.info("user: {}",user);


    }

    @Test
    public void testLoginAndRegister() throws TException {
        TTransport transport = new TSocket("localhost", 7911);
        TProtocol protocol = new TBinaryProtocol(transport);
        TMultiplexedProtocol mp1 = new TMultiplexedProtocol(protocol,
                "LoginService");
        // TProtocol protocol = new TBinaryProtocol(transport);
        // LoginService.Client client = new LoginService.Client(protocol);
        LoginService.Client loginClient = new LoginService.Client(mp1);
        TMultiplexedProtocol mp2 = new TMultiplexedProtocol(protocol,
                "RegisterService");
        RegisterService.Client registerClient = new RegisterService.Client(
                mp2);
        transport.open();

        User user = loginClient.login("penngo", "123");
        if (user != null) {
            System.out.println("登录成功：" + user.getId() + " "
                    + user.getName());
        } else {
            System.out.println("登录失败");
        }
        User user2 = registerClient.createUser("test", "123");
        if (user2 != null) {
            System.out.println("创建用户成功：" + user2.getId() + " "
                    + user2.getName());
        } else {
            System.out.println("创建用户失败");
        }
    }
}

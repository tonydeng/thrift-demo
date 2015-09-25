package com.github.tonydeng.demo.thrift.handler;

import com.github.tonydeng.demo.thrift.api.RegisterService;
import com.github.tonydeng.demo.thrift.api.User;
import org.apache.thrift.TException;

/**
 * Created by tonydeng on 15/9/25.
 */
public class RegisterServiceHandler implements RegisterService.Iface {
    @Override
    public User createUser(String name, String psw) throws TException {
        User user = new User();
        user.setId(2);
        user.setName(name);
        user.setPassword(psw);
        return user;
    }
}

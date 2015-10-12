package com.github.tonydeng.demo.thrift.handler;

import com.github.tonydeng.demo.thrift.api.LoginService;
import com.github.tonydeng.demo.thrift.api.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TException;

/**
 * Created by tonydeng on 15/9/25.
 */
public class LoginServiceHandler implements LoginService.Iface {
    @Override
    public User login(String name, String psw) throws TException {
        User user = null;
        if (StringUtils.equals("tony", name) && StringUtils.equals("123", psw)) {
            user = new User();
            user.setId(1);
            user.setName(name);
        }
        return user;
    }
}

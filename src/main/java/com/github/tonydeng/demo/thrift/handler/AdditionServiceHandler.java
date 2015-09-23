package com.github.tonydeng.demo.thrift.handler;

import com.github.tonydeng.demo.thrift.api.AdditionService;
import org.apache.thrift.TException;

/**
 * Created by tonydeng on 15/9/22.
 */
public class AdditionServiceHandler implements AdditionService.Iface{
    @Override
    public int add(int n1, int n2) throws TException {
        return n1+n2;
    }
}

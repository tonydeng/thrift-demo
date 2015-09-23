package com.github.tonydeng.demo.thrift.client;

import com.github.tonydeng.demo.thrift.api.AdditionService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tonydeng on 15/9/22.
 */
public class AdditionClient implements AdditionService.Iface {
    private static final Logger log = LoggerFactory.getLogger(AdditionClient.class);

    public static void main(String[] args) {
        try {
            TTransport transport;
            transport = new TSocket("localhost", 9090);

            TProtocol protocol = new TBinaryProtocol(transport);

            AdditionService.Client client = new AdditionService.Client(protocol);
            System.out.println("100 + 200 = " + client.add(100, 200));

            transport.close();
        } catch (TException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int add(int n1, int n2) throws TException {
        TTransport transport = new TSocket("localhost", 9090);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        AdditionService.Client client = new AdditionService.Client(protocol);
        int number = client.add(n1, n2);
        log.info("{} + {} = {}", n1, n2, number);
        transport.close();
        return number;
    }
}

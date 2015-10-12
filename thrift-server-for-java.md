# Java中各种Thrift Server可用实现及比较

Java中可以用的Server实现：

* TSimpleServer
* TNonblockingServer
* THsHaServer
* TThreadedSelectorServer
* TThreadPoolServer

我们来看看看他们的区别及性能特点。

## TSimpleServer

TSimpleServer接受一个连接，处理连接请求，知道客户端关闭了连接，它才回去接受一个新的连接。

因为它只是在一个单独的线程中以阻塞I/O的方式完成这些工作，所以它只能服务一个客户端连接，其他所有客户端在被服务器端接受之前都只能等待。

TSimpleServer一般我们只会用来测试而已，千万不要在生产环境中使用它。

## TNonblockingServer

TNonblockingServer使用非阻塞的I/O解决了TSimpleServer一个客户端阻塞其他所有客户端的问题。它使用了`java.nio.channels.Selector`，通过调用`select()`，它使得你阻塞在多个连接上，而不是阻塞在单一的连接上。

当一个或多个连接准备好**被接受请求/读/写**时，`select()`调用便会返回。

TNonblockingServer处理这些连接的时候，要么接受它，要么从它那读取数据，要么把数据写到它那里，然后再次调用select()来等待下一个可用的连接。通过这种方式，server可以同时服务多个客户端，而不会出现一个客户端把其他客户端全部“饿死”的情况。

不过，还有一个棘手的问题：所有的消息是被调用`select()`方法的同一个线程处理的。

假设有10个客户端，处理每条消息所需时间为100毫秒，那么，latency和吞吐量分别是多少？

当一条消息被处理的时候，其他9个客户端就等着被select，所以客户端需要等待1秒钟才能从服务器端得到回应，吞吐量就是10请求/秒。

## THsHaServer

THsHAServer（半同步/半异步的server）就是为了解决TNonblocking吞吐量不够的问题。

它使用一个单独的线程来处理网络I/O，一个独立的worker线程池来处理消息。这样，只要有空闲的worker线程，消息就会被理解处理，因此多条消息能被并行处理。用上面的例子来说，现在的latency就是100毫秒，而吞吐量就是100个请求/秒。

为了演示，我做了一个测试，有10个客户端和一个修改过的消息处理器--它的功能仅仅只是在返回之前简单的sleep 100毫秒。我使用的是有10个worker线程的THsHaServer。消息处理器的代码看起来就像这样：

```java
public ResponseCode sleep() throws TException{
    try{
        Thread.sleep(100);
    }catch (Execption ex){
    }
    return ResponseCode.Success;
}
```

![sleep throughput](https://github.com/m1ch1/mapkeeper/raw/master/ycsb/data/thrift2/sleep_throughput.png)

![sleep latency](https://github.com/m1ch1/mapkeeper/raw/master/ycsb/data/thrift2/sleep_latency.png)

结果正如我们想像的那样，THsHaServer能够并行处理所有请求，而TNonblockingServer只能一次处理一个请求。

## TThreadedSelectorServer

Thrift 0.8引入了另一种server实现，即TThreadedSelectorServer。它与THsHaServer的主要区别在于，TThreadedSelectorServer允许你用多个线程来处理网络I/O。它维护了两个线程池，一个用来处理网络I/O，另一个用来进行请求的处理。当网络I/O是瓶颈的时候，TThreadedSelectorServer比THsHaServer的表现要好。为了展现它们的区别，我进行了一个测试，令其消息处理器在不做任何工作的情况下立即返回，以衡量在不同客户端数量的情况下的平均latency和吞吐量。对THsHaServer，我使用32个worker线程；对TThreadedSelectorServer，我使用16个worker线程和16个selector线程。

![num clients throughput](https://github.com/m1ch1/mapkeeper/raw/master/ycsb/data/thrift2/num_clients_throughput.png)

![num clients latency](https://github.com/m1ch1/mapkeeper/raw/master/ycsb/data/thrift2/num_clients_latency.png)

结果显示，TThreadedSelectorServer比THsHaServer的吞吐量高得多，并且维持在一个更低的latency上。

# TThreadPoolServer

最后，还剩下 TThreadPoolServer。

TThreadPoolServer与其他三种server不同的是：

* 有一个专用的线程用来接受连接。
* 一旦接受了一个连接，它就会被放入ThreadPoolExecutor中的一个worker线程里处理。
* worker线程被绑定到特定的客户端连接上，直到它关闭。一旦连接关闭，该worker线程就又回到了线程池中。
* 你可以配置线程池的最小、最大线程数，默认值分别是5（最小）和Integer.MAX_VALUE（最大）。
 
这意味着，如果有1万个并发的客户端连接，你就需要运行1万个线程。所以它对系统资源的消耗不像其他类型的server一样那么“友好”。此外，如果客户端数量超过了线程池中的最大线程数，在有一个worker线程可用之前，请求将被一直阻塞在那里。

我们已经说过，TThreadPoolServer的表现非常优异。在我正在使用的计算机上，它可以支持1万个并发连接而没有任何问题。如果你提前知道了将要连接到你服务器上的客户端数量，并且你不介意运行大量线程的话，TThreadPoolServer对你可能是个很好的选择。

![num_clients_throughput_pool](https://github.com/m1ch1/mapkeeper/raw/master/ycsb/data/thrift2/num_clients_throughput_pool.png) 

![num_clients_latency_pool](https://github.com/m1ch1/mapkeeper/raw/master/ycsb/data/thrift2/num_clients_latency_pool.png)


## 结论

希望本文能帮你做出决定：哪一种Thrift server适合你。我认为TThreadedSelectorServer对大多数案例来说都是个安全之选。如果你的系统资源允许运行大量并发线程的话，你可能会想考虑使用TThreadPoolServer。

## 参考文章

[Thrift Java Servers Compared](https://github.com/m1ch1/mapkeeper/wiki/Thrift-Java-Servers-Compared)

[Thrift Java Servers Compared中文翻译](http://www.codelast.com/?p=4824)
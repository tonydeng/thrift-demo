namespace java com.github.tonydeng.demo.thrift.ping.api
namespace php com.github.tonydeng.demo.thrift.ping.api

struct Ping {
    1: string message;
}

struct Pong {
    1: string answer;
}

service PingPongService {
    Pong knock(1: Ping ping);
}

namespace java com.github.tonydeng.demo.thrift.api

typedef i32 int

service AdditionService {
    int add(1:int n1, 2:int n2),
}
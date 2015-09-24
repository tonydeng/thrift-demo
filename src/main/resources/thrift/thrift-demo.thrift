namespace java com.github.tonydeng.demo.thrift.api
namespace php thrifDemo

typedef i32 int

/**
* Common status reporting mechanism acress all services
*/
enum Status {
    DEAD = 0,
    STARTING = 1,
    ALIVE = 2,
    STOPPING = 3,
    STOPPED = 4,
    WARNING = 5,
}

/**
* Standard base Service
*/
service FacebookService{
    string getName(),
    string getVersion(),
    Status getStatus(),

    string getStatusDetails(),

    map<string,i64> getCounters(),

    i64 getCounter(1:string key),
    void setOption(1:string key),
    string getOption(1:string key),
    map<string,string> getOptions(),
  string getCpuProfile(1: i32 profileDurationInSec),
    i64 aliveSince(),
    oneway void reinitalize(),
    oneway void shutdown(),
}

service AdditionService {
    int add(1:int n1, 2:int n2),
}
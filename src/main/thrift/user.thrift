namespace java com.github.tonydeng.demo.thrift.api

struct User{
    1: i64 id,
    2: string name,
    3: string password
}

service LoginService{
    User login(1:string name, 2:string psw);
}

service RegisterService{
    User createUser(1:string name, 2:string psw);
}
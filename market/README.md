## 레디스 설치 
https://redis.io/download

$ wget http://download.redis.io/releases/redis-5.0.5.tar.gz
$ tar xzf redis-5.0.5.tar.gz
$ cd redis-5.0.5
$ make

-- redis run 
$ src/redis-server

-- redis cli
$ src/redis-cli

keys *
get key


## redis 추가
편의상 controller 에 호출을 하는 방식으로 구현을 하였으나, 바로 메서드를 실행하는 식으로 개발해도 된다.  


### 프로세스 설명

1. 최초 clt 라는 값을 조회하였을때, 캐쉬 정보가 없으니 메서드 안쪽의 내용을 실행한다.  
 두번째 호출부터는 메서드 안쪽의 로직을 실행하지 않고, 캐쉬에서 정보를 가지고 온다.

```  
$ http localhost:8081/market/offer name==clt  

{
    "id": 1,
    "name": "clt"
}

```


```
$ redis-cli
127.0.0.1:6379> keys *
1) "app::clt"
127.0.0.1:6379> get app::clt
"\xac\xed\x00\x05sr\x00\x1bcom.example.template.Offers\xd2\x04\x8b\xae\x04\xa1\x1bn\x02\x00\x02L\x00\x02idt\x00\x10Ljava/lang/Long;L\x00\x04namet\x00\x12Ljava/lang/String;xpsr\x00\x0ejava.lang.Long;\x8b\xe4\x90\xcc\x8f#\xdf\x02\x00\x01J\x00\x05valuexr\x00\x10java.lang.Number\x86\xac\x95\x1d\x0b\x94\xe0\x8b\x02\x00\x00xp\x00\x00\x00\x00\x00\x00\x00\x01t\x00\x03clt"

```

2. 저장되어있는 값의 id 를 변경하였다.  
케쉬에 바로 반영이 되어서, 다시 조회를 하여도 메서드 안쪽의 로직을 타지 않는다.  
http localhost:8081/market/save id==2 name==clt  

```  
$ http localhost:8081/market/offer name==clt  

{
    "id": 2,
    "name": "clt"
}

```


3. 아래 메서드는 redisTemplate 을 사용하여 직접 데이터를 넣는 부분이다.  
이렇게 사용시 cache처럼 사용도 가능하지만 데이터용도로도 사용이 가능하다.  
cacheConfiguration 에 적혀있는 시간이 지난뒤에 케쉬가 사라지지 않는다.  

http localhost:8081/market/offer2 name==clts 
http localhost:8081/market/save2 id==3 name==clt3
http localhost:8081/market/save2 id==4 name==clt4    

-- clt 이름이 포함된 키를 조회하여 데이터를 가져온다.  
http localhost:8081/market/offerList name==clt  

-- clt 이라는 이름의 키를 조회하여 데이터를 삭제한다.  
http localhost:8081/market/delete name==clt 
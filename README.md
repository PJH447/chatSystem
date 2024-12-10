MySQL
- 
```
docker run --name local-mysql -e MYSQL_ROOT_PASSWORD=1234 -d -p 3306:3306 mysql:latest
```

Redis
- 
```
docker run -d -p 6379:6379 --name redis-test-container redis:latest --requirepass "pass"
```

RabbitMQ
- 
```
docker pull rabbitmq

docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 -p 61613:61613 --restart=unless-stopped rabbitmq:3-management

docker exec rabbitmq rabbitmq-plugins enable rabbitmq_management

http://localhost:15672

[TCP connection failure in session _system_: Failed to connect]
1. rabbitmq-plugins enable rabbitmq_web_stomp
2. rabbitmq-plugins enable rabbitmq_web_stomp_examples
```

MySql
-
only_full_group_by option
```
SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY
SET SESSION sql_mode = (SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY', ''));
SET GLOBAL sql_mode = (SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY', ''));
```

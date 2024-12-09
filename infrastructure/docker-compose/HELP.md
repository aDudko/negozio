# How to try this project?

#### Warning❗ It is recommended to set variable of environment `GLOBAL_NETWORK`
#### Warning❗ It is also recommended to have `12 Gb` of memory limit and `4 CPU` limit for local docker deployment. And to run the services `one by one`, monitoring the CPU and memory load.

## Start ZooKeeper
**It runs in a new Terminal**
```sh
docker-compose -f common.yml -f zookeeper.yml up
```
### Test the health of ZooKeeper
**It runs in a new Terminal**
```sh
echo ruok | nc localhost 2181
```
The answer should be `imok`. See: [ZooKeeper Commands: The Four Letter Words](https://zookeeper.apache.org/doc/r3.1.2/zookeeperAdmin.html#sc_zkCommands)


## Start cluster of Kafka
**It runs in a new Terminal**
```sh
docker-compose -f common.yml -f kafka_cluster.yml up
```
### Init Kafka: create topics (runs one time)
**It runs in a new Terminal**
```sh
docker-compose -f common.yml -f config/init_kafka.yml up
```
### Check Kafka Manager
- Go to [User Interface](http://localhost:9000/)
- `Cluster` `->` `Add Cluster`
  - `Cluster Name` `=` `negozio-kafka-cluster`
  - `Cluster Zookeeper Host` `=` `zookeeper:2181`
  - `->` `Save`
- `Go to cluster view`


## Start cluster of Elastic
**It runs in a new Terminal**
```sh
docker-compose -f common.yml -f elastic_cluster.yml up
```
### Verify that logs from microservices are coming into Logstash
**It runs in a new Terminal**
```sh
docker logs logstash
```

## Start Postgres
**It runs in a new Terminal**
```sh
docker-compose -f common.yml -f postgres.yml up
```


## Running web-services
**It runs in a new Terminal**
```sh
docker-compose -f common.yml -f web_services.yml up
```

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
See: [ZooKeeper Commands: The Four Letter Words](https://zookeeper.apache.org/doc/r3.1.2/zookeeperAdmin.html#sc_zkCommands)


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

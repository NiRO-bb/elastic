# Elasticsearch API microservice
This project presents microservice for retrieving messages from Kafka topic.

## Install
### Preferenced requirements
* Java 21
* Maven 3.9.9
* Spring Boot 3.5.3
* Docker

### Steps to install project
1. Clone repository
```shell
git clone https://github.com/NiRO-bb/elastic.git
```

2. Create .env files
   You must write .env_dev and .env_prod files with following values:
* SPRING_ELASTICSEARCH_URIS
* ELASTIC_SERVICE_INDEX_METHOD - name of index used for saving method log documents
* ELASTIC_SERVICE_INDEX_REQUEST - name of index used for saving request log documents

<p>.env_dev - for local development </p>
<p>.env_prod - for container (docker) development</p>

3. Build with Maven
```shell
mvn clean package
```

## Usage
1. Launch Docker
```shell
docker compose up
```
<b>!</b> docker-compose.yml uses docker network - 'producer-consumer'.
This for interaction with other containers. But you must create this network manually:
```shell
docker network create producer-consumer
```

## API
You can get API docs by http://localhost:8080/swagger-ui/index.html

## Contributing
<a href="https://github.com/NiRO-bb/audit-listener/graphs/contributors/">Contributors</a>

## License
No license 
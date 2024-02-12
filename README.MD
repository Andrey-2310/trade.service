### How to start the application
#### Docker-based solution:

To run the application you should have Docker and docker-compose installed on your machine (See [Helpful links](helpful-links))

On the project root level:

1. `mvn clean package` to build the .jar file that will be used in Dockerfile
2. `docker-compose up` to run the application and the Redis server.

#### Without Docker solution:
1. Run Redis on-premises in whatever way you want (depends on OS)
2. `mvn clean package` to build the .jar file that will be used in Dockerfile
3. `java -jar "./target/trade-service.jar"`

### Testing the application

The following curl command might be used in order to call the API:

`curl -X POST -F file=@trade.csv $host/api/v1/trade`,
`host` should be defined as env variable, e.g. for local testing:
`export host=http://localhost:8080`

The `trade.csv` file should be present in the folder from which `curl` happens.

### Configuring the application

#### Multipart configuration 
The API uses "multipart/form-data" instead of having data in the payload because it's more efficient for large amount of data.

In order to control the max size of the file the following flags are used:
* `spring.servlet.multipart.max-file-size`
* `spring.servlet.multipart.max-request-size`

Currently, they are set to 50MB.

#### Redis configuration

In order to control the Redis connection configuration, the following environment variables might be used:
* `SPRING_REDIS_HOST` - specifies the host, on which Redis is running (default: `"localhost"`)
* `SPRING_REDIS_PORT` - specifies the port, on which Redis is running (default: `6379`)
* `SPRING_REDIS_PASSWORD` - specifies the password for Redis(default: `""`)

### Trades processing optimizations
In order to support large sets of trades, the following optimizations where done:
* Incoming .csv file is handled in a sequential way, meaning that .csv rows are processed one by one (using for-each, which is implemented with iterator() and hasNext() methods under the hood).
* .csv rows are not collected in the Java collections in order to avoid memory issues which might happen on a large scale.
* On the final step, Trade with the Project Name is written directly into the output stream.

### Optimizations for Products
* Currently, Products are stored under Redis to perform `find by id` operation with time Complexity of O(1), instead of iterating over .csv file during each request.
* The process of uploading Products into Redis is controlled by the following flags:
  * `redis.onstart.invalidate.products` - if set to `true`, all previous values will be invalidated.
  * `redis.onstart.update.products` - if set to `true`, current state of the products.csv (resources/static/) will be reflected in Redis.

### Further optimizations
#### Increasing the amount of Products
If one instance of Redis won't be enough to support the given amount of the Products - multiple Redis server might be used.
  * In order to understand, which exact server stores the particular Product, `Consistent Hashing` technic might be used.
  * In order not to lose the data once the particular server is down, `Data Replication` might be used.
  * Data synchronization: Assuming the following,
    * `N` - total number of replicas
    * `W` - amount of replicas that should acknowledge Write operation
    * `R` - amount of replicas that should acknowledge Read operation
    
    Then, according to `Quorum Consensus`, we should guarantee, that `W + R > N`. As soon as this application is mostly optimized
for Read operations, we can configure `R = 1, W = N`

#### Increase of incoming traffic
If many users access the
web server simultaneously and the server's load limit is reached, users generally
experience slower response or fail to connect to the server. Using horizontal scaling along with load balancer might be a
good technique to address these problems.

#### Rate limiter
In order to control the rate of traffic sent by a client, Rate Limiter might be used. It will limit the number of client requests allowed to be
sent over a specified period.




### Helpful Links

[Installing Docker](https://docs.docker.com/engine/install/ubuntu/)

[Running Docker commands without sudo ](https://askubuntu.com/questions/477551/how-can-i-use-docker-without-sudo)

[Installing docker-compose](https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-compose-on-ubuntu-20-04)

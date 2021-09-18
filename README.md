# springboot-rest-api-with-concurrency
RESTful API in Springboot with concurrency support


## Notes
* There are two specified APIs to call. One is single threaded. Another uses multi-threaded approach with `CompletableFuture`.
* GET api that works in a single thread: [http://localhost:8080/primes-count?n={input-number}](http://localhost:8080/primes-count?n=10000000)
* GET api that works in multiple (10 at most) threads: [http://localhost:8080/primes-count-multithreaded?n={input-number}](http://localhost:8080/primes-count-multithreaded?n=10000000)


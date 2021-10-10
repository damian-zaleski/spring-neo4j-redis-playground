What I've learned:
- generally how to use the Caching Abstraction in `Spring`
- difference between `@Cacheable` and `@CachePut`
- how to handle conditional caching (e.g. `condition="#item.name=='John'"` or `unless="#result.length()<10"`)
- how to evict a cache (e.g. `cacheManager` with `.clear()`, or `@CacheEvict` annotation)
- how to test `@Cacheable` annotation with `Spring Boot`

Read more:
- https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache
- https://redis.io/documentation
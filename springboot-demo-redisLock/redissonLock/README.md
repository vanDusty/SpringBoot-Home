Redis 分布式锁(一)：SpringBoot集成Redisson分布式锁

> Redisson实现了分布式和可扩展的Java数据结构，和Jedis相比，功能较为简单，不支持字符串操作，不支持排序、事务、管道、分区等Redis特性。Redisson的宗旨是促进使用者对Redis的关注分离，从而让使用者能够将精力更集中地放在处理业务逻辑上。

# 一、项目Demo

## 1.1 项目依赖

```pom
<!-- 单元测试 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<!-- redis -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!-- redisson -->
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson</artifactId>
    <version>3.10.6</version>
</dependency>
```

## 1.2 项目配置-redis

```yml
spring:
  redis:
    host: 47.98.178.84
    port: 6379
    database: 0
    password: password
    timeout: 60s
```

## 1.3 Redisson的配置类RedissonConfig

```java
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * RedissonClient,单机模式
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        return Redisson.create(config);
    }

    @Bean
    public RedissonLocker redissonLocker(RedissonClient redissonClient){
        RedissonLocker locker = new RedissonLocker(redissonClient);
        //设置LockUtil的锁处理对象
        LockUtil.setLocker(locker);
        return locker;
    }
}
```

## 1.4 定义一个Loker接口，用于分布式锁的一些操作

> 主要是为了考虑扩展，本文用redisson实现，同样的`Loker`接口，也可用redis实现分布式锁，详见另一篇文章。

```java
public interface Locker {
    /**
     * 获取锁，如果锁不可用，则当前线程处于休眠状态，直到获得锁为止。
     *
     * @param lockKey
     */
    void lock(String lockKey);

    /**
     * 释放锁
     *
     * @param lockKey
     */
    void unlock(String lockKey);

    /**
     * 带超时的锁: 超过加锁时间后自动解锁
     *
     * @param lockKey
     * @param timeout : 秒
     */
    void lock(String lockKey, int timeout);

    /**
     * 带超时的锁: 超过加锁时间后自动解锁
     *
     * @param lockKey
     * @param unit
     * @param timeout
     */
    void lock(String lockKey, TimeUnit unit, int timeout);

    /**
     * 尝试获取锁，获取到立即返回true,未获取到立即返回false
     *
     * @param lockKey
     * @return
     */
    boolean tryLock(String lockKey);

    /**
     * 尝试获取锁，在等待时间内获取到锁则返回true,否则返回false,如果获取到锁，则要么执行完后程序释放锁，
     * 要么在给定的超时时间leaseTime后释放锁
     * @param lockKey
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @return
     */
    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit)
            throws InterruptedException;

    /**
     * 锁是否被任意一个线程锁持有
     *
     * @param lockKey
     * @return
     */
    boolean isLocked(String lockKey);
}
```

## 1.5 基于Redisson的实现类RedissonLocker

```java
public class RedissonLocker implements Locker{

    private RedissonClient redissonClient;

    public RedissonLocker(RedissonClient redissonClient) {
        super();
        this.redissonClient = redissonClient;
    }

    
    public void lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
    }


    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    
    public void lock(String lockKey, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
    }

    
    public void lock(String lockKey, TimeUnit unit, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    
    public boolean tryLock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.tryLock();
    }

    
    public boolean tryLock(String lockKey, long waitTime, long leaseTime,
                           TimeUnit unit) throws InterruptedException{
        RLock lock = redissonClient.getLock(lockKey);
        return lock.tryLock(waitTime, leaseTime, unit);
    }

    
    public boolean isLocked(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.isLocked();
    }
}
```

## 1.6 定义一个分布式锁工具类LockUtil

```java
public class LockUtil {
    private static Locker locker;

    /**
     * 设置工具类使用的locker
     * @param locker
     */
    public static void setLocker(Locker locker) {
        LockUtil.locker = locker;
    }

    /**
     * 获取锁
     * @param lockKey
     */
    public static void lock(String lockKey) {
        locker.lock(lockKey);
    }

    /**
     * 释放锁
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        locker.unlock(lockKey);
    }

    /**
     * 获取锁，超时释放
     * @param lockKey
     * @param timeout
     */
    public static void lock(String lockKey, int timeout) {
        locker.lock(lockKey, timeout);
    }

    /**
     * 获取锁，超时释放，指定时间单位
     * @param lockKey
     * @param unit
     * @param timeout
     */
    public static void lock(String lockKey, TimeUnit unit, int timeout) {
        locker.lock(lockKey, unit, timeout);
    }

    /**
     * 尝试获取锁，获取到立即返回true,获取失败立即返回false
     * @param lockKey
     * @return
     */
    public static boolean tryLock(String lockKey) {
        return locker.tryLock(lockKey);
    }

    /**
     * 尝试获取锁，在给定的waitTime时间内尝试，获取到返回true,获取失败返回false,获取到后再给定的leaseTime时间超时释放
     * @param lockKey
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public static boolean tryLock(String lockKey, long waitTime, long leaseTime,
                                  TimeUnit unit) throws InterruptedException {
        return locker.tryLock(lockKey, waitTime, leaseTime, unit);
    }

    /**
     * 锁释放被任意一个线程持有
     * @param lockKey
     * @return
     */
    public static boolean isLocked(String lockKey) {
        return locker.isLocked(lockKey);
    }
}
```

## 1.7 单元测试

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class LockerTest {

    private static final Logger logger = LoggerFactory.getLogger(LockerTest.class);

    @Test
    public void testLock() {
        LockUtil.lock("hello");
        logger.info("获取了锁");
        try {
            //TODO 干事情
        } catch (Exception e) {
            //异常处理
        }finally{
            //释放锁
            LockUtil.unlock("hello");
            logger.info("释放锁");
        }
    }
}
```

## 1.8 源码

[SpringBoot集成Redisson分布式锁](https://github.com/vanDusty/SpringBoot-Home/tree/master/springboot-demo-redisLock/redissonLock)

# 二、Redisson 之 Redlock

> 一顿猛操作后，我们来看看它的优势。

## 2.1 基于Redis命令的分布式锁

我们都知道`Redis`分布式锁大部分人都会想到：

```
- 获取锁（unique_value可以是UUID等）
SET resource_name unique_value NX PX 30000

- 释放锁（lua脚本中，一定要比较value，防止误解锁）
if redis.call("get",KEYS[1]) == ARGV[1] then
    return redis.call("del",KEYS[1])
else
    return 0
end
```

这种实现方式有3大要点：

1. set命令要用set key value px milliseconds nx；
1. value要具有唯一性；
1. 释放锁时要验证value值，不能误解锁；

同时，他也存在一个很大的问题：它加锁时只作用在一个Redis节点上，即使Redis通过sentinel保证高可用，如果这个master节点由于某些原因发生了主从切换，那么就会出现锁丢失的情况：

- 在Redis的master节点上拿到了锁；
- 但是这个加锁的key还没有同步到slave节点；
- master故障，发生故障转移，slave节点升级为master节点；

正因为如此，Redis作者antirez基于分布式环境下提出了一种更高级的分布式锁的实现方式：Redlock

## 2.2 Redlock实现

在Redis的分布式环境中，我们假设有N个Redis master。这些节点完全互相独立，不存在主从复制或者其他集群协调机制。我们确保将在N个实例上使用与在Redis单实例下相同方法获取和释放锁。为了取到锁，客户端应该执行以下操作:

1. 获取当前Unix时间，以毫秒为单位。
1. 依次尝试从N个实例，使用相同的key和具有唯一性的value（例如UUID）获取锁。当向Redis请求获取锁时，客户端应该设置一个网络连接和响应超时时间，这个超时时间应该小于锁的失效时间。例如你的锁自动失效时间为10秒，则超时时间应该在5-50毫秒之间。这样可以避免服务器端Redis已经挂掉的情况下，客户端还在死死地等待响应结果。如果服务器端没有在规定时间内响应，客户端应该尽快尝试去另外一个Redis实例请求获取锁。
1. 客户端使用当前时间减去开始获取锁时间（步骤1记录的时间）就得到获取锁使用的时间。当且仅当从大多数（N/2+1 个节点）的Redis节点都取到锁，并且使用的时间小于锁失效时间时，锁才算获取成功。
1. 如果取到了锁，key的真正有效时间等于有效时间减去获取锁所使用的时间。
1. 如果因为某些原因，获取锁失败（没有在至少N/2+1个Redis实例取到锁或者取锁时间已经超过了有效时间），客户端应该在所有的Redis实例上进行解锁（即便某些Redis实例根本就没有加锁成功，防止某些节点获取到锁但是客户端没有得到响应而导致接下来的一段时间不能被重新获取锁）。

## 2.3 Redlock源码

### 2.3.1 唯一ID

实现分布式锁的一个非常重要的点就是set的value要具有唯一性，`redisson`的value是怎样保证value的唯一性呢？答案是**`UUID+threadId`**。源码在Redisson.java和RedissonLock.java中：

```java
protected final UUID id = UUID.randomUUID();
String getLockName(long threadId) {
    return id + ":" + threadId;
}
```

### 2.3.2 获取锁

获取锁的代码为`redLock.tryLock()`或者`redLock.tryLock(500, 10000, TimeUnit.MILLISECONDS)`，两者的最终核心源码都是下面这段代码，只不过前者获取锁的默认租约时间（leaseTime）是**LOCK_EXPIRATION_INTERVAL_SECONDS**，即30s：

```java
<T> RFuture<T> tryLockInnerAsync(long leaseTime, TimeUnit unit, long threadId, RedisStrictCommand<T> command) {
    internalLockLeaseTime = unit.toMillis(leaseTime);
    // 获取锁时需要在redis实例上执行的lua命令
    return commandExecutor.evalWriteAsync(getName(), LongCodec.INSTANCE, command,
              // 首先分布式锁的KEY不能存在，如果确实不存在，那么执行hset命令（hset REDLOCK_KEY uuid+threadId 1），并通过pexpire设置失效时间（也是锁的租约时间）
              "if (redis.call('exists', KEYS[1]) == 0) then " +
                  "redis.call('hset', KEYS[1], ARGV[2], 1); " +
                  "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                  "return nil; " +
              "end; " +
              // 如果分布式锁的KEY已经存在，并且value也匹配，表示是当前线程持有的锁，那么重入次数加1，并且设置失效时间
              "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                  "redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
                  "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                  "return nil; " +
              "end; " +
              // 获取分布式锁的KEY的失效时间毫秒数
              "return redis.call('pttl', KEYS[1]);",
              // 这三个参数分别对应KEYS[1]，ARGV[1]和ARGV[2]
                Collections.<Object>singletonList(getName()), internalLockLeaseTime, getLockName(threadId));
}
```

获取锁的命令:

1. `KEYS[1]`就是`Collections.singletonList(getName())`，表示分布式锁的key，即`REDLOCK_KEY`；

1. `ARGV[1]`就是`internalLockLeaseTime`，即锁的租约时间，默认30s；

1. `ARGV[2]`就是`getLockName(threadId)`，是获取锁时set的唯一值，即`UUID+threadId`。

### 2.3.2 释放锁

释放锁的代码为`redLock.unlock()`，核心源码如下：

```java
protected RFuture<Boolean> unlockInnerAsync(long threadId) {
    // 释放锁时需要在redis实例上执行的lua命令
    return commandExecutor.evalWriteAsync(getName(), LongCodec.INSTANCE, RedisCommands.EVAL_BOOLEAN,
            // 如果分布式锁KEY不存在，那么向channel发布一条消息
            "if (redis.call('exists', KEYS[1]) == 0) then " +
                "redis.call('publish', KEYS[2], ARGV[1]); " +
                "return 1; " +
            "end;" +
            // 如果分布式锁存在，但是value不匹配，表示锁已经被占用，那么直接返回
            "if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then " +
                "return nil;" +
            "end; " +
            // 如果就是当前线程占有分布式锁，那么将重入次数减1
            "local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1); " +
            // 重入次数减1后的值如果大于0，表示分布式锁有重入过，那么只设置失效时间，还不能删除
            "if (counter > 0) then " +
                "redis.call('pexpire', KEYS[1], ARGV[2]); " +
                "return 0; " +
            "else " +
                // 重入次数减1后的值如果为0，表示分布式锁只获取过1次，那么删除这个KEY，并发布解锁消息
                "redis.call('del', KEYS[1]); " +
                "redis.call('publish', KEYS[2], ARGV[1]); " +
                "return 1; "+
            "end; " +
            "return nil;",
            // 这5个参数分别对应KEYS[1]，KEYS[2]，ARGV[1]，ARGV[2]和ARGV[3]
            Arrays.<Object>asList(getName(), getChannelName()), LockPubSub.unlockMessage, internalLockLeaseTime, getLockName(threadId));

}
```

单例模式可以说只要是一个合格的开发都会写，但是如果要深究，小小的单例模式可以牵扯到很多东西，比如：多线程是否安全？是否懒加载？性能等等。还有你知道几种单例模式的写法呢？如何防止反射破坏单例模式？


## 一、 单例模式

### 1.1 定义

单例模式就是在程序运行中只实例化一次，创建一个全局唯一对象。有点像 `Java` 的静态变量，但是单例模式要优于静态变量：

1. 静态变量在程序启动的时候`JVM`就会进行加载，如果不使用，会造成大量的资源浪费；
2. 单例模式能够实现懒加载，能够在使用实例的时候才去创建实例。

开发工具类库中的很多工具类都应用了单例模式，比例线程池、缓存、日志对象等，它们都只需要创建一个对象，如果创建多份实例，可能会带来不可预知的问题，比如资源的浪费、结果处理不一致等问题。

### 1.2 单例的实现思路

1. 静态化实例对象；
1. 私有化构造方法，禁止通过构造方法创建实例；
1. 提供一个公共的静态方法，用来返回唯一实例。

### 1.3 单例的好处

1. 只有一个对象，内存开支少、性能好；
1. 避免对资源的多重占用；
1. 在系统设置全局访问点，优化和共享资源访问。

## 二、 单例模式的实现


1. 饿汉模式
2. 懒汉模式
3. 双重检查锁模式
4. 静态内部类单例模式
5. 枚举类实现单例模式


### 2.1 饿汉模式

在定义静态属性时，直接实例化了对象

```java
public class HungryMode {

    /**
     * 利用静态变量来存储唯一实例
     */
    private static final HungryMode instance = new HungryMode();

    /**
     * 私有化构造函数
     */
    private HungryMode(){
        // 里面可以有很多操作
    }

    /**
     * 提供公开获取实例接口
     * @return
     */
    public static HungryMode getInstance(){
        return instance;
    }
}
```

#### 2.1.1 优点

由于使用了`static`关键字，保证了在引用这个变量时，关于这个变量的所以写入操作都完成，所以保证了`JVM`层面的线程安全

#### 2.1.2 缺点

不能实现懒加载，造成空间浪费：如果一个类比较大，我们在初始化的时就加载了这个类，但是我们长时间没有使用这个类，这就导致了内存空间的浪费。

> 所以，能不能只有用到 `getInstance()`方法，才会去初始化单例类，才会加载单例类中的数据。所以就有了：**懒汉式**。

### 2.2 懒汉模式

懒汉模式是一种偷懒的模式，在程序初始化时不会创建实例，只有在使用实例的时候才会创建实例，所以懒汉模式解决了饿汉模式带来的空间浪费问题。

#### 2.2.1 懒汉模式的一般实现

```java
public class LazyMode {
    /**
     * 定义静态变量时，未初始化实例
     */
    private static LazyMode instance;

    /**
     * 私有化构造函数
     */
    private LazyMode(){
        // 里面可以有很多操作
    }
    /**
     * 提供公开获取实例接口
     * @return
     */
    public static LazyMode getInstance(){
        // 使用时，先判断实例是否为空，如果实例为空，则实例化对象
        if (instance == null) {
            instance = new LazyMode();
        }
        return instance;
    }
}
```

但是这种实现在多线程的情况下是不安全的，有可能会出现多份实例的情况：

```java
if (instance == null) {
    instance = new LazyMode();
}
```

假设有两个线程同时进入到上面这段代码，因为没有任何资源保护措施，所以两个线程可以同时判断的 `instance` 都为空，都将去初始化实例，所以就会出现多份实例的情况。

#### 2.2.2 懒汉模式的优化

我们给`getInstance()`方法加上`synchronized`关键字，使得`getInstance()`方法成为受保护的资源就能够解决多份实例的问题。

```java
public class LazyModeSynchronized {
    /**
     * 定义静态变量时，未初始化实例
     */
    private static LazyModeSynchronized instance;
    /**
     * 私有化构造函数
     */
    private LazyModeSynchronized(){
        // 里面可以有很多操作
    }
    /**
     * 提供公开获取实例接口
     * @return
     */
    public synchronized static LazyModeSynchronized getInstance(){
        /**
         * 添加class类锁，影响了性能，加锁之后将代码进行了串行化，
         * 我们的代码块绝大部分是读操作，在读操作的情况下，代码线程是安全的
         *
         */
        if (instance == null) {
            instance = new LazyModeSynchronized();
        }
        return instance;
    }
}
```

#### 2.2.3 懒汉模式的优点

实现了懒加载，节约了内存空间。

#### 2.2.4 懒汉模式的缺点

1. 在不加锁的情况下，线程不安全，可能出现多份实例；
1. 在加锁的情况下，会使程序串行化，使系统有严重的性能问题。

懒汉模式中加锁的问题，对于`getInstance()`方法来说，绝大部分的操作都是读操作，读操作是线程安全的，所以我们没必让每个线程必须持有锁才能调用该方法，我们需要调整加锁的问题。由此也产生了一种新的实现模式：**双重检查锁模式**。

### 2.3 双重检查锁模式

#### 2.3.1 双重检查锁模式的一般实现

```java
public class DoubleCheckLockMode {

    private static DoubleCheckLockMode instance;

    /**
     * 私有化构造函数
     */
    private DoubleCheckLockMode(){

    }
    /**
     * 提供公开获取实例接口
     * @return
     */
    public static DoubleCheckLockMode getInstance(){
        // 第一次判断，如果这里为空，不进入抢锁阶段，直接返回实例
        if (instance == null) {
            synchronized (DoubleCheckLockMode.class) {
                // 抢到锁之后再次判断是否为空
                if (instance == null) {
                    instance = new DoubleCheckLockMode();
                }
            }
        }
        return instance;
    }
}
```

双重检查锁模式解决了单例、性能、线程安全问题，但是这种写法同样存在问题：在多线程的情况下，可能会出现空指针问题，出现问题的原因是`JVM`在实例化对象的时候会进行优化和指令重排序操作。

#### 2.3.2 什么是指令重排？

```java
private SingletonObject(){
	  // 第一步
     int x = 10;
	  // 第二步
     int y = 30;
     // 第三步
     Object o = new Object(); 
}
```

上面的构造函数`SingletonObject()`，`JVM` 会对它进行指令重排序，所以执行顺序可能会乱掉，但是不管是那种执行顺序，`JVM` 最后都会保证所以实例都完成实例化。 **如果构造函数中操作比较多时，为了提升效率，`JVM` 会在构造函数里面的属性未全部完成实例化时，就返回对象**。双重检测锁出现空指针问题的原因就是出现在这里，当某个线程获取锁进行实例化时，其他线程就直接获取实例使用，由于`JVM`指令重排序的原因，其他线程获取的对象也许不是一个完整的对象，所以在使用实例的时候就**会出现空指针异常问题**。

#### 2.3.3 双重检查锁模式优化

要解决双重检查锁模式带来空指针异常的问题，只需要使用`volatile`关键字，`volatile`关键字严格遵循`happens-before`原则，即：在读操作前，写操作必须全部完成。

```java
public class DoubleCheckLockModelVolatile {
    /**
     * 添加volatile关键字，保证在读操作前，写操作必须全部完成
     */
    private static volatile DoubleCheckLockModelVolatile instance;
    /**
     * 私有化构造函数
     */
    private DoubleCheckLockModelVolatile(){

    }
    /**
     * 提供公开获取实例接口
     * @return
     */
    public static DoubleCheckLockModelVolatile getInstance(){

        if (instance == null) {
            synchronized (DoubleCheckLockModelVolatile.class) {
                if (instance == null) {
                    instance = new DoubleCheckLockModelVolatile();
                }
            }
        }
        return instance;
    }
}
```

### 2.4 静态内部类模式

静态内部类模式也称单例持有者模式，实例由内部类创建，由于 `JVM` 在加载外部类的过程中, 是不会加载静态内部类的, 只有内部类的属性/方法被调用时才会被加载, 并初始化其静态属性。静态属性由`static`修饰，保证只被实例化一次，并且严格保证实例化顺序。

```java
public class StaticInnerClassMode {

    private StaticInnerClassMode(){

    }

    /**
     * 单例持有者
     */
    private static class InstanceHolder{
        private  final static StaticInnerClassMode instance = new StaticInnerClassMode();

    }

    /**
     * 提供公开获取实例接口
     * @return
     */
    public static StaticInnerClassMode getInstance(){
        // 调用内部类属性
        return InstanceHolder.instance;
    }
}
```

这种方式跟饿汉式方式采用的机制类似，但又有不同。两者都是采用了类装载的机制来保证初始化实例时只有一个线程。不同的地方：

1. 饿汉式方式是只要`Singleton`类被装载就会实例化，没有`Lazy-Loading`的作用；
1. 静态内部类方式在`Singleton`类被装载时并不会立即实例化，而是在需要实例化时，调用`getInstance()`方法，才会装载`SingletonInstance`类，从而完成`Singleton`的实例化。

类的静态属性只会在第一次加载类的时候初始化，所以在这里，`JVM`帮助我们保证了线程的安全性，在类进行初始化时，别的线程是无法进入的。

所以这种方式**在没有加任何锁的情况下，保证了多线程下的安全，并且没有任何性能影响和空间的浪费**。

### 2.5 枚举类实现单例模式

因为枚举类型是线程安全的，并且只会装载一次，设计者充分的利用了枚举的这个特性来实现单例模式，枚举的写法非常简单，而且枚举类型是所用单例实现中**唯一一种不会被破坏的单例实现模式**。

```java
public class EnumerationMode {
    
    private EnumerationMode(){
        
    }

    /**
     * 枚举类型是线程安全的，并且只会装载一次
     */
    private enum Singleton{
        INSTANCE;

        private final EnumerationMode instance;

        Singleton(){
            instance = new EnumerationMode();
        }

        private EnumerationMode getInstance(){
            return instance;
        }
    }

    public static EnumerationMode getInstance(){

        return Singleton.INSTANCE.getInstance();
    }
}
```

#### 适用场合：

1. 需要频繁的进行创建和销毁的对象；
1. 创建对象时耗时过多或耗费资源过多，但又经常用到的对象；
1. 工具类对象；
1. 频繁访问数据库或文件的对象。

## 三、单例模式的问题及解决办法

除枚举方式外, 其他方法都会通过反射的方式破坏单例

### 3.1 单例模式的破坏

```java
/**
 * 以静态内部类实现为例
 * @throws Exception
 */
@Test
public void singletonTest() throws Exception {
    Constructor constructor = StaticInnerClassMode.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    StaticInnerClassMode obj1 = StaticInnerClassMode.getInstance();
    StaticInnerClassMode obj2 = StaticInnerClassMode.getInstance();
    StaticInnerClassMode obj3 = (StaticInnerClassMode) constructor.newInstance();

    System.out.println("输出结果为："+obj1.hashCode()+"," +obj2.hashCode()+","+obj3.hashCode());
}
```

控制台打印：

```
输出结果为：1454171136,1454171136,1195396074
```

从输出的结果我们就可以看出`obj1`和`obj2`为同一对象，`obj3`为新对象。`obj3`是我们通过反射机制，进而调用了私有的构造函数，然后产生了一个新的对象。

### 3.2 如何阻止单例破坏

可以在构造方法中进行判断，若已有实例, 则阻止生成新的实例，解决办法如下:

```java
public class StaticInnerClassModeProtection {

    private static boolean flag = false;

    private StaticInnerClassModeProtection(){
        synchronized(StaticInnerClassModeProtection.class){
            if(flag == false){
                flag = true;
            }else {
                throw new RuntimeException("实例已经存在，请通过 getInstance()方法获取！");
            }
        }
    }

    /**
     * 单例持有者
     */
    private static class InstanceHolder{
        private  final static StaticInnerClassModeProtection instance = new StaticInnerClassModeProtection();
    }

    /**
     * 提供公开获取实例接口
     * @return
     */
    public static StaticInnerClassModeProtection getInstance(){
        // 调用内部类属性
        return InstanceHolder.instance;
    }
}
```

测试：

```java
/**
 * 在构造方法中进行判断，若存在则抛出RuntimeException
 * @throws Exception
 */
@Test
public void destroyTest() throws Exception {
    Constructor constructor = StaticInnerClassModeProtection.class.getDeclaredConstructor();
    constructor.setAccessible(true);

    StaticInnerClassModeProtection obj1 = StaticInnerClassModeProtection.getInstance();
    StaticInnerClassModeProtection obj2 = StaticInnerClassModeProtection.getInstance();
    StaticInnerClassModeProtection obj3 = (StaticInnerClassModeProtection) constructor.newInstance();

    System.out.println("输出结果为："+obj1.hashCode()+"," +obj2.hashCode()+","+obj3.hashCode());
}
```

控制台打印：

```
Caused by: java.lang.RuntimeException: 实例已经存在，请通过 getInstance()方法获取！
	at cn.van.singleton.demo.mode.StaticInnerClassModeProtection.<init>(StaticInnerClassModeProtection.java:22)
	... 35 more
```


## 四、总结

### 4.1 各种实现的对比

| 名称| 饿汉模式 | 懒汉模式 | 双重检查锁模式 | 静态内部类实现 | 枚举类实现 |
| -- | -- | -- | -- | -- | -- |
| 可用性 | 可用 | 不推荐使用 | 推荐使用 | 推荐使用 | 推荐使用 |
| 特点 | 不能实现懒加载，可能造成空间浪费 | 不加锁线程不安全；加锁性能差 | 线程安全；延迟加载；效率较高 | 避免了线程不安全，延迟加载，效率高。 | 写法简单；线程安全；只装载一次 | 


### 4.2 技术交流

1. [风尘博客](https://www.dustyblog.cn/)
1. [风尘博客-博客园](https://www.cnblogs.com/vandusty)
1. [风尘博客-CSDN](https://blog.csdn.net/weixin_42036952)

关注公众号，了解更多：


![风尘博客](https://github.com/vanDusty/SpringBoot-Home/blob/master/dusty_blog.png?raw=true)
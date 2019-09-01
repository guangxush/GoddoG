## 分布式事务
分布式事务就是指事务的参与者、支持事务的服务器、资源服务器以及事务管理器分别位于不同的分布式系统的不同节点之上。
简单的说，就是一次大的操作由不同的小操作组成，这些小的操作分布在不同的服务器上，且属于不同的应用，分布式事务需要保证这些小操作要么全部成功，要么全部失败。分布式事务就是为了保证不同数据库的数据一致性。
由于单个数据库性能有限，可能需要对数据库进行扩展，或者分布在不同的地区，保证高可用，那么这时候一个业务动作需要把结果保存在不同的DB中。传统的单一数据库不能够满足该业务。
![分布式数据库存储数据](https://upload-images.jianshu.io/upload_images/7632302-b24d78952e4ff4c1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
目前很多系统采用SOA化，就是业务的服务化。比如原来单机支撑了整个电商网站，现在对整个网站进行拆解，分离出了订单中心、用户中心、库存中心。对于订单中心，有专门的数据库存储订单信息，用户中心也有专门的数据库存储用户信息，库存中心也会有专门的数据库存储库存信息。这时候如果要同时对订单和库存进行操作，那么就会涉及到订单数据库和库存数据库，为了保证数据一致性，就需要用到分布式事务。
![电商交易](https://upload-images.jianshu.io/upload_images/7632302-4f811c212340a0f9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
如何保证订单、交易、库存能够在一笔交易之后完成事务，保证数据的一致性，需要用到分布式事务。
## 事务的基本特性

- 原子性（A）在整个事务中的所有操作，要么全部完成，要么全部不做，没有中间状态。对于事务在执行中发生错误，所有的操作都会被回滚，整个事务就像从没被执行过一样。

- 一致性（C）事务的执行必须保证系统的一致性，就拿转账为例，A有500元，B有300元，如果在一个事务里A成功转给B50元，那么不管并发多少，不管发生什么，只要事务执行成功了，那么最后A账户一定是450元，B账户一定是350元。

- 隔离性（I）事务与事务之间不会互相影响，一个事务的中间状态不会被其他事务感知。

- 持久性（D）事务完成了，那么事务对数据所做的变更就完全保存在了数据库中，即使发生停电，系统宕机也是如此。


## 二阶段提交框架
这里采用二阶段事务提交TCC，解决分布式事务一致性的问题，TCC提供了一个编程框架，将整个业务逻辑分为三块：Try、Confirm和Cancel三个操作。以在线下单为例，Try阶段会去扣库存，Confirm阶段则是去更新订单状态，如果更新订单失败，则进入Cancel阶段，会去恢复库存。总之，TCC就是通过代码人为实现了两阶段提交。
![TCC二阶段提交](https://upload-images.jianshu.io/upload_images/7632302-58a6fd761067b882.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


## 代码逻辑
这里采用三个相同的数据库Business,BusinessOne,BusinessTwo模拟事务的提交，保证同一笔数据在三个数据库同时成功的时候，才能一致。
1. 首先业务动作出发之后，本地Business事务先提交，并发起Try操作，如果BusinessOne和BusinessTwo都执行成功，先将status更新为1。
2. Business监听到成功消息之后，执行Confirm操作，status更新为2，并将执行成功的结果返回给用户，交易完成。

![TCC成功](https://upload-images.jianshu.io/upload_images/7632302-072b516cdb2bc1c0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

1. 首先业务动作出发之后，本地Business事务先提交，并发起Try操作，如果BusinessOne执行成功，先将status更新为1，BusinessTwo执行失败，直接返回；
2. Business监听到失败消息之后，执行Cancel操作，将BusinessOne中的status更改为0，表示已经撤销；同时撤销本地数据库提交的事务，并将执行失败的结果返回给用户。

![TCC失败](https://upload-images.jianshu.io/upload_images/7632302-46ae4124323903f4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


为了防止Confirm或者Cancel出现网络问题导致不一致，设置了业务活动管理器，定期去数据库中捞取数据检查是否一致，不一致的数据及时进行回滚。
## 具体实现
核心业务逻辑
```
@Service
public class RequestService {

    @Autowired
    private PostRequestService postRequestService;

 @Transactional(rollbackFor = Exception.class)   
 public boolean doBusiness(String[] urls, BusinessVO businessVO) {
        List<String> sendUrl = new ArrayList<>();
        for (String url : urls) {
            if (doTry(url, businessVO)) {
                sendUrl.add(url);
            }
        }
        if (sendUrl.size() == urls.length) {
            for (String url : sendUrl) {
                doConfirm(url, businessVO);
            }
            return true;
        } else {
            for (String url : sendUrl) {
                doCancel(url, businessVO);
            }
            return false;
        }
    }

    public boolean doTry(String url, BusinessVO businessVO) {
        businessVO.setStatus(1);
        return postRequestService.jsonRequest(url, businessVO);
    }

    public boolean doConfirm(String url, BusinessVO businessVO) {
        url = url.replace("insert", "update");
        businessVO.setStatus(2);
        return postRequestService.jsonRequest(url, businessVO);
    }

    public boolean doCancel(String url, BusinessVO businessVO) {
        businessVO.setStatus(3);
        return postRequestService.jsonRequest(url, businessVO);
    }
}
```
## 源码参考
[源码参考](https://github.com/guangxush/GoddoG)



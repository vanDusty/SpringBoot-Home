package cn.van.rocket.demo.bean;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * @公众号： 风尘博客
 * @Classname LocalTransactionExecuterWrapper
 * @Description 本地事务执行器
 * @Date 2020/3/5 3:46 下午
 * @Author by Van
 */
@Slf4j
public class LocalTransactionExecuterWrapper implements LocalTransactionExecuter {

    private final LocalTransactionExecuter localTransactionExecuter;

    public LocalTransactionExecuterWrapper(LocalTransactionExecuter localTransactionExecuter) {
        this.localTransactionExecuter = localTransactionExecuter;
    }

    @Override
    public TransactionStatus execute(Message msg, Object arg) {
        try {
            return localTransactionExecuter.execute(msg, arg);
        } catch (Throwable throwable) {
            log.error("localTransactionExecuter.execute ex caught", throwable);
            return TransactionStatus.RollbackTransaction;
        }
    }

}

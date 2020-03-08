package cn.van.rocket.demo.bean;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @公众号： 风尘博客
 * @Classname LocalTransactionCheckerImpl
 * @Description MQ 发送事务消息本地Check接口实现类
 * @Date 2020/3/5 4:28 下午
 * @Author by Van
 */
@Slf4j
@Component("localTransactionChecker")
public class LocalTransactionCheckerImpl implements LocalTransactionChecker {

    @Override
    public TransactionStatus check(Message message) {
        // 实现规范，参考 https://help.aliyun.com/document_detail/29548.html 事务回查机制说明
        log.info("message={}", message);

        final String topic = message.getTopic();
        log.info("topic:____{}", topic);
        return TransactionStatus.RollbackTransaction;
    }

}

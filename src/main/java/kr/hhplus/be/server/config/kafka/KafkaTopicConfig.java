package kr.hhplus.be.server.config.kafka;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String ISSUE_COUPON_TOPIC = "issueCoupon";
    public static final int partitionCount = 3;
    public static final int replicaCount = 1;

    @Bean
    public NewTopic issueCouponTopic() {
        return TopicBuilder
                .name(ISSUE_COUPON_TOPIC)
                .partitions(partitionCount)
                .replicas(replicaCount)
                .build();
    }

}

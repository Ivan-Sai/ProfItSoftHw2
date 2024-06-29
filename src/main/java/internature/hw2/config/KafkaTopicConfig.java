package internature.hw2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class KafkaTopicConfig {
//
//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapAddress;
//
//    @Value("${kafka.topic.email}")
//    private String emailTopic ;
//
//    @Bean
//    public KafkaAdmin kafkaAdmin() {
//        Map<String, Object> configs = Map.of(
//                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//        return new KafkaAdmin(configs);
//    }
//
//    @Bean
//    public NewTopic emailTopic() {
//        return new NewTopic(emailTopic, 2, (short) 1);
//    }
}

package com.bootcamp.ehs.producer;

import com.bootcamp.ehs.DTO.CreditDTO;
import com.bootcamp.ehs.model.Transaction;
import com.bootcamp.ehs.service.impl.TransactionPayCreditServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KafkaPayCreditProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPayCreditProducer.class);
    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplateCredit; // Envia mensajes a topico de kafka

    public KafkaPayCreditProducer(KafkaTemplate<String, String> kafkaTemplate,
                                  ObjectMapper objectMapper) {
        this.kafkaTemplateCredit = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public Mono<Void> sendMessagePayCredit(CreditDTO credit) throws JsonProcessingException {
        LOGGER.info("En SendMessagePayCredit");
        String message = objectMapper.writeValueAsString(credit);
        LOGGER.info("Producing message PayCredit{} ", message);
        this.kafkaTemplateCredit.send("payCredit-topic", message);
        return Mono.empty();
    }
}

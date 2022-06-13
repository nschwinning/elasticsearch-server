package com.example.elasticsearchserver.configuration;

import com.example.elasticsearchserver.data.Person;
import com.example.elasticsearchserver.mapper.PersonMapper;
import com.example.elasticsearchserver.service.PersonDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class KafkaConsumerConfiguration {

    private final PersonMapper personMapper;
    private final PersonDocumentService personDocumentService;

    @Bean
    public Consumer<Message<Person>> complexQuoteConsumer() {
        return message -> createPersonDocument(message);
    }

    @Bean
    public Serde<Person> quoteSerde() {
        return new JsonSerde<>(Person.class);
    }

    private void createPersonDocument(Message<Person> message) {
        personDocumentService.createPerson(personMapper.map(message.getPayload()));
    }

}

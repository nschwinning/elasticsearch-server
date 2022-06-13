package com.example.elasticsearchserver.mapper;

import com.example.elasticsearchserver.data.Person;
import com.example.elasticsearchserver.data.PersonDocument;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonDocument map(Person person) {
        return PersonDocument.builder()
                .firstName(person.firstName())
                .lastName(person.lastName())
                .middleName(person.middleName())
                .favouriteQuote(person.favouriteQuote())
                .build();
    }

}

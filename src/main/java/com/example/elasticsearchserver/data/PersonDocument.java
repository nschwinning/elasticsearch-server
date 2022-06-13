package com.example.elasticsearchserver.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.io.Serializable;

@Document(indexName = PersonDocument.PERSON_INDEX)
@Setting(settingPath = PersonDocument.PERSON_DOCUMENT_SETTINGS_JSON)
@Mapping(mappingPath = PersonDocument.PERSON_DOCUMENT_MAPPINGS_JSON)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonDocument implements Serializable {

    public static final String PERSON_INDEX = "person-document-index";
    public static final String PERSON_DOCUMENT_SETTINGS_JSON = "elasticsearch/person-document-settings.json";
    public static final String PERSON_DOCUMENT_MAPPINGS_JSON = "elasticsearch/person-document-mappings.json";

    @Id
    private String personId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String favouriteQuote;

}

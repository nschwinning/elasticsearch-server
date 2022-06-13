package com.example.elasticsearchserver.service;

import com.example.elasticsearchserver.data.Person;
import com.example.elasticsearchserver.data.PersonDocument;
import com.example.elasticsearchserver.data.PersonDocumentRepository;
import com.example.elasticsearchserver.mapper.SearchStringToQueryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.elasticsearchserver.data.PersonDocument.PERSON_INDEX;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonDocumentService {

    public static final String PERSON_DOCUMENT_MAPPINGS_RESOURCE = "classpath:elasticsearch/person-document-mappings.json";

    @Value(PERSON_DOCUMENT_MAPPINGS_RESOURCE)
    Resource resourceFile;

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final PersonDocumentRepository personDocumentRepository;
    private final SearchStringToQueryMapper searchStringToQueryMapper;

    public void createPerson(PersonDocument personDocument) {
        personDocumentRepository.save(personDocument);
    }

    public List<PersonDocument> findAll() {
        return StreamSupport.stream(personDocumentRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Page<PersonDocument> findBySearchString(String searchString, Pageable pageable) {
        var query = searchStringToQueryMapper.map(searchString);
        var searchHits = elasticsearchTemplate.search(query, PersonDocument.class, IndexCoordinates.of(PERSON_INDEX));
        var page = SearchHitSupport.searchPageFor(searchHits, pageable);
        var result = page.getContent().stream().map(SearchHit::getContent).collect(toList());
        return new PageImpl<>(result, pageable, page.getTotalElements());
    }

    public void createPersonDocumentIndex() {
        createDocumentIndex(PersonDocument.class, resourceFile);
    }

    public void deletePersonDocumentIndex() {
        deleteDocumentIndex(PersonDocument.class);
    }

    private void createDocumentIndex(Class<?> clazz, Resource resource) {

        var indexOperations = elasticsearchTemplate.indexOps(clazz);
        indexOperations.create();

        try(var reader = new InputStreamReader(resource.getInputStream(), UTF_8))  {
            var mapping = FileCopyUtils.copyToString(reader);
            var doc = Document.parse(mapping);
            indexOperations.putMapping(doc);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void deleteDocumentIndex(Class<?> clazz) {

        var indexOperations = elasticsearchTemplate.indexOps(clazz);
        indexOperations.delete();
    }

}

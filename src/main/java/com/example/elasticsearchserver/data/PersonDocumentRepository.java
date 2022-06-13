package com.example.elasticsearchserver.data;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PersonDocumentRepository extends ElasticsearchRepository<PersonDocument, Long> {}

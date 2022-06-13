package com.example.elasticsearchserver.web.controller;

import com.example.elasticsearchserver.data.PersonDocument;
import com.example.elasticsearchserver.service.PersonDocumentService;
import com.example.elasticsearchserver.web.model.SearchResource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PersonController {

    private final PersonDocumentService personDocumentService;

    @GetMapping("/api/persons")
    public ResponseEntity<List<PersonDocument>> getAllPersons() {
        return ResponseEntity.ok(personDocumentService.findAll());
    }

    @PostMapping("/api/persons")
    public ResponseEntity<Page<PersonDocument>> searchPersons(@RequestBody SearchResource searchResource) {
        var pageable = PageRequest.of(searchResource.getPage(), searchResource.getSize());
        return  ResponseEntity.ok(personDocumentService.findBySearchString(searchResource.getSearchString(), pageable));
    }

}

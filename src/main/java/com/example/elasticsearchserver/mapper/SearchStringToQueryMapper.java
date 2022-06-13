package com.example.elasticsearchserver.mapper;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Component
public class SearchStringToQueryMapper {

    private final String[] WILD_CARD_SEARCH_FIELDS = {
            "firstName",
            "lastName",
            "middleName",
            "favouriteQuote"
    };

    private final int MIN_SEARCH_STR_LENGTH = 3;

    public NativeSearchQuery map(String searchString) {

        var boolQueryBuilder = QueryBuilders.boolQuery();

        wildCardMatch(boolQueryBuilder, searchString);

        return new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).build();

    }

    private void wildCardMatch(BoolQueryBuilder boolQueryBuilder, String queryString) {
        if (!hasText(queryString)) {
            return;
        }

        var wildcardTerms = Arrays.stream(queryString.split(" "))
                .filter(term -> term.length() >= MIN_SEARCH_STR_LENGTH)
                .map(term -> "*" + term.toLowerCase() + "*")
                .collect(Collectors.toList());

        for (String term : wildcardTerms) {
            var wildcardQuery = boolQuery();
            for (String fieldName : WILD_CARD_SEARCH_FIELDS) {
                wildcardQuery.should(wildcardQuery(fieldName, term));
            }
            wildcardQuery.minimumShouldMatch(1);
            boolQueryBuilder.must(wildcardQuery);
        }
    }

}

package com.example.elasticsearchserver.web.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResource {

    private int page = 0;
    private int size = 20;
    private String searchString;

}

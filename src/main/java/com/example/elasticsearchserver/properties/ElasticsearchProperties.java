package com.example.elasticsearchserver.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import javax.validation.constraints.NotNull;

@Data
@ConfigurationProperties(prefix = "elasticsearch.config")
public class ElasticsearchProperties {

    @NotNull
    private String hostname;
    private int port;

}

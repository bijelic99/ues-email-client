package com.ftn.ues.email_client.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableElasticsearchRepositories
public class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

    @Value("${indexing.elasticsearch.endpoint}")
    private String endpoint;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration configuration = ClientConfiguration.builder()
                .connectedTo(endpoint)
                .build();

        return RestClients.create(configuration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchOperations(RestHighLevelClient client, EntityMapper mapper) {
        return new ElasticsearchRestTemplate(client, mapper);
    }

    @Bean
    public EntityMapper mapper(ObjectMapper mapper){
        return new EntityMapper() {
            @Override
            public String mapToString(Object o) throws IOException {
                return mapper.writeValueAsString(o);
            }

            @Override
            public <T> T mapToObject(String s, Class<T> aClass) throws IOException {
                return mapper.readValue(s, aClass);
            }

            @Override
            public Map<String, Object> mapObject(Object o) {
                Map<String, Object> map = new HashMap<>();
                map.put("dateTime", o);
                return map;
            }

            @Override
            public <T> T readObject(Map<String, Object> map, Class<T> aClass) {
                return (T) map.get("dateTime");
            }
        };
    }
}

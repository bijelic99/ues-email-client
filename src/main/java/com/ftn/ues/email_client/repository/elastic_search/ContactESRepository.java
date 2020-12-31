package com.ftn.ues.email_client.repository.elastic_search;

import com.ftn.ues.email_client.dao.elastic_search.Contact;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactESRepository extends ElasticsearchRepository<Contact, Long> {

}

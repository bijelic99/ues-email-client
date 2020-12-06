package com.ftn.ues.emailclient.repository.elastic_search;

import com.ftn.ues.emailclient.dao.elastic_search.Contact;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ContactRepository extends ElasticsearchRepository<Contact, Long> {

}

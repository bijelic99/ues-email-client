package com.ftn.ues.email_client.repository.elastic_search;

import com.ftn.ues.email_client.dao.elastic_search.Attachment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AttachmentESRepository extends ElasticsearchRepository<Attachment, Long> {
}

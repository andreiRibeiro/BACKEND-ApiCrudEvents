package br.com.aaribeiro.eventsApi.repository;

import br.com.aaribeiro.eventsApi.document.EventDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<EventDocument, Long> {

    Page<EventDocument> findByNameIgnoreCaseContainingOrderByNameAsc(String name, Pageable pageable);
}

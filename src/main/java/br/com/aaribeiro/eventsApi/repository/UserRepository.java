package br.com.aaribeiro.eventsApi.repository;

import br.com.aaribeiro.eventsApi.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, Long> {

    Optional<UserDocument> findByEmail(String email);
}

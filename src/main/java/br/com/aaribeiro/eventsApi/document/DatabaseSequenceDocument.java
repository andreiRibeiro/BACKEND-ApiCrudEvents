package br.com.aaribeiro.eventsApi.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@Document(collection = "sequences")
public class DatabaseSequenceDocument {

    @Id
    private String id;

    private long seq;
}

package br.com.aaribeiro.eventsApi.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Document(collection = "events")
public class EventDocument implements Serializable {

    @Transient
    @JsonIgnore
    public static final String EVENTS_SEQUENCE = "events_sequence";

    @Id
    private long id;

    @NotBlank(message = "{name.not.blank}")
    @NotNull(message = "{name.not.null}")
    private String name;

    @NotNull(message = "{dateOfEvent.not.null}")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfEvent;

    private String user;
}
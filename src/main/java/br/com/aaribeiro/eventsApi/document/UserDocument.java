package br.com.aaribeiro.eventsApi.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Document(collection = "users")
public class UserDocument implements Serializable {

    @Transient
    @JsonIgnore
    public static final String USERS_SEQUENCE = "users_sequence";

    @Id
    private long id;

    @NotBlank(message = "{name.not.blank}")
    @NotNull(message = "{name.not.null}")
    private String name;

    @NotBlank(message = "{email.not.blank}")
    @NotNull(message = "{email.not.null}")
    @Email(message = "{email.invalid}")
    private String email;

    @NotBlank(message = "{password.not.blank}")
    @NotNull(message = "{password.not.null}")
    private String password;
}

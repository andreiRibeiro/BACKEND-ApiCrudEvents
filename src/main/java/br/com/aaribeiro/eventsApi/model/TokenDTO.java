package br.com.aaribeiro.eventsApi.model;

import lombok.Data;

@Data
public class TokenDTO {
    private String login;
    private String token;
}

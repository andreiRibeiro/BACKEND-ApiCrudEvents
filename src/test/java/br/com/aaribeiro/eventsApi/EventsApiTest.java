package br.com.aaribeiro.eventsApi;

import br.com.aaribeiro.eventsApi.controller.EventController;
import br.com.aaribeiro.eventsApi.controller.EventControllerAdvice;
import br.com.aaribeiro.eventsApi.document.EventDocument;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

public class EventsApiTest extends EventsApiApplicationTest {

    private MockMvc mockMvc;

    @Autowired
    private EventController eventController;

    @Autowired
    private EventControllerAdvice eventControllerAdvice;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(eventController).setControllerAdvice(eventControllerAdvice).build();
    }

    private String getEventJson() throws JsonProcessingException {
        EventDocument eventDocument = new EventDocument();
        eventDocument.setName("Andrei");
        eventDocument.setDateOfBirth(LocalDate.of(1990, 05, 03));

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(eventDocument);
    }

    @Test
    public void mustRegisterCorrectEvent_ReturnStatusCode201() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.getEventJson())
        )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void mustRegisterIncorrectEvent_ReturnStatusCode400() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void mustUpdateExistingtEvent_ReturnStatusCode200() throws Exception {
        /*POST Event*/
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                .post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.getEventJson())).andReturn();

        EventDocument eventDocument = mapper.readValue(result.getResponse().getContentAsString(), EventDocument.class);
        eventDocument.setName("New event");

        /*PUT Event*/
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(eventDocument))
        )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void mustConsultEventThatExists_ReturnStatusCode200() throws Exception {
        /*POST Event*/
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.getEventJson())
        );

        /*GET Event*/
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/events/1")
        )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void mustConsultEventThatDoesNotExists_ReturnStatusCode404() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/events/99")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void mustDeleteAnEventThatExists_ReturnStatusCode200() throws Exception {
        /*POST Event*/
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.getEventJson())
        );

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.getEventJson())
        );

        /*DELETE Event*/
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/events/1")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void mustChangeExistingtEvent_ReturnStatusCode200() throws Exception {
        /*POST Event*/
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.getEventJson())
        );

        String patch = "[{\"op\":\"replace\", \"path\":\"/name\", \"value\":\"Andrei Antonio Ribeiro\"}]";

        /*PATCH Event*/
        this.mockMvc.perform(MockMvcRequestBuilders
                .patch("/events/1")
                .contentType("application/json-patch+json")
                .content(patch)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
}

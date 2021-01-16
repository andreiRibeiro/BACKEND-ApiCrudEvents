package br.com.aaribeiro.eventsApi.service;

import br.com.aaribeiro.eventsApi.document.EventDocument;
import br.com.aaribeiro.eventsApi.repository.EventRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Optional<EventDocument> getEvent(long id) throws Exception {
        return eventRepository.findById(id);
    }

    public Page<EventDocument> getEvents(Pageable pageable) throws Exception {
        return eventRepository.findAll(pageable);
    }

    public Page<EventDocument> find(String name, Pageable pageable) throws Exception {
        return eventRepository.findByNameIgnoreCaseContainingOrderByNameAsc(name, pageable);
    }

    public EventDocument updateEvent(EventDocument eventDocument) throws Exception {
        return eventRepository.save(eventDocument);
    }

    public void deleteEvent(long id) throws Exception {
        eventRepository.deleteById(id);
    }

    public EventDocument setEvent(EventDocument movieJson) throws Exception {
        return eventRepository.save(movieJson);
    }

    public EventDocument applyPatchToEvent(JsonPatch patch, EventDocument targetEvent) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode patched = patch.apply(mapper.convertValue(targetEvent, JsonNode.class));
        return mapper.treeToValue(patched, EventDocument.class);
    }
}
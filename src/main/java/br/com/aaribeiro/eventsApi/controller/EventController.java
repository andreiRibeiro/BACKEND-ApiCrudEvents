package br.com.aaribeiro.eventsApi.controller;

import br.com.aaribeiro.eventsApi.document.EventDocument;
import br.com.aaribeiro.eventsApi.service.EventService;
import br.com.aaribeiro.eventsApi.service.SequenceGeneratorService;
import com.github.fge.jsonpatch.JsonPatch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@Api(tags = "Events")
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get event.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return event."),
            @ApiResponse(code = 404, message = "Event not found."),
            @ApiResponse(code = 500, message = "General processing errors .")
    })
    public ResponseEntity<EventDocument> getEvent(@PathVariable int id) throws Exception {
        try {
            Optional<EventDocument> eventDocument = eventService.getEvent(id);
            if (!eventDocument.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(eventDocument.get());
        } catch (Exception e) {
            throw new Exception(e.getLocalizedMessage());
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get events with pagination.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return all events."),
            @ApiResponse(code = 500, message = "General processing errors.")
    })
    public ResponseEntity<Page<EventDocument>> getEvents(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) throws Exception {
        try {
            PageRequest pageable = PageRequest.of(page, size, Sort.Direction.ASC, "name");
            return ResponseEntity.status(HttpStatus.OK).body(eventService.getEvents(pageable));
        } catch (Exception e){
            throw new Exception(e.getLocalizedMessage());
        }
    }

    @GetMapping(path = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Search by name (or containing parts) with pagination.")
    public ResponseEntity<Page<EventDocument>> find(
        @RequestParam(value = "name", required = false) String name,
        @RequestParam(value = "page", defaultValue = "0", required = false) int page,
        @RequestParam(value = "size", defaultValue = "10", required = false) int size) throws Exception {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.ASC, "name");
        return ResponseEntity.status(HttpStatus.OK).body(eventService.find(name, pageable));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Set customer.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Event created with successful."),
            @ApiResponse(code = 400, message = "Something wrong with your request."),
            @ApiResponse(code = 500, message = "General processing errors.")
    })
    public ResponseEntity<EventDocument> setEvent(@RequestBody @Valid EventDocument eventDocument) throws Exception {
        eventDocument.setId(sequenceGeneratorService.generateSequence(EventDocument.EVENTS_SEQUENCE));
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.setEvent(eventDocument));
    }

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Change event.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event change success"),
            @ApiResponse(code = 400, message = "Something wrong with your request."),
            @ApiResponse(code = 500, message = "General processing errors.")
    })
    public ResponseEntity<EventDocument> changeEvent(@PathVariable int id, @RequestBody JsonPatch patch) throws Exception {
        Optional<EventDocument> eventDocument = eventService.getEvent(id);
        if (eventDocument.isPresent()){
            EventDocument eventPatched = eventService.applyPatchToEvent(patch, eventDocument.get());
            eventService.updateEvent(eventPatched);
            return ResponseEntity.ok(eventPatched);
        }
        return null;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update event.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Update the events. If the event does not exist, a new event will be created."),
            @ApiResponse(code = 400, message = "Something wrong with your request.")
    })
    public ResponseEntity<EventDocument> updateEvent(@RequestBody @Valid EventDocument eventDocument) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateEvent(eventDocument));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete event.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event deleted"),
            @ApiResponse(code = 404, message = "Event not found")
    })
    public ResponseEntity deleteEvent(@PathVariable int id){
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
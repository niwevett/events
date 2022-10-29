package com.futuapi.events.controller;

import com.futuapi.events.model.Event;
import com.futuapi.events.model.Option;
import com.futuapi.events.repository.EventRepository;
import com.futuapi.events.repository.OptionRepository;
import com.futuapi.events.utils.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("api/v1/")
public class EventController {

    @Autowired
    @Qualifier("eventRepository")
    private EventRepository eventRepository;
    @Autowired
    @Qualifier("optionRepository")
    private OptionRepository optionRepository;
    //get Events list
    @GetMapping("events/list")
    public Map<String, Object> getAllEvents() throws ResponseStatusException {
        try {
            Iterable<Event> allEvents = eventRepository.findAll();
            for (Event event : allEvents) {
                updateEventWithSelfRef(event);
            }
            return ResponseModel.successResponse(allEvents);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"There are no events to show");
        }
    }

    //get One event
    @GetMapping("events/{id}")
    public Map<String, Object> getEventById(@PathVariable(value = "id") Long eventId) throws ResponseStatusException {
            if (eventId == null){
                return ResponseModel.errorResponse("Could not find event id from request");
            }
            Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Event not found with id:" + eventId));
            updateEventWithSelfRef(event);
            return ResponseModel.successResponse(event);
    }

    //post Event
    @PostMapping("events")
    public Map<String, Object> createEvent(@Valid @RequestBody Event event) throws ResponseStatusException  {
        try {
            event = eventRepository.save(event);
            Long id = event.getId();
            ArrayList<String> dates = new ArrayList<String>();
            Iterable<Option> options = optionRepository.findByEvent(id);
            for (Option option : options) {
                dates.add(option.getValue());
            }
            event.setDates(dates.toString());
            eventRepository.save(event);
            updateEventWithSelfRef(event);
            return ResponseModel.successResponse(event);
        }
        catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,("Please check your post body and try again")
            );
        }
    }

    private void updateEventWithSelfRef(Event event) throws RestClientException {
        event.add(linkTo(methodOn(EventController.class).getEventById(event.getId())).withSelfRel());
    }
}

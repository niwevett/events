package com.futuapi.events.controller;

import com.futuapi.events.model.Event;
import com.futuapi.events.model.Option;
import com.futuapi.events.repository.EventRepository;
import com.futuapi.events.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    CollectionModel<EntityModel<Event>> getAllEvents() throws ResponseStatusException {
        try {
            List<EntityModel<Event>> events = eventRepository.findAll().stream()
                    .map(event -> EntityModel.of(event,
                            linkTo(methodOn(EventController.class).getEventById(event.getId())).withSelfRel(),
                            linkTo(methodOn(EventController.class).getAllEvents()).withRel("events")))
                    .collect(Collectors.toList());
            return CollectionModel.of(events, linkTo(methodOn(EventController.class).getAllEvents()).withSelfRel());
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"There are no events to show");
        }
    }

    //get One event
    @GetMapping("events/{id}")
    EntityModel<Event> getEventById(@PathVariable(value = "id") Long eventId) throws ResponseStatusException {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with id:" + eventId));
        return EntityModel.of(event, //
                linkTo(methodOn(EventController.class).getEventById(eventId)).withSelfRel(),
                linkTo(methodOn(EventController.class).getAllEvents()).withRel("events"));
    }
    //post Event
    @PostMapping("events")
    EntityModel<Event> createEvent(@Valid @RequestBody Event event) throws ResponseStatusException  {
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
            return EntityModel.of(event, //
                    linkTo(methodOn(EventController.class).getEventById(event.getId())).withSelfRel(),
                    linkTo(methodOn(EventController.class).getAllEvents()).withRel("events"));
        }
        catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,("Please check your post body and try again")
            );
        }
    }
}

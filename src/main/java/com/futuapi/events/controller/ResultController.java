package com.futuapi.events.controller;

import com.futuapi.events.model.Event;
import com.futuapi.events.model.Option;
import com.futuapi.events.repository.EventRepository;
import com.futuapi.events.repository.OptionRepository;
import com.futuapi.events.repository.VoteRepository;
import com.futuapi.events.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;


@RestController
@RequestMapping("api/v1/")
public class ResultController {
    @Autowired
    @Qualifier("eventRepository")
    private EventRepository eventRepository;
    @Autowired
    @Qualifier("optionRepository")
    private OptionRepository optionRepository;

    @Autowired
    @Qualifier("voteRepository")
    private VoteRepository voteRepository;

    //returns value that suits for all participants if any exists
    @GetMapping(value="/events/{id}/results")
    public Map<String, List> getResult(@PathVariable(value = "id") Long eventId) throws ResponseStatusException {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found with id:" + eventId));
        Iterable<Option> options = optionRepository.findByEvent(eventId);
        return Result.getResult(options);
    }
}

package com.futuapi.events.controller;

import com.futuapi.events.model.Event;
import com.futuapi.events.model.Option;
import com.futuapi.events.model.Vote;
import com.futuapi.events.repository.EventRepository;
import com.futuapi.events.repository.OptionRepository;
import com.futuapi.events.repository.VoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;


@RestController
@RequestMapping("api/v1/")
public class ResultController {
    Logger logger = LoggerFactory.getLogger(LogController.class);
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
        List<String> suitableOptions = new ArrayList<String>();
        Iterable<Option> options = optionRepository.findByEvent(eventId);
        List<String> allVoters = new ArrayList<String>();
        for(Option option: options){
            if(option.getVotes() != null) {
                Iterable<Vote> votes = option.getVotes();
                for(Vote vote : votes){
                    if(!allVoters.contains(vote.getEmail())) {
                        allVoters.add(vote.getEmail());
                    }
                }
            }
        }
        for(Option option: options){
            List<String> voters = new ArrayList<String>();
            if(option.getVotes() != null) {
                Iterable<Vote> votes = option.getVotes();
                for(Vote vote : votes){
                    voters.add(vote.getEmail());
                }
            }
            //logger.info(voters.toString());
            if(allVoters.containsAll(voters) && voters.containsAll(allVoters)){
                suitableOptions.add(option.getValue());
            }
        }
        //logger.info(allVoters.toString());
        Map<String, List> map = new HashMap<String, List>();
            map.put("Result: ", suitableOptions);

        return map;
    }
}

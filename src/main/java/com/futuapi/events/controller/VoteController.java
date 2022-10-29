package com.futuapi.events.controller;

import com.futuapi.events.model.Event;
import com.futuapi.events.model.Option;
import com.futuapi.events.model.Vote;
import com.futuapi.events.repository.EventRepository;
import com.futuapi.events.repository.OptionRepository;
import com.futuapi.events.repository.VoteRepository;
import com.futuapi.events.utils.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("api/v1/")
public class VoteController{

    @Autowired
    @Qualifier("voteRepository")
    private VoteRepository voteRepository;
    @Autowired
    @Qualifier("eventRepository")
    private EventRepository eventRepository;

    @Autowired
    @Qualifier("optionRepository")
    private OptionRepository optionRepository;



    @PostMapping(value="/events/{id}/votes")
    public Map<String, Object> createVote(@PathVariable(value = "id") Long eventId,@Valid @RequestBody Vote vote
                                           ) throws ResponseStatusException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found with id: " + eventId));
        Long option_id = vote.getOption().getId();
        Iterable<Option> options = event.getOptions();
        for (Option option: options){
            if (voteRepository.findByEmailAndOption(vote.getEmail(), option_id).iterator().hasNext()){
                return ResponseModel.errorResponse("You have already voted for this option.");
            }
            if(option.getId() == option_id){
                vote = voteRepository.save(vote);
                return ResponseModel.successResponse(vote);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not find option with id: " + option_id + " for event with id:" + eventId);
        //return ResponseModel.errorResponse("Could not find option with id: " + option_id + " for event with id:" + eventId);
    }

    @GetMapping(value="/events/{id}/votes")
    public	Map<String, Object>	getAllOptions(@PathVariable(value = "id") Long eventId) throws ResponseStatusException{
        try {
            Iterable<Option> votes = optionRepository.findByEvent(eventId);
            return ResponseModel.successResponse(votes);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No event found with id: " + eventId);
        }
    }
}

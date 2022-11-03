package com.futuapi.events.utils;

import com.futuapi.events.controller.LogController;
import com.futuapi.events.model.Option;
import com.futuapi.events.model.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {
    static Logger logger = LoggerFactory.getLogger(LogController.class);

    public static Map<String, List> getResult(Iterable<Option> options) {
        List<String> suitableOptions = new ArrayList<String>();
        List<String> allVoters = new ArrayList<String>();
        for (Option option : options) {
            if (option.getVotes() != null) {
                Iterable<Vote> votes = option.getVotes();
                for (Vote vote : votes) {
                    if (!allVoters.contains(vote.getEmail())) {
                        allVoters.add(vote.getEmail());
                    }
                }
            }
        }
        for (Option option : options) {
            List<String> voters = new ArrayList<String>();
            if (option.getVotes() != null) {
                Iterable<Vote> votes = option.getVotes();
                for (Vote vote : votes) {
                    voters.add(vote.getEmail());
                }
            }
            logger.debug(voters.toString());
            if (allVoters.containsAll(voters) && voters.containsAll(allVoters)) {
                suitableOptions.add(option.getValue());
            }
        }
        logger.debug(allVoters.toString());
        Map<String, List> map = new HashMap<String, List>();
        map.put("Result: ", suitableOptions);
        return map;
    }
}

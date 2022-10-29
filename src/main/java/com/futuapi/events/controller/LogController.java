package com.futuapi.events.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

    // creating a logger
    Logger logger
            = LoggerFactory.getLogger(LogController.class);

}

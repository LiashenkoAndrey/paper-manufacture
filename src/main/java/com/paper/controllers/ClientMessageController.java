package com.paper.controllers;

import com.paper.domain.ClientMessage;
import com.paper.exceptions.ClientMessageNotFoundException;
import com.paper.exceptions.ControllerException;
import com.paper.exceptions.ValidationException;
import com.paper.repositories.ClientMessageRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class ClientMessageController {

    private static final Logger logger = LogManager.getLogger(ClientMessageController.class);

    private final ClientMessageRepository repository;

    @GetMapping("/all")
    public List<ClientMessage> getAllClientMessages() {
        return repository.findAll(Sort.by("createdOn").descending());
    }

    @PostMapping("/new")
    public ClientMessage newMessage(@Valid @RequestBody ClientMessage clientMessage) {
        logger.info(clientMessage);
        return repository.save(clientMessage);
    }

    @GetMapping("/getAmountOfUnrevisedMessages")
    public int getAmountOfUnrevisedMessages() {
        return repository.getAmountOfUnrevisedMessages();
    }

    @PostMapping("/setRevised/{id}")
    private void setRevisedById(@PathVariable("id") Long id) {
        ClientMessage message = repository.findById(id).orElseThrow(ClientMessageNotFoundException::new);
        message.setIsRevised(true);
        repository.save(message);
    }

    @GetMapping("/{id}")
    public ClientMessage get(@PathVariable("id") Long id) {
        return repository.findById(id).orElseThrow(ClientMessageNotFoundException::new);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleNullPointerException(MethodArgumentNotValidException ex) {
        throw new ControllerException(new ValidationException(ex));
    }

}

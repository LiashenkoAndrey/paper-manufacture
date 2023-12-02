package com.paper.repositories;

import com.paper.domain.ClientMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientMessageRepository extends JpaRepository<ClientMessage, Long> {

    @Query(value = "select count(c) from client_message c where c.is_revised = false", nativeQuery = true)
    int getAmountOfUnrevisedMessages();
}

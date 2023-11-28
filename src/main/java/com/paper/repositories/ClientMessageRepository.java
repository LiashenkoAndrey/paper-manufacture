package com.paper.repositories;

import com.paper.domain.ClientMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientMessageRepository extends JpaRepository<ClientMessage, Long> {
}

package com.paper.services;

import com.paper.domain.Producer;
import com.paper.dto.ProducerDto;

public interface ProducerService {

    Producer save(Producer producer);

    void deleteById(Long id);
}

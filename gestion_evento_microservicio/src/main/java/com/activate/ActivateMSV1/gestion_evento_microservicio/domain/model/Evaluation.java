package com.activate.ActivateMSV1.gestion_evento_microservicio.domain.model;

import com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.exceptions.DomainException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class Evaluation {
    private Long id;
    private String comment;
    private int score;
    private Participant author;

    Logger logger = LoggerFactory.getLogger(Evaluation.class);

    public Evaluation(Long id, String comment, int score, Participant author) {
        if(score < 0 || score > 5) {
            logger.error("Failed to create event due to invalid score");
            throw new DomainException("The score of the evaluation must be between 0 and 5");
        }
        if(comment == null || comment.isEmpty()) {
            logger.error("Failed to create event due to invalid comment");
            throw new DomainException("The comment of the evaluation cannot be null or empty");
        }
        if(author == null) {
            logger.error("Failed to create event due to invalid author");
            throw new DomainException("The author of the evaluation cannot be null");
        }
        this.id = id;
        this.comment = comment;
        this.score = score;
        this.author = author;
    }
}
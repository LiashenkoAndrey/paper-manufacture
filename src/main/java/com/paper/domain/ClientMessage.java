package com.paper.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
@AllArgsConstructor
public class ClientMessage extends Model {


    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    private String email;

    private String subject;

    private String phoneNumber;

    @NotNull
    private String message;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
}

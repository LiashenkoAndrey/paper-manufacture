package com.paper.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@ToString
public class Contacts {

    private String primaryPhone;

    private String secondaryPhone;

    private String email;

    private String twitterUsername;
    private String twitterUrl;

    private String instagramUsername;
    private String instagramUrl;

    private String facebookUsername;
    private String facebookUrl;

}

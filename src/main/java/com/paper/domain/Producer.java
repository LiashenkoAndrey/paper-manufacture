package com.paper.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table
public class Producer extends Model {

    @Builder
    public Producer(Long id, String name, String description, String logotypeId, String websiteUrl) {
        super(id);
        this.name = name;
        this.description = description;
        this.logotypeId = logotypeId;
        this.websiteUrl = websiteUrl;
    }

    @Size(min = 3, max = 255)
    @NotNull
    private String name;

    @Size(min = 10, max = 500)
    @NotNull
    private String description;

    private String logotypeId;

    @URL
    @NotNull
    private String websiteUrl;
}

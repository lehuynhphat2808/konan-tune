package com.example.demo.Model;

import com.example.demo.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

//import javax.persistence.*;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
@AllArgsConstructor
@Data

public abstract class BaseModel implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
//    @Convert(converter = UUIDToStringConverter.class)
    @Column(unique = true, updatable = true, nullable = false)
    @JsonView(Views.Public.class)
    protected UUID id;

    public void setId(UUID id) {
        this.id = id;
    }

    public BaseModel() {
        this.id = UUID.randomUUID();
    }
}

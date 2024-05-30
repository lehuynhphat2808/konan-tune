package com.example.demo.Model;

import com.example.demo.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Comment extends BaseModel {
    @JsonView(Views.Public.class)
    @Column(name = "product_id")
    private UUID productId;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "product_id",  referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnoreProperties("categories")
    Product product;

    @JsonView(Views.Public.class)
    @Column(name = "user_id")
    private UUID userId;

    @JsonView(Views.Detail.class)
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id",  referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"orders", "cart","username", "phone", "email", "password"})
    User user;

    @JsonView(Views.Public.class)
    @Column(name = "comment")
    String cmt;

    @JsonView(Views.Public.class)
    @Column(name = "rate")
    Integer rate;
}

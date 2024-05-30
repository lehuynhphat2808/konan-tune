package com.example.demo.Model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.HashCodeExclude;
import com.example.demo.Utilities.Views;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product extends BaseModel{
    @JsonView(Views.Public.class)
    @Column(name = "name")
    private String name;

    @JsonView(Views.Public.class)
    @Column(name = "price")
    private BigDecimal price;

    @JsonView(Views.Public.class)
    @Column(name = "summary", columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci")
    private String summary;

    @JsonView(Views.Detail.class)
    @Column(name = "content", columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci")
    private String content;

    @JsonView(Views.Public.class)
    @Column(name = "brand")
    private String brand;

    @JsonView(Views.Public.class)
    @Column(name = "color")
    private String color;

    @JsonView(Views.Public.class)
    @ElementCollection
    private List<String> linkImages = new ArrayList<String>();

    @JsonView(Views.Detail.class)
    @Column(name = "category_id")
    private UUID categoryId;

    @JsonView(Views.HaveCategoty.class)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id",  referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"products", "parentId"})
    private Category category;

    @JsonView(Views.Detail.class)
    @Column(name = "deleted")
    private boolean deleted = false;

    @JsonView(Views.Public.class)
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id",referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnoreProperties({"orders"})
    private User user;

    @Column(name = "selled", columnDefinition = "int default 0")
    private Integer selled = 0;

    @JsonView(Views.Public.class)
    @Column(name = "quantity", columnDefinition = "int default 1000")
    private Integer quantity = 1000;

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                '}';
    }
}

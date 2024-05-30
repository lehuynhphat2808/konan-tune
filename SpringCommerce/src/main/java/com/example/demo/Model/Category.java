package com.example.demo.Model;

import com.example.demo.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "category")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category extends BaseModel {
    @JsonView(Views.Public.class)
    @Column(name = "title")
    private String title;
    @JsonView(Views.Public.class)
    @Column(name = "parentId")
    private UUID parentId;


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
//    @JsonView(Views.Public.class)
    @JsonIgnoreProperties({"categoryId", "category", "price", "brand", "color", "linkImages", "summary", "content", "userId", "user"})
    private Set<Product> products = new HashSet<>();

}

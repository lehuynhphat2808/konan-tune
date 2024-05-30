package com.example.demo.Utilities;

import com.example.demo.Model.BaseModel;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedResponse<P extends BaseModel> {
    @JsonView(Views.Public.class)
    private List<P> elements;
    @JsonView(Views.Public.class)
    private long totalElements;
    @JsonView(Views.Public.class)
    private int totalPages;
}


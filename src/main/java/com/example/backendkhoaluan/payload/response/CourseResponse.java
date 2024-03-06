package com.example.backendkhoaluan.payload.response;

import com.example.backendkhoaluan.dto.CoursesDTO;
import lombok.Data;

import java.util.List;

@Data
public class CourseResponse {
    private List<CoursesDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}

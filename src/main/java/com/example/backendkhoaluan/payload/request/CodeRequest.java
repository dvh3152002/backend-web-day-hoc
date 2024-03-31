package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.constant.Constants;
import com.example.backendkhoaluan.domain.validator.numberState.NumberNotNull;
import com.example.backendkhoaluan.entities.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CodeRequest {
    private String title;
    private String description;
    @NotBlank(message = Constants.ErrorMessageCodeValidation.CODE_NOT_BLANK)
    private String code;
    @NumberNotNull(message = Constants.ErrorMessageCodeValidation.ID_POST_NOT_BLANK)
    private Integer idPost;
}

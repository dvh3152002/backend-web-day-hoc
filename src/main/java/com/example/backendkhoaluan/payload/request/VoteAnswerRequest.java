package com.example.backendkhoaluan.payload.request;

import com.example.backendkhoaluan.enums.VoteType;
import lombok.Data;

@Data
public class VoteAnswerRequest {
    private Integer idAnswer;
    private Integer idUser;
    private VoteType voteType;
}

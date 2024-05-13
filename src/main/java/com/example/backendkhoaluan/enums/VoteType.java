package com.example.backendkhoaluan.enums;

import lombok.Data;

public enum VoteType {
    DOWNVOTE(-1),
    UPVOTE(1);
    VoteType(int direction){}
}

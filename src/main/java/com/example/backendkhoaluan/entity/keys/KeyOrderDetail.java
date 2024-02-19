package com.example.backendkhoaluan.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class KeyOrderDetail implements Serializable {
    @Column(name = "id_order")
    private int orderId;

    @Column(name = "id_course")
    private int courseId;
}

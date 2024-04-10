package com.example.backendkhoaluan.entities.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyOrderDetail implements Serializable {
    @Column(name = "id_order")
    private Integer idOrder;

    @Column(name = "id_course")
    private Integer idCourse;
}

package com.example.backendkhoaluan.entities.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class KeyOrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "id_order", nullable = false,insertable=false, updatable=false)
    private Integer idOrder;

    @Column(name = "id_course", nullable = false,insertable=false, updatable=false)
    private Integer idCourse;
}

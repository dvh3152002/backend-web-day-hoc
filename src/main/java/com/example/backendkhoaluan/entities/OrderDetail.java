package com.example.backendkhoaluan.entities;

import com.example.backendkhoaluan.entities.keys.KeyOrderDetail;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Data
@Entity
@ToString
@Table(name = "order_detail")
public class OrderDetail{
    @EmbeddedId
    private KeyOrderDetail id;

    @Column(name = "price")
    private Integer price;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne
    @MapsId("id_order")
    @JoinColumn(name = "id_order", referencedColumnName = "id", insertable = false, updatable = false)
    private Orders order;

    @ManyToOne
    @MapsId("id_course")
    @JoinColumn(name = "id_course", referencedColumnName = "id", insertable = false, updatable = false)
    private Courses course;
}

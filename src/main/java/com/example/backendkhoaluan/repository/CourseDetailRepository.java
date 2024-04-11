package com.example.backendkhoaluan.repository;

import com.example.backendkhoaluan.entities.CourseDetail;
import com.example.backendkhoaluan.entities.Courses;
import com.example.backendkhoaluan.entities.OrderDetail;
import com.example.backendkhoaluan.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CourseDetailRepository extends JpaRepository<CourseDetail,Integer>, JpaSpecificationExecutor<CourseDetail> {
    List<CourseDetail> findAllByIdUser(int idUser);

    Optional<CourseDetail> findByCourseAndIdUser(Courses courses, int id);
}

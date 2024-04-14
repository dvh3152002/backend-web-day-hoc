package com.example.backendkhoaluan.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class HelperUtils {
    public static Set<String> getAuthorities() {
        Set<String> authoritiesSet = new HashSet<>();

        // Xác thực người dùng và lấy thông tin xác thực
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra xem người dùng đã được xác thực chưa
        if (authentication != null && authentication.isAuthenticated()) {
            // Lấy danh sách các quyền của người dùng từ đối tượng Authentication
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                authoritiesSet.add(authority.getAuthority());
            }
        } else {
            System.out.println("Người dùng chưa được xác thực.");
        }

        return authoritiesSet;
    }

    public static Date getLimitDateTime(float month){
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Thêm số tháng đã nhập vào ngày và giờ hiện tại
        LocalDateTime futureDateTime = currentDateTime.plusMonths((long) month);

        // Chuyển đổi từ LocalDateTime sang Date
        Date futureDate = Date.from(futureDateTime.atZone(ZoneId.systemDefault()).toInstant());

        return futureDate;
    }
}

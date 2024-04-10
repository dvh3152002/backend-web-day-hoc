package com.example.backendkhoaluan.controller;

import com.example.backendkhoaluan.config.VNPayConfig;
import com.example.backendkhoaluan.dto.UsersDTO;
import com.example.backendkhoaluan.entities.Orders;
import com.example.backendkhoaluan.payload.request.PayRequest;
import com.example.backendkhoaluan.payload.response.BaseResponse;
import com.example.backendkhoaluan.service.imp.OrderService;
import com.example.backendkhoaluan.utils.JwtUtilsHelper;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtilsHelper jwtUtilsHelper;

    private final Gson gson = new Gson();

    @GetMapping("/payment-callback")
    public void paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response) throws IOException {
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");

        String orderId = queryParams.get("orderId");
        if(orderId!= null && !orderId.equals("")) {
            if ("00".equals(vnp_ResponseCode)) {
                // Giao dịch thành công
                // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
                orderService.updateOrder(queryParams);
                response.sendRedirect("http://localhost:3000/payment?idSuccess="+true);
            } else {
                // Giao dịch thất bại
                // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
                response.sendRedirect("http://localhost:3000/payment?idSuccess="+false);
            }
        }else {
            // Giao dịch thất bại
            // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
            response.sendRedirect("http://localhost:3000/payment?idSuccess="+false);
        }
    }

    @PostMapping("/pay")
    public BaseResponse getPay(@RequestBody PayRequest request,
                               @RequestHeader("Authorization") String header) throws UnsupportedEncodingException {
        String token = header.substring(7);
        String jwt = jwtUtilsHelper.verifyToken(token);
        UsersDTO user = gson.fromJson(jwt, UsersDTO.class);
        request.setIdUser(user.getId());
        String paymentUrl=orderService.createOrder(request);

        return BaseResponse.success(paymentUrl);
    }
}

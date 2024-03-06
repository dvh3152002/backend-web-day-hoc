package com.example.backendkhoaluan.service;

import com.example.backendkhoaluan.entities.OrderDetail;
import com.example.backendkhoaluan.entities.Orders;
import com.example.backendkhoaluan.entities.User;
import com.example.backendkhoaluan.exception.DeleteException;
import com.example.backendkhoaluan.repository.OrderDetailRepository;
import com.example.backendkhoaluan.repository.OrdersRepository;
import com.example.backendkhoaluan.service.imp.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public void deleteOrder(int id) {
        Optional<Orders> orders=ordersRepository.findById(id);
        deleteOrder(orders.get());
    }

    @Transactional
    @Override
    public void deleteOrder(Orders orders) {
        try {
            List<OrderDetail> listOrderDetails=orderDetailRepository.findByOrder(orders);
            listOrderDetails.forEach(orderDetail -> {
                orderDetailRepository.delete(orderDetail);
            });
            ordersRepository.delete(orders);
        }catch (Exception e){
            throw new DeleteException("Lỗi xóa giỏ hàng",e.getLocalizedMessage());
        }
    }

    @Override
    public List<Orders> findByUser(User user) {
        return ordersRepository.findByUser(user);
    }
}

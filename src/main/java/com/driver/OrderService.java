package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

@Autowired
OrderRepository orderRepository;

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public void addPartner(String partnerId) {
        orderRepository.addPartner(partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        orderRepository.addOrderPartnerPair(orderId,partnerId);
    }

    public Order getOrderById(String orderId) {
        Order order = orderRepository.getOrderById(orderId);
        return order;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        DeliveryPartner deliveryPartner = orderRepository.getPartnerById(partnerId);
        return deliveryPartner;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        Integer orderCount = orderRepository.getOrderCountByPartnerId(partnerId);
        return orderCount;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orders = orderRepository.getOrdersByPartnerId(partnerId);
        return orders;
    }

    public List<String> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Integer getCountOfUnassignedOrders() {
        return orderRepository.getCountOfUnassignedOrders();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int intTime = Integer.parseInt(time.substring(0,2))*60 + Integer.parseInt(time.substring(3));

        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(intTime, partnerId);
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int lastOrderTime = orderRepository.getLastDeliveryTimeByPartnerId(partnerId);

        int hours = lastOrderTime/60;
        int minutes = lastOrderTime%60;
        String HH = ""+hours;
        String MM = ""+minutes;

        if(HH.length()==1){
            HH = '0'+HH;
        }
        if(MM.length()==1){
            MM = '0'+MM;
        }
        return HH+':'+MM;
    }

    public void deletePartnerById(String partnerId) {
        orderRepository.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderRepository.deleteOrderById(orderId);
    }
}
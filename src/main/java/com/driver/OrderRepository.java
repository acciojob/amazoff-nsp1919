package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderHashMap = new HashMap<>();
    HashMap<String, DeliveryPartner> deliveryPartnerHashMap = new HashMap<>();

    HashMap<String, List<String>> partnerOrderHashMap = new HashMap<>();

    HashMap<String, String> orderToPartner = new HashMap<>();


    public void addOrder(Order order) {
        orderHashMap.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner dp = new DeliveryPartner(partnerId);
        deliveryPartnerHashMap.put(partnerId,dp);
    }


    public void addOrderPartnerPair(String orderId, String partnerId) {
        if (!partnerOrderHashMap.containsKey(partnerId)) {
            partnerOrderHashMap.put(partnerId, new ArrayList<>());
        }
        partnerOrderHashMap.get(partnerId).add(orderId);
        deliveryPartnerHashMap.get(partnerId).setNumberOfOrders
                (deliveryPartnerHashMap.get(partnerId).getNumberOfOrders() + 1);
        orderToPartner.put(orderId, partnerId);
    }
    public Order getOrderById(String orderId) {

        return orderHashMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {

        return deliveryPartnerHashMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
       return deliveryPartnerHashMap.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
//        List<String> orders = new ArrayList<>();
//        if (!partnerOrderHashMap.containsKey(partnerId)) return orders;
//        List<String> list = partnerOrderHashMap.get(partnerId);
//        for (String o: list) {
//            orders.add(o);
//        }
//        return orders;
        return partnerOrderHashMap.get(partnerId);
    }

    public List<String> getAllOrders() {
        List<String> orders = new ArrayList<>();
        for (String s: orderHashMap.keySet()) {
            orders.add(s);
        }
        return orders;
    }

    public Integer getCountOfUnassignedOrders() {
        return orderHashMap.size()-orderToPartner.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int  inttime, String partnerId) {
        int count = 0;
//        int inttime=Integer.parseInt(time);
        List<String> ordersToCheck = partnerOrderHashMap.get(partnerId);
        for(String orderID: ordersToCheck){
            if((orderHashMap.get(orderID).getDeliveryTime())> inttime){
                count++;
            }
        }
        return count;

    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {

        int lastOrderTime = 0;

        List<String> ordersOfPartner = partnerOrderHashMap.get(partnerId);
        for(String orderID: ordersOfPartner){
            if(orderHashMap.get(orderID).getDeliveryTime()>lastOrderTime){
                lastOrderTime = orderHashMap.get(orderID).getDeliveryTime();
            }
        }

        return lastOrderTime;
    }

    public void deletePartnerById(String partnerId) {

        DeliveryPartner dp = deliveryPartnerHashMap.get(partnerId);
        dp.setNumberOfOrders(0);

        List<String> list = partnerOrderHashMap.get(partnerId);
        for (String o: list) {
            orderToPartner.remove(o);
        }
        deliveryPartnerHashMap.remove(partnerId);
        partnerOrderHashMap.remove(partnerId);

    }

    public void deleteOrderById(String orderId) {
        if (!orderHashMap.containsKey(orderId)) return;
        String partnerId = orderToPartner.get(orderId);
        partnerOrderHashMap.get(partnerId).remove(orderId);
        deliveryPartnerHashMap.get(partnerId).setNumberOfOrders(deliveryPartnerHashMap.get(partnerId).
                getNumberOfOrders()-1);
        orderToPartner.remove(orderId);
        orderHashMap.remove(orderId);
    }
}
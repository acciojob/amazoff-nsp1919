package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String,Order> orderHashMap=new HashMap<>();
    HashMap<String,DeliveryPartner> deliveryPartnerHashMap=new HashMap<>();
    HashMap<String,String> oderToPatner=new HashMap<>();
    HashMap<String, List<Order>> partnerOrderHashMap =new HashMap<>();

    public void addOrder(Order order) {
        orderHashMap.put(order.getId(),order);

    }
    public void addPartner(String partnerId){
        DeliveryPartner dp=new DeliveryPartner(partnerId);
        deliveryPartnerHashMap.put(partnerId,dp);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        Order order = orderHashMap.get(orderId);
        DeliveryPartner deliveryPartner=deliveryPartnerHashMap.get(partnerId);
        if (order==null||deliveryPartner==null) return;
        if (partnerOrderHashMap .containsKey(partnerId)){
            partnerOrderHashMap .get(partnerId).add(order);
        }
        else{
            List<Order> temp=new ArrayList<>();
            temp.add(order);
            partnerOrderHashMap.put(partnerId,temp);
        }
        oderToPatner.put(orderId,partnerId);
        deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);

    }

    public Order getOrderById(String orderId) {

        return orderHashMap.get(orderId);
    }
//
    public DeliveryPartner getPartnerById(String partnerId) {

        return deliveryPartnerHashMap.get(partnerId);
    }
//
    public Integer getOrderCountByPartnerId(String partnerId) {
        if (!partnerOrderHashMap.containsKey(partnerId)) return 0;
        return partnerOrderHashMap.get(partnerId).size();
    }
//
    public List<String> getOrdersByPartnerId(String partnerId) {

        List<Order>list=partnerOrderHashMap.get(partnerId);
        List<String> temp=new ArrayList<>();
        for (Order o:list){
            temp.add(o.getId());
        }
        return temp;
    }

    public List<String> getAllOrders() {
        List<String> ans=new ArrayList<>();
        for (String s:orderHashMap.keySet()){
                ans.add(s);
            }
        return  ans;
    }
//
    public Integer getCountOfUnassignedOrders() {
        int countoforders=orderHashMap.size();
        int countofassignorders=oderToPatner.size();
        return countoforders-countofassignorders;
    }
}

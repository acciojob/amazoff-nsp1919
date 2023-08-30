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
    public Integer getCountOfUnassignedOrders() {Integer totalOrders = getAllOrders().size();

        Integer countOfAssignedOrders = 0;
        Integer ans = 0;

        for (String dp: partnerOrderHashMap.keySet()) {
            countOfAssignedOrders += getOrderCountByPartnerId(dp);
        }

        ans = totalOrders - countOfAssignedOrders;
        return ans;
    }

    public void deletePartnerById(String partnerId) {
        DeliveryPartner dp = deliveryPartnerHashMap.get(partnerId);
        dp.setNumberOfOrders(0);

        List<Order> list = partnerOrderHashMap.get(partnerId);
        for (Order o: list) {
            oderToPatner.remove(o.getId());
        }
        deliveryPartnerHashMap.remove(partnerId);
        partnerOrderHashMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        String partnerId = oderToPatner.get(orderId);
        Order order = orderHashMap.get(orderId);
        partnerOrderHashMap.get(partnerId).remove(order);
        deliveryPartnerHashMap.get(partnerId).setNumberOfOrders(deliveryPartnerHashMap.get(partnerId).getNumberOfOrders()-1);
        oderToPatner.remove(orderId);
        orderHashMap.remove(orderId);
    }
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        String hh = time.substring(0,2);
        String mm = time.substring(3);
        int h = Integer.parseInt(hh) * 60;
        int m = Integer.parseInt(mm);
        int givenTime = h+m;

        List<Order> list = partnerOrderHashMap.get(partnerId);
        Integer count = 0;

        for (Order o: list) {
            if (o.getDeliveryTime() > givenTime) {
                count++;
            }
        }

        return count;

    }
    public String getLastDeliveryTimeByPartnerId(String partnerId) {

        int time = 0;

        List<Order> list = partnerOrderHashMap.get(partnerId);

        for (Order o: list) {
            if (o.getDeliveryTime() > time) {
                time = o.getDeliveryTime();
            }
        }

        int h = time/60;
        int m = time%60;

        String hh = "";
        String mm = "";
        if (m >= 0 && m <=9) {
            mm = "0" + String.valueOf(m);
        }
        else {
            mm = String.valueOf(m);
        }

        if (h >= 0 && h <=9) {
            hh = "0" + String.valueOf(h);
        }
        else {
            hh = String.valueOf(h);
        }
//        String hh = ""+h;
//        String mm = ""+m;
//
//        if (hh.length() == 1) {
//            hh = '0' + hh;
//        }
//
//        if (mm.length() == 1) {
//            mm = '0' + mm;
//        }

        return hh + ":" + mm;
    }


}

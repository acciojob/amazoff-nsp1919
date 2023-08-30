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
        Order order = orderHashMap.get(orderId);
        DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(partnerId);

        if (order == null || deliveryPartner == null) return;

        if (partnerOrderHashMap.containsKey(deliveryPartner.getId())) {
            partnerOrderHashMap.get(deliveryPartner.getId()).add(order.getId());
        }

        else {
            List<String> list = new ArrayList<>();
            list.add(orderId);
            partnerOrderHashMap.put(deliveryPartner.getId(), list);
        }

        orderToPartner.put(orderId,partnerId);

        deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);
    }


    public Order getOrderById(String orderId) {
        if (!orderHashMap.containsKey(orderId)) return null;
        return orderHashMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
//        if (!deliveryPartnerHashMap.containsKey(partnerId)) return null;
        return deliveryPartnerHashMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        if (!partnerOrderHashMap.containsKey(partnerId)) return 0;
        List<String> list = partnerOrderHashMap.get(partnerId);
        return list.size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orders = new ArrayList<>();
        if (!partnerOrderHashMap.containsKey(partnerId)) return orders;
        List<String> list = partnerOrderHashMap.get(partnerId);
        for (String o: list) {
            orders.add(o);
        }
        return orders;
    }

    public List<String> getAllOrders() {
        List<String> orders = new ArrayList<>();
        for (String s: orderHashMap.keySet()) {
            orders.add(s);
        }
        return orders;
    }

    public Integer getCountOfUnassignedOrders() {
        Integer totalOrders = getAllOrders().size();
        Integer countOfAssignedOrders = 0;
        int ans = 0;

        for (String dp: partnerOrderHashMap.keySet()) {
            countOfAssignedOrders += getOrderCountByPartnerId(dp);
        }

        ans = totalOrders - countOfAssignedOrders;
        return ans;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        String hh = time.substring(0,2);
        String mm = time.substring(3);
        int h = Integer.parseInt(hh) * 60;
        int m = Integer.parseInt(mm);
        int givenTime = h+m;

        List<String> list = partnerOrderHashMap.get(partnerId);
        Integer count = 0;

        for (String o: list) {
            Order or=orderHashMap.get(o);
            if (or.getDeliveryTime() > givenTime) {
                count++;
            }
        }

        return count;

    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {

        int time = 0;

        List<String> list = partnerOrderHashMap.get(partnerId);

        for (String s: list) {
            Order o=orderHashMap.get(s);
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

        return hh + ":" + mm;
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
package com.android.goldengift.customer.checkout;

import com.android.goldengift.backend.invoice.InvoiceRepository;
import com.android.goldengift.model.Order;
import com.android.goldengift.model.OrderItem;
import com.android.goldengift.model.Product;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

public class CheckoutPresenter {

    private final InvoiceRepository invoiceRepository;
    private final CheckoutActivity view;

    public CheckoutPresenter(CheckoutActivity view, InvoiceRepository invoiceRepository) {
        this.view = view;
        this.invoiceRepository = invoiceRepository;
    }

    public double getOrderTotalCost() {
        return invoiceRepository.getTotalCost();
    }

    public long generateOrderNumber() {
        Random random = new Random();
        return Math.abs(random.nextLong());
    }

    public HashMap<String, OrderItem> getOrderItemsAsHashMap() {
        HashMap<String, OrderItem> orderItemHashMap = new HashMap<>();

        int key = 0;
        for (Product product : InvoiceRepository.orderItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setItemName(product.getName());
            orderItem.setQuantity(invoiceRepository.getProductItemQuantity(product));
            orderItem.setCost(invoiceRepository.getProductItemTotalCost(product));

            orderItemHashMap.put("item" + key++, orderItem);
        }
        return orderItemHashMap;
    }

    public boolean validateOrderInfo(Order order) {
        boolean validate = true;
        if (order.getPhoneNumber() == null || order.getPhoneNumber().isEmpty()) {
            view.showPhoneNumberIsRequiredMsg();
            validate = false;
        }
        if (order.getCustomerName() == null || order.getCustomerName().isEmpty()) {
            validate = false;
            view.showCustomerNameIsRequiredMsg();
        }
        if (order.getAddress() == null || order.getAddress().isEmpty()) {
            validate = false;
            view.showAddressIsRequiredMsg();
        }
        return validate;
    }

    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }
}

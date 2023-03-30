package com.example.controller;

import com.example.Constants;
import com.example.entity.OrderStatus;
import com.example.entity.Product;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class InventoryController {
    public static ArrayList<Product> products = new ArrayList<Product>();

    public static void addProduct(Product product){
        products.add(product);


    }

    @RabbitListener(queues = Constants.QUEUE )
    public void consumeMessageFromQueue(OrderStatus orderStatus) {
        System.out.println("Message Received from queue: " +orderStatus );
        String productId = orderStatus.getOrder().getProductId();
        double quantity = orderStatus.getOrder().getQuantity();

        for (Product product:products) {
            if (product.getProductId().equals(productId)){
                if (quantity <= product.getQuantity()){
                    System.out.println("order successful");
                    product.setQuantity((int) (product.getQuantity() - quantity));
                }
                else {
                    System.out.println("order wasn't successful");
                }
            }
            else {
                System.out.println("Product not found");
            }

        }
    }


}

package com.atelier.atelierstore;

import com.atelier.atelierstore.exception.OutOfStockException;
import com.atelier.atelierstore.model.Illustration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class AtelierStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtelierStoreApplication.class, args);
    /*    Illustration myArt = new Illustration("12", "art1", 100, "paintings");
        myArt.displayInfo();
        try {
            processOrder(0);
        } catch (OutOfStockException e) {
            throw new RuntimeException(e);
        }
    }

    public static void processOrder(int stock) throws OutOfStockException {
        if(stock <= 0){
            throw new OutOfStockException("抱歉，现在这款没货了。");
        }
    */
    }
}

package com.example.kafkademo;

import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Consumer;

@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class KafkaDemoApplication {


    private final StreamBridge streamBridge;
    public static int index = 0;

    @PostMapping("/send-data")
    public String publishData() {

        streamBridge.send("mySupplier-out-0", new Account(index++, 6000 * Math.random()));
        return "Success!!";
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaDemoApplication.class, args);
    }



    @Bean("ConsumeData")
    public Consumer<Message<Account>> ConsumeData() {
        return event -> {
           var result = event.getPayload();
           System.out.println("*****************************************************");
           System.out.println("id : " + result.getCode() + " \nbalance : " + result.getBalance());

            streamBridge.send("mySupplier-out-0", new Account(index++, 6000 * Math.random()));

        };
    }

}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class Account {

    private int code;
    private double balance;

}

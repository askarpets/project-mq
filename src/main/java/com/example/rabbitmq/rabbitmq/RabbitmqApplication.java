package com.example.rabbitmq.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Controller
public class RabbitmqApplication {

    private List<String> messageList = new ArrayList<>();

    @Autowired
    AmqpTemplate template;

    @RequestMapping("/send")
    @ResponseBody
    String queue1(@RequestParam("name") String name) {
        template.convertAndSend("anton-test", name);
        return "Emit to queue";
    }

    @GetMapping("/messages")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> messages() {
        Map<String, Object> messages = new HashMap<>();
        messages.put("messages", messageList);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


    @RabbitListener(queues = "anton-test")
    public void listen(String message) {
        messageList.add(message);
    }


    public static void main(String[] args) {
        SpringApplication.run(RabbitmqApplication.class, args);
    }
}

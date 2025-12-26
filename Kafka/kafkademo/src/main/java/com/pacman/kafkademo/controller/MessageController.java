package com.pacman.kafkademo.controller;

import com.pacman.kafkademo.service.ProducerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/messages")
@RestController
public class MessageController {
  private final ProducerService producerService;

  public MessageController(ProducerService producerService) {
    this.producerService = producerService;
  }

  @RequestMapping("/send")
  public String sendMessage(
      @RequestParam String message) {
    producerService.sendMessage("test-topic", message);
    return "Message sent" + message;
  }
}

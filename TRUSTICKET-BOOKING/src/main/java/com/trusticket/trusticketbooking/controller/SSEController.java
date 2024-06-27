package com.trusticket.trusticketbooking.controller;

import com.trusticket.trusticketbooking.service.SSEService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@AllArgsConstructor
public class SSEController {
    private final SSEService sseService;
    @GetMapping("/sse/{memberId}")
    public SseEmitter handleSse(@PathVariable Long memberId) {
        SseEmitter emitter = new SseEmitter(300 * 1000L);
        sseService.addEmitter(memberId, emitter);
        sseService.sendStatus();
        return emitter;
    }
}

package com.trusticket.trusticketbooking.service;

import com.trusticket.trusticketbooking.dto.BookingStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SSEService {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap();
    private Long currentOffset = 0L;

    public void setOffsetData(Long offset){
        this.currentOffset = offset;
    }


    public void addEmitter(Long memberId, SseEmitter emitter) {
        emitters.put(memberId, emitter);
        emitter.onCompletion(() -> emitters.remove(memberId));
        emitter.onTimeout(() -> emitters.remove(memberId));
    }

    @Scheduled(fixedRate = 1000)
    public void sendStatus() {
        for (Long memberId : emitters.keySet()) {
            SseEmitter emitter = emitters.get(memberId);
            if(emitter != null){
                try {
                    emitter.send(currentOffset);
                } catch (IOException e) {
                    emitter.complete();
                    emitters.remove(emitter);
                }
            }
        }
    }

    public void sendResultData(Long memberId, BookingStatus status) {
        SseEmitter emitter = emitters.get(memberId);
        if(emitter != null){
            try {
                emitter.send(status);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }

    }


}

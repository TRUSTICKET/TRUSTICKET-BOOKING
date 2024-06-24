package com.trusticket.trusticketbooking.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaConsumer {

    @KafkaListener(topics = "booking-request")
    public void getMessage(String kafkaMessage){
        log.info("Kafka Message: -> " + kafkaMessage);

        // 수신한 JSON 형식의 메시지를 Map으로 변환
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try{
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        }
        catch(JsonProcessingException ex){
            ex.printStackTrace();
        }

    }

}

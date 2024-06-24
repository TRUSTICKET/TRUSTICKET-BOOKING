package com.trusticket.trusticketbooking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trusticket.trusticketbooking.config.util.DatetimeUtil;
import com.trusticket.trusticketbooking.domain.Event;
import com.trusticket.trusticketbooking.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaConsumerService {

    private final EventRepository eventRepository;

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


    @Transactional
    @KafkaListener(topics = "event-create")
    public void eventCreateListener(String kafkaMessage){
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

        Event event = Event.builder()
                .eventId((String)map.get("eventId"))
                .price((Integer)map.get("price"))
                .stock((Integer)map.get("stock"))
                .maxStock((Integer)map.get("maxStock"))
                .startDate(DatetimeUtil.parseStringToDatetime((String)map.get("startDate")))
                .endDate(DatetimeUtil.parseStringToDatetime((String)map.get("endDate")))
                .build();

        eventRepository.save(event);

    }

}

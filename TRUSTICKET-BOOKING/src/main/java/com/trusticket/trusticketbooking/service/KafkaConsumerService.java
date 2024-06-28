package com.trusticket.trusticketbooking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trusticket.trusticketbooking.config.util.DatetimeUtil;
import com.trusticket.trusticketbooking.domain.Booking;
import com.trusticket.trusticketbooking.domain.Event;
import com.trusticket.trusticketbooking.dto.BookingStatus;
import com.trusticket.trusticketbooking.repository.*;
import jakarta.persistence.LockModeType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaConsumerService {

    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;
    private final EventCacheRepository eventCacheRepository;
    private final SSEService sseService;
    private final BookingCancelRepository bookingCancelRepository;
    private BookingCacheRepository bookingCacheRepository;
    private KafkaConsumer<String, String> kafkaConsumer; // Kafka Consumer 주입

    @KafkaListener(topics = "booking-request")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void getMessage(ConsumerRecord<String, String> record){
        String kafkaMessage = record.value();
        log.info("Kafka Message: -> " + record.offset());
        sseService.setOffsetData(record.offset());

        // 수신한 JSON 형식의 메시지를 Map으로 변환
        if(bookingCancelRepository.getBookingCancel(record.offset()) == null){
            Map<Object, Object> map = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            try{
                map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
            }
            catch(JsonProcessingException ex){
                ex.printStackTrace();
            }

                Booking event = Booking.builder()
                        .eventId((String)map.get("id"))
                        .memberId(Long.parseLong((String)map.get("memberId")))
                        .status("CONFIRM")
                        .build();

                Integer stock = eventCacheRepository.getEventCache(event.getEventId());
                if(stock == null){
                    Event e = eventRepository.findById(event.getEventId()).orElseThrow(() -> {
                        throw new NoSuchElementException("해당 이벤트를 찾을 수 없습니다");
                    });
                    eventCacheRepository.insertEventCache(e.getEventId(), e.getMaxStock());
                    stock = e.getStock();

//                    try{
//                        Optional<Event> e = eventRepository.findById(event.getEventId());
//                        if(e.get() == null){
//                            return;
//                        }
//                        else{
//                            eventCacheRepository.insertBookingCacheString(e.get().getEventId(), e.get().getMaxStock());
//                            stock = e.get().getStock();
//                        }
//                    }
//                    catch(Exception e){
//                        return;
//                    }

                }

                long count = bookingRepository.countByStatusConfirmWithLock();

                BookingStatus status = new BookingStatus("SUCCESS", "예매에 성공했습니다. 결제화면으로 이동합니다.");
                if(count < stock){
                    if(!bookingRepository.existsBookingByEventIdAndMemberId(event.getEventId(), event.getMemberId())){
                        bookingRepository.save(event);

                    }
                    else{
                        status = new BookingStatus("EXIST", "이미 예매한 내역이 있습니다.");
                    }
                }
                else{
                    status = new BookingStatus("FAIL", "인원이 초과되었습니다.");
                }

                sseService.sendResultData(event.getMemberId(), status);
                bookingCacheRepository.insertBookingCache(record.offset(), status.getResult());
        }
        else{
            bookingCancelRepository.deleteBookingCancel(record.offset());
        }

    }

    @KafkaListener(topics = "booking-cancel")
    public void getCancelMessage(String kafkaMessage){


        // 수신한 JSON 형식의 메시지를 Map으로 변환
        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try{
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        }
        catch(JsonProcessingException ex){
            ex.printStackTrace();
        }

        log.info("대기열을 이탈합니다: -> " + (String)map.get("offsetId"));
        bookingCancelRepository.insertBookingCancel(Long.parseLong((String)map.get("offsetId")));

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


    @Transactional
    @KafkaListener(topics = "payment-success")
    public void PaymentListener(String kafkaMessage){
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

        Long bookingId = Long.parseLong((String)map.get("bookingId"));
        Long paymentId = Long.parseLong((String)map.get("paymentId"));

        Optional<Booking> event = bookingRepository.findById(bookingId);
        if(event != null){
            Booking booking = event.get();
            booking.setStatus("SUCCESS");
            booking.setPaymentId(paymentId);
            bookingRepository.save(booking);
        }

    }





}

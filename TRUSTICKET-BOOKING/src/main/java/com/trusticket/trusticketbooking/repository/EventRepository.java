package com.trusticket.trusticketbooking.repository;

import com.trusticket.trusticketbooking.domain.Event;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface EventRepository extends JpaRepository<Event, String> {


}
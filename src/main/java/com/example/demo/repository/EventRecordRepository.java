package com.example.demo.repository;

import com.example.demo.entity.EventRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for accessing EventRecord entities.
 */
@Repository
public interface EventRecordRepository extends JpaRepository<EventRecord, Long> {
}

package com.futuapi.events.repository;

import com.futuapi.events.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long>{
    @Query(value="SELECT * FROM options o, events e  WHERE o.event_id = e.id AND e.id = ?1",
            nativeQuery	= true)
    List<Option> findByEvent(Long id);
}

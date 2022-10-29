package com.futuapi.events.repository;

import com.futuapi.events.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long>{
    @Query(value="SELECT * FROM options o, events e  WHERE o.event_id = e.id AND e.id = ?1",
            nativeQuery	= true)
    Iterable<Option> findByEvent(Long id);
}

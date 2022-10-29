package com.futuapi.events.repository;

import com.futuapi.events.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query(value="SELECT * FROM votes v, options o, events e  WHERE v.option_id = o.id AND o.event_id = e.id AND e.id = ?1",
            nativeQuery	= true)
    Iterable<Vote> findByEvent(Long id);

    @Query(value="SELECT v.* FROM votes v, options o WHERE o.id = v.option_id AND v.option_id = ?2 AND v.email = ?1",
            nativeQuery	= true)
    Iterable<Vote> findByEmailAndOption(String email, Long option_id);

}

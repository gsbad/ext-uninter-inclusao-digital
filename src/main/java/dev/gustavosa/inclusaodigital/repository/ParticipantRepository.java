package dev.gustavosa.inclusaodigital.repository;

import dev.gustavosa.inclusaodigital.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}

package dev.gustavosa.inclusaodigital.service;

import dev.gustavosa.inclusaodigital.dto.ParticipantForm;
import dev.gustavosa.inclusaodigital.entity.Participant;
import dev.gustavosa.inclusaodigital.mapper.ParticipantMapper;
import dev.gustavosa.inclusaodigital.repository.ParticipantRepository;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    public ParticipantService(ParticipantRepository participantRepository, ParticipantMapper participantMapper) {
        this.participantRepository = participantRepository;
        this.participantMapper = participantMapper;
    }

    public Participant register(ParticipantForm form) {
        Participant participant = participantMapper.toEntity(form);
        return participantRepository.save(participant);
    }
}

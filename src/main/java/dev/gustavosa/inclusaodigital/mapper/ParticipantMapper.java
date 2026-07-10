package dev.gustavosa.inclusaodigital.mapper;

import dev.gustavosa.inclusaodigital.dto.ParticipantForm;
import dev.gustavosa.inclusaodigital.entity.Participant;
import org.springframework.stereotype.Component;

@Component
public class ParticipantMapper {

    public Participant toEntity(ParticipantForm form) {
        return new Participant(form.getFullName(), form.getAgeRange(), form.getPhone());
    }
}

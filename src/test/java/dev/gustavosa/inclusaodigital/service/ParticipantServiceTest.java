package dev.gustavosa.inclusaodigital.service;

import dev.gustavosa.inclusaodigital.dto.ParticipantForm;
import dev.gustavosa.inclusaodigital.entity.AgeRange;
import dev.gustavosa.inclusaodigital.entity.Participant;
import dev.gustavosa.inclusaodigital.mapper.ParticipantMapper;
import dev.gustavosa.inclusaodigital.repository.ParticipantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private ParticipantMapper participantMapper;

    @InjectMocks
    private ParticipantService participantService;

    @Test
    void deveMapearEPersistirParticipante() {
        ParticipantForm form = new ParticipantForm();
        form.setFullName("Maria Silva");
        form.setAgeRange(AgeRange.SEVENTY_TO_SEVENTY_NINE);

        Participant mapped = new Participant("Maria Silva", AgeRange.SEVENTY_TO_SEVENTY_NINE, null);
        Participant saved = new Participant("Maria Silva", AgeRange.SEVENTY_TO_SEVENTY_NINE, null);

        when(participantMapper.toEntity(form)).thenReturn(mapped);
        when(participantRepository.save(mapped)).thenReturn(saved);

        Participant result = participantService.register(form);

        assertThat(result).isEqualTo(saved);
        verify(participantRepository).save(mapped);
    }
}

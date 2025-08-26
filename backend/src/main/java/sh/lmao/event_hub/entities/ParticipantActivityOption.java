package sh.lmao.event_hub.entities;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ParticipantActivityOption
 *
 * Just a junction table to connect ActivityOption and Participant
 */
@Entity
@Table(name = "participant_activity_options")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ParticipantActivityOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_option_id")
    @JsonIgnore
    private ActivityOption activityOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    @JsonIgnore
    private Participant participant;

    @Column(name = "activity_option_id", insertable = false, updatable = false)
    private UUID activityOptionId;

    @Column(name = "participant_id", insertable = false, updatable = false)
    private UUID participantId;
}

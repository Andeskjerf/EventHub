package sh.lmao.event_hub.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "activities")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // when the event is taking place, unix timestamp
    private long eventDate;

    // when registration should be disabled, as seconds before eventDate
    private long registerBefore;

    // where the event is taking place
    private String location;

    // where participants should meet up
    private String meetLocation;

    private String description;

    // 0 for unlimited
    private int maxParticipants;
}

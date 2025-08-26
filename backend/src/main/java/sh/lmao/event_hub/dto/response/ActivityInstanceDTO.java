package sh.lmao.event_hub.dto.response;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sh.lmao.event_hub.entities.ActivityOption;

@Getter
@Setter
@NoArgsConstructor
public class ActivityInstanceDTO {

    @NotBlank
    private UUID activityId;

    @NotBlank
    private UUID instanceId;

    @NotBlank
    private String name;

    @NotBlank
    private ZonedDateTime eventDate;

    private int registerBefore;

    @NotBlank
    private String location;

    @NotBlank
    private String meetLocation;

    private String description;

    private int participants;

    private int maxParticipants;

    private int repeatInterval;

    private List<ActivityOption> options = new ArrayList<>();
}

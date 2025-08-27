package sh.lmao.event_hub.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ParticipantDTO {

    private UUID activityInstanceId;

    private boolean anonymized;

    private String name;

    private String phoneNumber;

    private List<String> activityOptionNames = new ArrayList<>();
}

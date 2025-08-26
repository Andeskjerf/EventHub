package sh.lmao.event_hub.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterParticipantDTO {

    @NotNull(message = "activityInstanceId is required")
    private UUID activityInstanceId;

    @NotBlank(message = "participant name is required")
    private String name;

    @Pattern(regexp = "^\\+?[0-9\\s\\-]{0,20}$", message = "Phone number can contain digits, spaces, dashes, and optional + prefix")
    private String phoneNumber;

    private List<String> activityOptionIds = new ArrayList<>();
}

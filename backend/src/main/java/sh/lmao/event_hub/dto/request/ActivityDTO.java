package sh.lmao.event_hub.dto.request;

import java.time.ZonedDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ActivityDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    private String name;

    // when the event is taking place, unix timestamp
    @NotNull(message = "Event date is required")
    private ZonedDateTime eventDate;

    // when registration should be disabled, as hours before eventDate
    @Min(value = 0, message = "Registration deadline can't be less than 0")
    @Max(value = 168, message = "Registration deadline can't exceed 168 hours")
    private int registerBefore;

    // where the event is taking place
    @Size(min = 0, max = 100, message = "Location cannot exceed 100 characters")
    private String location;

    // where participants should meet up
    @Size(min = 0, max = 100, message = "Meetup location cannot exceed 100 characters")
    private String meetLocation;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    // 0 for unlimited
    @Min(value = 0, message = "Max participants cannot be negative")
    @Max(value = 10000, message = "Max participants cannot exceed 10000")
    private int maxParticipants;

    // if not 0 or null, repeat every nth day
    @Max(value = 365, message = "Repeat interval cannot exceed a year / 365 days")
    private int repeatInterval;
}

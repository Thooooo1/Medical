package medical.dto;

import java.time.LocalDateTime;

public record AppointmentDto(
        Long id,
        LocalDateTime appointmentTime,
        String note,
        DoctorMiniDto doctor,
        UserDto user
) {
}

package medical.controller;

import medical.dto.AppointmentDto;
import medical.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Frontend calls: POST /api/appointments?userId=1&doctorId=2&time=2026-01-20T08:30&note=...
     */
    @PostMapping
    public AppointmentDto book(
            @RequestParam Long userId,
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time,
            @RequestParam(required = false) String note
    ) {
        return appointmentService.book(userId, doctorId, time, note);
    }

    /**
     * Returns available slots (HH:mm) for a doctor on a given date (YYYY-MM-DD).
     */
    @GetMapping("/free-slots")
    public List<String> freeSlots(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return appointmentService.getFreeSlots(doctorId, date);
    }
}

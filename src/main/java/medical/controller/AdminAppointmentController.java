package medical.controller;

import medical.dto.AppointmentDto;
import medical.service.AppointmentService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/appointments")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    public AdminAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // GET /api/admin/appointments?date=YYYY-MM-DD
    @GetMapping
    public List<AppointmentDto> getAppointments(@RequestParam("date") LocalDate date) {
        return appointmentService.getAppointmentsForAdmin(date);
    }
}

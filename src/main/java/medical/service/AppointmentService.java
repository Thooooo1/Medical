package medical.service;

import medical.domain.AppUser;
import medical.domain.Appointment;
import medical.domain.Doctor;
import medical.dto.AppointmentDto;
import medical.dto.DoctorMiniDto;
import medical.dto.UserDto;
import medical.exception.ConflictException;
import medical.exception.NotFoundException;
import medical.repo.AppointmentRepository;
import medical.repo.DoctorRepository;
import medical.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private static final LocalTime WORK_START = LocalTime.of(8, 0);
    private static final LocalTime WORK_END = LocalTime.of(17, 0);
    private static final int SLOT_MINUTES = 30;

    private final AppointmentRepository appointmentRepo;
    private final DoctorRepository doctorRepo;
    private final UserRepository userRepo;
    private final MailService mailService;

    public AppointmentService(
            AppointmentRepository appointmentRepo,
            DoctorRepository doctorRepo,
            UserRepository userRepo,
            MailService mailService
    ) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
        this.userRepo = userRepo;
        this.mailService = mailService;
    }

    // POST /api/appointments?userId=&doctorId=&time=YYYY-MM-DDTHH:mm&note=
    @Transactional
    public AppointmentDto book(Long userId, Long doctorId, LocalDateTime time, String note) {

        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bác sĩ"));

        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

        if (!isWithinWorkingHours(time.toLocalTime())) {
            throw new ConflictException("Giờ khám phải trong khoảng 08:00-17:00");
        }

        // Fast check (still rely on DB unique constraint for race conditions)
        if (appointmentRepo.existsByDoctorIdAndAppointmentTime(doctorId, time)) {
            throw new ConflictException("Khung giờ đã được đặt");
        }

        Appointment appt = appointmentRepo.save(new Appointment(time, note, doctor, user));

        // send email (best-effort)
        try {
            mailService.sendBookingConfirmation(appt);
        } catch (Exception ignored) {
            // don't fail booking if mail fails
        }

        return toDto(appt);
    }

    // GET /api/appointments/free-slots?doctorId=&date=YYYY-MM-DD
    @Transactional(readOnly = true)
    public List<String> getFreeSlots(Long doctorId, LocalDate date) {
        // Ensure doctor exists
        doctorRepo.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bác sĩ"));

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        Set<LocalTime> booked = new HashSet<>(
                appointmentRepo.findByDoctorIdAndAppointmentTimeBetweenOrderByAppointmentTimeAsc(doctorId, start, end)
                        .stream()
                        .map(a -> a.getAppointmentTime().toLocalTime())
                        .collect(Collectors.toSet())
        );

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");

        return generateSlots().stream()
                .filter(t -> !booked.contains(t))
                .map(t -> t.format(fmt))
                .toList();
    }

    // GET /api/doctors/{id}/appointments?date=YYYY-MM-DD (nếu bạn có)
    @Transactional(readOnly = true)
    public List<AppointmentDto> getAppointmentsForDoctor(Long doctorId, LocalDate date) {
        doctorRepo.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bác sĩ"));

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return appointmentRepo.findByDoctorIdAndAppointmentTimeBetweenOrderByAppointmentTimeAsc(doctorId, start, end)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // GET /api/admin/appointments?date=YYYY-MM-DD
    @Transactional(readOnly = true)
    public List<AppointmentDto> getAppointmentsForAdmin(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return appointmentRepo.findByAppointmentTimeBetweenOrderByAppointmentTimeAsc(start, end)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private boolean isWithinWorkingHours(LocalTime time) {
        return !time.isBefore(WORK_START) && time.isBefore(WORK_END);
    }

    private List<LocalTime> generateSlots() {
        LocalTime t = WORK_START;
        var out = new java.util.ArrayList<LocalTime>();
        while (t.isBefore(WORK_END)) {
            out.add(t);
            t = t.plusMinutes(SLOT_MINUTES);
        }
        return out;
    }

    private AppointmentDto toDto(Appointment appt) {
        return new AppointmentDto(
                appt.getId(),
                appt.getAppointmentTime(),
                appt.getNote(),
                new DoctorMiniDto(appt.getDoctor().getId(), appt.getDoctor().getFullName()),
                new UserDto(appt.getUser().getId(), appt.getUser().getFullName())
        );
    }
}

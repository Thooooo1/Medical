package medical.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "appointments",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"doctor_id", "appointment_time"})
    }
)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người đặt
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Bác sĩ
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "appointment_time", nullable = false)
    private LocalDateTime appointmentTime;

    private String note;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    // ===== Getter / Setter =====
    public Long getId() { return id; }
    public User getUser() { return user; }
    public Doctor getDoctor() { return doctor; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public String getNote() { return note; }
    public AppointmentStatus getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public void setNote(String note) { this.note = note; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
}

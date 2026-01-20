package medical.service;

import medical.domain.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final @Nullable JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String from;

    public MailService(@Nullable JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a booking confirmation email if mail is configured.
     */
    public void sendBookingConfirmation(Appointment appt) {
        if (mailSender == null || from == null || from.isBlank()) {
            log.info("Mail not configured; skipping confirmation email.");
            return;
        }

        var user = appt.getUser();
        var doctor = appt.getDoctor();
        var time = appt.getAppointmentTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(user.getEmail());
        msg.setSubject("Xác nhận đặt lịch khám");
        msg.setText("Bạn đã đặt lịch khám thành công.\n" +
                "Bác sĩ: " + doctor.getFullName() + "\n" +
                "Chuyên khoa: " + doctor.getSpecialty() + "\n" +
                "Thời gian: " + time + "\n\n" +
                "Cảm ơn bạn!");

        mailSender.send(msg);
    }
}

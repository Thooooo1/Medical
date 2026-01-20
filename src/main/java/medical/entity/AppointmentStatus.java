package medical.entity;

public enum AppointmentStatus {
    PENDING,    // User đặt – chờ admin duyệt
    APPROVED,   // Admin duyệt
    CANCELLED   // Admin huỷ
}

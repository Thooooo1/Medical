import { useEffect, useState } from "react";
import { doctorApi } from "../api/doctor.api";
import { appointmentApi } from "../api/appointment.api";
import { Doctor } from "../types/Doctor";

export default function BookingPage() {
  const [doctors, setDoctors] = useState<Doctor[]>([]);
  const [doctorId, setDoctorId] = useState<number>();
  const [date, setDate] = useState("");
  const [slots, setSlots] = useState<string[]>([]);
  const [selectedTime, setSelectedTime] = useState("");
  const [note, setNote] = useState("");
  const [message, setMessage] = useState("");

  const USER_ID = 1; // demo user cố định

  // Load danh sách bác sĩ
  useEffect(() => {
    doctorApi.getAll().then((res) => setDoctors(res.data));
  }, []);

  // Load giờ trống khi chọn bác sĩ + ngày
  useEffect(() => {
    if (doctorId && date) {
      appointmentApi
        .getFreeSlots(doctorId, date)
        .then((res) => setSlots(res.data));
    }
  }, [doctorId, date]);

  const handleBooking = async () => {
    if (!doctorId || !date || !selectedTime) {
      setMessage("⚠️ Vui lòng chọn đầy đủ thông tin");
      return;
    }

    try {
      await appointmentApi.book({
        userId: USER_ID,
        doctorId,
        time: `${date}T${selectedTime}`,
        note,
      });

      setMessage("✅ Đặt lịch thành công! Vui lòng kiểm tra email.");
      setSelectedTime("");
      setNote("");
    } catch (e) {
      setMessage("❌ Lỗi: Khung giờ đã được đặt");
    }
  };

  return (
    <div style={{ maxWidth: 600, margin: "40px auto" }}>
      <h2>🩺 Đặt lịch khám</h2>

      {/* Chọn bác sĩ */}
      <label>Bác sĩ</label>
      <select
        value={doctorId}
        onChange={(e) => setDoctorId(Number(e.target.value))}
        style={{ width: "100%", padding: 8, marginBottom: 12 }}
      >
        <option value="">-- Chọn bác sĩ --</option>
        {doctors.map((d) => (
          <option key={d.id} value={d.id}>
            {d.fullName} ({d.specialty})
          </option>
        ))}
      </select>

      {/* Chọn ngày */}
      <label>Ngày khám</label>
      <input
        type="date"
        value={date}
        onChange={(e) => setDate(e.target.value)}
        style={{ width: "100%", padding: 8, marginBottom: 12 }}
      />

      {/* Hiển thị giờ trống */}
      {slots.length > 0 && (
        <>
          <label>Giờ trống</label>
          <div style={{ display: "flex", flexWrap: "wrap", gap: 8 }}>
            {slots.map((time) => (
              <button
                key={time}
                onClick={() => setSelectedTime(time)}
                style={{
                  padding: "6px 12px",
                  border:
                    selectedTime === time
                      ? "2px solid green"
                      : "1px solid #ccc",
                  background: selectedTime === time ? "#e8ffe8" : "white",
                }}
              >
                {time}
              </button>
            ))}
          </div>
        </>
      )}

      {/* Ghi chú */}
      <label style={{ marginTop: 16, display: "block" }}>Ghi chú</label>
      <textarea
        value={note}
        onChange={(e) => setNote(e.target.value)}
        style={{ width: "100%", padding: 8 }}
      />

      {/* Nút đặt lịch */}
      <button
        onClick={handleBooking}
        style={{
          marginTop: 16,
          width: "100%",
          padding: 10,
          background: "green",
          color: "white",
          border: "none",
          fontSize: 16,
        }}
      >
        Đặt lịch
      </button>

      {/* Thông báo */}
      {message && (
        <p style={{ marginTop: 12, fontWeight: "bold" }}>{message}</p>
      )}
    </div>
  );
}

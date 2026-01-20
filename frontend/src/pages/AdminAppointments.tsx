import { useEffect, useState } from "react";
import { axiosClient } from "../api/axiosClient";

interface Appointment {
  appointmentTime: string;
  note?: string;
  doctor: {
    fullName: string;
  };
  user: {
    fullName: string;
  };
}

export default function AdminAppointments() {
  const [date, setDate] = useState("");
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const loadAppointments = async () => {
    if (!date) {
      setMessage("⚠️ Vui lòng chọn ngày");
      return;
    }

    try {
      setLoading(true);
      const res = await axiosClient.get("/admin/appointments", {
        params: { date },
      });
      setAppointments(res.data);
      setMessage("");
    } catch (e) {
      setMessage("❌ Không tải được lịch");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 900, margin: "40px auto" }}>
      <h2>📋 Quản lý lịch khám (Admin)</h2>

      <div style={{ marginBottom: 20 }}>
        <label>Chọn ngày: </label>
        <input
          type="date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
          style={{ padding: 6, marginLeft: 10 }}
        />
        <button
          onClick={loadAppointments}
          style={{
            marginLeft: 10,
            padding: "6px 12px",
            background: "#1976d2",
            color: "white",
            border: "none",
          }}
        >
          Xem lịch
        </button>
      </div>

      {message && <p>{message}</p>}
      {loading && <p>Đang tải...</p>}

      {appointments.length > 0 && (
        <table
          style={{
            width: "100%",
            borderCollapse: "collapse",
            marginTop: 20,
          }}
        >
          <thead>
            <tr style={{ background: "#f0f0f0" }}>
              <th style={th}>Giờ</th>
              <th style={th}>Bác sĩ</th>
              <th style={th}>Bệnh nhân</th>
              <th style={th}>Ghi chú</th>
            </tr>
          </thead>
          <tbody>
            {appointments.map((a, idx) => (
              <tr key={idx}>
                <td style={td}>{a.appointmentTime.substring(11, 16)}</td>
                <td style={td}>{a.doctor.fullName}</td>
                <td style={td}>{a.user.fullName}</td>
                <td style={td}>{a.note || "-"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {!loading && appointments.length === 0 && date && (
        <p>Không có lịch nào trong ngày này</p>
      )}
    </div>
  );
}

const th: React.CSSProperties = {
  padding: 10,
  border: "1px solid #ccc",
};

const td: React.CSSProperties = {
  padding: 8,
  border: "1px solid #ccc",
};

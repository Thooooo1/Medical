import { Routes, Route, Navigate } from "react-router-dom";
import BookingPage from "./pages/BookingPage";
import AdminAppointments from "./pages/AdminAppointments";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/booking" replace />} />
      <Route path="/booking" element={<BookingPage />} />
      <Route path="/admin/appointments" element={<AdminAppointments />} />
    </Routes>
  );
}

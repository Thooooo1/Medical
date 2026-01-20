# Medical Services (Fullstack demo)

Một dự án demo đặt lịch khám gồm:

- **Backend**: Spring Boot + JPA (mặc định chạy H2 in-memory để chạy ngay)
- **Frontend**: React + Vite (gọi API ở `http://localhost:8080/api`)

## 1) Chạy Backend

### Yêu cầu
- Java 17+

### Chạy nhanh (H2 - mặc định)
```bash
cd "Medical  services"
./mvnw spring-boot:run
```

Backend chạy tại: `http://localhost:8080`

H2 console: `http://localhost:8080/h2-console`
 (JDBC URL mặc định: `jdbc:h2:mem:medical_db`)

### Dùng MySQL (tùy chọn)
Đặt biến môi trường (ví dụ):
```bash
export SPRING_DATASOURCE_URL='jdbc:mysql://localhost:3306/booking_db?useSSL=false&serverTimezone=UTC'
export SPRING_DATASOURCE_USERNAME='root'
export SPRING_DATASOURCE_PASSWORD='your_password'
export SPRING_DATASOURCE_DRIVER='com.mysql.cj.jdbc.Driver'
```

## 2) Chạy Frontend

```bash
cd "Medical  services/frontend"
npm install
npm run dev
```

Mặc định Vite chạy ở: `http://localhost:5173`

## 3) API chính (để khớp với frontend)

- `GET /api/doctors`
- `GET /api/appointments/free-slots?doctorId={id}&date=YYYY-MM-DD`
- `POST /api/appointments?userId=1&doctorId={id}&time=YYYY-MM-DDTHH:mm&note=...`
- `GET /api/admin/appointments?date=YYYY-MM-DD`

> Backend có seed dữ liệu demo (1 user + vài bác sĩ) để frontend chạy được ngay.

## 4) Mail (tùy chọn)

Nếu muốn gửi email xác nhận, đặt các biến môi trường:

```bash
export SPRING_MAIL_HOST='smtp.gmail.com'
export SPRING_MAIL_USERNAME='your@gmail.com'
export SPRING_MAIL_PASSWORD='app_password'
```

Nếu không cấu hình mail, backend vẫn chạy bình thường (chỉ bỏ qua bước gửi email).

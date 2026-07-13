# Hệ thống quản lý nhân sự

## Giới thiệu

Đây là dự án quản lý nhân sự được xây dựng bằng Java trên Apache NetBeans. Phần mềm hỗ trợ quản lý thông tin nhân viên, chấm công, tính lương và theo dõi các hoạt động trong doanh nghiệp.

## Chức năng chính

- Đăng nhập hệ thống.
- Quản lý nhân viên.
- Quản lý tài khoản.
- Quản lý hợp đồng.
- Chấm công.
- Quản lý đơn xin nghỉ phép.
- Tính lương.
- Thống kê nhân viên.
- Xuất dữ liệu ra Excel.

## Công nghệ sử dụng

- Java
- Java Swing
- Apache NetBeans
- JDBC
- MySQL
- Apache POI

## Cấu trúc dự án

```text
src/
├── Admin/
├── DAO/
├── Excel/
├── Login/
├── Model/
└── User/
```

## Yêu cầu hệ thống

- JDK 8 hoặc mới hơn.
- Apache NetBeans.
- MySQL Server.

## Cài đặt

### 1. Clone project

```bash
git clone https://github.com/USERNAME/ten-repository.git
```

### 2. Mở bằng NetBeans

- Mở Apache NetBeans.
- Chọn **File → Open Project**.
- Chọn thư mục dự án.

### 3. Cấu hình cơ sở dữ liệu

Tạo cơ sở dữ liệu MySQL và cập nhật thông tin kết nối trong lớp `DAO`.

Ví dụ:

```java
String url = "jdbc:mysql://localhost:3306/quanlynhansu";
String user = "root";
String password = "";
```

### 4. Chạy chương trình

Mở file:

```text
Admin/Main.java
```

Sau đó nhấn **Run Project**.

## Một số giao diện

- Đăng nhập.
- Quản lý nhân viên.
- Chấm công.
- Tính lương.
- Thống kê.

## Tác giả

- Họ và tên: Thái Bùi Trần Quốc
- Chuyên ngành: Công nghệ thông tin
- GitHub: https://github.com/Thai101-alt

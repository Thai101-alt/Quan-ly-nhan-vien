package Model;

import java.math.BigDecimal;
import java.sql.Date;

public class Nhanvien_m {

    private String manv;
    private String hoten;
    private Date ngaysinh;
    private String gioitinh;
    private String sdt;
    private String email;
    private String diachi;
    private String mapb;
    private String macv;
    private Date ngayvaolam;
    private String trangthai;
    private BigDecimal luongcoban;
    private BigDecimal hesoluong;

    public String getManv() {
        return manv;
    }

    public BigDecimal getLuongcoban() {
        return luongcoban;
    }

    public void setLuongcoban(BigDecimal luongcoban) {
        this.luongcoban = luongcoban;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public Date getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(Date ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getMapb() {
        return mapb;
    }

    public void setMapb(String mapb) {
        this.mapb = mapb;
    }

    public String getMacv() {
        return macv;
    }

    public void setMacv(String macv) {
        this.macv = macv;
    }

    public Date getNgayvaolam() {
        return ngayvaolam;
    }

    public void setNgayvaolam(Date ngayvaolam) {
        this.ngayvaolam = ngayvaolam;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public BigDecimal getHeSoLuong() {
        return hesoluong;
    }

    public void setHeSoLuong(BigDecimal hs) {
        this.hesoluong = hs;
    }

    @Override
    public String toString() {
        return "Mã NV: " + manv + ", Họ tên: " + hoten;
    }

    public String toFull() {
        return "Mã NV: " + manv + "\n"
                + "Họ tên: " + hoten + "\n"
                + "Ngày sinh: " + ngaysinh + "\n"
                + "Giới tính: " + gioitinh + "\n"
                + "SĐT: " + sdt + "\n"
                + "Email: " + email + "\n"
                + "Địa chỉ: " + diachi + "\n"
                + "Phòng ban: " + mapb + "\n"
                + "Chức vụ: " + macv + "\n"
                + "Ngày vào làm: " + ngayvaolam + "\n"
                + "Trạng thái: " + trangthai + "\n"
                + "Lương cơ bản: " + (luongcoban != null ? luongcoban : "0");
    }

}

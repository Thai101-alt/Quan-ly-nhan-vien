package Model;

import java.sql.Date;
import java.math.BigDecimal;

public class Hopdong_m {

    private String mahd;
    private String manv;
    private String loaihd;
    private Date ngaybatdau;
    private Date ngayketthuc;
    private BigDecimal luongcoban;

    public String getMahd() {
        return mahd;
    }

    public void setMahd(String mahd) {
        this.mahd = mahd;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public String getLoaihd() {
        return loaihd;
    }

    public void setLoaihd(String loaihd) {
        this.loaihd = loaihd;
    }

    public Date getNgaybatdau() {
        return ngaybatdau;
    }

    public void setNgaybatdau(Date ngaybatdau) {
        this.ngaybatdau = ngaybatdau;
    }

    public Date getNgayketthuc() {
        return ngayketthuc;
    }

    public void setNgayketthuc(Date ngayketthuc) {
        this.ngayketthuc = ngayketthuc;
    }

    public BigDecimal getLuongcoban() {
        return luongcoban;
    }

    public void setLuongcoban(BigDecimal luongcoban) {
        this.luongcoban = luongcoban;
    }

    @Override
    public String toString() {
        return "Mã HD: " + mahd + ", Mã NV: " + manv;
    }

    public String toFullString() {
        return "Mã HD: " + mahd + "\n"
                + "Mã NV: " + manv + "\n"
                + "Loại HD: " + loaihd + "\n"
                + "Ngày bắt đầu: " + ngaybatdau + "\n"
                + "Ngày kết thúc: " + ngayketthuc + "\n"
                + "Lương cơ bản: " + (luongcoban != null ? luongcoban : "0");
    }

    public String toHopDongDienTu(Nhanvien_m nv,
            Phongban_m pb,
            Chucvu_m cv) {

        return """
        =========================================
                 HỢP ĐỒNG LAO ĐỘNG
        =========================================

        I. THÔNG TIN CÔNG TY
        Tên công ty : CÔNG TY TNHH ABC
        Địa chỉ     : Việt Nam

        -----------------------------------------

        II. THÔNG TIN NHÂN VIÊN
        Mã NV       : %s
        Họ và tên   : %s
        Ngày sinh  : %s
        Giới tính  : %s
        Phòng ban  : %s
        Chức vụ    : %s

        -----------------------------------------

        III. THÔNG TIN HỢP ĐỒNG
        Mã hợp đồng: %s
        Loại HĐ    : %s
        Ngày bắt đầu : %s
        Ngày kết thúc: %s

        -----------------------------------------

        IV. LƯƠNG & CHẾ ĐỘ
        Lương cơ bản: %, .0f VNĐ
        Hình thức trả lương: Chuyển khoản

        -----------------------------------------

        Hai bên cam kết thực hiện đúng các điều
        khoản đã thỏa thuận trong hợp đồng này.

        Ngày ký hợp đồng: %s

        ĐẠI DIỆN CÔNG TY            NHÂN VIÊN
        (Ký, ghi rõ họ tên)        (Ký, ghi rõ họ tên)

        =========================================
        """.formatted(
                nv.getManv(),
                nv.getHoten(),
                nv.getNgaysinh(),
                nv.getGioitinh(),
                pb.getTenpb(),
                cv.getTencv(),
                mahd,
                loaihd,
                ngaybatdau,
                ngayketthuc,
                luongcoban != null ? luongcoban : BigDecimal.ZERO,
                new java.sql.Date(System.currentTimeMillis())
        );
    }

}

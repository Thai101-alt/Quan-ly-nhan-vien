package Model;

public class ChamCong_m {
    private String manv;
    private int thang;
    private int nam;
    private int ngayCong; // số ngày làm
    private int ngayNghi; // số ngày nghỉ
    private int tangCa;   // số ngày tăng ca

    public ChamCong_m() {}

    public ChamCong_m(String manv, int thang, int nam, int ngayCong, int ngayNghi, int tangCa) {
        this.manv = manv;
        this.thang = thang;
        this.nam = nam;
        this.ngayCong = ngayCong;
        this.ngayNghi = ngayNghi;
        this.tangCa = tangCa;
    }

    // ===== GETTER & SETTER =====
    public String getManv() { return manv; }
    public void setManv(String manv) { this.manv = manv; }

    public int getThang() { return thang; }
    public void setThang(int thang) { this.thang = thang; }

    public int getNam() { return nam; }
    public void setNam(int nam) { this.nam = nam; }

    public int getNgayCong() { return ngayCong; }
    public void setNgayCong(int ngayCong) { this.ngayCong = ngayCong; }

    public int getNgayNghi() { return ngayNghi; }
    public void setNgayNghi(int ngayNghi) { this.ngayNghi = ngayNghi; }

    public int getTangCa() { return tangCa; }
    public void setTangCa(int tangCa) { this.tangCa = tangCa; }
}

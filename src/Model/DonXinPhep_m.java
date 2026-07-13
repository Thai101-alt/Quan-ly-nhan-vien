package Model;

import java.sql.Date;

public class DonXinPhep_m {

    private String madon;
    private String manv;
    private Date ngaygui;
    private Date ngaybatdau;
    private Date ngayketthuc;
    private String lydonghi;
    private String trangthai;
    private String hoten;

    public DonXinPhep_m() {
    }

    public DonXinPhep_m(String madon, String manv, Date ngaygui, Date ngaybatdau, Date ngayketthuc, String lydonghi, String trangthai) {
        this.madon = madon;
        this.manv = manv;
        this.ngaygui = ngaygui;
        this.ngaybatdau = ngaybatdau;
        this.ngayketthuc = ngayketthuc;
        this.lydonghi = lydonghi;
        this.trangthai = trangthai;
    }

    public String getMadon() {
        return madon;
    }

    public void setMadon(String madon) {
        this.madon = madon;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public Date getNgaygui() {
        return ngaygui;
    }

    public void setNgaygui(Date ngaygui) {
        this.ngaygui = ngaygui;
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

    public String getLydonghi() {
        return lydonghi;
    }

    public void setLydonghi(String lydonghi) {
        this.lydonghi = lydonghi;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }
}

package Model;

import java.math.BigDecimal;

public class Luong_m {

    private String maluong;
    private String manv;
    private int thang;
    private int nam;
    private BigDecimal tongluong;

    public Luong_m(String maluong, String manv, int thang, int nam, BigDecimal tongluong) {
        this.maluong = maluong;
        this.manv = manv;
        this.thang = thang;
        this.nam = nam;
        this.tongluong = tongluong;
    }

    public String getMaluong() {
        return maluong;
    }

    public void setMaluong(String maluong) {
        this.maluong = maluong;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }

    public BigDecimal getTongluong() {
        return tongluong;
    }

    public void setTongluong(BigDecimal tongluong) {
        this.tongluong = tongluong;
    }

    @Override
    public String toString() {
        return "Mã Lương: " + maluong + ", MãNV: " + manv;
    }

    
    public String toFullString() {
        return "Mã Lương: " + maluong + "\n"
                + "Mã NV: " + manv + "\n"
                + "Tháng: " + thang + "\n"
                + "Năm: " + nam + "\n"
                + "Tổng lương: " + (tongluong != null ? tongluong : "0");
    }
}

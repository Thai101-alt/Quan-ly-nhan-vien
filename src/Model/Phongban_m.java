package Model;

public class Phongban_m {

    private String mapb;
    private String tenpb;
    private String mota;

    public Phongban_m() {
    }

    public Phongban_m(String mapb, String tenpb, String mota) {
        this.mapb = mapb;
        this.tenpb = tenpb;
        this.mota = mota;
    }

    public String getMapb() {
        return mapb;
    }

    public void setMapb(String mapb) {
        this.mapb = mapb;
    }

    public String getTenpb() {
        return tenpb;
    }

    public void setTenpb(String tenpb) {
        this.tenpb = tenpb;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    @Override
    public String toString() {
        return "Mã PB: " + mapb + ", Tên PB: " + tenpb;
    }

    public String toFullString() {
        return "Mã PB: " + mapb + "\n"
                + "Tên PB: " + tenpb + "\n"
                + "Mô tả: " + mota;
    }
}

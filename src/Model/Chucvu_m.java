package Model;

public class Chucvu_m {

    private String macv;
    private String tencv;
    private double hesoluong;

    public Chucvu_m() {
    }

    public Chucvu_m(String macv, String tencv, double hesoluong) {
        this.macv = macv;
        this.tencv = tencv;
        this.hesoluong = hesoluong;
    }

    public String getMacv() {
        return macv;
    }

    public void setMacv(String macv) {
        this.macv = macv;
    }

    public String getTencv() {
        return tencv;
    }

    public void setTencv(String tencv) {
        this.tencv = tencv;
    }

    public double getHesoluong() {
        return hesoluong;
    }

    public void setHesoluong(double hesoluong) {
        this.hesoluong = hesoluong;
    }

    @Override
    public String toString() {
        return "Mã CV: " + macv + ", Tên CV: " + tencv;
    }

    public String toFullString() {
        return "Mã CV: " + macv + "\n"
                + "Tên CV: " + tencv + "\n"
                + "Hệ số lương: " + hesoluong;
    }
}

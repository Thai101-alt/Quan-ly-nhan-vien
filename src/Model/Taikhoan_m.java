package Model;

public class Taikhoan_m {
    private String username;
    private String password;
    private String quyen;
    private String manv;

    public Taikhoan_m() {}

    public Taikhoan_m(String username, String password, String quyen, String manv) {
        this.username = username;
        this.password = password;
        this.quyen = quyen;
        this.manv = manv;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuyen() {
        return quyen;
    }

    public void setQuyen(String quyen) {
        this.quyen = quyen;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

  
}

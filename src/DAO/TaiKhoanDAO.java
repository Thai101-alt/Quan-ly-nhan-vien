package DAO;

import Model.Taikhoan_m;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class TaiKhoanDAO {

    ConnectionDB db = new ConnectionDB();

    public String getRole(String username, String password) {
        String sql = "SELECT quyen FROM taikhoan WHERE username = ? AND password = ?";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ps.setString(2, password.trim());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("quyen");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Taikhoan_m> getAll() {
        List<Taikhoan_m> list = new ArrayList<>();
        String sql = "SELECT * FROM taikhoan";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Taikhoan_m tk = new Taikhoan_m();
                tk.setUsername(rs.getString("username"));
                tk.setPassword(rs.getString("password"));
                tk.setQuyen(rs.getString("quyen"));
                tk.setManv(rs.getString("manv"));
                list.add(tk);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Taikhoan_m tk) {
        String sql = "INSERT INTO taikhoan(username, password, quyen, manv) VALUES (?,?,?,?)";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tk.getUsername());
            ps.setString(2, tk.getPassword()); // nên mã hóa
            ps.setString(3, tk.getQuyen());
            ps.setString(4,
                    tk.getManv() == null || tk.getManv().isEmpty()
                    ? null
                    : tk.getManv());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("❌ Lỗi thêm tài khoản (trùng username hoặc manv không tồn tại)");
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Taikhoan_m tk) {

        if (tk.getManv() != null && !tk.getManv().isEmpty()) {
            String sqlCheck = "SELECT COUNT(*) FROM nhanvien WHERE manv=?";
            try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sqlCheck)) {
                ps.setString(1, tk.getManv());
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(null, "Mã nhân viên không tồn tại!");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        String sql = """
        UPDATE taikhoan
        SET password = ?, quyen = ?, manv = ?
        WHERE username = ?
    """;

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tk.getPassword());
            ps.setString(2, tk.getQuyen());
            ps.setString(3,
                    tk.getManv() == null || tk.getManv().isEmpty()
                    ? null
                    : tk.getManv());
            ps.setString(4, tk.getUsername());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getManv(String username, String password) {
        String sql = "SELECT manv FROM taikhoan WHERE username=? AND password=?";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ps.setString(2, password.trim());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("manv");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(String username) {
        String sql = "DELETE FROM taikhoan WHERE username = ?";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean exists(String username) {
        String sql = "SELECT 1 FROM taikhoan WHERE username = ?";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

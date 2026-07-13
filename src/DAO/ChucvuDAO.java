package DAO;

import Model.Chucvu_m;
import java.sql.*;
import java.util.ArrayList;

public class ChucvuDAO {

    private ConnectionDB db = new ConnectionDB();

    /* =============== LẤY DANH SÁCH =============== */
    public ArrayList<Chucvu_m> getAll() {
        ArrayList<Chucvu_m> list = new ArrayList<>();
        String sql = "SELECT macv, tencv, hesoluong FROM dbo.chucvu";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Chucvu_m cv = new Chucvu_m(
                        rs.getString("macv"),
                        rs.getString("tencv"),
                        rs.getDouble("hesoluong")
                );
                list.add(cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* =============== THÊM =============== */
    public boolean insert(Chucvu_m cv) {
        String sql = "INSERT INTO dbo.chucvu(macv, tencv, hesoluong) VALUES (?, ?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cv.getMacv());
            ps.setString(2, cv.getTencv());
            ps.setDouble(3, cv.getHesoluong());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* =============== CẬP NHẬT =============== */
    public boolean update(Chucvu_m cv) {
        String sql = "UPDATE dbo.chucvu SET tencv=?, hesoluong=? WHERE macv=?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cv.getTencv());
            ps.setDouble(2, cv.getHesoluong());
            ps.setString(3, cv.getMacv());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* =============== XÓA =============== */
    public boolean delete(String macv) {
        String sql = "DELETE FROM dbo.chucvu WHERE macv=?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, macv);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* =============== TỰ SINH MÃ =============== */
    public String generateMaCV() {
        String sql = "SELECT MAX(macv) FROM dbo.chucvu";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next() && rs.getString(1) != null) {
                int num = Integer.parseInt(rs.getString(1).substring(2)) + 1;
                return String.format("CV%03d", num);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "CV001";
    }
    /* =============== LẤY THEO MÃ =============== */
public Chucvu_m getById(String macv) {
    String sql = "SELECT macv, tencv, hesoluong FROM dbo.chucvu WHERE macv = ?";

    try (Connection con = db.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, macv);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Chucvu_m(
                    rs.getString("macv"),
                    rs.getString("tencv"),
                    rs.getDouble("hesoluong")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

}

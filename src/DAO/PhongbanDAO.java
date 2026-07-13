package DAO;

import Model.Phongban_m;
import java.sql.*;
import java.util.ArrayList;

public class PhongbanDAO {

    private ConnectionDB db = new ConnectionDB();

    /* ================= LẤY DANH SÁCH ================= */
    public ArrayList<Phongban_m> getAll() {
        ArrayList<Phongban_m> list = new ArrayList<>();
        String sql = "SELECT mapb, tenpb, mota FROM phongban";

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Phongban_m pb = new Phongban_m();
                pb.setMapb(rs.getString("mapb"));
                pb.setTenpb(rs.getString("tenpb"));
                pb.setMota(rs.getString("mota"));
                list.add(pb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /* ================= THÊM PHÒNG BAN ================= */
    public boolean insert(Phongban_m pb) {
        String sql = "INSERT INTO phongban (mapb, tenpb, mota) VALUES (?, ?, ?)";

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pb.getMapb());
            ps.setString(2, pb.getTenpb());
            ps.setString(3, pb.getMota());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ================= CẬP NHẬT ================= */
    public boolean update(Phongban_m pb) {
        String sql = "UPDATE phongban SET tenpb=?, mota=? WHERE mapb=?";

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pb.getTenpb());
            ps.setString(2, pb.getMota());
            ps.setString(3, pb.getMapb());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ================= XÓA ================= */
    public boolean delete(String mapb) {
        String sql = "DELETE FROM phongban WHERE mapb=?";

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mapb);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ================= TỰ SINH MÃ PB ================= */
    public String generateMaPB() {
        String sql = "SELECT MAX(mapb) FROM phongban";

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                String max = rs.getString(1); // PB007
                int num = Integer.parseInt(max.substring(2)) + 1;
                return String.format("PB%03d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "PB001";
    }

    public Phongban_m getById(String mapb) {
        String sql = "SELECT mapb, tenpb, mota FROM phongban WHERE mapb=?";
        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mapb);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Phongban_m pb = new Phongban_m();
                pb.setMapb(rs.getString("mapb"));
                pb.setTenpb(rs.getString("tenpb"));
                pb.setMota(rs.getString("mota"));
                return pb;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

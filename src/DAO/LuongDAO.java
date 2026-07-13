package DAO;

import Model.Luong_m;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LuongDAO {

    ConnectionDB db = new ConnectionDB();

    /* ===== THÊM LƯƠNG ===== */
    public boolean insert(Luong_m l) {
        String sql = "INSERT INTO luong(maluong, manv, thang, nam, tongluong) VALUES (?,?,?,?,?)";
        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, l.getMaluong());
            ps.setString(2, l.getManv());
            ps.setInt(3, l.getThang());
            ps.setInt(4, l.getNam());
            ps.setBigDecimal(5, l.getTongluong());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ===== KIỂM TRA TRÙNG LƯƠNG ===== */
    public boolean exists(String manv, int thang, int nam) {
        String sql = "SELECT COUNT(*) FROM luong WHERE manv=? AND thang=? AND nam=?";
        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, manv);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ===== LẤY TẤT CẢ ===== */
    public ArrayList<Luong_m> getAll() {
        ArrayList<Luong_m> list = new ArrayList<>();
        String sql = "SELECT * FROM luong ORDER BY nam DESC, thang DESC";
        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Luong_m(
                        rs.getString("maluong"),
                        rs.getString("manv"),
                        rs.getInt("thang"),
                        rs.getInt("nam"),
                        rs.getBigDecimal("tongluong")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ===== XÓA ===== */
    public boolean delete(String maluong) {
        String sql = "DELETE FROM luong WHERE maluong=?";
        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maluong);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ===== TỔNG LƯƠNG THÁNG ===== */
    public BigDecimal tongLuongThang(int thang, int nam) {
        String sql = "SELECT SUM(tongluong) FROM luong WHERE thang=? AND nam=?";
        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1) == null ? BigDecimal.ZERO : rs.getBigDecimal(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    /* ===== TẠO MÃ LƯƠNG (CHUẨN) ===== */
    public String generateMaLuong() {
        String sql = "SELECT MAX(maluong) FROM luong";
        try (
                Connection con = db.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next() && rs.getString(1) != null) {
                int n = Integer.parseInt(rs.getString(1).substring(1)) + 1;
                return "L" + String.format("%03d", n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "L001";
    }

    public List<Luong_m> getByUser(String manv) {
        List<Luong_m> list = new ArrayList<>();
        String sql = "SELECT * FROM luong WHERE manv=? ORDER BY nam DESC, thang DESC";
        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, manv);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Luong_m(
                        rs.getString("maluong"),
                        rs.getString("manv"),
                        rs.getInt("thang"),
                        rs.getInt("nam"),
                        rs.getBigDecimal("tongluong")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ===== LẤY HỆ SỐ LƯƠNG THEO NHÂN VIÊN ===== */
    public BigDecimal getHeSoLuong(String manv) {
        String sql = "SELECT cv.hesoluong "
                + "FROM nhanvien nv "
                + "JOIN chucvu cv ON nv.macv = cv.macv "
                + "WHERE nv.manv = ?";
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, manv);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("hesoluong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal tatcaLuongThang(int thang, int nam) {
        BigDecimal tong = BigDecimal.ZERO;
        for (Luong_m l : getAll()) {
            if (l.getThang() == thang && l.getNam() == nam) {
                tong = tong.add(l.getTongluong());
            }
        }
        return tong;
    }

}

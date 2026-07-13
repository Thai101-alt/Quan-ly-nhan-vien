package DAO;

import java.sql.*;
import java.util.ArrayList;
import Model.Hopdong_m;

public class HopdongDAO {

    private ConnectionDB db = new ConnectionDB();

    /* ========== LẤY DANH SÁCH HỢP ĐỒNG ========== */
    public ArrayList<Hopdong_m> getAll() {
        ArrayList<Hopdong_m> list = new ArrayList<>();
        String sql = """
            SELECT mahd, manv, loaihd, ngaybatdau, ngayketthuc, luongcoban
            FROM hopdong
            ORDER BY mahd
        """;

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Hopdong_m hd = new Hopdong_m();
                hd.setMahd(rs.getString("mahd"));
                hd.setManv(rs.getString("manv"));
                hd.setLoaihd(rs.getString("loaihd"));
                hd.setNgaybatdau(rs.getDate("ngaybatdau"));
                hd.setNgayketthuc(rs.getDate("ngayketthuc"));
                hd.setLuongcoban(rs.getBigDecimal("luongcoban"));
                list.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ========== THÊM HỢP ĐỒNG ========== */
    public boolean insert(Hopdong_m hd) {

        String sql = """
            INSERT INTO hopdong
            (mahd, manv, loaihd, ngaybatdau, ngayketthuc, luongcoban)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hd.getMahd());
            ps.setString(2, hd.getManv());   // FK NV
            ps.setString(3, hd.getLoaihd());
            ps.setDate(4, hd.getNgaybatdau());
            ps.setDate(5, hd.getNgayketthuc());
            ps.setBigDecimal(6, hd.getLuongcoban());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ========== SỬA HỢP ĐỒNG ========== */
    public boolean update(Hopdong_m hd) {

        String sql = """
            UPDATE hopdong SET
                manv = ?,
                loaihd = ?,
                ngaybatdau = ?,
                ngayketthuc = ?,
                luongcoban = ?
            WHERE mahd = ?
        """;

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hd.getManv());
            ps.setString(2, hd.getLoaihd());
            ps.setDate(3, hd.getNgaybatdau());
            ps.setDate(4, hd.getNgayketthuc());
            ps.setBigDecimal(5, hd.getLuongcoban());
            ps.setString(6, hd.getMahd());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ========== XÓA HỢP ĐỒNG ========== */
    public boolean delete(String mahd) {

        String sql = "DELETE FROM hopdong WHERE mahd = ?";

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, mahd);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ========== SINH MÃ HỢP ĐỒNG ========== */
    public String generateMaHD() {
        String sql = """
            SELECT MAX(CAST(SUBSTRING(mahd, 3, LEN(mahd)) AS INT))
            FROM hopdong
            WHERE mahd LIKE 'HD%'
        """;

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                int num = rs.getInt(1) + 1;
                return String.format("HD%03d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "HD001";
    }

    /* ========== LẤY HỢP ĐỒNG THEO NHÂN VIÊN ========== */
    public Hopdong_m getByManv(String manv) {
        String sql = """
        SELECT TOP 1 mahd, manv, loaihd, ngaybatdau, ngayketthuc, luongcoban
        FROM hopdong
        WHERE manv = ?
        ORDER BY ngaybatdau DESC
    """;

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, manv);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Hopdong_m hd = new Hopdong_m();
                hd.setMahd(rs.getString("mahd"));
                hd.setManv(rs.getString("manv"));
                hd.setLoaihd(rs.getString("loaihd"));
                hd.setNgaybatdau(rs.getDate("ngaybatdau"));
                hd.setNgayketthuc(rs.getDate("ngayketthuc"));
                hd.setLuongcoban(rs.getBigDecimal("luongcoban"));
                return hd;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

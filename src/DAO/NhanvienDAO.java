package DAO;

import java.sql.*;
import java.util.ArrayList;
import Model.Nhanvien_m;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NhanvienDAO {

    private ConnectionDB db = new ConnectionDB();

    /* ========== LẤY DANH SÁCH (SELECT THƯỜNG) ========== */
    public ArrayList<Nhanvien_m> getAll() {
        ArrayList<Nhanvien_m> list = new ArrayList<>();
        String sql = """
        SELECT
            manv, hoten, ngaysinh, gioitinh, sdt, email, diachi,
            mapb, macv, ngayvaolam, trangthai, luongcoban
        FROM nhanvien
        ORDER BY manv
    """;

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Nhanvien_m nv = new Nhanvien_m();
                nv.setManv(rs.getString("manv"));
                nv.setHoten(rs.getString("hoten"));
                nv.setNgaysinh(rs.getDate("ngaysinh"));
                nv.setGioitinh(rs.getString("gioitinh"));
                nv.setSdt(rs.getString("sdt"));
                nv.setEmail(rs.getString("email"));
                nv.setDiachi(rs.getString("diachi"));
                nv.setMapb(rs.getString("mapb"));
                nv.setMacv(rs.getString("macv"));
                nv.setNgayvaolam(rs.getDate("ngayvaolam"));
                nv.setTrangthai(rs.getString("trangthai"));
                nv.setLuongcoban(rs.getBigDecimal("luongcoban"));
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* ========== THÊM NHÂN VIÊN ========== */
    public boolean insert(Nhanvien_m nv) {

        String sql = """
        INSERT INTO nhanvien
        (manv, hoten, ngaysinh, gioitinh, sdt, email, diachi,
         mapb, macv, ngayvaolam, trangthai, luongcoban)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getManv());
            ps.setString(2, nv.getHoten());
            ps.setDate(3, nv.getNgaysinh());
            ps.setString(4, nv.getGioitinh());
            ps.setString(5, nv.getSdt());
            ps.setString(6, nv.getEmail());
            ps.setString(7, nv.getDiachi());
            ps.setString(8, nv.getMapb());
            ps.setString(9, nv.getMacv());
            ps.setDate(10, nv.getNgayvaolam());
            ps.setString(11, nv.getTrangthai());
            ps.setBigDecimal(12, nv.getLuongcoban());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ========== SỬA NHÂN VIÊN ========== */
    public boolean update(Nhanvien_m nv) {

        String sql = """
        UPDATE nhanvien SET
            hoten = ?,
            ngaysinh = ?,
            gioitinh = ?,
            sdt = ?,
            email = ?,
            diachi = ?,
            mapb = ?,
            macv = ?,
            ngayvaolam = ?,
            trangthai = ?,
            luongcoban = ?
        WHERE manv = ?
    """;

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getHoten());
            ps.setDate(2, nv.getNgaysinh());
            ps.setString(3, nv.getGioitinh());
            ps.setString(4, nv.getSdt());
            ps.setString(5, nv.getEmail());
            ps.setString(6, nv.getDiachi());
            ps.setString(7, nv.getMapb());
            ps.setString(8, nv.getMacv());
            ps.setDate(9, nv.getNgayvaolam());
            ps.setString(10, nv.getTrangthai());
            ps.setBigDecimal(11, nv.getLuongcoban()); // ✅
            ps.setString(12, nv.getManv());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /* ========== XÓA NHÂN VIÊN ========== */
    public boolean delete(String manv) {

        String sql = "DELETE FROM nhanvien WHERE manv = ?";

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, manv);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM nhanvien";
        return getCount(sql);
    }

    public int countByGender(String gt) {
        String sql = "SELECT COUNT(*) FROM nhanvien WHERE gioitinh = ?";
        return getCount(sql, gt);
    }

    public Map<String, Integer> countByPhongBan() {
        String sql = """
            SELECT pb.tenpb, COUNT(*)
            FROM nhanvien nv
            JOIN phongban pb ON nv.mapb = pb.mapb
            GROUP BY pb.tenpb
            """;
        return getMap(sql);
    }

    public Map<String, Integer> countByChucVu() {
        String sql = """
            SELECT cv.tencv, COUNT(*)
            FROM nhanvien nv
            JOIN chucvu cv ON nv.macv = cv.macv
            GROUP BY cv.tencv
            """;
        return getMap(sql);
    }

    // ================= HÀM DÙNG CHUNG =================
    private int getCount(String sql, Object... params) {
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Map<String, Integer> getMap(String sql) {
        Map<String, Integer> map = new LinkedHashMap<>();
        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString(1), rs.getInt(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /* ========== SINH MÃ NV ========== */
    public String generateMaNV() {
        String sql = """
            SELECT MAX(CAST(SUBSTRING(manv, 3, LEN(manv)) AS INT))
            FROM nhanvien
            WHERE manv LIKE 'NV%'
        """;

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                int num = rs.getInt(1) + 1;
                return String.format("NV%03d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "NV001";
    }

// Lấy danh sách nhân viên kèm hệ số lương từ chức vụ
    public ArrayList<Nhanvien_m> getnvvl() {
        ArrayList<Nhanvien_m> list = new ArrayList<>();
        String sql = "SELECT nv.manv, nv.hoten, nv.luongcoban, cv.hesoluong "
                + "FROM nhanvien nv "
                + "JOIN chucvu cv ON nv.macv = cv.macv";

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Nhanvien_m nv = new Nhanvien_m();
                nv.setManv(rs.getString("manv"));
                nv.setHoten(rs.getString("hoten"));
                nv.setLuongcoban(rs.getBigDecimal("luongcoban"));
                nv.setHeSoLuong(rs.getBigDecimal("hesoluong"));
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getNhanvienChuaCoTK() {
        List<String> list = new ArrayList<>();
        String sql = """
        SELECT manv, hoten
        FROM nhanvien
        WHERE manv NOT IN (SELECT manv FROM taikhoan WHERE manv IS NOT NULL)
    """;

        try (Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("manv") + " - " + rs.getString("hoten"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Nhanvien_m> getNhanVienChuaCoHopDong() {
        ArrayList<Nhanvien_m> list = new ArrayList<>();

        String sql = """
        SELECT nv.*
        FROM nhanvien nv
        WHERE NOT EXISTS (
            SELECT 1 FROM hopdong hd
            WHERE hd.manv = nv.manv
        )
    """;

        try (
                Connection con = db.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Nhanvien_m nv = new Nhanvien_m();
                nv.setManv(rs.getString("manv"));
                nv.setHoten(rs.getString("hoten"));
                nv.setNgayvaolam(rs.getDate("ngayvaolam"));
                nv.setLuongcoban(rs.getBigDecimal("luongcoban"));
                list.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}

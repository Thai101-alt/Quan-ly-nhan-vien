package DAO;

import Model.DonXinPhep_m;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonXinPhepDAO {

    private Connection conn;

    public DonXinPhepDAO() {
        try {
            conn = new ConnectionDB().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Thêm đơn mới
    public boolean insert(DonXinPhep_m don) {
        String sql = "INSERT INTO donxinphep (madon, manv, ngaygui, ngaybatdau, ngayketthuc, lydonghi, trangthai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, don.getMadon());
            ps.setString(2, don.getManv());
            ps.setDate(3, don.getNgaygui());
            ps.setDate(4, don.getNgaybatdau());
            ps.setDate(5, don.getNgayketthuc());
            ps.setString(6, don.getLydonghi());
            ps.setString(7, don.getTrangthai());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách đơn của một nhân viên
    public List<DonXinPhep_m> getByUser(String manv) {
        List<DonXinPhep_m> list = new ArrayList<>();
        String sql = "SELECT * FROM donxinphep WHERE manv=? ORDER BY ngaygui DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, manv);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new DonXinPhep_m(
                        rs.getString("madon"),
                        rs.getString("manv"),
                        rs.getDate("ngaygui"),
                        rs.getDate("ngaybatdau"),
                        rs.getDate("ngayketthuc"),
                        rs.getString("lydonghi"),
                        rs.getString("trangthai")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật trạng thái đơn (duyệt / từ chối)
    public boolean updateStatus(String madon, String trangthai) {
        String sql = "UPDATE donxinphep SET trangthai=? WHERE madon=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, trangthai);
            ps.setString(2, madon);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra mã đơn đã tồn tại chưa
    public boolean exists(String madon) {
        String sql = "SELECT * FROM donxinphep WHERE madon=?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, madon);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tạo mã đơn tự động: PX_001, PX_002...
    public String generateMaDon() {
        String sql = "SELECT TOP 1 madon FROM donxinphep ORDER BY madon DESC";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                String last = rs.getString("madon");
                int num = Integer.parseInt(last.split("_")[1]) + 1;
                return String.format("PX_%03d", num);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "PX_001";
    }

    public List<DonXinPhep_m> getAllForAdmin() {
        List<DonXinPhep_m> list = new ArrayList<>();

        String sql = """
        SELECT d.madon, d.manv, nv.hoten,
               d.ngaygui, d.ngaybatdau, d.ngayketthuc,
               d.lydonghi, d.trangthai
        FROM donxinphep d
        JOIN nhanvien nv ON d.manv = nv.manv
        ORDER BY d.ngaygui DESC
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DonXinPhep_m d = new DonXinPhep_m();
                d.setMadon(rs.getString("madon"));
                d.setManv(rs.getString("manv"));
                d.setHoten(rs.getString("hoten"));
                d.setNgaygui(rs.getDate("ngaygui"));
                d.setNgaybatdau(rs.getDate("ngaybatdau"));
                d.setNgayketthuc(rs.getDate("ngayketthuc"));
                d.setLydonghi(rs.getString("lydonghi"));
                d.setTrangthai(rs.getString("trangthai"));
                list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}

package DAO;

import Model.ChamCong_m;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChamcongDAO {

    private Connection conn;

    public ChamcongDAO() {
        try {
            conn = new ConnectionDB().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy dữ liệu chấm công của nhân viên theo tháng/năm
    public ChamCong_m getChamCong(String manv, int thang, int nam) {
        String sql = "SELECT ngay, sogiolam FROM chamcong WHERE manv=? AND MONTH(ngay)=? AND YEAR(ngay)=?";
        int ngayCong = 0, ngayNghi = 0, tangCa = 0;
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, manv);
            ps.setInt(2, thang);
            ps.setInt(3, nam);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                float gio = rs.getFloat("sogiolam");
                if (gio == 0) {
                    ngayNghi++;
                } else if (gio > 8) {
                    tangCa++;
                } else {
                    ngayCong++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ChamCong_m(manv, thang, nam, ngayCong, ngayNghi, tangCa);
    }

    // Lưu hoặc cập nhật chấm công
    public boolean insertOrUpdate(ChamCong_m cc) {
        try {
            // Xóa dữ liệu cũ của tháng/năm này
            String del = "DELETE FROM chamcong WHERE manv=? AND MONTH(ngay)=? AND YEAR(ngay)=?";
            PreparedStatement psDel = conn.prepareStatement(del);
            psDel.setString(1, cc.getManv());
            psDel.setInt(2, cc.getThang());
            psDel.setInt(3, cc.getNam());
            psDel.executeUpdate();

            // MERGE (UPSERT) chấm công từng ngày
            String mergeSql = "MERGE chamcong AS target "
                    + "USING (SELECT ? AS macc, ? AS manv, ? AS ngay, ? AS sogiolam) AS source "
                    + "ON target.macc = source.macc "
                    + "WHEN MATCHED THEN "
                    + "  UPDATE SET sogiolam = source.sogiolam, ngay = source.ngay "
                    + "WHEN NOT MATCHED THEN "
                    + "  INSERT (macc, manv, ngay, sogiolam) VALUES (source.macc, source.manv, source.ngay, source.sogiolam);";

            PreparedStatement psIns = conn.prepareStatement(mergeSql);

            int ngayCong = cc.getNgayCong();
            int ngayNghi = cc.getNgayNghi();
            int tangCa = cc.getTangCa();

            for (int day = 1; day <= 31; day++) {
                float gio = 8; // mặc định 8h
                if (day <= ngayNghi) {
                    gio = 0;
                } else if (day <= ngayNghi + tangCa) {
                    gio = 10;
                }

                psIns.setString(1, cc.getManv() + "_" + day); // macc
                psIns.setString(2, cc.getManv());             // manv
                psIns.setDate(3, Date.valueOf(String.format("%04d-%02d-%02d", cc.getNam(), cc.getThang(), day))); // ngay
                psIns.setFloat(4, gio);                       // sogiolam
                psIns.addBatch();
            }

            psIns.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách chấm công toàn bộ nhân viên (tạm)
    public List<ChamCong_m> getAll() {
        List<ChamCong_m> list = new ArrayList<>();
        String sql = "SELECT manv, MONTH(ngay) AS thang, YEAR(ngay) AS nam, sogiolam FROM chamcong";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String manv = rs.getString("manv");
                int thang = rs.getInt("thang");
                int nam = rs.getInt("nam");
                int ngayCong = rs.getFloat("sogiolam") > 0 ? 1 : 0;
                list.add(new ChamCong_m(manv, thang, nam, ngayCong, 0, 0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

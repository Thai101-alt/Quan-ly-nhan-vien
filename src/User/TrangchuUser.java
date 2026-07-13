package User;

import DAO.LuongDAO;
import Model.Luong_m;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class TrangchuUser extends JPanel {

    private JLabel lblThongBao;
    private JTable tableLuong;
    private DefaultTableModel modelLuong;
    private LuongDAO luongDAO = new LuongDAO();

    public TrangchuUser() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 245, 255));

        // Tiêu đề
        lblThongBao = new JLabel("Chào mừng! Đây là trang chủ của bạn", JLabel.CENTER);
        lblThongBao.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblThongBao, BorderLayout.NORTH);

        // Bảng hiển thị lương gần nhất
        modelLuong = new DefaultTableModel(
                new String[]{"Tháng", "Năm", "Tổng lương"}, 0
        );
        tableLuong = new JTable(modelLuong);
        tableLuong.setRowHeight(28);

        add(new JScrollPane(tableLuong), BorderLayout.CENTER);

        loadLuongTrangChu();
    }

    public void loadLuongTrangChu() {
        modelLuong.setRowCount(0);
        String currentUser = UserSession.getCurrentUser();
        List<Luong_m> list = luongDAO.getByUser(currentUser);
        if (list.isEmpty()) {
            lblThongBao.setText("Chưa có dữ liệu lương!");
        } else {
            lblThongBao.setText("Tổng hợp lương của bạn");
            for (Luong_m l : list) {
                BigDecimal tong = l.getTongluong();
                modelLuong.addRow(new Object[]{l.getThang(), l.getNam(), tong});
            }
        }
    }
}

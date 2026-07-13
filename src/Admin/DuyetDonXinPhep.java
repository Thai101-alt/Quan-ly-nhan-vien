package Admin;

import DAO.DonXinPhepDAO;
import Model.DonXinPhep_m;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DuyetDonXinPhep extends JPanel {

    JTable table;
    DefaultTableModel model;
    DonXinPhepDAO dao = new DonXinPhepDAO();

    public DuyetDonXinPhep() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("DUYỆT ĐƠN XIN PHÉP", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{
                        "Mã đơn", "Mã NV", "Họ tên",
                        "Ngày gửi", "Từ ngày", "Đến ngày",
                        "Lý do", "Trạng thái"
                }, 0
        );

        table = new JTable(model);
        table.setRowHeight(28);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton btnDuyet = new JButton("✔ Duyệt");
        JButton btnTuChoi = new JButton("✖ Từ chối");
        JButton btnReload = new JButton("🔄 Tải lại");

        pnlBtn.add(btnDuyet);
        pnlBtn.add(btnTuChoi);
        pnlBtn.add(btnReload);

        add(pnlBtn, BorderLayout.SOUTH);

        loadData();

        // DUYỆT
        btnDuyet.addActionListener(e -> updateStatus("Đã duyệt"));

        // TỪ CHỐI
        btnTuChoi.addActionListener(e -> updateStatus("Từ chối"));

        btnReload.addActionListener(e -> loadData());
    }

    private void loadData() {
        model.setRowCount(0);
        for (DonXinPhep_m d : dao.getAllForAdmin()) {
            model.addRow(new Object[]{
                    d.getMadon(),
                    d.getManv(),
                    d.getHoten(),
                    d.getNgaygui(),
                    d.getNgaybatdau(),
                    d.getNgayketthuc(),
                    d.getLydonghi(),
                    d.getTrangthai()
            });
        }
    }

    private void updateStatus(String status) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn đơn cần xử lý!");
            return;
        }

        String trangthai = model.getValueAt(row, 7).toString();
        if (!trangthai.equals("Chờ duyệt")) {
            JOptionPane.showMessageDialog(this, "Đơn này đã được xử lý!");
            return;
        }

        String madon = model.getValueAt(row, 0).toString();
        dao.updateStatus(madon, status);
        loadData();
    }
}

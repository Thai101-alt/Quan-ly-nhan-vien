package Admin;

import DAO.NhanvienDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class ThongkeNhanvien extends JPanel {

    private JLabel lbTong, lbNam, lbNu;
    private JTable tablePB, tableCV;

    private DefaultTableModel modelPB, modelCV;

    private NhanvienDAO dao = new NhanvienDAO();

    public ThongkeNhanvien() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JLabel title = new JLabel("THỐNG KÊ NHÂN VIÊN", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        JPanel pnTop = new JPanel(new GridLayout(1, 3, 15, 15));
        pnTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnTop.setBackground(Color.WHITE);

        lbTong = createBox("Tổng nhân viên");
        lbNam = createBox("Nam");
        lbNu = createBox("Nữ");

        pnTop.add(lbTong);
        pnTop.add(lbNam);
        pnTop.add(lbNu);

        add(pnTop, BorderLayout.CENTER);

        modelPB = new DefaultTableModel(new String[]{"PHÒNG BAN", "Số lượng"}, 0);
        modelCV = new DefaultTableModel(new String[]{"CHỨC VỤ", "Số lượng"}, 0);

        tablePB = createTable(modelPB);
        tableCV = createTable(modelCV);

        JPanel pnBottom = new JPanel(new GridLayout(1, 2, 10, 10));
        pnBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnBottom.setBackground(Color.WHITE);

        pnBottom.add(new JScrollPane(tablePB));
        pnBottom.add(new JScrollPane(tableCV));

        add(pnBottom, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        // Label
        updateBox(lbTong, "Tổng nhân viên", dao.countAll());
        updateBox(lbNam, "Nam", dao.countByGender("Nam"));
        updateBox(lbNu, "Nữ", dao.countByGender("Nữ"));

        // Table
        loadTable(modelPB, dao.countByPhongBan());
        loadTable(modelCV, dao.countByChucVu());
    }

    public void reloadThongke() {
        SwingUtilities.invokeLater(() -> {
            loadData();
            revalidate();
            repaint();
        });
    }

    private JLabel createBox(String title) {
        JLabel lb = new JLabel("", JLabel.CENTER);
        lb.setOpaque(true);
        lb.setBackground(new Color(230, 240, 255));
        lb.setFont(new Font("Arial", Font.PLAIN, 16));
        lb.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return lb;
    }

    private void updateBox(JLabel lb, String title, int value) {
        lb.setText(
                "<html><center>" + title +
                "<br><b style='font-size:26px'>" + value +
                "</b></center></html>"
        );
    }

    private JTable createTable(DefaultTableModel model) {
        JTable tb = new JTable(model);
        tb.setRowHeight(28);
        tb.setEnabled(false);
        tb.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        return tb;
    }

    private void loadTable(DefaultTableModel model, Map<String, Integer> data) {
        model.setRowCount(0);
        for (String key : data.keySet()) {
            model.addRow(new Object[]{key, data.get(key)});
        }
    }
}

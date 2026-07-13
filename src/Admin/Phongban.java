package Admin;

import DAO.PhongbanDAO;
import Model.Phongban_m;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Phongban extends JPanel {

    JTextField txtMaPB, txtTenPB;
    JTextArea txtMoTa;
    JTable table;
    DefaultTableModel model;
    JTextField txtTimKiem;

    PhongbanDAO dao = new PhongbanDAO();

    public Phongban() {

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(255, 182, 193));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        /* ===== TIÊU ĐỀ ===== */
        JLabel lbTitle = new JLabel("QUẢN LÝ PHÒNG BAN", JLabel.CENTER);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 24));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(lbTitle, gbc);

        /* ===== FORM ===== */
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setOpaque(false);
        pnlForm.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Thông tin phòng ban",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(5, 5, 5, 5);
        f.fill = GridBagConstraints.HORIZONTAL;

        txtMaPB = new JTextField();
        txtTenPB = new JTextField();
        txtMoTa = new JTextArea(3, 20);
        txtMaPB.setEditable(false);

        // Mã PB
        f.gridx = 0;
        f.gridy = 0;
        pnlForm.add(new JLabel("Mã phòng ban:"), f);
        f.gridx = 1;
        pnlForm.add(txtMaPB, f);

        // Tên PB
        f.gridx = 0;
        f.gridy = 1;
        pnlForm.add(new JLabel("Tên phòng ban:"), f);
        f.gridx = 1;
        pnlForm.add(txtTenPB, f);

        // Mô tả
        f.gridx = 0;
        f.gridy = 2;
        pnlForm.add(new JLabel("Mô tả:"), f);
        f.gridx = 1;
        pnlForm.add(new JScrollPane(txtMoTa), f);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(pnlForm, gbc);
        /* ===== SEARCH ===== */
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setOpaque(false);

        pnlSearch.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(20);
        pnlSearch.add(txtTimKiem);

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(pnlSearch, gbc);


        /* ===== TABLE ===== */
        model = new DefaultTableModel(
                new String[]{"Mã phòng", "Tên phòng", "Mô tả"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(scroll, gbc);


        /* ===== BUTTON ===== */
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBtn.setOpaque(false);

        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới");

        pnlBtn.add(btnAdd);
        pnlBtn.add(btnUpdate);
        pnlBtn.add(btnDelete);
        pnlBtn.add(btnClear);

        gbc.gridy = 4;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(pnlBtn, gbc);

        /* ===== LOAD DATA ===== */
        loadTable();
        clearForm();
        txtTimKiem.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                timKiem();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                timKiem();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                timKiem();
            }
        }
        );


        /* ===== SỰ KIỆN ===== */
        btnAdd.addActionListener(e -> addPhongBan());
        btnUpdate.addActionListener(e -> updatePhongBan());
        btnDelete.addActionListener(e -> deletePhongBan());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtMaPB.setText(model.getValueAt(row, 0).toString());
                txtTenPB.setText(model.getValueAt(row, 1).toString());
                txtMoTa.setText(model.getValueAt(row, 2).toString());
            }
        });
    }

    /* ===== HÀM XỬ LÝ ===== */
    private void loadTable() {
        model.setRowCount(0);
        for (Phongban_m pb : dao.getAll()) {
            model.addRow(new Object[]{
                pb.getMapb(),
                pb.getTenpb(),
                pb.getMota()
            });
        }
    }

    private void timKiem() {
        String key = txtTimKiem.getText().toLowerCase();
        model.setRowCount(0);

        for (Phongban_m pb : dao.getAll()) {
            if (pb.getMapb().toLowerCase().contains(key)
                    || pb.getTenpb().toLowerCase().contains(key)
                    || pb.getMota().toLowerCase().contains(key)) {

                model.addRow(new Object[]{
                    pb.getMapb(),
                    pb.getTenpb(),
                    pb.getMota()
                });
            }
        }
    }

    private boolean validateForm() {
        if (txtTenPB.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tên phòng ban không được để trống!");
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtMaPB.setText(dao.generateMaPB());
        txtTenPB.setText("");
        txtMoTa.setText("");
        table.clearSelection();
        txtTimKiem.setText("");
        loadTable();

    }

    private void addPhongBan() {
        if (!validateForm()) {
            return;
        }

        Phongban_m pb = new Phongban_m();
        pb.setMapb(txtMaPB.getText());
        pb.setTenpb(txtTenPB.getText());
        pb.setMota(txtMoTa.getText());

        if (dao.insert(pb)) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            loadTable();
            clearForm();
        }
    }

    private void updatePhongBan() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Chọn phòng ban cần sửa!");
            return;
        }
        if (!validateForm()) {
            return;
        }

        Phongban_m pb = new Phongban_m();
        pb.setMapb(txtMaPB.getText());
        pb.setTenpb(txtTenPB.getText());
        pb.setMota(txtMoTa.getText());

        if (dao.update(pb)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadTable();
        }
    }

    private void deletePhongBan() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Chọn phòng ban cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xóa phòng ban này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(txtMaPB.getText())) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadTable();
                clearForm();
            }
        }
    }
}

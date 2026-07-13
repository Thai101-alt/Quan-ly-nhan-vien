package Admin;

import DAO.NhanvienDAO;
import DAO.TaiKhoanDAO;
import Model.Taikhoan_m;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Taikhoan extends JPanel {

    JTextField txtUsername, txtTimKiem;
    JPasswordField txtPassword;
    JComboBox<String> cbQuyen, cbNhanvien;

    JTable table;
    DefaultTableModel model;

    TaiKhoanDAO dao = new TaiKhoanDAO();
    NhanvienDAO nvDAO = new NhanvienDAO();

    public Taikhoan() {

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(255, 182, 193));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        /* ===== TITLE ===== */
        JLabel lbTitle = new JLabel("QUẢN LÝ TÀI KHOẢN", JLabel.CENTER);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lbTitle, gbc);

        /* ===== FORM ===== */
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setOpaque(false);
        pnlForm.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Thông tin tài khoản"
        ));

        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(5, 5, 5, 5);
        f.fill = GridBagConstraints.HORIZONTAL;

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        cbQuyen = new JComboBox<>(new String[]{"Admin", "Nhân viên"});
        cbNhanvien = new JComboBox<>();
        loadNhanvien();

        // Username
        f.gridx = 0;
        f.gridy = 0;
        pnlForm.add(new JLabel("Username:"), f);
        f.gridx = 1;
        pnlForm.add(txtUsername, f);

        // Password
        f.gridx = 0;
        f.gridy = 1;
        pnlForm.add(new JLabel("Password:"), f);
        f.gridx = 1;
        pnlForm.add(txtPassword, f);

        // Quyền
        f.gridx = 0;
        f.gridy = 2;
        pnlForm.add(new JLabel("Quyền:"), f);
        f.gridx = 1;
        pnlForm.add(cbQuyen, f);

        // Nhân viên
        f.gridx = 0;
        f.gridy = 3;
        pnlForm.add(new JLabel("Nhân viên:"), f);
        f.gridx = 1;
        pnlForm.add(cbNhanvien, f);

        gbc.gridy = 1;
        add(pnlForm, gbc);

        /* ===== SEARCH ===== */
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setOpaque(false);
        pnlSearch.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(20);
        pnlSearch.add(txtTimKiem);

        gbc.gridy = 2;
        add(pnlSearch, gbc);

        /* ===== TABLE ===== */
        model = new DefaultTableModel(
                new String[]{"Username", "Quyền", "Mã NV"}, 0);
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

        loadTable();
        clearForm();

        /* ===== EVENTS ===== */
        btnAdd.addActionListener(e -> add());
        btnUpdate.addActionListener(e -> update());
        btnDelete.addActionListener(e -> delete());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }

            txtUsername.setText(safeValue(row, 0));
            cbQuyen.setSelectedItem(safeValue(row, 1));

            String manv = safeValue(row, 2);

            cbNhanvien.removeAllItems();

            if (!manv.isEmpty()) {
                cbNhanvien.addItem(manv);
                cbNhanvien.setSelectedIndex(0);
            } else {
                loadNhanvien();
                cbNhanvien.setEnabled(true);
            }

            txtUsername.setEditable(false);
        });

        txtTimKiem.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                search();
            }
        });
    }

    /* ===== METHODS ===== */
    private void loadNhanvien() {
        cbNhanvien.removeAllItems();
        cbNhanvien.addItem("-- Chọn nhân viên --");
        for (String nv : nvDAO.getNhanvienChuaCoTK()) {
            cbNhanvien.addItem(nv);
        }
    }

    private void loadTable() {
        model.setRowCount(0);
        for (Taikhoan_m tk : dao.getAll()) {
            model.addRow(new Object[]{
                tk.getUsername(),
                tk.getQuyen(),
                tk.getManv()
            });
        }
    }

    private void search() {
        String key = txtTimKiem.getText().toLowerCase();
        model.setRowCount(0);
        for (Taikhoan_m tk : dao.getAll()) {
            if (tk.getUsername().toLowerCase().contains(key)
                    || tk.getQuyen().toLowerCase().contains(key)
                    || (tk.getManv() != null && tk.getManv().toLowerCase().contains(key))) {
                model.addRow(new Object[]{
                    tk.getUsername(),
                    tk.getQuyen(),
                    tk.getManv()
                });
            }
        }
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        cbQuyen.setSelectedIndex(0);
        loadNhanvien();
        txtUsername.setEditable(true);
        table.clearSelection();
        loadTable();
    }

    private void add() {

        String username = txtUsername.getText().trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username không được để trống!");
            return;
        }

        if (dao.exists(username)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Username đã tồn tại!\nVui lòng chọn username khác.",
                    "Trùng username",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (cbNhanvien.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn nhân viên!");
            return;
        }

        String manv = cbNhanvien.getSelectedItem().toString().split(" - ")[0];

        Taikhoan_m tk = new Taikhoan_m(
                username,
                new String(txtPassword.getPassword()),
                cbQuyen.getSelectedItem().toString(),
                manv
        );

        if (dao.insert(tk)) {
            JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
            loadTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm tài khoản thất bại!");
        }
    }

    private void update() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Chọn tài khoản cần cập nhật!");
            return;
        }

        String username = txtUsername.getText();
        String quyen = cbQuyen.getSelectedItem().toString();
        String manv = null;
        if (cbNhanvien.isEnabled() && cbNhanvien.getSelectedIndex() > 0) {
            manv = cbNhanvien.getSelectedItem().toString().split(" - ")[0];
        }

        String password = new String(txtPassword.getPassword());

        Taikhoan_m tk;

        if (password.isEmpty()) {

            tk = new Taikhoan_m(username, null, quyen, manv);
        } else {
            tk = new Taikhoan_m(username, password, quyen, manv);
        }

        if (dao.update(tk)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            clearForm();
        }
    }

    private void delete() {
        if (txtUsername.getText().isEmpty()) {
            return;
        }

        int c = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa tài khoản này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (c == JOptionPane.YES_OPTION && dao.delete(txtUsername.getText())) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            clearForm();
        }
    }

    private String safeValue(int row, int col) {
        Object v = model.getValueAt(row, col);
        return v == null ? "" : v.toString();
    }

}

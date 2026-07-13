package Admin;

import DAO.ChucvuDAO;
import Model.Chucvu_m;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableRowSorter;

public class Chucvu extends JPanel {

    private JTextField txtMaCV, txtTenCV, txtHeSoLuong;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTimKiem;
    private ChucvuDAO dao = new ChucvuDAO();

    public Chucvu() {

        setLayout(new GridBagLayout());
        setBackground(new Color(255, 204, 255));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        /* ===== TITLE ===== */
        JLabel title = new JLabel("QUẢN LÝ CHỨC VỤ", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(title, gbc);

        /* ===== FORM ===== */
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Thông tin chức vụ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(5, 5, 5, 5);
        f.fill = GridBagConstraints.HORIZONTAL;

        txtMaCV = new JTextField();
        txtMaCV.setEditable(false);
        txtTenCV = new JTextField();
        txtHeSoLuong = new JTextField();

        int r = 0;

        addRow(form, f, r++, "Mã chức vụ", txtMaCV, "Tên chức vụ", txtTenCV);
        f.gridy = r++;
        f.gridx = 0;
        f.weightx = 0;
        form.add(new JLabel("Hệ số lương"), f);

        f.gridx = 1;
        f.gridwidth = 3;
        f.weightx = 1;
        form.add(txtHeSoLuong, f);

        f.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridwidth = 4;
        add(form, gbc);
        /* ===== SEARCH ===== */
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlSearch.setOpaque(false);

        pnlSearch.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(20);
        pnlSearch.add(txtTimKiem);

        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(pnlSearch, gbc);


        /* ===== TABLE ===== */
        model = new DefaultTableModel(
                new String[]{"Mã CV", "Tên chức vụ", "Hệ số lương"}, 0
        );
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        TableRowSorter<DefaultTableModel> sorter
                = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        sorter.setSortKeys(
                Arrays.asList(
                        new RowSorter.SortKey(2, SortOrder.DESCENDING)
                )
        );

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

        /* ===== LOAD ===== */
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

        /* ===== EVENTS ===== */
        btnAdd.addActionListener(e -> insert());
        btnUpdate.addActionListener(e -> update());
        btnDelete.addActionListener(e -> delete());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> fillForm());
    }

    /* ===== HÀM PHỤ ===== */
    private void addRow(JPanel p, GridBagConstraints f, int y,
            String lb1, Component c1,
            String lb2, Component c2) {

        f.gridy = y;

        // LABEL 1
        f.gridx = 0;
        f.weightx = 0;
        p.add(new JLabel(lb1), f);

        // FIELD 1
        f.gridx = 1;
        f.weightx = 0.5;
        p.add(c1, f);

        if (lb2 != null) {
            // LABEL 2
            f.gridx = 2;
            f.weightx = 0;
            p.add(new JLabel(lb2), f);

            // FIELD 2
            f.gridx = 3;
            f.weightx = 0.5;
            p.add(c2, f);
        }
    }

    private void timKiem() {
        String key = txtTimKiem.getText().toLowerCase();
        model.setRowCount(0);

        for (Chucvu_m cv : dao.getAll()) {
            if (cv.getMacv().toLowerCase().contains(key)
                    || cv.getTencv().toLowerCase().contains(key)) {

                model.addRow(new Object[]{
                    cv.getMacv(),
                    cv.getTencv(),
                    cv.getHesoluong()
                });
            }
        }
    }

    private void loadTable() {
        model.setRowCount(0);
        for (Chucvu_m cv : dao.getAll()) {
            model.addRow(new Object[]{
                cv.getMacv(), cv.getTencv(), cv.getHesoluong()
            });
        }
    }

    private void clearForm() {
        txtMaCV.setText(dao.generateMaCV());
        txtTenCV.setText("");
        txtHeSoLuong.setText("");
        table.clearSelection();
    }

    private void fillForm() {
        int r = table.getSelectedRow();
        if (r < 0) {
            return;
        }

        txtMaCV.setText(model.getValueAt(r, 0).toString());
        txtTenCV.setText(model.getValueAt(r, 1).toString());
        txtHeSoLuong.setText(model.getValueAt(r, 2).toString());
    }

    private boolean validateForm() {
        if (txtTenCV.getText().isEmpty() || txtHeSoLuong.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không được để trống!");
            return false;
        }
        try {
            Double.valueOf(txtHeSoLuong.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Hệ số lương phải là số!");
            return false;
        }
        return true;
    }

    private void insert() {
        if (!validateForm()) {
            return;
        }

        Chucvu_m cv = new Chucvu_m(
                txtMaCV.getText(),
                txtTenCV.getText(),
                Double.parseDouble(txtHeSoLuong.getText())
        );

        if (dao.insert(cv)) {
            loadTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
        }
    }

    private void update() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ cần cập nhật!");
            return;
        }

        if (!validateForm()) {
            return;
        }

        Chucvu_m cv = new Chucvu_m(
                txtMaCV.getText(),
                txtTenCV.getText(),
                Double.parseDouble(txtHeSoLuong.getText())
        );

        if (dao.update(cv)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void delete() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa chức vụ này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(txtMaCV.getText())) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa chức vụ!");
            }
        }
    }

}

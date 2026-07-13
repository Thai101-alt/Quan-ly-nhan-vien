package Admin;

import DAO.ChucvuDAO;
import DAO.NhanvienDAO;
import DAO.PhongbanDAO;
import Model.Chucvu_m;
import Model.Nhanvien_m;
import Model.Phongban_m;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.TableRowSorter;

public class Nhanvien extends JPanel {

    JTextField txtMa, txtTen, txtNgaySinh, txtSDT,
            txtEmail, txtDiaChi, txtNgayLam, txtLuong;

    JComboBox<Phongban_m> cbPhongBan;
    JComboBox<Chucvu_m> cbChucVu;
    JComboBox<String> cbGioiTinh;

    JTable table;
    DefaultTableModel model;

    NhanvienDAO nhanvienDAO = new NhanvienDAO();
    PhongbanDAO phongbanDAO = new PhongbanDAO();
    ChucvuDAO chucvuDAO = new ChucvuDAO();

    List<Phongban_m> listPhongBan;
    List<Chucvu_m> listChucVu;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    JTextField txtTimKiem;

    public Nhanvien() {

        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(255, 204, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        /* ===== TITLE ===== */
        JLabel title = new JLabel("QUẢN LÝ NHÂN VIÊN", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(title, gbc);

        /* ===== FORM ===== */
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Thông tin nhân viên",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));

        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(5, 5, 5, 5);
        f.fill = GridBagConstraints.HORIZONTAL;

        txtMa = new JTextField();
        txtMa.setEditable(false);
        txtTen = new JTextField();
        txtNgaySinh = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();
        txtDiaChi = new JTextField();
        txtNgayLam = new JTextField();
        txtLuong = new JTextField();

        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cbPhongBan = new JComboBox<>();
        cbChucVu = new JComboBox<>();

        loadComboPhongBan();
        loadComboChucVu();

        int r = 0;

        addRow(form, f, r++, "Mã NV", txtMa, "Họ tên", txtTen);
        addRow(form, f, r++, "Ngày sinh", txtNgaySinh, "Giới tính", cbGioiTinh);
        addRow(form, f, r++, "SĐT", txtSDT, "Email", txtEmail);
        addRow(form, f, r++, "Địa chỉ", txtDiaChi, "Ngày vào làm", txtNgayLam);
        addRow(form, f, r++, "Lương CB", txtLuong, "Phòng ban", cbPhongBan);
        addRow(form, f, r++, "Chức vụ", cbChucVu, null, null);

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
        model = new DefaultTableModel(new String[]{
            "Mã NV", "Họ tên", "Ngày sinh", "Giới tính", "SĐT",
            "Email", "Địa chỉ", "Ngày vào làm", "Lương", "Phòng ban", "Chức vụ"
        }, 0);

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        TableRowSorter<DefaultTableModel> sorter
                = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

// Sắp xếp mặc định: Ngày vào làm DESC (mới nhất lên trên)
        sorter.setSortKeys(
                Arrays.asList(
                        new RowSorter.SortKey(7, SortOrder.DESCENDING)
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
        txtTimKiem.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                timKiem();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                timKiem();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                timKiem();
            }
        });

        /* ===== EVENTS ===== */
        btnAdd.addActionListener(e -> insertNV());
        btnUpdate.addActionListener(e -> updateNV());
        btnDelete.addActionListener(e -> deleteNV());
        btnClear.addActionListener(e -> clearForm());

        table.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());
    }

    /* ================= HÀM PHỤ ================= */
    private void addRow(JPanel p, GridBagConstraints f, int y,
            String lb1, Component c1,
            String lb2, Component c2) {

        f.gridy = y;

        f.gridx = 0;
        p.add(new JLabel(lb1), f);
        f.gridx = 1;
        p.add(c1, f);

        if (lb2 != null) {
            f.gridx = 2;
            p.add(new JLabel(lb2), f);
            f.gridx = 3;
            p.add(c2, f);
        }
    }

    private void loadTable() {
        model.setRowCount(0);
        listPhongBan = phongbanDAO.getAll();
        listChucVu = chucvuDAO.getAll();

        for (Nhanvien_m nv : nhanvienDAO.getAll()) {
            model.addRow(new Object[]{
                nv.getManv(), nv.getHoten(), nv.getNgaysinh(),
                nv.getGioitinh(), nv.getSdt(), nv.getEmail(),
                nv.getDiachi(), nv.getNgayvaolam(), nv.getLuongcoban(),
                nv.getMapb(), nv.getMacv()
            });
        }
    }

    private void loadComboPhongBan() {
        cbPhongBan.removeAllItems();
        for (Phongban_m pb : phongbanDAO.getAll()) {
            cbPhongBan.addItem(pb);
        }
    }

    private void timKiem() {
        String key = txtTimKiem.getText().toLowerCase();
        model.setRowCount(0);

        for (Nhanvien_m nv : nhanvienDAO.getAll()) {
            if (nv.getManv().toLowerCase().contains(key)
                    || nv.getHoten().toLowerCase().contains(key)
                    || nv.getSdt().toLowerCase().contains(key)) {
                model.addRow(new Object[]{
                    nv.getManv(), nv.getHoten(), nv.getNgaysinh(),
                    nv.getGioitinh(), nv.getSdt(), nv.getEmail(),
                    nv.getDiachi(), nv.getNgayvaolam(), nv.getLuongcoban(),
                    nv.getMapb(), nv.getMacv()
                });
            }
        }
    }

    private void loadComboChucVu() {
        cbChucVu.removeAllItems();
        for (Chucvu_m cv : chucvuDAO.getAll()) {
            cbChucVu.addItem(cv);
        }
    }

    private boolean validateForm() {
        if (txtTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống!");
            return false;
        }
        return true;
    }

    private void clearForm() {
        txtMa.setText(nhanvienDAO.generateMaNV());
        txtTen.setText("");
        txtNgaySinh.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtNgayLam.setText("");
        txtLuong.setText("");
        cbGioiTinh.setSelectedIndex(0);
        table.clearSelection();
        txtTimKiem.setText("");
        loadTable();

    }

    private void fillFormFromTable() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return;
        }

        int r = table.convertRowIndexToModel(viewRow);

        txtMa.setText(model.getValueAt(r, 0).toString());
        txtTen.setText(model.getValueAt(r, 1).toString());
        txtNgaySinh.setText(dateFormat.format((Date) model.getValueAt(r, 2)));
        cbGioiTinh.setSelectedItem(model.getValueAt(r, 3));
        txtSDT.setText(model.getValueAt(r, 4).toString());
        txtEmail.setText(model.getValueAt(r, 5).toString());
        txtDiaChi.setText(model.getValueAt(r, 6).toString());
        txtNgayLam.setText(dateFormat.format((Date) model.getValueAt(r, 7)));
        txtLuong.setText(model.getValueAt(r, 8).toString());
    }

    private void insertNV() {
        if (!validateForm()) {
            return;
        }
        try {
            Nhanvien_m nv = new Nhanvien_m();
            nv.setManv(nhanvienDAO.generateMaNV());
            nv.setHoten(txtTen.getText());
            nv.setNgaysinh(new Date(dateFormat.parse(txtNgaySinh.getText()).getTime()));
            nv.setGioitinh(cbGioiTinh.getSelectedItem().toString());
            nv.setSdt(txtSDT.getText());
            nv.setEmail(txtEmail.getText());
            nv.setDiachi(txtDiaChi.getText());
            nv.setNgayvaolam(new Date(dateFormat.parse(txtNgayLam.getText()).getTime()));
            nv.setLuongcoban(new BigDecimal(txtLuong.getText()));

            nv.setMapb(((Phongban_m) cbPhongBan.getSelectedItem()).getMapb());
            nv.setMacv(((Chucvu_m) cbChucVu.getSelectedItem()).getMacv());

            if (nhanvienDAO.insert(nv)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadTable();
                clearForm();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
        }
    }

    private void updateNV() {
        if (!validateForm() || table.getSelectedRow() < 0) {
            return;
        }

        try {
            Nhanvien_m nv = new Nhanvien_m();
            nv.setManv(txtMa.getText());
            nv.setHoten(txtTen.getText());
            nv.setNgaysinh(new Date(dateFormat.parse(txtNgaySinh.getText()).getTime()));
            nv.setGioitinh(cbGioiTinh.getSelectedItem().toString());
            nv.setSdt(txtSDT.getText());
            nv.setEmail(txtEmail.getText());
            nv.setDiachi(txtDiaChi.getText());
            nv.setNgayvaolam(new Date(dateFormat.parse(txtNgayLam.getText()).getTime()));
            nv.setLuongcoban(new BigDecimal(txtLuong.getText()));

            nv.setMapb(((Phongban_m) cbPhongBan.getSelectedItem()).getMapb());
            nv.setMacv(((Chucvu_m) cbChucVu.getSelectedItem()).getMacv());

            if (nhanvienDAO.update(nv)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadTable();
                clearForm();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
        }
    }

    private void deleteNV() {
        int viewRow = table.getSelectedRow();

        // Chưa chọn nhân viên
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng chọn nhân viên cần xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int r = table.convertRowIndexToModel(viewRow);
        String maNV = model.getValueAt(r, 0).toString();

        // Xác nhận xóa
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa nhân viên [" + maNV + "] không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Thực hiện xóa
        if (nhanvienDAO.delete(maNV)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Xóa nhân viên thành công!",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );
            loadTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể xóa nhân viên!\nNhân viên có thể đang được sử dụng.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void reloadPhongban_Chucvu() {
        loadComboPhongBan();
        loadComboChucVu();
    }

}

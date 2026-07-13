package Admin;

import DAO.LuongDAO;
import DAO.NhanvienDAO;
import DAO.ChamcongDAO;
import Model.Luong_m;
import Model.Nhanvien_m;
import Model.ChamCong_m;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.TableRowSorter;

public class Luong extends JPanel {

    // ===== FORM LƯƠNG =====
    JTextField txtMaLuong, txtLuongCB, txtHeSoLuong, txtPhuCap, txtThuong, txtKhauTru, txtTongLuong;
    JComboBox<Nhanvien_m> cbNhanVien;
    JComboBox<String> cbThang, cbNam;
    JTextField txtTimKiem;

    // ===== BẢNG LƯƠNG & CHẤM CÔNG =====
    JTable tableLuong;
    DefaultTableModel modelLuong;

    JTable tableChamCong;
    DefaultTableModel modelCC;

    // ===== TỔNG TIỀN THEO THÁNG =====
    JComboBox<String> cbTimThang, cbTimNam;
    JButton btnTongTien;
    JTextField txtTongTien;

    // ===== DAO =====
    LuongDAO luongDAO = new LuongDAO();
    NhanvienDAO nvDAO = new NhanvienDAO();
    ChamcongDAO ccDAO = new ChamcongDAO();
    ArrayList<Nhanvien_m> dsNhanVien;
    boolean isLoadingNV = false;

    // ===== CHẤM CÔNG =====
    JButton[] dayButtons = new JButton[31];
    JComboBox<String> cbStatus;
    JSpinner spHours;
    String selectedStatus = "Nghỉ";
    int selectedHour = 1;
    HashMap<String, String[]> chamCongData = new HashMap<>();

    public Luong() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 240, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        /* ===== TITLE ===== */
        JLabel title = new JLabel("QUẢN LÝ LƯƠNG", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        add(title, gbc);

        /* ===== FORM THÔNG TIN LƯƠNG ===== */
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Thông tin lương",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
        ));
        GridBagConstraints f = new GridBagConstraints();
        f.insets = new Insets(5, 5, 5, 5);
        f.fill = GridBagConstraints.HORIZONTAL;

        txtMaLuong = new JTextField();
        txtMaLuong.setEditable(false);
        txtLuongCB = new JTextField();
        txtLuongCB.setEditable(false);
        txtHeSoLuong = new JTextField();
        txtHeSoLuong.setEditable(false);
        txtPhuCap = new JTextField();
        txtThuong = new JTextField();
        txtKhauTru = new JTextField();
        txtTongLuong = new JTextField();
        txtTongLuong.setEditable(false);

        cbNhanVien = new JComboBox<>();
        cbThang = new JComboBox<>();
        cbNam = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cbThang.addItem(String.valueOf(i));
        }
        for (int i = 2023; i <= 2030; i++) {
            cbNam.addItem(String.valueOf(i));
        }

        loadNhanVien();

        int r = 0;
        addRow(form, f, r++, "Mã lương", txtMaLuong, "Nhân viên", cbNhanVien);
        addRow(form, f, r++, "Tháng", cbThang, "Năm", cbNam);
        addRow(form, f, r++, "Lương CB", txtLuongCB, "Hệ số lương", txtHeSoLuong);
        addRow(form, f, r++, "Phụ cấp", txtPhuCap, "Thưởng", txtThuong);
        addRow(form, f, r++, "Khấu trừ", txtKhauTru, "Tổng lương", txtTongLuong);

        gbc.gridy = 1;
        gbc.gridwidth = 4;
        add(form, gbc);

        /* ===== THANH TÌM KIẾM ===== */
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setOpaque(false);
        pnlSearch.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(20);
        pnlSearch.add(txtTimKiem);
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        add(pnlSearch, gbc);

        /* ===== THANH TÌM TỔNG TIỀN THEO THÁNG ===== */
        JPanel pnlTimThang = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlTimThang.setOpaque(false);

        pnlTimThang.add(new JLabel("Tháng:"));
        cbTimThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cbTimThang.addItem(String.valueOf(i));
        }
        pnlTimThang.add(cbTimThang);

        pnlTimThang.add(new JLabel("Năm:"));
        cbTimNam = new JComboBox<>();
        for (int i = 2023; i <= 2030; i++) {
            cbTimNam.addItem(String.valueOf(i));
        }
        pnlTimThang.add(cbTimNam);

        btnTongTien = new JButton("Tính tổng tiền");
        pnlTimThang.add(btnTongTien);

        txtTongTien = new JTextField(15);
        txtTongTien.setEditable(false);
        pnlTimThang.add(new JLabel("Tổng tiền:"));
        pnlTimThang.add(txtTongTien);

        gbc.gridy = 3;
        gbc.gridwidth = 4;
        add(pnlTimThang, gbc);

        /* ===== TAB LƯƠNG + CHẤM CÔNG ===== */
        JTabbedPane tabbedPane = new JTabbedPane();

        // Bảng lương
        JPanel pnlLuong = new JPanel(new BorderLayout());
        modelLuong = new DefaultTableModel(
                new String[]{"Mã lương", "Mã NV", "Tháng", "Năm", "Tổng lương"}, 0
        );
        tableLuong = new JTable(modelLuong);
        tableLuong.setRowHeight(28);
        pnlLuong.add(new JScrollPane(tableLuong), BorderLayout.CENTER);
        tabbedPane.addTab("Bảng lương", pnlLuong);

        TableRowSorter<DefaultTableModel> sorter
                = new TableRowSorter<>(modelLuong);
        tableLuong.setRowSorter(sorter);

        sorter.setSortKeys(
                java.util.Arrays.asList(
                        new RowSorter.SortKey(3, SortOrder.DESCENDING), // Năm
                        new RowSorter.SortKey(2, SortOrder.DESCENDING) // Tháng
                ));

        // Bảng chấm công
        JPanel pnlChamCong = new JPanel(new BorderLayout());
        JPanel pnlCalendar = new JPanel(new GridLayout(6, 7, 5, 5));
        pnlCalendar.setBorder(new TitledBorder("Chấm công tháng"));

        cbStatus = new JComboBox<>(new String[]{"Nghỉ", "Đi trễ", "Tăng ca", "Xóa"});
        cbStatus.addActionListener(ev -> selectedStatus = cbStatus.getSelectedItem().toString());
        spHours = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));

        JPanel pnlControls = new JPanel();
        pnlControls.add(new JLabel("Trạng thái:"));
        pnlControls.add(cbStatus);
        pnlControls.add(new JLabel("Giờ tăng ca:"));
        pnlControls.add(spHours);

        for (int i = 0; i < 31; i++) {
            JButton btn = new JButton(String.valueOf(i + 1));
            int day = i;
            btn.addActionListener(ev2 -> {
                selectedHour = (Integer) spHours.getValue();
                Nhanvien_m nv = (Nhanvien_m) cbNhanVien.getSelectedItem();
                if (nv == null) {
                    return;
                }

                String[] days = chamCongData.get(nv.getManv());
                if (days == null) {
                    return;
                }

                if (selectedStatus.equals("Xóa")) {
                    days[day] = "Công";
                    btn.setBackground(Color.WHITE);
                    btn.setText(String.valueOf(day + 1));
                } else {
                    days[day] = selectedStatus;
                    switch (selectedStatus) {
                        case "Nghỉ":
                            btn.setBackground(Color.PINK);
                            btn.setText((day + 1) + " Nghỉ");
                            break;
                        case "Đi trễ":
                            btn.setBackground(Color.YELLOW);
                            btn.setText((day + 1) + " TRỄ");
                            break;
                        case "Tăng ca":
                            btn.setBackground(Color.GREEN);
                            btn.setText((day + 1) + " TĂNG CA " + selectedHour + "h");
                            break;
                    }
                }
                saveChamCong(nv);
                loadChamCong();
            });

            dayButtons[i] = btn;
            pnlCalendar.add(btn);
        }

        modelCC = new DefaultTableModel(new String[]{"Mã NV", "Họ tên", "Ngày công", "Ngày nghỉ", "Tăng ca"}, 0);
        tableChamCong = new JTable(modelCC);

        pnlChamCong.add(pnlControls, BorderLayout.NORTH);
        pnlChamCong.add(pnlCalendar, BorderLayout.CENTER);
        pnlChamCong.add(new JScrollPane(tableChamCong), BorderLayout.SOUTH);

        tabbedPane.addTab("Chấm công", pnlChamCong);

        gbc.gridy = 4;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(tabbedPane, gbc);

        /* ===== BUTTON ===== */
        JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlBtn.setOpaque(false);
        JButton btnTinh = new JButton("Tính lương");
        JButton btnAdd = new JButton("Thêm");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới");

        Dimension btnSize = new Dimension(120, 30);
        btnTinh.setPreferredSize(btnSize);
        btnAdd.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize);
        btnClear.setPreferredSize(btnSize);

        pnlBtn.add(btnTinh);
        pnlBtn.add(btnAdd);
        pnlBtn.add(btnDelete);
        pnlBtn.add(btnClear);

        gbc.gridy = 5;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(pnlBtn, gbc);

        loadTableLuong();
        loadChamCong();
        clearForm();

        /* ===== EVENTS ===== */
        txtTimKiem.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent evDoc) {
                timKiem();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent evDoc) {
                timKiem();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent evDoc) {
                timKiem();
            }
        });

        cbNhanVien.addActionListener(e -> {
            if (isLoadingNV) {
                return;
            }
            Nhanvien_m nv = (Nhanvien_m) cbNhanVien.getSelectedItem();
            if (nv != null) {
                txtLuongCB.setText(nv.getLuongcoban().toString());
                txtHeSoLuong.setText(nv.getHeSoLuong().toString());
                txtPhuCap.setText("");
                txtThuong.setText("");
                txtKhauTru.setText("");
                txtTongLuong.setText("");
                loadButtonStatus(nv);
            }
        });

        btnTinh.addActionListener(e -> tinhLuong());
        btnAdd.addActionListener(e -> themLuong());
        btnDelete.addActionListener(e -> xoaLuong());
        btnClear.addActionListener(e -> clearForm());

        btnTongTien.addActionListener(e -> {
            try {
                int thang = Integer.parseInt(cbTimThang.getSelectedItem().toString());
                int nam = Integer.parseInt(cbTimNam.getSelectedItem().toString());
                BigDecimal tong = luongDAO.tatcaLuongThang(thang, nam);
                txtTongTien.setText(tong.toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            }
        });
    }

    // ===== HÀM HỖ TRỢ =====
    private void addRow(JPanel p, GridBagConstraints f, int y, String lb1, Component c1, String lb2, Component c2) {
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

    private void loadNhanVien() {
        isLoadingNV = true;
        dsNhanVien = nvDAO.getnvvl();
        cbNhanVien.removeAllItems();
        for (Nhanvien_m nv : dsNhanVien) {
            cbNhanVien.addItem(nv);
            String[] days = new String[31];
            for (int i = 0; i < 31; i++) {
                days[i] = "Công";
            }
            chamCongData.put(nv.getManv(), days);

            int thang = Integer.parseInt(cbThang.getSelectedItem().toString());
            int nam = Integer.parseInt(cbNam.getSelectedItem().toString());
            ChamCong_m cc = ccDAO.getChamCong(nv.getManv(), thang, nam);
            if (cc != null) {
                for (int i = 0; i < cc.getNgayNghi(); i++) {
                    days[i] = "Nghỉ";
                }
                for (int i = 0; i < cc.getTangCa(); i++) {
                    days[i] = "Tăng ca";
                }
            }
        }
        isLoadingNV = false;
    }

    private void loadTableLuong() {
        modelLuong.setRowCount(0);
        for (Luong_m l : luongDAO.getAll()) {
            modelLuong.addRow(new Object[]{l.getMaluong(), l.getManv(), l.getThang(), l.getNam(), l.getTongluong()});
        }
    }

    private void loadChamCong() {
        modelCC.setRowCount(0);
        int thang = Integer.parseInt(cbThang.getSelectedItem().toString());
        int nam = Integer.parseInt(cbNam.getSelectedItem().toString());
        for (Nhanvien_m nv : dsNhanVien) {
            ChamCong_m cc = ccDAO.getChamCong(nv.getManv(), thang, nam);
            int ngayCong = 0, ngayNghi = 0, tangCa = 0;
            if (cc != null) {
                ngayCong = cc.getNgayCong();
                ngayNghi = cc.getNgayNghi();
                tangCa = cc.getTangCa();
            }
            modelCC.addRow(new Object[]{nv.getManv(), nv.getHoten(), ngayCong, ngayNghi, tangCa});
        }
    }

    private void loadButtonStatus(Nhanvien_m nv) {
        String[] days = chamCongData.get(nv.getManv());
        for (int i = 0; i < 31; i++) {
            String status = days[i];
            JButton btn = dayButtons[i];
            if (status.equals("Công")) {
                btn.setBackground(Color.WHITE);
                btn.setText(String.valueOf(i + 1));
            } else if (status.equals("Nghỉ")) {
                btn.setBackground(Color.PINK);
                btn.setText((i + 1) + " Nghỉ");
            } else if (status.equals("Đi trễ")) {
                btn.setBackground(Color.YELLOW);
                btn.setText((i + 1) + " TRỄ");
            } else if (status.equals("Tăng ca")) {
                btn.setBackground(Color.GREEN);
                btn.setText((i + 1) + " TĂNG CA " + selectedHour + "h");
            }
        }
    }

    private void saveChamCong(Nhanvien_m nv) {
        String[] days = chamCongData.get(nv.getManv());
        int ngayCong = 0, ngayNghi = 0, tangCa = 0;
        for (String status : days) {
            switch (status) {
                case "Công":
                case "Đi trễ":
                    ngayCong++;
                    break;
                case "Nghỉ":
                    ngayNghi++;
                    break;
                case "Tăng ca":
                    tangCa++;
                    break;
            }
        }
        int thang = Integer.parseInt(cbThang.getSelectedItem().toString());
        int nam = Integer.parseInt(cbNam.getSelectedItem().toString());
        ChamCong_m cc = new ChamCong_m(nv.getManv(), thang, nam, ngayCong, ngayNghi, tangCa);
        ccDAO.insertOrUpdate(cc);
    }

    private void timKiem() {
        String key = txtTimKiem.getText().trim().toLowerCase();
        modelLuong.setRowCount(0);
        for (Luong_m l : luongDAO.getAll()) {
            if (l.getMaluong().toLowerCase().contains(key)
                    || l.getManv().toLowerCase().contains(key)
                    || String.valueOf(l.getThang()).contains(key)
                    || String.valueOf(l.getNam()).contains(key)) {
                modelLuong.addRow(new Object[]{l.getMaluong(), l.getManv(), l.getThang(), l.getNam(), l.getTongluong()});
            }
        }
    }

    private void tinhLuong() {
        try {
            int i = cbNhanVien.getSelectedIndex();
            if (i < 0) {
                return;
            }
            Nhanvien_m nv = dsNhanVien.get(i);
            int thang = Integer.parseInt(cbThang.getSelectedItem().toString());
            int nam = Integer.parseInt(cbNam.getSelectedItem().toString());

            ChamCong_m cc = ccDAO.getChamCong(nv.getManv(), thang, nam);
            int ngayCong = cc != null ? cc.getNgayCong() : 0;
            int tangCa = cc != null ? cc.getTangCa() : 0;

            BigDecimal luongCB = nv.getLuongcoban();
            BigDecimal hesoluong = nv.getHeSoLuong();
            BigDecimal phuCap = txtPhuCap.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtPhuCap.getText());
            BigDecimal thuong = txtThuong.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtThuong.getText());
            BigDecimal khauTru = txtKhauTru.getText().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtKhauTru.getText());

            BigDecimal tong = luongCB.multiply(hesoluong).multiply(BigDecimal.valueOf(ngayCong))
                    .divide(BigDecimal.valueOf(26), 0, RoundingMode.HALF_UP)
                    .add(BigDecimal.valueOf(tangCa * 50000))
                    .add(phuCap).add(thuong).subtract(khauTru);

            txtTongLuong.setText(tong.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
        }
    }

    private void xoaLuong() {
        int row = tableLuong.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lương cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa lương này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String maLuong = modelLuong.getValueAt(row, 0).toString();

        if (luongDAO.delete(maLuong)) {
            JOptionPane.showMessageDialog(this, "Xóa lương thành công!");
            loadTableLuong();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại!");
        }
    }

    private void themLuong() {
        int i = cbNhanVien.getSelectedIndex();
        if (i < 0 || txtTongLuong.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa tính lương!");
            return;
        }
        Nhanvien_m nv = dsNhanVien.get(i);
        int thang = Integer.parseInt(cbThang.getSelectedItem().toString());
        int nam = Integer.parseInt(cbNam.getSelectedItem().toString());

        if (luongDAO.exists(nv.getManv(), thang, nam)) {
            JOptionPane.showMessageDialog(this, "Nhân viên đã có lương tháng này!");
            return;
        }

        Luong_m l = new Luong_m(txtMaLuong.getText(), nv.getManv(), thang, nam, new BigDecimal(txtTongLuong.getText()));
        if (luongDAO.insert(l)) {
            JOptionPane.showMessageDialog(this, "Thêm lương thành công!");
            loadTableLuong();
            clearForm();
        }
    }

    private void clearForm() {
        txtMaLuong.setText(luongDAO.generateMaLuong());
        txtLuongCB.setText("");
        txtHeSoLuong.setText("");
        txtPhuCap.setText("");
        txtThuong.setText("");
        txtKhauTru.setText("");
        txtTongLuong.setText("");
        txtTimKiem.setText("");
        tableLuong.clearSelection();
    }

    public void reloadLuong() {
        loadNhanVien();
    }

}

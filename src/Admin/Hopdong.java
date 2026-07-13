package Admin;

import DAO.HopdongDAO;
import DAO.NhanvienDAO;
import Model.Hopdong_m;
import Model.Nhanvien_m;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.TableRowSorter;

public class Hopdong extends JPanel {

    private JTextField txtMaHD, txtNgayBD, txtNgayKT, txtLuong;
    private JComboBox<Nhanvien_m> cbNhanVien;
    private JComboBox<String> cbLoaiHD;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTimKiem;

    private HopdongDAO hdDAO = new HopdongDAO();
    private NhanvienDAO nvDAO = new NhanvienDAO();
    private ArrayList<Nhanvien_m> dsNhanVien = new ArrayList<>();
    private boolean isLoadingNV = false;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public Hopdong() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(255, 204, 255));

        // TITLE
        JLabel title = new JLabel("QUẢN LÝ HỢP ĐỒNG", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // FORM
        JPanel pnForm = new JPanel(new GridBagLayout());
        pnForm.setBorder(BorderFactory.createTitledBorder("Thông tin hợp đồng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaHD = new JTextField(15);
        txtMaHD.setEditable(false);
        txtMaHD.setText(hdDAO.generateMaHD());

        txtNgayBD = new JTextField(15);
        txtNgayBD.setEditable(false);

        txtNgayKT = new JTextField(15);
        txtLuong = new JTextField(15);
        txtLuong.setEditable(false);

        cbNhanVien = new JComboBox<>();
        cbLoaiHD = new JComboBox<>(new String[]{"Thử việc", "Nhân viên"});

        // Add to form
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnForm.add(new JLabel("Mã HĐ:"), gbc);
        gbc.gridx = 1;
        pnForm.add(txtMaHD, gbc);

        gbc.gridx = 2;
        pnForm.add(new JLabel("Nhân viên:"), gbc);
        gbc.gridx = 3;
        pnForm.add(cbNhanVien, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnForm.add(new JLabel("Loại HĐ:"), gbc);
        gbc.gridx = 1;
        pnForm.add(cbLoaiHD, gbc);

        gbc.gridx = 2;
        pnForm.add(new JLabel("Ngày bắt đầu:"), gbc);
        gbc.gridx = 3;
        pnForm.add(txtNgayBD, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        pnForm.add(new JLabel("Ngày kết thúc:"), gbc);
        gbc.gridx = 1;
        pnForm.add(txtNgayKT, gbc);

        gbc.gridx = 2;
        pnForm.add(new JLabel("Lương cơ bản:"), gbc);
        gbc.gridx = 3;
        pnForm.add(txtLuong, gbc);

        add(pnForm, BorderLayout.CENTER);

        // SEARCH + TABLE + BUTTON
        JPanel pnSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnSearch.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(20);
        pnSearch.add(txtTimKiem);

        model = new DefaultTableModel(new String[]{"Mã HĐ", "Mã NV", "Loại HĐ", "Ngày BD", "Ngày KT", "Lương"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        TableRowSorter<DefaultTableModel> sorter
                = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        sorter.setSortKeys(
                Arrays.asList(
                        new RowSorter.SortKey(3, SortOrder.DESCENDING)
                )
        );

        JPanel pnBtn = new JPanel();
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Cập nhật");
        JButton btnDelete = new JButton("Xóa");
        JButton btnClear = new JButton("Làm mới");
        pnBtn.add(btnAdd);
        pnBtn.add(btnUpdate);
        pnBtn.add(btnDelete);
        pnBtn.add(btnClear);

        JPanel pnBottom = new JPanel(new BorderLayout());
        pnBottom.add(pnSearch, BorderLayout.NORTH);
        pnBottom.add(scroll, BorderLayout.CENTER);
        pnBottom.add(pnBtn, BorderLayout.SOUTH);

        add(pnBottom, BorderLayout.SOUTH);

        // LOAD DATA
        loadNhanVien();
        loadTable();

        // EVENTS
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

        cbNhanVien.addActionListener(e -> {
            if (!isLoadingNV) {
                Nhanvien_m nv = (Nhanvien_m) cbNhanVien.getSelectedItem();
                if (nv != null) {
                    updateNVInfo(nv);
                }
            }
        });

        btnAdd.addActionListener(e -> {
            Hopdong_m hd = getForm();
            if (hd != null && hdDAO.insert(hd)) {
                JOptionPane.showMessageDialog(this, "Thêm hợp đồng thành công!");
                loadTable();
                loadNhanVien();
                clearForm();
            }
        });
        ;
        btnUpdate.addActionListener(e -> {
            Hopdong_m hd = getForm();
            if (hd != null && hdDAO.update(hd)) {
                loadTable();
            }
        });
        btnDelete.addActionListener(e -> {
            int viewRow = table.getSelectedRow();
            if (viewRow < 0) {
                return;
            }

            int r = table.convertRowIndexToModel(viewRow);

            if (r >= 0) {
                hdDAO.delete(model.getValueAt(r, 0).toString());
                loadTable();
                clearForm();
            }
        });
        btnClear.addActionListener(e -> clearForm());
        table.getSelectionModel().addListSelectionListener(e -> fillForm());
        
    }

    private void loadNhanVien() {
        isLoadingNV = true;

        dsNhanVien = nvDAO.getNhanVienChuaCoHopDong();
        cbNhanVien.removeAllItems();

        for (Nhanvien_m nv : dsNhanVien) {
            cbNhanVien.addItem(nv);
        }

        isLoadingNV = false;

        if (!dsNhanVien.isEmpty()) {
            cbNhanVien.setSelectedIndex(0);
        }
    }

    private void updateNVInfo(Nhanvien_m nv) {
        txtLuong.setText(nv.getLuongcoban() != null ? nv.getLuongcoban().toString() : "");
        txtNgayBD.setText(nv.getNgayvaolam() != null ? sdf.format(nv.getNgayvaolam()) : "");
    }

    private void loadTable() {
        model.setRowCount(0);
        for (Hopdong_m hd : hdDAO.getAll()) {
            model.addRow(new Object[]{hd.getMahd(), hd.getManv(), hd.getLoaihd(),
                hd.getNgaybatdau(), hd.getNgayketthuc(), hd.getLuongcoban()});
        }
    }

    private void timKiem() {
        String key = txtTimKiem.getText().trim().toLowerCase();
        model.setRowCount(0);
        for (Hopdong_m hd : hdDAO.getAll()) {
            if (hd.getMahd().toLowerCase().contains(key) || hd.getManv().toLowerCase().contains(key)
                    || hd.getLoaihd().toLowerCase().contains(key)) {
                model.addRow(new Object[]{hd.getMahd(), hd.getManv(), hd.getLoaihd(),
                    hd.getNgaybatdau(), hd.getNgayketthuc(), hd.getLuongcoban()});
            }
        }
    }

    private Hopdong_m getForm() {
        try {
            Hopdong_m hd = new Hopdong_m();
            Nhanvien_m nv = (Nhanvien_m) cbNhanVien.getSelectedItem();
            if (nv == null) {
                return null;
            }

            hd.setMahd(txtMaHD.getText());
            hd.setManv(nv.getManv());
            hd.setLoaihd(cbLoaiHD.getSelectedItem().toString());
            java.util.Date utilNgayBD = null;
            java.util.Date utilNgayKT = null;

            if (!txtNgayBD.getText().isEmpty()) {
                utilNgayBD = sdf.parse(txtNgayBD.getText());
                hd.setNgaybatdau(new Date(utilNgayBD.getTime()));
            }

            if (!txtNgayKT.getText().isEmpty()) {
                utilNgayKT = sdf.parse(txtNgayKT.getText());
                hd.setNgayketthuc(new Date(utilNgayKT.getTime()));
            }

            /* ⭐ VALIDATE: Ngày kết thúc phải sau ngày bắt đầu */
            if (utilNgayBD != null && utilNgayKT != null) {
                if (!utilNgayKT.after(utilNgayBD)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Ngày kết thúc phải sau ngày bắt đầu!",
                            "Lỗi dữ liệu",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return null;
                }
            }

            hd.setLuongcoban(new BigDecimal(txtLuong.getText()));
            return hd;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày phải có dạng dd-MM-yyyy");
            return null;
        }
    }

    private void fillForm() {
        int r = table.getSelectedRow();
        if (r >= 0) {
            txtMaHD.setText(model.getValueAt(r, 0).toString());
            txtNgayBD.setText(model.getValueAt(r, 3) != null ? sdf.format((Date) model.getValueAt(r, 3)) : "");
            txtNgayKT.setText(model.getValueAt(r, 4) != null ? sdf.format((Date) model.getValueAt(r, 4)) : "");
            txtLuong.setText(model.getValueAt(r, 5).toString());

            String manv = model.getValueAt(r, 1).toString();
            for (int i = 0; i < cbNhanVien.getItemCount(); i++) {
                if (cbNhanVien.getItemAt(i).getManv().equals(manv)) {
                    cbNhanVien.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public void reloadNhanvien() {
        loadNhanVien();
    }

    private void clearForm() {
        txtMaHD.setText(hdDAO.generateMaHD());
        txtNgayBD.setText("");
        txtNgayKT.setText("");
        txtLuong.setText("");
        table.clearSelection();
        if (!dsNhanVien.isEmpty()) {
            cbNhanVien.setSelectedIndex(0);
        }
    }
}

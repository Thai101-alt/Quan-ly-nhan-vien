package User;

import DAO.DonXinPhepDAO;
import Model.DonXinPhep_m;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class DonXinPhepUser extends JPanel {

    JTable tableDonPhep;
    DefaultTableModel modelDonPhep;
    DonXinPhepDAO donDAO = new DonXinPhepDAO();

    JButton btnThemDon;
    JTextField txtLyDo;
    JSpinner spNgayBD, spNgayKT;

    private String currentUser; 

    public DonXinPhepUser(String manv) {
        this.currentUser = manv;

        setLayout(new BorderLayout());

        // bảng hiển thị đơn
        modelDonPhep = new DefaultTableModel(new String[]{"Mã đơn", "Ngày gửi", "Ngày bắt đầu", "Ngày kết thúc", "Lý do", "Trạng thái"}, 0);
        tableDonPhep = new JTable(modelDonPhep);
        add(new JScrollPane(tableDonPhep), BorderLayout.CENTER);

        // form thêm đơn mới
        JPanel pnlForm = new JPanel(new FlowLayout());
        txtLyDo = new JTextField(15);
        spNgayBD = new JSpinner(new SpinnerDateModel());
        spNgayKT = new JSpinner(new SpinnerDateModel());
        btnThemDon = new JButton("Gửi đơn");

        pnlForm.add(new JLabel("Ngày bắt đầu:"));
        pnlForm.add(spNgayBD);
        pnlForm.add(new JLabel("Ngày kết thúc:"));
        pnlForm.add(spNgayKT);
        pnlForm.add(new JLabel("Lý do:"));
        pnlForm.add(txtLyDo);
        pnlForm.add(btnThemDon);

        add(pnlForm, BorderLayout.SOUTH);

        btnThemDon.addActionListener(e -> themDon());

        loadDonPhepByUser();
    }

    public void loadDonPhepByUser() {
        modelDonPhep.setRowCount(0);
        List<DonXinPhep_m> list = donDAO.getByUser(currentUser);
        for (DonXinPhep_m d : list) {
            modelDonPhep.addRow(new Object[]{
                d.getMadon(),
                d.getNgaygui(),
                d.getNgaybatdau(),
                d.getNgayketthuc(),
                d.getLydonghi(),
                d.getTrangthai()
            });
        }
    }

    private void themDon() {
        LocalDate bd = new java.sql.Date(((java.util.Date) spNgayBD.getValue()).getTime()).toLocalDate();
        LocalDate kt = new java.sql.Date(((java.util.Date) spNgayKT.getValue()).getTime()).toLocalDate();

        if (kt.isBefore(bd)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu!");
            return;
        }

        String lyDo = txtLyDo.getText().trim();
        if (lyDo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lý do!");
            return;
        }

        DonXinPhep_m don = new DonXinPhep_m();
        don.setMadon(donDAO.generateMaDon());
        don.setManv(currentUser);
        don.setNgaygui(Date.valueOf(LocalDate.now()));
        don.setNgaybatdau(Date.valueOf(bd));
        don.setNgayketthuc(Date.valueOf(kt));
        don.setLydonghi(lyDo);
        don.setTrangthai("Chờ duyệt");

        if (donDAO.insert(don)) {
            JOptionPane.showMessageDialog(this, "Gửi đơn thành công!");
            loadDonPhepByUser();
            txtLyDo.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Gửi đơn thất bại!");
        }
    }
}

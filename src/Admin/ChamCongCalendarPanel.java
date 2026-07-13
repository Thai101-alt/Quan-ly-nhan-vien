package Admin;


import Model.Nhanvien_m;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.table.DefaultTableModel;

public class ChamCongCalendarPanel extends JPanel {
    JButton[] dayButtons = new JButton[31];
    String selectedStatus = "Nghỉ";
    String selectedHour = "1";

    JComboBox<Nhanvien_m> cbNhanVien; // nhận từ Luong để lấy NV hiện tại
    DefaultTableModel modelCC;

    public ChamCongCalendarPanel(DefaultTableModel modelCC, JComboBox<Nhanvien_m> cbNhanVien){
        this.modelCC = modelCC;
        this.cbNhanVien = cbNhanVien;
        setLayout(new BorderLayout());

        // Panel lịch
        JPanel pnlCalendar = new JPanel(new GridLayout(6,7,5,5));
        for(int i=0;i<31;i++){
            JButton btn = new JButton(String.valueOf(i+1));
            btn.setBackground(Color.WHITE);
            dayButtons[i] = btn;
            pnlCalendar.add(btn);
            btn.addActionListener(e -> updateDay(btn));
        }

        add(pnlCalendar, BorderLayout.CENTER);

        // Panel trạng thái
        JPanel pnlStatus = new JPanel();
        JButton btnNghi = new JButton("Nghỉ");
        JButton btnTre = new JButton("Đi Trễ");
        JButton btnTangCa = new JButton("Tăng Ca");
        JButton btnXoa = new JButton("Xóa");

        JRadioButton r1 = new JRadioButton("1 Giờ");
        JRadioButton r2 = new JRadioButton("2 Giờ");
        JRadioButton r3 = new JRadioButton("3 Giờ");
        JRadioButton r4 = new JRadioButton("4 Giờ");
        ButtonGroup groupHour = new ButtonGroup();
        groupHour.add(r1); groupHour.add(r2); groupHour.add(r3); groupHour.add(r4);
        r1.setSelected(true);

        pnlStatus.add(btnNghi); pnlStatus.add(btnTre); pnlStatus.add(btnTangCa); pnlStatus.add(btnXoa);
        pnlStatus.add(r1); pnlStatus.add(r2); pnlStatus.add(r3); pnlStatus.add(r4);
        add(pnlStatus, BorderLayout.SOUTH);

        // Sự kiện chọn trạng thái
        btnNghi.addActionListener(e -> selectedStatus="Nghỉ");
        btnTre.addActionListener(e -> selectedStatus="Đi Trễ");
        btnTangCa.addActionListener(e -> {
            selectedStatus="Tăng ca";
            if(r1.isSelected()) selectedHour="1";
            else if(r2.isSelected()) selectedHour="2";
            else if(r3.isSelected()) selectedHour="3";
            else selectedHour="4";
        });
        btnXoa.addActionListener(e -> selectedStatus="Xóa");

        // Nút Thêm cập nhật vào bảng chấm công
        JButton btnSave = new JButton("Thêm");
        btnSave.addActionListener(e -> saveToTable());
        add(btnSave, BorderLayout.NORTH);
    }

    private void updateDay(JButton btn){
        int day = Integer.parseInt(btn.getText().split(" ")[0]);
        if(selectedStatus.equals("Nghỉ")){
            btn.setBackground(Color.PINK);
            btn.setText(day+" Nghỉ");
        } else if(selectedStatus.equals("Đi Trễ")){
            btn.setBackground(Color.YELLOW);
            btn.setText(day+" TRỄ");
        } else if(selectedStatus.equals("Tăng ca")){
            btn.setBackground(Color.GREEN);
            btn.setText(day+" TĂNG CA "+selectedHour+" Giờ");
        } else if(selectedStatus.equals("Xóa")){
            btn.setBackground(Color.WHITE);
            btn.setText(String.valueOf(day));
        }
    }

    private void saveToTable(){
        int indexNV = cbNhanVien.getSelectedIndex();
        if(indexNV<0) return;
        int ngayCong=0, tangCa=0;
        for(JButton btn: dayButtons){
            if(btn==null) continue;
            String text = btn.getText();
            if(text.contains("Nghỉ")) continue;
            else if(text.contains("TRỄ")) continue;
            else if(text.contains("TĂNG CA")){
                tangCa += Integer.parseInt(text.split(" ")[3]);
                ngayCong++;
            } else ngayCong++;
        }
        modelCC.setValueAt(ngayCong, indexNV, 2);
        modelCC.setValueAt(0, indexNV, 3);
        modelCC.setValueAt(tangCa, indexNV, 4);
    }
}

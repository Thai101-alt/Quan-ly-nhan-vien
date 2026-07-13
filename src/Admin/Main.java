package Admin;

import Excel.*;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Trangchu trangChuPanel;
    private Nhanvien nhanvienPanel;
    private Phongban phongbanPanel;
    private Chucvu chucvuPanel;
    private Hopdong hopdongPanel;
    private Luong luongPanel;
    private Taikhoan taikhoanPanel;
    private ThongkeNhanvien thongkePanel;
    private DuyetDonXinPhep duyetDonPanel;

    private RoundedButton[] buttons;

    public Main() {
        setTitle("Quản trị hệ thống");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel menu = new JPanel(new GridLayout(9, 1, 10, 10));
        menu.setPreferredSize(new Dimension(240, 0));
        menu.setBackground(new Color(255, 204, 204));
        menu.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        RoundedButton btnHome = new RoundedButton("🏠 Trang chủ", new Color(79, 126, 173));
        RoundedButton btnNV = new RoundedButton("👤 Quản lý nhân viên", new Color(79, 126, 173));
        RoundedButton btnPB = new RoundedButton("🏢 Quản lý phòng ban", new Color(79, 126, 173));
        RoundedButton btnCV = new RoundedButton("🎖 Quản lý chức vụ", new Color(79, 126, 173));
        RoundedButton btnHD = new RoundedButton("📄 Hợp đồng", new Color(79, 126, 173));
        RoundedButton btnLuong = new RoundedButton("💰 Lương - Chấm công", new Color(79, 126, 173));
        RoundedButton btnTK = new RoundedButton("🔐 Quản lý tài khoản", new Color(79, 126, 173));
        RoundedButton btnThongKe = new RoundedButton("📊 Thống kê nhân viên", new Color(79, 126, 173));
        RoundedButton btnDuyetDon = new RoundedButton("📝 Duyệt đơn nghỉ", new Color(79, 126, 173));

        buttons = new RoundedButton[]{
            btnHome, btnNV, btnPB, btnCV,
            btnHD, btnLuong, btnTK,
            btnThongKe, btnDuyetDon
        };

        for (RoundedButton b : buttons) {
            b.setPreferredSize(new Dimension(220, 45));
            menu.add(b);
        }

        add(menu, BorderLayout.WEST);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        trangChuPanel = new Trangchu();
        nhanvienPanel = new Nhanvien();
        phongbanPanel = new Phongban();
        chucvuPanel = new Chucvu();
        hopdongPanel = new Hopdong();
        luongPanel = new Luong();
        taikhoanPanel = new Taikhoan();
        thongkePanel = new ThongkeNhanvien();
        duyetDonPanel = new DuyetDonXinPhep();

        mainPanel.add(trangChuPanel, "HOME");
        mainPanel.add(nhanvienPanel, "NV");
        mainPanel.add(phongbanPanel, "PB");
        mainPanel.add(chucvuPanel, "CV");
        mainPanel.add(hopdongPanel, "HD");
        mainPanel.add(luongPanel, "LUONG");
        mainPanel.add(taikhoanPanel, "TK");
        mainPanel.add(thongkePanel, "THONGKE");
        mainPanel.add(duyetDonPanel, "DUYETDON");

        add(mainPanel, BorderLayout.CENTER);

        btnHome.addActionListener(e -> {
            trangChuPanel.reloadAll();   
            switchPanel("HOME", btnHome);
        });

        btnNV.addActionListener(e -> {
            nhanvienPanel.reloadPhongban_Chucvu();
            switchPanel("NV", btnNV);
        });

        btnPB.addActionListener(e -> switchPanel("PB", btnPB));
        btnCV.addActionListener(e -> switchPanel("CV", btnCV));

        btnHD.addActionListener(e -> {
            hopdongPanel.reloadNhanvien();
            switchPanel("HD", btnHD);
        });

        btnLuong.addActionListener(e -> {
            luongPanel.reloadLuong();
            switchPanel("LUONG", btnLuong);
        });

        btnTK.addActionListener(e -> switchPanel("TK", btnTK));
        btnThongKe.addActionListener(e -> {
            thongkePanel.reloadThongke();
            switchPanel("THONGKE", btnThongKe);
        });

        btnDuyetDon.addActionListener(e -> switchPanel("DUYETDON", btnDuyetDon));

        setActiveButton(btnHome);
        createMenuBar();
    }

    private void switchPanel(String name, RoundedButton btn) {
        cardLayout.show(mainPanel, name);
        setActiveButton(btn);
    }

    private void setActiveButton(RoundedButton active) {
        for (RoundedButton b : buttons) {
            b.setActive(false);
        }
        active.setActive(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");

        JMenuItem mNV = new JMenuItem("Xuất Excel - Nhân viên");
        mNV.addActionListener(e -> ExcelNhanvien.exportNhanvien());

        JMenuItem mPB = new JMenuItem("Xuất Excel - Phòng ban");
        mPB.addActionListener(e -> ExcelPhongban.exportPhongban());

        JMenuItem mCV = new JMenuItem("Xuất Excel - Chức vụ");
        mCV.addActionListener(e -> ExcelChucvu.exportChucvu());

        JMenuItem mHD = new JMenuItem("Xuất Excel - Hợp đồng");
        mHD.addActionListener(e -> ExcelHopdong.exportHopdong());

        JMenuItem mLuong = new JMenuItem("Xuất Excel - Lương");
        mLuong.addActionListener(e -> ExcelLuong.exportLuong());

        JMenuItem mTK = new JMenuItem("Xuất Excel - Tài khoản");
        mTK.addActionListener(e -> ExcelTaikhoan.exportTaikhoan());

        JMenuItem mLogout = new JMenuItem("Đăng xuất");
        mLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn đăng xuất?",
                    "Đăng xuất",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); // đóng Main
                new Login.Dang_nhap().setVisible(true);
            }
        });

        menuFile.add(mNV);
        menuFile.add(mPB);
        menuFile.add(mCV);
        menuFile.add(mHD);
        menuFile.add(mLuong);
        menuFile.add(mTK);
        menuFile.addSeparator();
        menuFile.add(mLogout);

        JMenu menuHelp = new JMenu("Trợ giúp");
        JMenuItem mAbout = new JMenuItem("Giới thiệu");
        mAbout.addActionListener(e
                -> JOptionPane.showMessageDialog(this,
                        "Phần mềm quản lý nhân sự\nJava Swing + SQL Server",
                        "Giới thiệu",
                        JOptionPane.INFORMATION_MESSAGE)
        );

        menuHelp.add(mAbout);

        menuBar.add(menuFile);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}

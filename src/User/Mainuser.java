package User;

import javax.swing.*;
import java.awt.*;

public class Mainuser extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private TrangchuUser trangchuPanel;
    private DonXinPhepUser donPhepPanel;

    private String currentUser;

    public Mainuser(String manv) {
        this.currentUser = manv;

        setTitle("User - Quản lý nhân sự");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel menu = new JPanel(new GridLayout(4, 1, 5, 5));
        menu.setPreferredSize(new Dimension(200, 0));
        menu.setBackground(new Color(200, 220, 255));

        JButton btnHome = new JButton("Trang chủ");
        JButton btnDonPhep = new JButton("Đơn xin phép");
        JButton btnLogout = new JButton("Đăng xuất");

        menu.add(btnHome);
        menu.add(btnDonPhep);
        menu.add(btnLogout);

        add(menu, BorderLayout.WEST);

        // ===== PANEL CHÍNH =====
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Khởi tạo panel
        trangchuPanel = new TrangchuUser();
        donPhepPanel = new DonXinPhepUser(currentUser); 
        mainPanel.add(trangchuPanel, "HOME");
        mainPanel.add(donPhepPanel, "DONPHEP");

        add(mainPanel, BorderLayout.CENTER);

        // ===== SỰ KIỆN =====
        btnHome.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));

        btnDonPhep.addActionListener(e -> {
            cardLayout.show(mainPanel, "DONPHEP");
            donPhepPanel.loadDonPhepByUser();
        });
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn đăng xuất?",
                    "Đăng xuất",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();                
                new Login.Dang_nhap().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        String manv = "NV001";
        SwingUtilities.invokeLater(() -> new Mainuser(manv).setVisible(true));
    }
}

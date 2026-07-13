package Login;

import DAO.TaiKhoanDAO;
import Admin.Main;
import User.Mainuser;
import User.UserSession;
import javax.swing.*;
import java.awt.*;

public class Dang_nhap extends JFrame {

    TaiKhoanDAO dao = new TaiKhoanDAO();

    public Dang_nhap() {
        setTitle("Đăng nhập hệ thống");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(255, 182, 193));
        header.setPreferredSize(new Dimension(500, 60));
        JLabel title = new JLabel("Đăng nhập");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(title);
        add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setLayout(null);
        body.setBackground(Color.WHITE);

        JLabel lblUser = new JLabel("Tài khoản:");
        lblUser.setBounds(60, 40, 100, 25);
        body.add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(160, 40, 250, 30);
        body.add(txtUser);

        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setBounds(60, 90, 100, 25);
        body.add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(160, 90, 250, 30);
        body.add(txtPass);

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setBounds(200, 140, 100, 35);
        btnLogin.setBackground(Color.GREEN);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        body.add(btnLogin);

        add(body, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> {
            String username = txtUser.getText();
            String password = new String(txtPass.getPassword());

            String role = dao.getRole(username, password);

            if (role != null) {
                JOptionPane.showMessageDialog(null, "Đăng nhập thành công!");
                dispose();

                if (role.equalsIgnoreCase("Admin")) {
                    new Main().setVisible(true);
                } else {
                    String manv = dao.getManv(username, password);
                    if (manv != null) {
                        UserSession.setCurrentUser(manv); 
                        new Mainuser(manv).setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(null, "Nhân viên chưa được gán tài khoản!");
                    }
                }

            } else {
                JOptionPane.showMessageDialog(null, "Sai tài khoản hoặc mật khẩu!");
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dang_nhap().setVisible(true));
    }
}

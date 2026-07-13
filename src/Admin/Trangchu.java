package Admin;

import DAO.*;
import Model.*;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

public class Trangchu extends JPanel {

    private JTree tree;
    private JTextArea txtInfo;

    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;

    private NhanvienDAO nvDAO = new NhanvienDAO();
    private HopdongDAO hdDAO = new HopdongDAO();
    private PhongbanDAO pbDAO = new PhongbanDAO();
    private ChucvuDAO cvDAO = new ChucvuDAO();
    private LuongDAO luongDAO = new LuongDAO();

    public Trangchu() {
        setLayout(new BorderLayout());

        root = new DefaultMutableTreeNode("HỆ THỐNG NHÂN SỰ");

        loadNhanVien(root);
        loadPhongBan(root);
        loadChucVu(root);
        loadHopDong(root);
        loadLuong(root);

        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setRootVisible(true);

        JScrollPane treeScroll = new JScrollPane(tree);
        treeScroll.setPreferredSize(new Dimension(300, 0));

        txtInfo = new JTextArea();
        txtInfo.setEditable(false);
        txtInfo.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane infoScroll = new JScrollPane(txtInfo);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                treeScroll,
                infoScroll
        );
        splitPane.setDividerLocation(300);

        add(splitPane, BorderLayout.CENTER);

        addTreeEvent();
    }


    private void addTreeEvent() {
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) return;

            Object obj = node.getUserObject();

            if (obj instanceof Nhanvien_m nv) {

                Hopdong_m hd = hdDAO.getByManv(nv.getManv());
                Phongban_m pb = pbDAO.getById(nv.getMapb());
                Chucvu_m cv = cvDAO.getById(nv.getMacv());

                if (hd != null && pb != null && cv != null) {
                    txtInfo.setText(hd.toHopDongDienTu(nv, pb, cv));
                } else {
                    txtInfo.setText(
                            "NHÂN VIÊN: " + nv.getHoten() +
                            "\nTRẠNG THÁI: CHƯA CÓ HỢP ĐỒNG"
                    );
                }

            } else if (obj instanceof Phongban_m pb) {
                txtInfo.setText(pb.toFullString());

            } else if (obj instanceof Chucvu_m cv) {
                txtInfo.setText(cv.toFullString());

            } else if (obj instanceof Hopdong_m hd) {
                txtInfo.setText(hd.toFullString());

            } else if (obj instanceof Luong_m l) {
                txtInfo.setText(l.toFullString());

            } else {
                txtInfo.setText(obj.toString());
            }
        });
    }

    private void loadNhanVien(DefaultMutableTreeNode root) {
        DefaultMutableTreeNode nvNode =
                new DefaultMutableTreeNode("Nhân viên");
        for (Nhanvien_m nv : nvDAO.getAll()) {
            nvNode.add(new DefaultMutableTreeNode(nv));
        }
        root.add(nvNode);
    }

    private void loadPhongBan(DefaultMutableTreeNode root) {
        DefaultMutableTreeNode pbNode =
                new DefaultMutableTreeNode("Phòng ban");
        for (Phongban_m pb : pbDAO.getAll()) {
            pbNode.add(new DefaultMutableTreeNode(pb));
        }
        root.add(pbNode);
    }

    private void loadChucVu(DefaultMutableTreeNode root) {
        DefaultMutableTreeNode cvNode =
                new DefaultMutableTreeNode("Chức vụ");
        for (Chucvu_m cv : cvDAO.getAll()) {
            cvNode.add(new DefaultMutableTreeNode(cv));
        }
        root.add(cvNode);
    }

    private void loadHopDong(DefaultMutableTreeNode root) {
        DefaultMutableTreeNode hdNode =
                new DefaultMutableTreeNode("Hợp đồng");
        for (Hopdong_m hd : hdDAO.getAll()) {
            hdNode.add(new DefaultMutableTreeNode(hd));
        }
        root.add(hdNode);
    }

    private void loadLuong(DefaultMutableTreeNode root) {
        DefaultMutableTreeNode luongNode =
                new DefaultMutableTreeNode("Lương");
        for (Luong_m l : luongDAO.getAll()) {
            luongNode.add(new DefaultMutableTreeNode(l));
        }
        root.add(luongNode);
    }

    public void reloadNhanvien() {
        reloadNode("Nhân viên", nvDAO.getAll());
    }

    public void reloadAll() {
        root.removeAllChildren();

        loadNhanVien(root);
        loadPhongBan(root);
        loadChucVu(root);
        loadHopDong(root);
        loadLuong(root);

        treeModel.reload();
    }

    private <T> void reloadNode(String nodeName, java.util.List<T> data) {
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) root.getChildAt(i);
            if (nodeName.equals(node.getUserObject().toString())) {
                node.removeAllChildren();
                for (T item : data) {
                    node.add(new DefaultMutableTreeNode(item));
                }
                treeModel.reload(node);
                break;
            }
        }
    }
}

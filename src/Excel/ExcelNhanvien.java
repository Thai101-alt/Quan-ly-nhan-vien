package Excel;

import DAO.NhanvienDAO;
import Model.Nhanvien_m;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ExcelNhanvien {

    public static void exportNhanvien() {
        try {
            NhanvienDAO dao = new NhanvienDAO();
            ArrayList<Nhanvien_m> list = dao.getAll(); // LẤY TỪ DATABASE

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("NhanVien");

            // ===== HEADER =====
            String[] cols = {
                "Mã NV", "Họ tên", "Ngày sinh", "Giới tính",
                "SĐT", "Email", "Địa chỉ",
                "Mã PB", "Mã CV",
                "Ngày vào làm", "Trạng thái", "Lương cơ bản"
            };

            Row header = sheet.createRow(0);
            for (int i = 0; i < cols.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);

                // style header (đẹp hơn)
                CellStyle style = wb.createCellStyle();
                Font font = wb.createFont();
                font.setBold(true);
                style.setFont(font);
                style.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(style);
            }

            // ===== DATA =====
            int rowIndex = 1;
            for (Nhanvien_m nv : list) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(nv.getManv());
                row.createCell(1).setCellValue(nv.getHoten());
                row.createCell(2).setCellValue(
                        nv.getNgaysinh() != null ? nv.getNgaysinh().toString() : ""
                );
                row.createCell(3).setCellValue(nv.getGioitinh());
                row.createCell(4).setCellValue(nv.getSdt());
                row.createCell(5).setCellValue(nv.getEmail());
                row.createCell(6).setCellValue(nv.getDiachi());
                row.createCell(7).setCellValue(nv.getMapb());
                row.createCell(8).setCellValue(nv.getMacv());
                row.createCell(9).setCellValue(
                        nv.getNgayvaolam() != null ? nv.getNgayvaolam().toString() : ""
                );
                row.createCell(10).setCellValue(nv.getTrangthai());
                row.createCell(11).setCellValue(
                        nv.getLuongcoban() != null ? nv.getLuongcoban().doubleValue() : 0
                );
            }

            // Auto size
            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // ===== SAVE FILE =====
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("nhanvien.xlsx"));

            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());
                wb.write(fos);
                fos.close();
                wb.close();

                JOptionPane.showMessageDialog(null,
                        "Xuất Excel nhân viên thành công!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Lỗi xuất Excel nhân viên: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

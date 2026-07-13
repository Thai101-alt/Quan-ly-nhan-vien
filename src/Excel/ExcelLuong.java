package Excel;

import DAO.LuongDAO;
import Model.Luong_m;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ExcelLuong {

    public static void exportLuong() {
        try {
            LuongDAO dao = new LuongDAO();
            ArrayList<Luong_m> list = dao.getAll(); // LẤY TỪ DATABASE

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("Luong");

            // ===== HEADER =====
            String[] cols = {
                "Mã lương", "Mã NV", "Tháng", "Năm", "Tổng lương"
            };

            Row header = sheet.createRow(0);
            for (int i = 0; i < cols.length; i++) {
                header.createCell(i).setCellValue(cols[i]);
            }

            // ===== DATA =====
            int rowIndex = 1;
            for (Luong_m l : list) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(l.getMaluong());
                row.createCell(1).setCellValue(l.getManv());
                row.createCell(2).setCellValue(l.getThang());
                row.createCell(3).setCellValue(l.getNam());
                row.createCell(4).setCellValue(
                        l.getTongluong().doubleValue()
                );
            }

            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
            }

            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("luong.xlsx"));

            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                FileOutputStream fos =
                        new FileOutputStream(fc.getSelectedFile());
                wb.write(fos);
                fos.close();
                wb.close();

                JOptionPane.showMessageDialog(null,
                        "Xuất Excel thành công!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Lỗi xuất Excel: " + e.getMessage());
        }
    }
}

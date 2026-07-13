package Excel;

import DAO.ChucvuDAO;
import Model.Chucvu_m;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ExcelChucvu {

    public static void exportChucvu() {
        try {
            ChucvuDAO dao = new ChucvuDAO();
            ArrayList<Chucvu_m> list = dao.getAll(); // LẤY TỪ DATABASE

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("ChucVu");

            // ===== HEADER =====
            String[] cols = {
                "Mã chức vụ", "Tên chức vụ", "Hệ số lương"
            };

            Row header = sheet.createRow(0);
            CellStyle headerStyle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            for (int i = 0; i < cols.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(cols[i]);
                cell.setCellStyle(headerStyle);
            }

            // ===== DATA =====
            int rowIndex = 1;
            for (Chucvu_m cv : list) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(cv.getMacv());
                row.createCell(1).setCellValue(cv.getTencv());
                row.createCell(2).setCellValue(cv.getHesoluong());
            }

            // Auto size
            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // ===== SAVE FILE =====
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("chucvu.xlsx"));

            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());
                wb.write(fos);
                fos.close();
                wb.close();

                JOptionPane.showMessageDialog(null,
                        "Xuất Excel chức vụ thành công!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Lỗi xuất Excel chức vụ: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package Excel;

import DAO.PhongbanDAO;
import Model.Phongban_m;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ExcelPhongban {

    public static void exportPhongban() {
        try {
            PhongbanDAO dao = new PhongbanDAO();
            ArrayList<Phongban_m> list = dao.getAll(); // LẤY TỪ DATABASE

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("PhongBan");

            // ===== HEADER =====
            String[] cols = {
                "Mã phòng ban", "Tên phòng ban", "Mô tả"
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
            for (Phongban_m pb : list) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(pb.getMapb());
                row.createCell(1).setCellValue(pb.getTenpb());
                row.createCell(2).setCellValue(
                        pb.getMota() != null ? pb.getMota() : ""
                );
            }

            // Auto size
            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // ===== SAVE FILE =====
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("phongban.xlsx"));

            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());
                wb.write(fos);
                fos.close();
                wb.close();

                JOptionPane.showMessageDialog(null,
                        "Xuất Excel phòng ban thành công!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Lỗi xuất Excel phòng ban: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

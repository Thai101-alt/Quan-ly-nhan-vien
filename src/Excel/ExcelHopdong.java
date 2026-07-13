package Excel;

import DAO.HopdongDAO;
import Model.Hopdong_m;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ExcelHopdong {

    public static void exportHopdong() {
        try {
            HopdongDAO dao = new HopdongDAO();
            ArrayList<Hopdong_m> list = dao.getAll(); // LẤY TỪ DATABASE

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("HopDong");

            // ===== HEADER =====
            String[] cols = {
                "Mã hợp đồng", "Mã NV", "Loại hợp đồng",
                "Ngày bắt đầu", "Ngày kết thúc", "Lương cơ bản"
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
            for (Hopdong_m hd : list) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(hd.getMahd());
                row.createCell(1).setCellValue(hd.getManv());
                row.createCell(2).setCellValue(hd.getLoaihd());

                row.createCell(3).setCellValue(
                        hd.getNgaybatdau() != null ? hd.getNgaybatdau().toString() : ""
                );
                row.createCell(4).setCellValue(
                        hd.getNgayketthuc() != null ? hd.getNgayketthuc().toString() : ""
                );
                row.createCell(5).setCellValue(
                        hd.getLuongcoban() != null ? hd.getLuongcoban().doubleValue() : 0
                );
            }

            // Auto size
            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // ===== SAVE FILE =====
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("hopdong.xlsx"));

            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());
                wb.write(fos);
                fos.close();
                wb.close();

                JOptionPane.showMessageDialog(null,
                        "Xuất Excel hợp đồng thành công!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Lỗi xuất Excel hợp đồng: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package Excel;

import DAO.TaiKhoanDAO;
import Model.Taikhoan_m;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ExcelTaikhoan {

    public static void exportTaikhoan() {
        try {
            TaiKhoanDAO dao = new TaiKhoanDAO();
            ArrayList<Taikhoan_m> list = (ArrayList<Taikhoan_m>) dao.getAll();

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("TaiKhoan");

            String[] cols = {
                "Username", "Quyền", "Mã nhân viên"
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

            int rowIndex = 1;
            for (Taikhoan_m tk : list) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(tk.getUsername());
                row.createCell(1).setCellValue(tk.getQuyen());
                row.createCell(2).setCellValue(
                        tk.getManv() == null ? "" : tk.getManv()
                );
            }
            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
            }

            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("taikhoan.xlsx"));

            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                FileOutputStream fos =
                        new FileOutputStream(fc.getSelectedFile());
                wb.write(fos);
                fos.close();
                wb.close();

                JOptionPane.showMessageDialog(
                        null,
                        "Xuất Excel tài khoản thành công!"
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Lỗi xuất Excel tài khoản: " + e.getMessage()
            );
            e.printStackTrace();
        }
    }
}

package com.dada.hrm.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.dada.hrm.entity.DailyActivity;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ReportExportService {
    private static final String[] HEADERS = {
            "Date", "Engineer", "Application", "App Status", "Backup", "Received", "Resolved", "Pending", "Status", "Remarks"
    };

    public byte[] toExcel(List<DailyActivity> activities) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Activity Report");
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row header = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                header.createCell(i).setCellValue(HEADERS[i]);
                header.getCell(i).setCellStyle(headerStyle);
            }

            int rowIndex = 1;
            for (DailyActivity activity : activities) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(String.valueOf(activity.getActivityDate()));
                row.createCell(1).setCellValue(activity.getEngineerName());
                row.createCell(2).setCellValue(activity.getApplicationName());
                row.createCell(3).setCellValue(String.valueOf(activity.getApplicationStatus()));
                row.createCell(4).setCellValue(String.valueOf(activity.getBackupStatus()));
                row.createCell(5).setCellValue(activity.getTicketsReceived());
                row.createCell(6).setCellValue(activity.getTicketsResolved());
                row.createCell(7).setCellValue(activity.getPendingTickets());
                row.createCell(8).setCellValue(String.valueOf(activity.getStatus()));
                row.createCell(9).setCellValue(activity.getRemarks() == null ? "" : activity.getRemarks());
            }

            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(output);
            return output.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to create Excel report", ex);
        }
    }

    public byte[] toPdf(List<DailyActivity> activities) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate(), 32, 32, 32, 32);
            PdfWriter.getInstance(document, output);
            document.open();

            Paragraph title = new Paragraph("Application Support Activity Report",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            title.setSpacingAfter(16);
            document.add(title);

            PdfPTable table = new PdfPTable(HEADERS.length);
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 1.1f, 1.5f, 1.8f, 1.2f, 1.2f, 0.9f, 0.9f, 0.9f, 1.1f, 2.2f });
            for (String header : HEADERS) {
                PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9)));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setPadding(6);
                table.addCell(cell);
            }

            for (DailyActivity activity : activities) {
                addCell(table, String.valueOf(activity.getActivityDate()));
                addCell(table, activity.getEngineerName());
                addCell(table, activity.getApplicationName());
                addCell(table, String.valueOf(activity.getApplicationStatus()));
                addCell(table, String.valueOf(activity.getBackupStatus()));
                addCell(table, String.valueOf(activity.getTicketsReceived()));
                addCell(table, String.valueOf(activity.getTicketsResolved()));
                addCell(table, String.valueOf(activity.getPendingTickets()));
                addCell(table, String.valueOf(activity.getStatus()));
                addCell(table, activity.getRemarks() == null ? "" : activity.getRemarks());
            }

            document.add(table);
            document.close();
            return output.toByteArray();
        } catch (DocumentException | IOException ex) {
            throw new IllegalStateException("Unable to create PDF report", ex);
        }
    }

    private void addCell(PdfPTable table, String value) {
        PdfPCell cell = new PdfPCell(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA, 8)));
        cell.setPadding(5);
        table.addCell(cell);
    }
}

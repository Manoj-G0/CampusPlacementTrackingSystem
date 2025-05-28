package com.cpt.util;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFReportGenerator {

	public static byte[] generateCompanyPlacementReport(List<Map<String, Object>> data) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, out);
		document.open();
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
		document.add(new Paragraph("Company Wise Placements Report", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18)));
		document.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10);
		table.setWidths(new float[] { 3f, 4.5f });
		String[] headers = { "Company", "Number of Students Placed" };
		for (String header : headers) {
			PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
			cell.setBackgroundColor(BaseColor.DARK_GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setNoWrap(true);
			cell.setPadding(8);
			table.addCell(cell);
		}
		for (Map<String, Object> row : data) {
			addTableCell(table, row, "Company", cellFont);
			addTableCell(table, row, "number_of_students_placed", cellFont);
		}

		document.add(table);
		document.close();
		return out.toByteArray();
	}

	public static byte[] generatePlacementDriveReport(List<Map<String, Object>> stats) throws DocumentException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, out);
		document.open();
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
		document.add(new Paragraph("Placement Drives Report", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18)));
		document.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10);
		table.setWidths(new float[] { 3f, 4f, 2.5f, 2.5f, 5.5f });
		String[] headers = { "PlacementID", "Placement Name", "Start Date", "End Date", "Number of Students Placed" };
		for (String header : headers) {
			PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
			cell.setBackgroundColor(BaseColor.DARK_GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setNoWrap(true);
			cell.setPadding(8);
			table.addCell(cell);
		}
		for (Map<String, Object> row : stats) {
			addTableCell(table, row, "PlacementID", cellFont);
			addTableCell(table, row, "Placement Name", cellFont);
			addTableCell(table, row, "Start Date", cellFont);
			addTableCell(table, row, "End Date", cellFont);
			addTableCell(table, row, "Number of Students Placed", cellFont);
		}

		document.add(table);
		document.close();
		return out.toByteArray();
	}

	public static byte[] generateBranchPlacementDriveReport(List<Map<String, Object>> stats) throws DocumentException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, out);
		document.open();
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
		document.add(
				new Paragraph("Branch Wise Placement Drives Report", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18)));
		document.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10);
		table.setWidths(new float[] { 3f, 4.5f });
		String[] headers = { "Branch Name", "Number of Students Placed" };
		for (String header : headers) {
			PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
			cell.setBackgroundColor(BaseColor.DARK_GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setNoWrap(true);
			cell.setPadding(8);
			table.addCell(cell);
		}
		for (Map<String, Object> row : stats) {
			addTableCell(table, row, "Branch Name", cellFont);
			addTableCell(table, row, "Number of Students Placed", cellFont);
		}

		document.add(table);
		document.close();
		return out.toByteArray();
	}

	public static byte[] generateStudentPlacementDriveReport(List<Map<String, Object>> stats) throws DocumentException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, out);
		document.open();

		document.add(
				new Paragraph("Student Placement Drives Report", FontFactory.getFont(FontFactory.TIMES_ROMAN, 18)));
		document.add(new Paragraph(" "));

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10);
		table.setWidths(new float[] { 4f, 3f, 1.5f, 2f, 1.5f, 3.5f });

		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);

		String[] headers = { "Registration Number", "Name", "Branch Name", "Gender", "CGPA", "Company Placed" };
		for (String header : headers) {
			PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
			cell.setBackgroundColor(BaseColor.DARK_GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setNoWrap(true);
			cell.setPadding(8);
			table.addCell(cell);
		}
		for (Map<String, Object> row : stats) {
			addTableCell(table, row, "Name", cellFont);
			addTableCell(table, row, "Branch", cellFont);
			addTableCell(table, row, "Gender", cellFont);
			addTableCell(table, row, "CGPA", cellFont);
			addTableCell(table, row, "Company Placed", cellFont);
		}

		document.add(table);
		document.close();
		return out.toByteArray();
	}

	public static byte[] generateRoundwiseReport(List<Map<String, Object>> data, int phase_seq) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document doc = new Document();
		PdfWriter.getInstance(doc, out);
		doc.open();
		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
		doc.add(new Paragraph("Round " + phase_seq + " Shortlisted Students Report",
				FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
		doc.add(new Paragraph(" "));
		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100);
		table.setSpacingBefore(10f);
		table.setWidths(new float[] { 4.5f, 4f, 2.5f, 3f, 1.5f, 2f, 1.5f });

		String[] headers = { "Registration Number", "Application ID", "Name", "Branch", "CGPA", "Gender", "Score" };
		for (String header : headers) {
			PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
			cell.setBackgroundColor(BaseColor.DARK_GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setNoWrap(true);
			cell.setPadding(8);
			table.addCell(cell);
		}
		for (Map<String, Object> row : data) {
			addTableCell(table, row, "Registration Number", cellFont);
			addTableCell(table, row, "Application ID", cellFont);
			addTableCell(table, row, "Name", cellFont);
			addTableCell(table, row, "Branch", cellFont);
			addTableCell(table, row, "CGPA", cellFont);
			addTableCell(table, row, "Gender", cellFont);
			addTableCell(table, row, "Score", cellFont);
		}

		doc.add(table);
		doc.close();
		return out.toByteArray();
	}

	private static PdfPCell createCenteredCell(String text, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(6);
		return cell;
	}

	public static void addTableCell(PdfPTable table, Map<String, Object> row, String cellvalue, Font cellFont) {
		table.addCell(createCenteredCell(row.get(cellvalue).toString(), cellFont));
	}

	public static byte[] generateAllRoundwiseReport(List<Map<String, Object>> stats) {

		return null;
	}
}

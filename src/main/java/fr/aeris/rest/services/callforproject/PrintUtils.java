package fr.aeris.rest.services.callforproject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import fr.sedoo.commons.util.ListUtil;
import fr.sedoo.commons.util.StringUtil;


public class PrintUtils {

	private Locale locale;
	private PdfEventListener eventListener;
	private String currentElement = "";
	private static Font h1Font = FontFactory.getFont(FontFactory.HELVETICA, 22,
			Font.BOLDITALIC);
	private static Font h2Font = FontFactory.getFont(FontFactory.HELVETICA, 15,
			Font.BOLD);
	private static Font normalFont = FontFactory.getFont(FontFactory.HELVETICA,
			10, Font.NORMAL);
	private static Font boldFont = FontFactory.getFont(FontFactory.HELVETICA,
			10, Font.BOLD);
	private static BaseColor backgroundColor = BaseColor.WHITE;

	public PrintUtils() {
		this(Locale.FRENCH);
	}

	public PrintUtils(Locale locale) {
		this.locale = locale;
		eventListener = new PdfEventListener();
	}

	public File printCallForProject(CallForProject callForProject,
			File outputFile) throws Exception {

		File result = null;
		if (outputFile != null) {
			result = outputFile;
			File parentFolder = result.getParentFile();
			if (!parentFolder.exists()) {
				parentFolder.mkdirs();
			}
		} else {
			result = File.createTempFile("AERIS_", ".pdf");
		}
		Document document = null;

		try {
			document = openDocument(result);
			writeHeader(document);
			writeProjectDescriptionPart(callForProject, document);
			writeApplicantDescription(callForProject, document);
			writeSubmissionDate(callForProject, document);
			// document.newPage();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			closeDocument(document);
		}
	}

	private void writeSubmissionDate(CallForProject callForProject,
			Document document) throws DocumentException {
		newLine(document);
		newLine(document);

		DateFormat dateFormater = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, locale);
		writePairValue("Date de soumission", dateFormater.format(callForProject.getSubmissionDate()), document);

	}

	private void writeApplicantDescription(CallForProject callForProject,
			Document document) throws DocumentException {

		writeSection("Informations relatives au demandeur", document);
		writeBoldPairValue("Nom", callForProject.getApplicantName(), document);
		writePairValue("Mél", callForProject.getApplicantEmail(), document);
		writePairValue("Laboratoire", callForProject.getApplicantLaboratory(), document);

		writeBoldSentence("Directeur du laboratoire", document);

		writeBoldPairValue("Nom",
				callForProject.getDirectorName(), document);

		writePairValue("Mél",
				callForProject.getDirectorEmail(), document);

	}

	private void writeBoldSentence(String sentence, Document document)
			throws DocumentException {
		setCurrentElement(sentence);
		PdfPTable table = new PdfPTable(1);
		PdfPCell cellule = new PdfPCell(new Phrase(sentence, boldFont));
		cellule.setBorder(0);
		cellule.setPaddingBottom(5);
		table.addCell(cellule);
		table.setWidthPercentage(100);
		document.add(table);

	}

	private void writeHeader(Document document) throws Exception {
		PdfPTable oneCellTable = getHeaderTable();
		String logoUrl = "http://www.aeris-data.fr/images/logo.png";
		Image logo = Image.getInstance(logoUrl);
		logo.scaleAbsolute(171, 90);
		PdfPCell logoCell = new PdfPCell(logo, false);
		logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		logoCell.setVerticalAlignment(Element.ALIGN_TOP);
		logoCell.setBackgroundColor(backgroundColor);
		logoCell.setBorder(0);
		logoCell.setBorderWidthBottom(2);
		logoCell.setBorderColorBottom(BaseColor.WHITE);
		logoCell.setPaddingBottom(7);
		logoCell.setPaddingTop(7);
		oneCellTable.addCell(logoCell);

		PdfPCell titleCell = new PdfPCell(new Phrase("Appel à projet", h1Font));
		titleCell.setBackgroundColor(backgroundColor);
		titleCell.setBorder(0);
		titleCell.setBorderWidthBottom(2);
		titleCell.setBorderColorBottom(BaseColor.WHITE);
		titleCell.setPaddingBottom(7);
		titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		titleCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);

		oneCellTable.addCell(titleCell);

		document.add(oneCellTable);
		newLine(document);
	}

	private void writeProjectDescriptionPart(CallForProject callForProject,
			Document document) throws DocumentException {
		writeSection("Description du projet", document);
		writeBoldPairValue("Nom", callForProject.getName() , document);
		writePairValue("Description", callForProject.getDescription(), document);
		writeProjectManagersPart(callForProject, document);
		writePairValue("Contexte scientifique",
				callForProject.getContext(), document);
		writePairValue("Support scientifique",
				callForProject.getSupport(), document);
		writePairValue("Utilisation envisagée",
				callForProject.getUse(), document);
		writePairValue("Ressources humaines",
				callForProject.getHumanResources(), document);
		writePairValue("Ressources informatiques",
				callForProject.getComputingResources(), document);
		writeSection("Echéance", document);
		writeBoldPairValue("Echéance",
				callForProject.getDueDate(), document);
		writeBoldPairValue("Justification",
				callForProject.getDueDateJustification(), document);

	}

	private Document openDocument(File output) throws Exception {
		Document document = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(output));
		writer.setPageEvent(eventListener);
		document.open();
		return document;
	}

	private void closeDocument(Document document) {
		if (document.isOpen()) {
			document.close();
		}
	}

	private void writeSection(String sectionName, Document document)
			throws DocumentException {
		setCurrentElement(sectionName);
		PdfPTable table = new PdfPTable(1);
		PdfPCell cellule = new PdfPCell(new Phrase(sectionName, h2Font));
		cellule.setBorder(0);
		cellule.setBorderColorBottom(new BaseColor(1, 159, 222));
		cellule.setPaddingBottom(5);
		cellule.setBorderWidthBottom(2);
		table.addCell(cellule);
		table.setWidthPercentage(100);
		document.add(table);
	}

	private void newLine(Document document) throws DocumentException {
		PdfPTable table = new PdfPTable(1);
		PdfPCell cellule = new PdfPCell(new Phrase("", h2Font));
		cellule.setBorder(0);
		cellule.setPaddingBottom(5);
		table.addCell(cellule);
		table.setWidthPercentage(100);
		document.add(table);
	}

	public String getCurrentElement() {
		return currentElement;
	}

	public void setCurrentElement(String currentElement) {
		if (eventListener != null) {
			if (eventListener.isWaitingStart()) {
				eventListener.setStartName(currentElement);
				eventListener.setWaitingStart(false);
			}
		}
		this.currentElement = currentElement;
	}

	private void writePairValue(String label, String value, Document document)
			throws DocumentException {
		String aux = value;
		if (StringUtil.isEmpty(value)) {
			return;
		}
		PdfPTable table = getDefaultTable();
		table.addCell(getLabelCell(label));
		PdfPCell valueCell = new PdfPCell(new Phrase(aux, normalFont));
		valueCell.setBackgroundColor(backgroundColor);
		valueCell.setBorder(0);
		valueCell.setBorderWidthBottom(2);
		valueCell.setBorderColorBottom(BaseColor.WHITE);
		valueCell.setPaddingBottom(7);
		valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		valueCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		table.addCell(valueCell);

		document.add(table);
	}

	private void writeBoldPairValue(String label, String value,
			Document document) throws DocumentException {
		String aux = value;
		if (StringUtil.isEmpty(value)) {
			return;
		}
		PdfPTable table = getDefaultTable();
		table.addCell(getLabelCell(label));
		PdfPCell valueCell = new PdfPCell(new Phrase(aux, boldFont));
		valueCell.setBackgroundColor(backgroundColor);
		valueCell.setBorder(0);
		valueCell.setBorderWidthBottom(2);
		valueCell.setBorderColorBottom(BaseColor.WHITE);
		valueCell.setPaddingBottom(7);
		valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		valueCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		table.addCell(valueCell);

		document.add(table);
	}

	private void writeLeftItem(String label, Document document)
			throws DocumentException {

		PdfPTable table = getDefaultTable();
		table.addCell(getLabelCell(label));
		PdfPCell valueCell = new PdfPCell(new Phrase("", normalFont));
		valueCell.setBackgroundColor(backgroundColor);
		valueCell.setBorder(0);
		valueCell.setBorderWidthBottom(2);
		valueCell.setBorderColorBottom(BaseColor.WHITE);
		valueCell.setPaddingBottom(7);
		valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		valueCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		table.addCell(valueCell);

		document.add(table);
	}

	private PdfPCell getLabelCell(String label) {
		String aux = label.trim();
		aux = ensureColumn(aux);
		PdfPCell labelCell = new PdfPCell(new Phrase(aux, boldFont));
		labelCell.setBorder(0);
		labelCell.setBackgroundColor(backgroundColor);
		labelCell.setBorderColorBottom(BaseColor.WHITE);
		labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		labelCell.setVerticalAlignment(Element.ALIGN_TOP);
		labelCell.setBorderWidthBottom(1);
		labelCell.setPaddingBottom(7);
		return labelCell;
	}

	private String ensureColumn(String value) {
		if (value.indexOf(":") < 0) {
			return value + " :";
		} else {
			return value;
		}
	}

	private PdfPTable getDefaultTable() {
		float[] widths = { 2, 8 };
		PdfPTable table = new PdfPTable(widths);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.setWidthPercentage(100);
		return table;
	}

	private PdfPTable getHeaderTable() {
		float[] widths = { 4, 6 };
		PdfPTable table = new PdfPTable(widths);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.setWidthPercentage(100);
		return table;
	}

	private PdfPTable getContactTable() {
		float[] widths = { 2.5f, 2.5f, 2.5f, 2.5f };
		PdfPTable table = new PdfPTable(widths);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.setWidthPercentage(100);
		return table;
	}

	private void writeProjectManagersPart(CallForProject callForProject,
			Document document) throws DocumentException {
		List<Responsible> projectManagers = callForProject
				.getResponsibles();
		if (ListUtil.isNotEmpty(projectManagers)) {

			writeLeftItem("Responsables", document);
			PdfPTable table = getContactTable();
			table.setHeaderRows(1);
			table.addCell(getHeaderCell("Nom"));
			table.addCell(getHeaderCell("Mél"));
			table.addCell(getHeaderCell("Organisation"));

			Iterator<Responsible> iterator = projectManagers.iterator();

			while (iterator.hasNext()) {
				Responsible aux = (Responsible) iterator.next();
				table.addCell(getContentCell(aux.getName()));
				table.addCell(getContentCell(aux.getEmail()));
				table.addCell(getContentCell(aux.getOrganisation()));
			}

			document.add(table);
			newLine(document);

		}

	}

	private PdfPCell getContentCell(String label) {
		String aux = StringUtil.trimToEmpty(label);
		PdfPCell labelCell = new PdfPCell(new Phrase(aux, normalFont));
		labelCell.setBorder(0);
		labelCell.setBackgroundColor(backgroundColor);
		labelCell.setBorderColorBottom(BaseColor.BLACK);
		labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		labelCell.setVerticalAlignment(Element.ALIGN_TOP);
		labelCell.setBorderWidthBottom(1);
		labelCell.setPaddingBottom(7);
		return labelCell;
	}

	private PdfPCell getHeaderCell(String label) {
		String aux = label.trim();
		PdfPCell labelCell = new PdfPCell(new Phrase(aux, boldFont));
		labelCell.setBorder(0);
		labelCell.setBackgroundColor(backgroundColor);
		labelCell.setBorderColorBottom(BaseColor.BLACK);
		labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		labelCell.setVerticalAlignment(Element.ALIGN_TOP);
		labelCell.setBorderWidthBottom(2);
		labelCell.setPaddingBottom(7);
		return labelCell;
	}

}

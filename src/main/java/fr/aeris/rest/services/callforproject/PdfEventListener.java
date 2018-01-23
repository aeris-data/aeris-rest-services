package fr.aeris.rest.services.callforproject;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfOutline;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfEventListener extends PdfPageEventHelper {

	private static final Logger LOGGER = Logger
			.getLogger(PdfEventListener.class);

	private boolean waitingStart = true;
	private String startName = "";
	private PdfContentByte cb;
	private PdfTemplate template;
	private BaseFont bf = null;
	private BaseFont bookmarkBf = null;
	private PdfOutline lastBookMarkOutline;
	private PdfWriter currentWriter;

	public PdfEventListener() {
		super();
		try {
			bookmarkBf = BaseFont.createFont(BaseFont.COURIER, BaseFont.CP1252,
					BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			LOGGER.error("Erreur", e);
		}
	}

	public boolean isWaitingStart() {
		return waitingStart;
	}

	public void setWaitingStart(boolean waitingStart) {
		this.waitingStart = waitingStart;

	}

	public void setStartName(String elementName) {
		startName = elementName;

	}

	public String getStartName() {
		return startName;
	}

	public void onOpenDocument(PdfWriter writer, Document document) {
		try {
			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252,
					BaseFont.NOT_EMBEDDED);
			cb = writer.getDirectContent();
			template = cb.createTemplate(50, 50);
		} catch (DocumentException de) {
			LOGGER.error("Erreur", de);
		} catch (IOException ioe) {
			LOGGER.error("Erreur", ioe);
		}
		currentWriter = writer;
	}

	public void onStartPage(PdfWriter writer, Document document) {
		setWaitingStart(true);

	}

	public void writeSubBookMark(String title) {
		PdfContentByte cb = currentWriter.getDirectContent();
		PdfDestination destination = new PdfDestination(PdfDestination.FIT);
		cb.setFontAndSize(bookmarkBf, 8);
		new PdfOutline(lastBookMarkOutline, destination, title);
		currentWriter.setViewerPreferences(PdfWriter.PageModeUseOutlines);
	}

	public void writeBookMark(String title) {
		PdfContentByte cb = currentWriter.getDirectContent();
		PdfDestination destination = new PdfDestination(PdfDestination.FIT);
		cb.setFontAndSize(bookmarkBf, 8);
		lastBookMarkOutline = new PdfOutline(cb.getRootOutline(), destination,
				title);
		currentWriter.setViewerPreferences(PdfWriter.PageModeUseOutlines);
	}

	public void onEndPage(PdfWriter writer, Document document) {
		int pageN = writer.getPageNumber();
		String text = "Page " + pageN + " / ";

		float len = bf.getWidthPoint(text, 8);
		cb.beginText();
		cb.setFontAndSize(bf, 8);
		cb.setTextMatrix(280, 30);
		cb.showText(text);
		cb.endText();
		cb.addTemplate(template, 280 + len, 30);

		// PdfPTable head = new PdfPTable(1);
		// head.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		// head.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		// head.getDefaultCell().setPaddingBottom(10);
		//
		// // for (int k = 1; k <= 6; ++k)
		// String complement = "";
		// if (getStartName().compareTo(service.getCurrentElement()) == 0) {
		// complement = getStartName();
		// } else {
		// complement = "de " + getStartName() + " \u00e0 " +
		// service.getCurrentElement();
		// }
		// head.addCell(new Phrase("Impression d'enqu\u00eate - Enquete: " +
		// metadata.getResourceTitle() + " - " + complement, font8));
		//
		// head.setTotalWidth(page.getWidth() - document.leftMargin() -
		// document.rightMargin());
		// head.writeSelectedRows(0, -1, document.leftMargin(), page.getHeight()
		// - document.topMargin() + head.getTotalHeight(),
		// writer.getDirectContent());
	}

	public void onCloseDocument(PdfWriter writer, Document document) {
		template.beginText();
		template.setFontAndSize(bf, 8);
		template.showText(String.valueOf(writer.getPageNumber() - 1));
		template.endText();
	}

	public void addBookmark(int index, String label) {

	}

}

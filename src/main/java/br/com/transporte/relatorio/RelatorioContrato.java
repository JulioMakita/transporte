package br.com.transporte.relatorio;

import static com.itextpdf.text.Font.FontFamily.HELVETICA;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.juliomakita.fxcomponents.control.util.BigDecimalUtil;
import br.com.juliomakita.fxcomponents.control.util.DocumentUtil;
import br.com.transporte.model.Cliente;
import br.com.transporte.model.Contrato;
import br.com.transporte.model.Endereco;
import br.com.transporte.model.ItinerarioContrato;
import br.com.transporte.model.ParcelaContrato;
import br.com.transporte.model.ValorContrato;
import br.com.transporte.util.ValorExtensoConverter;

public class RelatorioContrato {
	
	private static final String PATH = "contrato";
	
	private static final Logger LOG = LoggerFactory.getLogger(RelatorioContrato.class);
	
	private static final float SPACE_11 = 6f;
	
	private static final float SPACE_TEXT = 11f;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private static SimpleDateFormat hms = new SimpleDateFormat("-ddMMyy-hhmmss");
	
    private static BigDecimalUtil util = new BigDecimalUtil();
    
    private static LocalDate currentDate = LocalDateTime.now().toLocalDate();

	public static void geraContrato(final Cliente cliente){
		
		Endereco endereco = cliente.getEndereco();
		Contrato contrato = cliente.getContrato();
		ItinerarioContrato itineratio = contrato.getItinerarioContrato();
		ParcelaContrato parcela = contrato.getParcelaContrato();
		ValorContrato valor = contrato.getValorContrato();
		
		try {
			
			URL logoPath = RelatorioContrato.class.getClass().getResource("/images/logo.png");

			Document document = new Document(PageSize.A4);
			Font fontBold9 = new Font(HELVETICA, 8,Font.BOLD);
		    Font font7 = new Font(HELVETICA, 6);
		    Font font9 = new Font(HELVETICA, 8);
		    
		    File file = new File("contrato");
		    
		    if(!file.exists()){
		    	file.mkdir();
		    }
			
	        PdfWriter.getInstance(document, new FileOutputStream(new File(file, PATH + hms.format(new Date()) + ".pdf")));
	        
	        document.open();
	        
	        Image img = Image.getInstance(logoPath);
	        img.setAlignment(Element.PARAGRAPH);
	        img.scaleToFit(220, 300);

	        document.add(img);
	        
	        Paragraph cabecalho = new Paragraph();    
	        cabecalho.setLeading(10f);
	        cabecalho.setAlignment(Element.ALIGN_MIDDLE);
	        cabecalho.setFont(font7);
	        cabecalho.add("\n\n\n\n\n nonononononononononononononononononononononononononononononononononono"
	        		+ "\n nonononononononononononononononononononononononononononononononononono"
	        		+ "\n nonononononononononononononononononononononononononononononononononono");
	
	        document.add(cabecalho);
	        document.add(new Paragraph(SPACE_11, "\n"));

	        document.close();
	        
	        LOG.info("Done");

        } catch (Exception e) {
        	LOG.error(e.getMessage(), e);
        }
	}
	
	public static PdfPCell getCell(String text, Font font, int alignment) {
	    PdfPCell cell = new PdfPCell(new Phrase(15f, text, font));
	    cell.setPadding(0);
	    cell.setHorizontalAlignment(alignment);
	    cell.setBorder(PdfPCell.NO_BORDER);
	    return cell;
	}
}

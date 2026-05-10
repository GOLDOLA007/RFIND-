package view;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

public class NumeradorDePaginas extends PdfPageEventHelper {
    private Font fontRodape = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase(String.valueOf(writer.getPageNumber()), fontRodape);

        ColumnText.showTextAligned(
                cb,
                Element.ALIGN_RIGHT,
                footer,
                (document.right()),
                document.bottom() - 10,
                0
        );
    }
}

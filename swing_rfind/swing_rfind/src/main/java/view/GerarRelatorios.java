package view;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import control.RegistroControl;
import model.Registro;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class GerarRelatorios {
    public void geraRelatorio(int empresa_id, Integer id, String funcionario,
                                      String local, String sensor, String dataEntrada,
                                      String horaEntrada, String dataSaida, String horaSaida) {
        RegistroControl registroControl = new RegistroControl();
        try {
            Font fontTitulo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 32, Font.BOLD);
            Font fontTexto = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16);
            Font fontTextoBold = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, Font.BOLD);

            PdfPTable tblRegistros = new PdfPTable(5);
            String caminho = System.getProperty("user.home") + File.separator + "Downloads";
            Document document = new Document();

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(caminho + File.separator + "/Relatório de registros.pdf"));
            writer.setPageEvent(new NumeradorDePaginas());

            // Cabeçalho da tabela
            tblRegistros.addCell(new Phrase("ID do Func.", fontTextoBold));
            tblRegistros.addCell(new Phrase("Funcionário", fontTextoBold));
            tblRegistros.addCell(new Phrase("Local", fontTextoBold));
            tblRegistros.addCell(new Phrase("Entrada", fontTextoBold));
            tblRegistros.addCell(new Phrase("Saída", fontTextoBold));

            // Buscar registros com filtros
            List<Registro> registros;
            if (id == null && funcionario == null && local == null && sensor == null &&
                    dataEntrada == null && horaEntrada == null && dataSaida == null && horaSaida == null) {
                // Sem filtros - busca tudo
                registros = registroControl.buscarRegistros(empresa_id);
            } else {
                // Com filtros
                registros = registroControl.buscarRegistrosComFiltros(
                        id, funcionario, local, sensor,
                        dataEntrada, horaEntrada, dataSaida, horaSaida, empresa_id
                );
            }

            // Preencher tabela com registros
            for (Registro registro : registros) {
                tblRegistros.addCell(new Phrase(String.valueOf(registro.getFuncionario().getId()), fontTexto));
                tblRegistros.addCell(new Phrase(registro.getFuncionario().getNome(), fontTexto));
                tblRegistros.addCell(new Phrase(registro.getLocal().getNome(), fontTexto));

                String entrada = registro.getDataEntrada() + "-" + registro.getHoraEntrada();
                String saida = registro.getDataSaida() != null ?
                        registro.getDataSaida() + "-" + registro.getHoraSaida() : "N/A";

                tblRegistros.addCell(new Phrase(entrada, fontTexto));
                tblRegistros.addCell(new Phrase(saida, fontTexto));
            }

            String titulo = "Relatório de Registros";

            if (id != null) {
                titulo += " - Filtrado por ID: " + id;
            } else if (funcionario != null) {
                titulo += " - Filtrado por Funcionário: " + funcionario;
            } else if (local != null) {
                titulo += " - Filtrado por Local: " + local;
            } else if (sensor != null) {
                titulo += " - Filtrado por Sensor: " + sensor;
            } else if (dataEntrada != null) {
                titulo += " - Filtrado por Data de Entrada: " + dataEntrada;
            } else if (horaEntrada != null) {
                titulo += " - Filtrado por Hora de Entrada: " + horaEntrada;
            } else if (dataSaida != null) {
                titulo += " - Filtrado por Data de Saída: " + dataSaida;
            } else if (horaSaida != null) {
                titulo += " - Filtrado por Hora de Saída: " + horaSaida;
            }

            Paragraph paragraph = new Paragraph(titulo, fontTitulo);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setSpacingAfter(30f);

            document.open();
            document.add(paragraph);
            document.add(tblRegistros);
            document.close();

        } catch (Exception ex) {
            System.out.println("Erro ao gerar relatório: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

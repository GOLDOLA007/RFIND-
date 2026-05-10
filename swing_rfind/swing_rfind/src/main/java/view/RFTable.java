package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.InputStream;

public class RFTable extends JTable {
    private Font quicksand;
    private Font quicksandHead;
    private Color verydarkgray;

    public RFTable() {
        setOpaque(false);
        verydarkgray = new Color(32,35,42);
        try{
            InputStream fontePrincipal = RFTable.class.getResourceAsStream("/Fonts/Open_Sans/OpenSans-VariableFont_wdth,wght.ttf");
            quicksand = Font.createFont(Font.TRUETYPE_FONT,fontePrincipal);
            quicksand = quicksand.deriveFont(Font.PLAIN,15);

            quicksandHead = new Font("SansSerif", Font.BOLD, 16);
        }
        catch(Exception e){
            System.out.println("Excessão na fonte: "+e.getMessage());
        }

        setFont(quicksand);
        setForeground(Color.WHITE);
        setBackground(verydarkgray);
        setShowGrid(false); // Remove as linhas de grid
        setIntercellSpacing(new Dimension(10, 10)); // Espaçamento entre células
        setRowHeight(28); // Altura das linhas
        setOpaque(false);

        // Configura o renderizador centralizado para todas as colunas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setFont(quicksand);
        centerRenderer.setForeground(Color.WHITE);
        centerRenderer.setBackground(verydarkgray);

        // Aplica o renderizador a todas as colunas
        setDefaultRenderer(Object.class, centerRenderer);

        // Configuração do cabeçalho
        getTableHeader().setFont(quicksandHead);
        getTableHeader().setForeground(Color.WHITE);
        getTableHeader().setBackground(Color.DARK_GRAY);

        // Configura o renderizador centralizado para o cabeçalho
        ((DefaultTableCellRenderer)getTableHeader().getDefaultRenderer())
                .setHorizontalAlignment(JLabel.CENTER);

        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margem interna
    }
}
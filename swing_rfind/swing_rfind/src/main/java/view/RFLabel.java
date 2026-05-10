package view;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.IOException;

public class RFLabel extends JLabel{
    private Font quicksand;
    public RFLabel(String texto)
    {
        super(texto);
        setOpaque(false);
        try{
            InputStream fontePrincipal = RFLabel.class.getResourceAsStream("/Fonts/Open_Sans/OpenSans-VariableFont_wdth,wght.ttf");
            quicksand = Font.createFont(Font.TRUETYPE_FONT,fontePrincipal);
            quicksand = quicksand.deriveFont(Font.BOLD,24);
        }
        catch(Exception e){
            System.out.println("Excessão na fonte: "+e.getMessage());
        }
        setForeground(Color.WHITE);
        setFont(quicksand);
        setOpaque(false);
    }
}
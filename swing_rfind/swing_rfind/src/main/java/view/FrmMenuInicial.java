package view;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

public class FrmMenuInicial extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private final Color verydarkgray;
    //private final RFLabel lblTitulo;

    public FrmMenuInicial() throws IOException, FontFormatException {
        // Carrega a fonte personalizada
        try {
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            if (fonteLexendExa != null) {
                lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa).deriveFont(Font.PLAIN, 64);
            } else {
                lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa).deriveFont(Font.PLAIN, 64); // fallback
            }
        } catch (Exception e) {
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa).deriveFont(Font.PLAIN, 64); // fallback
        }


        // Cor de fundo
        verydarkgray = Color.decode("#20232a"); // fundo escuro

        // Configuração da janela
        setTitle("RFIND");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(verydarkgray);
        setLayout(new BorderLayout()); // centraliza o conteúdo
        setContentPane(new GradientPanel());
        getContentPane().setLayout(new BorderLayout());

        // ===== Barra de navegação no topo =====
        JPanel pnlNavBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 50));
        pnlNavBar.setBackground(verydarkgray);

        //Menu de Navegação
        RFNavBar navBar = new RFNavBar(lexendExa, verydarkgray);
        add(navBar, BorderLayout.NORTH);

        //comportamento dos botões
        navBar.getBotao("Funcionário").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new FrmFuncGerenciamento();
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        navBar.getBotao("Produto").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new FrmProdGerenciamento();
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        navBar.getBotao("Local").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new FrmLocalGerenciamento();
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        navBar.getBotao("Registros").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new FrmLocalRegistros();
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        pnlNavBar.add(navBar);

            // Adiciona o painel de navegação ao BorderLayout.NORTH
            add(pnlNavBar, BorderLayout.NORTH);

            //Painel centrar com GridBagLayout
            JPanel pnlCenter = new JPanel(new GridBagLayout());
            pnlCenter.setBackground(verydarkgray);

            // Texto central
            JLabel lblRFI = new JLabel("RFI");
            lblRFI.setFont(lexendExa.deriveFont(Font.PLAIN, 150)); // Ajuste a fonte para o tamanho desejado
            lblRFI.setForeground(Color.WHITE);

            JLabel lblN = new JLabel("N");
            lblN.setFont(lexendExa.deriveFont(Font.PLAIN, 150)); // Ajuste a fonte para o tamanho desejado
            lblN.setForeground(Color.decode("#9b1b30")); // vermelho escuro

            JLabel lblD = new JLabel("D");
            lblD.setFont(lexendExa.deriveFont(Font.PLAIN, 150)); // Ajuste a fonte para o tamanho desejado
            lblD.setForeground(Color.WHITE);

            // Adiciona as letras ao painel central
            JPanel pnlText = new JPanel();
            pnlText.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0)); // Centraliza as letras no centro
            pnlText.setOpaque(false);
            pnlText.add(lblRFI);
            pnlText.add(lblN);
            pnlText.add(lblD);

            pnlCenter.add(pnlText);

            add(pnlCenter, BorderLayout.CENTER);

            // Configurações do JFrame
            setSize(1280, 720);;
            setTitle("RFIND - Menu principal");
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       // pnlCabecalho.setOpaque(false);
        pnlCenter.setOpaque(false);
       // pnlTitulo.setOpaque(false);
        pnlText.setOpaque(false);
        pnlNavBar.setOpaque(false);
        navBar.setOpaque(false);
        navBar.getBotao("Local").setOpaque(false);
        navBar.getBotao("Registros").setOpaque(false);
        navBar.getBotao("Local").setOpaque(false);
        navBar.getBotao("Funcionário").setOpaque(false);
        navBar.getBotao("Produto").setOpaque(false);

            setVisible(true);
        }


        /*
    public static void main(String[] args){
            SwingUtilities.invokeLater(() -> {
                try {
                    new FrmMenuInicial();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (FontFormatException e) {
                    throw new RuntimeException(e);
                }
            });
    }*/



    }


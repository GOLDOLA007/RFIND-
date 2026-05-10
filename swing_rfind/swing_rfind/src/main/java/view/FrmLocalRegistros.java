package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import control.EmpresaControl;
import control.RegistroControl;
import model.Empresa;
import model.Registro;
import model.Sessao;

public class FrmLocalRegistros extends JFrame {
    private InputStream fonteLexendExa;
    private Font lexendExa;

    private EmpresaControl empresaControl = new EmpresaControl();
    private RegistroControl registroControl = new RegistroControl();

    private RFButton btnLimparRegistros;
    private RFButton btnGerarRelatorio;
    private RFButton btnRecarregar;

    private RFBarraPesquisa barraPesquisa;

    private Color darkred;
    private Color verydarkgray;

    private JPanel pnlCabecalho;
    private JScrollPane pnlCenter;
    private JPanel pnlConteudo;
    private JPanel pnlRodape;
    private JPanel pnlTitulo;

    private RFTable tblRegistros;
    private DefaultTableModel dados;

    private GridBagConstraints constraintsSecundarias;
    private GridBagConstraints gridBagConstraints;

    private Insets insetsCabecalho;
    private Insets insetsRodape;

    private RFLabel lblTitulo;

    private JPanel pnlNavBar;
    private JPanel pnlTopo;

    private Empresa empresa;
    private List<Registro> listaRegistros = new ArrayList<>();

    public FrmLocalRegistros() throws JsonProcessingException {
        try {
            empresa = empresaControl.findCnpj(relembraSessao().getCnpj());
            if (empresa != null) {
                listaRegistros = registroControl.buscarRegistros(empresa.getId());
            } else {
                listaRegistros = new ArrayList<>();
                RFMessageDialog.showMessageDialog(null, "Empresa não encontrada.","Erro");
            }
        } catch (Exception e) {
            e.printStackTrace();
            listaRegistros = new ArrayList<>();
            RFMessageDialog.showMessageDialog(null, "Erro ao carregar registros: " + e.getMessage(),"Erro");
        }

        try {
            fonteLexendExa = FrmBoasVindas.class.getResourceAsStream("/Fonts/Lexend_Exa/static/LexendExa-Regular.ttf");
            lexendExa = Font.createFont(Font.TRUETYPE_FONT, fonteLexendExa).deriveFont(Font.PLAIN, 36);
        } catch (Exception e) {
            e.printStackTrace();
        }

        darkred = new Color(155, 27, 48);
        verydarkgray = new Color(32, 35, 42);

        lblTitulo = new RFLabel("GERENCIAMENTO DE REGISTROS");
        lblTitulo.setFont(lexendExa.deriveFont(Font.BOLD, 20));

        pnlTitulo = new JPanel();
        pnlTitulo.setBackground(verydarkgray);
        pnlTitulo.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnlTitulo.add(lblTitulo);

        pnlNavBar = new JPanel();
        pnlTopo = new JPanel();

        pnlCabecalho = new JPanel();
        pnlRodape = new JPanel();
        pnlRodape.setBackground(verydarkgray);
        pnlRodape.setLayout(new GridBagLayout());
        pnlRodape.setPreferredSize(new Dimension(1280, 120));
        pnlRodape.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        pnlCenter = new JScrollPane();
        pnlCenter.setBackground(verydarkgray);
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlConteudo = new JPanel();

        tblRegistros = new RFTable();
        dados = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Todas as células não serão editáveis
                return false;
            }
        };

        btnRecarregar = new RFButton("Recarregar Tabela");
        btnLimparRegistros = new RFButton("Limpar Registros");
        btnGerarRelatorio = new RFButton("Gerar Relatório");

        insetsCabecalho = new Insets(75, 0, 75, 0);
        insetsRodape = new Insets(50, 20, 50, 20);

        constraintsSecundarias = new GridBagConstraints();
        gridBagConstraints = new GridBagConstraints();
        barraPesquisa = new RFBarraPesquisa();

        pnlTopo.setLayout(new BoxLayout(pnlTopo, BoxLayout.Y_AXIS));

        pnlNavBar.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));
        pnlNavBar.setBackground(verydarkgray);

        pnlTopo.setOpaque(false);
        pnlNavBar.setOpaque(false);
        pnlTitulo.setOpaque(false);
        pnlCabecalho.setOpaque(false);
        pnlRodape.setOpaque(false);
        pnlCenter.setOpaque(false);
        pnlCenter.getViewport().setOpaque(false);
        pnlConteudo.setOpaque(false);

        // Menu de Navegação
        RFNavBar navBar = new RFNavBar(lexendExa, verydarkgray);
        //add(navBar, BorderLayout.NORTH);

        // Comportamento dos botões da navbar
        navBar.getBotao("Registros").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popup = new JPopupMenu();
                popup.setBackground(verydarkgray);

                JMenuItem opVoltar = new JMenuItem("Voltar para Locais");
                opVoltar.setBackground(verydarkgray);
                opVoltar.setForeground(Color.WHITE);
                opVoltar.setOpaque(true);

                opVoltar.addActionListener(evt -> {
                    try {
                        new FrmLocalGerenciamento();
                        dispose();
                    } catch (JsonProcessingException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                //popup.add(opVoltar);
                popup.show(navBar.getBotao("Registros"), 0, navBar.getBotao("Registros").getHeight());
            }
        });

        navBar.getBotao("Funcionário").addActionListener(e -> {
            try {
                new FrmFuncGerenciamento();
                dispose();
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        });

        navBar.getBotao("Produto").addActionListener(e -> {
            try {
                new FrmProdGerenciamento();
                dispose();
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        });

        navBar.getBotao("Local").addActionListener(e -> {
            try {
                new FrmLocalGerenciamento();
                dispose();
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        navBar.setOpaque(false);
        navBar.getBotao("Local").setOpaque(false);
        navBar.getBotao("Registros").setOpaque(false);
        navBar.getBotao("Local").setOpaque(false);
        navBar.getBotao("Funcionário").setOpaque(false);
        navBar.getBotao("Produto").setOpaque(false);

        pnlCabecalho.setBackground(verydarkgray);
        pnlCabecalho.setLayout(new GridBagLayout());
        pnlCabecalho.setPreferredSize(new Dimension(1280, 115));

        pnlCenter.getViewport().setBackground(verydarkgray);

        // Aplica o scrollbar personalizado
        JScrollBar verticalScrollBar = pnlCenter.getVerticalScrollBar();
        verticalScrollBar.setUI(new RFFinoScrollBarUI());
        verticalScrollBar.setPreferredSize(new Dimension(5, 0));

        JScrollBar horizontalScrollBar = pnlCenter.getHorizontalScrollBar();
        horizontalScrollBar.setUI(new RFFinoScrollBarUI());
        horizontalScrollBar.setPreferredSize(new Dimension(0, 5));

        pnlConteudo.setLayout(new GridBagLayout());

        setTitle("RFIND - Registros de Acesso");
        setSize(960, 640);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // === GRADIENTE ===
        setContentPane(new GradientPanel());
        getContentPane().setLayout(new BorderLayout());

        inicializaTabela();
        configurarBarraPesquisa();

        pnlNavBar.add(navBar);

        pnlTopo.add(pnlNavBar);
        pnlTopo.add(pnlTitulo);
        pnlTopo.add(pnlCabecalho);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(10, 0, 10, 0);
        pnlCabecalho.add(barraPesquisa, gridBagConstraints);

        constraintsSecundarias.insets = new Insets(15, 20, 15, 20);
        constraintsSecundarias.gridx = 0;
        constraintsSecundarias.gridy = 0;
        pnlRodape.add(btnRecarregar, constraintsSecundarias);

        constraintsSecundarias.gridx = 1;
        //pnlRodape.add(btnLimparRegistros, constraintsSecundarias);

        constraintsSecundarias.gridx = 2;
        pnlRodape.add(btnGerarRelatorio, constraintsSecundarias);

        pnlConteudo.add(pnlCenter);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(verydarkgray);
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(pnlTopo, BorderLayout.NORTH);
        mainPanel.add(pnlCenter, BorderLayout.CENTER);
        mainPanel.add(pnlRodape, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        btnRecarregar.addActionListener(e -> limparPesquisa());

        btnLimparRegistros.addActionListener(e -> {
            try {
                int empresaId = empresa.getId();

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Deseja excluir TODOS os registros desta empresa?\nEsta ação é irreversível!",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    registroControl.excluirRegistrosPorEmpresa(empresaId);
                    RFMessageDialog.showMessageDialog(this, "Registros excluídos com sucesso!", "Sucesso");
                    atualizaTabela();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                RFMessageDialog.showMessageDialog(this, "Erro ao limpar registros: " + ex.getMessage(), "Erro");
            }
        });

        btnGerarRelatorio.addActionListener(e -> {
            GerarRelatorios gerarRelatorios = new GerarRelatorios();

            // Obter os valores dos filtros da barra de pesquisa
            String textoPesquisa = barraPesquisa.getTextoPesquisa().trim();
            String filtroSelecionado = barraPesquisa.getFiltroSelecionado();

            Integer id = null;
            String funcionario = null;
            String local = null;
            String sensor = null;
            String dataEntrada = null;
            String horaEntrada = null;
            String dataSaida = null;
            String horaSaida = null;

            // Aplicar filtros baseado na seleção
            if (!textoPesquisa.isEmpty()) {
                switch (filtroSelecionado) {
                    case "ID":
                        try {
                            id = Integer.parseInt(textoPesquisa);
                        } catch (NumberFormatException ex) {
                            RFMessageDialog.showMessageDialog(this, "ID inválido!", "Erro");
                            return;
                        }
                        break;
                    case "Funcionario":
                        funcionario = textoPesquisa;
                        break;
                    case "Local":
                        local = textoPesquisa;
                        break;
                    case "Sensor":
                        sensor = textoPesquisa;
                        break;
                    case "Data de Entrada":
                        dataEntrada = textoPesquisa;
                        break;
                    case "Hora de Entrada":
                        horaEntrada = textoPesquisa;
                        break;
                    case "Data de Saída":
                        dataSaida = textoPesquisa;
                        break;
                    case "Hora de Saída":
                        horaSaida = textoPesquisa;
                        break;
                }
            }

            // Gerar relatório com filtros
            gerarRelatorios.geraRelatorio(
                    empresa.getId(), id, funcionario, local, sensor,
                    dataEntrada, horaEntrada, dataSaida, horaSaida
            );

            RFMessageDialog.showMessageDialog(this, "Relatório gerado em /Downloads!", "Sucesso!");
        });

        setVisible(true);
    }

    private void inicializaTabela() {

        // Cria o modelo de tabela não editável
        dados = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Todas as células não editáveis
            }
        };

        dados.setColumnCount(0);
        dados.addColumn("ID");
        dados.addColumn("Funcionário");
        dados.addColumn("Local");
        dados.addColumn("Sensor");
        dados.addColumn("Entrada");
        dados.addColumn("Saída");

        dados.setNumRows(0);

        for (Registro r : listaRegistros) {
            dados.addRow(new Object[]{
                    r.getFuncionario().getId(),
                    r.getFuncionario().getNome() + " " + r.getFuncionario().getSobrenome(),
                    r.getLocal().getNome(),
                    r.getLocal().getIdrfid(),
                    r.getDataEntrada() + " - " + r.getHoraEntrada(),
                    r.getDataSaida() + " - " + r.getHoraSaida()
            });
        }

        tblRegistros.setModel(dados);
        tblRegistros.repaint();
        pnlCenter.setViewportView(tblRegistros);
    }

    private void atualizaTabela() {
        try {
            listaRegistros = registroControl.buscarRegistros(empresa.getId());
            dados.setNumRows(0);

            for (Registro r : listaRegistros) {
                dados.addRow(new Object[]{
                        r.getFuncionario().getId(),
                        r.getFuncionario().getNome() + " " + r.getFuncionario().getSobrenome(),
                        r.getLocal().getNome(),
                        r.getLocal().getIdrfid(),
                        r.getDataEntrada() + " - " + r.getHoraEntrada(),
                        r.getDataSaida() + " - " + r.getHoraSaida()
                });
            }
            tblRegistros.setModel(dados);
        } catch (Exception e) {
            e.printStackTrace();
            RFMessageDialog.showMessageDialog(this, "Erro ao atualizar registros: " + e.getMessage(), "Erro");
        }
    }

    private Sessao relembraSessao() {
        List<Sessao> sessao = Sessao.getSessao();
        return sessao.getFirst();
    }

    private void pesquisarDinamicamente() {
        String texto = barraPesquisa.getTextoPesquisa().trim();
        if (texto.isEmpty()) {
            atualizaTabela();
            return;
        }

        String filtro = barraPesquisa.getFiltroSelecionado();
        Integer id = null;
        String funcionario = null,
                local = null,
                sensor = null,
                dataEntrada = null,
                horaEntrada = null,
                dataSaida = null,
                horaSaida = null;

        try {
            int empresaId = relembraSessao().getId();

            switch (filtro) {
                case "ID":
                    try {
                        id = Integer.parseInt(texto);
                    } catch (NumberFormatException e) {
                        return;
                    }
                    break;
                case "Funcionario":
                    funcionario = texto;
                    break;
                case "Local":
                    local = texto;
                    break;
                case "Sensor":
                    sensor = texto;
                    break;
                case "Data de Entrada":
                    dataEntrada = texto;
                    break;
                case "Hora de Entrada":
                    horaEntrada = texto.contains(":") ? texto : texto + ":";
                    break;
                case "Data de Saída":
                    dataSaida = texto;
                    break;
                case "Hora de Saída":
                    horaSaida = texto.contains(":") ? texto : texto + ":";
                    break;
            }

            List<Registro> resultados = registroControl.buscarRegistrosComFiltros(
                    id, funcionario, local, sensor,
                    dataEntrada, horaEntrada, dataSaida, horaSaida, empresaId);

            atualizarTabelaResultados(resultados);

        } catch (Exception ex) {
            ex.printStackTrace();
            RFMessageDialog.showMessageDialog(null, "Erro ao pesquisar: " + ex.getMessage(), "Erro");
        }
    }

    private void atualizarTabelaResultados(List<Registro> resultados) {
        dados.setRowCount(0);

        for (Registro p : resultados) {
            String entrada = p.getDataEntrada() != null && p.getHoraEntrada() != null ?
                    p.getDataEntrada() + " - " + p.getHoraEntrada().substring(0, 5) : "";

            String saida = p.getDataSaida() != null && p.getHoraSaida() != null ?
                    p.getDataSaida() + " - " + p.getHoraSaida().substring(0, 5) : "";

            dados.addRow(new Object[]{
                    p.getFuncionario().getId(),
                    p.getFuncionario().getNome() + " " + p.getFuncionario().getSobrenome(),
                    p.getLocal().getNome(),
                    p.getLocal().getIdrfid(),
                    entrada,
                    saida
            });
        }
    }

    private void configurarBarraPesquisa() {
        String[] filtros = {
                "ID",
                "Funcionario",
                "Local",
                "Sensor",
                "Data de Entrada",
                "Data de Saída",
                "Hora de Entrada",
                "Hora de Saída"
        };

        barraPesquisa.setFiltros(filtros);

        barraPesquisa.addPesquisaListener((texto, filtro) -> {
            pesquisarDinamicamente();
        });

        barraPesquisa.getBotaoPesquisar().addActionListener(e -> {
            pesquisarDinamicamente();
        });
    }

    private void limparPesquisa() {
        barraPesquisa.limparPesquisa();
        atualizaTabela();
    }
}
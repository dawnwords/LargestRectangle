package gui;

import core.MyRectangle;
import core.SwarmThread;
import net.sourceforge.jswarm_pso.Swarm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;

public class MainFrame extends JFrame implements SwarmDisplay {

    private Mode currentMode;
    private ShopEditor shopEditor;
    private FunctionPanel functionPanel;
    private Path2D.Double shopPath;
    private String fontChoice;
    private double titleRatio;
    private boolean isStart;
    private Thread algorithm;

    public MainFrame() {
        super("PSO Test");
        currentMode = Mode.NONE;
        isStart = false;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0, 0, 800, 600);
        setResizable(false);
        setLayout(new BorderLayout());
        init();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MainFrame();
    }

    private void init() {
        shopEditor = new ShopEditor();
        functionPanel = new FunctionPanel();
        getContentPane().add(shopEditor, BorderLayout.CENTER);
        getContentPane().add(functionPanel, BorderLayout.SOUTH);
    }

    @Override
    public void showEvolveResult(MyRectangle rectangle) {
        shopEditor.showEvolveResult(rectangle);
    }

    @Override
    public void endEvolve() {
        functionPanel.stopAlgorithm();
        shopEditor.showResult();
    }

    @Override
    public Path2D.Double getPath() {
        return shopPath;
    }

    @Override
    public double getTitleRatio() {
        return titleRatio;
    }

    @Override
    public int particleNum() {
        return Swarm.DEFAULT_NUMBER_OF_PARTICLES * 100;
    }

    @Override
    public int iterationTimes() {
        return 10000;
    }

    @Override
    public double[] maxVelocity() {
        return new double[]{1, 1, 1, 1};
    }

    @Override
    public double[] minVelocity() {
        return new double[]{0.5, 0.5, 0.5, 0.01};
    }


    enum Mode {
        NONE, MOVE, LINE
    }

    private class ShopEditor extends JPanel {
        private MyRectangle rectangle;
        private boolean showResult;

        ShopEditor() {
            shopPath = new Path2D.Double();
            showResult = false;
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX(), y = e.getY();

                    System.out.printf("click (%d,%d)\n", x, y);
                    switch (currentMode) {
                        case MOVE:
                            shopPath.moveTo(x, y);
                            currentMode = Mode.LINE;
                            break;
                        case LINE:
                            shopPath.lineTo(x, y);
                            break;
                    }
                    repaint();
                }
            });
        }

        public void showEvolveResult(MyRectangle rectangle) {
            if (!rectangle.equals(this.rectangle)) {
                this.rectangle = rectangle;
                repaint();
            }
        }

        public void showResult() {
            showResult = true;
            repaint();
        }

        public void reset() {
            rectangle = null;
            showResult = false;
            repaint();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            if (shopPath == null) {
                g2.clearRect(0, 0, getWidth(), getHeight());
                shopPath = new Path2D.Double();
            }

            Font old = g2.getFont();
            if (showResult) {
                g2.setPaint(Color.RED);
                g2.rotate(rectangle.r, rectangle.x, rectangle.y);

                Font font = getFontByHeight(g2, (int) rectangle.h);
                g2.setFont(font);
                FontMetrics fm = g2.getFontMetrics(font);
                g2.drawString(functionPanel.getShopTitle(), (float) rectangle.x, (float) rectangle.y - fm.getDescent());
                g2.rotate(-rectangle.r, rectangle.x, rectangle.y);
            }
            if (rectangle != null) {
                rectangle.paint(g2);
            }

            g2.setFont(old);
            g2.setPaint(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.draw(shopPath);
        }

        private Font getFontByHeight(Graphics2D g2, int h) {
            Font font;
            int height = h;
            do {
                font = new Font(fontChoice, Font.PLAIN, height--);
            } while (g2.getFontMetrics(font).getHeight() > h);
            return font;
        }
    }

    private class FunctionPanel extends JPanel implements ActionListener, ItemListener {
        JTextField shopTitle;
        JButton drawLine, reset, startAlgorithm;
        JComboBox<String> fonts;

        FunctionPanel() {
            super(new BorderLayout());
            shopTitle = new JTextField(20);
            drawLine = new JButton("Line");
            reset = new JButton("Reset");
            startAlgorithm = new JButton("Start");
            fonts = new JComboBox<String>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());

            init();
            setLayout();
        }

        private void setLayout() {
            JPanel west = new JPanel(new GridLayout(2, 1));
            west.add(new JLabel("Shop Title:"));
            west.add(new JLabel("Font Family:"));

            JPanel center = new JPanel(new GridLayout(2, 1));
            center.add(shopTitle);
            center.add(fonts);

            JPanel east = new JPanel(new GridLayout(1, 3));
            east.add(drawLine);
            east.add(reset);
            east.add(startAlgorithm);

            add(west, BorderLayout.WEST);
            add(center, BorderLayout.CENTER);
            add(east, BorderLayout.EAST);
        }

        void init() {
            fonts.setSelectedItem(fontChoice);
            fonts.setMaximumRowCount(10);
            fonts.addItemListener(this);

            drawLine.addActionListener(this);
            reset.addActionListener(this);
            startAlgorithm.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == drawLine) {
                switch (currentMode) {
                    case LINE:
                        shopPath.closePath();
                    case MOVE:
                        currentMode = Mode.NONE;
                        drawLine.setText("Line");
                        break;
                    case NONE:
                        currentMode = Mode.MOVE;
                        drawLine.setText("Close");
                        break;
                }
                shopEditor.repaint();
            } else if (src == reset) {
                shopPath = null;
                currentMode = Mode.NONE;
                drawLine.setText("Line");
                shopEditor.reset();
            } else if (src == startAlgorithm) {
                if (isStart) {
                    stopAlgorithm();
                } else {
                    String title = getShopTitle();
                    if (title == null || "".equals(title)) {
                        JOptionPane.showMessageDialog(null, "Please Input Shop Title!");
                    } else {
                        Font font = new Font(fontChoice, Font.PLAIN, 45);
                        Graphics2D g = (Graphics2D) shopEditor.getGraphics();
                        FontMetrics fm = g.getFontMetrics(font);
                        titleRatio = fm.stringWidth(title) / (double) fm.getHeight();
                        startAlgorithm();
                    }
                }
            }
        }

        public String getShopTitle() {
            return shopTitle.getText();
        }

        public void stopAlgorithm() {
            shopEditor.setEnabled(true);
            if (algorithm.isAlive()) {
                algorithm.interrupt();
                algorithm = null;
            }
            startAlgorithm.setText("Start");
        }

        public void startAlgorithm() {
            shopTitle.setEditable(false);
            if (algorithm == null) {
                algorithm = new SwarmThread(MainFrame.this);
            }
            algorithm.start();
            startAlgorithm.setText("Stop");
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }
            fontChoice = (String) fonts.getSelectedItem();
        }
    }
}

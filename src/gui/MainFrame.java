package gui;

import core.*;
import net.sourceforge.jswarm_pso.Swarm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;

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
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0, 0, 600, 400);
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
        shopEditor.setPreferredSize(new Dimension(400, 400));
        functionPanel = new FunctionPanel();
        functionPanel.setPreferredSize(new Dimension(200, 400));
        getContentPane().add(shopEditor, BorderLayout.CENTER);
        getContentPane().add(functionPanel, BorderLayout.EAST);
    }

    @Override
    public void showEvolveResult(MyRectangle rectangle) {
        shopEditor.showEvolveResult(rectangle);
    }

    @Override
    public void endEvolve(MyRectangle best) {
        System.out.println(best.w * best.h);
        functionPanel.stopAlgorithm();
        shopEditor.repaint();
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
        return functionPanel.particleNum();
    }

    @Override
    public int iterationTimes() {
        return functionPanel.iterationTimes();
    }

    @Override
    public double[] maxVelocity() {
        return functionPanel.maxVelocity();
    }

    @Override
    public double[] minVelocity() {
        return functionPanel.minVelocity();
    }

    @Override
    public int neighborNum() {
        return functionPanel.neighborNum();
    }

    @Override
    public double inertia() {
        return functionPanel.inertia();
    }

    @Override
    public double particleIncrement() {
        return functionPanel.particleIncrement();
    }

    @Override
    public double globalIncrement() {
        return functionPanel.globalIncrement();
    }

    enum Mode {
        NONE, MOVE, LINE
    }

    private class ShopEditor extends JPanel {
        private MyRectangle rectangle;

        ShopEditor() {
            shopPath = new Path2D.Double();
            addMouseListener(new MouseAdapter() {
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

        public void reset() {
            rectangle = null;
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
            g2.setPaint(Color.BLACK);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Font old = g2.getFont();
            if (rectangle != null) {
                rectangle.paint(g2);
                drawShopTitle(g2);
            }

            g2.setFont(old);
            g2.setStroke(new BasicStroke(1));
            g2.draw(shopPath);
        }

        private void drawShopTitle(Graphics2D g2) {
            double x = rectangle.x;
            double y = rectangle.y;
            double r = rectangle.r;
            double h = rectangle.h;
            double w = rectangle.w;
            if (r > Math.PI / 2 || r < -Math.PI / 2) {
                x += h * Math.sin(r) + w * Math.cos(r);
                y += w * Math.sin(r) - h * Math.cos(r);
                r = r > Math.PI / 2 ? r - Math.PI : r + Math.PI;
            }
            g2.rotate(r, x, y);
            Font font = getFontByHeight(g2, (int) h);
            g2.setFont(font);
            FontMetrics fm = g2.getFontMetrics(font);
            g2.drawString(functionPanel.getShopTitle(), (float) x, (float) y - fm.getDescent());
            g2.rotate(-r, x, y);
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
        static final int SHOP_TITLE = 0;
        static final int PARTICLE_NUM = 1;
        static final int ITERATION_TIME = 2;
        static final int NEIGHBOR_NUM = 3;
        static final int INERTIA = 4;
        static final int PARTICLE_INC = 5;
        static final int GLOBAL_INC = 6;
        static final int MAX_X_V = 7;
        static final int MAX_Y_V = 8;
        static final int MAX_H_V = 9;
        static final int MAX_A_V = 10;
        static final int MIN_X_V = 11;
        static final int MIN_Y_V = 12;
        static final int MIN_H_V = 13;
        static final int MIN_A_V = 14;
        ArrayList<JTextField> textFields;
        JButton drawLine, reset, startAlgorithm;
        JLabel ratio;
        JComboBox<String> fonts;

        FunctionPanel() {
            super(new BorderLayout());
            textFields = new ArrayList<JTextField>();
            for (int i = 0; i < 15; i++) {
                textFields.add(new JTextField());
            }
            ratio = new JLabel();
            drawLine = new JButton("Line");
            reset = new JButton("Reset");
            startAlgorithm = new JButton("Start");
            fonts = new JComboBox<String>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());

            init();
            setLayout();
        }

        void init() {
            fonts.setSelectedItem(fontChoice);
            fonts.setMaximumRowCount(10);
            fonts.addItemListener(this);

            drawLine.addActionListener(this);
            reset.addActionListener(this);
            startAlgorithm.addActionListener(this);
        }

        private void setLayout() {
            JPanel top = new JPanel(new GridLayout(9, 2, 0, 2));
            JPanel center = new JPanel(new GridLayout(5, 3, 0, 2));
            JPanel bottom = new JPanel(new GridLayout(1, 3, 0, 2));

            top.add(new JLabel("Shop Title:"));
            top.add(textFields.get(SHOP_TITLE));
            top.add(new JLabel("Font Family:"));
            top.add(fonts);
            top.add(new JLabel("W/H Ratio:"));
            top.add(ratio);
            top.add(new JLabel("Particle Num:"));
            top.add(textFields.get(PARTICLE_NUM));
            top.add(new JLabel("Iteration Num:"));
            top.add(textFields.get(ITERATION_TIME));
            top.add(new JLabel("Neighbor Num:"));
            top.add(textFields.get(NEIGHBOR_NUM));
            top.add(new JLabel("Inertia:"));
            top.add(textFields.get(INERTIA));
            top.add(new JLabel("Particle Inc:"));
            top.add(textFields.get(PARTICLE_INC));
            top.add(new JLabel("Global Inc:"));
            top.add(textFields.get(GLOBAL_INC));

            center.add(new JLabel());
            center.add(new JLabel("Max"));
            center.add(new JLabel("Min"));
            center.add(new JLabel("X Velocity:"));
            center.add(textFields.get(MAX_X_V));
            center.add(textFields.get(MIN_X_V));
            center.add(new JLabel("Y Velocity:"));
            center.add(textFields.get(MAX_Y_V));
            center.add(textFields.get(MIN_Y_V));
            center.add(new JLabel("H Velocity:"));
            center.add(textFields.get(MAX_H_V));
            center.add(textFields.get(MIN_H_V));
            center.add(new JLabel("A Velocity:"));
            center.add(textFields.get(MAX_A_V));
            center.add(textFields.get(MIN_A_V));

            bottom.add(drawLine);
            bottom.add(reset);
            bottom.add(startAlgorithm);

            JPanel container = new JPanel(new BorderLayout());
            container.add(center, BorderLayout.NORTH);

            add(top, BorderLayout.NORTH);
            add(container, BorderLayout.CENTER);
            add(bottom, BorderLayout.SOUTH);
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
                    Font font = new Font(fontChoice, Font.PLAIN, 45);
                    Graphics2D g = (Graphics2D) shopEditor.getGraphics();
                    FontMetrics fm = g.getFontMetrics(font);
                    titleRatio = fm.stringWidth(title) / (double) fm.getHeight();
                    ratio.setText("" + titleRatio);
                    startAlgorithm();
                }
            }
        }

        public void stopAlgorithm() {
            for (JTextField textField : textFields) {
                textField.setEnabled(true);
            }
            if (algorithm.isAlive()) {
                algorithm.interrupt();
                algorithm = null;
            }
            startAlgorithm.setText("Start");
        }

        public void startAlgorithm() {
            for (JTextField textField : textFields) {
                textField.setEnabled(false);
            }
            if (algorithm == null) {
                algorithm = new SwarmThread(MainFrame.this);
            }
            if (!algorithm.isAlive()) {
                algorithm.start();
            }
            startAlgorithm.setText("Stop");
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }
            fontChoice = (String) fonts.getSelectedItem();
        }

        public String getShopTitle() {
            return textFields.get(SHOP_TITLE).getText();
        }

        public int particleNum() {
            return getIntValue(PARTICLE_NUM, 2500);
        }

        public int iterationTimes() {
            return getIntValue(ITERATION_TIME, 1000);
        }

        public int neighborNum() {
            return getIntValue(NEIGHBOR_NUM, 0);
        }

        public double inertia() {
            return getDoubleValue(INERTIA, Swarm.DEFAULT_INERTIA);
        }

        public double particleIncrement() {
            return getDoubleValue(PARTICLE_INC, Swarm.DEFAULT_PARTICLE_INCREMENT);
        }

        public double globalIncrement() {
            return getDoubleValue(GLOBAL_INC, Swarm.DEFAULT_GLOBAL_INCREMENT);
        }

        public double[] maxVelocity() {
            double[] result = new double[MyParticle.DIMENSION];
            result[MyFitnessFunction.X] = getDoubleValue(MAX_X_V, 1);
            result[MyFitnessFunction.Y] = getDoubleValue(MAX_Y_V, 1);
            result[MyFitnessFunction.H] = getDoubleValue(MAX_H_V, 1);
            result[MyFitnessFunction.A] = getDoubleValue(MAX_A_V, 0.1);
            return result;
        }

        public double[] minVelocity() {
            double[] result = new double[MyParticle.DIMENSION];
            result[MyFitnessFunction.X] = getDoubleValue(MIN_X_V, 0.1);
            result[MyFitnessFunction.Y] = getDoubleValue(MIN_Y_V, 0.1);
            result[MyFitnessFunction.H] = getDoubleValue(MIN_H_V, 0.1);
            result[MyFitnessFunction.A] = getDoubleValue(MIN_A_V, 0.01);
            return result;
        }

        private int getIntValue(int id, int def) {
            int result;
            try {
                result = Integer.parseInt(textFields.get(id).getText());
            } catch (Exception e) {
                result = def;
            }
            return result;
        }

        private double getDoubleValue(int id, double def) {
            double result;
            try {
                result = Double.parseDouble(textFields.get(id).getText());
            } catch (Exception e) {
                result = def;
            }
            return result;
        }
    }
}

package terminal;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

class Kbd extends javax.swing.JPanel implements java.awt.event.MouseListener {        

    // Individual keyboard rows
    private final String row_1[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "+", "fill", "BackSpace" };
    private final String row_2[] = { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\" };
    private final String row_3[] = { "A", "S", "D", "F", "G", "H", "J", "K", "L", ":", "\"", "fill", "fill", "Enter" };
    private final String row_4[] = { "Z",        "X",    "C",    "V",    "B",    "N",    "M", ",", ".",     "?", "blank", "^" };
    private final String row_5[] = { "blank", "blank", "fill", "fill", "fill", "fill", "fill", "fill",  "", "blank",     "<", "v", ">" };

    // Jbuttons corresponding to each individual rows
    private final JButton[] line_1;
    private final JButton[] line_2;
    private final JButton[] line_3;
    private final JButton[] line_4;
    private final JButton[] line_5;

    public static void main(String[] args) {
        Kbd a = new Kbd();
        a.setVisible(true);
    }

    Kbd() {
        JPanel contentPane = new JPanel();
//        {
//            @Override
//            protected void paintComponent(Graphics grphcs) {
//                Graphics2D g2d = (Graphics2D) grphcs;
//                Dimension d = this.getSize();
//                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//                GradientPaint gp = new GradientPaint(0, 0, getBackground().brighter().brighter(), 0, d.height, getBackground().darker().darker());
//
//                g2d.setPaint(gp);
//                g2d.fillRect(0, 0, d.width, d.height);
//
//                super.paintComponent(grphcs);
//            }
//        }
                ;
        contentPane.setOpaque(false);
//        setContentPane(contentPane);
        
        setLayout(new BorderLayout());// set the layout and place component in place and pack it

        // Various panel for the layout
        JPanel jpNorth = new JPanel();
        JPanel jpCenter = new JPanel();
        jpCenter.setPreferredSize(new Dimension(10, 10));
        JPanel jpKeyboard = new JPanel(new GridBagLayout());
        JPanel jpNote = new JPanel();

        add(jpNorth, BorderLayout.NORTH);
        add(jpNote);

        add(jpCenter, BorderLayout.CENTER);
        add(jpKeyboard, BorderLayout.SOUTH);

        jpNorth.setLayout(new BorderLayout());
        jpCenter.setLayout(new BorderLayout());
        jpCenter.setPreferredSize(new Dimension(10, 10));

        line_1 = new JButton[row_1.length];
        line_2 = new JButton[row_2.length];
        line_3 = new JButton[row_3.length];
        line_4 = new JButton[row_4.length];
        line_5 = new JButton[row_5.length];

        addKeys(jpKeyboard, 0, row_1, line_1);
        addKeys(jpKeyboard, 1, row_2, line_2);
        addKeys(jpKeyboard, 2, row_3, line_3);
        addKeys(jpKeyboard, 3, row_4, line_4);
        addKeys(jpKeyboard, 4, row_5, line_5);

        jpKeyboard.setPreferredSize(new Dimension(640, 160));

        jpNote.setOpaque(false);
        jpNorth.setOpaque(false);
        jpCenter.setOpaque(false);
        jpKeyboard.setOpaque(false);

        // add listeners to all the button
        for(JButton b : line_1)  { if (b != null) { b.addMouseListener(this); } }
        
        for(JButton b : line_2) { if (b != null) { b.addMouseListener(this); } }
        
        for(JButton b : line_3)  { if (b != null) { b.addMouseListener(this); } }
         
        for(JButton b : line_4) { if (b != null) { b.addMouseListener(this); } }
         
        for(JButton b : line_5)  { if (b != null) { b.addMouseListener(this); } }

//        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//        
//        setResizable(false);// set non re-sizable
//        
//        getContentPane().setPreferredSize(new Dimension(640, 160));// set size of the content pane ie frame
//
//        initWidgets();
//        pack();
//        toFront(); // brings this Window to the front and may make it the focused Window
//        setLocationRelativeTo(null); // Window appears center
    }

    
//    private void initWidgets() {// Method to initialize frame component
//    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        Object o = arg0.getSource();
        JButton b = null;
        if (o instanceof JButton) {
            b = (JButton) o;
        }
        if (b != null) {
            System.out.println(b.getText());
            keyPress();
        }
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }    
    
    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    private void addKeys(JPanel parent, int row, String[] keys, JButton[] buttons) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;

        int gap = 0;
        for (int index = 0; index < keys.length; index++) {
            String key = keys[index];
            if ("blank".equalsIgnoreCase(key)) {
                gbc.gridx++;
            } else if ("fill".equalsIgnoreCase(key)) {
                gbc.gridwidth++;
                gap++;
            } else {
                // System.out.println("Add " + key);
                JButton btn = new JButton(key);
                buttons[index] = btn;
                parent.add(btn, gbc);
                gbc.gridx += gap + 1;
                gbc.gridwidth = 1;
                gap = 0;

//                System.out.println(key);
                btn.setBackground(Color.WHITE);
                btn.setFocusable(false);
//                map.put(getKeyCode(key), btn);
            }
        }
    }
    
    private void keyPress() {
        try {
            Robot robot = new Robot();

            // Simulate a key press
            robot.keyPress(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_A);

        } catch (AWTException e) {
            Err.err(e);
        }
    }
        
}// end of class
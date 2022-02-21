package terminal;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import ru.attrans.proc.objs.RWObject;

class Emul extends javax.swing.JInternalFrame {
    
    static final SortedMap<String, RWObject> rwObjs = new TreeMap();
    static final Map<String, SortedMap<String, Integer>> rwObjNames = new HashMap();
    static DatagramSocket transmitter;
    
    Emul(){
        initComponents();
        initController();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        objSendTiket = new javax.swing.JButton();
        objTabbedPanel = new javax.swing.JTabbedPane();
        objSendToAll = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(true);
        setTitle("Уведомления");
        setVisible(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        objSendTiket.setText("Send Ticket");
        objSendTiket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                objSendTiketActionPerformed(evt);
            }
        });

        objSendToAll.setText("Отправить всем");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(objSendToAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                .addComponent(objSendTiket))
            .addComponent(objTabbedPanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(objTabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(objSendTiket)
                    .addComponent(objSendToAll)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void initController() {
        rwObjs.entrySet().stream().filter(p ->  !p.getKey().equals("ALARM") &&
                                                !p.getKey().equals("COMMAND") &&
                                                !p.getKey().equals("NOTE") &&
                                                !p.getKey().equals("STATUS")).forEach((Map.Entry<String, RWObject> e) -> {
            try {
                JComponent comp;
                JLabel comp2;
                JPanel panel;
                objTabbedPanel.addTab(e.getKey(), panel = new JPanel());
                SpringLayout layout = new SpringLayout();
                panel.setLayout(layout);
                panel.add(new JLabel("Object Name", JLabel.TRAILING));
                panel.add(comp = new JComboBox(), 1);
                comp.setName("objName");

                if (rwObjNames.containsKey(e.getKey())) {
                    for (Map.Entry<String, Integer> e2:rwObjNames.get(e.getKey()).entrySet()) {
//                        if (TeleSCADACommSrv.dbMan.objTypeMap.get(e2.getValue()).equalsIgnoreCase(e.getKey())) {
                            ((JComboBox)comp).addItem(e2.getKey());
//                        }
                    }
                }
                panel.add(new JLabel("Object ID", JLabel.TRAILING));
                panel.add(comp2 = new JLabel());
                comp2.setName("objId");
                if (((JComboBox)comp).getItemCount() > 0) {
                    comp2.setText(rwObjNames.get(e.getKey()).get((String)((JComboBox)comp).getItemAt(0)).toString());
                }
                ((JComboBox)comp).addActionListener((ActionEvent e1) -> {
                    comp2.setText(rwObjNames.get(e.getKey()).get(((JComboBox)e1.getSource()).getSelectedItem().toString()).toString());
                });
                int i = 2;
                for (Field f: e.getValue().getClass().getFields()) {
                    if (f.getName().equalsIgnoreCase("objid") || f.getName().equalsIgnoreCase("objtype") ||  f.getName().equalsIgnoreCase("timestamp")) {
                        continue;
                    }
                    panel.add(new JLabel(f.getName(), JLabel.TRAILING));
                    if (f.getType().getTypeName().equals("boolean")) {
                        panel.add(comp = new JCheckBox());
                    } else {
                        panel.add(comp = new JFormattedTextField());
                        ((JFormattedTextField)comp).setValue(0);
                        ((JFormattedTextField)comp).setColumns(2);
                    }
                    comp.setName(f.getName());

                    i++;
                }
                //
                makeCompactGrid(panel, i, 2, 6, 6, 6, 6);
                //
            } catch (IllegalArgumentException ex) {
                Log.log(ex.getMessage());
            }
        });
    }
    
    // Послать билетик..... как будто бы индикация!
    private static void sendTicket(RWObject obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(obj);
                oos.flush();
            }
            //
            byte[] buff = bos.toByteArray();
            DatagramPacket ticket = new DatagramPacket(buff, buff.length, new InetSocketAddress(Terminal.MULTICAST_GROUP, Integer.valueOf(Terminal.MULTICAST_PORT)));
            transmitter.send(ticket);

        } catch (IOException ex) {
            Log.log(ex.getMessage() + "\n");
        }
    }
   
    /* Used by makeCompactGrid. */
    private static SpringLayout.Constraints getConstraintsForCell(
                                                int row, int col,
                                                Container parent,
                                                int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    public static void makeCompactGrid(Container parent,
                                       int rows, int cols,
                                       int initialX, int initialY,
                                       int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout)parent.getLayout();
        } catch (ClassCastException exc) {
            System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
            return;
        }

        //Align all cells in each column and make them the same width.
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                                   getConstraintsForCell(r, c, parent, cols).
                                       getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        //Align all cells in each row and make them the same height.
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                
                height = Spring.max(height,
                                    getConstraintsForCell(r, c, parent, cols).
                                        getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints =
                        getConstraintsForCell(r, c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        //Set the parent's size.
        //SpringLayout.Constraints pCons = layout.getConstraints(parent);
        //pCons.setConstraint(SpringLayout.SOUTH, y);
        //pCons.setConstraint(SpringLayout.EAST, x);
    }
    
    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
//        Close();
        setVisible(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void objSendTiketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_objSendTiketActionPerformed
        JPanel panel = (JPanel)objTabbedPanel.getSelectedComponent();
        if (panel != null) {
            //
            RWObject obj = rwObjs.get(objTabbedPanel.getTitleAt(objTabbedPanel.getSelectedIndex()));
            obj.timestamp = System.currentTimeMillis();
            Log.log(obj.getClass().getName());// + "\n");
            // Заполняем поля экземпляра класса.......
            for (Component c:panel.getComponents()) {
                if (c.getName() != null) {
                    try {
                        Log.log(c.getName() + " : ");
                        if (c instanceof JCheckBox) {
                            obj.getClass().getField(c.getName()).setBoolean(obj, ((JCheckBox)c).isSelected());
                        } else if (c instanceof JFormattedTextField) {
                            obj.getClass().getField(c.getName()).setInt(obj, Integer.valueOf(((JFormattedTextField)c).getText().replace(",", "")));
                        } else if (c instanceof JLabel) {
                            obj.getClass().getField(c.getName()).setInt(obj, Integer.valueOf(((JLabel)c).getText()));
                        } else {
                        }
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                        Log.log(ex.getMessage());// + "\n");
                    }
                }
            }
            // Отпрвляем "билетик".......
            if (!objSendToAll.isSelected()) {
                sendTicket(obj);
            } else
            // Ну.... или всем билетики...
            {
                JComboBox cb = (JComboBox)panel.getComponent(1);
                for (int i = 0; i < cb.getItemCount(); i++) {
                    obj.objId = rwObjNames.get(objTabbedPanel.getTitleAt(objTabbedPanel.getSelectedIndex())).get((String)cb.getItemAt(i));
                    sendTicket(obj);
                }
            }
        }
    }//GEN-LAST:event_objSendTiketActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton objSendTiket;
    private javax.swing.JCheckBox objSendToAll;
    private javax.swing.JTabbedPane objTabbedPanel;
    // End of variables declaration//GEN-END:variables
}

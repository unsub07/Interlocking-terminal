//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

class RailnetAxis extends javax.swing.JInternalFrame {

    private static Railnet rOFin;

    private static void setRailnet() {
        if (!Terminal.Railnets_Hash.isEmpty()) {
            //String[] s = ((String) jComboBox1.getModel().getSelectedItem()).split(":");
            rOFin = Terminal.Railnets_Hash.get(Integer.valueOf(((String) jComboBox1.getModel().getSelectedItem()).split("\\:")[0]));
            // System.out.println("s0 "+s[0]+" s1 "+ s[1]);
            fldCount.setText(String.valueOf(rOFin.axis));
        }
    }

    private static void btn_pressed(boolean plus) {
        setRailnet();
        if (plus) {
            String ID_OBJ = (String.valueOf(rOFin.id_obj)).trim();
//            System.out.println("C.CTL:" + ID_OBJ + ":" + "RAILNET_AXIS_PLUS");
            Net.Send("C.CTL:" + ID_OBJ + ":" + "RAILNET_AXIS_PLUS");
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Err.err(e);
            }
            Net.Send("C.CTL:" + ID_OBJ + ":" + "RAILNET_AXIS_PLUS0");
        } else {
            String ID_OBJ = String.valueOf(rOFin.id_obj);
//            System.out.println("C.CTL:" + ID_OBJ + ":" + "RAILNET_AXIS_MINUS");
            Net.Send("C.CTL:" + ID_OBJ + ":" + "RAILNET_AXIS_MINUS");
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Err.err(e);
            }
            Net.Send("C.CTL:" + ID_OBJ + ":" + "RAILNET_AXIS_MINUS0");
        }
        setRailnet();
    }

    static private void Close() {
        CpuMon.toggleAxisChange.setSelected(false);
    }

    RailnetAxis() {
        initComponents();
        fldAxisToSet.setEditable(false);
        fldAxisToSet.setEnabled(false);
        fldAxisToSet.setVisible(false);
        btnSetAxisNum.setEnabled(false);
        btnSetAxisNum.setVisible(false);
        setCombo();
        // btnPlus.setEnabled(false);
        // btnMinus.setEnabled(false);
        // //rn = (RailnetNameCell)
        // (Terminal.mainCellsHash.get(getTwin()));
        // rn_Thread(true);
    }
    
    private void setCombo(){//заполнить комбо бокс
        Railnet rO;
        DefaultComboBoxModel <String> comboRN = new DefaultComboBoxModel<>();
        comboRN.removeAllElements();//очищаем комбо бокс
        for (int id_obj : Terminal.Railnets_Hash.keySet()) {
            rO = Terminal.Railnets_Hash.get(id_obj);
            if (сheckBox_All_RN.isSelected()){
                if (rO.vStatus == 0){
                    comboRN.addElement(rO.id_obj + ": " + rO.s_name);
                }
                
            } else {
                if (rO.vStatus == 0){
                    if(rO.type==9 || rO.type==6){//Пути и тупики
                        comboRN.addElement(rO.id_obj + ": " + rO.s_name);
                    }//end if
                }
            }//end else
        }//end for
        jComboBox1.setModel(comboRN);
        if (jComboBox1.getItemCount() > 0) {//надо проверить чтобы небыло пусто!
            jComboBox1.setSelectedIndex(0);
        }        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<String>();
        btnPlus = new javax.swing.JButton();
        btnMinus = new javax.swing.JButton();
        fldAxisToSet = new javax.swing.JTextField();
        btnSetAxisNum = new javax.swing.JButton();
        fldCount = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        сheckBox_All_RN = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(true);
        setTitle("Корректировка колличества осей на контролируемых участках");
        setToolTipText("");
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

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        btnPlus.setText("+");
        btnPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlusActionPerformed(evt);
            }
        });

        btnMinus.setText("-");
        btnMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinusActionPerformed(evt);
            }
        });

        fldAxisToSet.setText("0");
        fldAxisToSet.setToolTipText("Установить заданное количество");

        btnSetAxisNum.setText("установить");
        btnSetAxisNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetAxisNumActionPerformed(evt);
            }
        });

        fldCount.setEditable(false);
        fldCount.setText("0");

        jLabel2.setText("Осей:");

        сheckBox_All_RN.setText("Все участки");
        сheckBox_All_RN.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                сheckBox_All_RNItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fldAxisToSet)
                    .addComponent(fldCount, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSetAxisNum)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(сheckBox_All_RN))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnPlus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMinus, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMinus)
                    .addComponent(btnPlus)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fldCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fldAxisToSet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSetAxisNum))
                        .addGap(0, 53, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(сheckBox_All_RN))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        Close();
        setVisible(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        setRailnet();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void btnPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlusActionPerformed
        btn_pressed(true);
    }//GEN-LAST:event_btnPlusActionPerformed

    private void btnMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinusActionPerformed
        btn_pressed(false);
    }//GEN-LAST:event_btnMinusActionPerformed

    private void btnSetAxisNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetAxisNumActionPerformed
        btnSetAxisNum.setEnabled(false);
        btnPlus.setEnabled(false);
        btnMinus.setEnabled(false);
        boolean plusic;
        int axis_nuno = 0;
        String arg = fldAxisToSet.getText();
        for (char c : arg.toCharArray()) {
            if (!Character.isDigit(c)) {
                JOptionPane.showMessageDialog(null, "Это не число");
                return;
                // break;
            }
            axis_nuno = Integer.valueOf(arg);
        }

        if (axis_nuno < 0) {
            JOptionPane.showMessageDialog(null, "Установите положительное число осей.");
            fldAxisToSet.setText("0");
            return;
        }
        if (axis_nuno > 999) {
            JOptionPane.showMessageDialog(null, "Слишком большое число осей.");
            fldAxisToSet.setText("0");
            return;
        }

        // System.out.println("осей нужно " + axis_nuno);
        int axis_est = Integer.valueOf(fldCount.getText());
        // System.out.println("осей сейчас " + axis_est);
        int cicle;

        if (axis_est < axis_nuno) {
            plusic = true;
            cicle = axis_nuno - axis_est;
            // System.out.println("добавлять " + cicle);
        } else {
            plusic = false;
            cicle = axis_est - axis_nuno;
            // System.out.println("убавлять " + cicle);
        }

        for (int j = 0; j < cicle; j++) {
            // System.out.println("меняем " + plusic +" "+ j);
            btn_pressed(plusic);
        }

        btnSetAxisNum.setEnabled(true);
        btnPlus.setEnabled(true);
        btnMinus.setEnabled(true);
    }//GEN-LAST:event_btnSetAxisNumActionPerformed

    private void сheckBox_All_RNItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_сheckBox_All_RNItemStateChanged
        setCombo();
    }//GEN-LAST:event_сheckBox_All_RNItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnMinus;
    public static javax.swing.JButton btnPlus;
    private javax.swing.JButton btnSetAxisNum;
    private javax.swing.JTextField fldAxisToSet;
    private static javax.swing.JTextField fldCount;
    private static javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JCheckBox сheckBox_All_RN;
    // End of variables declaration//GEN-END:variables
}

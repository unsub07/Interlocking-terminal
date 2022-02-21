//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.awt.*;

class CustomDialog2 extends modalInternalFrame {

//    private static final long serialVersionUID = 1L;

    private Integer ret = 0;

    CustomDialog2() {
        initComponents();
        
        jLabel1.setIcon(Terminal.mainPictureHash.get("w64"));
        
        btnYes.setIcon(Terminal.mainPictureHash.get("102"));
        btnNo.setIcon(Terminal.mainPictureHash.get("101"));
        btnYes.setFont(Terminal.SANS18P);
        btnNo.setFont(Terminal.SANS18P);
        lblMessage.setFont(Terminal.SANS18P);
        // -------------------------------Translate--------------------------------------
        String yes = "Да";
        String no = "Нет";

        // ------------------------------------------------------------------------------
        btnYes.setText(yes);
        btnNo.setText(no);

        // getRootPane().setDefaultButton(btnNo);// 'btnNo' will be your start
        // button - эта штука всё гадит!
        btnYes.setMnemonic(java.awt.event.KeyEvent.VK_F10);
        btnNo.setMnemonic(java.awt.event.KeyEvent.VK_N);

        btnNo.requestFocusInWindow();// This button will have the initial focus.
        btnNo.requestFocus();// установть фокус

        btnNo.setSelected(true);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnNo = new javax.swing.JButton();
        btnYes = new javax.swing.JButton();
        lblMessage = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 3)));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Внимане! Ответственная операция.");
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
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

        btnNo.setText("Нет");
        btnNo.setSelected(true);
        btnNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoActionPerformed(evt);
            }
        });

        btnYes.setText("Да");
        btnYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnYesActionPerformed(evt);
            }
        });

        lblMessage.setText("message");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 304, Short.MAX_VALUE)
                        .addComponent(btnYes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNo)
                        .addGap(6, 6, 6))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 48, Short.MAX_VALUE))
                    .addComponent(lblMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnYes)
                    .addComponent(btnNo)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        ret = -1;
        Close();
        setVisible(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoActionPerformed
        ret = 0;
        dispose();
    }//GEN-LAST:event_btnNoActionPerformed

    private void btnYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnYesActionPerformed
        ret = 1;
        dispose();
    }//GEN-LAST:event_btnYesActionPerformed

    private void Close() {
        dispose();
    }

    int showOptionDialog(int cx, int cy, String title, String message) {
        int sx = cx;
        int sy = cy;
        if (sx < 0) {
            sx = 0;
        }
        if (sy < 0) {
            sy = 0;
        }

        int w = Terminal.commander.cmdScroller.getHorizontalScrollBar().getValue()
                + Terminal.commander.cmdScroller.getViewport().getSize().width;
        int wx = sx + Commander.customDialog2.getWidth();

        if (w < wx) {
            sx = w - Commander.customDialog2.getWidth();
        }

        setTitle(title);
        lblMessage.setText(message);
        setLocation(sx, sy);

        btnNo.requestFocusInWindow();// This button will have the initial focus.
        btnNo.requestFocus();// установть фокус
        setVisible(true);
        // ----------------------------------pause---------------------------------------
        // try {
        // Thread.sleep(500);
        // } catch (InterruptedException ex) {
        // ex.printStackTrace();
        // }
        // ------------------------------------------------------------------------------
        btnYes.setEnabled(true);
        return ret;
    }

// --Commented out by Inspection START (16.01.18 15:00):
//    public void showMessageDialog(String title, String message) {
//        setTitle(title);
//        lblMessage.setText(message);
//        // setLocation(x, y);
//        setVisible(true);
//        btnYes.setEnabled(true);
//        btnYes.setText("ОК");
//        btnNo.setVisible(false);
//    }
// --Commented out by Inspection STOP (16.01.18 15:00)
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNo;
    private javax.swing.JButton btnYes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblMessage;
    // End of variables declaration//GEN-END:variables
}

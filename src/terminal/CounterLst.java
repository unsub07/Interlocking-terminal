//Copyright (c) 2011, 2020, ЗАО "НПЦ "АТТРАНС"
package terminal;

class CounterLst extends javax.swing.JInternalFrame {

    static final javax.swing.table.DefaultTableModel DTM = new javax.swing.table.DefaultTableModel();
    private int beamObjId;
    private int beam2;
    private int cntLogAddress;
    private int INC_SGN; //направление движения через датчик

    CounterLst() {
        initComponents();
        tbl.setFont(Terminal.SANS14P);
        btnAdjustCounter.setVisible(Terminal.SHN);//настраивает только Механик
        btnAdjustCounter.setText("<html>Настроить<br> НСУ</html>");
        btnInit.setVisible(Terminal.SIM);//только Симулятор
        btnPlus.setVisible(Terminal.SIM);//только Симулятор
        btnMinus.setVisible(Terminal.SIM);//только Симулятор
        btnInitAll.setVisible(Terminal.SIM);//только Симулятор
        setTitle("Перечень счетных пунктов");
        tbl.getSelectionModel().addListSelectionListener((javax.swing.event.ListSelectionEvent e) -> {
            if (tbl.getSelectedRow() > -1) {
                int cnt = Integer.parseInt(String.valueOf(DTM.getValueAt(tbl.convertRowIndexToModel(tbl.getSelectedRow()), 0)));//исправлено чтобы конпка соображала правильно
                if (Terminal.CounterCell_Hash.get(cnt).adjusable) {
                    btnAdjustCounter.setEnabled(true);
                } else {
                    btnAdjustCounter.setEnabled(false);
                }
            }
        });
        tbl.getTableHeader().setReorderingAllowed(false);
        initTableCounterList();
    }

    private void initTableCounterList() {// вот эта штука выполняется каждый раз!

        while (DTM.getRowCount() > 0) {
            DTM.removeRow(0); // чистим так таблицу
        }
        DTM.setColumnCount(8);//                0      1         2           3       4       5            6        7
        DTM.setColumnIdentifiers(new Object[]{"Код", "Луч", "Адр.", "Датчик", "КУ", "Состояние", "Дин.данные", "ошибок"});
        tbl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

        tbl.getColumnModel().removeColumn(tbl.getColumnModel().getColumn(0));// код

        tbl.getColumnModel().getColumn(0).setPreferredWidth(50);// Луч СО #1
        tbl.getColumnModel().getColumn(1).setPreferredWidth(36);// Лог.адрес
        tbl.getColumnModel().getColumn(2).setPreferredWidth(54);// датчик
        tbl.getColumnModel().getColumn(3).setPreferredWidth(140);// КУ
        tbl.getColumnModel().getColumn(4).setPreferredWidth(140);// состояние
        tbl.getColumnModel().getColumn(5).setPreferredWidth(120);// длин.Данные
        tbl.getColumnModel().getColumn(6).setPreferredWidth(54);// N ош.связи

        javax.swing.table.JTableHeader header = tbl.getTableHeader();
        header.setToolTipText(title);

        tbl.setRowSorter(new javax.swing.table.TableRowSorter<>(DTM));//

        Terminal.CounterCell_Hash.keySet().forEach((id_obj) -> DTM.addRow(new Object[]{
            String.valueOf(id_obj), // id Код
            Terminal.Beams_Hash.get(Terminal.CounterCell_Hash.get(id_obj).beam_id).s_name,// beam_name
            String.valueOf(Terminal.CounterCell_Hash.get(id_obj).log_address),// counter_log_address
            Terminal.CounterCell_Hash.get(id_obj).s_name,// counter_name

            Terminal.CounterCell_Hash.get(id_obj).getRNname(Terminal.CounterCell_Hash.get(id_obj).rn1)// left_rn_name")
            + " : "
            + Terminal.CounterCell_Hash.get(id_obj).getRNname(Terminal.CounterCell_Hash.get(id_obj).rn2)// right_rn_name")
                ,
            Terminal.CounterCell_Hash.get(id_obj).counterState,// состояние
            Terminal.CounterCell_Hash.get(id_obj).dynData,     // длин.Данные
            Terminal.CounterCell_Hash.get(id_obj).commErrors   // N ош.связи
        }));
        if (tbl.getRowCount() > -1) {
            tbl.changeSelection(0, 0, true, false);
        }
        Terminal.counterTableReady = true;// FUCK FUFUFU Victor ????
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAdjustCounter = new javax.swing.JButton();
        cnScroller = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        btnInit = new javax.swing.JButton();
        btnPlus = new javax.swing.JButton();
        btnMinus = new javax.swing.JButton();
        btnInitAll = new javax.swing.JButton();

        setClosable(true);
        setResizable(true);
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

        btnAdjustCounter.setFont(Terminal.SANS14);
        btnAdjustCounter.setText("<html>Настроить<br> НСУ<html>");
        btnAdjustCounter.setToolTipText("Настоить НСУ");
        btnAdjustCounter.setFocusPainted(false);
        btnAdjustCounter.setFocusable(false);
        btnAdjustCounter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdjustCounterActionPerformed(evt);
            }
        });

        tbl.setModel(DTM);
        tbl.setFocusable(false);
        tbl.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        cnScroller.setViewportView(tbl);

        btnInit.setText("init");
        btnInit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInitActionPerformed(evt);
            }
        });

        btnPlus.setText("right");
        btnPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlusActionPerformed(evt);
            }
        });

        btnMinus.setText("left");
        btnMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinusActionPerformed(evt);
            }
        });

        btnInitAll.setText("init all");
        btnInitAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInitAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdjustCounter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnInit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPlus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMinus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInitAll, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cnScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnAdjustCounter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnInit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPlus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnMinus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnInitAll)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(cnScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdjustCounterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdjustCounterActionPerformed
//        int cntRow = Integer.parseInt(String.valueOf(DTM.getValueAt(tbl.convertRowIndexToModel(tbl.getSelectedRow()), 0)));
//        int beamObjId = Terminal.CounterCell_Hash.get(cntRow).beam_id;
//        int beam2 = Terminal.Beams_Hash.get(beamObjId).cmd2;//дополнительный объект в адрес которого будем слать
//        int cntLogAddress = Terminal.CounterCell_Hash.get(cntRow).log_address;
//Terminal.CPU - берем из DEFAULTS
        getRow();
        if (Terminal.CPU == 1) {//Тип процессора (0 - Siemens (TSAP - 102 порт), 1 - HIMA, 2 - Siemens (Fetch & Write))
            Net.sendDirectCmd_SHN(beamObjId, "255." + String.valueOf(cntLogAddress));
        } else {
            Net.sendDirectCmd_SHN(beamObjId, "255." + String.valueOf(cntLogAddress));// адрес (старший байт) 1.1
            Net.sendDirectCmd_SHN(beam2, "1.1");           // денисовка и азот      Команда сюда пишем 1.1             (младший адрес)
        }
    }//GEN-LAST:event_btnAdjustCounterActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        Terminal.counterTableReady = false;
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnInitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInitActionPerformed
        if (Terminal.SIM){
            getRow();
            Net.sendDirectCmd_SHN(beamObjId, "65535." + String.valueOf(8192 + cntLogAddress));//8000h 32768
            //кто сбрасывает биты?
        }
    }//GEN-LAST:event_btnInitActionPerformed

    private void btnPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlusActionPerformed
        if (Terminal.SIM){//+
            getRow();
            if (INC_SGN == 0){
                Net.sendDirectCmd_SHN(beamObjId, "65535." + String.valueOf(16384 + cntLogAddress));//4000h 16384
            } else {
                Net.sendDirectCmd_SHN(beamObjId, "65535." + String.valueOf(32768 + cntLogAddress));//2000h 8192
            }
            //кто сбрасывает биты?
        }
    }//GEN-LAST:event_btnPlusActionPerformed

    private void btnMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinusActionPerformed
        if (Terminal.SIM){//-
            getRow();
            if (INC_SGN == 0) {
                Net.sendDirectCmd_SHN(beamObjId, "65535." + String.valueOf(32768 + cntLogAddress));//2000h 8192
            } else {
                Net.sendDirectCmd_SHN(beamObjId, "65535." + String.valueOf(16384 + cntLogAddress));//4000h 16384
            }
            //кто сбрасывает биты?
        }
    }//GEN-LAST:event_btnMinusActionPerformed

    private void btnInitAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInitAllActionPerformed
        if (Terminal.SIM) {
            for (int r = 0; r < DTM.getRowCount(); r++) {
                tbl.setRowSelectionInterval(0, r);
                getRow();
                Net.sendDirectCmd_SHN(beamObjId, "65535." + String.valueOf(8192 + cntLogAddress));//8000h 32768
                pause(50);
            }
        }
    }//GEN-LAST:event_btnInitAllActionPerformed
    
    private void getRow(){
        int cntRow = Integer.parseInt(String.valueOf(DTM.getValueAt(tbl.convertRowIndexToModel(tbl.getSelectedRow()), 0)));
        beamObjId = Terminal.CounterCell_Hash.get(cntRow).beam_id;
        beam2 = Terminal.Beams_Hash.get(beamObjId).cmd2;//дополнительный объект в адрес которого будем слать
        cntLogAddress = Terminal.CounterCell_Hash.get(cntRow).log_address;
        INC_SGN = Terminal.CounterCell_Hash.get(cntRow).inc_sgn;
    }
    
    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Err.err(e);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdjustCounter;
    private javax.swing.JButton btnInit;
    private javax.swing.JButton btnInitAll;
    private javax.swing.JButton btnMinus;
    private javax.swing.JButton btnPlus;
    private javax.swing.JScrollPane cnScroller;
    private javax.swing.JTable tbl;
    // End of variables declaration//GEN-END:variables

}

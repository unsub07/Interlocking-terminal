package terminal;

import javax.swing.BorderFactory;
import javax.swing.event.ListSelectionEvent;
import static terminal.Terminal.Cabinets_Hash;
import static terminal.Terminal.Unit_Hash;

class UnitLst extends javax.swing.JInternalFrame {

    private final javax.swing.table.DefaultTableModel dtm = new javax.swing.table.DefaultTableModel();
    // private final int cabinet; //id_obj кабинета

    UnitLst(int CABINET) {
        initComponents();
        setTitle("Перечень модулей");
        lblForScroller.setText("<html><b>" + Cabinets_Hash.get(CABINET).getName() + ",</b> перечень модулей</html>");

        initTable();
        fillTable(CABINET);
        CpuMon.unitListActive = true;

        unitTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (unitTable.getSelectedRow() > -1) {
                int a = Integer.parseInt(String.valueOf(dtm.getValueAt(unitTable.getSelectedRow(), 0)));
                lblParams.setText(Unit_Hash.get(a).reportAllParameters());
                lblChannels.setText(Unit_Hash.get(a).reportAllChannels());
            }
        });

        if (dtm.getRowCount() >= 0) {
            unitTable.changeSelection(0, 0, true, false);
        }
    }

    private void initTable() { // вызывается как нажмем посмотреть 100 тысяч раз

        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0); // чистим так таблицу
        }
        dtm.setColumnCount(7);
        dtm.setColumnIdentifiers(new Object[]{"Код", "Полка", "Место", "Тип", "Лог.адрес", "Наименование", "Состояние"});
        unitTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        unitTable.getColumnModel().getColumn(0).setPreferredWidth(50);// код
        unitTable.getColumnModel().getColumn(1).setPreferredWidth(50);// Полка
        unitTable.getColumnModel().getColumn(2).setPreferredWidth(50);// Место
        unitTable.getColumnModel().getColumn(3).setPreferredWidth(60);// Тип
        unitTable.getColumnModel().getColumn(4).setPreferredWidth(70);// Лог адрес
        unitTable.getColumnModel().getColumn(5).setPreferredWidth(302);// Наименоваение
        // unitTable.getColumnModel().getColumn(6).setPreferredWidth(unitTable.getWidth() - 500);
        unitTable.getColumnModel().getColumn(6).setPreferredWidth(354);// Состояние
    }

    private void fillTable(int CABINET) {

        Unit_Hash.keySet().stream().filter((i) -> (CABINET == Unit_Hash.get(i).cabinet_id)).forEach((i) -> dtm.addRow(new Object[]{
            String.valueOf(Unit_Hash.get(i).id_obj),// unit_id"), //    // id Код
            String.valueOf(Unit_Hash.get(i).rack),// unit_rack"), // полка
            String.valueOf(Unit_Hash.get(i).place),// unit_place"),      // место
            //                    String.valueOf(Unit_Hash.get(i).type),// unit_type"), //// FUFUFU FUCK Vcitor - переделать в int // тип
            String.valueOf(Unit_Hash.get(i).type_name),// unit_type"), // тип
            String.valueOf(Unit_Hash.get(i).log_address),// unit_log_address"), FUFUFU FUCK Vcitor - переделать в int // лог адрес
            Unit_Hash.get(i).s_name,// unit_s_name"), // Наименование
            //                    "" //Состояние
            Unit_Hash.get(i).dot()//Состояние
        }));

    }

    void updateUnitState(
            int unitId,
            String freshParameters,
            String freshChannels,
            String freshStateInDots
    ) {
        for (int i = 0; i < dtm.getRowCount(); i++) {
            String jj = (String) dtm.getValueAt(i, 0);
//            int j = (Integer) dtm.getValueAt(i, 0);
            int j = Integer.valueOf(jj);
            if (j == unitId) {
                dtm.setValueAt(freshStateInDots, i, 6);
            }
            if (unitTable.isRowSelected(i)) {
                lblParams.setText(freshParameters);
                lblChannels.setText(freshChannels);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblForScroller = new javax.swing.JLabel();
        unitScroller = new javax.swing.JScrollPane();
        unitTable = unitTable= new javax.swing.JTable(){@Override              public boolean isCellEditable(int arg0, int arg1) {                  return false;              } };
        lblParams = new javax.swing.JLabel();
        lblChannels = new javax.swing.JLabel();

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

        lblForScroller.setText("Перечень модулей в ");

        unitTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        unitTable.setModel(dtm);
        unitTable.setName("unitTable"); // NOI18N
        unitScroller.setViewportView(unitTable);

        lblParams.setText("12345");
        lblParams.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lblChannels.setBackground(new java.awt.Color(204, 204, 204));
        lblChannels.setText("12345");
        lblChannels.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblChannels.setName("lblChannels"); // NOI18N

        lblChannels.setBorder(BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblParams, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblChannels, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblForScroller)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(unitScroller))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblForScroller)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unitScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblParams, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                    .addComponent(lblChannels, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        lblParams.setBorder(BorderFactory.createEtchedBorder());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        CpuMon.unitListActive = false;
        dispose();
    }//GEN-LAST:event_formInternalFrameClosing
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblChannels;
    private javax.swing.JLabel lblForScroller;
    private javax.swing.JLabel lblParams;
    private javax.swing.JScrollPane unitScroller;
    private javax.swing.JTable unitTable;
    // End of variables declaration//GEN-END:variables
}

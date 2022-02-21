//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

import static terminal.Terminal.Cabinets_Hash;
import static terminal.Terminal.PLCs_Hash;

@SuppressWarnings("ALL")
class CpuMon extends javax.swing.JInternalFrame {

    static boolean unitListActive;
    private static final javax.swing.DefaultListModel<Cabinet> dtm = new javax.swing.DefaultListModel<>();// список кабнетов (шкафов)
    private static final javax.swing.DefaultListModel<CountUnit> dtm2 = new javax.swing.DefaultListModel<>(); // список лучей
    private static final javax.swing.JTextArea jTextArea1 = new javax.swing.JTextArea();
    private javax.swing.JScrollPane jScrollPane3 = new javax.swing.JScrollPane();
    private String lblCom = "";
    private String lblDB = "";
    private javax.swing.JInternalFrame rnaxis;
    private javax.swing.JInternalFrame unitLst;
    static int max = 0;

    CpuMon() {
        initComponents();
        setFont(Terminal.SANS18P);
        jTabbedPane1.setFont(Terminal.SANS18P);
        cabinetList.setFont(Terminal.SANS14P);
        co_cpu_Panel.setFont(Terminal.SANS14P);
        countUnitList.setFont(Terminal.SANS14P);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setAutoscrolls(false);
        jTextArea1.setFocusable(false);
        jScrollPane3.setViewportView(jTextArea1);

        cabinetList.setCellRenderer(new ListRender<>());
        countUnitList.setCellRenderer(new ListRender<>());

        jTabbedPane1.setIconAt(0, Terminal.mainPictureHash.get("cpu"));// CPU
        jTabbedPane1.setIconAt(1, Terminal.mainPictureHash.get("counters_1"));// CO
        jTabbedPane1.setIconAt(2, Terminal.mainPictureHash.get("db_processing_on"));// COMM_SERVER

        if (Util.GetUserGroups("SHNCS")) {// SHNCS
            toggleAxisChange.setVisible(true);
            toggleAxisChange.setEnabled(true);
        } else {
            toggleAxisChange.setVisible(false);
            toggleAxisChange.setEnabled(false);
        }
        // initUnitList();//!!!!!!!!!!!!!! доделать
        CabRack();

        GetControllers();

        csPanel.add(jTextArea1);
//        csPanel.add(lblCom);
//        csPanel.add(lblDB);
//        csPanel.add(xz);
        // initTable();
        initCountUnitList(); // пишем список из колличества процессоров счета
        // осей их обычно бывает 1 или 2 (1 всегда есть)
        // и выбираем первый
        if (countUnitList.getMaxSelectionIndex() > -1) {
            countUnitList.setSelectedIndex(0); // т.к. хотябы один процессор
            // счета осей (в рабочей системе) всегда есть -
            // устанавливаем на него курсор.
            // (для разработки не подходит - CO может быть ещё не создан)
        }
        // initTableCounrerEvent();
        // startCountShadow(true);
    }

    static private void Close() {
        AltMenu.toggleCPUMonitor.setSelected(false);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        cpuPanel = new javax.swing.JPanel();
        cabinetScroller = new javax.swing.JScrollPane();
        cabinetList = new javax.swing.JList();
        coPanel = new javax.swing.JPanel();
        co_cpu_Panel = new javax.swing.JPanel();
        countUnitScroller = new javax.swing.JScrollPane();
        countUnitList = new javax.swing.JList();
        toggleAxisChange = new javax.swing.JToggleButton();
        csPanel = new javax.swing.JPanel();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(true);
        setTitle("Диагностика МПЦ");
        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(600, 700));
        setVisible(false);
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

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTabbedPane1.setToolTipText("");
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        cpuPanel.setPreferredSize(new java.awt.Dimension(533, 300));

        cabinetList.setModel(dtm);
        cabinetList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        cabinetList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cabinetListMouseClicked(evt);
            }
        });
        cabinetScroller.setViewportView(cabinetList);

        javax.swing.GroupLayout cpuPanelLayout = new javax.swing.GroupLayout(cpuPanel);
        cpuPanel.setLayout(cpuPanelLayout);
        cpuPanelLayout.setHorizontalGroup(
            cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cabinetScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
        );
        cpuPanelLayout.setVerticalGroup(
            cpuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cabinetScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 637, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CPU", cpuPanel);

        coPanel.setPreferredSize(new java.awt.Dimension(533, 300));
        coPanel.setRequestFocusEnabled(false);

        co_cpu_Panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Список процессоров счета осей"));

        countUnitList.setModel(dtm2);
        countUnitList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        countUnitList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                countUnitListMouseClicked(evt);
            }
        });
        countUnitScroller.setViewportView(countUnitList);

        toggleAxisChange.setFont(Terminal.SANS12);
        toggleAxisChange.setText("Корр.осей");
        toggleAxisChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleAxisChangeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout co_cpu_PanelLayout = new javax.swing.GroupLayout(co_cpu_Panel);
        co_cpu_Panel.setLayout(co_cpu_PanelLayout);
        co_cpu_PanelLayout.setHorizontalGroup(
            co_cpu_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(countUnitScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
            .addGroup(co_cpu_PanelLayout.createSequentialGroup()
                .addComponent(toggleAxisChange)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        co_cpu_PanelLayout.setVerticalGroup(
            co_cpu_PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(co_cpu_PanelLayout.createSequentialGroup()
                .addComponent(countUnitScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(toggleAxisChange)
                .addContainerGap())
        );

        javax.swing.GroupLayout coPanelLayout = new javax.swing.GroupLayout(coPanel);
        coPanel.setLayout(coPanelLayout);
        coPanelLayout.setHorizontalGroup(
            coPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(co_cpu_Panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        coPanelLayout.setVerticalGroup(
            coPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(coPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(co_cpu_Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(125, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("CO", coPanel);

        javax.swing.GroupLayout csPanelLayout = new javax.swing.GroupLayout(csPanel);
        csPanel.setLayout(csPanelLayout);
        csPanelLayout.setHorizontalGroup(
            csPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 486, Short.MAX_VALUE)
        );
        csPanelLayout.setVerticalGroup(
            csPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 637, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CommServer", csPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        Close();
        setVisible(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        int tab = jTabbedPane1.getSelectedIndex();

        switch (tab) {
            case 0:
                setTitle("Диагностика МПЦ");
                break;
            case 1:
                setTitle("Монитор счета осей");
                break;
            case 2:
                Comm();
                setTitle("Диагностика БД, Коммуникационного сервера, Контроллера");
                Net.Send("STATUS");
//                jTextArea1.removeAll();
                jTextArea1.setText("");
//                csPanel.revalidate();
//                csPanel.repaint();
                jTextArea1.append(lblCom + "\n");
//                jTextArea1.append(lblDB + "\n");
                jTextArea1.append("Packets: " + Net.getPacket_count() + " Bytes: " + Net.getByte_count() + "\n");
                break;
            // case 3:
            // setTitle("Корректировка колличества осей на контролируемых участках");
            // break;
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void countUnitListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_countUnitListMouseClicked
        showCounterList();
    }//GEN-LAST:event_countUnitListMouseClicked

    private void cabinetListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cabinetListMouseClicked
        showUnitList();
    }//GEN-LAST:event_cabinetListMouseClicked

    private void toggleAxisChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleAxisChangeActionPerformed
        if (toggleAxisChange.isSelected()) {// true
            if (rnaxis == null) {
                rnaxis = new RailnetAxis();
            }
            rnaxis.setBounds(Terminal.AXIS_X, Terminal.AXIS_Y, Terminal.AXIS_WIDTH, Terminal.AXIS_HEIGHT);// victor - коордиаты надо читать из базы
            Commander.cmdLayers.setLayer(Commander.cmdLayers.add(rnaxis), 30);
            rnaxis.setVisible(true);
        }
    }//GEN-LAST:event_toggleAxisChangeActionPerformed

    private void showUnitList() {
        unitLst = null;
        Cabinet c;
//        int cabinet = cabinetList.getSelectedValue().getIdObj();
        c = (Cabinet) cabinetList.getSelectedValue();
        int cab = c.getIdObj();

        // if (cabinet == 0 ) то cabinet = 1 чтобы небыло null pointer !!!
        // if (unitList == null){
        // unitList = new UnitList(this, false, cabinet); //modal = false !!!
        // // }
        // unitList.setLocationRelativeTo(this);
        // unitList.setVisible(true);
        unitLst = new UnitLst(cab); // modal = false !!!
        unitLst.setBounds(Terminal.UNIT_X, Terminal.UNIT_Y, Terminal.UNIT_WIDTH, Terminal.UNIT_HEIGHT);//victor--коордиаты надо читать из базы
        Commander.cmdLayers.setLayer(Commander.cmdLayers.add(unitLst), 30);
        unitLst.setVisible(true);
    }

    public UnitLst getUnitList() {
        return (UnitLst) unitLst;
    }

    private void showCounterList() {
//        int countUniter = countUnitList.getSelectedValue().id_obj;
        CountUnit c = (CountUnit) countUnitList.getSelectedValue();
//        int countUniter = c.id_obj;//номер счетного пункта
        // if (counterLst == null) {
//        javax.swing.JInternalFrame counterLst = new CounterLst(countUniter);
        javax.swing.JInternalFrame counterLst = new CounterLst();
//        javax.swing.JInternalFrame counterLst = new CounterLst(1234567);
        // }
        counterLst.setBounds(Terminal.CO_X, Terminal.CO_Y, Terminal.CO_WIDTH, Terminal.CO_HEIGHT);// victor - коордиаты надо читать из базы
        Commander.cmdLayers.setLayer(Commander.cmdLayers.add(counterLst), 30);
        counterLst.setVisible(true);
    }

    private void CabRack() { // заполнить объект - Шкаф, обектом полка (полка это
        // JPanel)
        // for (Cabinet c : Cabinets_Hash) {
        // for (Component r : c.getComponents()) {
        // if (r instanceof Rack) {
        // ((Rack) r).fillHash();
        // }
        // }
        Cabinets_Hash.keySet().forEach((Integer k) -> {
            // тут надо заполнить RACK
            // for (Component r : Cabinets_Hash.get(i).getComponents()){
            dtm.addElement(Cabinets_Hash.get(k));
            // }
        });
        if (cabinetList.getMaxSelectionIndex() != -1) {
            cabinetList.setSelectedIndex(0);//
        }
    }

    private void initCountUnitList() {
        Terminal.Beams_Hash.keySet().forEach((k) -> {// по каждому лучу заполнить красивость
            int counters = Terminal.Beams_Hash.get(k).counterAmount;
            if (max < counters) {
                max = counters;
            }
        });
        
        Terminal.Beams_Hash.keySet().forEach((k) -> {// по каждому лучу заполнить красивость
            Terminal.Beams_Hash.get(k).fillHash();
        });
        Terminal.CountUnit_Hash.keySet().stream().map(Terminal.CountUnit_Hash::get).forEach(dtm2::addElement);
    }

    private void GetControllers() { // init - SELECT * FROM CPU;
        PLCs_Hash.keySet().stream().peek((i) -> {
            csPanel.setLayout(new java.awt.GridLayout(4, 0));// 3 rows 1-colums
        }).map(PLCs_Hash::get).forEach((p) -> csPanel.add(p));
    }

    private void Comm() {
        if (Net.clientSocket != null) {
            lblCom = "<html>Соединение с коммуникационным сервером : "
                    + Net.clientSocket.getInetAddress() + ":"
                    + Net.clientSocket.getPort()
                    + "<FONT COLOR=GREEN>: - работает</FONT></html>";
        } else {
            lblCom = "<html>Соединение с коммуникационным сервером "
                    + "<FONT COLOR=RED>: не работает</FONT></html>";
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList cabinetList;
    private javax.swing.JScrollPane cabinetScroller;
    private javax.swing.JPanel coPanel;
    private javax.swing.JPanel co_cpu_Panel;
    private javax.swing.JList countUnitList;
    private javax.swing.JScrollPane countUnitScroller;
    private javax.swing.JPanel cpuPanel;
    private static javax.swing.JPanel csPanel;
    private javax.swing.JTabbedPane jTabbedPane1;
    protected static javax.swing.JToggleButton toggleAxisChange;
    // End of variables declaration//GEN-END:variables
}

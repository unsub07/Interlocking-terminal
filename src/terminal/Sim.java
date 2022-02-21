//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
//!после восстановления осей удалить файл!
package terminal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JOptionPane;

class Sim extends javax.swing.JInternalFrame {

    private static final javax.swing.table.DefaultTableModel DTM = new javax.swing.table.DefaultTableModel();
    private static boolean paintSimFlag;
    private static boolean lt_bl = false;
    private static boolean rn_bl = false;
    private static boolean rn_ir = false;
    private static boolean tr_bl = false;
    private static boolean rn_slz = false;
    private static HashMap<Integer, Short> COUNT_IN_RN = new HashMap<>(10);
    
    private static String er;
    private static void InsertMessage(long dt, int ID_OBJ, String mes) {
        DTM.insertRow(0, new Object[]{
            Util.DateFromLong(dt), // Дата
            Util.TimeFromLong(dt), // Время
            Util.objType(ID_OBJ),// Тип
            Terminal.obj_name.get(ID_OBJ),// Объект --- проверить существует ли?
            mes // Состояние
        });
    }
    private static void ShowMess(String fullName, int ret, int row){
        //если успешно пишем, путь, имя файла, сколько сохранено.
        if (ret==0){
            JOptionPane.showMessageDialog(null, "Cохранен \n"+fullName+"\n"+row);
        }
        //если ошибка пишем - путь, имя файла, сколько и ошибку.
        if (ret==-1){
            JOptionPane.showMessageDialog(null, "Не удалось сохранить \n"+fullName+"\n"+er);
        }
    }

    Sim() {
        initComponents();
        // -------------------------------Translate-----------------------------
//        String date = Terminal.Translate("Date", "Дата");
//        String time = Terminal.Translate("Time", "Время");
//        String type = Terminal.Translate("Type", "Тип");
//        String object = Terminal.Translate("Object", "Объект");
//        String condition = Terminal.Translate("Condition", "Состояние");
        String date = "Дата";
        String time = "Время";
        String type = "Тип";
        String object = "Объект";
        String condition = "Состояние";
        DTM.setColumnCount(5);
        DTM.setColumnIdentifiers(new Object[]{date, time, type, object, condition});
        eventTable.getTableHeader().setFont(Terminal.SANS12);
        eventTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        eventTable.getColumnModel().getColumn(0).setPreferredWidth(85); // Дата
        eventTable.getColumnModel().getColumn(1).setPreferredWidth(75); // Время
        eventTable.getColumnModel().getColumn(2).setPreferredWidth(160); // Тип объекта
        eventTable.getColumnModel().getColumn(3).setPreferredWidth(160); // Объект
        eventTable.getColumnModel().getColumn(4).setPreferredWidth(330); // Состояние
        eventTable.getTableHeader().setReorderingAllowed(false);
        // ---------------------------------------------------------------------
        Sim_Thread();
    }
    
    private void Sim_Thread() {
        Log.log("Starting SIM shadow repaint daemon...");
        paintSimFlag = true;
        try {
            class back_run extends Thread {

                back_run() {
                }

                @Override
                public void run() {
                    Log.log("Thread SIM starting...");
                    try {
                        while (paintSimFlag) {
                            work();
                            back_run.sleep(50);
                        }
                    } catch (InterruptedException e) {
                        Err.err(e);
                    }
                    Log.log("SIM_Thread END! ");
                }
            }
            Thread thr_rePaint = new back_run();
            thr_rePaint.start();
            Log.log("SIM shadow repaint daemon started!");
        } catch (Exception e) {
            Log.log("SIM shadow++++" + e);
        }
    }
    
    private void work() {
//--------------------
        if (lt_bl) {
            Light lO;
            for (int id_obj : Terminal.LightsCell_Hash.keySet()) {
                lO = Terminal.LightsCell_Hash.get(id_obj);
                if (lO.control != 2) { //FULL CONTROL

                    if (btnLT_bl.isSelected()) {
                        btnLT_bl.setText("Разблокировать Светофоры");//Разблокировать Светофоры
                        btnLT_bl.setSelected(true);
                        Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_BLOCK_ON");
                        pause(200);
                        if (lO.block) {
                            InsertMessage(lO.getDtime(), id_obj, "ok");
                        } else {
                            InsertMessage(lO.getDtime(), id_obj, "false");
                        }
                    } else {
                        btnLT_bl.setText("Заблокировать Светофоры");//Заблокировать Светофоры
                        btnLT_bl.setSelected(false);
                        Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_BLOCK_OFF");
                    }
                }
            }
            lt_bl = false;
        }
//----------------------
        if (tr_bl) {
            Turnout tO;
            for (int id_obj : Terminal.Turnouts_Hash.keySet()) {
                tO = Terminal.Turnouts_Hash.get(id_obj);
                if (tO.id_type != 4) {
                    if (btnTR_bl.isSelected()) {
                        btnTR_bl.setText("Разблокировать Стрелки");//Разблокировать Стрелки
                        btnTR_bl.setSelected(true);
                        Net.sendMaskedCmd_DSP(tO.cmdSender, "TURNOUT_BLOCK_ON");
                        pause(200);
                        if (tO.block) {
                            InsertMessage(tO.getDtime(), id_obj, "ok");
                        } else {
                            InsertMessage(tO.getDtime(), id_obj, "false");
                        }
                    } else {
                        btnTR_bl.setText("Заблокировать Стрелки");//Заблокировать Стрелки
                        btnTR_bl.setSelected(false);
                        Net.sendMaskedCmd_DSP(tO.cmdSender, "TURNOUT_BLOCK_OFF");
                    }
                }//end if id_type != 4
            }
            tr_bl = false;
        }
//----------------------
        if (rn_bl) {
            Railnet rO;
            for (int id_obj : Terminal.Railnets_Hash.keySet()) {
                rO = Terminal.Railnets_Hash.get(id_obj);
                if (rO.type == 1 || rO.type == 2 || rO.type == 5 || rO.type == 9) {
                    if (btnRN_bl.isSelected()) {
                        btnRN_bl.setText("Разблокировка РЦ");
                        btnRN_bl.setSelected(true);
                        Net.sendMaskedCmd_DSP(id_obj, "RAILNET_BLOCK_ON");
                        pause(200);
                        if (rO.block) {
                            InsertMessage(rO.getDtime(), id_obj, "ok");
                        } else {
                            InsertMessage(rO.getDtime(), id_obj, "false");
                        }
                    } else {
                        btnRN_bl.setText("Заблокировать РЦ");
                        btnRN_bl.setSelected(false);
                        Net.sendMaskedCmd_DSP(id_obj, "RAILNET_BLOCK_OFF");
                    }
                }//end if id_type ==
            }//end for
            rn_bl = false;
        }
//---------------------
        if (rn_ir) {
            Railnet rO;
            for (int id_obj : Terminal.Railnets_Hash.keySet()) {
                rO = Terminal.Railnets_Hash.get(id_obj);
                if (rO.type == 1 || rO.type == 2) {
                    Net.sendMaskedCmd_DSP(id_obj, "RAILNET_IR_PHASE1");
                    pause(300);
                    Net.sendMaskedCmd_DSP(id_obj, "RAILNET_IR_PHASE2");
                
                pause(200);
                if (rO.ir) {
                    InsertMessage(rO.getDtime(), id_obj, "ok");
                } else {
                    InsertMessage(rO.getDtime(), id_obj, "false");
                }
                }//end if id_type = 1 or 2
            }
            rn_ir = false;
        }
        
        if (rn_slz) {
            Railnet rO;
            for (int id_obj : Terminal.Railnets_Hash.keySet()) {
                rO = Terminal.Railnets_Hash.get(id_obj);
                    Net.sendMaskedCmd_DSP(id_obj, "RAILNET_SLZ_PHASE1");
                if (rO.type == 1 || rO.type == 2) {
                    Net.sendMaskedCmd_DSP(id_obj, "RAILNET_IR_PHASE1");
                }//end if id_type = 1 or 2
            }//for
            pause(500);
            for (int id_obj : Terminal.Railnets_Hash.keySet()) {
                rO = Terminal.Railnets_Hash.get(id_obj);
                    Net.sendMaskedCmd_DSP(id_obj, "RAILNET_SLZ_PHASE2");
                if (rO.type == 1 || rO.type == 2) {
                    Net.sendMaskedCmd_DSP(id_obj, "RAILNET_IR_PHASE2");
                }//end if id_type = 1 or 2
            }//for
            
            rn_slz = false;
        }
    }
    
    private void Save(){
        String infoType = "fuck";
        int ret = 0; //возвращаем 0 - если всё удачно, если не равно 0 то в String er - ошибка
        int row = eventTable.getRowCount();
        int col = eventTable.getColumnCount();
        StringBuilder line = new StringBuilder();

        if (row != 0) {
            BufferedWriter out = null;//out = null,
//--------------Date Time from machine (not SQL)----------------
//            String dt = new java.util.Date().toString();
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");
            String dt = Terminal.SDF3.format(new java.util.Date());
            //String file = infoType + "_" + dt + ".csv";
//            Object preffix = dtm.getValueAt(0, 0);
//            Object suffix = dtm.getValueAt(row - 1, 0);

            String ATTRANS_HOME = System.getenv("ATTRANS_HOME");
            if (ATTRANS_HOME == null) {
                ATTRANS_HOME = System.getProperty("user.dir");
            } //откудава запущено
            String fs = System.getProperty("file.separator");
            //проверить существует ли каталог - log
            File dir = new File(ATTRANS_HOME + fs + "log");//new File("log");
            boolean wasSuccessful = true;
            if (!dir.exists()) {
                wasSuccessful = dir.mkdir();
            }
            if (wasSuccessful){
            String path = ATTRANS_HOME + fs + "log" + fs;
//            String file = dt + "_" + infoType + ".csv";
            String fileHTML = dt + "_" + infoType;
            String fullName = path + fileHTML + ".csv";
            try {
//                out = new BufferedWriter(new FileWriter(path + file));
                out = new BufferedWriter(new FileWriter(fullName));

                for (int i = 0; i < row; i++) {

                    for (int j = 0; j < col; j++) {
                        Object o = DTM.getValueAt(eventTable.convertRowIndexToModel(i), j);

                        String Temp = (o == null) ? "" : ((String) o);//if ( o == null){ Temp = "";} else {Temp = (String) o;};
                        line.append(Temp).append(";");
                        out.write(Temp);
                    }
                    line.append("\n");
//                    System.out.println(line);
//                    out.write(line);
                    line = new StringBuilder();
                    out.write("\n");
                }
            } catch (IOException e) {
                ret = -1;
                er = e.getMessage();
                Err.err(e);
            } finally {
                try {
//                    if (out!=null){out.close();}
                    if (out != null) {
                        out.close();
                    }

                } catch (IOException e) {
                    ret = -1;
                    er = e.getMessage();
                    Err.err(e);
                }
            }
            ShowMess(fullName, ret, row);
            }//end if - Directory already exist
        }
    }
    
    private void Store(){//сохраняем в файл timestamp пробел id рельсы пробел кол-вл осей. !не удаляется!
        String infoType = "rn";
        int ret = 0; //возвращаем 0 - если всё удачно, если не равно 0 то в String er - ошибка
        int row = COUNT_IN_RN.size();
        StringBuilder line = new StringBuilder();
        
        COUNT_IN_RN.keySet().forEach((id_obj) -> {
            
        });

        if (row != 0) {
            BufferedWriter out = null;//out = null,

            String dt = Terminal.SDF3.format(new java.util.Date());
            //String file = infoType + "_" + dt + ".csv";
//            Object preffix = dtm.getValueAt(0, 0);
//            Object suffix = dtm.getValueAt(row - 1, 0);

            String ATTRANS_HOME = System.getenv("ATTRANS_HOME");
            if (ATTRANS_HOME == null) {
                ATTRANS_HOME = System.getProperty("user.dir");
            } //откудава запущено
            String fs = System.getProperty("file.separator");
            //проверить существует ли каталог - log
            File dir = new File(ATTRANS_HOME + fs + "log");//new File("log");
            boolean wasSuccessful = true;
            if (!dir.exists()) {
                wasSuccessful = dir.mkdir();
            }
            if (wasSuccessful){
            String path = ATTRANS_HOME + fs + "log" + fs;
//            String file = dt + "_" + infoType + ".csv";
            String fileHTML = dt + "_" + infoType;
            String fullName = path + fileHTML + ".csv";
            try {
//                out = new BufferedWriter(new FileWriter(path + file));
                out = new BufferedWriter(new FileWriter(fullName));
                
                Railnet rn;
                
                for (Integer id_obj : COUNT_IN_RN.keySet()) {
                    rn = Terminal.Railnets_Hash.get(id_obj);
                    rn.getDtime();
                    String id = String.valueOf(id_obj);
                    String axis = String.valueOf(COUNT_IN_RN.get(id_obj));
                    String time = String.valueOf(rn.getDtime());
                    String Temp = time + " " + id + " "+axis+"\n";

                        out.write(Temp);
                }

            } catch (IOException e) {
                ret = -1;
                er = e.getMessage();
                Err.err(e);
            } finally {
                try {
//                    if (out!=null){out.close();}
                    if (out != null) {
                        out.close();
                    }

                } catch (IOException e) {
                    ret = -1;
                    er = e.getMessage();
                    Err.err(e);
                }
            }
            ShowMess(fullName, ret, row);
            }//end if - Directory already exist
        }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        eventTableScroller = new javax.swing.JScrollPane();
        eventTable = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        btnIR = new javax.swing.JButton();
        btnRN_bl = new javax.swing.JToggleButton();
        btnZ = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnLT_bl = new javax.swing.JToggleButton();
        jSeparator2 = new javax.swing.JSeparator();
        btnLT_peregR = new javax.swing.JToggleButton();
        btnLT_peregZ = new javax.swing.JToggleButton();
        btnTR_bl = new javax.swing.JToggleButton();
        btnTR_Z = new javax.swing.JToggleButton();
        btnTR_PM = new javax.swing.JToggleButton();
        btnClrTbl = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnSLZ = new javax.swing.JButton();
        btnStore = new javax.swing.JButton();
        btnRestore = new javax.swing.JButton();
        btnEmul = new javax.swing.JButton();
        btnVzrez = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(true);
        setTitle("Симулятор");
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

        eventTable.setModel(DTM);
        eventTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        eventTableScroller.setViewportView(eventTable);

        btnIR.setText("ИР");
        btnIR.setToolTipText("Исскуственная разделка");
        btnIR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIRActionPerformed(evt);
            }
        });

        btnRN_bl.setText("Заблокировать РЦ");
        btnRN_bl.setToolTipText("Блокировка участков");
        btnRN_bl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRN_blActionPerformed(evt);
            }
        });

        btnZ.setText("Освободить РЦ");
        btnZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZActionPerformed(evt);
            }
        });

        btnLT_bl.setText("Заблокировать Светофоры");
        btnLT_bl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLT_blActionPerformed(evt);
            }
        });

        btnLT_peregR.setText("Перегореть Разрешающий");
        btnLT_peregR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLT_peregRActionPerformed(evt);
            }
        });

        btnLT_peregZ.setText("Перегореть Запрещающий");
        btnLT_peregZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLT_peregZActionPerformed(evt);
            }
        });

        btnTR_bl.setText("Заблокировать Стрелки");
        btnTR_bl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTR_blActionPerformed(evt);
            }
        });

        btnTR_Z.setText("Потерять контроль Ст.");
        btnTR_Z.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTR_ZActionPerformed(evt);
            }
        });

        btnTR_PM.setText("Перевети Стрелки в +");
        btnTR_PM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTR_PMActionPerformed(evt);
            }
        });

        btnClrTbl.setText("Clear Table");
        btnClrTbl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClrTblActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnSLZ.setText("slz");
        btnSLZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSLZActionPerformed(evt);
            }
        });

        btnStore.setText("Store");
        btnStore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStoreActionPerformed(evt);
            }
        });

        btnRestore.setText("Restore");
        btnRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnSLZ)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnStore)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRestore)
                .addGap(0, 44, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSLZ)
                    .addComponent(btnStore)
                    .addComponent(btnRestore))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        btnEmul.setText("Emul");
        btnEmul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmulActionPerformed(evt);
            }
        });

        btnVzrez.setText("vzrez");
        btnVzrez.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVzrezActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(eventTableScroller)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addComponent(jSeparator2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(btnIR)
                        .addGap(18, 18, 18)
                        .addComponent(btnRN_bl)
                        .addGap(18, 18, 18)
                        .addComponent(btnZ)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClrTbl))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnLT_bl)
                                .addGap(18, 18, 18)
                                .addComponent(btnLT_peregR)
                                .addGap(18, 18, 18)
                                .addComponent(btnLT_peregZ)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnVzrez))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnTR_bl)
                                .addGap(18, 18, 18)
                                .addComponent(btnTR_Z)
                                .addGap(18, 18, 18)
                                .addComponent(btnTR_PM)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEmul)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnIR)
                        .addComponent(btnRN_bl)
                        .addComponent(btnZ)
                        .addComponent(btnClrTbl)
                        .addComponent(btnSave))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLT_bl)
                    .addComponent(btnLT_peregR)
                    .addComponent(btnLT_peregZ)
                    .addComponent(btnVzrez))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTR_bl)
                    .addComponent(btnTR_Z)
                    .addComponent(btnTR_PM)
                    .addComponent(btnEmul))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eventTableScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
//Railnet
    private void btnIRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIRActionPerformed
        rn_ir = true;
    }//GEN-LAST:event_btnIRActionPerformed

    private void btnRN_blActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRN_blActionPerformed
        rn_bl = true;
    }//GEN-LAST:event_btnRN_blActionPerformed

    private void btnZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZActionPerformed
        Railnet rO;
        for (int id_obj : Terminal.Railnets_Hash.keySet()) {
            rO = Terminal.Railnets_Hash.get(id_obj);
            if (rO.type != 8) {
                if (btnZ.isSelected()) {
                    btnZ.setText("Занять РЦ   ");
                    btnZ.setSelected(true);
                    Net.sendMaskedCmd_SIM(id_obj, "RAILNET_SIM_Z1");
                } else {
                    btnZ.setText("Освободить РЦ");
                    btnZ.setSelected(false);
                    Net.sendMaskedCmd_SIM(id_obj, "RAILNET_SIM_Z0");
                }
            }
        }
    }//GEN-LAST:event_btnZActionPerformed
// Lights
    private void btnLT_blActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLT_blActionPerformed
        lt_bl = true;
    }//GEN-LAST:event_btnLT_blActionPerformed

    private void btnLT_peregRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLT_peregRActionPerformed
        Light lO;
        for (int id_obj : Terminal.LightsCell_Hash.keySet()) {
            lO = Terminal.LightsCell_Hash.get(id_obj);
            if (lO.control == 0) { //FULL CONTROL
                if (btnLT_peregR.isSelected()) {
                    btnLT_peregR.setText("Перегореть Разрешающий");//
                    btnLT_peregR.setSelected(true);
                    Net.sendMaskedCmd_SIM(id_obj, "LIGHTS_SIM_R1");
                } else {
                    btnLT_peregR.setText("Восстановить Разрешающий");//
                    btnLT_peregR.setSelected(false);
                    Net.sendMaskedCmd_SIM(id_obj, "LIGHTS_SIM_R0");
                }
            }
        }
    }//GEN-LAST:event_btnLT_peregRActionPerformed

    private void btnLT_peregZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLT_peregZActionPerformed
        Light lO;
        for (int id_obj : Terminal.LightsCell_Hash.keySet()) {
            lO = Terminal.LightsCell_Hash.get(id_obj);
            if (lO.control == 0) { //FULL CONTROL
                if (btnLT_peregZ.isSelected()) {
                    btnLT_peregZ.setText("Перегореть Запрещающий");//
                    btnLT_peregZ.setSelected(true);
                    Net.sendMaskedCmd_SIM(id_obj, "LIGHTS_SIM_Z1");
                } else {
                    btnLT_peregZ.setText("Восстановить Запрещающий");//
                    btnLT_peregZ.setSelected(false);
                    Net.sendMaskedCmd_SIM(id_obj, "LIGHTS_SIM_Z0");
                }
            }
        }
    }//GEN-LAST:event_btnLT_peregZActionPerformed
//Turnouts
    private void btnTR_blActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTR_blActionPerformed
        tr_bl = true;
    }//GEN-LAST:event_btnTR_blActionPerformed

    private void btnTR_ZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTR_ZActionPerformed
        Turnout tO;
        for (int id_obj : Terminal.Turnouts_Hash.keySet()) {
            tO = Terminal.Turnouts_Hash.get(id_obj);
            if (tO.id_type != 4) {
                if (btnTR_Z.isSelected()) {
                    btnTR_Z.setText("Восстановть контроль Ст.");//Восстановть контроль Ст.
                    btnTR_Z.setSelected(true);
                    Net.sendMaskedCmd_SIM(id_obj, "TURNOUT_SIM_K1");
                } else {
                    btnTR_Z.setText("Потерять контроль Ст.");//Потерять контроль Ст.
                    btnTR_Z.setSelected(false);
                    Net.sendMaskedCmd_SIM(id_obj, "TURNOUT_SIM_K0");
                }
            }
        }
    }//GEN-LAST:event_btnTR_ZActionPerformed

    private void btnTR_PMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTR_PMActionPerformed
        Turnout tO;
        for (int id_obj : Terminal.Turnouts_Hash.keySet()) {
            tO = Terminal.Turnouts_Hash.get(id_obj);
            if (tO.id_type != 4) {
                if (btnTR_PM.isSelected()) {
                    btnTR_PM.setText("Перевети Стрелки в -");//
                    btnTR_PM.setSelected(true);
                    Net.sendMaskedCmd_DSP(tO.cmdSender, "TURNOUT_POSITION_PLUS1");
                    pause(300);
                    Net.sendMaskedCmd_DSP(tO.cmdSender, "TURNOUT_POSITION_PLUS2");
                } else {
                    btnTR_PM.setText("Перевети Стрелки в +");//Перевети Стрелки в -
                    btnTR_PM.setSelected(false);
                    Net.sendMaskedCmd_DSP(tO.cmdSender, "TURNOUT_POSITION_MINUS1");
                    pause(300);
                    Net.sendMaskedCmd_DSP(tO.cmdSender, "TURNOUT_POSITION_MINUS2");
                }
            }
        }
    }//GEN-LAST:event_btnTR_PMActionPerformed

    private void btnClrTblActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClrTblActionPerformed
        while (DTM.getRowCount() > 0) {
            DTM.removeRow(0); // чистим таблицу
        }
    }//GEN-LAST:event_btnClrTblActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        Save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSLZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSLZActionPerformed
        rn_slz = true;
    }//GEN-LAST:event_btnSLZActionPerformed

    private void btnStoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStoreActionPerformed
        COUNT_IN_RN.clear();
        Railnet rO;
            for (int id_obj : Terminal.Railnets_Hash.keySet()) {
                rO = Terminal.Railnets_Hash.get(id_obj);
                if (rO.axis > 0){//если на RN есть оси
                    COUNT_IN_RN.put(id_obj, rO.axis);
                }
            }
        Store();
    }//GEN-LAST:event_btnStoreActionPerformed

    private void btnRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestoreActionPerformed
        COUNT_IN_RN.keySet().forEach((id_obj) -> {
            new Rn_Axis_Thread(id_obj,COUNT_IN_RN.get(id_obj)).start();
        }); //end for
    }//GEN-LAST:event_btnRestoreActionPerformed

    private void btnEmulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmulActionPerformed
        Commander.emul.setVisible(true);
    }//GEN-LAST:event_btnEmulActionPerformed

    private void btnVzrezActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVzrezActionPerformed
        new Vzrez1();
    }//GEN-LAST:event_btnVzrezActionPerformed

    private void pause(int pause) {
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            Err.err(e);
        }
    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {// GEN-FIRST:event_formInternalFrameClosing
        AltMenu.toggleNavigator.setSelected(false);
        setVisible(false);
    }// GEN-LAST:event_formInternalFrameClosing
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClrTbl;
    private javax.swing.JButton btnEmul;
    private javax.swing.JButton btnIR;
    private javax.swing.JToggleButton btnLT_bl;
    private javax.swing.JToggleButton btnLT_peregR;
    private javax.swing.JToggleButton btnLT_peregZ;
    private javax.swing.JToggleButton btnRN_bl;
    private javax.swing.JButton btnRestore;
    private javax.swing.JButton btnSLZ;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnStore;
    private javax.swing.JToggleButton btnTR_PM;
    private javax.swing.JToggleButton btnTR_Z;
    private javax.swing.JToggleButton btnTR_bl;
    private javax.swing.JButton btnVzrez;
    private javax.swing.JToggleButton btnZ;
    private javax.swing.JTable eventTable;
    private javax.swing.JScrollPane eventTableScroller;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables

}

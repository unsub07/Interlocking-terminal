//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
//чем больше число тем приорететнее звук
package terminal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.swing.event.ListSelectionEvent;

@SuppressWarnings("serial")
class Alarms extends javax.swing.JInternalFrame {

    private static javax.swing.JButton btnDispConfirm;
    private static final javax.swing.table.DefaultTableModel DTM = new javax.swing.table.DefaultTableModel();
    private static javax.swing.JTable alarmTable;

    private static String AlarmTitle;
    private static String date;
    private static String Restored;
    private static int rowsInTable = 0;
    private static boolean enablePolling = false;

// private static final javax.swing.table.TableRowSorter sorter = new
    // javax.swing.table.TableRowSorter(DTM);

    // private static ListSelectionListener lineChangeListener;

    Alarms() {
        initComponents();
        btnDispConfirm.setIcon(Terminal.mainPictureHash.get("eye"));

//        AlarmTitle = Terminal.Translate("AlarmTitle", "Аварийные уведомления. Всего: ");
////        RightFault = Terminal.Translate("RightFault", "Не достаточно полномочий.");
//        date = Terminal.Translate("Date", "Дата");
//        Restored = Terminal.Translate("Restored", "Восстановлено");
////        String area = Terminal.Translate("Area", "Район");
//        String type = Terminal.Translate("Type", "Тип");
//        String object = Terminal.Translate("Object", "Объект");
//        String condition = Terminal.Translate("Condition", "Состояние");
        AlarmTitle = "Аварийные уведомления. Всего: ";
        date = "Дата";
        Restored = "Восстановлено";
        String type = "Тип";
        String object = "Объект";
        String condition = "Состояние";
        // ------------------------------------------------------------------------------
        DTM.setColumnCount(6);
//        DTM.setColumnIdentifiers(new Object[]{"#", date, Restored, area, type, object, condition, "ДСП", "id_sound"});
        DTM.setColumnIdentifiers(new Object[]{"#", date, Restored, type, object, condition, "ДСП", "id_sound"});

        alarmTable.removeColumn(alarmTable.getColumn("#"));
        alarmTable.removeColumn(alarmTable.getColumn("id_sound"));
       
        alarmTable.getTableHeader().setFont(Terminal.SANS11);
        alarmTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        alarmTable.getColumnModel().getColumn(0).setPreferredWidth(150);// дата
//        alarmTable.getColumnModel().getColumn(0).setPreferredWidth(85);// дата
//        alarmTable.getColumnModel().getColumn(1).setPreferredWidth(75);// Время
        alarmTable.getColumnModel().getColumn(1).setPreferredWidth(150);// востановленно
//        alarmTable.getColumnModel().getColumn(2).setPreferredWidth(160);// Район
        alarmTable.getColumnModel().getColumn(2).setPreferredWidth(160);// Тип
        alarmTable.getColumnModel().getColumn(3).setPreferredWidth(160);// Объект
        alarmTable.getColumnModel().getColumn(4).setPreferredWidth(300);// Состояние
        alarmTable.getColumnModel().getColumn(5).setPreferredWidth(150);// ДСП
        // --
        alarmTable.setColumnSelectionAllowed(false);

        // alarmTable.setRowSorter(sorter);
        alarmTable.getTableHeader().setReorderingAllowed(false);

        alarmTable.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            if (alarmTable.getSelectedRow() > -1) {
                LineChanged(alarmTable.getSelectedRow());
            }
        } // Если выбрана строчка - глаз доступен?
        );
        while (DTM.getRowCount() > 0) {
            DTM.removeRow(0); // чистим таблицу
        }
  
        for (int i = 0; i < alarmTable.getColumnCount(); i++) {
            alarmTable.getColumnModel().getColumn(i).setCellRenderer(new AlarmTableCellRenderer());
        }
        // ------------------------------------------------------------------------------
        if (!Terminal.DSP) {
            btnDispConfirm.setEnabled(false);
            btnDispConfirm.setVisible(false);
        }
//        load_alarm();
        startPolling();// нитка ждет 8 секунд и если надо играет музыку
//        init();
        setTitle(AlarmTitle + rowsInTable);
    }

    static void alarm_on(long dt, int mes, int id_obj) {//victor FUCK FUFUFU (если есть исправленный то с зеленого не краснеет)
        int currRow = rowFound(String.valueOf(id_obj) + ":" + String.valueOf(mes));
        if (currRow == -1) {

            add_alarm(Util.DateFromLong(dt) + " " + Util.TimeFromLong(dt), "-", mes, id_obj, "-");
            // alarmTable.changeSelection(0, 0, true, false);
        } else {
            repeat_alarm(dt, currRow);
        }
    }

    private static void add_alarm(String d, String res, int mes, int ID_OBJ, String dsp) {
        DTM.insertRow(
                0,
                new Object[]{
                    String.valueOf(ID_OBJ) + ":" + String.valueOf(mes),// ALARM_ID String  #
                    d,
                    //                    Util.DateFromLong(dt) + " " + Util.TimeFromLong(dt),
                    //                    Util.DateFromLong(dt), // date 0
                    //                    Util.TimeFromLong(dt), // time 1
                    res, // ALARM_TIME_OFF 2 (restored)
//                    Terminal.AREA, // AREA_NAME 3
                    Util.objType(ID_OBJ), // OBJ_TYPE 4
                    Terminal.obj_name.get(ID_OBJ),// Объект // проверить существует ли? OBJ_NAME 5
                    Terminal.GetMess(mes),// ALARM_TEXT 6
                    dsp,// DISP_TIME (Confirmation) . 7
                    Terminal.GetSound(mes) //8
                });
        rowsInTable += 1;
        if (Commander.alarm != null) {
            Commander.set_alarm_title(AlarmTitle + rowsInTable);
        }
        playSoundIfNeeded();//играем сразу не ждем цикл 8 сек.
    }

    private static void repeat_alarm(long dt, int currRow) {
        //DTM.setValueAt(Util.DateFromLong(dt) + " " + Util.TimeFromLong(dt), currRow, DTM.findColumn("ШН"));
        DTM.setValueAt(Util.DateFromLong(dt) + " " + Util.TimeFromLong(dt), currRow, 1);
        DTM.setValueAt("-", currRow, DTM.findColumn(Restored));
        btnDispConfirm.setEnabled(true);
    }

    private static void del_alarm() {
        // isRowFull(0);
        for (int r = 0; r < DTM.getRowCount(); r++) {
            if (isRowFull(r)) {
                DTM.removeRow(r);
                rowsInTable -= 1;
                Commander.set_alarm_title(AlarmTitle + rowsInTable);
            }
        }
    }

    static void alarm_off(long dt, int mes, int id_obj) {
        int currRow = rowFound(String.valueOf(id_obj) + ":" + String.valueOf(mes));
        if (currRow != -1) {
            DTM.setValueAt(Util.DateFromLong(dt) + " " + Util.TimeFromLong(dt), currRow, DTM.findColumn(Restored));
//            alarmTable.repaint();
        }
    }

    static void alarm_kvit(long dt, int mes, int id_obj) {
        if (Terminal.VZREZ) {
            if (mes == 217261) {//убрать аларм взреза
                Net.sendMaskedCmd_DSP_SHN(id_obj, "TURNOUT_VZREZ_OFF");
            }
            if (mes == 217271) {//убрать аларм взреза стрелка без контроля
                Net.sendMaskedCmd_DSP_SHN(id_obj, "TURNOUT_VZREZ_OFF");
            }
        }
        int currRow = rowFound(String.valueOf(id_obj) + ":" + String.valueOf(mes));
        if (currRow != -1) {
            DTM.setValueAt(Util.DateFromLong(dt) + " " + Util.TimeFromLong(dt), currRow, DTM.findColumn("ДСП"));
        }
    }

    private static int rowFound(String ALARM_ID) {//cука сдесь медленно перебор всех алармов
        int currRow = -1;
        for (int r = 0; r < DTM.getRowCount(); r++) {
            if (DTM.getValueAt(r, DTM.findColumn("#")).equals(ALARM_ID)) {
                return r;
            }
        }
        return currRow;
    }

    private static boolean isRowFull(int r) {
        return (!DTM.getValueAt(r, DTM.findColumn(Restored)).equals("-") && !DTM.getValueAt(r, DTM.findColumn("ДСП")).equals("-"));
    }

    private static void startPolling() {
        //        Log.log("Starting MPC polling daemon...");
        enablePolling = true;
        try {
            class back_run extends Thread {

                back_run() {
                }

                @Override
                public void run() {
//                    Log.log("Thread starting...");
                    try {
                        while (enablePolling) {
                            Thread.sleep(8_000);
                            del_alarm();
                            playSoundIfNeeded();
                        }
                    } catch (InterruptedException e) {
                        Err.err(e);
                    }
//                    Log.log("Thread END! ");
                }
            }
            Thread t = new back_run();
            t.start();
//            Log.log("MPC polling daemon started!");
        } catch (Exception e) {
            Err.err(e);
        }
//        Log.log("MPC polling daemon working...");
    }

    private static void playSoundIfNeeded() {
        int ID_SOUND = needToPlaySound();
        if (ID_SOUND > 0) {
//            System.out.println("need play sound " + ID_SOUND);
            new Sound(ID_SOUND).start();
        }
    }

    private static int needToPlaySound() {
        int ret = 0;
        if (DTM.getRowCount() > 0) {
            for (int r = 0; r < DTM.getRowCount(); r++) {
                if (String.valueOf(DTM.getValueAt(r, DTM.findColumn("ДСП"))).equals("-")) {
                    int snd = Integer.valueOf(String.valueOf(DTM.getValueAt(r, DTM.findColumn("id_sound"))));
                    if (ret < snd) {
                        ret = snd;
                    }
                }
            }
        }
        return ret;
    }

    private static void Close() {
        AltMenu.toggleAlarmer.setSelected(false);
    }

    @SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		btnDispConfirm = new javax.swing.JButton();
		javax.swing.JScrollPane alarmTableScroller = new javax.swing.JScrollPane();
		alarmTable = new javax.swing.JTable() {
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false; // Disallow the editing of any cell
			}
		};

		setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(0, 0, 0)));
		setClosable(true);
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(true);
		setTitle("Уведомления");
		setVisible(true);
		addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
			public void internalFrameActivated(
					javax.swing.event.InternalFrameEvent evt) {
			}

			public void internalFrameClosed(
					javax.swing.event.InternalFrameEvent evt) {
			}

			public void internalFrameClosing(
					javax.swing.event.InternalFrameEvent evt) {
				formInternalFrameClosing();
			}

			public void internalFrameDeactivated(
					javax.swing.event.InternalFrameEvent evt) {
			}

			public void internalFrameDeiconified(
					javax.swing.event.InternalFrameEvent evt) {
			}

			public void internalFrameIconified(
					javax.swing.event.InternalFrameEvent evt) {
			}

			public void internalFrameOpened(
					javax.swing.event.InternalFrameEvent evt) {
			}
		});

		btnDispConfirm.addActionListener((java.awt.event.ActionEvent evt) -> btnDispConfirmActionPerformed());

		alarmTable.setModel(DTM);
		alarmTable
				.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		alarmTableScroller.setViewportView(alarmTable);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(btnDispConfirm)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(alarmTableScroller,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										748, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(btnDispConfirm)
								.addGap(0, 140, Short.MAX_VALUE))
				.addComponent(alarmTableScroller,
						javax.swing.GroupLayout.PREFERRED_SIZE, 0,
						Short.MAX_VALUE));

		pack();
	}// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing() {//GEN-FIRST:event_formInternalFrameClosing
        Close();
        setVisible(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnDispConfirmActionPerformed() {//GEN-FIRST:event_btnDispConfirmActionPerformed
        int r = alarmTable.getSelectedRow();
        if (Terminal.DSP) {
            if (r >= 0) {// Проверить выбрана ли строчка!
                String ALARM_ID = String.valueOf(DTM.getValueAt(r, DTM.findColumn("#")));
//                System.out.println("send kvitirovaie confirm " + String.valueOf(DTM.getValueAt(r, 0)));
                Log.log("send kvitirovaie confirm " + String.valueOf(DTM.getValueAt(r, DTM.findColumn("#"))));
                Net.Send("ALARM:" + ALARM_ID);// send command kvitirovanie
            }
        } else {
            //javax.swing.JOptionPane.showMessageDialog(null, RightFault);
            javax.swing.JOptionPane.showMessageDialog(null, "RightFault");
        }
    }//GEN-LAST:event_btnDispConfirmActionPerformed

    private void LineChanged(int r) {
        if (String.valueOf(DTM.getValueAt(r, DTM.findColumn("ДСП"))).equals("-")) {
            btnDispConfirm.setEnabled(true);
        } else {
            btnDispConfirm.setEnabled(false);
        }
    }

    static int save_alarm() {
        int ok = 0;
        if (Terminal.DSP){
        File f = new File(Terminal.ATTRANS_HOME + Terminal.FS + "log" + Terminal.FS + Terminal.MPC_NAME + ".alarm");
        FileOutputStream fos = null;
        BufferedWriter writer = null;
        try {
            fos = new FileOutputStream(f, false);
            writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            for (int r = 0; r < DTM.getRowCount(); r++) {
                String key = String.valueOf(DTM.getValueAt(r, DTM.findColumn("#"))) + ",";
                String dt = String.valueOf(DTM.getValueAt(r, DTM.findColumn(date))) + ",";
                String res = String.valueOf(DTM.getValueAt(r, DTM.findColumn(Restored))) + ",";
                String dsp = String.valueOf(DTM.getValueAt(r, DTM.findColumn("ДСП")));
//                    System.out.println("key "+key +" dt "+ dt + " res "+res+" dsp "+dsp);
                writer.write(key + dt + res + dsp + "\n");
                writer.flush();
                ok ++;
            }
        } catch (IOException e) {
            ok = 0;
            Err.err(e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                ok = 0;
                Err.err(e);
            }
        }
        }//end if
        return ok;
    }

    static void load_alarm() {
        if (Terminal.DSP){
        File f = new File(Terminal.ATTRANS_HOME + Terminal.FS + "log" + Terminal.FS + Terminal.MPC_NAME + ".alarm");
        FileInputStream fis = null;
        BufferedReader reader = null;
        try {
            fis = new FileInputStream(f);
            reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line = "";
            while (line != null) {// с 0 строки по конец файла. (цикл по файлу)
                line = reader.readLine();
                if (line != null) {
                    if (!line.isEmpty()) {
                        String s[] = line.split(",");
                        if (s.length == 4) {
                            String[] key = s[0].split(":");//id
                            String dt = s[1];//dt
                            String res = s[2];//restored
                            String dsp = s[3];//dsp
//System.out.println("key0 "+key[0] +" key1 "+key[1] +" dt "+ dt + " res "+res+" dsp "+dsp);
                            add_alarm(dt, res, Integer.valueOf(key[1]), Integer.valueOf(key[0]), dsp);
                        }
                    }
                }
            }
        } catch (IOException e) {
            Err.err(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                Err.err(e);
            }
        }
    }
    }//end if
    // Variables declaration - do not modify//GEN-BEGIN:variables
    
    // End of variables declaration//GEN-END:variables
}

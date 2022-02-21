//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JTable;
import ru.attrans.proc.objs.CRC_PLC;

class PLC extends javax.swing.JPanel {

    
//    private static final long serialVersionUID = 1L;
    static String crc = "0000000000000000";

    private final String ip_address;

    private final int id_obj;
    private final int port_ind;// порт индикации
    private final int port_cmd_r;// порт чтения команд (зачем разные порты??)
    private final int port_cmd_w;// порт записи команд (зачем разные порты??)
    private final int port_partner;// порт сименса send/receive - он подключается
    boolean read_IND = false; // 2 переменная можно читать индикацию или нет
    // (если соединение установилось то true.)
    private long dtime;// последнее время
    private boolean connection = false; // 0 подключение
    private boolean S7_work = false; // 1 если сименс не приконектился - false
    private boolean read_CMD = false; // 3 переменная можно читать команды или
    // нет (если соединение установилось то
    // true.)
    private boolean write_CMD = false; // 4 переменная если соединение
    // установилось то true.
    private boolean Main_S7 = false; // 5 главный цпу
    private javax.swing.JTable statusTable;
    

    PLC(
            int ID_OBJ, // ID_OBJ
    //        int ID_AREA, // ID_AREA
            String S_NAME,// S_NAME                
    //        String MPC_NAME, // MPC_NAME
            String IP_CPU, // IP_CPU
    //        int MERKER_WORK, // MERKER_WORK
    //        int MERKER_MASTER, // MERKER_MASTER
    //        int BIT_MASTER, // BIT_MASTER
    //        int ID_PARTNER, // ID_PARTNER
            int PORT_IND, // PORT_IND
            int PORT_CMD_W, // PORT_CMD_W
            int PORT_CMD_R, // PORT_CMD_R
    //        int PORT_DIA, // PORT_DIA
            int PORT_PARTNER // PORT_PARTNER

    ) {
        initComponents();
        id_obj = ID_OBJ;
        ip_address = IP_CPU;
        port_ind = PORT_IND;
        port_cmd_r = PORT_CMD_R;
        port_cmd_w = PORT_CMD_W;
        port_partner = PORT_PARTNER;// это и есть диагностика - читаем по этому порту из сименса его статус

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), S_NAME));
        initTable();
        setTable();
    }

    private void initTable() {
        // while (dtm.getRowCount() > 0) {
        // dtm.removeRow(0);
        // }
        // dtm.setColumnCount(3);
        //
        //
        // dtm.setColumnIdentifiers(new Object[]{/*"#",*/"Параметр", "Значение",
        // "Состояние"});

        statusTable.getTableHeader().setFont(Terminal.SANS11);
        statusTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        statusTable.getTableHeader().setReorderingAllowed(false);

        statusTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Параметр
        statusTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Значение
        statusTable.getColumnModel().getColumn(2).setPreferredWidth(80); // Состояние
    }

    void setStatus(String s) {
        // System.out.println("SET STATUS " + s);
        int val = Integer.valueOf(s); // значение от 0 до 256 (байт)
        if (val == 32_771){
//            event(203);//?? Выключен ??
            alarm_on();
        }
        if (val == 32_799){
//            event(220);//Восстановлена связь с ЦПУ
            alarm_off();
        }
        connection = (1 & val) != 0; // 0 бит
        S7_work = (1 << 1 & val) != 0;// 1 бит
        read_IND = (1 << 2 & val) != 0;// 2 бит
        read_CMD = (1 << 3 & val) != 0;// 3 бит
        write_CMD = (1 << 4 & val) != 0;// 4 бит
        Main_S7 = (1 << 5 & val) != 0;// 5 бит
        setTable();
    }
    
    private void setState(long DTIME, byte[] b) {
        setDtime(DTIME);
        crc = Util.bytes2Hex(b);
    }
        
    //@Override
    public void setState(Object oid){
        CRC_PLC c = (CRC_PLC) oid;
        setState(c.timestamp,
                c.rawData// bytearray
        );
    }
    
    private void setDtime(long DTIME) {
        dtime = DTIME;
    }
    
    private long getDtime() {
        return dtime;
    }
    
    private void setTable() {

        statusTable.getModel().setValueAt(ip_address, 0, 1);
        statusTable.getModel().setValueAt(connection, 0, 2);
        statusTable.getModel().setValueAt(S7_work, 1, 2);// Режим работа(RUN)

        statusTable.getModel().setValueAt(port_ind, 2, 1);// номер порта записи
        // индикация
        statusTable.getModel().setValueAt(read_IND, 2, 2);// - работает или нет

        statusTable.getModel().setValueAt(port_cmd_w, 3, 1);// номер порта
        // записи чтение
        statusTable.getModel().setValueAt(read_CMD, 3, 2);// - работает или нет

        statusTable.getModel().setValueAt(port_cmd_r, 4, 1);// номер порта
        // записи команд
        statusTable.getModel().setValueAt(write_CMD, 4, 2);// порт команд запись
        // - работает или
        // нет

        statusTable.getModel().setValueAt(port_partner, 5, 1); // номер порта в
        // базе в
        // сименсе для
        // чтения
        // статуса
        // сименса
        statusTable.getModel().setValueAt(Main_S7, 5, 2);// Главный ЦПУ
    }

    @SuppressWarnings("unchecked")// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
		statusTable = new javax.swing.JTable();
        javax.swing.JButton jButton1 = new javax.swing.JButton();

		setBorder(javax.swing.BorderFactory.createTitledBorder(
				javax.swing.BorderFactory.createLineBorder(new java.awt.Color(
						0, 0, 0)), "CPU"));

		statusTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { "Соединение", null, null },
						{ "Режим Работа(\"RUN\")", null, null },
						{ "Порт индикация", null, null },
						{ "Порт команд чтение", null, null },
						{ "Порт команд запись", null, null },
						{ "Главный ЦПУ", null, null } }, new String[] {
						"Параметр", "Значение", "Состояние" }) {
			final Class[] types = new Class[] { java.lang.String.class,
					java.lang.String.class, java.lang.Boolean.class };
			final boolean[] canEdit = new boolean[] { false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jScrollPane1.setViewportView(statusTable);

		jButton1.setText("Подробно");
		jButton1.addActionListener((java.awt.event.ActionEvent evt) -> jButton1ActionPerformed());

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE).addComponent(jButton1)
								.addContainerGap()).addComponent(jScrollPane1));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addComponent(jScrollPane1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										129,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jButton1)
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));
	}// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed() {// GEN-FIRST:event_jButton1ActionPerformed
        browse();// Start browser
    }// GEN-LAST:event_jButton1ActionPerformed

    private void browse() {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop()
                : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                // String [] s = ((String)
                // jComboBox1.getSelectedItem()).split(":");
                // desktop.browse(new URL("http://"+s[1]).toURI());
                desktop.browse(new URL("http://"
                        + statusTable.getModel().getValueAt(0, 1)).toURI());

                // desktop.browse(new URI((String)
                // statusTable.getModel().getValueAt(0, 1)));
            } catch (IOException | URISyntaxException e) {
                Err.err(e);
            }
        }
        // statusTable.getModel().getValueAt(0, 1));
    }

// --Commented out by Inspection START (16.01.18 15:00):
//    private void event(int id_msg) {
//        if (getDtime() > 0) {
//            Events.InsertMessage(getDtime(), id_obj, id_msg);
//        }
//    }
// --Commented out by Inspection STOP (16.01.18 15:00)

    private void alarm_on() {
        if (getDtime() > 0) {
            Alarms.alarm_on(getDtime(), 121, id_obj);
        }
    }

    private void alarm_off() {
        if (getDtime() > 0) {
            Alarms.alarm_off(getDtime(), 121, id_obj);
        }
    }

}

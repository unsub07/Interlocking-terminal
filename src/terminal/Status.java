//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
//freeCell
package terminal;

import static terminal.Commander.cmdLayers;
import static terminal.Terminal.PLCs_Hash;

class Status {

    private static final javax.swing.JLabel lblCmdEmpty = new javax.swing.JLabel();
    static boolean work = true;
    private static String status_not_managed = "Недостоверные данные! Управление невозможно!";//tanslate
//    private static String status_not_managed = Util.getTxt("status_not_managed");//translated

    void setState() {
        boolean WORK = Net.logged && get_Conroller_Connection();
        if (work != WORK) {
            work = WORK;
            if (WORK) {
                cmdLayers.setBackground(new java.awt.Color(221, 237, 252));
                lblCmdEmpty.setVisible(false);
            } else {
                cmdLayers.setBackground(new java.awt.Color(204, 204, 204));// серый
                setStatus("STATUS:0:0:0:#:");
                lblCmdEmpty.setVisible(true);
            }

        }
    }
    
    void SetCmd(boolean WORK) {
        //area
        Terminal.Area_Hash.keySet().forEach((Integer k) -> {
            Terminal.Area_Hash.get(k).SetCmd(WORK);
        });
        //dga
        Terminal.DGA_Hash.keySet().forEach((Integer k) -> {
            Terminal.DGA_Hash.get(k).SetCmd(WORK);
        });
        //heating
        Terminal.Heating_Hash.keySet().forEach((Integer k) -> {
            Terminal.Heating_Hash.get(k).SetCmd(WORK);
        });
        //mpab
        Terminal.Mpab_Hash.keySet().forEach((Integer k) -> {
            Terminal.Mpab_Hash.get(k).SetCmd(WORK);
        });
        //fan
        Terminal.FanCell_Hash.keySet().forEach((Integer k) -> {
            Terminal.FanCell_Hash.get(k).SetCmd(WORK);
        });
    }

    void setStatus(String st) {// тут надо распарсить строку которая пришла от сервака
//        Log.log("STATUS " + st);
//         0        1        2   3   4         
//      STATUS:1479217824914:0:32771:#:
        String[] s = st.split(":");
        // long z = Long.valueOf(s[1]);
        // Date d = new Date(z);
        // Clock.setTime(d);

//         System.out.println("STATUS : s[2] "+s[2]+"s[3] "+s[3]+" s[2] "+s[5]+" s[3] "+s[6]);
        // Controller cpu_obj;
        // for (int s : CPU_Hash.keySet()) {//цикл по всем процессорам сименс
        // cpu_obj = CPU_Hash.get(s);
        if (Commander.cpuMon != null) {
            System.out.println("st : " + st);
            System.out.println("len " + s.length);
            if (s.length == 5){
                PLCs_Hash.get(Integer.valueOf(s[2])).setStatus(s[3]);// CPU 0
            }
            //CpuMon.CPU_Hash.get(Integer.valueOf(s[5])).setStatus(s[6]);//CPU 1
        }
        // }

        // if ("0".equals(s[3]) && "0".equals(s[6])){//нет обоих сименсов
        //
        // }
        // boolean Controller_work =
        // (CPU_Hash.get(Integer.valueOf(s[2])).connection) ||
        // (CPU_Hash.get(Integer.valueOf(s[5])).connection);
        // setState();
    }

    private static boolean get_Conroller_Connection() {
        boolean Controller_work = false;
        PLC cpu_obj;
        for (int s : PLCs_Hash.keySet()) {// цикл по всем контроллерам
            cpu_obj = PLCs_Hash.get(s);
            Controller_work = Controller_work || cpu_obj.read_IND;
        }
        return Controller_work;
    }

    Status(int GX, int GY, int SHIFT_X, int SHIFT_Y) {
        lblCmdEmpty.setText(status_not_managed);
        int LAYER = 12;
        int cmdX = Cell.getRealX(GX, SHIFT_X);
        int cmdY = Cell.getRealY(GY, SHIFT_Y);// + 64;
        lblCmdEmpty.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.red, 2));
        lblCmdEmpty.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblCmdEmpty.setFont(Terminal.SANS12);
        lblCmdEmpty.setBackground(new java.awt.Color(255, 204, 204));
        lblCmdEmpty.setOpaque(true);
        lblCmdEmpty.setFocusable(false);
        int xx = lblCmdEmpty.getFontMetrics(lblCmdEmpty.getFont()).stringWidth(lblCmdEmpty.getText()) + 4;
        int yy = lblCmdEmpty.getFontMetrics(lblCmdEmpty.getFont()).getHeight() + 4;
        lblCmdEmpty.setBounds(cmdX, cmdY, xx, yy);
        lblCmdEmpty.setVisible(true);
        terminal.Commander.cmdLayers.add(lblCmdEmpty);
        terminal.Commander.cmdLayers.setLayer(lblCmdEmpty, LAYER);
        setState();
    }
}

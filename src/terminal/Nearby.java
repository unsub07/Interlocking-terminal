//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import ru.attrans.proc.objs.NearByState;

class Nearby extends Cell {

    private static final Color cmdIdleForeground = Color.DARK_GRAY;
    private static final Color cmdIdleBackground = Color.DARK_GRAY;
    private static final Border cmdIdleBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
    private static final Color cmdAlarmBackgroundHigh = new Color(255, 204, 204);
    private static final Color cmdAlarmBackgroundLow = new Color(155, 104, 104);
    private static final Border cmdAlarmBorderLow = BorderFactory.createLineBorder(new Color(255, 0, 0), 2);
    private static final Border cmdAlarmBorderHigh = BorderFactory.createLineBorder(new Color(170, 0, 0), 2);
    private final boolean direction;
    private final javax.swing.JLabel lbl = new javax.swing.JLabel();
//    private String s_name;
    private boolean alarm;
    private boolean alarmLow;
    private final ActionListener NEARBY1 = (ActionEvent e) -> {
        if (alarm) {
            alarmLow = !alarmLow;
            lbl.setBackground(cmdAlarmBackgroundHigh);
            if (alarmLow) {
                lbl.setBorder(cmdAlarmBorderLow);
                lbl.setBackground(cmdAlarmBackgroundLow);
            } else {
                lbl.setBorder(cmdAlarmBorderHigh);
                lbl.setBackground(cmdAlarmBackgroundHigh);
            }
        }
    };

    Nearby(
            int ID_OBJ, //0=ID_OBJ
    //        int ID_AREA, //1=ID_AREA
            String S_NAME, //2=S_NAME
    //        String MPC_NAME, //3=MPC_NAME
            int X, //4=X
            int Y, //5=Y
            int SHIFT_X, //6=SHIFT_X
            int SHIFT_Y, //7=SHIFT_Y
    //        int VIDEO_STATUS, //8=VIDEO_STATUS
            int ORIENTATION, //9=VIDEO_STATUS
    //        int LAYER, //10=LAYER
            //boolean futured,
            boolean DIRECTION //11-DIRECTION
    //        int IND//12-IND
//            int CMD //13-CMD
    ) {
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, 0, 24);// 24=NEARBY
        direction = DIRECTION;
//        s_name = S_NAME;
//        tooltip = S_NAME;
        s_name = ((DIRECTION) ? "▶▶▶" : "◀◀◀");
        setPanes(ORIENTATION);
    }

    private void setPanes(int ORIENTATION) {
        lbl.setFont(Terminal.SANS14);
        lbl.setForeground(cmdIdleForeground);

        int cmdW = lbl.getFontMetrics(lbl.getFont()).stringWidth(s_name);
        int cmdH = lbl.getFontMetrics(lbl.getFont()).getHeight();

        lbl.setBounds(0, 0, cmdW + 8, cmdH + 4);
        lbl.setOpaque(true);
        lbl.setBorder(cmdIdleBorder);
        lbl.setBackground(cmdIdleBackground);
        lbl.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lbl.setVerticalAlignment(javax.swing.JLabel.TOP);

        lbl.setText((direction) ? "<html><font color=gray>▶▶▶</font></html>" : "<html><font color=gray>◀◀◀</font></html>");
        
        switch (ORIENTATION) {
            case 3:
                shift_x = 48;
                shift_y = 24 - cmdH - 4; // готово
                break;
            case 6:
                shift_x = 48;
                shift_y = 36 - cmdH / 2 - 2; // готово
                break;
            case 9:
                shift_x = 48;
                shift_y = 48; // готово
                break;
            case 12:
                shift_x = 36 - cmdW / 2 - 2;
                shift_y = 48; // готово
                break;
            case 15:
                shift_x = 24 - cmdW - 4;
                shift_y = 48; // готово
                break;
            case 18:
                shift_x = 24 - cmdW - 4;
                shift_y = 36 - cmdH / 2 - 2; // готово
                break;
            case 21:
                shift_x = 24 - cmdW - 4;
                shift_y = 24 - cmdH - 4; // готово
                break;
            case 24:
                shift_x = 36 - cmdW / 2 - 2;
                shift_y = 24 - cmdH - 4; // готово
                break;
        }

        lbl.setSize(cmdW + 8, cmdH + 4);
        cmdX += shift_x;
        cmdY += shift_y;
        lbl.setLocation(cmdX, cmdY);
        lbl.setVisible(true);
        terminal.Commander.cmdLayers.add(lbl);
        terminal.Commander.cmdLayers.setLayer(lbl, CELLPANE_LAYER_10);//10
    }

    void setState(long DTIME, int STATE) {
        setDtime(DTIME);
        alarm = false;
        switch (STATE) {// int
            case 1:// "NOROUTE"://1 NOROUTE MESS_NEARBY_ROUT_OFF 270
                lbl.setText((direction) ? "<html><font color=gray>▶▶▶</html>" : "<html><font color=gray>◀◀◀</html>");
                lbl.setBorder(cmdIdleBorder);
                event(270);
                alarm_off();
                break;
            case 2:// "EMPTYROUTE"://2 EMPTYROUTE MESS_NEARBY_ROUT_ON 271
                lbl.setText((direction) ? "<html><font color=yellow>▶▶▶</html>" : "<html><font color=yellow>◀◀◀</html>");
                lbl.setBorder(cmdIdleBorder);
                event(271);
                alarm_off();
                break;
            case 4:// "BUSYROUTE"://4 BUSYROUTE MESS_NEARBY_TRAIN_ON 273
                lbl.setText((direction) ? "<html><font color=red>▶▶▶</html>" : "<html><font color=red>◀◀◀</html>");
                lbl.setBorder(cmdIdleBorder);
                event(273);
                alarm_off();
                break;
            case 8:// "FAULT"://8 FAULT MESS_NEARBY_FAULT_ON 269
                lbl.setText((direction) ? "<html><font color=red>▶▶▶</html>" : "<html><font color=red>◀◀◀</html>");
                alarm = true;
                event(269);
                alarm_on();
                break;
            case 12:// "FAULT"://8 FAULT MESS_NEARBY_FAULT_ON 269
                lbl.setText((direction) ? "<html><font color=red>▶▶▶</html>" : "<html><font color=red>◀◀◀</html>");
                alarm = true;
                event(269);
                alarm_on();
                break;
            default:
                Log.log(" nearbay state: " + STATE);
        }

        if (alarm) {
            Terminal.TIMER600.addActionListener(NEARBY1);
        } else {
            Terminal.TIMER600.removeActionListener(NEARBY1);
            lbl.setForeground(cmdIdleForeground);
            lbl.setBackground(cmdIdleBackground);
            lbl.setBorder(cmdIdleBorder);
            lbl.repaint();
        }
    }

    private void event(int id_msg) {
        if (getDtime() > 0) {
            Events.InsertMessage(getDtime(), id_obj, id_msg);
        }
    }

    private void alarm_on() {
        if (getDtime() > 0) {
            Alarms.alarm_on(getDtime(), 135, id_obj);
        }
    }

    private void alarm_off() {
        if (getDtime() > 0) {
            Alarms.alarm_off(getDtime(), 135, id_obj);
        }
    }

    @Override
    public void setState(Object oid) {
        NearByState n = (NearByState) oid;
        this.setState(n.timestamp, n.STATE);
    }
}

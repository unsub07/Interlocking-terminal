//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

import ru.attrans.proc.objs.HeatState;

class Heat extends javax.swing.JLabel {


    private final int id_obj;
    private long dtime;// последнее время
    private boolean hot_on;//	1 - включен
    private boolean hot_err;//	1 - неисправность (в шкафу)
    private boolean hot_gnd = true;//	0 - нарушение изоляции

    Heat(int ID_OBJ, String S_NAME) {
        super();
        id_obj = ID_OBJ;
        setPreferredSize(new java.awt.Dimension(22, 22));
        setHorizontalAlignment(javax.swing.JLabel.CENTER);
        setVerticalAlignment(javax.swing.JLabel.CENTER);
        setFont(Terminal.SANS13);
        setOpaque(true);
        setBackground(java.awt.Color.DARK_GRAY);
//        setText("<html><font color=white size=2>" + "вкл."  + "</font><br>" + S_NAME + "</html>");
        setText(S_NAME);
    }

    private void setOn(boolean HOT_ON) {
//        hot_on = HOT_ON;
        setBackground((HOT_ON) ? java.awt.Color.GREEN : java.awt.Color.GRAY);
    }

    private void setErr(boolean HOT_ERR) {
//        hot_err = HOT_ERR;
//        setToolTipText("неисправность (в шкафу)");
        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK, 1), javax.swing.BorderFactory.createLineBorder(((HOT_ERR) ? java.awt.Color.RED : java.awt.Color.GREEN), ((HOT_ERR)) ? 3 : 1)));
//        setForeground((HOT_ERR) ? java.awt.Color.PINK : java.awt.Color.GREEN);
    }

    private void setGnd(boolean HOT_GND) {
//        hot_err = HOT_GND;
//        setToolTipText("нарушение изоляции");
//        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK, 1), javax.swing.BorderFactory.createLineBorder(((HOT_GND) ? java.awt.Color.RED : java.awt.Color.GREEN), ((HOT_GND)) ? 3 : 1)));
        setForeground((!HOT_GND) ? java.awt.Color.RED : java.awt.Color.BLACK);
    }

    private long getDtime() {
        return dtime;
    }

    private void setDtime(long DTIME) {
        dtime = DTIME;
    }

    void setState(
            long DTIME,
            boolean HOT_ON,
            boolean HOT_ERR,
            boolean HOT_GND
    ) {
        setDtime(DTIME);
// ---------------------------------------------------------------------
        if (hot_on != HOT_ON) {
            hot_on = HOT_ON;
            if (HOT_ON) {
                event(381);
            } else {
                event(380);
            }
        }
// ---------------------------------------------------------------------
        if (hot_err != HOT_ERR) {
            hot_err = HOT_ERR;
            if (HOT_ERR) {//on - 1
                event(383);
                alarm_on(387);
                setToolTipText("неисправность (в шкафу)");
            } else {//off - 0
                event(385);
                alarm_off(387);
                setToolTipText("");
            }
        }
// ---------------------------------------------------------------------
        if (hot_gnd != HOT_GND) {
            hot_gnd = HOT_GND;
            if (!HOT_GND) {
                setToolTipText("нарушение изоляции");
                event(382);//ON - 0
                alarm_on(386);
            } else {
                setToolTipText("");
                event(384);//OFF - 1
                alarm_off(386);
            }
        }
// ---------------------------------------------------------------------
        setOn(HOT_ON);
        setErr(HOT_ERR);
        setGnd(HOT_GND);
    }

    private void event(int id_msg) {
        if (getDtime() > 0) {
            Events.InsertMessage(getDtime(), id_obj, id_msg);
        }
    }

    private void alarm_on(int id) {
        if (getDtime() > 0) {
            Alarms.alarm_on(getDtime(), id, id_obj);
        }
    }

    private void alarm_off(int id) {
        if (getDtime() > 0) {
            Alarms.alarm_off(getDtime(), id, id_obj);
        }
    }

//    @Override
    public void setState(Object oid) {
        HeatState h = (HeatState) oid;
        this.setState(
                h.timestamp,
                h.HOT_ON,
                h.HOT_ERR,
                h.HOT_GND
        );
    }

}

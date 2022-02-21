//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

import javax.swing.event.PopupMenuEvent;
import ru.attrans.proc.objs.GateState;

class Gate extends Cell {
    
    private static final int LAYER = 14;
    boolean on;
    private int gate;// = true;
//    private final javax.swing.JLabel lblGateClose = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblGateOpen = new javax.swing.JLabel();
    private final Img lblGateClose = new Img();
    private final Img lblGateOpen = new Img();
    
    private final String menu_title;
    private final javax.swing.JPopupMenu Popup = new javax.swing.JPopupMenu();// меню
    private final javax.swing.JMenuItem SimOpenItem = new javax.swing.JMenuItem("<html>Симулятор: <b><font color=blue>Открыть</font></b></html>");
    private final javax.swing.JMenuItem SimCloseItem = new javax.swing.JMenuItem("<html>Симулятор: <b><font color=blue>Закрыть</font></b></html>");
//    int byteNumber = -1;
//    int bitNumber = -1;

    Gate(
            int ID_OBJ, //0=ID_OBJ
//            int ID_AREA, //1=ID_AREA
//            String S_NAME, //2=S_NAME
//            String MPC_NAME, //3=MPC_NAME
            int X, //3=X
            int Y, //4=Y
            int SHIFT_X, //5=SHIFT_X
            int SHIFT_Y, //6=SHIFT_Y
            int VIDEO_STATUS, //7=VIDEO_STATUS
            int ORIENTATION //8=ORIENTATION
//            int IND //11=IND
//            int CMD //12=CMD
    ) {
        super(ID_OBJ, "GATE", X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 21);//21 - GATE
        menu_title = "<html><b>" + Util.objType(ID_OBJ) + " <font color=blue></font></b></html>";
        if (VIDEO_STATUS != 2) {//невидимый
            setPanes(ORIENTATION);
        }
    }

    // picture prefixes
    private String cmdPicturePrefix(int ORIENTATION) {
        return "gate__" + String.valueOf(ORIENTATION) + "__";
    }

    private void setPanes(int ORIENTATION) {

        cmdX += Terminal.TunePngX;// -6
        cmdY += Terminal.TunePngY;// -6

        Util.prep_lbl(lblGateClose, LAYER, cmdPicturePrefix(ORIENTATION) + "off", cmdX, cmdY);
        Util.prep_lbl(lblGateOpen, LAYER, cmdPicturePrefix(ORIENTATION) + "on", cmdX, cmdY);

        if (Terminal.SIM) {
            setPopup();
        }

        lblGateClose.setVisible(false);
        lblGateOpen.setVisible(true);
    }

    private void setPopup() {
        if (Terminal.SIM) {//
            Popup.setBorder(javax.swing.BorderFactory.createTitledBorder(menu_title));
            Popup.setBorderPainted(true);
            SimOpenItem.addActionListener(evt -> SimOnItemActionPerformed());
            Popup.add(SimOpenItem);
            SimCloseItem.addActionListener(evt -> SimOffItemActionPerformed());
            Popup.add(SimCloseItem);
            Popup.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    preparePopup();
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            });
            lblGateOpen.setComponentPopupMenu(Popup);
            lblGateClose.setComponentPopupMenu(Popup);
        }
    }

    private void preparePopup() {
        if (Util.GetUserGroups("SIM")) {
            SimOpenItem.setEnabled(!on && Status.work);
            SimCloseItem.setEnabled(on && Status.work);
        }
    }

    private void SimOnItemActionPerformed() {
//        if ((byteNumber != -1) | (bitNumber != -1)) {
            //Net.sendDirectCmd_SIM(12, String.valueOf(byteNumber) + "." + String.valueOf(bitNumber) + ".1");//SIEMENS //включить
            Net.sendMaskedCmd_SIM(id_obj, "GATE_SIM_ON");
//        }
    }

    private void SimOffItemActionPerformed() {
//        if ((byteNumber != -1) | (bitNumber != -1)) {
            //Net.sendDirectCmd_SIM(12, String.valueOf(byteNumber) + "." + String.valueOf(bitNumber) + ".0");//SIEMENS //выключить
            Net.sendMaskedCmd_SIM(id_obj, "GATE_SIM_OFF");
//        }
    }

    void setState(
            long DTIME,
            int GATE
    ) {
        if (vStatus != 2) {//невидимый
            setDtime(DTIME);

            if (gate != GATE) {
                gate = GATE;
                if (GATE == 1) {
                    lblGateClose.setVisible(false);
                    lblGateOpen.setVisible(true);
                    event(215);//Открыт
                } else {
                    lblGateOpen.setVisible(false);
                    lblGateClose.setVisible(true);
                    event(207);//закрыт
                }
            }
        }
    }

    private void event(int id_msg) {
        if (getDtime() > 0) {
            Events.InsertMessage(getDtime(), id_obj, id_msg);
        }
    }

    @Override
    public void setState(Object oid) {
        GateState g = (GateState) oid;
        this.setState(
                g.timestamp,
                g.GATE
        );
    }

}

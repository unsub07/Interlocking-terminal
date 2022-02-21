//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

import javax.swing.event.PopupMenuEvent;
import ru.attrans.proc.objs.ElevatedTrackState;

public class ElevatedTrack extends Cell {

    private static final int LAYER = 1;
    private boolean on;// = true;
//    private final javax.swing.JLabel lblCmdOff = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdOn = new javax.swing.JLabel();
    private final Img lblCmdOff = new Img();
    private final Img lblCmdOn = new Img();

    private final String menu_title;
    private final javax.swing.JPopupMenu Popup = new javax.swing.JPopupMenu();// меню
    private final javax.swing.JMenuItem SimOnItem = new javax.swing.JMenuItem("<html>Симулятор: <b><font color=blue>Включить</font></b></html>");
    private final javax.swing.JMenuItem SimOffItem = new javax.swing.JMenuItem("<html>Симулятор: <b><font color=blue>Выключить</font></b></html>");

    ElevatedTrack(
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
        super(ID_OBJ, "ELEVATEDTRACK", X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 82);//82 - ELEVATEDTRACK
        menu_title = "<html><b>" + Util.objType(ID_OBJ) + " <font color=blue></font></b></html>";
        setPanes(ORIENTATION);
    }

    // picture prefixes
    private String cmdPicturePrefix(int ORIENTATION) {
        return "elevatedtrack__" + String.valueOf(ORIENTATION) + "__";
    }

    private void setPanes(int ORIENTATION) {
        int orient;
        if (ORIENTATION==12 | ORIENTATION==24){
            orient = 12;
        } else {
            orient = 6;
        }

        cmdX += Terminal.TunePngX;// -6
        cmdY += Terminal.TunePngY;// -6
        
        Util.prep_lbl(lblCmdOff, LAYER, cmdPicturePrefix(orient) + "off", cmdX, cmdY);
        Util.prep_lbl(lblCmdOn, LAYER, cmdPicturePrefix(orient) + "on", cmdX, cmdY);

        if (Terminal.SIM) {
            setPopup();
        }

        lblCmdOff.setVisible(true);
        lblCmdOn.setVisible(false);
    }

    private void setPopup() {
        if (Terminal.SIM) {//
            Popup.setBorder(javax.swing.BorderFactory.createTitledBorder(menu_title));
            Popup.setBorderPainted(true);
            SimOnItem.addActionListener(evt -> SimOnItemActionPerformed());
            Popup.add(SimOnItem);
            SimOffItem.addActionListener(evt -> SimOffItemActionPerformed());
            Popup.add(SimOffItem);
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
            lblCmdOn.setComponentPopupMenu(Popup);
            lblCmdOff.setComponentPopupMenu(Popup);
        }
    }

    private void preparePopup() {
        if (Util.GetUserGroups("SIM")) {
            SimOnItem.setEnabled(!on && Status.work);
            SimOffItem.setEnabled(on && Status.work);
        }
    }

    private void SimOnItemActionPerformed() {
//        if ((byteNumber != -1) | (bitNumber != -1)) {
            //Net.sendDirectCmd_SIM(12, String.valueOf(byteNumber) + "." + String.valueOf(bitNumber) + ".1");//SIEMENS //включить
            Net.sendMaskedCmd_SIM(id_obj, "ELEVATEDTRACK_SIM_ON");
//        }
    }

    private void SimOffItemActionPerformed() {
//        if ((byteNumber != -1) | (bitNumber != -1)) {
            //Net.sendDirectCmd_SIM(12, String.valueOf(byteNumber) + "." + String.valueOf(bitNumber) + ".0");//SIEMENS //выключить
            Net.sendMaskedCmd_SIM(id_obj, "ELEVATEDTRACK_SIM_OFF");
//        }
    }

    void setState(
            long DTIME,
            boolean ON
    ) {
        setDtime(DTIME);

        if (on != ON) {
            on = ON;
            if (ON) {
                lblCmdOff.setVisible(false);//yelow
                lblCmdOn.setVisible(true);
                event(400);//400	<html>Эстакада пути % движение <font color=green>разрешено</font></html>	MESS_ELEVAT_OFF
            } else {
                lblCmdOn.setVisible(false);//green  
                lblCmdOff.setVisible(true);
                event(399);//399	<html>Эстакада пути % движение <font color=orange>запрещено</font></html>	MESS_ELEVAT_ON
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
        ElevatedTrackState e = (ElevatedTrackState) oid;
        this.setState(
                e.timestamp,
                e.TRACK_ON
        );
    }

}

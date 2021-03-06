//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

import ru.attrans.proc.objs.PusherState;

public class Pusher extends Cell {

    private static final int LAYER = 1;
    private boolean canceled;// Погашенный - Серый (Нет согласия на установку маршрута) 
    private boolean consentReceived;// Получено согласие - Желтый (Получено согласие на задание маршрута) 
    private boolean routeSet;// Задан Маршрут - Зелёный 
    
    private final Img lblCmdOff = new Img();// Получено согласие - Желтый (Получено согласие на задание маршрута) 
    private final Img lblCmdOn = new Img();// Задан Маршрут - Зелёный 
//    private final Img lblCmdRed = new Img();
    private final Img lblCmdGray = new Img();// Погашенный - Серый (Нет согласия на установку маршрута) 

//    private final String menu_title;

    Pusher(
            int ID_OBJ, //0=ID_OBJ
//            int ID_AREA, //1=ID_AREA
            String S_NAME, //2=S_NAME
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
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 29);//29 - PUSHER
//        menu_title = "<html><b>" + Util.objType(ID_OBJ) + " <font color=blue></font></b></html>";

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
        
        Util.prep_lbl(lblCmdOff, LAYER, cmdPicturePrefix(orient) + "off", cmdX, cmdY);// Получено согласие - Желтый (Получено согласие на задание маршрута) 
        Util.prep_lbl(lblCmdOn, LAYER, cmdPicturePrefix(orient) + "on", cmdX, cmdY);// Задан Маршрут - Зелёный 
//        Util.prep_lbl(lblCmdRed, LAYER, cmdPicturePrefix(orient) + "r", cmdX, cmdY);
        Util.prep_lbl(lblCmdGray, LAYER, cmdPicturePrefix(orient) + "g", cmdX, cmdY);// Погашенный - Серый (Нет согласия на установку маршрута) 
        
        lblCmdGray.setToolTipText(s_name);
        lblCmdOff.setToolTipText(s_name);
        lblCmdOn.setToolTipText(s_name);
        lblCmdGray.setVisible(true);
        lblCmdOff.setVisible(false);
        lblCmdOn.setVisible(false);
    }

    void setState(
            long DTIME,
            boolean CANCELED,// Погашенный - Серый (Нет согласия на установку маршрута) 
            boolean CONSENTRECEIVED,// Получено согласие - Желтый (Получено согласие на задание маршрута) 
            boolean ROUTESET// Задан Маршрут - Зелёный 
    ) {
        setDtime(DTIME);

        if (canceled != CANCELED) {
            canceled = CANCELED;
            if (CANCELED) {
                lblCmdGray.setVisible(true);//gray
                lblCmdOff.setVisible(false);//yelow
                lblCmdOn.setVisible(false);//green
                event(410);//400	// Погашенный - Серый (Нет согласия на установку маршрута) 
            }
        }
        //----------------------------------------------------------------------
        if (consentReceived != CONSENTRECEIVED) {
            consentReceived = CONSENTRECEIVED;
            if (CONSENTRECEIVED) {
                lblCmdGray.setVisible(false);//gray
                lblCmdOff.setVisible(true);//yelow
                lblCmdOn.setVisible(false);//green
                event(411);//400	// Получено согласие - Желтый (Получено согласие на задание маршрута) 
            }
        }
        //----------------------------------------------------------------------
            if (routeSet != ROUTESET) {
            routeSet = ROUTESET;
            if (ROUTESET) {
                lblCmdGray.setVisible(false);//gray
                lblCmdOff.setVisible(false);//yelow
                lblCmdOn.setVisible(true);//green
                event(412);//400	// Задан Маршрут - Зелёный 
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
        PusherState p = (PusherState) oid;
        this.setState(
                p.timestamp,
                p.canceled,// Погашенный - Серый (Нет согласия на установку маршрута)  410
                p.consentReceived,// Получено согласие - Желтый (Получено согласие на задание маршрута)  411
                p.routeSet// Задан Маршрут - Зелёный  412
        );
    }
   
}

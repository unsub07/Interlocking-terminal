//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
/*
 1-FUTURE-строительство, 2-INVISIBLE-невидимый, 3-DEPRECATE-н.е.н.х.

нужно нарисовать неуправляемую стрелку!!!
                plus = true;
                minus = true;
                moving = false;
                lblCmdBusyRoutePlus.setVisible(false);
                lblCmdBusyRouteMinus.setVisible(false);
                lblCmdBusyPlus.setVisible(false);
                lblCmdBusyMinus.setVisible(false);
 */
package terminal;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.event.PopupMenuEvent;
import ru.attrans.proc.objs.TurnoutState;

class Turnout extends Cell {

    private boolean tr_back;// ?
    private boolean tr_fault;// ?
    private static final int TURNOUT_SIGN_PLUS_LR = 12;
    private static final int TURNOUT_SIGN_MINUS_LR = 13;
    private static final int TURNOUT_AVTOREV_LR = 14;
    // private static final int TURNOUT_UNKNOWN_POS = 15;
    private static final int TURNOUT_MOVING_LR = 16;
    private static final int TURNOUT_LOST_CONTROL_LR = 17;
    private static final int TURNOUT_NAME_LR = 18;

    private static final java.awt.Cursor TOUT_CURSOR = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(Terminal.mainPictureHash.get("tout_cursor").getImage(), new java.awt.Point(0, 0), "tout_cursor");
    final int inPut;// input direction
    final int outPut;// output direction
    final int o;
    final int s;
    final boolean plus_dir;
    final int rn_id; // ID_OBJ - рельсовой цепи
    // -------------------------------Translate--------------------------------------
    private final String menu_title;

    // ВСЕ, ЧТО ОТВЕЧАЕТ ЗА СТАТИКУ
    // private int routestate;//1 - EMPTY, 2 - ROUTE Поездной маршрут, 0 - M_ROUTE маневровый маршрут
    // ------------------------------------------------------------------------------
//    private final int vStatus;
    // private int SIDE_OUT;// output direction
    private final boolean backwarding;// автовозврат есть или нет (1 - true - есть автовозврат)
    // private int mainPosition;//нормальное положение стрелки если есть автовозврат
    private final int twin;// парная граф. стрелка, если съезд; если 0 - одиночная (нужно для рисования менюшек) ID_OBJ
    final int cmdSender;// лог. стрелка, к которой подключен управляющий блок; если одиночная - наш лог. объект, если съезд - главный лог. объект в съезде
//    private final boolean directPlus;// direct plus
//    private final String s_name;// turnout name
    private final boolean main;// главная стрелка
    private final javax.swing.JPopupMenu Popup = new javax.swing.JPopupMenu();
    private final javax.swing.JMenuItem toutPlusItem = new javax.swing.JMenuItem("<html>перевод в <b><font color=green>ПЛЮС</font></b></html>");
    private final javax.swing.JMenuItem toutMinusItem = new javax.swing.JMenuItem("<html>перевод в <b><font color=orange>МИНУС</font></b></html>");
    private final javax.swing.JMenuItem toutAuxPlusItem = new javax.swing.JMenuItem("<html>вспомогательный перевод в <b><font color=green>ПЛЮС</font></b></html>");
    private final javax.swing.JMenuItem toutAuxMinusItem = new javax.swing.JMenuItem("<html>вспомогательный перевод в <b><font color=orange>МИНУС</font></b></html>");
    private final javax.swing.JMenuItem toutBlockItem = new javax.swing.JMenuItem("<html>блокировка снята, <b><font color=orange>установить</font></b></html>");
    private final javax.swing.JMenuItem toutUnBlockItem = new javax.swing.JMenuItem("<html>блокировка установлена, <b><font color=green>снять</font></b></html>");
    private final javax.swing.JMenuItem toutFannItem = new javax.swing.JMenuItem("<html>обдувка, <b><font color=orange>включить</font></b></html>");

    private final javax.swing.JMenuItem trSimFaultItem = new javax.swing.JMenuItem("<html>Симулятор, <b><font color=blue>потеря контроля</font></b></html>");
    private final javax.swing.JMenuItem trSimUnFaultItem = new javax.swing.JMenuItem("<html>Симулятор, <b><font color=blue>вост. контроль</font></b></html>");

    private final javax.swing.JLabel lblName = new javax.swing.JLabel();

    private final Img lblCmdEmptyPlus = new Img();
    private final Img lblCmdEmptyMinus = new Img();
    private final Img lblCmdRoutePlus = new Img();
    private final Img lblCmdRouteMinus = new Img();
    private final Img lblCmdRouteUnknown = new Img();
    private final Img lblCmdMRoutePlus = new Img();
    private final Img lblCmdMRouteMinus = new Img();
    private final Img lblCmdMRouteUnknown = new Img();
    private final Img lblCmdBusyPlus = new Img();
    private final Img lblCmdBusyMinus = new Img();
    private final Img lblCmdBusyUnknown = new Img();
    private final Img lblCmdBusyRoutePlus = new Img();
    private final Img lblCmdBusyRouteMinus = new Img();
    private final Img lblCmdBlockPlus = new Img();
    private final Img lblCmdBlockMinus = new Img();
    private final Img lblCmdIRPlus = new Img();
    private final Img lblCmdIRMinus = new Img();
    private final Img lblCmdIRUnknown = new Img();
    private final Img lblCmdMoving = new Img();
    private final Img lblCmdPlusSign = new Img();
//    private final Img lblCmdAvtorev = new Img();
    private final Img lblCmdAvtorev1 = new Img();
//    private final Img lblCmdAvtorev2 = new Img();
    private final Img lblCmdMinusSign = new Img();
//    private final Img lblCmdToutfault = new Img();
    private final Img lblCmdToutfault1 = new Img();
    private final Img lblCmdToutfault2 = new Img();
    private final Img lblCmdFuture = new Img();
    private final Img lblVzrez = new Img();
    private final Img lblMenu = new Img();

    private final String S_plus, S_minus;
    boolean plus = true;// стрелка в ПЛЮСЕ (+)
    boolean minus;// стрелка в МИНУСЕ
    boolean toutInRoute = false;// стрелка в маршруте? (!)
    boolean fault = true;// что-то не так? (потеря контроля) (!) если falult = true то начальное сосстояние пришедшее из контроллера не поменяется на fault и не будет сообщения!!!
    boolean moving;// стрелка в движении
    int PositionState = 1;// 0-move, 1-plus, 2-minus, 3-prohibit +
    // А ВОТ ТУТ ТО, ЧЕМ ДЕЛАЕТСЯ ДИНАМИКА
    boolean toutBlocked;// блокирована?
    boolean toutFanned;// обдувается?
    private boolean busy = true;// секция занята? (!)
    private boolean route;// в поездном маршруте
    private boolean m_route = true;// в маневровом маршруте (+)
    boolean block;// блокирована РЦ
    private boolean ir;// под ИР //в стрелке нету исскуственной разделки ? - нужно только для того чтобы рисовалась картинка во время IR
    boolean backward;// требуется возврат
    boolean ToutFanCmd;// задана команда на обдувку
    boolean fanfltstate;// поломалась обдувка
    boolean err;// ошибочные действия
    private boolean vzrez;//Внимание!!! Опасность взреза стрелки!
    boolean nocontrol;//Внимание!!! Опасность стрелка без котроля положения!
    private Turnout mainLogObj;
    private final int dkp;
    private final int dkm;
    private final String name;
    boolean dk = true; // - так должно быть чтобы рисовалось или toutinroute=true 

    final int type;
    private final boolean dk_next_tr;
    private boolean dup;
    private boolean wfan;

    private final ActionListener TR_BACK = (ActionEvent evt) -> {
        tr_back = !tr_back;
        if (tr_back) {
            lblCmdAvtorev1.setVisible(false);
//            lblCmdAvtorev2.setVisible(true);
        } else {
//            lblCmdAvtorev2.setVisible(false);
            lblCmdAvtorev1.setVisible(true);
        }
    };

    private final ActionListener TR_FAULT = (ActionEvent evt) -> {
        tr_fault = !tr_fault;
        if (tr_fault) {
            lblCmdToutfault1.setVisible(false);
            lblCmdToutfault2.setVisible(true);
        } else {
            lblCmdToutfault2.setVisible(false);
            lblCmdToutfault1.setVisible(true);
        }
    };
    
    Turnout(
            int ID_OBJ, //0=ID_OBJ
            //        int ID_AREA, //1=ID_AREA
            String S_NAME, //2=S_NAME
            //        String MPC_NAME, //3=MPC_NAME
            int X, //4=X
            int Y, //5=Y
            int SHIFT_X, //6=SHIFT_X
            int SHIFT_Y, //7=SHIFT_Y
            int VIDEO_STATUS, //8=VIDEO_STATUS
            int ORIENTATION, //9=ORIENTATION
            int SIDE, //10=SIDE
            boolean PLUS_DIR,//11=PLUS_DIR
            int RN_ID, //12=RN_ID                
            int ID_TYPE, //13=ID_TYPE
            int ID_TWIN, //14=ID_TWIN
            boolean BACKWARD, //15=BACKWARD 1=требуется перевод сбрасывающей стрелки в начальное положение
            int DKP,
            int DKM,
            boolean WFAN
    //            int IND, // IND
    //            int CMD  // CMD            

    //            String TOUT_TYPE
    //            boolean DIRECTPLUS,
    //            int TWIN,
    //            int MAIN_TWIN
    ) {
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 17);// 17=TURNOUT
        // -------------------------------Translate--------------------------------------
        menu_title = "<html><b>" + Util.objType(ID_OBJ) + " <font color=blue>" + S_NAME + "</font></b></html>";

        toutPlusItem.setText(t_plus_menu_item);
        toutMinusItem.setText(t_minus_menu_item);
        toutAuxPlusItem.setText(t_auxplus_menu_item);
        toutAuxMinusItem.setText(t_auxminus_menu_item);
        toutBlockItem.setText(t_block_menu_item);
        toutUnBlockItem.setText(t_unblock_menu_item);
        // ---------------------------------------------------------------------
        toutPlusItem.setFont(Terminal.SANS14);
        toutMinusItem.setFont(Terminal.SANS14);
        toutAuxPlusItem.setFont(Terminal.SANS14);
        toutAuxMinusItem.setFont(Terminal.SANS14);
        toutBlockItem.setFont(Terminal.SANS14);
        toutUnBlockItem.setFont(Terminal.SANS14);
        trSimFaultItem.setFont(Terminal.SANS14);//if sim
        trSimUnFaultItem.setFont(Terminal.SANS14);//if sim          
        rn_id = RN_ID;
        backwarding = BACKWARD;
        dkp = DKP;
        dkm = DKM;
        dk_next_tr = dkm != 0 || dkp != 0;
//        vStatus = VIDEO_STATUS;

        inPut = Math.min(ORIENTATION, SIDE);
        outPut = Math.max(ORIENTATION, SIDE);
        o = ORIENTATION;
        s = SIDE;
        /*
        1	обычная	COMMON_TYPE
        2	сбрасывающая	CANCEL_TYPE
        3	охранная	PROTECT_TYPE
        4	неуправляемая	UNRULED_TYPE
        5	спаренная ведущая	TWIN_LEAD
        6	спаренная ведомая	TWIN_SLAVE
         */
//        main = ID_OBJ == MAIN_TWIN;// Писать сообщения только по главной стрелке!!!
//        if (ID_TYPE == 5) {main = true;} else {main = false;}
        main = ID_TYPE == 5;

        type = ID_TYPE;
        
        plus_dir = PLUS_DIR;
        if (PLUS_DIR) {
            S_plus = "__plus";
            S_minus = "__minus";
        } else {
            S_plus = "__minus";
            S_minus = "__plus";
        }

//        if (MAIN_TWIN == 0) {
//            s_name = S_NAME;
//        } else if (ID_OBJ == MAIN_TWIN) {
//            s_name = S_NAME.split("/")[0];// Vicotr fuck Ошибка если не "/" - и всё пизда
//        } else {
//            s_name = S_NAME.split("/")[1];// Vicotr fuck Ошибка если не "/" - и всё пизда
//        }
        name = S_NAME;
        if (ID_TYPE != 5 && ID_TYPE != 6) {
            s_name = S_NAME;
        }
        if (ID_TYPE == 5) {
            if (S_NAME.split("/").length >= 1) {
                s_name = S_NAME.split("/")[0];// Vicotr fuck Ошибка если не "/" - и всё пизда
            } else {
                s_name = S_NAME;
            }
        }
        if (ID_TYPE == 6) {
            if (S_NAME.split("/").length >= 2) {
                s_name = S_NAME.split("/")[1];// Vicotr fuck Ошибка если не "/" - и всё пизда
            } else {
                s_name = S_NAME;
            }
        }

//        String turnoutNameD = S_NAME;
        twin = ID_TWIN;

        if (ID_TYPE == 6) {
            cmdSender = ID_TWIN;
        } else {
            cmdSender = ID_OBJ;
        }// cmdSender = ((!(MAIN_TWIN == 0) && !(TWIN == 0)) ? MAIN_TWIN : ID_OBJ);

        if (ID_TYPE == 7) {
            m_route = false;
            busy = false;
        }
        wfan = WFAN;
        if (wfan){
            toutFannItem.setText(t_fann_menu_item);
            toutFannItem.setFont(Terminal.SANS14);
        }

        setPanes(ORIENTATION, SIDE, ID_TYPE, PLUS_DIR);
        setDefaultState(VIDEO_STATUS, ID_TYPE);
    }

    private void setPanes(int ORIENTATION, int SIDE, int ID_TYPE, boolean DIRECTPLUS) {
        cmdX += Terminal.TunePngX;// -6
        cmdY += Terminal.TunePngY;// -6
        lblName.setText(s_name);
        lblName.setFont(Terminal.SANS13);//zoom
        switch (Terminal.zoom) {
            case 72:
                lblName.setFont(Terminal.SANS13);//zoom
                break;
            case 56:
                lblName.setFont(Terminal.SANS10);//zoom
                break;
            case 36:
                lblName.setFont(Terminal.SANS08);//zoom
                break;
        }        
        int titleWidth = lblName.getFontMetrics(lblName.getFont()).stringWidth(lblName.getText()) + 8;
        lblName.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblName.setVerticalAlignment(javax.swing.JLabel.CENTER);
        lblName.setForeground(Color.BLUE);
        terminal.Commander.cmdLayers.add(lblName);
        terminal.Commander.cmdLayers.setLayer(lblName, TURNOUT_NAME_LR);
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 14;
        switch (Terminal.zoom) {
            case 72:
         
        switch (ORIENTATION) {//zoom
            case 3:
                if (SIDE == 12) {
                    a = 31 - titleWidth;
                    b = 26;
                    c = titleWidth;
                }
                if (SIDE == 18) {
                    a = 43;
                    b = 52;
                    c = titleWidth;
                }
                break;
            case 6:
                if (SIDE == 15) {
                    a = 49 - titleWidth;
                    b = 19;
                    c = titleWidth;
                }
                if (SIDE == 21) {
//                    a = 49 - titleWidth;
                    a = 54 - titleWidth;
                    b = 50;
                    c = titleWidth;
                }
                break;
            case 9:
                if (SIDE == 18) {
                    a = 48;//46
                    b = 22;//24
                    c = titleWidth;
                }
                if (SIDE == 24) {
                    a = 31 - titleWidth;
                    b = 43;
                    c = titleWidth;
                }
                break;
            case 12:
                if (SIDE == 3) {
                    a = 33 - titleWidth;
                    b = 35;
                    c = titleWidth;
                }
                if (SIDE == 21) {
                    a = 50;
                    b = 35;
                    c = titleWidth;
                }
                break;
            case 15:
                if (SIDE == 6) {
                    a = 40 - titleWidth;
                    b = 17;
                    c = titleWidth;
                }
                if (SIDE == 24) {
                    a = 52;
                    b = 43;
                    c = titleWidth;
                }
                break;
            case 18:
                if (SIDE == 3) {
                    a = 34;
                    b = 50;
                    c = titleWidth;
                }
                if (SIDE == 9) {
                    a = 30; //34
                    b = 19;
                    c = titleWidth;
                }
                break;
            case 21:
                if (SIDE == 6) {
                    a = 28 - titleWidth;
                    b = 38;
                    c = titleWidth;
                }
                if (SIDE == 12) {
                    a = 40;
                    b = 16;
                    c = titleWidth;
                }
                break;
            case 24:
                if (SIDE == 9) {
                    a = 33 - titleWidth;
                    b = 34;
                    c = titleWidth;
                }
                if (SIDE == 15) {
                    a = 50;
                    b = 34;
                    c = titleWidth;
                }
                break;
            default:
                Log.log("c & d on turnout label not defined");
        }
        break;
            case 56:
                //============================================================
                
                //============================================================
                break;
            case 36:
                //============================================================
                switch (ORIENTATION) {//zoom
            case 3:
                if (SIDE == 12) {
                    a = 31 - titleWidth;
                    b = 26;
                    c = titleWidth;
                }
                if (SIDE == 18) {
                    a = 22;//43
                    b = 26;//
                    c = titleWidth;
                }
                break;
            case 6:
                if (SIDE == 15) {
                    a = 13 - titleWidth + 8;//25
                    b = 2;//19
                    c = titleWidth;
                }
                if (SIDE == 21) {
//                    a = 49 - titleWidth;
                    a = 27 - titleWidth;//54
                    b = 25;//50
                    c = titleWidth;
                }
                break;
            case 9:
                if (SIDE == 18) {
                    a = 24;//48
                    b = 11;//22
                    c = titleWidth;
                }
                if (SIDE == 24) {
                    a = 31 - titleWidth;
                    b = 43;
                    c = titleWidth;
                }
                break;
            case 12:
                if (SIDE == 3) {
                    a = 33 - titleWidth;
                    b = 35;
                    c = titleWidth;
                }
                if (SIDE == 21) {
                    a = 50;
                    b = 35;
                    c = titleWidth;
                }
                break;
            case 15:
                if (SIDE == 6) {
                    a = 20 - titleWidth + 8;//40
                    b = 2;//17
                    c = titleWidth;
                }
                if (SIDE == 24) {
                    a = 52;
                    b = 43;
                    c = titleWidth;
                }
                break;
            case 18:
                if (SIDE == 3) {
                    a = 17;//34
                    b = 25;//50
                    c = titleWidth;
                }
                if (SIDE == 9) {
                    a = 15; //30
                    b = 2; //19
                    c = titleWidth;
                }
                break;
            case 21:
                if (SIDE == 6) {
                    a = 28 - titleWidth;
                    b = 38;
                    c = titleWidth;
                }
                if (SIDE == 12) {
                    a = 40;
                    b = 16;
                    c = titleWidth;
                }
                break;
            case 24:
                if (SIDE == 9) {
                    a = 33 - titleWidth;
                    b = 34;
                    c = titleWidth;
                }
                if (SIDE == 15) {
                    a = 50;
                    b = 34;
                    c = titleWidth;
                }
                break;
            default:
                Log.log("c & d on turnout label not defined");
        }
        //======================================================================
                break;
        }//end case zoom
        lblName.setBounds(cmdX + a, cmdY + b, c, d);
        //===========================

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        Util.prep_lbl(lblCmdEmptyPlus, RAILNET_EMPTY_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "empty" + S_plus, cmdX, cmdY);
        Util.prep_lbl(lblCmdFuture, RAILNET_EMPTY_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "future", cmdX, cmdY);
        Util.prep_lbl(lblCmdEmptyMinus, RAILNET_EMPTY_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "empty" + S_minus, cmdX, cmdY);
        Util.prep_lbl(lblCmdRoutePlus, RAILNET_ROUTE_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "route" + S_plus, cmdX, cmdY);//FUCK седелать из директа
        Util.prep_lbl(lblCmdRouteMinus, RAILNET_ROUTE_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "route" + S_minus, cmdX, cmdY);//FUCK седелать из директа
        Util.prep_lbl(lblCmdRouteUnknown, RAILNET_UKNOWN, cmdPicturePreffix(ORIENTATION, SIDE) + "route__unknown", cmdX, cmdY);
        Util.prep_lbl(lblCmdMRoutePlus, RAILNET_M_ROUTE_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "m_route" + S_plus, cmdX, cmdY);//FUCK седелать из директа
        Util.prep_lbl(lblCmdMRouteMinus, RAILNET_M_ROUTE_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "m_route" + S_minus, cmdX, cmdY);//FUCK седелать из директа
        Util.prep_lbl(lblCmdMRouteUnknown, RAILNET_UKNOWN, cmdPicturePreffix(ORIENTATION, SIDE) + "m_route__unknown", cmdX, cmdY);
        Util.prep_lbl(lblCmdBusyPlus, RAILNET_BUSY_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "busy" + S_plus, cmdX, cmdY);
        Util.prep_lbl(lblCmdBusyMinus, RAILNET_BUSY_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "busy" + S_minus, cmdX, cmdY);
        Util.prep_lbl(lblCmdBusyUnknown, RAILNET_BUSY_UNKNOWN_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "busy__unknown", cmdX, cmdY);//layer = 9 (Counter = 7)

        Util.prep_lbl(lblCmdBlockPlus, RAILNET_BLOCK_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "block" + S_plus, cmdX, cmdY);
        Util.prep_lbl(lblCmdBlockMinus, RAILNET_BLOCK_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "block" + S_minus, cmdX, cmdY);

        Util.prep_lbl(lblCmdIRUnknown, RAILNET_IR_UNKNOWN_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "ir__unknown", cmdX, cmdY);

        int orient;

        if (ORIENTATION >= 12) {
            orient = ORIENTATION - 12;
        } else {
            orient = ORIENTATION;
        }

        if (ORIENTATION < 12) {
            if (DIRECTPLUS) {
                Util.prep_lbl(lblCmdBusyRoutePlus, RAILNET_BUSY_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(inPut + 12) + "__" + "busy", cmdX, cmdY);//
                Util.prep_lbl(lblCmdBusyRouteMinus, RAILNET_BUSY_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__" + "busy", cmdX, cmdY);//
                Util.prep_lbl(lblCmdIRPlus, RAILNET_IR_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(inPut + 12) + "__" + "ir", cmdX, cmdY);//
                Util.prep_lbl(lblCmdIRMinus, RAILNET_IR_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__" + "ir", cmdX, cmdY);//
            } else {
                Util.prep_lbl(lblCmdBusyRoutePlus, RAILNET_BUSY_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__" + "busy", cmdX, cmdY);//
                Util.prep_lbl(lblCmdBusyRouteMinus, RAILNET_BUSY_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(inPut + 12) + "__" + "busy", cmdX, cmdY);//
                Util.prep_lbl(lblCmdIRPlus, RAILNET_IR_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__" + "ir", cmdX, cmdY);//
                Util.prep_lbl(lblCmdIRMinus, RAILNET_IR_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(inPut + 12) + "__" + "ir", cmdX, cmdY);//
            }
        } else if (DIRECTPLUS) {
            if (orient == 0) {
                Util.prep_lbl(lblCmdBusyRoutePlus, RAILNET_BUSY_LR, "direct__" + String.valueOf(ORIENTATION) + "_" + String.valueOf(ORIENTATION + 12) + "__" + "busy", cmdX, cmdY);//(12-24) denisovka ORIENTATION=12 SIDE=3
                Util.prep_lbl(lblCmdBusyRouteMinus, RAILNET_BUSY_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__" + "busy", cmdX, cmdY);//
                Util.prep_lbl(lblCmdIRPlus, RAILNET_IR_LR, "direct__" + String.valueOf(ORIENTATION) + "_" + String.valueOf(ORIENTATION + 12) + "__" + "ir", cmdX, cmdY);//(12-24) denisovka ORIENTATION=12 SIDE=3
                Util.prep_lbl(lblCmdIRMinus, RAILNET_IR_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__" + "ir", cmdX, cmdY);//
            } else {
                Util.prep_lbl(lblCmdBusyRoutePlus, RAILNET_BUSY_LR, "direct__" + String.valueOf(orient) + "_" + String.valueOf(orient + 12) + "__" + "busy", cmdX, cmdY);//
                Util.prep_lbl(lblCmdBusyRouteMinus, RAILNET_BUSY_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__" + "busy", cmdX, cmdY);//
                Util.prep_lbl(lblCmdIRPlus, RAILNET_IR_LR, "direct__" + String.valueOf(orient) + "_" + String.valueOf(orient + 12) + "__" + "ir", cmdX, cmdY);//
                Util.prep_lbl(lblCmdIRMinus, RAILNET_IR_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__" + "ir", cmdX, cmdY);//
            }
        } else {
            Util.prep_lbl(lblCmdBusyRoutePlus, RAILNET_BUSY_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__" + "busy", cmdX, cmdY);
            Util.prep_lbl(lblCmdBusyRouteMinus, RAILNET_BUSY_LR, "direct__" + String.valueOf(orient) + "_" + String.valueOf(orient + 12) + "__" + "busy", cmdX, cmdY);//
            Util.prep_lbl(lblCmdIRPlus, RAILNET_IR_LR, "direct__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__" + "ir", cmdX, cmdY);//
            Util.prep_lbl(lblCmdIRMinus, RAILNET_IR_LR, "direct__" + String.valueOf(orient) + "_" + String.valueOf(orient + 12) + "__" + "ir", cmdX, cmdY);//
        }

        Util.prep_lbl(lblCmdPlusSign, TURNOUT_SIGN_PLUS_LR, "plus", cmdX, cmdY);
        Util.prep_lbl(lblCmdMinusSign, TURNOUT_SIGN_MINUS_LR, "minus", cmdX, cmdY);
        
//        lblCmdPlusSign.setBounds(cmdX + 32, cmdY + 32, 20, 20);//zoom ==center=36 -6+32=26+10=36
        lblCmdPlusSign.setLocation(cmdX + (Terminal.DEF_WIDTH_B/2-lblCmdPlusSign.getIcon().getIconWidth()/2), cmdY + (Terminal.DEF_HEIGHT_B/2-lblCmdPlusSign.getIcon().getIconHeight()/2));//zoom_ok
        lblCmdMinusSign.setBounds(lblCmdPlusSign.getBounds());
        if (backwarding) {
//            Util.prep_lbl(lblCmdAvtorev, TURNOUT_AVTOREV_LR, "autorev", cmdX, cmdY); //if (backwarding)
            Util.prep_lbl(lblCmdAvtorev1, TURNOUT_AVTOREV_LR, "autorev1", cmdX, cmdY); //if (backwarding)
//            Util.prep_lbl(lblCmdAvtorev2, TURNOUT_AVTOREV_LR, "autorev2", cmdX, cmdY); //if (backwarding)
//            lblCmdAvtorev.setBounds(lblCmdPlusSign.getBounds());
            lblCmdAvtorev1.setBounds(lblCmdPlusSign.getBounds());
//            lblCmdAvtorev2.setBounds(lblCmdPlusSign.getBounds());
        }

        lblMenu.setFocusable(false);
        lblMenu.setVisible(true);
        terminal.Commander.cmdLayers.add(lblMenu);
        terminal.Commander.cmdLayers.setLayer(lblMenu, 18);

        Util.prep_lbl(lblVzrez, 1, "vzrez", cmdX, cmdY);

//        Util.prep_lbl(lblCmdToutfault, TURNOUT_LOST_CONTROL_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "toutfault", cmdX, cmdY);
        Util.prep_lbl(lblCmdToutfault1, TURNOUT_LOST_CONTROL_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "toutfault1", cmdX, cmdY);
        Util.prep_lbl(lblCmdToutfault2, TURNOUT_LOST_CONTROL_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "toutfault2", cmdX, cmdY);
//        switch (ORIENTATION) {//zoom
//            case 3:
//                if (SIDE == 12 || SIDE == 18) {
//                    lblCmdToutfault.setBounds(cmdX + 18, cmdY + 18, 48, 48);
//                }
////                if (SIDE_OUT == 18) {
////                    lblCmdToutfault.setBounds(cmdX+18, cmdY+18, 48, 48);
////                }
//                break;
//            case 6:
//                if (SIDE == 15) {
//                    lblCmdToutfault.setBounds(cmdX + 16, cmdY + 31, 49, 35);
//                }
//                if (SIDE == 21) {
//                    lblCmdToutfault.setBounds(cmdX + 16, cmdY + 18, 49, 35);
//                }
//                break;
//            case 9:
//                if (SIDE == 18 || SIDE == 24) {
//                    lblCmdToutfault.setBounds(cmdX + 18, cmdY + 18, 48, 48);
//                }
////                if (SIDE_OUT == 24) {
////                    lblCmdToutfault.setBounds(cmdX+18, cmdY+18, 48, 48);
////                }
//                break;
//            case 12:
//                if (SIDE == 3) {
//                    lblCmdToutfault.setBounds(cmdX + 32, cmdY + 16, 35, 49);
//                }
//                if (SIDE == 21) {
//                    lblCmdToutfault.setBounds(cmdX + 18, cmdY + 16, 35, 49);
//                }
//                break;
//            case 15:
//                if (SIDE == 6 || SIDE == 24) {
//                    lblCmdToutfault.setBounds(cmdX + 18, cmdY + 18, 48, 48);
//                }
////                if (SIDE_OUT == 24) {
////                    lblCmdToutfault.setBounds(cmdX+18, cmdY+18, 48, 48);
////                }
//                break;
//            case 18:
//                if (SIDE == 3) {
//                    lblCmdToutfault.setBounds(cmdX + 19, cmdY + 18, 49, 35);
//                }
//                if (SIDE == 9) {
//                    lblCmdToutfault.setBounds(cmdX + 19, cmdY + 31, 49, 35);
//                }
//                break;
//            case 21:
//                if (SIDE == 6 || SIDE == 12) {
//                    lblCmdToutfault.setBounds(cmdX + 18, cmdY + 18, 48, 48);
//                }
////                if (SIDE_OUT == 12) {
////                    lblCmdToutfault.setBounds(cmdX+18, cmdY+18, 48, 48);
////                }
//                break;
//            case 24:
//                if (SIDE == 9) {
//                    lblCmdToutfault.setBounds(cmdX + 31, cmdY + 19, 35, 49);
//                }
//                if (SIDE == 15) {
//                    lblCmdToutfault.setBounds(cmdX + 18, cmdY + 20, 35, 49);
//                }
//                break;
//            default:
//                lblCmdToutfault.setBounds(cmdX, cmdY, lblCmdToutfault.getIcon().getIconWidth(), lblCmdToutfault.getIcon().getIconHeight());
//        }
        Util.prep_lbl(lblCmdMoving, TURNOUT_MOVING_LR, cmdPicturePreffix(ORIENTATION, SIDE) + "moving", cmdX, cmdY);
//        lblCmdMoving.setBounds(lblCmdToutfault.getBounds());
//        lblMenu.setBounds(lblCmdToutfault.getBounds());
//        lblCmdMoving.setBounds(lblCmdToutfault1.getBounds());
        lblMenu.setBounds(lblCmdPlusSign.getBounds());

        //--------------------------------MENU----------------------------------
        if (vStatus == 0) {
            if (ID_TYPE != 4) { // не неуправляемая UNRULED_TYPE
                if ((Terminal.DSP || Terminal.SHN)) {//механик тут для обогрева
                    setToutPopup();
                    lblMenu.setComponentPopupMenu(Popup);
                    lblName.setComponentPopupMenu(Popup);
                    lblMenu.setCursor(TOUT_CURSOR);
                    lblName.setCursor(TOUT_CURSOR);
                }
            } else {
                if (Util.GetUserGroups("SIM")) { //добавим симулятор для неконтролируемой стрелки
                    Popup.add(toutPlusItem);
                    toutPlusItem.addActionListener(this::toutPlusItemActionPerformed);
                    Popup.add(toutMinusItem);
                    toutMinusItem.addActionListener(this::toutMinusItemActionPerformed);
                    trSimFaultItem.addActionListener(evt -> trSimFaultItemActionPerformed());
                    Popup.add(trSimFaultItem);
                    trSimUnFaultItem.addActionListener(evt -> trSimUnFaultItemActionPerformed());
                    Popup.add(trSimUnFaultItem);
                    lblMenu.setComponentPopupMenu(Popup);
                    lblName.setComponentPopupMenu(Popup);
                    lblMenu.setCursor(TOUT_CURSOR);
                    lblName.setCursor(TOUT_CURSOR);
                } //end if sim
            } //end else ID_TYPE = 4 (UNRULLED_TYPE)
        } //end if vStatus
    }

    private void setDefaultState(int VIDEO_STATUS, int ID_TYPE) {
        //videoStatus = vStatus обработка видимости стрелки - строящаяся, невидимая ...
        if (VIDEO_STATUS > 0) {
            if (VIDEO_STATUS == 1) {// 1-FUTURE
                lblCmdFuture.setVisible(true);
                lblName.setForeground(Color.LIGHT_GRAY);
//                    lblCmdBusyRoutePlus.setVisible(false);
//                    lblCmdBusyRouteMinus.setVisible(false);
//                    lblCmdBusyPlus.setVisible(false);
//                    lblCmdBusyMinus.setVisible(false);
//                    lblCmdBusyRoutePlus.setVisible(false);
//                    lblCmdBusyRouteMinus.setVisible(false);
//                    lblCmdBusyPlus.setVisible(false);
//                    lblCmdBusyMinus.setVisible(false);   
            }
        } else {
            switch (ID_TYPE) {
                case 4://----------UNRULLED TYPE
                    plus = true;
                    minus = true;
                    moving = false;
                    lblCmdBusyRoutePlus.setVisible(false);
                    lblCmdBusyRouteMinus.setVisible(false);
                    lblCmdBusyPlus.setVisible(false);
                    lblCmdBusyMinus.setVisible(false);
                    lblCmdBusyRoutePlus.setVisible(false);
                    lblCmdBusyRouteMinus.setVisible(false);
                    lblCmdBusyPlus.setVisible(false);
                    lblCmdBusyMinus.setVisible(false);
                    break;
                case 7:
                    plus = true;
                    minus = true;
                    moving = false;
                    lblCmdFuture.setVisible(true);
                    lblCmdEmptyPlus.setVisible(false);
                    lblCmdPlusSign.setVisible(true);
                    lblCmdMRouteUnknown.setVisible(false);// не m_rout а unknown
                    lblCmdBusyUnknown.setVisible(false);// busy
//                    lblCmdToutfault.setVisible(false);
                    break;
                default:
                    lblCmdFuture.setVisible(false);
                    lblCmdEmptyPlus.setVisible(true);
                    lblCmdPlusSign.setVisible(true);
//            lblCmdMRoutePlus.setVisible(true);// m_rout
                    lblCmdMRouteUnknown.setVisible(true);// не m_rout а unknown
                    lblCmdBusyUnknown.setVisible(true);// busy
//                    lblCmdToutfault.setVisible(true);// fault aaaaaaaaaaaaaaaaaa
                    if (fault){
                       Terminal.TIMER600.addActionListener(TR_FAULT); 
                    }
            }
        }
    }

    // картинки
    private String cmdPicturePreffix(int ORIENTATION, int SIDE) {
        return "turnout__" + String.valueOf(ORIENTATION) + "_" + String.valueOf(SIDE) + "__";
    }

    int getCmdSender() {
        return cmdSender;
    }

    private Turnout getMainLogObj() {
        return mainLogObj;
    }

    void setMainLogObj(Turnout MAIN_ID_OBJ) {
        mainLogObj = MAIN_ID_OBJ;
    }

    void setState(
            // boolean BUSY, int ROUTE, boolean BLOCK, boolean IR,
            long DTIME,
            int POSITION,
            boolean TOUTROUTE, //будем брать из RAILNET --- это DK ? Zst участие стрелки в маршруте (0-да, 1-нет)	 0-да, 1-нет
            boolean FAULT, //VZ	 потеря контроля (0-авария, 1-норма)
            boolean TOUTBLOCK, //BLst	 блокировка стрелки	 1 - заблокирована
            boolean FAN, //Obd	контроль обдува	 0-нет, 1- обдув
            boolean BACKWARD, //REVERS	 требуется перевод стрелки	 1 - требуется перевод
            boolean FANCMD,
            boolean FANFLT,
            boolean ERR,
            boolean DUP,//Не выполняются дополнительные условия безопасности
            boolean VZREZ,
            boolean NOCONTROL,
            boolean PK_ERR,//// Ошибка (реле, обрыв провода) ао внешней схеме контроля.......
            boolean MK_ERR//// Ошибка (реле, обрыв провода) ао внешней схеме контроля.......
    ) {

        if (vStatus == 0 && type != 4) { //&& ID_TYPE != 4 (UNRULLED_TYPE)
            setDtime(DTIME);
            /*----------------------------------------------------------------------
         0	MOVE
         1	PLUS        Плюс                  MESS_TOUT_PLUS             314
         2	MINUS       Минус                 MESS_TOUT_MINUS            312
         3	PROHIBIT    Неизвестное положение MESS_TOUT_POSITION_FAULT   315
             */
            if (PositionState != POSITION) {
                PositionState = POSITION;
                switch (POSITION) {// integer
                    case 1:// "PLUS"://1 PLUS Плюс MESS_TOUT_PLUS 314
                        plus = true;
                        minus = false;
                        moving = false;
                        event(314);
                        alarm_off(140);
                        break;
                    case 2:// "MINUS"://2 MINUS Минус MESS_TOUT_MINUS 312
                        minus = true;
                        plus = false;
                        moving = false;
                        event(312);
                        alarm_off(140);
                        break;
                    case 0:// "MOVE"://0 MOVE
                        moving = true;
                        // setInPlusPosition(false);
                        // setInMinusPosition(false);
                        break;
                    case 3:// "PROHIBIT"://3 PROHIBIT Неизвестное положение
                        // MESS_TOUT_POSITION_FAULT 315
                        // setMoving(false);
                        // setInPlusPosition(false);
                        // setInMinusPosition(false);
                        // setOnFault(true);
                        // event(315);
                        alarm_on(140);
                        break;
                }
                if (type == 7) {
                    lblCmdPlusSign.setVisible(plus);
                    lblCmdMinusSign.setVisible(minus);
                } else {
                    lblCmdEmptyPlus.setVisible(plus);
                    lblCmdPlusSign.setVisible(plus);
                    lblCmdEmptyMinus.setVisible(minus);
                    lblCmdMinusSign.setVisible(minus);
                    lblCmdMoving.setVisible(moving);
                    lblCmdToutfault1.setVisible(fault);
                    lblCmdToutfault2.setVisible(fault);
                    lblCmdMoving.setVisible(moving);
                }
            }
            // /*-RN------------------------------------------------------------
            // 0 M_ROUTE Маневровый маршрут
            // 1 - Незамкнута
            // 2 ROUTE Поездной маршрут
            // */
            // if (routestate != ROUTE) {
            // routestate = ROUTE;
            // switch (ROUTE) {//int
            // case 1://"-"://1 - Незамкнута
            // route = false;
            // m_route = false;
            // break;
            // case 2://"ROUTE"://2 ROUTE Поездной маршрут
            // route = true;
            // m_route = false;
            // break;
            // case 0://"M_ROUTE"://0 M_ROUTE Маневровый маршрут
            // route = false;
            // m_route = true;
            // break;
            // }
            // }
            /*-----------------------БЛЯ ТУТ ОПЯТЬ ВСЁ НАОБОРОТ!----------------
             0	TOUTFAULT	Потеря контроля	MESS_TOUT_POSITION_FAULT 315
             1	-
             */
            if (fault == FAULT) {
                fault = !FAULT;
                if (!FAULT) {// false 0 TOUTFAULT Потеря контроля MESS_TOUT_POSITION_FAULT 315
                    moving = false;
                    lblCmdMoving.setVisible(false);
                    event(315);
                    alarm_on(140);
                    Terminal.TIMER600.addActionListener(TR_FAULT);
                } else {// true 1 -TOUT_NO_FAULT
                    alarm_off(140);
                    Terminal.TIMER600.removeActionListener(TR_FAULT);
                    lblCmdToutfault1.setVisible(false);
                    lblCmdToutfault2.setVisible(false);
                }
//                lblCmdToutfault.setVisible(fault);
            }//end fault
            /*-!!!!!!!!!!!!!!!!!!!!!!!!-БЛЯ ОПЯТЬ ВСЁ НАОБОРОТ!-!!!!!!!!!!!!!!!-
             0	TOUTROUTE
             1	-
             */
//            if (toutInRoute != !TOUTROUTE) { //типа если в контроллере не настроена DK то всегда приходит 0
            toutInRoute = !TOUTROUTE;
//                }
            /*------------------------------------------------------------------
             1	FANUP	Обдувка         MESS_TOUT_FANUP         311
             0	FANOFF	Нет обдувки	MESS_TOUT_FANOFF	310
             */
            if (toutFanned != FAN) {
                toutFanned = FAN;
                if (FAN) {// true 1 FANUP Обдувка MESS_TOUT_FANUP 311
                    event(311);
                } else {// false 0 FANOFF Нет обдувки MESS_TOUT_FANOFF 310
                    event(310);
                }
            }
            /*------------------------------------------------------------------
             1	FAN_CMD_ON пришла команда обдувки :1 MESS_TOUT_FAN_CMD 309
             0	FAN_CMD_OFF
             */
            if (ToutFanCmd != FANCMD) {
                ToutFanCmd = FANCMD;
                if (FANCMD) {// true 1 FAN_CMD_ON пришла команда обдувки :1 MESS_TOUT_FAN_CMD 309
                    event(309);
                }  // false 0 FAN_CMD_OFF
            }
            /*------------------------------------------------------------------
             0	FAN_FLT_OFF
             1	FAN_FLT_ON
             */
            if (fanfltstate != FANFLT) {
                fanfltstate = FANFLT;
                if (FANFLT) {// true 1 FAN_FLT_ON
                    alarm_on(139);
                } else {// false 0 FAN_FLT_OFF
                    alarm_off(139);
                }
            }
            /*------------------------------------------------------------------
             1	TOUTBLOCK	Заблокирована	MESS_TOUT_BLOCK         307
             0	TOUTUNBLOCK	Разблокирована	MESS_TOUT_UN_BLOCK	316
             */
            if (toutBlocked != TOUTBLOCK) {
                toutBlocked = TOUTBLOCK;
                if (TOUTBLOCK) {//true 1 TOUTBLOCK Заблокирована MESS_TOUT_BLOCK 307
                    event(307);
                } else {//false 0 TOUTUNBLOCK Разблокирована MESS_TOUT_UN_BLOCK  316
                    event(316);
                }
            }
            /*------------------------------------------------------------------
             0	-
             1	TOUT_BACKWARD Требуется перевод MESS_TOUT_BACKWARD 306
             */
            if (backwarding) {
                if (backward != BACKWARD) {
                    backward = BACKWARD;
                    if (BACKWARD) {// true 1 TOUT_BACKWARD Требуется перевод MESS_TOUT_BACKWARD 306
                        event(306);
                        alarm_on(138);
                        Terminal.TIMER600.addActionListener(TR_BACK);
                    } else {// false 0 no need backward
                        alarm_off(138);
                        Terminal.TIMER600.removeActionListener(TR_BACK);
                        lblCmdAvtorev1.setVisible(false);
//                        lblCmdAvtorev2.setVisible(false);
                    }
//                    lblCmdAvtorev.setVisible(backward);
                }
            }
            /*------------------------------------------------------------------
             1 ERR_CTL Ошибочные действия ДСП MESS_TOUT_ERR_CTL 308
             0
             1       iERR
             */
            if (err != ERR) {
                err = ERR;
                if (ERR) {// true 1
                    event(308);// MESS_TOUT_ERR_CTL
                }  // false 0 no need message

            }
            if (dup != DUP) {
                dup = DUP;
                if (DUP) {// true 1
                    event(295);//<html><font color=blue>Установка маршрута задерживается. Не выполняются дополнительные условия безопасности</html>
                }
            }

            if (Terminal.VZREZ) {
                if (vzrez != VZREZ) {
                    vzrez = VZREZ;
                    if (VZREZ) {// true 1
                        alarm_on(217_261);//ALARM_VZREZ
                        event(117_261);// MESS_VZREZ
                        lblVzrez.setVisible(true);
                    } else {// false 0 no need message
                        alarm_off(217_261);
                        lblVzrez.setVisible(false);
                    }
                }
                if (nocontrol != NOCONTROL) {
                    nocontrol = NOCONTROL;
                    if (NOCONTROL) {// true 1
                        alarm_on(217_271);//ALARM_VZREZ
                        event(117_271);// MESS_NOCONTROL
                        lblVzrez.setVisible(true);
                    } else {// false 0 no need message
                        alarm_off(217_271);
                        if (!VZREZ) {
                            lblVzrez.setVisible(false);
                        }
                    }
                }
            }
            //render();
            // ------------------------------обдувка----------------------------
                if (toutFanned) {
                    lblName.setOpaque(true);
                    lblName.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    lblName.setForeground(Color.BLUE);
                    lblName.setBackground(Color.YELLOW);
                } else if (ToutFanCmd) {
                    lblName.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
                } else if (toutBlocked) {// блокировка стрелки
                    lblName.setOpaque(true);
                    lblName.setBorder(BorderFactory.createLineBorder(Color.CYAN));
                    lblName.setForeground(Color.WHITE);
                    lblName.setBackground(new Color(0, 128, 128));
                } else {
                    lblName.setOpaque(false);
                    lblName.setBorder(null);
                    lblName.setForeground(Color.BLUE);
                }

        } //end if vstatus or type=4(UNRULLED)
//        System.out.println("!!!!!!!    render from tr " + s_name);
        render();
    }

    void setVzrez(boolean VZREZ){
        if (vzrez != VZREZ) {
                    vzrez = VZREZ;
                    if (VZREZ) {// true 1
                        alarm_on(217_261);//ALARM_VZREZ
                        event(117_261);// MESS_VZREZ
                        lblVzrez.setVisible(true);
                    } else {// false 0 no need message
                        alarm_off(217_261);
                        lblVzrez.setVisible(false);
                    }
                }
    }
    
    private void render() {
        if (vStatus == 0 ){
//        if (toutInRoute) {// false 0 TOUTROUTE в маршруте
//            System.out.println(" Стрелка в маршруте " + s_name);
        if (dk_next_tr) {
//                System.out.println(" есть следующая ---свой toutInRoute = " + (toutInRoute) + " свой dk" + dk + " свой fault = " + fault);
            if (Terminal.Turnouts_Hash.containsKey(dkp)) {
//                    System.out.println("   если плюс предупредить " + Terminal.Turnouts_Hash.get(dkp).s_name);
                Terminal.Turnouts_Hash.get(dkp).setDK((minus || fault) && toutInRoute && dk);
//                                System.out.println("   для след стр. " + Terminal.Turnouts_Hash.get(dkp).s_name + " установим !plus || fault  && dk= " + !plus + " " + fault + " " + toutInRoute);
            }
            if (Terminal.Turnouts_Hash.containsKey(dkm)) {
//                    System.out.println("   если минус предупредить " + Terminal.Turnouts_Hash.get(dkm).s_name);
                Terminal.Turnouts_Hash.get(dkm).setDK((plus || fault) && toutInRoute && dk);
//                                System.out.println("   для след стр. " + Terminal.Turnouts_Hash.get(dkm).s_name + " установим !minus || fault && dk = " + !minus + " " + fault + " " + toutInRoute);
            }
        }

        // --------------------- слой занятости--------------
        lblCmdBusyPlus.setVisible(plus && busy && !(route || m_route));//?
        lblCmdBusyMinus.setVisible(minus && busy && !(route || m_route));//?
        lblCmdBusyRoutePlus.setVisible(plus && busy && toutInRoute && dk);
        lblCmdBusyRouteMinus.setVisible(minus && busy && toutInRoute && dk);
        lblCmdBusyUnknown.setVisible(fault && busy && toutInRoute && dk);
        // ---------------------- слой ИР------------------------
        lblCmdIRPlus.setVisible(plus && ir && toutInRoute && (route || m_route) && dk);
        lblCmdIRMinus.setVisible(minus && ir && toutInRoute && (route || m_route) && dk);
        lblCmdIRUnknown.setVisible(fault && ir && toutInRoute && dk);
        // --------------------- блокировочный слой--------
        lblCmdBlockPlus.setVisible(plus && block);
        lblCmdBlockMinus.setVisible(minus && block);

        lblCmdRoutePlus.setVisible(plus && route && toutInRoute && dk);
        lblCmdRouteMinus.setVisible(minus && route && toutInRoute && dk);
        lblCmdRouteUnknown.setVisible(fault && route && toutInRoute && dk);

        lblCmdMRoutePlus.setVisible(plus && m_route && toutInRoute && dk);
        lblCmdMRouteMinus.setVisible(minus && m_route && toutInRoute && dk);
        lblCmdMRouteUnknown.setVisible(fault && m_route && toutInRoute && dk);

        Terminal.DirectCell_Hash.values().stream().filter((dc) -> (dc.id_tr == id_obj)).forEach((dc) -> dc.setState(busy, ir, block, route, m_route));
        }
    }

    private void setDK(boolean DK) {
        dk = DK;
        render();
    }

    void setStateRN(// from RN
            boolean BUSY,//SP свободность/занятость секции в которую входит стрелка	 1 - свободна, 0 - занята
            boolean IR,
            boolean BLOCK,
            boolean ROUTE,
            boolean M_ROUTE
    ) {
        busy = BUSY;
        ir = IR;
        block = BLOCK;
        route = ROUTE;
        m_route = M_ROUTE;
        render();
    }

    // пункт меню активируется, если выполняются условия:
    // свободность секции (!isBusy),
    // отсутствие замыкания секции в любом маршруте (!isAnyRoute),
    // отсутствие блокировки стрелки (!isToutBlock)
    // и текущее положение «неПлюсовое»/«неМинусовое»
    // по этой стрелке и если есть парная стрелка,
    // то такая же проверка делается и там и решение - по результату опроса
    // обеих
    private boolean toutPlusItemCanBeEnabled() {
        boolean twEnables = true;
        if (!(twin == 0)) {
            Turnout tw = (Turnout) (Terminal.mainCellsHash.get(twin));
            twEnables = (!tw.busy && !(tw.route || tw.m_route) && !tw.toutBlocked && (!tw.plus || tw.moving || tw.fault));
        }
        return (!busy && !(route || m_route) && !toutBlocked && (!plus || moving || fault) && twEnables);
    }

    private boolean toutMinusItemCanBeEnabled() {
        boolean twEnables = true;
        if (!(twin == 0)) {
            Turnout tw = (Turnout) (Terminal.mainCellsHash.get(twin));
            twEnables = (!tw.busy && !(tw.route || tw.m_route) && !tw.toutBlocked && (!tw.minus || tw.moving || tw.fault));
        }
        // System.out.println("!rn.busy:" + !busy +
        // " !(rn.route || rn.m:_route)" + !(route || m_route) +
        // " !toutBlocked:" + !toutBlocked + "  !minus:" + !minus +
        // " twEnables:" + twEnables + " fault:" + fault + " twIsFault:" +
        // twIsFault);
        return (!busy && !(route || m_route) && !toutBlocked && (!minus || moving || fault) && twEnables);
    }

    // пункт меню активируется, если выполняются условия:
    // занятость секции (isBusy),
    // отсутствие замыкания секции в любом маршруте (!isAnyRoute),
    // отсутствие блокировки стрелки (!isToutBlock) и текущее положение
    // «неПлюсовое»/«неМинусовое»
    // по этой стрелке и если есть парная стрелка,
    // то такая же проверка делается и там и решение - по результату опроса обеих
    private boolean toutAuxPlusItemCanBeEnabled() {
        boolean twBusy = false, twRouted = false, twBlocked = false, twPlus = false, twFault = false, twMoving = false;
        if (!(twin == 0)) {
            Turnout tw = (Turnout) (Terminal.mainCellsHash.get(twin));
            twMoving = tw.moving;
            twBusy = tw.busy;
            twRouted = (tw.route || tw.m_route);
            twBlocked = tw.toutBlocked;
            twPlus = tw.plus;
            twFault = tw.fault;
        }
        return ((busy || twBusy) && (!twRouted && !(route || m_route)) && (!twBlocked && !toutBlocked) && ((!twPlus || twMoving || twFault) && (!plus || moving || fault)));
    }

    private boolean toutAuxMinusItemCanBeEnabled() {
        boolean twBusy = false, twRouted = false, twBlocked = false, twMinus = false, twFault = false, twMoving = false;
        if (!(twin == 0)) {
            Turnout tw = (Turnout) (Terminal.mainCellsHash.get(twin));
            twMoving = tw.moving;
            twBusy = tw.busy;
            twRouted = (tw.route || tw.m_route);
            twBlocked = tw.toutBlocked;
            twMinus = tw.minus;
            twFault = tw.fault;
        }
//        return ((busy || twBusy) && (!twRouted && !(route || m_route)) && (!twBlocked && !toutBlocked) && (!twMinus && !minus) || ((fault || twFault) && (busy || twBusy)));
        return ((busy || twBusy) && (!twRouted && !(route || m_route)) && (!twBlocked && !toutBlocked) && ((!twMinus || twMoving || twFault) && (!minus || moving || fault)));
    }

    // менюшка
    private void setToutPopup() {

        if (Terminal.DSP && vStatus == 0) {// && VIDEO_STATUS == 0) {

            Popup.setBorder(BorderFactory.createTitledBorder(menu_title));
            Popup.setBorderPainted(true);

            if (type == 7) {
                Popup.add(toutPlusItem);
                toutPlusItem.addActionListener(this::toutPlusItemActionPerformed);
                Popup.add(toutMinusItem);
                toutMinusItem.addActionListener(this::toutMinusItemActionPerformed);
            } else {
                Popup.add(toutPlusItem);
                toutPlusItem.addActionListener(this::toutPlusItemActionPerformed);
                Popup.add(toutMinusItem);
                toutMinusItem.addActionListener(this::toutMinusItemActionPerformed);
                Popup.addSeparator();
                Popup.add(toutAuxPlusItem);
                toutAuxPlusItem.addActionListener(this::toutAuxPlusItemActionPerformed);
                Popup.add(toutAuxMinusItem);
                toutAuxMinusItem.addActionListener(this::toutAuxMinusItemActionPerformed);
                Popup.addSeparator();
                Popup.add(toutBlockItem);
                toutBlockItem.addActionListener(this::toutBlockItemActionPerformed);
                Popup.add(toutUnBlockItem);
                toutUnBlockItem.addActionListener(this::toutUnBlockItemActionPerformed);
            }
            if (Util.GetUserGroups("SIM")) {
                Popup.addSeparator();
                trSimFaultItem.addActionListener(evt -> trSimFaultItemActionPerformed());
                Popup.add(trSimFaultItem);
                trSimUnFaultItem.addActionListener(evt -> trSimUnFaultItemActionPerformed());
                Popup.add(trSimUnFaultItem);
            }
        }
        // ---------------------------------------------------------------------

        if (Terminal.OBDUV && wfan) {
            if (Terminal.DSP || Terminal.SHN) {
                Popup.addSeparator();
                Popup.add(toutFannItem);
                toutFannItem.addActionListener((java.awt.event.ActionEvent evt) -> toutFannItemActionPerformed());
            }
        }
        // ---------------------------------------------------------------------
        Popup.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                preparePopup();
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }

    private void preparePopup() {
        toutPlusItem.setEnabled(toutPlusItemCanBeEnabled() && Status.work && Area.Manager_PC);
        toutMinusItem.setEnabled(toutMinusItemCanBeEnabled() && Status.work && Area.Manager_PC);
        toutAuxPlusItem.setEnabled(toutAuxPlusItemCanBeEnabled() && Status.work && Area.Manager_PC);
        toutAuxMinusItem.setEnabled(toutAuxMinusItemCanBeEnabled() && Status.work && Area.Manager_PC);
        toutBlockItem.setEnabled(!toutBlocked && Status.work && Area.Manager_PC);
        toutUnBlockItem.setEnabled(toutBlocked && Status.work && Area.Manager_PC);
        if(wfan){
            toutFannItem.setEnabled(!toutFanned && Status.work && Area.Manager_PC);//Status.work <- Net.logged
        }
        if (Util.GetUserGroups("SIM")) {
            toutPlusItem.setEnabled(toutPlusItemCanBeEnabled() && Status.work && Area.Manager_PC);
            toutMinusItem.setEnabled(toutMinusItemCanBeEnabled() && Status.work && Area.Manager_PC);
            trSimFaultItem.setEnabled(!fault && Status.work && Area.Manager_PC);
            trSimUnFaultItem.setEnabled(fault && Status.work && Area.Manager_PC);
        }
        // toutUnFannItem.setEnabled(isToutFanned());
    }

    // команды
    private void toutPlusItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (getMainLogObj() != null) {
            getMainLogObj().toutPlusItemActionPerformed(evt);
        } else {
            Net.sendMaskedCmd_DSP(cmdSender, "TURNOUT_POSITION_PLUS1");
            pause();
            Net.sendMaskedCmd_DSP(cmdSender, "TURNOUT_POSITION_PLUS2");
        }
    }

    private void toutMinusItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (getMainLogObj() != null) {
            getMainLogObj().toutMinusItemActionPerformed(evt);
        } else {
            Net.sendMaskedCmd_DSP(cmdSender, "TURNOUT_POSITION_MINUS1");
            pause();
            Net.sendMaskedCmd_DSP(cmdSender, "TURNOUT_POSITION_MINUS2");
        }
    }

    private void toutAuxPlusItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (getMainLogObj() != null) {
            getMainLogObj().toutAuxPlusItemActionPerformed(evt);
        } else {
            switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                    CAUNTION + "! " + OPERATION,
                    "<html><font color=red size=5 style=bold><b> "
                    + OPERATION
                    + "!!</b></font><br>Подтвердите операцию\n вспомогательного перевода\n стрелки <font color=red size=5 style=bold><b>"
                    + name + "</b></font> в \"ПЛЮС\"</html>")) {
                case 0:
                    break;
                case 1:
                    Net.sendMaskedCmd_DSP(cmdSender, "TURNOUT_AUXILIARY_POSITION_PLUS1");
                    pause();
                    Net.sendMaskedCmd_DSP(cmdSender, "TURNOUT_AUXILIARY_POSITION_PLUS2");
                    break;
            }
        }// end else
    }

    private void toutAuxMinusItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (getMainLogObj() != null) {
            getMainLogObj().toutAuxMinusItemActionPerformed(evt);
        } else {
            switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                    CAUNTION + "! " + OPERATION,
                    "<html><font color=red size=5 style=bold><b> "
                    + OPERATION
                    + "!!</b></font><br>Подтвердите операцию\n вспомогательного перевода\n стрелки <font color=red size=5 style=bold><b>"
                    + name + "</b></font> в \"МИНУС\"</html>")) {
                case 0:
                    break;
                case 1:
                    Net.sendMaskedCmd_DSP(cmdSender,
                            "TURNOUT_AUXILIARY_POSITION_MINUS1");
                    pause();
                    Net.sendMaskedCmd_DSP(cmdSender,
                            "TURNOUT_AUXILIARY_POSITION_MINUS2");
                    break;
            }
        }// end else
    }

    private void toutBlockItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (getMainLogObj() != null) {
            getMainLogObj().toutBlockItemActionPerformed(evt);
        } else {
            Net.sendMaskedCmd_DSP(cmdSender, "TURNOUT_BLOCK_ON");
        }
    }

    private void toutUnBlockItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (getMainLogObj() != null) {
            getMainLogObj().toutUnBlockItemActionPerformed(evt);
        } else {
            Net.sendMaskedCmd_DSP(cmdSender, "TURNOUT_BLOCK_OFF");
        }
    }

    private void toutFannItemActionPerformed() {
        if (wfan){
            Net.sendMaskedCmd_DSP_SHN(id_obj, "TURNOUT_FAN_ON");
        }
    }

    private void trSimFaultItemActionPerformed() {
//        if ((byteNumber != -1) | (bitNumber != -1)) {
//            Net.sendDirectCmd_SIM(12, String.valueOf(byteNumber) + "." + String.valueOf(bitNumber) + ".1");//SIEMENS //включить
//        }
        Net.sendMaskedCmd_SIM(cmdSender, "TURNOUT_SIM_K1");
    }

    private void trSimUnFaultItemActionPerformed() {
//        if ((byteNumber != -1) | (bitNumber != -1)) {
//            Net.sendDirectCmd_SIM(12, String.valueOf(byteNumber) + "." + String.valueOf(bitNumber) + ".0");//SIEMENS //включить
//        }
        Net.sendMaskedCmd_SIM(cmdSender, "TURNOUT_SIM_K0");
    }

    private void event(int id_msg) {
//        if (getDtime() > 0) {
        if (type != 6) {//!main
            Events.InsertMessage(getDtime(), id_obj, id_msg);
        }
//        }
    }

    private void alarm_on(int id) {
//        if (getDtime() > 0) {
        if (type != 6) {//!main
            Alarms.alarm_on(getDtime(), id, id_obj);
        }
//        }
    }

    private void alarm_off(int id) {
//        if (getDtime() > 0) {
        if (type != 6) {//!main
            Alarms.alarm_off(getDtime(), id, id_obj);
        }
//        }
    }

    private void pause() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Err.err(e);
        }
    }

    @Override
    public void setState(Object oid) {
        TurnoutState t = (TurnoutState) oid;

        this.setState(
                t.timestamp,
                t.POSITION,
                t.TOUTROUTE,
                t.FAULT,
                t.TOUTBLOCK,
                t.FAN,
                t.BACKWARD,
                t.FANCMD,
                t.FANFLT,
                t.ERR,
                t.DUP,
                t.VZREZ,
                t.NOCONTROL,
                t.PK_ERR,
                t.MK_ERR
        );
    }
}

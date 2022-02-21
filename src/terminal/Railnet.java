//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
/*
 RN_TYPE - переделать в число
 1-FUTURE-строительство, 2-INVISIBLE-невидимый, 3-DEPRECATE-н.е.н.х.
 */
package terminal;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.event.PopupMenuEvent;
import ru.attrans.proc.objs.RailNetState;
import static terminal.Terminal.boomer;

class Railnet extends Cell {

    private static final java.awt.Cursor RN_CURSOR = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(Terminal.mainPictureHash.get("rn_cursor").getImage(), new java.awt.Point(0, 0), "rn_cursor");

    private final java.awt.Color cmdIdleForeground = java.awt.Color.DARK_GRAY;

    private final java.awt.Color cmdIdleBackground = new java.awt.Color(220, 236, 250);
    private final javax.swing.border.Border cmdIdleBorder = BorderFactory.createLineBorder(java.awt.Color.DARK_GRAY);

    private final java.awt.Color cmdBlockBackground = new java.awt.Color(0, 128, 128);
    private final java.awt.Color cmdBlockForeground = java.awt.Color.WHITE;
    private final javax.swing.border.Border cmdBLockBorder = BorderFactory.createLineBorder(java.awt.Color.CYAN);
    private final java.awt.Color cmdIRBackground = new java.awt.Color(255, 255, 215);
    private final javax.swing.border.Border cmdIRBorderLow = BorderFactory.createLineBorder(new java.awt.Color(191, 191, 161), 2);
    private final javax.swing.border.Border cmdIRBorderHigh = BorderFactory.createLineBorder(new java.awt.Color(114, 114, 96), 2);
    private final java.awt.Color cmdAlarmBackground = new java.awt.Color(255, 204, 204);
    private final javax.swing.border.Border cmdAlarmBorderLow = BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 2);
    private final javax.swing.border.Border cmdAlarmBorderHigh = BorderFactory.createLineBorder(new java.awt.Color(170, 0, 0), 2);
//    final String s_name;
    // -------------------------------Translate---------------------------------
    private final String menu_title;

    // -------------------------------------------------------------------------
//    private final int vStatus;
    private final boolean show_axle;
    private final boolean boom;// Default Value типа для того чтобы понять есть ограждение составов или нет
    private final boolean skidAble;// рельса обошмачивается
    private final java.util.HashMap<Integer, Integer> counterIdKey = new java.util.HashMap<>(); // для рисования ограждения путей
    private final javax.swing.JLabel lblName = new javax.swing.JLabel();
    private final javax.swing.JPopupMenu Popup = new javax.swing.JPopupMenu();// меню
//    private final javax.swing.JMenuItem rnIrItem = new javax.swing.JMenuItem("<html>запуск <b><font color=orange>искусственной разделки</font></b></html>");
//    private final javax.swing.JMenuItem rnBlockItem = new javax.swing.JMenuItem("<html>блокировка снята, <b><font color=orange>установить</font></b></html>");
//    private final javax.swing.JMenuItem rnUnBlockItem = new javax.swing.JMenuItem("<html>блокировка установлена, <b><font color=green>снять</font></b></html>");
//    private final javax.swing.JMenuItem rnSkidItem = new javax.swing.JMenuItem("<html>башмаки не установлены, <b><font color=orange>установить</font></b></html>");
//    private final javax.swing.JMenuItem rnUnSkidItem = new javax.swing.JMenuItem("<html>башмаки установлены, <b><font color=green>снять</font></b></html>");
//    private final javax.swing.JMenuItem rnHollowCancelItem = new javax.swing.JMenuItem("<html>сброс <b><font color=green>ложной занятости</font></b></html>");
//    private final javax.swing.JMenuItem rnBommItem = new javax.swing.JMenuItem("<html><b><font color=green>Согласие на ограждение пути</font></b></html>");
//    private final javax.swing.JMenuItem rnBoomCancelItem = new javax.swing.JMenuItem("<html>Аварийная <b><font color=green>отмена ограждения</font></b></html>");

    private final javax.swing.JMenuItem rnIrItem = new javax.swing.JMenuItem(r_ir_menu_item);
    private final javax.swing.JMenuItem rnBlockItem = new javax.swing.JMenuItem(r_block_menu_item);
    private final javax.swing.JMenuItem rnUnBlockItem = new javax.swing.JMenuItem(r_unblock_menu_item);
    private final javax.swing.JMenuItem rnSkidItem = new javax.swing.JMenuItem(r_skid_menu_item);
    private final javax.swing.JMenuItem rnUnSkidItem = new javax.swing.JMenuItem(r_unskid_menu_item);
    private final javax.swing.JMenuItem rnHollowCancelItem = new javax.swing.JMenuItem(r_hollowCancel_menu_item);
    private final javax.swing.JMenuItem rnBommItem = new javax.swing.JMenuItem(r_bomm_menu_item);
    private final javax.swing.JMenuItem rnBoomCancelItem = new javax.swing.JMenuItem(r_boomcancel_menu_item);

    private final javax.swing.JMenuItem rnVgnLstItem = new javax.swing.JMenuItem("Показать список вагонов");

    private final javax.swing.JMenuItem rnSimBusyItem = new javax.swing.JMenuItem("<html>Симулятор: <b><font color=blue>Занять секцию</font></b></html>");
    private final javax.swing.JMenuItem rnSimUnBusyItem = new javax.swing.JMenuItem("<html>Симулятор: <b><font color=blue>Освободить секцию</font></b></html>");

    final int type;
    short axis; // оси на РЦ
    private int axis_number = 0;// в дургой системе координат
    private int ir_timer;// текущее показание щётчика
    private int routestate = 1;// Default Value - "1 Незамкнута"
    private int boom_state = 1;// Default Value - "BOOM_OFF"://1 BOOM_OFF Ограждение пути снято MESS_RAILNET_BOMM_OFF // 286
    private int alarmstate;// Default Value - "NO_ALARM"://0 NO_ALARM Исправна
    private int od;// Ошибочные действия
    private boolean route;// Default Value from RN
    private boolean m_route;// Default Value from RN
    private boolean busy;// Default Value from RN
    boolean ir;// Default Value;//есть или нет ИР from RN
    boolean block;// Default Value from RN
    private boolean flt_og_r; // FUFUFU FUCK Victor Default value and как будет
    // работать когда нет ограждения путей на станции
    private boolean err_dsp; // FUFUFU FUCK Victor Default value and как будет
    // работать когда нет ограждения путей на станции
    private boolean flt_og_sv;// FUFUFU FUCK Victor Default value and как будет
    // работать когда нет ограждения путей на станции
    private boolean alarm;// Default Value
    private boolean irLow, alarmLow, blockLow;
    private final java.awt.event.ActionListener RAILNET1 = (ActionEvent evt) -> {
        if (ir) {
            irLow = !irLow;
            lblName.setBackground(cmdIRBackground);
            if (irLow) {
                lblName.setBorder(cmdIRBorderLow);
            } else {
                lblName.setBorder(cmdIRBorderHigh);
            }
        }
        if (alarm) {
            alarmLow = !alarmLow;
            lblName.setBackground(cmdAlarmBackground);
            if (alarmLow) {
                lblName.setBorder(cmdAlarmBorderLow);
            } else {
                lblName.setBorder(cmdAlarmBorderHigh);
            }
        }
        if (block) {
            blockLow = !blockLow;
            lblName.setBackground(cmdBlockBackground);
            lblName.setForeground(cmdBlockForeground);
            if (blockLow) {
                lblName.setBorder(cmdBLockBorder);
            } else {
                lblName.setBorder(cmdBLockBorder);
            }
        } else {
            lblName.setForeground(cmdIdleForeground);
        }
    };
//    private final javax.swing.Timer blinkTimer = new javax.swing.Timer(600, blinkPerformer);
    private boolean skidded;// default value - башмаки установлены или нет.
    private boolean boom_cancel;// //Default Value //false 0 BOOM_CANSEL_OFF Нет аварийной отмены заграждения
    private boolean boomZapr;// запрос ограждения
    private boolean boomSet;//
    private Timer cmdIRTimer;
    private Timer cmdBoomTimer;
//    int byteNumber = -1;
//    int bitNumber = -1;
    private final int S_X;
    private final int S_Y;
    private final boolean vgn_lst;//показывать или нет Список вагонов в меню.

// (DIRECT_TYPE) не используется(Бесстрелочный участок в горловине станции...)
    Railnet(
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
            int ID_TYPE, //10=ID_TYPE - RN_TYPE числом
            //                getString(tokens[9]), // RN_TYPE ??
            //                getString(tokens[10]),// RN_TYPE_MPC ??
            boolean SKIDABLE,//11= SKIDABLE
            boolean BLOCK,//12=boom (BLOCK)
            boolean SHOW_AXLE,//13=SHOW_AXLE
            //        int LENGTH, //14=LENGTH
            //        int IND, //15=IND
            //        int CMD, //16=CMD
            boolean VGN_LST
    ) {
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 14);// 14=RAILNET
// -------------------------------Translate-------------------------------------
        menu_title = "<html><b>" + Util.objType(ID_OBJ) + " <font color=blue>" + S_NAME + "</font></b></html>";
// -----------------------------------------------------------------------------

        //    s_name = S_NAME;
        boom = BLOCK;
        show_axle = SHOW_AXLE;
        skidAble = SKIDABLE;// рельса обошмачивается

        vgn_lst = VGN_LST;//Показывать или нет меню "Список вагонов"

        if (BLOCK) {
            loadKey(ID_OBJ);
        }
        // setCellType("RAILNET");
//        vStatus = VIDEO_STATUS;
        type = ID_TYPE;
        S_X = SHIFT_X;
        S_Y = SHIFT_Y;
        setPanes(ORIENTATION, VIDEO_STATUS);
    }

    private boolean isAnyRoute() {
        return (route || m_route);
    }

    /*
1	Стрелочный участок в горловине станции	TOUT_TYPE
2	Бесстрелочный участок в горловине станции	DIRECT_TYPE
3	Участок приближения	ONCOMING_TYPE
4	Перегон	SPAN_TYPE
5	Межстанционный путь	INTERSTATION_TYPE
6	Тупик	DEADLOCK_TYPE
7	Подъездной путь	SPUR_TYPE
8	Неконтролируемая зона	UNRULED_TYPE
9	Станционный путь	TRACK_TYPE
     */
    private void setPanes(int ORIENTATION, int VIDEO_STATUS) {
        rnIrItem.setFont(Terminal.SANS14);
        rnBlockItem.setFont(Terminal.SANS14);
        rnUnBlockItem.setFont(Terminal.SANS14);
        rnSkidItem.setFont(Terminal.SANS14);
        rnUnSkidItem.setFont(Terminal.SANS14);
        rnHollowCancelItem.setFont(Terminal.SANS14);
        rnBommItem.setFont(Terminal.SANS14);
        rnBoomCancelItem.setFont(Terminal.SANS14);
        //name
        lblName.setText(s_name);
        lblName.setOpaque(true);
        lblName.setBackground(cmdIdleBackground);
        lblName.setBorder(cmdIdleBorder);
        lblName.setFont(Terminal.SANS11);//zoom
        switch (Terminal.zoom) {
            case 72:
                lblName.setFont(Terminal.SANS11);//zoom
                break;
            case 56:
                lblName.setFont(Terminal.SANS10);//zoom
                break;
            case 36:
                lblName.setFont(Terminal.SANS08);//zoom
                break;
        }
        
        
        
        lblName.setForeground(cmdIdleForeground);
        lblName.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblName.setVerticalAlignment(javax.swing.JLabel.CENTER);

//        int wordNum = lblName.getText().split("~").length;
//        int cmdW = 0;
//        int cmdH;
//        if (wordNum > 1) {
//            cmdH = fm.getHeight() * wordNum;
//            for (String s : lblName.getText().split("~")) {
//                if (fm.stringWidth(s) > cmdW) {
//                    cmdW = fm.stringWidth(s);
//                }
//            }
//            lblName.setText("<html>" + lblName.getText().replace("~", "<br>") + "</html>");
//        } else {
        int cmdW = lblName.getFontMetrics(lblName.getFont()).stringWidth(s_name);
//        int cmdH = 16;//lblName.getFontMetrics(lblName.getFont()).getHeight();//zoom
        int cmdH = lblName.getFontMetrics(lblName.getFont()).getHeight() + 2;//zoom
//      }
        // int cmdW =
        // lblCmdRnName.getFontMetrics(lblCmdRnName.getFont()).stringWidth(this.s_name);
        // int cmdH =
        // lblCmdRnName.getFontMetrics(lblCmdRnName.getFont()).getHeight() *
        // lblCmdRnName.getText().split(" ").length;

        if (Terminal.DSP && (VIDEO_STATUS == 0)) {// DSP

            Popup.setBorder(BorderFactory.createTitledBorder(menu_title));
            Popup.setBorderPainted(true);
            // rnPopup.add(rnPopupTitle.replace("?", this.logObjTypeMpc +
            // " <font color=blue>" + this.s_name + "</font>"));
            // rnPopup.addSeparator();
//            if ((RN_TYPE.equals("DIRECT_TYPE") || RN_TYPE.equals("TOUT_TYPE"))) {// FFF FUCK FUFUFU Victor переделать в  int
            if (type == 1 || type == 2) {//RN_TYPE.equals("DIRECT_TYPE") || RN_TYPE.equals("TOUT_TYPE")
                Popup.add(rnIrItem);
                Popup.addSeparator();
                rnIrItem.addActionListener((java.awt.event.ActionEvent evt) -> rnIrItemActionPerformed());
            }

//            if (RN_TYPE.equals("DIRECT_TYPE")// FFF FUCK FUFUFU Victor переделать в int
//                    || RN_TYPE.equals("TOUT_TYPE")// FFF FUCK FUFUFU Victor переделать в int
//                    || RN_TYPE.equals("TRACK_TYPE")// FFF FUCK FUFUFU Victor переделать в int
//                    || RN_TYPE.equals("INTERSTATION_TYPE") // ||
//                    // getLogObjType().equals("ONCOMING_TYPE")  //FFF FUCK FUFUFU Victor переделать в  int
//                    ) {
            if (type == 1 || type == 2 || type == 5 || type == 9) {//RN_TYPE.equals("DIRECT_TYPE") || RN_TYPE.equals("TOUT_TYPE") || RN_TYPE.equals("INTERSTATION_TYPE") || RN_TYPE.equals("TRACK_TYPE")
                Popup.add(rnBlockItem);
                rnBlockItem.addActionListener((java.awt.event.ActionEvent evt) -> rnBlockItemActionPerformed());
                Popup.add(rnUnBlockItem);
                rnUnBlockItem.addActionListener((java.awt.event.ActionEvent evt) -> rnUnBlockItemActionPerformed());
                Popup.addSeparator();
            }

//            if (!(RN_TYPE.equals("UNRULED_TYPE"))) {// FUCK FFF FUFUFU Victor неконтролируемая зона ! оси на названии рисовать  не надо! (и меню не надо)
            if (type != 8) {// 8 Неконтролируемая зона UNRULED_TYPE
                Popup.add(rnHollowCancelItem);
                rnHollowCancelItem.addActionListener((java.awt.event.ActionEvent evt) -> rnHollowCancelItemActionPerformed());
                Popup.addSeparator();
                if (skidAble) {
                    Popup.add(rnSkidItem);
                    rnSkidItem.addActionListener((java.awt.event.ActionEvent evt) -> rnSkidItemActionPerformed());
                    Popup.add(rnUnSkidItem);
                    rnUnSkidItem.addActionListener((java.awt.event.ActionEvent evt) -> rnUnSkidItemActionPerformed());
                }
                if (boom) {
                    Popup.add(rnBommItem);
                    rnBommItem.addActionListener(evt1 -> rnrnBommItemActionPerformed());
                    Popup.add(rnBoomCancelItem);
                    rnBoomCancelItem.addActionListener((java.awt.event.ActionEvent evt) -> rnrnBoomCancelItemActionPerformed());
                }// end if boom
                if (Util.GetUserGroups("SIM")) {
                    Popup.addSeparator();
                    rnSimBusyItem.addActionListener(evt -> rnSimBusyItemActionPerformed());
                    Popup.add(rnSimBusyItem);
                    rnSimUnBusyItem.addActionListener(evt -> rnSimUnBusyItemActionPerformed());
                    Popup.add(rnSimUnBusyItem);
                }

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
                lblName.setComponentPopupMenu(Popup);
                lblName.setCursor(RN_CURSOR);
            }// end if UNRULED_TYPE
            if (vgn_lst) {
                Popup.add(rnVgnLstItem);
                rnVgnLstItem.addActionListener(evt -> rnVgnLstItemActionPerformed());
            }
        }// end DSP

        switch (Terminal.zoom) {
            case 72:
                switch (ORIENTATION) {//zoom
                    case 3:
                        shift_x = 48;// 72/2 = 24 24*2=48
                        shift_y = 24 - cmdH; // 72/3 = 24 *1
                        break;
                    case 6:
                        shift_x = 48;
                        shift_y = 36 - cmdH / 2 - 2;
                        break;
                    case 9:
                        shift_x = 48;
                        shift_y = 48;
                        break;
                    case 12:
                        shift_x = 36 - cmdW / 2 - 2;
                        shift_y = 48;
                        break;
                    case 15:
                        shift_x = 24 - cmdW - 4;
                        shift_y = 48;
                        break;
                    case 18:
                        shift_x = 24 - cmdW - 4;
                        shift_y = 36 - cmdH / 2;
                        break;
                    case 21:
                        shift_x = 24 - cmdW - 4;
                        shift_y = 24 - cmdH;
                        break;
                    case 24:
                        shift_x = 36 - cmdW / 2 - 2;
                        shift_y = 24 - cmdH;
                        break;
                }
                break;
            case 56:
                switch (ORIENTATION) {//zoom
                    case 3:
                        shift_x = 48;// 72/2 = 24 24*2=48
                        shift_y = 24 - cmdH; // 72/3 = 24 *1
                        break;
                    case 6:
                        shift_x = 48;
                        shift_y = 36 - cmdH / 2 - 2;
                        break;
                    case 9:
                        shift_x = 48;
                        shift_y = 48;
                        break;
                    case 12:
                        shift_x = 36 - cmdW / 2 - 2;
                        shift_y = 48;
                        break;
                    case 15:
                        shift_x = 24 - cmdW - 4;
                        shift_y = 48;
                        break;
                    case 18:
                        shift_x = 24 - cmdW - 4;
                        shift_y = 36 - cmdH / 2;
                        break;
                    case 21:
                        shift_x = 24 - cmdW - 4;
                        shift_y = 24 - cmdH;
                        break;
                    case 24:
                        shift_x = 36 - cmdW / 2 - 2;
                        shift_y = 24 - cmdH;
                        break;
                }
                break;
            case 36:
                switch (ORIENTATION) {//zoom
                    case 3:
                        shift_x = 24;// 72/3 = 24 24*2=48 36/3=12*2=24
                        shift_y = 12 - cmdH; // 72/3 = 24 *1 36/3=12*1
                        break;
                    case 6:
                        shift_x = 24;
                        shift_y = 18 - cmdH / 2 - 2; //72/2=36 36/2=
                        break;
                    case 9:
                        shift_x = 24;
                        shift_y = 48;
                        break;
                    case 12:
                        shift_x = 18 - cmdW / 2 - 2;
                        shift_y = 24;
                        break;
                    case 15:
                        shift_x = 12 - cmdW - 4;
                        shift_y = 24;
                        break;
                    case 18:
                        shift_x = 12 - cmdW - 4;
                        shift_y = 18 - cmdH / 2;
                        break;
                    case 21:
                        shift_x = 12 - cmdW - 4;
                        shift_y = 12 - cmdH;
                        break;
                    case 24:
                        shift_x = 18 - cmdW / 2 - 2;
                        shift_y = 12 - cmdH;
                        break;
                }
                break;
        }//end zoom
        cmdX += shift_x;
        cmdY += shift_y;

        lblName.setLocation(cmdX, cmdY);
        lblName.setSize(cmdW + 6, cmdH);
        lblName.setVisible(true);
        lblName.setVisible(!(VIDEO_STATUS == 2));// 2-INVISIBLE

//        cmdIRTimer = new TimerCell(GX, GY, shift_x + lblName.getX(), shift_y + lblName.getY() + lblName.getHeight());
        if (type != 8 || type != 9) { //таймера исскуственной разделки нету на пути и у неконтролируемого блока
//            cmdIRTimer = new Timer(GX, GY, shift_x + lblName.getX(), shift_y + lblName.getY() + lblName.getHeight());
            cmdIRTimer = new Timer(GX, GY, shift_x + S_X, shift_y + lblName.getHeight() + S_Y);
        }

//        System.out.println("new TimerCell " + GX + " " +  GY + " " +  shift_x + " " +  lblName.getX() + " " +  shift_y + " " +  lblName.getY() + " " +  lblName.getHeight() );
        if (boom) {
//            cmdBoomTimer = new TimerCell(GX, GY, shift_x + lblName.getX(), shift_y + lblName.getY() + lblName.getHeight());
            cmdBoomTimer = new Timer(GX, GY, shift_x + S_X, shift_y + S_Y + lblName.getHeight());
        }
        terminal.Commander.cmdLayers.add(lblName);
        terminal.Commander.cmdLayers.setLayer(lblName, 19);
        if (VIDEO_STATUS == 1) {
            lblName.setForeground(Color.GRAY);
            lblName.setBackground(new java.awt.Color(204, 204, 204));
            lblName.setBorder(BorderFactory.createLineBorder(java.awt.Color.GRAY));
        }
    }

    void setState(
            long DTIME,
            boolean BUSYSTATE,
            int ROUTESTATE,
            boolean BLOCKSTATE,
            boolean IR,
            int ALARMSTATE,
            boolean SKIDDEDSTATE,
            int AXIS_NUMBER,
            int IR_TIMER,
            int BOOM_STATE,
            boolean FLT_OG_R,
            boolean BOOM_CANSEL,
            int OD,
            boolean ERR_DSP,
            boolean FLT_OG_SV
    ) {
        setDtime(DTIME);
        if (vStatus == 0) {

            if ((axis_number != AXIS_NUMBER) && !IR && !BOOM_CANSEL) {
                axis_number = AXIS_NUMBER;
                // axis = (short) ((AXIS_NUMBER >> 8) + ((AXIS_NUMBER &
                // 0x000000ff) << 8));//Siemens
                axis = (short) (AXIS_NUMBER);
//----------------------------------
                // FUFUFU FFF Victor показывать оси на рельсовых цепях или нет
                if (show_axle) {
                    if (type == 9) {
                        lblName.setText(s_name + " (" + axis + "); у.в.: " + String.valueOf((axis % 4 != 0) ? ((axis / 4) + 1) : (axis / 4)));
                        int cmdW = lblName.getFontMetrics(lblName.getFont()).stringWidth(s_name + " (" + axis + "); у.в.: " + String.valueOf((axis % 4 != 0) ? ((axis / 4) + 1) : (axis / 4)));
                        int cmdH = 16;
                        lblName.setSize(cmdW + 8, cmdH);
                    } else {
                        lblName.setText(s_name + " (" + axis + ")");
                        int cmdW = lblName.getFontMetrics(lblName.getFont()).stringWidth(s_name + " (" + axis + ")");
                        int cmdH = 16;
                        lblName.setSize(cmdW + 8, cmdH);
                        lblName.setToolTipText("осей: " + axis + "; у.в.: " + String.valueOf((axis % 4 != 0) ? ((axis / 4) + 1) : (axis / 4)));
                    }
                }

                // Popup.setBorder(BorderFactory.createTitledBorder(rnPopupTitle.replace("?",
                // "<font color=blue>" + s_name + "; Осей: " + axis + "</font>")));
//            Popup.setBorder(BorderFactory.createTitledBorder(menu_title + "Осей: " + axis));// fuck <html><b> + Util.objType(ID_OlocalhostBJ) +  <font color=blue> + S_NAME + </font></b></html> ----->>> + axis <<<------ NOT WORKING;
//            Popup.setBorderPainted(true);
//----------------------------------
            }

            /*
             * 0 BUSY Занята MESS_RAILNET_BUSY 287 1 EMPTY Свободна
             * MESS_RAILNET_EMPTY 291
             */
            // ---------------------------БЛЯ ТУТ ВСЁ НАОБОРОТ!-----------------
            //перерисовываем при BUSY, IR, BLOCK, ROUTE, M_ROUTE
            if (busy == BUSYSTATE) { // !!!!!!!!!!!!!!!!!!!!! помоему тут всё на оборот !!!!!!!!!!!!!!
                busy = !BUSYSTATE;
                if (busy) {// true 1 EMPTY Свободна MESS_RAILNET_EMPTY 287
                    event(287);
                } else {// false 0 BUSY Занята MESS_RAILNET_BUSY 291
                    event(291);
                    if (type == 4) { //Перегон
                        Terminal.Mpab_Hash.keySet().stream().map((MPAB_ID) -> Terminal.Mpab_Hash.get(MPAB_ID)).forEach((m) -> {
                            if (m._00) {//Путевой Прием
                                new Sound(5).start();
                            }
                        });
                    }
                }
            }
            /*
             * 0 M_ROUTE Маневровый маршрут 1 - Незамкнута 2 ROUTE Поездной
             * маршрут
             */
            if (routestate != ROUTESTATE) {//перерисовываем при BUSY, IR, BLOCK, ROUTE, M_ROUTE + DK
                routestate = ROUTESTATE;
//DK берем из всех стрелок в секции все DK
                switch (ROUTESTATE) {// int
                    case 1:// "-"://1 - Незамкнута
                        route = false;
                        m_route = false;
                        break;
                    case 2:// "ROUTE"://2 ROUTE Поездной маршрут
                        route = true;
                        m_route = false;
                        break;
                    case 0:// "M_ROUTE"://0 M_ROUTE Маневровый маршрут
                        route = false;
                        m_route = true;
                        break;
                } //end switch
            } //end if route
            /*
             * 0 UNBLOCK Разблокирована MESS_RAILNET_UNBLOCK 297 1 BLOCK
             * Блокирована MESS_RAILNET_BLOCK 279
             */
            if (block != BLOCKSTATE) {//перерисовываем при BUSY, IR, BLOCK, ROUTE, M_ROUTE
                block = BLOCKSTATE;
                if (BLOCKSTATE) {//true 1 BLOCK Блокирована MESS_RAILNET_BLOCK 279
                    event(279);
                } else {//false 0 UNBLOCK Разблокирована MESS_RAILNET_UNBLOCK 297
                    event(297);
                }
            }
            /*
             * 0 - Нет искуственной разделки 1 IR Искутвенная разделка
             */
            if (ir != IR) {//перерисовываем при BUSY, IR, BLOCK, ROUTE, M_ROUTE
                ir = IR;
            }
            if (IR) {// true 1 IR Искутвенная разделка-------------Тут надо каждый раз переустанавливать счётчик
                if (ir_timer != IR_TIMER) {
                    ir_timer = IR_TIMER;
                    // cmdIRTimer.setState((IR_TIMER >> 8) + (IR_TIMER &
                    // 0x000000ff << 8));//Siemens
                    cmdIRTimer.setState(IR_TIMER);
                }
            } else {// false 0 - Нет искуственной разделки
                ir_timer = 0;
                cmdIRTimer.setState(0);
            }
            /*
             * 0 NO_ALARM Исправна 1 ALARM_COUNTER Неисправность секции
             * (неисправность датчиков СО) 2 COUNTING_ERROR Отрицательное к-во
             * осей на секции MESS_RAILNET_COUNT_ERR 288 3 GLOBAL_ERROR Общая
             * Неисправность MESS_RAILNET_COUNT_ERR 288
             */
            if (alarmstate != ALARMSTATE) {
                alarmstate = ALARMSTATE;
                switch (ALARMSTATE) {// int
                    case 0:// "NO_ALARM"://0 NO_ALARM Исправна
                        alarm = false;
                        // setHollowCancelItemOff(false);
                        break;
                    case 1:// "ALARM_COUNTER"://1 ALARM_COUNTER Неисправность секции (неисправность датчиков СО) setState
                        alarm = true;
                        // setHollowCancelItemOff(true);
                        break;
                    case 2:// "COUNTING_ERROR"://2 COUNTING_ERROR Отрицательное к-во осей на секции MESS_RAILNET_COUNT_ERR 288
                        alarm = false;
                        // setHollowCancelItemOff(false);
                        event(288);
                        break;
                    case 3://"GLOBAL_ERROR"://3 GLOBAL_ERROR Общая Неисправность MESS_RAILNET_COUNT_ERR 288
                        alarm = true;
                        // setHollowCancelItemOff(true);
                        event(288);
                        break;
                }
            }
            // =================================================================
            if (skidAble) {// если рельса обошмачивется
                /*
                 * 0 UNSKIDDED Башмаки сняты MESS_RAILNET_UNSKID 198 1 SKIDDED
                 * Башмаки установлены MESS_RAILNET_SKID 196
                 */
                if (skidded != SKIDDEDSTATE) {
                    skidded = SKIDDEDSTATE;
                    Direct dc = findDirect();
                    if (!(dc == null)) {
                        dc.setSkidded(SKIDDEDSTATE);
                        // return;
                    }
                    Deadlock dlc = findDeadlock();
                    if (!(dlc == null)) {
                        dlc.setSkidded(SKIDDEDSTATE);
                    }
                    if (SKIDDEDSTATE) {// true 1 SKIDDED Башмаки установлены
                        // MESS_RAILNET_SKID 196
                        event(296);//<html><font color=olive>Башмаки установлены</html>
                    } else {// false 0 UNSKIDDED Башмаки сняты
                        // MESS_RAILNET_UNSKID 198
                        event(298);//<html><font color=olive>Башмаки сняты</html>
                    }
                }
            }
            // =================================================================
            if (boom) {// (рельса ограждается)
                /*
                 * 0 BOOM_XZ нет ограждения 1 BOOM_OFF Ограждение пути снято
                 * MESS_RAILNET_BOMM_OFF 286 2 BOMM_ZAPR Запрос на ограждение
                 * пути MESS_RAILNET_BOMM_ZAPR 285 4 BOOM_ON Путь огражден
                 * MESS_RAILNET_BOMM_ON 284
                 */
                if (boom_state != BOOM_STATE) {
                    boom_state = BOOM_STATE;
                    switch (BOOM_STATE) {// int
                        case 1:// "BOOM_OFF"://1 BOOM_OFF Ограждение пути снято
                            // MESS_RAILNET_BOMM_OFF 286
                            boomSet = false;
                            boomZapr = false;
                            event(286);
                            break;
                        case 2:// "BOMM_ZAPR"://2 BOMM_ZAPR Запрос на ограждение
                            // пути MESS_RAILNET_BOMM_ZAPR 285
                            boomSet = false;
                            boomZapr = true;
                            event(285);
                            break;
                        case 4:// "BOOM_ON"://4 BOOM_ON Путь огражден
                            // MESS_RAILNET_BOMM_ON 284
                            boomSet = true;
                            boomZapr = false;
                            event(284);
                            break;
                        default:
                            break;
                    }
                }
                /*
                 * 0 BOOM_CANSEL_OFF Нет аварийной отмены заграждения 1
                 * BOOM_CANSEL_ON Аварийная отмена ограждения
                 * MESS_RAILNET_BOMM_CAMSEL 280
                 */
                if (boom_cancel != BOOM_CANSEL) {
                    boom_cancel = BOOM_CANSEL;
                    if (BOOM_CANSEL) {
                        event(280);
                    }
                }

                if (BOOM_CANSEL) {// true 1 BOOM_CANSEL_ON Аварийная отмена
                    // ограждения MESS_RAILNET_BOMM_CAMSEL
                    // 280
                    // cmdBoomTimer.setState((IR_TIMER >> 8) + (IR_TIMER &
                    // 0x000000ff << 8));//Siemens
                    cmdBoomTimer.setState(IR_TIMER);
                } else {// false 0 BOOM_CANSEL_OFF Нет аварийной отмены
                    // заграждения
                    cmdBoomTimer.setState(0);
                }

// ------------------------------if boom -----------------------------------
                if (flt_og_r != FLT_OG_R) {
                    flt_og_r = FLT_OG_R;
                    if (FLT_OG_R) {// true 1 282 MESS_RAILNET_BOMM_FLT_OG_R
                        event(282);
                        alarm_on(136);// 1
                    } else {// false 0
                        alarm_off(136);// 0

                    }
                }
                // -------------------------------------------------------------
                if (err_dsp != ERR_DSP) {
                    err_dsp = ERR_DSP;
                    if (ERR_DSP) {// true 1
                        event(281);
                    }  // false 0 // event();

                }
                // -------------------------------------------------------------
                if (flt_og_sv != FLT_OG_SV) {
                    flt_og_sv = FLT_OG_SV;
                    if (FLT_OG_SV) {// true 1
                        event(283);
                        alarm_on(137);// 1
                    } else {// false 0
                        alarm_off(137);// 0

                    }
                }
                // -------------------------------------------------------------
                for (int i = 0; i < counterIdKey.size(); i++) {// --НАКУЯ ЦИКЛ?-
//                    CounterCell c = (CounterCell) Terminal.mainCellsHash.get(counterIdKey.get(i));
                    Counter c = Terminal.CounterCell_Hash.get(counterIdKey.get(i));
                    if (c != null) {
                        c.setFenceState(BOOM_STATE, FLT_OG_R, BOOM_CANSEL);
                    }
                }
// ----------------------------end if boom---------------------------------
            }// end if boom (рельса ограждается)
            // =================================================================
            if (alarm || block || ir) {
//                if (!blinkTimer.isRunning()) {
//                    blinkTimer.start();
//                }
                Terminal.TIMER600.addActionListener(RAILNET1);
            } else {
//                if (blinkTimer.isRunning()) {
//                    blinkTimer.stop();
//                }
                Terminal.TIMER600.removeActionListener(RAILNET1);
                lblName.setForeground(cmdIdleForeground);
                lblName.setBackground(cmdIdleBackground);
                lblName.setBorder(cmdIdleBorder);
                lblName.repaint();
            }
            //перерисовываем при BUSY, IR, BLOCK, ROUTE, M_ROUTE
            if (type == 1) {//TOUT_TYPE
//TOUT_TYPE                Terminal.Area_Hash.values().stream().filter((ar) -> (ar.last_route == 0)).forEachOrdered();
//                System.out.println("---=== Установим DK для секции " + s_name + " ");
                Terminal.Turnouts_Hash.values().stream().filter((tc) -> (tc.rn_id == this.id_obj)).forEach((Turnout tc) -> tc.setStateRN(busy, ir, block, route, m_route));
            }//end tout_type
            Terminal.DirectCell_Hash.values().stream().filter((dc) -> ((dc.id_rn == this.id_obj) && dc.chain == 0)).forEach((Direct dc) -> { //NO_TOUT == 0
                dc.setState(busy, ir, block, route, m_route);
            });
            Terminal.DeadlockCell_Hash.values().stream().filter((dd) -> (dd.id_rn == this.id_obj)).forEach((dd) -> {//тут можно сделать dd.id_obj но надо ID_OBJ в базе одинаковый с ID_RN ---- и тогда можно обойтись без цикла.
                dd.setState(busy);
            });
            /*
            * 0 OD 1 DSP_ERR MESS_RAILNET_ERR_DSP1 Установка маршрута
            * невозможна. Участок занят 292 2 DSP_ERR2 MESS_RAILNET_ERR_DSP2
             * Установка маршрута невозможна. Участок замкнут 293 4 DSP_ERR3
             * MESS_RAILNET_ERR_DSP3 Установка маршрута невозможна. Участок
             * заблокирован 294 8 CLER_ERR MESS_RAILNET_NOT_CLER Не выполняются
             * дополнительные условия безопасности 295
             */
            if (od != OD) {
                od = OD;
                if (((OD) & 1) == 1) {
                    event(292);//<html><font color=blue>Установка маршрута невозможна. Участок занят</html>
                }
                if (((OD >> 1) & 1) == 1) {
                    event(293);//<html><font color=blue>Установка маршрута невозможна. Участок замкнут</html>
                }
                if (((OD >> 2) & 1) == 1) {
                    event(294);//<html><font color=blue>Установка маршрута невозможна. Участок заблокирован</html>
                }
                if (((OD >> 3) & 1) == 1) {
                    event(295);//<html><font color=blue>Установка маршрута задерживается. Не выполняются дополнительные условия безопасности</html>
                }
//                switch (OD) {// int
//                    case 1:// "DSP_ERR"://1
//                        event(292);//<html><font color=blue>Установка маршрута невозможна. Участок занят</html>
//                        break;
//                    case 2:// "DSP_ERR2"://2
//                        event(293);//<html><font color=blue>Установка маршрута невозможна. Участок замкнут</html>
//                        break;
//                    case 4:// "DSP_ERR3"://4
//                        event(294);//<html><font color=blue>Установка маршрута невозможна. Участок заблокирован</html>
//                        break;
//                    case 8:// "CLER_ERR"://8
//                        event(295);//<html><font color=blue>Установка маршрута задерживается. Не выполняются дополнительные условия безопасности</html>
//                        break;
//                    default:
//                        break;
//                }// end switch

            }// end if OD

        }// end if video
    }

    private Direct findDirect() {
        for (Direct dc : Terminal.DirectCell_Hash.values()) {
            if ((dc.GX == GX) && (dc.GY == GY)) {
                return dc;
            }
        }
        return null;
    }

    private Deadlock findDeadlock() {
        for (Deadlock dd : Terminal.DeadlockCell_Hash.values()) {
//            if ((dd.GX == GX) && (dd.GY == GY)) { // - это неверно т.к. сравнивать коодинаты нельзя потому что название тупика может быть в соседней клеточке.
            if (dd.id_rn == this.id_obj) {
                return dd;
            }
        }
        return null;
    }

    private void loadKey(int ID_OBJ) {// victor Если в инитной табличке стоит на рельсе BOOM (BOLOCK= 1 то у неё есть ограждение составов.... ?
        int ii = 0;
        for (String[] s : boomer) {
            int ID_RAILNET = Integer.valueOf(s[0]);// ID_RAILNET
            int ID_COUNTER = Integer.valueOf(s[1]);// ID_COUNTER
            if (ID_OBJ == ID_RAILNET) {
                counterIdKey.put(ii, ID_COUNTER);
                ii++;
            }
        }
    }

    private void preparePopup() {
        rnIrItem.setEnabled((isAnyRoute() && !ir) && Status.work && Area.Manager_PC);
        rnBlockItem.setEnabled((!isAnyRoute() && !block) && Status.work && Area.Manager_PC);
        rnUnBlockItem.setEnabled(block && Status.work && Area.Manager_PC);
        rnHollowCancelItem.setEnabled(Status.work && Area.Manager_PC);
        rnSkidItem.setEnabled((busy && !skidded) && Status.work && Area.Manager_PC);
        rnUnSkidItem.setEnabled(skidded && Status.work && Area.Manager_PC);
        rnBommItem.setEnabled(boomZapr && Status.work && Area.Manager_PC);
        rnBoomCancelItem.setEnabled(boomSet && Status.work && Area.Manager_PC);
        if (Util.GetUserGroups("SIM")) {
            rnSimBusyItem.setEnabled(!busy && Status.work && Area.Manager_PC);
            rnSimUnBusyItem.setEnabled(busy && Status.work && Area.Manager_PC);
        }
    }

    private void rnIrItemActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=5 style=bold><b> "
                + OPERATION
                + "!!</b></font><br>Подтвердите запуск искусственной разделки<br> для участка/секции <b>"
                + s_name + "</b></html>")) {
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP(id_obj, "RAILNET_IR_PHASE1");
                pause();
                // play();
                Net.sendMaskedCmd_DSP(id_obj, "RAILNET_IR_PHASE2");
                break;
        }
    }

    private void rnBlockItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "RAILNET_BLOCK_ON");
    }

    private void rnUnBlockItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "RAILNET_BLOCK_OFF");
    }

    private void rnSkidItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "RAILNET_SKID_INSTALL");
    }

    private void rnUnSkidItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "RAILNET_SKID_REMOVE");
    }

    private void rnHollowCancelItemActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=5 style=bold><b> "
                + OPERATION
                + "!!</b></font><br>Подтвердите сброс ложной занятости<br> участка/секции <b>"
                + s_name + "</b></html>")) {
            case 0:
                break;
            case 1:
                //
                Net.sendMaskedCmd_DSP(id_obj, "RAILNET_SLZ_PHASE1");
                pause();
                // play();
                Net.sendMaskedCmd_DSP(id_obj, "RAILNET_SLZ_PHASE2");
                break;
        }
    }

    private void rnrnBoomCancelItemActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=5 style=bold><b> "
                + OPERATION
                + "!!</b></font><br>Подтвердите аварийную отмену заграждения<br> участка/секции "
                + s_name + "</html>")) {
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP(id_obj, "RAILNET_BOOM_AOOG_P1");
                pause();
                // new Sound("alarm").start(); //play();
                Net.sendMaskedCmd_DSP(id_obj, "RAILNET_BOOM_AOOG_P2");
                break;
        }
    }

    private void rnrnBommItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "RAILNET_BOOM_SOG_P1");
        pause();
        // play();
        Net.sendMaskedCmd_DSP(id_obj, "RAILNET_BOOM_SOG_P2");
    }

    private void rnSimBusyItemActionPerformed() {
//        if ((byteNumber != -1) | (bitNumber != -1)) {
//            Net.sendDirectCmd_SIM(12, String.valueOf(byteNumber) + "." + String.valueOf(bitNumber) + ".0");//SIEMENS //включить
//        }
        Net.sendMaskedCmd_SIM(id_obj, "RAILNET_SIM_Z0");
    }

    private void rnSimUnBusyItemActionPerformed() {
//        if ((byteNumber != -1) | (bitNumber != -1)) {
//            Net.sendDirectCmd_SIM(12, String.valueOf(byteNumber) + "." + String.valueOf(bitNumber) + ".1");//SIEMENS //выключить
//        }
        Net.sendMaskedCmd_SIM(id_obj, "RAILNET_SIM_Z1");
    }

    private void rnVgnLstItemActionPerformed() {
        Net.Send("VGN_LST:" + String.valueOf(id_obj));
        Log.log("VGN_LST:" + String.valueOf(id_obj));
    }

    private void event(int id_msg) {
        if (getDtime() > 0) {
            if (type != 8) {// 8 Неконтролируемая зона UNRULED_TYPE - не нужно писать сообщения
                Events.InsertMessage(getDtime(), id_obj, id_msg);
            }
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

    private void pause() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Err.err(e);
        }
    }

    @Override
    public void setState(Object oid) {
        RailNetState r = (RailNetState) oid;
        setState(
                r.timestamp,
                r.BUSYSTATE,
                r.ROUTESTATE,
                r.BLOCKSTATE,
                r.IR,
                r.ALARMSTATE,
                r.SKIDDEDSTATE,
                r.AXIS_NUMBER,
                r.IR_TIMER,
                r.BOOM_STATE,
                r.FLT_OG_R,
                r.BOOM_CANSEL,
                r.OD,
                r.ERR_DSP,// только алармы
                r.FLT_OG_SV
        );
    }
}

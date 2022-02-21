//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
/*
 Видео статус не нужно хранить ни в каких переменных - его надо передать один раз для начального рисования и всё. т.е. setPanes(videoStatus).
 1-FUTURE-строительство, 2-INVISIBLE-невидимый, 3-DEPRECATE-н.е.н.х.

    WARNING LIGHT = нет меню, один гонь, нет таймера, нет блокировки
    REPEAT (один огонь) черный, зеленый, перегорание зеленого - нет меню, один гонь, нет таймера, нет блокировки (цвета = 1,2,512)
 */
package terminal;

import ru.attrans.proc.objs.LightsState;
import static terminal.Commander.cmdLayers;

class Light extends Cell {

    // Lights layers
    private static final int LIGHTS_SOUL_LR = 10;//1+9
    private static final int LIGHTS_STAND_LR = 11;//2+9
    private static final int LIGHTS_LOWER_LIGHT_LR = 12;//3+9
    // -------------------------------------------------------------------------
    private static final int LIGHTS_UPPER_LIGHT_LR = 13;//4+9
    // static final int LIGHTS_LOWER_FAULT_LR = 5;
    // static final int LIGHTS_UPPER_FAULT_LR = 6;
    private static final int LIGHTS_BLOCK_LR = 16;//7+9
    private static final int LIGHTS_CONTROL_BUTTON_LR = 22;//17;//8+9
    private static final int LIGHTS_LAYER = 18;//9+9


    // СДЕЛАТЬ ОДИН ФАУЛТ НА ВСЕХ!!!!!!!!! //тут 5 строчки ниже закоментировать
    private static final javax.swing.ImageIcon faultRedLight = Terminal.mainPictureHash.get("light_red_fault");
    private static final javax.swing.ImageIcon faultBlueLight = Terminal.mainPictureHash.get("light_blue_fault");
    private static final javax.swing.ImageIcon faultGreenLight = Terminal.mainPictureHash.get("light_green_fault");
    private static final javax.swing.ImageIcon faultWhiteLight = Terminal.mainPictureHash.get("light_white_fault");
    private static final javax.swing.ImageIcon faultYellowLight = Terminal.mainPictureHash.get("light_yellow_fault");
    private static final javax.swing.ImageIcon faultLight = Terminal.mainPictureHash.get("light_fault");//--нахуй

    private final javax.swing.JLabel lblCmdLowerFault = new javax.swing.JLabel(Terminal.mainPictureHash.get("light_fault"));
    private final javax.swing.JLabel lblCmdUpperFault = new javax.swing.JLabel(Terminal.mainPictureHash.get("light_fault"));
    // СДЕЛАТЬ ОДИН ФАУЛТ НА ВСЕХ!!!!!!!!!

    private static final javax.swing.ImageIcon redLight = Terminal.mainPictureHash.get("light_red");
    private static final javax.swing.ImageIcon blueLight = Terminal.mainPictureHash.get("light_blue");
    private static final javax.swing.ImageIcon yellowLight = Terminal.mainPictureHash.get("light_yellow");
    private static final javax.swing.ImageIcon yellowBlinkLight = Terminal.mainPictureHash.get("light_yellow_blink");//gif
    private static final javax.swing.ImageIcon greenLight = Terminal.mainPictureHash.get("light_green");
    private static final javax.swing.ImageIcon whiteLight = Terminal.mainPictureHash.get("light_white");
    private static final javax.swing.ImageIcon whiteBlinkLight = Terminal.mainPictureHash.get("light_white_blink");//gif
    private static final javax.swing.ImageIcon blackLight = Terminal.mainPictureHash.get("light_black");
    private static final javax.swing.ImageIcon blockLight = Terminal.mainPictureHash.get("light_block");
    private static final javax.swing.ImageIcon futureLight = Terminal.mainPictureHash.get("light_future");

    private static final java.awt.Color idleBtnBackground = new java.awt.Color(225, 225, 225);
    private static final java.awt.Color pressBtnBackground = java.awt.Color.green.brighter();
    private static final java.awt.Color wrongBtnBackground = java.awt.Color.YELLOW;
    private static final java.awt.Color routeBtnBackground = java.awt.Color.GREEN;
    private static final javax.swing.border.Border idleBtnBorder = javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK, 2);
    private static final javax.swing.border.Border onlyLightBtnBorder = javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY, 1);

    private static final javax.swing.border.Border wrongBtnBorder = javax.swing.BorderFactory.createLineBorder(java.awt.Color.CYAN, 2);
    private static final javax.swing.border.Border cancelBtnBorder = javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED, 2);
    private static final javax.swing.border.Border pressedBorder = javax.swing.BorderFactory.createLineBorder(java.awt.Color.ORANGE, 2);
    private static final java.awt.Cursor LT_CURSOR = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(Terminal.mainPictureHash.get("lights_cursor").getImage(), new java.awt.Point(0, 0), "lights_cursor");
    private final javax.swing.JLayeredPane cmdCellPane = new javax.swing.JLayeredPane();
    // -------------------------------Translate--------------------------------------
    private final String menu_title;

//    private final int vStatus;

    private final boolean warningLights; // в таблице тип светофора WARNING - это TRAIN и предвходной у которого приходящий из сименса сигнал WHITE_BLINK - расшифровывается как YELLOW_BLINK
    private final boolean redProhibit;// true красный, false синий
    private final boolean withInviting;
    final int control;
    private final javax.swing.JPopupMenu Popup = new javax.swing.JPopupMenu();
    // блокировка
    private final javax.swing.JMenuItem ltBlockItem = new javax.swing.JMenuItem("<html>блокировка снята, <b><font color=orange>установить</font></b></html>");
    private final javax.swing.JMenuItem ltUnBlockItem = new javax.swing.JMenuItem("<html>блокировка установлена, <b><font color=green>снять</font></b></html>");
    // пригласительный
    private final javax.swing.JMenuItem ltInviteOnItem = new javax.swing.JMenuItem("<html><b><font color=blue>включение</font></b> пригласительного</html>");
    private final javax.swing.JMenuItem ltInviteOffItem = new javax.swing.JMenuItem("<html><b><font color=red>выключение</font></b> пригласительного</html>");
    private final javax.swing.JMenuItem ltInviteRepeateItem = new javax.swing.JMenuItem("<html><b><font color=orange>повтор</font></b> пригласительного</html>");
    // восстановление
    private final javax.swing.JMenuItem ltRecoverItem = new javax.swing.JMenuItem("<html><b><font color=green>восстановление</font></b> (повторное открытие)</html>");
    // симулятор
    private final javax.swing.JCheckBoxMenuItem ltSimRItem = new javax.swing.JCheckBoxMenuItem("<html>Cимулятор: <b><font color=blue>Перегорание разрешающего</font></b></html>");
    private final javax.swing.JCheckBoxMenuItem ltSimZItem = new javax.swing.JCheckBoxMenuItem("<html>Cимулятор: <b><font color=blue>Перегорание запрещающего</font></b></html>");

    private final javax.swing.JLabel lblCmdLowerLight = new javax.swing.JLabel();
    private final javax.swing.JLabel lblCmdUpperLight = new javax.swing.JLabel();
    private final javax.swing.JLabel lblName          = new javax.swing.JLabel();
    private final javax.swing.JLabel lblCmdLowerBlock = new javax.swing.JLabel();
    private final javax.swing.JLabel lblCmdUpperBlock = new javax.swing.JLabel();

    private Timer cmdLightsTimer = null;
    private boolean trainLights;// is train lights?
    private boolean zg_light;// is zg lights?
    private boolean repeat_light;//ПЧ1 эльга
    boolean block;// default value
    private boolean routed;// default value
    private boolean canceling;// default value - отмена
    private boolean block_error;// defaul value
    private boolean kmg;//контроь мигания
    private int light = 0;// default value "STOP" or default value "STOP_FAULT"
    // = 256 ?? тут храниться текуший цвет светофора
    private int route = 0;// default value
    private int btnstate = 0;// default value "RELEASE_BTN состояние нажания
    private javax.swing.border.Border storedBorder;// вроде не должна быть статик
    private int correct_y = 0;
    private boolean disabled;//Запрещаемый или нет для эльги в зависимости от направления свойство светофора из базы
    private boolean disable;//из контроллера - запрещен (становится серым)

    Light(
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
            boolean MOUNT, //10=MOUNT (0-ON_GROUND 1-ON_STAND)
            boolean RED_PROHIBIT, // STOP_LIGHT (0-BLUE, 1-RED)
            int TYPE, // (0-SHUNTING, 1-TRAIN, 2-WARNING)
            //            boolean TRAIN_LIGHT,
            //            boolean WARN
            int CONTROL, //13=CONTROL (0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
            boolean INVITING, //14=INVITING
    //        int IND, //15=IND
    //        int CMD //16=CMD
            boolean DISABLED //Запрещаемый или нет для эльги в зависимости от направления
    ) {
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 11);// 11=LIGHTS
        disabled = DISABLED;
        ltSimRItem.setSelected(true);
        ltSimZItem.setSelected(true);

        menu_title = "<html><b>" + Util.objType(ID_OBJ) + " <font color=blue>" + S_NAME + "</font></b></html>";

        // Popup.setName(l_menu.replace("?", S_NAME));
        ltBlockItem.setText(l_block_off_menu_item);
        ltUnBlockItem.setText(l_block_on_menu_item);
        ltInviteOnItem.setText(l_on_menu_item);
        ltInviteRepeateItem.setText(l_repeat_menu_item);
        ltInviteOffItem.setText(l_off_menu_item);
        ltRecoverItem.setText(l_recover_menu_item);

ltBlockItem.setFont(Terminal.SANS14);
ltUnBlockItem.setFont(Terminal.SANS14);
ltInviteOnItem.setFont(Terminal.SANS14);
ltInviteOffItem.setFont(Terminal.SANS14);
ltInviteRepeateItem.setFont(Terminal.SANS14);
ltRecoverItem.setFont(Terminal.SANS14);

        trainLights = TYPE == 1;
        warningLights = TYPE == 2;// в таблице тип светофора WARNING - это TRAIN и предвходной у которого приходящий из сименса  сигнал WHITE_BLINK - расшифровывается как YELLOW_BLINK
        zg_light = TYPE == 3;
        repeat_light = TYPE== 4;

//        if(warningLights){
//            trainLights = true;
//        }

        redProhibit = RED_PROHIBIT; // STOP_LIGHT (0-BLUE, 1-RED)
        control = CONTROL;//(0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
        withInviting = INVITING;
        // ---------------------------------------------------------------------
        if (!(VIDEO_STATUS == 2)){
        int cmdShift;

        lblName.setText(S_NAME);
        lblName.setOpaque(true);
        lblName.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblName.setVerticalAlignment(javax.swing.JLabel.CENTER);
        lblName.setBackground(idleBtnBackground);
        lblName.setBorder(idleBtnBorder);
        
        
        lblName.setFont(Terminal.SANS12);//zoom
        switch (Terminal.zoom) {
            case 72:
                lblName.setFont(Terminal.SANS12);//zoom
                break;
            case 56:
                lblName.setFont(Terminal.SANS10);//zoom
                break;
            case 36:
                lblName.setFont(Terminal.SANS08);//zoom
                break;
        }
        if (VIDEO_STATUS == 1) {
            lblName.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY, 2));
            lblName.setForeground(java.awt.Color.GRAY);
        }

        if (Terminal.DSP && vStatus == 0) {// DSP && VIDEO_STATUS
            lblName.setComponentPopupMenu(Popup);
            lblCmdLowerLight.setComponentPopupMenu(Popup);
            lblCmdUpperLight.setComponentPopupMenu(Popup);
            lblName.setCursor(LT_CURSOR);
            lblCmdLowerLight.setCursor(LT_CURSOR);
            lblCmdUpperLight.setCursor(LT_CURSOR);
            if (control != 1) {//(0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
                setLtPopup();
            }
        }// end DSP

        int cmdW = lblName.getFontMetrics(lblName.getFont()).stringWidth(lblName.getText()) + 4;

        if (cmdW <= 38) {
            cmdW = 38;
            if (Terminal.zoom == 36){//zoom
                cmdW = 26;//zoom
            }
        }
        if (CONTROL==1){
            lblName.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY, 1));
            cmdW = lblName.getFontMetrics(lblName.getFont()).stringWidth(lblName.getText()) + 4;
        }
        lblName.setSize(cmdW, 16);
        cmdShift = ((cmdW - 38) > 0) ? (cmdW - 38) : 0;

        javax.swing.JLabel lblCmdSoul = new javax.swing.JLabel();
        javax.swing.JLabel lblCmdStand = new javax.swing.JLabel();
        // ---------------------------------------------------------------------
        lblCmdLowerLight.setSize(16, 16);
        lblCmdUpperLight.setSize(16, 16);
        lblCmdUpperLight.setIcon(blackLight);

        cmdCellPane.setLayer(cmdCellPane.add(lblName), LIGHTS_CONTROL_BUTTON_LR);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdSoul), LIGHTS_SOUL_LR);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdLowerLight), LIGHTS_LOWER_LIGHT_LR);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdUpperLight), LIGHTS_UPPER_LIGHT_LR);

//---это повторяется и выполним 1 раз перед swith
        lblName.setLocation(38 - cmdW, 0);//lblName.setLocation(0, 0);
        if (MOUNT) {
            cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
        }
        // ---------------------------------------------------------------------
        switch (Terminal.zoom) {
            case 72:
        switch (ORIENTATION) {//zoom
            // <editor-fold defaultstate="collapsed" desc="case 1: ...">
            case 1:
                shift_x = 46;
                shift_y = -18;
                if (MOUNT) {
                    cmdCellPane.setSize(38 + cmdShift, 58);
                    lblCmdLowerLight.setLocation(0, 26);// 26
                    lblCmdUpperLight.setLocation(0, 42);// 42
                } else {
                    cmdCellPane.setSize(38 + cmdShift, 54);
                    lblCmdLowerLight.setLocation(0, 22);
                    lblCmdUpperLight.setLocation(0, 38);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 4);
                lblCmdSoul.setLocation(0, 18);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(16, 4);
                    lblCmdStand.setLocation(0, 22);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 23: ...">
            case 23://светофор смотрит вниз
                correct_y = -36;
                shift_x = -12 - cmdShift;
                shift_y = -18;
                if (MOUNT) {
                    cmdCellPane.setSize(38 + cmdShift, 58);
                    lblCmdLowerLight.setLocation(22 + cmdShift, 26);// 26
                    lblCmdUpperLight.setLocation(22 + cmdShift, 42);// 42
                } else {
                    cmdCellPane.setSize(38 + cmdShift, 54);
                    lblCmdLowerLight.setLocation(22 + cmdShift, 22);
                    lblCmdUpperLight.setLocation(22 + cmdShift, 38);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 4);
                lblCmdSoul.setLocation(22 + cmdShift, 18);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(16, 4);
                    lblCmdStand.setLocation(22 + cmdShift, 22);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 11: ...">
            case 11:
                if (MOUNT) {
                    lblName.setLocation(0, 42);
                    cmdCellPane.setSize(38 + cmdShift, 58);
                    lblCmdSoul.setLocation(0, 36);
                } else {
                    lblName.setLocation(0, 38);
                    cmdCellPane.setSize(38 + cmdShift, 54);
                    lblCmdSoul.setLocation(0, 32);
                }
                shift_x = 46;
                shift_y = 36;

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 4);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(16, 4);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(0, 32);
                }

                lblCmdLowerLight.setLocation(0, 16);// 26
                lblCmdUpperLight.setLocation(0, 0);// 42
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 13: ...">
            case 13:
                if (MOUNT) {
                    lblName.setLocation(0, 42);
                    lblCmdSoul.setLocation(22 + cmdShift, 36);
                    cmdCellPane.setSize(38 + cmdShift, 58);
                } else {
                    lblName.setLocation(0, 38);
                    lblCmdSoul.setLocation(22 + cmdShift, 32);
                    cmdCellPane.setSize(38 + cmdShift, 54);
                }
                shift_x = -12 - cmdShift;
                shift_y = 36;

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 4);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(16, 4);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(22 + cmdShift, 32);
                }

                lblCmdLowerLight.setLocation(22 + cmdShift, 16);// 26
                lblCmdUpperLight.setLocation(22 + cmdShift, 0);// 42
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 17: ...">
            case 17:
                if (MOUNT) {
                    cmdCellPane.setSize(80 + cmdShift, 16);
                    lblCmdLowerLight.setLocation(48 + cmdShift, 0);// 26
                    lblCmdUpperLight.setLocation(64 + cmdShift, 0);// 42
                } else {
                    cmdCellPane.setSize(76 + cmdShift, 16);
                    lblCmdLowerLight.setLocation(44 + cmdShift, 0);// 26
                    lblCmdUpperLight.setLocation(60 + cmdShift, 0);// 42

                }
                shift_x = -40 - cmdShift;
                shift_y = 46;

//                lblCmdLightsName.setLocation(0, 0);
                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(4, 16);

                lblCmdSoul.setLocation(40 + cmdShift, 0);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(4, 16);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(44 + cmdShift, 0);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 19: ...">
            case 19:
                if (MOUNT) {
                    cmdCellPane.setSize(80 + cmdShift, 16);
                    lblCmdLowerLight.setLocation(48 + cmdShift, 0);// 26
                    lblCmdUpperLight.setLocation(64 + cmdShift, 0);// 42
                } else {
                    cmdCellPane.setSize(76 + cmdShift, 16);
                    lblCmdLowerLight.setLocation(44 + cmdShift, 0);// 26
                    lblCmdUpperLight.setLocation(60 + cmdShift, 0);// 42
                }
                shift_x = -40 - cmdShift;
                shift_y = 10;

//                lblCmdLightsName.setLocation(0, 0);
                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(4, 16);

                lblCmdSoul.setLocation(40 + cmdShift, 0);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(4, 16);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(44 + cmdShift, 0);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 5: ...">
            case 5:
                if (MOUNT) {
                    lblName.setLocation(42, 0);
                    lblCmdSoul.setLocation(36, 0);
                    cmdCellPane.setSize(80 + cmdShift, 16);
                    shift_x = 32;
                    shift_y = 10;
                } else {
                    lblName.setLocation(38, 0);
                    lblCmdSoul.setLocation(32, 0);
                    cmdCellPane.setSize(76 + cmdShift, 16);
                    shift_x = 36;
                    shift_y = 10;
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(4, 16);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(4, 16);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(32, 0);
                }

                lblCmdLowerLight.setLocation(16, 0);// 26
                lblCmdUpperLight.setLocation(0, 0);// 42
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 7: ...">
            case 7:
                if (MOUNT) {
                    lblName.setLocation(42, 0);
                    lblCmdSoul.setLocation(36, 0);
                    cmdCellPane.setSize(80 + cmdShift, 16);
                    shift_x = 32;
                    shift_y = 46;
                } else {
                    lblName.setLocation(38, 0);
                    lblCmdSoul.setLocation(32, 0);
                    cmdCellPane.setSize(76 + cmdShift, 16);
                    shift_x = 36;
                    shift_y = 46;
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(4, 16);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(4, 16);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(32, 0);
                }

                lblCmdLowerLight.setLocation(16, 0);// 26
                lblCmdUpperLight.setLocation(0, 0);// 42
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 2: ...">
            case 2:
                correct_y = -38;
                if (MOUNT) {
                    lblName.setLocation(13, 0);
                    lblCmdSoul.setLocation(22 + cmdShift, 16);
                    cmdCellPane.setSize(52 + cmdShift, 54);
                    shift_x = 28 - cmdShift;
                    shift_y = -35;
                    lblCmdLowerLight.setLocation(12 + cmdShift, 26);// 10,25
                    lblCmdUpperLight.setLocation(1 + cmdShift, 37);// 42
                } else {
                    lblName.setLocation(10, 0);
                    lblCmdSoul.setLocation(18 + cmdShift, 15);
                    cmdCellPane.setSize(48 + cmdShift, 50);
                    shift_x = 32 - cmdShift;
                    shift_y = -35;
                    lblCmdLowerLight.setLocation(12 + cmdShift, 22);// 26
                    lblCmdUpperLight.setLocation(1 + cmdShift, 33);// 42
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    lblCmdStand.setLocation(23 + cmdShift, 23);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 4: ...">
            case 4:
                if (MOUNT) {
                    cmdCellPane.setSize(71 + cmdShift, 47);
                    shift_x = 54;
                    shift_y = -4;
                } else {
                    cmdCellPane.setSize(67 + cmdShift, 43);
                    shift_x = 58;
                    shift_y = -4;
                }

                if (MOUNT) {
                    lblName.setLocation(33, 0);
                } else {
                    lblName.setLocation(29, 0);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_3"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(22, 9);
                } else {
                    lblCmdSoul.setLocation(18, 9);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(23, 17);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(11, 20);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(11, 16);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(0, 31);// 42
                } else {
                    lblCmdUpperLight.setLocation(0, 27);// 42
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 14: ...">
            case 14:
                if (MOUNT) {
                    cmdCellPane.setSize(52 + cmdShift, 54);
                    shift_x = -9;
                    shift_y = 54;
                } else {
                    cmdCellPane.setSize(48 + cmdShift, 50);
                    shift_x = -8;
                    shift_y = 58;
                }

                if (MOUNT) {
                    lblName.setLocation(0, 38);
                } else {
                    lblName.setLocation(0, 34);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(14, 22);
                } else {
                    lblCmdSoul.setLocation(14, 18);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(21, 24);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(24, 12);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(21, 11);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(35, 1);// 42
                } else {
                    lblCmdUpperLight.setLocation(32, 0);// 42
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 16: ...">
            case 16:
                if (MOUNT) {
                    cmdCellPane.setSize(74 + cmdShift, 48);
                    shift_x = -54 - cmdShift;
                    shift_y = 29;
                } else {
                    cmdCellPane.setSize(67 + cmdShift, 43);
                    shift_x = -53 - cmdShift;
                    shift_y = 33;
                }

                if (MOUNT) {
                    lblName.setLocation(0, 31);
                } else {
                    lblName.setLocation(0, 27);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(33 + cmdShift, 23);
                } else {
                    lblCmdSoul.setLocation(33 + cmdShift, 18);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(41 + cmdShift, 25);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(44 + cmdShift, 12);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(40 + cmdShift, 11);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(55 + cmdShift, 1);// 42
                } else {
                    lblCmdUpperLight.setLocation(51 + cmdShift, 0);// 42
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 8: ...">
            case 8:
                if (MOUNT) {
                    lblName.setLocation(33, 31);
                    lblCmdSoul.setLocation(22, 22);
                    cmdCellPane.setSize(71 + cmdShift, 47);
                    shift_x = 55;
                    shift_y = 29;
                    lblCmdLowerLight.setLocation(11, 11);// 11,20
                    lblCmdUpperLight.setLocation(0, 0);// 42
                } else {
                    lblName.setLocation(29, 27);
                    lblCmdSoul.setLocation(18, 18);
                    cmdCellPane.setSize(67 + cmdShift, 43);
                    shift_x = 59;
                    shift_y = 33;
                    lblCmdLowerLight.setLocation(11, 11);// 26
                    lblCmdUpperLight.setLocation(0, 0);// 42
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_9" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    lblCmdStand.setLocation(23, 23);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 10: ...">
            case 10:
                if (MOUNT) {
                    cmdCellPane.setSize(51 + cmdShift, 54);
                    shift_x = 29 - cmdShift;
                    shift_y = 55;
                } else {
                    cmdCellPane.setSize(48 + cmdShift, 50);
                    shift_x = 33 - cmdShift;
                    shift_y = 59;
                }

                if (MOUNT) {
                    lblName.setLocation(13, 38);
                } else {
                    lblName.setLocation(10, 34);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_9" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(22 + cmdShift, 22);
                } else {
                    lblCmdSoul.setLocation(18 + cmdShift, 18);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    lblCmdStand.setLocation(23 + cmdShift, 23);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(11 + cmdShift, 11);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(11 + cmdShift, 11);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(cmdShift, 0);// 42
                } else {
                    lblCmdUpperLight.setLocation(cmdShift, 0);// 42
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 20: ...">
            case 20:
                if (MOUNT) {
                    lblCmdSoul.setLocation(33 + cmdShift, 10);
                    cmdCellPane.setSize(71 + cmdShift, 47);
                    shift_x = -54 - cmdShift;
                    shift_y = -4;
                    lblCmdLowerLight.setLocation(44 + cmdShift, 20);// 11,20
                    lblCmdUpperLight.setLocation(55 + cmdShift, 31);// 42
                } else {
                    lblCmdSoul.setLocation(34 + cmdShift, 8);
                    cmdCellPane.setSize(70 + cmdShift, 43);
                    shift_x = -54 - cmdShift;
                    shift_y = -2;
                    lblCmdLowerLight.setLocation(41 + cmdShift, 15);// 26
                    lblCmdUpperLight.setLocation(52 + cmdShift, 26);// 42
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_9" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    lblCmdStand.setLocation(41 + cmdShift, 17);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 22: ...">
            case 22:
                if (MOUNT) {
                    cmdCellPane.setSize(51 + cmdShift, 54);
                    shift_x = -7;
                    shift_y = -35;
                } else {
                    cmdCellPane.setSize(48 + cmdShift, 50);
                    shift_x = -7;
                    shift_y = -35;
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_9" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(13, 16);
                } else {
                    lblCmdSoul.setLocation(13, 16);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    lblCmdStand.setLocation(21, 23);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(24, 26);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(20, 22);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(35, 36);// 42
                } else {
                    lblCmdUpperLight.setLocation(30, 32);// 42
                }
                break;
            // </editor-fold>
        }
        break;
            case 56:
                //==============================================================
                
                //==============================================================
                break;
            case 36:
                //==============================================================
                        switch (ORIENTATION) {//zoom
            // <editor-fold defaultstate="collapsed" desc="case 1: ...">
            case 1:
                shift_x = 46;
                shift_y = -18;
                if (MOUNT) {
                    cmdCellPane.setSize(38 + cmdShift, 58);
                    lblCmdLowerLight.setLocation(0, 26);// 26
                    lblCmdUpperLight.setLocation(0, 42);// 42
                } else {
                    cmdCellPane.setSize(38 + cmdShift, 54);
                    lblCmdLowerLight.setLocation(0, 22);
                    lblCmdUpperLight.setLocation(0, 38);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 4);
                lblCmdSoul.setLocation(0, 18);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(16, 4);
                    lblCmdStand.setLocation(0, 22);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 23: ...">
            case 23://светофор смотрит вниз
                correct_y = -36;
                shift_x = -12 - cmdShift;
                shift_y = -18;
                if (MOUNT) {
                    cmdCellPane.setSize(38 + cmdShift, 58);
                    lblCmdLowerLight.setLocation(22 + cmdShift, 26);// 26
                    lblCmdUpperLight.setLocation(22 + cmdShift, 42);// 42
                } else {
                    cmdCellPane.setSize(38 + cmdShift, 54);
                    lblCmdLowerLight.setLocation(22 + cmdShift, 22);
                    lblCmdUpperLight.setLocation(22 + cmdShift, 38);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 4);
                lblCmdSoul.setLocation(22 + cmdShift, 18);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(16, 4);
                    lblCmdStand.setLocation(22 + cmdShift, 22);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 11: ...">
            case 11:
                if (MOUNT) {
                    lblName.setLocation(0, 42);
                    cmdCellPane.setSize(38 + cmdShift, 58);
                    lblCmdSoul.setLocation(0, 36);
                } else {
                    lblName.setLocation(0, 38);
                    cmdCellPane.setSize(38 + cmdShift, 54);
                    lblCmdSoul.setLocation(0, 32);
                }
                shift_x = 20;
                shift_y = -1;

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 4);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(16, 4);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(0, 32);
                }

                lblCmdLowerLight.setLocation(0, 16);// 26
                lblCmdUpperLight.setLocation(0, 0);// 42
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 13: ...">
            case 13:
                if (MOUNT) {
                    lblName.setLocation(0, 42);
                    lblCmdSoul.setLocation(22 + cmdShift, 36);
                    cmdCellPane.setSize(38 + cmdShift, 58);
                } else {
                    lblName.setLocation(0, 38);
                    lblCmdSoul.setLocation(22 + cmdShift, 32);
                    cmdCellPane.setSize(38 + cmdShift, 54);
                }
                shift_x = -12 - cmdShift;
                shift_y = 36;

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 4);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_vert" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(16, 4);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(22 + cmdShift, 32);
                }

                lblCmdLowerLight.setLocation(22 + cmdShift, 16);// 26
                lblCmdUpperLight.setLocation(22 + cmdShift, 0);// 42
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 17: ...">
            case 17:
                if (MOUNT) {
                    cmdCellPane.setSize(80 + cmdShift, 16);
                    lblCmdLowerLight.setLocation(48 + cmdShift, 0);//
                    lblCmdUpperLight.setLocation(64 + cmdShift, 0);//
                } else {
                    cmdCellPane.setSize(76 + cmdShift, 16);
                    lblCmdLowerLight.setLocation(44 + cmdShift, 0);//
                    lblCmdUpperLight.setLocation(60 + cmdShift, 0);//

                }
                shift_x = -42 - cmdShift;//40
                shift_y = 25;//46

//                lblCmdLightsName.setLocation(0, 0);
                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(4, 16);

                lblCmdSoul.setLocation(40 + cmdShift, 0);//40

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(4, 16);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(44 + cmdShift, 0);//44
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 19: ...">
            case 19:
                if (MOUNT) {
                    cmdCellPane.setSize(80 + cmdShift, 16);
                    lblCmdLowerLight.setLocation(48 + cmdShift, 0);// 26
                    lblCmdUpperLight.setLocation(64 + cmdShift, 0);// 42
                } else {
                    cmdCellPane.setSize(76 + cmdShift, 16);
                    lblCmdLowerLight.setLocation(44 + cmdShift, 0);// 26
                    lblCmdUpperLight.setLocation(60 + cmdShift, 0);// 42
                }
                shift_x = -40 - cmdShift;
                shift_y = 10;

//                lblCmdLightsName.setLocation(0, 0);
                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(4, 16);

                lblCmdSoul.setLocation(40 + cmdShift, 0);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(4, 16);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(44 + cmdShift, 0);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 5: ...">
            case 5:
                if (MOUNT) {
                    lblName.setLocation(42, 0);//42
                    lblCmdSoul.setLocation(36, 0);//36
                    cmdCellPane.setSize(80 + cmdShift, 16);
                    shift_x = -2;//32
                    shift_y = -5;//10
                } else {
                    lblName.setLocation(38, 0);
                    lblCmdSoul.setLocation(32, 0);
                    cmdCellPane.setSize(76 + cmdShift, 16);
                    shift_x = 2;//36
                    shift_y = -5;//10
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(4, 16);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(4, 16);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(32, 0);//32
                }

                lblCmdLowerLight.setLocation(16, 0);//
                lblCmdUpperLight.setLocation(0, 0);//
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 7: ...">
            case 7:
                if (MOUNT) {
                    lblName.setLocation(42, 0);
                    lblCmdSoul.setLocation(36, 0);
                    cmdCellPane.setSize(80 + cmdShift, 16);
                    shift_x = 32;
                    shift_y = 46;
                } else {
                    lblName.setLocation(38, 0);
                    lblCmdSoul.setLocation(32, 0);
                    cmdCellPane.setSize(76 + cmdShift, 16);
                    shift_x = 36;
                    shift_y = 46;
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(4, 16);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(4, 16);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(32, 0);
                }

                lblCmdLowerLight.setLocation(16, 0);// 26
                lblCmdUpperLight.setLocation(0, 0);// 42
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 2: ...">
            case 2:
                correct_y = -38;//38
                if (MOUNT) {
                    lblName.setLocation(13, 0);
                    lblCmdSoul.setLocation(22 + cmdShift, 16);
                    cmdCellPane.setSize(52 + cmdShift, 54);
                    shift_x = -4 - cmdShift;
                    shift_y = -32;
                    lblCmdLowerLight.setLocation(12 + cmdShift, 26);// 10,25
                    lblCmdUpperLight.setLocation(1 + cmdShift, 37);// 42
                } else {
                    lblName.setLocation(10, 0);
                    lblCmdSoul.setLocation(18 + cmdShift, 15);
                    cmdCellPane.setSize(48 + cmdShift, 50);
                    shift_x = -4 - cmdShift;
                    shift_y = -32;
                    lblCmdLowerLight.setLocation(12 + cmdShift, 22);// 26
                    lblCmdUpperLight.setLocation(1 + cmdShift, 33);// 42
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    lblCmdStand.setLocation(23 + cmdShift, 23);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 4: ...">
            case 4:
                if (MOUNT) {
                    cmdCellPane.setSize(71 + cmdShift, 47);
                    shift_x = 54;
                    shift_y = -4;
                } else {
                    cmdCellPane.setSize(67 + cmdShift, 43);
                    shift_x = 58;
                    shift_y = -4;
                }

                if (MOUNT) {
                    lblName.setLocation(33, 0);
                } else {
                    lblName.setLocation(29, 0);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_3"
                        + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(22, 9);
                } else {
                    lblCmdSoul.setLocation(18, 9);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(23, 17);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(11, 20);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(11, 16);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(0, 31);// 42
                } else {
                    lblCmdUpperLight.setLocation(0, 27);// 42
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 14: ...">
            case 14:
                if (MOUNT) {
                    cmdCellPane.setSize(52 + cmdShift, 54);
                    shift_x = -18;//-9
                    shift_y = 16;//54
                } else {
                    cmdCellPane.setSize(48 + cmdShift, 50);
                    shift_x = -14;//-8
                    shift_y = 17;//58
                }

                if (MOUNT) {
                    lblName.setLocation(0, 38);
                } else {
                    lblName.setLocation(0, 34);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(14, 22);
                } else {
                    lblCmdSoul.setLocation(14, 18);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(21, 24);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(24, 12);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(21, 11);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(35, 1);// 42
                } else {
                    lblCmdUpperLight.setLocation(32, 0);// 42
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 16: ...">
            case 16:
                if (MOUNT) {
                    cmdCellPane.setSize(74 + cmdShift, 48);
                    shift_x = -54 - cmdShift;
                    shift_y = 29;
                } else {
                    cmdCellPane.setSize(67 + cmdShift, 43);
                    shift_x = -53 - cmdShift;
                    shift_y = 33;
                }

                if (MOUNT) {
                    lblName.setLocation(0, 31);
                } else {
                    lblName.setLocation(0, 27);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(33 + cmdShift, 23);
                } else {
                    lblCmdSoul.setLocation(33 + cmdShift, 18);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
//                    cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
                    lblCmdStand.setLocation(41 + cmdShift, 25);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(44 + cmdShift, 12);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(40 + cmdShift, 11);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(55 + cmdShift, 1);// 42
                } else {
                    lblCmdUpperLight.setLocation(51 + cmdShift, 0);// 42
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 8: ...">
            case 8:
                if (MOUNT) {
                    lblName.setLocation(33, 31);
                    lblCmdSoul.setLocation(22, 22);
                    cmdCellPane.setSize(71 + cmdShift, 47);
                    shift_x = 14;//55
                    shift_y = 0;//29
                    lblCmdLowerLight.setLocation(11, 11);// 11,20
                    lblCmdUpperLight.setLocation(0, 0);// 42
                } else {
                    lblName.setLocation(29, 27);
                    lblCmdSoul.setLocation(18, 18);
                    cmdCellPane.setSize(67 + cmdShift, 43);
                    shift_x = 18;//59
                    shift_y = 6;//33
                    lblCmdLowerLight.setLocation(11, 11);// 26
                    lblCmdUpperLight.setLocation(0, 0);// 42
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_9" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    lblCmdStand.setLocation(23, 23);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 10: ...">
            case 10:
                if (MOUNT) {
                    cmdCellPane.setSize(51 + cmdShift, 54);
                    shift_x = 29 - cmdShift;
                    shift_y = 55;
                } else {
                    cmdCellPane.setSize(48 + cmdShift, 50);
                    shift_x = 33 - cmdShift;
                    shift_y = 59;
                }

                if (MOUNT) {
                    lblName.setLocation(13, 38);
                } else {
                    lblName.setLocation(10, 34);
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_9" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(22 + cmdShift, 22);
                } else {
                    lblCmdSoul.setLocation(18 + cmdShift, 18);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    lblCmdStand.setLocation(23 + cmdShift, 23);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(11 + cmdShift, 11);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(11 + cmdShift, 11);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(cmdShift, 0);// 42
                } else {
                    lblCmdUpperLight.setLocation(cmdShift, 0);// 42
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 20: ...">
            case 20:
                if (MOUNT) {
                    lblCmdSoul.setLocation(33 + cmdShift, 10);
                    cmdCellPane.setSize(71 + cmdShift, 47);
                    shift_x = -54 - cmdShift;
                    shift_y = -4;
                    lblCmdLowerLight.setLocation(44 + cmdShift, 20);// 11,20
                    lblCmdUpperLight.setLocation(55 + cmdShift, 31);// 42
                } else {
                    lblCmdSoul.setLocation(34 + cmdShift, 8);
                    cmdCellPane.setSize(70 + cmdShift, 43);
                    shift_x = -54 - cmdShift;
                    shift_y = -2;
                    lblCmdLowerLight.setLocation(41 + cmdShift, 15);// 26
                    lblCmdUpperLight.setLocation(52 + cmdShift, 26);// 42
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_9" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    lblCmdStand.setLocation(41 + cmdShift, 17);
                }
                break;
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="case 22: ...">
            case 22:
                if (MOUNT) {
                    cmdCellPane.setSize(51 + cmdShift, 54);
                    shift_x = -7;
                    shift_y = -35;
                } else {
                    cmdCellPane.setSize(48 + cmdShift, 50);
                    shift_x = -7;
                    shift_y = -35;
                }

                lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_9" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                lblCmdSoul.setSize(16, 16);

                if (MOUNT) {
                    lblCmdSoul.setLocation(13, 16);
                } else {
                    lblCmdSoul.setLocation(13, 16);
                }

                if (MOUNT) {
                    lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_3" + ((VIDEO_STATUS != 1) ? "" : "_future")));
                    lblCmdStand.setSize(7, 7);
                    lblCmdStand.setLocation(21, 23);
                }

                if (MOUNT) {
                    lblCmdLowerLight.setLocation(24, 26);// 11,20
                } else {
                    lblCmdLowerLight.setLocation(20, 22);// 26
                }

                if (MOUNT) {
                    lblCmdUpperLight.setLocation(35, 36);// 42
                } else {
                    lblCmdUpperLight.setLocation(30, 32);// 42
                }
                break;
            // </editor-fold>
        }
                //==============================================================
                break;
        }

// victor - Если в базе данных координата написана с ошибкой то всё плохо...
        if (!warningLights) {
            if (VIDEO_STATUS == 0) {
            cmdLightsTimer = new Timer(GX, GY,
                    shift_x + SHIFT_X + lblName.getX(),
                    shift_y + SHIFT_Y + lblName.getY() + lblName.getHeight() + correct_y);
            }
        }

        lblCmdLowerBlock.setBounds(lblCmdLowerLight.getBounds());
        lblCmdLowerBlock.setIcon(blockLight);
        lblCmdLowerBlock.setVisible(false);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdLowerBlock), LIGHTS_BLOCK_LR);

        lblCmdUpperBlock.setBounds(lblCmdUpperLight.getBounds());
        lblCmdUpperBlock.setIcon(blockLight);
        lblCmdUpperBlock.setVisible(false);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdUpperBlock), LIGHTS_BLOCK_LR);

        lblCmdLowerFault.setBounds(lblCmdLowerLight.getBounds());
        lblCmdLowerFault.setIcon(faultLight);
        lblCmdLowerFault.setVisible(false);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdLowerFault), LIGHTS_BLOCK_LR);

        lblCmdUpperFault.setBounds(lblCmdUpperLight.getBounds());
        lblCmdUpperFault.setIcon(faultLight);
        lblCmdUpperFault.setVisible(false);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdUpperFault), LIGHTS_BLOCK_LR);

        lblCmdSoul.setOpaque(false);
        lblCmdLowerLight.setOpaque(false);
        lblCmdUpperLight.setOpaque(false);

        if (!trainLights) {
            lblCmdUpperLight.setEnabled(false);
            lblCmdUpperLight.setVisible(false);
            lblCmdUpperBlock.setEnabled(false);
            lblCmdUpperBlock.setVisible(false);
        }

        if (CONTROL == 2) {//        if (CONTROL_TYPE.equals("ONLY_BUTTON")) { (0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
            lblCmdUpperLight.setVisible(false);
            lblCmdLowerLight.setVisible(false);

            lblCmdUpperBlock.setVisible(false);
            lblCmdLowerBlock.setVisible(false);

            lblCmdSoul.setVisible(false);
            lblCmdStand.setVisible(false);
        }
        // ----------------------------Set Default State------------------------
        if (redProhibit) {
            lblCmdLowerLight.setIcon(faultRedLight);
        } else {
            lblCmdLowerLight.setIcon(faultBlueLight);
        }
        if (warningLights) {
            lblCmdLowerLight.setIcon(yellowLight);
        }
        if (repeat_light) {
            lblCmdLowerLight.setIcon(blackLight);
        }
        // ---------------------------------------------------------------------
        if (VIDEO_STATUS == 1) {// 1 - "FUTURE"
            lblCmdLowerLight.setIcon(futureLight);
            lblCmdUpperLight.setIcon(futureLight);
        }
        setCmdLayers();
        }//enf if invisible
    }

    private void setCmdLayers() {
        cmdX += shift_x;
        cmdY += shift_y;
        cmdCellPane.setLocation(cmdX, cmdY);
        cmdLayers.add(cmdCellPane);
        cmdLayers.setLayer(cmdCellPane, Light.LIGHTS_LAYER);
    }

    void setState(
            long DTIME,
            int LIGHT,
            int BTNSTATE,
            int ROUTESTATE,
            boolean BLOCKSTATE,
            boolean TIMERSTATE,
            int CURTIME,
            boolean BLOCK_ERROR,
            boolean DISABLE,
            boolean KMG
    ) {
        setDtime(DTIME);
        if (vStatus == 0) {
            if (light != LIGHT) {
                light = LIGHT;
                switch (LIGHT) {
                    case 0:// "-"://0
                        break;
                    case 1://STOP Запрещающий огонь
                        if (repeat_light) {
                            lblCmdLowerLight.setIcon(blackLight);
                        } else {
                            lblCmdUpperLight.setIcon(blackLight);
                            if (redProhibit) {
                                lblCmdLowerLight.setIcon(redLight);
                            } else {
                                lblCmdLowerLight.setIcon(blueLight);
                            }
                        }
                        event(259);
                        alarm_off(132);
                        break;
                    case 2:// "WHITE": //2 WHITE белый MESS_LIGHTS_WHITE 263
                        if (repeat_light) {
                            lblCmdLowerLight.setIcon(greenLight);
                            event(251);
                            alarm_off(129);
                        } else {
                            if (zg_light) {
                                lblCmdLowerLight.setIcon(blackLight);
                                alarm_off(132);
                            } else {
                                if (trainLights) {
                                    lblCmdUpperLight.setIcon(whiteLight);
                                    lblCmdLowerLight.setIcon(blackLight);
                                } else {
                                    lblCmdLowerLight.setIcon(whiteLight);
                                }
                                event(263);
                                alarm_off(133);
                            }
                        }
                        break;
                    case 4:// "GREEN": //4 GREEN зеленый MESS_LIGHTS_GREEN 251
                        if (warningLights){
                            lblCmdLowerLight.setIcon(greenLight);
                        } else {
                            lblCmdUpperLight.setIcon(greenLight);
                            lblCmdLowerLight.setIcon(blackLight);
                        }
                        event(251);
                        alarm_off(129);
                        break;
                    case 8:// "YELLOW": //8 YELLOW желтый MESS_LIGHTS_YELLOW 266
                        if (warningLights){
                            lblCmdLowerLight.setIcon(yellowLight);
                        } else {
                            lblCmdUpperLight.setIcon(yellowLight);
                            lblCmdLowerLight.setIcon(blackLight);
                        }
                        event(266);
                        alarm_off(134);
                        break;
                    case 16://DOUBLE_YELLOW два желтых
                        lblCmdUpperLight.setIcon(yellowLight);
                        lblCmdLowerLight.setIcon(yellowLight);
                        event(247);
                        // нет аларма?
                        alarm_off(134);
                        alarm_off(131);
                        break;
                    case 32://DOUBLE_YELLOW_WITH_BLINK два желтых мигающих
                        lblCmdUpperLight.setIcon(yellowBlinkLight);
                        lblCmdLowerLight.setIcon(yellowLight);
                        event(248);
                        // нет аларма? alarm_off(134);or alarm_off(131);
                        break;
                    case 64://WHITE_BLINK белый мигающий
                        lblCmdUpperLight.setIcon(whiteBlinkLight);
                        lblCmdLowerLight.setIcon(redLight);
                        event(264);
                        alarm_off(133);
                        break;
                    case 65://STOP_WHITE_BLINK Запрещающий и белый мигающий
                        if (warningLights) {
                            lblCmdLowerLight.setIcon(yellowBlinkLight);
//                            lblCmdUpperLight.setIcon(yellowBlinkLight);
//                            lblCmdLowerLight.setIcon(blackLight);
                            event(401);
                        } else {
                            lblCmdUpperLight.setIcon(whiteBlinkLight);
                            lblCmdLowerLight.setIcon(redLight);
                            event(264);
                        }
                        // нет аларма? alarm_off(133); or alarm_off(132);
                        break;
                    case 128://DOUBLE_WHITE два белых
                        lblCmdUpperLight.setIcon(whiteLight);
                        lblCmdLowerLight.setIcon(whiteLight);
                        event(246);
                        // нет аларма?
                        break;
                    case 256://STOP_FAULT Перегорание запрещающего огня
                        if (redProhibit) {
                            lblCmdUpperLight.setIcon(blackLight);
                            lblCmdLowerLight.setIcon(faultRedLight);
                        } else {
                            lblCmdUpperLight.setIcon(blackLight);
                            lblCmdLowerLight.setIcon(faultBlueLight);
                        }
                        event(260);
                        alarm_on(132);
                        break;
                    case 512://WHITE_FAULT Перегорание лампы белого огня
                        if (repeat_light) {
                            lblCmdLowerLight.setIcon(faultGreenLight);
                            event(252);
                            alarm_on(129);
                        } else {
                            if (zg_light) {
                                lblCmdLowerLight.setIcon(faultRedLight);
                                event(260);
                                alarm_on(132);
                            } else {
                                if (trainLights) {
                                    lblCmdUpperLight.setIcon(faultWhiteLight);
                                    lblCmdLowerLight.setIcon(blackLight);
                                } else {
                                    lblCmdLowerLight.setIcon(faultWhiteLight);

                                }
                                event(265);
                                alarm_on(133);
                            } //end if zg
                        }//end if repeat
                        break;
                    case 1_024://GREEN_FAULT Перегорание лампы зеленого огня
                        if (warningLights){
                            lblCmdLowerLight.setIcon(faultGreenLight);
                        } else {
                            lblCmdUpperLight.setIcon(faultGreenLight);
                            lblCmdLowerLight.setIcon(blackLight);
                        }
                        event(252);
                        alarm_on(129);
                        break;
                    case 2_048://YELLOW_FAULT Перегорание лампы желтого огня
                        if (warningLights){
                            lblCmdLowerLight.setIcon(faultYellowLight);
                        } else {
                            lblCmdUpperLight.setIcon(faultYellowLight);
                            lblCmdLowerLight.setIcon(blackLight);
                        }
                        event(267);
                        alarm_on(134);
                        break;
                    case 4_096://SECOND_YELLOW_FAULT Перег лампа второго желтого
                        lblCmdUpperLight.setIcon(blackLight);
                        lblCmdLowerLight.setIcon(faultYellowLight);
                        event(258);
                        alarm_on(131);
                        break;
                    case 6_144://DOUBLE_YELLOW_FAULT Перегорание двух желтых
                        event(249);
                        // alarm_on(134); or alarm_on(131);
                        break;
                    case 8_192://SECOND_WHITE_FAULT перегор. верх. белого
                        lblCmdLowerLight.setIcon(faultWhiteLight);
                        event(257);
                        alarm_on(130);
                        break;
                    default:
                        System.out.println("LIGHT light not found");
                        break;
                }
            }
            /*------------------------------------------------------------------
             0	-
             1	ROUTE_BEGIN	Начало маршрута	MESS_LIGHTS_ROUTE_BEGIN 254
             2	ROUTE_END	Конец маршрута	MESS_LIGHTS_ROUTE_END 256
             4	ROUTE_CANCEL	Отмена маршрута	MESS_LIGHTS_ROUTE_CANCEL 255
             */
            if (route != ROUTESTATE) {
                route = ROUTESTATE;
                switch (ROUTESTATE) {
                    case 0:// "-": //0
                        routed = false;
                        canceling = false;
                        if (BTNSTATE != 1) {// 1 PRESS_BTN
                            lblName.setBorder(idleBtnBorder);
                            lblName.setBackground(idleBtnBackground);
                        }
                        break;
                    case 1:// "ROUTE_BEGIN": //1 ROUTE_BEGIN Начало маршрута
                        // MESS_LIGHTS_ROUTE_BEGIN 254
                        routed = true;
                        canceling = false;
                        event(254);
                        lblName.setBackground(routeBtnBackground); // green
                        break;
                    case 2:// "ROUTE_END": //2 ROUTE_END Конец маршрута
                        // MESS_LIGHTS_ROUTE_END 256
                        routed = true;
                        canceling = false;
                        lblName.setBackground(routeBtnBackground); // green
                        event(256);
                        break;
                    case 4:// "ROUTE_CANCEL": //4 ROUTE_CANCEL Отмена маршрута
                        // MESS_LIGHTS_ROUTE_CANCEL 255
                        lblName.setBorder(cancelBtnBorder); //
                        routed = false;
                        canceling = true;
                        event(255);
                        break;
                    default:
                        routed = false;
                        canceling = false;
                }
            }
            //------------------------------------------------------------------
            if (btnstate != BTNSTATE) {
                btnstate = BTNSTATE;
                if (control != 1) {//if (CONTROL_TYPE==ONLY_LIGHTS) {(0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
                    // btnState = currBtnState;
                    switch (BTNSTATE) {// int
                        case 1://PRESS_BTN
                            lblName.setBackground(pressBtnBackground); // green
                            break;
                        case 0://RELEASE_BTN
                            if (!routed) {
                                lblName.setBackground(idleBtnBackground); // default
                                lblName.setBorder(idleBtnBorder);
                            }
                            break;
                        case 2://WRONG_BTN Неправельный светофор
                            lblName.setBackground(wrongBtnBackground);
                            lblName.setBorder(wrongBtnBorder);
                            event(250);
                            break;
                        default:
                            lblName.setBackground(idleBtnBackground);
                            break;
                    }
                }
            }
            // -----------------------------------------------------------------
            if (control != 2) {//if (CONTROL_TYPE=="ONLY_BUTTON") {(0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
                if (block != BLOCKSTATE) {
                    block = BLOCKSTATE;
                    if (BLOCKSTATE) {// true 1 BLOCK Блокирован
                        if (trainLights) {
                            lblCmdUpperBlock.setVisible(true);
                        } else {
                            lblCmdLowerBlock.setVisible(true);
                        }
                        event(244);
                    } else {//UNBLOCK Разблокирован
                        if (trainLights) {
                            lblCmdUpperBlock.setVisible(false);
                        } else {
                            lblCmdLowerBlock.setVisible(false);
                        }
                        event(262);
                    }
                }
            }
            // ----------------------------------------------------------------
            if (block_error != BLOCK_ERROR) {
                block_error = BLOCK_ERROR;
                if (block_error) {//BLOCK_ERROR Ошибка блокировки
                    event(245);
                }
                // else {//false
                // 0 BLOCK_NO_ERROR
                // }
            }
            // ----------------------------------------------------------------
            if (disabled) {//если запрещаемый
                if (disable != DISABLE) {
                    disable = DISABLE;
                    DissableLight(!disable);
                }
            }
            // ----------------------------------------------------------------
            if (kmg != KMG) {
                kmg = KMG;
                if (KMG) {// true 1 KMG Ошибка блокировки ALARM_LIGHTS_KMG 347
                    alarm_on(347);
                } else {//false
                    alarm_off(347);
                }
            }
            // ----------------------------------------------------------------
            if (control != 1) {//if (CONTROL_TYPE.equals("ONLY_LIGHTS")) { (0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
                // if (timerOn != TIMERSTATE) {
                if (!warningLights) {
                    if (TIMERSTATE) {// true 1 TIMER_ON
                        if ((CURTIME) != 0) {
                            cmdLightsTimer.setState(CURTIME, canceling);
                        } else { // false 0 TIMER_OFF
                            cmdLightsTimer.setState(0, canceling);
                        }
                    } else { // false 0 TIMER_OFF
                        cmdLightsTimer.setState(0, canceling);//cmdLightsTimer = null - Exception in thread "Thread-1" java.lang.NullPointerException and Ticket3 crach
                    }
                }
            }
        }// end if
    }

    // =========================================================================
    private void DissableLight(boolean d) {
        lblCmdLowerLight.setEnabled(d);
        lblCmdUpperLight.setEnabled(d);
        lblName.setEnabled(d);
        lblCmdLowerBlock.setEnabled(d);
        lblCmdUpperBlock.setEnabled(d);
        lblCmdLowerFault.setEnabled(d);
        lblCmdUpperFault.setEnabled(d);
    }

    private boolean ltBlockItemCanBeEnabled() {
        return (!block && (light == 1 || light == 256));// STOP or STOP_FAULT
    }

    private boolean ltInviteOnItemCanBeEnabled() {
        return light != 65;// STOP_WHITE_BLINK
    }

    private boolean ltInviteRepeateItemCanBeEnabled() {
        return light == 65;// STOP_WHITE_BLINK
    }

    private boolean ltInviteOffItemCanBeEnabled() {
        return light == 65;// STOP_WHITE_BLINK
    }

    private boolean ltRecoverItemCanBeEnabled() {
        return light == 1 || light == 256;// STOP or STOP_FAULT
    }

    // -------------------------------------------------------------------------
    // подготовка меню и кнопочных команд
    private void setLtPopup() {

            Popup.setBorder(javax.swing.BorderFactory.createTitledBorder(menu_title));
            Popup.setBorderPainted(true);

            Popup.add(ltRecoverItem);

            ltRecoverItem.addActionListener((java.awt.event.ActionEvent e) -> ltRecoverItemActionPerformed());

            Popup.addSeparator();
            Popup.add(ltBlockItem);
            ltBlockItem.addActionListener((java.awt.event.ActionEvent evt) -> ltBlockItemActionPerformed());
            Popup.add(ltUnBlockItem);
            ltUnBlockItem.addActionListener((java.awt.event.ActionEvent evt) -> ltUnBlockItemActionPerformed());

            if (withInviting) {
                Popup.addSeparator();
                Popup.add(ltInviteOnItem);
                ltInviteOnItem.addActionListener((java.awt.event.ActionEvent evt) -> ltInviteOnItemActionPerformed());
                Popup.add(ltInviteRepeateItem);
                ltInviteRepeateItem.addActionListener((java.awt.event.ActionEvent evt) -> ltInviteRepeateItemActionPerformed());
                Popup.add(ltInviteOffItem);
                ltInviteOffItem.addActionListener((java.awt.event.ActionEvent evt) -> ltInviteOffItemActionPerformed());
            }

            if (Util.GetUserGroups("SIM")) {
                Popup.addSeparator();
                ltSimRItem.addActionListener(evt -> ltSimRItemActionPerformed());
                Popup.add(ltSimRItem);
                ltSimZItem.addActionListener(evt -> ltSimZItemActionPerformed());
                Popup.add(ltSimZItem);
            }

            Popup.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                    preparePopup();
                }

                @Override
                public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {
                    // throw new
                    // UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {
                    // throw new
                    // UnsupportedOperationException("Not supported yet.");
                }
            });

            lblName.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mousePressed(java.awt.event.MouseEvent evt) {
                            lblCmdLightNameMousePressed(evt);
                        }

                        @Override
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                            lblCmdLightNameMouseReleased(evt);
                        }
                    });

    }

    private void lblCmdLightNameMousePressed(java.awt.event.MouseEvent evt) {
        if (Status.work && Area.Manager_PC) {
            if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                storedBorder = lblName.getBorder();
                lblName.setBorder(pressedBorder);

////---------------------- нажатие кнопки маневрового маршрута -----------------
Terminal.Area_Hash.values().stream().filter((ar) -> (!ar.withTrainRouting && !ar.withShuntingRouting && ar.groupCancel && ar.route == 0)).forEachOrdered(Area::btnShuntingRouteMousePressed);
                pause(50);
////-----------------------------------------------------------------------
                Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_BTN_PRESSED");
            }
        }
    }

    private void lblCmdLightNameMouseReleased(java.awt.event.MouseEvent evt) {
        if (Status.work && Area.Manager_PC) {
            if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
                pause(500);
                lblName.setBorder(storedBorder);
                Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_BTN_RELEASED");
            }
        }
    }

    private void ltRecoverItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_RECOVER_PHASE1");
        pause(500);
        Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_RECOVER_PHASE2");
    }

    private void ltBlockItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_BLOCK_ON");
    }

    private void ltUnBlockItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_BLOCK_OFF");
    }

    private void ltInviteOnItemActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=4 style=bold> "
                + OPERATION
                + "!!</font><br> Подвердите включение пригласительного огня<br> на светофоре <font color=red size=5 style=bold>"
                + lblName.getText() + "</font></html>")) {
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP(id_obj, "FIRST_LIGHTS_INVITE_ON");
                pause(500);
                Net.sendMaskedCmd_DSP(id_obj, "REPEAT_LIGHTS_INVITE_ON_PHASE1");
                break;
        }
    }

    private void ltInviteRepeateItemActionPerformed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=4 style=bold> "
                + OPERATION
                + "!!</font><br>Подвердите продление действия пригласительного огня<br> на светофоре <font color=red size=5 style=bold>"
                + lblName.getText() + "</font></html>")) {
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP(id_obj, "REPEAT_LIGHTS_INVITE_ON_PHASE2");
                pause(500);
                Net.sendMaskedCmd_DSP(id_obj, "REPEAT_LIGHTS_INVITE_ON_PHASE1");
                break;
        }
    }

    private void ltInviteOffItemActionPerformed() {
        Net.sendMaskedCmd_DSP(id_obj, "LIGHTS_INVITE_OFF_PHASE1");
        pause(500);
        Net.sendMaskedCmd_DSP(id_obj, "REPEAT_LIGHTS_INVITE_OFF");
    }

    private void ltSimRItemActionPerformed() {
        if (ltSimRItem.isSelected()) {
            Net.sendMaskedCmd_SIM(id_obj, "LIGHTS_SIM_R0");
        } else {
            Net.sendMaskedCmd_SIM(id_obj, "LIGHTS_SIM_R1");
        }
    }

    private void ltSimZItemActionPerformed() {
        if (ltSimZItem.isSelected()) {
//            if ((byteNumberZ != -1) | (bitNumberZ != -1)) {
//                Net.sendDirectCmd_SIM(12, String.valueOf(byteNumberZ) + "." + String.valueOf(bitNumberZ) + ".0");//SIEMENS //включить
//            }
            Net.sendMaskedCmd_SIM(id_obj, "LIGHTS_SIM_Z0");
        } else //if ((byteNumberZ != -1) | (bitNumberZ != -1))
        {
//            Net.sendDirectCmd_SIM(12, String.valueOf(byteNumberZ) + "." + String.valueOf(bitNumberZ) + ".1");//SIEMENS //выключить
            Net.sendMaskedCmd_SIM(id_obj, "LIGHTS_SIM_Z1");
        }
    }

    private void preparePopup() {// подготовка опций перед включением
        ltBlockItem.setEnabled(ltBlockItemCanBeEnabled() && Status.work && Area.Manager_PC);
        ltUnBlockItem.setEnabled(block && Status.work && Area.Manager_PC);
        if (withInviting) {
            ltInviteOnItem.setEnabled(ltInviteOnItemCanBeEnabled() && Status.work && Area.Manager_PC);
            ltInviteRepeateItem.setEnabled(ltInviteRepeateItemCanBeEnabled() && Status.work && Area.Manager_PC);
            ltInviteOffItem.setEnabled(ltInviteOffItemCanBeEnabled() && Status.work && Area.Manager_PC);
        }
        ltRecoverItem.setEnabled(ltRecoverItemCanBeEnabled() && Status.work && Area.Manager_PC);

        if (Util.GetUserGroups("SIM")) {
            ltSimRItem.setEnabled(Status.work && Area.Manager_PC);
            ltSimZItem.setEnabled(Status.work && Area.Manager_PC);
        }
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

    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Err.err(e);
        }
    }

    @Override
    public void setState(Object oid) {
        LightsState l = (LightsState) oid;
        this.setState(
                l.timestamp,
                l.LIGHT,
                l.BTNSTATE,
                l.ROUTESTATE,
                l.BLOCKSTATE,
                l.TIMERSTATE,
                l.CURTIME,
                l.BLOCK_ERROR,
                l.DISABLE,
                l.KMG
        );
    }
}

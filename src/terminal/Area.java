//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import ru.attrans.proc.objs.AreaNameState;
import static terminal.Commander.cmdLayers;

class Area extends Cell {//нахуя Cell?

    private static final java.awt.Color cmdAreaNameBackgroundColor = new java.awt.Color(140, 255, 100, 80);
    private static final Border cmdAreaNameBorder2 = BorderFactory.createLineBorder(new Color(150, 180, 210));
    // --Commented out by Inspection (16.01.18 15:00):private static final Border cmdAreaNameBorder = BorderFactory.createLineBorder(new Color(190, 220, 250));
    // иконки
//    private static final javax.swing.ImageIcon oddGreenIcon = Terminal.mainPictureHash.get("odd_green");//убрать все ImageIcon
//    private static final javax.swing.ImageIcon oddYellowIcon = Terminal.mainPictureHash.get("odd_yellow");
//    private static final javax.swing.ImageIcon evenGreenIcon = Terminal.mainPictureHash.get("even_green");
//    private static final javax.swing.ImageIcon evenYellowIcon = Terminal.mainPictureHash.get("even_yellow");
//    private static final javax.swing.ImageIcon idleIcon = Terminal.mainPictureHash.get("idle");
    // метки, индикаторы
    private static final javax.swing.JLabel lblName = new javax.swing.JLabel();
//    private static final javax.swing.JLabel lblCmdHollowCancelCounter = new javax.swing.JLabel("36");
//    private static final javax.swing.JLabel lblCmdRouteArrow = new javax.swing.JLabel();

    private final Img lbl_oddGreenIcon = new Img();
    private final Img lbl_oddYellowIcon = new Img();
    private final Img lbl_evenGreenIcon = new Img();
    private final Img lbl_evenYellowIcon = new Img();
    private final Img lbl_idleIcon = new Img();

    // кнопки
    private static final javax.swing.JButton btnTele = new javax.swing.JButton("<html>режим ARM:<br>Контроль</html>");
    private static final javax.swing.JButton btnGroupCancel = new javax.swing.JButton("<html>Групповая<br>отмена</html>");
    private static final javax.swing.JButton btnDialCancel = new javax.swing.JButton("<html>Отмена<br>набора</html>");
    private static final javax.swing.JButton btnTrainRoute = new javax.swing.JButton("<html><br>ПМ</html>");
    private static final javax.swing.JButton btnShuntingRoute = new javax.swing.JButton("<html><br>ММ</html>");
    private static final javax.swing.JButton btnFirstResetHW = new javax.swing.JButton("<html>Сброс<br>оборудования</html>");
    private static final javax.swing.JButton btnSecondResetHW = new javax.swing.JButton("<html> Меж-парк <br>связь<html>");

    private final javax.swing.JLayeredPane cmdCellPane = new javax.swing.JLayeredPane();

    private final boolean honestOdd; // нечетное направление: TRUE - слева
    // направо, FLASE - справа налево
//    private final String s_name;
    private final boolean withFirstResetHw;
    private final boolean withSecondResetHw;
    private final boolean withBtnTU;//кнопка телеуправления
    private final boolean got; //групповая отмена
    private final boolean sn; //сброс набора
    final boolean withTrainRouting; // кнопка режима задания поездных маршрутов
    final boolean withShuntingRouting; // кнопка режима задания маневровых маршрутов
    private boolean hwAcknLow1;// ?
    private boolean hwAcknLow2;// ?
    private int hollows = 0;// Default state 0
    int route = 0;// Default state "NO_ROUTING"
    int last_route = 0;
    boolean groupCancel = true; // Default state //GROUP_CHANSEL
    // "RELEASE_BTN"
    private boolean dialCancel = false; // Default state; // 4 "DIAL_CANCEL"
    // "RELEASE_BTN"
    private boolean hwAckn_1 = false; // Default state; // 5 "HW_ACKN_1"
    // "RELEASE_BTN"
//    private boolean alarmSound = false; // Default state; // 6 "ALARM_SOUND"
    // "MUTE"
//    private boolean hollowCancel = false; // Default state; // 7 "HOLLOW_CANCEL"
    // "NOT_ALLOWED"
    private boolean boolBtnTU = false; //Def state;//8
    // "HOLLOW_CANCEL_BTN"
    // "RELEASE_BTN"
    private boolean hwAckn_2 = false; //Def state; //9 "HW_ACKN_2" "ALERT_OFF"
    private boolean ackn_2err = false; //Def state; //10 "ACKN_2ERR" "NO_ERR"
    private Color cmdBtnIdleBckColor;
    private final ActionListener AREA1 = (ActionEvent evt) -> {
        hwAcknLow1 = !hwAcknLow1;
        if (hwAcknLow1) {
            btnFirstResetHW.setBackground(new Color(153, 255, 0));
        } else {
            btnFirstResetHW.setBackground(cmdBtnIdleBckColor);
        }
    };
//    private final javax.swing.Timer blinkTimer1 = new javax.swing.Timer(600, blinkPerformer1);
    private final ActionListener AREA2 = (ActionEvent evt) -> {
        hwAcknLow2 = !hwAcknLow2;
        if (hwAcknLow2) {
            btnSecondResetHW.setBackground(new Color(153, 255, 0));
        } else {
            btnSecondResetHW.setBackground(cmdBtnIdleBckColor);
        }
    };
//    private final javax.swing.Timer blinkTimer2 = new javax.swing.Timer(600, blinkPerformer2);
    private static int vst;

    private static int terminal_number = 0;
    static boolean Manager_PC = true;
    static Status status;
//#0=ID_OBJ,1=S_NAME,2=MPC_NAME,3=X,4=Y,5=SHIFT_X,6=SHIFT_Y,7=VIDEO_STATUS,8=PRIMARY_IP,9=SECONDARY_IP,10=FIRST_RESET_HW,11=SECOND_RESET_HW,12=HOLLOW_CANCELING,13=TRAIN_ROUTING,14=SHUNTING_ROUITING,15=HONEST_ODD,16=IND,17=CMD,18=STATE,19=STATE_TIME

    Area(
            int ID_OBJ, //0=ID_OBJ
            String S_NAME, //1=S_NAME
            String MPC_NAME, //2=MPC_NAME
            int X, //3=X
            int Y, //4=Y
            int SHIFT_X, //5=SHIFT_X
            int SHIFT_Y, //6=SHIFT_Y
            int VIDEO_STATUS, //7=VIDEO_STATUS
            boolean FIRSTRESETHW,
            boolean SECONDRESETHW,
            boolean WITHBTNTU,
            boolean WITHTRAINROUTING,
            boolean WITHSHUNTINGROUTING,
            boolean HONESTODD,
            //        int IND,
            //        int CMD,
            boolean GOT, //18 = GOT //групповая отмена
            boolean SN //19 = SN //сброс набора
    ) {
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 1);//1-areaname
        vst = VIDEO_STATUS;
        Terminal.MPC_NAME = MPC_NAME;
//        s_name = S_NAME;
        withBtnTU = WITHBTNTU;
        withTrainRouting = WITHTRAINROUTING;
        withShuntingRouting = WITHSHUNTINGROUTING;
        honestOdd = HONESTODD;
        withFirstResetHw = FIRSTRESETHW;
        withSecondResetHw = SECONDRESETHW;// меж.парковая связь
        got = GOT;
        sn = SN;
        setPanes();
        if (!Terminal.play) {
            status = new Status(GX, GY, SHIFT_X, SHIFT_Y);
        }
        SetDefault(true);
    }

    private void SetDefault(boolean STATUS) {
        boolean work = (STATUS & Terminal.DSP & (vst == 0) & Manager_PC);
        btnShuntingRoute.setEnabled(work);
        btnTrainRoute.setEnabled(work);
//        lblCmdRouteArrow.setEnabled(work);
        btnDialCancel.setEnabled(work);
        btnGroupCancel.setEnabled(work);
        btnTele.setEnabled(STATUS & Terminal.DSP & (vst == 0));
//        lblCmdHollowCancelCounter.setEnabled(work);
        btnFirstResetHW.setEnabled(work);
        btnSecondResetHW.setEnabled(work);
    }

    // <editor-fold defaultstate="collapsed" desc="setPanes">
    private void setPanes() {
        int cmdH = 64;
        // int cmdW = 400 + 36 //346
        int cmdW = 270
                + (withBtnTU ? 64 : 0)
                + (withSecondResetHw ? 84 : 0)
                + (withSecondResetHw ? 46 : 0)
                + (withTrainRouting ? 46 : 0)
                + (withShuntingRouting ? 46 : 0)
                + (withFirstResetHw ? 46 : 0)
                + (honestOdd ? 46 : 0);//слева направо или справа налево - должен быть отдельный бит! рисовать или нет стрелку

        cmdCellPane.setSize(cmdW, cmdH);
        cmdCellPane.setLocation(cmdX, cmdY);
        cmdCellPane.setBackground(cmdAreaNameBackgroundColor);
        cmdCellPane.setBorder(cmdAreaNameBorder2);
        cmdCellPane.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
        setCmdLayers();
        cmdCellPane.setVisible(true);

        lblName.setText(s_name);
        lblName.setPreferredSize(new Dimension(cmdW, lblName.getFontMetrics(Terminal.SANS14).getHeight()));
        lblName.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblName.setVerticalAlignment(javax.swing.JLabel.TOP);
        lblName.setFont(Terminal.SANS14);
        lblName.setVisible(true);
        set_setpane(lblName);
        // -----------------------with first reset hw-------------------------
        if (withFirstResetHw) {
            btnFirstResetHW.setFont(Terminal.SANS11);
            btnFirstResetHW.addActionListener((java.awt.event.ActionEvent e) -> btnResetHardwareMousePressed());
            set_setpane(btnFirstResetHW);
        }
        // ----------------with secind reset hw-------------------------
        if (withSecondResetHw) {
            btnSecondResetHW.setToolTipText("Межпарковая связь");
            btnSecondResetHW.setFont(Terminal.SANS11);
            // btnSecondResetHW.setHorizontalAlignment(JButton.CENTER);
            btnSecondResetHW.setPreferredSize(new java.awt.Dimension(120, 36));// когда
            // ставим рамку кнопке размеры кнопки улетают...
            btnSecondResetHW.addActionListener((java.awt.event.ActionEvent e) -> btnSecondResetHWMousePressed());
            set_setpane(btnSecondResetHW);
        }
        // -------------------------with hollow cancelling----------------------
        if (withBtnTU) {
            Manager_PC = false;
            set_terminal_number();
            btnTele.setFont(Terminal.SANS11);
            btnTele.setSize(60, 26);
            btnTele.setEnabled(true);
            btnTele.setVisible(true);
            btnTele.addActionListener((java.awt.event.ActionEvent e) -> btnTUMousePressed());
            set_setpane(btnTele);
        } else {

        }
        // -----------------------Групповая отмена GOT--------------------------
        if (got) {
            btnGroupCancel.setFont(Terminal.SANS11);
            btnGroupCancel.setHorizontalAlignment(javax.swing.JButton.CENTER);
            btnGroupCancel.addActionListener((java.awt.event.ActionEvent e) -> btnGroupCancelMousePressed());
            set_setpane(btnGroupCancel);
        }
        // -------------------------отмена набора SN----------------------------
        if (sn) {
            btnDialCancel.setFont(Terminal.SANS11);
            btnDialCancel.setHorizontalAlignment(javax.swing.JButton.CENTER);
            btnDialCancel.addActionListener((java.awt.event.ActionEvent e) -> btnDialCancelMousePressed());
            set_setpane(btnDialCancel);
        }
        // ------------------------with train routing---------------------------
        if (withTrainRouting) {
            btnTrainRoute.setFont(Terminal.SANS11);
            btnTrainRoute.setBounds(375, 20, 54, 26);
            btnTrainRoute.setVisible(true);
            btnTrainRoute.setEnabled(true);
            btnTrainRoute.addActionListener((java.awt.event.ActionEvent e) -> btnTrainRouteMousePressed());
            set_setpane(btnTrainRoute);
        }
        // -----------------------with shunting routing-------------------------
        if (withShuntingRouting) {
            btnShuntingRoute.setFont(Terminal.SANS11);
            btnShuntingRoute.setBounds(375, 44, 54, 26);
            btnShuntingRoute.addActionListener((java.awt.event.ActionEvent e) -> btnShuntingRouteMousePressed());
            set_setpane(btnShuntingRoute);
        }
        // ------------------------with honest odd ----------------------
//        if (honestOdd) {
////            lblCmdRouteArrow.setPreferredSize(new Dimension(36, 20));
////            lblCmdRouteArrow.setIcon(idleIcon);
////            lblCmdRouteArrow.setHorizontalAlignment(javax.swing.JLabel.CENTER);
////            lblCmdRouteArrow.setVisible(true);
////            set_setpane(lblCmdRouteArrow);
//        }

        cmdBtnIdleBckColor = btnGroupCancel.getBackground();

        prep_lbl(lbl_oddGreenIcon, "odd_green");//1
        prep_lbl(lbl_oddYellowIcon, "odd_yellow");//1
        prep_lbl(lbl_evenGreenIcon, "even_green");//1
        prep_lbl(lbl_evenYellowIcon, "even_yellow");//1
        prep_lbl(lbl_idleIcon, "idle");//1
        lbl_idleIcon.setVisible(true);

        if (!Terminal.DSP) {//и в статусе чтобы не включалось
            btnShuntingRoute.setEnabled(false);
            btnTrainRoute.setEnabled(false);
//            lblCmdRouteArrow.setEnabled(false);
            btnDialCancel.setEnabled(false);
            btnGroupCancel.setEnabled(false);
            btnTele.setEnabled(false);
//            lblCmdHollowCancelCounter.setEnabled(false);
            btnFirstResetHW.setEnabled(false);
        }
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="prep_lbl">
    private void prep_lbl(Img lbl, String ico) {
        if (Terminal.mainPictureHash.containsKey(ico)) {//если есть такая картинка
            lbl.setIcon(Terminal.mainPictureHash.get(ico));
            int x = lbl.getIcon().getIconWidth();//если нет картинки то - NullPointer
            int y = lbl.getIcon().getIconHeight();
            lbl.setPreferredSize(new Dimension(x, y));
//            lbl.setBounds(0, 0, x, y);
            lbl.setVisible(false);
            lbl.setFocusable(false);
            lbl.setOpaque(false);
            set_setpane(lbl);
        } else {
            Log.log(" in lib picture does not exist : " + ico);
        }
    }

    private void set_setpane(java.awt.Component comp) {
        int c = cmdCellPane.getComponentCount();
        cmdCellPane.add(comp);
        cmdCellPane.setLayer(comp, 2);
        cmdCellPane.setPosition(comp, c + 1);
    }
    // </editor-fold>

    private void setCmdLayers() {
        cmdLayers.add(cmdCellPane);
        cmdLayers.setLayer(cmdCellPane, CELLPANE_LAYER_10);//10
    }

    void SetCmd(boolean STATUS) {
        // запретим или разрешим все кнопки при изменении переменной когда связь
        // пропадает появляется.
        boolean work = (STATUS & Terminal.DSP & (vst == 0) & Manager_PC);
        btnShuntingRoute.setEnabled(work);
        btnTrainRoute.setEnabled(work);
//        lblCmdRouteArrow.setEnabled(work);
        btnDialCancel.setEnabled(work);
        btnGroupCancel.setEnabled(work);
        btnTele.setEnabled(STATUS & Terminal.DSP & (vst == 0));
//        lblCmdHollowCancelCounter.setEnabled(work);
        btnFirstResetHW.setEnabled(work);
        btnSecondResetHW.setEnabled(work);
    }

    private void btnDialCancelMousePressed() {// обработка кнопки отмена набора
        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_SET_CANCEL1");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_SET_CANCEL2");
    }

    private void btnTrainRouteMousePressed() {// обработка кнопки поездного маршрута
        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_ROUTE");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_ROUTE_OFF");
    }

    void btnShuntingRouteMousePressed() {// обработка кнопки маневрового маршрута
        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_M_ROUTE");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_M_ROUTE_OFF");
    }

    private void btnTUMousePressed() {// обработка кнопки режима управления
//        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_RSLS");
//        pause();
//        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_RSLS_OFF");
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=5 style=bold><b> "
                + OPERATION
                + "!!</b></font><br>Подтвердите переключение<br> <b>в режим УПРАВЛЕНИЯ </b></html>")) {
            case 0:
                break;
            case 1:
//                Net.sendDirectCmd_DSP(id_obj, ((terminal_number << 8 & 0xFF00) | (32 & 0xFF)) + "." + ((terminal_number << 8 & 0xFF00) | (32 & 0xFF)));
                Net.sendDirectCmd_DSP(id_obj, "65535." + ((terminal_number << 8 & 0xFF00) | (32 & 0xFF)));
                pause();
//                Net.sendDirectCmd_DSP(id_obj, ((terminal_number << 8 & 0xFF00) | (0 & 0xFF)) + "." + ((terminal_number << 8 & 0xFF00) | (0 & 0xFF)));
                Log.log("Net.sendDirectCmd_DSP " + id_obj + " " + String.valueOf(((terminal_number << 8 & 0xFF00) | (32 & 0xFF))) + "." + String.valueOf(((terminal_number << 8 & 0xFF00) | (32 & 0xFF))));
        }
    }

    private void btnResetHardwareMousePressed() {// обработка кнопки сброса оборудования
        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_ACK");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_ACK_OFF");
    }

    private void btnSecondResetHWMousePressed() {// обработка кнопки межпарковой связи
        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_ACK2");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_ACK2_OFF");
    }

    private void btnGroupCancelMousePressed() {// обработка кнопки групповой отмены
        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_GOT1");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "AREANAME_GOT2");
    }

    void setState(
            long DTIME,
            int HOLLOWS, // 1 "HOLLOWS"
            int ROUTE, // 2 "ROUTE"
            boolean GROUP_CANCEL, // GOT
            boolean DIAL_CANCEL, // 4 "DIAL_CANCEL"
            boolean HW_ACKN_1, // 5 "HW_ACKN_1"
            boolean ALARM_SOUND, // 6 "ALARM_SOUND"
            boolean HOLLOW_CANCEL, // 7 "HOLLOW_CANCEL"
            boolean HOLLOW_CANCEL_BTN, // 8 "HOLLOW_CANCEL_BTN"
            boolean HW_ACKN_2, // 9 "HW_ACKN_2"
            boolean ACKN_2ERR // 10 "ACKN_2ERR"
    ) {
        if (vStatus == 0) {
            setDtime(DTIME);
            if (hollows != HOLLOWS) {
                hollows = HOLLOWS;
//            lblCmdHollowCancelCounter.setText(String.valueOf(hollows));
                System.out.println("HOLLOWS: " + HOLLOWS);
                if (withBtnTU) {
                    setManager(hollows);
                }
            }

            if (route != ROUTE) {
                last_route = route;
                route = ROUTE;
                switch (route) {// int
                    case 0:// "NO_ROUTING"://0
//                    lblCmdRouteArrow.setIcon(idleIcon);
                        btnTrainRoute.setBackground(cmdBtnIdleBckColor);
                        btnShuntingRoute.setBackground(cmdBtnIdleBckColor);
                        break;
                    case 1:// "TRAIN"://1
                        btnTrainRoute.setBackground(Color.GREEN);
//                    lblCmdRouteArrow.setIcon(idleIcon);
                        break;
                    case 5:// "TRAIN_ODD"://5
                        btnTrainRoute.setBackground(Color.GREEN);
//                    lblCmdRouteArrow.setIcon(honestOdd ? oddGreenIcon
//                            : evenGreenIcon);
//                    lblCmdRouteArrow.setVisible(true);
                        break;
                    case 9:// "TRAIN_EVEN"://9
                        btnTrainRoute.setBackground(Color.GREEN);
//                    lblCmdRouteArrow.setIcon(honestOdd ? evenGreenIcon
//                            : oddGreenIcon);
//                    lblCmdRouteArrow.setVisible(true);
                        break;
                    case 2:// "SHUNTING"://2
                        btnShuntingRoute.setBackground(Color.YELLOW);
//                    lblCmdRouteArrow.setIcon(idleIcon);
                        break;
                    case 6:// "SHUNTING_ODD"://6
                        btnShuntingRoute.setBackground(Color.YELLOW);
//                    lblCmdRouteArrow.setIcon(honestOdd ? oddYellowIcon
//                            : evenYellowIcon);
//                    lblCmdRouteArrow.setVisible(true);
                        break;
                    case 10:// "SHUNTING_EVEN"://10
                        btnShuntingRoute.setBackground(Color.YELLOW);
//                    lblCmdRouteArrow.setIcon(honestOdd ? evenYellowIcon
//                            : oddYellowIcon);
//                    lblCmdRouteArrow.setVisible(true);
                        break;
                }//end switch
                if (honestOdd) {
                    lbl_oddGreenIcon.setVisible(route == 5);
                    lbl_oddYellowIcon.setVisible(route == 6);
                    lbl_evenGreenIcon.setVisible(route == 9);
                    lbl_evenYellowIcon.setVisible(route == 10);
                } else {
                    lbl_oddGreenIcon.setVisible(route == 9);
                    lbl_oddYellowIcon.setVisible(route == 10);
                    lbl_evenGreenIcon.setVisible(route == 5);
                    lbl_evenYellowIcon.setVisible(route == 6);
                }
                lbl_idleIcon.setVisible(route == 0 | route == 1 | route == 2);
            }

//            System.out.println("last_route " + last_route);
            //0000
            //0001
            //0011
            //0101
            //0111

            if (groupCancel != GROUP_CANCEL) {
                groupCancel = GROUP_CANCEL;
                if (groupCancel) {// boolean //"PRESS_BTN"://1 true
                    btnGroupCancel.setBackground(cmdBtnIdleBckColor);
                    btnGroupCancel.setForeground(Color.BLACK);
                } else {// "RELEASE_BTN"://0 false
                    btnGroupCancel.setBackground(Color.RED);
                    btnGroupCancel.setForeground(Color.WHITE);
                }
            }

            if (dialCancel != DIAL_CANCEL) {
                dialCancel = DIAL_CANCEL;
                if (DIAL_CANCEL) {// true 1
                    btnDialCancel.setBackground(Color.RED);
                    btnDialCancel.setForeground(Color.WHITE);
                } else {// false "RELEASE_BTN"://0
                    btnDialCancel.setBackground(cmdBtnIdleBckColor);
                    btnDialCancel.setForeground(Color.BLACK);
                }
            }
            if (hwAckn_1 != HW_ACKN_1) {
                hwAckn_1 = HW_ACKN_1;
                if (HW_ACKN_1) {// true "NEED_ACKN"://1 NEED_ACKN Требуется сброс
                    // оборудывания 1 MESS_AREA_NEED_ACK 178
//                if (!blinkTimer1.isRunning()) {
//                    blinkTimer1.start();
//                }
                    Terminal.TIMER600.addActionListener(AREA1);
                    event(178);
                } else {// false "NO_NEED_ACKN"://0 NO_NEED_ACKN Не требуется сброс
                    // оборудывания 1
//                blinkTimer1.stop();
                    Terminal.TIMER600.removeActionListener(AREA1);
                    btnFirstResetHW.setBackground(cmdBtnIdleBckColor);
                }
            }
//        if (alarmSound != ALARM_SOUND) {
//            alarmSound = ALARM_SOUND;
//            // else{//false "MUTE"://0 MUTE Нет ошибки персонала
//            //
//            // }
//        }
//        if (hollowCancel != HOLLOW_CANCEL) {
//            hollowCancel = HOLLOW_CANCEL;
//            if (HOLLOW_CANCEL) {// true "ALLOWED"://1
//                lblCmdHollowCancelCounter.setBackground(Color.GREEN);
//                event(177);
//            } else {// false "NOT_ALLOWED"://0
//                lblCmdHollowCancelCounter.setBackground(Color.WHITE);
//            }
//        }
            if (boolBtnTU != HOLLOW_CANCEL_BTN) {
                boolBtnTU = HOLLOW_CANCEL_BTN;
                if (HOLLOW_CANCEL_BTN) {// true "PRESS_BTN"://1 PRESS_BTN Нажата
                    // програмная кнопка разрешения сброса
                    // ложной занятости
                    btnTele.setBackground(Color.GREEN);
                } else {// "RELEASE_BTN"://0 RELEASE_BTN Не нажата програмная кнопка
                    // разрешения сброса ложной занятости
                    btnTele.setBackground(cmdBtnIdleBckColor);
                }
            }
            if (hwAckn_2 != HW_ACKN_2) {
                hwAckn_2 = HW_ACKN_2;
                if (HW_ACKN_2) {// true "NEED_ACKN"://1 NEED_ACKN Требуется сброс
                    // оборудывания 2
//                if (!blinkTimer2.isRunning()) {
//                    blinkTimer2.start();
//                }
                    Terminal.TIMER600.addActionListener(AREA2);
                } else {// false "NO_NEED_ACKN"://0 NO_NEED_ACKN Не требуется сброс
                    // оборудывания 2
                    Terminal.TIMER600.removeActionListener(AREA2);
//                blinkTimer2.stop();
                    btnSecondResetHW.setBackground(cmdBtnIdleBckColor);
                }
            }
            if (ackn_2err != ACKN_2ERR) {
                ackn_2err = ACKN_2ERR;
                if (ACKN_2ERR) {// true"ERR"://1 ERR неисправность оборудования 2
                    // (межпарковая связь) MESS_ACKN_2ERR 157
                    btnSecondResetHW.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 1));//
                    event(157);
                    alarm_on();
                } else {// false "NO_ERR"://0 NO_ERR Нет ошибок оборудования 2
                    btnSecondResetHW.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2));
                    alarm_off();
                }
            }
        }
    }

    private void event(int id_msg) {
//        if (getDtime() > 0) {
        Events.InsertMessage(getDtime(), id_obj, id_msg);
//        }
    }

    private void alarm_on() {//Inline value '101' for parameter 'id'
//        if (getDtime() > 0) {
        Alarms.alarm_on(getDtime(), 101, id_obj);
//        }
    }

    private void alarm_off() {//Inline value '101' for parameter 'id'
//        if (getDtime() > 0) {
        Alarms.alarm_off(getDtime(), 101, id_obj);
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
        AreaNameState a = (AreaNameState) oid;
        this.setState(
                a.timestamp,
                a.HOLLOWS, // 1 "HOLLOWS"
                a.ROUTE, // 2 "ROUTE"
                a.GROUP_CANCEL, // GOT
                a.DIAL_CANCEL, // 4 "DIAL_CANCEL"
                a.HW_ACKN_1, // 5 "HW_ACKN_1"
                a.ALARM_SOUND, // 6 "ALARM_SOUND"
                a.HOLLOW_CANCEL, // 7 "HOLLOW_CANCEL"
                a.HOLLOW_CANCEL_BTN,// 8 "HOLLOW_CANCEL_BTN"
                a.HW_ACKN_2, // 9 "HW_ACKN_2"
                a.ACKN_2ERR);
    }

    private void set_terminal_number() {
        Net.myIp();
        int ddd = Net.getMyIpAddr()[3] & 0xFF;//myIpAddr[3] & 0xFF;//unsignedToBytes
        int ccc = Net.getMyIpAddr()[2] & 0xFF;//myIpAddr[2] & 0xFF;//unsignedToBytes
        if (ddd > 100) {
            terminal_number = ddd - 100 + ccc;
        } else {
//                terminal_number = ddd - 100 + ccc;
            terminal_number = ddd + ccc;
        }
        Log.log("ID_terminal = " + terminal_number);
    }

    private void setManager(int TERMINAL_NUM) {
        if (TERMINAL_NUM == terminal_number) {
            Manager_PC = true;
            btnTele.setBackground(new java.awt.Color(140, 255, 100));//Зеленый
            btnTele.setText("<html>режим ARM:<br>УПРАВЛЕНИЕ</html>");
        } else {
            Manager_PC = false;
            btnTele.setBackground(new java.awt.Color(240, 255, 128));//Желтый
            btnTele.setText("<html>режим ARM:<br>Контроль</html>");
        }
        status.SetCmd(true);
    }
}

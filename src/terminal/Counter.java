//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.awt.Color;
import javax.swing.BorderFactory;
import ru.attrans.proc.objs.CounterState;

class Counter extends Cell {
//    private final boolean co = Terminal.counter;
    final java.util.HashMap<Integer, Integer> vzrez = new java.util.HashMap<>(3);// int
    final int inc_sgn;//направление движения через датчик
    private final boolean orientation;
    // Counter layers
    private static final int COUNTER_LR = 10;//8;//Может быть надо 7+1
    private static final int COUNTER_CANSEL_LR = 14;//9;//Может быть надо 7+2 - Аварийная отмена
    private static final int COUNTER_FAULT_LR = 13;//Может быть надо 7+3    Неисправность
    private static final int COUNTER_FENCE_LR = 12;//Может быть надо 7+3    Огражден
    private static final int COUNTER_REQ_LR = 11;//Может быть надо 7+3    Запрос
//    private static final int COUNTER_LAYER = 7; //Может быть надо
    // ------------------------------------------------------------------------------
    // private final String area_name;
//    final String s_name;// COUNTER_NAME
    final int rn1;// left
    final int rn2;// right
    // final String leftRNName;
    // final String rightRNName;
    final int beam_id;
    final int log_address;
    private final boolean clear;// clearance //victor FUCK FUFUFU нафига эта переменная?

    final javax.swing.JLabel Counter = new javax.swing.JLabel();

    private final Img lblCmdCounterA = new Img();//adjust       9
    private final Img lblCmdCounter  = new Img();//static       18
    private final Img lblCmdCounterL = new Img();//dinamic left 20
    private final Img lblCmdCounterR = new Img();//dinami right 20
    private final Img lblCmdCounterB = new Img();//blocing      33
    private final Img lblCmdCounterD = new Img();//device error 81
    private final Img lblCmdCounterE = new Img();//error        129,145
    private final Img lblCmdCounterU = new Img();//unknown      --
    private final Img lblCmdUnClear  = new Img();
    private final Img lblCmdCounterFenceFault = new Img();//gif
    private final Img lblCmdCounterFence = new Img();
    private final Img lblCmdCounterFenceCancel = new Img();//gif
    private final Img lblCmdCounterFenceReq = new Img();//gif

    int commErrors;
// String stringProp;
    String counterState = "<html><font color=gray>статус неизвестен</html>";
    String dynData = "код ош: 0";
    boolean adjusable = true;// //Default
    private boolean boom;
    private boolean boomZap;// запрос на ограждение (есть или нет)
    private boolean boom_cancel;
    // ------------------------------------------------------------------------------
    // privats
    private boolean boom_fault;
    private int boom_state;
    private int counter_state; // COUNTER_STATE
    private int axis; // AXIS
    private int errorCode; // ERROR_CODE
    private int adjPhase; // ADJ_PHASE
    int orient;
    private int dir=2;//направление =0-влево, 1-вправо, 2=неизвестно
    private boolean status_unknown=true;
    
    Counter(
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
            int RN1, //10=RN1
            int RN2, //11=RN2
            int INC_SGN, //12=INC_SGN --левый правый рельс
            boolean CLRTYPE, //13=CLRTYPE
            int BEAM_ID, //14=BEAM_ID
            int LOG_ADDRESS //15=LOG_ADDRESS
    //        int IND //16=IND
    ) {
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 7);// 7=COUNTER
        clear = CLRTYPE; //victor FUCK FUFUFU нафига эта переменная?
        beam_id = BEAM_ID;
        log_address = LOG_ADDRESS;
//        int count_unit_id = COUNT_UNIT_ID;
//        s_name = S_NAME;
        rn1 = RN1;
        rn2 = RN2;
        inc_sgn = INC_SGN;
        
        orientation = INC_SGN != 0;

        if (ORIENTATION > 12) {
            orient = ORIENTATION - 12;
        } else {
            orient = ORIENTATION;
        }
        setPanes();// для отображения датчиков в терминале
        setDefaultState();
        // ------------------------------------------------------------------------------
        Counter.setPreferredSize(new java.awt.Dimension(25, 30));
        Counter.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        Counter.setVerticalAlignment(javax.swing.JLabel.CENTER);
        Counter.setFont(Terminal.SANS09);
        Counter.setOpaque(true);
        if (ID_OBJ > 1) {
            Counter.setBackground(Color.DARK_GRAY);
            Counter.setText("<html><font color=white size=2>"
                    + String.valueOf(LOG_ADDRESS) + "</font><br>" + S_NAME
                    + "</html>");
            Counter.setToolTipText(S_NAME);

            Counter.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 1),
                    BorderFactory.createLineBorder(Color.GRAY, 2)));
            Counter.setForeground(Color.GRAY);

        } else {
            Counter.setBorder(null);
        }
    }

    private void setPanes() {//zoom
        switch (Terminal.zoom) {
            case 72:
                switch (orient) {
                    case 3:
                        cmdX += 61;//84-22=62
                        cmdY += -11;
                        break;
                    case 6:
                        cmdX += 61;
                        cmdY += 25;//84/2=42-11=31-6=25
                        break;
                    case 9:
                        cmdX += 61;
                        cmdY += 61;
                        break;
                    case 12:
                        cmdX += 25;
                        cmdY += 61;
                        break;
                }
                break;
            case 56:
                switch (orient) {
                    case 3:
                        cmdX += 44;//56-22=34
                        cmdY += -11;//половина высоты counter/2==11
                        break;
                    case 6:
                        cmdX += 44;
                        cmdY += 17;//56/2=28-11=17
                        break;
                    case 9:
                        cmdX += 44;
                        cmdY += 44;
                        break;
                    case 12:
                        cmdX += 17;
                        cmdY += 44;
                        break;
                }
                break;
            case 36:
                switch (orient) {
                    case 3:
                        cmdX += 30;//19;//42-22=20
                        cmdY += 0;//-11;//половина высоты counter/2==11
                        break;
                    case 6:
                        cmdX += 30;
                        cmdY += 11;//42/2=22-11=11
                        break;
                    case 9:
                        cmdX += 30;
                        cmdY += 30;
                        break;
                    case 12:
                        cmdX += 11;
                        cmdY += 30;
                        break;
                }
                break;
            case 18:
                break;

        }
        Util.prep_lbl(lblCmdCounterA, COUNTER_LR-1, "counter__" + "adjust", cmdX, cmdY);//adjust        9
        Util.prep_lbl(lblCmdCounter,  COUNTER_LR,   cmdPicturePreffix(orient) + "empty",  cmdX, cmdY);//static        18
        Util.prep_lbl(lblCmdCounterL, COUNTER_LR+1, cmdPicturePreffix(orient) + "emptyL", cmdX, cmdY);//dinamic left  20
        Util.prep_lbl(lblCmdCounterR, COUNTER_LR+1, cmdPicturePreffix(orient) + "emptyR", cmdX, cmdY);//dinamic right 20
        Util.prep_lbl(lblCmdCounterB, COUNTER_LR+1, cmdPicturePreffix(orient) + "emptyB", cmdX, cmdY);//block         33
        Util.prep_lbl(lblCmdCounterD, COUNTER_LR+1, cmdPicturePreffix(orient) + "emptyD", cmdX, cmdY);//device error  81
        Util.prep_lbl(lblCmdCounterE, COUNTER_LR+1, cmdPicturePreffix(orient) + "emptyE", cmdX, cmdY);//Errror        129,145
        Util.prep_lbl(lblCmdCounterU, COUNTER_LR+1, cmdPicturePreffix(orient) + "emptyU", cmdX, cmdY);//unknown       --

        Util.prep_lbl(lblCmdCounterFence, COUNTER_FENCE_LR, cmdPicturePreffix(orient) + "fence", cmdX, cmdY);
        Util.prep_lbl(lblCmdCounterFenceCancel, COUNTER_CANSEL_LR, cmdPicturePreffix(orient) + "fence_cancel", cmdX, cmdY);
        Util.prep_lbl(lblCmdCounterFenceFault, COUNTER_FAULT_LR, cmdPicturePreffix(orient) + "fence_fault", cmdX, cmdY);
        Util.prep_lbl(lblCmdCounterFenceReq, COUNTER_REQ_LR, cmdPicturePreffix(orient) + "fence_req", cmdX, cmdY);

        if (!clear) {
            Util.prep_lbl(lblCmdUnClear, 15, cmdPicturePreffix(-1) + "unclear", cmdX, cmdY);
        }
        lblCmdUnClear.setVisible(!clear);//victor FUCK FUFUFU нафига эта переменная?        

    }

    private String cmdPicturePreffix(int orient) {
        String s = "";
        if (orient != -1) {
            s = String.valueOf(orient);
        }
        return "counter__" + s + "__";
    }

    // default state
    private void setDefaultState() {
        lblCmdCounterA.setVisible(false);//adjust       9
        lblCmdCounter.setVisible(true);//статик         18
        lblCmdCounterL.setVisible(false);//динамик left 20
        lblCmdCounterR.setVisible(false);//динам right  20
        lblCmdCounterB.setVisible(false);//blocing      33
        lblCmdCounterD.setVisible(false);//devive error 81
        lblCmdCounterE.setVisible(false);//error        129,145
        lblCmdCounterU.setVisible(false);//error        --
        
        lblCmdCounterFence.setVisible(false);
        lblCmdCounterFenceCancel.setVisible(false);
        lblCmdCounterFenceFault.setVisible(false);
        lblCmdCounterFenceReq.setVisible(false);
    }

    // для индикации ограждений
    public void setFenceState(int BOOMSTATE, boolean FLT_OG_R,
            boolean BOOM_CANSEL) {// BOOMSTATE, FLT_OG_R, BOOM_CANSEL
        /*
         * 1 BOOM_OFF Ограждение пути снято MESS_RAILNET_BOMM_OFF 286 2 BOMM_ZAPR
         * Запрос на ограждение пути MESS_RAILNET_BOMM_ZAPR 285 4 BOOM_ON Путь
         * огражден MESS_RAILNET_BOMM_ON 284
         */
        if (boom_state != BOOMSTATE) {
            boom_state = BOOMSTATE;
            switch (BOOMSTATE) {// int
                case 1:// "BOOM_OFF"://1 BOOM_OFF Ограждение пути снято
                    // MESS_RAILNET_BOMM_OFF 286
                    boom = false;
                    boomZap = false;
//                    event(286);//не надо ничего писать т.к. в рельсе уже написано
                    break;
                case 4:// "BOOM_ON"://4 BOOM_ON Путь огражден MESS_RAILNET_BOMM_ON
                    // 284
                    boom = true;
                    boomZap = false;
//                    event(284);//не надо ничего писать т.к. в рельсе уже написано
                    break;
                case 2:// "BOMM_ZAPR"://2 BOMM_ZAPR Запрос на ограждение пути
                    // MESS_RAILNET_BOMM_ZAPR 285
                    boomZap = true;
                    boom = false;
//                    event(285);//не надо ничего писать т.к. в рельсе уже написано
                    break;
            }
            lblCmdCounterFence.setVisible(boom);
            lblCmdCounterFenceReq.setVisible(boomZap);
        }
        /*
         * 0 BOOM_CANSEL_OFF Нет аварийной отмены заграждения 1 BOOM_CANSEL_ON
         * Аварийная отмена ограждения MESS_RAILNET_BOMM_CAMSEL 280
         */
        if (boom_cancel != BOOM_CANSEL) {
            boom_cancel = BOOM_CANSEL;
//            if (BOOM_CANSEL) {// true 1 BOOM_CANSEL_ON Аварийная отмена
            // ограждения MESS_RAILNET_BOMM_CAMSEL 280
//                event(280);//не надо ничего писать т.к. в рельсе уже написано
//            } else {// false 0 BOOM_CANSEL_OFF Нет аварийной отмены заграждения

//            }
            lblCmdCounterFenceCancel.setVisible(BOOM_CANSEL);
        }
        /*
         * 0 BOOM_FLT_OG_R_OFF Схема ограждения пути исправна 1 BOOM_FLT_OG_R_ON
         * Неисправность схемы ограждения пути MESS_RAILNET_BOMM_FLT_OG_R 282
         */
        if (boom_fault != FLT_OG_R) {
            boom_fault = FLT_OG_R;
//            if (FLT_OG_R) {// true 1 BOOM_FLT_OG_R_ON Неисправность схемы
            // ограждения пути MESS_RAILNET_BOMM_FLT_OG_R 282
//                event(282);//не надо ничего писать т.к. в рельсе уже написано
//            } else {// false 0 BOOM_FLT_OG_R_OFF Схема ограждения пути исправна

//            }
            lblCmdCounterFenceFault.setVisible(FLT_OG_R);
        }
    }

    void setState(long DTIME, int COUNTER_STATE, int AXIS, int COMM_ERRORS, int ERROR_CODE, int ADJ_PHASE) {// Вместо int axis ставим short
        setDtime(DTIME);//68 43 402
        //----------------------------------------------------------------------
        if (axis != AXIS) {// Default = 0
            // axis = (short)((AXIS >> 8) + ((AXIS & 0x0000_00ff) << 8));//Siemens
            if (COUNTER_STATE == 18) {//статик
                dynData = "кол. осей: " + AXIS;
                dir=2;
//                if (Terminal.VZREZ) {
//                    vzrez(AXIS);
//                }
            }
            if (COUNTER_STATE == 20) {//динамик
                dynData = "кол. осей: " + AXIS;
                dir=get_course(AXIS);
                if (Terminal.VZREZ) {
                    vzrez(AXIS);
                }
            }
            axis = (AXIS);
        }
        //----------------------------------------------------------------------
        if (commErrors != COMM_ERRORS) {// Default = 0
            // commErrors = ((COMM_ERRORS >> 8) + ((COMM_ERRORS & 0x0000_00ff) <<  8));//Siemens
            commErrors = (COMM_ERRORS);
            if (COUNTER_STATE == 129) {
                dynData = "кол. ошибок: " + commErrors;
            }
            if (COUNTER_STATE == 145) {
                dynData = "кол. ошибок: " + commErrors;
            }
        }
        //----------------------------------------------------------------------
        if (errorCode != ERROR_CODE) {// Default = 0
            // errorCode = ((ERROR_CODE >> 8) + ((ERROR_CODE & 0x0000_00ff) << 8));//Siemens
            errorCode = (ERROR_CODE);
            if (COUNTER_STATE == 33) {
                dynData = "код операции:" + intToHex(errorCode);
            }
            if (COUNTER_STATE == 81) {
                dynData = "код ош: " + intToHex(errorCode);
            }
        }
        //----------------------------------------------------------------------
        if (adjPhase != ADJ_PHASE) {// Default = 0
            // adjPhase = ((ADJ_PHASE >> 8) + ((ADJ_PHASE & 0x0000_00ff) << 8));//Siemens
            if (COUNTER_STATE == 9) {
                dynData = "стадия:" + intToHex(adjPhase);
            }
            adjPhase = (ADJ_PHASE);
        }
        //----------------------------------------------------------------------
        /*
         * 0 NO_INFO Нет датчика? MESS_COUNTER_NOINFO 153 9 ADJUST Настройка
         * MESS_COUNTER_ADJUST 192 18 COUNTING_STATIC Счет осей статика
         * MESS_COUNTER_STATIC 154 20 COUNTING_DYNAMIC Счет осей динамика
         * MESS_COUNTER_DYNAMIC 152 33 BLOCKING Блокировка MESS_COUNTER_BLOCK
         * 193 81 DEVICE_ERROR Ошибка НСУ MESS_COUNTER_DEV_ERR 194 129
         * COMM_ERROR Ошибка связи MESS_COUNTER_COMM_ERR 151 145 COMM_ERROR
         * Ошибка связи MESS_COUNTER_COMM_ERR 151
         */
        if (counter_state != COUNTER_STATE) {// int Default value = 0;
            counter_state = COUNTER_STATE;
            switch (COUNTER_STATE) {// int
                case 9:// 9 ADJUST Настройка MESS_COUNTER_ADJUST 192
                    status_unknown = false;
                    adjusable = true;
//                    boolean fault = false;
                    counterState = "<html><font color=#996600>настройка</html>";
//                    dynData = "стадия:" + intToHex(adjPhase);
                    
                    Counter.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.BLACK, 1),
                            BorderFactory.createLineBorder(Color.YELLOW, 2)));
                    Counter.setForeground(Color.YELLOW);
                    event(192);
                    alarm_off(114);
                    break;
                case 18:// 18 COUNTING_STATIC Счет осей статика MESS_COUNTER_STATIC 154
                    status_unknown = false;
                    adjusable = false;
                    counterState = "<html><font color=blue>счет осей : покой</html>";
//                    dynData = "кол-во осей: " + axis;
                    Counter.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.BLACK, 1),
                            BorderFactory.createLineBorder(Color.GREEN, 2)));
                    Counter.setForeground(Color.GREEN);
//                    event(154); не нужно писать в евент
                    alarm_off(113);
                    alarm_off(114);
                    break;
                case 20:// 20 COUNTING_DYNAMIC Счет осей динамика MESS_COUNTER_DYNAMIC 152
                    status_unknown = false;
                    adjusable = true;//?????????????????????????????????????????
                    counterState = "<html><font color=green>счет осей : счет</html>";
//                    dynData = "кол-во осей: " + axis;
                    Counter.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.BLACK, 1),
                            BorderFactory.createLineBorder(Color.GREEN, 2)));
                    Counter.setForeground(Color.GREEN);
//                    event(152); не нужно писать в евент
                    alarm_off(113);
                    alarm_off(114);
                    break;
                case 129:// "COMM_ERROR"://129 || 145 129 COMM_ERROR Ошибка связи MESS_COUNTER_COMM_ERR 151
                    status_unknown = false;
                    adjusable = true;                    
                    counterState = "<html><font color=red>ошибка связи</html>";
//                    dynData = "кол-во ошибок: " + commErrors;
                    Counter.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.BLACK, 1),
                            BorderFactory.createLineBorder(Color.RED, 2)));
                    Counter.setForeground(Color.PINK);
                    event(151);
                    alarm_on(113);
                    break;
                case 145:// 145 COMM_ERROR Ошибка связи MESS_COUNTER_COMM_ERR 151
                    status_unknown = false;
                    adjusable = true;                    
                    counterState = "<html><font color=red>ошибка связи</html>";
//                    dynData = "кол-во ошибок: " + commErrors;
                    Counter.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.BLACK, 1),
                            BorderFactory.createLineBorder(Color.RED, 2)));
                    Counter.setForeground(Color.PINK);
                    event(151);
                    alarm_on(113);
                    break;
                case 33:// 33 BLOCKING Блокировка MESS_COUNTER_BLOCK 193
                    status_unknown = false;
                    adjusable = true;                    
                    counterState = "<html><font color=#996600>блокировка</html>";
//                    dynData = "код операции:" + intToHex(errorCode);
                    Counter.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.BLACK, 1),
                            BorderFactory.createLineBorder(Color.YELLOW, 2)));
                    Counter.setForeground(Color.YELLOW);
                    event(193);
                    break;
                case 81:// 81 DEVICE_ERROR Ошибка НСУ MESS_COUNTER_DEV_ERR 194
                    status_unknown = false;
                    adjusable = true;                    
                    counterState = "<html><font color=red>ошибка НСУ</html>";
//                    dynData = "код ошибки: " + intToHex(errorCode);
                    Counter.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.BLACK, 1),
                            BorderFactory.createLineBorder(Color.RED, 2)));
                    Counter.setForeground(Color.PINK);
                    event(194);
                    alarm_on(114);
                    break;
                default:
                    status_unknown = true;
                    adjusable = true;                    
                    counterState = "<html><font color=gray>статус неизвестен</html>";
                    dynData = "код ош: " + intToHex(errorCode);
                    Counter.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.BLACK, 1),
                            BorderFactory.createLineBorder(Color.GRAY, 2)));
                    Counter.setForeground(Color.GRAY);
                    event(153);
            }
//нужно сделать 
            if (Commander.cpuMon.isVisible()) {
                Commander.cpuMon.repaint();
            }
            if (Terminal.counter) {
                lblCmdCounterA.setVisible(counter_state==9);//adjust       9
//                lblCmdCounter.setVisible(counter_state==18);//статик         18
                lblCmdCounterL.setVisible((counter_state==20) & (dir==0));//динамик left 20
                lblCmdCounterR.setVisible((counter_state==20) & (dir==1));//динам right  20
                lblCmdCounterB.setVisible(counter_state==33);//blocing      33
                lblCmdCounterD.setVisible(counter_state==81);//devive error 81
//                lblCmdCounterE.setVisible(counter_state==129);//error        129,145
                lblCmdCounterE.setVisible(counter_state==129 | counter_state==145);//error        129,145
                lblCmdCounterU.setVisible(status_unknown);//error        --
            }
        }//end if counter state changed

        if (Terminal.counterTableReady) {
            for (int i = 0; i < CounterLst.DTM.getRowCount(); i++) {
                if (Integer.parseInt(CounterLst.DTM.getValueAt(i, 0).toString()) == id_obj) {
                    if (!CounterLst.DTM.getValueAt(i, 5).toString().equals(counterState)) {
                        CounterLst.DTM.setValueAt(counterState, i, 5);
                    }
                    if (!CounterLst.DTM.getValueAt(i, 6).toString().equals(dynData)) {
                        CounterLst.DTM.setValueAt(dynData, i, 6);
                    }
                    if ((int) CounterLst.DTM.getValueAt(i, 7) != commErrors) {// victor - это ваще пипец -> toStrind -> toInt...
                        CounterLst.DTM.setValueAt(commErrors, i, 7);
                    }
                }
            }
        }
    }

    String getRNname(int id_obj) {
        if (Terminal.Railnets_Hash.containsKey(id_obj)) {
            return Terminal.Railnets_Hash.get(id_obj).s_name;
        }
        return "";
    }

    private String intToHex(int input) {
        String ret;
        ret = Integer.toHexString(input);
        ret = (ret.length() == 3) ? "0x" + ("0" + ret).toUpperCase() : "0x"
                + ret.toUpperCase();
        return ret;
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

    @Override
    public void setState(Object oid) {
        CounterState c = (CounterState) oid;
        setState(
                c.timestamp,
                c.COUNTER_STATE,
                c.AXIS,
                c.COMM_ERRORS,
                c.ERROR_CODE,
                c.ADJ_PHASE
        );
    }
    
    private int get_course(int AXIS) {
        int DIRECTION;
        int a;
        a = AXIS - axis;
        if (Math.abs(a) > 32767) {
            a = a < 0 ? a + 65536 : a - 65536;
        }
        if ((a > 0 && !orientation) || (a < 0 && orientation)) {// Откуда и куда едем...
            DIRECTION = 1;//вправо
        } else {
            DIRECTION = 0;//влево
        }
        return DIRECTION;
    }

    private void vzrez(int AXIS) {
        if (getDtime() > 0) {
            int TEST;
            int POSITION;
//            int DIRECTION;
//            int a;
////            Railnet rn;
//            String rnL;
//            String rnR;
//            String orient = "";
//            a = AXIS - axis;
//            // Переход через 0 !!!!!!!
//            if (Math.abs(a) > 32767) {
//                a = a < 0 ? a + 65536 : a - 65536;
//            }
//                rnL = Terminal.Railnets_Hash.get(rn1).s_name;
//                rnR = Terminal.Railnets_Hash.get(rn2).s_name;
//            orient = orientation ? " L" : " R";
//            if ((a > 0 && !orientation) || (a < 0 && orientation)) {// Откуда и куда едем...
//                DIRECTION = 1;//вправо
//                Log.log("" + id_obj + " " + s_name + " old.axis:" + axis + " new.axis:" + AXIS + " " + rnL + " -> " + rnR + " " + orient + " " + inc_sgn + " " + orientation);
//            } else {
//                DIRECTION = 0;//влево
//                Log.log("" + id_obj + " " + s_name + " old.axis:" + axis + " new.axis:" + AXIS + " " + rnL + " <- " + rnR + " " + orient + " " + inc_sgn + " " + orientation);
//            }
            for (Integer TURNOUT_ID : vzrez.values()) {
                Turnout t = Terminal.Turnouts_Hash.get(TURNOUT_ID);
                if (t.getDtime() != 0) {
                    POSITION = t.PositionState;
//                String p = POSITION < 2 ? "-": "+";
//                    Log.log(t.s_name + " in position " + POSITION);
                    TEST = (((TURNOUT_ID << 1) | (dir & 0x1)) << 2) | (POSITION & 0x3);
                    Log.log("vzrez_hash 1 " + vzrez.get(TEST) + " TEST:" + TEST + " DIRECTION:" + dir + " POSITION:" + POSITION);
                    if (vzrez.containsKey(TEST)) {
                        Log.log("vzrez_hash " + vzrez.get(TEST) + " TEST:" + TEST + " DIRECTION:" + dir + " POSITION:" + POSITION);
                        Net.sendMaskedCmd_DSP_SHN(TURNOUT_ID, "TURNOUT_VZREZ_ON");//шлем команду в контроллер о взрезе
                    }
                }//end if time==0
            }
        }//end get_time
    }//end vzrez
}

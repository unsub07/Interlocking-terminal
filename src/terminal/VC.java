//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
/*
 1-FUTURE-строительство, 2-INVISIBLE-невидимый, 3-DEPRECATE-н.е.н.х.
 */
package terminal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import ru.attrans.proc.objs.VoltageControlState;
import static terminal.Commander.cmdLayers;

class VC extends Cell {//нахуя Cell?
    // ----------------------------
    // иконки

    private static final javax.swing.ImageIcon fuse_ok = Terminal.mainPictureHash.get("fuse_ok");
    private static final javax.swing.ImageIcon fuse_fault = Terminal.mainPictureHash.get("fuse_fault");
    private static final javax.swing.ImageIcon grounding_ok = Terminal.mainPictureHash.get("grounding_ok");
    private static final javax.swing.ImageIcon grounding_fault = Terminal.mainPictureHash.get("grounding_fault");
    private static final javax.swing.ImageIcon error = Terminal.mainPictureHash.get("error");
    private static final javax.swing.ImageIcon no_error = Terminal.mainPictureHash.get("no_error");
    private static final javax.swing.ImageIcon upsOverload = Terminal.mainPictureHash.get("overload");
    private static final javax.swing.ImageIcon no_overload = Terminal.mainPictureHash.get("no_overload");
    private static final javax.swing.ImageIcon ups_ac_empty = Terminal.mainPictureHash.get("ups_ac_empty");
    // ------------------------------------------------------------------------------
    private static final javax.swing.ImageIcon ups_empty = Terminal.mainPictureHash.get("ups_empty");

    private static final javax.swing.ImageIcon ups_half = Terminal.mainPictureHash.get("ups_half");
    private static final javax.swing.ImageIcon ups_ac_full = Terminal.mainPictureHash.get("ups_ac_full");
    private static final javax.swing.ImageIcon locked = Terminal.mainPictureHash.get("locked");
    private static final javax.swing.ImageIcon unlocked = Terminal.mainPictureHash.get("unlocked");
    private static final javax.swing.ImageIcon fireAlarm = Terminal.mainPictureHash.get("fire");// gif
    private static final javax.swing.ImageIcon no_fire = Terminal.mainPictureHash.get("no_fire");

    private static final Color cmdCellPaneBackground = new Color(190, 220, 250, 130);
    private static final Border cmdCellPaneBorder = BorderFactory.createLineBorder(new Color(150, 180, 210));
    private static final Border cmdActiveIndicatorBorder = BorderFactory.createLineBorder(new Color(0, 140, 0), 3);
    private final javax.swing.JLayeredPane cmdCellPane = new javax.swing.JLayeredPane();
    private final javax.swing.JLabel lblCmdFuseControl = new javax.swing.JLabel(fuse_fault);
    private final javax.swing.JLabel lblCmdGroundingControl = new javax.swing.JLabel(grounding_fault);
    private final javax.swing.JLabel lblUpsError = new javax.swing.JLabel(no_error);
    private final javax.swing.JLabel lblUpsOverload = new javax.swing.JLabel(no_overload);
    private final javax.swing.JLabel lblUpsBattery = new javax.swing.JLabel(ups_ac_full);
    private final javax.swing.JLabel lblDoorLocked = new javax.swing.JLabel(locked);
    private final javax.swing.JLabel lblFireAlarm = new javax.swing.JLabel(no_fire);
    // ------------------------Translate---------------------------------------------
    private final String VCTitle;
    private final String Feeder;
    private final String Fuse;
    private final String Ground;

    private final boolean w_1fider;
    private final boolean w_2fider;
    private final boolean w_3fider;
    private final boolean w_lowvc;
    private final boolean w_fuse_ctrl;
    private final boolean w_ground_ctrl;
    private final boolean w_amperes; // используются аналоговые амперметры ?
    private final boolean w_ups;
    private final boolean w_fire;
    private final boolean w_access;
    private final boolean w_ogsp;// есть на станции контроль неисправности
    // ограждения составов метки, индикаторы
    private final javax.swing.JLabel lblName = new javax.swing.JLabel();
    private final javax.swing.JLabel lblCmdFirstFeeder = new javax.swing.JLabel("~Ф1");
    private final javax.swing.JLabel lblCmdSecondFeeder = new javax.swing.JLabel("~Ф2");
    private final javax.swing.JLabel lblCmdThirdFeeder = new javax.swing.JLabel("~Ф3");
    private final javax.swing.JLabel lblCmdLowVoltage = new javax.swing.JLabel("=24v");
    // private final JLabel lblCmdToutAmperes = new JLabel();
    private final javax.swing.JProgressBar pbCmdAmperes = new javax.swing.JProgressBar(0, 50);
    private final javax.swing.JPanel pnlUps = new javax.swing.JPanel();
    private final javax.swing.JPanel pnlFireAndDoor = new javax.swing.JPanel();
    private boolean firstFeederUp;// = true;//
    private boolean firstFeederActive;// = true;//
    private boolean secondFeederUp;// = true;//
    private boolean secondFeederActive;// = true;//
    private boolean thirdFeederUp;// = true;//
    private boolean thirdFeederActive;// = true;//
    private boolean lowVoltageUp;// Напряжение =24В 220B 36B 12B
    private boolean cur_dig;// 0 - аналоговый амперметр 1-цифровой амперметр
    private boolean fuseControlUp;// = true;//
    private boolean groundingControlUp = true;// ?
    // --Commented out by Inspection (16.01.18 14:56):private boolean toutCurrentControlUp;
    private boolean full_discharge;
    private boolean discharging;
    private boolean overload;
    private boolean common_err;
    private boolean fire;
    private boolean access;
    private boolean ogsp;

    private final int amplification;
    private final int drift_zero;
    private final int stepscale;
    private boolean revers_bit;

    private final ActionListener VC1 = (ActionEvent evt) -> {
        revers_bit = !revers_bit;
        if (revers_bit) {
            lblCmdFirstFeeder.setBackground(Color.RED);
            lblCmdSecondFeeder.setBackground(Color.WHITE);
        } else {
            lblCmdFirstFeeder.setBackground(Color.WHITE);
            lblCmdSecondFeeder.setBackground(Color.RED);
        }
    };
//    private final javax.swing.Timer blinkTimer1 = new javax.swing.Timer(600, blinkPerformer1);

    VC(
            int ID_OBJ, //0=ID_OBJ
            //        int ID_AREA, //1=ID_AREA
            String S_NAME, //2=S_NAME
            //        String MPC_NAME, //3=MPC_NAME
            int X, //4=X
            int Y, //5=Y
            int SHIFT_X, //6=SHIFT_X
            int SHIFT_Y, //7=SHIFT_Y
            int VIDEO_STATUS, //8=VIDEO_STATUS

            boolean W_1FIDER,
            boolean W_2FIDER,
            boolean W_3FIDER,
            boolean W_LOWVC,
            boolean W_FUSE_CTRL,
            boolean W_GROUND_CTRL,
            boolean W_AMPERS,
            //        boolean W_CURRENT_PRESENCE,
            boolean W_UPS,
            boolean W_FIRE,
            boolean W_ACCESS,
            boolean W_OGSP,
            int AMPLIFICATION,
            int DRIFT_ZERO,
            int STEPSCALE
    ) { // ++++++++++++ videoStatus
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 19);//19 VOLTAGE_CONTROL
        // -------------------------------Translate--------------------------------------
        VCTitle = "Электропитание: ";//translate
        Feeder = "Контроль фидера питания";//translate
        Fuse = "Контроль предохранителей";//translate
        Ground = "Контроль заземлений";//translate

        // ------------------------------------------------------------------------------
//        s_name = S_NAME;
        w_1fider = W_1FIDER;
        w_2fider = W_2FIDER;
        w_3fider = W_3FIDER;
        w_lowvc = W_LOWVC;
        w_fuse_ctrl = W_FUSE_CTRL;
        w_ground_ctrl = W_GROUND_CTRL;
        w_amperes = W_AMPERS;
        // boolean w_current_presence = W_CURRENT_PRESENCE;
        w_ups = W_UPS;
        w_fire = W_FIRE;
        w_access = W_ACCESS;
        w_ogsp = W_OGSP;

        amplification = AMPLIFICATION;
        drift_zero = DRIFT_ZERO;
        stepscale = STEPSCALE;

        setPanes(W_1FIDER, W_2FIDER, W_3FIDER, W_LOWVC, W_FUSE_CTRL, W_GROUND_CTRL, W_UPS, W_FIRE, W_ACCESS);
        setDefaultState();
    }

    private void setFirstFeederUp(boolean firstFeederUp) {
        this.firstFeederUp = firstFeederUp;
        setControlIndicatorUp(firstFeederUp, lblCmdFirstFeeder);
    }

    private void setFirstFeederActive(boolean firstFeederActive) {
        this.firstFeederActive = firstFeederActive;
        setControlIndicatorActive(firstFeederActive, lblCmdFirstFeeder);
    }

    private void setSecondFeederUp(boolean secondFeederUp) {
        this.secondFeederUp = secondFeederUp;
        setControlIndicatorUp(secondFeederUp, lblCmdSecondFeeder);
    }

    private void setSecondFeederActive(boolean secondFeederActive) {
        this.secondFeederActive = secondFeederActive;
        setControlIndicatorActive(secondFeederActive, lblCmdSecondFeeder);
    }

    private void setThirdFeederUp(boolean thirdFeederUp) {
        this.thirdFeederUp = thirdFeederUp;
        setControlIndicatorUp(thirdFeederUp, lblCmdThirdFeeder);
    }

    private void setThirdFeederActive(boolean thirdFeederActive) {
        this.thirdFeederActive = thirdFeederActive;
        setControlIndicatorActive(thirdFeederActive, lblCmdThirdFeeder);
    }

    private void setLowVoltageUp(boolean lowVoltageUp) {
        this.lowVoltageUp = lowVoltageUp;
        setControlIndicatorUp(lowVoltageUp, lblCmdLowVoltage);
    }

    private void setControlIndicatorUp(boolean isUp, javax.swing.JLabel indicator) {// переключение текстовых аварийных индикаторов
        if (isUp) {
            indicator.setForeground(new java.awt.Color(0, 80, 0));
            indicator.setBackground(new java.awt.Color(140, 255, 100));
            indicator.repaint();
        } else {
            indicator.setForeground(java.awt.Color.WHITE);
            indicator.setBackground(java.awt.Color.RED);
        }
    }

    private void setControlIndicatorActive(boolean isActive, javax.swing.JLabel indicator) {
        if (isActive) {
            indicator.setBorder(cmdActiveIndicatorBorder);
        } else {
            indicator.setBorder(BorderFactory.createEtchedBorder());
        }
    }

    private void setFuseControlUp(boolean fuseControlUp) {
        this.fuseControlUp = fuseControlUp;
        setControlIndicatorUp(fuseControlUp, lblCmdFuseControl);
        if (fuseControlUp) {
            lblCmdFuseControl.setIcon(fuse_ok);
        } else {
            lblCmdFuseControl.setIcon(fuse_fault);
        }
    }

    private void setGroundingControlUp(boolean groundingControlUp) {
        if (groundingControlUp) {
            lblCmdGroundingControl.setIcon(grounding_fault);
            lblCmdGroundingControl.setForeground(java.awt.Color.WHITE);
            lblCmdGroundingControl.setBackground(java.awt.Color.RED);
        } else {
            lblCmdGroundingControl.setIcon(grounding_ok);
            lblCmdGroundingControl.setForeground(new java.awt.Color(0, 80, 0));
            lblCmdGroundingControl.setBackground(new java.awt.Color(140, 255, 100));
            lblCmdGroundingControl.repaint();
        }
    }

    private void setPanes(
            boolean W1FIDER,
            boolean W2FIDER,
            boolean W3FIDER,
            boolean W_LOWVC,
            boolean W_FUSE_CTRL,
            boolean W_GROUND_CTRL,
            //        boolean W_CURRENT_PRESENCE,
            boolean WUPS,
            boolean WFIRE,
            boolean WACCESS
    //        boolean OGSP
    ) {

        int lblAmount
                = (W1FIDER ? 1 : 0)
                + (W2FIDER ? 1 : 0)
                + (W_FUSE_CTRL ? 1 : 0)
                + (W_GROUND_CTRL ? 1 : 0)
                + (W_LOWVC ? 1 : 0)
                + (W3FIDER ? 1 : 0);

        int cmdW, cmdH;
        int lblW = 40, lblH = 22;
        int pbW = 156, pbH = 22;
        int pnlUpsW = 76, pnlUpsH = 22;
        int pnlFireW = 0, pnlFireH = 0;
        if (WFIRE) {
            pnlFireW = 50;
            pnlFireH = 22;
        }

        lblName.setText(VCTitle + s_name);
        lblName.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblName.setVerticalAlignment(javax.swing.JLabel.TOP);
        lblName.setFont(Terminal.SANS14);
        int nameW = lblName.getFontMetrics(lblName.getFont())
                .stringWidth(lblName.getText()) + 4;
        int nameH = lblName.getFontMetrics(lblName.getFont())
                .getHeight();
        lblName.setSize(nameW, nameH);

        cmdW = Math.max(pbW, Math.max(nameW, (lblW + 2) * lblAmount + pnlUpsW
                + pnlFireW + 6));
        cmdH = nameH + lblH + pbH + 12;
        pbW = cmdW - 4;

        cmdCellPane.setSize(cmdW, cmdH);
        cmdCellPane.setLocation(cmdX, cmdY);
        cmdCellPane.setBackground(cmdCellPaneBackground);
        cmdCellPane.setBorder(cmdCellPaneBorder);
        cmdCellPane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 2));

        setCmdLayers();
        cmdCellPane.setVisible(true);

        lblName.setVisible(true);
        cmdCellPane.add(lblName);
        cmdCellPane.setLayer(lblName, 2);

        pbCmdAmperes.setPreferredSize(new Dimension(pbW, pbH));
        pbCmdAmperes.setBorder(BorderFactory.createLineBorder(new java.awt.Color(0, 140, 0), 2));
        pbCmdAmperes.setBorderPainted(true);
        pbCmdAmperes.setFont(Terminal.SANS10);

        pbCmdAmperes.setMaximum(50);// махю 50 ампер
        pbCmdAmperes.setStringPainted(true);

        cmdCellPane.add(pbCmdAmperes);
        cmdCellPane.setLayer(pbCmdAmperes, 2);

        if (W1FIDER) {// есть 1-й фидер
            prep_lbl(lblCmdFirstFeeder, Feeder + " 1", lblW, lblH);
        }

        if (W2FIDER) {// есть 2-й фидер
            prep_lbl(lblCmdSecondFeeder, Feeder + " 2", lblW, lblH);
        }

        if (W3FIDER) {// есть 3-й фидер
            prep_lbl(lblCmdThirdFeeder, Feeder + " 3", lblW, lblH);
        }

        if (W_LOWVC) {// есть контроль питания 24v
            prep_lbl(lblCmdLowVoltage, "контроль питания 24v", lblW, lblH);
        }

        if (W_FUSE_CTRL) {// контроль предохранителей/автоматов
            prep_lbl(lblCmdFuseControl, Fuse, lblW, lblH);
        }

        if (W_GROUND_CTRL) {// контроль заземления
            prep_lbl(lblCmdGroundingControl, Ground, lblW, lblH);
        }

        if (WUPS) {// если есть в проекте UPS
            pnlUps.setPreferredSize(new Dimension(pnlUpsW, pnlUpsH));
            pnlUps.setBorder(BorderFactory.createEtchedBorder());
            pnlUps.setBackground(Color.DARK_GRAY);
            pnlUps.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 3, 0));
            lblUpsError.setToolTipText("Сбой оборудования ИБП");
            pnlUps.add(lblUpsError);
            lblUpsBattery.setToolTipText("Наличие входного напряжения и состояние батареи");
            pnlUps.add(lblUpsBattery);
            lblUpsOverload.setToolTipText("Перегрузка ИБП");
            pnlUps.add(lblUpsOverload);

            pnlUps.setOpaque(true);
            cmdCellPane.add(pnlUps);
            cmdCellPane.setLayer(pnlUps, 2);
        }

        if (WACCESS || WFIRE) {// есть в модуле пожарная/охранная сигнализация
            pnlFireAndDoor.setPreferredSize(new Dimension(pnlFireW, pnlFireH));
            pnlFireAndDoor.setBorder(BorderFactory.createEtchedBorder());
            pnlFireAndDoor.setBackground(Color.DARK_GRAY);
            pnlFireAndDoor.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 3, 0));

            lblFireAlarm.setToolTipText("Пожарная сигнализация");
            pnlFireAndDoor.add(lblFireAlarm);

            lblDoorLocked.setToolTipText("Контроль открытия дверей");
            pnlFireAndDoor.add(lblDoorLocked);

            pnlFireAndDoor.setOpaque(true);
            cmdCellPane.add(pnlFireAndDoor);
            cmdCellPane.setLayer(pnlFireAndDoor, 2);
        }
        if (vStatus != 0) {

            lblCmdFuseControl.setEnabled(false);
            lblCmdGroundingControl.setEnabled(false);
            lblUpsError.setEnabled(false);
            lblUpsOverload.setEnabled(false);
            lblUpsBattery.setEnabled(false);
            lblDoorLocked.setEnabled(false);
            lblFireAlarm.setEnabled(false);
        }
    }

    private void prep_lbl(javax.swing.JLabel lbl, String s, int x, int y) {
        lbl.setOpaque(true);
        lbl.setFont(Terminal.SANS12);
        lbl.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lbl.setVerticalAlignment(javax.swing.JLabel.BOTTOM);
        lbl.setPreferredSize(new Dimension(x, y));
        lbl.setToolTipText(s);
        lbl.setBorder(BorderFactory.createEtchedBorder());
        cmdCellPane.add(lbl);
        cmdCellPane.setLayer(lbl, 2);
        lbl.setVisible(true);
    }

    private void setCmdLayers() {
        cmdLayers.add(cmdCellPane);
        cmdLayers.setLayer(cmdCellPane, CELLPANE_LAYER_10);//10
    }

    /*
     * 58 MESS_AREA_DCH_DOWN 59 MESS_AREA_DCH_UP 60 MESS_AREA_DCL1_DOWN 61
     * MESS_AREA_DCL1_UP 62 MESS_AREA_DCL2_DOWN 63 MESS_AREA_DCL2_UP 64
     * MESS_AREA_DCL3_DOWN 65 MESS_AREA_DCL3_UP 66 MESS_AREA_FUSE_DOWN 67
     * MESS_AREA_F1_ACTIVE 68 MESS_AREA_F1_FAULT 69 MESS_AREA_F1_NORMA 70
     * MESS_AREA_F2_ACTIVE 71 MESS_AREA_F2_FAULT 72 MESS_AREA_F2_NORMA 73
     * MESS_AREA_F3_ACTIVE 74 MESS_AREA_F3_FAULT 75 MESS_AREA_F3_NORMA 76
     * MESS_AREA_GND_DOWN 222 MESS_VOLL_CONT_ACCESS 223
     * MESS_VOLL_CONT_ACCESS_OFF 224 MESS_VOLL_CONT_FIRE 225
     * MESS_VOLL_CONT_FIRE_OFF 226 MESS_VOLL_CONT_UPS_BATT_LOW_OFF 227
     * MESS_VOLL_CONT_UPS_BATT_LOW_ON 228 MESS_VOLL_CONT_UPS_ERR_OFF 229
     * MESS_VOLL_CONT_UPS_ERR_ON 230 MESS_VOLL_CONT_UPS_OFF_BATT 231
     * MESS_VOLL_CONT_UPS_ON_BATT 232 MESS_VOLL_CONT_UPS_OVL_OFF 233
     * MESS_VOLL_CONT_UPS_OVL_ON
     */
    void setState(long DTIME, int AMPERS,// int ampers,
            boolean FIRST_FEEDER_UP,// boolean firstFeederUp,
            boolean FIRST_FIDER_ACTIVE,// boolean firstFeederActive,
            boolean SECOND_FIDER_UP,// boolean secondFeederUp,
            boolean SECOND_FIDER_ACTRIVE,// boolean secondFeederActive,
            boolean THIRD_FIDER_UP,// boolean thirdFeederUp,
            boolean THIRD_FIDER_ACTIVE,// boolean thirdFeederActive,
            boolean FUSEUP,// boolean fuseControlUp,
            boolean GROUNDIGNGUP,// boolean groundingControlUp,
            boolean DCHIGH,// dcHigh
            boolean DCLOW1,// boolean lowVoltageUp,
            boolean DCLOW2,// dcLow2
            boolean DCLOW3,// dcLow3
            boolean CUR_DIG,// аналоговый или цифровой амперметр у стрелки
            boolean OVERLOAD, boolean DISCHARGING,// discharging
            boolean FULL_DISCHARGE,// fullDischarge
            boolean COMMON_ERR, boolean FIRE, boolean ACCESS, boolean OGSP) {
        if (vStatus == 0) {
            setDtime(DTIME);
            // int ampsReversed = ((AMPERS >> 8) + (AMPERS & 0x00ff << 8));//Siemens

            if (cur_dig != CUR_DIG) {
                cur_dig = CUR_DIG;
            }

            if (w_amperes && !CUR_DIG) {
                int pbAmps = (AMPERS > 32767) ? (AMPERS - 65536) : (AMPERS);
                if (pbAmps >= -32_768 && pbAmps < -4_000) {//azot
//            if (pbAmps >= -32768 && pbAmps < -50) {//luga, podolsk
                    pbCmdAmperes.setValue(0);
                    pbCmdAmperes.setString("Перегрузка (минус)");
                    pbCmdAmperes.setBorder(BorderFactory.createLineBorder(Color.RED));
                }
                if (pbAmps >= -4_000 && pbAmps < -3_400) {//azot
//            if (pbAmps >= -50 && pbAmps < 299) {//luga, podolsk, cmk
                    pbCmdAmperes.setValue(0);
                    pbCmdAmperes.setString("0.0 А");
                    pbCmdAmperes.setBorder(BorderFactory.createLineBorder(new Color(0, 140, 0), 2));
                }
                if (pbAmps >= -3_400 && pbAmps < 24_649) {//azot
//            if (pbAmps >= 300 && pbAmps < 27649) {//luga, podolsk, cmk
                    float k = pbCmdAmperes.getMaximum() / 27647f;
                    k = k * amplification / 10;
                    double amper = ((pbAmps + drift_zero) * k);
                    int finAmper = Math.round((float) (Math.log10(amper + 1) * stepscale));
                    pbCmdAmperes.setValue(finAmper);// Math.round((pbAmps + 3400) * k));
                    DecimalFormat df = new DecimalFormat("###.##");
                    String s = df.format((pbAmps + drift_zero) * k) + " А";
                    pbCmdAmperes.setString(s);
                    pbCmdAmperes.setBorder(BorderFactory.createLineBorder(new Color(0, 140, 0), 2));
                    pbCmdAmperes.repaint();
                }
//            if (pbAmps >= 24_649 && pbAmps <= 32_767) {//27649 - 32767
                if (pbAmps >= 27649 && pbAmps <= 32767) {//27649 - 32767
                    pbCmdAmperes.setValue(pbCmdAmperes.getMaximum());
                    pbCmdAmperes.setString("Перегрузка (плюс)");
                    pbCmdAmperes.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                }
            } else if (AMPERS > 0) {
                pbCmdAmperes.setValue(pbCmdAmperes.getMaximum());
                pbCmdAmperes.setString("Перевод стрелки");
            } else {
                pbCmdAmperes.setValue(0);
                pbCmdAmperes.setString("");
            }

            /*
         * 1 FEEDER_UP Фидер 1 в норме MESS_AREA_F1_NORMA 169 0 FEEDER_DOWN
         * Напряжение на фидере 1 отсутсвует MESS_AREA_F1_FAULT 168
             */
            if (w_1fider) {
                if (firstFeederUp != FIRST_FEEDER_UP) {
                    firstFeederUp = FIRST_FEEDER_UP;
                    setFirstFeederUp(FIRST_FEEDER_UP);
                    if (FIRST_FEEDER_UP) {// 1 FEEDER_UP Фидер 1 в норме MESS_AREA_F1_NORMA 169
                        event(169);
                        alarm_off(107);// 1
                    } else {
                        event(168);// 0 FEEDER_DOWN Напряжение на фидере 1 отсутсвует MESS_AREA_F1_FAULT 168
                        alarm_on(107);// 0
                    }
                }

                /*
             * 0 INACTIVE Фидер 1 не активен (none) 1 ACTIVE Фидер 1 активен
             * MESS_AREA_F1_ACTIVE 167
                 */
                if (firstFeederActive != FIRST_FIDER_ACTIVE) {
                    firstFeederActive = FIRST_FIDER_ACTIVE;
                    setFirstFeederActive(FIRST_FIDER_ACTIVE);
                    if (FIRST_FIDER_ACTIVE) {// 1 ACTIVE Фидер 1 активен
                        // MESS_AREA_F1_ACTIVE 167
                        event(167);
                    }
                }
            }// end if w1fider

            /*
         * 1 FEEDER_UP Фидер 2 в норме MESS_AREA_F2_NORMA 172 0 FEEDER_DOWN
         * Напряжение на фидере 2 отсутсвует MESS_AREA_F2_FAULT 171
         * secondFeederUp; boolean secondFeederActive
             */
            if (w_2fider) {
                if (secondFeederUp != SECOND_FIDER_UP) {
                    secondFeederUp = SECOND_FIDER_UP;
                    setSecondFeederUp(SECOND_FIDER_UP);
                    if (SECOND_FIDER_UP) {
                        event(172);
                        alarm_off(108);// 1
                    } else {// 0 FEEDER_DOWN Напряжение на фидере 2 отсутсвует
                        // MESS_AREA_F2_FAULT 171
                        event(171);
                        alarm_on(108);// 0
                    }
                }
                /*
             * 0 INACTIVE Фидер 2 не активен (none) 1 ACTIVE Фидер 2 активен
             * MESS_AREA_F2_ACTIVE 170
                 */
                if (secondFeederActive != SECOND_FIDER_ACTRIVE) {
                    secondFeederActive = SECOND_FIDER_ACTRIVE;
                    setSecondFeederActive(SECOND_FIDER_ACTRIVE);
                    if (SECOND_FIDER_ACTRIVE) {// 1 ACTIVE Фидер 2 активен
                        // MESS_AREA_F2_ACTIVE 170
                        event(170);
                    }
                }
            }// end if w2fider
            /*
         * 1 FEEDER_UP Фидер 3 в норме MESS_AREA_F3_NORMA 175 0 FEEDER_DOWN
         * Напряжение на фидере 3 отсутсвует MESS_AREA_F3_FAULT 174
             */
            if (w_3fider) {
                if (thirdFeederUp != THIRD_FIDER_UP) {
                    thirdFeederUp = THIRD_FIDER_UP;
                    setThirdFeederUp(THIRD_FIDER_UP);
                    if (THIRD_FIDER_UP) {// 1 FEEDER_UP Фидер 3 в норме
                        // MESS_AREA_F3_NORMA 175
                        event(175);
                        alarm_off(109);// 1
                    } else {// 0 FEEDER_DOWN Напряжение на фидере 3 отсутсвует
                        // MESS_AREA_F3_FAULT 174
                        event(174);
                        alarm_on(109);// 0
                    }
                }
                /*
             * 0 INACTIVE Фидер 3 не активен (none) 1 ACTIVE Фидер 3 активен
             * MESS_AREA_F3_ACTIVE 173
                 */
                if (thirdFeederActive != THIRD_FIDER_ACTIVE) {
                    thirdFeederActive = THIRD_FIDER_ACTIVE;
                    setThirdFeederActive(THIRD_FIDER_ACTIVE);
                    if (THIRD_FIDER_ACTIVE) {// 1 ACTIVE Фидер 3 активен
                        // MESS_AREA_F3_ACTIVE 173
                        event(173);
                    }
                }
            }// end if w3fider
            /*
         * 1 FUSE_UP Автоматы защиты в норме MESS_AREA_FUSE_UP - 0 FUSE_DOWN
         * Сработали автоматы защиты MESS_AREA_FUSE_DOWN 166
             */
            if (w_fuse_ctrl) {
                if (fuseControlUp != FUSEUP) {
                    fuseControlUp = FUSEUP;
                    setFuseControlUp(FUSEUP);
                    if (FUSEUP) {// 1 FUSE_UP Автоматы защиты в норме
                        // MESS_AREA_FUSE_UP -
                        alarm_off(106);// 1
                    } else {// 0 FUSE_DOWN Сработали автоматы защиты
                        // MESS_AREA_FUSE_DOWN 166
                        event(166);
                        alarm_on(106);// 0

                    }
                }
            }// end if w_fuse_ctrl
            /*
         * 1 GROUNDING_DOWN Страбатывание сигнализаторов заземления
         * MESS_AREA_GND_DOWN 176 0 GROUNDING_UP Сигнализаторы заземления в
         * норме MESS_AREA_GND_UP -
             */
            if (w_ground_ctrl) {
                if (groundingControlUp != GROUNDIGNGUP) {
                    groundingControlUp = GROUNDIGNGUP;
                    setGroundingControlUp(GROUNDIGNGUP);
                    if (GROUNDIGNGUP) {// 1 GROUNDING_DOWN Страбатывание
                        // сигнализаторов заземления
                        // MESS_AREA_GND_DOWN 176
                        event(176);
                        alarm_on(110);// 1
                    } else {// 0 GROUNDING_UP Сигнализаторы заземления в норме
                        // MESS_AREA_GND_UP -
                        alarm_off(110);// 0
                    }
                }
            }// end if w_grounf_ctrl
            /*
         * 1 DC_UP Напряжение =220В MESS_AREA_DCH_UP 159 0 DC_DOWN Отсутствует
         * напряжение =220В MESS_AREA_DCH_DOWN 158
             */
            if (w_lowvc) {
                if (DCHIGH) {
                    if (DCHIGH) {// 1 DC_UP Напряжение =220В MESS_AREA_DCH_UP 159
                        event(159);
                    } else {// 0 DC_DOWN Отсутствует напряжение =220В
                        // MESS_AREA_DCH_DOWN 158
                        event(158);
                        alarm_on(102);// 0
                    }
                }
                /*
             * 1 DC_UP Напряжение =24В MESS_AREA_DCL1_UP 161 0 DC_DOWN
             * Отсутствует напряжение =24В MESS_AREA_DCL1_DOWN 160
                 */
                if (lowVoltageUp != DCLOW1) {
                    lowVoltageUp = DCLOW1;
                    setLowVoltageUp(DCLOW1);
                    if (DCLOW1) {// 1 DC_UP Напряжение =24В MESS_AREA_DCL1_UP 161
                        event(161);
                    } else {// 0 DC_DOWN Отсутствует напряжение =24В
                        // MESS_AREA_DCL1_DOWN 160
                        event(160);
                        alarm_on(103);// 0
                    }
                }

                /*
             * 0 DC_DOWN Отсутствует напряжение =12В MESS_AREA_DCL2_DOWN 1 DC_UP
             * Напряжение =12В MESS_AREA_DCL2_UP
                 */
                if (DCLOW2) {
                    if (!DCLOW2) {
                        alarm_on(104);// 0
                    }
                }
                /*
             * 0 DC_DOWN Отсутствует напряжение=36В MESS_AREA_DCL3_DOWN 1 DC_UP
             * Напряжение =36В MESS_AREA_DCL3_UP
                 */
                if (!DCLOW3) {
                    if (DCLOW3) {
                        alarm_on(105);// 0
                    }
                }
            }// end if w_lowvc
            /*
         * 0 NORMAL Нагрузка на UPS в норме MESS_VOLL_CONT_UPS_OVL_OFF 332 1
         * OVERLOAD UPS перегружен MESS_VOLL_CONT_UPS_OVL_ON 333
             */
            if (w_ups) {
                if (overload != OVERLOAD) {
                    overload = OVERLOAD;
                    if (OVERLOAD) {// 1 OVERLOAD UPS перегружен
                        // MESS_VOLL_CONT_UPS_OVL_ON 333
                        lblUpsOverload.setIcon(upsOverload);
                        event(333);
                        alarm_on(149);// 1
                    } else {// 0 NORMAL Нагрузка на UPS в норме
                        // MESS_VOLL_CONT_UPS_OVL_OFF 332
                        lblUpsOverload.setIcon(no_overload);
                        event(332);
                        alarm_off(149);// 0
                    }
                }
                /*
             * 1 COMMON_ERROR Неисправность UPS MESS_VOLL_CONT_UPS_ERR_ON 329 0
             * NORMAL UPS в норме MESS_VOLL_CONT_UPS_ERR_OFF 328
                 */
                if (common_err != COMMON_ERR) {
                    common_err = COMMON_ERR;
                    if (COMMON_ERR) {
                        lblUpsError.setIcon(error);
                        event(329);
                        alarm_on(147);// 1
                    } else {
                        lblUpsError.setIcon(no_error);
                        event(328);
                        alarm_off(147);// 0
                    }
                }
                /*
             * 1 DISCHARGING UPS работает от батореи MESS_VOLL_CONT_UPS_ON_BATT
             * 331 0 NORMAL UPS работает от сети MESS_VOLL_CONT_UPS_OFF_BATT 330
                 */
                if (discharging != DISCHARGING) {
                    discharging = DISCHARGING;
                    if (DISCHARGING) {// 1 DISCHARGING UPS работает от батореи
                        // MESS_VOLL_CONT_UPS_ON_BATT 331
                        event(331);
                        alarm_on(148);// 1
                    } else {// 0 NORMAL UPS работает от сети
                        // MESS_VOLL_CONT_UPS_OFF_BATT 330
                        event(330);
                        alarm_off(148);// 0
                    }
                }
                /*
             * 1 FULL_DISCHARGE Батарея UPS разряжена
             * MESS_VOLL_CONT_UPS_BATT_LOW_ON 327 0 NORMAL Батарея UPS в норме
             * MESS_VOLL_CONT_UPS_BATT_LOW_OFF 326
                 */
                if (full_discharge != FULL_DISCHARGE) {
                    full_discharge = FULL_DISCHARGE;
                    if (FULL_DISCHARGE) {// 1 FULL_DISCHARGE Батарея UPS разряжена
                        // MESS_VOLL_CONT_UPS_BATT_LOW_ON 327
                        event(327);
                        alarm_on(146);// 1
                    } else {// 0 NORMAL Батарея UPS в норме
                        // MESS_VOLL_CONT_UPS_BATT_LOW_OFF 326
                        event(326);
                        alarm_off(146);// 0
                    }
                }
            }// end w_ups
            /*
         * 1 FIRE Пожар MESS_VOLL_CONT_FIRE 324 0 NORMAL Пожара нет
         * MESS_VOLL_CONT_FIRE_OFF 325
             */
            if (w_fire) {
                if (fire != FIRE) {
                    fire = FIRE;
                    if (FIRE) {// true 1 FIRE Пожар MESS_VOLL_CONT_FIRE
                        lblFireAlarm.setIcon(fireAlarm);
                        event(324);
                        alarm_on(145);// 1
                    } else {// false 0 NORMAL Пожара нет MESS_VOLL_CONT_FIRE_OFF
                        lblFireAlarm.setIcon(no_fire);
                        event(325);
                        alarm_off(145);// 0
                    }
                }
            }// end w_fire
            /*
         * 1 BREAK Взлом MESS_VOLL_CONT_ACCESS 322 0 NORMAL Взлома нет
         * MESS_VOLL_CONT_ACCESS_OFF 323
             */
            if (w_access) {
                if (access != ACCESS) {
                    access = ACCESS;
                    if (ACCESS) {// 1 BREAK Взлом MESS_VOLL_CONT_ACCESS 322
                        lblDoorLocked.setIcon(unlocked);
                        event(322);
                        alarm_on(144);// 1
                    } else {// 0 NORMAL Взлома нет MESS_VOLL_CONT_ACCESS_OFF 323
                        lblDoorLocked.setIcon(locked);
                        event(323);
                        alarm_off(144);// 0
                    }
                }
            }// end w_acces
            /*
         * 0 OGSP_ON Неисправность схемы ОГСП (сообщения почему-то нету) 1
         * OGSP_OFF Схема ОГСП в норме (сообщения почему-то нету)
             */
            if (w_ogsp) {
                if (ogsp != OGSP) {
                    ogsp = OGSP;
                    if (!OGSP) {// 1 OGSP_OFF Схема ОГСП в норме (сообщения почему-то
                        alarm_on(143);
                    }
                }
            }
            // ==============================================================================
            if ((!DISCHARGING) && (!FULL_DISCHARGE)) {// false && false
                lblUpsBattery.setIcon(ups_ac_full);
            }
            if ((!DISCHARGING) && (FULL_DISCHARGE)) {// false && true
                lblUpsBattery.setIcon(ups_ac_empty);
            }
            if ((DISCHARGING) && (!FULL_DISCHARGE)) {// true && false
                lblUpsBattery.setIcon(ups_half);
            }
            if ((DISCHARGING) && (FULL_DISCHARGE)) {// true && true
                lblUpsBattery.setIcon(ups_empty);
            }
            //------------------------------------------------------------------------------
//        if (w_1fider && w_2fider) {
//            if (!firstFeederUp&&!secondFeederUp) {
////                if (!blinkTimer1.isRunning()) {
////                    blinkTimer1.start();
////                }
//                Terminal.TIMER600.addActionListener(VC1);
//            } else {
////                blinkTimer1.stop();
//                Terminal.TIMER600.removeActionListener(VC1);
//                setFirstFeederUp(FIRST_FEEDER_UP);
//                setSecondFeederUp(SECOND_FIDER_UP);
////                lblCmdSecondFeeder.setBackground(Color.GRAY);
//            }
//        }
//------------------------------------------------------------------------------
        }
    }

    private void setDefaultState() {
        setState(0, // time
                0, // 0 amperes,
                true, // 1 "FEEDER_UP",//firstFeederUp,
                true, // 1 "ACTIVE",//firstFeederActive,
                true, // 1 "FEEDER_UP",//secondFeederUp,
                false, // 0 "INACTIVE",//secondFeederActive,
                false,// 0 "FEEDER_DOWN",//thirdFeederUp,
                false,// 0 "INACTIVE",//thirdFeederActive,
                true, // 1 "FUSE_UP",//fuseControlUp,
                false,// 0 "GROUNDING_UP",//groundingControlUp,
                true, // 1 "DC_UP",//dcHigh,
                true, // 1 "DC_UP",//lowVoltageUp,
                true, // 1 "DC_UP",//dcLow2,
                true, // 1 "DC_UP",//dcLow3,
                false,// аналоговый или цифровой амперметр у стрелки
                false,// 0 "NORMAL",//overload,
                false,// 0 "NORMAL",//discharging,discharging
                false,// 0 "NORMAL",//fullDischarge,
                false,// 0 "NORMAL",//commonError,
                false,// 0 "NORMAL",//fire,
                false,// 0 "NORMAL",//access,
                false // 0 "",//OGSP
        );
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
        VoltageControlState v = (VoltageControlState) oid;
        this.setState(v.timestamp, v.AMPERS,// int ampers,
                v.FIRST_FEEDER_UP,// boolean firstFeederUp,
                v.FIRST_FIDER_ACTIVE,// boolean firstFeederActive,
                v.SECOND_FIDER_UP,// boolean secondFeederUp,
                v.SECOND_FIDER_ACTRIVE,// boolean secondFeederActive,
                v.THIRD_FIDER_UP,// boolean thirdFeederUp,
                v.THIRD_FIDER_ACTIVE,// boolean thirdFeederActive,
                v.FUSEUP,// boolean fuseControlUp,
                v.GROUNDIGNGUP,// boolean groundingControlUp,
                v.DCHIGH,// dcHigh
                v.DCLOW1,// boolean lowVoltageUp,
                v.DCLOW2,// dcLow2
                v.DCLOW3,// dcLow3
                v.CUR_DIG,// аналоговый или цифровой амперметр у стрелки
                v.OVERLOAD, v.DISCHARGING,// discharging
                v.FULL_DISCHARGE,// fullDischarge
                v.COMMON_ERR, v.FIRE, v.ACCESS, v.OGSP
        );
    }
}

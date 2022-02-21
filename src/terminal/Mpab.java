//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
// Кнопки 5 штук: (TRUE - нажата, FALSE - )
// 1. DSO  - Дача согласия
// 2. ODSO - Отмена согласия
// 3. OX   - Отправка хозяйственного поезда
// 4. IFP  - Фиктивное прибытие
// 5. IV   - Исскуственное восстановление
// new Sound(99).start();//99-close sound
package terminal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
//import javax.swing.Timer;
import ru.attrans.proc.objs.MpabState;
import static terminal.Commander.cmdLayers;

class Mpab extends Cell {

    private java.awt.Color BackgroundColor = new java.awt.Color(221, 237, 252);//new java.awt.Color(140, 255, 100, 80);
    private javax.swing.border.Border cmdAreaNameBorder2 = javax.swing.BorderFactory.createLineBorder(new Color(150, 180, 210));
    
    private final Img lblKeyOn = new Img(Terminal.mainPictureHash.get("key-on"));
    private final Img lblKeyOff = new Img(Terminal.mainPictureHash.get("key-off"));
    // кнопки
    private javax.swing.JButton btnDSO = new javax.swing.JButton("ДСО");
    private javax.swing.JButton btnODSO = new javax.swing.JButton("ОДСО");
    private javax.swing.JButton btnOX = new javax.swing.JButton("ОХП");
    private javax.swing.JButton btnIFP = new javax.swing.JButton("ИФП");
    private javax.swing.JButton btnIV = new javax.swing.JButton("ИВ");
    private javax.swing.JButton btnDP = new javax.swing.JButton("ДП");
    private javax.swing.JButton btnKZ = new javax.swing.JButton(Terminal.mainPictureHash.get("key-off"));
//    private javax.swing.JToggleButton btnKZ = new javax.swing.JToggleButton(Terminal.mainPictureHash.get("key"));
//    private java.awt.Color cmdBtnIdleBckColor;
    void SetCmd(boolean STATUS) {
        //нужно убрать static
        // запретим или разрешим все кнопки при изменении переменной когда связь пропадает появляется.
        boolean status = (STATUS & Terminal.DSP && Area.Manager_PC);
        btnDSO.setEnabled(status);
        btnODSO.setEnabled(status);
        btnOX.setEnabled(status);
        btnIFP.setEnabled(status);
        btnIV.setEnabled(status);
        btnDP.setEnabled(status);
        btnKZ.setEnabled(status);
    }

    private final javax.swing.JLayeredPane cmdCellPane = new javax.swing.JLayeredPane();

//    private final String s_name;
    // метки, индикаторы
    private final javax.swing.JLabel lblName = new javax.swing.JLabel();
    private final javax.swing.JLabel lblDSO = new javax.swing.JLabel("<html>ДСО</html>");//дача согласия на отправление 1
    private final javax.swing.JLabel lblPS = new javax.swing.JLabel("ПС");//получение согласия 2
    private final javax.swing.JLabel lblPP = new javax.swing.JLabel("ПП");//путевой прием 3
    private final javax.swing.JLabel lblPO = new javax.swing.JLabel("ПО");//путевое отправление 4
    private final javax.swing.JLabel lblKZ = new javax.swing.JLabel("КЖ");//ключ-жезл 5
    private final javax.swing.JLabel lblKP = new javax.swing.JLabel("КП");//контроль перегона 6
    private final javax.swing.JLabel lblKI = new javax.swing.JLabel("КИ");//контроль исправности 7
    private final javax.swing.JLabel lblOK = new javax.swing.JLabel("ОК");//основной комплект (МПАБ-Р)
    private final javax.swing.JLabel lblRK = new javax.swing.JLabel("РК");//резервный комплект (МПАБ-Р)
    private final javax.swing.JLabel lblRN = new javax.swing.JLabel("Н");//Неисправность 8 (МПАБ-Р)
    // Панельки
    private final javax.swing.JPanel pnlName = new javax.swing.JPanel();
    private final javax.swing.JPanel pnlInd = new javax.swing.JPanel();
    private final javax.swing.JPanel pnlCmd = new javax.swing.JPanel();

    private final int mpab_r;//простой МПАБ или МПАБ_Р или Содовый или НАШ МПАБ-> это содовй 
    private boolean blink1;
    private boolean blink2;

    boolean _00;
    private boolean _01;
    private boolean _02;
    private boolean _03;
    private boolean _04 = true;
    private boolean _05 = true;
    private boolean _06;
    // --Commented out by Inspection (16.01.18 15:00):private boolean _07;
    private boolean _10;
    private boolean _11;
    private boolean _12 = true;//RN
    private boolean _13;
    private boolean _14;
    private boolean _15;
    private boolean _16;
    private boolean _17;
    private boolean _20;
    private boolean _21;
    private boolean _22;
    private boolean _23;
    private boolean _24;
    private boolean _25;
    private boolean _26;
    private boolean _27;
    private boolean _30;
    private boolean _31;
    private boolean _32;
    private final ActionListener MPAB1 = (ActionEvent evt) -> {
        blink1 = !blink1;
        if (blink1) {
            lblKZ.setBackground(Color.YELLOW);
        } else {
            lblKZ.setBackground(Color.LIGHT_GRAY);//мигание (ключ жезл изъят) RED
        }
    };
//    private final Timer blinkTimer1 = new javax.swing.Timer(600, blinkPerformer1);
    private final ActionListener MPAB2 = (ActionEvent evt) -> {
        blink2 = !blink2;
        if (blink2) {
            lblRN.setBackground(Color.YELLOW);
        } else {
            lblRN.setBackground(Color.LIGHT_GRAY);//мигание
        }
    };
//    private final Timer blinkTimer2 = new javax.swing.Timer(600, blinkPerformer2);

    Mpab(
            int ID_OBJ, //0=ID_OBJ
    //        int ID_AREA, //1=ID_AREA
            String S_NAME, //2=S_NAME
    //        String MPC_NAME, //3=MPC_NAME
            int X, //4=X
            int Y, //5=Y
            int SHIFT_X, //6=SHIFT_X
            int SHIFT_Y, //7=SHIFT_Y
            int VIDEO_STATUS, //8=VIDEO_STATUS
            int MPAB_R //9=MPAB_R
    //        int IND, //10=IND
    //        int CMD //11=CMD
    ) {
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 12);// 12=MPAB
        // -------------------------------Translate--------------------------------------

        mpab_r = MPAB_R;//2=mpab soda_new, bool (true МПАБ-Р, false-MPAB)
        setPanes();
        setDefaultState();
        SetCmd(true);
    }


    private void setPanes() { //IF (DSP) -> слушатель кнопок
        if(vStatus==0){
        int lblW = 34, lblH = 20;//40,22
        int btnW = 60, btnH = 28;//40,22
//        int cmdH = 64;
//        int cmdW = 300;
        int cmdH = 80;
        int cmdW = 310;
        if (mpab_r == 3) {
        cmdW += 50;
        }
        //-----------------------------Рамка------------------------------------
        cmdCellPane.setSize(cmdW, cmdH);
        cmdCellPane.setLocation(cmdX, cmdY);
//        cmdCellPane.setBackground(cmdNameBackgroundColor);
//        cmdCellPane.setBackground(cmdAreaNameBackgroundColor);
        cmdCellPane.setBorder(cmdAreaNameBorder2);
//        cmdCellPane.setOpaque(true);
//        cmdCellPane.setLayout(null);//1-столбик, 3-строчки (сюда добавим панельки)
        cmdCellPane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
        setCmdLayers();
        cmdCellPane.setVisible(true);//VIDEO_STATUS
        //============================Панельки==================================
        //----------------------------------------------------------------------
        if (mpab_r==1) {//2=mpab soda_new, bool (true МПАБ-Р, false-MPAB)
            pnlName.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 2));
        } else {
            pnlName.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 2));
        }

        pnlName.setBackground(BackgroundColor);
//            pnlName.setOpaque(true);
//            pnlName.setPreferredSize(new Dimension(200, 24));
//            pnlName.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        cmdCellPane.add(pnlName);
        cmdCellPane.setLayer(pnlName, 2);
        //----------------------------------------------------------------------
//            pnlCmd.setBorder(BorderFactory.createEtchedBorder());
//            pnlCmd.setLayout(new java.awt.GridLayout(1, 1, 0, 0));
        pnlCmd.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 1, 0));
        pnlCmd.setBackground(BackgroundColor);
//            pnlCmd.setOpaque(true);
        cmdCellPane.add(pnlCmd);
        cmdCellPane.setLayer(pnlCmd, 2);
        //----------------------------------------------------------------------
//            pnlInd.setBorder(BorderFactory.createEtchedBorder());
//            pnlInd.setLayout(new java.awt.GridLayout(1, 1, 0, 0));
        pnlInd.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 7, 2));
        pnlInd.setBackground(BackgroundColor);
//            pnlInd.setOpaque(true);
        cmdCellPane.add(pnlInd);
        cmdCellPane.setLayer(pnlInd, 2);

        //------------------------------Имя-------------------------------------
        lblName.setText(s_name + "   ");
        lblName.setFont(Terminal.SANS14);
//        lblName.setBounds(120, 4, 60, 16);
//        lblName.setBackground(Color.BLUE);
//        lblName.setOpaque(true);
//        cmdCellPane.add(lblName);
//        cmdCellPane.setLayer(lblName, 2);
        lblName.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblName.setVerticalAlignment(javax.swing.JLabel.CENTER);
        pnlName.add(lblName);
        //============================Индикация=================================
        //----------------------------------------------------------------------
        lblDSO.setOpaque(true);
        lblDSO.setBorder(BorderFactory.createEtchedBorder());
        lblDSO.setFont(Terminal.SANS12);
        lblDSO.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblDSO.setPreferredSize(new Dimension(lblW, lblH));
        lblDSO.setToolTipText("дача согласия на отправление");
        pnlInd.add(lblDSO);
        //----------------------------------------------------------------------
        lblPS.setOpaque(true);
        lblPS.setBorder(BorderFactory.createEtchedBorder());
        lblPS.setFont(Terminal.SANS12);
        lblPS.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblPS.setPreferredSize(new Dimension(lblW, lblH));
        lblPS.setToolTipText("получение согласия");
        pnlInd.add(lblPS);
        //----------------------------------------------------------------------
        lblPP.setOpaque(true);
        lblPP.setBorder(BorderFactory.createEtchedBorder());
        lblPP.setFont(Terminal.SANS12);
        lblPP.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblPP.setPreferredSize(new Dimension(lblW, lblH));
        lblPP.setToolTipText("путевой прием");
        pnlInd.add(lblPP);
        //----------------------------------------------------------------------
        lblPO.setOpaque(true);
        lblPO.setFont(Terminal.SANS12);
        lblPO.setBorder(BorderFactory.createEtchedBorder());
        lblPO.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblPO.setPreferredSize(new Dimension(lblW, lblH));
        lblPO.setToolTipText("путевое отправление");
        pnlInd.add(lblPO);
        //---------------------------ключ-жезл (мигает)-------------------------
        lblKZ.setOpaque(true);
        lblKZ.setFont(Terminal.SANS12);
        lblKZ.setBorder(BorderFactory.createEtchedBorder());
        lblKZ.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblKZ.setPreferredSize(new Dimension(lblW, lblH));
        lblKZ.setToolTipText("ключ-жезл");
        pnlInd.add(lblKZ);
        //----------------------------------------------------------------------
        if (mpab_r!=2) {//2=mpab soda_new, bool (true МПАБ-Р, false-MPAB)
        lblKP.setOpaque(true);
        lblKP.setFont(Terminal.SANS12);
        lblKP.setBorder(BorderFactory.createEtchedBorder());
        lblKP.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblKP.setPreferredSize(new Dimension(lblW, lblH));
        lblKP.setBackground(Color.RED);//default
        lblKP.setToolTipText("контроль перегона");
        pnlInd.add(lblKP);
        //----------------------------------------------------------------------
        lblKI.setOpaque(true);
        lblKI.setFont(Terminal.SANS12);
        lblKI.setBorder(BorderFactory.createEtchedBorder());
        lblKI.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblKI.setPreferredSize(new Dimension(lblW, lblH));
        lblKI.setToolTipText("контроль исправности");
        pnlInd.add(lblKI);
        }
        //----------------------------------------------------------------------
        if (mpab_r==1) {//2=mpab soda_new, bool (true МПАБ-Р, false-MPAB)
            lblOK.setOpaque(true);
            lblOK.setFont(Terminal.SANS12);
            lblOK.setBorder(BorderFactory.createEtchedBorder());
            lblOK.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            lblOK.setPreferredSize(new Dimension(lblW, lblH));
            lblOK.setToolTipText("основной комплект");
            pnlName.add(lblOK);
            //------------------------------------------------------------------
            lblRK.setOpaque(true);
            lblRK.setFont(Terminal.SANS12);
            lblRK.setBorder(BorderFactory.createEtchedBorder());
            lblRK.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            lblRK.setPreferredSize(new Dimension(lblW, lblH));
            lblRK.setToolTipText("резервный комплект");
            pnlName.add(lblRK);
            lblRK.setVisible(true);
            //------------------------------------------------------------------
            lblRN.setOpaque(true);
            lblRN.setFont(Terminal.SANS12);
            lblRN.setBorder(BorderFactory.createEtchedBorder());
            lblRN.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            lblRN.setPreferredSize(new Dimension(lblW, lblH));
            lblRN.setToolTipText("Неисправность");
            pnlName.add(lblRN);
        }
        //========================Кнопки управления=============================
//if (Terminal.DSP){ 
        //----------------------------------------------------------------------
        btnDSO.setToolTipText("Дача согласия");
        btnDSO.setFocusable(false);
        btnDSO.setFont(Terminal.SANS12);
//        btnDSO.setOpaque(true);
//        btnDSO.setBackground(Color.LIGHT_GRAY);
//        btnDSO.setBounds(4, 60, 40, 20);
        btnDSO.setPreferredSize(new Dimension(btnW, btnH));
//        btnDSO.setBorder(cmdAreaNameBorder2);
        btnDSO.addActionListener((java.awt.event.ActionEvent e) -> btnDSOMousePressed());
//        cmdCellPane.add(btnDSO);
//        cmdCellPane.setLayer(btnDSO, 2);
        pnlCmd.add(btnDSO);
        //----------------------------------------------------------------------
        btnODSO.setToolTipText("Отмена согласия");
        btnODSO.setFocusable(false);
        btnODSO.setFont(Terminal.SANS12);
//        btnODSO.setOpaque(true);
//        btnODSO.setBackground(Color.LIGHT_GRAY);
        btnODSO.addActionListener((java.awt.event.ActionEvent e) -> btnODSOMousePressed());
        pnlCmd.add(btnODSO);
        //--------------------Ответственная Команда-----------------------------
        if (mpab_r != 2) {
                btnIV.setToolTipText("Исскуственное восстановление");
                btnIV.setFocusable(false);
                btnIV.setFont(Terminal.SANS12);
//        btnIV.setOpaque(true);
//        btnIV.setBackground(Color.LIGHT_GRAY);
                btnIV.addActionListener((java.awt.event.ActionEvent e) -> btnIVPressed());
                pnlCmd.add(btnIV);
            }//end if mapb_r
        //----------------------------------------------------------------------
        btnOX.setToolTipText("Отправка хозяйственного поезда");
        btnOX.setFocusable(false);
        btnOX.setFont(Terminal.SANS12);
//        btnOX.setOpaque(true);
//        btnOX.setBackground(Color.LIGHT_GRAY);
        btnOX.addActionListener((java.awt.event.ActionEvent e) -> btnOXPressed());
        pnlCmd.add(btnOX);
        //--------------------Ответственная Команда-----------------------------
        btnIFP.setToolTipText("Искусственное фактическое прибытие");
        btnIFP.setFocusable(false);
        btnIFP.setFont(Terminal.SANS12);
//        btnIFP.setOpaque(true);
//        btnIFP.setBackground(Color.LIGHT_GRAY);
        btnIFP.addActionListener((java.awt.event.ActionEvent e) -> btnIFPMousePressed());
        pnlCmd.add(btnIFP);
        //----------------------------------------------------------------------
        if (mpab_r == 2) {
                btnDP.setToolTipText("Дача прибытия");
                btnDP.setFocusable(false);
                btnDP.setFont(Terminal.SANS12);
                btnDP.addActionListener((java.awt.event.ActionEvent e) -> btnDPMousePressed());
                pnlCmd.add(btnDP);
            }
        if (mpab_r == 3) {
//                btnDP.setToolTipText("Дача прибытия");
//                btnDP.setFocusable(false);
//                btnDP.setFont(Terminal.SANS12);
//                btnDP.addActionListener((java.awt.event.ActionEvent e) -> btnDPMousePressed());
//                pnlCmd.add(btnDP);
                //
                btnKZ.setPreferredSize(new java.awt.Dimension(50, 28));
                btnKZ.setToolTipText("Ключ жезл");
                btnKZ.setFocusable(false);
                btnKZ.setFont(Terminal.SANS12);
                btnKZ.addActionListener((java.awt.event.ActionEvent e) -> btnKZMousePressed());
                pnlCmd.add(btnKZ);
            }
//        cmdBtnIdleBckColor = btnODSO.getBackground();
//}//end if dsp
        }//end vStatus
    }

    private void setCmdLayers() {
        cmdLayers.add(cmdCellPane);
        cmdLayers.setLayer(cmdCellPane, CELLPANE_LAYER_10);//10
    }

    void setState(
            long DTIME, boolean _00, boolean _01, boolean _02, boolean _03, boolean _04, boolean _05, //(тоже самое что и _04)
            boolean _06,
//            boolean _07, //не используется т.к. - это инвертированое _06 (для WINCC)
            boolean _10, boolean _11, boolean _12, boolean _13, boolean _14, boolean _15, boolean _16, boolean _17,
            boolean _20, boolean _21, boolean _22, boolean _23, boolean _24, boolean _25, boolean _26, boolean _27
            , boolean _30
            , boolean _31
    ) {
        setDtime(DTIME);

        if (this._00 != _00) {//режим "путевой прием"
            this._00 = _00;
            if (_00) {
                lblPP.setBackground(Color.RED);
            } else {
                lblPP.setBackground(Color.LIGHT_GRAY);
            }
        }
        if (this._01 != _01) {//контроль посылки "дача согласия"
            this._01 = _01;
            if (_01) {
                lblDSO.setBackground(Color.YELLOW);
            } else {
                lblDSO.setBackground(Color.LIGHT_GRAY);
            }
        }
        if (this._02 != _02) {//режим "путевое отправление"
            this._02 = _02;
            if (_02) {
                lblPO.setBackground(Color.RED);
            } else {
                lblPO.setBackground(Color.LIGHT_GRAY);
            }
        }
        if (this._03 != _03) {//"получено согласие"
            this._03 = _03;
            if (_03) {
                lblPS.setBackground(Color.GREEN);
            } else {
                lblPS.setBackground(Color.LIGHT_GRAY);
            }
        }
        if (this._04 != _04) {
            //наличие ключа-жезла хозяйственного поезда
            this._04 = _04;
            if (_04) {
                event(112_041);
                alarm_off(212_041);
            } else {
                event(112_040);
                alarm_on(212_041);
            }
        }
        if (this._05 != _05) {
            //наличие ключа-жезла подталкивающего локомотива
            this._05 = _05;
            if (_05) {
                event(112_051);
                alarm_off(212_051);
            } else {
                event(112_050);
                alarm_on(212_051);
            }
        }
        if (_04 && _05) {
            Terminal.TIMER600.removeActionListener(MPAB1);
//            blinkTimer1.stop();
            lblKZ.setBackground(Color.YELLOW);
            lblKZ.setToolTipText("Ключ-жезл установлен");
            btnKZ.setIcon(lblKeyOn.getIcon());
        } else {
//            if (!blinkTimer1.isRunning()) {
//                blinkTimer1.start();
//            }
            Terminal.TIMER600.addActionListener(MPAB1);
            lblKZ.setToolTipText("Изъят ключ-жезл");
            btnKZ.setIcon(lblKeyOff.getIcon());
        }
        
        btnKZ.setSelected(_04 && _05);
        
        if (this._06 != _06) {
            //свободность перегона
            this._06 = _06;
            if (_06) {
                lblKP.setBackground(Color.WHITE);
                lblKP.setToolTipText("Перегон свободен");
                event(112_061);
            } else {
                lblKP.setBackground(Color.RED);
                lblKP.setToolTipText("Перегон занят");
                event(112_060);
            }
        }
//        if (this._07 != _07) {//занятость перегона 
//                this._07 = _07;
//                if (_07) {
////                    event(161);
//                } else {
////                    event(160);
////                    alarm_on(103);
//                }
//            }
        if (mpab_r==1) {
            if (this._10 != _10) {
                //работа на основном комплекте МПАБ-Р
                this._10 = _10;
                if (_10) {
                    lblOK.setBackground(Color.GREEN);
                    event(112_101);
                } else {
                    lblOK.setBackground(Color.LIGHT_GRAY);
                }
            }
            if (this._11 != _11) {
                //работа на резервном комплекте МПАБ-Р
                this._11 = _11;
                if (_11) {
                    lblRK.setBackground(Color.GREEN);
                    event(112_101);
                } else {
                    lblRK.setBackground(Color.LIGHT_GRAY);
                }
            }
            if (this._12 != _12) {
                //неисправность аппаратуры МПАБ-Р
                this._12 = _12;
                if (_12) {
//                    blinkTimer2.stop();
                    Terminal.TIMER600.removeActionListener(MPAB2);
                    lblRN.setBackground(Color.YELLOW);
                    event(112_121);
                    alarm_off(212_121);
                } else {
//                    if (!blinkTimer2.isRunning()) {
//                        blinkTimer2.start();
//                    }
                    Terminal.TIMER600.addActionListener(MPAB2);
                    event(112_120);
                    alarm_on(212_121);
                }
            }
        }//end if mpab_r==1
        if (this._13 != _13) {
            //отказ аппаратуры МПАБ или МПАБ-Р (оба комплекта)
            this._13 = _13;
            if (_13) {
                lblKI.setBackground(Color.LIGHT_GRAY);
                event(112_131);
                alarm_off(212_131);
            } else {
                lblKI.setBackground(Color.RED);
                event(112_131);
                alarm_on(212_131);
            }
        }
        if (this._14 != _14) {
            //сообщение запрет нажатия OXk из-за задан маршрут отправления (открыт вых. сигнал)
            this._14 = _14;
            if (_14) {
                event(112_141);
            }
        }
        if (this._15 != _15) {
            //сообщение запрет нажатия OXk из-за нет отправления
            this._15 = _15;
            if (_15) {
                event(112_151);
            }
        }
        if (this._16 != _16) {
            //сообщение запрет нажатия OXk из-за изъят ключ-жезл хоз.поезда
            this._16 = _16;
            if (_16) {
                event(112_161);
            }
        }
        if (this._17 != _17) {
            //сообщение запрет нажатия OXk из-за неисправности аппаратуры
            this._17 = _17;
            if (_17) {
                event(112_171);
            }
        }
        if (this._20 != _20) {
            this._20 = _20;
            if (_20) {
                btnDSO.setBackground(Color.GREEN);
//                    btnDSO.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 1));
                event(112_201);
            } else {
                btnDSO.setBackground(Color.LIGHT_GRAY);
//                    btnDSO.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2));
            }
        }
        if (this._21 != _21) {
            this._21 = _21;
            if (_21) {
                btnODSO.setBackground(Color.GREEN);
                event(112_211);
            } else {
                btnODSO.setBackground(Color.LIGHT_GRAY);
            }
        }
        if (this._22 != _22) {
            //????????????????????????????????????????????????
            this._22 = _22;
            if (_22) {
                btnOX.setBackground(Color.GREEN);
                event(112_221);
            } else {
                btnOX.setBackground(Color.LIGHT_GRAY);
            }
        }
        if (this._23 != _23) {
            this._23 = _23;
            if (_23) {
                btnOX.setBackground(Color.GREEN);
                event(112_231);
            } else {
                btnOX.setBackground(Color.LIGHT_GRAY);
//                    event(112230)???
            }
        }
        if (this._24 != _24) {
            this._24 = _24;
            if (_24) {
                btnIFP.setBackground(Color.GREEN);
                event(112_241);
            } else {
                btnIFP.setBackground(Color.LIGHT_GRAY);
            }
        }
        if (this._25 != _25) {
            this._25 = _25;
            if (_25) {
                btnIV.setBackground(Color.GREEN);
                event(112_251);
            } else {
                btnIV.setBackground(Color.LIGHT_GRAY);
            }
        }
        if (this._26 != _26) {
            //сообщение запрет нажатия DSOk из-за изъят ключ-жезл хоз.поезда или подт.локомотива
            this._26 = _26;
            if (_26) {
                event(112_261);
            }
        }
        if (this._27 != _27) {
            //сообщение запрет нажатия IVk из-за изъят ключ-жезл хоз.поезда или подт.локомотива
            this._27 = _27;
            if (_27) {
                event(112_271);
            }
        }
        if (this._30 != _30) {
            this._30 = _30;
            if (_30) {
                new Sound(99).start();
                //event(112_301);
            } else {
                //cmdCellPane.setBorder(cmdAreaNameBorder);
            }
        }
        if (this._31 != _31) {
            this._31 = _31;
            if (_31) {
                btnDP.setBackground(Color.GREEN);
                event(112_311);//Фактическое прибытие
            } else {
                btnDP.setBackground(Color.LIGHT_GRAY);
            }
        }
//        switch (DSO) {
//            case "PRESS_BTN":
//                btnIV.setBackground(Color.RED);
//                btnIV.setForeground(Color.WHITE);
//                break;
//            case "RELEASE_BTN":
//                btnIV.setBackground(cmdBtnIdleBckColor);
//                btnIV.setForeground(Color.BLACK);
//                break;
//        }
    }

    private void setDefaultState() {
        lblDSO.setBackground(Color.LIGHT_GRAY);
        lblPS.setBackground(Color.LIGHT_GRAY);
        lblPP.setBackground(Color.LIGHT_GRAY);
        lblPO.setBackground(Color.LIGHT_GRAY);
        lblKZ.setBackground(Color.YELLOW);//true
        lblKP.setBackground(Color.RED);//Default false
        lblKI.setBackground(Color.RED);//Default false
        lblOK.setBackground(Color.LIGHT_GRAY);
        lblRK.setBackground(Color.LIGHT_GRAY);
        lblRN.setBackground(Color.YELLOW);//true
    }

    // обработка кнопки DSO
    private void btnDSOMousePressed() {
        Net.sendMaskedCmd_DSP(id_obj, "MPAB_DSO_ON");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "MPAB_DSO_OFF");
    }

    // обработка кнопки ODSO
    private void btnODSOMousePressed() {
        Net.sendMaskedCmd_DSP(id_obj, "MPAB_ODSO_ON");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "MPAB_ODSO_OFF");
    }

    // обработка кнопки OX
    private void btnOXPressed() {
        Net.sendMaskedCmd_DSP(id_obj, "MPAB_OX_ON");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "MPAB_OX_OFF");
    }

    // обработка кнопки IFP (ответственная)
    private void btnIFPMousePressed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=5 style=bold> "
                + OPERATION
                + "!!</font><br>Подтвердите операцию\nИскусственного фактического\nприбытия поезда</font></html>")) {
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP(id_obj, "MPAB_IFP_ON");
                pause();
                Net.sendMaskedCmd_DSP(id_obj, "MPAB_IFP_OFF");
                break;
        }
    }

    // обработка кнопки IV (ответственная)
    private void btnIVPressed() {
        switch (Commander.customDialog2.showOptionDialog(cmdX, cmdY,
                CAUNTION + "! " + OPERATION,
                "<html><font color=red size=5 style=bold> "
                + OPERATION
                + "!!</font><br>Подтвердите операцию\nИскусственного востановления\nначального состояния</font></html>")) {
            case 0:
                break;
            case 1:
                Net.sendMaskedCmd_DSP(id_obj, "MPAB_IV_ON");
                pause();
                Net.sendMaskedCmd_DSP(id_obj, "MPAB_IV_OFF");
                break;
        }
    }
    
    // обработка кнопки DSO
    private void btnDPMousePressed() {
        Net.sendMaskedCmd_DSP(id_obj, "MPAB_DP_ON");
        pause();
        Net.sendMaskedCmd_DSP(id_obj, "MPAB_DP_OFF");
    }

    // обработка кнопки KZ ()
    private void btnKZMousePressed() {
        if (_04 && _05) {
            Net.sendMaskedCmd_DSP(id_obj, "MPAB_KZ_OFF");
        } else {
            Net.sendMaskedCmd_DSP(id_obj, "MPAB_KZ_ON");
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

    private void pause() {
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            Err.err(e);
        }
    }

    @Override
    public void setState(Object oid) {
        MpabState m = (MpabState) oid;
                setState(
                m.timestamp,
                m._00,
                m._01,
                m._02,
                m._03,
                m._04,
                m._05,
                m._06,
     //           m._07,//ключ жезл установлен, из
                m._10,
                m._11,
                m._12,
                m._13,
                m._14,
                m._15,
                m._16,
                m._17,
                m._20,
                m._21,
                m._22,
                m._23,
                m._24,
                m._25,
                m._26,
                m._27,
                m._30,//Звонок
                m._31 //раскраска ДП (белым)
                
        );   
    }
}

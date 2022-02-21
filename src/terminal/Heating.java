//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import ru.attrans.proc.objs.HeatingState;
import static terminal.Commander.cmdLayers;

class Heating extends Cell {

    private java.awt.Color cmdAreaNameBackgroundColor = new java.awt.Color(140, 255, 100, 80);
    private javax.swing.border.Border cmdAreaNameBorder2 = javax.swing.BorderFactory.createLineBorder(new Color(150, 180, 210));
    private javax.swing.JButton btn = new javax.swing.JButton("ВЫКЛ.");
    private final JLayeredPane cmdCellPane = new JLayeredPane();
    private boolean toggle;
    private boolean hot_all_on; //	Только для первого шкафа (1 - включен)
    private boolean hot_all_err; //	Только для первого шкафа (1 - неисправность схемы)
    private final javax.swing.JLabel lblName = new javax.swing.JLabel();
    private final JPanel H = new JPanel();
    
    void SetCmd(boolean STATUS) {//нужно убрать static
    // запретим или разрешим все кнопки при изменении переменной когда связь пропадает появляется.
        boolean status = (STATUS & Terminal.DSP && Area.Manager_PC);
        btn.setEnabled(status);
    }

    Heating(
            int ID_OBJ,
    //        int ID_AREA,
            String S_NAME,
            int X,
            int Y,
            int SHIFT_X,
            int SHIFT_Y,
            int VIDEO_STATUS
    ) {
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 83);//83 - HEATING
//        s_name = S_NAME;
        setPanes();
        plug_heat();
        SetDefaultState();
        boolean status = (true & Terminal.DSP && Area.Manager_PC);
        btn.setEnabled(status);
//        SetCmd(true);
    }

    private void setPanes() {
        int cmdH = 64;
        int cmdW = 280;

        cmdCellPane.setSize(cmdW, cmdH);
        cmdCellPane.setLocation(cmdX, cmdY);
        cmdCellPane.setBackground(cmdAreaNameBackgroundColor);
        cmdCellPane.setBorder(cmdAreaNameBorder2);
        cmdCellPane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 2, 2));

        setCmdLayers();
        cmdCellPane.setVisible(true);

        lblName.setText(s_name);

        lblName.setPreferredSize(new Dimension(cmdW, lblName.getFontMetrics(Terminal.SANS14).getHeight()));

        lblName.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lblName.setVerticalAlignment(javax.swing.JLabel.TOP);
        lblName.setFont(Terminal.SANS14);
        lblName.setVisible(true);
        cmdCellPane.add(lblName);
        cmdCellPane.setLayer(lblName, 2);

//        btn.setToolTipText("включть обогрев");
        btn.setFont(Terminal.SANS10);
        btn.setPreferredSize(new Dimension(60, 26));
//        btn.setBackground(Color.LIGHT_GRAY);
        btn.setFocusable(false);
//        btn.setOpaque(true); // чтобы не рисовала вокруг кнопки всякую фигю надо btn.setOpaque(false)

        btn.addActionListener((java.awt.event.ActionEvent e) -> btnMousePressed());

        cmdCellPane.add(btn);
        cmdCellPane.setLayer(btn, 2);

//        H.setPreferredSize(new Dimension(204, 32));
        H.setBorder(BorderFactory.createEtchedBorder());
//       H.setBackground(Color.GRAY);
        H.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 3, 2));
//        H.setOpaque(true);
        cmdCellPane.add(H);
        cmdCellPane.setLayer(H, 2);

    }

    private void setCmdLayers() {
        cmdLayers.add(cmdCellPane);
        cmdLayers.setLayer(cmdCellPane, CELLPANE_LAYER_10);//10
    }

    private void plug_heat() {
        //тут надо надобавлять квадратиков.
        Terminal.Heat_Hash.values().forEach(H::add);
    }

    private void SetDefaultState() {
        btn.setBackground(Color.LIGHT_GRAY);
        btn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2));//normal
    }

    // обработка кнопки
    private void btnMousePressed() {
        if (toggle) {
            Net.sendMaskedCmd_DSP(id_obj, "HEATING_ALL_OFF_1");
            pause();
            Net.sendMaskedCmd_DSP(id_obj, "HEATING_ALL_OFF_0");
        } else {
            Net.sendMaskedCmd_DSP(id_obj, "HEATING_ALL_ON_1");
            pause();
            Net.sendMaskedCmd_DSP(id_obj, "HEATING_ALL_ON_0");
        }
    }

    private void pause() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Err.err(e);
        }
    }

    void setState(long DTIME, boolean HOT_ALL_ON, boolean HOT_ALL_ERR) {
        setDtime(DTIME);
// --------------------------------------------------------------------
        if (hot_all_on != HOT_ALL_ON) {
            hot_all_on = HOT_ALL_ON;
            if (HOT_ALL_ON) {
                btn.setText("ВКЛ.");
                btn.setBackground(Color.GREEN);
                btn.setToolTipText("Обогрев стрелок включен");
//                btn.setSelected(true);
                toggle = true;
                event(365);//365	Обогрев стрелок включен	MESS_HEATING_ALL_ON	INFO_MESS
            } else {
                btn.setText("ВЫКЛ.");
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setToolTipText("Обогрев стрелок выключен");
//                btn.setSelected(false);
                toggle = false;
                event(367);//367	Обогрев стрелок выключен	MESS_HEATING_ALL_OFF	INFO_MESS
//                тут вроде должны все алармы сняться
            }
        }
// --------------------------------------------------------------------
        if (hot_all_err != HOT_ALL_ERR) {
            hot_all_err = HOT_ALL_ERR;
            if (HOT_ALL_ERR) {
//                btn.setForeground(Color.RED);
                btn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 51), 2));//red
                event(366);//366	Неисправность схемы управления обогрева стрелочных переводов	MESS_HEATING_ALL_ERR	INFO_MESS
                alarm_on();//369	Неисправность схемы управления обогревом стрелочных переводов	ALARM_HEATING_ALL_ERR	ALARM
            } else {
//                btn.setForeground(Color.GREEN);
                btn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2));//normal
                event(368);//368	Схема управления обогревом стрелочных переводов в норме	MESS_HEATING_ALL_NOERR	INFO_MESS     
                alarm_off();
            }
        }
    }

    private void event(int id_msg) {
        if (getDtime() > 0) {
            Events.InsertMessage(getDtime(), id_obj, id_msg);
        }
    }

    private void alarm_on() {
        if (getDtime() > 0) {
            Alarms.alarm_on(getDtime(), 369, id_obj);
        }
    }

    private void alarm_off() {
        if (getDtime() > 0) {
            Alarms.alarm_off(getDtime(), 369, id_obj);
        }
    }

    @Override
    public void setState(Object oid) {
        HeatingState h = (HeatingState) oid;
        this.setState(
                h.timestamp,
                h.HOT_ALL_ON,
                h.HOT_ALL_ERR
        );
    }
}

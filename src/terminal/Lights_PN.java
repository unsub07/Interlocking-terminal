//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
/*
 этот сетофор только в Усть-луге  
 */
package terminal;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import ru.attrans.proc.objs.LightsState_PN;
import static terminal.Commander.cmdLayers;

class Lights_PN extends Cell {
    // Lights layers
    private static final int LIGHTS_SOUL_LR = 1;
    private static final int LIGHTS_STAND_LR = 2;
    private static final int LIGHTS_LOWER_LIGHT_LR = 3;
    private static final int LIGHTS_CONTROL_BUTTON_LR = 8;
    private static final int LIGHTS_LAYER = 9;

    private static final javax.swing.ImageIcon yellowLight = Terminal.mainPictureHash.get("light_yellow");
    private static final javax.swing.ImageIcon faultYellowLight = Terminal.mainPictureHash.get("light_yellow_fault");
    private static final javax.swing.ImageIcon yellowBlinkLight = Terminal.mainPictureHash.get("light_yellow_blink");

    private static final javax.swing.ImageIcon greenLight = Terminal.mainPictureHash.get("light_green");
    private static final javax.swing.ImageIcon faultGreenLight = Terminal.mainPictureHash.get("light_green_fault");    
//    private static final javax.swing.ImageIcon greenBlinkLight = Terminal.mainPictureHash.get("light_green_blink");    
    
    private static final javax.swing.ImageIcon blackLight = Terminal.mainPictureHash.get("light_black");


    private static final Color idleBtnBackground = new Color(225, 225, 225);
    private static final Border idleBtnBorder = BorderFactory.createLineBorder(Color.BLACK, 2);

    private static final java.awt.Cursor lightsCursor = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(Terminal.mainPictureHash.get("lights_cursor").getImage(), new java.awt.Point(0, 0), "lights_cursor");
    private final javax.swing.JLayeredPane cmdCellPane = new javax.swing.JLayeredPane();

    private final javax.swing.JLabel lblCmdLowerLight = new javax.swing.JLabel();

    private int light = 0;

    Lights_PN(
            int ID_OBJ,
            String S_NAME,
            int X,
            int Y,
    //        int ORIENTATION,
            int SHIFT_X,
            int SHIFT_Y,
            int VIDEO_STATUS
            ) {
        super(ID_OBJ, S_NAME, X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 87);// 11=LIGHTS // 87=LIGHTS

        int cmdShift;

        JLabel lblCmdLightsName = new JLabel();
        lblCmdLightsName.setText(S_NAME);
        lblCmdLightsName.setOpaque(true);
        lblCmdLightsName.setBackground(idleBtnBackground);
        lblCmdLightsName.setBorder(idleBtnBorder);
        lblCmdLightsName.setFont(Terminal.SANS12);
        lblCmdLightsName.setHorizontalAlignment(JLabel.CENTER);
        lblCmdLightsName.setVerticalAlignment(JLabel.CENTER);

        if (Terminal.DSP) {// DSP && VIDEO_STATUS
            lblCmdLightsName.setCursor(lightsCursor);
        }// end DSP


        int cmdW = lblCmdLightsName.getFontMetrics(lblCmdLightsName.getFont()).stringWidth(lblCmdLightsName.getText()) + 4;
        if (cmdW <= 38) {
            cmdW = 38;
        }
        lblCmdLightsName.setSize(cmdW, 16);
        cmdShift = ((cmdW - 38) > 0) ? (cmdW - 38) : 0;


        // ---------------------------------------------------------------------

        JLabel lblCmdSoul = new JLabel();
        JLabel lblCmdStand = new JLabel();

        cmdCellPane.setSize(80 + cmdShift, 16);
        shift_x = 32;
        shift_y = 10;

        cmdCellPane.setLayer(cmdCellPane.add(lblCmdLightsName), LIGHTS_CONTROL_BUTTON_LR);

        lblCmdLightsName.setLocation(42, 0);

        lblCmdSoul.setIcon(Terminal.mainPictureHash.get("lights_soul_horiz"));

        lblCmdSoul.setSize(4, 16);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdSoul), LIGHTS_SOUL_LR);

        lblCmdSoul.setLocation(36, 0);

        lblCmdStand.setIcon(Terminal.mainPictureHash.get("lights_stand_horiz"));

        lblCmdStand.setSize(4, 16);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdStand), LIGHTS_STAND_LR);
        lblCmdStand.setLocation(32, 0);

        lblCmdLowerLight.setSize(16, 16);
        cmdCellPane.setLayer(cmdCellPane.add(lblCmdLowerLight),
                LIGHTS_LOWER_LIGHT_LR);
        lblCmdLowerLight.setLocation(16, 0);// 26

        // ------------------------------------------------------------------------------
        lblCmdSoul.setOpaque(false);
        lblCmdLowerLight.setOpaque(false);

        cmdX += shift_x;
        cmdY += shift_y;
        cmdCellPane.setLocation(cmdX, cmdY);
        setCmdLayers();
        cmdCellPane.setVisible(true);
        // ----------------------------Set Default1 State-----------------------
        lblCmdLowerLight.setIcon(faultYellowLight);
    }
    
    private void setCmdLayers() {
        cmdLayers.add(cmdCellPane);
        cmdLayers.setLayer(cmdCellPane, Lights_PN.LIGHTS_LAYER);
    }    

    private void setState(
            long DTIME,
            int LIGHT
    ) {
        setDtime(DTIME);

        if (light != LIGHT) {
            light = LIGHT;
            switch (LIGHT) {
                case 0:
                    //такого не может прислать контроллер
                    break;
                case 1://(ПН) Желтый огонь
                    lblCmdLowerLight.setIcon(yellowLight);
                    event(266);//Открыт: Желтый
                    alarm_off(389);//off 389	Перегорание лампы желтого огня	ALARM_LIGHT_YOR
                    alarm_off(390);//off 390	Перегорание лампы зеленого огня	ALARM_LIGHT_GOR
                    break;
                case 2://(ПН) Желтый мигающий огонь
                    lblCmdLowerLight.setIcon(yellowBlinkLight);
                    event(401);//Открыт: Желтый мигающий
                    alarm_off(347);//Неиспавность схемы мигания
                    break;
                case 4://(ПН) Желтый огонь, неиправность схемы мигания
                    lblCmdLowerLight.setIcon(yellowLight);
                    event(347);//Неиспавность схемы мигания
                    alarm_on(347);//Неиспавность схемы мигания
                    break;
                case 8://(ПН) Зеленый огонь
                    lblCmdLowerLight.setIcon(greenLight);
                    event(242);//Открыт. зеленый
                    alarm_off(391);//off 391	Перегорание основной нити желтого огня	ALARM_LIGHT_YOO
                    alarm_off(392);// off //392	Перегорание основной нити зеленого огня	ALARM_LIGHT_GOO
                    break;
                case 16://(ПН) Зеленый мигающий огонь
//нет картинки                    lblCmdLowerLight.setIcon(greenBlinkLight);
                    alarm_off(347);
                    break;
                case 32://(ПН) Зеленый огонь, неиправность схемы мигания
                    lblCmdLowerLight.setIcon(greenLight);
                    alarm_on(347);
                    break;
                    
                case 128://(ПН) Погасший, неисправность схемы мигания
                    lblCmdLowerLight.setIcon(blackLight);
                    event(347);//Неиспавность схемы мигания
                    alarm_on(347);//Неиспавность схемы мигания
                    break;
                    
                case 256://(ПН) перегорание (резервной нити) желтого огня (желтый не горит)
                    lblCmdLowerLight.setIcon(faultYellowLight);
                    event(393);//393	ОТКАЗ! Перегорание лампы желтого огня	MESS_LIGHT_YOR
                    alarm_on(389);//389	Перегорание лампы желтого огня	ALARM_LIGHT_YOR
                    break;
                case 512://(ПН) перегорание (резервной нити) зеленого огня (зеленый не горит)
                    lblCmdLowerLight.setIcon(faultGreenLight);
                    event(394);//394	ОТКАЗ! Перегорание лампы зеленого огня	MESS_LIGHT_GOR
                    alarm_on(390);//390	Перегорание лампы зеленого огня	ALARM_LIGHT_GOR
                    break;
                    
                case 2_048://(ПН) пперегорание (основной нити) желтого огня(желтый горит)
                    lblCmdLowerLight.setIcon(yellowLight);
                    event(395);//395	НЕИСПРАВНОСТЬ! Перегорание основной нити желтого огня	MESS_LIGHT_YOO
                    alarm_on(391);//391	Перегорание основной нити желтого огня	ALARM_LIGHT_YOO
                    break;
                case 4_096://(ПН) перегорание (основной нити) зеленого огня (зеленый горит)
                    lblCmdLowerLight.setIcon(greenLight);
                    event(396);//396	НЕИСПРАВНОСТЬ! Перегорание основной нити зеленого огня	MESS_LIGHT_GOO
                    alarm_on(392);//392	Перегорание основной нити зеленого огня	ALARM_LIGHT_GOO
                    break;                
                default:
                    System.out.println("LIGHT_PN light_new not found - такой цвет не найден " + LIGHT);
                    break;

            }
                Log.log("PN " + LIGHT);
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

    @Override
    public void setState(Object oid) {
        LightsState_PN l = (LightsState_PN) oid;
        this.setState(
                l.timestamp,
                l.LIGHT
        );
    }
}

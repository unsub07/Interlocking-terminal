//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

class SetState extends Thread {

    private static Long dtime;
    private static int id_obj;
    private static String new_val;
    private static String old_val;
    private static byte b[];
    private static byte bi[];
    private static int obj_type = 0;
    private static String dsp;
    private static String ip;
    private static String cmd;//(64.64)
    private static boolean init;

    static void s0(String line) {
        String[] s = line.split("\\ ");
        dtime = Clock.SetTime(s[0]);
        switch (s[1]) {
            case "CMD":// "NO_ROUTING"://0
//                Globals.CMD_TOTAL++;
                id_obj = Integer.valueOf(s[2]);
                cmd = s[3];
                ip = s[4];
                dsp = s[5];
//                          время  тип_объекта  имя_объекта
//                Alarm.InsertCmd(dtime, id_obj, cmd, dsp, ip);
                break;
            case "NOTE":
                break;
            default:
//                String txt = s[1] + " " + s[2] + " " + s[3];
//                Viewlog.HMAP.put(dtime, txt);
                id_obj = Integer.valueOf(s[1]);

                new_val = s[2];
                old_val = s[3];

                init = new_val == null ? old_val == null : new_val.equals(old_val);
                
                if (!init){
                
                if (Terminal.obj_type.containsKey(id_obj)) { //from id_obj get obj_type
                    obj_type = Terminal.obj_type.get(id_obj);
//------------------------------------------------------------------------------
                    String[] ba = new_val.split("\\.");

                    bi = new byte[ba.length];

                    //(b[2] & 0xFF)
                    for (int i = ba.length - 1; i >= 0; i--) {
                        int z = Integer.valueOf(ba[i]);
                        if (z > 127) {
                            bi[i] = (byte) (z - 256);
                        } else {
                            bi[i] = (byte) z;
                        }
                    }
                    int j = 0;
                    b = new byte[ba.length];
                    for (int i = ba.length - 1; i >= 0; i--) {
                        b[j] = bi[i];
                        j++;
                    }
                    s1();
//------------------------------------------------------------------------------
                } else {
                    Log.log("obj_type not contains id " + id_obj);
                }
        }//enf not init
        }//end case
    }//end s0

    static void s1() {
        switch (obj_type) {
            case 1:// "AREANAME":
                area();
                break;
            case 2:// "BEAM":
                break;
            case 3:// "CABINET":
                break;
            case 4:// "CHANNEL":
                break;
            case 5:// "COMM_SERVER":
                break;
            case 6:// "CONTROL_UNIT":
                break;
            case 7:// "COUNTER":
                counter();
                break;
            case 8:// "COUNT_UNIT":
                count_unit();
                break;
            case 9:// "CPU":
                plc();
                break;
            case 10:// "CROSSING":
                crossing();
                break;
            case 24:// "FAN":
                fan();
                break;
            case 11:// "LIGHTS":
                light();
                break;
            case 12:// "MPAB":
                mpab();
                break;
            case 22:// "NEARBY":
                nearbby();
                break;
//            case 13:// "NOTE":
//                note();
//                break;
            case 23:// "PROTECTED_ZONE":
                protect();
                break;
            case 14:// "RAILNET":
                railnet();
                break;
            case 15:// "SIMULATOR":
                break;
            case 16:// "TECHZONE":
                break;
            case 17:// "TURNOUT":
                turnout();
                break;
            case 18:// "UNIT":
                unit();
                break;
            case 19:// "VOLTAGE_CONTROL":
                vc();
                break;
            case 30:// "DIO_20/8":
                dio();
                break; 
            case 31://blokpost
                blokpost();
                break;
            case 80:// "DGA":
                dga();
                break;
            case 82:// "ELEVATEDTRACK":
                elevated();
                break;
            case 83:// "HEATING":
                heating();
                break;
            case 84:// "UKSPS":
                uksps();
                break;
            case 85:// "HEAT":
                heat();
                break;
//            case 86:// "LIGHTS_NEW":
//                light_new();
//                break;
//            case 87:// "LIGHTS_PN":
//                light_pn();
//                break;                                                                   
            default:
                Log.log("неизвестный тип объекта: " + obj_type);
                break;
        }
    }

    private static void area() {
        int HOLLOWS = ((b[3] << 8 & 0xFF00) | (b[2] & 0xFF));
        // 76543210
        int ROUTE = (b[4] & 0b0000_1111);

        boolean GROUP_CANCEL = (b[4] & 0b0001_0000) != 0x00;
        boolean DIAL_CANCEL = (b[4] & 0b0010_0000) != 0x00;
        boolean HW_ACKN_1 = (b[4] & 0b0100_0000) != 0x00;

        boolean HOLLOW_CANCEL = (b[6] & 0b0000_0001) != 0x00;
        boolean HOLLOW_CANCEL_BTN = (b[6] & 0b0000_0010) != 0x00;

        boolean HW_ACKN_2 = (b[6] & 0b0100_0000) != 0x00;
        boolean ACKN_2ERR = (b[6] & 0b1000_0000) != 0x00;

        Area o = Terminal.Area_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, HOLLOWS, ROUTE, GROUP_CANCEL, DIAL_CANCEL, HW_ACKN_1, ACKN_2ERR, HOLLOW_CANCEL, HOLLOW_CANCEL_BTN, HW_ACKN_2, ACKN_2ERR);
        } else {
            er(id_obj);
        }         
    }

    private static void counter() {
        int COMM_ERRORS = b[0];
        int COUNTER_STATE = b[1];
        int AXIS = (b[3] << 8 & 0xFF00) | (b[2] & 0xFF);
        int ERROR_CODE = (b[5] << 8 & 0xFF00) | (b[4] & 0xFF);
        int ADJ_PHASE = (b[7] << 8 & 0xFF00) | (b[6] & 0xFF);

        Counter o = Terminal.CounterCell_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, COUNTER_STATE, AXIS, COMM_ERRORS, ERROR_CODE, ADJ_PHASE);
        } else {
            er(id_obj);
        }         
    }

    private static void count_unit() {
        // 76543210
        boolean DIS_SVZ = (b[0] & 0b0000_0001) != 0x0;
        boolean ERR_SVZ = (b[0] & 0b0000_0010) != 0x0;
        boolean STP_CPUSO=(b[0] & 0b0000_0100) != 0x0;
        boolean ERR_GET = (b[0] & 0b0000_1000) != 0x0;
        boolean ERR_PB1 = (b[0] & 0b0001_0000) != 0x0;
        boolean ERR_PB2 = (b[0] & 0b0010_0000) != 0x0;

        CountUnit o = Terminal.CountUnit_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, DIS_SVZ, ERR_SVZ, STP_CPUSO, ERR_GET, ERR_PB1, ERR_PB2);
        } else {
            er(id_obj);
        }        
    }

    private static void crossing() {
        // 76543210
        boolean CLOSED = (b[0] & 0b0000_0001) != 0x00;
        boolean BOOM = (b[0] & 0b0000_0010) != 0x00;
        int NOTICE = (b[0] & 0b0000_1100) >> 2;
        int FAULT = (b[0] & 0b0011_0000) >> 4; // FLT и FAIL
        boolean MANUAL = (b[0] & 0b0100_0000) != 0x00;
        boolean BLOKED = (b[0] & 0b1000_0000) != 0x00;// запрет подачи извещения
        // 0-нет запрета (0-byte
        // 7-bit)
        boolean PEREG = (b[1] & 0b0000_0001) != 0x00;
        boolean NOZP = (b[1] & 0b0000_0010) != 0x00;
        boolean NETMG = (b[1] & 0b0000_0100) != 0x00;
        boolean PA = (byte)  (b[1] & 0b0000_1000) != 0x00;//несиправность схемы питания
        boolean KNB = (byte) (b[1] & 0b0001_0000) != 0x00;//Батарея разряжена
        boolean KSO = (byte) (b[1] & 0b0010_0000) != 0x00;//Неисправность счета осей

        Crossing o = Terminal.Crossings_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, CLOSED, BOOM, NOTICE, FAULT, MANUAL, PEREG, NOZP, NETMG, BLOKED, PA, KNB, KSO);
        } else {
            er(id_obj);
        }                
    }

    private static void fan() {
                                //   7654_3210
        boolean FAN_LITE = (b[0] & 0b0000_0001) != 0x00;
        boolean FAN_NORM = (b[0] & 0b0000_0010) != 0x00;
        boolean FAN_HARD = (b[0] & 0b0000_0100) != 0x00;
//        boolean FAN_OFF =  (b[0] & 0b0000_1000) != 0x00;//здесь не так (весь байт=0 то обдувка выключена)

        Fan o = Terminal.FanCell_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, FAN_LITE, FAN_NORM, FAN_HARD);//, FAN_OFF);
        } else {
            er(id_obj);
        }
    }

    private static void light() {
        // 76543210
        int CURTIME = (b[1] << 8 & 0xFF00) | (b[0] & 0xFF);
        int LIGHT = (b[3] << 8 & 0xFF00) | (b[0] & 0xFF);
        boolean DISABLE = (b[3] & 0b01000000) != 0;//(для эльги вз азвисимости от направления запрещаем светофор)
        boolean KMG =       (b[3] & 0b1000_0000) != 0;
        int BTNSTATE = (b[4] & 0b0000_0011);
        int ROUTESTATE = (b[4] & 0b0001_1100) >> 2;
        boolean BLOCKSTATE = (b[4] & 0b0010_0000) != 0;
        boolean BLOCK_ERROR = (b[4] & 0b0100_0000) != 0;
        boolean TIMERSTATE = (b[4] & 0b1000_0000) != 0;

        Light o = Terminal.LightsCell_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, LIGHT, BTNSTATE, ROUTESTATE, BLOCKSTATE, TIMERSTATE, CURTIME, BLOCK_ERROR, DISABLE, KMG);
        } else {
            er(id_obj);
        }        
    }

    private static void mpab() {//obj_type = 12
        Terminal.Mpab_Hash.get(id_obj).setState(
                dtime,
                (b[0] & 0b0000_0001) != 0x00,//режим "путевой прием"
                (b[0] & 0b0000_0010) != 0x00,//контроль посылки "дача согласия"
                (b[0] & 0b0000_0100) != 0x00,//режим "путевое отправление"
                (b[0] & 0b0000_1000) != 0x00,//"получено согласие"
                (b[0] & 0b0001_0000) != 0x00,//наличие ключа-жезла хозяйственного поезда
                (b[0] & 0b0010_0000) != 0x00,//наличие ключа-жезла подталкивающего локомотива
                (b[0] & 0b0100_0000) != 0x00,//свободность перегона
//                (b[0] & 0b1000_0000) != 0x00,//занятость перегона (перевернутеое _06)
                (b[1] & 0b0000_0001) != 0x00,
                (b[1] & 0b0000_0010) != 0x00,
                (b[1] & 0b0000_0100) != 0x00,//работа на основном комплекте МПАБ-Р
                (b[1] & 0b0000_1000) != 0x00,//работа на резервном комплекте МПАБ-Р
                (b[1] & 0b0001_0000) != 0x00,//неисправность аппаратуры МПАБ-Р
                (b[1] & 0b0010_0000) != 0x00,//отказ аппаратуры МПАБ или МПАБ-Р (оба комплекта)
                (b[1] & 0b0100_0000) != 0x00,//сообщение запрет нажатия OXk из-за задан маршрут отправления (открыт вых. сигнал)
                (b[1] & 0b1000_0000) != 0x00,//сообщение запрет нажатия OXk из-за нет отправления
                (b[2] & 0b0000_0001) != 0x00,//сообщение запрет нажатия OXk из-за изъят ключ-жезл хоз.поезда
                (b[2] & 0b0000_0010) != 0x00,//сообщение запрет нажатия OXk из-за неисправности аппаратуры
                (b[2] & 0b0000_0100) != 0x00,
                (b[2] & 0b0000_1000) != 0x00,
                (b[2] & 0b0001_0000) != 0x00,
                (b[2] & 0b0010_0000) != 0x00,
                (b[2] & 0b0100_0000) != 0x00,//сообщение запрет нажатия DSOk из-за изъят ключ-жезл хоз.поезда или подт.локомотива
                (b[2] & 0b1000_0000) != 0x00,//сообщение запрет нажатия IVk из-за изъят ключ-жезл хоз.поезда или подт.локомотива
                (b[3] & 0b0000_0001) != 0x00,
                (b[3] & 0b0000_0010) != 0x00
        );
    }

    private static void nearbby() {
        int STATE = (b[0]);

        Nearby o = Terminal.NearbyCell_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, STATE);
        } else {
            er(id_obj);
        }         
    }

/*    private static void note() {
        int gX = 0;
        int gY = 0;
        int shiftX = 0;
        int shiftY = 0;

        String[] args = new String(b).split("\\|");

        if (args.length == 5) {
            gX = Integer.valueOf(args[0]);
            gY = Integer.valueOf(args[1]);
            shiftX = Integer.valueOf(args[2]);
            shiftY = Integer.valueOf(args[3]);
        }
        // NoteCell n = Terminal.NoteCell_Hash.get(id_obj);
        // n.setState(dtime, gX, gY, shiftX, shiftY, text, id_obj);
    } */

    private static void protect() {
        // 76543210
        int PROTECTED_TIME = (b[1] << 8 & 0xFF00) | (b[0] & 0xFF);
        boolean KNM = (b[2] & 0b0000_0001) != 0x00;
        boolean CNCL_KNM = (b[2] & 0b0000_0010) != 0x00;
//        boolean OM = (b[2] & 0b0000_0100) != 0x00;
        boolean SPECIAL_NOTICE = (b[2] & 0b0000_1000) != 0x00;
        boolean BLOCK = (b[2] & 0b0001_0000) != 0x00;
        Protected o = Terminal.ProtectedCell_Hash.get(id_obj);//++++++++++++++++
        if (ObjExist(o)) {
            o.setState(
                    dtime,
                    PROTECTED_TIME,
                    KNM,
                    CNCL_KNM,
//                    OM,
                    SPECIAL_NOTICE,
                    BLOCK);
        } else {
            er(id_obj);
        }        
    }

    private static void railnet() {
        // 76543210
        int AXIS_NUMBER = (b[1] << 8 & 0xFF00) | (b[0] & 0xFF);
        int IR_TIMER = (b[1] << 8 & 0xFF00) | (b[0] & 0xFF);

        boolean BUSYSTATE = (b[2] & 0b0000_0001) != 0x00;
        int ROUTESTATE = (b[2] & 0b0000_0110) >> 1;
        boolean IR = (b[2] & 0b0000_1000) != 0x00;
        boolean BLOCKSTATE = (b[2] & 0b0001_0000) != 0x00;

        int ALARMSTATE = (b[2] & 0b0110_0000) >> 5;

        boolean SKIDDEDSTATE = (b[3] & 0b0000_0001) != 0x00;
        int OD = (b[3] & 0b0001_1110) >> 1;// (нету пока в индикации)

        int BOOM_STATE = 0;

        boolean BOOM_CANSEL = false;
        boolean ERR_DSP = false;
        boolean FLT_OG_R = false;
        boolean FLT_OG_SV = false;
        byte [] rawData = null;

        if (b.length > 4) {
            BOOM_STATE = (b[4] & 0b0000_0111);

            BOOM_CANSEL = (b[4] & 0b0000_1000) != 0x00;
            ERR_DSP = (b[4] & 0b0001_0000) != 0x00;// (нету пока в индикации)
            FLT_OG_R = (b[4] & 0b0010_0000) != 0x00;
            FLT_OG_SV = (b[4] & 0b0100_0000) != 0x00;// (нету пока в индикации)
        }
        Railnet o = Terminal.Railnets_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, BUSYSTATE, ROUTESTATE, BLOCKSTATE, IR, ALARMSTATE, SKIDDEDSTATE, AXIS_NUMBER, IR_TIMER, BOOM_STATE, FLT_OG_R, BOOM_CANSEL, OD, ERR_DSP, FLT_OG_SV);
        } else {
            er(id_obj);
        }
    }

    private static void turnout() {
        int POSITION = (b[0] & 0b0000_0011);
        boolean TOUTROUTE = (b[0] & 0b0000_0100) != 0x00;
        boolean FAULT = (b[0] & 0b0000_1000) != 0x00;
        boolean TOUTBLOCK = (b[0] & 0b0001_0000) != 0x00;
        boolean FAN = (b[0] & 0b0010_0000) != 0x00;
        boolean BACKWARD = (b[0] & 0b0100_0000) != 0x00;

        boolean FANCMD = (b[2] & 0b0000_0010) != 0x00;
        boolean FANFLT = (b[2] & 0b0000_0100) != 0x00;
        boolean DUP =            (b[1] & 0b00010000) != 0x00;
        boolean ERR = (b[2] & 0b0010_0000) != 0x00;// (нету пока в индикации)
        boolean VZREZ = (b[2] & 0b0100_0000) != 0x00;//Внимание!!! Опасность взреза стрелки!
        boolean NOCONTROL = (b[2] & 0b1000_0000) != 0x00;//Внимание!!! Опасность стрелка без котроля положения!
        boolean PK_ERR =         (b[2] & 0b00001000) != 0x00;// Ошибка (реле, обрыв провода) ао внешней схеме контроля.......
        boolean MK_ERR =         (b[2] & 0b00010000) != 0x00;
        Turnout o = Terminal.Turnouts_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, POSITION, TOUTROUTE, FAULT, TOUTBLOCK, FAN, BACKWARD, FANCMD, FANFLT, DUP, ERR, VZREZ, NOCONTROL, PK_ERR, MK_ERR);
        } else {
            er(id_obj);
        }
    }

    private static void unit() {
        byte[] rawData = new byte[b.length];
        System.arraycopy(b, 0, rawData, 0, rawData.length - 1 + 1);
        Unit o = Terminal.Unit_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(
//                    dtime,
                    rawData);
        } else {
            er(id_obj);
        }        
    }
    
    private static void plc() {
        byte[] rawData = new byte[b.length];
        System.arraycopy(b, 0, rawData, 0, rawData.length - 1 + 1);
        PLC o = Terminal.PLCs_Hash.get(0);
        if (ObjExist(o)) {
            o.setState(rawData);
        } else {
            er(id_obj);
        }         
    }

    private static void vc() {
        int AMPERS = (b[1] << 8 & 0xFF00) | (b[0] & 0xFF); // Значение тока
        // перевода стрелок
        boolean FIRST_FEEDER_UP = (b[5] & 0b1) != 0x0; // 1-й фидер
        boolean FIRST_FIDER_ACTIVE = (b[5] & 0b10) != 0x0;
        boolean SECOND_FIDER_UP = (b[5] & 0b100) != 0x0; // 2-й фидер
        boolean SECOND_FIDER_ACTRIVE = (b[5] & 0b1000) != 0x0;
        boolean THIRD_FIDER_UP = (b[5] & 0b1_0000) != 0x0; // 3-й фидер
        boolean THIRD_FIDER_ACTIVE = (b[5] & 0b10_0000) != 0x0;
        boolean FUSEUP = (b[5] & 0b100_0000) != 0x0; // контроль
        // предохранителей/автоматов
        boolean GROUNDIGNGUP = (b[5] & 0b1000_0000) != 0x0; // контроль
        // заземления

        boolean FIRE = (b[6] & 0b1) != 0x0; // Пожарная тревога
        boolean ACCESS = (b[6] & 0b10) != 0x0; // Контроль доступа
        boolean DCHIGH = (b[6] & 0b100) != 0x0; // питание 220v
        boolean DCLOW1 = (b[6] & 0b1000) != 0x0; // питание 24v
        boolean DCLOW2 = (b[6] & 0b1_0000) != 0x0; // питание 12v
        boolean DCLOW3 = (b[6] & 0b10_0000) != 0x0; // питание 36v
        boolean CUR_DIG = (b[6] & 0b100_0000) != 0x0; // аналоговый или цифровой
        // амперметр у стрелки
        boolean OGSP = (b[6] & 0b1000_0000) != 0x0; // контроль неисправности
        // ограждения составов

        boolean DISCHARGING = (b[7] & 0b1) != 0x0; // discharging работа от
        // батареи или от сети
        boolean FULL_DISCHARGE = (b[7] & 0b10) != 0x0; // fullDischarge
        boolean COMMON_ERR = (b[7] & 0b100) != 0x0; // ИБП - неисправность
        boolean OVERLOAD = (b[7] & 0b1000) != 0x0; // ИБП - перегрузка

        VC o = Terminal.VCs_Hash.get(id_obj);
        o.setState(dtime, AMPERS, FIRST_FEEDER_UP, FIRST_FIDER_ACTIVE,
                SECOND_FIDER_UP, SECOND_FIDER_ACTRIVE, THIRD_FIDER_UP,
                THIRD_FIDER_ACTIVE, FUSEUP, GROUNDIGNGUP, DCHIGH, DCLOW1,
                DCLOW2, DCLOW3, CUR_DIG, OVERLOAD, DISCHARGING, FULL_DISCHARGE,
                COMMON_ERR, FIRE, ACCESS, OGSP);
    }
    
    private static void dio() {
        byte[] rawData = new byte[b.length];
        for (int i = rawData.length - 1; i >= 0; i--) {
            rawData[i] = b[i];// Replace with 'System.arrayCopy()'
        }
        f3_dio_20_8_02 o = Terminal.f3_dio_20_8.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, rawData, id_obj);
        } else {
            er(id_obj);
        }
    }
    
    private static void dga() {
        int DGA_TIME  = (b[1] << 8 & 0xFF00) | (b[0] & 0xFF);
        boolean DGA_AUTO  = (b[2] & 0b00000001) !=  0x00;
        boolean DGA_HNDL  = (b[2] & 0b00000010) !=  0x00;
        boolean DGA_STOP  = (b[2] & 0b00000100) !=  0x00;
        boolean DGA_FUEL  = (b[2] & 0b00001000) !=  0x00;
        boolean START_HND = (b[2] & 0b00010000) !=  0x00;
        boolean STOP_HND  = (b[2] & 0b00100000) !=  0x00;
        boolean START_AUT = (b[2] & 0b01000000) !=  0x00;
        boolean STOP_AUT  = (b[2] & 0b10000000) !=  0x00;
        boolean RNx       = (b[3] & 0b00000001) !=  0x00;
        boolean KMx       = (b[3] & 0b00000010) !=  0x00;
        boolean DGA_ON    = (b[3] & 0b00000100) !=  0x00;
        boolean RUN       = (b[3] & 0b00001000) !=  0x00;
        Dga o = Terminal.DGA_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, DGA_TIME,
                    DGA_AUTO,
                    DGA_HNDL,
                    DGA_STOP,
                    DGA_FUEL,
                    START_HND,
                    STOP_HND,
                    START_AUT,
                    STOP_AUT,
                    RNx,
                    KMx,
                    DGA_ON,
                    RUN);
        } else {
            er(id_obj);
        }
    }
    
    private static void elevated() {
        boolean TRACK_ON = (b[0] & 0b00000001) !=  0x00;
        ElevatedTrack o = Terminal.ElevatedTrackCell_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, TRACK_ON);
        } else {
            er(id_obj);
        }
    }
    
    private static void heating() {
        boolean HOT_ALL_ON  = (b[0] & 0b00000001) !=  0x00;
        boolean HOT_ALL_ERR = (b[0] & 0b00000010) !=  0x00;
        Heating o = Terminal.Heating_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, HOT_ALL_ON, HOT_ALL_ERR);
        } else {
            er(id_obj);
        }
    }

    private static void uksps() {
        boolean KS0   = (b[0] & 0b00000001) !=  0x00;
        boolean KZHZ  = (b[0] & 0b00000010) !=  0x00;
//        boolean KS1   = (b[1] & 0b00000001) !=  0x00;
        boolean KZ    = (b[1] & 0b00000010) !=  0x00;
        boolean HZ    = (b[1] & 0b00000100) !=  0x00;
        boolean NORMA = (b[1] & 0b00001000) !=  0x00;
        Uksps o = Terminal.Uksps_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, KS0, KZHZ, KZ, HZ, NORMA);
        } else {
            er(id_obj);
        }
    }  
    
    private static void heat() {
        boolean HOT_ON =  (b[0] & 0b00000001) !=  0x00;
        boolean HOT_ERR = (b[0] & 0b00000010) !=  0x00;
        boolean HOT_GND = (b[0] & 0b00000100) !=  0x00;
        Heat o = Terminal.Heat_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, HOT_ON, HOT_ERR, HOT_GND);
        } else {
            er(id_obj);
        }
    }     
    
    private static void blokpost() {
//                                76543210
        boolean NORMA = (b[0] & 0b00000001) !=  0x00;//норма
        boolean HZ    = (b[0] & 0b00000010) !=  0x00;//неисправность
        boolean _02   = (b[0] & 0b00000100) !=  0x00;//
        boolean _03   = (b[0] & 0b00001000) !=  0x00;//
        boolean _04   = (b[0] & 0b00010000) !=  0x00;//
        boolean _05   = (b[0] & 0b00100000) !=  0x00;//
        boolean _06   = (b[0] & 0b01000000) !=  0x00;//
        boolean _07   = (b[0] & 0b10000000) !=  0x00;//
        boolean _10   = (b[1] & 0b00000001) !=  0x00;//
        boolean _11   = (b[1] & 0b00000001) !=  0x00;//
        boolean _12   = (b[1] & 0b00000001) !=  0x00;//
        boolean _13   = (b[1] & 0b00000001) !=  0x00;//
        boolean _14   = (b[1] & 0b00000001) !=  0x00;//
        boolean _15   = (b[1] & 0b00000001) !=  0x00;//
        boolean _16   = (b[1] & 0b00000001) !=  0x00;//
        boolean _17   = (b[1] & 0b00000001) !=  0x00;//
        Blokpost o = Terminal.Blokpost_Hash.get(id_obj);
        if (ObjExist(o)) {
            o.setState(dtime, NORMA, HZ, _02, _03, _04, _05, _06, _07, _10, _11, _12, _13, _14, _15, _16, _17);
        } else {
            er(id_obj);
        }
    }

//    SetState(String line) {// 0=IND:1=12345678:2=id_obj:3=stateNew
//        // Server.SendAll
//        // (obj.id_type+":"+obj.id_obj+":"+stateNew+":"+(!obj.stateOld.isEmpty() ?
//        // (obj.stateOld) : ("null")));//
//        String[] s = line.split(":");
//        // obj_type = Integer.valueOf(s[0]);
//        dtime = Long.valueOf(s[1]);
//        id_obj = Integer.valueOf(s[2]);
//        new_val = s[3];
//        // old_val = s[4];
//
//        if (Terminal.obj_type.containsKey(id_obj)) {//from id_obj get obj_type
//            obj_type = Terminal.obj_type.get(id_obj);
//        } else {
//            Log.log("obj_type not contains id " + id_obj);
//        }
//
//        String[] ba = s[3].split("\\.");
//        b = new byte[ba.length];
//
//        // for (int i = ba.length - 1; i >= 0; i--) {
//        // int z = Integer.valueOf(ba[i]);
//        // if (z > 127){
//        // b[i] = (byte) (z - 256);
//        // } else {
//        // b[i] = (byte) z;
//        // }
//        for (int i = 0; i <= (ba.length - 1); i++) {
//            int z = Integer.valueOf(ba[i]);
//            if (z > 127) {
//                b[i] = (byte) (z - 256);
//            } else {
//                b[i] = (byte) z;
//            }
//
//        }
//    }
    
    
    private static boolean ObjExist(Object c){
        return c != null;
    }
    
    private static void er(int idobj){
        Log.log("SetState object not exist with obj_id = " + idobj);
    }
    
    
    @Override
    public void run() {
        s1();
    }

}

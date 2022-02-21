//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Thread.sleep;
import static terminal.Commander.cmdLayers;
import static terminal.Commander.cpuMon;
import static terminal.Terminal.Cabinets_Hash;
import static terminal.Terminal.PLCs_Hash;
import static terminal.Terminal.boomer;

@SuppressWarnings("UnusedAssignment")
class Init {

    private int progress = 0;

    private static void adjustCpuMon() {
        cpuMon.setBounds(
                Terminal.CPU_MONITOR_X,
                Terminal.CPU_MONITOR_Y,
                Terminal.CPU_MONITOR_WIDTH,
                Terminal.CPU_MONITOR_HEIGHT
        );
    }

    private void LoadFromCSV(String ResourceFile) {
        // <editor-fold defaultstate="collapsed" desc=" LoadFromCSV ">

        if (ResourceFile == null) {
            Log.log("ResourceFile == null");
            System.exit(2);
        }

        String fs = Terminal.FS;
        File f = new File(Terminal.ATTRANS_HOME + fs + "config" + fs + ResourceFile);// resource.att - FFF Victor надо проверить ResourceFile = что он не равен "" или null и что он существует!!!
        FileInputStream fis = null;
        BufferedReader reader = null; // reading file line by line in Java using BufferedReader

        try {
            fis = new FileInputStream(f);
            reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String object = "";
            String line = "";
            int i = 0;
            while (line != null) {// с 0 строки по конец файла. (цикл по файлу)
                line = reader.readLine();
                if (line != null) {
                    if (line.isEmpty() || !line.startsWith("#")) {
                        if (line.startsWith("[")) {
                            object = Util.getObject(line).toUpperCase();
                            progress += 5;
                            Splash.jProgressBar1.setValue(progress);
                            Splash.lblText.setText("Загрузка " + object);
                        } else {
                            parse(object, line);
                            try {
                                sleep(1);
//                                System.out.println("i = " + i);
                                i++;
                            } catch (InterruptedException e) {
                                Err.err(e);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            Err.err(e);
            Commander.HALT.exit();//FFF FUCK victor Exit перенести из коммандера в terminal
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                Err.err(e);
            }
        }
        // </editor-fold>
    }

    private void parse(String object, String line) {
        // <editor-fold defaultstate="collapsed" desc=" parse ">
        if (!"".equals(line) || !"".equals(object)) {
            String[] tokens = line.split(",");
            switch (object) {
                case "VI_COLORS":// defaults
                    color(tokens);
                    break;
                case "VI_DEFAULTS":// defaults
                    defaults(tokens);
                    break;
                case "VI_OBJ_TYPES":// VC FFF Translate
                    obj_types(tokens);
                    break;
                case "VI_NOTE_TYPES":// note_type
                    note_type(tokens);
                    break;
                case "VI_MESS":// mpab
                    mess(tokens);
                    break;
                case "VI_CMDMESS":
                    cmds(tokens);
                    break;

                case "VI_AREAS":// areas
                    area(tokens);
                    break;
                // -------------------------------------------------------------
                case "VI_COUNT_UNITS":// 1
                    count_unit(tokens);
                    break;
                case "VI_BEAMS":// 2
                    beam(tokens);
                    break;
                case "VI_COUNTERS":// 3
                    counter(tokens);
                    break;
                // ------------------------------------------------------------------------------
                case "VI_CABINETS":// cabinets
                    cabinet(tokens);
                    break;
                case "VI_CHANNELS":
                    channels(tokens);
                    break;
                case "VI_BORDERS":// borders
                    border(tokens);
                    break;
                case "VI_PLCS":// cpu
                    plc(tokens);
                    break;
                case "VI_CROSSINGS":// crossing
                    crossing(tokens);
                    break;
                case "VI_DEADLOCKS":// deadlock //не нужен берем из railnet
                    deadlock(tokens);
                    break;
                case "VI_RAILNET_COUNTERS":// railnet
                    railnet_boom(tokens);
                    break;
                case "VI_RAILNETS":// railnet
                    railnet(tokens);
                    break;
                case "VI_TURNOUTS":// turnout
                    turnout(tokens);
                    turnout_set_twin();
                    break;
                case "VI_DIRECTS":// directs //не нужен берем из railnet
                    direct(tokens);
                    break;
                case "VI_FANS":// fan
                    fan(tokens);
                    break;
                case "VI_LIGHTS":// lights
                    light(tokens);
                    break;
                case "VI_LIGHTS_NEW":// lights
                    light_new(tokens);
                    break;
                case "VI_LIGHTS_PN":// lights
                    light_pn(tokens);
                    break;
                case "VI_MPABS":// mpab
                    mpab(tokens);
                    break;
                case "VI_NEARBYS":// nearby
                    nearby(tokens);
                    break;
                case "VI_NOTES":// note
                    note(tokens);
                    break;
                case "VI_PARAMS":// params (for channels)
                    params(tokens);
                    break;
                case "VI_PROTECTEDS":// protected //не нужен берем из
                    // railnet
                    protect(tokens);
                    break;
                case "VI_UNITS":
                    unit(tokens);
                    break;
                case "VI_F3_DIO_20_8":
                    f3_dio_20_8(tokens);
                    break;
                case "VI_UNRULEDS":// unruled //гурзим спец. графическйи
                    // элимент на экран
                    unrulled(tokens);
                    break;
                case "VI_VCS":// VC
                    vc(tokens);
                    break;
                case "VI_WEIGHERS":// Weigher
                    weigher(tokens);
                    break;
                case "VI_ELEVATEDTRACKS":// Elevated track
                    elevatedtrack(tokens);
                    break;
                case "VI_UKSPS":// uksps
                    uksps(tokens);
                    break;
                case "VI_DGAS":// dga
                    dga(tokens);
                    break;
                case "VI_HEATS":// heat
                    heat(tokens);
                    break;
                case "VI_HEATINGS":// heating
                    heating(tokens);
                    break;
                case "VI_GATE":// heating
                    gate(tokens);
                    break;
                case "VI_PUSHERS":// heating
                    pushers(tokens);
                    break;
                case "VI_WINDOWS":// windows
                    windows(tokens);
                    break;
                case "VI_TXT":// txt
                    txt(tokens);
                    break;
                case "VI_US"://blokpost
                    blokpost(tokens);
                    break;
                case "VI_VZREZ":// vzrez
                    if (Terminal.VZREZ) {
                        vzrez(tokens);
                    }
                    break;
                case "VI_VS":// vzrez siemens (if PLC simatic!)
                    if (Terminal.VZREZ) {
                        vs(tokens);
                    }
                    break;
                default:
//                    Log.log("Неизвестно :( " + object);
                    break;
            }// end switch
        }// end if
        // </editor-fold>
    }

    private int getInt(String tag) {
        // int ret;
        // if (tag.equals("null")) {ret = 0; System.out.println("null");}
        // else {ret = Integer.valueOf(tag);}
        // return ret;
        return Integer.valueOf(tag);
    }

    private Long getLong(String tag) {
        // int ret;
        // if (tag.equals("null")) {ret = 0; System.out.println("null");}
        // else {ret = Integer.valueOf(tag);}
        // return ret;
        return Long.valueOf(tag);
    }

    // private Boolean getBoolean(String tag) {
    // return Boolean.valueOf(tag);
    // }
    // private Timestamp getTimestamp(String tag) {
    // long l = Long.valueOf(tag);
    // Timestamp ts = new Timestamp(l);
    // return ts;
    // }
    private String getString(String tag) {
        return tag;
    }

    private void color(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" color ">
//[VI_COLORS]
//#0=ID_OBJ,1=COUNT_UNIT_ID,2=S_NAME,3=CMD2
//        Color c = new Color(
//                getInt(tokens[0]), // ID_OBJ
//                getInt(tokens[1]), // ID_AREA
//                tokens[2], // S_NAME
        String MPC_NAME = tokens[3]; // MPC_NAME
        int R = getInt(tokens[4]); // R
        int G = getInt(tokens[5]); // G
        int B = getInt(tokens[6]); // B
//                getInt(tokens[7])  // ID_TYPE
//        );

        Terminal.ColorsHash.put(MPC_NAME, new java.awt.Color(R, G, B));
//        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
//        Terminal.obj_type.put(getInt(tokens[0]), 88);// 88 COLOR

        // </editor-fold>
    }

    private void area(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" area ">
//[VW_INIT_AREA]
//#0=ID_OBJ,1=S_NAME,2=MPC_NAME,3=X,4=Y,5=SHIFT_X,6=SHIFT_Y,7=VIDEO_STATUS,8=PRIMARY_IP,9=SECONDARY_IP,10=FIRST_RESET_HW,11=SECOND_RESET_HW,12=HOLLOW_CANCELING,13=TRAIN_ROUTING,14=SHUNTING_ROUITING,15=HONEST_ODD,16=IND,17=CMD,18=STATE,19=STATE_TIME
//        Terminal.AREA = tokens[1];
        Terminal.obj_name.put(getInt(tokens[0]), tokens[1]);
        Terminal.obj_type.put(getInt(tokens[0]), 1);// 1 AREANAME
        Area a = new Area(
                getInt(tokens[0]),// ID_OBJ
                tokens[1], // S_NAME
                tokens[2], // MPC_NAME
                getInt(tokens[3]),// X
                getInt(tokens[4]),// Y
                getInt(tokens[5]),// SHIFT_X
                getInt(tokens[6]),// SHIFT_Y
                getInt(tokens[7]),// VIDEO_STATUS

                (getInt(tokens[10]) == 1),// FIRST_RESET_HW
                (getInt(tokens[11]) == 1),// SECOND_RESET_HW
                (getInt(tokens[12]) == 1),// HOLLOW_CANCELING
                (getInt(tokens[13]) == 1),// TRAIN_ROUTING
                (getInt(tokens[14]) == 1),// SHUNTING_ROUTING
                (getInt(tokens[15]) == 0),// HONEST_ODD

                //                getInt(tokens[16]), // IND
                //                getInt(tokens[17]), // CMD
                (getInt(tokens[18]) == 1), // GOT
                (getInt(tokens[19]) == 1) // SN
        );
        Terminal.Area_Hash.put(getInt(tokens[0]), a);
        // </editor-fold>
    }

    private void beam(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" beam ">
//[VW_INIT_BEAM]
//#0=ID_OBJ,1=COUNT_UNIT_ID,2=S_NAME,3=CMD2
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 2);// 2 BEAM
        Beam b = new Beam(
                //                getInt(tokens[0]), // ID_OBJ
                //                getInt(tokens[1]), // COUNT_UNIT_ID
                tokens[2], // S_NAME
                getInt(tokens[3]) // CMD2
        );

        Terminal.Beams_Hash.put(getInt(tokens[0]), b);

        // сюда добавляем лучи в COUNT_UNIT
        Terminal.CountUnit_Hash.get(getInt(tokens[1])).putBaeam(b);
        // </editor-fold>
    }

    private void border(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" border ">
//[VW_INIT_BORDER]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=ORIENTATION
        Border b = new Border(
                //                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                //                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]),// X
                getInt(tokens[5]),// Y
                getInt(tokens[6]),// SHIFT_X
                getInt(tokens[7]),// SHIFT_Y
                //                getInt(tokens[8]),// VIDEO_STATUS
                getInt(tokens[9]) // ORIENTATION
        );
        b = null;
        // </editor-fold>
    }

    private void cabinet(String[] tokens) {// FFF FUFUFU FUCK Victor !!! SELECT DISTINCT - переделать вьюху
        // <editor-fold defaultstate="collapsed" desc=" cabinet ">
//[VW_INIT_CABINET]
//#0=ID_OBJ,1=ID_AREA,2=CABINET_NAME,3=MPC_NAME,4=DE,5=EN,6=RACKS_AMOUNT,7=TYPE (type 0=siemens)
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 3);// 3 CABINET
        Cabinet c = new Cabinet(
                //                tokens[1],// AREA_NAME
                getInt(tokens[0]),// ID_OBJ
                tokens[2],// CABINET_NAME
                getInt(tokens[6]),
                getInt(tokens[7])// RACKS_AMOUN
        );
        Cabinets_Hash.put(getInt(tokens[0]), c);
        // </editor-fold>
    }

    private void channels(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" channels ">
//[VW_INIT_CHANNEL]
//#0=ID_OBJ,1=UNIT_ID,2=LINK_NUMBER,3=CHANNEL_PURPOSE
        Channels ch = new Channels(
                //                getInt(tokens[0]),// ID_OBJ
                getInt(tokens[1]),// UNIT_ID
                getInt(tokens[2]),// LINK
                tokens[3]// CHANNEL_PURPOSE
        );

        Terminal.Channels_Hash.put(getInt(tokens[0]), ch);
//        Terminal.obj_type.put(getInt(tokens[2]), 4);// 4 CHANNEL ??????????????
        // </editor-fold>
    }

    private void cmds(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" cmd ">
        // [VW_INIT_CMD]

        String PROP_VAL = tokens[0];
        long ID_TYPE = getLong(tokens[1]);
        Terminal.Cmd.put(PROP_VAL, ID_TYPE);

        // </editor-fold>
    }

    private void counter(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" counter ">
//[VI_COUNTERS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=ORIENTATION,10=RN1,11=RN2,12=INC_SGN,13=CLRTYPE,14=BEAM_ID,15=LOG_ADDRESS,16=IND
        if (getInt(tokens[8])==0){ // if video statuss
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 7);// 7 COUNTER
        Counter cc = new Counter(
                getInt(tokens[0]), // ID_OBJ
                //                getInt(tokens[1]), // ID_AREA
                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]), // X
                getInt(tokens[5]), // Y
                getInt(tokens[6]), // SHIFT_X
                getInt(tokens[7]), // SHIFT_Y
                getInt(tokens[8]), // VIDEO_STATUS
                getInt(tokens[9]), // ORIENTATION
                getInt(tokens[10]), // RN1
                getInt(tokens[11]), // RN2
                getInt(tokens[12]), // INC_SGN
                (getInt(tokens[13]) == 1), // CLRTYPE
                getInt(tokens[14]), // BEAM_ID
                getInt(tokens[15]) // LOG_ADDRESS
        //                getInt(tokens[16]) // IND
        );
        if (getInt(tokens[15]) != -1) {// LOG_ADDRESS)
            Terminal.CounterCell_Hash.put(getInt(tokens[0]), cc);
            Terminal.Beams_Hash.get(getInt(tokens[14])).plugCounter(cc.Counter);// датчик добавляется в луч
        }
        }
        // </editor-fold>
    }

    private void count_unit(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc="count unit ">
//[VW_INIT_COUNT_UNIT]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=DE,5=EN,6=IND,7=STATE,8=STATE_TIME
//[VI_COUNT_UNITS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=DE,5=EN,6=IND
        // CountUnit(int objId) {
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 8);// 8 COUNT_UNIT
        CountUnit cu = new CountUnit(
                getInt(tokens[0]), // ID_OBJ
                tokens[2]// S_NAME
        );
        Terminal.CountUnit_Hash.put(getInt(tokens[0]), cu);

        // !!!!!!!!!!!! лучше сделать наоборот в луче добавляться в Count Unit.
        // сюда добавляем лучи beams принадлежащие этому count_unit
        // for (int k : Terminal.Beams_Hash.keySet()){
        // Beam b = Terminal.Beams_Hash.get(k);
        // if (b.counnt_unit_id == getInt(tokens[0])){
        // cu.putBaeam(b);
        // }
        // }
        // </editor-fold>
    }

    @SuppressWarnings("UnusedAssignment")
    private void plc(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" cpu ">
//[VW_INIT_CPU]
//#0=ID_OBJ,1=ID_AREA,2=RU,3=MPC_NAME,4=IP_CPU,5=MERKER_WORK,6=MERKER_MASTER,7=BIT_MASTER,8=ID_PARTNER,9=PORT_IND,10=PORT_CMD_W,11=PORT_CMD_R,12=PORT_DIA,13=PORT_PARTNER,14=DE,15=EN
        int i = 0;//fuck
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 9);// 9 CPU contriller
        PLCs_Hash.put(i, new PLC(
                getInt(tokens[0]), // ID_OBJ
                //                getInt(tokens[1]), // ID_AREA
                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                tokens[4], // IP_CPU
                //                getInt(tokens[5]), // MERKER_WORK
                //                getInt(tokens[6]), // MERKER_MASTER
                //                getInt(tokens[7]), // BIT_MASTER
                //                getInt(tokens[8]), // ID_PARTNER
                getInt(tokens[9]), // PORT_IND
                getInt(tokens[10]), // PORT_CMD_W
                getInt(tokens[11]), // PORT_CMD_R
                //                getInt(tokens[12]), // PORT_DIA
                getInt(tokens[13]) // PORT_PARTNER
        ));
        //noinspection UnusedAssignment
        i++;
        // </editor-fold>
    }

    private void crossing(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" crossings ">
//[VI_CROSSINGS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=ID_MPC,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=ORIENTATION,10=SIDE,11=HONEST_ODD,12=RULED,13=LAYER,14=IND,15=CMD,16=STATE,17=STATE_TIME
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 10);// 10 CROSSING
        Crossing cc = new Crossing(
                getInt(tokens[17]),// ID_GOBJ
                //                getInt(tokens[1]),// ID_AREA
                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]),// X
                getInt(tokens[5]),// Y
                getInt(tokens[6]),// SHIFT_X
                getInt(tokens[7]),// SHIFT_Y
                getInt(tokens[8]),// VIDEO_STATUS
                getInt(tokens[9]), // ORIENTATION
                getInt(tokens[10]), // SIDE
                (getInt(tokens[11]) == 1),// HONEST_ODD
                getInt(tokens[12]), // RULED (0 - не управляемый , 1 - управляемый, 3 - контролируемый, 4 - не котролируемый, 5 - управляемый с отключателем извещения
                //                getInt(tokens[13]), // LAYER
                //                getInt(tokens[14]), // IND
                //                getInt(tokens[15]), // CMD
                (getInt(tokens[16]) == 1),// ZG_CMD есть команда включить заграждение с арма
                getInt(tokens[0]) //ID_OBJ
        );
        Terminal.Crossings_Hash.put(getInt(tokens[17]), cc);

        // }//end for
        // </editor-fold>
    }

    private void deadlock(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" deadlock ">
//[VI_DEADLOCKS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=X,4=Y,5=SHIFT_X,6=SHIFT_Y,7=VIDEO_STATUS,8=ORIENTATION,9=LAYER,10=ID_RN
        Terminal.obj_type.put(getInt(tokens[0]), 77);// 77 DEADLOCK
        Deadlock dc = new Deadlock(
                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                //                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[3]),// X
                getInt(tokens[4]),// Y
                getInt(tokens[5]),// SHIFT_X
                getInt(tokens[6]),// SHIFT_Y
                getInt(tokens[7]),// VIDEO_STATUS
                getInt(tokens[8]), // ORIENTATION
                //                (getInt(tokens[9]) == 0), //LAYER // ipar - верхний слой или нет - здесь
                getInt(tokens[10]) // ID_RN
        );
        Terminal.DeadlockCell_Hash.put(getInt(tokens[0]), dc);
        // </editor-fold>
    }

    private void defaults(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" defaults ">
        // [VW_INIT_DEFAULTS]
        // #0=ID_OBJ,1=ID_AREA,2=OBDUV,3=CPU,4=DB,5=CELL_SHIFT
        Terminal.OBDUV = (getInt(tokens[2]) == 1);// если 1 то в этом проекте есть обдувка стрелок (рисуем в меню дополнительный  пункт)
        Terminal.CPU = getInt(tokens[3]); // For config NCU //Тип процессора (0 - Siemens (TSAP - 102 порт), 1 - HIMA, 2 - Siemens (Fetch & Write))
//        if (tokens.length==6){
        Terminal.CELL_SHIFT = getInt(tokens[5]);
//        }
        // </editor-fold>
    }

    private void direct(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" directs ">
//[VI_DIRECTS]
//#0=ID_OBJ,1=ID_AREA,2=X,3=Y,4=SHIFT_X,5=SHIFT_Y,6=VIDEO_STATUS,7=ORIENTATION,8=SIDE,9=IPAR,10=ID_RN,11=ID_TR,12=DIRECT_CHAIN,13=CHAIN
        Terminal.obj_type.put(getInt(tokens[0]), 27);// 27 DIRECT
        Direct dc = new Direct(
                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                //                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[2]),// X
                getInt(tokens[3]),// Y
                getInt(tokens[4]),// SHIFT_X
                getInt(tokens[5]),// SHIFT_Y
                getInt(tokens[6]),// VIDEO_STATUS
                getInt(tokens[7]), // ORIENTATION
                getInt(tokens[8]), // SIDE
                //                (getInt(tokens[9]) == 1),//LAYER  IPAR ?? для чего нужна ?? (а это для перекресков какой из директов будет рисоваться на верхнем слое
                getInt(tokens[10]),// ID_RN -- это id рельсы
                getInt(tokens[11]),// ID_TR -- это id стрелки
                getInt(tokens[12]) // DIRECT_CHAIN
        );
        Terminal.DirectCell_Hash.put(getInt(tokens[0]), dc);

        // </editor-fold>
    }

    private void fan(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" fans ">
//[VI_FANS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=IND,10=CMD,11=STATE,12=STATE_TIME
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 24);// 24 FAN
        Fan fc = new Fan(
                //                getInt(tokens[0]),// ID_OBJ
                getInt(tokens[0]),// ID_AREA
                //                tokens[2], // S_NAME
                tokens[2], // MPC_NAME
                getInt(tokens[4]),// X
                getInt(tokens[5]),// Y
                getInt(tokens[6]),// SHIFT_X
                getInt(tokens[7]),// SHIFT_Y
                getInt(tokens[8])// VIDEO_STATUS
        //                getInt(tokens[9]),// IND
        //                getInt(tokens[10])// CMD
        );
        Terminal.FanCell_Hash.put(getInt(tokens[0]), fc);
        // </editor-fold>
    }

    private void light(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" lightss ">
//[VI_LIGHTS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=ORIENTATION,10=MOUNT,11=STOP_LIGHT,12=TYPE,13=CONTROL,14=INVITING,15=IND,16=CMD
//Light(int ID_OBJ, int ID_AREA, String S_NAME, String MPC_NAME, int X, int Y, int SHIFT_X, int SHIFT_Y, int VIDEO_STATUS, int ORIENTATION, boolean MOUNT, boolean RED_PROHIBIT, int TYPE, boolean TRAIN_LIGHT, boolean WARN, int CONTROL, boolean INVITING, int IND, int CMD ) {
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 11);// 11 LIGHTS
        Light lc = new Light(
                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]),// X
                getInt(tokens[5]),// Y
                getInt(tokens[6]),// SHIFT_X
                getInt(tokens[7]),// SHIFT_Y
                getInt(tokens[8]),// VIDEO_STATUS
                getInt(tokens[9]),// ORIENTATION
                (getInt(tokens[10]) == 1),// MOUNT (0-ON_GROUND, 1-ON_STAND)
                (getInt(tokens[11]) == 1),// STOP_LIGHT (0-BLUE, 1-RED)
                getInt(tokens[12]),// TYPE (0-SHUNTING, 1-TRAIN, 2-WARNING)

                //                getString(tokens[4]).equals("TRAIN"),// LIGHTS_TYPE (TRAIN, WARNING, SHUNTING)
                //                getString(tokens[4]).equals("WARNING"),// пригласительный

                getInt(tokens[13]), // CONTROL (0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
                (getInt(tokens[14]) == 1),// INVITING
        //                getInt(tokens[15]), // IND
        //                getInt(tokens[16]) // CMD
                (getInt(tokens[17]) == 1)//DISABLED
        );
        Terminal.LightsCell_Hash.put(getInt(tokens[0]), lc);
        // </editor-fold>
    }

    private void light_new(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" light new">
//[VI_LIGHTS_NEW]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=ORIENTATION,10=MOUNT,11=STOP_LIGHT,12=TYPE,13=CONTROL,14=INVITING,15=IND,16=CMD
//2606,19,Н,N_LT,22,6,0,0,0,5,1,1,1,0,1,1530,228
//LightsCell_New(int ID_OBJ, String S_NAME, int X, int Y, int VIDEO_STATUS, int ORIENTATION, boolean TRAIN_LIGHT, boolean ON_STAND, boolean RED_PROHIBIT, String CONTROL_TYPE, boolean WITHINVITING, int SHIFT_X, int SHIFT_Y, boolean WARN) {
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 11);// 11 LIGHTS
        Lights_New lc = new Lights_New(
                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]),// X
                getInt(tokens[5]),// Y
                getInt(tokens[6]),// SHIFT_X
                getInt(tokens[7]),// SHIFT_Y
                getInt(tokens[8]),// VIDEO_STATUS
                getInt(tokens[9]),// ORIENTATION
                (getInt(tokens[10]) == 1),// MOUNT (0-ON_GROUND, 1-ON_STAND)
                (getInt(tokens[11]) == 1),// STOP_LIGHT (0-BLUE, 1-RED)
                getInt(tokens[12]),// TYPE (0-SHUNTING, 1-TRAIN, 2-WARNING)

                //                getString(tokens[4]).equals("TRAIN"),// LIGHTS_TYPE (TRAIN, WARNING, SHUNTING)
                //                getString(tokens[4]).equals("WARNING"),// пригласительный

                getInt(tokens[13]), // CONTROL (0-FULL, 1-ONLY_LIGHTS, 2-ONLY_BUTTON)
                (getInt(tokens[14]) == 1)// INVITING
        //                getInt(tokens[15]), // IND
        //                getInt(tokens[16]) // CMD
        );
//        Terminal.LightsCell_Hash.put(getInt(tokens[0]), lc);
        // </editor-fold>
    }

    private void light_pn(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" light pn">
//[VI_LIGHTS_PN]
//#0=ID_OBJ,1=S_NAME,2=X,3=Y,4=ORIENTATION,5=SHIFT_X,6=SHIFT_Y,7=VIDEO_STATUS,8=IND
//2611,ПН,23,6,5,0,0,0,1540
        Lights_PN lc = new Lights_PN(
                getInt(tokens[0]),// ID_OBJ
                getString(tokens[1]),// S_NAME
                getInt(tokens[2]),// X
                getInt(tokens[3]),// Y
                //                getInt(tokens[4]),// ORIENTATION
                getInt(tokens[5]),// SHIFT_X
                getInt(tokens[6]),// SHIFT_Y
                getInt(tokens[7])// VIDEO_STATUS
        );
//        Terminal.LightsCell_New_Hash.put(getInt(tokens[1]), lc);
        Terminal.obj_name.put(getInt(tokens[0]), tokens[1]);
        Terminal.obj_type.put(getInt(tokens[0]), 87);// 87 LIGHTS_PN
        // </editor-fold>
    }

    private void mess(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" mess ">
        // [VW_INIT_MESS]
        // #0=A_ID,1=RU,2=EN,3=DE,4=ID_SOUND

        int x;
        switch (Terminal.LANG) {
            case "ru":
                x = 0;
                break;
            case "en":
                x = 1;
                break;
            case "de":
                x = 2;
                break;
            default:
                x = 0;
                break;
        }

        Terminal.Mess.put(getLong(tokens[0]), // A_ID
                getString(tokens[1 + x]) // MESS
        );
        Terminal.Sound.put(getLong(tokens[0]), // A_ID
                getInt(tokens[4]) // ID_SOUND
        );
        // </editor-fold>
    }

    private void mpab(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" mpab ">
//[VI_MPABS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=MPAB_R,10=IND,11=CMD
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 12);// 12 MPAB
        Mpab m = new Mpab(
                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]),// X
                getInt(tokens[5]),// Y
                getInt(tokens[6]),// SHIFT_X
                getInt(tokens[7]),// SHIFT_Y
                getInt(tokens[8]),// VIDEO_STATUS
                getInt(tokens[9]) //MPAB_R //простой МПАБ=0 или МПАБ_Р=1 РПБ=2
        //            getInt(tokens[10]),// IND
        //            getInt(tokens[11])// CMD
        );
        Terminal.Mpab_Hash.put(getInt(tokens[0]), m);
        // </editor-fold>
    }

    private void nearby(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" nearby ">
//[VI_NEARBYS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=ORIENTATION,10=LAYER,11=DIRECTION,12=IND,13=CMD
        // int addShiftX, int addShiftY) {
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 22);// 22 NEARBY
        Nearby nc = new Nearby(
                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]),// X
                getInt(tokens[5]),// Y
                getInt(tokens[6]),// SHIFT_X
                getInt(tokens[7]),// SHIFT_Y
                //                getInt(tokens[8]),// VIDEO_STATUS
                getInt(tokens[9]),// ORIENTATION
                //                getInt(tokens[10]),// LAYER
                (getInt(tokens[11]) == 0)// DIRECTION (стрелочки TRUE = +, FALSE = +
        //                getInt(tokens[12])// IND
        //                getInt(tokens[13]) // CMD
        );
        Terminal.NearbyCell_Hash.put(getInt(tokens[0]), nc);
        // </editor-fold>
    }

    private void note_type(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" note_type ">
        // [VW_INIT_NOTE_OPTIONS2]
        // #0=ID_OBJ,1=NOTE_TEXT

        int x;
        switch (Terminal.LANG) {
            case "ru":
                x = 0;
                break;
            case "en":
                x = 1;
                break;
            case "de":
                x = 2;
                break;
            default:
                x = 1;
                break;
        }

        int id_note = getInt(tokens[0]);
        String note_text = tokens[1 + x];
        Terminal.Note_Menu.put(id_note, note_text);

        // </editor-fold>
    }

    private void note(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" notice ">
        // [VW_INIT_NOTECELL]
        // #0=GX,1=GY,2=ID_GOBJ,3=SHIFT_X,4=SHIFT_Y,5=NOTE_TEXT
        // NoteCell(Viewpoint VP, int GX, int GY, int objId, int shiftX, int
        // shiftY, String text) {
        Note lc = new Note(
                getInt(tokens[0]),// X
                getInt(tokens[1]),// Y
                getInt(tokens[2]),// ID_OBJ
                getInt(tokens[3]),// SHIFT_X
                getInt(tokens[4]),// SHIFT_Y
                getString(tokens[5])// S_NAME
        );
        Terminal.obj_type.put(getInt(tokens[2]), 13);// 13 NOTE
        // </editor-fold>
    }

    private void params(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" params ">
//[VW_INIT_PARAM]
//#0=ID_OBJ,1=UNIT_TYPE,2=PARAM_LINK,3=RU,4=EN,5=DE
        int x;
        switch (Terminal.LANG) {
            case "ru":
                x = 0;
                break;
            case "en":
                x = 1;
                break;
            case "de":
                x = 2;
                break;
            default:
                x = 0;
                break;
        }

//        Parametr p = new Parametr(
//                getInt(tokens[0]),// ID_OBJ
//                getInt(tokens[1]),// UNIT_TYPE
//                getInt(tokens[2]),// LINK
//                tokens[3 + x]// RU
//        );
        Terminal.Parametr_Hash.put(getInt(tokens[2]), tokens[3 + x]);//link, s_name
        // Terminal.obj_type.put(getInt(tokens[0]), xx);
        // </editor-fold>
    }

    private void protect(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" protected ">
//[VI_PROTECTEDS]
//#0=ID_GOBJ,1=ID_OBJ,2=ID_AREA,3=S_NAME,4=IPAR,5=X,6=Y,7=SHIFT_X,8=SHIFT_Y,9=VIDEO_STATUS,10=ORIENTATION,11=LAYER
//Protected(int ID_GOBJ, int ID_OBJ, int ID_AREA, String S_NAME, int IPAR, int GX, int GY, int SHIFT_X, int SHIFT_Y, int VIDEO_STATUS, int ORIENTATION, int LAYER, boolean WITHNAME) {
        Terminal.obj_name.put(getInt(tokens[0]), tokens[3]);
        Terminal.obj_type.put(getInt(tokens[0]), 23);// 23 PROTECTED

        Protected pc = new Protected(
                //                getInt(tokens[0]),// ID_GOBJ
                getInt(tokens[1]),// ID_OBJ
                //                getInt(tokens[2]),// ID_AREA
                getString(tokens[3]),// S_NAME
                //                getInt(tokens[4]),// IPAR
                getInt(tokens[5]),// X
                getInt(tokens[6]),// Y
                getInt(tokens[7]),// SHIFT_X
                getInt(tokens[8]),// SHIFT_Y
                getInt(tokens[9]),// VIDEO_STATUS
                getInt(tokens[10]),// ORIENTATION
                //                getInt(tokens[11]),// LAYER
                (getInt(tokens[4]) > 0)// > 0 = boolean WithName with S_NAME IPAR > 0
        );
        Terminal.ProtectedCell_Hash.put(getInt(tokens[0]), pc);
        // </editor-fold>
    }

    private void railnet_boom(String[] tokens) {// это грузим вперед railnet
        // <editor-fold defaultstate="collapsed" desc=" railnet counter BOOM">
        // [VW_INIT_RAILNET_COUNTER]
//[VI_RAILNET_COUNTERS]
//#0=RN_ID,1=CO_ID
        boomer.add(tokens);

        // </editor-fold>
    }

    private void railnet(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" railnet ">
//[VI_RAILNETS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=ORIENTATION,10=ID_TYPE,11=SKIDABLE,12=BLOCK,13=SHOW_AXLE,14=LENGTH,15=IND,16=CMD,17=STATE,18=STATE_TIME
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 14);// 14 RAILNET
        Railnet rn = new Railnet(
                getInt(tokens[0]), // ID_OBJ
                //                getInt(tokens[1]), // ID_AREA
                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]), // X
                getInt(tokens[5]), // Y
                getInt(tokens[6]), // SHIFT_X
                getInt(tokens[7]), // SHIFT_Y
                getInt(tokens[8]), // VIDEO_STATUS
                getInt(tokens[9]), // ORIENTATION
                getInt(tokens[10]), // ID_TYPE - RN_TYPE числом
                (getInt(tokens[11]) == 1),// SKIDABLE
                (getInt(tokens[12]) == 1),// boom (BLOCK)
                (getInt(tokens[13]) == 1),// SHOW_AXLE
                //                getInt(tokens[14]), // LENGTH
                //                getInt(tokens[15]), // IND
                //                getInt(tokens[16]), // CMD
                (getInt(tokens[17]) == 1) // VGN_LST
        );
        Terminal.Railnets_Hash.put(getInt(tokens[0]), rn);
        // </editor-fold>
    }

    private void turnout(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" turnout ">
//[VI_TURNOUTS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=ORIENTATION,10=SIDE,11=PLUS_DIR,12=RN_ID,13=ID_TYPE,14=ID_TWIN,15=BACKWARD,16=IND,17=CMD,18=STATE,19=STATE_TIME
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 17);// 17 TURNOUT
        Turnout tc = new Turnout(
                getInt(tokens[0]), // ID_OBJ
                //                getInt(tokens[1]), // ID_AREA
                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]), // X
                getInt(tokens[5]), // Y
                getInt(tokens[6]), // SHIFT_X
                getInt(tokens[7]), // SHIFT_Y
                getInt(tokens[8]), // VIDEO_STATUS
                getInt(tokens[9]), // ORIENTATION
                getInt(tokens[10]), // SIDE
                (getInt(tokens[11]) == 1),// PLUS_DIR
                getInt(tokens[12]), // RN_ID
                getInt(tokens[13]), // ID_TYPE
                getInt(tokens[14]), // ID_TWIN
                (getInt(tokens[15]) == 1), // BACKWARD 1=требуется перевод сбрасывающей стрелки в начальное положение
                //                getInt(tokens[16]), // IND
                //                getInt(tokens[17])  // CMD
                getInt(tokens[18]), // DKP
                getInt(tokens[19]), // DKM
                (getInt(tokens[20]) == 1) // WFAN 1= у стрелки есть обдувка
        );
        Terminal.Turnouts_Hash.put(getInt(tokens[0]), tc);
        // ещё надо добавить все direct принадлежащие этой стрелке
        // </editor-fold>
    }

    private void turnout_set_twin() { // тут вычисляем главную и ведущую
        Terminal.Turnouts_Hash.values().stream().filter((tc) -> (tc.id_obj != tc.getCmdSender())).forEach((tc) -> Terminal.Turnouts_Hash.values().stream().filter((tc1) -> (tc.getCmdSender() == tc1.id_obj)).forEach(tc::setMainLogObj));
    }

    private void unit(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" unit ">
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);//??????????????????????
        Terminal.obj_type.put(getInt(tokens[0]), 18);// 18 UNIT ///?????????????????
        Unit u = new Unit(
                getInt(tokens[0]),// ID_OBJ
                getInt(tokens[1]),// CABINET
                getInt(tokens[2]),// RACK
                getInt(tokens[3]),// PLACE
                getInt(tokens[4]),// UNIT_TYPE (8 or 9)
                getInt(tokens[5]),// LOG_ADDRESS - нет логадреса у блока питания (в базе поставил -1 вместо null)
                tokens[6],// S_NAME
                //                getInt(tokens[7]),// PARAM_OFFSET
                getInt(tokens[8]),// PARAM_BYTES
                //                getInt(tokens[9]),// CHANNELS_OFFSET - нет офсета у блока питания (в базе поставил 0 вместо null)
                getInt(tokens[10]),// CHANNELS_BYTES - нет байт у блока питания (в базе поставил 0 вместо null)
                tokens[11]// TYPE_NAME (DI or PS)
        );
        Terminal.Unit_Hash.put(getInt(tokens[0]), u);

        Cabinet c = Terminal.Cabinets_Hash.get(getInt(tokens[1]));
        c.getRack(getInt(tokens[2])).plugUnit(
                //                getInt(tokens[3]),//int PLACE
                u
        );//----------------!!!!!! ЕСЛИ НЕТ ТАКОЙ ПОЛКИ ТО ОШИБКА
        // </editor-fold>
    }

    private void f3_dio_20_8(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" f3_dio_20_8 ">
        // [VW_INIT_UNIT]
        // #0=ID_OBJ,1=CABINET_ID,2=RACK,3=PLACE,4=UNIT_TYPE,5=LOG_ADDRESS,6=S_NAME,7=PARAM_OFFSET,8=PARAM_BYTES,9=CHANNELS_OFFSET,10=CHANNEL_BYTES,11=TYPE_NAME
        // 349,344,1,1,7,null,блок питания PS1,0,1,null,null
        // Unit(int ID_OBJ, int CABINET_ID, int RACK, int PLACE, int UNIT_TYPE,
        // int LOG_ADDRESS, String S_NAME, int PARAM_OFFSET, int PARAM_BYTES,
        // int CHANNELS_OFFSET, int CHANNEL_BYTES) {
        Terminal.obj_name.put(getInt(tokens[0]), tokens[11] + " " + getInt(tokens[5]));//S_NAME
        Terminal.obj_type.put(getInt(tokens[0]), 30);// 30 F3_DIO_20_8
        f3_dio_20_8_02 u = new f3_dio_20_8_02(
                getInt(tokens[0])// ID_OBJ
        //                getInt(tokens[1]),// CABINET
        //                getInt(tokens[2]),// RACK
        //                getInt(tokens[3]),// PLACE
        //                getInt(tokens[4]),// UNIT_TYPE (8 or 9)
        //                getInt(tokens[5]),// LOG_ADDRESS - нет логадреса у блока питания (в базе поставил -1 вместо null)
        //                tokens[6]// S_NAME
        //                getInt(tokens[7]),// PARAM_OFFSET
        //                getInt(tokens[8]),// PARAM_BYTES
        //                getInt(tokens[9]),// CHANNELS_OFFSET - нет офсета у блока питания (в базе поставил 0 вместо null)
        //                getInt(tokens[10]),// CHANNELS_BYTES - нет байт у блока питания (в базе поставил 0 вместо null)
        //                tokens[11]// TYPE_NAME (DI or PS)
        );
        Terminal.f3_dio_20_8.put(getInt(tokens[0]), u);

//        Cabinet c = Terminal.Cabinets_Hash.get(getInt(tokens[1]));
//        c.getRack(getInt(tokens[2])).plugUnit(getInt(tokens[3]), u);//----------------!!!!!! ЕСЛИ НЕТ ТАКОЙ ПОЛКИ ТО ОШИБКА
        // </editor-fold>
    }

    private void unrulled(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" unrulled ">
//[VI_UNRULEDS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=ORIENTATION
        Terminal.obj_type.put(getInt(tokens[0]), 29);// 29 UNRULED
        Unruled uc = new Unruled(
                //                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                //                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]),// X
                getInt(tokens[5]),// Y
                getInt(tokens[6]),// SHIFT_X
                getInt(tokens[7]),// SHIFT_Y
                getInt(tokens[8]),// VIDEO_STATUS
                getInt(tokens[9]) // ORIENTATION
        );
        // </editor-fold>
    }

    private void vc(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" voltage_control ">
//[VI_VCS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=FIRST_FEEDER,10=SECOND_FEEDER,11=THIRD_FEEDER,12=LOW_VOLTAGE,13=FUSE,14=GROUNDING,15=AMPERES,16=CURRENT_PRESENCE,17=UPS,18=FIRE,19=DOOR_BREAK,20=OGSP,21=AMPLIFICATION,22=DRIFT_ZERO,23=STEPSCALE,24=IND,25=CMD
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 19);// 19 VOLTAGE_CONTROL
        VC vc = new VC(
                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]),// X
                getInt(tokens[5]),// Y
                getInt(tokens[6]),// SHIFT_X
                getInt(tokens[7]),// SHIFT_Y
                getInt(tokens[8]),// VIDEO_STATUS
                (getInt(tokens[9]) == 1),// FIRST_FEEDER
                (getInt(tokens[10]) == 1),// SECOND_FEEDER
                (getInt(tokens[11]) == 1),// THIRD_FEEDER
                (getInt(tokens[12]) == 1),// LOW_VOLTAGE
                (getInt(tokens[13]) == 1),// FUSE
                (getInt(tokens[14]) == 1),// GROUNDING
                (getInt(tokens[15]) == 1),// AMPERES
                //                (getInt(tokens[16]) == 1),// CURRENT_PRESENCE
                (getInt(tokens[17]) == 1),// UPS
                (getInt(tokens[18]) == 1),// FIRE
                (getInt(tokens[19]) == 1),// DOOR
                (getInt(tokens[20]) == 1),// OGSP
                getInt(tokens[21]),//AMPLIFICATION
                getInt(tokens[22]),//DRIFT_ZERO
                getInt(tokens[23])//STEPSCALE
        // (getInt(tokens[21]) == 1)//UPS_OUT //для подольска контроль есть ли напряжение на выходе ИБП
        );
        Terminal.VCs_Hash.put(getInt(tokens[0]), vc);
        // </editor-fold>
    }

    private void weigher(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" weigher ">
//[VI_WEIGHERS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=ORIENTATION
        Weigher wc = new Weigher(
                //                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]),// X
                getInt(tokens[5]),// Y
                getInt(tokens[6]),// SHIFT_X
                getInt(tokens[7]), // SHIFT_Y
                getInt(tokens[8]),// VIDEO_STATUS
                getInt(tokens[9]) // ORIENTATION
        );
        // </editor-fold>
    }

    private void elevatedtrack(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" elevatedtrack ">
//[VI_ELEVATEDTRACKS]
//#0=ID_OBJ,1=ID_AREA,2=RU,3=X,4=Y,5=SHIFT_X,6=SHIFT_Y,7=VIDEO_STATUS,8=ORIENTATION,9=DE,10=EN,11=IND,12=CMD
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 82);// 82 ELEVATEDTRACK
        ElevatedTrack et = new ElevatedTrack(
                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                //                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[3]),// X
                getInt(tokens[4]),// Y
                getInt(tokens[5]),// SHIFT_X
                getInt(tokens[6]), // SHIFT_Y
                getInt(tokens[7]),// VIDEO_STATUS
                getInt(tokens[8]) // ORIENTATION
        //                getInt(tokens[11]) // IND
        //                getInt(tokens[12]) // CMD
        );
        Terminal.ElevatedTrackCell_Hash.put(getInt(tokens[0]), et);
        // </editor-fold>
    }

    private void uksps(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" uksps ">
//[VI_UKSPS]
//#0=ID_OBJ,1=X,2=Y,3=ORIENTATION,4=VIDEO_STATUS,5=SHIFT_X,6=SHIFT_Y,7=RU
        int x;
        switch (Terminal.LANG) {
            case "ru":
                x = 0;
                break;
            case "en":
                x = 1;
                break;
            case "de":
                x = 2;
                break;
            default:
                x = 0;
                break;
        }
        Terminal.obj_name.put(getInt(tokens[0]), tokens[7 + x]);
        Terminal.obj_type.put(getInt(tokens[0]), 84);// 84 UKSPS
        Uksps uc = new Uksps(
                getInt(tokens[0]),// ID_OBJ
                getInt(tokens[1]),// X
                getInt(tokens[2]),// Y
                getInt(tokens[3]),// ORIENTATION
                getInt(tokens[4]),// VIDEO_STATUS
                getInt(tokens[5]),// SHIFT_X
                getInt(tokens[6]), // SHIFT_Y
                tokens[7 + x]//RU
        );
        // </editor-fold>
    }

    private void dga(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" dga ">
//[VI_DGAS]
//#0=ID_OBJ,1=RU,2=X,3=Y,4=SHIFT_X,5=SHIFT_Y,6=VIDEO_STATUS
        Terminal.obj_name.put(getInt(tokens[0]), tokens[1]);
        Terminal.obj_type.put(getInt(tokens[0]), 80);// 80 DGA
        Dga dc = new Dga(
                getInt(tokens[0]),// ID_OBJ
                tokens[1],// RU
                getInt(tokens[2]),// X
                getInt(tokens[3]),// Y
                getInt(tokens[4]),// SHIFT_X
                getInt(tokens[5]), // SHIFT_Y
                getInt(tokens[6])// VIDEO_STATUS
        );
        // </editor-fold>
    }

    private void heat(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" heating ">
//[VI_HEATS]
//#0=ID_OBJ,1=S_NAME
        Terminal.obj_name.put(getInt(tokens[0]), "муфта обогрева" + tokens[1]);
        Terminal.obj_type.put(getInt(tokens[0]), 85);// 85 HEAT
        Heat h = new Heat(
                getInt(tokens[0]),// ID_OBJ
                tokens[1]// S_NAME
        );
        Terminal.Heat_Hash.put(getInt(tokens[0]), h);
        // </editor-fold>
    }

    private void heating(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" heating ">
//[VI_HEATINGS]
//#0=ID_OBJ,1=ID_AREA,2=RU,3=X,4=Y,5=SHIFT_X,6=SHIFT_Y,7=VIDEO_STATUS
        int x;
        switch (Terminal.LANG) {
            case "ru":
                x = 0;
                break;
            case "en":
                x = 1;
                break;
            case "de":
                x = 2;
                break;
            default:
                x = 0;
                break;
        }
        Terminal.obj_name.put(getInt(tokens[0]), tokens[6 + x]);
        Terminal.obj_type.put(getInt(tokens[0]), 83);// 83 HEATING
        Heating hc = new Heating(
                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                tokens[2 + x],// S_NAME
                getInt(tokens[3]),// X
                getInt(tokens[4]),// Y
                getInt(tokens[5]),// SHIFT_X
                getInt(tokens[6]), // SHIFT_Y
                getInt(tokens[7])// VIDEO_STATUS
        );
        // </editor-fold>
    }

    private void gate(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" gate ">
//[VI_GATE]
//#0=ID_OBJ,1=ID_AREA,2=RU,3=X,4=Y,5=SHIFT_X,6=SHIFT_Y,7=VIDEO_STATUS,8=ORIENTATION,11=IND,12=CMD
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 21);// 21 ELEVATEDTRACK
        Gate g = new Gate(
                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                //                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[3]),// X
                getInt(tokens[4]),// Y
                getInt(tokens[5]),// SHIFT_X
                getInt(tokens[6]), // SHIFT_Y
                getInt(tokens[7]),// VIDEO_STATUS
                getInt(tokens[8]) // ORIENTATION
        //                getInt(tokens[11]) // IND
        //                getInt(tokens[12]) // CMD
        );
        Terminal.Gate_Hash.put(getInt(tokens[0]), g);
        // </editor-fold>
    }

    private void pushers(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" pushers ">
//[VI_PUSHERS]
//#0=ID_OBJ,1=ID_AREA,2=RU,3=X,4=Y,5=SHIFT_X,6=SHIFT_Y,7=VIDEO_STATUS,8=ORIENTATION,9=DE,10=EN,11=IND,12=CMD
        Terminal.obj_name.put(getInt(tokens[0]), tokens[2]);
        Terminal.obj_type.put(getInt(tokens[0]), 29);// 29 PUSHERS
        Pusher p = new Pusher(
                getInt(tokens[0]),// ID_OBJ
                //                getInt(tokens[1]),// ID_AREA
                                tokens[2], // S_NAME
                //                tokens[3], // MPC_NAME
                getInt(tokens[4]),// X
                getInt(tokens[5]),// Y
                getInt(tokens[6]),// SHIFT_X
                getInt(tokens[7]), // SHIFT_Y
                getInt(tokens[8]),// VIDEO_STATUS
                getInt(tokens[9]) // ORIENTATION
                //type
        //                getInt(tokens[11]) // IND
        //                getInt(tokens[12]) // CMD
        );
        Terminal.Pusher_Hash.put(getInt(tokens[0]), p);
        // </editor-fold>
    }

    private void blokpost(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" blokpost ">
//[VI_PUSHERS]
//#0=ID_OBJ,1=ID_AREA,2=S_NAME,3=MPC_NAME,4=X,5=Y,6=SHIFT_X,7=SHIFT_Y,8=VIDEO_STATUS,9=ORIENTATION,10=TYPE,11=IND,12=CMD
        Terminal.obj_name.put(getInt(tokens[0]), tokens[1]);
        Terminal.obj_type.put(getInt(tokens[0]), 31);// 31 us установка сигнальная (блок пост)
        Blokpost b = new Blokpost(
                getInt(tokens[0]),// ID_OBJ
                tokens[1], // S_NAME
                getInt(tokens[2]),// X
                getInt(tokens[3]),// Y
                getInt(tokens[4]),// SHIFT_X
                getInt(tokens[5]),// SHIFT_Y
                getInt(tokens[6]),// VIDEO_STATUS
                getInt(tokens[7]) // TYPE

        );
        Terminal.Blokpost_Hash.put(getInt(tokens[0]), b);
        // </editor-fold>
    }

    private void windows(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" windows ">
        /*
1	Clock
2	Event
3	Alarm
4	CPU
5	CO (CounterList)
6	Simulator
7	Commander
8	Vgn
9	Axis (RailnetAxis)
10	About
11      UnitList
         */
//[VI_WINDOWS]
//#0=ID_OBJ,1=X,2=Y,3=W,4=H
        int ID_OBJ = getInt(tokens[0]);//ID_OBJ
        int X = getInt(tokens[1]);//X
        int Y = getInt(tokens[2]);//Y
        int W = getInt(tokens[3]);//W
        int H = getInt(tokens[4]);//H

        switch (ID_OBJ) {
            case 1://clock
                Terminal.CLOCK_X = X;
                Terminal.CLOCK_Y = Y;
                Terminal.CLOCK_WIDTH = W;
                Terminal.CLOCK_HEIGHT = H;
                break;
            case 2://Event
                Terminal.EVENTER_X = X;
                Terminal.EVENTER_Y = Y;
                Terminal.EVENTER_WIDTH = W;
                Terminal.EVENTER_HEIGHT = H;
                break;
            case 3://Alarm
                Terminal.ALARMER_X = X;
                Terminal.ALARMER_Y = Y;
                Terminal.ALARMER_WIDTH = W;
                Terminal.ALARMER_HEIGHT = H;
                break;
            case 4://CPU
                Terminal.CPU_MONITOR_X = X;
                Terminal.CPU_MONITOR_Y = Y;
                Terminal.CPU_MONITOR_WIDTH = W;
                Terminal.CPU_MONITOR_HEIGHT = H;
                break;
            case 5://CO (CounterList)
                Terminal.CO_X = X;
                Terminal.CO_Y = Y;
                Terminal.CO_WIDTH = W;
                Terminal.CO_HEIGHT = H;
                break;
            case 6://Simulator
                Terminal.SIM_X = X;
                Terminal.SIM_Y = Y;
                Terminal.SIM_WIDTH = W;
                Terminal.SIM_HEIGHT = H;
                break;
            case 7://Commander
                Terminal.COMMANDER_X = X;
                Terminal.COMMANDER_Y = Y;
                Terminal.COMMANDER_WIDTH = W;
                Terminal.COMMANDER_HEIGHT = H;//
                break;
            case 8://Vgn
                Terminal.VGN_X = X;
                Terminal.VGN_Y = Y;
                Terminal.VGN_WIDTH = W;
                Terminal.VGN_HEIGHT = H;
                break;
            case 9://Axis
                Terminal.AXIS_X = X;
                Terminal.AXIS_Y = Y;
                Terminal.AXIS_WIDTH = W;
                Terminal.AXIS_HEIGHT = H;
                break;
            case 10://About
                Terminal.ABOUT_X = X;
                Terminal.ABOUT_Y = Y;
                Terminal.ABOUT_WIDTH = W;
                Terminal.ABOUT_HEIGHT = H;
                break;
            case 11://UnitList
                Terminal.UNIT_X = X;
                Terminal.UNIT_Y = Y;
                Terminal.UNIT_WIDTH = W;
                Terminal.UNIT_HEIGHT = H;
                break;
            case 12://Log (play)
                Terminal.LOG_X = X;
                Terminal.LOG_Y = Y;
                Terminal.LOG_WIDTH = W;
                Terminal.LOG_HEIGHT = H;
                break;
        }
        // </editor-fold>
    }

    private void txt(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" txt ">
        // [VI_TXT]

        String SKEY = tokens[0];
        String STRINGS = tokens[1];
        Terminal.Txt.put(SKEY, STRINGS);

        // </editor-fold>
    }

    private void vzrez(String[] tokens) {
        if (Terminal.VZREZ) {
            /*
            //заполнять в таблице нужно опасное положение!
UPDATE "ZAPSIB2"."VZREZ" SET POSITION = '3' WHERE position = 1;
UPDATE "ZAPSIB2"."VZREZ" SET POSITION = '1' WHERE position = 0;
UPDATE "ZAPSIB2"."VZREZ" SET POSITION = '0' WHERE position = 3;
             */
            // <editor-fold defaultstate="collapsed" desc=" vzrez ">
//[VI_VZREZ]
//#0=COUNTER_ID,1=TURNOUT_ID,2=DIRECTION,3=CONTROL,4=POSITION
            int COUNTER_ID = getInt(tokens[0]);//7083
            int TURNOUT_ID = getInt(tokens[1]);//17072
            int DIRECTION = getInt(tokens[2]);//0 <-
            int CONTROL = getInt(tokens[3]); //0 - учитывать
            int POSITION = getInt(tokens[4]);//1 - plus
            Log.log(COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + "," + CONTROL + "," + POSITION);
            if (CONTROL == 0) {//по шерсти

                int TEST = (((TURNOUT_ID << 1) | (DIRECTION & 0x1)) << 2) | (POSITION & 0x3);
//            Log.log("+++++ TEST ++++  = " + TEST + " TR - " + TURNOUT_ID + " DIR - " + DIRECTION + " pos - " + POSITION);
                for (Integer key : Terminal.CounterCell_Hash.keySet()) {
                    if (key == getInt(tokens[0])) {
                        Counter c = Terminal.CounterCell_Hash.get(key);
                        c.vzrez.put(TEST, TURNOUT_ID);
                    }
                }
            }
            Terminal.Vzrez_Hash.put(Terminal.Vzrez_num, tokens);
            Terminal.Vzrez_num++;
            // </editor-fold>
        }
    }

    private void vs(String[] tokens) {//vzrez siemens
        if (Terminal.VZREZ) {
//[VI_VS]
//#0=ID_OBJ,1=IND
            
            int ID_OBJ = getInt(tokens[0]);//7083
            int IND = getInt(tokens[1]);//17072
            int TR_ID = getInt(tokens[2]);//17072
            Vzrez v = new Vzrez(ID_OBJ, IND, TR_ID);
            Terminal.Vs_Hash.put(ID_OBJ, v);
            // </editor-fold>
        }
    }
    
    private void obj_types(String[] tokens) {
        // <editor-fold defaultstate="collapsed" desc=" obj_type_name ">

        int x;
        switch (Terminal.LANG) {
            case "ru":
                x = 0;
                break;
            case "en":
                x = 1;
                break;
            case "de":
                x = 2;
                break;
            default:
                x = 0;
                break;
        }

        int TYPE_ID = getInt(tokens[0]);// TYPE_ID
        String S_NAME = tokens[2 + x];
        Terminal.obj_type_name.put(TYPE_ID, S_NAME);
        // </editor-fold>
    }

    void run(String ResourceFile) {
        // <editor-fold defaultstate="collapsed" desc=" start ">

        Terminal.commander = new Commander();// init windows

        LoadFromCSV(ResourceFile);

        Alarms.load_alarm();

//------------------заполним пустые модули-------------------
        Cabinets_Hash.keySet().forEach((k) -> {
            Cabinet c;
            Rack r;
            c = Cabinets_Hash.get(k);
            int grid_col;
            if (c.type != 0) {
                grid_col = 5;//hima
            } else {
                grid_col = 12;//siemens
            }
            for (int i = 1; i <= c.racks; i++) {
                r = c.getRack(i);

                r.fillHash(grid_col);//Для сименса 12 штук, для Hima 5 штук
            }
        });
//------------------заполним каналы у моуля-------------------
        Terminal.Unit_Hash.keySet().forEach((u) -> {
            Unit un;
            un = Terminal.Unit_Hash.get(u);
            Terminal.Channels_Hash.keySet().stream().filter((c) -> (Terminal.Channels_Hash.get(c).unit_id == u)).forEach((c) -> un.addChannel(Terminal.Channels_Hash.get(c).link,// link_number"),
                    Terminal.Channels_Hash.get(c)));
        });
// --------------------------------------------------------
        cpuMon = new CpuMon();
        adjustCpuMon();
        cmdLayers.setLayer(cmdLayers.add(cpuMon), 30);
        cpuMon.setVisible(Terminal.CPU_MONITOR_VISIBLE);

        Splash.jProgressBar1.setValue(50);
        Splash.lblText.setText("Анализ состояния модулей МПС...");

        boomer = null;// ArrayList boomer больше не нужен.

        // </editor-fold>
    }

}

// 6 CONTROL_UNIT
// 12 SIMULATOR
// 16 TECHZONE
// 25 VERSION
// 26 USER
// 100


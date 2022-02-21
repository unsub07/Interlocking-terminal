//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
// /home/user/netbeans8.2/etc/netbeans.conf -> netbeans_default_options=".... --fontsize 16"
// /home/user/.sqldeveloper/system17.3.1.279.0537/o.sqldeveloper/ide.properties -> Ide.FontSize.en=14
// Сделать HashMap для всех String и хранить его в конфигурационном файле.
package terminal;

import java.util.ArrayList;
import java.util.HashMap;

class Terminal {

    // <editor-fold defaultstate="collapsed" desc="//Variables declaration">
//    static lt_popup lp;// = new lt_popup();
    static int DEF_WIDTH_B = 84;
    static int DEF_HEIGHT_B = 84;
    static int DEF_WIDTH_S = 72;
    static int DEF_HEIGHT_S = 72;
    static int TunePngX = -6;
    static int TunePngY = -6;
    static TicketHandler TH;// = new TicketHandler();
    static Recieve_Thread RT;// = new TicketHandler();
    static Net net;
    static final String FS = java.lang.System.getProperty("file.separator");
    static final java.text.SimpleDateFormat SDF1 = new java.text.SimpleDateFormat("HH:mm:ss");// time
    static final java.text.SimpleDateFormat SDF2 = new java.text.SimpleDateFormat("dd.MM.yyy");// date
    static final java.text.SimpleDateFormat SDF3 = new java.text.SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");// date_time
    // hashes
    static final HashMap<String, java.awt.Color> ColorsHash = new HashMap<>(4);
    static final HashMap<Integer, Cell> mainCellsHash = new HashMap<>(400);
    static final HashMap<Integer, Area> Area_Hash = new HashMap<>(1);
    static final HashMap<Integer, Crossing> Crossings_Hash = new HashMap<>(4);
    static final HashMap<Integer, Counter> CounterCell_Hash = new HashMap<>(50);
    static final HashMap<Integer, CountUnit> CountUnit_Hash = new HashMap<>(4);
    static final HashMap<Integer, Beam> Beams_Hash = new HashMap<>(6);
    static final HashMap<Integer, String> Parametr_Hash = new HashMap<>(15);//for unit
    static final HashMap<Integer, Unit> Unit_Hash = new HashMap<>(50);
    static final HashMap<Integer, f3_dio_20_8_02> f3_dio_20_8 = new HashMap<>(25);
    static final HashMap<Integer, Heat> Heat_Hash = new HashMap<>(6);
    static final HashMap<Integer, Heating> Heating_Hash = new HashMap<>(1);
    static final HashMap<Integer, Dga> DGA_Hash = new HashMap<>(1);
    static final HashMap<Integer, Uksps> Uksps_Hash = new HashMap<>(1);
    static final HashMap<Integer, Cabinet> Cabinets_Hash = new HashMap<>(10);// Шкафы
    static final HashMap<Integer, Channels> Channels_Hash = new HashMap<>(400);
    static final HashMap<Integer, PLC> PLCs_Hash = new HashMap<>(2);// для физических CPU
    static final HashMap<Integer, Deadlock> DeadlockCell_Hash = new HashMap<>(10);
    static final HashMap<Integer, Direct> DirectCell_Hash = new HashMap<>(100);
    static final HashMap<Integer, Fan> FanCell_Hash = new HashMap<>(1);
    static final HashMap<Integer, Light> LightsCell_Hash = new HashMap<>(50);
    static final HashMap<Integer, Nearby> NearbyCell_Hash = new HashMap<>(4);
    static final HashMap<Integer, Protected> ProtectedCell_Hash = new HashMap<>(8);
    static final HashMap<Integer, Railnet> Railnets_Hash = new HashMap<>(50); // для набрасывателя осей.
    static final HashMap<Integer, ElevatedTrack> ElevatedTrackCell_Hash = new HashMap<>(4);
    static final HashMap<Integer, Turnout> Turnouts_Hash = new HashMap<>(50);
    static final HashMap<Integer, Mpab> Mpab_Hash = new HashMap<>(1);
    static final HashMap<Integer, VC> VCs_Hash = new HashMap<>(2);
    static final HashMap<String, javax.swing.ImageIcon> mainPictureHash = new HashMap<>(700);
    static final HashMap<Integer, byte[]> mainAudioHash = new HashMap<>(2);
    static final HashMap<Integer, String> Note_Menu = new HashMap<>(8);// для менюшки Note
    static final HashMap<Long, String> Mess = new HashMap<>(332);// Все сообщения
    static final HashMap<Long, Integer> Sound = new HashMap<>(332);// соответствие ID-сообщения с ID-музыки
    static final HashMap<String, Long> Cmd = new HashMap<>(46);// Все комманды
    static final HashMap<Integer, String> obj_name = new HashMap<>(442);// Все имена объектов (колличество объектов)
    static final HashMap<Integer, String> obj_type_name = new HashMap<>(36);// int
    static final HashMap<Integer, Integer> obj_type = new HashMap<>(38);// int
    static final HashMap<Integer, Gate> Gate_Hash = new HashMap<>(1);// int
    static final HashMap<Integer, Pusher> Pusher_Hash = new HashMap<>(1);// int
    static final HashMap<Integer, Blokpost> Blokpost_Hash = new HashMap<>(1);// int
    static final HashMap<Integer, String[]> Vzrez_Hash = new HashMap<>(171);// int
    static final HashMap<Integer, Vzrez> Vs_Hash = new HashMap<>(42);// vzrez siemens
    static final HashMap<String, String> Txt = new HashMap<>(46);// Все тексты
    static int Vzrez_num = 0;
    static ArrayList<String[]> boomer = new ArrayList<>();// для railnet ограждение путей составов...
    static boolean init_completed;
    static boolean VZREZ = false;
    static boolean DIRECTION = false;        //показывать направление или нет
    static boolean play = false;            //плеер
    static boolean crossing = false;//azot
    static boolean counter = false;//рисовать статус датчика(эксперементальная функция)
    static boolean kbd = true;
    static boolean noGuest = true;
    static boolean DSP = false;
    static boolean SHN = false;
    static boolean SIM = false;
    static boolean SEC = false;
    static boolean GST = false;
    static boolean ALARMER_VISIBLE = false;
    static boolean CLOCK_VISIBLE = true;
    static boolean CPU_MONITOR_VISIBLE = false;
    static boolean EVENTER_VISIBLE = false;
    static boolean left_mobitor = true;//
    static boolean withoutsound = false;
    static boolean full_screen = false;
    static boolean OBDUV = false;
    static boolean debug = false;// вывод дополнительной информации на консоль для отладки...
    static boolean counterTableReady = false;
    static Commander commander;
    static String LOGNAME = "";
    static String NAME_1 = "";
    static String NAME_2 = "";
    static String NAME_3 = "";
    static String FIO;// tanslane(LOGNAME) + NAME_1 + NAME_2 + NAME_3
    static String PASSWORD = "";// hash.hash256
    static String ResourceFile = "15.att";
    static String ATTRANS_HOME;// System variable
    static String role;
    static String MPC_NAME = "alarm";
    static String MULTICAST_GROUP = "225.001.001.001";//239.1.1.232//225.1.1.1
    static int CYCLE_TIME = 50;
    static int CPU = 1;//Тип процессора (0 - Siemens (TSAP - 102 порт), 1 - HIMA, 2 - Siemens (Fetch & Write))
    static int CELL_SHIFT = 0;
    static int ALARMER_HEIGHT = 250;
    static int ALARMER_WIDTH = 1_400;
    static int ALARMER_X = 1_000;
    static int ALARMER_Y = 1_126;
    static int CLOCK_HEIGHT = 130;
    static int CLOCK_WIDTH = 300;
    static int CLOCK_X = 10;
    static int CLOCK_Y = 10;
    static int COMMANDER_HEIGHT = 2_519;//
    static int COMMANDER_WIDTH = 2_519;
    static int COMMANDER_X = 0;
    static int COMMANDER_Y = 0;
    static int CPU_MONITOR_HEIGHT = 1_000;
    static int CPU_MONITOR_WIDTH = 760;
    static int CPU_MONITOR_X = 1_400;
    static int CPU_MONITOR_Y = 0;
    static int EVENTER_HEIGHT = 250;
    static int EVENTER_WIDTH = 1_000;
    static int EVENTER_X = 0;
    static int EVENTER_Y = 1_126;
    static int SIM_X = 100;
    static int SIM_Y = 200;
    static int SIM_WIDTH = 600;
    static int SIM_HEIGHT = 400;
    static int VGN_X = 0;
    static int VGN_Y = 600;
    static int VGN_WIDTH = 1000;
    static int VGN_HEIGHT = 300;
    static int PLAYER_X = 400;
    static int PLAYER_Y = 0;
    static int PLAYER_WIDTH = 800;
    static int PLAYER_HEIGHT = 100;
    static int CO_X = 0;
    static int CO_Y = 0;
    static int CO_WIDTH = 720;
    static int CO_HEIGHT = 1540;
    static int AXIS_X = 0;
    static int AXIS_Y = 0;
    static int AXIS_WIDTH = 500;
    static int AXIS_HEIGHT = 90;
    static int ABOUT_X = 0;
    static int ABOUT_Y = 0;
    static int ABOUT_WIDTH = 460;
    static int ABOUT_HEIGHT = 420;
    static int UNIT_X = 0;
    static int UNIT_Y = 0;
    static int UNIT_WIDTH = 960;
    static int UNIT_HEIGHT = 1030;
    static int LOG_X = 0;
    static int LOG_Y = 0;
    static int LOG_WIDTH = 960;
    static int LOG_HEIGHT = 1030;
    static int NETWORK_BUFFER_SIZE = 200_000;
    static String[] USR_GROUPS = {""};
    static String[] USR_MANAGED_AREA;
    static final String LANG = "ru";// ENGLISH = "en", FRENCH = "fr", GERMAN = "de",
    static String MULTICAST_PORT = /* "12345"; */ "23456";
    private static long tm;// = System.nanoTime();
    private static int color_shema = 0;
    static int zoom = 72;
    private static int timeout = 1500;//таймаут милисикунд для тикет хэндлера

//    static final java.awt.Font SANS12 = new java.awt.Font("sansserif", java.awt.Font.BOLD, 12);//standart 0
    static java.awt.Font SANS12;// = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new java.io.File("/opt/attrans/font/a.ttf")).deriveFont(java.awt.Font.BOLD, 12f);

    static java.awt.Font SANS08;
    static java.awt.Font SANS09;// = SANS12.deriveFont(SANS12.getStyle() | java.awt.Font.BOLD, SANS12.getSize() - 3);//9  -3
    static java.awt.Font SANS10;// = SANS12.deriveFont(SANS12.getStyle() | java.awt.Font.BOLD, SANS12.getSize() - 2);//10 -2
    static java.awt.Font SANS11;// = SANS12.deriveFont(SANS12.getStyle() | java.awt.Font.BOLD, SANS12.getSize() - 1);//11 -1

    static java.awt.Font SANS13;// = SANS12.deriveFont(SANS12.getStyle() | java.awt.Font.BOLD, SANS12.getSize() + 1);//13 +1
    
    static java.awt.Font SANS14;// = SANS12.deriveFont(SANS12.getStyle() | java.awt.Font.BOLD, SANS12.getSize() + 2);//14 +2
    static java.awt.Font SANS14P;// = SANS12.deriveFont(SANS12.getStyle() & ~java.awt.Font.BOLD, SANS12.getSize() + 2);//14 +2   PLAIN
    static java.awt.Font SANS18;// = SANS12.deriveFont(SANS12.getStyle() | java.awt.Font.BOLD, SANS12.getSize() + 4);//18 +4
    static java.awt.Font SANS18P;// = SANS12.deriveFont(SANS12.getStyle() & ~java.awt.Font.BOLD, SANS12.getSize() + 4);//18 +4 PLAIN

    static final javax.swing.Timer TIMER600 = new javax.swing.Timer(600, null);

    static String host = null;
    static int port = 8_081;

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="main">
    public static void main(java.lang.String[] args) {
        
        
        ATTRANS_HOME = System.getenv("ATTRANS_HOME");// получить env
        if (args.length != 0) {
            Args(args);
        }
        if ("".equals(ATTRANS_HOME) || ATTRANS_HOME == null || "null".equals(ATTRANS_HOME)) {
            Log.log("NOT SET VARIABLE - ATTRANS_HOME !!!");
            System.exit(1);
        }
//        new Err();
        new CustomFont();
// -------------------------------------------------------------------
//        String s = ATTRANS_HOME + FS + "update.jar";
//
//        if (test_file(s)) {
//            String[] a = {"java", "-jar", s};
//            try {
//                Runtime.getRuntime().exec(a);
//            } catch (java.io.IOException e) {
//                Err.err(e);
//            }
//        }
// -------------------------------------------------------------------
        Thread sigHandler;
        sigHandler = new Thread(
                () -> {
                    Exit.setEXIT(true);
                    Log.log("SIGKILL received");
                    if (Commander.alarm != null) {
                        int ok = Alarms.save_alarm();//save alarms.
                    }
                    Net.Disconnect();
                });
        Runtime.getRuntime().addShutdownHook(sigHandler);
// -----------------------------------------------------------------
        new Ticket3().start();

        String s = ATTRANS_HOME + FS + "lib" + FS + "libs" + Terminal.color_shema;
        if (test_file(s)) {
            Util.getRes("libs" + Terminal.color_shema);
        } else {
            Terminal.color_shema = 0;
            if (test_file(s)) {
                Util.getRes("libs" + Terminal.color_shema);
                    } else {
                Log.log("not found resource file in lib " + s);
                System.exit(1);
            }
        }
        
        DEF_WIDTH_B  = Terminal.mainPictureHash.get("big_square").getIconWidth();
        DEF_HEIGHT_B = Terminal.mainPictureHash.get("big_square").getIconHeight();
        DEF_WIDTH_S  = Terminal.mainPictureHash.get("small_square").getIconWidth();
        DEF_HEIGHT_S = Terminal.mainPictureHash.get("small_square").getIconHeight();
        TunePngX = (DEF_WIDTH_S - DEF_WIDTH_B)/2;
        TunePngY = (DEF_HEIGHT_S - DEF_HEIGHT_B)/2;
        zoom = DEF_WIDTH_S;
        Log.log("zoom " + zoom + " DEF_WIDTH_B " + DEF_WIDTH_B + " TunePngX " + TunePngX);
        net = new Net();
        
        // <editor-fold defaultstate="collapsed" desc="Nimbus LAF setting code">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) {
            Err.err(e);
        }
        // </editor-fold>

        // Create and display the form
        java.awt.EventQueue.invokeLater(() -> {
            if (play) {
                new Splash(ResourceFile);
                init_completed = true;//для статуса и переинита
                commander.setVisible(true);
                //set status
                //set border
            } else {
                if (!noGuest && host != null) {
                    if (Net.Connect() == 0) {
                        if (Net.Login(Terminal.LOGNAME, Terminal.PASSWORD) != 0) {
                            Log.log("Login from terminal 1");
                            login();
                        }
                    } else {
                        Log.log("Login from terminal 2");
                        login();
                    }
                } else {
                    Log.log("Login from terminal 3");
                    login();
                }
//            tm = System.nanoTime();
                System.setProperty("java.net.preferIPv4Stack", "true");//for multicast windows10 ipv6
                TH = new TicketHandler();
//                lp = new lt_popup();
// -----------------------------------------------------------------
// Логин закончился, рисуем splash
                new Splash(ResourceFile);
//            splash = null;
                Net.Receiver();
                TH.start();
// -----------------------------------------------------------------
                init_completed = true;//для статуса и переинита
                Clock.Clock_Thread();
                commander.setVisible(true);

//            tm = System.nanoTime() - tm;
//            Log.log("Time init: " + tm / 1000000000.0 + " сек.");
                try {
                    java.lang.Thread.sleep(timeout);//Ждем создания всех объектов
                } catch (InterruptedException ignored) {
                }
                Net.Send("STATUS");
                Net.Send("INIT");
                TIMER600.start();
            }//end if play
        });
    }
    // </editor-fold>

    private static void login() {
        LoginBox loginBox = new LoginBox(host);
        loginBox.setVisible(true);
    }

    // <editor-fold defaultstate="collapsed" desc="args">
    private static void Args(String[] args) {
//написать неизвестный параметр
        for (int a = 0; a < args.length; a++) {
            
        String s = args[a].toLowerCase();
//------------------------------------------------------------------------------
//        switch (s){
//                case "-d"://
//                    debug = true;
//                    break;
//                case "--debug":
//                    debug = true;
//                    break;
//                case "-p":
//                    if ((args.length - 1) > a) {
//                        //MULTICAST_PORT = Integer.valueOf(args[a + 1]);
//                        MULTICAST_PORT = args[a + 1];
//                    }
//                    break;
//                case "--vzrez":
//                   VZREZ = true;
//                   break;
//                default:
//                    Log.log("");
//            }
//------------------------------------------------------------------------------            
            if (s.equals("-s") | s.equals("--withoutsound")) {
                withoutsound = true;
            }
            if (s.equals("--full_screen")) {
                full_screen = true;
            }
            if (s.equals("-d") | s.equals("--debug")) {
                debug = true;
            }
            if (s.equals("-l") | s.equals("--left")) {// где выводить логин бокс на правом мониторе или на левом
                left_mobitor = true;
            }
            if (s.equals("-r") | s.equals("--right")) {// где выводить логин бокс на правом мониторе или на левом
                left_mobitor = false;
            }
            if (s.equals("-t") | s.equals("--timeout")) {
                if ((args.length - 1) > a) {
                    timeout = Integer.valueOf(args[a + 1]);
                }
            }
            if (s.equals("-h") | s.equals("--host")) {
                if ((args.length - 1) > a) {
                    host = args[a + 1];
                    Net.hosts.add(host);
//                    System.out.println("host : " + args[a + 1]);
                }
            }

            if (s.equals("-p") | s.equals("--port")) {
                if ((args.length - 1) > a) {
                    port = Integer.valueOf(args[a + 1]);
                }
            }

            if (s.equals("-i") | s.equals("--images")) {
                if ((args.length - 1) > a) {
                    color_shema = Integer.valueOf(args[a + 1]);
                }
            }

            if (s.equals("--vzrez")) {
                VZREZ = true;
            }

            if (s.equals("--direction")) {
                DIRECTION = true;
            }

            if (s.equals("--play")) {
                play = true;
//                Terminal.LOGNAME = "GUEST";
//                Terminal.PASSWORD = Util.hash256("guest");
//                noGuest = false;
            }

            if (s.equals("-v") | s.equals("--version")) {
                java.util.Properties prop = new java.util.Properties();
                try {
                    prop.load(About.class.getResourceAsStream("version.properties"));
                    Log.log(prop.getProperty("version"));
                } catch (java.io.IOException e) {
                    Err.err(e);
                }
                System.exit(0);
            }
            if (s.equals("-cr") | s.equals("--crossing")) {//azot
                crossing = true;//azot
            }
            if (s.equals("-co") | s.equals("--counter")) {//counters
                counter = true;
            }
            if (s.equals("--help")) {
                Usage();
            }
            if (s.equals("-g") | s.equals("--guest")) {//
                Terminal.LOGNAME = "GUEST";
                Terminal.PASSWORD = Util.hash256("guest");
                noGuest = false;
            }
            if (s.equals("-bs")) {//
                if ((args.length - 1) > a) {
                    NETWORK_BUFFER_SIZE = Integer.valueOf(args[a + 1]) * 1_024;
                }
            }
            if (s.equals("--kbd")) {
                kbd = false;
            }
//            if (args[a].toLowerCase().equals("-z") | args[a].toLowerCase().equals("--zoom")) {
//                int z = 0;
//                if ((args.length - 1) > a) {
//                    z = Integer.valueOf(args[a + 1]);
//                }
//                switch (z) {
//                case 0://100%
//                    TunePngX = -6;
//                    TunePngY = -6;
//                    zoom = 72;
//                    break;
//                case 1://75%
//                    TunePngX = -4;
//                    TunePngY = -4;
//                    zoom = 56;
//                    break;
//                case 2://50%
//                    TunePngX = -3;
//                    TunePngY = -3;
//                    zoom = 36;
//                    break;
//                case 3://25%
//                    TunePngX = -2;
//                    TunePngY = -2;
//                    zoom = 18;
//                    break;
//                default:
//                    TunePngX = -6;
//                    TunePngY = -6;
//                    zoom = 72;
//                }
//            }
        }
    }
    // </editor-fold>

    private static void Usage() {
        System.out.println("terminal.Terminal.Usage()");
        System.exit(0);
    }

    static String GetCmd(String CMD) {
        if (Cmd.containsKey(CMD)) {
            return Mess.get(Cmd.get(CMD));
        }
        return "";
    }

    static String GetMess(long a_id) {
        if (!Mess.containsKey(a_id)) {
            return "not found A_ID";
        }
        return Mess.get(a_id);
    }

    static int GetSound(long a_id) {
        if (!Sound.containsKey(a_id)) {
            return 0;
        }
        return Sound.get(a_id);
    }

    static boolean test_file(String s) {
        java.io.File f = new java.io.File(s);
        return f.exists() && f.isFile() && !f.isDirectory() && f.canRead();
    }
}

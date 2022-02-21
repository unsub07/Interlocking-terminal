//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
//os.checkError();
package terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

class Net {
    static java.net.NetworkInterface netIf;
    static int SocketTimeout = 15000; //15 sec.
    static Socket clientSocket = null;
    static BufferedReader is = null;
    static final ArrayList<String> hosts = new ArrayList<>(2);
    static Iterator<String> hostsIt = null;
    static boolean Connection_to_CommServer = false;
    static boolean logged = false;// пока не послали пароль и логин - false
    static String host = "127.0.0.1";// куда коннектимся
    private static byte[] myIpAddr = new byte[]{127, 0, 0, 1};
    private static PrintStream os = null;
    private static long packet_count = 0;
    private static long byte_count = 0;
    static final java.net.DatagramPacket PACKET = new java.net.DatagramPacket(new byte[1024], 0, 1024);

    Net() {
        try {
            netIf = Net.getInterface();
        } catch (SocketException ex) {
            Log.log("no cnown network interface");
        }
    }

    static void myIp() {
        try {
            myIpAddr = getLocalHostLANAddress().getAddress();
            Log.log("my ip: " + Net.getLocalHostLANAddress().toString());//" + " (aaa.bbb.ccc.ddd) if ddd > 100 {ddd-100+ccc} = ID_terminal = " + terminal_number);
        } catch (UnknownHostException | SocketException ex) {
            Err.err(ex);
        }
    }

    static int Connect() {
        
        if (hostsIt == null || !hostsIt.hasNext()) {
            hostsIt = hosts.iterator();
        }
        if (hostsIt.hasNext()) {
            host = hostsIt.next();//Exception in thread "AWT-EventQueue-0" java.util.ConcurrentModificationException  + Exception in thread "AWT-EventQueue-0" java.util.NoSuchElementException
        }

        int ret;
        try {
            clientSocket = new Socket(host, Terminal.port);
            clientSocket.setSoTimeout(SocketTimeout);
            // System.out.println("clientSocket.getKeepAlive();" +
            // clientSocket.getKeepAlive());
            // System.out.println("clientSocket.getSoTimeout();" +
            // clientSocket.getSoTimeout());
            // System.out.println("clientSocket.getTcpNoDelay();" +
            // clientSocket.getTcpNoDelay());

            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "utf-8"));
            os = new PrintStream(clientSocket.getOutputStream(), true, "utf-8");
            Connection_to_CommServer = true;
            if (Terminal.commander != null) {
                Terminal.commander.set_title();
                Terminal.host = host;
            }
            ret = 0;// ok
        } catch (UnknownHostException e) {
            logged = false;
            Connection_to_CommServer = false;
            Log.log("Don't know about host " + host);
            ret = 7;
            return ret;
        } catch (IOException e) {
            logged = false;
            Connection_to_CommServer = false;
//            Log.log("Connect: Couldn't get I/O for the connection to the host " + host);
            ret = 8;
            return ret;
        }
        return ret;
    }

    static void Receiver() {
        if (status_test()) {
            new Thread(new Recieve_Thread()).start();
        }
    }

    static int Login(String usr, String psw) {

        int ret = 6;
        try {
            String msgRecieved;

            while ((msgRecieved = is.readLine()) != null) {

                String line = msgRecieved.trim();// .toUpperCase();//без пробелов малелькими буквами
                String[] s = line.split(":");
                String cmd = s[0].toUpperCase();

                switch (cmd) {
                    case "LOGIN":
                        Log.log("LOGIN:" + usr);
                        os.println("LOGIN:" + usr);
                        os.flush();
                        break;
                    case "PASSWORD":
                        Log.log("PASSWORD:" + psw);
                        os.println("PASSWORD:" + psw);
                        os.flush();
                        break;
                    case "OK":
                        logged = true;
                        os.println("USER");
                        os.flush();
                        break;
                    case "BYE":
                        Log.log("user logged false");
                        logged = false;
                        String reson = "0";
                        if (s.length > 1) {
                            if (!" ".equals(s[1])) {//надо проверить что это цифра
                                reson = s[1];
                            }
                            Log.log("reson : " + reson);
                        }
//                        Connection_to_CommServer = false;
                        ret = Integer.valueOf(reson);// false
                        return ret;
                    case "USER":
                        System.out.println("s[1] " + s[1]);
                        if ("NO DATA".equals(s[1])) {
                            ret = 5;
                        } else {
                            Def(s[1]);
                            ret = 0;// ok
                        }
                        return ret;
                    default:
                        Log.log("default login (net) " + line);
                        break;

                }// end switch
                Log.log("login cmd " + line);
            }// end while

        } catch (UnknownHostException e) {
            Connection_to_CommServer = false;
            logged = false;
            Log.log("Don't know about host " + host);
            ret = 5;
            return ret;
        } catch (IOException e) {
            logged = false;
            Connection_to_CommServer = false;
            Log.log("Login: Couldn't get I/O for the connection to the host " + host);
            ret = 6;
            return ret;
        }
        return ret;
//        finally {
//            return ret;
//        }
//        return ret;// false
    }

    static void Disconnect() {
        try {
            if (clientSocket != null) {
                // os.checkError();
                if (!os.checkError()) {// ?????????????????
                    os.println("EXIT");
                    Log.log("Net.Disconect send Exit");
                    os.flush();
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        Err.err(e);
                    }
                    
                }
                clientSocket.close();// Exception in thread "Thread-2" java.LANG.NullPointerException
                clientSocket = null;
                logged = false;
                Connection_to_CommServer = false;
                Terminal.TH.stop();
            }
            // else{
            // System.out.println("clientSocket alredy null");
            // }
        } catch (IOException e) {
            logged = false;
            Connection_to_CommServer = false;
            Log.log("Disconnect FUFUFU");
        }
    }

    private static void deleteSynchroniser(MulticastSocket s, String address) throws IOException {
        if (s == null) {
            return;
        }
        InetAddress group = InetAddress.getByName(address);
        s.leaveGroup(group);
        s.close();
    }
    private static void Def(String ans) {

        String[] s = ans.split("\\|");
        // 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 1516 17 18 19 20 21 22 23 24 25 26
        // 27 28 29 3031 32 33 34 35 36 37 38 39
        // USER:null|null|Завод|250|1400|1000|1100|1|130|300|10|10|1|1357|2500|0|0|440|600|1400|200|0|250|1000|0|1100|1|60|330|10|54|0|380|1350|70|0|0|DSP.SHN|18|18|
        // Terminal.USR_ID = rs.getInt("USR_ID");
        // Terminal.LOGNAME = rs.getString("LOGNAME").toUpperCase();

        Terminal.NAME_1 = (("null".equals(s[0])) ? ("") : (s[0]));
        Terminal.NAME_2 = (("null".equals(s[1])) ? ("") : (s[1]));
        Terminal.NAME_3 = (("null".equals(s[2])) ? ("") : (s[2]));

        Terminal.ALARMER_HEIGHT = getInt(s[3]);
        Terminal.ALARMER_WIDTH = getInt(s[4]);
        Terminal.ALARMER_X = getInt(s[5]);
        Terminal.ALARMER_Y = getInt(s[6]);
        Terminal.ALARMER_VISIBLE = (getInt(s[7]) == 1);
        Terminal.CLOCK_HEIGHT = getInt(s[8]);
        Terminal.CLOCK_WIDTH = getInt(s[9]);
        Terminal.CLOCK_X = getInt(s[10]);
        Terminal.CLOCK_Y = getInt(s[11]);
        Terminal.CLOCK_VISIBLE = (getInt(s[12]) == 1);
        Terminal.COMMANDER_HEIGHT = getInt(s[13]);
        Terminal.COMMANDER_WIDTH = getInt(s[14]);
        Terminal.COMMANDER_X = getInt(s[15]);
        Terminal.COMMANDER_Y = getInt(s[16]);
        Terminal.CPU_MONITOR_HEIGHT = getInt(s[17]);
        Terminal.CPU_MONITOR_WIDTH = getInt(s[18]);
        Terminal.CPU_MONITOR_X = getInt(s[19]);
        Terminal.CPU_MONITOR_Y = getInt(s[20]);
        Terminal.CPU_MONITOR_VISIBLE = (getInt(s[21]) == 1);
        Terminal.EVENTER_HEIGHT = getInt(s[22]);
        Terminal.EVENTER_WIDTH = getInt(s[23]);
        Terminal.EVENTER_X = getInt(s[24]);
        Terminal.EVENTER_Y = getInt(s[25]);
        Terminal.EVENTER_VISIBLE = (getInt(s[26]) == 1);
//        Terminal.MENU_HEIGHT = getInt(s[27]);
//        Terminal.MENU_WIDTH = getInt(s[28]);
//        Terminal.MENU_X = getInt(s[29]);
//        Terminal.MENU_Y = getInt(s[30]);
//        Terminal.MENU_VISIBLE = (getInt(s[31]) == 1);

        Terminal.USR_GROUPS = (s[37].toUpperCase().split("\\."));// Exception
        // in
        // thread
        // "AWT-EventQueue-0"
        // java.LANG.NullPointerException
        if (Util.GetUserGroups("DSP")) {
            Terminal.DSP = true;
            Terminal.noGuest = true;
        }
        if (Util.GetUserGroups("SIM")) {
            Terminal.SIM = true;
            Terminal.noGuest = true;
        }
        if (Util.GetUserGroups("SEC")) {
            Terminal.SEC = true;
            Terminal.noGuest = false;
        }
        if (Util.GetUserGroups("SHN")) {
            Terminal.SHN = true;
            Terminal.noGuest = true;
        }
        if (Util.GetUserGroups("GUEST")) {
            Terminal.DSP = false;
            Terminal.SHN = false;
            Terminal.noGuest = false;
            Terminal.GST = true;

        }

        Terminal.USR_MANAGED_AREA = (s[38].toUpperCase().split("\\."));
        Terminal.ResourceFile = Terminal.USR_MANAGED_AREA[0] + ".att";

//        System.out.println("Terminal.USR_MANAGED_AREA[0]" + Terminal.USR_MANAGED_AREA[0]);
//        Terminal.USR_OBSERVED_AREA = s[39];
        // Terminal.USR_STATUS = getInt(s[6]);
        // Terminal.USR_MAC = s[];
        // Terminal.USR_NET = s[];
        Terminal.FIO = Terminal.NAME_1 + " " + Terminal.NAME_2 + " " + Terminal.NAME_3;
        Terminal.MULTICAST_GROUP = s[40];
    }
    private static int getInt(String tag) {
        return Integer.valueOf(tag);
    }
    static void sendDirectCmd_SHN(int ID_OBJ, String cmd) {// для датчиков в луч шлём настройку
        if (Terminal.SHN) {
            Send("D.CTL:" + ID_OBJ + ":" + cmd);
            Log.log("D.CTL:" + ID_OBJ + ":" + cmd);
        }
    }
    static void sendDirectCmd_DSP(int ID_OBJ, String cmd) {// для датчиков в луч шлём настройку
        if (Terminal.DSP) {
            Send("D.CTL:" + ID_OBJ + ":" + cmd);
            Log.log("D.CTL:" + ID_OBJ + ":" + cmd);
        }
    }
    static void sendMaskedCmd_DSP(int ID_OBJ, String PROP_VAL) {
        if (Terminal.DSP) {
            Send("C.CTL" + ":" + ID_OBJ + ":" + PROP_VAL);
        }
    }
    static void sendMaskedCmd_DSP_SHN(int ID_OBJ, String PROP_VAL) {
        if (Terminal.DSP || Terminal.SHN) {
            Send("C.CTL" + ":" + ID_OBJ + ":" + PROP_VAL);
        }
    }
    static void sendMaskedCmd_SIM(int ID_OBJ, String PROP_VAL) {
        if (Terminal.SIM) {
            Send("C.CTL" + ":" + ID_OBJ + ":" + PROP_VAL);
        }
    }
    static void Send(String msg) {
        if (status_test()) {
            os.println(msg);
            os.flush();
            os.checkError();
        } else if (Connect() == 0) {
            if (Login(Terminal.LOGNAME, Terminal.PASSWORD) == 0) {
                Log.log("aaaaaaaaaaaaaaaaaaaaaaa ----------------------------- Login from Net.Send");
                Receiver();
                if (Terminal.init_completed) {

                    Terminal.TH.stop();
                    Terminal.TH.start();

                    Send("STATUS");
                    Send("INIT");
                }
            } else {
                Log.log("Login from Net.Send ret !=0");
            }
        }
    }
    private static boolean status_test() {
        boolean ret;
        try {
            if (netIf.isUp()){
                if (clientSocket != null) {
                    ret = clientSocket.isConnected() && !clientSocket.isClosed();//                if (logged) {
                }// end if null
                else {// если клиент сокет = null
                    ret = false;
                }
            } else {//end netif.isUp
                ret = false;
            }
        } catch (SocketException ex) {
            ret = false;
        }
        
        if (!ret) {Disconnect();}
        
        Area.status.setState();
        return ret;
    }

    // <editor-fold defaultstate="collapsed" desc="ping">
    private void ping(String host) {

        try {
            java.net.InetAddress inet = java.net.InetAddress.getByName(host);
            Log.log("Sending Ping Request to " + host + " " + inet.getHostAddress());

            boolean status = inet.isReachable(1000); //Timeout = 1000 milli seconds

            if (status) {
                Log.log("Проверка сети (echo 7) - статус: " + host + " доступен");
            } else {
                Log.log("Проверка сети (echo 7) - статус: " + host + " не доступен, проерим ещё icmp");

                String pinger;
                String OSname = System.getProperty("os.name");
                if ("Linux".equals(OSname)) {
                    pinger = "ping -c 1 ";
                } else {
                    pinger = "ping -n 1 ";
                }

                String pingResult = "";
                Process ping = Runtime.getRuntime().exec(pinger + host);//runs ping command once on the IP address in CMD
                Log.log(pinger + host);
                try (BufferedReader in = new BufferedReader(new InputStreamReader(ping.getInputStream()))) {
                    String inputLine;
                    int lineCount = 0;
                    while ((inputLine = in.readLine()) != null) {
                        Log.log(inputLine);
                        lineCount++;
                        if (lineCount == 5 && "1".equals(inputLine.substring(23, 24))) {
                            pingResult = inputLine.substring(23, 24);
                        }

                    }//while
                    if ("1".equals(pingResult)) {
                        Log.log("Получен ответ от: " + host);
                    } else {
                        Log.log("не пингуется: " + host);
//                        JOptionPane.showMessageDialog(this, "Не получен ответ от сревера. " + host + "\n"
//                                + "провете сетевое соединение  ", "Ошибка связи", JOptionPane.ERROR_MESSAGE);
                    }
                    in.close();
                }//try
                catch (IOException e) {
                    Log.log("IOException: " + e.getMessage());
                }//catch

            }
        } catch (UnknownHostException e) {
//            JOptionPane.showMessageDialog(this,
//                    "Дальнейшая работа невозможна.\n"
//                    + "IP-адрес сервера БД не указан.\n"
//                    + "(Необходимо внести изменения в файл HOSTS\n"
//                    + "с правами администратора системы.)  ",
//                    "Системная ошибка",
//                    JOptionPane.ERROR_MESSAGE);
            Log.log("Exception: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            Log.log("Error in reaching the Host: " + e.getMessage());
        }
    }//end ping
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="get_ip">
    /**
     * Returns an <code>InetAddress</code> object encapsulating what is most
     * likely the machine's LAN IP address.
     * <p/>
     * This method is intended for use as a replacement of JDK method
     * <code>InetAddress.getLocalHost</code>, because that method is ambiguous
     * on Linux systems. Linux systems enumerate the loopback network interface
     * the same way as regular LAN network interfaces, but the JDK
     * <code>InetAddress.getLocalHost</code> method does not specify the
     * algorithm used to select the address returned under such circumstances,
     * and will often return the loopback address, which is not valid for
     * network communication. Details
     * <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4665037">here</a>.
     * <p/>
     * This method will scan all IP addresses on all network interfaces on the
     * host machine to determine the IP address most likely to be the machine's
     * LAN address. If the machine has multiple IP addresses, this method will
     * prefer a site-local IP address (e.g. 192.168.x.x or 10.10.x.x, usually
     * IPv4) if the machine has one (and will return the first site-local
     * address if the machine has more than one), but if the machine does not
     * hold a site-local address, this method will return simply the first
     * non-loopback address found (IPv4 or IPv6).
     * <p/>
     * If this method cannot find a non-loopback address using this selection
     * algorithm, it will fall back to calling and returning the result of JDK
     * method <code>InetAddress.getLocalHost</code>.
     * <p/>
     *
     * @throws UnknownHostException If the LAN address of the machine cannot be
     * found.
     */
    static InetAddress getLocalHostLANAddress() throws UnknownHostException, SocketException {
        try {
            InetAddress ia = null;
            NetworkInterface netIf;
            for (java.util.Enumeration<java.net.NetworkInterface> e = java.net.NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();) {
                netIf = e.nextElement();
                for (Enumeration inetAddrs = netIf.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {

                        if (inetAddr.isSiteLocalAddress()) {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr;
                        } else if (ia == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            ia = inetAddr;
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if (ia != null) {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return ia;
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (UnknownHostException e) {
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }
// </editor-fold>

    private static java.net.MulticastSocket createMSocket(InetAddress ip, int port) throws java.io.IOException {
        java.net.MulticastSocket s = new java.net.MulticastSocket(port);
        java.net.SocketAddress mcastaddr = new java.net.InetSocketAddress(ip, port);
//        java.net.NetworkInterface netIf = Net.getInterface();
        netIf = Net.getInterface();
        if (netIf != null) {
            s.setNetworkInterface(netIf);
            s.joinGroup(mcastaddr, netIf);
            s.setSoTimeout(20);
            s.setReceiveBufferSize(Terminal.NETWORK_BUFFER_SIZE);
            Log.log("_ip_: " + ip + " " + port);
            return s;
        }
        return s;
    }

    private static java.net.NetworkInterface getInterface() throws java.net.SocketException {
//        java.net.NetworkInterface netIf;
        for (java.util.Enumeration<java.net.NetworkInterface> e = java.net.NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();) {
            netIf = e.nextElement();
            if (!netIf.isLoopback() && netIf.isUp() && !netIf.isPointToPoint()) {
                for (InterfaceAddress ia : netIf.getInterfaceAddresses()) {
                    if (ia.getAddress() instanceof Inet4Address) {
                        return netIf;
                    }
                }
                break;
            }
        }
        return NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress());
    }

    static java.net.DatagramSocket getDatagramSocet() {
        java.net.DatagramSocket s = null;
        try {
            int port = Integer.parseInt(Terminal.MULTICAST_PORT);
            s = Net.createMSocket(getMulticastIp(), port);
        } catch (IOException e) {
            Err.err(e);
        }
        return s;
    }

    private static java.net.InetAddress getMulticastIp() {
        java.net.InetAddress ip = null;
        try {
            ip = java.net.InetAddress.getByName(Terminal.MULTICAST_GROUP);
        } catch (UnknownHostException e) {
            Err.err(e);
        }
        return ip;
    }

    /**
     * @return the myIpAddr
     */
    public static byte[] getMyIpAddr() {
        return myIpAddr;
    }
//
//    /**
//     * @param aMyIpAddr the myIpAddr to set
//     */
//    public static void setMyIpAddr(byte[] aMyIpAddr) {
//        myIpAddr = aMyIpAddr;
//    }

    /**
     * @return the packet_count
     */
    public static long getPacket_count() {
        return packet_count;
    }

    /**
     * @param aPacket_count the packet_count to set
     */
    public static void setPacket_count(long aPacket_count) {
        packet_count = aPacket_count;
    }

    /**
     * @return the byte_count
     */
    public static long getByte_count() {
        return byte_count;
    }

    /**
     * @param aByte_count the byte_count to set
     */
    public static void setByte_count(long aByte_count) {
        byte_count = aByte_count;
    }
}

package terminal;

import java.util.SortedMap;
import java.util.TreeMap;
import ru.attrans.proc.objs.UnitState;

class Unit extends javax.swing.JLabel {

    final int id_obj;
    // final String type;
    private final int type;
    final int log_address;
    final String s_name;
    final String type_name;
    final int cabinet_id;
    final int rack;
    final int place;

    private final int param_bytes;
    private final int channel_bytes;

    private final SortedMap<Integer, Channels> channelMap;// channels

    private int param;
    private int chanel;

    Unit(
            int ID_OBJ,
            int CABINET_ID,
            int RACK,
            int PLACE,
            int UNIT_TYPE,
            int LOG_ADDRESS,
            String S_NAME,
    //        int PARAM_OFFSET,
            int PARAM_BYTES,
    //        int CHANNELS_OFFSET,
            int CHANNEL_BYTES,
            String TYPE_NAME
    ) {
        super();

        param_bytes = PARAM_BYTES;
        channel_bytes = CHANNEL_BYTES;
        channelMap = new TreeMap<>();
        // setName(type);
        type = UNIT_TYPE;//если 2 то показывать параметры
        id_obj = ID_OBJ;
        log_address = LOG_ADDRESS;
        s_name = S_NAME;
        type_name = TYPE_NAME;
        cabinet_id = CABINET_ID;
        rack = RACK;
        place = PLACE;

//        getLinkedChannels();
        // нужно ! установть дефаулт
        // setState("0.0.0.2.0.11");//FUFUFU FUCK Victor - setState в байтах
        setPreferredSize(new java.awt.Dimension(30, 30));
        setHorizontalAlignment(javax.swing.JLabel.CENTER);
        setVerticalAlignment(javax.swing.JLabel.CENTER);
        setFont(Terminal.SANS09);
        setOpaque(true);
        String la;
        if (log_address == -1) {
            la = "";
        } else {
            la = String.valueOf(log_address);
        }
        if (!isDummy()) {
            setBackground(java.awt.Color.DARK_GRAY);
            setText("<html><font color=white size=2>" + type_name
                    + "</font><br>" + la + "</html>");
            setToolTipText(S_NAME);
        } else {
            setBorder(javax.swing.BorderFactory.createEtchedBorder());
        }
    }

    void setFault(boolean FAULT) {
        setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(
                        java.awt.Color.BLACK, 1), javax.swing.BorderFactory
                .createLineBorder(((FAULT) ? java.awt.Color.RED
                        : java.awt.Color.GREEN), ((FAULT)) ? 3 : 1)));

        setForeground((FAULT) ? java.awt.Color.PINK : java.awt.Color.GREEN);
    }

    String reportAllParameters() {// ??????????????????????????????????????????? Почему-то вызывается 2 раза ??????????????????????????
        StringBuilder result = new StringBuilder();
        if (!(type == 1 || type == 2 || type == 3)) {
//        if (type != 1 || type != 2 || type != 3 || type != 8 || type != 9) {
            return "Контроль параметров \nневозможен.";
        }
        for (int i : Terminal.Parametr_Hash.keySet()) {
//                boolean b = (param & (1 << (i-1))) != 0x00;
//                if (param == 512){
//                    System.out.println("i "+i + " "+ b);
//                }
            result.append("<tr height=18>").append("<td width=455 align=right>").append(Terminal.Parametr_Hash.get(i)// Purpose
            ).append(":</td>").append("<td width=5> </td>").append("<td width=40><font color=").append(((param & (1 << (i - 1))) != 0x00) ? "red> ОТКАЗ."
                    : "green> НОРМА.").append("</font></td>").append("</tr>");
        }
        result = new StringBuilder("<html><table>" + result + "</table></html>");
        return result.toString();
    }

    String dot() {
        return reportAllParametersInDots() + reportAllChannelsInDots();
    }

    private String reportAllParametersInDots() {
        StringBuilder result = new StringBuilder();
        if (!(type == 1 || type == 2 || type == 3)) {
//        if (type != 1 || type != 2 || type != 3 || type != 8 || type != 9) {
            return "<html>  Параметры не заданы.  </html>";
        }
        for (int i : Terminal.Parametr_Hash.keySet()) {
            result.append("<font color=").append(((param & (1 << (i - 1))) != 0x00) ? "red> \u258c"
                    : "green> \u258c").append("</font>");
        }
        result = new StringBuilder("<html>" + result + "</html>");
        return result.toString();
    }

    void addChannel(int link, Channels c) {
        channelMap.put(link, c);// ASS FUCK FUFUFU Victor
    }

    String reportAllChannels() {
        StringBuilder result = new StringBuilder();
        if (channelMap.isEmpty()) {
            return "Контроль каналов \nневозможен.";
        }
        for (int i : channelMap.keySet()) {
            // int id = channelMap.get(i).getIdObj();
//            int l = channelMap.get(i).getLink();
//            boolean fl = channelMap.get(i).isFaulted();
            result.append("<tr height=18>").append("<td width=455 align=right>").append(channelMap.get(i).purpose).append(":</td>").append("<td width=5> </td>").append("<td width=40><font color=").append((channelMap.get(i).isFaulted()) ? "red> НЕИСПРАВЕН."
                    : "green> ИСПРАВЕН.").append("</font></td>").append("</tr>");
        }
        result = new StringBuilder("<html><table>" + result + "</table></html>");
        return result.toString();
    }

    private String reportAllChannelsInDots() {
        StringBuilder result = new StringBuilder();
        if (channelMap.isEmpty()) {
            return "<html>  Каналы не заданы. </html>";
        }
        for (int i : channelMap.keySet()) {
            // int id = channelMap.get(i).getIdObj();
//            int l = channelMap.get(i).getLink();
//            boolean fl = channelMap.get(i).isFaulted();
            result.append("<font color=").append((channelMap.get(i).isFaulted()) ? "red> \u258c"
                    : "green> \u258c").append("</font>");
        }
        result = new StringBuilder("<html>" + result + "</html>");
        return result.toString();
    }

    void setState(
//            long DTIME,
            byte[] ba) {
//        setDtime(DTIME);

        if (channel_bytes != 0) { //DI 0 - не контролируется поэтому у него байтов 0 и не будем разбирать посылку от коммуникационника

            chanel = (ba[2] << 24 & 0xff000000) | (ba[3] << 16 & 0x00ff_0000) | (ba[4] << 8 & 0x0000_ff00) | (ba[5] & 0x0000_00ff);

            if (!channelMap.isEmpty()) {
                channelMap.values().forEach((ch) -> {
                    ch.faulted = (chanel & (1 << ch.link)) != 0x00;
//                setFault(ch.faulted);//надо или не надо не понятно
                });
            }
        }

        if (param_bytes != 0) {//DI 0 - не контролируется поэтому у него байтов 0 и не будем разбирать посылку от коммуникационника
            param = (ba[0] << 8 & 0x0000_ff00) | (ba[1] & 0x0000_00df);//df чтобы убрать бит "Имеется информация о канале"
        }
        setFault(!(param == 0 || param == 32) || chanel > 0);//32=информация о канале

        if (CpuMon.unitListActive) {
//            System.out.println("+++++++++++++++++++++++ unit list active +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            UnitLst ul = Commander.cpuMon.getUnitList();
            ul.updateUnitState(id_obj, reportAllParameters(),
                    reportAllChannels(), reportAllParametersInDots()
                    + reportAllChannelsInDots());
        }
    }

    private boolean isDummy() {
        return (id_obj < 1);
    }

// --Commented out by Inspection START (16.01.18 16:05):
//    private long getDtime() {
//        return dtime;
//    }
// --Commented out by Inspection STOP (16.01.18 16:05)

//    private void setDtime(long DTIME) {
//    }

// --Commented out by Inspection START (16.01.18 15:00):
//    private void event(int id_msg) {
//        if (getDtime() > 0) {
//            Events.InsertMessage(getDtime(), id_obj, id_msg);
//        }
//    }
// --Commented out by Inspection STOP (16.01.18 15:00)

    //@Override
    void setState(Object oid) {
        UnitState u = (UnitState) oid;
        setState(
//                u.timestamp,
                u.rawData// bytearray
        );
    }
}

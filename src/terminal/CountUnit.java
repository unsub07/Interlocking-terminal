//Copyright (c) 2011, 2020, ЗАО "НПЦ "АТТРАНС"
package terminal;

class CountUnit extends javax.swing.JPanel {

    public final int id_obj;
    private long dtime;// последнее время
//    private String s_name;
    private boolean dis_svz;// DIS_SVZ;//Превышено время обмена данными с ЦПУ СО
    private boolean err_svz;// ERR_SVZ;//Ошибка связи с ЦПУ СО
    private boolean stp_cpuso;// STP_CPUSO;//Остановка ЦПУ СО
    private boolean err_get;// ERR_GET;//Ошибка приема данных от ЦПУ СО
    private boolean err_pb1;// ERR_PB1;//Нет связи с КП342 (1) ЦПУ СО
    private boolean err_pb2;// ERR_PB2;//Нет связи с КП342 (2) ЦПУ СО
    // private static final ArrayList<Beam> beams = new ArrayList<>();

    CountUnit(int ID_OBJ, String S_NAME) {// Нужно сделать вьюху
        super();
        id_obj = ID_OBJ;

        setName(S_NAME);
        setBorder(javax.swing.BorderFactory.createTitledBorder(getName()));
        // setLayout(new java.awt.GridLayout(beams.size(), 1));
        setLayout(new java.awt.GridLayout(Terminal.Beams_Hash.size(), 1));// ?
        // а нужна вообще?
        setFont(Terminal.SANS12);

        // for (Beam b : beams) {
        // b.setLayout(new java.awt.GridLayout(1, 30, 2, 2));
        // this.add(b);
        // }
    }

    void putBaeam(Beam b) {
        b.setLayout(new java.awt.GridLayout(1, 30, 2, 2));
        add(b);
    }

    private long getDtime() {
        return dtime;
    }

    private void setDtime(long DTIME) {
        dtime = DTIME;
    }

// --Commented out by Inspection START (16.01.18 15:00):
//    public int getIdObj() {
//        return id_obj;
//    }
// --Commented out by Inspection STOP (16.01.18 15:00)

    // Beam getBeam(int beamId) {
    // Beam b = null;
    // // for (java.awt.Component c : getComponents()) {
    // // if (c instanceof Beam) {
    // // if (((Beam) c).getBeamId() == beamId) {
    // // b = (Beam) c;
    // // }
    // // }
    // // }
    // for (int i = 0; i < beams.size(); i++){
    // b = beams.get(i);
    // if (b.getBeamId() == beamId){return b;}
    // }
    // return b;
    // }
    void setState(
            long DTIME,
            boolean DIS_SVZ,// Превышено время обмена данными с ЦПУ СО
            boolean ERR_SVZ,// Ошибка связи с ЦПУ СО
            boolean STP_CPUSO,// Остановка ЦПУ СО
            boolean ERR_GET,// Ошибка приема данных от ЦПУ СО
            boolean ERR_PB1,// Нет связи с КП342 (1) ЦПУ СО
            boolean ERR_PB2// Нет связи с КП342 (2) ЦПУ СО
    ) {
        setDtime(DTIME);
        /*
         * 1 DIS_SVZ_ON <html><font color=red>Превышено интервал ожидания ЦПУ
         * СО</html> MESS_COUNT_UNIT_DIS_SVZ 195
         */
        if (dis_svz != DIS_SVZ) {
            dis_svz = DIS_SVZ;
            if (DIS_SVZ) {// true 1
                event(195);
            }  // false 0

        }
        /*
         * 1 ERR_SVZ_ON <html><font color=red>Ошибка связи с ЦПУ СО</html>
         * MESS_COUNT_UNIT_ERR_SVZ 199
         */
        if (err_svz != ERR_SVZ) {
            err_svz = ERR_SVZ;
            if (ERR_SVZ) {// true 1
                event(199);
            }  // false 0

        }
        /*
         * 1 STP_CPUSO_ON <html><font color=red>Остановка ЦПУ СО</html>
         * MESS_COUNT_UNIT_STP_CPUSO 200
         */
        if (stp_cpuso != STP_CPUSO) {
            stp_cpuso = STP_CPUSO;
            if (STP_CPUSO) {// true 1
                event(200);
            }  // false 0

        }
        /*
         * 1 ERR_GET_ON <html><font color=red>Ошибка приема данных от ЦПУ
         * СО</html> MESS_COUNT_UNIT_ERR_GET 196
         */
        if (err_get != ERR_GET) {
            err_get = ERR_GET;
            if (ERR_GET) {// true 1
                event(196);
            }  // false 0

        }
        /*
         * 1 ERR_PB1_ON <html><font color=red>Нет связи с КП342 (1) ЦПУ
         * СО</html> MESS_COUNT_UNIT_ERR_PB1 197
         */
        if (err_pb1 != ERR_PB1) {
            err_pb1 = ERR_PB1;
            if (ERR_PB1) {// true 1
                event(197);
            }  // false 0

        }
        /*
         * 1 ERR_PB2_ON <html><font color=red>Нет связи с КП342 (2) ЦПУ
         * СО</html> MESS_COUNT_UNIT_ERR_PB2 198
         */
        if (err_pb2 != ERR_PB2) {
            err_pb2 = ERR_PB2;
            if (ERR_PB2) {// true 1
                event(198);
            }  // false 0

        }
    }

    private void event(int id_msg) {
        if (getDtime() > 0) {
            Events.InsertMessage(getDtime(), id_obj, id_msg);
        }
    }

// --Commented out by Inspection START (16.01.18 15:00):
////    private void alarm_on(int id) {
////        if (getDtime() > 0) {
////            Alarms.alarm_on(getDtime(), id, id_obj);
////        }
////    }
////
////    private void alarm_off(int id) {
////        if (getDtime() > 0) {
////            Alarms.alarm_off(getDtime(), id, id_obj);
////        }
////    }
//    void setState(Object oid) {
//        CountUnitState c = (CountUnitState) oid;
//        this.setState(
//                c.timestamp,
//                c.DIS_SVZ,
//                c.ERR_SVZ,
//                c.STP_CPUSO,
//                c.ERR_GET,
//                c.ERR_PB1,
//                c.ERR_PB2
//        );
//    }
// --Commented out by Inspection STOP (16.01.18 15:00)
}// end class CountUnit


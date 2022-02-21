package terminal;

class Play_hash {

    private static final java.text.SimpleDateFormat SDF = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static boolean is_cmd;
    private static boolean is_note;
    private static boolean is_ind;
    private static boolean init;
    private static boolean skip;
    private static long dtime;
    private static int line_num;
    private static String Dtime;
    private static int id_obj;
    private static String new_val;
    private static String old_val;
    private static byte b[];
    private static byte bi[];
    private static int obj_type = 0;
    private static String dsp;
    private static String ip;
    private static String cmd;//(64.64)
    private static String line;//не нужна - память занимает.

    Play_hash(String LINE) {
        line = LINE;
        String[] s = line.split("\\ ");
        dtime = test_String2(s[0]);
        Dtime = DateFromLong(dtime);
        switch (s[1]) {
            case "CMD":// "NO_ROUTING"://0
                is_cmd = true;
                id_obj = Integer.valueOf(s[2]);
                cmd = s[3];
                ip = s[4];
                dsp = s[5];
//                          время  тип_объекта  имя_объекта
//                Alarm.InsertCmd(dtime, id_obj, cmd, dsp, ip);
                break;
            case "NOTE":
                is_note = true;
                break;
            default:
                is_ind = true;
//                String txt = s[1] + " " + s[2] + " " + s[3];
                id_obj = Integer.valueOf(s[1]);

                new_val = s[2];
                old_val = s[3];

                init = new_val == null ? old_val == null : new_val.equals(old_val);

                if (!init) {

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
                    } else {
                        Log.log("obj_type not contains id " + id_obj);
                    }
                }//enf not init
        }//end case
    }
    
    private static String DateFromLong(long dt) {
        return SDF.format(new java.util.Date(dt));
    }
    
    private static long test_String2(String long_time) {
        long lt;
        try {
            lt = Long.parseLong(long_time);
        } catch (NumberFormatException e) {
            skip = true;
            lt = new java.util.Date().getTime();
            Log.log("format time wrong");
        }
        return lt;
    }
   
}

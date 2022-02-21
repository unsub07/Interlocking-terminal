//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

class Err {

    private static PrintWriter pw;
//    private static int SIZE = 1_000; // колличество байт в файле.
//    private static int ROTATIONCOUNT = 5;// число лог файлов.
    private static int NumError = 0;
    private static File fpw;
    private static boolean log_to_file = false;
    private static int file_created = 0;

    static void err(java.lang.Exception e) {
        if(file_created==0){
            new Err();
        }
        // throw new UnsupportedOperationException("Not supported yet."); //To
        // change body of generated methods, choose Tools | Templates.
        java.util.logging.Logger.getLogger(Err.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        // ex.printStackTrace();
        if (log_to_file) {
            if (0 == NumError) {
                Log_to_file(e.getMessage());// в файл запишем только первую
                // произошедшую ошибку (потому как если
                // они в цикле до бесконечности...)
            }
            // else {
            // }
            NumError++;
        }
    }

    static void Log_to_file(String message) {
        // <editor-fold defaultstate="collapsed" desc="log(String)">
        if (fpw.canWrite()) {
            pw.println(DateTime() + "  -  " + message);
            pw.flush();
        }
        // write to console
        // </editor-fold>
    }

    private static String DateTime() {
        // <editor-fold defaultstate="collapsed" desc="DateTime()">
        // String dt = new Date().toString();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");

        return "Error_log_" + Terminal.SDF3.format(new Date());
        // </editor-fold>
    }

    Err() {
        fpw = new File(Terminal.ATTRANS_HOME + Terminal.FS + "log" + Terminal.FS + "error" + DateTime() + ".log");
        File dir = new File(Terminal.ATTRANS_HOME + Terminal.FS + "log");
        boolean wasSuccessful = true;
        if (!dir.exists()) {
            wasSuccessful = dir.mkdir();
        }
        if (wasSuccessful) {
            try {
                pw = new PrintWriter(fpw);
                log_to_file = true;
                file_created = 1;
            } catch (FileNotFoundException e) {
                log_to_file = false;
                file_created = 2;
                Err.err(e);
            }
        }
    }

}

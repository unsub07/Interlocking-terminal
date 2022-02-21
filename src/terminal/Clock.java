//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
class Clock extends javax.swing.JPanel {// implements MouseListener {

    static final javax.swing.JLabel lblUser = new javax.swing.JLabel();
    private static final javax.swing.JLabel lblTime = new javax.swing.JLabel();
    private static final javax.swing.JLabel lblDate = new javax.swing.JLabel();
    private static final AltMenu MENU = new AltMenu();

//    private static boolean paintFlag;

    private static void GetTime() {
        Net.Send("TIME");
    }

    static long SetTime(String d) {
        Calendar cl = new GregorianCalendar();
        long dt = test_String2(d);
        cl.setTime(new Date(dt));
        lblTime.setText(Util.TimeFromLong(dt));
        lblDate.setText("<html>" + getDayName(cl.get(Calendar.DAY_OF_WEEK), java.util.Locale.getDefault()) + ",<br>" + Util.DateFromLong(dt) + "</html>");
        return dt;
    }

    static void Clock_Thread() {
        try {
            class back_run extends Thread {

                back_run() {
                }

                @Override
                public void run() {
//                    Log.log("Thread starting...");
                    try {
                        while (!Exit.isEXIT()) {
                            GetTime();
                            back_run.sleep(1_000);
                        }
                    } catch (InterruptedException e) {
                        Err.err(e);
                    }
                    Log.log("Clock_Thread END! ");
                }
            }
            Thread Clock = new back_run();
            Clock.start();
//            Log.log("CLOCK shadow repaint daemon started!");
        } catch (Exception e) {
//            Log.log("CLOCK shadow++++" + e);
        }
    }

    private static String getDayName(int day, java.util.Locale locale) {
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] dayNames = symbols.getWeekdays();
        return dayNames[day];
    }

//    private static long test_String(String long_time){
//        boolean arg = true;
//        long lt = new Date().getTime();
//        System.out.println("time str.length() = " + long_time.length());
//        if (long_time.length() != 8){}
//        for (char c : long_time.toCharArray()) {
//                    if (!Character.isDigit(c)) {
//                        arg = false;
//                        break;
//                    }
//                }
//            if (arg){
////                lt = Long.parseLong(long_time);
//                lt = Long.valueOf(long_time);
//            }
//        return lt;
//    }
    private static long test_String2(String long_time) {
        long lt;
        try {
            lt = Long.parseLong(long_time);
        } catch (NumberFormatException e) {
            lt = new java.util.Date().getTime();
        }
        return lt;
    }
    
    Clock() {// конструктор
        super();
        
        setBackground(new java.awt.Color(102, 102, 102));
        setBorder(BorderFactory.createEtchedBorder());
        setForeground(new java.awt.Color(255, 255, 255));
        
        lblTime.setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 26));
        lblTime.setForeground(new java.awt.Color(255, 255, 255));
        lblTime.setHorizontalAlignment(SwingConstants.LEADING);
        lblTime.setSize(120, 32);
        lblTime.setLocation(0, 0);
        add(lblTime);
        
        lblDate.setFont(Terminal.SANS14P);
        lblDate.setForeground(new java.awt.Color(51, 255, 0));
        lblTime.setHorizontalAlignment(SwingConstants.LEADING);
        lblDate.setSize(120, 32);
        lblDate.setLocation(120, 0);
        
        add(lblDate);
        
        add(MENU);
        lblUser.setText("");// Имя пользователя...
        add(lblUser);
        
        SetTime(String.valueOf(new java.util.Date().getTime()));
    }

}

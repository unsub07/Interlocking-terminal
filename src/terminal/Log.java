//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

class Log {

    static void log(String message) {
        if (Terminal.debug) {
            System.out.println(message);
        }
    }

}

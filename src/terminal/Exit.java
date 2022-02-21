//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

class Exit {
    
    private static boolean EXIT;

    void exit() {
        setEXIT(true);
        int ret = 0;
        Log.log("Net.Disconnect Exit");
        Net.Disconnect();
        Runtime r = Runtime.getRuntime();
        try {
            //get system - windows or linux and launch run.bat or run.sh

            String fs = Terminal.FS;
            String path;
            path = System.getenv("ATTRANS_HOME") + fs + "scripts" + fs;
            java.io.File term;
            term = new java.io.File(path + "terminal.sh");
            if (term.exists()) {
                if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
                    path = System.getenv("ATTRANS_HOME") + fs + "scripts" + fs;
                    r.exec(path + "terminal.sh");
                } else {
                    path = System.getenv("ATTRANS_HOME") + fs + "scripts" + fs;
                    r.exec(path + "terminal.bat");
                }
            }
        } catch (java.io.IOException e) {
            Err.err(e);
        }
        System.exit(ret);
    }

    /**
     * @return the EXIT
     */
    public static boolean isEXIT() {
        return EXIT;
    }

    /**
     * @param aEXIT the EXIT to set
     */
    public static void setEXIT(boolean aEXIT) {
        EXIT = aEXIT;
    }
}

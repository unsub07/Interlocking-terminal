//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
/*
 new Rn_Axis_Thread(id_rn, axis).start();
 */
package terminal;

class Rn_Axis_Thread extends Thread {

    private final int id_obj;
    private final short axis;

    Rn_Axis_Thread(int ID_OBJ, short AXIS) {
        id_obj = ID_OBJ;
        axis = AXIS;
    }

    @Override
    public void run() {
        for (int i = 0; i < axis; i++) {
            Net.Send("C.CTL:" + id_obj + ":" + "RAILNET_AXIS_PLUS");
            pause();
            Net.Send("C.CTL:" + id_obj + ":" + "RAILNET_AXIS_PLUS0");
        }//end for
    }

    private void pause() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Err.err(e);
        }
    }
    
}

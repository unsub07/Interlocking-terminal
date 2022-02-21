//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

import ru.attrans.proc.objs.CRC_PLC;
import ru.attrans.proc.objs.CommandState;
import ru.attrans.proc.objs.F3_DIO_20_8_02;
import ru.attrans.proc.objs.HeatState;
import ru.attrans.proc.objs.NoteState;
import ru.attrans.proc.objs.RWObject;
import ru.attrans.proc.objs.StatusState;
import ru.attrans.proc.objs.UnitState;
//import ru.attrans.proc.objs.VzrezState;
import static terminal.TicketHandler.QUEUE;

class Ticket3 extends Thread {

    @Override
    public void run() {
        while (true) {
            byte [] b = QUEUE.poll();
            if (b != null) {
                deserialize(b);
            } else {
                try {
                    Ticket3.sleep(20);
                } catch (InterruptedException e) {
                    Err.err(e);
                }
            }
        }
    }

    private void deserialize(byte[] b) {
        try {
            set((RWObject) new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(b)).readObject());
        } catch (java.io.IOException | ClassNotFoundException ex) {
            Err.err(ex);
        }
    }

    private void set(RWObject internalObj) {
//        Log.log(internalObj.toString());
        if (internalObj instanceof StatusState) {
            Area.status.setStatus(internalObj.toString());
        } else if (internalObj instanceof CommandState) {
            Command(internalObj.toString() );
        } else if (Terminal.mainCellsHash.containsKey(internalObj.objId)) {
            Cell o;
            o = Terminal.mainCellsHash.get(internalObj.objId);
            if (ObjExist(o)) {
                o.setState(internalObj);
            } else {
                er(internalObj.objId);
            }
        } else if (internalObj instanceof NoteState) {
            Note.Create_Note(internalObj);
        } else if (internalObj instanceof UnitState) {
            Unit o;
            o = Terminal.Unit_Hash.get(internalObj.objId);
            if (ObjExist(o)) {
                o.setState(internalObj);
            } else {
                er(internalObj.objId);
            }
        } else if (internalObj instanceof F3_DIO_20_8_02) {
            f3_dio_20_8_02 o;
            o = Terminal.f3_dio_20_8.get(internalObj.objId);
            if (ObjExist(o)) {
                o.setState(internalObj);
            } else {
                er(internalObj.objId);
            }
//        }
//        else if (internalObj instanceof VzrezState) {
//            Vzrez o;
//            o = Terminal.Vs_Hash.get(internalObj.objId);
//            if (ObjExist(o)) {
//                o.setState(internalObj);
//            } else {
//                er(internalObj.objId);
//            }
        } else if (internalObj instanceof CRC_PLC) {
            PLC o;
            o = Terminal.PLCs_Hash.get(0);
            if (ObjExist(o)) {
                o.setState(internalObj);
            } else {
                er(internalObj.objId);
            }
        } else if (internalObj instanceof HeatState) {
            Heat o;
            o = Terminal.Heat_Hash.get(internalObj.objId);
            if (ObjExist(o)) {
                o.setState(internalObj);
            } else {
                er(internalObj.objId);
            }
        }//end heat state
        Log.log(internalObj.objId + " " + internalObj.toString());
    }//end set

    private void er(int idobj) {
        Log.log("Ticket3 object not exist with obj_id = " + idobj);
    }

    private boolean ObjExist(Object c) {
        return c != null;
    }

    private void Command(String cmd) {
        String line = cmd.trim().toUpperCase();//без пробелов малелькими буквами
        String[] s = line.split(":");
        String c = s[0].toUpperCase();
        switch (c) {
            case "CMDMESS":
                Events.InsertCmdMessage(line);
                break;
            case "ALARM":
                // ALARM:1423063081613|8592|108|D
                String[] a = s[1].split("\\|");
                long dt = Long.valueOf(a[0]);
                int id_obj = Integer.valueOf(a[1]);
                int mes = Integer.valueOf(a[2]);
                Alarms.alarm_kvit(dt, mes, id_obj);
                break;
            case "TEMP":
                // ALARM:1423063081613|8592|108|D
                String[] a0 = s[1].split("\\|");
                long dt0 = Long.valueOf(a0[0]);
                int id_obj0 = Integer.valueOf(a0[1]);
                String mes0 = a0[2];
                
                Clock.lblUser.setText("<html><font color=white size=3>" + mes0 + "<font color=#33ff00 size=3>" + "</html>");
                
                break;
            default:
                break;
        }
    }
}

//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

//import ru.attrans.proc.objs.VzrezState;

public class Vzrez {

    private boolean vzrez;
    private int id_obj;
    private int ind;
    private int tr_id;

    Vzrez(
            int ID_OBJ,
            int IND,
            int TR_ID
    ) {
        id_obj = ID_OBJ;
        ind = IND;
        tr_id = TR_ID;
    }
    
    private void setState(long DATE, boolean VZREZ){
        vzrez = VZREZ;
        Turnout t = Terminal.Turnouts_Hash.get(tr_id);
        t.setVzrez(VZREZ);
    }
    
//    @Override
//    public void setState(Object oid) {
//        VzrezState v = (VzrezState) oid;
//
//        this.setState(
//                v.timestamp,
//                v.VZREZ
//
//        );
//    }
}
    

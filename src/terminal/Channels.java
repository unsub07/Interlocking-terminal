//Copyright (c) 2011, 2020, ЗАО "НПЦ "АТТРАНС"
package terminal;

class Channels {

    final int unit_id;
    final int link;
    final String purpose;
    boolean faulted = false;

    Channels(
    //        int ID_OBJ,
            int UNIT_ID,
            int LINK,
            String PURPOSE
    ){
//        int id_obj = ID_OBJ;
        unit_id = UNIT_ID;
        link = LINK;
        purpose = PURPOSE;
    }

//    int getLink() {
//        return link;
//    }

    boolean isFaulted() {
        return faulted;
    }

}

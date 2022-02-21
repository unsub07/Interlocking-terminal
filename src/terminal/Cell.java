//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

class Cell implements Globals {

    final int id_obj;
    String s_name;
    final int GX;// global X
    final int GY;// global Y
    int shift_x;
    int shift_y;
    final int vStatus;//1-FUTURE-строительство, 2-INVISIBLE-невидимый, 3-DEPRECATE-н.е.н.х.
    final int id_type;
    int cmdX;// screen X for CMD - real in pixels
    int cmdY;// screen Y for CMD - real in pixels
    private long dtime = 0;// время последненго изменения объекта.
    int CELLPANE_LAYER_10 = 10;


    Cell(int ID_OBJ, String S_NAME, int gX, int gY, int SHIFT_X, int SHIFT_Y, int VIDEO_STATUS, int ID_TYPE) {
        id_obj = ID_OBJ;
        s_name = S_NAME;
        GX = gX;
        GY = gY;
        shift_x = SHIFT_X;
        shift_y = SHIFT_Y;
        vStatus = VIDEO_STATUS;
        id_type = ID_TYPE;
        cmdX = getRealX(GX, shift_x);
        cmdY = getRealY(GY, shift_y);
        placeCellInCellsHash();
    }

    public void setState(Object oid) {
    }

    private void placeCellInCellsHash() {// place THIS to cells hash map
        Terminal.mainCellsHash.put(id_obj, this);
    }

    long getDtime() {
        return dtime;
    }

    void setDtime(long DTIME) {
        dtime = DTIME;
    }
    
    static int getRealX(int X, int SHIFT_X){
        return X * Terminal.zoom + SHIFT_X + Terminal.CELL_SHIFT;
    }
    
    static int getRealY(int Y, int SHIFT_Y){
        return Y * Terminal.zoom + SHIFT_Y;
    }
    
    static int getRealX_Png(int X, int SHIFT_X){
        return X * Terminal.zoom + SHIFT_X + Terminal.CELL_SHIFT + Terminal.TunePngX;
    }
    
    static int getRealY_Png(int Y, int SHIFT_Y){
        return Y * Terminal.zoom + SHIFT_Y + Terminal.TunePngY;
    }
}

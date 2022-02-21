//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
//сделать - freeCell
package terminal;

class Weigher {

    Weigher(
            String S_NAME, //2=S_NAME
            int X, //4=X
            int Y, //5=Y
            int SHIFT_X, //6=SHIFT_X
            int SHIFT_Y, //7=SHIFT_Y
            int VIDEO_STATUS, //8=VIDEO_STATUS
            int ORIENTATION //9=ORIENTATION
    ) {
        int orient;
        if (ORIENTATION==9 | ORIENTATION==15 | ORIENTATION==21){
            orient = 3;
        } else {
            orient = 6;
        }
            
        int cmdX = Cell.getRealX_Png(X, SHIFT_X);
        int cmdY = Cell.getRealY_Png(Y, SHIFT_Y);
        Img lbl = new Img();
        Util.prep_lbl(lbl, 1, "weigher__" + String.valueOf(orient), cmdX, cmdY);
        lbl.setToolTipText(S_NAME);
        lbl.setVisible(!(VIDEO_STATUS == 2));// 2 - INVISIBLE
    }
}

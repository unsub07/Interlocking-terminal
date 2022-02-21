//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
//сделать - freeCell
package terminal;

class Unruled {

    Unruled(
            
            int X, //4=X
            int Y, //5=Y
            int SHIFT_X, //6=SHIFT_X
            int SHIFT_Y, //7=SHIFT_Y
            int VIDEO_STATUS, //8=VIDEO_STATUS
            int ORIENTATION //9=VIDEO_STATUS
    ) {
        int cmdX = Cell.getRealX_Png(X, SHIFT_X);
        int cmdY = Cell.getRealY_Png(Y, SHIFT_Y);

        Img lbl = new Img();
        Util.prep_lbl(lbl, 1, "unruled__" + String.valueOf(ORIENTATION) + "__" + (VIDEO_STATUS == 1 ? "future" : "empty"), cmdX, cmdY);

        lbl.setVisible(!(VIDEO_STATUS == 2));// 2 - INVISIBLE
    }
}

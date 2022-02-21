//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
//сделать - freeCell
package terminal;

class Border {

    Border(

            int X,
            int Y,
            int SHIFT_X,
            int SHIFT_Y,
            //int VIDEO_STATUS, //8=VIDEO_STATUS
            int ORIENTATION
    ) {
        int cmdX = Cell.getRealX_Png(X, SHIFT_X);
        int cmdY = Cell.getRealY_Png(Y, SHIFT_Y);
        Img lbl = new Img();
        Util.prep_lbl(lbl, 1, "border__" + String.valueOf(ORIENTATION), cmdX, cmdY);
//        lbl.setVisible(!(VIDEO_STATUS == 2));

//        javax.swing.JLabel lblCmdEmpty = new javax.swing.JLabel();
//        lblCmdEmpty.setBounds(0, 0, 84, 84);
//        lblCmdEmpty.setLocation(cmdX, cmdY);
//        lblCmdEmpty.setOpaque(false);
//        lblCmdEmpty.setVisible(true);//(!(VIDEO_STATUS == 2));// 2 - INVISIBLE
//        terminal.Commander.cmdLayers.add(lblCmdEmpty);
//        lblCmdEmpty.setIcon(Terminal.mainPictureHash.get("border__" + String.valueOf(ORIENTATION)));
//        terminal.Commander.cmdLayers.setLayer(lblCmdEmpty, 1);
    }
}

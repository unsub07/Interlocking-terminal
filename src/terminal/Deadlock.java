//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

class Deadlock extends Cell {
    private final Img lblCmdEmpty = new Img();
    private final Img lblCmdBusy = new Img();
    private final Img lblCmdSkidded = new Img();
    private final Img lblCmdFuture = new Img();
    
    private boolean skidded;// skidded - башмаки
    private boolean busy = true;
    final int id_rn;

    Deadlock(
            int ID_OBJ, //0=ID_OBJ
    //        int ID_AREA, //1=ID_AREA
    //        String S_NAME, //2=S_NAME
    //            String MPC_NAME, //3=MPC_NAME
            int X, //3=X
            int Y, //4=Y
            int SHIFT_X, //5=SHIFT_X
            int SHIFT_Y, //6=SHIFT_Y
            int VIDEO_STATUS, //7=VIDEO_STATUS
            int ORIENTATION, //8=ORIENTATION
    //        boolean LAYER, //9=LAYER
            int ID_RN //10=ID_RN
    ) {
        super(ID_OBJ, "DEADLOCK", X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 77);
        id_rn = ID_RN;
        setPanes(ORIENTATION, VIDEO_STATUS);
    }

    public void setSkidded(boolean SKIDDED) {
        skidded = SKIDDED;
        if (!(lblCmdSkidded == null)) {
            lblCmdSkidded.setVisible(SKIDDED);
        }
    }

    private String cmdPicturePreffix(int ORIENTATION) {
        return "deadlock__" + String.valueOf(ORIENTATION) + "__";
    }

    private void setPanes(int ORIENTATION, int VIDEO_STATUS) {

        cmdX += Terminal.TunePngX;// -6
        cmdY += Terminal.TunePngY;// -6
        if (VIDEO_STATUS==0){
            Util.prep_lbl(lblCmdEmpty, RAILNET_EMPTY_LR, cmdPicturePreffix(ORIENTATION) + "empty", cmdX, cmdY);
            Util.prep_lbl(lblCmdBusy, RAILNET_BUSY_LR, cmdPicturePreffix(ORIENTATION) + "busy",  cmdX, cmdY);
        } else {
            Util.prep_lbl(lblCmdFuture, RAILNET_EMPTY_LR, cmdPicturePreffix(ORIENTATION) + "future",  cmdX, cmdY);
        }        
//        if (skidded){//должно быть skidable!
            Util.prep_lbl(lblCmdSkidded, RAILNET_SKID_LR, cmdPicturePreffix(ORIENTATION) + "skid",  cmdX, cmdY);//FUCK можно использовать просто SKID
//        }

        // ----------------------Set Default setState---------------------------
        if (VIDEO_STATUS == 0) {
            lblCmdEmpty.setVisible(true);
            lblCmdBusy.setVisible(busy);
            lblCmdSkidded.setVisible(skidded);
        }
        if (VIDEO_STATUS == 1) {
            lblCmdEmpty.setVisible(false);
            lblCmdBusy.setVisible(false);
            lblCmdSkidded.setVisible(false);
            lblCmdFuture.setVisible(true);
        }
    }

    void setState(boolean BUSY) {
        if (vStatus == 0) {
            if (busy != BUSY) {
                busy = BUSY;
                lblCmdBusy.setVisible(BUSY);
            }
        }
    }
}

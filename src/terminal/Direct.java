//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
/*
 1-FUTURE-строительство, 2-INVISIBLE-невидимый, 3-DEPRECATE-н.е.н.х.
 */
package terminal;

class Direct extends Cell {

    final int chain;// это типа 4=TOUT, 0=NO_TOUT, 1=PLUS, 2=MINUS
    final int id_tr;
    final int id_rn;
//    private final int vStatus;
    final int inPut;// input direction //private
    final int outPut;// output direction //private
    // privats
//    private final javax.swing.JLabel lblCmdEmpty = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdRoute = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdMRoute = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdBusy = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdBlock = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdIR = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdSkidded = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCmdFuture = new javax.swing.JLabel();

    private final Img lblCmdEmpty = new Img();
    private final Img lblCmdRoute = new Img();
    private final Img lblCmdMRoute = new Img();
    private final Img lblCmdBusy = new Img();
    private final Img lblCmdBlock = new Img();
    private final Img lblCmdIR = new Img();
    private final Img lblCmdSkidded = new Img();
    private final Img lblCmdFuture = new Img();
        
    private final Turnout tc;
    // private final int id_obj2;//это у нас стрелка
    // private final boolean upperLayer;// upper layer if crossing
    private boolean skidded;// skids
    // свойства динамики
    // private int routestate;//def = ?
    private boolean busy = true;// default state
    private boolean route = false;// default state
    private boolean m_route = true;// default state
    private boolean block = false;// default state
    private boolean ir = false;// default state
    private boolean activeChain = true;// default state

    Direct(
            int ID_OBJ, //0=ID_OBJ8
//            int ID_AREA, //1=ID_AREA
            //            String S_NAME, //2=S_NAME
            //            String MPC_NAME, //3=MPC_NAME
            int X, //2=X
            int Y, //3=Y
            int SHIFT_X, //4=SHIFT_X
            int SHIFT_Y, //5=SHIFT_Y
            int VIDEO_STATUS, //6=VIDEO_STATUS
            int ORIENTATION, //7=ORIENTATION
            int SIDE, //8=SIDE
    //        boolean LAYER, //9=LAYER
            int ID_RN, //10=ID_RN
            int ID_TR, //11=ID_TR
            int DIRECT_CHAIN
    //int ID_OBJ2
    ) {// ID_AREA - не нужна
        super(ID_OBJ, "DIRECT", X, Y, SHIFT_X, SHIFT_Y, VIDEO_STATUS, 27);//27-DIRECT

        tc = (Turnout) Terminal.mainCellsHash.get(ID_TR);// id_obj2 - это не gobj в хэше!!!//ID_TR
        id_tr = ID_TR;
        id_rn = ID_RN;
        chain = DIRECT_CHAIN;
        // id_obj2 = ID_OBJ2;
//        vStatus = VIDEO_STATUS;
        inPut = Math.min(ORIENTATION, SIDE);
        outPut = Math.max(ORIENTATION, SIDE);
        // upperLayer = UP_LAYER;
        setPanes(VIDEO_STATUS);
        setDefaultState(VIDEO_STATUS);
    }

    public void setSkidded(boolean SKIDDED) {
        skidded = SKIDDED;
        if (!(lblCmdSkidded == null)) {
            lblCmdSkidded.setVisible(SKIDDED);
        }
    }

    private String cmdPicturePreffix() {// picture prefixes return getCellType().toLowerCase() + "__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__";
        return "direct__" + String.valueOf(inPut) + "_" + String.valueOf(outPut) + "__";
    }

    private void setPanes(int VIDEO_STATUS) {
        cmdX += Terminal.TunePngX;// -6
        cmdY += Terminal.TunePngY;// -6

//        int railnetLayer = (!UP_LAYER) ? RAILNET_LOW_LAYER : RAILNET_HIGH_LAYER;
        Util.prep_lbl(lblCmdEmpty, RAILNET_EMPTY_LR + 1, cmdPicturePreffix() + "empty", cmdX, cmdY);//1
        Util.prep_lbl(lblCmdRoute, RAILNET_ROUTE_LR + 1, cmdPicturePreffix() + "route", cmdX, cmdY);//2
        Util.prep_lbl(lblCmdMRoute, RAILNET_M_ROUTE_LR + 1, cmdPicturePreffix() + "m_route", cmdX, cmdY);//3
        Util.prep_lbl(lblCmdBusy, RAILNET_BUSY_LR + 1, cmdPicturePreffix() + "busy", cmdX, cmdY); // 4
        Util.prep_lbl(lblCmdBlock, RAILNET_BLOCK_LR + 1, cmdPicturePreffix() + "block", cmdX, cmdY); // 5
        Util.prep_lbl(lblCmdIR, RAILNET_IR_LR + 1, cmdPicturePreffix() + "ir", cmdX, cmdY); // 6
        Util.prep_lbl(lblCmdFuture, RAILNET_EMPTY_LR, cmdPicturePreffix() + "future", cmdX, cmdY);//1

        if (((inPut == 3) && (outPut == 15))
                || ((inPut == 6) && (outPut == 18))
                || ((inPut == 9) && (outPut == 21))) {
            Util.prep_lbl(lblCmdSkidded, RAILNET_SKID_LR, cmdPicturePreffix() + "skid", cmdX, cmdY); // 7
        }

//        cmdCellPane.setVisible(!(VIDEO_STATUS == 2));// 2- INVISIBLE
    }

    private void setDefaultState(int VIDEO_STATUS) {// default state

        if (VIDEO_STATUS > 0) {
            if (VIDEO_STATUS == 1) {// 1-FUTURE
                lblCmdFuture.setVisible(true);

                lblCmdBlock.setVisible(false);
                lblCmdBusy.setVisible(false);
                lblCmdEmpty.setVisible(false);// lblCmdEmpty - слой с незанятой РЦ включен постоянно
                lblCmdIR.setVisible(false);
                lblCmdMRoute.setVisible(false);
                lblCmdRoute.setVisible(false);

                if (!(lblCmdSkidded == null)) {
                    lblCmdSkidded.setVisible(false);
                }

            }

        } else {

            lblCmdBlock.setVisible(block);
            Railnet rn = Terminal.Railnets_Hash.get(id_rn);
            if (rn != null){
            if (rn.type == 8){
                lblCmdBusy.setVisible(false);
                lblCmdMRoute.setVisible(false);
            } else {
                lblCmdBusy.setVisible(busy);
                lblCmdMRoute.setVisible(m_route);
            }
            }
            lblCmdEmpty.setVisible(VIDEO_STATUS == 0);// lblCmdEmpty - слой с
            // незанятой РЦ включен
            // постоянно
            lblCmdIR.setVisible(ir);
//            lblCmdMRoute.setVisible(m_route);//FUCK !!!!!!!!!!!!!!!!!!!!!!!
            lblCmdRoute.setVisible(route);
            if (!(lblCmdSkidded == null)) {
                lblCmdSkidded.setVisible(skidded);
            }
            lblCmdFuture.setVisible(false);// 1-FUTURE
        }
    }

    void setState(boolean BUSY, boolean IR, boolean BLOCK, boolean ROUTE, boolean M_ROUTE) {// всё это приходит от рельсовой цепи.
        if (vStatus == 0) {
            busy = BUSY;
            ir = IR;
            block = BLOCK;
            route = ROUTE;
            m_route = M_ROUTE;
            // -------------------------------activechain------------------------------------
            // TurnoutCell tc = (TurnoutCell)
            // Terminal.mainCellsHash.get(id_obj2);//id_obj2 - это не gobj
            // в хэше!!!
            if (chain == 4) {//NO_TOUT
                activeChain = (route || m_route);
            }

            if (tc != null) {

                switch (chain) {
                    case 0:// TOUT
                        activeChain = (tc.moving || tc.fault) && tc.dk;
                        break;
                    case 1:// PLUS
                        activeChain = (tc.fault || tc.plus) && tc.toutInRoute && tc.dk;
                        break;
                    case 2:// MINUS
                        activeChain = (tc.fault || tc.minus) && tc.toutInRoute && tc.dk;
                        break;
////---------------------------dk----------------------------------                        
//                    case 1:// PLUS
//                        activeChain = (tc.fault || tc.plus) && tc.dk;
//                        break;
//                    case 2:// MINUS
//                        activeChain = (tc.fault || tc.minus) && tc.dk;
//                        break;
////---------------------------------------------------------------                        
                    // case 4://NO_TOUT
                    // activeChain = (route || m_route);
                    // break;
                }
            }
            render();
        }
    }

    private void render() {
        lblCmdBlock.setVisible(block);

        // lblCmdBusy, lblCmdRoute, lblCmdMRoute, lblCmdIR
        // если есть какой-либо маршрут, то занятость, поедной, маневровый,
        // разделка -
        // - только на активной цепочке при наличии соотв. признака
        if (route || m_route) {
            lblCmdBusy.setVisible  (activeChain && busy);
            lblCmdRoute.setVisible (activeChain && route);
            lblCmdMRoute.setVisible(activeChain && m_route);
            lblCmdIR.setVisible    (activeChain && ir);
        } else { // если маршрута нет - занятость поголовная при наличии
            // признака, остальные трое - невидимые
            lblCmdBusy.setVisible  (busy);
            lblCmdRoute.setVisible (false);
            lblCmdMRoute.setVisible(false);
            lblCmdIR.setVisible    (false);
        }
    }
}

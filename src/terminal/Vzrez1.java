//Copyright (c) 2011, 2020, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

class Vzrez1 {

    private Counter c = null;
    private Turnout t = null;
    private Railnet r = null;
    private Deadlock dl = null;
    private Unruled u = null;
    private Direct d = null;

    private int COUNTER_ID;
    private int TURNOUT_ID;//17042
    private int DIRECTION;//0 <- //bool 0-left 1-right
    private int CONTROL; //0 - учитывать //bool 0-по шерсти 1-против шерсти (0-учитывается)
    private int POSITION;//1 - plus //int 0-пофиг 1-plus  2-minus (!!! опасное положение)
    private String COUNTER_NAME;
    private String TURNOUT_NAME;
    private int X_O;
    private int Y_O;
    private int X;
    private int Y;
    private int n = 1;
    private int nn = 1;
    private int temp;// 6 последнее направление которое надо сравнивать
    private int object_in_cell;
    private static long tm;// = System.nanoTime();
    //------------------grid----------------------------------------------
//    private JLabel lbl = new JLabel();
//    private final Random rnd = new Random();
//    private final Color gridColor = new Color(150 + rnd.nextInt(70), 150 + rnd.nextInt(70), 150 + rnd.nextInt(70));
    private final Color gridColor = new Color(150, 150, 150);
    private final Color activeColor = new Color(255, 226, 226);
    private final Color destinationColor = new Color(226, 255, 226);
    private final javax.swing.border.Border gridBorder = BorderFactory.createLineBorder(gridColor);
//    static final Dimension DEFAULT_CMD_CELL_SIZE = new Dimension(72, 72);
//    static final int SERVICE_LAYER = 1;    

    Vzrez1() {
//        lbl.setSize(72, 72);
        System.out.println("------------------------------------------------------------------");
        tm = System.nanoTime();
        for (String[] tokens : Terminal.Vzrez_Hash.values()) {
            COUNTER_ID = getInt(tokens[0]);//7083
            TURNOUT_ID = getInt(tokens[1]);//17042
            DIRECTION = getInt(tokens[2]);//0 <-
            CONTROL = getInt(tokens[3]); //0 - учитывать
            POSITION = getInt(tokens[4]);//1 - plus

            c = Terminal.CounterCell_Hash.get(COUNTER_ID);
            COUNTER_NAME = c.s_name;
            t = Terminal.Turnouts_Hash.get(TURNOUT_ID); //test UNRULED_TYPE (4);
            TURNOUT_NAME = t.s_name;
            X_O = c.GX;
            Y_O = c.GY;
            X = c.GX;
            Y = c.GY;
            temp = c.orient;//последняя ориентация

//            if (n == 85) {
//                Log.log("stop");
//            }

            get_next_xy(temp, DIRECTION);
            fill();
            n++;
//            pause();
        }
        tm = System.nanoTime() - tm;
        System.out.println("------------------------------------------------------------------");
        Log.log("Time calculate vzrez: " + tm / 1000000000.0 + " сек.");
        System.out.println("------------------------------------------------------------------");
    }

    private void fill() {
        if (is_tr(X, Y)) {
            if (is_our_tr(X, Y, TURNOUT_ID)) {//наша
                int common = get_common(TURNOUT_ID);
                int plus = get_plus(TURNOUT_ID);
                int minus = get_minus(TURNOUT_ID);
                compare(common, plus, minus);
            } else {//не наша (если стрелка ко мне общим хвостом, то остальные не смотрим. - они будут общим. (т.е. 1,0)
                int tr = get_tr(X, Y);
                int common = get_common(tr);//чужой стрелки
                int plus = get_plus(tr);//чужой стрелки
                int minus = get_minus(tr);//чужой стрелки
                compare2(common, plus, minus, tr);

            }
        } else {//не стрелка
            if (is_direct(X, Y)) {
                get_direct(X, Y);
                int in = d.inPut;// = 6
                int out = d.outPut;
                get_direct_out(in, out, DIRECTION);//следующая клетка
                fill();
//                Log.log(n + " is direct");
            } else { //не нашли
                String s1 = COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + "," + CONTROL + "," + POSITION;
                String s2 = COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + ",1,0";
                if (s1.equals(s2)) {
                    Log.log(n + "  " + s1 + " = " + s2 + " not found ok");
                } else {
                    Log.log(n + "  " + s1 + "|1,0|" + COUNTER_NAME + "," + TURNOUT_NAME + "," + X + "," + Y + ",common" + "," + get_direction(DIRECTION) + " temp:" + temp + " not found");
                show_cell(X, Y);
                }
            }
        }//end else
    }

    private void compare(int common, int plus, int minus) {
        if (test_tout(common)) {
            String s1 = COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + "," + CONTROL + "," + POSITION;
            String s2 = COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + ",1,0";
            if (s1.equals(s2)) {
                Log.log(n + "  " + s1 + " = " + s2);
            } else {
                Log.log(n + "  " + s1 + "|1,0|" + COUNTER_NAME + "," + TURNOUT_NAME + "," + X + "," + Y + ",common" + "," + get_direction(DIRECTION) + " temp:" + temp + " plus:" + plus + " minus:" + minus + " common:" + common);
                show_cell(X, Y);
            }
        } else {
            if (test_tout(plus)) {
                String s1 = COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + "," + CONTROL + "," + POSITION;
                String s2 = COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + ",0,2";
                if (s1.equals(s2)) {
                    Log.log(n + "  " + s1 + " = " + s2);
                } else {
                    Log.log(n + "  " + s1 + "|0,2|" + COUNTER_NAME + "," + TURNOUT_NAME + "," + X + "," + Y + ",plus" + "," + get_direction(DIRECTION) + " temp:" + temp + " plus:" + plus + " minus:" + minus + " common:" + common);
                    show_cell(X, Y);
                }
            } else {
                if (test_tout(minus)) {
                    String s1 = COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + "," + CONTROL + "," + POSITION;
                    String s2 = COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + ",0,1";
                    if (s1.equals(s2)) {
                        Log.log(n + "  " + s1 + " = " + s2);
                    } else {
                        Log.log(n + "  " + s1 + "|0,1|" + COUNTER_NAME + "," + TURNOUT_NAME + "," + X + "," + Y + ",minus" + "," + get_direction(DIRECTION) + " temp:" + temp + " plus:" + plus + " minus:" + minus + " common:" + common);
                        show_cell(X, Y);
                    }
                }
            }
        }
    }

    private void compare2(int common, int plus, int minus, int tr_id) {
        if (test_tout(common)) {
            String s1 = COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + "," + CONTROL + "," + POSITION;
            String s2 = COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + ",1,0";
            if (s1.equals(s2)) {
                Log.log(n + " !" + s1 + " = " + s2 + " not our tr - ok");
            } else {
                Log.log(n + " !" + s1 + "|1,0|" + COUNTER_NAME + "," + TURNOUT_NAME + "," + X + "," + Y + ",common" + "," + get_direction(DIRECTION) + " temp:" + temp + " plus:" + plus + " minus:" + minus + " common:" + common + " tr_id:" + tr_id);
                show_cell(X, Y);
            }
        } else {
            if (test_tout(plus)) {
//            String s1 = COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + "," + CONTROL + "," + POSITION;
//            Log.log(n+"!!" +s1 + " Ищем ывход с плюса на общий ");
                int in = Math.min(plus, common);
                int out = Math.max(plus, common);
                get_direct_out(in, out, DIRECTION);//следующая клетка
                fill();
            } else {
                if (test_tout(minus)) {
//                String s1 = COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + "," + CONTROL + "," + POSITION;
//            Log.log(n+"!!" +s1 + " Ищем ывход с минуса на общий ");
                    int in = Math.min(minus, common);
                    int out = Math.max(minus, common);
                    get_direct_out(in, out, DIRECTION);//следующая клетка
                    fill();
                }//end else if minus
            } //end else if plus
        }//end else if common
    }

    private boolean is_direct(int x, int y) {
        int direct_num = 0;
        for (Direct di : Terminal.DirectCell_Hash.values()) {
            if (di.GX == x & di.GY == y) {
                direct_num++;
            }//end if
        } //end for
        if (direct_num == 1) {
            return true;
        } else {
            if (direct_num > 1) {
                Log.log("В клетке много директов!!!" + x + ", " + y + " direct_num:" + direct_num + " " + COUNTER_ID + "," + TURNOUT_ID + "," + DIRECTION + "," + CONTROL + "," + POSITION + "," + COUNTER_NAME + "," + TURNOUT_NAME + "," + get_direction(DIRECTION));
            }
            return false;
        }
    }

    private int get_direct(int x, int y) { //директ принадлежит стрелке.
        int out = 0;
        for (Direct di : Terminal.DirectCell_Hash.values()) {
            if (di.GX == x & di.GY == y) {
                d = di;
//                d.id_tr;
            }//end if
        }
        return out;
    }

// ----------------------------TURNOUT---------------------------------------
    private boolean is_tr(int x, int y) {
        int turnout_num = 0;
        for (Turnout tu : Terminal.Turnouts_Hash.values()) {
            if (tu.GX == x & tu.GY == y) {
                turnout_num++;
            }//end if
        }
        if (turnout_num == 1) {
            return true;
        } else {
            if (turnout_num > 1) {
                Log.log("В клетке много стрелок!!!" + x + ", " + y);
            }
            return false;
        }
    }

    private int get_tr(int x, int y) {
        int tr = -1;
        for (Turnout tu : Terminal.Turnouts_Hash.values()) {
            if (tu.GX == x & tu.GY == y) {
//                if (tu.type != 4) {//если неуправляемая  test UNRULED_TYPE (4);//ну и что пофиг положение то есть
                tr = tu.id_obj;
//                }
//                break;
            }//end if
        }
        return tr;
    }

    private boolean is_our_tr(int x, int y, int tr_id) {
        if (get_tr(x, y) == -1) {
            return false;
        } else {
            return tr_id == get_tr(x, y);
        }
    }

    private int get_common(int tr_id) {
        t = Terminal.Turnouts_Hash.get(tr_id);
        return t.o;
    }

    private int get_plus(int tr_id) {
        t = Terminal.Turnouts_Hash.get(tr_id);
        int plus;
        if (t.plus_dir) {
            if (t.o < 13) {
                plus = t.o + 12;
            } else {
                plus = t.o - 12;
            }
        } else {
            plus = t.s;
        }
        return plus;
    }

    private int get_minus(int tr_id) {
        t = Terminal.Turnouts_Hash.get(tr_id);
        int minus;
        if (t.plus_dir) {
            minus = t.s;
        } else {
            if (t.o < 13) {
                minus = t.o + 12;
            } else {
                minus = t.o - 12;
            }
        }
        return minus;
    }

    private boolean test_tout(int o) {
        int m = 99;
        boolean ret;
        if (DIRECTION == 1) {// -> смотрим от датчика вправо
            switch (o) {
                case 3:// \
                    m = 15;
                    break;
                case 6:// |
                    m = 18;
                    break;
                case 9:// /
                    m = 21;
                    break;
                case 12:// _
                    m = 24;
                    break;
                case 15://
                    m = 3;
                    break;
                case 18:
                    m = 6;
                    break;
                case 21:
                    m = 9;
                    break;
                case 24:
                    m = 12;
                    break;
//-------------------------------------                    
            }
            ret = temp == m;
        } else {// <- смотрим от датчика влево
            switch (o) {
                case 3:// \
                    m = 15;
                    break;
                case 6:// |
                    m = 18;
                    break;
                case 9:// /
                    m = 21;
                    break;
                case 12:// _
                    m = 24;
                    break;
                case 15://
                    m = 3;
                    break;
                case 18:
                    m = 6;
                    break;
                case 21:
                    m = 9;
                    break;
                case 24:
                    m = 12;
                    break;
            }
            //ret = temp == o;
            ret = temp == m;
        }
        return ret;
    }

    private int getInt(String tag) {
        return Integer.valueOf(tag);
    }

    private int bool2int(boolean b) {
        return b == false ? 0 : 1;
    }

    private String get_direction(int DIRECTION) {
        return DIRECTION == 1 ? "->" : "\\<<-";
    }

    private void get_next_xy(int last_orient, int direction) {
        hide_cell();
        X_O = X;
        Y_O = Y;
        if (direction == 0) {//0 <- //bool 0-left 1-right
            switch (last_orient) {
                case 3:
                    X += 0;//61
                    Y += 0;
                    temp = 15;
                    break;
                case 6:
                    X += 0;
                    Y += 0;
                    temp = 18;
                    break;
                case 9:
                    X += 0;
                    Y += 0;
                    temp = 21;
                    break;
                case 12:
                    X += 0;
                    Y += 0;
                    temp = 24;
                    break;
            }
        } else {//1 -> //bool 0-left 1-right
            switch (last_orient) {
                case 3:
                    X += 1;//61
                    Y += -1;
                    temp = 3;//3
                    break;
                case 6:
                    X += 1;
                    Y += 0;
                    temp = 6;
                    break;
                case 9:
                    X += 1;
                    Y += 1;
                    temp = 9;
                    break;
                case 12:
                    X += 0;
                    Y += 1;
                    temp = 12;
                    break;
            }
        }
//        show_cell(X, Y);

    }

    private void get_RN() {
        if (DIRECTION == 0) {
            r = Terminal.Railnets_Hash.get(c.rn1);//влево
        } else {
            r = Terminal.Railnets_Hash.get(c.rn2);//вправо
        }
    }

    private int get_num_turnout_in_rn() {
        int i = 0;
        for (Turnout tr : Terminal.Turnouts_Hash.values()) {
            if (tr.rn_id == r.id_obj) {
                i++;
            }
        }
        return i;
    }

    private int get_num_counter_in_rn() {
        int i = 0;
        for (Counter co : Terminal.CounterCell_Hash.values()) {
            if (co.rn1 == r.id_obj | co.rn2 == r.id_obj) {
                i++;
            }
        }
        return i;
    }

    private void show_cell(int x, int y) {
        JLabel lbl = new JLabel();
        lbl.setBounds(x * 72, y * 72, 72, 72);
        lbl.setOpaque(false);
        lbl.setBorder(gridBorder);
        lbl.setForeground(gridColor);//red fir dir , green for turnout
        lbl.setHorizontalAlignment(JLabel.CENTER);
        lbl.setVerticalTextPosition(JLabel.CENTER);
        lbl.setText("<html>" + "<b>X: " + String.valueOf(x) + "</b><br>" + "<b>Y: " + String.valueOf(y) + "</b><br>" + get_direction(DIRECTION) + "</html>");// + direction

        terminal.Commander.cmdLayers.add(lbl);
        terminal.Commander.cmdLayers.setLayer(lbl, 27);
        lbl.setVisible(true);
    }

    private void hide_cell() {
//        lbl.setVisible(false);
//        terminal.Commander.cmdLayers.remove(lbl);
    }

    private void pause() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Err.err(e);
        }
    }

    private void get_direct_out(int in, int out, int direction) {
        if (direction == 0) {//<-
            if (in == 3 & out == 12) { //<-
                X += 0;
                Y += +1;
                temp = 12;
            }
            if (in == 3 & out == 15) { //<-
                X += -1;
                Y += +1;
                temp = 15;
            }
            if (in == 3 & out == 18) { //<-
                X += -1;
                Y += 0;
                temp = 18;
            }
//---
            if (in == 6 & out == 15) { //<-
                X += -1;
                Y += +1;
                temp = 15;
            }
            if (in == 6 & out == 18) { //<-
                X += -1;
                Y += 0;
                temp = 18;
            }
            if (in == 6 & out == 21) { //<-
                X += -1;
                Y += -1;
                temp = 21;
            }
//---
            if (in == 9 & out == 18) { //<-
                X += -1;
                Y += 0;
                temp = 18;
            }
            if (in == 9 & out == 21) { //<-
                X += -1;
                Y += -1;
                temp = 21;
            }
            if (in == 9 & out == 24) { //<-
                X += 0;
                Y += -1;
                temp = 24;
            }
//---
            if (in == 12 & out == 21) { //<-
                X += 0;
                Y += 0;
                temp = 21;
            }
            if (in == 12 & out == 24) { //<- xz
                X += -0;
                Y += -1;
                temp = 24;
            }
//---
            if (in == 15 & out == 24) { //<-
                X += -1;
                Y += +1;
                temp = 15;
            }
        } else { //->
            if (in == 3 & out == 12) { //->
                X += +1;
                Y += -1;
                temp = 3;
            }
            if (in == 3 & out == 15) { //->
                X += +1;
                Y += -1;
                temp = 3;
            }
            if (in == 3 & out == 18) { //->
                X += +1;
                Y += -1;
                temp = 3;
            }
//---
            if (in == 6 & out == 15) { //->
                X += +1;
                Y += 0;
                temp = 6;
            }
            if (in == 6 & out == 18) { //->
                X += +1;
                Y += 0;
                temp = 6;
            }
            if (in == 6 & out == 21) { //->
                X += +1;
                Y += 0;
                temp = 6;
            }
//---
            if (in == 9 & out == 18) { //->
                X += 1;
                Y += 1;
                temp = 9;
            }
            if (in == 9 & out == 21) { //->
                X += 1;
                Y += 1;
                temp = 9;
            }
            if (in == 9 & out == 24) { //->
                X += 1;
                Y += 1;
                temp = 9;
            }
//---
            if (in == 12 & out == 21) { //->
                X += 0;
                Y += 1;
                temp = 12;
            }
            if (in == 12 & out == 24) { //-> xz
                X += 0;
                Y += 1;
                temp = 24;
            }
//---
            if (in == 15 & out == 24) { //->
                X += 0;
                Y += -1;
                temp = 24;
            }
        }//end else direction
//        Log.log(get_direction(direction) + " in:" + d.inPut + " out:" + d.outPut);
    }
}
/*
SELECT
  a.counter_id,
  a.turnout_id,
  a.direction,
  a.control,
  a.position,
  a.counter_name,
  a.turnout_name
FROM
  (SELECT
    c.ID_OBJ counter_id,
    c.S_NAME counter_name,
    t.ID_OBJ turnout_id,
    t.S_NAME turnout_name,
    0 direction,
    1 control,
    0 position
    FROM
      COUNTER c,
      TURNOUT t
    WHERE
      c.RN1 = t.RN_ID
  UNION
    SELECT
      c.ID_OBJ counter_id,
      c.S_NAME counter_name,
      t.ID_OBJ turnout_id,
      t.S_NAME turnout_name,
      1 direction,
      1 control,
      0 position
    FROM
      COUNTER c,
      TURNOUT t
    WHERE
      c.RN2 = t.RN_ID) a
--      ,
--      OBJ o,
--      OBJ o2
--    WHERE
--      a.counter_id = o.ID_OBJ
--    AND
--      a.turnout_id = o2.ID_OBJ
  ORDER BY
    counter_id,
    turnout_id
  ASC;
*/

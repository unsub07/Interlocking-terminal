//сделать проверку координат, чтобы не вылазить за край экрана
//или зона аншлага окно
package terminal;

//import javax.swing.*;
import ru.attrans.proc.objs.NoteState;

class Note extends Cell {

    private static final int NOTE_LAYER = 20;
    private final javax.swing.JLayeredPane cmdCellPane = new javax.swing.JLayeredPane();
    private final javax.swing.JLabel lbl = new javax.swing.JLabel();
//    private final javax.swing.JLabel lblCancelButton = new javax.swing.JLabel();
    private final javax.swing.JPopupMenu Popup = new javax.swing.JPopupMenu();
    private final javax.swing.JMenuItem delete_note = new javax.swing.JMenuItem("<html><font color=red><b>X</b></font></html>");

    Note(int GX, int GY, int ID_OBJ, int SHIFT_X, int SHIFT_Y, String text) {
        super(ID_OBJ, "NOTE", GX, GY, SHIFT_X, SHIFT_Y, 0, 13);// 13=NOTE
        lbl.setFont(Terminal.SANS12);
        setText(text);

        cmdCellPane.setLocation(cmdX, cmdY);
//        cmdCellPane.setBackground(new java.awt.Color(255, 240, 240));//255, 200, 153 !!!!!!!!!!!!!!!!!!!!!!!!!!!!1 from color
        cmdCellPane.setBackground(Terminal.ColorsHash.get("NOTE_BACKGROUND"));//255, 200, 153 !!!!!!!!!!!!!!!!!!!!!!!!!!!!1 from color
        cmdCellPane.setOpaque(true);

        prep_lbl(lbl, text);
//        lblCmdText.setForeground(Color.red);// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1 from color
        lbl.setForeground(Terminal.ColorsHash.get("NOTE_FOREGROUND"));// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1 from color

        if (Terminal.DSP) {
            cmdCellPane.addMouseMotionListener(new moveCmdCellPaneHandler());
            cmdCellPane.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    lblCmdTextMouseReleased(evt);
                }
            });
//            lblCancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
//                @Override
//                public void mousePressed(java.awt.event.MouseEvent evt) {
//                    lblCancelButtonMousePressed();
//                }
//            });
            setNotePopup();
        }// end noGuest
        terminal.Commander.cmdLayers.add(cmdCellPane);
        terminal.Commander.cmdLayers.setLayer(cmdCellPane, Note.NOTE_LAYER);
    }

    static void Create_Note(Object obj) {
        NoteState ns = (NoteState) obj;
        Create_Note(ns.gX, ns.gY, ns.shiftX, ns.shiftY, ns.text, ns.objId);
    }

    private static void Create_Note(int gX, int gY, int shiftX, int shiftY, String text, int ID_OBJ) {
        new Note(gX, gY, ID_OBJ, shiftX, shiftY, text);
    }

    static void create_note_gobj(int gX, int gY, int shiftX, int shiftY) {
        Net.Send("NOTE:CREATE:" + gX + "|" + gY + "|" + shiftX + "|" + shiftY + "|" + "Внимание!");// + "|" + String.valueOf(new java.util.Date().getTime());
    }

    private static void update_note_gobj(int idGobj, int gX, int gY, int shiftX, int shiftY, String text) {
        Net.Send("NOTE:UPDATE:" + idGobj + ":" + gX + "|" + gY + "|" + shiftX + "|" + shiftY + "|" + text);
    }

    private static void delete_note_gobj(int idGobj) {
        Net.Send("NOTE:DELETE:" + idGobj);
    }

    private static void delete_note(int idGobj) {
        Note nc = (Note) Terminal.mainCellsHash.get(idGobj);
        nc.cmdCellPane.setEnabled(false);
        nc.cmdCellPane.setVisible(false);
        if (Terminal.mainCellsHash.containsKey(idGobj)) {
            Terminal.mainCellsHash.remove(idGobj);
        }
    }

    private void prep_lbl(javax.swing.JLabel lbl, String txt) {
        lbl.setText(txt);
        lbl.setBorder(javax.swing.BorderFactory.createLineBorder(Terminal.ColorsHash.get("NOTE_BORDER"), 2));
        lbl.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        lbl.setVerticalAlignment(javax.swing.JLabel.CENTER);
        cmdCellPane.setLayer(cmdCellPane.add(lbl), 1);
    }

    private void Update_Note(int gX, int gY, int shiftX, int shiftY, String text, int ID_OBJ) {
        delete_note(ID_OBJ);
        if (!(gX == 0 && gY == 0 && shiftX == 0 && shiftY == 0 && "".equals(text))) {
            Create_Note(gX, gY, shiftX, shiftY, text, ID_OBJ);
        }
    }

    @Override
    public void setState(Object oid) {
        NoteState n = (NoteState) oid;
        this.setState(n.timestamp, n.gX, n.gY, n.shiftX, n.shiftY, n.text, n.objId);
    }

    private void setState(long DTIME, int gX, int gY, int shiftX, int shiftY, String text, long id_gobj) {
        setDtime(DTIME);
        int id = (int) id_gobj;
        Update_Note(gX, gY, shiftX, shiftY, text, id);
    }

    private void setText(String text) {
        lbl.setText(text);
        int txt_width = lbl.getFontMetrics(lbl.getFont()).stringWidth(text) + 8;
        if (txt_width > 44){
            lbl.setBounds(0, 0, txt_width, 18);
            cmdCellPane.setSize(txt_width, 18);
        } else {
            lbl.setBounds(0, 0, 44, 18);
            cmdCellPane.setSize(44, 18);
        }
    }

    private void delete_noteActionPerformed() {
        delete_note_gobj(id_obj);
    }    
    
    private void lblCmdTextMouseReleased(java.awt.event.MouseEvent evt) {
        if (evt.isControlDown()) {
            delete_note_gobj(id_obj);
        } else {
            int x = cmdCellPane.getX(), y = cmdCellPane.getY();
            int gX = x / 72,
                    gY = y / 72,
                    sX = x - gX * 72,
                    sY = y - gY * 72;
            Note.update_note_gobj(id_obj, gX, gY, sX, sY, lbl.getText());
//        System.out.println("update: " + id_obj + " gX " + gX + " gY " + gY + " sX " + sX + " sY " + sY + " text " + lblCmdText.getText());
        }
    }

    private void setNotePopup() {
        if (!Terminal.Note_Menu.isEmpty()) {
            Terminal.Note_Menu.values().stream().map(javax.swing.JMenuItem::new).peek((mi) -> mi.addActionListener(this::miActionPerformed)).forEach(Popup::add);
        }
        Popup.addSeparator();
        Popup.add(delete_note);
        delete_note.addActionListener((java.awt.event.ActionEvent evt) -> delete_noteActionPerformed());
        cmdCellPane.setComponentPopupMenu(Popup);
    }

    private void miActionPerformed(java.awt.event.ActionEvent evt) {
        setText(((javax.swing.AbstractButton) evt.getSource()).getText());
        int x = cmdCellPane.getX(), y = cmdCellPane.getY();
        int gX = x / 72,
                gY = y / 72,
                sX = x - gX * 72,
                sY = y - gY * 72;
        Note.update_note_gobj(id_obj, gX, gY, sX, sY, lbl.getText());
    }

    private class moveCmdCellPaneHandler extends java.awt.event.MouseMotionAdapter {
        @Override
        public void mouseDragged(java.awt.event.MouseEvent e) {
            java.awt.Component c = e.getComponent();
            c.setLocation(c.getX() + e.getX(), c.getY() + e.getY());
        }
    }
}

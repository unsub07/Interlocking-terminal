//Copyright (c) 2011, 2020, ЗАО "НПЦ "АТТРАНС"
package terminal;

//import java.awt.Component;

class Cabinet extends javax.swing.JPanel {

    private final int id_obj;// Шкаф
    final int type;// тип шкафа
    final int racks;

    Cabinet(int ID_OBJ, String S_NAME, int RACKS, int TYPE) {
        super();
        id_obj = ID_OBJ;
        racks = RACKS;
        type = TYPE;
        setName(S_NAME);
        setBorder(javax.swing.BorderFactory.createTitledBorder(getName()));
        setLayout(new java.awt.GridLayout(RACKS, 1));
        setFont(Terminal.SANS09);
        
        int grid_col;
        if (type !=0){
            grid_col = 5;//hima
        } 
        else {
            grid_col = 12;//siemens
        }

        for (int i = 0; i < RACKS; i++) {
            Rack r = new Rack(i + 1);
            r.setLayout(new java.awt.GridLayout(1, grid_col, 2, 2));
            add(r);// Добавить полку
        }

    }

    int getIdObj() {
        return id_obj;
    }

    Rack getRack(int rackNumber) {
        Rack rack = null;
        for (java.awt.Component c : getComponents()) {
            if (c instanceof Rack) {
                if (((Rack) c).number == rackNumber) {
                    rack = (Rack) c;
                }
            }
        }
        return rack;
    }
}

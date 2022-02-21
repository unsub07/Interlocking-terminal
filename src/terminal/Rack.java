//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

class Rack extends javax.swing.JPanel {

    final int number;//номер полки
//    private final Unit[] unitArray = new Unit[12];
    private int numUnit = 0;//колличество юнитов на полке

    Rack(int NUMBER) {//int M количество пустых ячеек 12 сименс 5 хима
        super();
        number = NUMBER;
        setBorder(javax.swing.BorderFactory.createTitledBorder(
                javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createEtchedBorder(),
                        javax.swing.BorderFactory.createLineBorder(
                                java.awt.Color.GREEN, 2)), String
                .valueOf(NUMBER)));
        setFont(Terminal.SANS09);
    }

//    int getNumber() {
//        return number;
//    }
    void plugUnit(
//            int PLACE,
            Unit u) {
//        unitArray[PLACE - 1] = u;
        numUnit++;
        add(u);
    }

//	void plugDummyUnits() {
//		for (Unit anUnitArray : unitArray) {
//			if (anUnitArray == null) {
//				// unitArray[i] = new Unit("DUMMY", 0, "", "", 0, 0, 0, 0);
//			}
//		}
//	}
//	void fillHash() {
//		plugDummyUnits();
//		for (Unit u : unitArray) {
//			if (!u.isDummy()) {
//				EWAreaMonitor.Unit_Hash.put(u.getIdObj(), u);
//			}
//			add(u);
//		}
//	}
    void fillHash(int m) {
        for (int i = numUnit; i < m; i++) {
            javax.swing.JLabel u = new javax.swing.JLabel();
            u.setPreferredSize(new java.awt.Dimension(30, 30));
//            u.setHorizontalAlignment(javax.swing.JLabel.CENTER);
//            u.setVerticalAlignment(javax.swing.JLabel.CENTER);
            u.setOpaque(true);
            u.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            add(u);
        }
    }
}

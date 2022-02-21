//Copyright (c) 2011, 2020, ЗАО "НПЦ "АТТРАНС"
package terminal;

class Beam extends javax.swing.JPanel {

//    private static final int maxCountersOnBeam = 25;//надо кол-во брать из конфигуратора
    final String s_name;
    int counterAmount;
    final int cmd2;

    Beam(
            //        int ID_OBJ,
            //        int COUNT_UNIT_ID,
            String S_NAME,
            int CMD2) {
        super();
//        count_unit_id = COUNT_UNIT_ID;
        s_name = S_NAME;
        cmd2 = CMD2;
        setBorder(javax.swing.BorderFactory.createTitledBorder(
                javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createEtchedBorder(),
                        javax.swing.BorderFactory.createLineBorder(
                                java.awt.Color.GREEN, 2)), S_NAME));
        setFont(Terminal.SANS09);
    }

    void plugCounter(javax.swing.JLabel Counter) {
        add(Counter);
        counterAmount++;
    }

    void fillHash() {// пустые
        while (counterAmount < CpuMon.max) {
            Img Counter = new Img();
            Counter.setPreferredSize(new java.awt.Dimension(32, 30));
//            Counter.setHorizontalAlignment(javax.swing.JLabel.CENTER);
//            Counter.setVerticalAlignment(javax.swing.JLabel.CENTER);
            Counter.setOpaque(true);
            Counter.setBorder(null);
            add(Counter);
            counterAmount++;
        }
    }
}// end class Beam

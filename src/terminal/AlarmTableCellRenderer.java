//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

class AlarmTableCellRenderer extends javax.swing.table.DefaultTableCellRenderer {

//    private static final long serialVersionUID = 1L;

    @Override
    public java.awt.Component getTableCellRendererComponent(
            javax.swing.JTable table,
            Object obj,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column
    ) {
        java.awt.Component cell = super.getTableCellRendererComponent(
                table,
                obj,
                isSelected,
                hasFocus,
                row,
                column
        );

        boolean unRepaired = table.getValueAt(row, 1).equals("-");//restored
        boolean unCheckedByDisp = table.getValueAt(row, 5).equals("-");//dsp
        
        java.awt.Color defSelBack = table.getSelectionBackground();
        java.awt.Color defSelFor = table.getSelectionForeground();
        java.awt.Color defBack = table.getBackground();
        java.awt.Color defFor = table.getForeground();

        if (unRepaired && unCheckedByDisp) {
            if (isSelected) {
                cell.setBackground(defSelBack);
                cell.setForeground(java.awt.Color.PINK);
            } else {
                cell.setBackground(java.awt.Color.PINK);
                cell.setForeground(java.awt.Color.RED);
            }
        }
        if (unRepaired && !unCheckedByDisp) {
            if (isSelected) {
                cell.setBackground(defSelBack);
                cell.setForeground(java.awt.Color.PINK);
            } else {
                cell.setBackground(java.awt.Color.PINK);
                cell.setForeground(java.awt.Color.RED);
            }
        }
        if (!unRepaired && unCheckedByDisp) {
            if (isSelected) {
                cell.setBackground(defSelBack);
                cell.setForeground(java.awt.Color.CYAN);
            } else {
                cell.setBackground(java.awt.Color.CYAN);
                cell.setForeground(java.awt.Color.BLUE);
            }
//            System.out.println("cyan");
        }
        if (!unRepaired && !unCheckedByDisp) {
            if (isSelected) {
                cell.setBackground(defSelBack);
                cell.setForeground(defSelFor);
            } else {
                cell.setBackground(defBack);
                cell.setForeground(defFor);
            }
//            System.out.println("white");
        }
        return cell;
    }
}

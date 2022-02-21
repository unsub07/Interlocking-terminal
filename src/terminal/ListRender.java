//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

//class ListRender <JList> extends javax.swing.JComponent implements javax.swing.ListCellRenderer  {

class ListRender <JList> extends javax.swing.JList implements javax.swing.ListCellRenderer {

//    private static final long serialVersionUID = 1L;
//    javax.swing.JPanel panel;
    
    @Override
    public java.awt.Component getListCellRendererComponent(
            javax.swing.JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        javax.swing.JPanel panel = (javax.swing.JPanel) value;
        java.awt.Color background = isSelected ? list.getSelectionBackground() : list.getBackground();
        java.awt.Color foreground = isSelected ? list.getSelectionForeground() : list.getForeground();
        panel.setForeground(foreground);
        panel.setBackground(background);
        return panel;
    }
    
//    @Override
//    public java.awt.Dimension getPreferredSize() {
//        return new java.awt.Dimension(panel.getWidth(), 60);
//    }
}
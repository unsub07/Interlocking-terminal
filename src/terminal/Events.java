//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
package terminal;

class Events extends javax.swing.JInternalFrame {

//    private static final long serialVersionUID = 1L;
    private static final javax.swing.table.DefaultTableModel DTM = new javax.swing.table.DefaultTableModel();
    private static boolean init = true;
    private static int count_mes = 0;
    // --Commented out by Inspection (16.01.18 15:00):private static java.util.SortedMap<Long, Object[]> init_mes;

    // 1417497954480l|15|AREANAME_SET_CANCEL2|VGN
    static void InsertCmdMessage(String line) {

        String[] s0 = line.split(":");
        String[] s1 = s0[1].split("\\|");// String[] s = line.split(":");

        if (Terminal.Cmd.containsKey(s1[2])) {
            if (s1.length == 4) {

                if (DTM.getRowCount() > 100) {
                    DTM.removeRow(DTM.getRowCount() - 1);
                }

                int id_gobj = Integer.valueOf(s1[1]);
                if (Terminal.obj_name.containsKey(id_gobj)) {
                    // Cell c;
                    // c = Terminal.mainCellsHash.get(id_gobj);
                    //
                    // int id_obj = c.id_obj;
                    // if (id_obj == id_gobj) {

                    // String obj_type = objType(c.getCellType());
                    try {
                        DTM.insertRow(
                                0,
                                new Object[]{
                                    Util.DateFromLong(Long.valueOf(s1[0])), // Дата
                                    Util.TimeFromLong(Long.valueOf(s1[0])), // Время
 //                                   Terminal.AREA,// Район
                                    Util.objType(id_gobj),// Тип (// s1[1],// Объект
                                    Terminal.obj_name.get(Integer.valueOf(s1[1])),
                                    Terminal.GetCmd(s1[2]).replaceAll("%", "<font color=red><b>" + s1[3] + ": </b></font>") // Состояние
                                });
                    } catch (SecurityException e) {
                        Err.err(e);
                    }
                    // }
                }// end if

            }
        }
    }

    static void InsertMessage(long dt, int ID_OBJ, int mes) {

        if (init) {
            if (count_mes >= 20) {
                init = false;
                // fill_table();
            }
            // init_mes.put(dt, new Object[]{
            // Util.DateFromLong(dt), // Дата
            // Util.TimeFromLong(dt), // Время
            // Terminal.AREA,// Район
            // Util.objType(type),// Тип
            // s_name,// Объект
            // Terminal.GetMess(mes)//Состояние
            // });

        }
        count_mes += 1;
        // System.out.println("count mes " + count_mes);

        if (count_mes >= 200) {
            DTM.removeRow(DTM.getRowCount() - 1);
            count_mes -= 1;
        }
        DTM.insertRow(0, new Object[]{Util.DateFromLong(dt), // Дата
            Util.TimeFromLong(dt), // Время
//            Terminal.AREA,// Район
            Util.objType(ID_OBJ),// Тип
            Terminal.obj_name.get(ID_OBJ),// Объект ------- проверить
            // существует ли?
            Terminal.GetMess(mes) // Состояние
    });
    }

//    private static void fill_table() {
//        for (int i = 0; i < init_mes.size(); i++) {
//            long k = init_mes.firstKey();
//            DTM.insertRow(0, init_mes.get(k));
//            init_mes.remove(k);
//        }
//    }

    Events() {
        initComponents();
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            @Override
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            @Override
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing();
            }
            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            @Override
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            @Override
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            @Override
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            @Override
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        
        // -------------------------------Translate-----------------------------
//        String Title = Terminal.Translate("EventTitle", "Сообщения МПЦ");
//        String date = Terminal.Translate("Date", "Дата");
//        String time = Terminal.Translate("Time", "Время");
////        String area = Terminal.Translate("Area", "Район");
//        String type = Terminal.Translate("Type", "Тип");
//        String object = Terminal.Translate("Object", "Объект");
//        String condition = Terminal.Translate("Condition", "Состояние");
        
        String Title = "Сообщения МПЦ";
        String date = "Дата";
        String time = "Время";
//        String area = Terminal.Translate("Area", "Район");
        String type = "Тип";
        String object = "Объект";
        String condition = "Состояние";
        // -------------------------------init table----------------------------
        // while (DTM.getRowCount() > 0) {
        // DTM.removeRow(0);
        // }
        DTM.setColumnCount(5);
        DTM.setColumnIdentifiers(new Object[]{date, time, type, object, condition});
        eventTable.getTableHeader().setFont(Terminal.SANS11);
        eventTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        eventTable.getColumnModel().getColumn(0).setPreferredWidth(85); // Дата
        eventTable.getColumnModel().getColumn(1).setPreferredWidth(75); // Время
//        eventTable.getColumnModel().getColumn(2).setPreferredWidth(160); // Район
        eventTable.getColumnModel().getColumn(2).setPreferredWidth(160); // Тип объекта
        eventTable.getColumnModel().getColumn(3).setPreferredWidth(160); // Объект
        eventTable.getColumnModel().getColumn(4).setPreferredWidth(550); // Состояние
        eventTable.getTableHeader().setReorderingAllowed(false);
        // ------------------------------------------------------------------------------
        setTitle(Title);
    }

   private void formInternalFrameClosing() {
        AltMenu.toggleEventer.setSelected(false);
        setVisible(false);
    }    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JScrollPane eventTableScroller = new javax.swing.JScrollPane();
        eventTable = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(true);
        setTitle("Сообщения МПЦ");
        setVisible(true);

        eventTable.setModel(DTM);
        eventTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        eventTableScroller.setViewportView(eventTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(eventTableScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(eventTableScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable eventTable;
    // End of variables declaration//GEN-END:variables
}

//Copyright (c) 2011, 2019, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.util.Arrays;

class Vgn extends javax.swing.JInternalFrame {

//    private static final long serialVersionUID = 1L;
    private static final javax.swing.table.DefaultTableModel DTM = new javax.swing.table.DefaultTableModel();
    //пустые
//    private static final javax.swing.ImageIcon loco    = Terminal.mainPictureHash.get("loco");      //1
    private static final javax.swing.ImageIcon bunker3 = Terminal.mainPictureHash.get("bunker3");   //2
    private static final javax.swing.ImageIcon bunker4 = Terminal.mainPictureHash.get("bunker4");   //3
    private static final javax.swing.ImageIcon dgku    = Terminal.mainPictureHash.get("dgku");      //4
    private static final javax.swing.ImageIcon hopper = Terminal.mainPictureHash.get("hopper");     //5
    private static final javax.swing.ImageIcon kryt = Terminal.mainPictureHash.get("kryt");         //6
    private static final javax.swing.ImageIcon plat_c = Terminal.mainPictureHash.get("plat_c");     //7
    private static final javax.swing.ImageIcon platf = Terminal.mainPictureHash.get("platf");       //8
    private static final javax.swing.ImageIcon plat = Terminal.mainPictureHash.get("plat");         //9
    private static final javax.swing.ImageIcon plf = Terminal.mainPictureHash.get("plf");           //10
    private static final javax.swing.ImageIcon polu = Terminal.mainPictureHash.get("polu");         //11
    private static final javax.swing.ImageIcon sneg = Terminal.mainPictureHash.get("sneg");         //12
    private static final javax.swing.ImageIcon spec = Terminal.mainPictureHash.get("spec");         //13
    private static final javax.swing.ImageIcon vcb = Terminal.mainPictureHash.get("vcb");           //14
    private static final javax.swing.ImageIcon vcgaz = Terminal.mainPictureHash.get("vcgaz");       //15
    //груженые
    private static final javax.swing.ImageIcon bunker3_g = Terminal.mainPictureHash.get("bunker3_g");//16
    private static final javax.swing.ImageIcon bunker4_g = Terminal.mainPictureHash.get("bunker4_g");//17
    private static final javax.swing.ImageIcon hopper_g = Terminal.mainPictureHash.get("hopper_g");  //18
    private static final javax.swing.ImageIcon kryt_g = Terminal.mainPictureHash.get("kryt_g");      //19
    private static final javax.swing.ImageIcon platf_g = Terminal.mainPictureHash.get("platf_g");    //20
    private static final javax.swing.ImageIcon plat_g = Terminal.mainPictureHash.get("plat_g");      //21
    private static final javax.swing.ImageIcon polu_g = Terminal.mainPictureHash.get("polu_g");      //22
    private static final javax.swing.ImageIcon vcb_g = Terminal.mainPictureHash.get("vcb_g");        //23
    private static final javax.swing.ImageIcon vcgaz_g = Terminal.mainPictureHash.get("vcgaz_g");    //24

    private static final javax.swing.JLabel loco_l = new javax.swing.JLabel(Terminal.mainPictureHash.get("loco"));//1
    private static final javax.swing.JLabel bunker3_l = new javax.swing.JLabel();//2
    private static final javax.swing.JLabel bunker4_l = new javax.swing.JLabel();//3
    private static final javax.swing.JLabel dgku_l = new javax.swing.JLabel();//4
    private static final javax.swing.JLabel hopper_l = new javax.swing.JLabel();//5
    private static final javax.swing.JLabel kryt_l = new javax.swing.JLabel();//6
    private static final javax.swing.JLabel plat_c_l = new javax.swing.JLabel();//7
    private static final javax.swing.JLabel platf_l = new javax.swing.JLabel();//8
    private static final javax.swing.JLabel plat_l = new javax.swing.JLabel();//9
    private static final javax.swing.JLabel plf_l = new javax.swing.JLabel();//10
    private static final javax.swing.JLabel polu_l = new javax.swing.JLabel();//11
    private static final javax.swing.JLabel sneg_l = new javax.swing.JLabel();//12
    private static final javax.swing.JLabel spec_l = new javax.swing.JLabel();//13
    private static final javax.swing.JLabel vcb_l = new javax.swing.JLabel();//14
    private static final javax.swing.JLabel vcgaz_l = new javax.swing.JLabel();//15
    private static final javax.swing.JLabel bunker3_g_l = new javax.swing.JLabel();//16
    private static final javax.swing.JLabel bunker4_g_l = new javax.swing.JLabel();//17
    private static final javax.swing.JLabel hopper_g_l = new javax.swing.JLabel();//18
    private static final javax.swing.JLabel kryt_g_l = new javax.swing.JLabel();//19
    private static final javax.swing.JLabel platf_g_l = new javax.swing.JLabel();//20
    private static final javax.swing.JLabel plat_g_l = new javax.swing.JLabel();//21
    private static final javax.swing.JLabel polu_g_l = new javax.swing.JLabel();//22
    private static final javax.swing.JLabel vcb_g_l = new javax.swing.JLabel();//23
    private static final javax.swing.JLabel vcgaz_g_l = new javax.swing.JLabel();//24

    // --Commented out by Inspection (16.01.18 14:08):private boolean gruz = true;
    private static int count_mes = 0;

    Vgn() {
        initComponents();
//Graphics2D g2d;
//        g2d.
        pnlVgn.setLayout(null);

//        loco_l.setIcon(loco);//1
//loco_l.setSize(32, 32);
//loco_l.setOpaque(false);
//loco_l.setVisible(false);
        bunker3_l.setIcon(bunker3);//2
        bunker4_l.setIcon(bunker4);//3
        dgku_l.setIcon(dgku);//4
        hopper_l.setIcon(hopper);//5
        kryt_l.setIcon(kryt);//6
        plat_c_l.setIcon(plat_c);//7
        platf_l.setIcon(platf);//8
        plat_l.setIcon(plat);//9
        plf_l.setIcon(plf);//10
        polu_l.setIcon(polu);//11
        sneg_l.setIcon(sneg);//12
        spec_l.setIcon(spec);//13
        vcb_l.setIcon(vcb);//14
        vcgaz_l.setIcon(vcgaz);//15
        bunker3_g_l.setIcon(bunker3_g);//16
        bunker4_g_l.setIcon(bunker4_g);//17
        hopper_g_l.setIcon(hopper_g);//18
        kryt_g_l.setIcon(kryt_g);//19
        platf_g_l.setIcon(platf_g);//20
        plat_g_l.setIcon(plat_g);//21
        polu_g_l.setIcon(polu_g);//22
        vcb_g_l.setIcon(vcb_g);//23
        vcgaz_g_l.setIcon(vcgaz_g);//24

        DTM.setColumnCount(7);
        DTM.setColumnIdentifiers(new Object[]{"Дата", "№ Вагона", "Тип", "Ком.сост", "Осмотр", "Собственник", "Оси"});
        vgnTable.getTableHeader().setFont(Terminal.SANS11);
        vgnTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        vgnTable.getColumnModel().getColumn(0).setPreferredWidth(140); // Дата
        vgnTable.getColumnModel().getColumn(1).setPreferredWidth(75);  // № Вагона
        vgnTable.getColumnModel().getColumn(2).setPreferredWidth(160); // Тип
        vgnTable.getColumnModel().getColumn(3).setPreferredWidth(160); // Ком.сост
        vgnTable.getColumnModel().getColumn(4).setPreferredWidth(140); // Осмотр
        vgnTable.getColumnModel().getColumn(5).setPreferredWidth(250); // Собственник
        vgnTable.getColumnModel().getColumn(6).setPreferredWidth(40);  // Осей
        vgnTable.getTableHeader().setReorderingAllowed(false);

        setTitle("Список вагонов");
    }

    static void Show(String line){
        
        while (DTM.getRowCount() > 0) {
            DTM.removeRow(0);
        }

        count_mes = 0;
        pnlVgn.removeAll();
        String[] a1 = line.split("\\|");
        long dt = Long.valueOf(a1[0]);
        int id_obj = Integer.valueOf(a1[1]);
        
        String k = String.valueOf(a1[2]);
        
        String [] a2 = k.split("\\{");
        
//        for (int i = 1; i < a2.length; i = i + 1) {
        int xx = 1;
        for (int i = a2.length-1; i >= 1; i -= 1) {            
            String s = String.valueOf(a2[i]);
            String[] s0 = s.split(",");
//            System.out.println("length " + s0.length);
//            System.out.println(i + " N_VGN = " + s0[0]);
int cod_pic = 0;
//==========// КодКартинки (1 цистерна, 2 Товарный вагон, 4 локомотив)
            if (!"".equals(s0[1])){
                cod_pic = Integer.valueOf(s0[1]);
            }
//==========// СостояниеКод ()
//            if (!"".equals(s0[2])){
//                 int cod_sost = Integer.valueOf(s0[2]);
//            }
            
//==========// ТехнологическаяОперацияКод (Выгрузка, Погрузка)
//            if ("".equals(s0[3])){
//                int cod_oper = 0;
//            } else {
//                int cod_oper = Integer.valueOf(s0[3]);
//            }
            
//==========// РодГрузаКод (Бензин, сжиженный газ, ...)
//            if ("".equals(s0[4])){
//                
//            } else {
//                int cod_gruz = Integer.valueOf(s0[4]);
//            }
//==========
            //? ЖД Оператор
            //? Контрагент
                   
//            InsertImage(dt, Integer.valueOf(s0[0]), cod_pic, oper(s0[3]), gruz(s0[4]), xx);
            InsertImage(dt, s0[0], cod_pic, oper(s0[3]), gruz(s0[4]), xx);
            xx += 50;

        }
        pnlVgn.setPreferredSize(new java.awt.Dimension(xx, 24));
        pnlVgn.validate();
                
//        Vgn.InsertImage(System.currentTimeMillis(), 502102158, "Локомотив", "-", "-", "РЖД", 6);
//        Vgn.InsertImage(System.currentTimeMillis(), 502102159, "Платформа", "Порожний", "19,08,2017", "СТ", 4);
//        Vgn.InsertImage(System.currentTimeMillis(), 702102167, "Цистерна", "Пропан", "-", "Сибур", 4);
//        Vgn.InsertImage(System.currentTimeMillis(), 502102158, "Цистерна", "Изобутан", "-", "Сибур-Тр", 4);
//        Vgn.InsertImage(System.currentTimeMillis(), 902102158, "Кран", "-", "-", "-", 4);
        
        Commander.vgn.setTitle("Сисок вагонов : " + Terminal.Railnets_Hash.get(id_obj).s_name + " - " + (a2.length-1) + testEnd(a2.length-1));
        Commander.vgn.setVisible(true);
    }

//    private static void InsertImage(long dt, int N_VGN, int pic, String state, String view, int x) {
    private static void InsertImage(long dt, String N_VGN, int pic, String state, String view, int x) {
        count_mes += 1;
//        int x = 1;
        
        String s = "";
        int axis = 0;
        switch (pic) {
                case 1:
                    s = "Цистерна 4x осная";
//                    vcb_l.setBounds(x, 1, 50, 30);
//                    pnlVgn.add(vcb_l);//new javax.swing.JLabel()
                    pnlVgn.add(new javax.swing.JLabel(vcb));//
                    pnlVgn.getComponent(pnlVgn.getComponentCount()-1).setBounds(x, 1, 50, 30);
//                    pnlVgn.getComponent(count_mes).setIcon(vcb);
                    axis = 4;
                    break;
                case 2:
                    s = "Товарный вагон";
                    kryt_l.setBounds(x, 1, 50, 30);
                    pnlVgn.add(kryt_l);  
                    axis = 4;
                    break;
                case 3:
                    s = "цистерна 8х осная";
                    vcgaz_g_l.setBounds(x, 1, 50, 30);
                    pnlVgn.add(vcgaz_g_l);
                    axis = 4;
                    break;
                case 4:
                    s = "Локомотив";
                    loco_l.setBounds(x, 1, 50, 30);
                    pnlVgn.add(loco_l);
                    axis = 6;
                    break;
                default:
                    s = "Цистерна 4x осная";
//                    vcb_l.setBounds(x, 1, 50, 30);
//                    pnlVgn.add(vcb_l);//new javax.swing.JLabel()
                    pnlVgn.add(new javax.swing.JLabel(vcb));//
                    pnlVgn.getComponent(pnlVgn.getComponentCount()-1).setBounds(x, 1, 50, 30);
//                    pnlVgn.getComponent(count_mes).setIcon(vcb);
                    axis = 4;
                    System.out.println("============================== " + pic);
                    break;
            }
        System.out.println("pnlVgn.getComponents() " + Arrays.toString(pnlVgn.getComponents()));
pnlVgn.setPreferredSize(new java.awt.Dimension(x+51, 24));
//        loco_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(loco_l);
//        x = x + 50;
//        bunker3_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(bunker3_l);
//        x = x + 50;
//        bunker4_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(bunker4_l);
//        x = x + 50;
//        dgku_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(dgku_l);
//        x = x + 50;
//        hopper_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(hopper_l);
//        x = x + 50;
//        kryt_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(kryt_l);
//        x = x + 50;
//        plat_c_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(plat_c_l);
//        x = x + 50;
//        platf_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(platf_l);
//        x = x + 50;
//        plat_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(plat_l);
//        x = x + 50;
//        plf_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(plf_l);
//        x = x + 50;
//        polu_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(polu_l);
//        x = x + 50;
//        sneg_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(sneg_l);
//        x = x + 50;
//        spec_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(spec_l);
//        x = x + 50;
//        vcb_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(vcb_l);
//        x = x + 50;
//        vcgaz_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(vcgaz_l);
//        x = x + 50;
//        bunker3_g_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(bunker3_g_l);
//        x = x + 50;
//        bunker4_g_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(bunker4_g_l);
//        x = x + 50;
//        hopper_g_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(hopper_g_l);
//        x = x + 50;
//        kryt_g_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(kryt_g_l);
//        x = x + 50;
//        platf_g_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(platf_g_l);
//        x = x + 50;
//        plat_g_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(plat_g_l);
//        x = x + 50;
//        polu_g_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(polu_g_l);
//        x = x + 50;
//        vcb_g_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(vcb_g_l);
//        x = x + 50;
//        vcgaz_g_l.setBounds(x, 1, 50, 30);
//        pnlVgn.add(vcgaz_g_l);
//        x = x + 50;
        pnlVgn.setPreferredSize(new java.awt.Dimension(x, 24));
        pnlVgn.validate();

        if (count_mes >= 200) {
            DTM.removeRow(DTM.getRowCount() - 1);
            count_mes -= 1;
        }

        DTM.insertRow(0, new Object[]{
            Util.DateFromLong(dt) + " " + Util.TimeFromLong(dt), // Дата
            N_VGN, // Номер вагона
            s,// Тип
            state,// коммерческое состояное
            view,// Осмотрен
                "РЖД", // Состояние
            axis
        });
    }
    
    private static String testEnd(int n){
        String OV = " вагон";
        String end;
        //0 - ов
        //1 - 
        //2 - а
        //3 - а
        //4 - а
        //5 - ов...
        switch (n % 10) {//возвращает остаток от деления первого операнда на второй. 
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
        }
        switch (n) {//возвращает остаток от деления первого операнда на второй. 
            case 11:
                end = "ов";
                break;
            case 12:
                end = "ов";
                break;
            case 13:
                end = "ов";
                break;
            case 14:
                end = "ов";
                break;
            default:
                end = "ов";
        }
        return OV + end;
    }
    
//    private static void Close() {
//        AltMenu.toggleEventer.setSelected(false);
//        while (DTM.getRowCount() > 0) {
//            DTM.removeRow(0);
//        }
//    }
    
    private static String gruz(String cod_gruz) {//==========// РодГрузаКод (Бензин, сжиженный газ, ...)
        String s = "-";
        if ("".equals(cod_gruz)) {
            s = "-";
        } else {
            int gruz = Integer.valueOf(cod_gruz);
            switch (gruz) {
                case 1:
                    s = "бензин";
                    break;
                case 2:
                    s = "сжиженный газ";
                    break;
            }
        }
        return s;
    }
    
    private static String oper(String cod_oper) {//==========// ТехнологическаяОперацияКод (Выгрузка, Погрузка)
        String s = "-";
        if ("".equals(cod_oper)) {
            s = "-";
        } else {
            int oper = Integer.valueOf(cod_oper);
            switch (oper) {
                case 1:
                    s = "Выгрузка";
                    break;
                case 2:
                    s = "Погрузка";
                    break;
            }
        }
        return s;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JScrollPane vgnTableScroller = new javax.swing.JScrollPane();
        vgnTable = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        pnlVgn = new javax.swing.JPanel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(true);
        setTitle("Список вагонов");
        setVisible(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing();
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        vgnTable.setModel(DTM);
        vgnTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        vgnTableScroller.setViewportView(vgnTable);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 16, Short.MAX_VALUE)
        );

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        pnlVgn.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlVgn.setAutoscrolls(true);

        javax.swing.GroupLayout pnlVgnLayout = new javax.swing.GroupLayout(pnlVgn);
        pnlVgn.setLayout(pnlVgnLayout);
        pnlVgnLayout.setHorizontalGroup(
            pnlVgnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlVgnLayout.setVerticalGroup(
            pnlVgnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(pnlVgn);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(vgnTableScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vgnTableScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing() {//GEN-FIRST:event_formInternalFrameClosing
//        Close();
        setVisible(false);
    }//GEN-LAST:event_formInternalFrameClosing

    private static javax.swing.JPanel pnlVgn;
    private javax.swing.JTable vgnTable;
    // End of variables declaration                   
}

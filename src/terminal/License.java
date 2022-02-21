//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
//нужно поставить координаты !!!
package terminal;

class License extends javax.swing.JInternalFrame {

    License() {
        initComponents();
        appTitleLabel.setFont(Terminal.SANS14);
        jTextArea1.setFont(Terminal.SANS18P);
        lblLogo.setIcon(Terminal.mainPictureHash.get("att_logo"));
        getRootPane().setDefaultButton(closeButton);
        setTitle("ЛИЦЕНЗИОННОЕ СОГЛАШЕНИЕ");
    }

    static void info() {
        jTextArea1.removeAll();
        String txt = "";
        jTextArea1.append(txt + "\n");
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        appTitleLabel = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        closeButton = new javax.swing.JButton();
        lblLogo = new javax.swing.JLabel();

        setClosable(true);
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        setVisible(true);

        appTitleLabel.setText("ВНИМАТЕЛЬНО ПРОЧТИТЕ ЛИЦЕНЗИОННОЕ СОГЛАШЕНИЕ"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("\tЛИЦЕНЗИОННОЕ СОГЛАШЕНИЕ\n\n   1. Исключительные права на программу для ЭВМ, включая документацию в электронном виде, (далее - Изделие) принадлежат ЗАО «НПЦ «АТТРАНС», далее - Правообладатель.\n   2. Настоящее соглашение является офертой ЗАО «НПЦ «АТТРАНС» к физическому или юридическому лицу, далее - Пользователь. \n   3. Пользователь в соответствии с настоящим соглашением получает право использовать Изделие на территории Российской Федерации.\n   4. Установка Изделия в память ЭВМ рассматривается как безусловное согласие Пользователя с условиями настоящего соглашения.\n   5. В случае несогласия с каким-либо из условий настоящего соглашения Пользователь не имеет права продолжать установку Изделия в память ЭВМ, а в случае установки Изделия в память ЭВМ обязан удалить Изделие из ЭВМ.\n   6. Пользователь имеет право использовать Изделие в некоммерческих целях и с целью ознакомления с Изделием и проверкой его работоспособности и функциональных характеристик в течение одного месяца с момента установки Изделия в память ЭВМ.\n   7. Пользователь имеет право использовать Изделие в соответствии с его назначением и правилами пользования, изложенными в эксплуатационной документации, которое включает право на установку, хранение и воспроизведение Изделия в память ЭВМ, ограниченное правом копирования и запуска.\n   8. Пользователь не вправе:\n\t- пытаться дизассемблировать, декомпилировать (преобразовывать объектный код в исходный текст) Изделие и его компоненты;\n\t- вносить какие-либо изменения в объектный код Изделия, за исключением тех, которые вносятся средствами, включенными в комплект Изделия и описанными в документации;\n\t- совершать относительно Изделия иные действия в нарушение норм законодательства об авторском праве и смежных правах.\n   9. Пользователь имеет право на получение технической поддержки по Изделию, а Правообладатель обязуется оказать Пользователю услуги по технической поддержке Изделия при наличии у Пользователя:\n\t- договора на оказание услуг по технической поддержке Программного продукта между Пользователем и Правообладателем в соответствии с условиями такого договора.\n   10. Настоящее соглашение распространяет свое действие на весь период использования Изделия. При прекращении использования Изделия Пользователь обязан удалить Изделие из памяти ЭВМ.\n   11. Нарушение условий настоящего соглашения является нарушением исключительных прав Правообладателя и преследуется по закону.");
        jTextArea1.setFocusable(false);
        jTextArea1.setRequestFocusEnabled(false);
        jScrollPane3.setViewportView(jTextArea1);

        closeButton.setText("Продолжить"); // NOI18N
        closeButton.setName("closeButton"); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogo.setName("lblLogo"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton)
                .addGap(209, 209, 209))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(appTitleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel appTitleLabel;
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane jScrollPane3;
    private static javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lblLogo;
    // End of variables declaration//GEN-END:variables
}

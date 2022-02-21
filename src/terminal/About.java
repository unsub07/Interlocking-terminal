//Copyright (c) 2011, 2021, ЗАО "НПЦ "АТТРАНС"
// о программе
//нужно поставить координаты !!!
package terminal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;
//import java.util.zip.CRC32C;
import java.util.zip.CRC32;

final class About extends javax.swing.JInternalFrame {

    private static String ver = "1.0.0";
    private final String aboutTitle = "О программе: АРМ Оператора МПЦ вер. ";

    About() {
        initComponents();
        lbl.setFont(Terminal.SANS14);
        lblLogo.setIcon(Terminal.mainPictureHash.get("att_logo"));
        setPreferredSize(new java.awt.Dimension(Terminal.ABOUT_WIDTH, Terminal.ABOUT_HEIGHT));
    }

    void info() {
        getRootPane().setDefaultButton(closeButton);
        Properties prop = new Properties();
        try {
            prop.load(About.class.getResourceAsStream("version"));
            ver = prop.getProperty("version");
        } catch (IOException e) {
            Err.err(e);
        }
        setTitle(aboutTitle + ver);
//        PLC c;
//        c = Terminal.PLCs_Hash.get(0);//в хеше CPU лежат 0,1,2...
//        String crc = c.crc;
        jTextArea1.setText("");
        jTextArea1.removeAll();

        String OSversion = System.getProperty("os.version");
        String userHome = System.getProperty("user.home");
        String userDir = System.getProperty("user.dir");
        String OSname = System.getProperty("os.name");// .toLowerCase().substring(0, // 3);
        String JAVAversion = System.getProperty("java.version");
        String JAVAvm = System.getProperty("java.vm.name");
        String OSarch = System.getProperty("os.arch");
        String JAVAvmvesrsion = System.getProperty("java.vm.version");
        String Encoding = System.getProperty("file.encoding");
        String userName = System.getProperty("user.name");// User's account name
        jTextArea1.append("Версия продукта: terminal " + ver + " (build: 20200730)" + "\n");
        jTextArea1.append("Поставщик: ЗАО \"НПЦ АТ Транс\", СофтИнжиниринг" + "\n");
        jTextArea1.append("--------------------System info---------------" + "\n");
        jTextArea1.append("System: " + OSname + " version " + OSversion + " on " + OSarch + "; " + Encoding + "\n");
        jTextArea1.append("User: " + userName + "; User dir:  " + userDir + "; Home dir: " + userHome + "\n");
        jTextArea1.append("Java: " + JAVAversion + "; " + JAVAvm + " " + JAVAvmvesrsion + "\n");
        jTextArea1.append("DB: PostgreSql" + "\n");
        jTextArea1.append(checksum(Terminal.ATTRANS_HOME + Terminal.FS + "terminal.jar") + "\n");
        jTextArea1.append("CRC summ programm loaded in PLC: " + PLC.crc + "\n");
    }

    private static String checksum(String filepath) {
        String ret = "";
        File f = new File(filepath);
        if (f.isFile() & f.canRead()) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                MessageDigest sha1 = MessageDigest.getInstance("SHA1");
                md5.reset();
                sha1.reset();
//                CRC32C crc = new CRC32C();
                CRC32 crc = new CRC32();

                byte[] bytes = new byte[4096];

                try (InputStream is = new BufferedInputStream(
                        new FileInputStream(f))) {

                    int c;// num Bytes
                    while ((c = is.read(bytes)) != -1) {
                        md5.update(bytes, 0, c);
                        sha1.update(bytes, 0, c);
                        crc.update(bytes, 0, c);
                    }
                } catch (IOException e) {
                    Err.err(e);
                }

                byte[] md5digest = md5.digest();
                byte[] sha1digest = sha1.digest();
                byte[] crcdisgest = longToByteArray(crc.getValue());

                ret = "MD5: " + Util.bytes2Hex(md5digest) + "\n" + "SHA1: "
                        + Util.bytes2Hex(sha1digest) + "\n" + "CRC32: "
                        + Util.bytes2Hex(crcdisgest) + "\n" + "Length: "
                        + f.length() + " bytes\n" + "Date: "
                        + Terminal.SDF3.format(new Date(f.lastModified()));

            } catch (NoSuchAlgorithmException e) {
                Err.err(e);
            }
        }
        return ret;
    }

    private static byte[] longToByteArray(long value) {
        return new byte[]{(byte) (value >> 56), (byte) (value >> 48),
            (byte) (value >> 40), (byte) (value >> 32),
            (byte) (value >> 24), (byte) (value >> 16),
            (byte) (value >> 8), (byte) value};
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        closeButton = new javax.swing.JButton();
        lblLogo = new javax.swing.JLabel();

        setClosable(true);
        setTitle("О программе: АРМ Оператора МПЦ вер. 1.0.0 "); // NOI18N
        setToolTipText("");
        setName("aboutBox"); // NOI18N
        setPreferredSize(new java.awt.Dimension(460, 420));
        setRequestFocusEnabled(false);
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        setVisible(true);

        lbl.setText("АРМ Оператора МПЦ"); // NOI18N
        lbl.setName("lbl"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setAutoscrolls(false);
        jTextArea1.setFocusable(false);
        jTextArea1.setName("jTextArea1"); // NOI18N
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
                .addComponent(lbl)
                .addGap(174, 174, 174))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton)
                .addGap(181, 181, 181))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl)
                .addGap(10, 10, 10)
                .addComponent(lblLogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
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
    protected javax.swing.JButton closeButton;
    private javax.swing.JScrollPane jScrollPane3;
    private static javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lblLogo;
    // End of variables declaration//GEN-END:variables
}

//Copyright (c) 2011, 2020, ЗАО "НПЦ "АТТРАНС"
//FUCK Victor сделать Terminal.DEVELOPMENT = 1 (TRUE) то в меню есть пользователь SIM
package terminal;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JPanel;

class LoginBox extends javax.swing.JDialog implements java.awt.event.MouseListener{

    private final ArrayList<String[]> hosts = new ArrayList<>(2);
    private int num = 0;
    private int i = 0;
    
// Individual keyboard rows
    private final String row_1[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "+", "fill", "BackSpace" };
    private final String row_2[] = { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\" };
    private final String row_3[] = { "A", "S", "D", "F", "G", "H", "J", "K", "L", ":", "\"", "fill", "fill", "Enter" };
    private final String row_4[] = { "Z",        "X",    "C",    "V",    "B",    "N",    "M", ",", ".",     "?", "blank", "^" };
    private final String row_5[] = { "blank", "blank", "fill", "fill", "fill", "fill", "fill", "fill",  " ", "blank",     "<", "v", ">" };

    // Jbuttons corresponding to each individual rows
    private JButton[] line_1;
    private JButton[] line_2;
    private JButton[] line_3;
    private JButton[] line_4;
    private JButton[] line_5;
    
    // <editor-fold defaultstate="collapsed" desc="LoginBox">
    @SuppressWarnings("unchecked")
    LoginBox(String HOST) {
        super((java.awt.Frame) null);
        setTitle("Регистрация в системе"); // NOI18N
        setModal(true);
        setName("loginBox"); // NOI18N
        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(Terminal.mainPictureHash.get("key").getImage());// установить иконку
        initComponents();
//------------
        cancelButton.setFont(Terminal.SANS18); // NOI18N
        cancelButton.setText("Выход"); // NOI18N
        okButton.setFont(Terminal.SANS18); // NOI18N
        okButton.setText("Продолжить"); // NOI18N
        
        cancelButton.addActionListener((java.awt.event.ActionEvent evt) -> cancelButtonActionPerformed());

        fldPassword.setFont(Terminal.SANS18); // NOI18N
        fldPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                fldPasswordFocusGained();
            }
        });
        fldPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                fldPasswordKeyPressed(evt);
            }
        });

        okButton.addActionListener((java.awt.event.ActionEvent evt) -> okButtonActionPerformed());
        okButton.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                okButtonKeyPressed();
            }
        });
//-------------        
        
        if (Terminal.debug){
        cbLoginName.setModel(new javax.swing.DefaultComboBoxModel <>(
                new String[]{"ДСП1", "ДСП2", "ДСП3", "ДСП4",
                    "ДСП5", "ДСП6", "ДСП7", "ДСП8", "ДСП9", "ДСП10",
                    "ДСП11", "ДСП12", "ДСП13", "ДСП14", "ДСП15", "ДСП16",
                    "ДСП17", "ДСП18",
                    "ДСП19", "ДСП20",
                    "ШНЦ", "ШНЦС",
                    "Гость",
                    "СИМ"
                }));
        cbLoginName.setSelectedIndex(cbLoginName.getItemCount()-1);
        fldPassword.setText("sim");
        } else {
            cbLoginName.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"ДСП1", "ДСП2", "ДСП3", "ДСП4",
                    "ДСП5", "ДСП6", "ДСП7", "ДСП8", "ДСП9", "ДСП10",
                    "ДСП11", "ДСП12", "ДСП13", "ДСП14", "ДСП15", "ДСП16",
                    "ДСП17", "ДСП18",
                    "ДСП19", "ДСП20",
                    "ШНЦ", "ШНЦС",
                    "Гость"
                }));
        }

        lblLogo.setIcon(Terminal.mainPictureHash.get("att_logo"));
        okButton.setIcon(Terminal.mainPictureHash.get("102"));
        cancelButton.setIcon(Terminal.mainPictureHash.get("101"));
        serverLabel.setVisible(false);
        cbServerName.setVisible(false);
        if (HOST == null) {// if host = null doparse
            DoParse();// разберем файлик hosts в КомбоБокс
        }

//        int x = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();// 5000
//        int y = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();// java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;//1400
//        int x2 = x / 2;// серединка
//        int y2 = y / 2;//
//
//        // перед setLocation надо знать куда мы подключаемся в парк А или парк Б
//        if (Terminal.left_mobitor) {
//            setLocation(x2 / 2 - (getPreferredSize().width / 2) - 100, y2 / 2 + (getPreferredSize().height / 2));
//        } else if (x > 2_560) {
//            setLocation(x2 + x2 / 2 - (getPreferredSize().width / 2), y2 / 2 + (getPreferredSize().height / 2));
//        } else {
//            setLocation(x2 / 2 - (getPreferredSize().width / 2) + 100, y2 / 2 + (getPreferredSize().height / 2));
//        }
        setAlwaysOnTop(true);// всегда на переднем плане
//------------------------------------------------------------------------------
            Kbd();
    if (Terminal.kbd) {
            toglbtnKbd.setVisible(true);
//            Kbd();
        } else {
            toglbtnKbd.setVisible(false);
        }
        contentPane.setVisible(false);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="DoParse">
    private void DoParse() {
        String s = Terminal.ATTRANS_HOME + Terminal.FS + "config" + Terminal.FS + "hosts";
        if (Terminal.test_file(s)) {
            File f = new File(s);// resource.att - FUFUFU FFF Victor надо проверить ResourceFile = что он не равен "" или null и что  он существует!!!

            FileInputStream fis = null;
            BufferedReader reader = null; // reading file line by line in Java using BufferedReader

            try {
                fis = new FileInputStream(f);
                reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
                String object = "";
                String line = "";// = reader.readLine();
                while (line != null) {// с 0 строки по конец файла. (цикл по файлу)
                    line = reader.readLine();
                    if (line != null) {
                        if ((line.isEmpty()) || (!line.startsWith("#"))) {
                            if (line.startsWith("[")) {
                                object = Util.getObject(line).trim();
                                // System.out.println("object " + object);
                                cbServerName.addItem(object);
                                num += 1;
                            } else if (!"".equals(line) || !"".equals(object)) {
                                String[] tokens = {"", "", ""};// new String[3];

                                String[] spl = line.split("\\t");// (сплит таб)
                                tokens[0] = object;
                                if (spl.length > 0) {
                                    tokens[1] = spl[0];// тут надо if spl.length > 0
                                    Net.hosts.add(tokens[1]);
                                }
                                if (spl.length > 1) {
                                    tokens[2] = spl[1];// тут надо if spl.length > 1
                                    Net.hosts.add(tokens[2]);
                                }
                                hosts.add(tokens);
                            }
                        }
                    }// if
                }// while
            } catch (IOException e) {
                Err.err(e);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException e) {
                    Err.err(e);
                }
            }

            if (num > 1) {
                serverLabel.setVisible(true);
                cbServerName.setVisible(true);
            }
        } else {
            Log.log("not found resource file 2 = hosts");
            System.exit(1);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ok">
    private int ok(String pwd) {
        i++;
        int ret;
        String usr = "GUEST";
        switch ((String) Objects.requireNonNull(cbLoginName.getSelectedItem())) {
            case "ДСП1":
                usr = "DSP1";
                break;
            case "ДСП2":
                usr = "DSP2";
                break;
            case "ДСП3":
                usr = "DSP3";
                break;
            case "ДСП4":
                usr = "DSP4";
                break;
            case "ДСП5":
                usr = "DSP5";
                break;
            case "ДСП6":
                usr = "DSP6";
                break;
            case "ДСП7":
                usr = "DSP7";
                break;
            case "ДСП8":
                usr = "DSP8";
                break;
            case "ДСП9":
                usr = "DSP9";
                break;
            case "ДСП10":
                usr = "DSP10";
                break;
            case "ДСП11":
                usr = "DSP11";
                break;
            case "ДСП12":
                usr = "DSP12";
                break;
            case "ДСП13":
                usr = "DSP13";
                break;
            case "ДСП14":
                usr = "DSP14";
                break;
            case "ДСП15":
                usr = "DSP15";
                break;
            case "ДСП16":
                usr = "DSP16";
                break;
            case "ДСП17":
                usr = "DSP17";
                break;
            case "ДСП18":
                usr = "DSP18";
                break;
            case "ДСП19":
                usr = "DSP19";
                break;
            case "ДСП20":
                usr = "DSP20";
                break;                
            case "ШНЦ":
                usr = "SHNC";
                break;
            case "ШНЦС":
                usr = "SHNCS";
                break;
            case "СИМ":
                usr = "SIM";
                break;
            case "Гость":
                usr = "GUEST";
                fldPassword.setText("guest");
                Terminal.noGuest = false;
                break;
        }

        if ("".equals(pwd)) {
            Terminal.noGuest = false;
            usr = "guest";
            pwd = "guest";
        }
        // -----------------------------------------------------------------

        if (Terminal.host == null) {
            String g = (String) cbServerName.getSelectedItem();

//            Net.hosts.isEmpty();
            //noinspection CollectionAddedToSelf
//            Net.hosts.removeAll(Net.hosts);
            Net.hosts.clear();
            Net.hostsIt = null;

            hosts.stream().filter((hh) -> (!hosts.isEmpty())).filter((hh) -> (hh[0] == null ? g == null : hh[0].equals(g))).peek((hh) -> Log.log("h0 " + hh[0] + " h1 " + hh[1] + " h2 " + hh[2])).forEach((hh) -> {
                Net.hosts.add(hh[1]);// тут надо в NET передать список IP по которым коннектиться. (и описания которые надо писать в коммандер титле)
            });
        }//end host==null
        // -----------------------------------------------------------------
        Terminal.LOGNAME = usr.toUpperCase();
        Terminal.PASSWORD = Util.hash256(pwd);
//=======================CONNECT================
        String result;
        ret = Net.Connect();
        switch (ret) {
            case 0://ok
                result = "Соединение: соединение с сервером установлено";
                break;
            case 7://
                result = "<html><font color=red><b>Соединение: не удалось установить соединение с сервером (возможно он выключен)</b></font></html>";
                break;
            case 8://
                result = "<html><font color=red><b>Соединение: не запущен коммуникационник </b></font></html>";
                break;
            default:
                result = "<html><font color=red><b>Соединение: ошибка</b></font></html>";
                break;
        }
//=======================LOGIN===================
        if (ret == 0) {
            ret = Net.Login(Terminal.LOGNAME, Terminal.PASSWORD);
            switch (ret) {
                case 0://ok
                    result = "Вход: успешный вход в систему";
                    break;
                case 1://неверный пароль
                    result = "<html><font color=red><b>Вход: неверный пароль</b></font></html>";
                    if (Net.Connection_to_CommServer) {
                        Log.log(result + "Net.Disconnect 1");
                        Net.Disconnect();
                    }
                    break;
                case 2://Доступ с недопустимого IP адреса
                    result = "<html><font color=red><b>Вход: Доступ с недопустимого IP адреса</b></font></html>";
                    if (Net.Connection_to_CommServer) {
                        Log.log(result + "Net.Disconnect 2");
                        Net.Disconnect();
                    }
                    break;
                case 3://В системе уже есть диспетчер
                    result = "<html><font color=red><b>Вход: В систему уже есть DSP</b></font></html>";
                    if (Net.Connection_to_CommServer) {
                        Log.log(result + "Net.Disconnect 3");
                        Net.Disconnect();
                    }
                    break;
                case 4://Нет такого пользователя
                    result = "<html><font color=red><b>Вход: Нет такого пользователя</b></font></html>";
                    if (Net.Connection_to_CommServer) {
                        Log.log(result + "Net.Disconnect 4");
                        Net.Disconnect();
                    }
                    break;
                case 5://
                    result = "<html><font color=red><b>Вход: Ошибка подключения к базе данных</b></font></html>";
                    if (Net.Connection_to_CommServer) {
                        Log.log(result + "Net.Disconnect 5");
                        Net.Disconnect();
                    }
                    break;
                case 6://
                    result = "<html><font color=red><b>Вход: Ошибка</b></font></html>";
                    if (Net.Connection_to_CommServer) {
                        Log.log(result + "Net.Disconnect 6");
                        Net.Disconnect();
                    }
                    break;
                case 7://
                    result = "<html><font color=red><b>Вход: не удалось установить соединение с сервером (возможно он выключен)</b></font></html>";
                    if (Net.Connection_to_CommServer) {
                        Log.log(result + "Net.Disconnect 7");
                        Net.Disconnect();
                    }
                    break;
                case 8://
                    result = "<html><font color=red><b>Вход: не запущен коммуникационник</b></font></html>";
                    if (Net.Connection_to_CommServer) {
                        Log.log(result + "Net.Disconnect 8");
                        Net.Disconnect();
                    }
                    break;
                default:
                    result = "<html><font color=red><b>Вход: ошибка</b></font></html>";
                    if (Net.Connection_to_CommServer) {
                        Log.log(result + "Net.Disconnect default");
                        Net.Disconnect();
                    }
                    break;
            } 
        }
        lbl_err.setText(result);
        Log.log(i+ " ok "+ ret);
        return ret;
    }
    // </editor-fold>

    private void Exit() {
        if (Net.Connection_to_CommServer) {
            Log.log("LoginBox Exit");
            Net.Disconnect();
        }
        System.exit(0);
    }

    @SuppressWarnings("uncheked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JLabel loginLabel = new javax.swing.JLabel();
        javax.swing.JLabel passwordLabel = new javax.swing.JLabel();
        javax.swing.JLabel frmDescLabel = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        fldPassword = new javax.swing.JPasswordField();
        okButton = new javax.swing.JButton();
        cbLoginName = new javax.swing.JComboBox();
        serverLabel = new javax.swing.JLabel();
        cbServerName = new javax.swing.JComboBox();
        lblLogo = new javax.swing.JLabel();
        lbl_err = new javax.swing.JLabel();
        toglbtnKbd = new javax.swing.JToggleButton();
        contentPane = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(800, 360));
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        loginLabel.setFont(Terminal.SANS18);
        loginLabel.setText("Имя:"); // NOI18N
        loginLabel.setFocusable(false);

        passwordLabel.setFont(Terminal.SANS18);
        passwordLabel.setText("Пароль:"); // NOI18N
        passwordLabel.setFocusable(false);

        frmDescLabel.setFont(Terminal.SANS18);
        frmDescLabel.setText("Регистрация в системе"); // NOI18N
        frmDescLabel.setFocusable(false);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("terminal/Bundle"); // NOI18N
        cancelButton.setText(bundle.getString("LoginBox.cancelButton.text")); // NOI18N

        fldPassword.setFont(Terminal.SANS18);
        fldPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        okButton.setText(bundle.getString("LoginBox.okButton.text")); // NOI18N
        okButton.setToolTipText(bundle.getString("LoginBox.okButton.toolTipText")); // NOI18N

        cbLoginName.setFont(Terminal.SANS18);

        serverLabel.setFont(Terminal.SANS18);
        serverLabel.setText("Сервер");
        serverLabel.setFocusable(false);

        cbServerName.setFont(Terminal.SANS18);
        cbServerName.setName("cbServerName"); // NOI18N

        lblLogo.setFocusable(false);
        lblLogo.setName("lblLogo"); // NOI18N

        lbl_err.setFont(Terminal.SANS18);
        lbl_err.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_err.setName("lbl_err"); // NOI18N

        toglbtnKbd.setFont(Terminal.SANS18);
        toglbtnKbd.setText(bundle.getString("LoginBox.toglbtnKbd.text")); // NOI18N
        toglbtnKbd.setName("toglbtnKbd"); // NOI18N
        toglbtnKbd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toglbtnKbdActionPerformed(evt);
            }
        });

        contentPane.setFocusable(false);
        contentPane.setName("contentPane"); // NOI18N
        contentPane.setOpaque(false);
        contentPane.setRequestFocusEnabled(false);
        contentPane.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(contentPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(frmDescLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(loginLabel)
                                            .addComponent(passwordLabel)
                                            .addComponent(serverLabel))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cbServerName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(fldPassword)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(toglbtnKbd, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(cbLoginName, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(okButton)
                                .addGap(25, 25, 25)
                                .addComponent(cancelButton))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(lbl_err, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(217, 217, 217)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(frmDescLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbLoginName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(loginLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(passwordLabel)
                            .addComponent(toglbtnKbd, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(serverLabel)
                            .addComponent(cbServerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(okButton)
                            .addComponent(cancelButton))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentPane, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_err, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cancelButton.getAccessibleContext().setAccessibleName(bundle.getString("LoginBox.cancelButton.AccessibleContext.accessibleName")); // NOI18N
        okButton.getAccessibleContext().setAccessibleName(bundle.getString("LoginBox.okButton.AccessibleContext.accessibleName")); // NOI18N
        okButton.getAccessibleContext().setAccessibleDescription(bundle.getString("LoginBox.okButton.AccessibleContext.accessibleDescription")); // NOI18N
        toglbtnKbd.getAccessibleContext().setAccessibleName(bundle.getString("LoginBox.toglbtnKbd.AccessibleContext.accessibleName")); // NOI18N

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Exit();
        }
    }//GEN-LAST:event_formKeyPressed

    private void toglbtnKbdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toglbtnKbdActionPerformed
        if(toglbtnKbd.isSelected()){
            contentPane.setVisible(true);
            fldPassword.requestFocus();
        } else {
            contentPane.setVisible(false);
        }
    }//GEN-LAST:event_toglbtnKbdActionPerformed

    private void cancelButtonActionPerformed() {
        Exit();
    }

    private void fldPasswordFocusGained() {
        fldPassword.selectAll();
    }

    private void okButtonActionPerformed() {
        go();
    }

    private void fldPasswordKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            okButton.requestFocusInWindow();
        }
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Exit();
        }
    }

    private void okButtonKeyPressed() {
        go();
    }
    
    private void go(){
        int ret = ok(String.copyValueOf(fldPassword.getPassword()));
        if (ret == 0) {// FUCK чтобы заходило
            dispose();
            Log.log("dispose");
        } else {
        if (Net.Connection_to_CommServer) {
            Net.Disconnect();
        }
            fldPassword.requestFocusInWindow();
        }
    }
    
    private void Kbd(){
        // Various panel for the layout
        JPanel jpNorth = new JPanel();
        JPanel jpCenter = new JPanel();
        jpCenter.setPreferredSize(new Dimension(10, 10));
        JPanel jpKeyboard = new JPanel(new GridBagLayout());
        JPanel jpNote = new JPanel();

        contentPane.add(jpNorth, BorderLayout.NORTH);
        contentPane.add(jpNote);

        contentPane.add(jpCenter, BorderLayout.CENTER);
        contentPane.add(jpKeyboard, BorderLayout.SOUTH);

        jpNorth.setLayout(new BorderLayout());
        jpCenter.setLayout(new BorderLayout());
        jpCenter.setPreferredSize(new Dimension(10, 10));

        line_1 = new JButton[row_1.length];
        line_2 = new JButton[row_2.length];
        line_3 = new JButton[row_3.length];
        line_4 = new JButton[row_4.length];
        line_5 = new JButton[row_5.length];

        addKeys(jpKeyboard, 0, row_1, line_1);
        addKeys(jpKeyboard, 1, row_2, line_2);
        addKeys(jpKeyboard, 2, row_3, line_3);
        addKeys(jpKeyboard, 3, row_4, line_4);
        addKeys(jpKeyboard, 4, row_5, line_5);

        jpKeyboard.setPreferredSize(new Dimension(640, 160));

        jpNote.setOpaque(false);
        jpNorth.setOpaque(false);
        jpCenter.setOpaque(false);
        jpKeyboard.setOpaque(false);

        // add listeners to all the button
        for(JButton b : line_1)  { if (b != null) { b.addMouseListener(this); } }
        
        for(JButton b : line_2) { if (b != null) { b.addMouseListener(this); } }
        
        for(JButton b : line_3)  { if (b != null) { b.addMouseListener(this); } }
         
        for(JButton b : line_4) { if (b != null) { b.addMouseListener(this); } }
         
        for(JButton b : line_5)  { if (b != null) { b.addMouseListener(this); } }
    }
    
    @Override
    public void mouseClicked(MouseEvent arg0) {
        Object o = arg0.getSource();
        JButton b = null;
        if (o instanceof JButton) {
            b = (JButton) o;
        }
        if (b != null) {
            Log.log(b.getText());
            keyPress(b.getText());
            java.awt.Toolkit.getDefaultToolkit().beep();
//            System.out.println("\007");
        }
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }    
    
    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    private void addKeys(JPanel parent, int row, String[] keys, JButton[] buttons) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;

        int gap = 0;
        for (int index = 0; index < keys.length; index++) {
            String key = keys[index];
            if ("blank".equalsIgnoreCase(key)) {
                gbc.gridx++;
            } else if ("fill".equalsIgnoreCase(key)) {
                gbc.gridwidth++;
                gap++;
            } else {
                // System.out.println("Add " + key);
                JButton btn = new JButton(key);
                buttons[index] = btn;
                parent.add(btn, gbc);
                gbc.gridx += gap + 1;
                gbc.gridwidth = 1;
                gap = 0;

//                System.out.println(key);
                btn.setBackground(Color.WHITE);
                btn.setFocusable(false);
//                map.put(getKeyCode(key), btn);
            }
        }
    }
    
    private void keyPress(String s) {
        switch (s) {// int
            case "Enter":
                contentPane.setVisible(false);
                toglbtnKbd.setSelected(false);
                okButton.requestFocus();
                break;
            case "BackSpace":
//                String str = fldPassword.getText();
                String str = new String(fldPassword.getPassword());
                if (str.length() > 0){
                    str = str.substring(0, str.length()-1);
                }
                fldPassword.setText(str);
                break;
            default:
                try {
                    Robot robot = new Robot();

                    // Simulate a key press
                    robot.keyPress(s.charAt(0));
                    robot.keyRelease(s.charAt(0));

                } catch (AWTException e) {
                    Err.err(e);
                }
            }
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox cbLoginName;
    private javax.swing.JComboBox cbServerName;
    private javax.swing.JPanel contentPane;
    private javax.swing.JPasswordField fldPassword;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lbl_err;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel serverLabel;
    private javax.swing.JToggleButton toglbtnKbd;
    // End of variables declaration//GEN-END:variables
}

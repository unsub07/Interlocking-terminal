//Copyright (c) 2011, 2020, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static terminal.Commander.cmdLayers;

class Player extends javax.swing.JInternalFrame {
    /**
     * this flag indicates whether the playback completes or not.
     */
    private boolean playCompleted;

    /**
     * this flag indicates whether the playback is stopped or not.
     */
    //for button
    private boolean isStopped;
    private boolean isPaused;
    private boolean isPlaying = false;
    //for thread
    private boolean isPause = false;
    private boolean isRunning = false;
    private boolean isReset = false;
//    private int btnRev; // 1,  2,  4,  8,  16, max= 255
//    private int btnFF;  //-1, -2, -4, -8, -16, max=-255
    private int last_line_num = 0;
    private int currentline = 0;
    private long start_long;
    private long stop_long;
    private long razn_long;//4 progress
    private String start_string;
    private String stop_string;
    private int speed = 0;//1 = play, 0 = pause, начало и конец = стоп = 128
    private final String Title = "Player";
    private static final java.text.SimpleDateFormat SDF = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String sf = "/opt/attrans/backup/2020-02-25_18-15-01.28";
    private static File f;
    private static java.util.HashMap<Integer, Play_hash> Log_play = new java.util.HashMap<>(1);

    private static final javax.swing.ImageIcon player_ff_g = Terminal.mainPictureHash.get("player_ff_g");
    private static final javax.swing.ImageIcon player_ff = Terminal.mainPictureHash.get("player_ff");
    private static final javax.swing.ImageIcon player_next_g = Terminal.mainPictureHash.get("player_next_g");
    private static final javax.swing.ImageIcon player_next = Terminal.mainPictureHash.get("player_next");
    private static final javax.swing.ImageIcon player_open_g = Terminal.mainPictureHash.get("player_open_g");
    private static final javax.swing.ImageIcon player_open = Terminal.mainPictureHash.get("player_open");
    private static final javax.swing.ImageIcon player_pause_g = Terminal.mainPictureHash.get("player_pause_g");
    private static final javax.swing.ImageIcon player_pause = Terminal.mainPictureHash.get("player_ff_g");
    private static final javax.swing.ImageIcon iconPlay_sel = Terminal.mainPictureHash.get("player_play_g");
    private static final javax.swing.ImageIcon iconPlay = Terminal.mainPictureHash.get("player_play");
    private static final javax.swing.ImageIcon player_prev_g = Terminal.mainPictureHash.get("player_prev_g");
    private static final javax.swing.ImageIcon player_prev = Terminal.mainPictureHash.get("player_prev");
    private static final javax.swing.ImageIcon player_rev_g = Terminal.mainPictureHash.get("player_rev_g");
    private static final javax.swing.ImageIcon player_rev = Terminal.mainPictureHash.get("player_rev");
    private static final javax.swing.ImageIcon iconStop_sel = Terminal.mainPictureHash.get("player_stop_g");
    private static final javax.swing.ImageIcon iconStop = Terminal.mainPictureHash.get("player_stop");

    Player() {
        initComponents();
        cmdLayers.setBackground(new java.awt.Color(175, 222, 175));//green
        setTitul(Title);
        
        jSlider1.setMinimum(0);
        jSlider1.setMaximum(last_line_num);


        btnOpen.setIcon(player_open);
        btnOpen.setSelectedIcon(player_open_g);

        btnPlay.setIcon(iconStop);
        btnPlay.setSelectedIcon(iconPlay_sel);
        btnPlay.setSelected(false);
        btnPlay.setEnabled(false);
        btnPlay.setToolTipText("Проирывать");
        
        toglFF.setIcon(player_ff);
        toglFF.setIcon(player_ff_g);
        toglFF.setSelected(false);
        toglFF.setEnabled(false);
        toglFF.setToolTipText("Вперед");
        
        toglRev.setIcon(player_rev);
        toglRev.setIcon(player_rev_g);
        toglRev.setSelected(false);
        toglRev.setEnabled(false);
        toglRev.setToolTipText("Назад");
    }

    private void setTitul(String s){
        setTitle(s);
    }

    private void set_speed(long tek, long next){
        
        setTitul(Title + " x" + speed);
    }

    private void pause(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Err.err(e);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnOpen = new javax.swing.JButton();
        toglRev = new javax.swing.JToggleButton();
        toglFF = new javax.swing.JToggleButton();
        lblStart = new javax.swing.JLabel();
        lblStop = new javax.swing.JLabel();
        btnPlay = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(true);
        setTitle("Player");
        setVisible(true);

        btnOpen.setToolTipText("Открыть");
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });

        toglRev.setText("Rev");
        toglRev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toglRevActionPerformed(evt);
            }
        });

        toglFF.setText("FF");
        toglFF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toglFFActionPerformed(evt);
            }
        });

        lblStart.setText("0000-00-00 00:00:00");

        lblStop.setText("0000-00-00 00:00:00");

        btnPlay.setText("Play");
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnOpen)
                .addGap(153, 153, 153)
                .addComponent(toglRev)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPlay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toglFF)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblStart)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblStop)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStart)
                    .addComponent(lblStop))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOpen)
                    .addComponent(toglRev)
                    .addComponent(toglFF)
                    .addComponent(btnPlay))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void toglRevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toglRevActionPerformed
        if (toglRev.isSelected()){
            toglRev.setSelected(true);
            toglFF.setSelected(false);
            btnPlay.setSelected(false);
        } else {
            toglRev.setSelected(false);
        }
        
    }//GEN-LAST:event_toglRevActionPerformed

    private void toglFFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toglFFActionPerformed
        if (toglFF.isSelected()){
            toglFF.setSelected(true);
            toglRev.setSelected(false);
            btnPlay.setSelected(false);
        } else {
            toglRev.setSelected(false);
        }
    }//GEN-LAST:event_toglFFActionPerformed

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        Log_play.clear();
        getFirstTime(sf);
        System.out.println("start_long " + start_long);
        System.out.println("stop_long " + stop_long);
        System.out.println("razn_long " + razn_long);
        System.out.println("start_string " + start_string);
        System.out.println("stop_string " + stop_string);
        System.out.println("Lines_in_file " + last_line_num);
        btnPlay.setEnabled(true);
        toglFF.setEnabled(true);
        toglRev.setEnabled(true);
        playbackThread();
    }//GEN-LAST:event_btnOpenActionPerformed

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        if (speed == 1 ){
            speed = 0;
            isPlaying = false;
            isPaused = true;
        } else {
            speed = 1;
            isPlaying = true;
            isPaused = false;
        }
        
        setTitul(Title + " x" + speed);
            //set icon_FF   = green
            //set icon_Play = black
            //set icon_Rev  = black
    }//GEN-LAST:event_btnPlayActionPerformed

    private void getFirstTime(String sf) {
        long tm = System.nanoTime();
        f = new File(sf);
        String first_line = null;
        String last_line = null;
        String[] s0;
        int i = 0;
//        long dt;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            first_line = br.readLine();
            while ((line = br.readLine()) != null) {
                Play_hash c = new Play_hash(line);
                Log_play.put(i, c);
                last_line = line;
                i++;
            }
            last_line_num = i;
        } catch (FileNotFoundException e) {
            Err.err(e);
        } catch (IOException e) {
            Err.err(e);
        }

        tm = System.nanoTime() - tm;
        System.out.println("Time first - last line: " + tm / 1000000000.0 + " сек.");

        s0 = first_line.split("\\ ");
        start_long = StringToLong(s0[0]);
        start_string = DateFromLong(start_long);

        s0 = last_line.split("\\ ");
        stop_long = StringToLong(s0[0]);
        stop_string = DateFromLong(stop_long);
        razn_long = stop_long - start_long;
        lblStart.setText(start_string);
        lblStop.setText(stop_string);
    }

    static String DateFromLong(long dt) {
        return SDF.format(new java.util.Date(dt));
    }

    static long StringToLong(String s) {
        long l;
        try {
            l = Long.valueOf(s);
        } catch (NumberFormatException e) {
            l = 0;
        }
        return l;
    }

    private void playbackThread() {
        try {
            class back_run extends Thread {

                back_run() {
                }

                @Override
                public void run() {
                    try {
                        go();
                    } catch (Exception e) {
                        Err.err(e);
                    }
                }
            }
            Thread Player = new back_run();
            Player.start();
        } catch (Exception e) {
            Err.err(e);
        }
    }
    
    private void go(){
        if (currentline == 0){

            jSlider1.setValue(currentline);
            currentline ++;
        }
        Play_hash c = Log_play.get(currentline);
        pause(25);
        testEnd();
        System.out.println("currentline: " + currentline);
    }
    
    private void testEnd(){
        if (currentline == 0){
            isStopped = true;
            playCompleted = true;
        } else {
            if (currentline == last_line_num){
                isStopped = true;
                playCompleted = true;
            }
        }
    }

    /**
     * Start playing back the sound.
     */
    private void playBack() {
        isPlaying = true;
    }
    
    private void play(){
        if (!isPlaying) {
                    playBack();
                } else {
                    stopPlaying();//pause
                }
        if (!isPause) {
                    pausePlaying();
                } else {
                    resumePlaying();
                }
    }
 
    private void stop() {//Stop playing back.
        isStopped = true;
    }

    private void pause() {
        isPaused = true;
    }

    private void resume() {
        isPaused = false;
    }
    
    private void reset() {
        isReset = true;
        isRunning = false;
    }

    private void pauseTimer() {
        isPause = true;
    }

    private void resumeTimer() {
        isPause = false;
    }
    
    private void stopPlaying() {
        isPause = false;
        btnPlay.setText("Pause");
        btnPlay.setEnabled(false);
        reset();
//        timer.interrupt();
        stop();
//        playbackThread.interrupt();
    }

    private void pausePlaying() {
        btnPlay.setText("Resume");
        isPause = true;
        pause();
        pauseTimer();
//        playbackThread.interrupt();
    }

    private void resumePlaying() {
        btnPlay.setText("Pause");
        isPause = false;
        resume();
        resumeTimer();
//        playbackThread.interrupt();
    }

    private void resetControls() {
        reset();
//        timer.interrupt();

        btnPlay.setText("Play");
        btnPlay.setIcon(iconPlay);

        isPlaying = false;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOpen;
    private javax.swing.JButton btnPlay;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JLabel lblStart;
    private javax.swing.JLabel lblStop;
    private javax.swing.JToggleButton toglFF;
    private javax.swing.JToggleButton toglRev;
    // End of variables declaration//GEN-END:variables
}

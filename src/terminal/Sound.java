//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
/*
 You could use following thread starting code, to play sounds by this class:
 new Sound("test.wav").start();
 */
package terminal;

import static terminal.Terminal.mainAudioHash;

class Sound extends Thread {

    private final int id_sound;

    Sound(int ID_SOUND) {
        id_sound = ID_SOUND;
    }

    @Override
    public void run() {
        if (Exit.isEXIT()){
            Log.log("Sound Thread return " + Exit.isEXIT());
            return;
        }
        if (Terminal.withoutsound) {// если в коммандной строке при запуске
            // написано -s или --withoutsound то
            // музыку не будем играть.
//            Log.log("Sound disabled on start");
            return;
        }
        if (!mainAudioHash.containsKey(id_sound)) {
            Log.log("Wave file not found: " + id_sound);
            return;
        }
        java.io.InputStream is;
        is = new java.io.ByteArrayInputStream(mainAudioHash.get(id_sound));// "alarm"

        javax.sound.sampled.AudioInputStream alarmSound;
        try {
            alarmSound = javax.sound.sampled.AudioSystem.getAudioInputStream(is);
        } catch (javax.sound.sampled.UnsupportedAudioFileException | java.io.IOException e) {
            Err.err(e);
            return;
        }

        javax.sound.sampled.AudioFormat format = alarmSound.getFormat();
        javax.sound.sampled.SourceDataLine line;
        javax.sound.sampled.DataLine.Info info = new javax.sound.sampled.DataLine.Info(javax.sound.sampled.SourceDataLine.class, format);

        try {
            line = (javax.sound.sampled.SourceDataLine) javax.sound.sampled.AudioSystem.getLine(info);
            line.open(format);
        } catch (javax.sound.sampled.LineUnavailableException | IllegalArgumentException e) {
            return;
        }
//        Log.log("pay " + id_sound);
        line.start();
        int nBytesRead = 0;
        byte[] abData = new byte[mainAudioHash.get(id_sound).length];

        try {
            while (nBytesRead != -1) {
                nBytesRead = alarmSound.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                    line.write(abData, 0, nBytesRead);
                }
            }
        } catch (java.io.IOException e) {
            Err.err(e);
        } finally {
            line.drain();
            line.close();
        }
    }
}

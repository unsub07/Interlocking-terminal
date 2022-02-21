//Copyright (c) 2011, 2018, ЗАО "НПЦ "АТТРАНС"
package terminal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Collections;
import java.util.zip.ZipInputStream;

class Util {

    static void getRes(final String fp) {
        String fs = Terminal.FS;
        String f = Terminal.ATTRANS_HOME + fs + "lib" + fs + fp;
        byte[] buffer = new byte[4096];

        InputStream middle = null;
        try {
            middle = new FileInputStream(f);
        } catch (java.io.FileNotFoundException ex) {
            Log.log("file resourece not found");
        }

        java.util.List<InputStream> streams = java.util.Arrays.asList(
                new ByteArrayInputStream("PK".getBytes()),
                middle);
        InputStream story = new SequenceInputStream(Collections.enumeration(streams));

//        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(f))) {
        try ( ZipInputStream zis = new ZipInputStream(story)) {
            // написать что нету такого файла.
            java.util.zip.ZipEntry ze;
            // ZipEntry ze = zis.getNextEntry();//get the zipped file list entry
            while ((ze = zis.getNextEntry()) != null) {
                String fileName = ze.getName();
                String hashString = fileName.split("\\.")[0];// fileName;
                String fileType = fileName.split("\\.")[1];// fileType

                ByteArrayOutputStream out = new ByteArrayOutputStream();

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                out.close();
                byte[] Data = out.toByteArray();// if f.length > 0

                if (fileType.equals("png") || fileType.equals("gif")) {
                    javax.swing.ImageIcon loadedImage = new javax.swing.ImageIcon(Data);
                    Terminal.mainPictureHash.put(hashString, loadedImage);
                }
                if (fileType.equals("wav")) {
                    Terminal.mainAudioHash.put(Integer.valueOf(hashString), Data);
                }
                // ze = zis.getNextEntry();
            }// end while
            zis.closeEntry();
        } catch (java.io.IOException e) {
            Err.err(e);
        }
    }

    static boolean GetUserGroups(String ROLE) {
        boolean ret = false;
        if (Terminal.USR_GROUPS != null) {
            for (String s : Terminal.USR_GROUPS) {
                if (ROLE.equals(s)) {
                    ret = true;
                }
            }
        }
        return ret;
    }

    static String DateFromLong(long dt) {
        return Terminal.SDF2.format(new java.util.Date(dt));
    }

    static String TimeFromLong(long dt) {
        return Terminal.SDF1.format(new java.util.Date(dt));
    }

    static String objType(int ID_OBJ) {//from id_obj get obj_type_name
        String obj_type = "";

        if (Terminal.obj_type.containsKey(ID_OBJ)) {//from id_obj get obj_type
            int type = Terminal.obj_type.get(ID_OBJ);
            if (Terminal.obj_type_name.containsKey(type)) {
                obj_type = Terminal.obj_type_name.get(type);
            } else {
                Log.log("Not found obj_type_name of obj_type " + type);//28-DIRECT
            }
        }
        return obj_type;
    }

    static String getObject(String line) {
        String object = "";
        if ((line.startsWith("["))) {
            object = line.substring(1, line.length() - 1);
        }
        return object;
    }

    static boolean isClass() {
        try {
            Class.forName("terminal.Sim");

//            Constructor c = Class.forName(className).getConstructors();
//            c.newInstance(id, newVector);
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

    static String hash256(String password) {
        java.security.MessageDigest md = null;
        try {
            md = java.security.MessageDigest.getInstance("SHA-256");
        } catch (java.security.NoSuchAlgorithmException e) {
            Err.err(e);
        }
        if (md == null) {
            return null;
        }
        md.update(password.getBytes());
        return Util.bytes2Hex(md.digest());
    }

    static String bytes2Hex(byte[] rawData) {
        StringBuilder result = new StringBuilder();
        for (byte b : rawData) {
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    // <editor-fold defaultstate="collapsed" desc="prep_lbl">
    static void prep_lbl(Img lbl, int LAYER, String ico, int cmdX, int cmdY) {
        if (Terminal.mainPictureHash.containsKey(ico)) {//если есть такая картинка
            lbl.setIcon(Terminal.mainPictureHash.get(ico));
            int x = lbl.getIcon().getIconWidth();//если нет картинки то - NullPointer
            int y = lbl.getIcon().getIconHeight();
//            int x = lbl.getIconWidth();//если нет картинки то - NullPointer
//            int y = lbl.getIconHeight();
            lbl.setBounds(cmdX, cmdY, x, y);
            lbl.setVisible(false);
            lbl.setFocusable(false);
            lbl.setOpaque(false);
            terminal.Commander.cmdLayers.add(lbl);
            terminal.Commander.cmdLayers.setLayer(lbl, LAYER);
        } else {
            Log.log(" in lib picture does not exist : " + ico);
        }
    }
    // </editor-fold>
 
     static String getTxt(String SKEY){
        if (Terminal.Txt.containsKey(SKEY)){
            return Terminal.Txt.get(SKEY);//translated
        } else {
            Log.log("SKEY not found" + SKEY);
            return "";
        }
    }
}

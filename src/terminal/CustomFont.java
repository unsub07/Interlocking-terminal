package terminal;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CustomFont {

    Font customFont;

    CustomFont() {
        int F = 12;
        switch (Terminal.zoom) {
            case 72:
                F = 12;
                break;
            case 56:
                F = 10;
                break;
            case 36:
                F = 8;
                break;
        }
                
        try {
            Terminal.SANS12 = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new java.io.File("/opt/attrans/fonts/a.ttf")).deriveFont(java.awt.Font.BOLD, F);
//            Terminal.SANS12 = new java.awt.Font("sansserif", java.awt.Font.BOLD, 12);//standart 0

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("/opt/attrans/fonts/a.ttf")));
        } catch (IOException | FontFormatException e) {
            Terminal.SANS12 = new java.awt.Font("sansserif", java.awt.Font.BOLD, F);//standart 0
            Err.err(e);
        }   
            Terminal.SANS08 = Terminal.SANS12.deriveFont(Terminal.SANS12.getStyle() | java.awt.Font.BOLD, Terminal.SANS12.getSize() - 4);//8  -4
            Terminal.SANS09 = Terminal.SANS12.deriveFont(Terminal.SANS12.getStyle() | java.awt.Font.BOLD, Terminal.SANS12.getSize() - 3);//9  -3
            Terminal.SANS10 = Terminal.SANS12.deriveFont(Terminal.SANS12.getStyle() | java.awt.Font.BOLD, Terminal.SANS12.getSize() - 2);//10 -2
            Terminal.SANS11 = Terminal.SANS12.deriveFont(Terminal.SANS12.getStyle() | java.awt.Font.BOLD, Terminal.SANS12.getSize() - 1);//11 -1

            Terminal.SANS13 = Terminal.SANS12.deriveFont(Terminal.SANS12.getStyle() | java.awt.Font.BOLD, Terminal.SANS12.getSize() + 1);//13 +1

            Terminal.SANS14 = Terminal.SANS12.deriveFont(Terminal.SANS12.getStyle() | java.awt.Font.BOLD, Terminal.SANS12.getSize() + 2);//14 +2
            Terminal.SANS14P = Terminal.SANS12.deriveFont(Terminal.SANS12.getStyle() & ~java.awt.Font.BOLD, Terminal.SANS12.getSize() + 2);//14 +2   PLAIN
            Terminal.SANS18 = Terminal.SANS12.deriveFont(Terminal.SANS12.getStyle() | java.awt.Font.BOLD, Terminal.SANS12.getSize() + 4);//18 +4
            Terminal.SANS18P = Terminal.SANS12.deriveFont(Terminal.SANS12.getStyle() & ~java.awt.Font.BOLD, Terminal.SANS12.getSize() + 4);//18 +4 PLAIN        
    }
}

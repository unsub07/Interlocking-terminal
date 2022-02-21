
package terminal;

import javax.swing.JComponent;
import java.awt.Graphics;
//import java.awt.Image;
import javax.swing.ImageIcon;

public final class Img extends JComponent {

//    private Image image;//это круче но гиф не работает
    private ImageIcon icon;
    
    public Img(ImageIcon icon) {
        super();
        setIcon(icon);
    }

    public Img() {
        this(null);
    }

//    public Image getIcon() {
//        return image;//это круче но гиф не работает
//    }
    public ImageIcon getIcon() {
        return icon;
    }    
//    public int getIconWidth() {
//        return image.getWidth(this);
//    }
//
//
//    public int getIconHeight() {
//        return image.getHeight(this);
//    }

    public void setIcon(ImageIcon icon) {
        if (icon != null) {
//            image = icon.getImage();//это круче но гиф не работает
            this.icon = icon;
        }
    }

//    @Override
//    public void paintComponent(Graphics g) {
//        if(image == null) return;
//        g.drawImage(image, 0, 0, null);    //это круче но гиф не работает
//    }
    @Override
    public void paintComponent(Graphics g) {
        if(icon == null) return;
        super.paintComponent(g);
        icon.paintIcon(this, g, 0, 0);
    }
}
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by aperte on 29.06.2015.
 */
public class GameMenu extends JPanel {
    BufferedImage background;
    BufferedImage button_start_normal;
    BufferedImage button_maps_normal;
    BufferedImage button_exit_normal;
    BufferedImage button_hover_start;
    BufferedImage button_hover_maps;
    BufferedImage button_hover_exit;

    Sound bmg;
    GameWindow master;

    public GameMenu(GameWindow master) {
        this.master = master;

        try {
            background = ImageIO.read(new File("gfx/menu_draft.bmp"));
            button_start_normal = ImageIO.read(new File("gfx/button_start_normal.bmp"));
            button_maps_normal = ImageIO.read(new File("gfx/button_maps_normal.bmp"));
            button_exit_normal = ImageIO.read(new File("gfx/button_exit_normal.bmp"));

            button_hover_start = ImageIO.read(new File("gfx/button_hover_start.bmp"));
            button_hover_maps = ImageIO.read(new File("gfx/button_hover_maps.bmp"));
            button_hover_exit = ImageIO.read(new File("gfx/button_hover_exit.bmp"));
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(331, 400, 57, 160));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.setOpaque(false);
        JButton startButton = new JButton(new ImageIcon(button_start_normal));
        startButton.setSize(new Dimension(240, 64));
        startButton.setBorder(BorderFactory.createEmptyBorder());
        startButton.setRolloverEnabled(true);
        startButton.setRolloverIcon(new ImageIcon(button_hover_start));
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                master.startGame();
            }
        });

        JButton mapsButton = new JButton(new ImageIcon(button_maps_normal));
        mapsButton.setPreferredSize(new Dimension(240, 64));
        mapsButton.setBorder(BorderFactory.createEmptyBorder());
        mapsButton.setRolloverEnabled(true);
        mapsButton.setRolloverIcon(new ImageIcon(button_hover_maps));

        JButton exitButton = new JButton(new ImageIcon(button_exit_normal));
        exitButton.setSize(new Dimension(240, 64));
        exitButton.setBorder(BorderFactory.createEmptyBorder());
        exitButton.setRolloverEnabled(true);
        exitButton.setRolloverIcon(new ImageIcon(button_hover_exit));
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                master.dispose();
            }
        });

        buttonPanel.add(startButton);
        buttonPanel.add(Box.createVerticalStrut(8));
        buttonPanel.add(mapsButton);
        buttonPanel.add(Box.createVerticalStrut(9));
        buttonPanel.add(exitButton);

        add(buttonPanel);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(background, 0, 0, null);
    }
}

class Sound {
 // dummy class
}

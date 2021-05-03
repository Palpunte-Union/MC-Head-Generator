package com.github.eighty88.mcskin;

import com.github.eighty88.mcskin.imager.ImageConverter;
import com.github.eighty88.mcskin.imager.PlayerSkin;
import com.github.eighty88.mcskin.imager.SkinPose;
import com.github.eighty88.mcskin.imager.renderer.PointLight;
import com.github.eighty88.mcskin.imager.renderer.point.Point3d;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends JFrame implements ActionListener, KeyListener {

    static JFrame frame;

    private JLabel labelPreview = null;

    private JPanel panelCenter = null;
    private JPanel panelBottom = null;
    private JPanel totalContentPane = null;

    private JTextField textFieldMCID = null;

    private JButton buttonSearch = null;
    private JButton buttonSave = null;

    private String s = "";

    Main(String title) {
        setTitle(title);
        setBounds(100, 100, 300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(getPanelContentPane());
        setResizable(false);

        getButtonSearch().addActionListener(this);
        getButtonSave().addActionListener(this);
        textFieldMCID.addKeyListener(this);
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame = new Main("MC Head Generator");
        frame.setVisible(true);
    }

    private JPanel getPanelContentPane() {
        if (totalContentPane == null) {
            try {
                totalContentPane = new JPanel();
                totalContentPane.setName("PanelContentPane");
                totalContentPane.setLayout(new BorderLayout(5, 5));
                totalContentPane.setPreferredSize(new Dimension(300, 400));
                totalContentPane.add(getPanelCenter(), "Center");
                totalContentPane.add(getPanelBottom(), "South");
            } catch (Throwable ivjExc) {
                ivjExc.printStackTrace();
            }
        }
        return totalContentPane;
    }

    private JPanel getPanelCenter() {
        if (panelCenter == null) {
            try {
                (panelCenter = new JPanel()).setName("PanelCenter");
                panelCenter.setLayout(null);
                panelCenter.add(getFieldMCID(), getFieldMCID().getName());
                panelCenter.add(getButtonSearch(), getButtonSearch().getName());
                panelCenter.add(getLabelPreview(), getLabelPreview().getName());
            } catch (Throwable ivjExc) {
                ivjExc.printStackTrace();
            }
        }
        return panelCenter;
    }

    private JLabel getLabelPreview() {
        if (labelPreview == null) {

            try {
                labelPreview = new JLabel();
                labelPreview.setName("LabelPreview");
                labelPreview.setBounds(10, 40, 270, 270);
                labelPreview.setPreferredSize(new Dimension(270, 270));
                labelPreview.setText("");
            } catch (Throwable ivjExc) {
                ivjExc.printStackTrace();
            }
        }
        return labelPreview;
    }

    private JTextField getFieldMCID() {
        if (textFieldMCID == null) {

            try {
                textFieldMCID = new JTextField();
                textFieldMCID.setName("FieldFolder");
                textFieldMCID.setBounds(10, 27, 240, 20);
                textFieldMCID.setEditable(true);
                textFieldMCID.setPreferredSize(new Dimension(287, 20));
            } catch (Throwable ivjExc) {
                ivjExc.printStackTrace();
            }
        }
        return textFieldMCID;
    }

    private JButton getButtonSearch() {
        if (buttonSearch == null) {
            try {
                BufferedImage myPicture = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("assets/search.png"), "Search icon not found."));
                Image scaled = myPicture.getScaledInstance(25 - 8, 20 - 6, Image.SCALE_SMOOTH);
                buttonSearch = new JButton(new ImageIcon(scaled));
                buttonSearch.setName("ButtonSearch");
                buttonSearch.setBounds(250, 27, 25, 20);
                buttonSearch.setPreferredSize(new Dimension(25, 20));
            } catch (Throwable ivjExc) {
                ivjExc.printStackTrace();
            }
        }
        return buttonSearch;
    }

    private JPanel getPanelBottom() {
        if (panelBottom == null) {
            try {
                panelBottom = new JPanel();
                panelBottom.setName("PanelBottom");
                panelBottom.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
                panelBottom.setPreferredSize(new Dimension(100, 55));
                panelBottom.add(getButtonSave(), getButtonSave().getName());
            } catch (Throwable ivjExc) {
                ivjExc.printStackTrace();
            }
        }
        return panelBottom;
    }

    private JButton getButtonSave() {
        if (buttonSave == null) {

            try {
                buttonSave = new JButton();
                buttonSave.setName("ButtonSave");
                buttonSave.setPreferredSize(new Dimension(200, 26));
                buttonSave.setText("Save");
            } catch (Throwable ivjExc) {
                ivjExc.printStackTrace();
            }
        }
        return buttonSave;
    }


    public static BufferedImage a(String str, boolean b) {
        BufferedImage image = null;
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + str);

            ArrayList<PointLight> lights = new ArrayList<>();
            lights.add(new PointLight(new Point3d(0, 150, 125), 5000, 3));
            JSONObject object = new JSONObject(Objects.requireNonNull(readURL(url)));
            String uuidString = (String) object.get("id");
            image = ImageConverter.renderSkin(
                    new PlayerSkin(uuidString),
                    new SkinPose(SkinPose.standing().getValues()),
                    0f,
                    0f,
                    lights,
                    0,
                    0,
                    0,
                    1,
                    1000,
                    1000
            );
            image = image.getSubimage(367, 76, 264, 264);
            if (b) {
                ImageIO.write(image, "png", new File(str + ".png"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private static String readURL(URL url) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder builder = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                builder.append(chars, 0, read);
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getButtonSave()) {
            if (!s.equals("")) {
                a(s, true);
            }
            System.out.println("SaveEvent");
        } else if (e.getSource() == getButtonSearch()) {
            System.out.println("SearchEvent");
            s = textFieldMCID.getText();
            if (!s.equals("")) {
                BufferedImage bi = a(s, false);
                labelPreview.setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
                labelPreview.setIcon(new ImageIcon(bi.getScaledInstance(bi.getWidth(), bi.getHeight(), 0)));
                buttonSave.setText("Save as " + s + ".png");
            } else {
                buttonSave.setText("Save");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("SearchEvent");
            s = textFieldMCID.getText();
            if (!s.equals("")) {
                BufferedImage bi = a(s, false);
                labelPreview.setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
                labelPreview.setIcon(new ImageIcon(bi.getScaledInstance(bi.getWidth(), bi.getHeight(), 0)));
                buttonSave.setText("Save as " + s + ".png");
            } else {
                buttonSave.setText("Save");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by David on 20/08/2016.
 */
public class RCValidator {
    private JLabel lblTicketType;
    private JRadioButton rdTicketNormal;
    private JRadioButton rbEticket;
    private JLabel lblConditions;
    private JCheckBox chkPUM;
    private JCheckBox chkLar;
    private JCheckBox chkAttendants;
    private JLabel lblSiteList;
    private JTextField textField1;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JCheckBox checkBox4;
    private JCheckBox checkBox5;
    private JCheckBox checkBox6;
    private JCheckBox checkBox7;
    private JCheckBox checkBox8;
    private JCheckBox checkBox9;
    private JCheckBox checkBox10;
    private JTextField textField2;
    private JButton BUSCARButton;
    private JPanel RCPanel;

    public RCValidator() {
        BUSCARButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                RC rc = new RC();
                rc.getURL();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RCValidator");
        frame.setContentPane(new RCValidator().RCPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

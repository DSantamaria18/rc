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
    private JCheckBox checkBox11;
    private JCheckBox checkBox12;
    private JCheckBox checkBox13;
    private JCheckBox checkBox14;
    private JCheckBox checkBox15;
    private JCheckBox checkBox16;
    private JCheckBox checkBox17;
    private JCheckBox checkBox18;
    private JCheckBox checkBox19;
    private JCheckBox checkBox20;
    private JCheckBox checkBox21;
    private JCheckBox checkBox22;
    private JCheckBox checkBox23;
    private JCheckBox checkBox24;
    private JCheckBox checkBox25;
    private JCheckBox checkBox26;
    private JCheckBox checkBox27;
    private JCheckBox checkBox28;
    private JCheckBox checkBox29;
    private JCheckBox checkBox30;
    private JCheckBox checkBox31;
    private JCheckBox checkBox32;
    private JCheckBox checkBox33;
    private JCheckBox checkBox34;
    private JCheckBox checkBox35;
    private JCheckBox checkBox36;
    private JCheckBox checkBox37;
    private JCheckBox checkBox38;
    private JCheckBox checkBox39;
    private JCheckBox checkBox40;
    private JCheckBox checkBox41;
    private JCheckBox checkBox42;
    private JCheckBox checkBox43;
    private JCheckBox checkBox44;
    private JCheckBox checkBox45;
    private JCheckBox checkBox46;
    private JCheckBox checkBox47;
    private JCheckBox checkBox48;
    private JCheckBox checkBox49;
    private JCheckBox checkBox50;

    public RCValidator() {
        BUSCARButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                // TIPO DE TICKET
                int ticketType = 99;
                if (rdTicketNormal.isSelected()) {
                    ticketType = 0;
                } else {
                    if (rbEticket.isSelected()) {
                        ticketType = 2;
                    }
                }

                // CONDICIONES
                boolean pum = chkPUM.isSelected();
                boolean lar = chkLar.isSelected();
                boolean withAttendants = chkAttendants.isSelected();

                // SITES



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

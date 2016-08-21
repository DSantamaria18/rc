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
    private JCheckBox checkBox1;
    private JTextField textField2;
    private JButton BUSCARButton;
    private JPanel RCPanel;
    private JLabel lblSites;
    private JComboBox comboBox1;

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

                if (checkBox1.isSelected())


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

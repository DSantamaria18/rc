import javax.swing.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

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
    private JCheckBox chkLimiteInt;
    private JTextField textResult;
    private JButton BUSCARButton;
    private JPanel RCPanel;
    private JLabel lblSites;
    private JComboBox comboSites;
    private JButton REPORTARButton;
    private JTextArea txtReport;

    private RC rc;

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
                boolean limiteInt = chkLimiteInt.isSelected();

                //Sites
                String domain = comboSites.getSelectedItem().toString();

                textResult.setText("");
                rc = new RC();
                String res =  rc.getURL(ticketType, pum, lar, withAttendants, limiteInt, domain);
                textResult.setText(res);
            }
        });
        REPORTARButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String url = textResult.getText();
                Map<String, String> condiciones = new HashMap<String, String>();

                // Tipo de entrada
                if (rbEticket.isSelected()) {
                    condiciones.put("Tipo", "E-Ticket");
                } else {
                    if (rdTicketNormal.isSelected()){
                        condiciones.put("Tipo", "NORMAL");
                    } else {
                        condiciones.put("Tipo", "SIN ESPECIFICAR");
                    }
                }

                // Condiciones
                if (chkPUM.isSelected()){
                    condiciones.put("PUM", "TRUE");
                } else {
                    condiciones.put("PUM", "FALSE");
                }

                if (chkLar.isSelected()){
                    condiciones.put("LAR", "TRUE");
                } else {
                    condiciones.put("LAR", "FALSE");
                }

                if (chkAttendants.isSelected()){
                    condiciones.put("WITH ATTENDATNS", "TRUE");
                } else {
                    condiciones.put("WITH ATTENDATNS", "FALSE");
                }

                if (chkLimiteInt.isSelected()){
                    condiciones.put("LIMITE INT", "TRUE");
                } else {
                    condiciones.put("LIMITE INT", "FALSE");
                }
                txtReport.setText(rc.getReportTemplate(url, condiciones));
            }
        });
    }

    public void main(String[] args) {
        JFrame frame = new JFrame("RCValidator");
        frame.setContentPane(new RCValidator().RCPanel);
        frame.setContentPane(RCPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        comboSites = new JComboBox();
        RC rc = new RC();
        String[] dominios = rc.getSitesList();
        for (int i = 0; i < dominios.length; i++) {
            comboSites.addItem(dominios[i]);
        }
    }
}

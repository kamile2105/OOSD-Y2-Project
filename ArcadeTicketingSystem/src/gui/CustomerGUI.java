package gui;

import dao.CustomerDAO;
import dao.UserDAO;
import model.Customer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class CustomerGUI extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);

    // the neon colour palette to design the look of my interface
    private final Color BG_DARK = new Color(15, 15, 25);
    private final Color NEON_PINK = new Color(255, 0, 255);
    private final Color NEON_CYAN = new Color(0, 255, 255);
    private final Color TEXT_COLOR = Color.WHITE;

    // customer registration fields
    private JTextField txtName = createStyledTextField();
    private JTextField txtEmail = createStyledTextField();
    private JTextField txtTickets = createStyledTextField();
    private JButton btnAdd = createNeonButton("REGISTER", NEON_CYAN);

    // the login components
    private JTextField txtUser = createStyledTextField();
    private JPasswordField txtPass = new JPasswordField(20);
    private JButton btnLogin = createNeonButton("ENTER SYSTEM", NEON_PINK);

    public CustomerGUI() {
        // title
        setTitle("SYNTH-CITY SLOTS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // removing window borders
        setUndecorated(true);

        mainContainer.setBackground(BG_DARK);

        // adding panels to the layout
        mainContainer.add(createSelectionPanel(), "SELECTION");
        mainContainer.add(createLoginPanel(), "STAFF_LOGIN");
        mainContainer.add(createCustomerPanel(), "CUSTOMER_FORM");

        // destination panels
        mainContainer.add(createStaffDashboard(), "STAFF_DASHBOARD");
        mainContainer.add(createCustomerWelcomePage(), "CUSTOMER_WELCOME");

        add(mainContainer);

        // making the interface full screen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            // the fallback if the hardware doesnt support it
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        }
    }

    // after staff login successful
    private JPanel createStaffDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        JLabel lbl = new JLabel("STAFF CONTROL UNIT ACTIVE", SwingConstants.CENTER);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 36));
        lbl.setForeground(NEON_PINK);
        panel.add(lbl, BorderLayout.CENTER);

        JButton btnLogout = createNeonButton("LOGOUT", Color.GRAY);
        btnLogout.addActionListener(e -> cardLayout.show(mainContainer, "SELECTION"));
        panel.add(btnLogout, BorderLayout.SOUTH);
        return panel;
    }

    // after customer registers successfully
    private JPanel createCustomerWelcomePage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        JLabel lbl = new JLabel("REGISTRATION COMPLETE. WELCOME TO THE GRID.", SwingConstants.CENTER);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 24));
        lbl.setForeground(NEON_CYAN);
        panel.add(lbl, BorderLayout.CENTER);

        JButton btnBack = createNeonButton("RETURN TO START", NEON_CYAN);
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "SELECTION"));
        panel.add(btnBack, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        // increased padding
        panel.setBorder(new EmptyBorder(100, 100, 100, 100));

        // header section
        JLabel lblTitle = new JLabel("SYNTH-CITY SLOTS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Monospaced", Font.BOLD, 48));
        lblTitle.setForeground(NEON_CYAN);
        lblTitle.setBorder(new EmptyBorder(0, 0, 50, 0));
        panel.add(lblTitle, BorderLayout.NORTH);

        // grid panel
        JPanel cardGrid = new JPanel(new GridLayout(1, 2, 40, 0));
        cardGrid.setOpaque(false);

        // staff card
        JButton btnStaff = createZoomStyleCard("STAFF LOGIN", "System Admin & Management", NEON_PINK);
        btnStaff.addActionListener(e -> cardLayout.show(mainContainer, "STAFF_LOGIN"));

        // customer card
        JButton btnCust = createZoomStyleCard("CUSTOMER ACCESS", "Register & View Tickets", NEON_CYAN);
        btnCust.addActionListener(e -> cardLayout.show(mainContainer, "CUSTOMER_FORM"));

        cardGrid.add(btnCust);
        cardGrid.add(btnStaff);

        panel.add(cardGrid, BorderLayout.CENTER);

        return panel;
    }

    // helper method for title/card look
    private JButton createZoomStyleCard(String title, String subtitle, Color neonColor) {
        // html for multiline affect
        String buttonText = "<html><center><font size='6'>◈</font><br><br><b>" + title + "</b><br><font size='3'>" + subtitle + "</font></center></html>";

        JButton btn = new JButton(buttonText);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setBackground(new Color(25, 25, 40)); // Slightly lighter than background
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(neonColor, 2, true)); // 'true' for rounded corners
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // hover affect neon glow
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(neonColor);
                btn.setForeground(BG_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(25, 25, 40));
                btn.setForeground(Color.WHITE);
            }
        });
        return btn;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblHeader = new JLabel("STAFF AUTHENTICATION", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Monospaced", Font.BOLD, 22));
        lblHeader.setForeground(NEON_PINK);
        panel.add(lblHeader, BorderLayout.NORTH);

        JPanel loginForm = new JPanel(new GridBagLayout());
        loginForm.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        styleLabel(new JLabel("Username:"), loginForm, gbc, 0);
        gbc.gridx = 1; loginForm.add(txtUser, gbc);

        styleLabel(new JLabel("Password:"), loginForm, gbc, 1);
        gbc.gridx = 1;
        txtPass.setBackground(new Color(30, 30, 50));
        txtPass.setForeground(Color.WHITE);
        txtPass.setCaretColor(NEON_PINK);
        txtPass.setBorder(new LineBorder(NEON_PINK, 1));
        loginForm.add(txtPass, gbc);

        panel.add(loginForm, BorderLayout.CENTER);

        JPanel footer = new JPanel(new GridLayout(1, 2, 15, 15));
        footer.setOpaque(false);
        JButton btnBack = createNeonButton("BACK", Color.GRAY);

        footer.add(btnBack);
        footer.add(btnLogin);
        panel.add(footer, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "SELECTION"));
        btnLogin.addActionListener(e -> {
            String user = txtUser.getText();
            String pass = new String(txtPass.getPassword());
            if (new UserDAO().loginUser(user, pass)) {
                // NAVIGATION: Switch to the staff dashboard page
                cardLayout.show(mainContainer, "STAFF_DASHBOARD");
            } else {
                showNeonError("Access Denied: Invalid Credentials");
            }
        });

        return panel;
    }

    private JPanel createCustomerPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel lblHeader = new JLabel("NEW CUSTOMER UPLINK", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Monospaced", Font.BOLD, 22));
        lblHeader.setForeground(NEON_CYAN);
        mainPanel.add(lblHeader, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        styleLabel(new JLabel("Name:"), formPanel, gbc, 0);
        gbc.gridx = 1; formPanel.add(txtName, gbc);

        styleLabel(new JLabel("Email:"), formPanel, gbc, 1);
        gbc.gridx = 1; formPanel.add(txtEmail, gbc);

        styleLabel(new JLabel("Initial Tickets:"), formPanel, gbc, 2);
        gbc.gridx = 1; formPanel.add(txtTickets, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new GridLayout(1, 2, 15, 15));
        footer.setOpaque(false);
        JButton btnBack = createNeonButton("LOGOUT", Color.GRAY);

        footer.add(btnBack);
        footer.add(btnAdd);
        mainPanel.add(footer, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            try {
                // Validation: Ensure fields aren't empty
                if(txtName.getText().isEmpty() || txtEmail.getText().isEmpty()) {
                    showNeonError("All fields are required!");
                    return;
                }

                Customer c = new Customer();
                c.setName(txtName.getText());
                c.setEmail(txtEmail.getText());
                c.setTickets(Integer.parseInt(txtTickets.getText()));

                new CustomerDAO().addCustomer(c);

                // switch to customer welcome page
                cardLayout.show(mainContainer, "CUSTOMER_WELCOME");

                // Clear fields
                txtName.setText("");
                txtEmail.setText("");
                txtTickets.setText("");

            } catch (NumberFormatException ex) {
                showNeonError("Data Error: Tickets must be Numeric");
            } catch (Exception ex) {
                showNeonError("System Error: " + ex.getMessage());
            }
        });

        return mainPanel;
    }

    // the helper methods for styling the interface
    private JButton createNeonButton(String text, Color neonColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 14));
        btn.setBackground(BG_DARK);
        btn.setForeground(neonColor);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(neonColor, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // hover affect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(neonColor);
                btn.setForeground(BG_DARK);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BG_DARK);
                btn.setForeground(neonColor);
            }
        });
        return btn;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setBackground(new Color(30, 30, 50));
        field.setForeground(Color.WHITE);
        field.setCaretColor(NEON_CYAN);
        field.setBorder(new LineBorder(NEON_CYAN, 1));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return field;
    }

    private void styleLabel(JLabel label, JPanel panel, GridBagConstraints gbc, int y) {
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Monospaced", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(label, gbc);
    }

    private void showNeonError(String msg) {
        UIManager.put("OptionPane.background", BG_DARK);
        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("OptionPane.messageForeground", NEON_PINK);
        JOptionPane.showMessageDialog(this, msg, "SYSTEM ERROR", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerGUI().setVisible(true));
    }
}
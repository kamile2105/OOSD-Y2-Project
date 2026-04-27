// Kamile Kacinskaite
// Arcade Ticketing System Project
// C00312390
// Date submitted: 21/04/2026

package gui;

import dao.CustomerDAO;
import dao.PurchaseDAO;
import dao.UserDAO;
import model.Customer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CustomerGUI extends JFrame {

    // ── layout ────────────────────────────────────────────────────────────────
    private final CardLayout cardLayout    = new CardLayout();
    private final JPanel     mainContainer = new JPanel(cardLayout);

    // ── colour palette ────────────────────────────────────────────────────────
    private final Color BG_DARK     = new Color(10, 10, 20);
    private final Color BG_PANEL    = new Color(18, 18, 35);
    private final Color BG_CARD     = new Color(25, 25, 45);
    private final Color NEON_PINK   = new Color(255, 0, 200);
    private final Color NEON_CYAN   = new Color(0, 230, 255);
    private final Color NEON_PURPLE = new Color(160, 0, 255);
    private final Color NEON_YELLOW = new Color(255, 220, 0);
    private final Color NEON_GREEN  = new Color(0, 255, 120);
    private final Color TEXT_WHITE  = Color.WHITE;
    private final Color TEXT_DIM    = new Color(160, 160, 200);

    // ── validation patterns ───────────────────────────────────────────────────
    private final String NAME_PATTERN  = "^[a-zA-Z\\s]+$"; // validating that a name cannot contain numbers
    private final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$"; // general correct email pattern

    // ── staff login shared fields ─────────────────────────────────────────────
    private final JTextField     txtStaffUser  = mkField(NEON_PINK);
    private final JPasswordField txtStaffPass  = mkPass(NEON_PINK);
    private final JButton        btnStaffLogin = neonBtn("ACCESS GRANTED  →", NEON_PINK); // button to proceed after enterimg details into username and password

    // ── staff dashboard table ─────────────────────────────────────────────────
    private DefaultTableModel tableModel;
    private JTable            customerTable;

    // ── staff edit / delete / top up shared state ──────────────────────────────
    private final JTextField txtEditName    = mkField(NEON_PINK);
    private final JTextField txtEditEmail   = mkField(NEON_PINK);
    private final JTextField txtEditTickets = mkField(NEON_PINK);
    private       int        editingId      = -1;

    // ── staff search ──────────────────────────────────────────────────────────
    private final JTextField txtSearch = mkField(NEON_CYAN);

    // ── customer registration fields ──────────────────────────────────────────
    private final JTextField txtRegName    = mkField(NEON_CYAN);
    private final JTextField txtRegEmail   = mkField(NEON_CYAN);
    private final JTextField txtRegTickets = mkField(NEON_CYAN);

    // ── customer login field ──────────────────────────────────────────────────
    private final JTextField txtCustEmail = mkField(NEON_CYAN);

    // ── logged-in customer live labels & edit fields ──────────────────────────
    private int              loggedInId       = -1;
    private final JLabel     lblBalanceVal    = glowLbl("--",     NEON_CYAN,   56);
    private final JLabel     lblHubWelcome    = glowLbl("PLAYER", NEON_PURPLE, 18);
    private final JTextField txtCustEditName  = mkField(NEON_PURPLE);
    private final JTextField txtCustEditEmail = mkField(NEON_PURPLE);

    // ── delete-screen live labels ─────────────────────────────────────────────
    private final JLabel lblStaffDeleteTarget = glowLbl("[ PLAYER NAME ]", NEON_PINK, 30);

    // ── top-up screen live labels ─────────────────────────────────────────────
    private final JTextField txtTopUpCustomer = mkField(NEON_GREEN);
    private final JTextField txtTopUpAmount   = mkField(NEON_GREEN);
    private final JLabel     lblTopUpCurrent  = glowLbl("Current: -- tickets", TEXT_DIM, 13);


    /**
     * Constructor for the CustomerGUI
     * Initializes the main window and registers all application screens
     * and sets the application to full-screen mode.
     */
    public CustomerGUI() {
        // basic window setup
        setTitle("SYNTH-CITY SLOTS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close app when window gets closed
        setUndecorated(true);               // remove title bar and borders for a kiosk feel
        mainContainer.setBackground(BG_DARK);       // set initial theme colour

        // Screen Registration (CardLayout)
        // each line adds a functional panel to the main container with a unique String ID
        mainContainer.add(buildSelectionScreen(),    "SELECTION");
        mainContainer.add(buildStaffLoginScreen(),   "STAFF_LOGIN");
        mainContainer.add(buildStaffHubScreen(),     "STAFF_HUB");
        mainContainer.add(buildStaffViewScreen(),    "STAFF_VIEW");
        mainContainer.add(buildStaffAddScreen(),     "STAFF_ADD");
        mainContainer.add(buildStaffEditScreen(),    "STAFF_EDIT");
        mainContainer.add(buildStaffDeleteScreen(),  "STAFF_DELETE");
        mainContainer.add(buildStaffTopUpScreen(),   "STAFF_TOPUP");
        mainContainer.add(buildCustomerRegister(),   "CUSTOMER_REGISTER");
        mainContainer.add(buildCustomerLogin(),      "CUSTOMER_LOGIN");
        mainContainer.add(buildCustomerHub(),        "CUSTOMER_HUB");
        mainContainer.add(buildCustBalanceScreen(),  "CUST_BALANCE");
        mainContainer.add(buildCustEditScreen(),     "CUST_EDIT");
        mainContainer.add(buildCustDeleteScreen(),   "CUST_DELETE");
        mainContainer.add(buildCustomerWelcome(),    "CUSTOMER_WELCOME");
        mainContainer.add(buildPrizesScreen(),       "CUST_PRIZES");

        // add the populated container to the JFrame
        add(mainContainer);

        // Display Configuration
        // access the system's graphics environment to handle window sizing
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        // attempt to enter exclusive full screen mode
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            // fallback: maximise the window if full screen isnt supported
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        }
    }

    // option selection screen — landing / home
    private JPanel buildSelectionScreen() {
        JPanel p = darkP(new BorderLayout());
        p.setBorder(new EmptyBorder(80, 120, 80, 120));

        // title block
        JPanel title = new JPanel(new GridLayout(3, 1, 0, 8));
        title.setOpaque(false);
        JLabel t1 = glowLbl("◈  SYNTH-CITY SLOTS  ◈", NEON_CYAN, 52);
        t1.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel t2 = glowLbl("ARCADE TICKETING SYSTEM", NEON_PURPLE, 16);
        t2.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel t3 = glowLbl("─────────────────────────────────────────", TEXT_DIM, 13);
        t3.setHorizontalAlignment(SwingConstants.CENTER);
        title.add(t1); title.add(t2); title.add(t3);
        p.add(title, BorderLayout.NORTH);

        // two big cards
        JPanel cardRow = new JPanel(new GridLayout(1, 2, 50, 0));
        cardRow.setOpaque(false);
        cardRow.setBorder(new EmptyBorder(50, 0, 50, 0));

        JButton btnCust  = bigCard("◉  PLAYER ACCESS",
                "Register  ·  Login  ·  View Tickets", NEON_CYAN);
        JButton btnStaff = bigCard("◈  STAFF PORTAL",
                "Manage Customers  ·  Reports  ·  Top-Up", NEON_PINK);

        // action listener
        btnCust.addActionListener(e -> {
            // define the button options
            String[] opts = {"REGISTER NEW ACCOUNT", "LOGIN TO MY ACCOUNT"};
            // show a styled option dialog to let the user choose between Register or Login
            int c = JOptionPane.showOptionDialog(this, "SELECT ACCESS MODE", "PLAYER GATEWAY",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
            // navigate based on the users choice
            if (c == 0) cardLayout.show(mainContainer, "CUSTOMER_REGISTER");
            else if (c == 1) cardLayout.show(mainContainer, "CUSTOMER_LOGIN");
        });
        // action lisenter for the staff button goes straight to staff login page as any staff
        // member gets made an account initially in the database, this is used for them to login only
        btnStaff.addActionListener(e -> cardLayout.show(mainContainer, "STAFF_LOGIN"));

        // add buttons to a row container and place that row in the center of the panel
        cardRow.add(btnCust);
        cardRow.add(btnStaff);
        p.add(cardRow, BorderLayout.CENTER);

        // styled shutdown button which exits the app completely
        JButton btnExit = neonBtn("⏻  SHUTDOWN SYSTEM", Color.RED);
        // terminate when that button is clicked
        btnExit.addActionListener(e -> System.exit(0));
        // centering and positioning the shutdown button
        JPanel south = centreOf(btnExit);
        south.setBorder(new EmptyBorder(0, 0, 10, 0));
        p.add(south, BorderLayout.SOUTH);
        return p; // return the fully constructed panel
    }

    // staff login panel
    private JPanel buildStaffLoginScreen() {
        JPanel p = darkP(new BorderLayout(0, 30));
        p.setBorder(new EmptyBorder(60, 0, 60, 0));
        // bright coloured header
        p.add(header("◈  STAFF AUTHENTICATION", NEON_PINK), BorderLayout.NORTH);

        JPanel card = cardP(new GridBagLayout());
        card.setBorder(new EmptyBorder(50, 80, 50, 80));
        GridBagConstraints g = gbcBase();
        row(card, g, "USERNAME :", txtStaffUser, 0);
        row(card, g, "PASSWORD :", txtStaffPass, 1);
        p.add(centreOf(card), BorderLayout.CENTER);

        // buttons to go back or clear the fields entirely to start over
        JButton btnBack  = neonBtn("← BACK",   TEXT_DIM);
        JButton btnClear = neonBtn("⟳  CLEAR", NEON_YELLOW);
        btnBack .addActionListener(e -> cardLayout.show(mainContainer, "SELECTION"));
        btnClear.addActionListener(e -> { txtStaffUser.setText(""); txtStaffPass.setText(""); txtStaffUser.requestFocus(); });

        // making sure both fields are entered in so the login goes through smoothly
        btnStaffLogin.addActionListener(e -> {
            String u = txtStaffUser.getText().trim();
            String pw = new String(txtStaffPass.getPassword()).trim();
            if (u.isEmpty() || pw.isEmpty()) { err("BOTH FIELDS ARE REQUIRED."); return; }
            // if successful then bring to the staff hub if not present message that access is denied
            try {
                if (new UserDAO().loginUser(u, pw)) {
                    txtStaffUser.setText(""); txtStaffPass.setText("");
                    cardLayout.show(mainContainer, "STAFF_HUB");
                } else { err("ACCESS DENIED: Invalid credentials."); }
            } catch (Exception ex) { err("CONNECTION ERROR: " + ex.getMessage()); ex.printStackTrace(); }
        });

        p.add(row3(btnBack, btnClear, btnStaffLogin), BorderLayout.SOUTH);
        return p; // return fully constructed panel
    }

    // staff hub — operation menu
    private JPanel buildStaffHubScreen() {
        JPanel p = darkP(new BorderLayout(0, 20));
        p.setBorder(new EmptyBorder(50, 80, 50, 80));
        // staff control panel
        p.add(header("◈  STAFF CONTROL UNIT", NEON_PINK), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 30, 30));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(30, 0, 30, 0));

        // allow staff to control customers accounts and their activity
        JButton btnView = bigCard("◉  VIEW CUSTOMERS",  "Read  ·  Search  ·  Filter All Records",  NEON_CYAN);
        JButton btnAdd  = bigCard("✚  ADD CUSTOMER",    "Create a New Customer Account",            NEON_GREEN);
        JButton btnEdit = bigCard("✎  EDIT CUSTOMER",   "Update Details  ·  Select From Table",     NEON_PINK);
        JButton btnDel  = bigCard("✖  DELETE CUSTOMER", "Remove a Customer From The System",        Color.RED);

        btnView.addActionListener(e -> { refreshTable(); cardLayout.show(mainContainer, "STAFF_VIEW"); });
        btnAdd .addActionListener(e -> cardLayout.show(mainContainer, "STAFF_ADD"));
        btnEdit.addActionListener(e -> { refreshTable(); info("SELECT a row in the table, then click  ✎ EDIT."); cardLayout.show(mainContainer, "STAFF_VIEW"); });
        btnDel .addActionListener(e -> { refreshTable(); info("SELECT a row in the table, then click  ✖ DELETE."); cardLayout.show(mainContainer, "STAFF_VIEW"); });

        grid.add(btnView); grid.add(btnAdd);
        grid.add(btnEdit); grid.add(btnDel);
        p.add(grid, BorderLayout.CENTER);

        // button to leave the panel
        JButton btnLogout = neonBtn("⏻  TERMINATE SESSION", Color.GRAY);
        btnLogout.addActionListener(e -> cardLayout.show(mainContainer, "SELECTION"));
        p.add(centreOf(btnLogout), BorderLayout.SOUTH);
        return p;
    }

    // staff view — read with search/filter and action buttons
    private JPanel buildStaffViewScreen() {
        JPanel p = darkP(new BorderLayout(0, 10));
        p.setBorder(new EmptyBorder(20, 25, 20, 25));
        p.add(header("◉  CUSTOMER DATABASE", NEON_CYAN), BorderLayout.NORTH);

        // table with all fields
        String[] cols = {"ID", "NAME", "EMAIL", "TICKETS"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        customerTable = new JTable(tableModel);
        styleTable(customerTable);
        JScrollPane scroll = new JScrollPane(customerTable);
        scroll.getViewport().setBackground(BG_CARD);
        scroll.setBorder(new LineBorder(NEON_CYAN, 1));
        p.add(scroll, BorderLayout.CENTER);

        // search for the customer
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        searchRow.setOpaque(false);
        JLabel lbl = glowLbl("SEARCH :", NEON_CYAN, 13);
        txtSearch.setPreferredSize(new Dimension(280, 32));
        JButton btnFilter  = neonBtn("FILTER",   NEON_CYAN);
        JButton btnShowAll = neonBtn("SHOW ALL", TEXT_DIM);
        btnFilter .addActionListener(e -> filterTable(txtSearch.getText().trim().toLowerCase()));
        btnShowAll.addActionListener(e -> { txtSearch.setText(""); refreshTable(); });
        searchRow.add(lbl); searchRow.add(txtSearch); searchRow.add(btnFilter); searchRow.add(btnShowAll);

        // actions for the customer options from staff panel
        JButton btnEdit    = neonBtn("✎  EDIT",    NEON_PINK);
        JButton btnDel     = neonBtn("✖  DELETE",  Color.RED);
        JButton btnTopUp   = neonBtn("⬆  TOP-UP",  NEON_GREEN);
        JButton btnRefresh = neonBtn("⟳  REFRESH", TEXT_DIM);
        JButton btnBack    = neonBtn("← BACK",     TEXT_DIM);

        // must select a row in order to edit
        btnEdit.addActionListener(e -> {
            int row = customerTable.getSelectedRow();
            if (row < 0) { err("SELECT A ROW FIRST."); return; }
            editingId = (int) tableModel.getValueAt(row, 0);
            txtEditName   .setText((String) tableModel.getValueAt(row, 1));
            txtEditEmail  .setText((String) tableModel.getValueAt(row, 2));
            txtEditTickets.setText(String.valueOf(tableModel.getValueAt(row, 3)));
            cardLayout.show(mainContainer, "STAFF_EDIT");
        });

        btnDel.addActionListener(e -> {
            int row = customerTable.getSelectedRow();
            if (row < 0) { err("SELECT A ROW FIRST."); return; }
            editingId = (int) tableModel.getValueAt(row, 0);
            lblStaffDeleteTarget.setText(((String) tableModel.getValueAt(row, 1)).toUpperCase());
            cardLayout.show(mainContainer, "STAFF_DELETE");
        });

        btnTopUp.addActionListener(e -> {
            int row = customerTable.getSelectedRow();
            if (row < 0) { err("SELECT A ROW FIRST."); return; }
            editingId = (int) tableModel.getValueAt(row, 0);
            txtTopUpCustomer.setText((String) tableModel.getValueAt(row, 1));
            txtTopUpAmount.setText("");
            lblTopUpCurrent.setText("Current balance: " + tableModel.getValueAt(row, 3) + " tickets");
            cardLayout.show(mainContainer, "STAFF_TOPUP");
        });

        btnRefresh.addActionListener(e -> refreshTable());
        btnBack   .addActionListener(e -> cardLayout.show(mainContainer, "STAFF_HUB"));

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        actionRow.setOpaque(false);
        actionRow.add(btnEdit); actionRow.add(btnDel); actionRow.add(btnTopUp);
        actionRow.add(btnRefresh); actionRow.add(btnBack);

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.add(searchRow, BorderLayout.NORTH);
        south.add(actionRow, BorderLayout.SOUTH);
        p.add(south, BorderLayout.SOUTH);

        p.addComponentListener(new ComponentAdapter() {
            @Override public void componentShown(ComponentEvent e) { refreshTable(); }
        });
        return p; // return fully constructed panel
    }

    // staff add — create a new customer
    private JPanel buildStaffAddScreen() {
        JTextField fName    = mkField(NEON_GREEN);
        JTextField fEmail   = mkField(NEON_GREEN);
        JTextField fTickets = mkField(NEON_GREEN);

        JPanel p = darkP(new BorderLayout(0, 20));
        p.setBorder(new EmptyBorder(60, 0, 60, 0));
        p.add(header("✚  ADD NEW CUSTOMER", NEON_GREEN), BorderLayout.NORTH);

        JPanel card = cardP(new GridBagLayout());
        card.setBorder(new EmptyBorder(40, 80, 40, 80));
        GridBagConstraints g = gbcBase();
        row(card, g, "FULL NAME :",       fName,    0);
        row(card, g, "EMAIL :",           fEmail,   1);
        row(card, g, "STARTING TICKETS :", fTickets, 2);
        p.add(centreOf(card), BorderLayout.CENTER);

        JButton btnSave  = neonBtn("✚  REGISTER CUSTOMER", NEON_GREEN);
        JButton btnClear = neonBtn("⟳  CLEAR",              NEON_YELLOW);
        JButton btnBack  = neonBtn("← BACK",                TEXT_DIM);

        btnClear.addActionListener(e -> { fName.setText(""); fEmail.setText(""); fTickets.setText(""); fName.requestFocus(); });
        btnBack .addActionListener(e -> cardLayout.show(mainContainer, "STAFF_HUB"));

        // making sure all fields are correct before going any further
        btnSave.addActionListener(e -> {
            String name = fName.getText().trim(), email = fEmail.getText().trim(), tRaw = fTickets.getText().trim();
            if (name.isEmpty() || email.isEmpty() || tRaw.isEmpty()) { err("ALL FIELDS REQUIRED."); return; }
            if (!name.matches(NAME_PATTERN))   { err("IDENTITY ERROR: Name must contain letters only."); return; }
            if (!email.matches(EMAIL_PATTERN)) { err("COMMS ERROR: Invalid email format."); return; }
            try {
                int t = Integer.parseInt(tRaw);
                if (t < 0) { err("TICKETS CANNOT BE NEGATIVE."); return; }
                Customer c = new Customer(); c.setName(name); c.setEmail(email); c.setTickets(t);
                new CustomerDAO().addCustomer(c);
                ok("CUSTOMER REGISTERED SUCCESSFULLY.");
                fName.setText(""); fEmail.setText(""); fTickets.setText("");
                cardLayout.show(mainContainer, "STAFF_HUB");
            } catch (NumberFormatException ex) { err("TICKETS MUST BE A WHOLE NUMBER.");
            } catch (Exception ex)             { err("SYSTEM ERROR: " + ex.getMessage()); }
        });

        p.add(row3(btnBack, btnClear, btnSave), BorderLayout.SOUTH);
        return p; // return the fully constructed panel
    }

    // staff edit — update pre-filled record
    private JPanel buildStaffEditScreen() {
        JPanel p = darkP(new BorderLayout(0, 20));
        p.setBorder(new EmptyBorder(60, 0, 60, 0));
        p.add(header("✎  EDIT CUSTOMER RECORD", NEON_PINK), BorderLayout.NORTH);

        JPanel card = cardP(new GridBagLayout());
        card.setBorder(new EmptyBorder(40, 80, 40, 80));
        GridBagConstraints g = gbcBase();
        row(card, g, "FULL NAME :", txtEditName,    0);
        row(card, g, "EMAIL :",     txtEditEmail,   1);
        row(card, g, "TICKETS :",   txtEditTickets, 2);
        p.add(centreOf(card), BorderLayout.CENTER);

        JButton btnSave   = neonBtn("✎  SAVE CHANGES", NEON_PINK);
        JButton btnCancel = neonBtn("← CANCEL",         TEXT_DIM);
        btnCancel.addActionListener(e -> cardLayout.show(mainContainer, "STAFF_VIEW"));

        btnSave.addActionListener(e -> {
            String name = txtEditName.getText().trim(), email = txtEditEmail.getText().trim(), tRaw = txtEditTickets.getText().trim();
            if (name.isEmpty() || email.isEmpty() || tRaw.isEmpty()) { err("ALL FIELDS REQUIRED."); return; }
            if (!name.matches(NAME_PATTERN))   { err("IDENTITY ERROR: Name must contain letters only."); return; }
            if (!email.matches(EMAIL_PATTERN)) { err("COMMS ERROR: Invalid email format."); return; }
            try {
                int t = Integer.parseInt(tRaw);
                if (t < 0) { err("TICKETS CANNOT BE NEGATIVE."); return; }
                Customer c = new Customer();
                c.setCustomerId(editingId); c.setName(name); c.setEmail(email); c.setTickets(t);
                new CustomerDAO().updateCustomer(c);
                ok("CUSTOMER RECORD UPDATED.");
                cardLayout.show(mainContainer, "STAFF_VIEW");
            } catch (NumberFormatException ex) { err("TICKETS MUST BE A WHOLE NUMBER.");
            } catch (Exception ex)             { err("SYSTEM ERROR: " + ex.getMessage()); }
        });

        JPanel footer = new JPanel(new GridLayout(1, 2, 20, 0));
        footer.setOpaque(false); footer.setBorder(new EmptyBorder(0, 200, 30, 200));
        footer.add(btnCancel); footer.add(btnSave);
        p.add(footer, BorderLayout.SOUTH);
        return p;
    }

    // staff delete — delete confirmation screen
    private JPanel buildStaffDeleteScreen() {
        JPanel p = darkP(new BorderLayout(0, 30));
        p.setBorder(new EmptyBorder(80, 120, 80, 120));
        p.add(header("✖  DELETE CUSTOMER", Color.RED), BorderLayout.NORTH);

        JPanel centre = new JPanel(new GridLayout(4, 1, 0, 20));
        centre.setOpaque(false);
        JLabel w1 = glowLbl("YOU ARE ABOUT TO PERMANENTLY DELETE:", TEXT_DIM, 16);
        w1.setHorizontalAlignment(SwingConstants.CENTER);
        lblStaffDeleteTarget.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel w2 = glowLbl("ALL TICKET DATA FOR THIS PLAYER WILL BE ERASED.", Color.RED, 13);
        w2.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel w3 = glowLbl("THIS ACTION CANNOT BE UNDONE.", NEON_PINK, 14);
        w3.setHorizontalAlignment(SwingConstants.CENTER);
        centre.add(w1); centre.add(lblStaffDeleteTarget); centre.add(w2); centre.add(w3);
        p.add(centre, BorderLayout.CENTER);

        JButton btnConfirm = neonBtn("✖  CONFIRM DELETE", Color.RED);
        JButton btnCancel  = neonBtn("← CANCEL",           TEXT_DIM);
        btnCancel.addActionListener(e -> cardLayout.show(mainContainer, "STAFF_VIEW"));
        btnConfirm.addActionListener(e -> {
            try {
                new CustomerDAO().deleteCustomer(editingId);
                ok("CUSTOMER PURGED FROM SYSTEM.");
                cardLayout.show(mainContainer, "STAFF_VIEW");
            } catch (Exception ex) { err("DELETE FAILED: " + ex.getMessage()); }
        });

        JPanel footer = new JPanel(new GridLayout(1, 2, 20, 0));
        footer.setOpaque(false); footer.setBorder(new EmptyBorder(0, 200, 0, 200));
        footer.add(btnCancel); footer.add(btnConfirm);
        p.add(footer, BorderLayout.SOUTH);
        return p;
    }

    // staff top-up — add tickets to selected customer
    private JPanel buildStaffTopUpScreen() {
        txtTopUpCustomer.setEditable(false);
        txtTopUpCustomer.setForeground(NEON_CYAN);

        JPanel p = darkP(new BorderLayout(0, 20));
        p.setBorder(new EmptyBorder(60, 0, 60, 0));
        p.add(header("⬆  TICKET TOP-UP", NEON_GREEN), BorderLayout.NORTH);

        JPanel card = cardP(new GridBagLayout());
        card.setBorder(new EmptyBorder(40, 80, 40, 80));
        GridBagConstraints g = gbcBase();
        row(card, g, "CUSTOMER :",   txtTopUpCustomer, 0);
        row(card, g, "ADD TICKETS :", txtTopUpAmount,   1);
        lblTopUpCurrent.setHorizontalAlignment(SwingConstants.CENTER);
        fullRow(card, g, lblTopUpCurrent, 2);
        p.add(centreOf(card), BorderLayout.CENTER);

        JButton btnApply  = neonBtn("⬆  APPLY TOP-UP", NEON_GREEN);
        JButton btnCancel = neonBtn("← CANCEL",          TEXT_DIM);
        btnCancel.addActionListener(e -> cardLayout.show(mainContainer, "STAFF_VIEW"));

        btnApply.addActionListener(e -> {
            String raw = txtTopUpAmount.getText().trim();
            if (raw.isEmpty()) { err("ENTER AN AMOUNT TO ADD."); return; }
            try {
                int add = Integer.parseInt(raw);
                if (add <= 0) { err("AMOUNT MUST BE GREATER THAN ZERO."); return; }
                Customer c = new CustomerDAO().getCustomerById(editingId);
                if (c == null) { err("CUSTOMER NOT FOUND."); return; }
                c.setTickets(c.getTickets() + add);
                new CustomerDAO().updateCustomer(c);
                ok("TOP-UP COMPLETE: +" + add + " tickets added.");
                cardLayout.show(mainContainer, "STAFF_VIEW");
            } catch (NumberFormatException ex) { err("AMOUNT MUST BE A WHOLE NUMBER.");
            } catch (Exception ex)             { err("TOP-UP FAILED: " + ex.getMessage()); }
        });

        JPanel footer = new JPanel(new GridLayout(1, 2, 20, 0));
        footer.setOpaque(false); footer.setBorder(new EmptyBorder(0, 200, 30, 200));
        footer.add(btnCancel); footer.add(btnApply);
        p.add(footer, BorderLayout.SOUTH);
        return p;
    }

    // customer register — create a new player account
    private JPanel buildCustomerRegister() {
        JPanel p = darkP(new BorderLayout(0, 20));
        p.setBorder(new EmptyBorder(60, 0, 60, 0));
        p.add(header("◉  NEW PLAYER UPLINK", NEON_CYAN), BorderLayout.NORTH);

        JPanel card = cardP(new GridBagLayout());
        card.setBorder(new EmptyBorder(40, 80, 40, 80));
        GridBagConstraints g = gbcBase();
        row(card, g, "FULL NAME :",        txtRegName,    0);
        row(card, g, "EMAIL :",             txtRegEmail,   1);
        row(card, g, "STARTING TICKETS :", txtRegTickets, 2);
        p.add(centreOf(card), BorderLayout.CENTER);

        JButton btnReg   = neonBtn("◉  REGISTER PLAYER", NEON_CYAN);
        JButton btnClear = neonBtn("⟳  CLEAR",           NEON_YELLOW);
        JButton btnBack  = neonBtn("← BACK",              TEXT_DIM);

        btnClear.addActionListener(e -> { txtRegName.setText(""); txtRegEmail.setText(""); txtRegTickets.setText(""); txtRegName.requestFocus(); });
        btnBack .addActionListener(e -> cardLayout.show(mainContainer, "SELECTION"));

        btnReg.addActionListener(e -> {
            String name = txtRegName.getText().trim(), email = txtRegEmail.getText().trim(), tRaw = txtRegTickets.getText().trim();
            if (name.isEmpty() || email.isEmpty() || tRaw.isEmpty()) { err("ALL FIELDS REQUIRED."); return; }
            if (!name.matches(NAME_PATTERN))   { err("IDENTITY ERROR: Name must contain letters only."); return; }
            if (!email.matches(EMAIL_PATTERN)) { err("COMMS ERROR: Invalid email format."); return; }
            try {
                int t = Integer.parseInt(tRaw);
                if (t < 0) { err("TICKETS CANNOT BE NEGATIVE."); return; }
                Customer c = new Customer(); c.setName(name); c.setEmail(email); c.setTickets(t);
                new CustomerDAO().addCustomer(c);
                txtRegName.setText(""); txtRegEmail.setText(""); txtRegTickets.setText("");
                cardLayout.show(mainContainer, "CUSTOMER_WELCOME");
            } catch (NumberFormatException ex) { err("TICKETS MUST BE A WHOLE NUMBER.");
            } catch (Exception ex)             { err("SYSTEM ERROR: " + ex.getMessage()); }
        });

        p.add(row3(btnBack, btnClear, btnReg), BorderLayout.SOUTH);
        return p;
    }

    // customer login — look up by email (read to authenticate)
    private JPanel buildCustomerLogin() {
        JPanel p = darkP(new BorderLayout(0, 20));
        p.setBorder(new EmptyBorder(80, 0, 80, 0));
        p.add(header("◉  PLAYER AUTHENTICATION", NEON_CYAN), BorderLayout.NORTH);

        JPanel card = cardP(new GridBagLayout());
        card.setBorder(new EmptyBorder(50, 90, 50, 90));
        GridBagConstraints g = gbcBase();
        row(card, g, "EMAIL :", txtCustEmail, 0);
        JLabel hint = glowLbl("Enter your registered email to access your player account.", TEXT_DIM, 12);
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        fullRow(card, g, hint, 1);
        p.add(centreOf(card), BorderLayout.CENTER);

        JButton btnEnter = neonBtn("◉  ACCESS SYSTEM", NEON_CYAN);
        JButton btnClear = neonBtn("⟳  CLEAR",          NEON_YELLOW);
        JButton btnBack  = neonBtn("← BACK",             TEXT_DIM);

        btnClear.addActionListener(e -> txtCustEmail.setText(""));
        btnBack .addActionListener(e -> cardLayout.show(mainContainer, "SELECTION"));

        btnEnter.addActionListener(e -> {
            String email = txtCustEmail.getText().trim();
            if (email.isEmpty()) { err("EMAIL FIELD REQUIRED."); return; }
            try {
                Customer found = new CustomerDAO().getCustomerByEmail(email);
                if (found != null) {
                    loggedInId = found.getCustomerId();
                    lblHubWelcome.setText("WELCOME BACK,  " + found.getName().toUpperCase());
                    lblBalanceVal.setText(String.valueOf(found.getTickets()));
                    txtCustEmail.setText("");
                    cardLayout.show(mainContainer, "CUSTOMER_HUB");
                } else { err("ACCESS DENIED: No account found for that email."); }
            } catch (Exception ex) { err("CONNECTION ERROR: " + ex.getMessage()); }
        });

        p.add(row3(btnBack, btnClear, btnEnter), BorderLayout.SOUTH);
        return p;
    }

    // customer hub — player main menu
    private JPanel buildCustomerHub() {
        JPanel p = darkP(new BorderLayout(0, 20));
        p.setBorder(new EmptyBorder(50, 80, 50, 80));

        JPanel titleBlock = new JPanel(new GridLayout(2, 1, 0, 6));
        titleBlock.setOpaque(false);
        JLabel hub = glowLbl("◉  PLAYER HUB", NEON_CYAN, 38);
        hub.setHorizontalAlignment(SwingConstants.CENTER);
        lblHubWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        titleBlock.add(hub); titleBlock.add(lblHubWelcome);
        p.add(titleBlock, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 30, 30));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(30, 0, 30, 0));

        JButton btnBal    = bigCard("◎  TICKET BALANCE", "View Your Current Ticket Total",       NEON_CYAN);
        JButton btnPrizes = bigCard("🎁  PRIZE SHOP",     "Redeem Tickets  ·  Browse Prizes",     NEON_YELLOW);
        JButton btnEdit   = bigCard("✎  EDIT PROFILE",   "Update Your Name or Email",            NEON_PURPLE);
        JButton btnDel    = bigCard("✖  DELETE ACCOUNT", "Permanently Remove Your Account",      Color.RED);

        btnBal.addActionListener(e -> { reloadBalance(); cardLayout.show(mainContainer, "CUST_BALANCE"); });

        btnPrizes.addActionListener(e -> { reloadBalance(); cardLayout.show(mainContainer, "CUST_PRIZES"); });

        btnEdit.addActionListener(e -> {
            try {
                Customer c = new CustomerDAO().getCustomerById(loggedInId);
                if (c != null) { txtCustEditName.setText(c.getName()); txtCustEditEmail.setText(c.getEmail()); }
            } catch (Exception ex) { err("LOAD ERROR: " + ex.getMessage()); return; }
            cardLayout.show(mainContainer, "CUST_EDIT");
        });

        btnDel.addActionListener(e -> cardLayout.show(mainContainer, "CUST_DELETE"));

        grid.add(btnBal); grid.add(btnPrizes); grid.add(btnEdit); grid.add(btnDel);
        p.add(grid, BorderLayout.CENTER);

        JButton btnLogout = neonBtn("⏻  LOG OUT", TEXT_DIM);
        btnLogout.addActionListener(e -> { loggedInId = -1; cardLayout.show(mainContainer, "SELECTION"); });
        p.add(centreOf(btnLogout), BorderLayout.SOUTH);
        return p;
    }

    // customer balance — read: big glowing ticket display
    private JPanel buildCustBalanceScreen() {
        JPanel p = darkP(new BorderLayout(0, 20));
        p.setBorder(new EmptyBorder(60, 120, 60, 120));
        p.add(header("◎  YOUR TICKET BALANCE", NEON_CYAN), BorderLayout.NORTH);

        JPanel display = cardP(new BorderLayout(0, 12));
        display.setBorder(new EmptyBorder(60, 100, 60, 100));

        JLabel tagTop  = glowLbl("CURRENT BALANCE", TEXT_DIM, 14);
        tagTop.setHorizontalAlignment(SwingConstants.CENTER);
        lblBalanceVal.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel tagBot = glowLbl("TICKETS", NEON_PURPLE, 22);
        tagBot.setHorizontalAlignment(SwingConstants.CENTER);

        display.add(tagTop,       BorderLayout.NORTH);
        display.add(lblBalanceVal, BorderLayout.CENTER);
        display.add(tagBot,       BorderLayout.SOUTH);
        p.add(display, BorderLayout.CENTER);

        JButton btnBack = neonBtn("← BACK TO HUB", NEON_CYAN);
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "CUSTOMER_HUB"));

        p.addComponentListener(new ComponentAdapter() {
            @Override public void componentShown(ComponentEvent e) { reloadBalance(); }
        });
        p.add(centreOf(btnBack), BorderLayout.SOUTH);
        return p;
    }

    // customer edit — update OWN name / email
    private JPanel buildCustEditScreen() {
        JPanel p = darkP(new BorderLayout(0, 20));
        p.setBorder(new EmptyBorder(60, 0, 60, 0));
        p.add(header("✎  EDIT YOUR PROFILE", NEON_PURPLE), BorderLayout.NORTH);

        JPanel card = cardP(new GridBagLayout());
        card.setBorder(new EmptyBorder(40, 80, 40, 80));
        GridBagConstraints g = gbcBase();
        row(card, g, "FULL NAME :", txtCustEditName,  0);
        row(card, g, "EMAIL :",     txtCustEditEmail, 1);
        p.add(centreOf(card), BorderLayout.CENTER);

        JButton btnSave   = neonBtn("✎  SAVE CHANGES", NEON_PURPLE);
        JButton btnCancel = neonBtn("← CANCEL",         TEXT_DIM);
        btnCancel.addActionListener(e -> cardLayout.show(mainContainer, "CUSTOMER_HUB"));

        btnSave.addActionListener(e -> {
            String name = txtCustEditName.getText().trim(), email = txtCustEditEmail.getText().trim();
            if (name.isEmpty() || email.isEmpty()) { err("BOTH FIELDS REQUIRED."); return; }
            if (!name.matches(NAME_PATTERN))   { err("IDENTITY ERROR: Name must contain letters only."); return; }
            if (!email.matches(EMAIL_PATTERN)) { err("COMMS ERROR: Invalid email format."); return; }
            try {
                Customer c = new CustomerDAO().getCustomerById(loggedInId);
                if (c == null) { err("SESSION EXPIRED. PLEASE LOG IN AGAIN."); cardLayout.show(mainContainer, "SELECTION"); return; }
                c.setName(name); c.setEmail(email);
                new CustomerDAO().updateCustomer(c);
                lblHubWelcome.setText("WELCOME BACK,  " + name.toUpperCase());
                ok("PROFILE UPDATED SUCCESSFULLY.");
                cardLayout.show(mainContainer, "CUSTOMER_HUB");
            } catch (Exception ex) { err("UPDATE FAILED: " + ex.getMessage()); }
        });

        JPanel footer = new JPanel(new GridLayout(1, 2, 20, 0));
        footer.setOpaque(false); footer.setBorder(new EmptyBorder(0, 200, 30, 200));
        footer.add(btnCancel); footer.add(btnSave);
        p.add(footer, BorderLayout.SOUTH);
        return p;
    }


    // customer delete — delete OWN account confirmation
    private JPanel buildCustDeleteScreen() {
        JPanel p = darkP(new BorderLayout(0, 30));
        p.setBorder(new EmptyBorder(80, 120, 80, 120));
        p.add(header("✖  DELETE YOUR ACCOUNT", Color.RED), BorderLayout.NORTH);

        JPanel centre = new JPanel(new GridLayout(3, 1, 0, 20));
        centre.setOpaque(false);
        JLabel l1 = glowLbl("WARNING: THIS WILL PERMANENTLY DELETE YOUR ACCOUNT.", Color.RED, 18);
        l1.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel l2 = glowLbl("ALL YOUR TICKETS AND HISTORY WILL BE ERASED.", TEXT_DIM, 14);
        l2.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel l3 = glowLbl("ARE YOU SURE YOU WANT TO PROCEED?", NEON_PINK, 16);
        l3.setHorizontalAlignment(SwingConstants.CENTER);
        centre.add(l1); centre.add(l2); centre.add(l3);
        p.add(centre, BorderLayout.CENTER);

        JButton btnConfirm = neonBtn("✖  YES, DELETE MY ACCOUNT", Color.RED);
        JButton btnCancel  = neonBtn("← NO, TAKE ME BACK",         NEON_CYAN);
        btnCancel.addActionListener(e -> cardLayout.show(mainContainer, "CUSTOMER_HUB"));
        btnConfirm.addActionListener(e -> {
            try {
                new CustomerDAO().deleteCustomer(loggedInId);
                loggedInId = -1;
                ok("ACCOUNT DELETED. GOODBYE, PLAYER.");
                cardLayout.show(mainContainer, "SELECTION");
            } catch (Exception ex) { err("DELETE FAILED: " + ex.getMessage()); }
        });

        JPanel footer = new JPanel(new GridLayout(1, 2, 20, 0));
        footer.setOpaque(false); footer.setBorder(new EmptyBorder(0, 150, 0, 150));
        footer.add(btnCancel); footer.add(btnConfirm);
        p.add(footer, BorderLayout.SOUTH);
        return p;
    }


    // customer welcome — registration success
    private JPanel buildCustomerWelcome() {
        JPanel p = darkP(new BorderLayout(0, 30));
        p.setBorder(new EmptyBorder(100, 120, 100, 120));

        JPanel centre = new JPanel(new GridLayout(4, 1, 0, 18));
        centre.setOpaque(false);
        JLabel l1 = glowLbl("◉  REGISTRATION COMPLETE", NEON_GREEN, 42);
        l1.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel l2 = glowLbl("WELCOME TO THE GRID, PLAYER.", NEON_CYAN, 22);
        l2.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel l3 = glowLbl("Your account is live. Login to view your balance.", TEXT_DIM, 14);
        l3.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel l4 = glowLbl("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓", NEON_GREEN, 12);
        l4.setHorizontalAlignment(SwingConstants.CENTER);
        centre.add(l1); centre.add(l2); centre.add(l3); centre.add(l4);
        p.add(centre, BorderLayout.CENTER);

        JButton btnLogin = neonBtn("◉  LOGIN NOW",        NEON_CYAN);
        JButton btnHome  = neonBtn("⌂  RETURN TO START",  TEXT_DIM);
        btnLogin.addActionListener(e -> cardLayout.show(mainContainer, "CUSTOMER_LOGIN"));
        btnHome .addActionListener(e -> cardLayout.show(mainContainer, "SELECTION"));

        JPanel footer = new JPanel(new GridLayout(1, 2, 20, 0));
        footer.setOpaque(false); footer.setBorder(new EmptyBorder(0, 200, 0, 200));
        footer.add(btnHome); footer.add(btnLogin);
        p.add(footer, BorderLayout.SOUTH);
        return p;
    }



    // ── prizes screen ─────────────────────────────────────────────────────────
    private JPanel buildPrizesScreen() {

        // Prize data: {name, ticketCost, symbol}
        Object[][] prizes = {
                {"Bouncy Ball",        50,   "◉"},
                {"Temporary Tattoo",   100,  "✦"},
                {"Sticker Pack",       150,  "★"},
                {"Mini Notepad",       200,  "◈"},
                {"Pencil Set",         300,  "✏"},
                {"Keyring",            400,  "⬡"},
                {"Playing Cards",      600,  "♠"},
                {"Puzzle Cube",        800,  "⬢"},
                {"Bluetooth Speaker", 1500,  "♫"},
                {"Arcade Plushie",    2500,  "◆"},
        };

        JLabel lblPrizeBalance = glowLbl("--", NEON_YELLOW, 26);

        JPanel p = darkP(new BorderLayout(0, 10));
        p.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel topBlock = new JPanel(new BorderLayout(0, 4));
        topBlock.setOpaque(false);
        topBlock.add(header("PRIZE SHOP", NEON_YELLOW), BorderLayout.NORTH);
        JPanel balStrip = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
        balStrip.setOpaque(false);
        balStrip.add(glowLbl("YOUR BALANCE:", TEXT_DIM, 13));
        balStrip.add(lblPrizeBalance);
        balStrip.add(glowLbl("TICKETS", NEON_YELLOW, 13));
        topBlock.add(balStrip, BorderLayout.SOUTH);
        p.add(topBlock, BorderLayout.NORTH);

        // prizes grid - 2 rows x 5 cols
        JPanel grid = new JPanel(new GridLayout(2, 5, 16, 16));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(10, 0, 10, 0));

        for (int prizeIndex = 0; prizeIndex < prizes.length; prizeIndex++) {
            Object[] prize    = prizes[prizeIndex];
            String prizeName  = (String) prize[0];
            int    prizeCost  = (int)    prize[1];
            String prizeSym   = (String) prize[2];
            final  int prizeId = prizeIndex + 1;  // 1-based, matches prize table

            JPanel card = new JPanel(new BorderLayout(0, 6));
            card.setBackground(BG_CARD);
            card.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(NEON_YELLOW, 1, true),
                    new EmptyBorder(14, 10, 14, 10)
            ));

            JLabel lSym = glowLbl(prizeSym, NEON_YELLOW, 28);
            lSym.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel lName = glowLbl(prizeName, TEXT_WHITE, 13);
            lName.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel lCost = glowLbl(prizeCost + " TICKETS", NEON_CYAN, 12);
            lCost.setHorizontalAlignment(SwingConstants.CENTER);

            JButton btnBuy = neonBtn("REDEEM", NEON_YELLOW);
            btnBuy.setFont(new Font("Monospaced", Font.BOLD, 11));

            btnBuy.addActionListener(e -> {
                try {
                    Customer c = new CustomerDAO().getCustomerById(loggedInId);
                    if (c == null) { err("SESSION EXPIRED. PLEASE LOG IN AGAIN."); cardLayout.show(mainContainer, "SELECTION"); return; }
                    if (c.getTickets() < prizeCost) {
                        err("INSUFFICIENT TICKETS!\n\nYou need " + prizeCost + " tickets but only have " + c.getTickets() + ".\nKeep playing to earn more!");
                        return;
                    }
                    UIManager.put("OptionPane.background",        BG_DARK);
                    UIManager.put("Panel.background",             BG_DARK);
                    UIManager.put("OptionPane.messageForeground", NEON_YELLOW);
                    int confirm = JOptionPane.showConfirmDialog(CustomerGUI.this,
                            "Redeem  \"" + prizeName + "\"  for  " + prizeCost + " tickets?",
                            "CONFIRM REDEMPTION", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (confirm != JOptionPane.YES_OPTION) return;

                    c.setTickets(c.getTickets() - prizeCost);
                    new CustomerDAO().updateCustomer(c);
                    new PurchaseDAO().addPurchase(loggedInId, prizeId);

                    String code = "ARC-"
                            + Integer.toHexString(loggedInId).toUpperCase()
                            + "-"
                            + Long.toHexString(System.currentTimeMillis() % 0xFFFFFL).toUpperCase()
                            + "-"
                            + (char)('A' + (int)(Math.random() * 26))
                            + (char)('A' + (int)(Math.random() * 26));

                    lblPrizeBalance.setText(String.valueOf(c.getTickets()));
                    lblBalanceVal.setText(String.valueOf(c.getTickets()));

                    UIManager.put("OptionPane.background",        BG_DARK);
                    UIManager.put("Panel.background",             BG_DARK);
                    UIManager.put("OptionPane.messageForeground", NEON_GREEN);
                    JOptionPane.showMessageDialog(CustomerGUI.this,
                            "REDEMPTION SUCCESSFUL!\n\n"
                                    + "PRIZE        : " + prizeName + "\n"
                                    + "TICKETS USED : " + prizeCost + "\n"
                                    + "NEW BALANCE  : " + c.getTickets() + " tickets\n\n"
                                    + "REDEMPTION CODE :  " + code + "\n\n"
                                    + "Present this code at any arcade counter\nto collect your prize.",
                            "PRIZE REDEEMED", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception ex) { err("REDEMPTION FAILED: " + ex.getMessage()); }
            });

            JPanel textBlock = new JPanel(new GridLayout(2, 1, 0, 3));
            textBlock.setOpaque(false);
            textBlock.add(lName);
            textBlock.add(lCost);

            card.add(lSym,      BorderLayout.NORTH);
            card.add(textBlock, BorderLayout.CENTER);
            card.add(btnBuy,    BorderLayout.SOUTH);
            grid.add(card);
        }

        p.add(grid, BorderLayout.CENTER);

        JButton btnBack = neonBtn("\u2190 BACK TO HUB", TEXT_DIM);
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "CUSTOMER_HUB"));
        p.add(centreOf(btnBack), BorderLayout.SOUTH);

        p.addComponentListener(new ComponentAdapter() {
            @Override public void componentShown(ComponentEvent e) {
                try {
                    Customer c = new CustomerDAO().getCustomerById(loggedInId);
                    if (c != null) lblPrizeBalance.setText(String.valueOf(c.getTickets()));
                } catch (Exception ex) { lblPrizeBalance.setText("ERR"); }
            }
        });

        return p;
    }


    // data helpers
    private void refreshTable() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        try {
            for (Customer c : new CustomerDAO().getAllCustomers())
                tableModel.addRow(new Object[]{c.getCustomerId(), c.getName(), c.getEmail(), c.getTickets()});
        } catch (Exception ex) { err("FAILED TO LOAD DATA: " + ex.getMessage()); }
    }

    private void filterTable(String q) {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        try {
            for (Customer c : new CustomerDAO().getAllCustomers())
                if (c.getName().toLowerCase().contains(q) || c.getEmail().toLowerCase().contains(q))
                    tableModel.addRow(new Object[]{c.getCustomerId(), c.getName(), c.getEmail(), c.getTickets()});
        } catch (Exception ex) { err("SEARCH ERROR: " + ex.getMessage()); }
    }

    private void reloadBalance() {
        try {
            Customer c = new CustomerDAO().getCustomerById(loggedInId);
            if (c != null) lblBalanceVal.setText(String.valueOf(c.getTickets()));
        } catch (Exception ex) { lblBalanceVal.setText("ERR"); }
    }


    // user interface factory helpers
    private JPanel darkP(LayoutManager lm)  { JPanel p = new JPanel(lm); p.setBackground(BG_DARK);  return p; }
    private JPanel cardP(LayoutManager lm)  { JPanel p = new JPanel(lm); p.setBackground(BG_PANEL); p.setBorder(new LineBorder(NEON_CYAN, 1)); return p; }

    private JLabel glowLbl(String t, Color c, int size) {
        JLabel l = new JLabel(t); l.setForeground(c); l.setFont(new Font("Monospaced", Font.BOLD, size)); return l;
    }

    private JLabel header(String t, Color c) {
        JLabel l = glowLbl(t, c, 28); l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setBorder(new MatteBorder(0, 0, 2, 0, c)); return l;
    }

    private JButton bigCard(String title, String sub, Color col) {
        String html = "<html><center><b>" + title + "</b><br><font color='#9999bb'>" + sub + "</font></center></html>";
        JButton b = new JButton(html);
        b.setFont(new Font("Monospaced", Font.BOLD, 15));
        b.setBackground(BG_CARD); b.setForeground(TEXT_WHITE);
        b.setFocusPainted(false); b.setBorder(new LineBorder(col, 2, true));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(col);    b.setForeground(BG_DARK); }
            public void mouseExited (MouseEvent e) { b.setBackground(BG_CARD); b.setForeground(TEXT_WHITE); }
        });
        return b;
    }

    private JButton neonBtn(String text, Color col) {
        JButton b = new JButton(text);
        b.setFont(new Font("Monospaced", Font.BOLD, 13));
        b.setBackground(BG_DARK); b.setForeground(col);
        b.setFocusPainted(false); b.setBorder(new LineBorder(col, 2));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(col);   b.setForeground(BG_DARK); }
            public void mouseExited (MouseEvent e) { b.setBackground(BG_DARK); b.setForeground(col); }
        });
        return b;
    }

    private JTextField mkField(Color col) {
        JTextField f = new JTextField(22);
        f.setBackground(new Color(22, 22, 40)); f.setForeground(TEXT_WHITE); f.setCaretColor(col);
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(col, 1), new EmptyBorder(4, 8, 4, 8)));
        f.setFont(new Font("SansSerif", Font.PLAIN, 14)); return f;
    }

    private JPasswordField mkPass(Color col) {
        JPasswordField f = new JPasswordField(22);
        f.setBackground(new Color(22, 22, 40)); f.setForeground(TEXT_WHITE); f.setCaretColor(col);
        f.setBorder(BorderFactory.createCompoundBorder(new LineBorder(col, 1), new EmptyBorder(4, 8, 4, 8)));
        f.setFont(new Font("SansSerif", Font.PLAIN, 14)); return f;
    }

    private GridBagConstraints gbcBase() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(12, 10, 12, 10); g.fill = GridBagConstraints.HORIZONTAL; return g;
    }

    private void row(JPanel p, GridBagConstraints g, String lbl, JComponent field, int y) {
        JLabel l = glowLbl(lbl, TEXT_DIM, 12);
        g.gridx = 0; g.gridy = y; g.weightx = 0; p.add(l, g);
        g.gridx = 1;              g.weightx = 1; p.add(field, g);
    }

    private void fullRow(JPanel p, GridBagConstraints g, JComponent comp, int y) {
        g.gridx = 0; g.gridy = y; g.gridwidth = 2; g.weightx = 1; p.add(comp, g); g.gridwidth = 1;
    }

    private JPanel row3(JButton b1, JButton b2, JButton b3) {
        JPanel row = new JPanel(new GridLayout(1, 3, 20, 0));
        row.setOpaque(false); row.setBorder(new EmptyBorder(10, 120, 30, 120));
        row.add(b1); row.add(b2); row.add(b3); return row;
    }

    private JPanel centreOf(JComponent c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER)); p.setOpaque(false); p.add(c); return p;
    }

    private void styleTable(JTable t) {
        t.setBackground(BG_CARD); t.setForeground(TEXT_WHITE);
        t.setGridColor(new Color(50, 50, 80));
        t.setFont(new Font("Monospaced", Font.PLAIN, 13)); t.setRowHeight(30);
        t.setSelectionBackground(NEON_PINK); t.setSelectionForeground(BG_DARK);
        t.getTableHeader().setBackground(BG_PANEL);
        t.getTableHeader().setForeground(NEON_CYAN);
        t.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 13));
        DefaultTableCellRenderer ctr = new DefaultTableCellRenderer();
        ctr.setHorizontalAlignment(SwingConstants.CENTER);
        t.getColumnModel().getColumn(0).setCellRenderer(ctr);
        t.getColumnModel().getColumn(3).setCellRenderer(ctr);
    }

    // ── dialogs ───────────────────────────────────────────────────────────────
    private void err(String msg) {
        UIManager.put("OptionPane.background", BG_DARK); UIManager.put("Panel.background", BG_DARK);
        UIManager.put("OptionPane.messageForeground", NEON_PINK);
        JOptionPane.showMessageDialog(this, msg, "SYSTEM ERROR", JOptionPane.ERROR_MESSAGE);
    }
    private void ok(String msg) {
        UIManager.put("OptionPane.background", BG_DARK); UIManager.put("Panel.background", BG_DARK);
        UIManager.put("OptionPane.messageForeground", NEON_CYAN);
        JOptionPane.showMessageDialog(this, msg, "SYSTEM OK", JOptionPane.INFORMATION_MESSAGE);
    }
    private void info(String msg) {
        UIManager.put("OptionPane.background", BG_DARK); UIManager.put("Panel.background", BG_DARK);
        UIManager.put("OptionPane.messageForeground", NEON_YELLOW);
        JOptionPane.showMessageDialog(this, msg, "INFO", JOptionPane.INFORMATION_MESSAGE);
    }

    // main part
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerGUI().setVisible(true));
    }
}
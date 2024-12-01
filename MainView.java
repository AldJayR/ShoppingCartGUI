import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainView extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private CartView cartView;
    private JLabel cartCountLabel;
    private static final Color HEADER_COLOR = new Color(255, 255, 255);
    private static final Color FOOTER_COLOR = new Color(50, 50, 50); // Dark footer color
    private static final Color ACCENT_COLOR = new Color(0, 122, 255); // Accent color (blue)
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FOOTER_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    public MainView() {
        setTitle("Fowlers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        ArrayList<Product> products = new ArrayList<>();
        initProducts(products);

        // Create Views
        ProductView productView = new ProductView(products, this::refreshCartView);
        cartView = new CartView();
        CheckoutView checkoutView = new CheckoutView();

        // Add views to card layout
        mainPanel.add(productView, "ProductView");
        mainPanel.add(cartView, "CartView");
        mainPanel.add(checkoutView, "CheckoutView");

        // Create Header/Navbar
        JPanel headerPanel = createHeaderPanel();

        // Create Footer (without being fixed)
        JPanel footerPanel = createFooterPanel();

        // Add components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Show ProductView by default
        cardLayout.show(mainPanel, "ProductView");
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBackground(FOOTER_COLOR);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Footer content (you can add links, info, etc.)
        JLabel footerLabel = new JLabel("© 2024 Fowlers - All Rights Reserved");
        footerLabel.setFont(FOOTER_FONT);
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        footerPanel.add(footerLabel, BorderLayout.CENTER);
        return footerPanel;
    }

    private void initProducts(ArrayList<Product> products) {
        products.add(new Product("Laptop", 45000.00, "Electronics", "laptop.png"));
        products.add(new Product("Smartphone", 15000.00, "Electronics", "smartphone.png"));
        products.add(new Product("Headphones", 3500.00, "Electronics", "headphones.png"));
        products.add(new Product("Smartwatch", 6000.00, "Electronics", "smartwatch.png"));
        products.add(new Product("Tablet", 12000.00, "Electronics", "tablet.png"));
        products.add(new Product("Keyboard", 2000.00, "Electronics", "keyboard.png"));

        products.add(new Product("Lipstick", 500.00, "Makeup", "lipstick.png"));
        products.add(new Product("Mascara", 700.00, "Makeup", "mascara.png"));
        products.add(new Product("Foundation", 1000.00, "Makeup", "foundation.png"));
        products.add(new Product("Blush", 600.00, "Makeup", "blush.png"));
        products.add(new Product("Eyeliner", 400.00, "Makeup", "eyeliner.png"));
        products.add(new Product("Concealer", 450.00, "Makeup", "concealer.png"));

        products.add(new Product("Blender", 2000.00, "Home Appliances", "blender.png"));
        products.add(new Product("Microwave", 3500.00, "Home Appliances", "microwave.png"));
        products.add(new Product("Toaster", 1500.00, "Home Appliances", "toaster.png"));
        products.add(new Product("Vacuum Cleaner", 6000.00, "Home Appliances", "vacuum.png"));
        products.add(new Product("Air Purifier", 8000.00, "Home Appliances", "purifier.png"));
        products.add(new Product("Induction Cooker", 7000.00, "Home Appliances", "cooker.png"));

        products.add(new Product("Hammer", 250.00, "Hardware", "hammer.png"));
        products.add(new Product("Screwdriver Set", 500.00, "Hardware", "screwdriver.png"));
        products.add(new Product("Drill", 4000.00, "Hardware", "drill.png"));
        products.add(new Product("Wrench Set", 800.00, "Hardware", "wrench.png"));
        products.add(new Product("Tape Measure", 200.00, "Hardware", "measure.png"));
        products.add(new Product("Staple Gun", 300.00, "Hardware", "staplegun.png"));

        products.add(new Product("Notebook", 50.00, "School", "notebook.png"));
        products.add(new Product("Pen", 15.00, "School", "pen.png"));
        products.add(new Product("Pencil", 10.00, "School", "pencil.png"));
        products.add(new Product("Ruler", 30.00, "School", "ruler.png"));
        products.add(new Product("Backpack", 1500.00, "School", "backpack.png"));
        products.add(new Product("Highlighter", 100.00, "School", "highlighter.png"));
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Create a gradient background (top to bottom)
                GradientPaint gradient = new GradientPaint(0, 0, new Color(0, 122, 255), 0, getHeight(), new Color(0, 150, 255));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 20, 10, 20)));

        // Logo/Brand on the left
        JLabel brandLabel = new JLabel("Fowlers");
        brandLabel.setFont(HEADER_FONT);
        brandLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        brandLabel.setForeground(HEADER_COLOR);
        brandLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(mainPanel, "ProductView");
            }
        });

        // Cart button on the right
        JButton cartButton = new JButton("Go to Cart");
        cartButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cartButton.setForeground(Color.WHITE);
        cartButton.setBackground(ACCENT_COLOR);
        cartButton.setBorderPainted(false);
        cartButton.setFocusPainted(false);
        cartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect to cart button
        cartButton.addMouseListener(new java.awt.event.MouseAdapter() 
        {
            public void mouseEntered(java.awt.event.MouseEvent evt) 
            {
                cartButton.setBackground(ACCENT_COLOR.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) 
            {
                cartButton.setBackground(ACCENT_COLOR);
            }

            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                cardLayout.show(mainPanel, "CartView");
            }
        });

        headerPanel.add(brandLabel, BorderLayout.WEST);
        headerPanel.add(cartButton, BorderLayout.EAST);
        return headerPanel;
    }

    private void addToCart(Product product) {
        Cart cart = Cart.getInstance();
        CartItem existingItem = cart.getItems()
            .stream()
            .filter(item -> item.getProduct().equals(product))
            .findFirst()
            .orElse(null);

        if (existingItem == null) {
            cart.addProduct(new CartItem(product, 1));
        } else {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        }

        refreshCartView();
    }

    private void refreshCartView() {
        cartView.refreshView();
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            MainView mainView = new MainView();
            mainView.setExtendedState(JFrame.MAXIMIZED_BOTH);
            mainView.setVisible(true); // Make the window visible
        });
    }
}

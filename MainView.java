import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;

public class MainView extends JFrame
{
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private CartView cartView;
    private static final Color HEADER_COLOR = new Color(255, 255, 255);
    private static final Color ACCENT_COLOR = new Color(0, 122, 255);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);

    public MainView()
    {
        setTitle("Fowlers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);

        // Initialize layout
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        // Create dummy products
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Laptop", 999.99, "Electronics", ""));
        products.add(new Product("Smartphone", 499.99, "Electronics", ""));
        products.add(new Product("Headphones", 199.99, "Electronics", ""));

        // Create Views
        ProductView productView = new ProductView(products, this::refreshCartView);  // Pass method to handle adding to cart
        cartView = new CartView();

        // Add views to card layout
        mainPanel.add(productView, "ProductView");
        mainPanel.add(cartView, "CartView");

        // Create Header/Navbar
        JPanel headerPanel = createHeaderPanel();

        // Add components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Show ProductView by default
        cardLayout.show(mainPanel, "ProductView");
    }

    private void addToCart(Product product)
    {
        Cart cart = Cart.getInstance();
        // Check if the product is already in the cart
        CartItem existingItem = cart.getItems().stream()
            .filter(item -> item.getProduct().equals(product))
            .findFirst()
            .orElse(null);

        if (existingItem == null)
        {
            // If the product isn't in the cart, create a new CartItem and add it to the cart
            cart.addProduct(new CartItem(product, 1));
        }
        else
        {
            // If the product is already in the cart, increase the quantity
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        }

        // Refresh the cart view and header info
        refreshCartView();
    }

    private JPanel createHeaderPanel()
    {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            new EmptyBorder(10, 20, 10, 20)
        ));

        // Logo/Brand on the left
        JLabel brandLabel = new JLabel("Fowlers");
        brandLabel.setFont(HEADER_FONT);
        brandLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        cartButton.addActionListener(e -> cardLayout.show(mainPanel, "CartView"));

        // Add hover effect to cart button
        cartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cartButton.setBackground(ACCENT_COLOR.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                cartButton.setBackground(ACCENT_COLOR);
            }
        });

        headerPanel.add(brandLabel, BorderLayout.WEST);
        headerPanel.add(cartButton, BorderLayout.EAST);

        // Add cart item count indicator
        JPanel cartInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cartInfoPanel.setBackground(HEADER_COLOR);
        
        JLabel cartCountLabel = new JLabel("(" + getCartItemCount() + " items)");
        cartCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cartCountLabel.setForeground(Color.GRAY);
        cartInfoPanel.add(cartCountLabel);
        cartInfoPanel.add(Box.createHorizontalStrut(10));
        cartInfoPanel.add(cartButton);

        headerPanel.add(cartInfoPanel, BorderLayout.EAST);

        return headerPanel;
    }

    // Get total cart item count (including quantity)
    private int getCartItemCount()
    {
        return Cart.getInstance().getItems().stream()
                   .mapToInt(CartItem::getQuantity)
                   .sum(); // Sum the quantities of all items in the cart
    }

    private void refreshCartView()
    {
        cartView.refreshView();
        mainPanel.revalidate();
        mainPanel.repaint();

        // Update cart count in header
        SwingUtilities.invokeLater(() -> {
            Component headerPanel = getContentPane().getComponent(0);
            if (headerPanel instanceof JPanel) {
                Component cartInfoPanel = ((JPanel) headerPanel).getComponent(2);
                if (cartInfoPanel instanceof JPanel) {
                    JLabel cartCountLabel = (JLabel) ((JPanel) cartInfoPanel).getComponent(0);
                    cartCountLabel.setText("(" + getCartItemCount() + " items)");
                }
            }
        });
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}

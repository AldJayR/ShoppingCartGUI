import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;

public class ProductView extends JPanel
{
    private java.util.List<Product> products;
    private Runnable refreshCartViewCallback;
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 247);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(0, 122, 255);
    private static final Color TEXT_COLOR = new Color(29, 29, 31);

    public ProductView(java.util.List<Product> products, Runnable refreshCartViewCallback)
    {
        this.products = products;
        this.refreshCartViewCallback = refreshCartViewCallback;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Group products by category
        Map<String, java.util.List<Product>> productsByCategory = new HashMap<>();
        for (Product product : products)
        {
            productsByCategory.computeIfAbsent(product.getCategory(), k -> new ArrayList<>()).add(product);
        }

        // Create a panel for each category
        productsByCategory.forEach((category, categoryProducts) ->
        {
            add(createCategoryPanel(category, categoryProducts));
            add(Box.createVerticalStrut(30)); // Add spacing between categories
        });
    }

    private JPanel createCategoryPanel(String category, java.util.List<Product> categoryProducts)
    {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setBackground(BACKGROUND_COLOR);
        categoryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Category header
        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        categoryLabel.setForeground(TEXT_COLOR);
        categoryLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        categoryPanel.add(categoryLabel);

        // Create grid layout for products
        JPanel productGridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        productGridPanel.setBackground(BACKGROUND_COLOR);

        categoryProducts.forEach(product -> productGridPanel.add(createProductCard(product)));
        categoryPanel.add(productGridPanel);

        return categoryPanel;
    }

private JPanel createProductCard(Product product)
{
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBackground(CARD_COLOR);
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
        new EmptyBorder(15, 15, 15, 15)
    ));

    // Product name
    JLabel nameLabel = new JLabel(product.getName());
    nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
    nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    nameLabel.setForeground(TEXT_COLOR);

    // Price
    JLabel priceLabel = new JLabel(String.format("$%.2f", product.getPrice()));
    priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    priceLabel.setForeground(TEXT_COLOR);

    // Quantity spinner
    JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
    quantitySpinner.setMaximumSize(new Dimension(80, 25));
    quantitySpinner.setAlignmentX(Component.CENTER_ALIGNMENT);

    // Add to cart button
    JButton addButton = createStyledButton("Add to Cart");
    addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    addButton.addActionListener(e ->
    {
        int quantity = (int) quantitySpinner.getValue();
        Cart.getInstance().addProduct(new CartItem(product, quantity));
        showAddedToCartNotification(product.getName(), quantity);
        refreshCartViewCallback.run();
    });

    // Add components to card with spacing
    card.add(Box.createVerticalStrut(10));
    card.add(nameLabel);
    card.add(Box.createVerticalStrut(5));
    card.add(priceLabel);
    card.add(Box.createVerticalStrut(10));
    card.add(quantitySpinner);
    card.add(Box.createVerticalStrut(10));
    card.add(addButton);
    card.add(Box.createVerticalStrut(10));

    // Add hover effect
    card.addMouseListener(new MouseAdapter()
    {
        @Override
        public void mouseEntered(MouseEvent e)
        {
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                new EmptyBorder(15, 15, 15, 15)
            ));
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 15, 15)
            ));
        }
    });

    return card;
}

    private JButton createStyledButton(String text)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                button.setBackground(ACCENT_COLOR.darker());
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                button.setBackground(ACCENT_COLOR);
            }
        });

        return button;
    }

    private void showAddedToCartNotification(String productName, int quantity) 
    {
        SwingUtilities.invokeLater(() -> {
             JPanel notification = new JPanel();
        notification.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        notification.setBackground(new Color(40, 167, 69));

        JLabel message = new JLabel("✓ " + quantity + " " + productName + " added to cart");
        message.setForeground(Color.WHITE);
        message.setFont(new Font("Segoe UI", Font.BOLD, 14));

        notification.add(message);

        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this));
        dialog.setUndecorated(true);
        dialog.setAlwaysOnTop(true);
        dialog.setModal(false);
        dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        dialog.add(notification);
        dialog.pack();

        // Add a slight shadow/border
        notification.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Position the notification in the bottom-right corner
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(
            screenSize.width - dialog.getWidth() - 20,
            screenSize.height - dialog.getHeight() - 50
        );

        // Show notification and start fade-out timer
        dialog.setOpacity(1.0f);
        dialog.setVisible(true);

        Timer timer = new Timer(2000, e -> {
            dialog.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    });
    }
}

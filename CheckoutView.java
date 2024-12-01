import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CheckoutView extends JPanel {
    private ColorScheme colorScheme;
    private JLabel totalPriceLabel;

    public CheckoutView() {
        colorScheme = new ColorScheme();

        setLayout(new BorderLayout());
        setBackground(colorScheme.getBackgroundColor());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(colorScheme.getBackgroundColor());

        JLabel titleLabel = new JLabel("Checkout");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(colorScheme.getTextColor());
        titlePanel.add(titleLabel);

        // Create cart summary panel
        JPanel cartSummaryPanel = new JPanel();
        cartSummaryPanel.setLayout(new BoxLayout(cartSummaryPanel, BoxLayout.Y_AXIS));
        cartSummaryPanel.setBackground(colorScheme.getBackgroundColor());

        JScrollPane scrollPane = new JScrollPane(cartSummaryPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);

        // Populate cart items
        List<CartItem> cartItems = Cart.getInstance().getItems();
        for (CartItem item : cartItems) {
            cartSummaryPanel.add(createCartItemPanel(item));
            cartSummaryPanel.add(Box.createVerticalStrut(10)); // Add spacing
        }

        // Total price section
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(colorScheme.getBackgroundColor());
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        totalPriceLabel = new JLabel("Total: ₹" + calculateTotal(cartItems));
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalPriceLabel.setForeground(colorScheme.getTextColor());
        totalPanel.add(totalPriceLabel);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.setBackground(colorScheme.getBackgroundColor());

        JButton confirmButton = new JButton("Confirm Order");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBackground(new Color(0, 122, 255));
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.addActionListener(e -> handleConfirmOrder());

        JButton backButton = new JButton("Back to Cart");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(200, 0, 0));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) this.getParent().getLayout();
            cl.show(this.getParent(), "CartView");
        });

        actionPanel.add(confirmButton);
        actionPanel.add(backButton);

        // Add all components to the layout
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(totalPanel, BorderLayout.SOUTH);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel createCartItemPanel(CartItem item) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(colorScheme.getBackgroundColor());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JLabel itemName = new JLabel(item.getProduct().getName() + " (x" + item.getQuantity() + ")");
        itemName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemName.setForeground(colorScheme.getTextColor());
        panel.add(itemName, BorderLayout.WEST);

        JLabel itemPrice = new JLabel("₹" + (item.getProduct().getPrice() * item.getQuantity()));
        itemPrice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemPrice.setForeground(colorScheme.getTextColor());
        panel.add(itemPrice, BorderLayout.EAST);

        return panel;
    }

    private double calculateTotal(List<CartItem> cartItems) {
        return cartItems.stream()
                        .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                        .sum();
    }

    private void handleConfirmOrder() {
        JOptionPane.showMessageDialog(this, "Order Confirmed!", "Checkout", JOptionPane.INFORMATION_MESSAGE);
        Cart.getInstance().clear(); // Clear the cart
        totalPriceLabel.setText("Total: ₹0.00"); // Reset total price
        // Optionally navigate back to ProductView
        CardLayout cl = (CardLayout) this.getParent().getLayout();
        cl.show(this.getParent(), "ProductView");
    }
}

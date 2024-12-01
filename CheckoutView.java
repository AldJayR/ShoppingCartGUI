import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


public class CheckoutView extends JPanel {
    private ColorScheme colorScheme;
    private JLabel totalPriceLabel;
    private JPanel cartSummaryPanel;
    private static final Color CARD_COLOR = new Color(240, 240, 240); // Light gray
    private static final Color TEXT_COLOR = new Color(0, 0, 0); // Black

    public CheckoutView()
    {
        colorScheme = new ColorScheme();

        setLayout(new BorderLayout());
        setBackground(colorScheme.getBackgroundColor());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(colorScheme.getBackgroundColor());

        JLabel titleLabel = new JLabel("Checkout");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(colorScheme.getTextColor());
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        cartSummaryPanel = new JPanel();
        cartSummaryPanel.setLayout(new BoxLayout(cartSummaryPanel, BoxLayout.Y_AXIS));
        cartSummaryPanel.setBackground(colorScheme.getBackgroundColor());

        JScrollPane scrollPane = new JScrollPane(cartSummaryPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // South panel for total price and action buttons
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(colorScheme.getBackgroundColor());

        totalPriceLabel = new JLabel("Total: Ph₱0.00");
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalPriceLabel.setForeground(colorScheme.getTextColor());
        totalPanel.add(totalPriceLabel);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.setBackground(colorScheme.getBackgroundColor());

        JButton confirmButton = new JButton("Confirm Order");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.setForeground(Color.BLACK);
        confirmButton.setBackground(new Color(0, 122, 255));
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        

        JButton backButton = new JButton("Back to Cart");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(new Color(200, 0, 0));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) this.getParent().getLayout();
            cl.show(this.getParent(), "CartView");
        });

        actionPanel.add(confirmButton);
        actionPanel.add(backButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(totalPanel, BorderLayout.NORTH);
        southPanel.add(actionPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // Populate items
        refreshCartItems();

        // Listen for changes to cart
        Cart.getInstance().addCartUpdateListener(() -> {
            refreshCartItems();
            if (!Cart.getInstance().getItems().isEmpty()) {
                // Ensure the action listener is only added once
                for (ActionListener listener : confirmButton.getActionListeners()) {
                    confirmButton.removeActionListener(listener);
                }
                confirmButton.addActionListener(e -> handleConfirmOrder());
            }
        });
    }

    private void refreshCartItems()
    {
        cartSummaryPanel.removeAll();

        List<CartItem> cartItems = Cart.getInstance().getItems();
        if (cartItems == null || cartItems.isEmpty())
        {
            JLabel emptyLabel = new JLabel("No items in cart.");
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            emptyLabel.setForeground(colorScheme.getTextColor());
            cartSummaryPanel.add(emptyLabel);
        }
        else
        {
            for (CartItem item : cartItems)
            {
                cartSummaryPanel.add(createCartItemPanel(item));
                cartSummaryPanel.add(Box.createVerticalStrut(10));
            }
        }

        totalPriceLabel.setText("Total: ₱" + calculateTotal(cartItems));

        cartSummaryPanel.revalidate();
        cartSummaryPanel.repaint();
    }

    private JPanel createCartItemPanel(CartItem item)
    {
        Product product = item.getProduct();
        JPanel card = new JPanel();
        card.setBackground(CARD_COLOR);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        card.setPreferredSize(new Dimension(0, 120));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setMinimumSize(new Dimension(0, 120));

        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(CARD_COLOR);

        JLabel imageLabel = new JLabel();
        System.out.println(product.getImage());
        if (product.getImage() != null)
        {
            ImageIcon imageIcon = new ImageIcon(product.getImage());
            imageLabel.setIcon(imageIcon);
            imageLabel.setPreferredSize(new Dimension(80, 80));
        }
        imagePanel.add(imageLabel);

        card.add(imagePanel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        infoPanel.setBackground(CARD_COLOR);

        JLabel nameLabel = new JLabel(product.getName() != null ? product.getName() : "Unknown");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(TEXT_COLOR);

        JLabel priceLabel = new JLabel("P" + product.getPrice());
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priceLabel.setForeground(TEXT_COLOR);

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        JLabel quantityLabel = new JLabel("Qty: " + item.getQuantity());
        quantityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        quantityLabel.setForeground(TEXT_COLOR);
        card.add(quantityLabel, BorderLayout.EAST);

        return card;
    }

    private double calculateTotal(List<CartItem> cartItems)
    {
        return cartItems.stream()
            .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
            .sum();
    }

    private void handleConfirmOrder()
    {

        JOptionPane.showMessageDialog(this, "Order Confirmed!", "Checkout",
                                      JOptionPane.INFORMATION_MESSAGE);
        Cart.getInstance().clear();
        refreshCartItems();

        CartView cartView = (CartView) getParent().getComponent(
            1);
        cartView.refreshView();
        totalPriceLabel.setText("Total: ₱0.00");

        CardLayout cl = (CardLayout) this.getParent().getLayout();
        cl.show(this.getParent(), "ProductView");
    }
}

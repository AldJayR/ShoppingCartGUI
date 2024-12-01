import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class CartView extends JPanel {
    private final JPanel productListPanel;
    private final JLabel totalPriceLabel;
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 247);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(0, 122, 255);
    private static final Color TEXT_COLOR = new Color(29, 29, 31);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font CATEGORY_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font PRICE_FONT = new Font("Segoe UI", Font.BOLD, 18);

    public CartView()
    {
        setLayout(new BorderLayout(0, 20));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel header = new JLabel("Shopping Cart");
        header.setFont(HEADER_FONT);
        header.setForeground(TEXT_COLOR);
        headerPanel.add(header, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        productListPanel = new JPanel();
        productListPanel.setLayout(new BoxLayout(productListPanel, BoxLayout.Y_AXIS));
        productListPanel.setBackground(BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(productListPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            new EmptyBorder(20, 0, 0, 0)));

        totalPriceLabel = new JLabel("Total: $0.00");
        totalPriceLabel.setFont(PRICE_FONT);
        totalPriceLabel.setForeground(TEXT_COLOR);

        JButton checkoutButton = createStyledButton("Proceed to Checkout");

        bottomPanel.add(totalPriceLabel, BorderLayout.WEST);
        bottomPanel.add(checkoutButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        refreshView();
    }

    private JButton createStyledButton(String text)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 40));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                button.setBackground(ACCENT_COLOR.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                button.setBackground(ACCENT_COLOR);
            }
        });

        button.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) this.getParent().getLayout();
            cardLayout.show(this.getParent(), "CheckoutView");
        });

        return button;
    }

    private JPanel createCategoryPanel(String category, List<CartItem> categoryItems)
    {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setBackground(BACKGROUND_COLOR);
        categoryPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        for (CartItem item : categoryItems)
        {
            if (item != null && item.getProduct() != null)
            {
                categoryPanel.add(createProductCard(item));
                categoryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        return categoryPanel;
    }

    private JPanel createProductCard(CartItem item)
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

        JSpinner quantitySpinner =
            new JSpinner(new SpinnerNumberModel(item.getQuantity(), 1, 100, 1));
        quantitySpinner.setPreferredSize(new Dimension(60, 25));

        JComponent editor = (JComponent) quantitySpinner.getEditor();
        JTextField textField = (JTextField) editor.getComponent(0);
        textField.setBackground(Color.WHITE);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(
            BorderFactory.createLineBorder(Color.GRAY));

        Component upButton = quantitySpinner.getComponent(0);
        Component downButton = quantitySpinner.getComponent(1);

        upButton.setBackground(new Color(0, 122, 255));
        upButton.setForeground(Color.WHITE);
        downButton.setBackground(new Color(0, 122, 255));
        downButton.setForeground(Color.WHITE);

        upButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                upButton.setBackground(new Color(0, 150, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                upButton.setBackground(new Color(0, 122, 255));
            }
        });

        downButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                downButton.setBackground(new Color(0, 150, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                downButton.setBackground(new Color(0, 122, 255));
            }
        });

        quantitySpinner.addChangeListener(e -> {
            int newQuantity = (Integer) quantitySpinner.getValue();
            item.setQuantity(newQuantity);
            updateTotalPrice(Cart.getInstance());
        });

        card.add(quantitySpinner, BorderLayout.EAST);

        JButton removeButton = new JButton("Remove");
        removeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        removeButton.setForeground(ACCENT_COLOR);
        removeButton.setBackground(CARD_COLOR);
        removeButton.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
        removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        removeButton.setFocusPainted(false);
        removeButton.setPreferredSize(new Dimension(100, 30));

        removeButton.addActionListener(e -> {
            Cart.getInstance().removeProduct(item);
            updateTotalPrice(Cart.getInstance());
            refreshView();
        });

        card.add(removeButton, BorderLayout.SOUTH);

        return card;
    }

    public void refreshView()
    {
        SwingUtilities.invokeLater(() -> {
            try
            {
                updateCartDisplay();
            } catch (Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating cart view: " + e.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void updateCartDisplay()
    {
        productListPanel.removeAll();

        Cart cart = Cart.getInstance();
        if (cart == null)
        {
            showEmptyCartMessage();
            return;
        }

        List<CartItem> items = cart.getItems();
        if (items == null || items.isEmpty())
        {
            showEmptyCartMessage();
            return;
        }

        Map<String, List<CartItem>> itemsByCategory = new HashMap<>();
        for (CartItem item : items)
        {
            Product product = item.getProduct();
            if (product != null)
            {
                itemsByCategory.computeIfAbsent(product.getCategory(), k -> new ArrayList<>())
                    .add(item);
            }
        }

        for (Map.Entry<String, List<CartItem>> entry : itemsByCategory.entrySet())
        {
            String category = entry.getKey();
            List<CartItem> categoryItems = entry.getValue();

            JPanel categoryPanel = createCategoryPanel(category, categoryItems);
            JLabel categoryLabel = new JLabel(category);
            categoryLabel.setFont(CATEGORY_FONT);
            categoryLabel.setForeground(TEXT_COLOR);
            categoryLabel.setBorder(new EmptyBorder(10, 0, 5, 0));
            productListPanel.add(categoryLabel);
            productListPanel.add(categoryPanel);
        }

        updateTotalPrice(cart);

        revalidate();
        repaint();
    }

    private void updateTotalPrice(Cart cart)
    {
        double total = cart.calculateTotal();
        totalPriceLabel.setText("Total: P" + total);
    }
    private void showEmptyCartMessage()
    {
        productListPanel.removeAll();
        JLabel emptyMessage = new JLabel("Your cart is empty.");
        emptyMessage.setFont(new Font("Segoe UI", Font.BOLD, 18));
        emptyMessage.setForeground(TEXT_COLOR);
        productListPanel.add(emptyMessage);
        revalidate();
        repaint();
    }
}

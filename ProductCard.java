import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.util.function.BiConsumer;

public class ProductCard extends JPanel {
    private Product product;
    private ColorScheme colorScheme;
    private BiConsumer<Product, Integer> addToCartCallback;

    public ProductCard(Product product, ColorScheme colorScheme, BiConsumer<Product, Integer> addToCartCallback) {
        this.product = product;
        this.colorScheme = colorScheme;
        this.addToCartCallback = addToCartCallback;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(colorScheme.getCardColor());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        initializeComponents();
        addHoverEffect();
    }

    private void initializeComponents() {
        add(createLabel(product.getName(), Font.BOLD, 16));
        add(Box.createVerticalStrut(5));
        add(createLabel(String.format("$%.2f", product.getPrice()), Font.PLAIN, 14));
        add(Box.createVerticalStrut(10));

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        quantitySpinner.setMaximumSize(new Dimension(80, 25));
        quantitySpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(quantitySpinner);
        add(Box.createVerticalStrut(10));

        JButton addButton = createStyledButton("Add to Cart");
        addButton.addActionListener(e -> {
            int quantity = (int) quantitySpinner.getValue();
            addToCartCallback.accept(product, quantity);
        });
        add(addButton);
    }

    private JLabel createLabel(String text, int fontStyle, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", fontStyle, fontSize));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(colorScheme.getTextColor());
        return label;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(colorScheme.getAccentColor());
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(colorScheme.getAccentColor().darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(colorScheme.getAccentColor());
            }
        });

        return button;
    }

    private void addHoverEffect() {
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(colorScheme.getAccentColor(), 1),
                    new EmptyBorder(15, 15, 15, 15)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    new EmptyBorder(15, 15, 15, 15)
                ));
            }
        });
    }
}
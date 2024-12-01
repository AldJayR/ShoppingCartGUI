import java.awt.*;
import java.net.URL;
import java.util.function.BiConsumer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProductCard extends JPanel {
    private Product product;
    private ColorScheme colorScheme;
    private BiConsumer<Product, Integer> addToCartCallback;

    public ProductCard(Product product, ColorScheme colorScheme,
                       BiConsumer<Product, Integer> addToCartCallback)
    {
        this.product = product;
        this.colorScheme = colorScheme;
        this.addToCartCallback = addToCartCallback;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(colorScheme.getCardColor());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)));

        initializeComponents();
        addHoverEffect();
    }

    private void initializeComponents()
    {
        // Panel for the image
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout()); // Use BorderLayout for full-width image
        imagePanel.setBackground(colorScheme.getCardColor());

        // Add Product Image if available
        String imageFileName = product.getImage();
        if (imageFileName != null && !imageFileName.isEmpty())
        {
            URL imageUrl = getClass().getClassLoader().getResource("images/" + imageFileName);
            if (imageUrl != null)
            {
                ImageIcon productImage = new ImageIcon(imageUrl);
                Image scaledImage =
                    scaleImage(productImage.getImage(), 400, 200); // Max size; adjust as needed
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                JLabel imageLabel = new JLabel(scaledIcon);
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imagePanel.add(imageLabel, BorderLayout.CENTER);
            }
            else
            {
                imagePanel.add(createLabel("[Image Not Found]", Font.ITALIC, 14),
                               BorderLayout.CENTER);
            }
        }
        else
        {
            imagePanel.add(createLabel("[No Image]", Font.ITALIC, 14), BorderLayout.CENTER);
        }

        add(imagePanel); // Add the image panel to the card

        // Panel for name, price, and button
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(colorScheme.getCardColor());

        // Add Product name
        infoPanel.add(createLabel(product.getName(), Font.BOLD, 16));

        // Add Product price
        infoPanel.add(createLabel(String.format("P%.2f", product.getPrice()), Font.PLAIN, 14));

        // Add a fixed vertical space before the button
        infoPanel.add(Box.createVerticalStrut(10)); // Vertical spacing

        // Add to cart button
        JButton addButton = createStyledButton("Add to Cart");
        addButton.addActionListener(e -> { addToCartCallback.accept(product, 1); });
        infoPanel.add(addButton);

        add(infoPanel); // Add the info panel to the card
    }

    private Image scaleImage(Image originalImage, int maxWidth, int maxHeight)
    {
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);

        if (originalWidth <= maxWidth && originalHeight <= maxHeight)
        {
            return originalImage;
        }

        double aspectRatio = (double) originalWidth / originalHeight;
        int newWidth = maxWidth;
        int newHeight = (int) (newWidth / aspectRatio);

        if (newHeight > maxHeight)
        {
            newHeight = maxHeight;
            newWidth = (int) (newHeight * aspectRatio);
        }

        return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    private JLabel createLabel(String text, int fontStyle, int fontSize)
    {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", fontStyle, fontSize));
        label.setForeground(colorScheme.getTextColor());
        label.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align
        return label;
    }

    private JButton createStyledButton(String text)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(colorScheme.getAccentColor());
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                button.setBackground(colorScheme.getAccentColor().darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                button.setBackground(colorScheme.getAccentColor());
            }
        });

        return button;
    }

    private void addHoverEffect()
    {
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(colorScheme.getAccentColor(), 1),
                    new EmptyBorder(15, 15, 15, 15)));
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                    new EmptyBorder(15, 15, 15, 15)));
            }
        });
    }
}

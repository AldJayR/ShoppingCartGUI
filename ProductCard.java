import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.function.BiConsumer;
import javax.swing.border.EmptyBorder;

public class ProductCard extends JPanel
{
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
        
        // Padding all around the card
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)));

        initializeComponents();
        addHoverEffect();
    }

    private void initializeComponents()
    {
        // Panel for name, image, and price
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS)); // Horizontal layout
        infoPanel.setBackground(colorScheme.getCardColor());

        // Add Product Image if available
        String imageFileName = product.getImage();
        if (imageFileName != null && !imageFileName.isEmpty())
        {
            URL imageUrl = getClass().getClassLoader().getResource("images/" + imageFileName);
            if (imageUrl != null)
            {
                ImageIcon productImage = new ImageIcon(imageUrl);
                Image scaledImage = scaleImage(productImage.getImage(), 80, 80); // Max size 80x80
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                JLabel imageLabel = new JLabel(scaledIcon);
                
                // Set fixed size and margin right for the image
                imageLabel.setPreferredSize(new Dimension(80, 80)); 
                imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Margin right

                infoPanel.add(imageLabel);
            }
            else
            {
                System.out.println("Image not found: " + imageFileName);
            }
        }

        // Add Product name
        infoPanel.add(createLabel(product.getName(), Font.BOLD, 16));

        // Add Product price
        infoPanel.add(Box.createHorizontalStrut(10)); // Spacing between name and price
        infoPanel.add(createLabel(String.format("P%.2f", product.getPrice()), Font.PLAIN, 14));

        // Add glue to push components properly
        infoPanel.add(Box.createHorizontalGlue());

        add(infoPanel);
        
        // Add a fixed vertical space before the button (keeping it below the name)
        add(Box.createVerticalStrut(10)); // Vertical spacing

        // Add to cart button directly beneath the product name
        JButton addButton = createStyledButton("Add to Cart");
        addButton.addActionListener(e -> { addToCartCallback.accept(product, 1); });
        add(addButton);
    }

    private Image scaleImage(Image originalImage, int maxWidth, int maxHeight)
    {
        int originalWidth = originalImage.getWidth(null);
        int originalHeight = originalImage.getHeight(null);

        // Calculate the new dimensions while maintaining the aspect ratio
        double aspectRatio = (double) originalWidth / originalHeight;
        int newWidth = maxWidth;
        int newHeight = (int) (newWidth / aspectRatio);

        // If new height exceeds maxHeight, adjust width and height accordingly
        if (newHeight > maxHeight)
        {
            newHeight = maxHeight;
            newWidth = (int) (newHeight * aspectRatio);
        }

        // Scale the image
        return originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    private JLabel createLabel(String text, int fontStyle, int fontSize)
    {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", fontStyle, fontSize));
        label.setAlignmentX(Component.LEFT_ALIGNMENT); // Align left
        label.setForeground(colorScheme.getTextColor());
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
        button.setAlignmentX(Component.LEFT_ALIGNMENT); // Align button to the left

        button.addMouseListener(new java.awt.event.MouseAdapter()
        {
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
        addMouseListener(new java.awt.event.MouseAdapter()
        {
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

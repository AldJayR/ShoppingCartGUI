import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.util.function.BiConsumer;

public class ProductView extends JPanel {
    private List<Product> products;
    private Runnable refreshCartViewCallback;
    private ColorScheme colorScheme;

    public ProductView(List<Product> products, Runnable refreshCartViewCallback) {
        this.products = products;
        this.refreshCartViewCallback = refreshCartViewCallback;
        this.colorScheme = new ColorScheme();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(colorScheme.getBackgroundColor());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initializeProductGrid();
    }

    private void initializeProductGrid() {
        Map<String, List<Product>> productsByCategory = groupProductsByCategory();
        productsByCategory.forEach(this::addCategoryPanel);
    }

    private Map<String, List<Product>> groupProductsByCategory() {
        Map<String, List<Product>> productsByCategory = new HashMap<>();
        for (Product product : products) {
            productsByCategory.computeIfAbsent(product.getCategory(), k -> new ArrayList<>()).add(product);
        }
        return productsByCategory;
    }

    private void addCategoryPanel(String category, List<Product> categoryProducts) {
        add(createCategoryPanel(category, categoryProducts));
        add(Box.createVerticalStrut(30));
    }

    private JPanel createCategoryPanel(String category, List<Product> categoryProducts) {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setBackground(colorScheme.getBackgroundColor());
        categoryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        categoryLabel.setForeground(colorScheme.getTextColor());
        categoryLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        categoryPanel.add(categoryLabel);

        JPanel productGridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        productGridPanel.setBackground(colorScheme.getBackgroundColor());

        categoryProducts.forEach(product -> 
            productGridPanel.add(new ProductCard(product, colorScheme, this::addToCart))
        );
        categoryPanel.add(productGridPanel);

        return categoryPanel;
    }

    private void addToCart(Product product, int quantity) {
        Cart.getInstance().addProduct(new CartItem(product, quantity));
        NotificationManager.showNotification(product.getName(), quantity);
        refreshCartViewCallback.run();
    }
}
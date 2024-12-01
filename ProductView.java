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
    private JScrollPane scrollPane;

    public ProductView(List<Product> products, Runnable refreshCartViewCallback) {
        this.products = products;
        this.refreshCartViewCallback = refreshCartViewCallback;
        this.colorScheme = new ColorScheme();

        // Create a container panel for the content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(colorScheme.getBackgroundColor());
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Set the layout for this panel
        setLayout(new BorderLayout());
        setBackground(colorScheme.getBackgroundColor());

        // Initialize the grid on the content panel
        initializeProductGrid(contentPanel);

        // Create scroll pane and add the content panel to it
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        // Add the scroll pane to this panel
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initializeProductGrid(JPanel contentPanel) {
        Map<String, List<Product>> productsByCategory = groupProductsByCategory();
        productsByCategory.forEach((category, products) -> 
            addCategoryPanel(contentPanel, category, products));
    }

    private Map<String, List<Product>> groupProductsByCategory() {
        Map<String, List<Product>> productsByCategory = new HashMap<>();
        for (Product product : products) {
            productsByCategory.computeIfAbsent(product.getCategory(), k -> new ArrayList<>()).add(product);
        }
        return productsByCategory;
    }

    private void addCategoryPanel(JPanel contentPanel, String category, List<Product> categoryProducts) {
        contentPanel.add(createCategoryPanel(category, categoryProducts));
        contentPanel.add(Box.createVerticalStrut(30));
    }

    private JPanel createCategoryPanel(String category, List<Product> categoryProducts) {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.setBackground(colorScheme.getBackgroundColor());
        categoryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create a panel for the category label with FlowLayout
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setBackground(colorScheme.getBackgroundColor());
        
        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        categoryLabel.setForeground(colorScheme.getTextColor());
        categoryLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        labelPanel.add(categoryLabel);
        categoryPanel.add(labelPanel);

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
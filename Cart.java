import java.util.ArrayList;
import java.util.List;

public class Cart
{
    private static Cart instance;
    private List<CartItem> items;

    private Cart()
    {
        items = new ArrayList<>();
    }

    public static Cart getInstance()
    {
        if (instance == null)
        {
            synchronized (Cart.class)
            {
                if (instance == null)
                {
                    instance = new Cart();
                }
            }
        }
        return instance;
    }

    public void addProduct(CartItem item)
    {
        // Check if the item already exists in the cart based on product ID
        for (CartItem cartItem : items)
        {
            if (cartItem.getProduct().getId() == item.getProduct().getId())
            {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());  // Increase quantity if the product already exists
                return;
            }
        }
        items.add(item);  // Otherwise, add the new item to the cart
    }

    public void removeProduct(CartItem item)
    {
        items.removeIf(cartItem -> cartItem.getProduct().getId() == item.getProduct().getId()); // Remove the item from the cart
    }

    public List<CartItem> getItems()
    {
        return new ArrayList<>(items); // Return a copy of the list to avoid external modifications
    }

    public void clear()
    {
        this.items.clear();
    }

    public double calculateTotal()
    {
        return items.stream()
                    .mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity()) // Multiply by quantity
                    .sum();
    }
}

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance;
    private List<CartItem> items;
    private List<Runnable> updateListeners;

    private Cart()
    {
        items = new ArrayList<>();
        updateListeners = new ArrayList<>();
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
        for (CartItem cartItem : items)
        {
            if (cartItem.getProduct().getId() == item.getProduct().getId())
            {
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                notifyListeners();
                return;
            }
        }
        items.add(item);
        notifyListeners();
    }

    public void removeProduct(CartItem item)
    {
        items.removeIf(cartItem -> cartItem.getProduct().getId() == item.getProduct().getId());
        notifyListeners();
    }

    public List<CartItem> getItems()
    {
        return new ArrayList<>(items);
    }

    public void clear()
    {
        this.items.clear();
        notifyListeners();
    }

    public double calculateTotal()
    {
        return items.stream()
            .mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
            .sum();
    }

    public void addCartUpdateListener(Runnable listener)
    {
        updateListeners.add(listener);
    }

    private void notifyListeners()
    {
        for (Runnable listener : updateListeners)
        {
            listener.run();
        }
    }

    public void updateQuantity(CartItem item, int newQuantity) 
    {
        item.setQuantity(newQuantity);
        notifyListeners();
    }
}

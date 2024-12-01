import java.util.Objects;

public class Product
{
    private static int idCounter = 0; 
    private int id;
    private String name;
    private double price;
    private String category;
    private String imageURL;

    public Product(String name, double price, String category, String imageURL)
    {
        this.id = ++idCounter;
        this.name = name;
        this.price = price;
        this.category = category != null ? category : "";  
        this.imageURL = imageURL != null ? imageURL : "";
    }

    // Constructor without category and imageURL
    public Product(String name, double price)
    {
        this(name, price, null, null);
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice()
    {
        return price;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public String getCategory() 
    {
        return category;
    }

    @Override
    public String toString()
    {
        return "Product [ID: " + id + ", Name: " + name + ", Price: $" + price + "]";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        Product other = (Product) obj;
        return Objects.equals(this.getName(), other.getName());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getName()); // Or another unique identifier
    }
}

package ecommerce;

import java.util.*;
import java.math.BigDecimal;

// ==============================
// PRODUCT
// ==============================
class Product {
    private int id;
    private String name;
    private BigDecimal price;
    private int stock;

    public Product(int id, String name, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }

    public void reduceStock(int amount) {
        if (amount <= stock) stock -= amount;
    }
}

// ==============================
// USER
// ==============================
class User {
    private int id;
    private String name;
    private String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}

// ==============================
// CART ITEM
// ==============================
class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public BigDecimal getTotalPrice() {
        return product.getPrice().multiply(new BigDecimal(quantity));
    }
}

// ==============================
// CART
// ==============================
class Cart {
    private User user;
    private List<CartItem> items = new ArrayList<>();

    public Cart(User user) {
        this.user = user;
    }

    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                int newQty = item.getQuantity() + quantity;
                items.remove(item);
                items.add(new CartItem(product, newQty));
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items)
            total = total.add(item.getTotalPrice());
        return total;
    }

    public List<CartItem> getItems() { return items; }
}

// ==============================
// PAYMENT ABSTRACT
// ==============================
abstract class Payment {
    protected BigDecimal amount;

    public Payment(BigDecimal amount) {
        this.amount = amount;
    }

    public abstract boolean process();
}

// ==============================
// CARD PAYMENT
// ==============================
class CardPayment extends Payment {
    private String cardNumber;

    public CardPayment(BigDecimal amount, String cardNumber) {
        super(amount);
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean process() {
        return cardNumber.length() >= 12;
    }
}

// ==============================
// ORDER ITEM
// ==============================
class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}

// ==============================
// ORDER
// ==============================
class Order {
    private int id;
    private User user;
    private List<OrderItem> items;
    private BigDecimal totalPrice;
    private Payment payment;

    public Order(int id, User user, List<OrderItem> items, BigDecimal totalPrice, Payment payment) {
        this.id = id;
        this.user = user;
        this.items = items;
        this.totalPrice = totalPrice;
        this.payment = payment;
    }

    public boolean confirmOrder() {
        return payment.process();
    }
}

// ==============================
// ORDER SERVICE
// ==============================
class OrderService {
    private int orderCounter = 1;

    public Order createOrder(User user, Cart cart, Payment payment) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            orderItems.add(new OrderItem(item.getProduct(), item.getQuantity()));
        }

        return new Order(orderCounter++, user, orderItems, cart.getTotal(), payment);
    }
}

// ==============================
// MAIN — FULL DEMO
// ==============================
public class Ecommerce {
    public static void main(String[] args) {

        Product p1 = new Product(1, "Laptop", new BigDecimal("350000"), 10);
        Product p2 = new Product(2, "Phone", new BigDecimal("150000"), 20);

        User user = new User(1, "Ұлбо", "ulbo@mail.com");

        Cart cart = new Cart(user);
        cart.addItem(p1, 1);
        cart.addItem(p2, 2);

        Payment payment = new CardPayment(cart.getTotal(), "123456789012");

        OrderService service = new OrderService();
        Order order = service.createOrder(user, cart, payment);

        System.out.println("=== E-COMMERCE ORDER RESULT ===");
        System.out.println("Client: " + user.getName());
        System.out.println("Total Pay: " + cart.getTotal());
        System.out.println(order.confirmOrder() ? "Order SUCCESS" : "Order FAILED");
    }
}

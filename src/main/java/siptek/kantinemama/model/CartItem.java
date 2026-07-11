package siptek.kantinemama.model;

public class CartItem {
    private MenuItem menuItem;
    private int qty;

    public CartItem(MenuItem menuItem, int qty) {
        this.menuItem = menuItem;
        this.qty = qty;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getSubtotal() {
        return menuItem.getHarga() * qty;
    }
}

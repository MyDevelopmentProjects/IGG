package ge.unknown.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AllProductsSingleton {

    private List<Product> products = new ArrayList<>();

    private static AtomicReference<AllProductsSingleton> instance = new AtomicReference<>();

    private AllProductsSingleton() { }

    public static AllProductsSingleton getInstance() {
        AllProductsSingleton foo = instance.get();
        if (foo == null) {
            foo = new AllProductsSingleton();
            if (instance.compareAndSet(null, foo))
                return foo;
            else
                return instance.get();
        } else {
            return foo;
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        getInstance().products.clear();
        getInstance().products.addAll(products);
    }
}

package product.order.cli.app.formatter;

import lombok.Getter;

import java.util.*;

@Getter
public class OrderTransactions {

    private Map<Long, Integer> map = new HashMap<>();

    public void createOrderTransaction(Long productNumber, Integer quantity) {
        Integer currentQuantity = map.getOrDefault(productNumber, 0);
        map.put(productNumber, currentQuantity + quantity);
    }

    public Set<Long> productNumbers() {
        return map.keySet();
    }

    public List<Integer> orderQuantities() {
        return new ArrayList<>(map.values());
    }
}

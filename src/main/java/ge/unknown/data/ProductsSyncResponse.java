package ge.unknown.data;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class ProductsSyncResponse extends BaseResponse {
    private List<Product> products = new ArrayList<>();
}

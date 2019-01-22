package ge.unknown.data;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductsSyncReq {
    private String oper = "syncproduct";
}

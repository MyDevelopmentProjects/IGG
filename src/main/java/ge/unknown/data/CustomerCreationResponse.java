package ge.unknown.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerCreationResponse extends BaseResponse {
    private Long partnerid;
}

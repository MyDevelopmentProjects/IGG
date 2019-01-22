package ge.unknown.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Doctor extends BaseResponse {
    private Long doctorid;
    private String name;
}

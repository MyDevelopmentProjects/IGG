package ge.unknown.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorVisitCreationResponse extends BaseResponse {
    private Long visitid;
}

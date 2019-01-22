package ge.unknown.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponse{
    @Builder.Default
    private Boolean success = false;
    private String message;
    private Integer code;
    private List<String> errors;
    private Map<String,String> meta;
}
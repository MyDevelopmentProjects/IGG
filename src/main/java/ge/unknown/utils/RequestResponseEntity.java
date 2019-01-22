package ge.unknown.utils;

import lombok.Getter;
import lombok.Setter;


/**
 * Created by user on 5/16/18.
 */
@Getter
@Setter
public class RequestResponseEntity<T>  extends RequestResponse {
    private T content;

    public RequestResponseEntity(T content) {
        if(content != null){
            this.setSuccess(true);
        }
        this.content = content;
    }
}

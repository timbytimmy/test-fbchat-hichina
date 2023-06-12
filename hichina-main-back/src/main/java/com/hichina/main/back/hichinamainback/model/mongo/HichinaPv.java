package com.hichina.main.back.hichinamainback.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document("HichinaPv")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HichinaPv {
    private String pageName;
    private String clientIp;
    private Date timestamp;
}

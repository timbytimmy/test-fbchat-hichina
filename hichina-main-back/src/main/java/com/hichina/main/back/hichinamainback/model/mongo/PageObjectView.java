package com.hichina.main.back.hichinamainback.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("PageObjectView")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageObjectView {
    @Id
    private String id;
    private String objId;
    private String objType;
    private Long viewCnt;
    @Version
    private Long version;
}

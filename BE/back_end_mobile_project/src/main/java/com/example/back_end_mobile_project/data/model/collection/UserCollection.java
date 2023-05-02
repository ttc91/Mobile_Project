package com.example.back_end_mobile_project.data.model.collection;

import com.example.back_end_mobile_project.data.constant.MongoConstant;
import com.example.back_end_mobile_project.data.model.collection.base.BaseCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(MongoConstant.CollectionName.USER_COLLECTION)
public class UserCollection extends BaseCollection implements Serializable {

    @Indexed(unique = true)
    private String username;

}

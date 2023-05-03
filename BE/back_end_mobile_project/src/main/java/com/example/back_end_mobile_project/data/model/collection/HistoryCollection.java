package com.example.back_end_mobile_project.data.model.collection;

import com.example.back_end_mobile_project.data.constant.MongoConstant;
import com.example.back_end_mobile_project.data.model.collection.base.BaseCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(MongoConstant.CollectionName.HISTORY_COLLECTION)
public class HistoryCollection extends BaseCollection {

    private String historyDate;

    private String historyHabitsState;

    private String username;

    private Long habitId;

}

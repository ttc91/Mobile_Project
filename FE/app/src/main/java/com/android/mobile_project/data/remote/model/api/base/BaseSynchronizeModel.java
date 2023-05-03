package com.android.mobile_project.data.remote.model.api.base;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseSynchronizeModel<T> {

    @SerializedName("username")
    private String username;

    @SerializedName("dataList")
    private List<T> dataList;

}

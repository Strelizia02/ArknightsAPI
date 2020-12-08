package com.wzy.arknights.vo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

public class BaseVO implements Serializable {

  public BaseVO() {
  }

  public BaseVO(Object source) {
    BeanUtils.copyProperties(source, this);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}

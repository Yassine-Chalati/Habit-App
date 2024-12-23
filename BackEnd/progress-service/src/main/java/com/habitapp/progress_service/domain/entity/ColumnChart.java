package com.habitapp.progress_service.domain.entity;

import com.habitapp.progress_service.domain.base.Chart;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class ColumnChart extends Chart {

}

package com.test.project.litis.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Organization {
	private int orgNo;
	private String orgName;
	private int parentOrgNo;
	private String orgNoDelFl;
}

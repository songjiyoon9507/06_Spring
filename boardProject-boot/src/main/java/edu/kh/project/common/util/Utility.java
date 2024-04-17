package edu.kh.project.common.util;

import java.text.SimpleDateFormat;

// 프로그램 전체적으로 사용될 유용한 기능 모음 클래스
public class Utility {

	// 필드와 메서드는 전부 static 으로 만들거
	
	public static int seqNum = 1; // 사용하고 나면 ++ 해줄거임
	// 1~99999; 반복하도록
	
	public static String fileRename(String originalFileName) {
		
		// fileRename -> 20240417102814_00004.jpg 년월일시분초_시퀀스No.확장자
		
		// SimpleDateFormat : 시간을 원하는 형태의 문자열로 간단히 변경해주는 애
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		// new java.util.Date() : 현재 시간을 저장한 자바 객체
		String date = sdf.format(new java.util.Date());
		
		String number = String.format("%05d", seqNum); // 00000 만들어두고 seqNum 에 따라 숫자 채워줌
		
		seqNum++; // 1증가 후 필드에 저장됨
		
		if(seqNum == 100000) seqNum = 1;
		
		// 확장자 구하기
		// "문자열".substring(인덱스)
		// - 문자열을 인덱스부터 끝까지 잘라낸 결과를 반환
		
		// "문자열".lastIndexOf(".")
		// - 문자열에서 마지막 "."의 인덱스를 반환

		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		// originalFileName == 뚱이.jpg
		// .jpg 까지 ext 에 저장됨
		
		return date + "_" + number + ext;
	}
}

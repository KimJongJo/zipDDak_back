package com.zipddak.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileSaveUtil {
	
	//파일 리네임
	public String createFileReName(String originalFilename) {
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".")); // 속성(파일확장자)
        
        Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String today = sf.format(date);

		int no = (int) Math.ceil(Math.random() * 1000);
		String makeFileRenm = today + "_" + no + ext;
		
		return makeFileRenm;
    }

	//저장경로에 파일 저장
    public void saveFile(MultipartFile multipartFile, String uploadPath, String FileRename) throws Exception {
        File dir = new File(uploadPath);  //파일저장 경로에 파일 저장 
        
        //경로에 디렉토리 없으면 만들기
        if (!dir.exists()) {  
            dir.mkdirs();
        }

        //
        File target = new File(uploadPath, FileRename);
        multipartFile.transferTo(target);
    }

}

package com.zipddak.util;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zipddak.entity.ProductFile;
import com.zipddak.repository.ProductFileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileSaveService {

    private final FileSaveUtil fileSaveUtil;
    private final ProductFileRepository productFile_repo;

    // 상품관련 파일 저장 + File INSERT + PK 리턴
    public Integer uploadFile(MultipartFile multipartFile, String uploadPath, String FileType) throws Exception {

    	//첨부 파일이 없을 경우 
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        //원래 파일명
        String originalFileName = multipartFile.getOriginalFilename();
        //파일 리네임
        String storedFileName   = fileSaveUtil.createFileReName(originalFileName);

        // 실제 파일 시스템 저장
        fileSaveUtil.saveFile(multipartFile, uploadPath, storedFileName);

        // DB 저장
        if(FileType.equals("product")) { //(ProductFile 테이블에 저장)
			ProductFile productFileEntity = productFile_repo.save(ProductFile.builder()
												                     .fileName(originalFileName)
												                     .fileRename(storedFileName)
												                     .storagePath(uploadPath)
												                     .build());
									
			return productFileEntity.getProductFileIdx();
        	
        } else {
        	
        	return null;
        }
       
    }
    
   

}

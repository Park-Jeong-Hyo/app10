package com.kh.app.domain.common.svc;

import com.kh.app.domain.entity.UploadFile;
import com.kh.app.web.common.AttachFileType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MultipartFileToUploadFile {

  @Value("${attach.root_dir}")
  private String ROOT_DIR;

  //물리 파일 저장 경로
  //d:/attach/분류코드/
  //name()는 enum의 분류코드를 읽어온다.
  public String getfullPath(AttachFileType attachFileType) {
    StringBuffer path = new StringBuffer();
    path = path.append(ROOT_DIR).append(attachFileType.name()).append("/"); //d:/attach/분류코드/
    //경로가 없으면 생성
    createFolder(path.toString());
    //경로 반환
    return path.toString();
  }

  //분류코드 폴더 생성
  private void createFolder(String path) {
    File folder = new File(path);
    //폴더생성
    if(!folder.exists()) folder.mkdir();
  }

  public UploadFile convert(MultipartFile mf, AttachFileType attachFileType){
    if(mf.isEmpty()) return null;
    //빈 객체를 생성
    UploadFile uploadFile = new UploadFile();
    //어디서 만들었는지를 알기위한 코드
    //.name()을 하면 //"F018301"값이 문자열로 입력된다.
    uploadFile.setCode(attachFileType.name());
//    uploadFile.setRid(rid);
    //오리지날 파일명을 읽을 때
    uploadFile.setUpload_filename(mf.getOriginalFilename());
    //오리지날 파일 이름으로 storefilename을 만든다.
    String storeFilename = createStoreFilename(mf.getOriginalFilename());
    // 사용자가 업로드한 파일 명을 가지고 와서 유니크한 파일명을 만든다.
    uploadFile.setStore_filename(storeFilename);
    //getsize하면 사이즈를 얻을 수 있다.
    //숫자지만 문자열화 해서 저장했다.(수학이 없으면 숫자타입이 아니어도 상관없다)
    uploadFile.setFsize(String.valueOf(mf.getSize()));
    //타입저장
    uploadFile.setFtype(mf.getContentType());

    //물리 파일 저장하는 코드
    storeFile(mf,attachFileType, storeFilename);

    //메타데이터를 만들었다.
    return uploadFile;
  }

  private void storeFile(MultipartFile mf, AttachFileType attachFileType, String storeFilename) {
    String fullPath = getfullPath(attachFileType);
    File file = new File(fullPath+storeFilename);
    try{
      //디렉토리에 저장
      mf.transferTo(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public List<UploadFile> convert(List<MultipartFile> mfs, AttachFileType attachFileType) {
    if(mfs.size() < 1) return null;
    List<UploadFile> uploadFiles = new ArrayList<>();
    for (MultipartFile mf : mfs) {
      if(mf.isEmpty()) continue;
      uploadFiles.add(convert(mf,attachFileType));
    }
    return uploadFiles;
  }

  //임의파일명 생성
  private String createStoreFilename(String originalFile) {
    StringBuffer storeFileName = new StringBuffer();
    storeFileName.append(UUID.randomUUID().toString())
        .append(".")
        .append(extractExt(originalFile)); // xxx-yyy-zzz-ttt..
    return storeFileName.toString();
  }

  //확장자 추출
  private String extractExt(String originalFile) {
    int posOfExt =originalFile.lastIndexOf(".");
    String ext = originalFile.substring(posOfExt + 1);
    return ext;
  }
}

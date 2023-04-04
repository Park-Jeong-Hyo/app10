package com.kh.app.domain.common;

import com.kh.app.domain.common.dao.UploadFileDAO;
import com.kh.app.domain.entity.UploadFile;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
//필요한 객체를 알아서 만든다(components가 붙은)
public class UploadFileDAOImpl {

  //component가 붙은 애들을 자동으로 묶어준다.
  //ioc
  @Autowired
  private UploadFileDAO uploadFileDAO;

  //AssertJ
  //junit
  //기능은 비슷하지만 asserj거를 더 많이 쓴다.
  @Test
  @DisplayName("단건첨부")
  void addFile() {
    UploadFile uploadFile = new UploadFile();
    uploadFile.setCode("F010301");
    uploadFile.setRid(10L);
    uploadFile.setStore_filename(UUID.randomUUID() + ".png");
    uploadFile.setUpload_filename("배경이미지.png");
    uploadFile.setFsize("100");
    uploadFile.setFsize("image/png");
    Long fid = uploadFileDAO.addFile(uploadFile);

    Assertions.assertThat(fid).isGreaterThan(0L);
  }

  @Test
  @DisplayName("여러건 첨부")
  void addFiles() {
    List<UploadFile> files = new ArrayList<>();
    for(int i = 0; i < 5; i++) {
      UploadFile uploadFile = new UploadFile();
      uploadFile.setCode("F010302");
      uploadFile.setRid(10L);
      uploadFile.setStore_filename(UUID.randomUUID() + ".png");
      uploadFile.setUpload_filename("배경이미지" + (i + 1)+ ".png");
      uploadFile.setFsize("100");
      uploadFile.setFsize("image/png");
      files.add(uploadFile);
    }

    uploadFileDAO.addFiles(files);
    List<UploadFile> list = uploadFileDAO.findFilesByCodeWithRid("F010302", 10L);
    Assertions.assertThat(list.size()).isEqualTo(5);
  }
}

package com.kh.app.domain.product.svc;

import com.kh.app.domain.common.dao.UploadFileDAO;
import com.kh.app.domain.entity.Product;
import com.kh.app.domain.entity.UploadFile;
import com.kh.app.domain.product.dao.ProductDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//transactional(매개변수 한번에 둘 다 처리하거나, 처리하지 않거나)
@Transactional
@Service
@RequiredArgsConstructor
public class ProductSVCImpl implements ProductSVC{

  private final ProductDAO productDAO;
  private final UploadFileDAO uploadFileDAO;

  @Override
  public Long save(Product product) {
    return productDAO.save(product);
  }

  @Override
  public Long save(Product product, List<UploadFile> uploadFiles) {
    Long productId = save(product);
    uploadFiles.stream().forEach(file->file.setRid(productId));
    uploadFileDAO.addFiles(uploadFiles);
    return productId;
  }

  @Override
  public Optional<Product> findById(Long productId) {
    return productDAO.findById(productId);
  }

  @Override
  public int update(Long productId, Product product) {
    return productDAO.update(productId, product);
  }

  @Override
  public int delete(Long productId) {
    return productDAO.delete(productId);
  }

  @Override
  public int deleteParts(List<Long> productIds) {
    return productDAO.deleteParts(productIds);
  }

  @Override
  public List<Product> findAll() {
    return productDAO.findAll();
  }

  @Override
  public boolean isExist(Long productId) {
    return productDAO.isExist(productId);
  }
}

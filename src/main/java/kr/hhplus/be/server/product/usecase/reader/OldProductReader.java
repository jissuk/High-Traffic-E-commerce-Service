//package kr.hhplus.be.server.product.usecase.reader;
//
//import kr.hhplus.be.server.product.domain.model.Product;
//import kr.hhplus.be.server.product.domain.repository.ProductRepository;
//import kr.hhplus.be.server.product.exception.ProductNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ProductReader {
//
//    private final ProductRepository productRepository;
//
//    public Product findProductOrThrow(long id) {
//        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
//    }
//}

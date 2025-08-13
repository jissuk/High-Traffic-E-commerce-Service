package kr.hhplus.be.server.product.scheduler;

import kr.hhplus.be.server.product.usecase.ClearProductSalesCacheUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductSalesCacheClearScheduler {

    private final ClearProductSalesCacheUseCase clearProductSalesCacheUseCase;

    @Scheduled(cron = "0 0 0 * * *")
    public void clearProductSalesCache(){
        clearProductSalesCacheUseCase.execute();
    }
}

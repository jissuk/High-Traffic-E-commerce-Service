package kr.hhplus.be.server.product.scheduler;

import kr.hhplus.be.server.product.usecase.ClearProductSalesCacheUseCase;
import kr.hhplus.be.server.product.usecase.RegisterTop3DaysProductsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductSalesScheduler {

    private final ClearProductSalesCacheUseCase clearProductSalesCacheUseCase;
    private final RegisterTop3DaysProductsUseCase registerTop3DaysProductsUsecase;

    @Scheduled(cron = "0 0 0 * * *")
    public void clearProductSalesCache(){
        clearProductSalesCacheUseCase.execute();
    }

    @Scheduled(cron = "0 5 0 * * *")
    public void registerTop3DaysProducts(){
        registerTop3DaysProductsUsecase.execute();
    }
}

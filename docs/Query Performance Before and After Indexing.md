# Query Performance Before and After Indexing
- **조회 성능 저하 가능성이 있는 기능을 식별**
- **쿼리 실행계획(Explain) 기반으로 문제를 분석** 

---

## [문제] 인덱스 적용 대상 쿼리
조회 성능 저하가 우려되어 인덱스 적용이 필요하다고 생각된 쿼리는 인기 판매 상품 조회입니다.
<br>
현재 쿼리에서는 `product.create_at`, `payment.payment_status` 컬럼에 인덱스가 없으면, MySQL은 테이블을 Full Scan을 수행하기에 성능에 큰 영향을 줄 수 있습니다.

### 인기 판매 상품 조회(3일)
```angular2html
SELECT 
    pr.id, 
    pr.product_name,
    SUM(oi.quantity) AS total_sold
FROM tbl_payment p
JOIN tbl_order_item oi ON p.order_item_id = oi.id
JOIN tbl_product pr ON oi.product_id = pr.id
WHERE p.create_at >= CURDATE() - INTERVAL 3 DAY
  AND p.create_at < CURDATE() - INTERVAL 0 DAY  -- 오늘 제외
  AND p.payment_status = 'COMPLETED'
GROUP BY pr.id, pr.product_name
ORDER BY total_sold DESC
LIMIT 5;
```

---

## [해결] 인덱스 설정 고려사항 및 적용  

인덱스 설계 시 카디널리티를 우선적으로 고려했습니다.
<br> 카디널리티가 높은 컬럼을 먼저 배치하면 이후 적용될 인덱스들이 비교적 적은 데이터 검증하여 성능의 이점이 있기 때문입니다.

선정된 인덱스 컬럼은 아래와 같습니다. 
- `product.create_at`
- `payment.payment_status`
- `payment.order_item_id`(FK)

`product.create_at`와 `payment.payment_status`은 `WHERE`절에서 핕터링 조건으로 사용되므로 검색 범위를 좁히는 역할을 하며
<br>
`payment.order_item_id`는 FK로서 `JOIN`에 활용되므로 조인 성능을 높이는 역할을 합니다.

---

## [성과] 인덱스 적용 후 성능 개선

### 적용 전
![인덱스_적용_전](https://github.com/user-attachments/assets/e1428c27-0151-4160-93d4-a2d28696990a)
<br>
### 적용 후
![인덱스_적용_후](https://github.com/user-attachments/assets/f0e727cf-fd34-4eab-8181-26f1607877c2)

### 쿼리 성능 개선 보고

인덱스 적용 전후 실행 계획을 분석한 결과, 쿼리 성능이 개선된 것을 확인할 수 있습니다.
특히 `tbl_payment.create_at`, `tbl_payment.payment_status`, `tbl_order_item.product_id` 컬럼에 인덱스를 적용한 후 불필요한 전체 테이블 스캔이 줄어들었습니다.

| 구분 | type | rows | Extra | 설명                                                      |
|------|------|------|-------|---------------------------------------------------------|
| 인덱스 적용 전 | ALL | 2,962 | Using where; Using temporary; Using filesort | 테이블 Full Sacn 후 WHERE 조건과 GROUP BY/ORDER BY 수행 |
| 인덱스 적용 후 | range | 1,735 | Using where; Using index; Using temporary; Using filesort | 인덱스를 활용하여 범위 검색 가능, 전체 행의 약 40%만 조회, Full Table Scan 회피 |

### 결론
- 인덱스 적용으로 불필요한 Full Table Scan을 피하고, 검색 대상 데이터 행 수가 감소
- `type`이 ALL → range로 변경되어 쿼리 효율성이 상승

# Query Performance Before and After Indexing
- **조회 성능 저하 가능성이 있는 기능을 식별**
- **쿼리 실행계획(Explain) 기반으로 문제를 분석** 

---

## [문제] 인덱스 적용 대상 쿼리
조회 성능 저하가 우려되어 인덱스 적용이 필요하다고 생각된 쿼리는 인기 판매 상품 조회입니다.
<br>
인기 판매 상품 조회의 쿼리를 보면  `product.create_at`, `payment.payment_status` 컬럼에 인덱스가 없으면, MySQL은 테이블을 Full Scan을 해야하기에 성능 상의 큰 이슈가 생깁니다. 
<br>
그렇기에 인기 판매 상품 조회 쿼리에 인덱스를 적용하고자 하였습니다.

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

인덱스를 설정 할 때 가장 신경썻던 부분은 카디널리티였습니다.

복합인덱스를 작성할 때 카디널리티가 높은 순서로 작성을 하게 되면 
<br>
이후 적용될 인덱스들이 비교적 적은 데이터를 검증함으로서 성능의 이점이 있기 때문입니다.

제가 인덱스 요소로 생각한 컬럼은 `product.create_at`, `payment.payment_status` 그리고 FK로 설정한 `payment.order_item_id`입니다.

`product.create_at`와 `payment.payment_status`은 `WHERE`절에서 핕터링 조건으로 사용되므로 검색 범위를 좁히는 역할을 하며
<br>
`payment.order_item_id`는 FK로서 `JOIN`에 활용되므로 조인 성능을 높이는 역할을합니다.

---

## [성과] 인덱스 적용 후 성능 개선

### 적용 전
![인덱스_적용_전](https://github.com/user-attachments/assets/e1428c27-0151-4160-93d4-a2d28696990a)
<br>
### 적용 후
![인덱스_적용_후](https://github.com/user-attachments/assets/f0e727cf-fd34-4eab-8181-26f1607877c2)

### 쿼리 성능 개선 보고

인덱스 적용 전후 실행 계획을 분석한 결과, 쿼리 성능이 눈에 띄게 개선되었습니다.  
특히 `tbl_payment.create_at`, `tbl_payment.payment_status`, `tbl_order_item.product_id` 컬럼에 인덱스를 적용한 후 불필요한 전체 테이블 스캔이 줄고, 검색 대상 행 수가 감소하여 I/O 부담이 경감되었습니다.

| 구분 | type | rows | Extra | 설명                                                      |
|------|------|------|-------|---------------------------------------------------------|
| 인덱스 적용 전 | ALL | 2,962 | Using where; Using temporary; Using filesort | 테이블 Full Sacn 후 WHERE 조건과 GROUP BY/ORDER BY 수행 |
| 인덱스 적용 후 | range | 1,735 | Using where; Using index; Using temporary; Using filesort | 인덱스를 활용하여 범위 검색 가능, 전체 행의 약 40%만 조회, Full Table Scan 회피 |

### 결론
- 인덱스 적용으로 불필요한 Full Table Scan을 피하고, 검색 대상 데이터 행 수가 감소
- `type`이 ALL → range로 변경되어 쿼리 효율성이 상승
- `Using temporary`와 `Using filesort`는 여전히 존재하므로, 추가적인 GROUP BY/ORDER BY 최적화 가능

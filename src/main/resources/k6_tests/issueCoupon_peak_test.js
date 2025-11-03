import http from 'k6/http';
import { check } from 'k6';

export const options = {
    scenarios: {
        constant_request_rate: {
            executor: 'constant-arrival-rate',
            rate: 1000,               // 초당 1000 요청
            timeUnit: '1s',           // 단위: 1초
            duration: '60s',          // 60초 동안 실행
            preAllocatedVUs: 1000,    // 사전 할당 VU 수
            // maxVUs: 200,           // 필요시 최대 VU까지 확장
        },
    },
};

export default function () {
    const userId = __VU;
    const url = 'http://localhost:8080/coupons/issue';

    const payload = JSON.stringify({
        userId: userId,
        couponId: 1
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    let res = http.post(url, payload, params);

    check(res, { 'status is 200': (r) => r.status === 200 });

}
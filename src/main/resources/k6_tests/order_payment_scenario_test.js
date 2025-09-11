// import http from 'k6/http';
// import { check } from 'k6';
//
// export const options = {
//     scenarios: {
//         constant_request_rate: {
//             executor: 'constant-arrival-rate',
//             rate: 100,             // 초당 500 요청
//             timeUnit: '1s',        // 단위: 1초
//             duration: '1s',        // 30초 동안 실행
//             preAllocatedVUs: 100,  // 사전 할당 VU 수
//             maxVUs: 200,           // 필요시 최대 VU까지 확장
//         },
//     },
//     ext: {
//         loadimpact: {
//             influxdb: {
//                 url: 'http://influxdb:8086',
//                 org: 'hanghae',
//                 bucket: 'k6',
//                 token: 'token',
//             },
//         },
//     },
// };
//
// export default function () {
//     const userId = __VU;
//
//     const orderUrl = 'http://localhost:8080/orders';
//     const orderBody = JSON.stringify({
//         userId: userId,
//         productId: 1,
//         quantity: 1,
//         price: 3000
//     });
//
//     const params = {
//         headers: {
//             'Content-Type': 'application/json',
//         },
//     };
//
//     const orderRes = http.post(orderUrl, orderBody, params);
//
//     check(orderRes, {
//         'orderStatus is 200': (r) => r.status === 200,
//     });
//
//     // body가 문자열이므로 한 번 더 파싱
//     const body = JSON.parse(orderRes.body);
//
//     const orderId = body.data.orderId;
//     const orderItemId = body.data.id;
//
//
//     const paymentUrl = 'http://localhost:8080/payments';
//     const paymentBody = JSON.stringify({
//         userId : userId,
//         orderId : orderId,
//         orderItemId : orderItemId,
//         productId : 1
//     })
//
//     const paymentRes = http.post(paymentUrl, paymentBody, params);
//
//     check(paymentRes, {
//         'paymentStatus is 200': (r) => r.status === 200,
//     });
// }

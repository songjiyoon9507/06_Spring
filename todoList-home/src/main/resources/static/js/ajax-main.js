/* 요소 얻어와서 변수에 저장 */
const totalCount = document.querySelector("#totalCount");
const completeCount = document.querySelector("#completeCount");
const reloadBtn = document.querySelector("reloadBtn");

// 전체 todo개수 조회 및 출력하는 함수 정의
function getTotalCount() {

    // 비동기로 서버(DB)에서 전체 Todo 개수 조회하는
    // fetch() API 코드 작성
    // (fetch : 가지고 오다)

    fetch("/ajax/totalCount") // fetch("요청주소") 비동기 요청 수행 -> Promise 객체 반환
    .then(response => {
        // 응답을 가지고 돌아올 때 객체 하나를 반환해줌
        // Promise 객체 반환 (비동기요청 보냈을 때 받는 응답 형태)
        // .then 은 응답이 됐을 때 돌아온 객체명을 response 라고 하겠다
        // (매개변수형태) 이름은 어떻게 쓰든 상관 없음

        // response : 비동기 요청에 대한 응답이 담긴 객체


    })
    // 두번째 then의 매개변수 (result)
    // == 첫번째 then 에서 반환된 Promise 객체의 PromiseResult 값
    .then(result => {
        // result 매개변수 == Controller 메서드에서 반환된 진짜 값
        
    });
};
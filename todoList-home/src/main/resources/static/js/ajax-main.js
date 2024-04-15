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

        // response : 비동기 요청에 대한 응답이 담긴 객체 (많은 정보가 들어있음)

        return response.text();
        // 응답이 담긴 객체.text() : 응답 데이터를 문자열/숫자 형태로 변환한 결과를 가지는
        // Promise 객체 반환
        // 단일 값으로 넘어오는 값은 .text() 로 두번째 .then한테 넘겨줄 수 있음

    })
    // 두번째 then의 매개변수 (result)
    // == 첫번째 then 에서 반환된 Promise 객체의 PromiseResult 값
    .then(result => {
        // result 매개변수 == Controller 메서드에서 반환된 진짜 값
        
        // 첫번째 then에서 text()로 변환해서 보내준 값을 result 로 받아서
        // #totalCount 요소의 내용을 result로 변경
        totalCount.innerText = result;
    });
};

// 완료된 할 일 개수
// completeCount 값 비동기 통신으로 얻어와서 화면 출력
function getCompleteCount() {

    // fetch() : 비동기로 요청해서 결과 데이터 가져오기
    fetch("/ajax/completeCount")
    .then(resp => resp.text())
    .then(result => {
        completeCount.innerText = result;
    });
};

// 새로 고침 버튼이 클릭 되었을 때
reloadBtn.addEventListener("click", () => {
    getTotalCount(); // 비동기로 전체 할 일 개수 조회
    getCompleteCount(); // 비동기로 완료된 할 일 개수 조회
});

getTotalCount(); // 함수 호출
getCompleteCount(); // 함수 호출
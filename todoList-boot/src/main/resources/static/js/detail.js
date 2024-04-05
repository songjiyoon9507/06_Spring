// 목록으로 버튼 동작
const goToList = document.getElementById("goToList");

goToList.addEventListener("click", () => {
    location.href = "/"; // 메인 페이지 요청
});

// 완료 여부 변경 버튼 동작
const completeBtn = document.querySelector(".complete-btn");

completeBtn.addEventListener("click", (e) => {

    const todoNo = e.target.dataset.todoNo;
    // 요소.dataset 으로 값을 얻어올 수 있음

    // console.log(todoNo);

    // Y <-> N 변경

    let complete = e.target.innerText; // 기존 완료 여부 값 얻어오기

    complete = (complete === 'Y') ? 'N' : 'Y';
    // === 값, 자료형 모두 같은지 확인
    // == 값만 같은지

    // 완료 여부 수정 요청하기
    location.href
        = `/todo/changeComplete?todoNo=${todoNo}&complete=${complete}`;
    // location 으로 보낸 주소도 GET 요청
});

// --------------------------------------------------------------------

// 수정 버튼 클릭 시
const updateBtn = document.querySelector("#updateBtn");

updateBtn.addEventListener("click", e => {
    // data-todo-no 얻어오기
    const todoNo = e.target.dataset.todoNo;

    location.href = `/todo/update?todoNo=${todoNo}`;
});

// --------------------------------------------------------------------

// 삭제 버튼 클릭 시
const deleteBtn = document.querySelector("#deleteBtn");

deleteBtn.addEventListener("click", e => {

    if(confirm("삭제 하시겠습니까?")) { // 확인 누르면 true 반환
        location.href = `/todo/delete?todoNo=${e.target.dataset.todoNo}`;
    }

});
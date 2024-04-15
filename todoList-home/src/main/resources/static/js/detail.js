// 목록으로 버튼 동작
const goToList = document.querySelector("#goToList");

goToList.addEventListener("click", () => {
    // 메인 페이지로 재요청
    location.href = "/";
    // 메인 페이지 요청
    // javaScript window 객체 속성 중 하나
    // 현재 문서의 URL 을 나타냄
});

// 완료 여부 버튼 클릭 시 Y <-> N
// 완료 여부 변경 버튼 동작
const completeBtn = document.querySelector(".complete-btn");

completeBtn.addEventListener("click", e => {
    // e -> event 객체
    // data-todo-no 얻어올 거임
    // 요소.dataset 으로 얻어올 수 있음
    const todoNo = e.target.dataset.todoNo;

    // Y <-> N 변경
    // 현재 상태가 Y 인지 N 인지가 중요함
    // 기존 완료 여부값 얻어오기
    let complete = e.target.innerText;
    // innerText 가 Y 인지 N 인지

    // complete 가 Y 이면 N 으로 보내주고 N 이면 Y 로 보내줌
    complete = (complete === 'Y') ? 'N' : 'Y';

    // 완료 여부 수정 요청하기 (요청 주소 작성)
    location.href = `/todo/changeComplete?todoNo=${todoNo}&complete=${complete}`;

    // /todo/changeComplete?todoNo=3&complete=Y
    // -> 이런 식으로 넘어갈 거임
});

// 수정 버튼 클릭 시 동작
const updateBtn = document.querySelector("#updateBtn");

updateBtn.addEventListener("click", e => {

    // data-todo-no 에 실어둔 todoNo 얻어오기
    const todoNo = e.target.dataset.todoNo;

    location.href = `/todo/update?todoNo=${todoNo}`;
});

// -------------------------------------------------

// 삭제 버튼 클릭 시
const deleteBtn = document.querySelector("#deleteBtn");

deleteBtn.addEventListener("click", e => {

    if (confirm("정말 삭제하시겠습니까?")) {
        // 확인 눌렀을 때
        location.href = `/todo/delete?todoNo=${e.target.dataset.todoNo}`;
    }
});
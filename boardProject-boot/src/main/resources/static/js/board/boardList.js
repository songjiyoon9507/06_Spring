/* 글쓰기 버튼 클릭 시 */
const insertBtn = document.querySelector("#insertBtn");

// 글쓰기 버튼이 존재할 때 (로그인 상태인 경우)
if(insertBtn != null) {
    insertBtn.addEventListener("click", () => {

        // get 방식 요청
        // /editBoard/게시판코드/insert
        // 게시판코드 사용하려면 html 에서 requestScope 에
        // 실려있는 boardCode를 세팅해서 넘겨줘야함
        location.href = `/editBoard/${boardCode}/insert`;
    });
};
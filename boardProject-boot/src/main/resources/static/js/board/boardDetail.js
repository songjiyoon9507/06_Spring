console.log("boardDetail.js 연결 확인");

/* 좋아요 버튼(하트) 클릭 시 비동기로 좋아요 INSERT/DELETE */

// Thymeleaf 코드 해석 순서
// 1. th: 코드(java) + Spring EL
// 2. html 코드 (+ css, js)

// 1) 로그인한 회원 번호 준비
//    --> session 에서 얻어오기 (session은 서버에서 관리하기 때문에 JS에서 바로 얻어올 방법 없음)
// 2) 현재 게시글 번호 준비
// 3) 좋아요 여부 준비

// 1. #boardLike 가 클릭 되었을 때
document.querySelector("#boardLike").addEventListener("click", e => {

    // 2. 로그인 상태가 아닌 경우 동작 X
    if(loginMemberNo == null) {
        alert("로그인 후 이용해주세요");
        return;
    }

    // 3. 준비된 3개의 변수를 객체로 저장 (JSON 변환 예정)
    const obj = {
        "memberNo"  : loginMemberNo,
        "boardNo"   : boardNo,
        "likeCheck" : likeCheck
    };

    // 4. 좋아요 INSERT/DELETE 비동기 요청
    fetch("/board/like", {
        method : "POST",
        headers : {"Content-Type" : "application/json"},
        body : JSON.stringify(obj)
    })
    .then(resp => resp.text()) // 반환 결과 text 형태로 변환
    .then(count => {

        // 잘못됐을 때 -1 들어옴
        if(count == -1) {
            console.log("좋아요 처리 실패");
            return;
        }

        // 5. likeCheck 값 0 <-> 1 변환
        // -> 클릭 될 때 마다 INSERT/DELETE 동작을 번갈아 가면서 하게끔
        likeCheck = likeCheck == 0 ? 1 : 0;

        // 6. 하트를 채웠다/비웠다 바꾸기 toggle
        e.target.classList.toggle("fa-regular");
        e.target.classList.toggle("fa-solid");

        // 7. 게시글 좋아요 수 수정 nextSibling 이 좋아요 개수 넣어둔 span 태그
        e.target.nextElementSibling.innerText = count;
    })

});

// ----------------------------------------------------------------------------

/* 게시글 수정 버튼 클릭 시 */

const updateBtn = document.querySelector("#updateBtn");

// 화면상에 수정 버튼이 존재할 때
if(updateBtn != null) {
    
    updateBtn.addEventListener("click", () => {

        // GET 방식
        // 목표 : /editBoard/1/1990/update
        // 게시글 상세 조회 시 현재 경로 : /board/1/1990?cp=1
        location.href = location.pathname.replace('board', 'editBoard') + "/update" + location.search;
        // location. 현재 경로 뽑아내서
        // location.search 물음표 뒷부분부터 찾아서 붙여줌
        // -> /editBoard/1/1990/update?cp=1
    });
};

// --------------------------------------------------------------------------------

/* 게시글 삭제 버튼 클릭 시 */
// Get 요청
const deleteBtn = document.querySelector("#deleteBtn");

if(deleteBtn != null) {

    deleteBtn.addEventListener("click", () => {
        location.href = location.pathname.replace('board', 'editBoard') + "/delete" + location.search;
        // -> /editBoard/1/2006/delete?cp=1
    });
};
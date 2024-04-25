/* 글쓰기 버튼 클릭 시 */
const insertBtn = document.querySelector("#insertBtn");

if(insertBtn != null) {
    insertBtn.addEventListener("click", () => {
        location.href = `/editBoard/${boardCode}/insert`;
    });
};
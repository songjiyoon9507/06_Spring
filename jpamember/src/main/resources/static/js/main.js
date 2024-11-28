console.log("main.js 연결 확인");

const jpaList = document.querySelector("#jpaList");
const jpaRegist = document.querySelector("#jpaRegist");

jpaList.addEventListener("click", () => {
    location.href = "/jpa/memberList";
})

jpaRegist.addEventListener("click", () => {
    location.href = "/jpa/memberWriteForm";
})
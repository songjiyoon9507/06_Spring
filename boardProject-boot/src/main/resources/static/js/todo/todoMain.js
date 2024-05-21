console.log("todoMain.js 연결 확인");

const todoAddBtn = document.querySelector("#todo-add-btn");

const popupLayer = document.querySelector("#popupLayer");
const updateLayer = document.querySelector("#updateLayer");

const updateTitle = document.querySelector("#updateTitle");
const updateContent = document.querySelector("#updateContent");

const myPageSubmit = document.querySelector(".myPage-submit");

if(todoAddBtn != null) {

    todoAddBtn.addEventListener("click", () => {
        
        // 할 일 추가하기 눌렀을 때 요청자는 자기 자신.
        // loginMemberNo 를 보내줘야함
        // 기본 담당자도 자기 자신 선택된 상태

        // popupLayer.classList.remove("popup-hidden");
        updateLayer.classList.remove("popup-hidden");

        const obj = {
            "memberNo" : loginMemberNo,
            "todoTitle" : updateTitle,
            "todoContent" : updateContent
        };

    });

};

if(myPageSubmit != null) {

    myPageSubmit.addEventListener("click", () => {
        console.log(uploadFile.get(0));
    })
}

const updateCancel = document.querySelector("#updateCancel");

if(updateCancel != null) {

    updateCancel.addEventListener("click", () => {

        updateLayer.classList.add("popup-hidden");
    });
};
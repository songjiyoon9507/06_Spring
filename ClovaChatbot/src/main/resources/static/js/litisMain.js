console.log("litisMain.js 연결 확인");

// 사각형 클릭 시 +1
const addNum = document.querySelector(".red-square");

if(addNum != null) {

    addNum.addEventListener("click", () => {
        let num = addNum.innerText;
        num++;
        addNum.innerText = num;
    })
}
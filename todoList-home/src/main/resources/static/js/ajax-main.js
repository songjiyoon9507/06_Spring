/* 요소 얻어와서 변수에 저장 */
const totalCount = document.querySelector("#totalCount");
const completeCount = document.querySelector("#completeCount");
const reloadBtn = document.querySelector("#reloadBtn");

// 할 일 추가 관련 요소
const todoTitle = document.querySelector("#todoTitle");
const todoContent = document.querySelector("#todoContent");
const addBtn = document.querySelector("#addBtn");

// 할 일 목록 조회 관련 요소
const tbody = document.querySelector("#tbody");

// 할 일 상세 조회 관련 요소
const popupLayer = document.querySelector("#popupLayer");
const popupTodoNo = document.querySelector("#popupTodoNo");
const popupTodoTitle = document.querySelector("#popupTodoTitle");
const popupClose = document.querySelector("#popupClose");
const popupComplete = document.querySelector("#popupComplete");
const popupRegDate = document.querySelector("#popupRegDate");
const popupTodoContent = document.querySelector("#popupTodoContent");

// 상세 조회 팝업 레이어에 있는 버튼
const changeComplete = document.querySelector("#changeComplete");
const updateView = document.querySelector("#updateView");
const deleteBtn = document.querySelector("#deleteBtn");

// 수정 레이어 버튼
const updateLayer = document.querySelector("#updateLayer");

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

// ------------------------------------------------------

// 할 일 추가 버튼 클릭 시 동작
addBtn.addEventListener("click", () => {

    // 비동기로 할 일 추가하는 fetch() 코드 작성
    // - 요청 주소 : "/ajax/add"
    // - 데이터 전달 방식(method) : "POST" 방식

    // 파라미터를 저장한 JS 객체 만들기 (Key : Value)
    const param = {
        "todoTitle" : todoTitle.value,
        "todoContent" : todoContent.value
    };

    // JS 객체는 java 에서 쓸 수 없음
    // 그대로 이용할 수 없기 때문에 중간 과정 필요함
    // java 와 JS 둘 다 쓸 수 있는 JSON 이용할 거임
    // JS -JSON-> java

    fetch("/ajax/add", {
        // key : value
        // 옵션에 대한 key value 형태로 작성
        method : "POST", // POST 방식 요청
        headers : {"Content-Type" : "application/json"}, // 요청 데이터 json 형태로 변환해서 보내겠다.
        body : JSON.stringify(param) // param JS 객체를 JSON 으로 변환해주는 코드
        // JSON 은 Key Value 모두 String 형태
    })
    .then(resp => resp.text()) // 반환된 값을 text로 변환
    .then(result => {
        // 첫번째 then 에서 반환된 값 중 변환된 text를 result에 저장

        if(result > 0) { // 성공
            alert("추가 성공");

            // 추가 성공한 제목, 내용 지우기
            // 비동기 요청이라서 새로고침 안됨
            todoTitle.value = "";
            todoContent.value = "";

            // 할 일이 추가되었기 때문에 전체 Todo 개수 다시 조회
            getTotalCount();

            // 할 일 목록 다시 조회
            selectTodoList();
        } else { // 실패
            alert("추가 실패");
        }
    });
});

// --------------------------------------------------------------

// 할 일 목록에서 할 일 제목 a 태그 누르면 상세 조회 되게 만들 거
// 비동기(ajax)로 할 일 상세 조회하는 함수
const selectTodo = (url) => {
    // 함수에서 받는 매개변수
    // url == "/ajax/detail?todoNo=" + todo.todoNo 형태의 문자열

    // response.json() : 
    // - 응답 데이터가 JSON인 경우
    //   이를 자동으로 Object 형태로 변환하는 메서드
    //   == JSON.parse(JSON 데이터)
    // 단일 값은 .text()
    // java 객체 형태는 .json()
    // java 단에서 JSON 으로 넘겼을 때 사용

    fetch(url)
    .then(resp => resp.json())
    .then(todo => {
        // 매개변수 todo :
        // - 서버 응답(JSON)이 Object로 반환된 값

        // popup Layer에 조회된 값을 출력
        popupTodoNo.innerText = todo.todoNo; // key 값으로 접근해서 value 값 꺼내옴
        popupTodoTitle.innerText = todo.todoTitle;
        popupComplete.innerText = todo.complete;
        popupRegDate.innerText = todo.regDate;
        popupTodoContent.innerText = todo.todoContent;

        // popup Layer 보이게 하기
        popupLayer.classList.remove("popup-hidden");

    });
}

// fetch(url) // 누르는 거에 따라 url 주소 달라짐
// .then(resp => resp.text())
// .then(result => {

//     // result 에는 JSON 형태 string 으로 값이 들어있음
//     const todo = JSON.parse(result);
//     // JSON.parse(result) => String 형태의 JSON 을 JS 객체로 만들어주는 거

// });

// ---------------------------------------------------------------

// 상세 조회 창에서 x 버튼 클릭시 닫히게하기
// popup Layer의 x 버튼 (#popupClose) 클릭 시 닫기
popupClose.addEventListener("click", () => {
    // 숨기는 class 추가
    popupLayer.classList.add("popup-hidden");
})

// ---------------------------------------------------------------

// 비동기로 할 일 목록 조회하는 함수
const selectTodoList = () => {

    fetch("/ajax/selectList")
    .then(resp => resp.text()) // 응답 결과를  text로 변환
    .then(result => {
        // result 는 객체가 아닌 문자열 형태

        // 문자열은 가공은 할 수 있지만 사용하기가 힘듦
        // JSON.parse(JSON데이터) 이용

        // JSON.parse(JSON데이터) : string -> object
        // - string 형태의 JSON 데이터를 JS Object 타입으로 변환

        // JSON.stringify(JS Object) : object -> string
        // - JS Object 타입을 String 형태의 JSON 데이터로 변환
        const todoList = JSON.parse(result);
        // JS 객체 형태로 변환 K : V

        // 기존에 출력되어있던 할 일 목록을 모두 삭제
        tbody.innerHTML = "";

        // #tbody 에 tr/td 요소를 생성해서 내용 동적으로 추가하기
        // 하나씩 접근해서 꺼내줘야함
        // 향상된 for문 이용
        // 객체를 key 로 접근 하는 건 for in
        for(let todo of todoList) {

            // tr 태그 생성
            const tr = document.createElement("tr");

            // 접근할 때 key 값으로 쓸 거
            const arr = ['todoNo', 'todoTitle', 'complete', 'regDate'];

            for(let key of arr) {
                const td = document.createElement("td");

                // 제목인 경우
                if(key === 'todoTitle') {
                    const a = document.createElement("a"); // a태그 생성
                    // 제목 클릭하면 상세조회 해야해서
                    a.innerText = todo[key]; // 제목을 a 태그 내용으로 대입한 거 key 가 todoTitle 이니까
                    a.href = "/ajax/detail?todoNo=" + todo.todoNo;
                    td.append(a);
                    tr.append(td);

                    // a 태그 클릭 시 기본 이벤트(페이지 이동) 막기
                    // -> 동기 요청이라서
                    a.addEventListener("click", (e) => {
                        e.preventDefault(); // 기본 이벤트 막기

                        // 할 일 상세 조회 비동기 요청
                        selectTodo(e.target.href);
                        // e.target.href : 클릭된 a 태그의 href 속성 값을 넘겨줄 거라는 뜻
                        // "/ajax/detail?todoNo=" + todo.todoNo;
                    });

                    continue; // 다음 코드로 넘어가라
                }

                td.innerText = todo[key];
                tr.append(td);
            }

            // tbody 의 자식으로 tr(한 행) 추가
            tbody.append(tr);
        }

    });
};

// -----------------------------------------------

// 삭제 버튼 클릭 시
deleteBtn.addEventListener("click", () => {

    // 정말 삭제하시겠습니까? 물어보고
    // 취소 클릭 시 아무것도 안함
    if(!confirm("정말 삭제하시겠습니까?")) {
        // 부정논리연산자 붙여서 취소 클릭했을 때 if문 수행
        return;
    }

    // 확인 버튼 클릭 시
    // 현재 삭제하려는 할 일 번호 얻어오기
    const todoNo = popupTodoNo.innerText;
    // 삭제 버튼은 상세 조회 모달창 떴을 때 뜨는 버튼
    // #popupTodoNo 내용 얻어오기

    // 요청할 때 todoNo 같이 보낼 거임
    // 비동기 DELETE 방식 요청
    fetch("/ajax/delete", {
        method : "DELETE", // DELETE 방식 요청 -> Controller 에서 @DeleteMapping 으로 요청 처리
        headers : {"Content-Type" : "application/json"},
        // 데이터 하나를 전달하더라도 JSON 방식이란 걸 알려줘야해서
        // application/json 작성해줘야함
        body : todoNo
        // todoNo 값을 body 에 담아서 전달하겠다.
        // -> Controller에서 @RequestBody 로 꺼냄
    })
    .then(resp => resp.text())
    .then(result => {

        if(result > 0) {
            // 삭제 성공
            alert("삭제 성공");
            // 삭제 후 상세조회 창 닫아줄 거
            popupLayer.classList.add("popup-hidden");

            // 전체, 완료된 할 일 개수 다시 조회
            // + 할 일 목록 다시 조회
            selectTodoList();
            getTotalCount();
            getCompleteCount();
        } else {
            // 삭제 실패
            alert("삭제 실패");
        }
    })

});

// -----------------------------------------------------------

// 완료 여부 변경 버튼 클릭 시
changeComplete.addEventListener("click", () => {

    // 변경할 할 일 번호, 완료 여부 (Y <-> N)
    // js 에서 완료 여부를 반대로 바꿔서 java 에 넘겨줄 거임
    // -> SQL 에서 삽입하기 편하게
    const todoNo = popupTodoNo.innerText;
    // 삼항 연산자 이용해서 완료 여부 바꿔서 대입해줄 거임
    const complete = popupComplete.innerText === 'Y' ? 'N' : 'Y';
    // Y 이면 N 으로 Y가 아니라면(== N) Y로

    // SQL 수행에 필요한 값을 JS 객체 형태로 묶음
    const obj = {"todoNo" : todoNo, "complete" : complete};

    // 비동기로 완료 여부 변경
    fetch("/ajax/changeComplete", {
        method : "PUT",
        headers : {"Content-Type" : "application/json"},
        body : JSON.stringify(obj) // obj 값을 JSON 으로 변환해서 java 로 보내줌
    })
    .then(resp => resp.text())
    .then(result => {

        // result 성공한 행의 개수, 실패했으면 0
        // 분기 처리
        if(result > 0) {

            // 성공했을 때 update된 DB 데이터를 다시 조회해서 화면에 출력
            // -> 서버 부하가 큼

            // selectTodo();
            // => 서버 부하를 줄이기 위해 상세 조회에서 Y/N 만 바꾸기
            popupComplete.innerText = complete;

            // 완료된 Todo 개수도 바꿔줘야함
            // 뒤에 화면에서도 Y, N 바뀐 거 알려줘야함

            // getCompleteCount();
            // 서버 부하를 줄이기 위해 완료된 Todo 개수 +-1
            // innerText 로 가져오면 타입이 String
            // 숫자형태로 변경해줘야함
            const count = Number(completeCount.innerText);

            if(complete === 'Y') completeCount.innerText = count + 1;
            else completeCount.innerText = count - 1;

            // 서버 부하 줄이기 가능은 하지만 코드가 복잡해서 다시 호출
            selectTodoList();

        } else {
            // 실패했을 때
            alert("완료 여부 변경 실패");
        }
    });
});

// -------------------------------------------------------------

// 상세 조회에서 수정 버튼 (#updateView) 클릭 시
updateView.addEventListener("click", () => {

    // 기존 팝업 레이어는 숨기고
    popupLayer.classList.add("popup-hidden");
});

selectTodoList();
getTotalCount(); // 함수 호출
getCompleteCount(); // 함수 호출
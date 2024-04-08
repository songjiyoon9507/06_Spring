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
const popupComplete = document.querySelector("#popupComplete");
const popupRegDate = document.querySelector("#popupRegDate");
const popupTodoContent = document.querySelector("#popupTodoContent");
const popupClose = document.querySelector("#popupClose");

// 상세 조회 버튼
const deleteBtn = document.querySelector("#deleteBtn");
const updateView = document.querySelector("#updateView");
const changeComplete = document.querySelector("#changeComplete");

// 전체 Todo 개수 조회 및 출력하는 함수 정의
function getTotalCount() {

    // 비동기로 서버(DB)에서 전체 Todo 개수 조회하는
    // fetch() API 코드 작성
    // (fetch : 가지고 오다) 기본 GET 요청

    fetch("/ajax/totalCount") // 비동기 요청 수행 -> Promise 객체 반환 (응답 형태)
    .then(response => {
        // response : 비동기 요청에 대한 응답이 담긴 객체 (매개변수명)

        console.log("response : ", response);
        // response.text() : 응답 데이터를 문자열/숫자 형태로 변환한
        //                   결과를 가지는 Promise 객체 반환 (두번째 then 한테 넘겨줌)
        return response.text();
    })
    // 두 번째 then의 매개변수 (result)
    // == 첫 번째 then에서 반환된 Promise 객체의 PromiseResult 값 (응답값)
    .then(result => {
        // result 매개변수 == Controller 메서드에서 반환된 값
        console.log("result : ", result);

        // #totalCount 요소의 내용을 result 변경
        totalCount.innerText = result;
    });
}

// completeCount 값 비동기 통신으로 얻어와서 화면 출력

// 완료된 Todo 개수
function getCompleteCount() {
    
    // fetch() : 비동기로 요청해서 결과 데이터 가져오기
    
    // 첫 번째 then의 response :
    // - 응답 결과, 요청 주소, 응답 데이터 등이 담겨있음
    
    // response.text() : 응답 데이터를 text 형태로 변환
    
    // 두 번째 then 의 result
    // - 첫 번째 then 에서 text로 변환된 응답 데이터
    fetch("/ajax/completeCount")
    .then(response => {
        return response.text();
    })
    .then(result => {
        // #completeCount 요소에 내용으로 result 값 출력
        completeCount.innerText = result;
    });
    
}

// 새로고침 버튼이 클릭 되었을 때
reloadBtn.addEventListener("click", () => {
    getTotalCount(); // 비동기로 전체 할 일 개수 조회
    getCompleteCount(); // 비동기로 완료된 할 일 개수 조회
});

// -----------------------------------------------------

// 할 일 추가 버튼 클릭 시 동작
addBtn.addEventListener("click", () => {

    // 비동기로 할 일 추가하는 fetch() 코드 작성
    // - 요청 주소 : "/ajax/add"
    // - 데이터 전달 방식(method) : "POST" 방식

    // 파라미터를 저장한 JS 객체
    const param = {
        // Key        : Value
        "todoTitle"   : todoTitle.value,
        "todoContent" : todoContent.value
    };

    /* javaScript 객체 형태를 java 에서 그대로 쓸 수 없음 json 사용할거임 */

    fetch("/ajax/add" , { // ajax 앞에 / 빼먹으면 안됨
        // 옵션에 대한 key, value 형태로 작성
        // key : value
        method : "POST", // POST 방식 요청
        headers : {"Content-Type" : "application/json"}, // 요청 데이터의 형식을 JSON으로 지정해서 보냄
        body : JSON.stringify(param) // param 객체를 JSON(string)으로 변환
    })
    .then(resp => resp.text()) // 반환된 값을 text로 변환
    .then(temp => { // 첫번째 then 에서 반환된 값 중 변환된 text를 temp에 저장

        if(temp > 0) { // 성공
            alert("추가 성공");

            // 추가 성공한 제목, 내용 지우기
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

// 비동기(ajax)로 할 일 상세 조회하는 함수
const selectTodo = (url) => {
    // 매개변수 url == "/ajax/detail?todoNo=10" 형태의 문자열

    // response.json() :
    // - 응답 데이터가 JSON인 경우
    //   이를 자동으로 Object 형태로 변환하는 메서드
    //   == JSON.parse(JSON 데이터)
    fetch(url)
    .then(resp => resp.json())
    .then(todo => {
        // 매개 변수 todo :
        // - 서버 응답(JSON)이 Object로 변환된 값

        // const todo = JSON.parse(result);
        // javaScript 객체 형태로 바꿔주는 애 parse string으로 넘어온 값을

        // console.log(todo);
        console.log(todo);

        // popup Layer에 조회된 값을 출력
        popupTodoNo.innerText = todo.todoNo;
        popupTodoTitle.innerText = todo.todoTitle;
        popupComplete.innerText = todo.complete;
        popupRegDate.innerText = todo.regDate;
        popupTodoContent.innerText = todo.todoContent;

        // popup layer 보이게 하기
        popupLayer.classList.remove("popup-hidden");
    });
};

// -----------------------------------------------------

// popup layer의 x 버튼 (#popupClose) 클릭 시 닫기

popupClose.addEventListener("click", () => {
    popupLayer.classList.add("popup-hidden");
});

// 비동기로 할 일 목록을 조회하는 함수
const selectTodoList = () => {

    fetch("/ajax/selectList")
    .then(resp => resp.text()) // 응답 결과를 text로 변환
    .then(result => {
        console.log(result);
        console.log(typeof result); // 객체가 아닌 문자열 형태

        // 문자열은 가공은 할 수 있지만 너무 힘들다.
        // -> JSON.parse(JSON데이터) 이용

        // JSON.parse(JSON데이터) : string -> object
        // - string 형태의 JSON 데이터를 JS Object 타입으로 변환

        // JSON.stringify(JS Object) : object -> string
        // - JS Object 타입을 string 형태의 JSON 데이터로 변환
        const todoList = JSON.parse(result);

        console.log(todoList);

        // ------------------------------------------------------

        // 기존에 출력되어있던 할 일 목록을 모두 삭제
        tbody.innerHTML = "";

        // #tbody에 tr/td 요소를 생성해서 내용 추가
        for(let todo of todoList) { // 향상된 for문

            // tr 태그 생성
            const tr = document.createElement("tr");

            const arr = ['todoNo', 'todoTitle', 'complete', 'regDate'];

            for(let key of arr) {
                const td = document.createElement("td");

                // 제목인 경우
                if(key === 'todoTitle') {
                    const a = document.createElement("a"); // a 태그 생성
                    a.innerText = todo[key]; // 제목을 a 태그 내용으로 대입
                    a.href = "/ajax/detail?todoNo=" + todo.todoNo;
                    td.append(a);
                    tr.append(td);

                    // a 태그 클릭 시 기본 이벤트(페이지 이동) 막기
                    a.addEventListener("click", (e) => {
                        e.preventDefault(); // 기본 이벤트 막아주는 메서드
                        // 페이지 이동하는 게 동기 요청이라서 막고 비동기 요청으로
                        // 할 일 상세 조회 비동기 요청
                        selectTodo(e.target.href);
                        // e.target.href : 클릭된 a태그의 href 속성 값
                    });

                    continue;
                }

                td.innerText = todo[key];
                tr.append(td);
            }
            // tbody의 자식으로 tr(한 행) 추가
            tbody.append(tr);
        }
    });
};

// -------------------------------------------------

// 삭제 버튼 클릭 시
deleteBtn.addEventListener("click", () => {

    // 취소 클릭 시 아무것도 안함
    if( !confirm("정말 삭제하시겠습니까?") ) { return; }

    // 삭제할 할 일 번호 얻어오기
    const todoNo = popupTodoNo.innerText; // #popupTodoNo 내용 얻어오기

    // 비동기 DELETE 방식 요청 ajax REST API
    fetch("/ajax/delete", {
        method : "DELETE", // DELETE 방식 요청 -> @DeleteMapping 처리
        
        // 데이터 하나를 전달해도 application/json 작성
        headers : {"content-type" : "application/json"},
        body : todoNo // todoNo 값을 body에 담아서 전달
        //               -> @RequestBody로 꺼냄
    })
    .then(resp => resp.text())
    .then(result => {
        if(result > 0) { // 삭제 성공
            alert("삭제 되었습니다.");

            // 상세 조회 창 닫기
            popupLayer.classList.add("popup-hidden");

            // 전체, 완료된 할 일 개수 다시 조회
            // + 할 일 목록 다시 조회
            getTotalCount();
            getCompleteCount();
            selectTodoList();

        } else { // 삭제 실패
            alert("삭제 실패");
        }
    });
});

changeComplete.addEventListener("click", () => {
    let complete = popupComplete.innerText;
    const todoNo = popupTodoNo.innerText;

    console.log(complete);
    console.log(todoNo);

    complete = (complete === 'Y') ? 'N' : 'Y';
    
    popupComplete.innerHTML = complete;
    const param = {
        // Key     : Value
        "todoNo"   : todoNo,
        "complete" : complete
    };

    fetch("/ajax/changeComplete", {
        method : "PUT",

        headers : {"content-type" : "application/json"},
        body : JSON.stringify(param)
    })
    .then(resp => resp.text())
    .then(result => {
        if(result > 0) { // 수정 성공
            alert("수정 완료");

            popupLayer.classList.add("popup-hidden");

            getTotalCount();
            getCompleteCount();
            selectTodoList();
        } else {
            alert("수정 실패");
        }
    });
});

selectTodoList();

getTotalCount(); // 함수 호출
getCompleteCount();
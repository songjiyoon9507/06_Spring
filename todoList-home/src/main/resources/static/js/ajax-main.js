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
                        // selectTodo(e.target.href);
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

getTotalCount(); // 함수 호출
getCompleteCount(); // 함수 호출
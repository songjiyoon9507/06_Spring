// console.log("main.js loaded.");

// 쿠키에서 key가 일치하는 value 얻어오기 함수
// 쿠키는 "K=V; K=V; K=V; ..." 형식

const getCookie = (key) => {

    // document.cookie = "test" + "=" + "유저일";

    const cookies = document.cookie;

    // cookies 문자열을 배열 형태로 변환 saveId=user01@kh.or.kr;
    const cookieList = cookies.split("; ") // ["K=V", "K=V", "K=V"]
                        .map(el => el.split("="));

    // 배열.map(함수) : 배열의 각 요소를 이용해 함수 수행 후
    //                 결과 값으로 새로운 배열을 만들어서 반환                    
    // => ["K","V"]["K","V"],["K","V"]...
    // console.log(cookieList);

    // console.log(cookies);

    // 배열을 객체 형태로 변환 (다루기 쉽게)
    const obj = {}; // 비어있는 객체 선언

    // for 문 이용해서 obj 값 채워주기
    for(let i = 0 ; i < cookieList.length ; i++) {
        const k = cookieList[i][0]; // key 값 i 번째 인덱스의 0번 인덱스
        const v = cookieList[i][1]; // value  값
        obj[k] = v; // 객체에 추가
    }

    // console.log(obj);

    return obj[key]; // 매개변수로 전달 받은 key와
                     // obj 객체에 저장된 키가 일치하는 요소의 value 값 반환
}

// console.log(getCookie("saveId"));

// memberEmail 요소 얻어오기
const loginEmail = document.querySelector("#loginForm input[name='memberEmail']");
// 아이디가 loginForm 인 애의 자식 중 input 태그의 name 속성값이 memberEmail인 요소 얻어오기

// 로그인 안된 상태인 경우에 수행
// 로그인이 안된 상태에서만 input 창이 보임
// 아래 처리 안해주면 로그인이 된 상태에서 javaScript 에러남
if(loginEmail != null) {
    // 로그인 창에 이메일 입력부분이 화면에 있을 때

    // 쿠키 중에 key 값이 "saveId"인 요소의 value 얻어오기
    const saveId = getCookie("saveId"); // undefined 또는 이메일

    // 체크 박스 체크 된 경우 (saveId 값이 있을 경우)
    if(saveId != undefined) {
        loginEmail.value = saveId; // 쿠키에서 얻어온 값을 input 에 value 로 세팅

        // 아이디 저장 체크박스에 체크 해두기
        document.querySelector("input[name='saveId']").checked = true;
    }
};

// 이메일, 비밀번호 미작성 시 로그인 막기
const loginForm = document.querySelector("#loginForm");

const loginPw = document.querySelector("#loginForm input[name='memberPw']");

// #loginForm이 화면에 존재할 때 (로그인된 상태에는 loginForm 없음)
if(loginForm != null) {

    // 제출 이벤트 발생 시 (submit)
    loginForm.addEventListener("submit", e => {

        // 이메일 미작성 처리
        if(loginEmail.value.trim().length === 0) {
            // 아무것도 작성 안됐을 때
            alert("이메일을 작성해주세요");
            // 기본 이벤트 제출을 막아야함
            e.preventDefault();
            // email 작성하는 곳으로 초점 이동
            loginEmail.focus();
            return;
        }

        // 비밀번호 미작성
        if(loginPw.value.trim().length === 0) {
            // 아무것도 작성 안됐을 때
            alert("비밀번호를 작성해주세요");
            // 기본 이벤트 제출을 막아야함
            e.preventDefault();
            // email 작성하는 곳으로 초점 이동
            loginPw.focus();
            return;
        }
    });
}
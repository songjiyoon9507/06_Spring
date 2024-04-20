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

// -------------------------------------------------

/* 빠른 로그인 */
const quickLoginBtns = document.querySelectorAll(".quick-login");

quickLoginBtns.forEach((item, index) => {
    // item : 현재 반복 시 꺼내온 객체
    // index : 현재 반복 중인 인덱스

    // quickLoginBtns 요소인 button 태그 하나씩 꺼내서 이벤트 리스너 추가
    item.addEventListener("click", () => { // 각 버튼에 클릭 이벤트 추가
        const email = item.innerText; // 버튼에 작성된 이메일 얻어오기

        location.href = "/member/quickLogin?memberEmail=" + email;
    })
});

// --------------------------------------------------------

/* 회원 목록 조회(비동기) */

// 조회 버튼
const selectMemberList = document.querySelector("#selectMemberList");

// tbody
const memberList = document.querySelector("#memberList");

// td 요소를 만들고 text 추가 후 반환
const createTd = (text) => {
    const td = document.createElement("td");
    td.innerText = text;
    return td;
}

// 조회 버튼 클릭 시
selectMemberList.addEventListener("click", () => {

    // 1) 비동기로 회원 목록 조회
    // (포함될 회원 정보 : 회원번호, 이메일, 닉네임, 탈퇴여부)

    fetch("/member/selectMemberList")
    .then(resp => resp.json())
    .then(list => {
        // list 바로 이용 -> JS 객체 배열

        // 이전 내용 삭제
        memberList.innerHTML = "";

        //tbody에 들어갈 요소를 만들고 값 세팅 후 추가
        list.forEach((member,index) => {
            // member : 현재 반복 접근 중인 요소
            // index : 현재 접근 중인 인덱스

            // tr 만들어서 그 안에 td 만들고, append 후
            // tr 을 tbody에 append

            const keyList = ['memberNo', 'memberEmail', 'memberNickname', 'memberDelFl'];

            const tr = document.createElement("tr");

            keyList.forEach(key => tr.append(createTd(member[key])));

            // tbody 자식으로 tr 추가
            memberList.append(tr);
        });
    })
});

// ---------------------------------------------------------------

/* 특정 회원 비밀번호 초기화 */
const resetMemberNo = document.querySelector("#resetMemberNo");
const resetPw = document.querySelector("#resetPw");

resetPw.addEventListener("click", () => {

    // 입력 받은 회원 번호 얻어오기
    const inputNo = resetMemberNo.value;

    if(inputNo.trim().length === 0) {
        alert("회원 번호를 입력해주세요.");
        return;
    }

    fetch("/member/resetPw", {
        method : "PUT", // PUT : 수정 요청 방식
        headers : {"Content-Type" : "application/json"},
        body : inputNo
    })
    .then(resp => resp.text())
    .then(result => {

        if(result > 0) {
            alert("초기화 성공");
        } else {
            alert("해당 회원이 존재하지 않습니다.");
        }
    });
});

// ----------------------------------------------------

/* 특정 회원 탈퇴 복구 */
const restorationBtn = document.querySelector("#restorationBtn");
const restorationMemberNo = document.querySelector("#restorationMemberNo");

restorationBtn.addEventListener("click", () => {

    if(restorationMemberNo.value.trim().length == 0) {
        alert("회원번호를 입력해주세요");
        return;
    } else {
        
        const memberNo = restorationMemberNo.value;
    
        fetch("/member/restorationMemberNo", {
            method : "PUT",
            headers : {"Content-Type" : "application/json"},
            body : memberNo
        })
        .then(resp => resp.text())
        .then(result => {
            // update 결과값 반환
            if(result > 0) {
                restorationMemberNo.value = "";
                alert("탈퇴 복구 성공");
            } else {
                restorationMemberNo.value = "";
                alert("존재하지 않는 회원 번호입니다.");
            }
        })
    }

});
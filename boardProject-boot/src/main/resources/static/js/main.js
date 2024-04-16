// console.log("main.js loaded.");

// 브라우저 쿠키에 저장돼있는 값 꺼내오기
// 쿠키에서 key가 일치하는 value 얻어오기 함수

// 쿠키는 "K=V; K=V; K=V;..." 형식

// 배열.map(함수) : 배열의 각 요소를 이용해 함수 수행 후
//                 결과 값으로 새로운 배열을 만들어서 반환

const getCookie = (key) => {

    // 자바스크립트에서 cookie 담아주기
    // document.cookie="test" + "=" + "유저일";

    // cookies 아님
    const cookies = document.cookie; // "K=V; K=V"

    // console.log(cookies);

    // cookies 문자열을 배열 형태로 변환
    const cookieList = cookies.split("; ").map( el => el.split("="));
     // 첫번째 split 에서 ["K=V", "K=V"]
     // 두번째 split 에서 ["K", "V"] ...
    // console.log(cookieList);

    // 배열 -> 객체로 변환 (그래야 다루기 쉽다)

    const obj = {}; // 비어있는 객체 선언

    for(let i = 0 ; i < cookieList.length ; i++) {
        const k = cookieList[i][0]; // key 값
        const v = cookieList[i][1]; // value 값
        obj[k] = v; // 객체에 추가
    }

    // console.log(obj);

    return obj[key]; // 매개변수로 전달 받은 key 와
    // obj 객체에 저장된 키가 일치하는 요소의 value 값 반환

}

// getCookie("saveId");

// console.log(getCookie("saveId"));

const loginEmail = document.querySelector("#loginForm input[name='memberEmail']");

// 로그인 안된 상태인 경우에 수행
if(loginEmail != null) { // 로그인 창의 이메일 입력부분이 화면에 있을 때
    // 이 처리를 안해주면 에러남 없는 걸 찾음

    // 쿠키 중 key 값이 "saveId"인 요소의 value 얻어오기
    const saveId = getCookie("saveId"); // undefined 또는 이메일
    // 아이디 저장 체크 안 했을 때 undefined
    // 체크 했을 때는 이메일 넘어옴

    // saveId 값이 있을 경우
    if(saveId != undefined) {
        loginEmail.value = saveId; // 쿠키에서 얻어온 값을 input에 value로 세팅

        // 아이디 저장 체크박스에 체크 해두기
        document.querySelector("input[name='saveId']").checked = true;
    }

};

// 이메일, 비밀번호 미작성 시 로그인 막기
// loginEmail 은 위에서 얻어놓음
const loginForm = document.querySelector("#loginForm");
const loginPw = document.querySelector("#loginForm input[name='memberPw']");

// #loginForm 이 화면에 존재할 때 (== 로그인 상태 아닐 때)
if(loginForm != null) {
    
    // 제출 이벤트 발생 시
    loginForm.addEventListener("submit", e => {

        // 이메일 미작성
        if(loginEmail.value.trim().length === 0) {
            alert("이메일을 작성해주세요");
            e.preventDefault(); // 기본 이벤트(제출) 막기
            loginEmail.focus(); // 초점 이동
            return;
        }

        // 비밀번호 미작성
        if(loginPw.value.trim().length === 0) {
            alert("비밀번호를 작성해주세요");
            e.preventDefault(); // 기본 이벤트(제출) 막기
            loginPw.focus(); // 초점 이동
            return;
        }
    });
}

/* const testLogin = document.querySelector('#test-login');

testLogin.addEventListener('click', function(e){
   
   fetch('/member/testLogin?memberEmail=' + testLogin.innerText)
   .then(resp => resp.text())
   .then(result => {
      
      if(result == 1){
         // 멤버를 찾은 경우
         window.location.href = '/';
      }
      else{
         alert('존재하지 않는 아이디입니다.');
      }
   })
   .catch(error => console.log((error)))
}); */

// ------------------------------------------------------------

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

// ------------------------------------------------------------

/* 회원 목록 조회(비동기) */
const selectMemberList = document.querySelector("#selectMemberList");

const tbody = document.querySelector("#memberList");

selectMemberList.addEventListener("click", () => {
    
    fetch("/member/selectAll")
    .then(resp => resp.text())
    .then(result => {
        
        const memberList = JSON.parse(result);

        tbody.innerHTML = "";
        // #tbody에 tr/td 요소를 생성해서 내용 추가
        for(let member of memberList) { // 향상된 for문

            // console.log(member);
            // tr 태그 생성
            const tr = document.createElement("tr");

            const arr = ['memberNo', 'memberEmail', 'memberNickname', 'memberDelFl'];

            for(let key of arr) {
                const td = document.createElement("td");

                tr.append(td);

                td.innerText = member[key];

                tr.append(td);
            }
            // tbody의 자식으로 tr(한 행) 추가
            tbody.append(tr);
        }
    })
});

// -----------------------------------------------------------

// 특정 회원 비밀번호 초기화(Ajax)
const resetMemberNo = document.querySelector("#resetMemberNo");
const resetPw = document.querySelector("#resetPw");

resetPw.addEventListener("click", () => {

    if(resetMemberNo.value.length == 0) {
        alert("회원번호를 입력해주세요");
        return;
    } else {
        
        const memberNo = resetMemberNo.value;
    
        // console.log("memberNo : " + memberNo);
    
        fetch("/member/resetPw", {
            method : "PUT",
            headers : {"Content-Type" : "application/json"},
            body : memberNo
        })
        .then(resp => resp.text())
        .then(result => {
            // update 결과값 반환
            if(result > 0) {
                resetMemberNo.value = "";
                alert("비밀번호 초기화 성공");
            } else {
                resetMemberNo.value = "";
                alert("존재하지 않는 회원 번호입니다.");
            }
        })
    }

})

// -------------------------------------------------------

// 특정 회원 탈퇴 복구 (Ajax)
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
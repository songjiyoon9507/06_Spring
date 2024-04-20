console.log("myPage.js 연결 확인")

/* 회원 정보 수정 페이지 */
const updateInfo = document.querySelector("#updateInfo");

// -----------------------------------------------------

/* 닉네임 중복 검사 */

let memberNicknameflag = false;

const nickname = document.querySelector("#memberNickname");

// if(updateInfo != null) 이거 작성 안하면 오류남
// nickname 에 input 이벤트가 있을 때 검사해야함 submit 할 때 검사하는 게 아님 닉네임 입력할 때마다 검사
if(updateInfo != null) {
    nickname.addEventListener("input", () => {

        fetch("/member/checkNickname?memberNickname=" + nickname.value)
        .then(resp => resp.text())
        .then(count => {
            if(count == 1) { // 중복되면
                alert("닉네임이 중복되었습니다.");
                nickname.focus();
                nickname.value="";
                memberNicknameflag = true;
            } else {
                memberNicknameflag = false;
            }
        });
    });
};

// -------------------------------------------------------

/* 다음 주소 API 활용 */
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById('address').value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById('detailAddress').focus();
        }
    }).open();
}

if(updateInfo != null) {
    document.querySelector("#searchAddress").addEventListener("click", execDaumPostcode);
}

// #updateInfo 요소가 존재할 때만 수행
// 아래 구문 안 써주면 js 에러남
if(updateInfo != null) {

    // form 태그 제출 시
    updateInfo.addEventListener("submit", e => {

        const memberNickname = document.querySelector("#memberNickname");
        const memberTel =document.querySelector("#memberTel");
        // querySelectorAll 배열 형태로 들어옴
        const memberAddress = document.querySelectorAll("[name='memberAddress']");

        // 닉네임 유효성 검사
        if(memberNickname.value.trim().length === 0) {
            alert("닉네임을 입력해주세요");
            e.preventDefault(); // 제출 막기
            return;
        }

        // 닉네임 정규식
        let regExp = /^[가-힣\w\d]{2,10}$/;
        
        if(!regExp.test(memberNickname.value)) { // 정규식 검사 유효하지 않을 때
            alert("닉네임이 유효하지 않습니다.");
            e.preventDefault(); // 제출 막기
            return;
        }

        // 닉네임 중복시
        if(memberNicknameflag == true) {
            e.preventDefault();
        }

        // 전화번호 유효성 검사
        if(memberTel.value.trim().length === 0) {
            alert("전화번호를 입력해주세요");
            e.preventDefault();
            return;
        }        

        // 전화번호 정규식
        regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;

        if(!regExp.test(memberTel.value)) { // 정규식 검사 유효하지 않을 때
            alert("유효하지 않은 전화번호 형식입니다.");
            e.preventDefault();
            return;
        }

        // 주소 유효성 검사
        const addr0 = memberAddress[0].value.trim().length == 0;
        // addr0 에 true 나 false 값이 들어올 거임
        const addr1 = memberAddress[1].value.trim().length == 0;
        const addr2 = memberAddress[2].value.trim().length == 0;

        // 모두 true 인 경우만 true 저장
        const result1 = addr0 && addr1 && addr2; // 아무것도 입력 안한 경우

        // 모두 false 인 경우만 true 저장
        const result2 = !(addr0 || addr1 || addr2); // 모두 다 입력한 경우

        // 모두 입력 또는 모두 미입력이 아니면
        if(!(result1 || result2)) {
            alert("주소를 모두 작성 또는 미작성 해주세요");
            e.preventDefault();
            return;
        }
    });
}
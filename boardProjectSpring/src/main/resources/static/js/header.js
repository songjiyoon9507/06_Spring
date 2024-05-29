console.log("header.js 연결 확인");

// 날씨 API 요청변수 (필수 항목)에 오늘 날짜와 시간 보내줘야함
// 필수 파라미터 오늘 날짜 return 하는 함수 작성
// 샘플데이터에 날짜는 20210628 이렇게 보내라고 써있음
function getCurrentDate() {
    const today = new Date();
    const year = today.getFullYear();
    // 뒤에서 두자리만 얻어와서 05 만들어줌
    const month = ('0' + (today.getMonth() + 1)).slice(-2);
    const day = ('0' + today.getDate()).slice(-2);
    return `${year}${month}${day}`;
}

/********** async & await **********/

// 비동기 처리 패턴
// 비동기를 마치 동기처럼 실행 순서를 지켜서 사용하는 방법

// async : 비동기가 수행되는 함수 정의 부분 앞에 붙여 사용하는 키워드
// => 이 함수에서 비동기 요청을 수행할 것이다.

// await : promise 객체를 리턴하는 비동기 요청 앞에 붙여 사용하는 키워드
// (promise 객체 == response)
// => 응답이 올 때까지 기다리겠다.
// async 를 붙이면 await 가 수행될 때까지 함수 수행 안됨

// 공공데이터 serviceKey 가져올 함수
// serviceKey 는 config.properties 에 넣어둠
async function getServiceKey() {
    try {

        // 가지고 온 serviceKey 그대로 돌려줌
        const response = await fetch("/getServiceKey");
        return response.text();
    } catch(err) {
        console.log("getServiceKey의 에러 : " + err);
    }
}

// 공공데이터 날씨 API 정보를 얻어올 함수
async function fetchData() {

    // 날짜 구해오는 함수 호출
    const currentDate = getCurrentDate();

    // getServiceKey 함수에 fetch 요청이 있음
    // 서비스 키를 가져온 후 수행돼야함.
    const serviceKey = await getServiceKey();

    // 단기 예보 조회 요청 주소
    // 선택하는 거에 따라서 요청 주소 url 이 달라짐
    const url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";

    // URLSearchParams : URL 의 쿼리 문자열을 쉽게 다룰 수 있게 해주는 내장 객체
    // 단, 사용 시 decode 서비스 키 사용해야함
    // -> URLSearchParams() 함수가 데이터를 인코딩하기 때문에
    // base_time 은 넣을 수 있는 시간이 정해져있음 word 파일 확인
    const queryParams = new URLSearchParams({
        serviceKey : serviceKey,
        pageNo : 1,
        numOfRows : 10,
        dataType : 'JSON',
        base_date : currentDate,
        base_time : '1100',
        nx : 58,
        ny : 125
    });
    // 좌표는 엑셀 파일에서 긁어오면 됨

    console.log(`${url}?${queryParams}`);

    // fetch 요청
    try {

        const response = await fetch(`${url}?${queryParams}`);
        // JSON 형태로 파싱
        const result = await response.json();

        console.log(result);

		const obj = result.response.body.items.item.reduce((acc, data) => {
            acc[data.category] = data.fcstValue;
            return acc;
        }, {});
   
        // console.log(obj);
        const sky = {
            "1" : "맑음",
            "3" : "구름많음",
            "4" : "흐림"
        }
        
        // 화면에 뿌리기..
        const weatherInfo = document.querySelector(".weather-info");
       
        // i 요소 생성 (아이콘)
        const icon = document.createElement('i');
       
        // span 요소 생성 (날씨 정보)
        const span = document.createElement('span');
      
        // 첫 번째 p 요소 생성 (기온 정보)
        const p1 = document.createElement('p');
        // 두 번째 p 요소 생성 (비 올 확률 정보)
        const p2 = document.createElement('p');
        // 강수형태(PTY) 코드 : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
        // 강수 형태에 따라 icon 지정
        if( obj.PTY == 0 ) { // 강수 없음
            switch(obj.SKY) { // SKY 상태에 따라 아이콘 지정
                case "1" : 
                    icon.className = 'fa-solid fa-sun';
                    icon.style.color = '#fcba03';
                    break;
   
                case "3" :
                    icon.className = 'fa-solid fa-cloud';
                    icon.style.color = 'gray';
                    break;
               
                case "4" :
                    icon.className = 'fa-solid fa-cloud-sun';
                    icon.style.color = 'gray';
                    break;
   
            }
        } else if(obj.PTY == 3) { // 눈 올 때
            icon.className = 'fa-solid fa-snowflake';
            icon.style.color = 'skyblue';
        } else { // 그외 비올때
            icon.className = 'fa-solid fa-cloud-rain';
            icon.style.icon = 'gray';
        }
        // 하늘 정보
        span.innerText = sky[obj.SKY];
        // 기온
        p1.innerText = `기온 : ${obj.TMP}℃`;
       
        // 강수확률
        p2.innerText = `강수 확률 : ${obj.POP}%`;
        // div에 자식 요소들 추가
        weatherInfo.appendChild(icon);
        weatherInfo.appendChild(span);
        weatherInfo.appendChild(p1);
        weatherInfo.appendChild(p2);
      
    } catch(err) {
        console.log(err);
    }
}

// API 함수 호출
fetchData();
console.log("chatbot.js 연결 확인");

// textarea에 적은 내용 가져오기
const inputChatting = document.getElementById("inputChatting");

// 보내기 버튼 가져오기
const sendBtn = document.getElementById("send");

// 보내기 버튼 클릭 시
sendBtn.addEventListener("click", e => {
    
    // textArea에 아무것도 입력 안 했을 경우
    if(inputChatting.value.trim().length == 0) {
        alert("채팅을 입력해주세요.");
        inputChatting.value = "";
        return;
    }

    const requestBody = {
        version: "v2",
        userId: "48650",
        timestamp: Date.now(),
        bubbles: [
            {
                type: "text",
                data: {
                    description: inputChatting.value
                }
            }
        ],
        event: "send"
        // "version": "v2",
        // "userId": "user1234",
        // "userIp": "8.8.8.8",
        // "timestamp": Date.now(),
        // "bubbles": [ {
        // "type": "text",
        // "data" : { "description" : inputChatting.value } } ],
        // "event": "send"
    };

    console.log("Sending request body:", JSON.stringify(requestBody)); // 보낼 데이터를 확인

    // 입력된 값이 있을 때 비동기 요청
    fetch("/chatbot/question", {
        method : "POST",
        headers : {"Content-Type" : "application/json"},
        body : JSON.stringify(requestBody)
    })
    .then(resp => resp.json())
    .then(result => {
        const description = result.bubbles[0].data.description;
        console.log("챗봇의 답변 : ", description);
        inputChatting.value = "";
    })

})
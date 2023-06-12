const $record = $('#record');               // 녹음 버튼
const $recordLoading = $('#recordLoading'); // 녹음 로딩
const $evaluationLoading = $('#evaluationLoading'); // 체점 로딩
const $problemSubmit = $('#problemSubmit'); // 문제 제출 버튼
const $playActualAudio = $('#playActualAudio'); // 실제 사운드 재생 버튼
const $actualAudioEl = $('#actualAudioEl'); // 실제 사운드
const $userAudioWithVisualizer = $('#userAudioWithVisualizer');
const $playUserAudio = $userAudioWithVisualizer.find('button');
const $userAudioEl = $userAudioWithVisualizer.find('audio');
const userAudioVisualizer = document.getElementById('userAudioVisualizer');

let isRecording = false; //녹음상태
let mediaRecorder = null;
const audioArray = [];   // 녹음 데이터(Blob) 조각 저장 배열

$(document).ready(function () {

    // 녹음 시작/종료 버튼 클릭 이벤트 처리
    $record.click (function () {

        // 녹음 중이 아닌 경우에만 녹음 시작 처리
        if (!isRecording) {
            startRecord();
            isRecording = true;                        // 녹음 중 상태로 변경
            $record.find('span').text('Mic_off');      // 마이크 꺼짐 아이콘으로 변경
            $recordLoading.show();                     // 녹음 로딩 GUI 활성화
            $problemSubmit.hide();                     // 문제 제출 버튼 비활성화
            $userAudioWithVisualizer.hide();
        }

        // 녹음 중인 경우 녹음 종료
        else {
            stopRecord();
            isRecording = false;                      // 녹음 중지 상태로 변경
            $record.find('span').text('Mic');         // 마이크 켜짐 아이콘으로 변경
            $recordLoading.hide();                    // 녹음 로딩 GUI 비활성화
            $problemSubmit.show();                    // 문제 제출 버튼 활성화
            $userAudioWithVisualizer.show();
        }
    });

    // 실제 사운드 재생 버튼 클릭 이벤트 처리
    $playActualAudio.click(function () {

        if(actualAudioElError) {
            alert("해당 오디오 파일이 등록되지 않았습니다. 관리자에게 문의해 주세요.");
        }
        else {
            let audio = $actualAudioEl[0];
            if(audio.paused) {
                $playActualAudio.find('span').text('stop_circle');
                audio.play();
            }

            else {
                $playActualAudio.find('span').text('play_circle');
                audio.pause();
            }
        }
    });

    $actualAudioEl.on('ended', function() {
        $playActualAudio.find('span').text('play_circle');
    });

    //유저 녹음 기록 듣기버튼 클릭 이벤트
    $playUserAudio.click(function () {
        let audio = $userAudioEl[0];
        if(audio.paused) {
            $userAudioWithVisualizer.find('span').text('stop_circle');
            audio.play();
        }

        else {
            $userAudioWithVisualizer.find('span').text('play_circle');
            audio.pause();
        }
    });

    $userAudioEl.on('ended', function() {
        $playUserAudio.find('span').text('play_circle');
    });

});

let actualAudioElError = false;

$actualAudioEl.on('error', function () {
    console.log("dasdasdasdasd");
    actualAudioElError = true;
});

//녹음 시작을 위한 처리 작업
async function startRecord() {

    // 마이크 mediaStream 생성: Promise를 반환하므로 async/await 사용
    const mediaStream = await navigator.mediaDevices.getUserMedia({audio: true});

    // 기존 오디오 데이터들은 모두 비워 초기화한다.
    audioArray.splice(0);

    // MediaRecorder 생성: 마이크 MediaStream을 인자로 입력
    mediaRecorder = new MediaRecorder(mediaStream);

    // 데이터 사용 가능 이벤트 핸들러
    mediaRecorder.addEventListener('dataavailable', function(event) {
        audioArray.push(event.data);
        drawWaveform(event.data)
    });


    // 이벤트핸들러: 녹음 종료 처리
    mediaRecorder.onstop = (event) => {

        // 녹음이 종료되면, 배열에 담긴 오디오 데이터(Blob)들을 합친다: 코덱도 설정해준다.
        const blob = new Blob(audioArray, {"type": "audio/ogg codecs=opus"});

        // Blob 데이터에 접근할 수 있는 객체URL을 생성한다.
        const blobURL = window.URL.createObjectURL(blob);

        // audio엘리먼트로 재생한다.
        $userAudioEl.attr('src', blobURL);
    }

    // 녹음 시작
    mediaRecorder.start();
}

function stopRecord(mediaStream) {
    mediaRecorder.stop();                     // 녹음 종료
}

// 문제 제출
function uploadUserAudio(problemId) {
    $problemSubmit.hide();
    $evaluationLoading.show();

    const formData = new FormData();
    const blob = new Blob(audioArray, { type : 'audio/ogg codecs=opus' });
    formData.append('audio', blob);

    $.ajax({
        type: 'post',
        url : '/content/problem/' + problemId,
        data: formData,

        timeout: 60000,
        processData: false, // 데이터 처리 방식 (파일 전송 시 false로 설정)
        contentType: false, // 컨텐츠 타입 (파일 전송 시 false로 설정)

        success: function(response) {
            console.log(response);
            $evaluationLoading.hide();

        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 전송 중 에러가 발생했을 때의 콜백 함수
            console.log(textStatus, errorThrown);
            alert(jqXHR.responseText);
            $evaluationLoading.hide();
            $problemSubmit.show();
        }
    });
}

const audioContext = new AudioContext();
const reader = new FileReader();
reader.onload = () => {
    const arrayBuffer = reader.result;
    audioContext.decodeAudioData(arrayBuffer, buffer => {
        const data = buffer.getChannelData(0);
        canvasContext.clearRect(0, 0, canvasWidth, canvasHeight);
        canvasContext.beginPath();
        const sliceWidth = canvasWidth * 1.0 / data.length;
        let x = 0;
        for (let i = 0; i < data.length; i++) {
            const v = data[i] * 0.5 + 0.5;
            const y = v * canvasHeight;
            if (i === 0) {
                canvasContext.moveTo(x, y);
            } else {
                canvasContext.lineTo(x, y);
            }
            x += sliceWidth;
        }
        canvasContext.moveTo(0, canvasHeight / 2);
        canvasContext.lineTo(canvasWidth, canvasHeight / 2);
        canvasContext.stroke();
    });
};

function drawWaveform(audioData) {
    reader.readAsArrayBuffer(audioData);
}

let canvasContext;
let canvasWidth;
let canvasHeight;

function setupVisualizer() {
    canvasContext = userAudioVisualizer.getContext('2d');
    canvasWidth = userAudioVisualizer.width;
    canvasHeight = userAudioVisualizer.height;
    canvasContext.clearRect(0, 0, canvasWidth, canvasHeight);
    canvasContext.lineWidth = 0.5;
    canvasContext.strokeStyle = 'rgb(255, 255, 255)';
    canvasContext.beginPath();
    canvasContext.moveTo(0, canvasHeight / 2);
    canvasContext.lineTo(canvasWidth, canvasHeight / 2);
    canvasContext.stroke();
}

setupVisualizer();














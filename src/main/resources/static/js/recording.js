// audio, button 태그 취득
const $audioEl = document.querySelector('#record_audio');
const $standard_AudioEl = document.querySelector('#audio-player');
const $standard_btn = document.querySelector("#standard_play_btn");
const $btn = document.querySelector("#record_control_btn");
const $my_play_btn = document.querySelector("#my_play_btn");
const loading_div = document.getElementById('loading_div');
const audio_and_visual_div = document.getElementById('audio_and_visual_div');
const visualizer = document.getElementById('visualizer');
const uploadButton = document.getElementById('uploadButton');

// 녹음 상태 체크용 변수
let isRecording = false;
let my_sound_playing = false;
let standard_sound_playing = false;

let mediaRecorder = null;
let audioContext;
let canvasContext;
let canvasWidth;
let canvasHeight;

uploadButton.addEventListener('click', upload);

// 녹음 데이터(Blob) 조각 저장 배열
const audioArray = [];

// 녹음 시작/종료 버튼 클릭 이벤트 처리
$btn.onclick = async function (event) {

    // 녹음 중이 아닌 경우에만 녹음 시작 처리
    if(!isRecording){
        audioArray.splice(0); // 기존 오디오 데이터들은 모두 비워 초기화한다.

        // 마이크 mediaStream 생성: Promise를 반환하므로 async/await 사용
        const mediaStream = await navigator.mediaDevices.getUserMedia({audio: true});

        // MediaRecorder 생성: 마이크 MediaStream을 인자로 입력
        mediaRecorder = new MediaRecorder(mediaStream);

        // 이벤트핸들러: 녹음 데이터 취득 처리
        mediaRecorder.ondataavailable = (event)=>{
            audioArray.push(event.data); // 오디오 데이터가 취득될 때마다 배열에 담아둔다.
            drawWaveform(event.data);
        }

        // 이벤트핸들러: 녹음 종료 처리 & 재생하기
        mediaRecorder.onstop = (event)=>{

            // 녹음이 종료되면, 배열에 담긴 오디오 데이터(Blob)들을 합친다: 코덱도 설정해준다.
            const blob = new Blob(audioArray, {"type": "audio/ogg codecs=opus"});

            // Blob 데이터에 접근할 수 있는 객체URL을 생성한다.
            const blobURL = window.URL.createObjectURL(blob);

            // audio엘리먼트로 재생한다.
            $audioEl.src = blobURL;

        }

        // 녹음 시작
        mediaRecorder.start();
        isRecording = true;
        loading_div.style.display = 'block';
        audio_and_visual_div.style.display = 'none';
        document.getElementById('record_btn_img').src = "/image/mic_off.jpg";
    }else{
        // 녹음 종료
        mediaRecorder.stop();
        isRecording = false;
        loading_div.style.display = 'none';
        audio_and_visual_div.style.display = 'block';
        document.getElementById('record_btn_img').src = "/image/mic_on.jpg";
    }
}

function drawWaveform(audioData) {
    audioContext = new AudioContext();
    const reader = new FileReader();
    reader.onload = () => {
        const arrayBuffer = reader.result;
        audioContext.decodeAudioData(arrayBuffer, buffer => {
            const data = buffer.getChannelData(0);
            canvasContext.fillStyle = 'rgb(255, 255, 255)';
            canvasContext.fillRect(0, 0, canvasWidth, canvasHeight);
            canvasContext.lineWidth = 2;
            canvasContext.strokeStyle = 'rgb(0, 0, 0)';
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
            canvasContext.lineTo(canvasWidth, canvasHeight / 2);
            canvasContext.stroke();
        });
    };
    reader.readAsArrayBuffer(audioData);
}

$standard_btn.onclick = async function (event) {
    if (standard_sound_playing == false){
        standard_sound_playing = true;
        $standard_AudioEl.play();
        document.getElementById('standard_play_btn_img').src = "/image/playing.jpeg";

    }

    else {
        standard_sound_playing = false;
        $standard_AudioEl.pause();
        document.getElementById('standard_play_btn_img').src = "/image/play.jpeg";
    }
}

document.getElementById('audio-player')
    .addEventListener('ended', function () {
        standard_sound_playing = false;

        document.getElementById('standard_play_btn_img').src = "/image/play.jpeg";
    });

$my_play_btn.onclick = async function (event) {
    if (my_sound_playing == false){
        my_sound_playing = true;
        $audioEl.play();
        document.getElementById('my_play_btn_img').src = "/image/playing.jpeg";

    }

    else {
        my_sound_playing = false;
        $audioEl.pause();
        document.getElementById('my_play_btn_img').src = "/image/play.jpeg";
    }
}

document.getElementById('record_audio')
    .addEventListener('ended', function () {
        my_sound_playing = false;

        document.getElementById('my_play_btn_img').src = "/image/play.jpeg";
    });


function upload() {
    const formData = new FormData();
    const blob = new Blob(audioArray, { type : 'audio/ogg codecs=opus' });
    formData.append('audio', blob);

    $.ajax({
        type: 'post',
        url : '/content/problem/1',
        data: formData,

        timeout: 60000,
        processData: false, // 데이터 처리 방식 (파일 전송 시 false로 설정)
        contentType: false, // 컨텐츠 타입 (파일 전송 시 false로 설정)
        success: function(response) {
            // 성공적으로 전송되었을 때의 콜백 함수
            console.log(response);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // 전송 중 에러가 발생했을 때의 콜백 함수
            console.log(textStatus, errorThrown);
        }
    });

}

function setupVisualizer() {
    canvasContext = visualizer.getContext('2d');
    canvasWidth = visualizer.width;
    canvasHeight = visualizer.height;
    canvasContext.fillStyle = 'rgb(255, 255, 255)';
    canvasContext.fillRect(0, 0, canvasWidth, canvasHeight);
    canvasContext.lineWidth = 2;
    canvasContext.strokeStyle = 'rgb(0, 0, 0)';
    canvasContext.beginPath();
    canvasContext.moveTo(0, canvasHeight / 2);
    canvasContext.lineTo(canvasWidth, canvasHeight / 2);
    canvasContext.stroke();
}

setupVisualizer();


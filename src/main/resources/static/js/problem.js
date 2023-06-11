const $record = $('#record');               // 녹음 버튼
const $recordLoading = $('#recordLoading'); // 녹음 로딩
const $problemSubmit = $('#problemSubmit'); // 문제 제출 버튼
const $playActualAudio = $('#playActualAudio'); // 실제 사운드 재생 버튼
const $actualAudioEl = $('#actualAudioEl'); // 실제 사운드
const $userAudioWithVisualizer = $('#userAudioWithVisualizer');
const $playUserAudio = $userAudioWithVisualizer.find('button')
const $userAudioEl = $userAudioWithVisualizer.find('audio')
const $test = $('#visualizer');

let isRecording = false; //녹음상태
let mediaStream = null;
let mediaRecorder = null;
const audioArray = [];   // 녹음 데이터(Blob) 조각 저장 배열

$(document).ready(function () {

    // 녹음 시작/종료 버튼 클릭 이벤트 처리
    $record .click (function () {

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
        let audio = $actualAudioEl[0];
        if(audio.paused) {
            $playActualAudio.find('span').text('stop_circle');
            audio.play();
        }

        else {
            $playActualAudio.find('span').text('play_circle');
            audio.pause();
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


//녹음 시작을 위한 처리 작업
async function startRecord() {

    // 마이크 mediaStream 생성: Promise를 반환하므로 async/await 사용
    mediaStream = await navigator.mediaDevices.getUserMedia({audio: true});

    // 기존 오디오 데이터들은 모두 비워 초기화한다.
    audioArray.splice(0);

    // MediaRecorder 생성: 마이크 MediaStream을 인자로 입력
    mediaRecorder = new MediaRecorder(mediaStream);

    // 데이터 사용 가능 이벤트 핸들러
    mediaRecorder.addEventListener('dataavailable', function(event) {
        audioArray.push(event.data);

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

function stopRecord() {
    mediaRecorder.stop();                     // 녹음 종료
    mediaStream.getAudioTracks()[0].stop();   // 마이크 액세스 중지 및 트랙 해제
}













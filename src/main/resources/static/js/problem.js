const $record = $('#record');               // 녹음 버튼
const $recordLoading = $('#recordLoading'); // 녹음 로딩
const $problemSubmit = $('#problemSubmit'); // 문제 제출 버튼
const $playActualAudio = $('#playActualAudio'); // 실제 사운드 재생 버튼
const $actualAudioEl = $('#actualAudioEl'); // 실제 사운드

let isRecording = false; //녹음상태

$(document).ready(function () {

    // 녹음 시작/종료 버튼 클릭 이벤트 처리
    $record .click (function () {
        // 녹음 중이 아닌 경우에만 녹음 시작 처리
        if(!isRecording){
            isRecording = true;                        // 녹음 중 상태로 변경
            $record.find('span').text('Mic_off');      // 마이크 꺼짐 아이콘으로 변경
            $recordLoading.show();                     // 녹음 로딩 GUI 활성화
            $problemSubmit.hide();                     // 문제 제출 버튼 비활성화
        }

        // 녹음 중인 경우 녹음 종료
        else{
            isRecording = false;                      // 녹음 중지 상태로 변경
            $record.find('span').text('Mic');         // 마이크 켜짐 아이콘으로 변경
            $recordLoading.hide();                    // 녹음 로딩 GUI 비활성화
            $problemSubmit.show();                    // 문제 제출 버튼 활성화
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
});

let mediaRecorder;
let chunks = [];
let isRecording = false;
let waveformEl = document.getElementById("waveform");
let canvasWidth = waveformEl.offsetWidth;
let canvasHeight = waveformEl.offsetHeight;
let lineSpacing = canvasHeight / 2;
let lineWidth = 1;
let lineCount = canvasWidth / lineWidth;
let centerLineY = canvasHeight / 2;

function toggleRecording() {
    if (isRecording) {
        stopRecording();
        document.getElementById("record-btn").textContent = "녹음 시작";
    } else {
        startRecording();
        document.getElementById("record-btn").textContent = "녹음 중지";
    }

    isRecording = !isRecording;
}

function startRecording() {
    navigator.mediaDevices.getUserMedia({ audio: true })
        .then(function(stream) {
            mediaRecorder = new MediaRecorder(stream);
            mediaRecorder.start();
            console.log("녹음 시작");
        }).catch(function(error) {
        console.log("녹음 시작 실패: " + error);
    });

    mediaRecorder.addEventListener("dataavailable", function(event) {
        chunks.push(event.data);
        drawWaveform(event.data);
    });
}

function stopRecording() {
    mediaRecorder.stop();
    console.log("녹음 중지");
}

function drawWaveform(data) {
    let audioCtx = new AudioContext();
    audioCtx.decodeAudioData(data, function(buffer) {
        let channelData = buffer.getChannelData(0);
        let canvas = document.createElement("canvas");
        let canvasCtx = canvas.getContext("2d");

        canvas.width = canvasWidth;
        canvas.height = canvasHeight;
        canvasCtx.fillStyle = "#f0f0f0";
        canvasCtx.fillRect(0, 0, canvasWidth, canvasHeight);
        canvasCtx.strokeStyle = "#000";
        canvasCtx.lineWidth = lineWidth;

        for (let i = 0; i < lineCount; i++) {
            let x = i * lineWidth;
            let y = channelData[Math.floor(i * channelData.length / lineCount)] * centerLineY + centerLineY;
            canvasCtx.beginPath();
            canvasCtx.moveTo(x, centerLineY);
            canvasCtx.lineTo(x, y);
            canvasCtx.stroke();
        }

        waveformEl.appendChild(canvas);
    });
}

function playAudio(blob) {
    let audioPlayer = document.getElementById("audio-player");
    let url = URL.createObjectURL(blob);
    audioPlayer.src = url;
    audioPlayer.play();
}
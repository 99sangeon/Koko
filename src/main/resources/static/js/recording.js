let audioChunks = [];
let mediaRecorder;
let audioContext;
let audioBuffer;
let source;
let canvasContext;
let canvasWidth;
let canvasHeight;

const recordButton = document.getElementById('recordButton');
const stopButton = document.getElementById('stopButton');
const playButton = document.getElementById('playButton');
const visualizer = document.getElementById('visualizer');
const reRecordButton = document.getElementById('reRecordButton');
const uploadButton = document.getElementById('uploadButton');

recordButton.addEventListener('click', startRecording);
stopButton.addEventListener('click', stopRecording);
playButton.addEventListener('click', playRecording);
reRecordButton.addEventListener('click', reRecord);
uploadButton.addEventListener('click', upload);

function startRecording() {

    audioChunks = [];
    canvasContext.clearRect(0, 0, canvasWidth, canvasHeight);
    recordButton.disabled = true;
    stopButton.disabled = false;
    playButton.disabled = true;
    reRecordButton.disabled = true;
    uploadButton.disabled = true;
    recordButton.innerText ="녹음중";

    navigator.mediaDevices.getUserMedia({ audio: true })
        .then(stream => {
            mediaRecorder = new MediaRecorder(stream);
            mediaRecorder.addEventListener('dataavailable', event => {
                audioChunks.push(event.data);
                drawWaveform(event.data);

            });
            mediaRecorder.start();
        })
        .catch(error => console.log(error));
}

function stopRecording() {
    mediaRecorder.stop();
    recordButton.innerText ="녹음시작";
    recordButton.disabled = false;
    stopButton.disabled = true;
    playButton.disabled = false;
    reRecordButton.disabled = false;
    uploadButton.disabled = false;
}

function playRecording() {
    audioContext = new AudioContext();
    const blob = new Blob(audioChunks, { type: 'audio/ogg; codecs=opus' });
    const audioURL = URL.createObjectURL(blob);
    const request = new XMLHttpRequest();
    request.open('GET', audioURL, true);
    request.responseType = 'arraybuffer';
    request.onload = () => {
        audioContext.decodeAudioData(request.response, buffer => {
            audioBuffer = buffer;
            source = audioContext.createBufferSource();
            source.buffer = audioBuffer;
            source.connect(audioContext.destination);
            source.start();
            drawWaveform(audioBuffer);
        });
    };
    request.send();
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

function upload() {
    const formData = new FormData();
    const blob = new Blob(audioChunks, { type: 'audio/ogg; codecs=opus' });
    formData.append('audio', blob, 'recording.ogg');
    fetch('/upload', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                console.log('Upload successful');
            } else {
                console.log('Upload failed');
            }
        })
        .catch(error => console.log(error));
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
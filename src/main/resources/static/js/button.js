const scrollButton = document.getElementById("scrollButton");

scrollButton.addEventListener("click", function() {
    var scrollStep = window.innerHeight / 100; // 한 번에 이동할 스크롤 거리를 계산 (전체 거리를 20 등분)
    var scrollInterval = setInterval(function() {
        if (window.scrollY >= window.innerHeight) {
            clearInterval(scrollInterval); // 목표 위치에 도달하면 애니메이션 종료
        }
        window.scrollBy(0, scrollStep); // 스크롤을 서서히 이동
    }, 1); // 각 단계별로 15밀리초 간격으로 애니메이션 실행
});
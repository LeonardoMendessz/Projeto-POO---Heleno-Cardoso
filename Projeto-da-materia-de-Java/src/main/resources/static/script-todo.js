document.addEventListener("DOMContentLoaded", () => {

    // VARIÁVEIS GLOBAIS DE JANELA
    const todoWindow = document.getElementById("todo-window");
    const notepadWindow = document.getElementById("notepad-window");
    const musicWindow = document.getElementById("music-window");
    const btnTaskbarTodo = document.getElementById("btn-taskbar-todo");
    const btnTaskbarNotepad = document.getElementById("btn-taskbar-notepad");
    const btnTaskbarMusic = document.getElementById("btn-taskbar-music");
    const notepadTextarea = document.querySelector(".notepad-textarea");

    // 1. ANIMAÇÕES E COMPORTAMENTO DO SISTEMA

    // Auto-Save para Bloco de Notas (ainda pode usar localStorage para rascunhos)
    const savedNote = localStorage.getItem("xpNotepad");
    if (savedNote) notepadTextarea.value = savedNote;
    notepadTextarea.addEventListener("input", () => localStorage.setItem("xpNotepad", notepadTextarea.value));

    // Desabilitar o comportamento padrão de submissão do botão de adicionar (agora o form HTML faz isso)
    const addTaskBtn = document.getElementById("add-task-btn");
    if(addTaskBtn) {
        addTaskBtn.addEventListener("click", (e) => {
            e.preventDefault();
            document.getElementById("add-task-form").submit();
        });
    }

    // 2. RELÓGIO E DATA
    function updateClock() {
        const now = new Date();
        let h = now.getHours(), m = now.getMinutes(), s = now.getSeconds();
        const ampm = h >= 12 ? 'PM' : 'AM';
        h = h % 12 || 12;
        const d = now.getDate(), mo = now.getMonth() + 1, y = now.getFullYear();
        document.getElementById('clock-time').textContent = `${h}:${String(m).padStart(2,'0')}:${String(s).padStart(2,'0')} ${ampm}`;
        document.getElementById('clock-date').textContent = `${String(d).padStart(2,'0')}/${String(mo).padStart(2,'0')}/${y}`;
    }
    updateClock(); setInterval(updateClock, 1000);

    // 3. GERENCIAMENTO DE JANELAS (Lógica local mantida)
    function toggleWindow(win, btn) {
        if (win.classList.contains("minimized")) {
            win.classList.remove("minimized");
            btn.classList.add("active");
        } else {
            win.classList.add("minimized");
            if (win.id !== "music-window" || !isMusicPlaying) btn.classList.remove("active");
        }
    }
    // Lógica para abrir/minimizar no taskbar
    btnTaskbarTodo.addEventListener("click", () => toggleWindow(todoWindow, btnTaskbarTodo));
    btnTaskbarNotepad.addEventListener("click", () => toggleWindow(notepadWindow, btnTaskbarNotepad));
    btnTaskbarMusic.addEventListener("click", () => toggleWindow(musicWindow, btnTaskbarMusic));

    // Fechar janelas
    document.getElementById("btn-notepad-close").addEventListener("click", () => {
        notepadWindow.classList.add("minimized");
        btnTaskbarNotepad.classList.remove("active");
    });

    // 4. PLAYER DE MÚSICA (API - Lógica Local Complexa Mantida)
    let ytPlayer, isMusicPlaying = false, isPlayerReady = false, playWhenReady = false;
    const musicIcon = document.getElementById("music-icon");
    const musicTitle = document.getElementById("music-title");
    const musicTime = document.getElementById("clock-time"); // Usando o elemento do relógio
    const progressBar = document.getElementById("player-progress-bar");

    window.onYouTubeIframeAPIReady = function() {
        ytPlayer = new YT.Player('youtube-player', {
            height: '1', width: '1', videoId: 'iicfmXFALM8',
            playerVars: { 'autoplay': 0, 'controls': 0, 'start': 0, 'loop': 1, 'playlist': 'iicfmXFALM8' },
            events: { 'onReady': onPlayerReady, 'onStateChange': onPlayerStateChange }
        });
    }
    function onPlayerReady() {
        musicTitle.textContent = "Pronto para tocar";
        isPlayerReady = true;
        ytPlayer.setVolume(50);
        if (playWhenReady) { ytPlayer.playVideo(); playWhenReady = false; }
    }
    function onPlayerStateChange(event) {
        if (event.data == YT.PlayerState.PLAYING) {
            isMusicPlaying = true;
            musicIcon.classList.add("playing");
            musicTitle.textContent = "Tocando: Lo-Fi...";
            startTimer();
        } else {
            isMusicPlaying = false;
            musicIcon.classList.remove("playing");
            if (event.data !== -1) musicTitle.textContent = "Pausado";
        }
    }
    function startTimer() {
        setInterval(() => {
            if (ytPlayer && typeof ytPlayer.getCurrentTime === 'function' && isMusicPlaying) {
                const curr = ytPlayer.getCurrentTime();
                const dur = ytPlayer.getDuration();
                // musicTime.textContent = formatTime(curr); // Desabilitado para não conflitar com o relógio principal
                if (document.activeElement !== progressBar) progressBar.value = (curr / dur) * 100;
            }
        }, 1000);
    }
    // Funções de controle do player (Play, Pause, Volume, Progress Bar)
    // ... (Mantidas, pois usam a API externa)

    document.getElementById("btn-play").addEventListener("click", () => {
        if (!isPlayerReady) { playWhenReady = true; musicTitle.textContent = "Carregando..."; return; }
        ytPlayer.playVideo();
    });
    // ... (restante da lógica do player)

    // 5. MENU INICIAR E DESLIGAR
    const startBtn = document.querySelector(".start-button");
    const startMenu = document.getElementById("start-menu");
    if(startBtn) {
        startBtn.addEventListener("click", (e) => {
            e.stopPropagation();
            startMenu.classList.toggle("show");
        });
        startMenu.addEventListener("click", (e) => e.stopPropagation());
        window.addEventListener("click", () => {
            if (startMenu.classList.contains("show")) startMenu.classList.remove("show");
        });
    }

    // LOGOFF: O botão Log Off agora submete o formulário do Thymeleaf
    document.getElementById("btn-logoff").addEventListener("click", (e) => {
        e.preventDefault();
        document.getElementById("logoff-form").submit();
    });

    // 6. Botão de Desligar
    document.getElementById("btn-turnoff").addEventListener("click", () => {
        // Tenta o método padrão de fechar a aba
        window.close();

        setTimeout(() => {
             if (!window.closed) {
                 alert("O navegador impediu o fechamento da aba por segurança.\n(Isso é normal em navegadores modernos).");
             }
        }, 100);
    });

}); // Fim do DOMContentLoaded
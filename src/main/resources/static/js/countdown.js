function startCountdown() {
    const timers = document.querySelectorAll('.timer');
    timers.forEach(timer => {
        const changePhaseTime = new Date(timer.getAttribute('data-change-phase-time'));
        const contestId = timer.id;

        function updateCountdown() {
            const now = new Date();
            const timeDifference = changePhaseTime - now;

            if (timeDifference <= 0) {
                timer.innerText = "Phase Ended";
            } else {
                const hours = Math.floor(timeDifference / (1000 * 60 * 60));
                const minutes = Math.floor((timeDifference % (1000 * 60 * 60)) / (1000 * 60));
                const seconds = Math.floor((timeDifference % (1000 * 60)) / 1000);

                timer.innerText = `${hours}h ${minutes}m ${seconds}s`;
            }
        }

        // Update the countdown every second
        setInterval(updateCountdown, 1000);
    });
}

window.onload = startCountdown;

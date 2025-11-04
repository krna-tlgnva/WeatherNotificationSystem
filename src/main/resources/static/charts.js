const state = { charts: { temp: null, wind: null } };
const WEEKDAYS = ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"];
const weekdayByOffset = o => { const d = new Date(); d.setDate(d.getDate()+o); return WEEKDAYS[d.getDay()]; };

async function getJSON(url) {
    const r = await fetch(url);
    if (!r.ok) throw new Error(`HTTP ${r.status}`);
    return await r.json();
}

async function loadCharts(city = "Astana") {
    try {
        const data = await getJSON(`/api/weather/${encodeURIComponent(city)}`);
        renderCharts(data?.weekly || []);

        fetch('/api/observer/city-changed', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId: 1, city })
        }).catch(()=>{});

    } catch (e) {
        console.error(e);
        renderCharts([]);
    }
}

function renderCharts(weekly) {
    if (!window.Chart) return;

    const labels = weekly.map((_, i) => weekdayByOffset(i + 1));
    const temps  = weekly.map(x => Number.isFinite(x?.actuallyTemp) ? x.actuallyTemp : null);
    const wind   = weekly.map(x => Number.isFinite(x?.windSpeed)   ? x.windSpeed   : null);

    state.charts.temp?.destroy?.();
    state.charts.wind?.destroy?.();

    const base = {
        responsive: true,
        maintainAspectRatio: false,
        aspectRatio: 2,
        animation: { duration: 0 },
        scales: {
            x: { ticks: { color: "#bbb" }, grid: { color: "rgba(255,255,255,0.05)" } },
            y: { ticks: { color: "#bbb" }, grid: { color: "rgba(255,255,255,0.05)" } }
        },
        plugins: { legend: { display: true, labels: { color: "#ddd" } } }
    };

    const ct = document.getElementById("chartTemp");
    const cw = document.getElementById("chartWind");

    if (ct) {
        state.charts.temp = new Chart(ct.getContext("2d"), {
            type: "line",
            data: {
                labels,
                datasets: [{
                    label: "Temperature (°C)",
                    data: temps,
                    tension: 0.35,
                    borderWidth: 3,
                    borderColor: "#7a5cff",
                    pointRadius: 3,
                    pointBackgroundColor: "#a88fff",
                    fill: false
                }]
            },
            options: base
        });
    }

    if (cw) {
        state.charts.wind = new Chart(cw.getContext("2d"), {
            type: "bar",
            data: {
                labels,
                datasets: [{
                    label: "Wind (m/s)",
                    data: wind,
                    backgroundColor: "#8f5effcc",
                    borderRadius: 6
                }]
            },
            options: base
        });
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const input = document.getElementById("q");
    input?.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            e.preventDefault();
            const city = input.value.trim() || "Astana";
            loadCharts(city);
        }
    });
    loadCharts("Astana");
});

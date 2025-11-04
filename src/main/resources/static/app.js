const $  = (id) => document.getElementById(id);
const text = (id, v) => { const el = $(id); if (el) el.textContent = v; };
const state = {
    isCelsius: true,
    lastBundle: null,
    city: 'Astana'
};
const CtoF = (c) => Math.round((c * 9) / 5 + 32);
const WEEKDAYS = ['Sun','Mon','Tue','Wed','Thu','Fri','Sat'];
const weekdayByOffset = (offset) => { const d=new Date(); d.setDate(d.getDate()+offset); return WEEKDAYS[d.getDay()]; };

async function getJSON(url, opts = {}, ms = 10000) {
    const ctl = new AbortController();
    const t = setTimeout(() => ctl.abort(), ms);
    try {
        const res = await fetch(url, { ...opts, signal: ctl.signal });
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        return await res.json();
    } finally { clearTimeout(t); }
}

function setIcon(codeOrDesc) {
    const root = $('icon'); if (!root) return;
    root.innerHTML = '';
    if (!codeOrDesc) return;

    const img = document.createElement('img');
    if (/^[0-9]{2}[dn]$/.test(codeOrDesc)) {
        img.src = `https://openweathermap.org/img/wn/${codeOrDesc}@2x.png`;
    } else {
        const desc = (codeOrDesc + '').toLowerCase();
        const emoji =
            desc.includes('cloud') ? '☁️' :
                desc.includes('rain')  ? '🌧️' :
                    desc.includes('snow')  ? '❄️' :
                        desc.includes('storm') ? '⛈️' :
                            desc.includes('wind')  ? '🌬️' :
                                desc.includes('sun')   ? '☀️'  : '🌤️';

        img.src = `data:image/svg+xml;utf8,${encodeURIComponent(
            `<svg xmlns='http://www.w3.org/2000/svg' width='64' height='64'><text x='50%' y='50%' dominant-baseline='middle' text-anchor='middle' font-size='48'>${emoji}</text></svg>`
        )}`;
    }
    img.alt = 'weather icon';
    root.appendChild(img);
}

function renderCurrent(cur) {
    if (!cur) return;
    state.city = cur.city || state.city;

    text('city', cur.city || '—');
    text('date', new Date().toLocaleString('en-GB', {
        weekday:'short', month:'short', day:'numeric', hour:'2-digit', minute:'2-digit'
    }));

    const toNum  = (v) => (v === null || v === undefined ? null : Number.parseFloat(v));
    const toUnit = (v) => Number.isFinite(v = toNum(v)) ? (state.isCelsius ? v : CtoF(v)) : null;
    const fmtTemp = (v) => v==null ? '—' : `${Math.round(v)}°${state.isCelsius?'C':'F'}`;

    text('temp',     fmtTemp(toUnit(cur.actuallyTemp)));
    text('feels',    fmtTemp(toUnit(cur.feelsLike)));
    text('humidity', Number.isFinite(toNum(cur.humidity)) ? `${toNum(cur.humidity)} %` : '—');
    text('pressure', Number.isFinite(toNum(cur.pressure)) ? `${toNum(cur.pressure)} hPa` : '—');
    text('wind',     Number.isFinite(toNum(cur.windSpeed)) ? `${toNum(cur.windSpeed)} m/s` : '—');

    setIcon(cur.icon || cur.description || '');
    const d = $('descr');
    if (d) {
        const desc = (cur.description || '—').toString();
        d.textContent = desc.length > 80 ? desc.slice(0,77) + '…' : desc;
    }
}

function makeCard(label, t, w, className = 'h-card') {
    const div = document.createElement('div');
    div.className = className;

    const toNum  = (v) => (v === null || v === undefined ? null : Number.parseFloat(v));
    const toUnit = (v) => Number.isFinite(v = toNum(v)) ? (state.isCelsius ? v : CtoF(v)) : null;

    div.innerHTML = `
  <div class="h-time">${label}</div>
  <div class="h-temp">${!Number.isFinite(toNum(t)) ? '—' : Math.round(toUnit(t))+'°'+(state.isCelsius?'C':'F')}</div>
  <div class="h-wind">${!Number.isFinite(toNum(w)) ? '—' : toNum(w)+' m/s'}</div>`;
    return div;
}

function renderHourly(list) {
    const root = $('hourly'); if (!root) return;
    root.innerHTML = '';
    if (!Array.isArray(list) || !list.length) {
        root.innerHTML = '<div class="h-card" style="opacity:.7">No hourly data</div>';
        return;
    }
    list.forEach((x,i) => root.appendChild(makeCard('+'+i+'h', x.actuallyTemp, x.windSpeed, 'h-card')));
}

function renderWeekly(list) {
    const root = $('weekly'); if (!root) return;
    root.innerHTML = '';

    let arr = Array.isArray(list) ? list : Object.values(list || {});
    arr = arr
        .filter(x => x && (x.hasOwnProperty('actuallyTemp') || x.hasOwnProperty('windSpeed')))
        .slice(0, 7);

    if (!arr.length) {
        root.innerHTML = '<div class="w-card" style="opacity:.7">No weekly data</div>';
        return;
    }

    arr.forEach((x,i) => {
        const label = weekdayByOffset(i+1);
        root.appendChild(makeCard(label, x.actuallyTemp, x.windSpeed, 'w-card'));
    });
}

async function observerSubscribe(userId = 1) {
    try {
        await fetch('/api/observer/subscribe', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ userId })
        });
    } catch (e) {
        console.error('observerSubscribe failed', e);
    }
}

async function observerCityChanged(city, userId = 1) {
    try {
        await fetch('/api/observer/city-changed', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ userId, city })
        });
    } catch {}
}

let _inboxLastTs = 0;
async function fetchInbox(userId = 1) {
    const res = await fetch('/api/observer/inbox?userId=' + userId);
    if (!res.ok) return [];
    return await res.json();
}

function renderInbox(items) {
    const ul = $('notifList'); if (!ul) return;
    if (!Array.isArray(items) || !items.length) {
        ul.innerHTML = '<li><span class="notif-city">No notifications yet</span></li>';
        return;
    }
    ul.innerHTML = items.map(it => {
        const dt = new Date(it.ts);
        const time = dt.toLocaleString();
        const cur = it.data?.current;
        const temp = (cur && typeof cur.actuallyTemp === 'number') ? `${cur.actuallyTemp}°C` : '—';
        const wind = (cur && typeof cur.windSpeed === 'number') ? `${cur.windSpeed} m/s` : '—';
        return `<li>
      <div class="notif-city">${it.city} · ${temp} · wind ${wind}</div>
      <div class="notif-time">${time}</div>
    </li>`;
    }).join('');
}

async function openInbox() {
    const panel = $('notifPanel'); if (!panel) return;
    const items = await fetchInbox(1);
    renderInbox(items);
    const newest = items?.[0]?.ts ?? 0;
    if (newest > _inboxLastTs) _inboxLastTs = newest;
    panel.hidden = false;
}

function closeInbox() {
    const panel = $('notifPanel'); if (panel) panel.hidden = true;
}

function startInboxPolling() {
    setInterval(async () => {
        try {
            const items = await fetchInbox(1);
            const newest = items?.[0]?.ts ?? 0;
            if (!$('notifPanel')?.hidden) renderInbox(items);
            if (newest > _inboxLastTs) _inboxLastTs = newest;
        } catch {}
    }, 30000);
}

function wireNotifyButton() {
    const btn = $('notifyBtn'); if (!btn) return;
    const close = $('notifClose');

    btn.addEventListener('click', async () => {
        await observerSubscribe(1);
        btn.classList.add('active');
        await openInbox();
    });

    close?.addEventListener('click', () => closeInbox());
    startInboxPolling();
}

async function loadCity(city) {
    try {
        const data = await getJSON(`/api/weather/${encodeURIComponent(city)}`);
        state.lastBundle = data || {};
        renderCurrent(data?.current);
        renderHourly(Array.isArray(data?.hourly) ? data.hourly : []);
        renderWeekly(data?.weekly);
        observerCityChanged(city, 1);

    } catch (e) {
        console.error(e);
        state.lastBundle = null;
        renderCurrent({ city, actuallyTemp:null, feelsLike:null, windSpeed:null, pressure:null, humidity:null, description:'' });
        renderHourly([]); renderWeekly([]);
    }
}
function wireSearch() {
    const input = $('q');
    input?.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            const value = input.value.trim();
            if (value) loadCity(value);
        }
    });
}

function wireUnitToggle() {
    const btn = $('unit'); if (!btn) return;
    btn.addEventListener('click', () => {
        state.isCelsius = !state.isCelsius;
        btn.textContent = state.isCelsius ? '°C' : '°F';
        btn.setAttribute('aria-pressed', String(state.isCelsius));

        if (state.lastBundle?.current) renderCurrent(state.lastBundle.current);
        if (state.lastBundle?.hourly)  renderHourly(state.lastBundle.hourly);
        if (state.lastBundle?.weekly)  renderWeekly(state.lastBundle.weekly);
    });
}

document.addEventListener('DOMContentLoaded', () => {
    wireSearch();
    wireUnitToggle();
    wireNotifyButton();
    const initial = (typeof INITIAL_CITY !== 'undefined' && INITIAL_CITY) ? INITIAL_CITY : 'Astana';
    loadCity(initial);
});

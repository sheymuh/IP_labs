const BASE = "http://localhost:8080/api/1.0/playlist";
export const fetchPlaylists = () =>
    fetch(`${BASE}`)
        .then((r) => r.json())
        .then((data) => data.items || data.content || data); // Извлекаем items
export const fetchPlaylist = (id) => fetch(`${BASE}/${id}`).then((r) => r.json());
export const createPlaylist = (p) =>
    fetch(BASE, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(p),
    }).then((r) => r.json());
export const updatePlaylist = (id, p) =>
    fetch(`${BASE}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(p),
    }).then((r) => r.json());
export const deletePlaylist = (id) => fetch(`${BASE}/${id}`, { method: "DELETE" });
